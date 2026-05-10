package info.x2a.soulshards.item;

import info.x2a.soulshards.api.ISoulWeapon;
import info.x2a.soulshards.core.registry.RegistrarSoulShards;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class ItemVileSword extends SwordItem implements ISoulWeapon {

    public static final Tier MATERIAL_VILE = new MaterialVile();

    public ItemVileSword() {
        super(MATERIAL_VILE, new Item.Properties().attributes(SwordItem.createAttributes(MATERIAL_VILE, 3, -2.4F)));
    }

    @Override
    public int getSoulBonus(ItemStack stack, Player player, LivingEntity killedEntity) {
        return 2;
    }


    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("tooltip.soulshards.vile_sword"));
    }

    public static class MaterialVile implements Tier {

        private final Supplier<Ingredient> ingredient;

        public MaterialVile() {
            this.ingredient = () -> Ingredient.of(RegistrarSoulShards.CORRUPTED_INGOT);
        }

        @Override
        public int getUses() {
            return Tiers.IRON.getUses();
        }

        @Override
        public float getSpeed() {
            return Tiers.IRON.getSpeed();
        }

        @Override
        public float getAttackDamageBonus() {
            return Tiers.IRON.getAttackDamageBonus();
        }

        @Override
        public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
            return Tiers.IRON.getIncorrectBlocksForDrops();
        }

        @Override
        public int getEnchantmentValue() {
            return Tiers.IRON.getEnchantmentValue();
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return ingredient.get();
        }

    }
}
