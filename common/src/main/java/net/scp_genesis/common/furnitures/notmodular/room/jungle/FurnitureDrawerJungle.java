package net.scp_genesis.common.furnitures.notmodular.room.jungle;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Blocks;
import net.scp_genesis.common.furnitures.notmodular.room.WoodenDrawerBlock;

public final class FurnitureDrawerJungle extends WoodenDrawerBlock {
    public static final MapCodec<FurnitureDrawerJungle> CODEC = simpleCodec(FurnitureDrawerJungle::new);
    public FurnitureDrawerJungle() { super(Blocks.JUNGLE_PLANKS); }
    private FurnitureDrawerJungle(Properties properties) { super(properties); }
    @Override protected MapCodec<? extends WoodenDrawerBlock> codec() { return CODEC; }
}