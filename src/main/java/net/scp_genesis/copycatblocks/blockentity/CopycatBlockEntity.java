package net.scp_genesis.copycatblocks.blockentity;

import com.mojang.serialization.DataResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.copycatblocks.data.CopycatData;
import net.scp_genesis.copycatblocks.util.CopycatConstants;
import net.scp_genesis.registry.ModBlockEntities;

/**
 * BlockEntity used by every Copycat Block.
 *
 * <p>This BlockEntity only stores and synchronizes the copied data.
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
     * Returns the Copycat data.
     */
    public CopycatData getData() {
        return data;
    }

    /**
     * Returns the copied BlockState.
     */
    public BlockState getCopiedState() {
        return data.getCopiedState();
    }

    /**
     * Sets the copied BlockState.
     */
    public void setCopiedState(BlockState state) {
        data.setCopiedState(state);
        updateBlock();
    }

    /**
     * Returns whether this Copycat currently contains a copied block.
     */
    public boolean hasCopiedState() {
        return data.hasCopiedState();
    }

    /**
     * Clears the copied BlockState.
     */
    public void clearCopiedState() {
        data.clear();
        updateBlock();
    }

    /**
     * Marks this BlockEntity as changed and synchronizes it with the client.
     */
    private void updateBlock() {
        setChanged();

        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    // ------------------------------------------------------------------------
    // Saving / Loading
    // ------------------------------------------------------------------------

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);

        writeCopycatData(tag, provider);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);

        readCopycatData(tag, provider);
    }

    /**
     * Serialize CopycatData into NBT using the official
     * NeoForge BlockState codec.
     */
    private void writeCopycatData(CompoundTag tag, HolderLookup.Provider provider) {
        RegistryOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);

        DataResult<Tag> result = BlockState.CODEC.encodeStart(
                ops,
                data.getCopiedState()
        );

        result.result().ifPresent(stateTag -> tag.put(CopycatConstants.COPIED_STATE_TAG, stateTag));
    }

    /**
     * Deserialize CopycatData from NBT using the official
     * NeoForge BlockState codec.
     */
    private void readCopycatData(CompoundTag tag, HolderLookup.Provider provider) {
        data.clear();

        if (!tag.contains(CopycatConstants.COPIED_STATE_TAG)) {
            return;
        }

        RegistryOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);

        DataResult<BlockState> result = BlockState.CODEC.parse(
                ops,
                tag.get(CopycatConstants.COPIED_STATE_TAG)
        );

        result.result().ifPresent(data::setCopiedState);
    }

    // ------------------------------------------------------------------------
    // Networking
    // ------------------------------------------------------------------------

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);

        writeCopycatData(tag, provider);

        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {
        readCopycatData(tag, provider);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(
            Connection connection,
            ClientboundBlockEntityDataPacket packet,
            HolderLookup.Provider provider
    ) {
        CompoundTag tag = packet.getTag();

        if (tag != null) {
            handleUpdateTag(tag, provider);

            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    }
}