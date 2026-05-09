package info.x2a.soulshards.core.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;

public interface RecipeSerde<T extends Container & RecipeInput> extends Recipe<T> {
    void setId(ResourceLocation id);
}
