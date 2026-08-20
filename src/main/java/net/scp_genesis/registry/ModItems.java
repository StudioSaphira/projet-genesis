package net.scp_genesis.registry;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import net.scp_genesis.scps.item.cards.keycards.mtf.epsilon11.*;
import net.scp_genesis.scps.item.cards.keycards.mtf.antheia5.*;
import net.scp_genesis.scps.item.cards.keycards.mtf.alpha1.*;
import net.scp_genesis.scps.item.cards.keycards.mtf.gamma8.*;
import net.scp_genesis.scps.item.cards.keycards.mtf.omega1.*;
import net.scp_genesis.scps.item.cards.keycards.mtf.umbra6.*;
import net.scp_genesis.scps.item.cards.keycards.mtf.zeta19.*;
import net.scp_genesis.scps.item.cards.keycards.mtf.resh1.*;
import net.scp_genesis.scps.item.cards.keycards.mtf.nu7.*;
import net.scp_genesis.scps.item.cards.keycards.logistics.*;
import net.scp_genesis.scps.item.cards.keycards.internal.*;
import net.scp_genesis.scps.item.cards.keycards.ethics.*;
import net.scp_genesis.scps.item.cards.keycards.admin.*;
import net.scp_genesis.scps.item.cards.keycards.raisa.*;
import net.scp_genesis.scps.item.cards.keycards.iia.*;
import net.scp_genesis.scps.item.cards.keycards.mjd.*;
import net.scp_genesis.scps.item.cards.keycards.mtf.*;
import net.scp_genesis.scps.item.cards.keycards.*;
import net.scp_genesis.scps.item.cards.id.*;
import net.scp_genesis.copycatblocks.item.*;
import net.scp_genesis.constants.ModConstants;

