package net.scp_genesis.common.furnitures.notmodular.room.cherry;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Blocks;
import net.scp_genesis.common.furnitures.notmodular.room.WoodenDrawerBlock;

public final class FurnitureDrawerCherry extends WoodenDrawerBlock {
    public static final MapCodec<FurnitureDrawerCherry> CODEC = simpleCodec(FurnitureDrawerCherry::new);
    public FurnitureDrawerCherry() { super(Blocks.CHERRY_PLANKS); }
    private FurnitureDrawerCherry(Properties properties) { super(properties); }
    @Override protected MapCodec<? extends WoodenDrawerBlock> codec() { return CODEC; }
}