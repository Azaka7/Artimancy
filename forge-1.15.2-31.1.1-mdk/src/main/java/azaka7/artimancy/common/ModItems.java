package azaka7.artimancy.common;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;

import com.google.common.collect.ImmutableList;

import azaka7.artimancy.common.item.SimpleArmorItem;
import azaka7.artimancy.common.item.ToolTier;
import azaka7.artimancy.common.item.ArmorShieldItem;
import azaka7.artimancy.common.item.ModSwordItem;
import azaka7.artimancy.common.item.MiscItem;
import azaka7.artimancy.common.item.ModArmorMaterial;
import azaka7.artimancy.common.item.ModAxeItem;
import azaka7.artimancy.common.item.ModHoeItem;
import azaka7.artimancy.common.item.ModPickaxeItem;
import azaka7.artimancy.common.item.ModShovelItem;

public class ModItems {
	
	private final ImmutableList<Item> modItems;
	
	private static final ModItems INSTANCE = new ModItems();
	public static final ModItems instance(){ return INSTANCE; }
	
	public final Item ore_chunk_iron, ore_chunk_gold, ore_chunk_silver, ore_chunk_copper, ore_chunk_nickel, 
		ore_chunk_platinum, ore_chunk_lead, ore_chunk_chromite, ore_chunk_cobalt, ore_chunk_tin, ore_chunk_titanium, ore_chunk_zinc;
	public final Item ore_chunk_uranium;
	public final Item slag;
	public final Item sulfur, saltpetre, graphite, stibnite;
	public final Item ceramic_tile;
	public final Item cast_arrow, cast_axe, cast_ball, cast_blade, cast_ingot, cast_nugget, cast_plate, cast_rod, cast_hilt, cast_pickaxe_head, cast_shovel_head, cast_hoe_head;
	public final Item cast_iron_plate, gold_plate, iron_plate, steel_plate;
	public final Item steel_ingot, steel_nugget, cast_iron_ingot, cast_iron_nugget, silver_ingot, silver_nugget;
	
	public final Item copper_ingot, nickel_ingot, platinum_ingot, lead_ingot, chromium_ingot, cobalt_ingot, tin_ingot, titanium_ingot;
	
	public final Item alexandrite, amazonite, amethyst, azurite, bloodstone, cassiterite, citrine, garnet, hematite, jade;
	public final Item kyanite, malachite, moonstone, obsidian, onyx, opal, pearl, peridot, ruby, sapphire, sunstone, topaz;//, turquoise;
	public final Item zircon_blue, zircon_red;
	public final Item adamant_shards, adamant_ingot, adamant_plate;
	
	public final ArmorItem steel_boots, steel_leggings, steel_chestplate, steel_helmet;
	public final ArmorItem steel_plated_boots, steel_plated_leggings, steel_plated_chestplate, steel_plated_helmet;
	public final ArmorItem iron_plated_boots, iron_plated_leggings, iron_plated_chestplate, iron_plated_helmet;
	public final ArmorItem castiron_plated_boots, castiron_plated_leggings, castiron_plated_chestplate, castiron_plated_helmet;
	public final ArmorItem gold_plated_boots, gold_plated_leggings, gold_plated_chestplate, gold_plated_helmet;
	
	public final ArmorShieldItem gold_tower_shield, iron_tower_shield, steel_tower_shield, cast_iron_tower_shield, adamant_tower_shield;
	
	public final ModSwordItem steel_sword, cast_iron_sword;
	public final ModPickaxeItem steel_pickaxe, cast_iron_pickaxe;
	public final ModAxeItem steel_axe, cast_iron_axe;
	public final ModShovelItem steel_shovel, cast_iron_shovel;
	public final ModHoeItem steel_hoe, cast_iron_hoe;
	
	//note: coal burns at 1200C (charcoal) - 1927C (perfect)
	
