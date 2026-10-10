package net.scp_genesis.common.scps.readers;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.registry.ModBlockEntities;
/** Stores private access data and advances timed states using absolute server ticks. */
public final class ReaderBlockEntity extends BlockEntity {
    private ReaderConfiguration config = ReaderConfiguration.DEFAULT;
    private long deadline;
    private boolean indexed;
    public ReaderBlockEntity(BlockPos pos, BlockState state) { super(ModBlockEntities.READER.get(),pos,state); }
    public ReaderConfiguration configuration() { return config; }
    public ReaderStatus status() { return getBlockState().getValue(ReaderBlock.STATUS); }
    public void configure(ReaderConfiguration next) {
        if (!(level instanceof ServerLevel server)) return;
        var old = config; config = next; deadline = 0; publish(ReaderStatus.BASE);
        ReaderDoorController.refresh(server,old); ReaderDoorController.refresh(server,config);
    }
    public void open() {
        if (config.type() == ReaderType.LOCKED || !(level instanceof ServerLevel server)) return;
        if (config.first() == null && config.second() == null) { error(50); return; }
        deadline = server.getGameTime()+200; publish(ReaderStatus.OPENING); ReaderDoorController.refresh(server,config);
    }
    public void close() {
        if (!(level instanceof ServerLevel server)) return;
        deadline = server.getGameTime()+100; publish(ReaderStatus.CLOSING); ReaderDoorController.refresh(server,config);
    }
    public void error(int ticks) {
        if (status() == ReaderStatus.OPENING || status() == ReaderStatus.CLOSING || status() == ReaderStatus.ERROR) return;
        assert level != null;
        deadline = level.getGameTime()+ticks; publish(ReaderStatus.ERROR);
    }
    public boolean ready() { return status() == ReaderStatus.BASE; }
    private void publish(ReaderStatus status) {
        if (!(level instanceof ServerLevel server)) return;
        var state = getBlockState().setValue(ReaderBlock.TYPE,config.type())
                .setValue(ReaderBlock.LEVEL,ReaderLevel.of(config.level())).setValue(ReaderBlock.STATUS,status);
        level.setBlock(worldPosition,state,Block.UPDATE_ALL);
        ReaderDirectory.get(server).update(worldPosition,config,status == ReaderStatus.OPENING ? deadline : 0);
        setChanged();
    }
    public static void serverTick(Level level, BlockPos pos, BlockState state, ReaderBlockEntity reader) {
        if (!reader.indexed) { reader.publish(reader.status()); reader.indexed = true; }
        if (reader.status() == ReaderStatus.BASE || level.getGameTime() < reader.deadline) return;
        if (reader.status() == ReaderStatus.OPENING) reader.close();
        else { reader.deadline = 0; reader.publish(ReaderStatus.BASE); }
    }
    public void destroy() {
        if (level instanceof ServerLevel server) {
            ReaderDirectory.get(server).remove(worldPosition); ReaderDoorController.refresh(server,config);
        }
    }
    @Override protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag,registries); tag.put("configuration",config.save()); tag.putLong("deadline",deadline);
    }
    @Override protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag,registries); config = ReaderConfiguration.load(tag.getCompound("configuration")); deadline = tag.getLong("deadline");
    }
    // No update tag: codes and identifiers are not broadcast to unrelated clients.
}
