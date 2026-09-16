package net.scp_genesis.common.furnitures.notmodular.room.oak;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Blocks;
import net.scp_genesis.common.furnitures.notmodular.room.WoodenDrawerBlock;

public final class FurnitureDrawerOak extends WoodenDrawerBlock {
    public static final MapCodec<FurnitureDrawerOak> CODEC = simpleCodec(FurnitureDrawerOak::new);
    public FurnitureDrawerOak() { super(Blocks.OAK_PLANKS); }
    private FurnitureDrawerOak(Properties properties) { super(properties); }
    @Override protected MapCodec<? extends WoodenDrawerBlock> codec() { return CODEC; }
}