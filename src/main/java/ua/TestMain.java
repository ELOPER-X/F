package ua;
import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TestMain extends JavaPlugin {
	
	private SQLiteDatabase database;
	private ReflectionHelper reflection = new ReflectionHelper();
	private final Random random = new Random();
	
	public void onEnable()
	{
		database = new SQLiteDatabase(this, "jdbc:sqlite:" + this.getDataFolder().getAbsolutePath() + File.separator + "SQlite.db");
		
		Bukkit.
		getPluginManager().
		registerEvents(new Listener(this), this);
		
		
	}
	
	public SQLiteDatabase getDatabase()
	{
		return database;
	}
	
	public Random getRandom()
	{
		return random;
	}
	
	public ReflectionHelper getReflectionHelper()
	{
		return reflection;
	}

}
