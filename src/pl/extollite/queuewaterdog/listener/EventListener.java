package pl.extollite.queuewaterdog.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryOpenEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.event.server.DataPacketSendEvent;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.ScriptCustomEventPacket;
import cn.nukkit.network.protocol.SetLocalPlayerAsInitializedPacket;
import cn.nukkit.network.protocol.StartGamePacket;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class EventListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDataPacket(DataPacketSendEvent event){
        if(event.getPacket().pid() == StartGamePacket.NETWORK_ID){
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
    public void onPacketReceive(DataPacketReceiveEvent event){
        if(event.getPacket().pid() == SetLocalPlayerAsInitializedPacket.NETWORK_ID){
            SetLocalPlayerAsInitializedPacket packet = (SetLocalPlayerAsInitializedPacket)event.getPacket();
            Player player = event.getPlayer();
            ScriptCustomEventPacket pk = new ScriptCustomEventPacket();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream a = new DataOutputStream(out);
            try {
                a.writeUTF("UUID");
                a.writeUTF(event.getPlayer().getUniqueId().toString());
                pk.eventName = "bungeecord:main";
                pk.eventData = out.toByteArray();
                event.getPlayer().dataPacket(pk);
            } catch (Exception e) {
                //ignore
            }
        }
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

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        event.setQuitMessage("");
        event.setAutoSave(false);
    }
}
