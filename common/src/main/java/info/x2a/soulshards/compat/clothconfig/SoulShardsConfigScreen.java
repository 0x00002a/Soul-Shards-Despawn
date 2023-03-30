package info.x2a.soulshards.compat.clothconfig;


import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.utils.GameInstance;
import info.x2a.soulshards.SoulShards;
import info.x2a.soulshards.core.config.ConfigClient;
import info.x2a.soulshards.core.network.Client;
import info.x2a.soulshards.core.config.ConfigServer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;


public class SoulShardsConfigScreen {
    private final Screen popup;
    private static final ConfigServer DEFAULT_CONFIG = new ConfigServer();
    private static final ConfigClient DEFAULT_CONFIG_CLI = new ConfigClient();

    private static boolean playerHasPerms() {
        if (GameInstance.getClient().player != null) {
            return GameInstance.getClient().player.hasPermissions(4);
        } else {
            return true;
        }
    }

    public SoulShardsConfigScreen(@Nullable Screen parent) {
        var builder = ConfigBuilder
                .create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("itemGroup.soulshards"))
                .setSavingRunnable(this::onSave);
        var entry = builder.entryBuilder();
        var hasPerms = true;
        if (GameInstance.getClient().player != null) {
            hasPerms = GameInstance.getClient().player.hasPermissions(4);
        }
        if (hasPerms) { // OP perms
            addServerCfg(builder);
        }
        var client = SoulShards.CONFIG_CLIENT;
        builder.getOrCreateCategory(Component.translatable("category.soulshards.client"))
               .addEntry(entry.startBooleanToggle(Component.translatable("option.soulshards.display_durability_bar"), client.displayDurabilityBar())
                              .setTooltip(Component.translatable("tooltip.soulshards.display_durability_bar"))
                              .setSaveConsumer(v -> client.displayDurabilityBar = v)
                              .setDefaultValue(DEFAULT_CONFIG_CLI.displayDurabilityBar)
                       .build());
        popup = builder.build();
    }

    private void addServerCfg(ConfigBuilder builder) {
        var cfg = SoulShards.CONFIG_SERVER;
        var balance = cfg.getBalance();
        var entry = builder.entryBuilder();
        var entities = SoulShards.CONFIG_SERVER.getEntityList();
        var entityCat = entry.startSubCategory(Component.translatable("category.soulshards.entity_list"));
        entityCat.add(entry.startStrList(Component.translatable("option.soulshards.disabled_entities"), entities.disabledIds())
                           .setDefaultValue(DEFAULT_CONFIG.getEntityList()
                                                          .disabledIds())
                           .setSaveConsumer(v -> SoulShards.CONFIG_SERVER.entityList = new ConfigServer.ConfigEntityList(v))
                           .setTooltip(Component.translatable("tooltip.soulshards.disabled_entities"))
                .build());
        builder.getOrCreateCategory(Component.translatable("category.soulshards.balance"))
               .addEntry(entry.startBooleanToggle(Component.translatable("option.soulshards.allow_shard_combine"), balance.allowShardCombination)
                              .setTooltip(Component.translatable("tooltip.soulshards.allow_shard_combine"))
                              .setDefaultValue(DEFAULT_CONFIG.getBalance().allowShardCombination)
                              .setSaveConsumer(v -> balance.allowShardCombination = v)
                       .build())
               .addEntry(entry.startBooleanToggle(Component.translatable("option.soulshards.allow_boss_spawns"), balance.allowBossSpawns)
                              .setDefaultValue(DEFAULT_CONFIG.getBalance().allowBossSpawns)
                              .setSaveConsumer(v -> balance.allowBossSpawns = v)
                              .setTooltip(Component.translatable("tooltip.soulshards.allow_boss_spawns"))
                       .build())
               .addEntry(entry.startIntField(Component.translatable("option.soulshards.absorb_bonus"), balance.absorptionBonus)
                              .setDefaultValue(DEFAULT_CONFIG.getBalance().absorptionBonus)
                              .setSaveConsumer(v -> balance.absorptionBonus = v)
                              .setTooltip(Component.translatable("tooltip.soulshards.absorb_bonus"))
                       .build())
               .addEntry(entry.startBooleanToggle(Component.translatable("option.soulshards.count_cage_born"), balance.countCageBornForShard)
                              .setTooltip(Component.translatable("tooltip.soulshards.count_cage_born"))
                              .setSaveConsumer(v -> balance.countCageBornForShard = v)
                              .setDefaultValue(DEFAULT_CONFIG.getBalance().countCageBornForShard)
                       .build())
               .addEntry(entry.startBooleanToggle(Component.translatable("option.soulshards.require_owner_online"), balance.requireOwnerOnline)
                              .setSaveConsumer(v -> balance.requireOwnerOnline = v)
                              .setTooltip(Component.translatable("tooltip.soulshards.require_owner_online"))
                              .setDefaultValue(DEFAULT_CONFIG.getBalance().requireOwnerOnline)
                       .build())
               .addEntry(entry.startBooleanToggle(Component.translatable("option.soulshards.require_redstone"), balance.requireRedstoneSignal)
                              .setDefaultValue(DEFAULT_CONFIG.getBalance().requireRedstoneSignal)
                              .setSaveConsumer(v -> balance.requireRedstoneSignal = v)
                              .setTooltip(Component.translatable("tooltip.soulshards.require_redstone"))
                       .build())
               .addEntry(entry.startIntField(Component.translatable("option.soulshards.spawn_cap"), balance.spawnCap)
                              .setDefaultValue(DEFAULT_CONFIG.getBalance().spawnCap)
                              .setSaveConsumer(v -> balance.spawnCap = v)
                              .setTooltip(Component.translatable("tooltip.soulshards.spawn_cap"))
                       .build())
               .addEntry(entityCat.build())
        ;
    }

    public static void popup() {
        ClientTickEvent.CLIENT_POST.register(new ClientTickEvent.Client() {
            @Override
            public void tick(Minecraft ev) {
                var screen = new SoulShardsConfigScreen(null).screen();
                ev.forceSetScreen(screen);
                ClientTickEvent.CLIENT_POST.unregister(this);
            }
        });
    }

    public Screen screen() {
        return popup;
    }

    private void onSave() {
        SoulShards.saveClient();
        if (GameInstance.getClient().player != null && playerHasPerms()) {
            Client.sendConfig(SoulShards.CONFIG_SERVER);
        }
    }
}
