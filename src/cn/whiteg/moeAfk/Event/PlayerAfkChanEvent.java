package cn.whiteg.moeAfk.Event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerAfkChanEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    final boolean isAfk;

    public PlayerAfkChanEvent(Player who,boolean isAfk) {
        super(who);
        this.isAfk = isAfk;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean isAfk() {
        return isAfk;
    }

    public void call() {
        try{
            Bukkit.getPluginManager().callEvent(this);
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

}
