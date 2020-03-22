package cn.whiteg.moeAfk;

import cn.whiteg.moeAfk.Event.PlayerAfkChanEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
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
            @Override
            public void run() {
                Iterator<Map.Entry<UUID, AfkStaus>> it = map.entrySet().iterator();
                Player kickPlayer = null;
                int afktime = 0;
                while (it.hasNext()) {
                    final Map.Entry<UUID, AfkStaus> e = it.next();
                    final AfkStaus s = e.getValue();
                    if (!s.player.isOnline()){
                        it.remove();
                        return;
                    }
                    s.check();
                    if (canKick && s.isAfkin() && s.getTime() > kickTime){
                        if (kickPlayer == null){
                            kickPlayer = s.player;
                            afktime = s.getTime();
                        } else {
                            int i = s.getTime();
                            if (i > afktime){
                                kickPlayer = s.getPlayer();
                                afktime = s.getTime();
                            }
                        }
                    }
                }
                if (kickPlayer != null){
                    kickPlayer.kickPlayer("在线玩家过多,已请出闲置玩家.");
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

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void playerOnTp(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        AfkStaus s = map.get(player.getUniqueId());
        if (s == null) return;
        s.setPitch(event.getTo().getPitch());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void playerOnRespawn(PlayerRespawnEvent event) {
        AfkStaus s = getAfkStaus(event.getPlayer());
        if (s == null) return;
        s.setPitch(event.getRespawnLocation().getPitch());
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

    public Map<UUID, AfkStaus> getMap() {
        return map;
    }

    public void stop() {
        bt.cancel();
        map.clear();
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

        public Player getPlayer() {
            return player;
        }

        public float getPitch() {
            return p;
        }

        public void setPitch(float p) {
            this.p = p;
        }

    }
}
