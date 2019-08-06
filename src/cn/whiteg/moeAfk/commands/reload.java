package cn.whiteg.moeAfk.commands;

import cn.whiteg.mmocore.CommandInterface;
import cn.whiteg.moeAfk.MoeAfk;
import cn.whiteg.moeAfk.Setting;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class reload extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (!sender.hasPermission("whiteg.test")){
            sender.sendMessage("§b权限不足");
            return true;
        }
        MoeAfk.plugin.setting = new Setting();  
        sender.sendMessage("已重载");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        return null;
    }
}
