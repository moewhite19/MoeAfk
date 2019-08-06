package cn.whiteg.moeAfk;

import cn.whiteg.moeAfk.Event.PlayerAfkChanEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class AfkTimer implements Listener {
    final MoeAfk plugin;
    final Map<UUID, AfkStaus> map = new HashMap<>();
    final BukkitTask bt;
    final private int kickTime;
    private boolean canKick = false;


    public AfkTimer() {
        plugin = MoeAfk.plugin;
        kickTime = plugin.setting.maximumTime + 10;
        bt = new BukkitRunnable() {
            /**
             * When an object implementing interface <code>Runnable</code> is used
             * to create a thread, starting the thread causes the object's
             * <code>run</code> method to be called in that separately executing
             * thread.
             * <p>
             * The general contract of the method <code>run</code> is that it may
             * take any action whatsoever.
             *
             * @see Thread#run()
             */
            @Override
            public void run() {

                Iterator<Map.Entry<UUID, AfkStaus>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    final Map.Entry<UUID, AfkStaus> e = it.next();
                    final AfkStaus s = e.getValue();
                    if (!s.player.isOnline()){
                        it.remove();
                        return;
                    }
                    s.check();
                    if (canKick && s.isAfkin() && s.getTime() > kickTime){
                        Bukkit.getScheduler().runTask(plugin,() -> s.player.kickPlayer("在线玩家过多 已请出闲置玩家"));
                    }
                }
            }
        }.runTaskTimer(plugin,20,20);
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        if (!players.isEmpty()){
            for (Player p : players) {
                map.put(p.getUniqueId(),new AfkStaus(p));
            }
            setCanKick();
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        map.put(event.getPlayer().getUniqueId(),new AfkStaus(event.getPlayer()));
        setCanKick();
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        map.remove(event.getPlayer().getUniqueId());
        setCanKick();
    }

    public AfkStaus getAfkStaus(Player player) {
        return map.get(player.getUniqueId());
    }

    public void setCanKick() {
        canKick = map.size() > plugin.setting.maxPlayer;
    }

    public void unreg() {
        BroadcastMessageEvent.getHandlerList().unregister(this);
        bt.cancel();
    }

    public class AfkStaus {
        final Player player;
        //        float y;
        private float p;
        private int comt = 0;
        private boolean afkin = false;

        AfkStaus(Player player) {
            this.player = player;
//            final Location l = player.getLocation();
            p = 0;
//            y = 0;
        }

        void check() {
            final Location l = player.getLocation();
            final float i = Math.abs(l.getPitch() - p);
            p = l.getPitch();
            if (afkin){
                if (i > 0.25F){
                    setAfk(false);
                } else {
                    comt++;
                }
            } else {
                if (i < 0.25F){
                    comt++;
                    if (comt >= plugin.setting.maximumTime){
                        setAfk(true);
                    }
                } else {
                    comt = 0;
                }
            }
        }

        public int getTime() {
            return comt;
        }

        public boolean isAfkin() {
            return afkin;
        }

        public void setAfk(boolean b) {
            if (b == afkin) return;
            if (afkin = b){
                PlayerAfkChanEvent e = new PlayerAfkChanEvent(player,true);
                e.call();
            } else {
                PlayerAfkChanEvent e = new PlayerAfkChanEvent(player,false);
                e.call();
                comt = 0;
            }
        }
    }
}
