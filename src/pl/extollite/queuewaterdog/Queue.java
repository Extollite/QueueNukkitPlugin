package pl.extollite.queuewaterdog;

import cn.nukkit.Server;
import cn.nukkit.event.Listener;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.plugin.PluginBase;

import cn.nukkit.utils.TextFormat;
import pl.extollite.queuewaterdog.listener.EventListener;

import java.util.*;


public class Queue extends PluginBase implements Listener {
    private static Queue instance;

    public static Queue getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        instance = this;
        List<String> authors = this.getDescription().getAuthors();
        this.getLogger().info(TextFormat.DARK_GREEN + "Plugin by " + authors.get(0));

        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
        for(int x = -3; x <= 3; x++){
            for(int z = -3; z <= 3; z++){
                BaseFullChunk chunk = Server.getInstance().getDefaultLevel().getChunk(x, z);
                for(int coordX = 0; coordX < 16; coordX++){
                    for(int coordZ = 0; coordZ < 16; coordZ++){
                        if(chunk != null)
                            chunk.setBiomeId(coordX, coordZ, 9);
                    }
                }
            }
        }
    }
}
