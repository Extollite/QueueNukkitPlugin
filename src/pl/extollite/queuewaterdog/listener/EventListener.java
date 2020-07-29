package pl.extollite.queuewaterdog.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryOpenEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.server.DataPacketSendEvent;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.StartGamePacket;

public class EventListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDataPacket(DataPacketSendEvent event){
        if(event.getPacket() instanceof StartGamePacket){
            if(((StartGamePacket) event.getPacket()).dimension == Level.DIMENSION_OVERWORLD){
                StartGamePacket packet = (StartGamePacket) event.getPacket();
                event.setCancelled();
                packet.dimension = Level.DIMENSION_THE_END;
                event.getPlayer().dataPacket(packet);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(event.getPlayer().isOp() || event.getPlayer().hasPermission("queue.admin"))
            return;
        event.setCancelled();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.setJoinMessage("");
        Player player = event.getPlayer();
        player.setGamemode(Player.SPECTATOR);
        player.setSpawn(new Vector3(0, 10, 0));
        if(event.getPlayer().isOp() || event.getPlayer().hasPermission("queue.admin"))
            return;
        event.getPlayer().setImmobile();
    }

    @EventHandler
    public void onInventory(InventoryOpenEvent event){
        event.setCancelled();
    }

    @EventHandler
    public void onChat(PlayerChatEvent event){
        event.setCancelled();
    }

    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent event){
        if(event.getPlayer().isOp() || event.getPlayer().hasPermission("queue.admin"))
            return;
        event.setCancelled();
    }
}
