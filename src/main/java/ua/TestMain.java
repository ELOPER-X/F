package ua;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TestMain extends JavaPlugin {
	
	public static TestMain Instance;
	public static SQLiteDatabase database;
	
	public void onEnable()
	{
		Instance = this;
		database = new SQLiteDatabase();
		
		Bukkit.
		getPluginManager().
		registerEvents(new Listener(), this);
		
		
	}

}
