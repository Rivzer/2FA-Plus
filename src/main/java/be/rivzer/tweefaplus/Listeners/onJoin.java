package be.rivzer.tweefaplus.Listeners;

import be.rivzer.tweefaplus.API_MANAGER.EnabledEvent;
import be.rivzer.tweefaplus.API_MANAGER.SuccesEvent;
import be.rivzer.tweefaplus.Config.Config;
import be.rivzer.tweefaplus.Logger;
import be.rivzer.tweefaplus.Main;
import be.rivzer.tweefaplus.Renderer;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.UUID;

public class onJoin implements Listener {

    public static ArrayList<UUID> authlocked;
    public static ArrayList<UUID> authlockedfirst;
    private Main plugin = Main.getPlugin(Main.class);

    public static String getGoogleAuthenticatorBarCode(String secretKey, String account, String issuer) {
        try {
            return "otpauth://totp/"
                    + URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
                    + "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20")
                    + "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void createQRCode(String barCodeData, String filePath, int height, int width)
            throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE,
                width, height);
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            MatrixToImageWriter.writeToStream(matrix, "png", out);
        }
    }

    @EventHandler
    public void onMapInitialize(MapInitializeEvent e) {
        MapView mapView = e.getMap();
        mapView.setScale(MapView.Scale.CLOSEST);
        mapView.setUnlimitedTracking(true);
        mapView.getRenderers().clear();
        mapView.addRenderer(new Renderer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();

        if(p.hasPermission("2fa.use")) {
            if (Config.getCustomConfig1().getString("authcodes." + p.getUniqueId()) == null) {
                Bukkit.getPluginManager().callEvent(new EnabledEvent(p, false));
            } else if (Config.getCustomConfig1().getString("authcodes." + p.getUniqueId() + ".2faEnabled") == null) {
                Bukkit.getPluginManager().callEvent(new EnabledEvent(p, false));
            } else if (Config.getCustomConfig1().getString("authcodes." + p.getUniqueId() + ".2faEnabled") == "false") {
                Bukkit.getPluginManager().callEvent(new EnabledEvent(p, false));
            } else {
                Bukkit.getPluginManager().callEvent(new EnabledEvent(p, true));
            }
        }
    }

    @EventHandler
    public void Interact(PlayerInteractEvent e){
        Player p = e.getPlayer();

        if(p.hasPermission("2fa.use")) {
            if (Config.getCustomConfig1().getString("authcodes." + p.getUniqueId()) == null) {
                Bukkit.getPluginManager().callEvent(new EnabledEvent(p, false));
            } else if (Config.getCustomConfig1().getString("authcodes." + p.getUniqueId() + ".2faEnabled") == null) {
                Bukkit.getPluginManager().callEvent(new EnabledEvent(p, false));
            } else if (Config.getCustomConfig1().getString("authcodes." + p.getUniqueId() + ".2faEnabled") == "false") {
                Bukkit.getPluginManager().callEvent(new EnabledEvent(p, false));
            } else {
                Bukkit.getPluginManager().callEvent(new EnabledEvent(p, true));
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if(p.hasPermission("2fa.use")){
            if(Config.getCustomConfig1().getString("authcodes." + p.getUniqueId()) == null){
                Bukkit.getPluginManager().callEvent(new EnabledEvent(p, false));
            }
            else if(Config.getCustomConfig1().getString("authcodes." + p.getUniqueId() + ".2faEnabled") == null){
                Bukkit.getPluginManager().callEvent(new EnabledEvent(p, false));
            }
            else if(Config.getCustomConfig1().getString("authcodes." + p.getUniqueId() + ".2faEnabled") == "false"){
                Bukkit.getPluginManager().callEvent(new EnabledEvent(p, false));
            }
            else {
                Bukkit.getPluginManager().callEvent(new EnabledEvent(p, true));
            }

            if(Config.getCustomConfig1().getString("authcodes." + p.getUniqueId() + ".2faEnabled") == "false" || Config.getCustomConfig1().getString("authcodes." + p.getUniqueId()) == null){
                if(p.getInventory().getItemInMainHand() == null) return;
                p.getInventory().setHeldItemSlot(4);
                if(p.getInventory().getItemInMainHand().getType().equals(Material.MAP)){
                    if(p.getInventory().getItemInMainHand().getItemMeta() == null) return;
                    if(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName() == null) return;
                    if(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName() == Logger.color("&bGoogle Auth QR Code"));
                    p.getInventory().setItem(4, new ItemStack(Material.AIR));
                }
                return;
            }

            GoogleAuthenticator gauth = new GoogleAuthenticator();
            GoogleAuthenticatorKey key = gauth.createCredentials();

            if(key.getKey() == null){
                p.sendMessage(Logger.color("&cEr ging iets fout bij het aanmaken van de 2FA Key!"));
                return;
            }

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for(int i = 0; i < 100; i++){
                    p.sendMessage("");
                }
            }, 1*20);

            if(Config.getCustomConfig1().getString("authcodes." + p.getUniqueId() + ".firstValid") == "false" || Config.getCustomConfig1().getString("authcodes." + p.getUniqueId() + ".firstValid") == null){
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    p.sendTitle(Logger.color("&7Scan u &bGoogle Auth Code"), "", 1, 6*20 ,1);

                    try {
                        String barCodeUrl = getGoogleAuthenticatorBarCode(key.getKey(), p.getPlayer().getName(), Config.getCustomConfig1().getString("BedrijfsNaam"));
                        createQRCode(barCodeUrl, "plugins//2FA-Plus//" + p.getUniqueId() + ".png", 32, 32);
                    } catch (WriterException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    authlockedfirst.add(p.getUniqueId());

                    ItemStack map = new ItemStack(Material.MAP, 1);
                    ItemMeta mapmeta = map.getItemMeta();
                    mapmeta.setDisplayName(Logger.color("&bGoogle Auth QR Code"));
                    map.setItemMeta(mapmeta);

                    p.getInventory().setHeldItemSlot(4);

                    if(!(p.getInventory().getItemInMainHand() == null)){
                        ItemStack item = new ItemStack(p.getInventory().getItemInMainHand().getType(), p.getInventory().getItemInMainHand().getAmount());
                        ItemMeta meta = item.getItemMeta();
                        if(item.getItemMeta() != null) {
                            if (item.getItemMeta().getDisplayName() != null) {
                                meta.setDisplayName(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                                ArrayList<String> lore = new ArrayList<>();
                                for (int i = 0; i < meta.getLore().size(); i++){
                                    lore.add(meta.getLore().get(i));
                                }
                                item.setItemMeta(meta);
                            }
                        }
                    }

                    p.getInventory().setItem(4, map);

                    Config.getCustomConfig1().set("authcodes." + p.getUniqueId() + ".KEY", key.getKey());
                    Config.getCustomConfig1().set("authcodes." + p.getUniqueId() + ".firstValid", false);
                    Config.saveConfig1();
                }, 1*10);
            }
            else {
                if(Config.getCustomConfig1().contains("authcodes." + p.getUniqueId() + ".KEY")){
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        p.sendTitle(Logger.color("&7Geef u &b2FA &7op"), "", 1, 6*20 ,1);

                        authlocked.add(p.getUniqueId());
                        p.sendMessage(Logger.color("&7Open de Google Authenticator-app en geef de zescijferige code op."));
                    }, 1*10);
                }
                else {
                    p.sendMessage(Logger.color("&cEr is iets fout gegaan met het laden van de 2FA Database, Relog aub!"));
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        String message = e.getMessage();

        if(authlocked.contains(p.getUniqueId())){
            if(!isInteger(message)){
                p.sendMessage(Logger.color(Config.getCustomConfig1().getString("Prefix")) + Logger.color("&c** Een code bevat alleen cijfers **"));
            }
            else {
                playerInputCode(p, Integer.valueOf(message));
            }

            e.setCancelled(true);
        }

        if(authlockedfirst.contains(p.getUniqueId())){
            if(!isInteger(message)){
                p.sendMessage(Logger.color(Config.getCustomConfig1().getString("Prefix")) + Logger.color("&c** Een code bevat alleen cijfers **"));
            }
            else {
                playerInputCode(p, Integer.valueOf(message));
            }

            e.setCancelled(true);
        }
    }

    @EventHandler
    public void move(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (authlocked.contains(p.getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void click(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(p == null) return;
        if (authlocked.contains(p.getUniqueId()) || authlockedfirst.contains(p.getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(p == null) return;
        if (authlocked.contains(p.getUniqueId()) || authlockedfirst.contains(p.getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void blockbreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (authlocked.contains(p.getUniqueId()) || authlockedfirst.contains(p.getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void blockplace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (authlocked.contains(p.getUniqueId()) || authlockedfirst.contains(p.getUniqueId())) {
            e.setCancelled(true);
        }
    }

    private boolean playerInputCode(Player p, int code) {
        String secretkey = Config.getCustomConfig1().getString("authcodes." + p.getUniqueId() + ".KEY");

        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        boolean codeisvalid = gAuth.authorize(secretkey, code);

        if (codeisvalid) {
            if(authlocked.contains(p.getUniqueId())){
                authlocked.remove(p.getUniqueId());
                p.sendMessage(Logger.color(Config.getCustomConfig1().getString("Prefix") + Logger.color("&a*Toegang Verleend* &bWelkom op de server!")));
            }

            if(authlockedfirst.contains(p.getUniqueId())){
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    authlockedfirst.remove(p.getUniqueId());
                    p.getInventory().setItem(4, new ItemStack(Material.AIR));
                    p.updateInventory();
                    Config.getCustomConfig1().set("authcodes." + p.getUniqueId() + ".firstValid", true);
                    Config.saveConfig1();
                    p.sendMessage(Logger.color(Config.getCustomConfig1().getString("Prefix") + "&7Uw &b2FA &7is nu ingesteld en klaar voor gebruik!"));
                    p.sendTitle(Logger.color("&7Uw &b2FA &7is ingesteld"), "", 1, 5*20, 1);
                }, 1*10);
            }

            Bukkit.getPluginManager().callEvent(new SuccesEvent(p, true));
            return codeisvalid;
        }
        else if(!codeisvalid){
            if(authlocked.contains(p.getUniqueId()))
                p.sendMessage(Logger.color(Config.getCustomConfig1().getString("Prefix")) + Logger.color("&cOnjuiste of verlopen code!"));

            if(authlockedfirst.contains(p.getUniqueId()))
                p.sendMessage(Logger.color(Config.getCustomConfig1().getString("Prefix")) + Logger.color("&cOnjuiste of verlopen code!"));
        }

        Bukkit.getPluginManager().callEvent(new SuccesEvent(p, false));
        return codeisvalid;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }
}
