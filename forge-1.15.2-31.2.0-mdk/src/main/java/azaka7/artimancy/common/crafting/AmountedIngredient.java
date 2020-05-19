package azaka7.artimancy.common.crafting;

import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.crafting.IIngredientSerializer;

public class AmountedIngredient extends Ingredient{
	
	private final byte amount;
	
	public static final AmountedIngredient EMPTY = new AmountedIngredient(Stream.empty(), 0);

	protected AmountedIngredient(Stream<? extends Ingredient.IItemList> itemLists, int amount) {
		super(itemLists);
		if(amount < 0) {amount = 0;} else if(amount > 64) { amount = 64;}
		this.amount = (byte) amount;
	}
	
	public byte getAmount() {
		return amount;
	}
	
	@Override
	public boolean test(@Nullable ItemStack stack) {
		return super.test(stack) && (stack.getCount() >= this.amount);
	}
	
	public static AmountedIngredient fromItemListStream(Stream<? extends Ingredient.IItemList> stream, int amount) {
		AmountedIngredient ingredient = new AmountedIngredient(stream, amount);
		if(amount <= 0) {return EMPTY;}
		return ingredient.hasNoMatchingItems() ? EMPTY : ingredient;
	}
	
	public static class Serializer implements IIngredientSerializer<AmountedIngredient>
	{
		public static final Serializer INSTANCE = new Serializer();

        @Override
        public AmountedIngredient parse(PacketBuffer buffer)
        {
        	int varInt = buffer.readVarInt();
        	ItemStack itemstack = buffer.readItemStack();
        	byte amt = buffer.readByte();
            return AmountedIngredient.fromItemListStream(Stream.generate(() -> new Ingredient.SingleItemList(itemstack)).limit(varInt), amt);
        }
        
        @Override
        public AmountedIngredient parse(JsonObject json)
        {
           return AmountedIngredient.fromItemListStream(Stream.of(Ingredient.deserializeItemList(json)), json.has("amount") ? json.get("amount").getAsInt() : 1);
        }
        
        @Override
        public void write(PacketBuffer buffer, AmountedIngredient ingredient)
        {
            ItemStack[] items = ingredient.getMatchingStacks();
            buffer.writeVarInt(items.length);

            for (ItemStack stack : items)
                buffer.writeItemStack(stack);
            
            buffer.writeByte(ingredient.amount);
        }
    }

}
