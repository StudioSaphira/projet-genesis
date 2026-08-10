package net.scp_genesis.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.scp_genesis.constants.ModConstants;
import net.scp_genesis.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.copycatblocks.util.CopycatConstants;

public final class ModBlockEntities {

    private ModBlockEntities() {

    }

    public static final DeferredRegister<BlockEntityType<?>> REGISTRY =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ModConstants.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CopycatBlockEntity>> COPYCAT_BLOCK_ENTITY =
            REGISTRY.register(CopycatConstants.BLOCK_ENTITY_ID,
                    () -> BlockEntityType.Builder.of(
                            CopycatBlockEntity::new,
                            ModBlocks.COPYCAT_CUBE.get()
                            // ModBlocks.COPYCAT_SLAB.get(),
                            // ModBlocks.COPYCAT_STAIRS.get(),
                            // ModBlocks.COPYCAT_SLOPE.get()
                    ).build(null)); // Unused
}