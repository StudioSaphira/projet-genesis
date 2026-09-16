package net.scp_genesis.common.furnitures.notmodular.room.spruce;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Blocks;
import net.scp_genesis.common.furnitures.notmodular.room.WoodenDrawerBlock;

public final class FurnitureDrawerSpruce extends WoodenDrawerBlock {
    public static final MapCodec<FurnitureDrawerSpruce> CODEC = simpleCodec(FurnitureDrawerSpruce::new);
    public FurnitureDrawerSpruce() { super(Blocks.SPRUCE_PLANKS); }
    private FurnitureDrawerSpruce(Properties properties) { super(properties); }
    @Override protected MapCodec<? extends WoodenDrawerBlock> codec() { return CODEC; }
}