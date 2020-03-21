package cn.whiteg.moeAfk.listener;

import cn.whiteg.moeAfk.AfkTimer;
import cn.whiteg.moeAfk.MoeAfk;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishListener implements Listener {
    //防挂机钓鱼
    @EventHandler(ignoreCancelled = true)
    public void onFish(PlayerFishEvent event) {
        AfkTimer.AfkStaus s = MoeAfk.plugin.afkTimer.getAfkStaus(event.getPlayer());
        if (s.isAfkin()){
            Bukkit.getScheduler().runTask(MoeAfk.plugin,() -> {
                event.getPlayer().kickPlayer("禁止挂机钓鱼");
            });
        }
    }
}
