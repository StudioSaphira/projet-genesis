package net.scp_genesis.common.furnitures;

import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.*;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.*;
import net.scp_genesis.common.furnitures.notmodular.*;
import net.scp_genesis.common.furnitures.storage.*;
import net.scp_genesis.common.platform.PlatformRegistryObject;
import net.scp_genesis.common.registry.*;

public final class LockerTest {
    private static void check(boolean value, String message) { if (!value) throw new AssertionError(message); }
    private static <T> PlatformRegistryObject<T> holder(T value) {
        return new PlatformRegistryObject<>() {
            public T get() { return value; }
            public ResourceLocation getId() { return ResourceLocation.parse("scp_genesis:test"); }
        };
    }

    public static void main(String[] args) throws Exception {
        SharedConstants.tryDetectVersion();
        // Emulate the loader registration window before vanilla freezes registries, only in this test JVM.
        var bootstrapped = Bootstrap.class.getDeclaredField("isBootstrapped");
        bootstrapped.setAccessible(true);
        bootstrapped.setBoolean(null, true);
        var basic = new FurnitureLocker();
        var shelf = new FurnitureLockerShelf();
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.parse("scp_genesis:locker_test"), basic);
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.parse("scp_genesis:locker_shelf_test"), shelf);
        // The vanilla factory interface is private; production creates this type in each platform adapter.
        var factoryType = Class.forName("net.minecraft.world.level.block.entity.BlockEntityType$BlockEntitySupplier");
        var factory = java.lang.reflect.Proxy.newProxyInstance(factoryType.getClassLoader(), new Class<?>[]{factoryType},
                (proxy, method, values) -> new LockerBlockEntity((BlockPos) values[0],
                        (net.minecraft.world.level.block.state.BlockState) values[1]));
        var constructor = net.minecraft.world.level.block.entity.BlockEntityType.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        @SuppressWarnings("unchecked")
        var type = (net.minecraft.world.level.block.entity.BlockEntityType<LockerBlockEntity>)
                constructor.newInstance(factory, java.util.Set.of(basic, shelf), null);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.parse("scp_genesis:locker_test"), type);
        bootstrapped.setBoolean(null, false);
        Bootstrap.bootStrap();
        ModMenus.LOCKER = holder(null);
        ModBlockEntities.LOCKER = holder(type);

        Inventory inventory = new Inventory(null);
        SimpleContainer container = new SimpleContainer(18);
        LockerMenu menu = new LockerMenu(1, inventory, container);
        check(menu.slots.size() == 54, "18 locker slots + 36 player slots");
        inventory.setItem(9, new ItemStack(Items.IRON_INGOT, 40));
        menu.quickMoveStack(null, 18);
        check(container.getItem(0).getCount() == 40 && inventory.getItem(9).isEmpty(), "Shift-click into locker");
        menu.quickMoveStack(null, 0);
        check(container.isEmpty() && inventory.getItem(8).getCount() == 40, "Shift-click back to hotbar");

        var original = new LockerBlockEntity(BlockPos.ZERO, shelf.defaultBlockState());
        original.setItem(17, new ItemStack(Items.DIAMOND, 23));
        var provider = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        var saved = original.saveWithoutMetadata(provider);
        var restored = new LockerBlockEntity(BlockPos.ZERO, shelf.defaultBlockState());
        restored.loadWithComponents(saved, provider);
        check(restored.getItem(17).is(Items.DIAMOND) && restored.getItem(17).getCount() == 23, "Inventory persistence");
        check(new LockerBlockEntity(BlockPos.ZERO, basic.defaultBlockState()).getContainerSize() == 0,
                "Hiding locker must not expose inventory slots");

        for (LockerBlock block : new LockerBlock[]{basic, shelf})
            for (boolean open : new boolean[]{false, true})
                for (DoubleBlockHalf half : DoubleBlockHalf.values()) {
                    var state = block.defaultBlockState().setValue(LockerBlock.OPEN, open).setValue(LockerBlock.HALF, half);
                    VoxelShape expected = state.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
                    for (int i = 0; i < 4; i++) {
                        check(!Shapes.joinIsNotEmpty(expected, state.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO),
                                BooleanOp.NOT_SAME), "Shape rotation");
                        VoxelShape rotated = Shapes.empty();
                        for (var b : expected.toAabbs()) rotated = Shapes.or(rotated,
                                Shapes.box(1-b.maxZ,b.minY,b.minX,1-b.minZ,b.maxY,b.maxX));
                        expected = rotated;
                        state = state.rotate(Rotation.CLOCKWISE_90);
                    }
                }
        var lower = basic.defaultBlockState().setValue(LockerBlock.OPEN, true);
        VoxelShape whole = Shapes.or(lower.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO),
                lower.setValue(LockerBlock.HALF, DoubleBlockHalf.UPPER).getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).move(0,1,0));
        VoxelShape crouched = Shapes.box(.2,3.0/16,.2,.8,3.0/16+1.5,.8);
        check(!Shapes.joinIsNotEmpty(whole,crouched,BooleanOp.AND), "Crouched player must fit inside");
        check(!Shapes.joinIsNotEmpty(whole,Shapes.box(.2,3.0/16,-.8,.8,3.0/16+1.5,.8),BooleanOp.AND),
                "Open doorway must allow entry");
        var animation = new LockerBlockEntity(BlockPos.ZERO, basic.defaultBlockState());
        for (int i=0;i<10;i++) LockerBlockEntity.clientTick(null,BlockPos.ZERO,lower,animation);
        check(Math.abs(animation.openness(1)-1)<1e-5,"Door opens in ten ticks");
        for (int i=0;i<10;i++) LockerBlockEntity.clientTick(null,BlockPos.ZERO,basic.defaultBlockState(),animation);
        check(Math.abs(animation.openness(1))<1e-5,"Door closes in ten ticks");
        System.out.println("PASS: 18 slots, shift-click both ways, saved inventory, four rotations, crouched entry, animation.");
    }
}
