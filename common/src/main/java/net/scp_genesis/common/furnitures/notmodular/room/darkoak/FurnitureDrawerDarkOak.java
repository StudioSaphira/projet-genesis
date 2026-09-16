package net.scp_genesis.common.furnitures.notmodular.room.darkoak;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Blocks;
import net.scp_genesis.common.furnitures.notmodular.room.WoodenDrawerBlock;

public final class FurnitureDrawerDarkOak extends WoodenDrawerBlock {
    public static final MapCodec<FurnitureDrawerDarkOak> CODEC = simpleCodec(FurnitureDrawerDarkOak::new);
    public FurnitureDrawerDarkOak() { super(Blocks.DARK_OAK_PLANKS); }
    private FurnitureDrawerDarkOak(Properties properties) { super(properties); }
    @Override protected MapCodec<? extends WoodenDrawerBlock> codec() { return CODEC; }
}