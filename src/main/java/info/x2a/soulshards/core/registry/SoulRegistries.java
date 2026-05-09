package info.x2a.soulshards.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SoulRegistries {
    public static final Registry<Item> ITEMS = BuiltInRegistries.ITEM;
    public static final Registry<Block> BLOCKS = BuiltInRegistries.BLOCK;
    public static final Registry<?> ENCHANTMENTS = BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE;

    public static final Registry<BlockEntityType<?>> BLOCK_ENTITIES = BuiltInRegistries.BLOCK_ENTITY_TYPE;

    public static final Registry<RecipeSerializer<?>> RECIPE_SERIALIZERS = BuiltInRegistries.RECIPE_SERIALIZER;
    public static final Registry<RecipeType<?>> RECIPES = BuiltInRegistries.RECIPE_TYPE;

    public static final Registry<CreativeModeTab> CREATIVE_TABS = BuiltInRegistries.CREATIVE_MODE_TAB;

    public static void init() {
    }
}
