package azaka7.artimancy.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import azaka7.artimancy.common.crafting.CastingRecipe;
import azaka7.artimancy.common.crafting.CastingRecipeBookPage;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBook;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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
		super(0, 0, 25, 25, textof(""));
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
		this.field_230690_l_ = x;
	    this.field_230691_m_ = y;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void func_230430_a_(MatrixStack matrix, int x, int y, float time) {
		if(!this.field_230694_p_) { return; }
		super.func_230430_a_(matrix, x, y, time);
		if (!Screen.func_231172_r_()) { //if control key is pressed
			this.time += time;
		}

		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bindTexture(RECIPE_BOOK);
		int i = 29;
		if (!this.list.containsCraftableRecipes()) {
			i += 25;
		}

		int j = 206;
		if (this.list.getRecipes(this.book.func_242141_a(this.container)).size() > 1) {
			j += 25;
		}

		boolean bouncing_frame = this.animationTime > 0.0F;
		if (bouncing_frame) {
			float f = 1.0F + 0.1F * (float)Math.sin((double)(this.animationTime / 15.0F * (float)Math.PI));
			RenderSystem.pushMatrix();
			RenderSystem.translatef((float)(this.field_230690_l_ + 8), (float)(this.field_230691_m_ + 12), 0.0F);
			RenderSystem.scalef(f, f, 1.0F);
			RenderSystem.translatef((float)(-(this.field_230690_l_ + 8)), (float)(-(this.field_230691_m_ + 12)), 0.0F);
			this.animationTime -= time;
		}

		this.func_238474_b_(matrix, this.field_230690_l_, this.field_230691_m_, i, j, this.field_230688_j_, this.field_230689_k_);
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
	 		minecraft.getItemRenderer().renderItemAndEffectIntoGUI(itemstack, 2*(this.field_230690_l_ + k + 4 - 6), 2*(this.field_230691_m_ + k - 4 + 2));
	 		if (itemstack.getCount() > 0) {
	 			minecraft.getItemRenderer().renderItemOverlayIntoGUI(minecraft.fontRenderer, itemstack, 2*(this.field_230690_l_ + k + 4 - 6), 2*(this.field_230691_m_ + k - 4 - 2), itemstack.getCount() == 1 ? "1" : null);
	 		}
	 		
	 		if(crec.getAmountedIngredients().size() > 1) {
	 			subIndex = MathHelper.floor(this.time / 10.0F) % crec.getAmountedIngredients().get(1).getMatchingStacks().length;
	 			itemstack = crec.getAmountedIngredients().get(1).getMatchingStacks()[subIndex];
		 		itemstack.setCount(crec.getIngredient2Amount());
	 			minecraft.getItemRenderer().renderItemAndEffectIntoGUI(itemstack, 2*(this.field_230690_l_ + k + 4 - 6), 2*(this.field_230691_m_ + k - 4 + 8));
	 			if (itemstack.getCount() > 0) {
		 			minecraft.getItemRenderer().renderItemOverlayIntoGUI(minecraft.fontRenderer, itemstack, 2*(this.field_230690_l_ + k + 4 - 6), 2*(this.field_230691_m_ + k), itemstack.getCount() == 1 ? "1" : null);
		 		}
	 		}
			subIndex = MathHelper.floor(this.time / 10.0F) % crec.getCast().getMatchingStacks().length;
			itemstack = crec.getCast().getMatchingStacks()[subIndex];
	 		minecraft.getItemRenderer().renderItemAndEffectIntoGUI(itemstack, 2*(this.field_230690_l_ + k + 4 + 6), 2*(this.field_230691_m_ + k - 4 + 2));
	 		
	 		RenderSystem.scalef(2.0f, 2.0f, 1.0f);
		}

		itemstack = recipe.getRecipeOutput();
		
		minecraft.getItemRenderer().renderItemAndEffectIntoGUI(itemstack, this.field_230690_l_ + k + 3, this.field_230691_m_ + k + 4);
		if (itemstack.getCount() > 1) {
			minecraft.getItemRenderer().renderItemOverlayIntoGUI(minecraft.fontRenderer, itemstack, this.field_230690_l_ + k + 1, this.field_230691_m_ + k + 1, null);
		}

		if (bouncing_frame) {
			RenderSystem.popMatrix();
		} else {
			//Render frame to cover parts of item textures that spill over, only when frame is NOT bouncing.
			RenderSystem.pushMatrix();
			minecraft.getTextureManager().bindTexture(RECIPE_BOOK);
			RenderSystem.translatef(0, 0, 200);
			this.func_238474_b_(matrix, this.field_230690_l_, this.field_230691_m_, i, j, 2, this.field_230689_k_);
			this.func_238474_b_(matrix, this.field_230690_l_, this.field_230691_m_, i, j, this.field_230688_j_, 2);
			this.func_238474_b_(matrix, this.field_230690_l_ + 22, this.field_230691_m_, i + 22, j, 3, this.field_230689_k_);
			this.func_238474_b_(matrix, this.field_230690_l_, this.field_230691_m_ + 22, i, j+22, this.field_230688_j_, 3);
			RenderSystem.popMatrix();
		}
		

	}

	private List<IRecipe<?>> getOrderedRecipes() {
		List<IRecipe<?>> list = this.list.getDisplayRecipes(true);
		if (!this.book.func_242141_a(this.container)) {
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

	public List<ITextComponent> getToolTipText(Screen screen) {
		List<IRecipe<?>> recipes = this.getOrderedRecipes();
		this.currentIndex = MathHelper.floor(this.time / 30.0F) % recipes.size();
		IRecipe<?> recipe = recipes.get(this.currentIndex);
		
		ItemStack itemstack = this.getOrderedRecipes().get(this.currentIndex).getRecipeOutput();
		List<ITextComponent> list = screen.func_231151_a_(itemstack);
		
		if(recipe instanceof CastingRecipe) {
			CastingRecipe crec = (CastingRecipe) recipe;
			itemstack = crec.getRecipeBonus();
			if(itemstack != null && !itemstack.isEmpty()) {
				list.add(translate("gui.artimancy.label.bonus").func_230529_a_(textof(": ")).func_230529_a_(crec.getRecipeBonus().getDisplayName()));
				list.add(textof(""));
			}
			int subIndex = MathHelper.floor(this.time / 10.0F) % crec.getAmountedIngredients().get(0).getMatchingStacks().length;
			list.add(translate("gui.artimancy.label.input").func_230529_a_(textof(": " + crec.getIngredient1Amount() + "x ")).func_230529_a_(crec.getAmountedIngredients().get(0).getMatchingStacks()[subIndex].getDisplayName()));
			if(crec.getAmountedIngredients().size() > 1) {
				subIndex = MathHelper.floor(this.time / 10.0F) % crec.getAmountedIngredients().get(1).getMatchingStacks().length;
				list.add(translate("gui.artimancy.label.input2").func_230529_a_(textof(": " + crec.getIngredient2Amount() + "x " )).func_230529_a_(crec.getAmountedIngredients().get(1).getMatchingStacks()[subIndex].getDisplayName()));
			}
			subIndex = MathHelper.floor(this.time / 10.0F) % crec.getCast().getMatchingStacks().length;
			list.add(translate("gui.artimancy.label.cast").func_230529_a_(textof(": ")).func_230529_a_(crec.getCast().getMatchingStacks()[subIndex].getDisplayName()));
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
	
	private static TranslationTextComponent translate(String string) {
		return new TranslationTextComponent(string);
	}
	
	private static StringTextComponent textof(String string) {
		return new StringTextComponent(string);
	}
}