package info.tehnut.soulshards.compat.waila;

import info.tehnut.soulshards.SoulShards;
import info.tehnut.soulshards.block.TileEntitySoulCage;
import info.tehnut.soulshards.core.data.Binding;
import mcp.mobius.waila.api.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class SoulShardsWailaPlugin implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        registrar.addComponent(new IEntityComponentProvider() {
            @Override
            public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
                if (accessor.getEntity().getEntityData().get(SoulShards.cageBornTag))
                    tooltip.addLine(MutableComponent.create(new TranslatableContents("tooltip.soulshards.cage_born")));
            }
        }, TooltipPosition.BODY, LivingEntity.class);

        registrar.addBlockData((data, block, config) -> {
            var binding = ((TileEntitySoulCage) block).getBinding();
            if (binding != null)
                data.put("binding", binding.serializeNBT());
        }, TileEntitySoulCage.class);

        registrar.addComponent(new IBlockComponentProvider() {
            @Override
            public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
                if (!accessor.getServerData().contains("binding"))
                    return;

                Binding binding = new Binding(accessor.getServerData().getCompound("binding"));

                if (binding.getBoundEntity() != null) {
                    var entityEntry = Registry.ENTITY_TYPE.get(binding.getBoundEntity());
                    if (entityEntry != null)
                        tooltip.addLine(MutableComponent.create(new TranslatableContents("tooltip.soulshards.bound",
                                entityEntry.getDescription())));
                    else
                        tooltip.addLine(MutableComponent.create(new TranslatableContents("tooltip.soulshards.bound",
                                binding.getBoundEntity().toString())).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                }

                tooltip.addLine(MutableComponent.create(new TranslatableContents("tooltip.soulshards.tier",
                        binding.getTier().getIndex())));
            }
        }, TooltipPosition.BODY, TileEntitySoulCage.class);
    }
}
