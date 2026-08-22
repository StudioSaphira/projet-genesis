package net.scp_genesis.common.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;

import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.copycatblocks.util.CopycatConstants;
import net.scp_genesis.common.platform.PlatformRegistry;
import net.scp_genesis.common.platform.PlatformRegistryObject;

public final class ModBlockEntities {

    private ModBlockEntities() {}

    public static PlatformRegistryObject<BlockEntityType<CopycatBlockEntity>>
            COPYCAT_BLOCK_ENTITY;

    @SuppressWarnings("DataFlowIssue")
    public static void register(
            PlatformRegistry registry
    ) {
        COPYCAT_BLOCK_ENTITY =
                registry.registerBlockEntity(
                        CopycatConstants.BLOCK_ENTITY_ID,
                        () -> BlockEntityType.Builder.of(
                                CopycatBlockEntity::new,
                                ModBlocks.COPYCAT_CUBE.get(),
                                ModBlocks.COPYCAT_SLAB.get(),
                                ModBlocks.COPYCAT_STAIRS.get()
                                // ModBlocks.COPYCAT_SLOPE.get()
                        ).build(null)
                );
    }
}