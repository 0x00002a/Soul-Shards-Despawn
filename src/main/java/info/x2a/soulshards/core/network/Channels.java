package info.x2a.soulshards.core.network;


import info.x2a.soulshards.SoulShards;
import info.x2a.soulshards.core.network.message.ConfigUpdate;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class Channels {

    public static void init() {
        PayloadTypeRegistry.configurationC2S().register(ConfigUpdate.TYPE, ConfigUpdate.CODEC);
        PayloadTypeRegistry.configurationS2C().register(ConfigUpdate.TYPE, ConfigUpdate.CODEC);
        SoulShards.Log.info("Networking initialised");
    }
}
