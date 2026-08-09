package net.scp_genesis.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.scp_genesis.ScpGenesisMod;

public class ScpGenesisModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ScpGenesisMod.MODID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CUSTOM_ITEMS = REGISTRY.register("custom_items",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.scp_genesis.custom_items")).icon(() -> new ItemStack(ScpGenesisModItems.ETHICS_COMMITTEE_KEYCARD_LV_5.get())).displayItems((parameters, tabData) -> {
				tabData.accept(ScpGenesisModItems.ETHICS_COMMITTEE_KEYCARD_LV_5.get());
				tabData.accept(ScpGenesisModItems.ETHICS_COMMITTEE_KEYCARD_LV_4.get());
				tabData.accept(ScpGenesisModItems.ETHICS_COMMITTEE_KEYCARD_LV_3.get());
				tabData.accept(ScpGenesisModItems.ETHICS_COMMITTEE_KEYCARD_LV_2.get());
				tabData.accept(ScpGenesisModItems.ETHICS_COMMITTEE_KEYCARD_LV_1.get());
				tabData.accept(ScpGenesisModItems.RAISA_KEYCARD_LV_5.get());
				tabData.accept(ScpGenesisModItems.RAISA_KEYCARD_LV_4.get());
				tabData.accept(ScpGenesisModItems.RAISA_KEYCARD_LV_3.get());
				tabData.accept(ScpGenesisModItems.RAISA_KEYCARD_LV_2.get());
				tabData.accept(ScpGenesisModItems.LOGISTICS_KEYCARD_LV_0.get());
				tabData.accept(ScpGenesisModItems.LOGISTICS_KEYCARD_LV_1.get());
				tabData.accept(ScpGenesisModItems.LOGISTICS_KEYCARD_LV_2.get());
				tabData.accept(ScpGenesisModItems.LOGISTICS_KEYCARD_LV_3.get());
				tabData.accept(ScpGenesisModItems.EXTERNAL_KEYCARD_LV_0.get());
				tabData.accept(ScpGenesisModItems.O_5_KEYCARD.get());
				tabData.accept(ScpGenesisModItems.ADMIN_KEYCARDS_LV_1.get());
				tabData.accept(ScpGenesisModItems.ADMIN_KEYCARDS_LV_2.get());
				tabData.accept(ScpGenesisModItems.ADMIN_KEYCARDS_LV_3.get());
				tabData.accept(ScpGenesisModItems.ADMIN_KEYCARDS_LV_4.get());
				tabData.accept(ScpGenesisModItems.ADMIN_KEYCARDS_LV_5.get());
				tabData.accept(ScpGenesisModItems.ADMIN_KEYCARDS_LV_6.get());
				tabData.accept(ScpGenesisModItems.INTERNAL_KEYCARD_LV_1.get());
				tabData.accept(ScpGenesisModItems.INTERNAL_KEYCARD_LV_2.get());
				tabData.accept(ScpGenesisModItems.INTERNAL_KEYCARD_LV_3.get());
				tabData.accept(ScpGenesisModItems.INTERNAL_KEYCARD_LV_4.get());
				tabData.accept(ScpGenesisModItems.INTERNAL_KEYCARD_LV_5.get());
				tabData.accept(ScpGenesisModItems.INTERNAL_KEYCARD_LV_6.get());
				tabData.accept(ScpGenesisModItems.D_CLASS_KEYCARD_LV_0.get());
				tabData.accept(ScpGenesisModItems.MJD_KEYCARD_LV_1.get());
				tabData.accept(ScpGenesisModItems.MJD_KEYCARD_LV_2.get());
				tabData.accept(ScpGenesisModItems.MJD_KEYCARD_LV_3.get());
				tabData.accept(ScpGenesisModItems.MTF_RANK_1_LV_2.get());
				tabData.accept(ScpGenesisModItems.MTF_RANK_2_LV_2.get());
				tabData.accept(ScpGenesisModItems.MTF_RANK_3_LV_1.get());
				tabData.accept(ScpGenesisModItems.ID_CARD.get());
				tabData.accept(ScpGenesisModItems.RAISA_KEYCARD_LV_6.get());
				tabData.accept(ScpGenesisModItems.ETHICS_COMMITTEE_KEYCARD_LV_6.get());
				tabData.accept(ScpGenesisModItems.IIA_KEYCARD_LV_1.get());
				tabData.accept(ScpGenesisModItems.IIA_KEYCARD_LV_2.get());
				tabData.accept(ScpGenesisModItems.IIA_KEYCARD_LV_3.get());
				tabData.accept(ScpGenesisModItems.IIA_KEYCARD_LV_4.get());
				tabData.accept(ScpGenesisModItems.IIA_KEYCARD_LV_5.get());
				tabData.accept(ScpGenesisModItems.IIA_KEYCARD_LV_6.get());
				tabData.accept(ScpGenesisModItems.MTF_ANTHEIA_5_KEYCARD_LV_4.get());
				tabData.accept(ScpGenesisModItems.MTF_ANTHEIA_5_KEYCARD_LV_3.get());
				tabData.accept(ScpGenesisModItems.MTF_ANTHEIA_5_KEYCARD_LV_5.get());
				tabData.accept(ScpGenesisModItems.MTF_UMBRA_6_LV_2.get());
				tabData.accept(ScpGenesisModItems.MTF_UMBRA_6_LV_1.get());
				tabData.accept(ScpGenesisModItems.MTF_UMBRA_6_LV_3.get());
				tabData.accept(ScpGenesisModItems.ALPHA_1_KEYCARD_LV_4.get());
				tabData.accept(ScpGenesisModItems.ALPHA_1_KEYCARD_LV_5.get());
				tabData.accept(ScpGenesisModItems.ALPHA_1_KEYCARD_LV_6.get());
				tabData.accept(ScpGenesisModItems.EPSILON_11_KEYCARD_LV_2.get());
				tabData.accept(ScpGenesisModItems.EPSILON_11_KEYCARD_LV_3.get());
				tabData.accept(ScpGenesisModItems.EPSILON_11_KEYCARD_LV_4.get());
				tabData.accept(ScpGenesisModItems.EPSILON_11_KEYCARD_LV_5.get());
				tabData.accept(ScpGenesisModItems.EPSILON_11_KEYCARD_LV_6.get());
				tabData.accept(ScpGenesisModItems.GAMMA_8_KEYCARD_LV_3.get());
				tabData.accept(ScpGenesisModItems.GAMMA_8_KEYCARD_LV_4.get());
				tabData.accept(ScpGenesisModItems.GAMMA_8_KEYCARD_LV_5.get());
				tabData.accept(ScpGenesisModItems.GAMMA_8_KEYCARD_LV_6.get());
				tabData.accept(ScpGenesisModItems.NU_7_KEYCARD_LV_2.get());
				tabData.accept(ScpGenesisModItems.NU_7_KEYCARD_LV_3.get());
				tabData.accept(ScpGenesisModItems.NU_7_KEYCARD_LV_4.get());
				tabData.accept(ScpGenesisModItems.NU_7_KEYCARD_LV_5.get());
				tabData.accept(ScpGenesisModItems.NU_7_KEYCARD_LV_6.get());
				tabData.accept(ScpGenesisModItems.OMEGA_1_KEYCARD_LV_1.get());
				tabData.accept(ScpGenesisModItems.OMEGA_1_KEYCARD_LV_2.get());
				tabData.accept(ScpGenesisModItems.OMEGA_1_KEYCARD_LV_3.get());
				tabData.accept(ScpGenesisModItems.OMEGA_1_KEYCARD_LV_4.get());
				tabData.accept(ScpGenesisModItems.OMEGA_1_KEYCARD_LV_5.get());
				tabData.accept(ScpGenesisModItems.OMEGA_1_KEYCARD_LV_6.get());
				tabData.accept(ScpGenesisModItems.RESH_1_KEYCARD_LV_4.get());
				tabData.accept(ScpGenesisModItems.RESH_1_KEYCARD_LV_5.get());
				tabData.accept(ScpGenesisModItems.RESH_1_KEYCARD_LV_6.get());
				tabData.accept(ScpGenesisModItems.ZETA_19_KEYCARD_LV_2.get());
				tabData.accept(ScpGenesisModItems.ZETA_19_KEYCARD_LV_3.get());
				tabData.accept(ScpGenesisModItems.ZETA_19_KEYCARD_LV_4.get());
				tabData.accept(ScpGenesisModItems.ZETA_19_KEYCARD_LV_5.get());
				tabData.accept(ScpGenesisModItems.MTF_RANK_1_LV_6.get());
				tabData.accept(ScpGenesisModItems.MTF_RANK_1_LV_3.get());
				tabData.accept(ScpGenesisModItems.MTF_RANK_1_LV_4.get());
				tabData.accept(ScpGenesisModItems.MTF_RANK_1_LV_5.get());
				tabData.accept(ScpGenesisModItems.MTF_RANK_2_LV_3.get());
				tabData.accept(ScpGenesisModItems.MTF_RANK_2_LV_4.get());
				tabData.accept(ScpGenesisModItems.MTF_RANK_2_LV_5.get());
				tabData.accept(ScpGenesisModItems.MTF_RANK_3_LV_2.get());
				tabData.accept(ScpGenesisModItems.MTF_RANK_3_LV_3.get());
				tabData.accept(ScpGenesisModItems.MTF_RANK_3_LV_4.get());
			}).withSearchBar().build());
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BUILDING_BLOCKS = REGISTRY.register("building_blocks",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.scp_genesis.building_blocks")).icon(() -> new ItemStack(ScpGenesisModBlocks.CLEAN_WHITE_WALL.get())).displayItems((parameters, tabData) -> {
				tabData.accept(ScpGenesisModBlocks.CLEAN_WHITE_WALL.get().asItem());
				tabData.accept(ScpGenesisModBlocks.BROKEN_WHITE_WALL_ONE.get().asItem());
				tabData.accept(ScpGenesisModBlocks.BROKEN_WHITE_WALL_TWO.get().asItem());
				tabData.accept(ScpGenesisModBlocks.DIRTY_BROKEN_WHITE_WALL_ONE.get().asItem());
				tabData.accept(ScpGenesisModBlocks.DIRTY_BROKEN_WHITE_WALL_TWO.get().asItem());
				tabData.accept(ScpGenesisModBlocks.WALL_BASE.get().asItem());
				tabData.accept(ScpGenesisModBlocks.GREEN_TILED_FLOOR.get().asItem());
			}).withSearchBar().withTabsBefore(CUSTOM_ITEMS.getId()).build());
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> COPYCAT_BLOCKS = REGISTRY.register("copycat_blocks",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.scp_genesis.copycat_blocks")).icon(() -> new ItemStack(ScpGenesisModBlocks.COPYCAT_CUBE.get())).displayItems((parameters, tabData) -> {
				tabData.accept(ScpGenesisModBlocks.COPYCAT_CUBE.get().asItem());
			}).withSearchBar().withTabsBefore(BUILDING_BLOCKS.getId()).build());
}