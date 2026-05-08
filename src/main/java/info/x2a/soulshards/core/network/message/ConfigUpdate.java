package info.x2a.soulshards.core.network.message;

import com.google.gson.reflect.TypeToken;
import info.x2a.soulshards.SoulShards;
import info.x2a.soulshards.core.config.ConfigServer;
import info.x2a.soulshards.core.util.JsonUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public record ConfigUpdate(ConfigServer.ConfigBalance balance,
                           ConfigServer.ConfigEntityList entityList) implements CustomPacketPayload {
    public static final ResourceLocation CONFIG_UPLOAD_PAYLOAD_ID =
            SoulShards.makeResource("update_config");
    public static final CustomPacketPayload.Type<ConfigUpdate> TYPE = new CustomPacketPayload.Type<>(CONFIG_UPLOAD_PAYLOAD_ID);
    public static final StreamCodec<FriendlyByteBuf, ConfigUpdate> CODEC = StreamCodec.ofMember(ConfigUpdate::encode, ConfigUpdate::new);

    public ConfigUpdate(ConfigServer config) {
        this(config.getBalance(), config.entityList);
    }

    public ConfigUpdate(FriendlyByteBuf buf) {
        this(JsonUtil.fromJson(TypeToken.get(ConfigServer.ConfigBalance.class), new String(buf.readByteArray(), StandardCharsets.UTF_8)), JsonUtil.fromJson(TypeToken.get(ConfigServer.ConfigEntityList.class), new String(buf.readByteArray(), StandardCharsets.UTF_8)));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeByteArray(JsonUtil.getJson(balance, TypeToken.get(ConfigServer.ConfigBalance.class))
                .getBytes(StandardCharsets.UTF_8));
        buf.writeByteArray(JsonUtil.getJson(entityList, TypeToken.get(ConfigServer.ConfigEntityList.class))
                .getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
