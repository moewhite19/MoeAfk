package cn.whiteg.moeAfk.listener;

import cn.whiteg.moeAfk.Event.PlayerAfkChanEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AfkListener implements Listener {

    @EventHandler
    public void onAfk(PlayerAfkChanEvent event) {
        String color = "§7";
        if (event.isAfk()){
            final String dn = event.getPlayer().getDisplayName();
            broadcastMessage(dn + "§r §b双手离开了键盘");
            int inx = dn.indexOf(event.getPlayer().getName());
            if (inx >= 0){
                final StringBuilder sb = new StringBuilder(dn);
                sb.insert(inx,color);
                event.getPlayer().setDisplayName(sb.toString());
            }
        } else {
            final String dn = event.getPlayer().getDisplayName();
            int inx = dn.indexOf(event.getPlayer().getName());
            if (inx >= 0){
                final StringBuilder sb = new StringBuilder(dn);
                final String c = sb.substring(inx - color.length(),inx);
                if (c.equals(color)){
                    sb.delete(inx - color.length(),inx);
                    event.getPlayer().setDisplayName(sb.toString());
                }
            }
            event.getPlayer().setPlayerListName(event.getPlayer().getDisplayName());
            broadcastMessage(event.getPlayer().getDisplayName() + "§r §b回来了");
        }
    }

    public void broadcastMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage(msg);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(msg);
        }
    }
}
