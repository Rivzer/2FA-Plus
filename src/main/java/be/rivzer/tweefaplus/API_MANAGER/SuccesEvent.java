package be.rivzer.tweefaplus.API_MANAGER;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SuccesEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    Player p;
    boolean succses = false;

    public SuccesEvent(Player p, boolean succses){
        this.p = p;
        this.succses = succses;
    }

    public Player getPlayer(){
        return p;
    }

    public Boolean getSuccses(){
        return succses;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
