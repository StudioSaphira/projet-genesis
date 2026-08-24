package net.scp_genesis.common.registry;

import net.minecraft.world.item.Item;

import net.scp_genesis.common.copycatblocks.item.CopycatRemoverItem;
import net.scp_genesis.common.copycatblocks.item.CopycatScraperItem;
import net.scp_genesis.common.copycatblocks.item.CopycatWrenchItem;
import net.scp_genesis.common.platform.PlatformRegistry;
import net.scp_genesis.common.platform.PlatformRegistryObject;
import net.scp_genesis.common.scps.item.cards.id.IDCardItem;
import net.scp_genesis.common.scps.item.cards.keycards.ExternalKeycardLv0Item;
import net.scp_genesis.common.scps.item.cards.keycards.O5KeycardItem;
import net.scp_genesis.common.scps.item.cards.keycards.admin.*;
import net.scp_genesis.common.scps.item.cards.keycards.ethics.*;
import net.scp_genesis.common.scps.item.cards.keycards.iia.*;
import net.scp_genesis.common.scps.item.cards.keycards.internal.*;
import net.scp_genesis.common.scps.item.cards.keycards.logistics.LogisticsKeycardLv0Item;
import net.scp_genesis.common.scps.item.cards.keycards.logistics.LogisticsKeycardLv1Item;
import net.scp_genesis.common.scps.item.cards.keycards.logistics.LogisticsKeycardLv2Item;
import net.scp_genesis.common.scps.item.cards.keycards.logistics.LogisticsKeycardLv3Item;
import net.scp_genesis.common.scps.item.cards.keycards.mjd.MJDKeycardLv1Item;
import net.scp_genesis.common.scps.item.cards.keycards.mjd.MJDKeycardLv2Item;
import net.scp_genesis.common.scps.item.cards.keycards.mjd.MJDKeycardLv3Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.*;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.alpha1.MTFAlpha1KeycardLv4Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.alpha1.MTFAlpha1KeycardLv5Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.alpha1.MTFAlpha1KeycardLv6Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.epsilon11.*;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.gamma8.MTFGamma8KeycardLv3Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.gamma8.MTFGamma8KeycardLv4Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.gamma8.MTFGamma8KeycardLv5Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.gamma8.MTFGamma8KeycardLv6Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.nu7.*;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.omega1.*;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.resh1.MTFResh1KeycardLv4Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.resh1.MTFResh1KeycardLv5Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.resh1.MTFResh1KeycardLv6Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.umbra6.MTFUmbra6KeycardLv1Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.umbra6.MTFUmbra6KeycardLv2Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.umbra6.MTFUmbra6KeycardLv3Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.zeta19.MTFZeta19KeycardLv2Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.zeta19.MTFZeta19KeycardLv3Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.zeta19.MTFZeta19KeycardLv4Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.zeta19.MTFZeta19KeycardLv5Item;
import net.scp_genesis.common.scps.item.cards.keycards.raisa.*;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.antheia5.MTFAntheia5KeycardLv3Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.antheia5.MTFAntheia5KeycardLv4Item;
import net.scp_genesis.common.scps.item.cards.keycards.mtf.antheia5.MTFAntheia5KeycardLv5Item;

public final class ModItems {

	private ModItems() {}

