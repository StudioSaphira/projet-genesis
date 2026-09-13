package net.scp_genesis.common.furnitures.notmodular;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Blocks;

public final class FurnitureLocker extends LockerBlock {
    public static final MapCodec<FurnitureLocker> CODEC = simpleCodec(FurnitureLocker::new);
    public FurnitureLocker() { this(Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(3, 6).noOcclusion()); }
    private FurnitureLocker(Properties properties) { super(properties, false); }
    @Override protected MapCodec<? extends LockerBlock> codec() { return CODEC; }
}