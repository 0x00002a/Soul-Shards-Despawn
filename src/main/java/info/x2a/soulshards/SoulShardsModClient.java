package info.x2a.soulshards;

import info.x2a.soulshards.core.network.NetworkMgr;
import info.x2a.soulshards.core.registry.RegistrarSoulShards;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public class SoulShardsModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SoulShards.afterLoad();
        if (SoulShards.IS_CLOTH_CONFIG_LOADED) {
            /*ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, env) -> {
                dispatcher.register(Commands.literal("soulshards").then(Commands.literal("config").executes((context) -> {
                    SoulShardsConfigScreen.popup();
                    return 1;
                })));
            });*/
        }
        BlockRenderLayerMap.INSTANCE.putBlock(RegistrarSoulShards.SOUL_CAGE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(RegistrarSoulShards.CURSED_FIRE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(RegistrarSoulShards.HALLOWED_FIRE, RenderType.cutout());
        NetworkMgr.initClient();
    }
}
