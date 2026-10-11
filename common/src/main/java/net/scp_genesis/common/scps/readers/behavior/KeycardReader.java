package net.scp_genesis.common.scps.readers.behavior;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.scp_genesis.common.scps.readers.blockentity.ReaderBlockEntity;

/** Accreditation is determined by explicit item tags, regardless of the card's department. */
public final class KeycardReader implements ReaderBehavior {

    public static int accreditation(ItemStack stack) {
        for (int level = 6; level >= 0; level--) {
            if (stack.is(
                    TagKey.create(
                            Registries.ITEM,
                            ResourceLocation.parse("scp_genesis:keycards/level_" + level)))) {
                return level;
            }
        }

        return -1;
    }

    public void use(ReaderBlockEntity reader, ServerPlayer player, ItemStack held) {
        if (!reader.ready()) {
            return;
        }

        if (accreditation(held) >= reader.configuration().level()) {
            reader.open();
        } else {
            reader.error(50);
        }
    }
}
