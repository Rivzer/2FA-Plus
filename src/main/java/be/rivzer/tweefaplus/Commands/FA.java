package be.rivzer.tweefaplus.Commands;

import be.rivzer.tweefaplus.Config.Config;
import be.rivzer.tweefaplus.Listeners.onJoin;
import be.rivzer.tweefaplus.Logger;
import be.rivzer.tweefaplus.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FA implements CommandExecutor {

    private Main plugin;

    public FA(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("2fa").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(cmd.getName().equalsIgnoreCase("2fa")){
            Player p = (Player) sender;
            if(!p.hasPermission("2fa.use")){
                p.sendMessage(Logger.color(Config.getCustomConfig1().getString("Prefix") + "&cU heeft niet de juiste rank voor die commando uit te voeren."));
                return true;
            }

            if(args.length == 0){
                if(Config.getCustomConfig1().getString("authcodes." + p.getUniqueId() + ".2faEnabled") == "false" || Config.getCustomConfig1().getString("authcodes." + p.getUniqueId()) == null){
                    Config.getCustomConfig1().set("authcodes." + p.getUniqueId() + ".2faEnabled", true);
                    Config.saveConfig1();
                    p.sendMessage(Logger.color(Config.getCustomConfig1().getString("Prefix") + "&7Uw &b2FA &7staat nu &aaan."));
                }
                else{
                    if(onJoin.authlocked.contains(p.getUniqueId())){
                        p.sendMessage(Logger.color(Config.getCustomConfig1().getString("Prefix") + "&cU moet eerst uw 2fa invoeren voor u uw 2fa kan uitschakelen!"));
                    }
                    else {
                        Config.getCustomConfig1().set("authcodes." + p.getUniqueId() + ".2faEnabled", false);
                        Config.saveConfig1();
                        p.sendMessage(Logger.color(Config.getCustomConfig1().getString("Prefix") + "&7Uw &b2FA &7staat nu &cuit."));
                    }
                }
            }
        }

        return false;
    }
}
