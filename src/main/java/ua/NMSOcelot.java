package ua;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;

import net.minecraft.server.v1_13_R2.ChatComponentText;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityOcelot;
import net.minecraft.server.v1_13_R2.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_13_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_13_R2.PathfinderGoalSelector;

public class NMSOcelot extends EntityOcelot {
	
	public static ArrayList<UUID> uuids = new ArrayList<UUID>();
	
	private TestMain plugin;
	
	public NMSOcelot (TestMain plugin, World world)
	{
		super(((CraftWorld) world).getHandle());
		
		this.plugin = plugin;
		
		uuids.add(this.getUniqueID());
		
		this.setCustomName(new ChatComponentText(rndString(5)));
		this.setCustomNameVisible(true);
		
		Set<?> goalSelectorB = (Set<?>) plugin.getReflectionHelper().GetPrivateField("b", PathfinderGoalSelector.class, goalSelector);
        Set<?> goalSelectorC = (Set<?>) plugin.getReflectionHelper().GetPrivateField("c", PathfinderGoalSelector.class, goalSelector);
        Set<?> targetSelectorB = (Set<?>) plugin.getReflectionHelper().GetPrivateField("b", PathfinderGoalSelector.class, targetSelector);
        Set<?> targetSelectorC = (Set<?>) plugin.getReflectionHelper().GetPrivateField("c", PathfinderGoalSelector.class, targetSelector);
        
        goalSelectorB.clear();
        goalSelectorC.clear();
        targetSelectorB.clear();
        targetSelectorC.clear();
        
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 0.7D, false));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
	}
	
	public String rndString(int len)
	{
		return plugin.getRandom().ints(48, 122)
		            .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
		            .mapToObj(i -> (char) i)
		            .limit(len)
		            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
		            .toString();
	}

}
