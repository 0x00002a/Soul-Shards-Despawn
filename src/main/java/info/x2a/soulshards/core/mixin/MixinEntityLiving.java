package info.x2a.soulshards.core.mixin;

import info.x2a.soulshards.core.EventHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinEntityLiving {

    @Shadow
    protected boolean dead;

    @Inject(method = "die", at = @At("HEAD"))
    private void onDeathEvent(DamageSource damageSource, CallbackInfo callbackInfo) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof Player || dead)
            return;

        EventHandler.onEntityDeath(entity, damageSource);
    }

    @Inject(method = "shouldDropExperience", at = @At("RETURN"), cancellable = true)
    private void shouldDropXp(CallbackInfoReturnable<Boolean> cir) {
        var me = (LivingEntity) (Object) this;
        if (!cir.getReturnValue()) {
            return;
        }
        if (!EventHandler.shouldDropXp(me)) {
            cir.setReturnValue(false);
        }
    }
}
