package info.x2a.soulshards.core.registry;

import com.google.gson.reflect.TypeToken;
import dev.architectury.registry.registries.RegistrySupplier;
import info.x2a.soulshards.SoulShards;
import info.x2a.soulshards.block.BlockHallowedFire;
import info.x2a.soulshards.block.BlockSoulCage;
import info.x2a.soulshards.block.BlockCursedFire;
import info.x2a.soulshards.block.TileEntitySoulCage;
import info.x2a.soulshards.core.util.GsonRecipeSerializer;
import info.x2a.soulshards.core.recipe.CursingRecipe;
import info.x2a.soulshards.core.util.EnchantmentSoulStealer;
import info.x2a.soulshards.item.ItemQuartzAndSteel;
import info.x2a.soulshards.item.ItemSoulShard;
import info.x2a.soulshards.item.ItemVileSword;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class RegistrarSoulShards {

    public static RegistrySupplier<BlockSoulCage> SOUL_CAGE;
    public static RegistrySupplier<BlockCursedFire> CURSED_FIRE;
    public static RegistrySupplier<BlockHallowedFire> HALLOWED_FIRE;

    public static RegistrySupplier<BlockEntityType<TileEntitySoulCage>> SOUL_CAGE_TE;

    public static RegistrySupplier<ItemSoulShard> SOUL_SHARD;
    public static RegistrySupplier<ItemQuartzAndSteel> QUARTZ_AND_STEEL;
    //public static final RegistrySupplier<Item> VILE_SWORD; = new ItemVileSword();

    public static RegistrySupplier<Item> CORRUPTED_INGOT;
    public static RegistrySupplier<Item> CORRUPTED_ESSENCE;
    public static RegistrySupplier<Enchantment> SOUL_STEALER;

    public static RegistrySupplier<RecipeType<CursingRecipe>> CURSING_RECIPE;
    public static RegistrySupplier<RecipeSerializer<CursingRecipe>> CURSING_RECIPE_SERIALIZER;

    public static void registerBlocks() {
        CURSED_FIRE = SoulRegistries.BLOCKS.register(SoulShards.makeResource("cursed_fire"), BlockCursedFire::new);
        HALLOWED_FIRE = SoulRegistries.BLOCKS.register(SoulShards.makeResource("hallowed_fire"), BlockHallowedFire::new);
        SOUL_CAGE = SoulRegistries.BLOCKS.register(new ResourceLocation(SoulShards.MODID, "soul_cage"), BlockSoulCage::new);
        SOUL_CAGE_TE = SoulRegistries.BLOCK_ENTITIES.register(new ResourceLocation(SoulShards.MODID, "soul_cage"),
                () -> BlockEntityType.Builder.of(TileEntitySoulCage::new, SOUL_CAGE.get())
                        .build(null));
        SoulRegistries.BLOCKS.register();
        SoulRegistries.BLOCK_ENTITIES.register();
    }

    public static void registerRecipes() {
        CURSING_RECIPE = SoulRegistries.RECIPES.register(CursingRecipe.ID, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return "cursing";
            }
        });
        CURSING_RECIPE_SERIALIZER = SoulRegistries.RECIPE_SERIALIZERS.register(CursingRecipe.ID, () -> new GsonRecipeSerializer<>(TypeToken.get(CursingRecipe.class)));
        SoulRegistries.RECIPES.register();
        SoulRegistries.RECIPE_SERIALIZERS.register();
        SoulShards.Log.info("Recipes registered");
    }

    public static void registerItems() {
        var registry = SoulRegistries.ITEMS;
        registry.register(new ResourceLocation(SoulShards.MODID, "soul_cage"),
                () -> new BlockItem(SOUL_CAGE.get(),
                        new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
        QUARTZ_AND_STEEL = registry.register(SoulShards.makeResource("quartz_and_steel"), ItemQuartzAndSteel::new);
        SOUL_SHARD = registry.register(new ResourceLocation(SoulShards.MODID, "soul_shard"),
                ItemSoulShard::new);
        registry.register(new ResourceLocation(SoulShards.MODID, "vile_sword"), ItemVileSword::new);
        CORRUPTED_ESSENCE = registry.register(new ResourceLocation(SoulShards.MODID, "corrupted_essence"), () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
        CORRUPTED_INGOT = registry.register(new ResourceLocation(SoulShards.MODID, "corrupted_ingot"),
                () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
        registry.register(SoulShards.makeResource("vile_sword_base"), () -> new Item(new Item.Properties().stacksTo(1)
                                                                                                          .tab(CreativeModeTab.TAB_MISC)));
        registry.register(new ResourceLocation(SoulShards.MODID, "vile_dust"), () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
        SoulRegistries.ITEMS.register();
    }

    public static void registerEnchantments() {
        SOUL_STEALER = SoulRegistries.ENCHANTMENTS.register(new ResourceLocation(SoulShards.MODID, "soul_stealer"),
                EnchantmentSoulStealer::new);
        SoulRegistries.ENCHANTMENTS.register();
    }
}
