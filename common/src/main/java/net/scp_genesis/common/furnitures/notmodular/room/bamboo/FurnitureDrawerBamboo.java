package net.scp_genesis.common.furnitures.notmodular.room.bamboo;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Blocks;
import net.scp_genesis.common.furnitures.notmodular.room.WoodenDrawerBlock;

public final class FurnitureDrawerBamboo extends WoodenDrawerBlock {
    public static final MapCodec<FurnitureDrawerBamboo> CODEC = simpleCodec(FurnitureDrawerBamboo::new);
    public FurnitureDrawerBamboo() { super(Blocks.BAMBOO_PLANKS); }
    private FurnitureDrawerBamboo(Properties properties) { super(properties); }
    @Override protected MapCodec<? extends WoodenDrawerBlock> codec() { return CODEC; }
}