package cn.whiteg.moeAfk;

import cn.whiteg.moeAfk.listener.AfkListener;
import cn.whiteg.moeAfk.listener.FishListener;
import cn.whiteg.moeAfk.listener.SleepListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;

import java.util.logging.Logger;


public class MoeAfk extends PluginBase {
    public static Logger logger;
    public static MoeAfk plugin;
    public CommandManage mainCommand;
    public Setting setting;
    public AfkTimer afkTimer;


    public MoeAfk() {
        plugin = this;
    }

    public void onLoad() {
        saveDefaultConfig();
        logger = getLogger();
    }

    @Override
    public void onEnable() {
        setting = new Setting();
        logger.info("开始加载插件");
        afkTimer = new AfkTimer();
        regListener(afkTimer);
        if (setting.antiAfkFishing) regListener(new FishListener());
        if (setting.sleepingIgnored) regListener(new SleepListener());
        PluginCommand pc = getCommand("moeafk");
        if (pc != null){
            pc.setExecutor(mainCommand = new CommandManage());
        }
        regListener(new AfkListener());
        logger.info("加载完成,当前在线" + afkTimer.map.size() + "最大挂机时间" + setting.maximumTime);
    }

    @Override
    public void onDisable() {
        //注销注册玩家加入服务器事件
        unregListener();
        afkTimer.getMap().forEach((uuid,afkStaus) -> {
            afkStaus.setAfk(false);
        });
        afkTimer.stop();

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
}
