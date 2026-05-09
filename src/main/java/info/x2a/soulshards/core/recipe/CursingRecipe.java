package info.x2a.soulshards.core.recipe;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import info.x2a.soulshards.SoulShards;
import info.x2a.soulshards.core.registry.RegistrarSoulShards;
import info.x2a.soulshards.core.util.RecipeSerde;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

class IdByCodecAdaptor implements JsonSerializer<Item>, JsonDeserializer<Item> {

    @Override
    public Item deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(jsonElement.getAsString()));
    }

    @Override
    public JsonElement serialize(Item item, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(BuiltInRegistries.ITEM.getKey(item).toString());
    }
}

public record CursingRecipe(Item input, Item result, int quantity) implements RecipeSerde<RecipeInput> {
    public static ResourceLocation ID = SoulShards.makeResource("cursing");

    public static final MapCodec<CursingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("input").forGetter(CursingRecipe::input),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("result").forGetter(CursingRecipe::result),
            Codec.INT.fieldOf("quantity").forGetter(CursingRecipe::quantity)
    ).apply(inst, CursingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CursingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.registry(Registries.ITEM), CursingRecipe::input,
                    ByteBufCodecs.registry(Registries.ITEM), CursingRecipe::result,
                    ByteBufCodecs.INT, CursingRecipe::quantity,
                    CursingRecipe::new
            );

    public Item input() {
        return input;
    }

    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeInput recipeInput, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return getResult();
    }

    public ItemStack getResult() {
        var stack = new ItemStack(result);
        stack.setCount(quantity);
        return stack;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(null, Ingredient.of(input.getDefaultInstance()));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RegistrarSoulShards.CURSING_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RegistrarSoulShards.CURSING_RECIPE;
    }

}
