package info.x2a.soulshards.core.data;

import info.x2a.soulshards.api.IBinding;
import info.x2a.soulshards.api.IShardTier;
import info.x2a.soulshards.core.util.INBTSerializable;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Binding implements IBinding, INBTSerializable<CompoundTag> {

    @Nullable
    private ResourceLocation boundEntity;
    @Nullable
    private UUID owner;
    private int kills;

    public Binding(@Nullable ResourceLocation boundEntity, @Nullable UUID owner, int kills) {
        this.boundEntity = boundEntity;
        this.owner = owner;
        this.kills = kills;
    }

    public Binding(ResourceLocation boundEntity, int kills) {
        this(boundEntity, null, kills);
    }

    public Binding(CompoundTag bindingTag) {
        deserializeNBT(bindingTag);
    }

    @Override
    public ResourceLocation getBoundEntity() {
        return boundEntity;
    }

    public Binding setBoundEntity(ResourceLocation boundEntity) {
        this.boundEntity = boundEntity;
        return this;
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    public Binding setOwner(UUID owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public int getKills() {
        return kills;
    }

    public Binding setKills(int kills) {
        this.kills = Math.min(Tier.maxKills, kills);
        return this;
    }

    @Override
    public Binding addKills(int kills) {
        this.kills = Math.min(Tier.maxKills, this.kills + kills);
        return this;
    }

    @Override
    public IShardTier getTier() {
        return Tier.TIERS.floorEntry(kills).getValue();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        if (boundEntity != null)
            tag.putString("bound", boundEntity.toString());
        if (owner != null)
            tag.putString("owner", owner.toString());
        tag.putInt("kills", kills);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.contains("bound"))
            this.boundEntity = ResourceLocation.parse(nbt.getString("bound"));
        if (nbt.contains("owner"))
            this.owner = UUID.fromString(nbt.getString("owner"));
        this.kills = nbt.getInt("kills");
    }

    @Nullable
    public static Binding fromNBT(ItemStack stack) {
        var tag = stack.get(DataComponents.CUSTOM_DATA);
        if (tag == null || !tag.contains("binding"))
            return null;

        return new Binding(tag.copyTag().getCompound("binding"));
    }
}
