package azaka7.artimancy.common;

import com.google.common.collect.ImmutableMap;

import azaka7.artimancy.common.block.CastFurnaceBlock;
import azaka7.artimancy.common.block.LuminBlock;
import azaka7.artimancy.common.block.MiscBlock;
import azaka7.artimancy.common.block.MiscStairsBlock;
import azaka7.artimancy.common.block.RotatedBlock;
import azaka7.artimancy.common.block.SlabsBlock;
import azaka7.artimancy.common.block.StoneyBlock;
import azaka7.artimancy.common.block.MushroomTopBlock;
import azaka7.artimancy.common.block.MushroomyBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;

public class ModBlocks {
	
	private final ImmutableMap<Block, ItemGroup> blockList;
	
	private static final ModBlocks INSTANCE = new ModBlocks();
	public static final ModBlocks instance(){ return INSTANCE; }
	
	public final Block hardened_bricks, warstone_bricks, coldstone_bricks;
	public final Block chromite_ore; //chromium_block
	public final Block cobalt_ore; //cobalt_block
	public final Block copper_ore; //copper_block
	public final Block lead_ore, lead_block;
	public final Block nickel_ore; //nickel_block
	public final Block platinum_ore; //platinum_block
	public final Block silver_ore, silver_block;
	public final Block tin_ore; //tin_block
	public final Block titanium_ore; //titanium_block
	public final Block uranium_ore; //uranium_block //sounds dangerous...
	public final Block coalstone, permafrost;
	public final Block andesite_geode, basalt_geode, diorite_geode, end_stone_geode, gabbro_geode, granite_geode, komatiite_geode, magma_geode, marble_geode, dunite_geode, 
						netherrack_geode, obsidian_geode, permafrost_geode, quartzite_geode, red_quartzite_geode, sandstone_geode, stone_geode, terracotta_geode;
	public final Block jade_block, jade_brick, jade_circle_brick, jade_cobblestone, jade_pent_brick, jade_square_brick, jade_stone;
	public final Block jade_pillar, jade_tile_pillar;
	public final Block cast_furnace;
	
	public final Block jade_stone_slab, jade_cobblestone_slab, jade_brick_slab, jade_tile_slab, jade_pentbrick_slab, jade_slab;
	public final Block jade_stone_stairs, jade_cobblestone_stairs, jade_brick_stairs, jade_tile_stairs, jade_pentbrick_stairs, jade_stairs;
	
	public final Block basalt, dunite, gabbro, komatiite, limestone, marble, mudstone, quartzite, red_quartzite, schist, shale;
	public final Block dunite_netherrack, marble_pillar, marble_brick;
	public final Block polished_basalt, polished_dunite, polished_gabbro, polished_komatiite, polished_marble, polished_mudstone, polished_schist;
	
	public final Block andesite_copper_ore, andesite_iron_ore, andesite_zircon_ore;
	public final Block basalt_diamond_ore, basalt_iron_ore, basalt_nickel_ore, basalt_redstone_ore, basalt_titanium_ore;
	public final Block diorite_iron_ore, diorite_quartz_ore, diorite_titanium_ore, diorite_zircon_ore;
	public final Block dunite_chromium_ore, dunite_diamond_ore, dunite_iron_ore, dunite_nickel_ore;
	public final Block gabbro_cobalt_ore, gabbro_copper_ore, gabbro_gold_ore, gabbro_iron_ore, gabbro_nickel_ore, gabbro_platinum_ore, gabbro_silver_ore;
	public final Block granite_iron_ore, granite_quartz_ore, granite_uranium_ore;
	public final Block komatiite_chromite_ore, komatiite_cobalt_ore, komatiite_diamond_ore, komatiite_gold_ore, komatiite_nickel_ore;
	public final Block limestone_coal_ore, limestone_copper_ore, limestone_iron_ore, limestone_lead_ore, limestone_tin_ore;
	public final Block mudstone_coal_ore, mudstone_copper_ore, mudstone_lead_ore, mudstone_tin_ore;
	public final Block quartzite_copper_ore, quartzite_ore, red_quartzite_copper_ore, red_quartzite_iron_ore, red_quartzite_ore;
	public final Block red_sandstone_gold_ore, red_sandstone_lead_ore, red_sandstone_tin_ore, sandstone_coal_ore, sandstone_copper_ore, sandstone_lead_ore, sandstone_tin_ore;
	public final Block schist_graphite_ore, schist_nickel_ore, shale_iron_ore, shale_tin_ore;
	
