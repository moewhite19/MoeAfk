package cn.whiteg.moeAfk.listener;

import cn.whiteg.moeAfk.Event.PlayerAfkChanEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SleepListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAfkStatusChan(PlayerAfkChanEvent event) {
        event.getPlayer().setSleepingIgnored(event.isAfk());
    }
}
