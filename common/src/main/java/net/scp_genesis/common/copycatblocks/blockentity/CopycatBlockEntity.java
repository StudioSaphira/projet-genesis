package net.scp_genesis.common.copycatblocks.blockentity;

import com.mojang.serialization.DataResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.data.CopycatData;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.util.CopycatConstants;
import net.scp_genesis.common.platform.PlatformServices;
import net.scp_genesis.common.registry.ModBlockEntities;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

/**
 * BlockEntity used by every Copycat Block.
 *
 * <p>This BlockEntity stores and synchronizes the copied data.
 * Rendering is handled entirely by the renderer.</p>
 */
public class CopycatBlockEntity extends BlockEntity {

    /**
     * Copycat data.
     */
    private final CopycatData data = new CopycatData();

    public CopycatBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COPYCAT_BLOCK_ENTITY.get(), pos, state);
    }

    /**
     * Returns all copied BlockStates indexed by Copycat part.
     */
    public @NotNull EnumMap<CopycatPart, BlockState> getCopiedStates() {
        EnumMap<CopycatPart, BlockState> copiedStates =
                new EnumMap<>(CopycatPart.class);

        for (CopycatPart part : CopycatPart.values()) {
            copiedStates.put(
                    part,
                    getCopiedState(part)
            );
        }

        return copiedStates;
    }

    // ------------------------------------------------------------------------
    // Main Copycat state
    // ------------------------------------------------------------------------

    /**
     * Returns the main copied BlockState.
     */
    public @NotNull BlockState getCopiedState() {
        return data.getCopiedState();
    }

    /**
     * Sets the main copied BlockState.
     */
    public void setCopiedState(@NotNull BlockState state) {
        data.setCopiedState(state);
        updateBlock();
    }

    /**
     * Returns whether the main Copycat currently contains a copied block.
     */
    public boolean hasCopiedState() {
        return data.hasCopiedState();
    }

    /**
     * Clears the main copied BlockState.
     */
    @SuppressWarnings("unused")
    public void clearCopiedState() {
        data.clear(CopycatPart.MAIN);
        updateBlock();
    }

    // ------------------------------------------------------------------------
    // Copycat Parts
    // ------------------------------------------------------------------------

    /**
     * Returns the copied BlockState for the specified part.
     */
    public @NotNull BlockState getCopiedState(
            @NotNull CopycatPart part
    ) {
        return data.getCopiedState(part);
    }

    /**
     * Sets the copied BlockState for the specified part.
     */
    public void setCopiedState(
            @NotNull CopycatPart part,
            @NotNull BlockState state
    ) {
        data.setCopiedState(part, state);
        updateBlock();
    }

    /**
     * Returns whether the specified part contains a copied BlockState.
     */
    public boolean hasCopiedState(
            @NotNull CopycatPart part
    ) {
        return data.hasCopiedState(part);
    }

    /**
     * Clears the specified part.
     */
    public void clearCopiedState(
            @NotNull CopycatPart part
    ) {
        data.clear(part);
        updateBlock();
    }

    // ------------------------------------------------------------------------
    // Block Updates
    // ------------------------------------------------------------------------

    /**
     * Marks this BlockEntity as changed and synchronizes it with the client.
     */
    private void updateBlock() {
        setChanged();

        if (level != null) {
            level.sendBlockUpdated(
                    worldPosition,
                    getBlockState(),
                    getBlockState(),
                    3
            );
        }

        PlatformServices.requestModelDataUpdate(this);
    }

    // ------------------------------------------------------------------------
    // Saving / Loading
    // ------------------------------------------------------------------------

    @Override
    protected void saveAdditional(
            @NotNull CompoundTag tag,
            HolderLookup.@NotNull Provider provider
    ) {
        super.saveAdditional(tag, provider);

        writeCopycatData(tag, provider);
    }

    @Override
    protected void loadAdditional(
            @NotNull CompoundTag tag,
            HolderLookup.@NotNull Provider provider
    ) {
        super.loadAdditional(tag, provider);

        readUpdateTag(tag, provider);
    }

    /**
     * Serializes all Copycat parts into NBT.
     */
    private void writeCopycatData(
            CompoundTag tag,
            HolderLookup.Provider provider
    ) {
        RegistryOps<Tag> ops =
                provider.createSerializationContext(NbtOps.INSTANCE);

        CompoundTag partsTag = new CompoundTag();

        for (CopycatPart part : CopycatPart.values()) {

            BlockState state = data.getCopiedState(part);

            DataResult<Tag> result =
                    BlockState.CODEC.encodeStart(
                            ops,
                            state
                    );

            result.result().ifPresent(
                    stateTag ->
                            partsTag.put(
                                    part.name(),
                                    stateTag
                            )
            );
        }

        tag.put(
                CopycatConstants.COPIED_STATES_TAG,
                partsTag
        );
    }

    /**
     * Deserializes all Copycat parts from NBT.
     */
    public void readUpdateTag(
            CompoundTag tag,
            HolderLookup.Provider provider
    ) {
        data.clear();

        if (!tag.contains(CopycatConstants.COPIED_STATES_TAG)) {
            return;
        }

        RegistryOps<Tag> ops =
                provider.createSerializationContext(NbtOps.INSTANCE);

        CompoundTag partsTag =
                tag.getCompound(
                        CopycatConstants.COPIED_STATES_TAG
                );

        for (CopycatPart part : CopycatPart.values()) {

            if (!partsTag.contains(part.name())) {
                continue;
            }

            DataResult<BlockState> result =
                    BlockState.CODEC.parse(
                            ops,
                            partsTag.get(part.name())
                    );

            result.result().ifPresent(
                    state ->
                            data.setCopiedState(
                                    part,
                                    state
                            )
            );
        }
    }

    // ------------------------------------------------------------------------
    // Networking
    // ------------------------------------------------------------------------

    @Override
    public @NotNull CompoundTag getUpdateTag(
            HolderLookup.@NotNull Provider provider
    ) {
        CompoundTag tag = super.getUpdateTag(provider);

        writeCopycatData(tag, provider);

        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}