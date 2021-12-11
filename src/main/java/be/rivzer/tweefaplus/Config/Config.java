package be.rivzer.tweefaplus.Config;

import be.rivzer.tweefaplus.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    static Main plugin = Main.getPlugin(Main.class);

    public static File customConfigFile1;
    private static FileConfiguration customConfig1;

    public static FileConfiguration getCustomConfig1() {
        return customConfig1;
    }

    public static void createCustomConfig1() {
        customConfigFile1 = new File(plugin.getDataFolder(), "config.yml");
        if (!customConfigFile1.exists()) {
            customConfigFile1.getParentFile().mkdirs();
            plugin.saveResource("config.yml", false);
        }

        customConfig1 = new YamlConfiguration();

        try {
            customConfig1.load(customConfigFile1);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig1() {
        File file = new File("plugins//2FA-Plus//config.yml");
        try {
            Config.getCustomConfig1().save(file);
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }

}
