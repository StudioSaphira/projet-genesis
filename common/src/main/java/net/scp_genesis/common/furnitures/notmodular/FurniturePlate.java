package net.scp_genesis.common.furnitures.notmodular;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import net.scp_genesis.common.furnitures.storage.PlateBlockEntity;

/** Hand-breakable plate: right-click to place one food portion, then right-click to eat it. */
public final class FurniturePlate extends BaseEntityBlock {
    public static final MapCodec<FurniturePlate> CODEC=simpleCodec(FurniturePlate::new);
    private static final VoxelShape SHAPE=Shapes.or(
            box(3,0,3,13,1,13),
            box(3,1,1,13,2,2),
            box(3,1,2,13,2,3),
            box(3,1,13,13,2,14),
            box(12,1,12,13,2,13),
            box(12,1,3,13,2,4),
            box(3,1,12,4,2,13),
            box(3,1,3,4,2,4),
            box(3,1,14,13,2,15),
            box(1,1,3,2,2,13),
            box(14,1,3,15,2,13),
            box(13,1,2,14,2,14),
            box(2,1,2,3,2,14)).optimize();
    public FurniturePlate(){this(Properties.of().strength(0.3F).sound(SoundType.DECORATED_POT).noOcclusion());}
    public FurniturePlate(Properties properties){super(properties);}
    @Override protected MapCodec<? extends FurniturePlate> codec(){return CODEC;}
    @Override public BlockEntity newBlockEntity(BlockPos pos,BlockState state){return new PlateBlockEntity(pos,state);}
    @Override protected RenderShape getRenderShape(BlockState state){return RenderShape.MODEL;}
    @Override protected VoxelShape getShape(BlockState state,BlockGetter level,BlockPos pos,CollisionContext context){return SHAPE;}
    @Override protected boolean isPathfindable(BlockState state,PathComputationType type){return false;}
    @Override protected boolean canSurvive(BlockState state,LevelReader level,BlockPos pos){return Block.canSupportCenter(level,pos.below(),Direction.UP);}
    @Override protected BlockState updateShape(BlockState state,Direction direction,BlockState neighbor,LevelAccessor level,BlockPos pos,BlockPos neighborPos){
        return direction==Direction.DOWN&&!state.canSurvive(level,pos)?Blocks.AIR.defaultBlockState():super.updateShape(state,direction,neighbor,level,pos,neighborPos);
    }
    @Override protected ItemInteractionResult useItemOn(ItemStack held,BlockState state,Level level,BlockPos pos,Player player,InteractionHand hand,BlockHitResult hit){
        if(!(level.getBlockEntity(pos) instanceof PlateBlockEntity plate))return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if(!plate.isEmpty()){
            if(!level.isClientSide)plate.eat(player);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        if(!PlateBlockEntity.accepts(held))return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if(!level.isClientSide&&plate.insert(held))held.consume(1,player);
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }
    @Override protected InteractionResult useWithoutItem(BlockState state,Level level,BlockPos pos,Player player,BlockHitResult hit){
        if(!(level.getBlockEntity(pos) instanceof PlateBlockEntity plate)||plate.isEmpty())return InteractionResult.PASS;
        if(!level.isClientSide)plate.eat(player);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
    @Override protected void onRemove(BlockState state,Level level,BlockPos pos,BlockState replacement,boolean moving){
        if(!state.is(replacement.getBlock())&&!level.isClientSide&&level.getBlockEntity(pos) instanceof PlateBlockEntity plate)
            Block.popResource(level,pos,plate.take());
        super.onRemove(state,level,pos,replacement,moving);
    }
}