public class ModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(ModConstants.MOD_ID);
	public static final DeferredItem<Item> ETHICS_COMMITTEE_KEYCARD_LV_6 = REGISTRY.register("ethics_committee_keycard_lv_6", EthicsCommitteeKeycardLv6Item::new);
	public static final DeferredItem<Item> ETHICS_COMMITTEE_KEYCARD_LV_5 = REGISTRY.register("ethics_committee_keycard_lv_5", EthicsCommitteeKeycardLv5Item::new);
	public static final DeferredItem<Item> ETHICS_COMMITTEE_KEYCARD_LV_4 = REGISTRY.register("ethics_committee_keycard_lv_4", EthicsCommitteeKeycardLv4Item::new);
	public static final DeferredItem<Item> ETHICS_COMMITTEE_KEYCARD_LV_3 = REGISTRY.register("ethics_committee_keycard_lv_3", EthicsCommitteeKeycardLv3Item::new);
	public static final DeferredItem<Item> ETHICS_COMMITTEE_KEYCARD_LV_2 = REGISTRY.register("ethics_committee_keycard_lv_2", EthicsCommitteeKeycardLv2Item::new);
	public static final DeferredItem<Item> ETHICS_COMMITTEE_KEYCARD_LV_1 = REGISTRY.register("ethics_committee_keycard_lv_1", EthicsCommitteeKeycardLv1Item::new);
	public static final DeferredItem<Item> RAISA_KEYCARD_LV_6 = REGISTRY.register("raisa_keycard_lv_6", RAISAKeycardLv6Item::new);
	public static final DeferredItem<Item> RAISA_KEYCARD_LV_5 = REGISTRY.register("raisa_keycard_lv_5", RAISAKeycardLv5Item::new);
	public static final DeferredItem<Item> RAISA_KEYCARD_LV_4 = REGISTRY.register("raisa_keycard_lv_4", RAISAKeycardLv4Item::new);
	public static final DeferredItem<Item> RAISA_KEYCARD_LV_3 = REGISTRY.register("raisa_keycard_lv_3", RAISAKeycardLv3Item::new);
	public static final DeferredItem<Item> RAISA_KEYCARD_LV_2 = REGISTRY.register("raisa_keycard_lv_2", RAISAKeycardLv2Item::new);
	public static final DeferredItem<Item> LOGISTICS_KEYCARD_LV_0 = REGISTRY.register("logistics_keycard_lv_0", LogisticsKeycardLv0Item::new);
	public static final DeferredItem<Item> LOGISTICS_KEYCARD_LV_1 = REGISTRY.register("logistics_keycard_lv_1", LogisticsKeycardLv1Item::new);
	public static final DeferredItem<Item> LOGISTICS_KEYCARD_LV_2 = REGISTRY.register("logistics_keycard_lv_2", LogisticsKeycardLv2Item::new);
	public static final DeferredItem<Item> LOGISTICS_KEYCARD_LV_3 = REGISTRY.register("logistics_keycard_lv_3", LogisticsKeycardLv3Item::new);
	public static final DeferredItem<Item> EXTERNAL_KEYCARD_LV_0 = REGISTRY.register("external_keycard_lv_0", ExternalKeycardLv0Item::new);
	public static final DeferredItem<Item> O5_KEYCARD = REGISTRY.register("o5_keycard", O5KeycardItem::new);
	public static final DeferredItem<Item> ADMIN_KEYCARDS_LV_1 = REGISTRY.register("admin_keycards_lv_1", AdminKeycardsLv1Item::new);
	public static final DeferredItem<Item> ADMIN_KEYCARDS_LV_2 = REGISTRY.register("admin_keycards_lv_2", AdminKeycardsLv2Item::new);
	public static final DeferredItem<Item> ADMIN_KEYCARDS_LV_3 = REGISTRY.register("admin_keycards_lv_3", AdminKeycardsLv3Item::new);
	public static final DeferredItem<Item> ADMIN_KEYCARDS_LV_4 = REGISTRY.register("admin_keycards_lv_4", AdminKeycardsLv4Item::new);
	public static final DeferredItem<Item> ADMIN_KEYCARDS_LV_5 = REGISTRY.register("admin_keycards_lv_5", AdminKeycardsLv5Item::new);
	public static final DeferredItem<Item> ADMIN_KEYCARDS_LV_6 = REGISTRY.register("admin_keycards_lv_6", AdminKeycardsLv6Item::new);
	public static final DeferredItem<Item> INTERNAL_KEYCARD_LV_1 = REGISTRY.register("internal_keycard_lv_1", InternalKeycardLv1Item::new);
	public static final DeferredItem<Item> INTERNAL_KEYCARD_LV_2 = REGISTRY.register("internal_keycard_lv_2", InternalKeycardLv2Item::new);
	public static final DeferredItem<Item> INTERNAL_KEYCARD_LV_3 = REGISTRY.register("internal_keycard_lv_3", InternalKeycardLv3Item::new);
	public static final DeferredItem<Item> INTERNAL_KEYCARD_LV_4 = REGISTRY.register("internal_keycard_lv_4", InternalKeycardLv4Item::new);
	public static final DeferredItem<Item> INTERNAL_KEYCARD_LV_5 = REGISTRY.register("internal_keycard_lv_5", InternalKeycardLv5Item::new);
	public static final DeferredItem<Item> INTERNAL_KEYCARD_LV_6 = REGISTRY.register("internal_keycard_lv_6", InternalKeycardLv6Item::new);
	public static final DeferredItem<Item> D_CLASS_KEYCARD_LV_0 = REGISTRY.register("d_class_keycard_lv_0", DClassKeycardLv0Item::new);
	public static final DeferredItem<Item> MJD_KEYCARD_LV_1 = REGISTRY.register("mjd_keycard_lv_1", MJDKeycardLv1Item::new);
	public static final DeferredItem<Item> MJD_KEYCARD_LV_2 = REGISTRY.register("mjd_keycard_lv_2", MJDKeycardLv2Item::new);
	public static final DeferredItem<Item> MJD_KEYCARD_LV_3 = REGISTRY.register("mjd_keycard_lv_3", MJDKeycardLv3Item::new);
	public static final DeferredItem<Item> MTF_RANK_1_LV_2 = REGISTRY.register("mtf_rank_1_lv_2", MTFRank1Lv2Item::new);
	public static final DeferredItem<Item> MTF_RANK_2_LV_2 = REGISTRY.register("mtf_rank_2_lv_2", MTFRank2Lv2Item::new);
	public static final DeferredItem<Item> MTF_RANK_3_LV_1 = REGISTRY.register("mtf_rank_3_lv_1", MTFRank3Lv1Item::new);
	public static final DeferredItem<Item> ID_CARD = REGISTRY.register("id_card", IDCardItem::new);
	public static final DeferredItem<Item> IIA_KEYCARD_LV_1 = REGISTRY.register("iia_keycard_lv_1", IIAKeycardLv1Item::new);
	public static final DeferredItem<Item> IIA_KEYCARD_LV_2 = REGISTRY.register("iia_keycard_lv_2", IIAKeycardLv2Item::new);
	public static final DeferredItem<Item> IIA_KEYCARD_LV_3 = REGISTRY.register("iia_keycard_lv_3", IIAKeycardLv3Item::new);
	public static final DeferredItem<Item> IIA_KEYCARD_LV_4 = REGISTRY.register("iia_keycard_lv_4", IIAKeycardLv4Item::new);
	public static final DeferredItem<Item> IIA_KEYCARD_LV_5 = REGISTRY.register("iia_keycard_lv_5", IIAKeycardLv5Item::new);
	public static final DeferredItem<Item> IIA_KEYCARD_LV_6 = REGISTRY.register("iia_keycard_lv_6", IIAKeycardLv6Item::new);
	public static final DeferredItem<Item> MTF_ANTHEIA_5_KEYCARD_LV_5 = REGISTRY.register("mtf_antheia_5_keycard_lv_5", MTFAntheia5KeycardLv5Item::new);
	public static final DeferredItem<Item> MTF_ANTHEIA_5_KEYCARD_LV_4 = REGISTRY.register("mtf_antheia_5_keycard_lv_4", MTFAntheia5KeycardLv4Item::new);
	public static final DeferredItem<Item> MTF_ANTHEIA_5_KEYCARD_LV_3 = REGISTRY.register("mtf_antheia_5_keycard_lv_3", MTFAntheia5KeycardLv3Item::new);
	public static final DeferredItem<Item> MTF_UMBRA_6_LV_3 = REGISTRY.register("mtf_umbra_6_lv_3", MTFUmbra6Lv3Item::new);
	public static final DeferredItem<Item> MTF_UMBRA_6_LV_2 = REGISTRY.register("mtf_umbra_6_lv_2", MTFUmbra6Lv2Item::new);
	public static final DeferredItem<Item> MTF_UMBRA_6_LV_1 = REGISTRY.register("mtf_umbra_6_lv_1", MTFUmbra6Lv1Item::new);
	public static final DeferredItem<Item> ALPHA_1_KEYCARD_LV_4 = REGISTRY.register("alpha_1_keycard_lv_4", Alpha1KeycardLv4Item::new);
	public static final DeferredItem<Item> ALPHA_1_KEYCARD_LV_5 = REGISTRY.register("alpha_1_keycard_lv_5", Alpha1KeycardLv5Item::new);
	public static final DeferredItem<Item> ALPHA_1_KEYCARD_LV_6 = REGISTRY.register("alpha_1_keycard_lv_6", Alpha1KeycardLv6Item::new);
	public static final DeferredItem<Item> EPSILON_11_KEYCARD_LV_2 = REGISTRY.register("epsilon_11_keycard_lv_2", Epsilon11KeycardLv2Item::new);
	public static final DeferredItem<Item> EPSILON_11_KEYCARD_LV_3 = REGISTRY.register("epsilon_11_keycard_lv_3", Epsilon11KeycardLv3Item::new);
	public static final DeferredItem<Item> EPSILON_11_KEYCARD_LV_4 = REGISTRY.register("epsilon_11_keycard_lv_4", Epsilon11KeycardLv4Item::new);
	public static final DeferredItem<Item> EPSILON_11_KEYCARD_LV_5 = REGISTRY.register("epsilon_11_keycard_lv_5", Epsilon11KeycardLv5Item::new);
	public static final DeferredItem<Item> EPSILON_11_KEYCARD_LV_6 = REGISTRY.register("epsilon_11_keycard_lv_6", Epsilon11KeycardLv6Item::new);
	public static final DeferredItem<Item> GAMMA_8_KEYCARD_LV_3 = REGISTRY.register("gamma_8_keycard_lv_3", Gamma8KeycardLv3Item::new);
	public static final DeferredItem<Item> GAMMA_8_KEYCARD_LV_4 = REGISTRY.register("gamma_8_keycard_lv_4", Gamma8KeycardLv4Item::new);
	public static final DeferredItem<Item> GAMMA_8_KEYCARD_LV_5 = REGISTRY.register("gamma_8_keycard_lv_5", Gamma8KeycardLv5Item::new);
	public static final DeferredItem<Item> GAMMA_8_KEYCARD_LV_6 = REGISTRY.register("gamma_8_keycard_lv_6", Gamma8KeycardLv6Item::new);
	public static final DeferredItem<Item> NU_7_KEYCARD_LV_2 = REGISTRY.register("nu_7_keycard_lv_2", Nu7KeycardLv2Item::new);
	public static final DeferredItem<Item> NU_7_KEYCARD_LV_3 = REGISTRY.register("nu_7_keycard_lv_3", Nu7KeycardLv3Item::new);
	public static final DeferredItem<Item> NU_7_KEYCARD_LV_4 = REGISTRY.register("nu_7_keycard_lv_4", Nu7KeycardLv4Item::new);
	public static final DeferredItem<Item> NU_7_KEYCARD_LV_5 = REGISTRY.register("nu_7_keycard_lv_5", Nu7KeycardLv5Item::new);
	public static final DeferredItem<Item> NU_7_KEYCARD_LV_6 = REGISTRY.register("nu_7_keycard_lv_6", Nu7KeycardLv6Item::new);
	public static final DeferredItem<Item> OMEGA_1_KEYCARD_LV_1 = REGISTRY.register("omega_1_keycard_lv_1", Omega1KeycardLv1Item::new);
	public static final DeferredItem<Item> OMEGA_1_KEYCARD_LV_2 = REGISTRY.register("omega_1_keycard_lv_2", Omega1KeycardLv2Item::new);
	public static final DeferredItem<Item> OMEGA_1_KEYCARD_LV_3 = REGISTRY.register("omega_1_keycard_lv_3", Omega1KeycardLv3Item::new);
	public static final DeferredItem<Item> OMEGA_1_KEYCARD_LV_4 = REGISTRY.register("omega_1_keycard_lv_4", Omega1KeycardLv4Item::new);
	public static final DeferredItem<Item> OMEGA_1_KEYCARD_LV_5 = REGISTRY.register("omega_1_keycard_lv_5", Omega1KeycardLv5Item::new);
	public static final DeferredItem<Item> OMEGA_1_KEYCARD_LV_6 = REGISTRY.register("omega_1_keycard_lv_6", Omega1KeycardLv6Item::new);
	public static final DeferredItem<Item> RESH_1_KEYCARD_LV_4 = REGISTRY.register("resh_1_keycard_lv_4", Resh1KeycardLv4Item::new);
	public static final DeferredItem<Item> RESH_1_KEYCARD_LV_5 = REGISTRY.register("resh_1_keycard_lv_5", Resh1KeycardLv5Item::new);
	public static final DeferredItem<Item> RESH_1_KEYCARD_LV_6 = REGISTRY.register("resh_1_keycard_lv_6", Resh1KeycardLv6Item::new);
	public static final DeferredItem<Item> ZETA_19_KEYCARD_LV_2 = REGISTRY.register("zeta_19_keycard_lv_2", Zeta19KeycardLv2Item::new);
	public static final DeferredItem<Item> ZETA_19_KEYCARD_LV_3 = REGISTRY.register("zeta_19_keycard_lv_3", Zeta19KeycardLv3Item::new);
	public static final DeferredItem<Item> ZETA_19_KEYCARD_LV_4 = REGISTRY.register("zeta_19_keycard_lv_4", Zeta19KeycardLv4Item::new);
	public static final DeferredItem<Item> ZETA_19_KEYCARD_LV_5 = REGISTRY.register("zeta_19_keycard_lv_5", Zeta19KeycardLv5Item::new);
	public static final DeferredItem<Item> MTF_RANK_1_LV_6 = REGISTRY.register("mtf_rank_1_lv_6", MTFRank1Lv6Item::new);
	public static final DeferredItem<Item> MTF_RANK_1_LV_3 = REGISTRY.register("mtf_rank_1_lv_3", MTFRank1Lv3Item::new);
	public static final DeferredItem<Item> MTF_RANK_1_LV_4 = REGISTRY.register("mtf_rank_1_lv_4", MTFRank1Lv4Item::new);
	public static final DeferredItem<Item> MTF_RANK_1_LV_5 = REGISTRY.register("mtf_rank_1_lv_5", MTFRank1Lv5Item::new);
	public static final DeferredItem<Item> MTF_RANK_2_LV_3 = REGISTRY.register("mtf_rank_2_lv_3", MTFRank2Lv3Item::new);
	public static final DeferredItem<Item> MTF_RANK_2_LV_4 = REGISTRY.register("mtf_rank_2_lv_4", MTFRank2Lv4Item::new);
	public static final DeferredItem<Item> MTF_RANK_2_LV_5 = REGISTRY.register("mtf_rank_2_lv_5", MTFRank2Lv5Item::new);
	public static final DeferredItem<Item> MTF_RANK_3_LV_2 = REGISTRY.register("mtf_rank_3_lv_2", MTFRank3Lv2Item::new);
	public static final DeferredItem<Item> MTF_RANK_3_LV_3 = REGISTRY.register("mtf_rank_3_lv_3", MTFRank3Lv3Item::new);
	public static final DeferredItem<Item> MTF_RANK_3_LV_4 = REGISTRY.register("mtf_rank_3_lv_4", MTFRank3Lv4Item::new);
	public static final DeferredItem<Item> CLEAN_WHITE_WALL = block(ModBlocks.CLEAN_WHITE_WALL);
	public static final DeferredItem<Item> BROKEN_WHITE_WALL_ONE = block(ModBlocks.BROKEN_WHITE_WALL_ONE);
	public static final DeferredItem<Item> BROKEN_WHITE_WALL_TWO = block(ModBlocks.BROKEN_WHITE_WALL_TWO);
	public static final DeferredItem<Item> DIRTY_BROKEN_WHITE_WALL_ONE = block(ModBlocks.DIRTY_BROKEN_WHITE_WALL_ONE);
	public static final DeferredItem<Item> DIRTY_BROKEN_WHITE_WALL_TWO = block(ModBlocks.DIRTY_BROKEN_WHITE_WALL_TWO);
	public static final DeferredItem<Item> WALL_BASE = block(ModBlocks.WALL_BASE);
	public static final DeferredItem<Item> GREEN_TILED_FLOOR = block(ModBlocks.GREEN_TILED_FLOOR);
	public static final DeferredItem<Item> COPYCAT_CUBE = block(ModBlocks.COPYCAT_CUBE);
	// public static final DeferredItem<Item> COPYCAT_STAIRS = block(ModBlocks.COPYCAT_STAIRS);
	public static final DeferredItem<Item> COPYCAT_SLAB = block(ModBlocks.COPYCAT_SLAB);
	public static final DeferredItem<Item> COPYCAT_WRENCH = REGISTRY.register("copycat_wrench", CopycatWrenchItem::new);
	public static final DeferredItem<Item> COPYCAT_SCRAPER = REGISTRY.register("copycat_scraper", CopycatScraperItem::new);
	public static final DeferredItem<Item> COPYCAT_REMOVER = REGISTRY.register("copycat_remover", CopycatRemoverItem::new);

	// Start of user code block custom items
	// End of user code block custom items
	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
		return block(block, new Item.Properties());
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block, Item.Properties properties) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), properties));
	}
}