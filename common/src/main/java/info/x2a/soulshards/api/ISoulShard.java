package info.x2a.soulshards.api;


import net.minecraft.world.item.ItemStack;

public interface ISoulShard {

    IBinding getBinding(ItemStack stack);
}
