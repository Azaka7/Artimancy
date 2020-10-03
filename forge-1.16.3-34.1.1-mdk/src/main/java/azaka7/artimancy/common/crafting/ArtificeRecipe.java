package azaka7.artimancy.common.crafting;

import javax.annotation.Nullable;

import azaka7.artimancy.common.ModBlocks;
import azaka7.artimancy.common.magic.AbstractSpell;
import azaka7.artimancy.common.magic.Spells;
import azaka7.artimancy.common.tileentity.ArtimancyTableTE;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ArtificeRecipe implements IRecipe<IInventory> {
	//These keys are used to prevent the recipe from showing up in any crafting book except the casting furnace's book.
	public static final int width_key = -663941167; //"azaka7" base 36 to base 10, made negative to prevent vanilla crafting recipes
	public static final int height_key = -37439508;   //"magic"   base 36 to base 10, made negative to prevent vanilla crafting recipes
	
	private final ResourceLocation id;
	private final String group;
	private final Ingredient sophic, elemental, input;
	private final ItemStack output;
	@Nullable
	private final AbstractSpell applySpell;
	
	public ArtificeRecipe(ResourceLocation id, String group, Ingredient sophic, Ingredient elemental, Ingredient input, ItemStack output, AbstractSpell applySpell) {
		this.id = id;
		this.group = group;
		this.sophic = sophic;
		this.elemental = elemental;
		this.input = input;
		this.output = output;
		this.applySpell = applySpell;
	}
	
	@Override
	public ItemStack getIcon() {
		return new ItemStack(ModBlocks.instance().artimancy_table);
	}
	
	@Override
	public boolean matches(IInventory inv, World worldIn) {
		return matches(inv);
	}
	
	public boolean matches(IInventory inv) {
		if(!(inv instanceof ArtimancyTableTE)) {return false;}
		//ArtimancyTableTE table = (ArtimancyTableTE) inv;
		return sophic.test(inv.getStackInSlot(ArtimancyTableTE.SOPHIC_SLOT)) 
			&& elemental.test(inv.getStackInSlot(ArtimancyTableTE.ELEMENTAL_SLOT)) 
			&& input.test(inv.getStackInSlot(ArtimancyTableTE.INPUT_SLOT));
	}
	
	@Override
	public ItemStack getCraftingResult(IInventory inv) {
		if(!matches(inv)) return ItemStack.EMPTY;
		ItemStack stackIn = inv.getStackInSlot(ArtimancyTableTE.INPUT_SLOT);
		ItemStack ret = output.isEmpty() ? stackIn.copy() : output.copy();
		if(applySpell != null) {
			Spells.applySpell(applySpell, ret);
			return ret;
		} else {
			return ret;
		}
	}
	
	@Override
	public boolean canFit(int width, int height) {
		return width == width_key && height == height_key;
	}
	
	//To be used for display purposes in case of recipe book.
	public ItemStack getRecipeOutput(int index) {
		ItemStack[] matching = input.getMatchingStacks();
		ItemStack ret = output.isEmpty() ? matching[index % matching.length] : output.copy();
		
		if(applySpell != null) {
			Spells.applySpell(applySpell, ret);
			return ret;
		} else {
			return ret;
		}
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return getRecipeOutput(0);
	}
	
	@Override
	public ResourceLocation getId() {
		return id;
	}
	
	@Override
	public String getGroup() {
		return group;
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ArtificeRecipeSerializer.instance();
	}
	@Override
	public IRecipeType<?> getType() {
		return ArtimancyTableTE.ARTIFICE_RECIPE_TYPE;
	}
	
	public Ingredient getSophicIngredient() {
		return sophic;
	}
	
	public Ingredient getElementalIngredient() {
		return elemental;
	}
	
	public Ingredient getInput() {
		return input;
	}
	
	public ItemStack getOutput() {
		return getRecipeOutput();
	}
	
	public AbstractSpell getSpellToApply() {
		return applySpell;
	}
}
