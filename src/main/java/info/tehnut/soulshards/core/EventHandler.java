package info.tehnut.soulshards.core;

import info.tehnut.soulshards.SoulShards;
import info.tehnut.soulshards.api.BindingEvent;
import info.tehnut.soulshards.api.ISoulWeapon;
import info.tehnut.soulshards.core.data.Binding;
import info.tehnut.soulshards.core.data.MultiblockPattern;
import info.tehnut.soulshards.core.data.Tier;
import info.tehnut.soulshards.item.ItemSoulShard;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class EventHandler {

    public static void init() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            MultiblockPattern pattern = ConfigSoulShards.getMultiblock();

            ItemStack held = player.getMainHandItem();
            if (!ItemStack.isSame(pattern.getCatalyst(), held))
                return InteractionResult.PASS;

            BlockState worldState = world.getBlockState(hitResult.getBlockPos());
            if (!pattern.isOriginBlock(worldState))
                return InteractionResult.PASS;

            InteractionResultHolder<Set<BlockPos>> match = pattern.match(world, hitResult.getBlockPos());
            if (match.getResult() == InteractionResult.FAIL)
                return match.getResult();

            match.getObject().forEach(matchedPos -> world.destroyBlock(matchedPos, false));
            held.shrink(1);
            ItemStack shardStack = new ItemStack(RegistrarSoulShards.SOUL_SHARD);
            if (!player.getInventory().add(shardStack)) {
                Containers.dropItemStack(world, player.getX(), player.getY(), player.getZ(), shardStack);
            }
            return InteractionResult.SUCCESS;
        });
    }

    public static void onEntityDeath(LivingEntity killed, DamageSource source) {
        if (!SoulShards.CONFIG.getBalance().allowBossSpawns() && !SoulShards.isBoss(killed))
            return;

        if (!SoulShards.CONFIG.getBalance().countCageBornForShard() && killed.getEntityData().get(SoulShards.cageBornTag))
            return;

        if (source.getEntity() instanceof Player) {
            var player = (Player) source.getEntity();
            var entityId = getEntityId(killed);

            if (!SoulShards.CONFIG.getEntityList().isEnabled(entityId))
                return;

            ItemStack shardStack = getFirstShard(player, entityId);
            if (shardStack.isEmpty())
                return;

            ItemSoulShard shard = (ItemSoulShard) shardStack.getItem();
            Binding binding = shard.getBinding(shardStack);
            if (binding == null)
                binding = getNewBinding(killed);

            if (binding == null)
                return;

            var mainHand = player.getMainHandItem();
            int soulsGained = 1 + EnchantmentHelper.getItemEnchantmentLevel(RegistrarSoulShards.SOUL_STEALER, mainHand);
            if (mainHand.getItem() instanceof ISoulWeapon)
                soulsGained += ((ISoulWeapon) mainHand.getItem()).getSoulBonus(mainHand, player, killed);

            soulsGained = BindingEvent.GAIN_SOULS.invoker().getGainedSouls(killed, binding, soulsGained);

            if (binding.getBoundEntity() == null)
                binding.setBoundEntity(entityId);

            if (binding.getOwner() == null)
                binding.setOwner(player.getGameProfile().getId());

            shard.updateBinding(shardStack, binding.addKills(soulsGained));
        }
    }

    private static ItemStack getFirstShard(Player player, ResourceLocation entityId) {
        // Checks the offhand first
        ItemStack shardItem = player.getOffhandItem();
        // If offhand isn't a shard, loop through the hotbar
        if (shardItem.isEmpty() || !(shardItem.getItem() instanceof ItemSoulShard)) {
            for (int i = 0; i < 9; i++) {
                shardItem = player.getInventory().getItem(i);
                if (!shardItem.isEmpty() && shardItem.getItem() instanceof ItemSoulShard) {
                    if (checkBinding(entityId, shardItem)) return shardItem;
                }
            }
        } else { // If offhand is a shard, check it it
            if (checkBinding(entityId, shardItem))
                return shardItem;
        }

        return ItemStack.EMPTY; // No shard found
    }

    private static boolean checkBinding(ResourceLocation entityId, ItemStack shardItem) {
        Binding binding = ((ItemSoulShard) shardItem.getItem()).getBinding(shardItem);

        // If there's no binding or no bound entity, this is a valid shard
        if (binding == null || binding.getBoundEntity() == null)
            return true;

        // If there is a bound entity and we're less than the max kills, this is a valid shard
        return binding.getBoundEntity().equals(entityId) && binding.getKills() < Tier.maxKills;

    }

    private static ResourceLocation getEntityId(LivingEntity entity) {
        ResourceLocation id = Registry.ENTITY_TYPE.getKey(entity.getType());
        return BindingEvent.GET_ENTITY_ID.invoker().getEntityName(entity, id);
    }

    private static Binding getNewBinding(LivingEntity entity) {
        Binding binding = new Binding(null, 0);
        return (Binding) BindingEvent.NEW_BINDINGS.invoker().onNewBinding(entity, binding).getObject();
    }
}
