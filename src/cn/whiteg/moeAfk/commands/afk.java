package cn.whiteg.moeAfk.commands;

import cn.whiteg.moeAfk.AfkTimer;
import cn.whiteg.moeAfk.CommandInterface;
import cn.whiteg.moeAfk.MoeAfk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class afk extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (sender instanceof Player){
            if (!sender.hasPermission("mmo.afk")){
                sender.sendMessage("§b权限不足");
                return true;
            }
            //坑待填
            AfkTimer.AfkStaus s = MoeAfk.plugin.afkTimer.getAfkStaus((Player) sender);
            if (s != null){
                s.setAfk(!s.isAfkin());
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        return null;
    }
}
