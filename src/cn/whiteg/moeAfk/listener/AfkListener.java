package cn.whiteg.moeAfk.listener;

import cn.whiteg.moeAfk.Event.PlayerAfkChanEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AfkListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAfk(PlayerAfkChanEvent event) {
        String tag = "  §7AFK";
        Player player = event.getPlayer();
        String dn = player.getPlayerListName();
        if (event.isAfk()){
            broadcastMessage(dn + "§r §b双手离开了键盘");
            if (!dn.contains(tag)){
                player.setPlayerListName(dn + tag);
            }
//            int inx = dn.indexOf(event.getPlayer().getName());
//            if (inx >= 0){
//                final StringBuilder sb = new StringBuilder(dn);
//                sb.insert(inx,color);
//                event.getPlayer().setDisplayName(sb.toString());
//            }
        } else {
            broadcastMessage(event.getPlayer().getDisplayName() + "§r §b回来了");
            int inx = dn.indexOf(tag);
            if (inx >= 0){
                StringBuilder sb = new StringBuilder(dn);
                sb.delete(inx,inx + tag.length());
                player.setPlayerListName(sb.toString());
            }
//            int inx = dn.indexOf(event.getPlayer().getName());
//            if (inx >= 0){
//                final StringBuilder sb = new StringBuilder(dn);
//                final String c = sb.substring(inx - tag.length(),inx);
//                if (c.equals(tag)){
//                    sb.delete(inx - tag.length(),inx);
//                    event.getPlayer().setDisplayName(sb.toString());
//                }
//            }
//            event.getPlayer().setPlayerListName(event.getPlayer().getDisplayName());
        }
    }

    public void broadcastMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage(msg);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(msg);
        }
    }
}
