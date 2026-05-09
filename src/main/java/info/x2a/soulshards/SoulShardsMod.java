package info.x2a.soulshards;

import info.x2a.soulshards.core.network.NetworkMgr;
import net.fabricmc.api.ModInitializer;

public class SoulShardsMod implements ModInitializer {

    @Override
    public void onInitialize() {
        SoulShards.initCommon();
        NetworkMgr.initServer();
    }
}
