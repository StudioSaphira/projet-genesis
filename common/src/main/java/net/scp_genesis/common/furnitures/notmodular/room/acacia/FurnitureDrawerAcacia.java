package net.scp_genesis.common.furnitures.notmodular.room.acacia;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Blocks;
import net.scp_genesis.common.furnitures.notmodular.room.WoodenDrawerBlock;

public final class FurnitureDrawerAcacia extends WoodenDrawerBlock {
    public static final MapCodec<FurnitureDrawerAcacia> CODEC = simpleCodec(FurnitureDrawerAcacia::new);
    public FurnitureDrawerAcacia() { super(Blocks.ACACIA_PLANKS); }
    private FurnitureDrawerAcacia(Properties properties) { super(properties); }
    @Override protected MapCodec<? extends WoodenDrawerBlock> codec() { return CODEC; }
}