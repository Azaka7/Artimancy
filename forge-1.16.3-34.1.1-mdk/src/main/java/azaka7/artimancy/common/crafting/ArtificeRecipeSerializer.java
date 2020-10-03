package azaka7.artimancy.common.crafting;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import azaka7.artimancy.common.magic.AbstractSpell;
import azaka7.artimancy.common.magic.Spells;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ArtificeRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ArtificeRecipe> {
	
	private static final ArtificeRecipeSerializer INSTANCE = new ArtificeRecipeSerializer();
	public static final ArtificeRecipeSerializer instance() {return INSTANCE;}
	
	private final List<Ingredient> valid_sophics = new ArrayList<Ingredient>(3);
	private final List<Ingredient> valid_elementals = new ArrayList<Ingredient>(4);
	
	private ArtificeRecipeSerializer() {}

	public boolean isValidSophic(ItemStack stack) {
		for(Ingredient ing : valid_sophics) {
			if(ing.test(stack)) {
				return true;
			}
		}
		return false;
	}

	public boolean isValidElemental(ItemStack stack) {
		for(Ingredient ing : valid_elementals) {
			if(ing.test(stack)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ArtificeRecipe read(ResourceLocation recipeId, JsonObject json) {
		if(!json.has("sophic") || !json.has("elemental") || !json.has("input")) {
			throw new IllegalStateException("ArtificeRecipe "+recipeId.toString()+" is missing one or more of: \"sophic\", \"elemental\", \"input\"");
		}
		
		String group = JSONUtils.getString(json, "group", "");
		
		JsonElement jsonelement = (JsonElement)(JSONUtils.isJsonArray(json, "sophic") ? JSONUtils.getJsonArray(json, "sophic") : JSONUtils.getJsonObject(json, "sophic"));
		Ingredient sophic = Ingredient.deserialize(jsonelement.getAsJsonObject());
		
		jsonelement = (JsonElement)(JSONUtils.isJsonArray(json, "elemental") ? JSONUtils.getJsonArray(json, "elemental") : JSONUtils.getJsonObject(json, "elemental"));
		Ingredient elemental = Ingredient.deserialize(jsonelement.getAsJsonObject());
		
		jsonelement = (JsonElement)(JSONUtils.isJsonArray(json, "input") ? JSONUtils.getJsonArray(json, "input") : JSONUtils.getJsonObject(json, "input"));
		Ingredient input = Ingredient.deserialize(jsonelement.getAsJsonObject());
		
		ItemStack output = ItemStack.EMPTY;
		if(json.has("result")) {
			if (json.get("result").isJsonObject()) {
				output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
			} else {
				String itemid = JSONUtils.getString(json, "result");
				output = new ItemStack(Registry.ITEM.func_241873_b(new ResourceLocation(itemid)).orElseThrow(() -> {
					return new IllegalStateException("Item: " + itemid + " does not exist");
					}));
			}
		}
		
		AbstractSpell spell;
		if(!json.has("spell")) {
			spell = null;
		} else {
			spell = Spells.getSpellFromID(JSONUtils.getString(json, "spell", null));
		}

		if(!valid_sophics.contains(sophic)) {valid_sophics.add(sophic);}
		if(!valid_elementals.contains(elemental)) {valid_elementals.add(elemental);}
		
		return new ArtificeRecipe(recipeId, group, sophic, elemental, input, output, spell);
	}

	@Override
	public ArtificeRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
		String group = buffer.readString(32767);
		Ingredient sophic = Ingredient.read(buffer);
		Ingredient elemental = Ingredient.read(buffer);
		Ingredient input = Ingredient.read(buffer);
		ItemStack output = buffer.readItemStack();
		AbstractSpell spell = Spells.getSpellFromID(buffer.readString());

		if(!valid_sophics.contains(sophic)) {valid_sophics.add(sophic);}
		if(!valid_elementals.contains(elemental)) {valid_elementals.add(elemental);}
		
		return new ArtificeRecipe(recipeId, group, sophic, elemental, input, output, spell);
	}

	@Override
	public void write(PacketBuffer buffer, ArtificeRecipe recipe) {
		// TODO Auto-generated method stub
		buffer.writeString(recipe.getGroup());
		recipe.getSophicIngredient().write(buffer);
		recipe.getElementalIngredient().write(buffer);
		recipe.getInput().write(buffer);
		buffer.writeItemStack(recipe.getOutput());
		buffer.writeString(Spells.getSpellID(recipe.getSpellToApply()));
	}

}