	private ModItems(){
		ImmutableList.Builder<Item> itemList = new ImmutableList.Builder<Item>();

		sulfur = new MiscItem("mineral_sulfur",ItemGroup.MATERIALS, itemList);
		saltpetre = new MiscItem("mineral_saltpetre",ItemGroup.MATERIALS, itemList);
		graphite = new MiscItem("graphite",ItemGroup.MATERIALS, itemList);

		//Stibnite - used for glitter/sparkles/pewter; used to make philosophic mercury; unstable, brings great change, amplify 
		stibnite = new MiscItem("stibnite",ItemGroup.MATERIALS, itemList);
		
		//Gold Ore, Silver Ore; no native gems
		ore_chunk_gold = new MiscItem("gold_ore_chunk",ItemGroup.MATERIALS, itemList);
		ore_chunk_silver = new MiscItem("silver_ore_chunk", ItemGroup.MATERIALS, itemList);
		
		//Copper Ore; Gem: Malachite - protection from evil & dark magic, improve/heal physical state; Gem: Azurite - 'stone of heaven', psychic powers, astrological power
		ore_chunk_copper = new MiscItem("copper_ore_chunk", ItemGroup.MATERIALS, itemList);
		ore_chunk_nickel = new MiscItem("nickel_ore_chunk", ItemGroup.MATERIALS, itemList);
		ore_chunk_platinum = new MiscItem("platinum_ore_chunk", ItemGroup.MATERIALS, itemList);
		ore_chunk_lead = new MiscItem("lead_ore_chunk", ItemGroup.MATERIALS, itemList);
		ore_chunk_chromite = new MiscItem("chromite_ore_chunk", ItemGroup.MATERIALS, itemList);
		ore_chunk_zinc = new MiscItem("zinc_ore_chunk", ItemGroup.MATERIALS, itemList);
		
		//Cobalt Ore; Drops from ore and geodes; Generates only in End; no native gems
		ore_chunk_cobalt = new MiscItem("cobalt_ore_chunk", ItemGroup.MATERIALS, itemList);
		
		//Tin Ore; Gem: Cassiterite - grounding, problem solving, guide souls after death
		ore_chunk_tin = new MiscItem("tin_ore_chunk", ItemGroup.MATERIALS, itemList);
		
		ore_chunk_titanium = new MiscItem("titanium_ore_chunk", ItemGroup.MATERIALS, itemList);
		
		//Uranium Ore; Not super useful. Weak passive heating. More heat when more is in the same place. Up to 300C without further refining
		ore_chunk_uranium = new MiscItem("uranium_ore_chunk", ItemGroup.MATERIALS, itemList);
		
		//Iron Ore; Gem: Hematite - healing (+ bloodstone), focus, grounding; Gem: Pyrite - manifestation, reflection, calming (+drops from lapis ore)
		ore_chunk_iron = new MiscItem("iron_ore_chunk",ItemGroup.MATERIALS, itemList);
		
		//Tungsten Ore; Drops from diamond, iron, and geodes; Generates only in Nether and End; Gem: Tungsten Carbide - hardness, crafted only
		
		slag = new MiscItem("slag",ItemGroup.MATERIALS, itemList);
		
		//Geode and Gemstones
		
		// -Diamond (in game) - plain,pink; protection from disease and magic, amplify both good and evil, success in battle
		// -Emerald (in game) - Holy, Luck, Healing
		// -Lapis Lazuli (in game) - Truth, Harmony, Friendship
		// -Quartz (in game) - clarity of thought and purpose, amplification of energy
		
		// -Alexandrite - Learning, Luck/Fortune, strengthen intuition
		alexandrite = new MiscItem("alexandrite",ItemGroup.MATERIALS, itemList);
		// -Amazonite - Gifted by Amazons, physical strength, physical healing
		amazonite = new MiscItem("amazonite",ItemGroup.MATERIALS, itemList);
		// -Amethyst - royalty, power, protects against intoxication
		amethyst = new MiscItem("amethyst",ItemGroup.MATERIALS, itemList);
		// -Azurite - see Copper Ore
		azurite = new MiscItem("azurite",ItemGroup.MATERIALS, itemList);
		// -Bloodstone - stone of martyrs, healing, protection; adds to Hematite
		bloodstone = new MiscItem("bloodstone",ItemGroup.MATERIALS, itemList);
		// -Cassiterite - see Tin Ore
		cassiterite = new MiscItem("cassiterite",ItemGroup.MATERIALS, itemList);
		// -Citrine - success, merchant stone, overcome fear
		citrine = new MiscItem("citrine",ItemGroup.MATERIALS, itemList);
		// -Garnet (Black) -  travel, success, healing
		garnet = new MiscItem("garnet",ItemGroup.MATERIALS, itemList);
		// -Hematite - see Iron Ore
		hematite = new MiscItem("hematite",ItemGroup.MATERIALS, itemList);
		// -Jade - imperial, value, prosperity, protection
		jade = new MiscItem("jade",ItemGroup.MATERIALS, itemList);
		// -Kyanite - Communication, Travel, super-natural abilities, cleansing of negative energy
		kyanite = new MiscItem("kyanite",ItemGroup.MATERIALS, itemList);
		// -Malachite - see Copper Ore
		malachite = new MiscItem("malachite",ItemGroup.MATERIALS, itemList);
		// -Moonstone - formed from moon-beams, fortune, fertility
		moonstone = new MiscItem("moonstone",ItemGroup.MATERIALS, itemList);
		// -Obsidian (gem) - truth, contact spirits, grounding
		obsidian = new MiscItem("obsidian",ItemGroup.MATERIALS, itemList);
		// -Onyx - inner strength, mental discipline, relieve negativity, unlucky
		onyx = new MiscItem("onyx",ItemGroup.MATERIALS, itemList);
		// -Opal - Normal, Fire, Moss; absorbing, amplify emotion, fire has karmic power, moss gives spiritual growth
		opal = new MiscItem("opal",ItemGroup.MATERIALS, itemList);
		// -Pearl - protection from fire and dragons, use in love potions, sadness/tears
		pearl = new MiscItem("pearl",ItemGroup.MATERIALS, itemList);
		// -Peridot - the "study stone", healing, focus, more receptive to learning
		peridot = new MiscItem("peridot", ItemGroup.MATERIALS,itemList);
		// -Ruby - peace, plenty, protection, heightened focus, natural energy of self
		ruby = new MiscItem("ruby",ItemGroup.MATERIALS, itemList);
		// -Sapphire - truth, loyalty, wisdom, protection from the night
		sapphire = new MiscItem("sapphire",ItemGroup.MATERIALS, itemList);
		// -Sunstone - luck, fortune, wealth, warmth
		sunstone = new MiscItem("sunstone",ItemGroup.MATERIALS, itemList);
		// -Topaz - physical strength, occasional invisibility
		topaz = new MiscItem("topaz",ItemGroup.MATERIALS, itemList);
		// -Turquoise - luck, protection, travel
		//turquoise = new ItemMisc("turquoise",ItemGroup.MATERIALS, itemList);
		// -Zircon - sleep, prosperous, grounding
		zircon_blue = new MiscItem("zircon_blue", ItemGroup.MATERIALS, itemList);
		zircon_red = new MiscItem("zircon_red", ItemGroup.MATERIALS, itemList);
		
		//Ingots, nuggets, plates
		adamant_plate = new MiscItem("adamant_plate",ItemGroup.MATERIALS, itemList);
		cast_iron_plate = new MiscItem("cast_iron_plate",ItemGroup.MATERIALS, itemList);
		gold_plate = new MiscItem("gold_plate",ItemGroup.MATERIALS, itemList);
		iron_plate = new MiscItem("iron_plate",ItemGroup.MATERIALS, itemList);
		steel_plate = new MiscItem("steel_plate",ItemGroup.MATERIALS, itemList);

		steel_ingot = new MiscItem("steel_ingot",ItemGroup.MATERIALS, itemList);
		steel_nugget = new MiscItem("steel_nugget",ItemGroup.MATERIALS, itemList);
		cast_iron_ingot = new MiscItem("cast_iron_ingot", ItemGroup.MATERIALS, itemList);
		cast_iron_nugget = new MiscItem("cast_iron_nugget", ItemGroup.MATERIALS, itemList);
		
		silver_ingot = new MiscItem("silver_ingot",ItemGroup.MATERIALS,itemList);
		silver_nugget = new MiscItem("silver_nugget",ItemGroup.MATERIALS,itemList);
		
		adamant_ingot = new MiscItem("adamant_ingot",ItemGroup.MATERIALS,itemList);
		adamant_shards = new MiscItem("adamant_shards",ItemGroup.MATERIALS,itemList);

		copper_ingot = new MiscItem("copper_ingot",ItemGroup.MATERIALS,itemList);
		nickel_ingot = new MiscItem("nickel_ingot",ItemGroup.MATERIALS,itemList);
		platinum_ingot = new MiscItem("platinum_ingot",ItemGroup.MATERIALS,itemList);
		lead_ingot = new MiscItem("lead_ingot",ItemGroup.MATERIALS,itemList);
		chromium_ingot = new MiscItem("chromium_ingot",ItemGroup.MATERIALS,itemList);
		cobalt_ingot = new MiscItem("cobalt_ingot",ItemGroup.MATERIALS,itemList);
		tin_ingot = new MiscItem("tin_ingot",ItemGroup.MATERIALS,itemList);
		titanium_ingot = new MiscItem("titanium_ingot",ItemGroup.MATERIALS,itemList);
		
		//TODO add following as recipes to cast furnace (No need for an alloy furnace!):
		/* Recipes:
		 * *Blast Furnace or w.iron + w.iron = 2 Steel + Slag
		 * *Normal Furnace or w.iron + charcoal = Cast Iron (durability-, protection ++)
		 * 3 w.iron + 1 tungsten ore (wolfram) = 3 Lycan Steel (durability+, magic resist ++, looks epic) + slag
		 * 3 steel + 1 cobalt = 4 Cerulian Steel (durability+, magic resist +, looks cool) + slag
		 * 3 steel + 1 chromium = 4 Viridian Steel (durability+, magic conduct +, magic resist +, looks envious)
		 * 3 steel + 1 cinnabar = 4 Sanguine Steel (durability+, magic conduct +, looks hot) + slag
		 * 2 w.iron + 3 obsidian shard = 2 Obstinite Steel (durability--, protection-, magic resist +++, toughness +, looks dark) + black slag (both slag and black dye)
		 * 2 w.iron + 1 nickel = 3 Invar (protection +, magic resist +) + slag
		 * 
		 * *1 w.iron + 3 adamant shards = 1 Adamant Ingot (for diamond armor/tools)
		 * 
		 * 3 copper + 1 tin = 4 Bronze (magic conduct +, magic resist -) + slag if ores
		 * 1 copper + 1 nickel = 2 Constantan (magic conduct -, magic resist +, mana gen from burn)
		 * 3 copper + 1 gold = 4 Corinthian Brass (magic conduct ++, magic resist --) + slag if ores
		 * 
		 * 5 tin + 1 stibnite (sparkly spikes) = 5 pewter (purely aesthetic, used for decorative blocks)
		 * 1 cinnabar + 1 stibnite = 1 philosophic mercury (used in crafting)
		 * 
		 * 3 nickel + 1 silver = 4 Cosmetic Silver (may be used in certain decorative blocks)
		 * 
		 * 1 gold + 1 silver = 2 Electrum (magic conduct +++, magic resist --) + slag if ores
		 * 
		 * 1 tungsten ore + 8 charcoal = 1 Wolfram Diamond (Tungsten Carbide, 'gem' form of tungsten) + slag (2x slag if coal instead of charcoal)
		 * 1 Wolfram Diamond + heat + time = Tungsten Nugget (it's really too bad coal and even lava can't get hot enough...)
		 * 
		 */
		
		//armor, tools, weapons, shields
		
		//Armor
		steel_helmet = new SimpleArmorItem("steel_helmet", ItemGroup.COMBAT, itemList, ModArmorMaterial.STEEL, EquipmentSlotType.HEAD);
		steel_chestplate = new SimpleArmorItem("steel_chestplate", ItemGroup.COMBAT, itemList, ModArmorMaterial.STEEL, EquipmentSlotType.CHEST);
		steel_leggings = new SimpleArmorItem("steel_leggings", ItemGroup.COMBAT, itemList, ModArmorMaterial.STEEL, EquipmentSlotType.LEGS);
		steel_boots = new SimpleArmorItem("steel_boots", ItemGroup.COMBAT, itemList, ModArmorMaterial.STEEL, EquipmentSlotType.FEET);

		steel_plated_helmet = new SimpleArmorItem("steel_plated_helmet", ItemGroup.COMBAT, itemList, ModArmorMaterial.STEEL_LEATHER, EquipmentSlotType.HEAD);
		steel_plated_chestplate = new SimpleArmorItem("steel_plated_chestplate", ItemGroup.COMBAT, itemList, ModArmorMaterial.STEEL_LEATHER, EquipmentSlotType.CHEST);
		steel_plated_leggings = new SimpleArmorItem("steel_plated_leggings", ItemGroup.COMBAT, itemList, ModArmorMaterial.STEEL_LEATHER, EquipmentSlotType.LEGS);
		steel_plated_boots = new SimpleArmorItem("steel_plated_boots", ItemGroup.COMBAT, itemList, ModArmorMaterial.STEEL_LEATHER, EquipmentSlotType.FEET);

		iron_plated_helmet = new SimpleArmorItem("iron_plated_helmet", ItemGroup.COMBAT, itemList, ModArmorMaterial.IRON_LEATHER, EquipmentSlotType.HEAD);
		iron_plated_chestplate = new SimpleArmorItem("iron_plated_chestplate", ItemGroup.COMBAT, itemList, ModArmorMaterial.IRON_LEATHER, EquipmentSlotType.CHEST);
		iron_plated_leggings = new SimpleArmorItem("iron_plated_leggings", ItemGroup.COMBAT, itemList, ModArmorMaterial.IRON_LEATHER, EquipmentSlotType.LEGS);
		iron_plated_boots = new SimpleArmorItem("iron_plated_boots", ItemGroup.COMBAT, itemList, ModArmorMaterial.IRON_LEATHER, EquipmentSlotType.FEET);

		castiron_plated_helmet = new SimpleArmorItem("cast_iron_plated_helmet", ItemGroup.COMBAT, itemList, ModArmorMaterial.CASTIRON_LEATHER, EquipmentSlotType.HEAD);
		castiron_plated_chestplate = new SimpleArmorItem("cast_iron_plated_chestplate", ItemGroup.COMBAT, itemList, ModArmorMaterial.CASTIRON_LEATHER, EquipmentSlotType.CHEST);
		castiron_plated_leggings = new SimpleArmorItem("cast_iron_plated_leggings", ItemGroup.COMBAT, itemList, ModArmorMaterial.CASTIRON_LEATHER, EquipmentSlotType.LEGS);
		castiron_plated_boots = new SimpleArmorItem("cast_iron_plated_boots", ItemGroup.COMBAT, itemList, ModArmorMaterial.CASTIRON_LEATHER, EquipmentSlotType.FEET);

		gold_plated_helmet = new SimpleArmorItem("gold_plated_helmet", ItemGroup.COMBAT, itemList, ModArmorMaterial.GOLD_LEATHER, EquipmentSlotType.HEAD);
		gold_plated_chestplate = new SimpleArmorItem("gold_plated_chestplate", ItemGroup.COMBAT, itemList, ModArmorMaterial.GOLD_LEATHER, EquipmentSlotType.CHEST);
		gold_plated_leggings = new SimpleArmorItem("gold_plated_leggings", ItemGroup.COMBAT, itemList, ModArmorMaterial.GOLD_LEATHER, EquipmentSlotType.LEGS);
		gold_plated_boots = new SimpleArmorItem("gold_plated_boots", ItemGroup.COMBAT, itemList, ModArmorMaterial.GOLD_LEATHER, EquipmentSlotType.FEET);

		//Shields
		iron_tower_shield = new ArmorShieldItem("iron_tower_shield", ArmorMaterial.IRON, ItemGroup.COMBAT, itemList, 1.0f);
		gold_tower_shield = new ArmorShieldItem("golden_tower_shield", ArmorMaterial.GOLD, ItemGroup.COMBAT, itemList, 1.0f);
		steel_tower_shield = new ArmorShieldItem("steel_tower_shield", ModArmorMaterial.STEEL, ItemGroup.COMBAT, itemList, 1.0f);
		cast_iron_tower_shield = new ArmorShieldItem("cast_iron_tower_shield", ModArmorMaterial.CASTIRON, ItemGroup.COMBAT, itemList, 1.0f);
		adamant_tower_shield = new ArmorShieldItem("adamant_tower_shield", ArmorMaterial.DIAMOND, ItemGroup.COMBAT, itemList, 1.0f);
		
		
		//Weapons
		itemList.add(steel_sword = new ModSwordItem("steel_sword", ToolTier.STEEL, ToolTier.STEEL));
		itemList.add(cast_iron_sword = new ModSwordItem("cast_iron_sword", ToolTier.CAST_IRON, ToolTier.CAST_IRON));
		
		IItemTier[] materials = new IItemTier[] {ItemTier.DIAMOND, ItemTier.GOLD, ItemTier.IRON, ToolTier.CAST_IRON, ToolTier.STEEL};
		
		for(int i = 0; i < materials.length; i++) {
			for(int j = 0; j < materials.length; j++) {
				if(i != j)
					itemList.add(new ModSwordItem(materials[i].toString().toLowerCase()+"_"+materials[j].toString().toLowerCase()+"_sword", materials[i], materials[j]));
			}
		}
		
		//Tools
		cast_iron_shovel = new ModShovelItem("cast_iron_shovel", ToolTier.CAST_IRON, 1.5F, -3.0F, ItemGroup.TOOLS, itemList);
		cast_iron_pickaxe = new ModPickaxeItem("cast_iron_pickaxe", ToolTier.CAST_IRON, 1, -2.8F, ItemGroup.TOOLS, itemList);
		cast_iron_axe = new ModAxeItem("cast_iron_axe", ToolTier.CAST_IRON, 5, -3.0F, ItemGroup.TOOLS, itemList);
		cast_iron_hoe = new ModHoeItem("cast_iron_hoe", ToolTier.CAST_IRON, -0.0F, ItemGroup.TOOLS, itemList);
		
		steel_shovel = new ModShovelItem("steel_shovel", ToolTier.STEEL, 1.5F, -3.0F, ItemGroup.TOOLS, itemList);
		steel_pickaxe = new ModPickaxeItem("steel_pickaxe", ToolTier.STEEL, 1, -2.8F, ItemGroup.TOOLS, itemList);
		steel_axe = new ModAxeItem("steel_axe", ToolTier.STEEL, 5, -3.0F, ItemGroup.TOOLS, itemList);
		steel_hoe = new ModHoeItem("steel_hoe", ToolTier.STEEL, -0.5F, ItemGroup.TOOLS, itemList);
		
		//Artifice, Artifice Table
		
		//Bows and Arrows
		
		//Tool Parts
		
		ceramic_tile = new MiscItem("cast_blank",ItemGroup.TOOLS, itemList);
		
		cast_arrow = new MiscItem("cast_arrow",ItemGroup.TOOLS, itemList);//"blank", "arrow", "axe", "ball", "blade", "hilt", "ingot", "nugget", "plate", "rod");
		cast_axe = new MiscItem("cast_axe",ItemGroup.TOOLS, itemList);
		cast_ball = new MiscItem("cast_ball",ItemGroup.TOOLS, itemList);
		cast_blade = new MiscItem("cast_blade",ItemGroup.TOOLS, itemList);
		cast_ingot = new MiscItem("cast_ingot",ItemGroup.TOOLS, itemList);
		cast_nugget = new MiscItem("cast_nugget",ItemGroup.TOOLS, itemList);
		cast_plate = new MiscItem("cast_plate",ItemGroup.TOOLS, itemList);
		cast_rod = new MiscItem("cast_rod",ItemGroup.TOOLS, itemList);
		cast_hilt = new MiscItem("cast_hilt",ItemGroup.TOOLS, itemList);
		cast_pickaxe_head = new MiscItem("cast_pickaxe_head",ItemGroup.TOOLS, itemList);
		cast_shovel_head = new MiscItem("cast_shovel_head", ItemGroup.TOOLS, itemList);
		cast_hoe_head = new MiscItem("cast_hoe_head", ItemGroup.TOOLS, itemList);
		
		new MiscItem("iron_blade",ItemGroup.TOOLS, itemList);
		new MiscItem("iron_hilt",ItemGroup.TOOLS, itemList);
		new MiscItem("golden_blade",ItemGroup.TOOLS, itemList);
		new MiscItem("golden_hilt",ItemGroup.TOOLS, itemList);
		new MiscItem("diamond_blade",ItemGroup.TOOLS, itemList);
		new MiscItem("diamond_hilt",ItemGroup.TOOLS, itemList);
		new MiscItem("cast_iron_blade",ItemGroup.TOOLS, itemList);
		new MiscItem("cast_iron_hilt",ItemGroup.TOOLS, itemList);
		new MiscItem("steel_blade",ItemGroup.TOOLS, itemList);
		new MiscItem("steel_hilt",ItemGroup.TOOLS, itemList);

		new MiscItem("iron_pickaxe_head",ItemGroup.TOOLS, itemList);
		new MiscItem("golden_pickaxe_head",ItemGroup.TOOLS, itemList);
		new MiscItem("diamond_pickaxe_head",ItemGroup.TOOLS, itemList);
		new MiscItem("cast_iron_pickaxe_head",ItemGroup.TOOLS, itemList);
		new MiscItem("steel_pickaxe_head",ItemGroup.TOOLS, itemList);

		new MiscItem("iron_axe_head",ItemGroup.TOOLS, itemList);
		new MiscItem("golden_axe_head",ItemGroup.TOOLS, itemList);
		new MiscItem("diamond_axe_head",ItemGroup.TOOLS, itemList);
		new MiscItem("cast_iron_axe_head",ItemGroup.TOOLS, itemList);
		new MiscItem("steel_axe_head",ItemGroup.TOOLS, itemList);

		new MiscItem("iron_shovel_head",ItemGroup.TOOLS, itemList);
		new MiscItem("golden_shovel_head",ItemGroup.TOOLS, itemList);
		new MiscItem("diamond_shovel_head",ItemGroup.TOOLS, itemList);
		new MiscItem("cast_iron_shovel_head",ItemGroup.TOOLS, itemList);
		new MiscItem("steel_shovel_head",ItemGroup.TOOLS, itemList);

		new MiscItem("iron_hoe_head",ItemGroup.TOOLS, itemList);
		new MiscItem("golden_hoe_head",ItemGroup.TOOLS, itemList);
		new MiscItem("diamond_hoe_head",ItemGroup.TOOLS, itemList);
		new MiscItem("cast_iron_hoe_head",ItemGroup.TOOLS, itemList);
		new MiscItem("steel_hoe_head",ItemGroup.TOOLS, itemList);
		
		modItems = itemList.build();
	}
	
	public final ImmutableList<Item> getModItemList(){
		return modItems;
	}

	public Item getItem(String itemID) {
		for(Item it : modItems) {
			if(it.getRegistryName().getPath().equals(itemID)) {
				return it;
			}
		}
		return null;
	}

}
