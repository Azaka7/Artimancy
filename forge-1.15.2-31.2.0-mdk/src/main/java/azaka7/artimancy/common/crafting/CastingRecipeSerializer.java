package azaka7.artimancy.common.crafting;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class CastingRecipeSerializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CastingRecipe> {

	private static final CastingRecipeSerializer INSTANCE = new CastingRecipeSerializer();
	public static final CastingRecipeSerializer instance() { return INSTANCE; }
	
	//lists used for faster search for item transfer in container. default sizes are to allocate sufficient memory ahead of populating
	private final List<Ingredient> valid_casts = new ArrayList<Ingredient>(16);
	private final List<Ingredient> valid_inputs = new ArrayList<Ingredient>(64);
	
	private CastingRecipeSerializer() {}
	
	
	public boolean isValidCast(ItemStack stack) {
		for(Ingredient in : valid_casts) {
			if(in.test(stack)) {return true;}
		}
		return false;
	}
	
	public boolean isValidInput(ItemStack stack) {
		for(Ingredient in : valid_inputs) {
			if(in.test(stack)) {return true;}
		}
		return false;
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public CastingRecipe read(ResourceLocation recipeId, JsonObject json) {
		String group = JSONUtils.getString(json, "group", "");
		
		JsonElement jsonelement = (JsonElement)(JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient"));
		AmountedIngredient ingredient1 = AmountedIngredient.Serializer.INSTANCE.parse(jsonelement.getAsJsonObject());
		
		AmountedIngredient ingredient2 = AmountedIngredient.EMPTY;
		if(json.has("ingredient2")) {
			jsonelement = (JsonElement)(JSONUtils.isJsonArray(json, "ingredient2") ? JSONUtils.getJsonArray(json, "ingredient2") : JSONUtils.getJsonObject(json, "ingredient2"));
			ingredient2 = AmountedIngredient.Serializer.INSTANCE.parse(jsonelement.getAsJsonObject());
		}
		
		jsonelement = (JsonElement)(JSONUtils.isJsonArray(json, "cast") ? JSONUtils.getJsonArray(json, "cast") : JSONUtils.getJsonObject(json, "cast"));
		Ingredient cast = Ingredient.deserialize(jsonelement);
		
		if (!json.has("result")) {
			throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
		}
		
		ItemStack result1;
		if (json.get("result").isJsonObject()) {
			result1 = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
		} else {
			String itemid = JSONUtils.getString(json, "result");
			result1 = new ItemStack(Registry.ITEM.getValue(new ResourceLocation(itemid)).orElseThrow(() -> {
				return new IllegalStateException("Item: " + itemid + " does not exist");
				}));
		}
		ItemStack result2 = ItemStack.EMPTY;
		if (json.has("bonus")) {
			if (json.get("bonus").isJsonObject()) {
				result2 = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "bonus"));
			} else {
				String itemid = JSONUtils.getString(json, "bonus");
				result2 = new ItemStack(Registry.ITEM.getValue(new ResourceLocation(itemid)).orElseThrow(() -> {
					return new IllegalStateException("Item: " + itemid + " does not exist");
					}));
			}
		}

		if(!valid_casts.contains(cast)) { valid_casts.add(cast); }
		if(!valid_inputs.contains(ingredient1)) { valid_inputs.add(ingredient1); }
		if(!valid_inputs.contains(ingredient2)) { valid_inputs.add(ingredient2); }
		
		float exp = JSONUtils.getFloat(json, "experience", 0.0F);
		int cooktime = JSONUtils.getInt(json, "cookingtime", 200);
		
		return new CastingRecipe(recipeId, group, ingredient1, ingredient2, cast, result1, result2, exp, cooktime);
	}

	@Override
	public CastingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
		String group = buffer.readString(32767);
		AmountedIngredient input1 = AmountedIngredient.Serializer.INSTANCE.parse(buffer);
		AmountedIngredient input2 = AmountedIngredient.Serializer.INSTANCE.parse(buffer);
		Ingredient cast = Ingredient.read(buffer);
		ItemStack result1 = buffer.readItemStack();
		ItemStack result2 = buffer.readItemStack();
		float exp = buffer.readFloat();
		int cooktime = buffer.readVarInt();
		
		if(!valid_casts.contains(cast)) { valid_casts.add(cast); }
		if(!valid_inputs.contains(input1)) { valid_inputs.add(input1); }
		if(!valid_inputs.contains(input2)) { valid_inputs.add(input2); }
		
		return new CastingRecipe(recipeId, group, input1, input2, cast, result1, result2, exp, cooktime);
	}

	@Override
	public void write(PacketBuffer buffer, CastingRecipe recipe) {
		buffer.writeString(recipe.getGroup());
		NonNullList<AmountedIngredient> ingreds = recipe.getAmountedIngredients();
		AmountedIngredient.Serializer.INSTANCE.write(buffer, ingreds.get(0));
		AmountedIngredient.Serializer.INSTANCE.write(buffer, ingreds.get(1));
		recipe.getCast().write(buffer);
		buffer.writeItemStack(recipe.getRecipeOutput());
		buffer.writeItemStack(recipe.getRecipeBonus());
		buffer.writeFloat(recipe.getExp());
		buffer.writeVarInt(recipe.getCookTime());
	}

}
