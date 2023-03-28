package info.n4tomic.soulshards.core.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import info.n4tomic.soulshards.SoulShards;
import info.n4tomic.soulshards.core.data.Binding;
import info.n4tomic.soulshards.core.data.Tier;
import info.n4tomic.soulshards.item.ItemSoulShard;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    @Inject(method = "renderGuiItemDecorations", at = @At("RETURN"))
    private void renderShardFullness(Font textRenderer, ItemStack stack, int x, int y, String text,
                                     CallbackInfo ci) {
        if (!SoulShards.CONFIG.getClient().displayDurabilityBar())
            return;

        if (stack.isEmpty() || !(stack.getItem() instanceof ItemSoulShard))
            return;

        ItemSoulShard shard = (ItemSoulShard) stack.getItem();
        Binding binding = shard.getBinding(stack);

        if (binding == null || binding.getKills() >= Tier.maxKills)
            return;

        float current = (float) binding.getKills();
        float max = (float) Tier.maxKills;
        float percentage = current / max;
        int color = 0x9F63ED;

        RenderSystem.disableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.disableCull();
        RenderSystem.disableBlend();
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        prepareQuad(buffer, x + 2, y + 13, 13, 2, 0, 0, 0);
        prepareQuad(buffer, x + 2, y + 13, (int) (percentage * 13), 1, color >> 16 & 255, color >> 8 & 255, color & 255);
        Tesselator.getInstance().end();
        RenderSystem.enableBlend();
        RenderSystem.enableCull();
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
    }

    private static void prepareQuad(BufferBuilder buffer, int x, int y, int width, int height, int r, int g, int b) {
        buffer.vertex(x, y, 0.0D).color(r, g, b, 255).endVertex();
        buffer.vertex(x, y + height, 0.0D).color(r, g, b, 255).endVertex();
        buffer.vertex(x + width, y + height, 0.0D).color(r, g, b, 255).endVertex();
        buffer.vertex(x + width, y, 0.0D).color(r, g, b, 255).endVertex();
    }
}