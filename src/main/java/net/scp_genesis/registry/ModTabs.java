package net.scp_genesis.registry;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.scp_genesis.constants.ModConstants;

public class ModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModConstants.MOD_ID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CUSTOM_ITEMS = REGISTRY.register("custom_items",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.scp_genesis.custom_items")).icon(() -> new ItemStack(ModItems.ETHICS_COMMITTEE_KEYCARD_LV_5.get())).displayItems((parameters, tabData) -> {
				tabData.accept(ModItems.ETHICS_COMMITTEE_KEYCARD_LV_5.get());
				tabData.accept(ModItems.ETHICS_COMMITTEE_KEYCARD_LV_4.get());
				tabData.accept(ModItems.ETHICS_COMMITTEE_KEYCARD_LV_3.get());
				tabData.accept(ModItems.ETHICS_COMMITTEE_KEYCARD_LV_2.get());
				tabData.accept(ModItems.ETHICS_COMMITTEE_KEYCARD_LV_1.get());
				tabData.accept(ModItems.RAISA_KEYCARD_LV_5.get());
				tabData.accept(ModItems.RAISA_KEYCARD_LV_4.get());
				tabData.accept(ModItems.RAISA_KEYCARD_LV_3.get());
				tabData.accept(ModItems.RAISA_KEYCARD_LV_2.get());
				tabData.accept(ModItems.LOGISTICS_KEYCARD_LV_0.get());
				tabData.accept(ModItems.LOGISTICS_KEYCARD_LV_1.get());
				tabData.accept(ModItems.LOGISTICS_KEYCARD_LV_2.get());
				tabData.accept(ModItems.LOGISTICS_KEYCARD_LV_3.get());
				tabData.accept(ModItems.EXTERNAL_KEYCARD_LV_0.get());
				tabData.accept(ModItems.O5_KEYCARD.get());
				tabData.accept(ModItems.ADMIN_KEYCARDS_LV_1.get());
				tabData.accept(ModItems.ADMIN_KEYCARDS_LV_2.get());
				tabData.accept(ModItems.ADMIN_KEYCARDS_LV_3.get());
				tabData.accept(ModItems.ADMIN_KEYCARDS_LV_4.get());
				tabData.accept(ModItems.ADMIN_KEYCARDS_LV_5.get());
				tabData.accept(ModItems.ADMIN_KEYCARDS_LV_6.get());
				tabData.accept(ModItems.INTERNAL_KEYCARD_LV_1.get());
				tabData.accept(ModItems.INTERNAL_KEYCARD_LV_2.get());
				tabData.accept(ModItems.INTERNAL_KEYCARD_LV_3.get());
				tabData.accept(ModItems.INTERNAL_KEYCARD_LV_4.get());
				tabData.accept(ModItems.INTERNAL_KEYCARD_LV_5.get());
				tabData.accept(ModItems.INTERNAL_KEYCARD_LV_6.get());
				tabData.accept(ModItems.D_CLASS_KEYCARD_LV_0.get());
				tabData.accept(ModItems.MJD_KEYCARD_LV_1.get());
				tabData.accept(ModItems.MJD_KEYCARD_LV_2.get());
				tabData.accept(ModItems.MJD_KEYCARD_LV_3.get());
				tabData.accept(ModItems.MTF_RANK_1_LV_2.get());
				tabData.accept(ModItems.MTF_RANK_2_LV_2.get());
				tabData.accept(ModItems.MTF_RANK_3_LV_1.get());
				tabData.accept(ModItems.ID_CARD.get());
				tabData.accept(ModItems.RAISA_KEYCARD_LV_6.get());
				tabData.accept(ModItems.ETHICS_COMMITTEE_KEYCARD_LV_6.get());
				tabData.accept(ModItems.IIA_KEYCARD_LV_1.get());
				tabData.accept(ModItems.IIA_KEYCARD_LV_2.get());
				tabData.accept(ModItems.IIA_KEYCARD_LV_3.get());
				tabData.accept(ModItems.IIA_KEYCARD_LV_4.get());
				tabData.accept(ModItems.IIA_KEYCARD_LV_5.get());
				tabData.accept(ModItems.IIA_KEYCARD_LV_6.get());
				tabData.accept(ModItems.MTF_ANTHEIA_5_KEYCARD_LV_4.get());
				tabData.accept(ModItems.MTF_ANTHEIA_5_KEYCARD_LV_3.get());
				tabData.accept(ModItems.MTF_ANTHEIA_5_KEYCARD_LV_5.get());
				tabData.accept(ModItems.MTF_UMBRA_6_LV_2.get());
				tabData.accept(ModItems.MTF_UMBRA_6_LV_1.get());
				tabData.accept(ModItems.MTF_UMBRA_6_LV_3.get());
				tabData.accept(ModItems.ALPHA_1_KEYCARD_LV_4.get());
				tabData.accept(ModItems.ALPHA_1_KEYCARD_LV_5.get());
				tabData.accept(ModItems.ALPHA_1_KEYCARD_LV_6.get());
				tabData.accept(ModItems.EPSILON_11_KEYCARD_LV_2.get());
				tabData.accept(ModItems.EPSILON_11_KEYCARD_LV_3.get());
				tabData.accept(ModItems.EPSILON_11_KEYCARD_LV_4.get());
				tabData.accept(ModItems.EPSILON_11_KEYCARD_LV_5.get());
				tabData.accept(ModItems.EPSILON_11_KEYCARD_LV_6.get());
				tabData.accept(ModItems.GAMMA_8_KEYCARD_LV_3.get());
				tabData.accept(ModItems.GAMMA_8_KEYCARD_LV_4.get());
				tabData.accept(ModItems.GAMMA_8_KEYCARD_LV_5.get());
				tabData.accept(ModItems.GAMMA_8_KEYCARD_LV_6.get());
				tabData.accept(ModItems.NU_7_KEYCARD_LV_2.get());
				tabData.accept(ModItems.NU_7_KEYCARD_LV_3.get());
				tabData.accept(ModItems.NU_7_KEYCARD_LV_4.get());
				tabData.accept(ModItems.NU_7_KEYCARD_LV_5.get());
				tabData.accept(ModItems.NU_7_KEYCARD_LV_6.get());
				tabData.accept(ModItems.OMEGA_1_KEYCARD_LV_1.get());
				tabData.accept(ModItems.OMEGA_1_KEYCARD_LV_2.get());
				tabData.accept(ModItems.OMEGA_1_KEYCARD_LV_3.get());
				tabData.accept(ModItems.OMEGA_1_KEYCARD_LV_4.get());
				tabData.accept(ModItems.OMEGA_1_KEYCARD_LV_5.get());
				tabData.accept(ModItems.OMEGA_1_KEYCARD_LV_6.get());
				tabData.accept(ModItems.RESH_1_KEYCARD_LV_4.get());
				tabData.accept(ModItems.RESH_1_KEYCARD_LV_5.get());
				tabData.accept(ModItems.RESH_1_KEYCARD_LV_6.get());
				tabData.accept(ModItems.ZETA_19_KEYCARD_LV_2.get());
				tabData.accept(ModItems.ZETA_19_KEYCARD_LV_3.get());
				tabData.accept(ModItems.ZETA_19_KEYCARD_LV_4.get());
				tabData.accept(ModItems.ZETA_19_KEYCARD_LV_5.get());
				tabData.accept(ModItems.MTF_RANK_1_LV_6.get());
				tabData.accept(ModItems.MTF_RANK_1_LV_3.get());
				tabData.accept(ModItems.MTF_RANK_1_LV_4.get());
				tabData.accept(ModItems.MTF_RANK_1_LV_5.get());
				tabData.accept(ModItems.MTF_RANK_2_LV_3.get());
				tabData.accept(ModItems.MTF_RANK_2_LV_4.get());
				tabData.accept(ModItems.MTF_RANK_2_LV_5.get());
				tabData.accept(ModItems.MTF_RANK_3_LV_2.get());
				tabData.accept(ModItems.MTF_RANK_3_LV_3.get());
				tabData.accept(ModItems.MTF_RANK_3_LV_4.get());
			}).withSearchBar().build());
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BUILDING_BLOCKS = REGISTRY.register("building_blocks",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.scp_genesis.building_blocks")).icon(() -> new ItemStack(ModBlocks.CLEAN_WHITE_WALL.get())).displayItems((parameters, tabData) -> {
				tabData.accept(ModItems.CLEAN_WHITE_WALL.get());
				tabData.accept(ModItems.BROKEN_WHITE_WALL_ONE.get());
				tabData.accept(ModItems.BROKEN_WHITE_WALL_TWO.get());
				tabData.accept(ModItems.DIRTY_BROKEN_WHITE_WALL_ONE.get());
				tabData.accept(ModItems.DIRTY_BROKEN_WHITE_WALL_TWO.get());
				tabData.accept(ModItems.WALL_BASE.get());
				tabData.accept(ModItems.GREEN_TILED_FLOOR.get());
			}).withSearchBar().withTabsBefore(CUSTOM_ITEMS.getId()).build());
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> COPYCAT_BLOCKS = REGISTRY.register("copycat_blocks",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.scp_genesis.copycat_blocks")).icon(() -> new ItemStack(ModBlocks.COPYCAT_CUBE.get())).displayItems((parameters, tabData) -> {
				tabData.accept(ModItems.COPYCAT_WRENCH.get());
				tabData.accept(ModItems.COPYCAT_SCRAPER.get());
				tabData.accept(ModItems.COPYCAT_REMOVER.get());
				tabData.accept(ModItems.COPYCAT_CUBE.get());
				tabData.accept(ModItems.COPYCAT_SLAB.get());
				// tabData.accept(ModItems.COPYCAT_STAIRS.get());
			}).withSearchBar().withTabsBefore(BUILDING_BLOCKS.getId()).build());
}