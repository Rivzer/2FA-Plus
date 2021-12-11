package be.rivzer.tweefaplus.API_MANAGER;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EnabledEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    Player p;
    boolean enabled = false;

    public EnabledEvent(Player p, boolean enabled){
        this.p = p;
        this.enabled = enabled;
    }

    public boolean getEnabled(){
        return enabled;
    }

    public Player getPlayer(){
        return p;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
