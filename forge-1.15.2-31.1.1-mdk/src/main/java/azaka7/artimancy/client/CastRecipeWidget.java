package azaka7.artimancy.client;

import com.mojang.blaze3d.systems.RenderSystem;

import azaka7.artimancy.common.crafting.CastingRecipe;
import azaka7.artimancy.common.crafting.CastingRecipeBookPage;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBook;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CastRecipeWidget extends Widget {
	private static final ResourceLocation RECIPE_BOOK = new ResourceLocation("textures/gui/recipe_book.png");
	private RecipeBookContainer<?> container;
	private RecipeBook book;
	private RecipeList list;
	private float time;
	private float animationTime;
	private int currentIndex;

	public CastRecipeWidget() {
		super(0, 0, 25, 25, "");
	}

	public void updateRecipeGroup(RecipeList recipes, CastingRecipeBookPage page) {
		this.list = recipes;
		this.container = (RecipeBookContainer<?>)page.func_203411_d().player.openContainer;
		this.book = page.func_203412_e();
		List<IRecipe<?>> list = recipes.getRecipes(false/*this.book.isFilteringCraftable(this.container)*/);

		for(IRecipe<?> irecipe : list) {
			if (this.book.isNew(irecipe)) {
				page.recipesShown(list);
				this.animationTime = 15.0F;
				break;
			}
		}
	}
	
	public RecipeList getList() {return this.list;
		}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void renderButton(int x, int y, float time) {
		if (!Screen.hasControlDown()) {
			this.time += time;
		}

		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bindTexture(RECIPE_BOOK);
		int i = 29;
		if (!this.list.containsCraftableRecipes()) {
			i += 25;
		}

		int j = 206;
		if (this.list.getRecipes(this.book.isFilteringCraftable(this.container)).size() > 1) {
			j += 25;
		}

		boolean bouncing_frame = this.animationTime > 0.0F;
		if (bouncing_frame) {
			float f = 1.0F + 0.1F * (float)Math.sin((double)(this.animationTime / 15.0F * (float)Math.PI));
			RenderSystem.pushMatrix();
			RenderSystem.translatef((float)(this.x + 8), (float)(this.y + 12), 0.0F);
			RenderSystem.scalef(f, f, 1.0F);
			RenderSystem.translatef((float)(-(this.x + 8)), (float)(-(this.y + 12)), 0.0F);
			this.animationTime -= time;
		}

		this.blit(this.x, this.y, i, j, this.width, this.height);
		List<IRecipe<?>> list = this.getOrderedRecipes();
		this.currentIndex = MathHelper.floor(this.time / 30.0F) % list.size();
		IRecipe<?> recipe = list.get(this.currentIndex);
		ItemStack itemstack;
		int k = 4;
		
		if(recipe instanceof CastingRecipe) {
	 		RenderSystem.scalef(0.5f, 0.5f, 1.0f);
	 		CastingRecipe crec = (CastingRecipe) recipe;
	 		int subIndex = MathHelper.floor(this.time / 10.0F) % crec.getAmountedIngredients().get(0).getMatchingStacks().length;
	 	 
	 		itemstack = crec.getAmountedIngredients().get(0).getMatchingStacks()[subIndex];
	 		itemstack.setCount(crec.getIngredient1Amount());
	 		minecraft.getItemRenderer().renderItemAndEffectIntoGUI(itemstack, 2*(this.x + k + 4 - 6), 2*(this.y + k - 4 + 2));
	 		if (itemstack.getCount() > 0) {
	 			minecraft.getItemRenderer().renderItemOverlayIntoGUI(minecraft.fontRenderer, itemstack, 2*(this.x + k + 4 - 6), 2*(this.y + k - 4 - 2), itemstack.getCount() == 1 ? "1" : null);
	 		}
	 		
	 		if(crec.getAmountedIngredients().size() > 1) {
	 			subIndex = MathHelper.floor(this.time / 10.0F) % crec.getAmountedIngredients().get(1).getMatchingStacks().length;
	 			itemstack = crec.getAmountedIngredients().get(1).getMatchingStacks()[subIndex];
		 		itemstack.setCount(crec.getIngredient2Amount());
	 			minecraft.getItemRenderer().renderItemAndEffectIntoGUI(itemstack, 2*(this.x + k + 4 - 6), 2*(this.y + k - 4 + 8));
	 			if (itemstack.getCount() > 0) {
		 			minecraft.getItemRenderer().renderItemOverlayIntoGUI(minecraft.fontRenderer, itemstack, 2*(this.x + k + 4 - 6), 2*(this.y + k), itemstack.getCount() == 1 ? "1" : null);
		 		}
	 		}
			subIndex = MathHelper.floor(this.time / 10.0F) % crec.getCast().getMatchingStacks().length;
			itemstack = crec.getCast().getMatchingStacks()[subIndex];
	 		minecraft.getItemRenderer().renderItemAndEffectIntoGUI(itemstack, 2*(this.x + k + 4 + 6), 2*(this.y + k - 4 + 2));
	 		
	 		RenderSystem.scalef(2.0f, 2.0f, 1.0f);
		}

		itemstack = recipe.getRecipeOutput();
		
		minecraft.getItemRenderer().renderItemAndEffectIntoGUI(itemstack, this.x + k + 3, this.y + k + 4);
		if (itemstack.getCount() > 1) {
			minecraft.getItemRenderer().renderItemOverlayIntoGUI(minecraft.fontRenderer, itemstack, this.x + k + 1, this.y + k + 1, null);
		}

		if (bouncing_frame) {
			RenderSystem.popMatrix();
		} else {
			//Render frame to cover parts of item textures that spill over, only when frame is NOT bouncing.
			RenderSystem.pushMatrix();
			minecraft.getTextureManager().bindTexture(RECIPE_BOOK);
			RenderSystem.translatef(0, 0, 200);
			this.blit(this.x, this.y, i, j, 2, this.height);
			this.blit(this.x, this.y, i, j, this.width, 2);
			this.blit(this.x + 22, this.y, i + 22, j, 3, this.height);
			this.blit(this.x, this.y + 22, i, j+22, this.width, 3);
			RenderSystem.popMatrix();
		}

	}

	private List<IRecipe<?>> getOrderedRecipes() {
		List<IRecipe<?>> list = this.list.getDisplayRecipes(true);
		if (!this.book.isFilteringCraftable(this.container)) {
			list.addAll(this.list.getDisplayRecipes(false));
		}

		return list;
	}

	public boolean isOnlyOption() {
		return this.getOrderedRecipes().size() == 1;
	}

	public IRecipe<?> getRecipe() {
		List<IRecipe<?>> list = this.getOrderedRecipes();
		return list.get(this.currentIndex);
	}

	public List<String> getToolTipText(Screen screen) {
		List<IRecipe<?>> recipes = this.getOrderedRecipes();
		this.currentIndex = MathHelper.floor(this.time / 30.0F) % recipes.size();
		IRecipe<?> recipe = recipes.get(this.currentIndex);
		
		ItemStack itemstack = this.getOrderedRecipes().get(this.currentIndex).getRecipeOutput();
		List<String> list = screen.getTooltipFromItem(itemstack);
		
		if(recipe instanceof CastingRecipe) {
			CastingRecipe crec = (CastingRecipe) recipe;
			itemstack = crec.getRecipeBonus();
			if(itemstack != null && !itemstack.isEmpty()) {
				list.add(I18n.format("gui.artimancy.label.bonus") + ": " + crec.getRecipeBonus().getDisplayName().getFormattedText());
				list.add("");
			}
			int subIndex = MathHelper.floor(this.time / 10.0F) % crec.getAmountedIngredients().get(0).getMatchingStacks().length;
			list.add(I18n.format("gui.artimancy.label.input")+": " + crec.getIngredient1Amount() + "x " + crec.getAmountedIngredients().get(0).getMatchingStacks()[subIndex].getDisplayName().getFormattedText());
			if(crec.getAmountedIngredients().size() > 1) {
				subIndex = MathHelper.floor(this.time / 10.0F) % crec.getAmountedIngredients().get(1).getMatchingStacks().length;
				list.add(I18n.format("gui.artimancy.label.input2")+": " + crec.getIngredient2Amount() + "x " + crec.getAmountedIngredients().get(1).getMatchingStacks()[subIndex].getDisplayName().getFormattedText());
			}
			subIndex = MathHelper.floor(this.time / 10.0F) % crec.getCast().getMatchingStacks().length;
			list.add(I18n.format("gui.artimancy.label.cast")+": " + crec.getCast().getMatchingStacks()[subIndex].getDisplayName().getFormattedText());
		}
		
		/* TODO Make right-clicking actually show all grouped recipes, then enable this
		if (this.list.getRecipes(this.book.isFilteringCraftable(this.container)).size() > 1) {
			list.add(I18n.format("gui.recipebook.moreRecipes"));
		}
		*/

		return list;
	}

	public int getWidth() {
		return 25;
	}

	protected boolean isValidClickButton(int click) {
		return click == 0 || click == 1;
	}
}