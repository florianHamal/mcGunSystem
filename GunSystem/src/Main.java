import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
public class Main extends JavaPlugin {
    private static Main plugin;
    @Override
    public void onEnable(){
        plugin=this;
        PluginManager pluginManager =  Bukkit.getPluginManager();
        pluginManager.registerEvents(new WeaponHandler(this),this);
    }
    public static Main getPlugin(){
        return plugin;
    }


    public static void main(String[] args) {

    }
}