	public static PlatformRegistryObject<Item> ID_CARD;
	public static PlatformRegistryObject<Item> KEYCARD_ADMIN_LV6, KEYCARD_ADMIN_LV5, KEYCARD_ADMIN_LV4, KEYCARD_ADMIN_LV3, KEYCARD_ADMIN_LV2, KEYCARD_ADMIN_LV1;
	public static PlatformRegistryObject<Item> KEYCARD_D_CLASS_LV0;
	public static PlatformRegistryObject<Item> KEYCARD_EC_LV6, KEYCARD_EC_LV5, KEYCARD_EC_LV4, KEYCARD_EC_LV3, KEYCARD_EC_LV2, KEYCARD_EC_LV1;
	public static PlatformRegistryObject<Item> KEYCARD_EXTERNAL_LV0;
	public static PlatformRegistryObject<Item> KEYCARD_IIA_LV6, KEYCARD_IIA_LV5, KEYCARD_IIA_LV4, KEYCARD_IIA_LV3, KEYCARD_IIA_LV2, KEYCARD_IIA_LV1;
	public static PlatformRegistryObject<Item> KEYCARD_INTERNAL_LV6, KEYCARD_INTERNAL_LV5, KEYCARD_INTERNAL_LV4, KEYCARD_INTERNAL_LV3, KEYCARD_INTERNAL_LV2, KEYCARD_INTERNAL_LV1;
	public static PlatformRegistryObject<Item> KEYCARD_LOGISTICS_LV3, KEYCARD_LOGISTICS_LV2, KEYCARD_LOGISTICS_LV1, KEYCARD_LOGISTICS_LV0;
	public static PlatformRegistryObject<Item> KEYCARD_MJD_LV3, KEYCARD_MJD_LV2, KEYCARD_MJD_LV1;
	public static PlatformRegistryObject<Item> KEYCARD_MTF_ALPHA1_LV6, KEYCARD_MTF_ALPHA1_LV5, KEYCARD_MTF_ALPHA1_LV4;
	public static PlatformRegistryObject<Item> KEYCARD_MTF_ANTHEIA5_LV5, KEYCARD_MTF_ANTHEIA5_LV4, KEYCARD_MTF_ANTHEIA5_LV3;
	public static PlatformRegistryObject<Item> KEYCARD_MTF_EPSILON11_LV6, KEYCARD_MTF_EPSILON11_LV5, KEYCARD_MTF_EPSILON11_LV4, KEYCARD_MTF_EPSILON11_LV3, KEYCARD_MTF_EPSILON11_LV2;
	public static PlatformRegistryObject<Item> KEYCARD_MTF_GAMMA8_LV6, KEYCARD_MTF_GAMMA8_LV5, KEYCARD_MTF_GAMMA8_LV4, KEYCARD_MTF_GAMMA8_LV3;
	public static PlatformRegistryObject<Item> KEYCARD_MTF_NU7_LV6, KEYCARD_MTF_NU7_LV5, KEYCARD_MTF_NU7_LV4, KEYCARD_MTF_NU7_LV3, KEYCARD_MTF_NU7_LV2;
	public static PlatformRegistryObject<Item> KEYCARD_MTF_OMEGA1_LV6, KEYCARD_MTF_OMEGA1_LV5, KEYCARD_MTF_OMEGA1_LV4, KEYCARD_MTF_OMEGA1_LV3, KEYCARD_MTF_OMEGA1_LV2, KEYCARD_MTF_OMEGA1_LV1;
	public static PlatformRegistryObject<Item> KEYCARD_MTF_GENERIC_RANK1_LV6, KEYCARD_MTF_GENERIC_RANK1_LV5, KEYCARD_MTF_GENERIC_RANK1_LV4, KEYCARD_MTF_GENERIC_RANK1_LV3, KEYCARD_MTF_GENERIC_RANK1_LV2;
	public static PlatformRegistryObject<Item> KEYCARD_MTF_GENERIC_RANK2_LV5, KEYCARD_MTF_GENERIC_RANK2_LV4, KEYCARD_MTF_GENERIC_RANK2_LV3, KEYCARD_MTF_GENERIC_RANK2_LV2;
	public static PlatformRegistryObject<Item> KEYCARD_MTF_GENERIC_RANK3_LV4, KEYCARD_MTF_GENERIC_RANK3_LV3, KEYCARD_MTF_GENERIC_RANK3_LV2, KEYCARD_MTF_GENERIC_RANK3_LV1;
	public static PlatformRegistryObject<Item> KEYCARD_MTF_RESH1_LV6, KEYCARD_MTF_RESH1_LV5, KEYCARD_MTF_RESH1_LV4;
	public static PlatformRegistryObject<Item> KEYCARD_MTF_UMBRA6_LV3, KEYCARD_MTF_UMBRA6_LV2, KEYCARD_MTF_UMBRA6_LV1;
	public static PlatformRegistryObject<Item> KEYCARD_MTF_ZETA19_LV5, KEYCARD_MTF_ZETA19_LV4, KEYCARD_MTF_ZETA19_LV3, KEYCARD_MTF_ZETA19_LV2;
	public static PlatformRegistryObject<Item> KEYCARD_O5;
	public static PlatformRegistryObject<Item> KEYCARD_RAISA_LV6, KEYCARD_RAISA_LV5, KEYCARD_RAISA_LV4, KEYCARD_RAISA_LV3, KEYCARD_RAISA_LV2;

