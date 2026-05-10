package info.x2a.soulshards.core.registry;

import com.mojang.serialization.MapCodec;
import info.x2a.soulshards.SoulShards;
import info.x2a.soulshards.api.IShardTier;
import info.x2a.soulshards.block.BlockCursedFire;
import info.x2a.soulshards.block.BlockHallowedFire;
import info.x2a.soulshards.block.BlockSoulCage;
import info.x2a.soulshards.block.TileEntitySoulCage;
import info.x2a.soulshards.core.data.Binding;
import info.x2a.soulshards.core.data.Tier;
import info.x2a.soulshards.core.recipe.CursingRecipe;
import info.x2a.soulshards.item.ItemQuartzAndSteel;
import info.x2a.soulshards.item.ItemSoulShard;
import info.x2a.soulshards.item.ItemVileSword;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

public class RegistrarSoulShards {

    public static BlockSoulCage SOUL_CAGE;
    public static BlockCursedFire CURSED_FIRE;
    public static BlockHallowedFire HALLOWED_FIRE;

    public static BlockEntityType<TileEntitySoulCage> SOUL_CAGE_TE;

    public static ItemSoulShard SOUL_SHARD;
    public static ItemQuartzAndSteel QUARTZ_AND_STEEL;
    public static Item VILE_SWORD_HAND_MODEL;

    public static Item CORRUPTED_INGOT;
    public static Enchantment SOUL_STEALER;
    public static CreativeModeTab SOUL_SHARDS_TAB;
    public static ResourceKey<CreativeModeTab> SOUL_SHARDS_TAB_KEY;

    public static void init() {
        var create_mode_tab = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), SoulShards.makeResource("item_group"));
        SOUL_SHARDS_TAB_KEY = create_mode_tab;
        SOUL_SHARDS_TAB =
                FabricItemGroup.builder().icon(() -> new ItemStack(SOUL_SHARD)).title(Component.literal("Soul Shards")).displayItems(
                                (params, output) -> {
                                    for (IShardTier tier : Tier.INDEXED) {
                                        var shard = SOUL_SHARD;
                                        var stack = new ItemStack(shard);
                                        var binding = new Binding(null, tier.getKillRequirement());
                                        shard.updateBinding(stack, binding);
                                        output.accept(stack);
                                    }
                                }
                        )
                        .build();
        Registry.register(SoulRegistries.CREATIVE_TABS, create_mode_tab, SOUL_SHARDS_TAB);


        registerRecipes();
        registerBlocks();
        registerItems();
    }

    public static <T extends Item> T registerAndAddCreative(Registry<Item> reg,
                                                            ResourceLocation id,
                                                            T item) {
        //var supplier = reg.register(id, prov);
        ItemGroupEvents.modifyEntriesEvent(SOUL_SHARDS_TAB_KEY).register(itemGroup -> {
            itemGroup.accept(item);
        });
        return Registry.register(reg, id, item);

    }

    public static RecipeType<CursingRecipe> CURSING_RECIPE;
    public static RecipeSerializer<CursingRecipe> CURSING_RECIPE_SERIALIZER;

    public static void registerBlocks() {
        CURSED_FIRE = Registry.register(SoulRegistries.BLOCKS, SoulShards.makeResource("cursed_fire"), new BlockCursedFire());
        HALLOWED_FIRE = Registry.register(SoulRegistries.BLOCKS, SoulShards.makeResource("hallowed_fire"), new BlockHallowedFire());
        SOUL_CAGE = Registry.register(SoulRegistries.BLOCKS, SoulShards.makeResource("soul_cage"), new BlockSoulCage());
        SOUL_CAGE_TE = Registry.register(SoulRegistries.BLOCK_ENTITIES, SoulShards.makeResource("soul_cage"),
                BlockEntityType.Builder.of(TileEntitySoulCage::new, SOUL_CAGE)
                        .build(null));
    }

    private static <T extends Item> T regItem(String id, T source) {
        return registerAndAddCreative(SoulRegistries.ITEMS, SoulShards.makeResource(id), source);
    }


    public static void registerRecipes() {
        CURSING_RECIPE = Registry.register(SoulRegistries.RECIPES, CursingRecipe.ID, new RecipeType<>() {
            @Override
            public String toString() {
                return "cursing";
            }
        });
        CURSING_RECIPE_SERIALIZER = Registry.register(SoulRegistries.RECIPE_SERIALIZERS, CursingRecipe.ID, new RecipeSerializer<CursingRecipe>() {

            @Override
            public @NotNull MapCodec<CursingRecipe> codec() {
                return CursingRecipe.CODEC;
            }

            @Override
            public @NotNull StreamCodec<RegistryFriendlyByteBuf, CursingRecipe> streamCodec() {
                return CursingRecipe.STREAM_CODEC;
            }
        });
        SoulShards.Log.info("Recipes registered");
    }

    public static void registerItems() {
        regItem("soul_cage", new BlockItem(SOUL_CAGE, new Item.Properties()));
        regItem("vile_sword", new ItemVileSword());
        regItem("corrupted_essence", new Item(new Item.Properties()));
        SOUL_SHARD = regItem("soul_shard", new ItemSoulShard());
        CORRUPTED_INGOT = regItem("corrupted_ingot",
                new Item(new Item.Properties()));
        regItem("vile_sword_base", new Item(new Item.Properties().stacksTo(1)));
        QUARTZ_AND_STEEL = regItem("quartz_and_steel", new ItemQuartzAndSteel());
        VILE_SWORD_HAND_MODEL = Registry.register(BuiltInRegistries.ITEM, "vile_sword_in_hand", new Item(new Item.Properties()));
    }
}
