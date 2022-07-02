package cn.whiteg.moeAfk;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

public class Setting {
    final int VER = 3;
    public FileConfiguration config;
    public boolean debug = false;
    public int maxPlayer = 0;
    public int maximumTime = 120;
    public boolean sleepingIgnored;
    public boolean antiAfkFishing;
    public boolean affectsSpawning;


    public Setting() {
        final Logger logger = MoeAfk.logger;
        final MoeAfk plugin = MoeAfk.plugin;
        final File file = new File(MoeAfk.plugin.getDataFolder(),"config.yml");
        config = YamlConfiguration.loadConfiguration(file);
        if (config.getInt("ver") != VER){
            logger.info("更新配置文件");
            plugin.saveResource("config.yml",true);
            config.set("ver",VER);
            final FileConfiguration newcon = YamlConfiguration.loadConfiguration(file);
            Set<String> keys = newcon.getKeys(true);
            for (String k : keys) {
                if (config.isSet(k)) continue;
                config.set(k,newcon.get(k));
                logger.info("在配置文件新增值: " + k);
            }
            try{
                config.save(file);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        debug = config.getBoolean("debug",debug);
        maxPlayer = config.getInt("maxPlayer",maxPlayer);
        maximumTime = config.getInt("MaximumTime",maximumTime);
        sleepingIgnored = config.getBoolean(" SleepingIgnored");
        antiAfkFishing = config.getBoolean(" AntiAfkFishing");
        affectsSpawning = config.getBoolean(" AffectsSpawning");
    }

}
