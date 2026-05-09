package info.x2a.soulshards;

import com.google.gson.reflect.TypeToken;
import info.x2a.soulshards.core.EventHandler;
import info.x2a.soulshards.core.config.ConfigClient;
import info.x2a.soulshards.core.config.ConfigServer;
import info.x2a.soulshards.core.data.Tier;
import info.x2a.soulshards.core.network.NetworkMgr;
import info.x2a.soulshards.core.network.message.ConfigUpdate;
import info.x2a.soulshards.core.registry.RegistrarSoulShards;
import info.x2a.soulshards.core.util.JsonResource;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;

public class SoulShards implements ModInitializer {
    public static final String MODID = "soulshards";
    public static final Logger Log = LogManager.getLogger("Soul Shards Despawn");

    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().toAbsolutePath().normalize();
    private static final JsonResource<ConfigServer> CONFIG_SERVER_RES = new JsonResource<>(new File(CONFIG_PATH.toString(), MODID + "/server.json"), new ConfigServer(), TypeToken.get(ConfigServer.class));

    private static final JsonResource<ConfigClient> CONFIG_CLIENT_RES = new JsonResource<>(
            new File(".minecraft/config", MODID + "/client.json"),
            new ConfigClient(), TypeToken.get(ConfigClient.class));
    public static ConfigServer CONFIG_SERVER;
    public static ConfigClient CONFIG_CLIENT;
    public static EntityDataAccessor<Boolean> cageBornTag;
    public static GameRules.Key<GameRules.BooleanValue> allowCageSpawns;
    public static boolean IS_CLOTH_CONFIG_LOADED;
    public static final String BOSS_TAG = "c:bosses";

    public static boolean isBoss(LivingEntity creature) {
        return creature.getTags().contains(BOSS_TAG);
    }

    public static void afterLoad() {
        Log.info("Soul Shards Despawn rises once again");
        IS_CLOTH_CONFIG_LOADED = FabricLoader.getInstance().isModLoaded("cloth-config") || FabricLoader.getInstance().isModLoaded("cloth_config");
        CONFIG_SERVER = CONFIG_SERVER_RES.get();
        CONFIG_CLIENT = CONFIG_CLIENT_RES.get();
    }

    public static void saveClient() {
        CONFIG_CLIENT_RES.save();
    }

    public static void saveServer() {
        CONFIG_SERVER_RES.save();
    }

    public static ResourceLocation makeResource(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }

    public static void initNetwork() {
        NetworkMgr.init();
    }

    public static MutableComponent translate(String fmt, Object... args) {
        return MutableComponent.create(new TranslatableContents(fmt, null, args));
    }

    public static MutableComponent translate(String fmt) {
        return translate(fmt, TranslatableContents.NO_ARGS);
    }

    public static void initCommon() {
        afterLoad();
        Tier.readTiers();
        ConfigServer.handleMultiblock();
        // give player the config on join
        ServerPlayerEvents.JOIN.register(p -> {
            if (!p.isLocalPlayer() && !p.getServer().isSingleplayer()) {
                ServerPlayNetworking.send(p, new ConfigUpdate(CONFIG_SERVER));
            }
        });

        allowCageSpawns = GameRuleRegistry.register("allowCageSpawns", GameRules.Category.SPAWNING,
                GameRuleFactory.createBooleanRule(true));
        RegistrarSoulShards.init();
        EventHandler.init();
        initNetwork();
    }

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            initClient();
        } else {
            initServer();
        }

    }

    static void initServer() {
        SoulShards.initCommon();
        NetworkMgr.initServer();
    }

    static void initClient() {
        SoulShards.initCommon();
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
