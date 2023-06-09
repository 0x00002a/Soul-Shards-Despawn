package info.x2a.soulshards.compat.clothconfig;


import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.utils.GameInstance;
import info.x2a.soulshards.SoulShards;
import info.x2a.soulshards.core.config.ConfigClient;
import info.x2a.soulshards.core.network.Client;
import info.x2a.soulshards.core.config.ConfigServer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.AbstractFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.IntFieldBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;


public class SoulShardsConfigScreen {
    private class Helper<Conf> {
        private final ConfigBuilder builder;
        private final ConfigEntryBuilder entry;
        private final Conf default_;
        private final Conf current;

        private Helper(ConfigBuilder builder, Conf aDefault, Conf current) {
            this.builder = builder;
            this.entry = builder.entryBuilder();
            default_ = aDefault;
            this.current = current;
        }

        @FunctionalInterface
        private interface BuilderMaker<Builder, T> {
            Builder make(Component name, T curr);
        }

        @Nullable
        private <Builder extends AbstractFieldBuilder<T, ?, S>, T, S extends AbstractFieldBuilder<T, ?, S>> S mkBuilder(String name, String target, BuilderMaker<Builder, T> mkBuilder) {
            try {
                var field = default_.getClass().getField(target);
                var currValue = (T) field.get(current);
                var defaultValue = (T) field.get(default_);
                return mkBuilder.make(Component.translatable("option.soulshards." + name), currValue)
                                .setSaveConsumer((value) -> {
                                    try {
                                        field.set(current, value);
                                    } catch (IllegalAccessException e) {
                                        SoulShards.Log.error(e);
                                    }
                                }).setDefaultValue(defaultValue)
                                .setTooltip(Component.translatable("tooltip.soulshards." + name));
            } catch (Exception e) {
                SoulShards.Log.error(e);
                return null;
            }
        }

        @Nullable
        public BooleanToggleBuilder bool(String name, String target) {
            return mkBuilder(name, target, (entry::startBooleanToggle));
        }

        @Nullable
        public IntFieldBuilder ient(String name, String target) {
            return mkBuilder(name, target, entry::startIntField);
        }
    }

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
        var entry = builder.entryBuilder();
        var entities = SoulShards.CONFIG_SERVER.getEntityList();
        var entityCat = entry.startSubCategory(Component.translatable("category.soulshards.entity_list"));
        entityCat.add(entry.startStrList(Component.translatable("option.soulshards.disabled_entities"), entities.disabledIds())
                           .setDefaultValue(DEFAULT_CONFIG.getEntityList()
                                                          .disabledIds())
                           .setSaveConsumer(v -> SoulShards.CONFIG_SERVER.entityList = new ConfigServer.ConfigEntityList(v))
                           .setTooltip(Component.translatable("tooltip.soulshards.disabled_entities"))
                .build());
        var helper = new Helper<>(builder, DEFAULT_CONFIG.balance, cfg.balance);
        try {
            builder.getOrCreateCategory(Component.translatable("category.soulshards.balance"))
                   .addEntry(helper.bool("allow_shard_combine", "allowShardCombination")
                           .build())
                   .addEntry(helper.bool("allow_boss_spawns", "allowBossSpawns")
                           .build())
                   .addEntry(helper.ient("absorb_bonus", "absorptionBonus")
                           .build())
                   .addEntry(helper.bool("count_cage_born", "countCageBornForShard")
                           .build())
                   .addEntry(helper.bool("require_owner_online", "requireOwnerOnline")
                           .build())
                   .addEntry(helper.bool("require_redstone", "requireRedstoneSignal")
                           .build())
                   .addEntry(helper.ient("spawn_cap", "spawnCap")
                           .build())
                   .addEntry(helper.bool("drop_xp", "dropExperience")
                           .build())
                   .addEntry(entityCat.build())
            ;
        } catch (Exception e) {
            SoulShards.Log.error(e);

        }
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
        } else if (GameInstance.getClient().player == null && GameInstance.getServer() == null) { // In menu
            SoulShards.saveServer();
        }
    }
}
