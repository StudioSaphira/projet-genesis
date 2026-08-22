package net.scp_genesis.main.registry;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;

import net.scp_genesis.main.platform.PlatformRegistry;
import net.scp_genesis.main.platform.PlatformRegistryObject;

public final class ModTabs {

	private ModTabs() {}

	public static PlatformRegistryObject<CreativeModeTab> CUSTOM_ITEMS;
	public static PlatformRegistryObject<CreativeModeTab> BUILDING_BLOCKS;
	public static PlatformRegistryObject<CreativeModeTab> COPYCAT_BLOCKS;

	public static void register(
			PlatformRegistry registry
	) {

		CUSTOM_ITEMS =
				registry.registerCreativeModeTab(
						"custom_items",
						() -> CreativeModeTab.builder()
								.title(
										Component.translatable(
												"item_group.scp_genesis.custom_items"
										)
								)
								.icon(
										() -> new ItemStack(
												ModItems.KEYCARD_EC_LV5.get()
										)
								)
								.displayItems(
										(parameters, tabData) -> {

											tabData.accept(ModItems.ID_CARD.get());

											tabData.accept(ModItems.KEYCARD_EC_LV6.get());
											tabData.accept(ModItems.KEYCARD_EC_LV5.get());
											tabData.accept(ModItems.KEYCARD_EC_LV4.get());
											tabData.accept(ModItems.KEYCARD_EC_LV3.get());
											tabData.accept(ModItems.KEYCARD_EC_LV2.get());
											tabData.accept(ModItems.KEYCARD_EC_LV1.get());

											tabData.accept(ModItems.KEYCARD_RAISA_LV6.get());
											tabData.accept(ModItems.KEYCARD_RAISA_LV5.get());
											tabData.accept(ModItems.KEYCARD_RAISA_LV4.get());
											tabData.accept(ModItems.KEYCARD_RAISA_LV3.get());
											tabData.accept(ModItems.KEYCARD_RAISA_LV2.get());

											tabData.accept(ModItems.KEYCARD_LOGISTICS_LV0.get());
											tabData.accept(ModItems.KEYCARD_LOGISTICS_LV1.get());
											tabData.accept(ModItems.KEYCARD_LOGISTICS_LV2.get());
											tabData.accept(ModItems.KEYCARD_LOGISTICS_LV3.get());

											tabData.accept(ModItems.KEYCARD_EXTERNAL_LV0.get());
											tabData.accept(ModItems.KEYCARD_O5.get());

											tabData.accept(ModItems.KEYCARD_ADMIN_LV1.get());
											tabData.accept(ModItems.KEYCARD_ADMIN_LV2.get());
											tabData.accept(ModItems.KEYCARD_ADMIN_LV3.get());
											tabData.accept(ModItems.KEYCARD_ADMIN_LV4.get());
											tabData.accept(ModItems.KEYCARD_ADMIN_LV5.get());
											tabData.accept(ModItems.KEYCARD_ADMIN_LV6.get());

											tabData.accept(ModItems.KEYCARD_INTERNAL_LV1.get());
											tabData.accept(ModItems.KEYCARD_INTERNAL_LV2.get());
											tabData.accept(ModItems.KEYCARD_INTERNAL_LV3.get());
											tabData.accept(ModItems.KEYCARD_INTERNAL_LV4.get());
											tabData.accept(ModItems.KEYCARD_INTERNAL_LV5.get());
											tabData.accept(ModItems.KEYCARD_INTERNAL_LV6.get());

											tabData.accept(ModItems.KEYCARD_D_CLASS_LV0.get());

											tabData.accept(ModItems.KEYCARD_MJD_LV1.get());
											tabData.accept(ModItems.KEYCARD_MJD_LV2.get());
											tabData.accept(ModItems.KEYCARD_MJD_LV3.get());

											tabData.accept(ModItems.KEYCARD_IIA_LV1.get());
											tabData.accept(ModItems.KEYCARD_IIA_LV2.get());
											tabData.accept(ModItems.KEYCARD_IIA_LV3.get());
											tabData.accept(ModItems.KEYCARD_IIA_LV4.get());
											tabData.accept(ModItems.KEYCARD_IIA_LV5.get());
											tabData.accept(ModItems.KEYCARD_IIA_LV6.get());

											tabData.accept(ModItems.KEYCARD_MTF_ANTHEIA5_LV3.get());
											tabData.accept(ModItems.KEYCARD_MTF_ANTHEIA5_LV4.get());
											tabData.accept(ModItems.KEYCARD_MTF_ANTHEIA5_LV5.get());

											tabData.accept(ModItems.KEYCARD_MTF_UMBRA6_LV2.get());
											tabData.accept(ModItems.KEYCARD_MTF_UMBRA6_LV1.get());
											tabData.accept(ModItems.KEYCARD_MTF_UMBRA6_LV3.get());

											tabData.accept(ModItems.KEYCARD_MTF_ALPHA1_LV6.get());
											tabData.accept(ModItems.KEYCARD_MTF_ALPHA1_LV5.get());
											tabData.accept(ModItems.KEYCARD_MTF_ALPHA1_LV4.get());

											tabData.accept(ModItems.KEYCARD_MTF_EPSILON11_LV6.get());
											tabData.accept(ModItems.KEYCARD_MTF_EPSILON11_LV5.get());
											tabData.accept(ModItems.KEYCARD_MTF_EPSILON11_LV4.get());
											tabData.accept(ModItems.KEYCARD_MTF_EPSILON11_LV3.get());
											tabData.accept(ModItems.KEYCARD_MTF_EPSILON11_LV2.get());

											tabData.accept(ModItems.KEYCARD_MTF_GAMMA8_LV3.get());
											tabData.accept(ModItems.KEYCARD_MTF_GAMMA8_LV4.get());
											tabData.accept(ModItems.KEYCARD_MTF_GAMMA8_LV5.get());
											tabData.accept(ModItems.KEYCARD_MTF_GAMMA8_LV6.get());

											tabData.accept(ModItems.KEYCARD_MTF_NU7_LV2.get());
											tabData.accept(ModItems.KEYCARD_MTF_NU7_LV3.get());
											tabData.accept(ModItems.KEYCARD_MTF_NU7_LV4.get());
											tabData.accept(ModItems.KEYCARD_MTF_NU7_LV5.get());
											tabData.accept(ModItems.KEYCARD_MTF_NU7_LV6.get());

											tabData.accept(ModItems.KEYCARD_MTF_OMEGA1_LV1.get());
											tabData.accept(ModItems.KEYCARD_MTF_OMEGA1_LV2.get());
											tabData.accept(ModItems.KEYCARD_MTF_OMEGA1_LV3.get());
											tabData.accept(ModItems.KEYCARD_MTF_OMEGA1_LV4.get());
											tabData.accept(ModItems.KEYCARD_MTF_OMEGA1_LV5.get());
											tabData.accept(ModItems.KEYCARD_MTF_OMEGA1_LV6.get());

											tabData.accept(ModItems.KEYCARD_MTF_RESH1_LV4.get());
											tabData.accept(ModItems.KEYCARD_MTF_RESH1_LV5.get());
											tabData.accept(ModItems.KEYCARD_MTF_RESH1_LV6.get());

											tabData.accept(ModItems.KEYCARD_MTF_ZETA19_LV2.get());
											tabData.accept(ModItems.KEYCARD_MTF_ZETA19_LV3.get());
											tabData.accept(ModItems.KEYCARD_MTF_ZETA19_LV4.get());
											tabData.accept(ModItems.KEYCARD_MTF_ZETA19_LV5.get());

											tabData.accept(ModItems.KEYCARD_MTF_GENERIC_RANK1_LV4.get());
											tabData.accept(ModItems.KEYCARD_MTF_GENERIC_RANK1_LV2.get());
											tabData.accept(ModItems.KEYCARD_MTF_GENERIC_RANK1_LV3.get());
											tabData.accept(ModItems.KEYCARD_MTF_GENERIC_RANK1_LV6.get());
											tabData.accept(ModItems.KEYCARD_MTF_GENERIC_RANK1_LV5.get());

											tabData.accept(ModItems.KEYCARD_MTF_GENERIC_RANK2_LV4.get());
											tabData.accept(ModItems.KEYCARD_MTF_GENERIC_RANK2_LV5.get());
											tabData.accept(ModItems.KEYCARD_MTF_GENERIC_RANK2_LV3.get());
											tabData.accept(ModItems.KEYCARD_MTF_GENERIC_RANK2_LV2.get());

											tabData.accept(ModItems.KEYCARD_MTF_GENERIC_RANK3_LV1.get());
											tabData.accept(ModItems.KEYCARD_MTF_GENERIC_RANK3_LV2.get());
											tabData.accept(ModItems.KEYCARD_MTF_GENERIC_RANK3_LV3.get());
											tabData.accept(ModItems.KEYCARD_MTF_GENERIC_RANK3_LV4.get());
										}
								)
								.withSearchBar()
				);

		BUILDING_BLOCKS =
				registry.registerCreativeModeTab(
						"building_blocks",
						CUSTOM_ITEMS,
						() -> CreativeModeTab.builder()
								.title(
										Component.translatable(
												"item_group.scp_genesis.building_blocks"
										)
								)
								.icon(
										() -> new ItemStack(
												ModBlocks.CLEAN_WHITE_WALL.get()
										)
								)
								.displayItems(
										(parameters, tabData) -> {
											tabData.accept(ModItems.CLEAN_WHITE_WALL.get());
											tabData.accept(ModItems.BROKEN_WHITE_WALL_ONE.get());
											tabData.accept(ModItems.BROKEN_WHITE_WALL_TWO.get());
											tabData.accept(ModItems.DIRTY_BROKEN_WHITE_WALL_ONE.get());
											tabData.accept(ModItems.DIRTY_BROKEN_WHITE_WALL_TWO.get());
											tabData.accept(ModItems.WALL_BASE.get());
											tabData.accept(ModItems.GREEN_TILED_FLOOR.get());
										}
								)
								.withSearchBar()
				);

		COPYCAT_BLOCKS =
				registry.registerCreativeModeTab(
						"copycat_blocks",
						BUILDING_BLOCKS,
						() -> CreativeModeTab.builder()
								.title(
										Component.translatable(
												"item_group.scp_genesis.copycat_blocks"
										)
								)
								.icon(
										() -> new ItemStack(
												ModBlocks.COPYCAT_CUBE.get()
										)
								)
								.displayItems(
										(parameters, tabData) -> {
											tabData.accept(ModItems.COPYCAT_WRENCH.get());
											tabData.accept(ModItems.COPYCAT_SCRAPER.get());
											tabData.accept(ModItems.COPYCAT_REMOVER.get());
											tabData.accept(ModItems.COPYCAT_CUBE.get());
											tabData.accept(ModItems.COPYCAT_STAIRS.get());
											tabData.accept(ModItems.COPYCAT_SLAB.get());
										}
								)
								.withSearchBar()
				);
	}
}