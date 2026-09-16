package net.scp_genesis.common.furnitures.notmodular.room.birch;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Blocks;
import net.scp_genesis.common.furnitures.notmodular.room.WoodenDrawerBlock;

public final class FurnitureDrawerBirch extends WoodenDrawerBlock {
    public static final MapCodec<FurnitureDrawerBirch> CODEC = simpleCodec(FurnitureDrawerBirch::new);
    public FurnitureDrawerBirch() { super(Blocks.BIRCH_PLANKS); }
    private FurnitureDrawerBirch(Properties properties) { super(properties); }
    @Override protected MapCodec<? extends WoodenDrawerBlock> codec() { return CODEC; }
}