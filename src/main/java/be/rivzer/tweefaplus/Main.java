package be.rivzer.tweefaplus;

import be.rivzer.tweefaplus.API_MANAGER.EnabledEvent;
import be.rivzer.tweefaplus.Commands.FA;
import be.rivzer.tweefaplus.Config.Config;
import be.rivzer.tweefaplus.Listeners.onJoin;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        //Console
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        //Config
        Config.createCustomConfig1();

        //Util
        onJoin.authlocked = new ArrayList<UUID>();
        onJoin.authlockedfirst = new ArrayList<UUID>();

        //Bstats
        Metrics metrics = new Metrics(this, 12554);

        //Commands
        new FA(this);

        //Listeners
        Bukkit.getServer().getPluginManager().registerEvents(new onJoin(), this);

        //Console Logs
        console.sendMessage(Logger.color("&f----------------------------------------"));
        console.sendMessage(Logger.color(""));
        console.sendMessage(Logger.color("&4&lPlugin wordt aangezettt..."));
        console.sendMessage(Logger.color(""));
        console.sendMessage(Logger.color("&4&lCoded by&f&l: Rivzer"));
        console.sendMessage(Logger.color(""));
        console.sendMessage(Logger.color("&f----------------------------------------"));
    }

    @Override
    public void onDisable() {
        //Console
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        //Console Logs
        console.sendMessage(Logger.color("&f----------------------------------------"));
        console.sendMessage(Logger.color(""));
        console.sendMessage(Logger.color("&c&lPlugin wordt uitgezet..."));
        console.sendMessage(Logger.color(""));
        console.sendMessage(Logger.color("&c&lCoded by&f&l: Rivzer"));
        console.sendMessage(Logger.color(""));
        console.sendMessage(Logger.color("&f----------------------------------------"));
    }
}