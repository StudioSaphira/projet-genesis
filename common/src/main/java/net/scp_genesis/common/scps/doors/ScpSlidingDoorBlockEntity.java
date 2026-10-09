package net.scp_genesis.common.scps.doors;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.registry.ModBlockEntities;

/** Client interpolation driven solely by the synchronized vanilla OPEN block state. */
public final class ScpSlidingDoorBlockEntity extends BlockEntity {
    private float previous;
    private float progress;
    private boolean initialized;

    public ScpSlidingDoorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SCP_SLIDING_DOOR.get(), pos, state);
        previous = progress = state.getValue(ScpSlidingDoorBlock.OPEN) ? 1 : 0;
    }

    /** A full movement lasts twelve ticks; reversing keeps the current position. */
    public static void tick(ScpSlidingDoorBlockEntity door) {
        float target = door.getBlockState().getValue(ScpSlidingDoorBlock.OPEN) ? 1 : 0;
        if (!door.initialized) {
            door.previous = door.progress = target;
            door.initialized = true;
        }
        door.previous = door.progress;
        door.progress = Mth.approach(door.progress, target, 1.0F / 12);
    }

    public float openness(float partialTick) {
        float value = Mth.lerp(partialTick, previous, progress);
        return value * value * (3 - 2 * value);
    }
}
