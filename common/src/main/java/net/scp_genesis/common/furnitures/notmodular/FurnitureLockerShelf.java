package net.scp_genesis.common.furnitures.notmodular;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Blocks;

public final class FurnitureLockerShelf extends LockerBlock {
    public static final MapCodec<FurnitureLockerShelf> CODEC = simpleCodec(FurnitureLockerShelf::new);
    public FurnitureLockerShelf() { this(Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(3, 6).noOcclusion()); }
    private FurnitureLockerShelf(Properties properties) { super(properties, true); }
    @Override protected MapCodec<? extends LockerBlock> codec() { return CODEC; }
}
