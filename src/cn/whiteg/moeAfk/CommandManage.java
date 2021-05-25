package cn.whiteg.moeAfk;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandManage extends CommandInterface {
    final public SubCommand subCommand = new SubCommand();
    public Map<String, CommandInterface> commandMap = new HashMap();
    public List<String> AllCmd;

    public CommandManage() {
        AllCmd = Arrays.asList("reload","afk");
        for (int i = 0; i < AllCmd.size(); i++) {
            try{
                Class c = Class.forName("cn.whiteg.moeAfk.commands." + AllCmd.get(i));
                regCommand(AllCmd.get(i),(CommandInterface) c.newInstance());
            }catch (ClassNotFoundException | InstantiationException | IllegalAccessException e){
                e.printStackTrace();
            }

        }
    }

    public static List<String> getMatches(String[] args,List<String> list) {
        return getMatches(args[args.length - 1],list);
    }

    public static List<String> getMatches(List<String> list,String[] args) {
        return getMatches(args[args.length - 1],list);
    }

    public static List<String> getMatches(String value,List<String> list) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i).intern().toLowerCase();
            if (str.startsWith(value.toLowerCase())){
                result.add(list.get(i));
            }
        }
        return result;
    }

    public static List<String> getMatches(List<String> list,String value) {
        return getMatches(list,value);
    }

    public static List<String> PlayersList(String arg) {
        List<String> players = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) players.add(p.getName());
        return getMatches(arg,players);
    }

    public static List<String> PlayersList(String[] arg) {
        return PlayersList(arg[arg.length - 1]);
    }

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 0){
            sender.sendMessage("§2[§bMoeAfk§2] §f当前允许挂机的最大在线玩家为: " + MoeAfk.plugin.setting.maxPlayer);
            return true;
        }

        CommandInterface subCommand = commandMap.get(args[0]);
        if (subCommand != null){
            if (args.length > 1){
                String[] subArgs = new String[args.length - 1];
                System.arraycopy(args,1,subArgs,0,subArgs.length);
                return subCommand.onCommand(sender,cmd,label,subArgs);
            } else {
                return subCommand.onCommand(sender,cmd,label,new String[]{});
            }
        } else {
            sender.sendMessage("无效指令");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            return getMatches(args[0].toLowerCase(),AllCmd);
        } else if (args.length > 1){
            CommandInterface subCommand = commandMap.get(args[0]);
            if (subCommand != null){
                String[] subArgs = new String[args.length - 1];
                System.arraycopy(args,1,subArgs,0,subArgs.length);
                return subCommand.onTabComplete(sender,cmd,label,subArgs);
            }
        }
        return null;
    }

    public void regCommand(String name,CommandInterface cmd) {
        commandMap.put(name,cmd);
        PluginCommand pc = MoeAfk.plugin.getCommand(name);
        if (pc != null){
            pc.setExecutor(subCommand);
            pc.setTabCompleter(subCommand);
        }
    }

    public class SubCommand extends CommandInterface {
        @Override
        public boolean onCommand(CommandSender commandSender,Command command,String s,String[] strings) {
            CommandInterface ci = commandMap.get(command.getName());
            if (ci == null) return false;
            String[] args = new String[strings.length + 1];
            args[0] = command.getName();
            System.arraycopy(strings,0,args,1,strings.length);
            ci.onCommand(commandSender,command,s,args);
            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender commandSender,Command command,String s,String[] strings) {
            CommandInterface ci = commandMap.get(command.getName());
            if (ci == null) return null;
            String[] args = new String[strings.length + 1];
            args[0] = command.getName();
            System.arraycopy(strings,0,args,1,strings.length);
            return ci.onTabComplete(commandSender,command,s,args);
        }
    }

}
