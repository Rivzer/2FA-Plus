package be.rivzer.tweefaplus;

import org.bukkit.entity.Player;
import org.bukkit.map.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Renderer extends MapRenderer {

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player p) {
        try {
            BufferedImage image = ImageIO.read(new File("plugins//2FA-Plus//" + p.getUniqueId() + ".png"));
            mapCanvas.drawImage(42, 42, image);

            MapCursorCollection cursors = new MapCursorCollection();
            cursors.addCursor(new MapCursor((byte) 60, (byte) 70, (byte) 12, (byte) 4, true));
            mapCanvas.setCursors(cursors);
        } catch (IOException e) {
            p.getInventory().clear();
            p.sendMessage(Logger.color("The image couldn't be loaded. This is likely due to a 403 error."));
            return;
        }
    }
}