package cn.whiteg.moeAfk;

import cn.whiteg.mmocore.util.PluginUtil;
import cn.whiteg.moeAfk.listener.AfkListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class MoeAfk extends JavaPlugin {
    public static Logger logger;
    public static MoeAfk plugin;
    public CommandManage mainCommand;
    public Map<String, Listener> listenerMap = new HashMap<>();
    public Setting setting;
    public AfkTimer afkTimer;


    public MoeAfk() {
        plugin = this;
    }

    public void onLoad() {
        saveDefaultConfig();
        logger = getLogger();
    }

    public void onEnable() {
        setting = new Setting();
        logger.info("开始加载插件");
        afkTimer = new AfkTimer();
        regEven(afkTimer);
        PluginCommand pc = PluginUtil.getPluginCommand(this,"moeafk");
        pc.setExecutor(mainCommand = new CommandManage());
        regEven(new AfkListener());
        logger.info("加载完成,当前在线" + afkTimer.map.size() + "最大挂机时间" + setting.maximumTime);
    }

    public void onDisable() {
        unregEven();
        //注销注册玩家加入服务器事件
        listenerMap.clear();
        logger.info("插件已关闭");
    }

    public void onRestart() {
        logger.info("--开始重载--");
        onDisable();
        setting = new Setting();
        Bukkit.getScheduler().runTask(this,() -> {
            onEnable();
            logger.info("--重载完成--");
        });
    }


    public void regEven(Listener listener) {
        regEven(listener.getClass().getName(),listener);

    }

    public void regEven(String key,Listener listener) {
        logger.info("注册事件:" + key);
        listenerMap.put(key,listener);
        Bukkit.getPluginManager().registerEvents(listener,plugin);

    }

    public void unregEven() {
        for (Map.Entry<String, Listener> entry : listenerMap.entrySet()) {
            try{
                unregEven(entry.getKey());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void unregEven(String Key) {
        Listener evens = listenerMap.get(Key);
        if (evens == null){
            return;
        }
        logger.info("注销: " + Key);
        try{
            Class c = evens.getClass();
            Method unreg = c.getDeclaredMethod("unreg");
            unreg.setAccessible(true);
            unreg.invoke(evens);
        }catch (Exception e){
            logger.info("没有注销: " + Key);
        }
    }
}
