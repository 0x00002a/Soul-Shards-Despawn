package info.x2a.soulshards.core;

import com.mojang.serialization.Codec;
import info.x2a.soulshards.SoulShards;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;

public class SoulShardsAttachments {
    public static final Codec<Boolean> CAGE_BORN_CODEC = Codec.BOOL;
    public static AttachmentType<Boolean> CAGE_BORN;

    public static void init() {
        CAGE_BORN = AttachmentRegistry.create(SoulShards.makeResource("cage_born"), b -> b.initializer(() -> false).persistent(CAGE_BORN_CODEC).syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all()).copyOnDeath());
    }
}
