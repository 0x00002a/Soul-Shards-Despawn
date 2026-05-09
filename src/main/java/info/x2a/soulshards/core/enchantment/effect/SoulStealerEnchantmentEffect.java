package info.x2a.soulshards.core.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record SoulStealerEnchantmentEffect(LevelBasedValue level) implements EnchantmentEntityEffect {
    public static final MapCodec<SoulStealerEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(SoulStealerEnchantmentEffect::level)
            ).apply(instance, SoulStealerEnchantmentEffect::new)
    );

    @Override
    public void apply(ServerLevel serverLevel, int level, EnchantedItemInUse enchantedItemInUse, Entity entity, Vec3 pos) {


    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