	public final Block slag_block;
	
	public final Block white_mushroom, white_mushroom_block;
	
	public final Block lumin_block;
	
	private ModBlocks(){
		System.out.println("Instantiating mod blocks...");
		ImmutableMap.Builder<Block, ItemGroup> blockMap = new ImmutableMap.Builder<Block, ItemGroup>();

		//plants and plant-like blocks
		
		white_mushroom = (new MushroomyBlock("white_mushroom", (Properties.create(Material.PLANTS)).doesNotBlockMovement().tickRandomly().sound(SoundType.PLANT)));
		blockMap.put(white_mushroom, ItemGroup.DECORATIONS);
		
		white_mushroom_block = (new MushroomTopBlock("white_mushroom_block", (Properties.create(Material.WOOD, MaterialColor.WHITE_TERRACOTTA)).hardnessAndResistance(0.5F, 1.0F).sound(SoundType.WOOD)));
		blockMap.put(white_mushroom_block, ItemGroup.DECORATIONS);
		
		//specialty stones and ores
		
		andesite_geode = (new MiscBlock("andesite_geode", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(andesite_geode, ItemGroup.BUILDING_BLOCKS);
		
		andesite_copper_ore = (new MiscBlock("andesite_copper_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(andesite_copper_ore, ItemGroup.BUILDING_BLOCKS);
		
		andesite_iron_ore = (new MiscBlock("andesite_iron_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(andesite_iron_ore, ItemGroup.BUILDING_BLOCKS);
		
		andesite_zircon_ore = (new MiscBlock("andesite_zircon_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(andesite_zircon_ore, ItemGroup.BUILDING_BLOCKS);
		
		basalt = (new RotatedBlock("basalt", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(basalt, ItemGroup.BUILDING_BLOCKS);
		
		polished_basalt = (new MiscBlock("polished_basalt", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(polished_basalt, ItemGroup.BUILDING_BLOCKS);
		
		basalt_geode = (new MiscBlock("basalt_geode", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(basalt_geode, ItemGroup.BUILDING_BLOCKS);
		
		basalt_diamond_ore = (new MiscBlock("basalt_diamond_ore", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(basalt_diamond_ore, ItemGroup.BUILDING_BLOCKS); 
		
		basalt_iron_ore = (new MiscBlock("basalt_iron_ore", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(basalt_iron_ore, ItemGroup.BUILDING_BLOCKS);
		
		basalt_nickel_ore = (new MiscBlock("basalt_nickel_ore", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(basalt_nickel_ore, ItemGroup.BUILDING_BLOCKS); 
		
		basalt_redstone_ore = (new MiscBlock("basalt_redstone_ore", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(basalt_redstone_ore, ItemGroup.BUILDING_BLOCKS); 
		
		basalt_titanium_ore = (new MiscBlock("basalt_titanium_ore", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(basalt_titanium_ore, ItemGroup.BUILDING_BLOCKS); 
		
		coalstone = (new MiscBlock("coalstone", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 4.0F)));
		blockMap.put(coalstone, ItemGroup.BUILDING_BLOCKS); 

		coldstone_bricks = (new MiscBlock("coldstone_bricks", (Properties.create(Material.ROCK, MaterialColor.ICE)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(coldstone_bricks, ItemGroup.BUILDING_BLOCKS); 

		copper_ore = (new MiscBlock("copper_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(copper_ore, ItemGroup.BUILDING_BLOCKS); 
		
		diorite_geode = (new MiscBlock("diorite_geode", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(diorite_geode, ItemGroup.BUILDING_BLOCKS);
		
		diorite_iron_ore = (new MiscBlock("diorite_iron_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(diorite_iron_ore, ItemGroup.BUILDING_BLOCKS);
		
		diorite_quartz_ore = (new MiscBlock("diorite_quartz_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(diorite_quartz_ore, ItemGroup.BUILDING_BLOCKS);
		
		diorite_titanium_ore = (new MiscBlock("diorite_titanium_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(diorite_titanium_ore, ItemGroup.BUILDING_BLOCKS);
		
		diorite_zircon_ore = (new MiscBlock("diorite_zircon_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(diorite_zircon_ore, ItemGroup.BUILDING_BLOCKS);

		dunite = (new MiscBlock("dunite", (Properties.create(Material.ROCK, MaterialColor.GREEN_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(dunite, ItemGroup.BUILDING_BLOCKS);

		dunite_chromium_ore = (new MiscBlock("dunite_chromium_ore", (Properties.create(Material.ROCK, MaterialColor.GREEN_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(dunite_chromium_ore, ItemGroup.BUILDING_BLOCKS);

		dunite_diamond_ore = (new MiscBlock("dunite_diamond_ore", (Properties.create(Material.ROCK, MaterialColor.GREEN_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(dunite_diamond_ore, ItemGroup.BUILDING_BLOCKS);

		dunite_iron_ore = (new MiscBlock("dunite_iron_ore", (Properties.create(Material.ROCK, MaterialColor.GREEN_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(dunite_iron_ore, ItemGroup.BUILDING_BLOCKS);

		dunite_nickel_ore = (new MiscBlock("dunite_nickel_ore", (Properties.create(Material.ROCK, MaterialColor.GREEN_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(dunite_nickel_ore, ItemGroup.BUILDING_BLOCKS);
		
		polished_dunite = (new MiscBlock("polished_dunite", (Properties.create(Material.ROCK, MaterialColor.GREEN_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(polished_dunite, ItemGroup.BUILDING_BLOCKS);
		
		dunite_netherrack = (new MiscBlock("dunite_netherrack", (Properties.create(Material.ROCK, MaterialColor.NETHERRACK)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(dunite_netherrack, ItemGroup.BUILDING_BLOCKS);

		dunite_geode = (new MiscBlock("dunite_geode", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(dunite_geode, ItemGroup.BUILDING_BLOCKS);
		
		end_stone_geode = (new MiscBlock("end_stone_geode", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(end_stone_geode, ItemGroup.BUILDING_BLOCKS);
		
		gabbro = (new MiscBlock("gabbro", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(gabbro, ItemGroup.BUILDING_BLOCKS);
		
		polished_gabbro = (new MiscBlock("polished_gabbro", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(polished_gabbro, ItemGroup.BUILDING_BLOCKS);
		
		gabbro_geode = (new MiscBlock("gabbro_geode", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(gabbro_geode, ItemGroup.BUILDING_BLOCKS);
		
		gabbro_cobalt_ore = (new MiscBlock("gabbro_cobalt_ore", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(gabbro_cobalt_ore, ItemGroup.BUILDING_BLOCKS);
		
		gabbro_copper_ore = (new MiscBlock("gabbro_copper_ore", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(gabbro_copper_ore, ItemGroup.BUILDING_BLOCKS);
		
		gabbro_gold_ore = (new MiscBlock("gabbro_gold_ore", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(gabbro_gold_ore, ItemGroup.BUILDING_BLOCKS);
		
		gabbro_iron_ore = (new MiscBlock("gabbro_iron_ore", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(gabbro_iron_ore, ItemGroup.BUILDING_BLOCKS);
		
		gabbro_nickel_ore = (new MiscBlock("gabbro_nickel_ore", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(gabbro_nickel_ore, ItemGroup.BUILDING_BLOCKS);
		
		gabbro_platinum_ore = (new MiscBlock("gabbro_platinum_ore", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(gabbro_platinum_ore, ItemGroup.BUILDING_BLOCKS);
		
		gabbro_silver_ore = (new MiscBlock("gabbro_silver_ore", (Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(gabbro_silver_ore, ItemGroup.BUILDING_BLOCKS);
		
		granite_geode = (new MiscBlock("granite_geode", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(granite_geode, ItemGroup.BUILDING_BLOCKS);
		
		granite_iron_ore = (new MiscBlock("granite_iron_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(granite_iron_ore, ItemGroup.BUILDING_BLOCKS);
		
		granite_quartz_ore = (new MiscBlock("granite_quartz_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(granite_quartz_ore, ItemGroup.BUILDING_BLOCKS);
		
		granite_uranium_ore = (new MiscBlock("granite_uranium_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(granite_uranium_ore, ItemGroup.BUILDING_BLOCKS);
		
		komatiite = (new MiscBlock("komatiite", (Properties.create(Material.ROCK, MaterialColor.LIME_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(komatiite, ItemGroup.BUILDING_BLOCKS);
		
		polished_komatiite = (new MiscBlock("polished_komatiite", (Properties.create(Material.ROCK, MaterialColor.LIME_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(polished_komatiite, ItemGroup.BUILDING_BLOCKS);

		komatiite_geode = (new MiscBlock("komatiite_geode", (Properties.create(Material.ROCK, MaterialColor.LIME_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(komatiite_geode, ItemGroup.BUILDING_BLOCKS);
		
		komatiite_chromite_ore = (new MiscBlock("komatiite_chromite_ore", (Properties.create(Material.ROCK, MaterialColor.LIME_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(komatiite_chromite_ore, ItemGroup.BUILDING_BLOCKS);
		
		komatiite_cobalt_ore = (new MiscBlock("komatiite_cobalt_ore", (Properties.create(Material.ROCK, MaterialColor.LIME_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(komatiite_cobalt_ore, ItemGroup.BUILDING_BLOCKS);
		
		komatiite_diamond_ore = (new MiscBlock("komatiite_diamond_ore", (Properties.create(Material.ROCK, MaterialColor.LIME_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(komatiite_diamond_ore, ItemGroup.BUILDING_BLOCKS);
		
		komatiite_gold_ore = (new MiscBlock("komatiite_gold_ore", (Properties.create(Material.ROCK, MaterialColor.LIME_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(komatiite_gold_ore, ItemGroup.BUILDING_BLOCKS);
		
		komatiite_nickel_ore = (new MiscBlock("komatiite_nickel_ore", (Properties.create(Material.ROCK, MaterialColor.LIME_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(komatiite_nickel_ore, ItemGroup.BUILDING_BLOCKS);
		
		limestone = (new MiscBlock("limestone", (Properties.create(Material.ROCK, MaterialColor.LIME)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(limestone, ItemGroup.BUILDING_BLOCKS);
		
		limestone_coal_ore = (new MiscBlock("limestone_coal_ore", (Properties.create(Material.ROCK, MaterialColor.LIME)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(limestone_coal_ore, ItemGroup.BUILDING_BLOCKS);
		
		limestone_copper_ore = (new MiscBlock("limestone_copper_ore", (Properties.create(Material.ROCK, MaterialColor.LIME)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(limestone_copper_ore, ItemGroup.BUILDING_BLOCKS);
		
		limestone_iron_ore = (new MiscBlock("limestone_iron_ore", (Properties.create(Material.ROCK, MaterialColor.LIME)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(limestone_iron_ore, ItemGroup.BUILDING_BLOCKS);
		
		limestone_lead_ore = (new MiscBlock("limestone_lead_ore", (Properties.create(Material.ROCK, MaterialColor.LIME)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(limestone_lead_ore, ItemGroup.BUILDING_BLOCKS);
		
		limestone_tin_ore = (new MiscBlock("limestone_tin_ore", (Properties.create(Material.ROCK, MaterialColor.LIME)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(limestone_tin_ore, ItemGroup.BUILDING_BLOCKS);
		
		magma_geode = (new MiscBlock("magma_geode", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(magma_geode, ItemGroup.BUILDING_BLOCKS);
		
		marble = (new MiscBlock("marble", (Properties.create(Material.ROCK, MaterialColor.QUARTZ)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(marble, ItemGroup.BUILDING_BLOCKS);
		
		polished_marble = (new MiscBlock("polished_marble", (Properties.create(Material.ROCK, MaterialColor.QUARTZ)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(polished_marble, ItemGroup.BUILDING_BLOCKS);
		
		marble_brick = (new MiscBlock("marble_bricks", (Properties.create(Material.ROCK, MaterialColor.QUARTZ)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(marble_brick, ItemGroup.BUILDING_BLOCKS);
		
		marble_pillar = (new RotatedBlock("marble_pillar", (Properties.create(Material.ROCK, MaterialColor.QUARTZ)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(marble_pillar, ItemGroup.BUILDING_BLOCKS);

		marble_geode = (new MiscBlock("marble_geode", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(marble_geode, ItemGroup.BUILDING_BLOCKS);
		
		mudstone = (new MiscBlock("mudstone", (Properties.create(Material.ROCK, MaterialColor.BROWN_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(mudstone, ItemGroup.BUILDING_BLOCKS);
		
		polished_mudstone = (new MiscBlock("polished_mudstone", (Properties.create(Material.ROCK, MaterialColor.BROWN_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(polished_mudstone, ItemGroup.BUILDING_BLOCKS);
		
		mudstone_coal_ore = (new MiscBlock("mudstone_coal_ore", (Properties.create(Material.ROCK, MaterialColor.BROWN_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(mudstone_coal_ore, ItemGroup.BUILDING_BLOCKS);
		
		mudstone_copper_ore = (new MiscBlock("mudstone_copper_ore", (Properties.create(Material.ROCK, MaterialColor.BROWN_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(mudstone_copper_ore, ItemGroup.BUILDING_BLOCKS);
		
		mudstone_lead_ore = (new MiscBlock("mudstone_lead_ore", (Properties.create(Material.ROCK, MaterialColor.BROWN_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(mudstone_lead_ore, ItemGroup.BUILDING_BLOCKS);
		
		mudstone_tin_ore = (new MiscBlock("mudstone_tin_ore", (Properties.create(Material.ROCK, MaterialColor.BROWN_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(mudstone_tin_ore, ItemGroup.BUILDING_BLOCKS);
		
		netherrack_geode = (new MiscBlock("netherrack_geode", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(netherrack_geode, ItemGroup.BUILDING_BLOCKS);

		obsidian_geode = (new MiscBlock("obsidian_geode", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F).harvestLevel(3)));
		blockMap.put(obsidian_geode, ItemGroup.BUILDING_BLOCKS);

		permafrost = (new MiscBlock("permafrost", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F).harvestLevel(0)));
		blockMap.put(permafrost, ItemGroup.BUILDING_BLOCKS);
		
		permafrost_geode = (new MiscBlock("permafrost_geode", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F).harvestLevel(0)));
		blockMap.put(permafrost_geode, ItemGroup.BUILDING_BLOCKS);
		
		quartzite = (new MiscBlock("quartzite", (Properties.create(Material.ROCK, MaterialColor.SAND)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(quartzite, ItemGroup.BUILDING_BLOCKS);

		quartzite_geode = (new MiscBlock("quartzite_geode", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(quartzite_geode, ItemGroup.BUILDING_BLOCKS);
		
		quartzite_copper_ore = (new MiscBlock("quartzite_copper_ore", (Properties.create(Material.ROCK, MaterialColor.SAND)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(quartzite_copper_ore, ItemGroup.BUILDING_BLOCKS); 
		
		quartzite_ore = (new MiscBlock("quartzite_ore", (Properties.create(Material.ROCK, MaterialColor.SAND)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(quartzite_ore, ItemGroup.BUILDING_BLOCKS);
		
		red_quartzite = (new MiscBlock("red_quartzite", (Properties.create(Material.ROCK, MaterialColor.ORANGE_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(red_quartzite, ItemGroup.BUILDING_BLOCKS);

		red_quartzite_geode = (new MiscBlock("red_quartzite_geode", (Properties.create(Material.ROCK, MaterialColor.ORANGE_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(red_quartzite_geode, ItemGroup.BUILDING_BLOCKS);

		red_quartzite_copper_ore = (new MiscBlock("red_quartzite_copper_ore", (Properties.create(Material.ROCK, MaterialColor.ORANGE_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(red_quartzite_copper_ore, ItemGroup.BUILDING_BLOCKS);
		
		red_quartzite_iron_ore = (new MiscBlock("red_quartzite_iron_ore", (Properties.create(Material.ROCK, MaterialColor.ORANGE_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(red_quartzite_iron_ore, ItemGroup.BUILDING_BLOCKS);
		
		red_quartzite_ore = (new MiscBlock("red_quartzite_ore", (Properties.create(Material.ROCK, MaterialColor.ORANGE_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(red_quartzite_ore, ItemGroup.BUILDING_BLOCKS);
		
		red_sandstone_gold_ore = (new MiscBlock("red_sandstone_gold_ore", (Properties.create(Material.ROCK, MaterialColor.ORANGE_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(red_sandstone_gold_ore, ItemGroup.BUILDING_BLOCKS);
		
		red_sandstone_lead_ore = (new MiscBlock("red_sandstone_lead_ore", (Properties.create(Material.ROCK, MaterialColor.ORANGE_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(red_sandstone_lead_ore, ItemGroup.BUILDING_BLOCKS);
		
		red_sandstone_tin_ore = (new MiscBlock("red_sandstone_tin_ore", (Properties.create(Material.ROCK, MaterialColor.ORANGE_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(red_sandstone_tin_ore, ItemGroup.BUILDING_BLOCKS);
		
		sandstone_geode = (new MiscBlock("sandstone_geode", (Properties.create(Material.ROCK, MaterialColor.SAND)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(sandstone_geode, ItemGroup.BUILDING_BLOCKS);
		
		sandstone_coal_ore = (new MiscBlock("sandstone_coal_ore", (Properties.create(Material.ROCK, MaterialColor.SAND)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(sandstone_coal_ore, ItemGroup.BUILDING_BLOCKS);
		
		sandstone_copper_ore = (new MiscBlock("sandstone_copper_ore", (Properties.create(Material.ROCK, MaterialColor.SAND)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(sandstone_copper_ore, ItemGroup.BUILDING_BLOCKS);
		
		sandstone_lead_ore = (new MiscBlock("sandstone_lead_ore", (Properties.create(Material.ROCK, MaterialColor.SAND)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(sandstone_lead_ore, ItemGroup.BUILDING_BLOCKS);
		
		sandstone_tin_ore = (new MiscBlock("sandstone_tin_ore", (Properties.create(Material.ROCK, MaterialColor.SAND)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(sandstone_tin_ore, ItemGroup.BUILDING_BLOCKS);
		
		schist = (new MiscBlock("schist", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(schist, ItemGroup.BUILDING_BLOCKS);
		
		schist_graphite_ore = (new MiscBlock("schist_graphite_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(schist_graphite_ore, ItemGroup.BUILDING_BLOCKS);
		
		schist_nickel_ore = (new MiscBlock("schist_nickel_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(schist_nickel_ore, ItemGroup.BUILDING_BLOCKS);
		
		polished_schist = (new MiscBlock("polished_schist", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(polished_schist, ItemGroup.BUILDING_BLOCKS);
		
		shale = (new MiscBlock("shale", (Properties.create(Material.ROCK, MaterialColor.LIGHT_GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(shale, ItemGroup.BUILDING_BLOCKS);
		
		shale_iron_ore = (new MiscBlock("shale_iron_ore", (Properties.create(Material.ROCK, MaterialColor.LIGHT_GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(shale_iron_ore, ItemGroup.BUILDING_BLOCKS);
		
		shale_tin_ore = (new MiscBlock("shale_tin_ore", (Properties.create(Material.ROCK, MaterialColor.LIGHT_GRAY_TERRACOTTA)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(shale_tin_ore, ItemGroup.BUILDING_BLOCKS);
		
		stone_geode = (new MiscBlock("stone_geode", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(stone_geode, ItemGroup.BUILDING_BLOCKS);

		terracotta_geode = (new MiscBlock("terracotta_geode", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(terracotta_geode, ItemGroup.BUILDING_BLOCKS);
		
		//normal ores

		chromite_ore = (new MiscBlock("chromite_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F).harvestLevel(3)));
		blockMap.put(chromite_ore, ItemGroup.BUILDING_BLOCKS); 
		
		cobalt_ore = (new MiscBlock("cobalt_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F).harvestLevel(3)));
		blockMap.put(cobalt_ore, ItemGroup.BUILDING_BLOCKS);
		
		lead_ore = (new MiscBlock("lead_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F)));
		blockMap.put(lead_ore, ItemGroup.BUILDING_BLOCKS);
		
		lead_block = (new MiscBlock("lead_block", (Properties.create(Material.ROCK, MaterialColor.BLACK_TERRACOTTA)).hardnessAndResistance(2.0F, 10.0F)));
		blockMap.put(lead_block, ItemGroup.BUILDING_BLOCKS);
		
		nickel_ore = (new MiscBlock("nickel_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F).harvestLevel(1)));
		blockMap.put(nickel_ore, ItemGroup.BUILDING_BLOCKS);
		
		platinum_ore = (new MiscBlock("platinum_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F).harvestLevel(2)));
		blockMap.put(platinum_ore, ItemGroup.BUILDING_BLOCKS);
		
		silver_ore = (new MiscBlock("silver_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F).harvestLevel(1)));
		blockMap.put(silver_ore, ItemGroup.BUILDING_BLOCKS);
		
		silver_block = (new MiscBlock("silver_block", (Properties.create(Material.ROCK, MaterialColor.IRON)).hardnessAndResistance(2.0F, 10.0F)));
		blockMap.put(silver_block, ItemGroup.BUILDING_BLOCKS);
		
		tin_ore = (new MiscBlock("tin_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F).harvestLevel(1)));
		blockMap.put(tin_ore, ItemGroup.BUILDING_BLOCKS);
		
		titanium_ore = (new MiscBlock("titanium_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F).harvestLevel(3)));
		blockMap.put(titanium_ore, ItemGroup.BUILDING_BLOCKS);
		
		uranium_ore = (new MiscBlock("uranium_ore", (Properties.create(Material.ROCK, MaterialColor.STONE)).hardnessAndResistance(1.5F, 6.0F).harvestLevel(2)));
		blockMap.put(uranium_ore, ItemGroup.BUILDING_BLOCKS);
		
		//utility blocks
		
		hardened_bricks = (new MiscBlock("hardened_bricks", (Properties.create(Material.ROCK, MaterialColor.OBSIDIAN)).hardnessAndResistance(20.0F, 200.0F)));
		blockMap.put(hardened_bricks, ItemGroup.BUILDING_BLOCKS);

		warstone_bricks = (new MiscBlock("war_bricks", (Properties.create(Material.ROCK, MaterialColor.OBSIDIAN)).hardnessAndResistance(30.0F, 500.0F)));
		blockMap.put(warstone_bricks, ItemGroup.BUILDING_BLOCKS);
		
		cast_furnace = (new CastFurnaceBlock("cast_furnace", (Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA)).hardnessAndResistance(3.5F, 5.0F)));
		blockMap.put(cast_furnace, ItemGroup.DECORATIONS);
		
		slag_block = (new MiscBlock("slag_block", (Properties.create(Material.EARTH, MaterialColor.GRAY)).harvestTool(ToolType.SHOVEL).hardnessAndResistance(0.5F, 10.0F).sound(SoundType.GROUND)));
		blockMap.put(slag_block, ItemGroup.BUILDING_BLOCKS);
		
		//Jade, because jade is special and deserves over a dozen variants /s
		jade_block = (new MiscBlock("jade_block", (Properties.create(Material.ROCK, MaterialColor.CYAN_TERRACOTTA)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_block, ItemGroup.BUILDING_BLOCKS);
		
		jade_brick = (new MiscBlock("jade_brick", (Properties.create(Material.ROCK, MaterialColor.CYAN_TERRACOTTA)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_brick, ItemGroup.BUILDING_BLOCKS);
		
		jade_circle_brick = (new MiscBlock("jade_circle_brick", (Properties.create(Material.ROCK, MaterialColor.CYAN_TERRACOTTA)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_circle_brick, ItemGroup.BUILDING_BLOCKS);
		
		jade_cobblestone = (new MiscBlock("jade_cobblestone", (Properties.create(Material.ROCK, MaterialColor.CYAN_TERRACOTTA)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_cobblestone, ItemGroup.BUILDING_BLOCKS);
		
		jade_pent_brick = (new MiscBlock("jade_pent_brick", (Properties.create(Material.ROCK, MaterialColor.CYAN_TERRACOTTA)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_pent_brick, ItemGroup.BUILDING_BLOCKS);
		
		jade_square_brick = (new MiscBlock("jade_square_brick", (Properties.create(Material.ROCK, MaterialColor.CYAN_TERRACOTTA)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_square_brick, ItemGroup.BUILDING_BLOCKS);
		
		jade_stone = (new StoneyBlock("jade_stone", jade_cobblestone, (Properties.create(Material.ROCK, MaterialColor.CYAN_TERRACOTTA)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_stone, ItemGroup.BUILDING_BLOCKS);
		
		jade_pillar = (new RotatedBlock("jade_pillar", (Properties.create(Material.ROCK, MaterialColor.CYAN)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_pillar, ItemGroup.BUILDING_BLOCKS);
		
		jade_tile_pillar = (new RotatedBlock("jade_tile_pillar", (Properties.create(Material.ROCK, MaterialColor.CYAN)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_tile_pillar, ItemGroup.BUILDING_BLOCKS);
		
		jade_stone_slab = (new SlabsBlock("jade_stone_slab", (Properties.create(Material.ROCK, MaterialColor.CYAN)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_stone_slab, ItemGroup.BUILDING_BLOCKS);
		
		jade_cobblestone_slab = (new SlabsBlock("jade_cobblestone_slab", (Properties.create(Material.ROCK, MaterialColor.CYAN)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_cobblestone_slab, ItemGroup.BUILDING_BLOCKS);
		
		jade_slab = (new SlabsBlock("jade_slab", (Properties.create(Material.ROCK, MaterialColor.CYAN)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_slab, ItemGroup.BUILDING_BLOCKS);
		
		jade_brick_slab = (new SlabsBlock("jade_brick_slab", (Properties.create(Material.ROCK, MaterialColor.CYAN)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_brick_slab, ItemGroup.BUILDING_BLOCKS);
		
		jade_tile_slab = (new SlabsBlock("jade_tile_slab", (Properties.create(Material.ROCK, MaterialColor.CYAN)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_tile_slab, ItemGroup.BUILDING_BLOCKS);
		
		jade_pentbrick_slab = (new SlabsBlock("jade_pentbrick_slab", (Properties.create(Material.ROCK, MaterialColor.CYAN)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_pentbrick_slab, ItemGroup.BUILDING_BLOCKS);
		
		jade_stone_stairs = (new MiscStairsBlock("jade_stone_stairs", () -> jade_stone.getDefaultState(), (Properties.create(Material.ROCK, MaterialColor.CYAN)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_stone_stairs, ItemGroup.BUILDING_BLOCKS);
		
		jade_cobblestone_stairs = (new MiscStairsBlock("jade_cobblestone_stairs", () -> jade_stone.getDefaultState(), (Properties.create(Material.ROCK, MaterialColor.CYAN)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_cobblestone_stairs, ItemGroup.BUILDING_BLOCKS);
		
		jade_brick_stairs = (new MiscStairsBlock("jade_brick_stairs", () -> jade_stone.getDefaultState(), (Properties.create(Material.ROCK, MaterialColor.CYAN)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_brick_stairs, ItemGroup.BUILDING_BLOCKS);
		
		jade_tile_stairs = (new MiscStairsBlock("jade_tile_stairs", () -> jade_stone.getDefaultState(), (Properties.create(Material.ROCK, MaterialColor.CYAN)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_tile_stairs, ItemGroup.BUILDING_BLOCKS);
		
		jade_pentbrick_stairs = (new MiscStairsBlock("jade_pentbrick_stairs", () -> jade_stone.getDefaultState(), (Properties.create(Material.ROCK, MaterialColor.CYAN)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_pentbrick_stairs, ItemGroup.BUILDING_BLOCKS);
		
		jade_stairs = (new MiscStairsBlock("jade_stairs", () -> jade_stone.getDefaultState(), (Properties.create(Material.ROCK, MaterialColor.CYAN)).hardnessAndResistance(1.5F, 7.0F)));
		blockMap.put(jade_stairs, ItemGroup.BUILDING_BLOCKS);
		
		System.out.println("doing lumin block");
		lumin_block = (new LuminBlock("lumin_block", (Properties.create(Material.GLASS, MaterialColor.AIR)).hardnessAndResistance(0.1F, 15.0F)));
		blockMap.put(lumin_block, ItemGroup.SEARCH);
		
		this.blockList = blockMap.build();
	}

	public ImmutableMap<Block, ItemGroup> getModBlockList() {
		return blockList;
	}
}
