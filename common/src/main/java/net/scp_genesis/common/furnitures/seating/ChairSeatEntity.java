package net.scp_genesis.common.furnitures.seating;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.scp_genesis.common.furnitures.notmodular.room.WoodenChairBlock;

/** Temporary, server-owned mount. Never saved to the world. */
public final class ChairSeatEntity extends Entity {
    public ChairSeatEntity(EntityType<? extends ChairSeatEntity> type, Level level) {
        super(type, level);
        noPhysics = true;
        setNoGravity(true);
        setInvulnerable(true);
        setInvisible(true);
    }

    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) {}
    @Override protected void readAdditionalSaveData(CompoundTag tag) {}
    @Override protected void addAdditionalSaveData(CompoundTag tag) {}

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && (!isVehicle()
                || !(level().getBlockState(blockPosition()).getBlock() instanceof WoodenChairBlock))) {
            ejectPassengers();
            discard();
        }
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return getPassengers().isEmpty();
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
        return Vec3.ZERO;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        // Stand above the seat rather than inside its collision shape.
        return new Vec3(getX(), blockPosition().getY() + 10.0 / 16.0, getZ());
    }
}
