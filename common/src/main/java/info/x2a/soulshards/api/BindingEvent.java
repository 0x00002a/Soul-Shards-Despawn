package info.x2a.soulshards.api;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;

public class BindingEvent {

    public static final Event<NewBinding> NEW_BINDINGS = EventFactory.of(
            (listeners) -> (entity, binding) -> {
                for (NewBinding newBinding : listeners) {
                    InteractionResultHolder<IBinding> currentResult = newBinding.onNewBinding(entity, binding);

                    if (currentResult.getResult() != InteractionResult.PASS) {
                        return currentResult;
                    }
                }

                return new InteractionResultHolder<>(InteractionResult.PASS, binding);
            }
    );

    public static final Event<GainSouls> GAIN_SOULS = EventFactory.of(
            (listeners) -> (entity, binding, amount) -> {
                int soulsGained = amount;

                for (GainSouls gainSouls : listeners) {
                    int newSoulsGained = gainSouls.getGainedSouls(entity, binding, amount);
                    soulsGained += newSoulsGained;
                }

                return soulsGained;
            }
    );

    public static final Event<GetEntityName> GET_ENTITY_ID = EventFactory.of(
            (listeners) -> (entity, currentName) -> {

                for (GetEntityName getEntityName : listeners) {
                    ResourceLocation ResourceLocation = getEntityName.getEntityName(entity, currentName);
                    if (ResourceLocation != null) return ResourceLocation;
                }

                return currentName;
            }
    );

    public interface NewBinding {
        InteractionResultHolder<IBinding> onNewBinding(LivingEntity entity, IBinding binding);
    }

    public interface GainSouls {
        int getGainedSouls(LivingEntity entity, IBinding binding, int amount);
    }

    public interface GetEntityName {
        ResourceLocation getEntityName(LivingEntity entity, ResourceLocation currentName);
    }
}
