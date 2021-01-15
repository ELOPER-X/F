package ua;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftItem;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityMetadata;

public class Listener implements org.bukkit.event.Listener {
	
	public static HashMap<Integer, Item> magicItems = new HashMap<Integer, Item>();
	
	@EventHandler
	public void join(PlayerJoinEvent event)
	{
		addPacketListener(event.getPlayer());
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent event)
	{
		removePacketListener(event.getPlayer());
	}
	
	@EventHandler
	public void EntityDeath(EntityDeathEvent event)
	{
		if (event.getEntityType() == EntityType.ZOMBIE)
		{
			if (event.getEntity().getKiller() == null)
				return;
			
			LivingEntity entity = event.getEntity();
			World world = entity.getWorld();
			Location loc = entity.getLocation();
			
			NMSOcelot ocelot = new NMSOcelot(world);
			
			ocelot.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
			
			((CraftWorld) world).getHandle().addEntity(ocelot);
		}
		else if (event.getEntityType() == EntityType.OCELOT)
		{
			if (!NMSOcelot.uuids.contains(event.getEntity().getUniqueId()) || event.getEntity().getKiller() == null)
				return;
			
			TestMain.database.saveData(
			event.getEntity().getKiller().getName(),
			event.getEntity().getName(),
			String.valueOf(System.currentTimeMillis())
					);
			
			event.getDrops().clear();
			
			Location loc = event.getEntity().getLocation();
			
			Item item = loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.LEATHER));
			
			item.setCustomNameVisible(true);
			
			magicItems.put(item.getEntityId(), item);
		}
	}
	
	public void addPacketListener(Player player)
	{
		ChannelDuplexHandler duplex = new ChannelDuplexHandler() {
			
			@Override
			public void write(ChannelHandlerContext context, Object packet, ChannelPromise promise) throws Exception
			{
				if (packet instanceof PacketPlayOutEntityMetadata)
				{	
					
					int entityId = (int) GetPrivateField("a", PacketPlayOutEntityMetadata.class, (PacketPlayOutEntityMetadata) packet);
					
					if (magicItems.containsKey(entityId))
					{
						Item item = magicItems.get(entityId);
						
						item.setCustomName(player.getName());
						
						packet = new PacketPlayOutEntityMetadata(entityId, ((CraftItem)item).getHandle().getDataWatcher(), true);
					}
				}
				
				super.write(context, packet, promise);
			}
			
		};
		
		ChannelPipeline pipeline = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel.pipeline();
		pipeline.addBefore("packet_handler", player.getName(), duplex);
	}
	
	public void removePacketListener(Player player)
	{
		Channel channel = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel;
		
		channel.eventLoop().submit(() ->
		{
			channel.pipeline().remove(player.getName());
			return null;
		});
	}
	
	public static Object GetPrivateField(String name, Class<?> fieldClass, Object instance)
	{
		try
		{
			Field unknownField = fieldClass.getDeclaredField(name);
			
			unknownField.setAccessible(true);
			
			return unknownField.get(instance);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
