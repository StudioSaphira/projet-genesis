package net.scp_genesis.common.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.scp_genesis.common.furnitures.seating.ChairSeatEntity;
import net.scp_genesis.common.platform.PlatformRegistry;
import net.scp_genesis.common.platform.PlatformRegistryObject;

public final class ModEntities {
    private ModEntities() {}
    public static PlatformRegistryObject<EntityType<ChairSeatEntity>> CHAIR_SEAT;

    public static void register(PlatformRegistry registry) {
        CHAIR_SEAT = registry.registerEntityType("chair_seat", () ->
                EntityType.Builder.<ChairSeatEntity>of(ChairSeatEntity::new, MobCategory.MISC)
                        .sized(0.01F, 0.01F).clientTrackingRange(8).updateInterval(20)
                        .noSave().noSummon().build("scp_genesis:chair_seat"));
    }
}
