package be.rivzer.tweefaplus.Inventorys;

import be.rivzer.tweefaplus.Logger;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FA {

    public static void OpenTelefoon(Player p){
        Inventory inv = Bukkit.createInventory(p, 9, Logger.color("&0."));

        setItem(inv, new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getDyeData()), 3, "&a&lTurn &b&l2FA &a&lOn", "turn2FAOn");

        setItem(inv, new ItemStack(Material.WOOL, 1, DyeColor.RED.getDyeData()), 5, "&c&lTurn &b&l2FA &c&lOff", "turn2FAOff");

        p.closeInventory();
        p.openInventory(inv);
    }

    public static void setItem(Inventory inv, ItemStack mat, Integer slotnum, String name, String nbteNaam){
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Logger.color(name));
        item.setItemMeta(meta);

        NBTItem nbti = new NBTItem(item);
        nbti.setString("mtcustom", nbteNaam);

        ItemStack item1 = nbti.getItem();

        inv.setItem(slotnum, item1);
    }

}
