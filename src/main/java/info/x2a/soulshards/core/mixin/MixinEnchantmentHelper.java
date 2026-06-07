package info.x2a.soulshards.core.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import info.x2a.soulshards.SoulShards;
import info.x2a.soulshards.core.registry.RegistrarSoulShards;
import net.minecraft.core.Holder;
import net.minecraft.util.random.Weight;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.stream.Stream;

@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {
    static class EnchantmentWeightOverride extends EnchantmentInstance {

        public EnchantmentWeightOverride(Holder<Enchantment> holder, int i) {
            super(holder, i);
        }

        @Override
        public @NotNull Weight getWeight() {
            // weight of getting soul stealer on a cursed sword
            return Weight.of(60);
        }
    }

    @ModifyReturnValue(method = "getAvailableEnchantmentResults", at = @At("TAIL"))
    private static List<EnchantmentInstance> afterGetEnchantmentResults(List<EnchantmentInstance> original, int i, ItemStack itemStack, Stream<Holder<Enchantment>> stream) {
        if (!itemStack.is(RegistrarSoulShards.VILE_SWORD)) {
            return original;
        }
        for (int n = 0; n != original.size(); ++n) {
            var enchant = original.get(n);
            if (enchant.enchantment.is(SoulShards.SOUL_STEALER_TAG)) {
                original.set(n, new EnchantmentWeightOverride(enchant.enchantment, enchant.level));
            }
        }
        return original;
    }

}