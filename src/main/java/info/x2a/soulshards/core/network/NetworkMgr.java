package info.x2a.soulshards.core.network;

import info.x2a.soulshards.SoulShards;
import info.x2a.soulshards.core.config.ConfigServer;
import info.x2a.soulshards.core.network.message.ConfigUpdate;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class NetworkMgr {
    public static void sendConfig(ConfigServer config) {
        ClientPlayNetworking.send(new ConfigUpdate(config));
    }

    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(ConfigUpdate.TYPE, ((payload, context) -> {
            // we are clientside
            SoulShards.CONFIG_SERVER.balance = payload.balance();
            SoulShards.CONFIG_SERVER.entityList = payload.entityList();
        }));
    }

    public static void initServer() {
        ServerPlayNetworking.registerGlobalReceiver(ConfigUpdate.TYPE, ((payload, context) -> {
            var player = context.player();
            var server = player.getServer();
            assert server != null;
            if (server.getPlayerList().isOp(player.getGameProfile())) {
                server.getPlayerList()
                        .getPlayers()
                        .stream()
                        .filter(p -> !p.getUUID().equals(player.getUUID()))
                        .forEach(p -> {
                            ServerPlayNetworking.send(p, payload);
                        });
                SoulShards.CONFIG_SERVER.balance = payload.balance();
                SoulShards.CONFIG_SERVER.entityList = payload.entityList();
                SoulShards.saveServer();
            }
        }));

    }
}
