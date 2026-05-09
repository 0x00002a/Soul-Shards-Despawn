package info.x2a.soulshards.core.util;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;

public interface RecipeSerde<T extends RecipeInput> extends Recipe<T> {
}
