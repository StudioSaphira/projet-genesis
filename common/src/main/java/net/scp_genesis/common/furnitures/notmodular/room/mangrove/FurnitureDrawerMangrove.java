package net.scp_genesis.common.furnitures.notmodular.room.mangrove;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Blocks;
import net.scp_genesis.common.furnitures.notmodular.room.WoodenDrawerBlock;

public final class FurnitureDrawerMangrove extends WoodenDrawerBlock {
    public static final MapCodec<FurnitureDrawerMangrove> CODEC = simpleCodec(FurnitureDrawerMangrove::new);
    public FurnitureDrawerMangrove() { super(Blocks.MANGROVE_PLANKS); }
    private FurnitureDrawerMangrove(Properties properties) { super(properties); }
    @Override protected MapCodec<? extends WoodenDrawerBlock> codec() { return CODEC; }
}