	public static PlatformRegistryObject<Item> COPYCAT_WRENCH, COPYCAT_SCRAPER, COPYCAT_REMOVER;

	public static PlatformRegistryObject<Item> CLEAN_WHITE_WALL;
	public static PlatformRegistryObject<Item> BROKEN_WHITE_WALL_ONE;
	public static PlatformRegistryObject<Item> BROKEN_WHITE_WALL_TWO;
	public static PlatformRegistryObject<Item> DIRTY_BROKEN_WHITE_WALL_ONE;
	public static PlatformRegistryObject<Item> DIRTY_BROKEN_WHITE_WALL_TWO;
	public static PlatformRegistryObject<Item> WALL_BASE;
	public static PlatformRegistryObject<Item> GREEN_TILED_FLOOR;

	public static PlatformRegistryObject<Item> COPYCAT_CUBE;
	public static PlatformRegistryObject<Item> COPYCAT_STAIRS;
	public static PlatformRegistryObject<Item> COPYCAT_SLAB;
	public static PlatformRegistryObject<Item> COPYCAT_SLOPE;

	public static void register(PlatformRegistry registry) {
		COPYCAT_WRENCH = registry.registerItem("copycat_wrench", CopycatWrenchItem::new);
		COPYCAT_SCRAPER = registry.registerItem("copycat_scraper", CopycatScraperItem::new);
		COPYCAT_REMOVER = registry.registerItem("copycat_remover", CopycatRemoverItem::new);

		ID_CARD = registry.registerItem("id_card", IDCardItem::new);

		KEYCARD_ADMIN_LV6 = registry.registerItem("keycard_admin_lv6", AdminKeycardsLv6Item::new);
		KEYCARD_ADMIN_LV5 = registry.registerItem("keycard_admin_lv5", AdminKeycardsLv5Item::new);
		KEYCARD_ADMIN_LV4 = registry.registerItem("keycard_admin_lv4", AdminKeycardsLv4Item::new);
		KEYCARD_ADMIN_LV3 = registry.registerItem("keycard_admin_lv3", AdminKeycardsLv3Item::new);
		KEYCARD_ADMIN_LV2 = registry.registerItem("keycard_admin_lv2", AdminKeycardsLv2Item::new);
		KEYCARD_ADMIN_LV1 = registry.registerItem("keycard_admin_lv1", AdminKeycardsLv1Item::new);

		KEYCARD_D_CLASS_LV0 = registry.registerItem("keycard_dclass_lv0", DClassKeycardLv0Item::new);

		KEYCARD_EC_LV6 = registry.registerItem("keycard_ec_lv6", EthicsCommitteeKeycardLv6Item::new);
		KEYCARD_EC_LV5 = registry.registerItem("keycard_ec_lv5", EthicsCommitteeKeycardLv5Item::new);
		KEYCARD_EC_LV4 = registry.registerItem("keycard_ec_lv4", EthicsCommitteeKeycardLv4Item::new);
		KEYCARD_EC_LV3 = registry.registerItem("keycard_ec_lv3", EthicsCommitteeKeycardLv3Item::new);
		KEYCARD_EC_LV2 = registry.registerItem("keycard_ec_lv2", EthicsCommitteeKeycardLv2Item::new);
		KEYCARD_EC_LV1 = registry.registerItem("keycard_ec_lv1", EthicsCommitteeKeycardLv1Item::new);

		KEYCARD_EXTERNAL_LV0 = registry.registerItem("keycard_external_lv0", ExternalKeycardLv0Item::new);

		KEYCARD_IIA_LV6 = registry.registerItem("keycard_iia_lv6", IIAKeycardLv6Item::new);
		KEYCARD_IIA_LV5 = registry.registerItem("keycard_iia_lv5", IIAKeycardLv5Item::new);
		KEYCARD_IIA_LV4 = registry.registerItem("keycard_iia_lv4", IIAKeycardLv4Item::new);
		KEYCARD_IIA_LV3 = registry.registerItem("keycard_iia_lv3", IIAKeycardLv3Item::new);
		KEYCARD_IIA_LV2 = registry.registerItem("keycard_iia_lv2", IIAKeycardLv2Item::new);
		KEYCARD_IIA_LV1 = registry.registerItem("keycard_iia_lv1", IIAKeycardLv1Item::new);

		KEYCARD_INTERNAL_LV6 = registry.registerItem("keycard_internal_lv6", InternalKeycardLv6Item::new);
		KEYCARD_INTERNAL_LV5 = registry.registerItem("keycard_internal_lv5", InternalKeycardLv5Item::new);
		KEYCARD_INTERNAL_LV4 = registry.registerItem("keycard_internal_lv4", InternalKeycardLv4Item::new);
		KEYCARD_INTERNAL_LV3 = registry.registerItem("keycard_internal_lv3", InternalKeycardLv3Item::new);
		KEYCARD_INTERNAL_LV2 = registry.registerItem("keycard_internal_lv2", InternalKeycardLv2Item::new);
		KEYCARD_INTERNAL_LV1 = registry.registerItem("keycard_internal_lv1", InternalKeycardLv1Item::new);

		KEYCARD_LOGISTICS_LV3 = registry.registerItem("keycard_logistics_lv3", LogisticsKeycardLv3Item::new);
		KEYCARD_LOGISTICS_LV2 = registry.registerItem("keycard_logistics_lv2", LogisticsKeycardLv2Item::new);
		KEYCARD_LOGISTICS_LV1 = registry.registerItem("keycard_logistics_lv1", LogisticsKeycardLv1Item::new);
		KEYCARD_LOGISTICS_LV0 = registry.registerItem("keycard_logistics_lv0", LogisticsKeycardLv0Item::new);

		KEYCARD_MJD_LV3 = registry.registerItem("keycard_mjd_lv3", MJDKeycardLv3Item::new);
		KEYCARD_MJD_LV2 = registry.registerItem("keycard_mjd_lv2", MJDKeycardLv2Item::new);
		KEYCARD_MJD_LV1	= registry.registerItem("keycard_mjd_lv1", MJDKeycardLv1Item::new);

		KEYCARD_MTF_ALPHA1_LV6 = registry.registerItem("keycard_mtf_alpha1_lv6", MTFAlpha1KeycardLv6Item::new);
		KEYCARD_MTF_ALPHA1_LV5 = registry.registerItem("keycard_mtf_alpha1_lv5", MTFAlpha1KeycardLv5Item::new);
		KEYCARD_MTF_ALPHA1_LV4 = registry.registerItem("keycard_mtf_alpha1_lv4", MTFAlpha1KeycardLv4Item::new);

		KEYCARD_MTF_ANTHEIA5_LV5 = registry.registerItem("keycard_mtf_antheia5_lv5", MTFAntheia5KeycardLv5Item::new);
		KEYCARD_MTF_ANTHEIA5_LV4 = registry.registerItem("keycard_mtf_antheia5_lv4", MTFAntheia5KeycardLv4Item::new);
		KEYCARD_MTF_ANTHEIA5_LV3 = registry.registerItem("keycard_mtf_antheia5_lv3", MTFAntheia5KeycardLv3Item::new);

		KEYCARD_MTF_EPSILON11_LV6 = registry.registerItem("keycard_mtf_epsilon11_lv6", MTFEpsilon11KeycardLv6Item::new);
		KEYCARD_MTF_EPSILON11_LV5 = registry.registerItem("keycard_mtf_epsilon11_lv5", MTFEpsilon11KeycardLv5Item::new);
		KEYCARD_MTF_EPSILON11_LV4 = registry.registerItem("keycard_mtf_epsilon11_lv4", MTFEpsilon11KeycardLv4Item::new);
		KEYCARD_MTF_EPSILON11_LV3 = registry.registerItem("keycard_mtf_epsilon11_lv3", MTFEpsilon11KeycardLv3Item::new);
		KEYCARD_MTF_EPSILON11_LV2 = registry.registerItem("keycard_mtf_epsilon11_lv2", MTFEpsilon11KeycardLv2Item::new);

		KEYCARD_MTF_GAMMA8_LV6 = registry.registerItem("keycard_mtf_gamma8_lv6", MTFGamma8KeycardLv6Item::new);
		KEYCARD_MTF_GAMMA8_LV5 = registry.registerItem("keycard_mtf_gamma8_lv5", MTFGamma8KeycardLv5Item::new);
		KEYCARD_MTF_GAMMA8_LV4 = registry.registerItem("keycard_mtf_gamma8_lv4", MTFGamma8KeycardLv4Item::new);
		KEYCARD_MTF_GAMMA8_LV3 = registry.registerItem("keycard_mtf_gamma8_lv3", MTFGamma8KeycardLv3Item::new);

		KEYCARD_MTF_NU7_LV6 = registry.registerItem("keycard_mtf_nu7_lv6", MTFNu7KeycardLv6Item::new);
		KEYCARD_MTF_NU7_LV5 = registry.registerItem("keycard_mtf_nu7_lv5", MTFNu7KeycardLv5Item::new);
		KEYCARD_MTF_NU7_LV4 = registry.registerItem("keycard_mtf_nu7_lv4", MTFNu7KeycardLv4Item::new);
		KEYCARD_MTF_NU7_LV3 = registry.registerItem("keycard_mtf_nu7_lv3", MTFNu7KeycardLv3Item::new);
		KEYCARD_MTF_NU7_LV2 = registry.registerItem("keycard_mtf_nu7_lv2", MTFNu7KeycardLv2Item::new);

		KEYCARD_MTF_OMEGA1_LV6 = registry.registerItem("keycard_mtf_omega1_lv6", MTFOmega1KeycardLv6Item::new);
		KEYCARD_MTF_OMEGA1_LV5 = registry.registerItem("keycard_mtf_omega1_lv5", MTFOmega1KeycardLv5Item::new);
		KEYCARD_MTF_OMEGA1_LV4 = registry.registerItem("keycard_mtf_omega1_lv4", MTFOmega1KeycardLv4Item::new);
		KEYCARD_MTF_OMEGA1_LV3 = registry.registerItem("keycard_mtf_omega1_lv3", MTFOmega1KeycardLv3Item::new);
		KEYCARD_MTF_OMEGA1_LV2 = registry.registerItem("keycard_mtf_omega1_lv2", MTFOmega1KeycardLv2Item::new);
		KEYCARD_MTF_OMEGA1_LV1 = registry.registerItem("keycard_mtf_omega1_lv1", MTFOmega1KeycardLv1Item::new);

		KEYCARD_MTF_GENERIC_RANK1_LV6 = registry.registerItem("keycard_mtf_generic_rank1_lv6", MTFGenericRank1KeycardLv6Item::new);
		KEYCARD_MTF_GENERIC_RANK1_LV5 = registry.registerItem("keycard_mtf_generic_rank1_lv5", MTFGenericRank1KeycardLv5Item::new);
		KEYCARD_MTF_GENERIC_RANK1_LV4 = registry.registerItem("keycard_mtf_generic_rank1_lv4", MTFGenericRank1KeycardLv4Item::new);
		KEYCARD_MTF_GENERIC_RANK1_LV3 = registry.registerItem("keycard_mtf_generic_rank1_lv3", MTFGenericRank1KeycardLv3Item::new);
		KEYCARD_MTF_GENERIC_RANK1_LV2 = registry.registerItem("keycard_mtf_generic_rank1_lv2", MTFGenericRank1KeycardLv2Item::new);

		KEYCARD_MTF_GENERIC_RANK2_LV5 = registry.registerItem("keycard_mtf_generic_rank2_lv5", MTFGenericRank2KeycardLv5Item::new);
		KEYCARD_MTF_GENERIC_RANK2_LV4 = registry.registerItem("keycard_mtf_generic_rank2_lv4", MTFGenericRank2KeycardLv4Item::new);
		KEYCARD_MTF_GENERIC_RANK2_LV3 = registry.registerItem("keycard_mtf_generic_rank2_lv3", MTFGenericRank2KeycardLv3Item::new);
		KEYCARD_MTF_GENERIC_RANK2_LV2 = registry.registerItem("keycard_mtf_generic_rank2_lv2", MTFGenericRank2KeycardLv2Item::new);

		KEYCARD_MTF_GENERIC_RANK3_LV4 = registry.registerItem("keycard_mtf_generic_rank3_lv4", MTFGenericRank3KeycardLv4Item::new);
		KEYCARD_MTF_GENERIC_RANK3_LV3 = registry.registerItem("keycard_mtf_generic_rank3_lv3", MTFGenericRank3KeycardLv3Item::new);
		KEYCARD_MTF_GENERIC_RANK3_LV2 = registry.registerItem("keycard_mtf_generic_rank3_lv2", MTFGenericRank3KeycardLv2Item::new);
		KEYCARD_MTF_GENERIC_RANK3_LV1 = registry.registerItem("keycard_mtf_generic_rank3_lv1", MTFGenericRank3KeycardLv1Item::new);

		KEYCARD_MTF_RESH1_LV6 = registry.registerItem("keycard_mtf_resh1_lv6", MTFResh1KeycardLv6Item::new);
		KEYCARD_MTF_RESH1_LV5 = registry.registerItem("keycard_mtf_resh1_lv5", MTFResh1KeycardLv5Item::new);
		KEYCARD_MTF_RESH1_LV4 = registry.registerItem("keycard_mtf_resh1_lv4", MTFResh1KeycardLv4Item::new);

		KEYCARD_MTF_UMBRA6_LV3 = registry.registerItem("keycard_mtf_umbra6_lv3", MTFUmbra6KeycardLv3Item::new);
		KEYCARD_MTF_UMBRA6_LV2 = registry.registerItem("keycard_mtf_umbra6_lv2", MTFUmbra6KeycardLv2Item::new);
		KEYCARD_MTF_UMBRA6_LV1 = registry.registerItem("keycard_mtf_umbra6_lv1", MTFUmbra6KeycardLv1Item::new);

		KEYCARD_MTF_ZETA19_LV5 = registry.registerItem("keycard_mtf_zeta19_lv5", MTFZeta19KeycardLv5Item::new);
		KEYCARD_MTF_ZETA19_LV4 = registry.registerItem("keycard_mtf_zeta19_lv4", MTFZeta19KeycardLv4Item::new);
		KEYCARD_MTF_ZETA19_LV3 = registry.registerItem("keycard_mtf_zeta19_lv3", MTFZeta19KeycardLv3Item::new);
		KEYCARD_MTF_ZETA19_LV2 = registry.registerItem("keycard_mtf_zeta19_lv2", MTFZeta19KeycardLv2Item::new);

		KEYCARD_O5 = registry.registerItem("keycard_o5",  O5KeycardItem::new);

		KEYCARD_RAISA_LV6 = registry.registerItem("keycard_raisa_lv6", RAISAKeycardLv6Item::new);
		KEYCARD_RAISA_LV5 = registry.registerItem("keycard_raisa_lv5", RAISAKeycardLv5Item::new);
		KEYCARD_RAISA_LV4 = registry.registerItem("keycard_raisa_lv4", RAISAKeycardLv4Item::new);
		KEYCARD_RAISA_LV3 = registry.registerItem("keycard_raisa_lv3", RAISAKeycardLv3Item::new);
		KEYCARD_RAISA_LV2 = registry.registerItem("keycard_raisa_lv2", RAISAKeycardLv2Item::new);

		CLEAN_WHITE_WALL = registry.registerBlockItem("clean_white_wall", ModBlocks.CLEAN_WHITE_WALL);
		BROKEN_WHITE_WALL_ONE = registry.registerBlockItem("broken_white_wall_one", ModBlocks.BROKEN_WHITE_WALL_ONE);
		BROKEN_WHITE_WALL_TWO = registry.registerBlockItem("broken_white_wall_two", ModBlocks.BROKEN_WHITE_WALL_TWO);
		DIRTY_BROKEN_WHITE_WALL_ONE = registry.registerBlockItem("dirty_broken_white_wall_one", ModBlocks.DIRTY_BROKEN_WHITE_WALL_ONE);
		DIRTY_BROKEN_WHITE_WALL_TWO = registry.registerBlockItem("dirty_broken_white_wall_two", ModBlocks.DIRTY_BROKEN_WHITE_WALL_TWO);
		WALL_BASE = registry.registerBlockItem("wall_base", ModBlocks.WALL_BASE);
		GREEN_TILED_FLOOR = registry.registerBlockItem("green_tiled_floor", ModBlocks.GREEN_TILED_FLOOR);

		COPYCAT_CUBE = registry.registerBlockItem("copycat_cube", ModBlocks.COPYCAT_CUBE);
		COPYCAT_STAIRS = registry.registerBlockItem("copycat_stairs", ModBlocks.COPYCAT_STAIRS);
		COPYCAT_SLAB = registry.registerBlockItem("copycat_slab", ModBlocks.COPYCAT_SLAB);
		COPYCAT_SLOPE = registry.registerBlockItem("copycat_slope", ModBlocks.COPYCAT_SLOPE);
	}
}