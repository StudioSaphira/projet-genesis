package net.scp_genesis.fabric.mixin;

import java.util.Comparator;
import java.util.List;
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroupImpl;
import net.fabricmc.fabric.mixin.itemgroup.ItemGroupAccessor;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.scp_genesis.fabric.platform.FabricTabSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeModeTabs.class)
public abstract class CreativeTabOrderMixin {
    public record Position(int page, CreativeModeTab.Row row, int column) {}

    @Inject(method = "tryRebuildTabContents", at = @At("RETURN"))
    private static void genesis$orderTabs(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) return;
        // Fabric 0.116.x has already paginated alphabetically. Reuse only our slots,
        // including page boundaries, leaving vanilla and other mods' positions intact.
        List<CreativeModeTab> tabs = FabricTabSettings.orderedTabs().stream()
                .filter(CreativeModeTab::shouldDisplay).toList();
        List<Position> positions = tabs.stream()
                .map(tab -> new Position(((FabricItemGroupImpl) tab).fabric_getPage(), tab.row(), tab.column()))
                .sorted(Comparator.comparingInt(Position::page)
                        .thenComparing(Position::row).thenComparingInt(Position::column)).toList();
        for (int i = 0; i < tabs.size(); i++) {
            CreativeModeTab tab = tabs.get(i);
            Position position = positions.get(i);
            ((FabricItemGroupImpl) tab).fabric_setPage(position.page());
            ((ItemGroupAccessor) tab).setRow(position.row());
            ((ItemGroupAccessor) tab).setColumn(position.column());
        }
    }
}
