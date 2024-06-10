package net.vladitandlplayer.syringemod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.vladitandlplayer.syringemod.SyringeMod;
import org.jetbrains.annotations.Nullable;

public class PotionMixing implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;

    public PotionMixing(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        ItemStack input1 = simpleContainer.getItem(1);
        ItemStack input2 = simpleContainer.getItem(2);

        // Check if the items match the recipe items and their NBT data
        boolean match1 = checkMatch(recipeItems.get(0), input1) && checkMatch(recipeItems.get(1), input2);
        boolean match2 = checkMatch(recipeItems.get(1), input1) && checkMatch(recipeItems.get(0), input2);

        return match1 || match2;
    }

    private boolean checkMatch(Ingredient ingredient, ItemStack stack) {
        if (stack == null) {
            return false;
        }

        for (ItemStack matchingStack : ingredient.getItems()) {
            boolean sameItem = matchingStack.getItem() == stack.getItem();
            boolean sameTag = matchingStack.getTag().equals(stack.getTag());
            SyringeMod.LOGGER.debug("Matching item: " + sameItem + ", tag: " + sameTag + ", stack: " + matchingStack);
            if (sameItem && sameTag) {
                return true;
            }
        }

        return false;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }


    public static class Type implements RecipeType<PotionMixing> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "potion_mixing";
    }

    public static class Serializer implements RecipeSerializer<PotionMixing> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(SyringeMod.MOD_ID, "potion_mixing");

        @Override
        public PotionMixing fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

            JsonArray ingredientsJson = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> ingredients = NonNullList.withSize(2, Ingredient.EMPTY);
            ItemStack potion1 = ItemStack.EMPTY;
            ItemStack potion2 = ItemStack.EMPTY;

            for (int i = 0; i < ingredients.size(); i++) {
                JsonObject ingredientJson = ingredientsJson.get(i).getAsJsonObject();
                if (ingredientJson.has("item")) {
                    // If it's an item, create an ItemStack from the item name
                    String itemName = GsonHelper.getAsString(ingredientJson, "item");
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
                    ItemStack itemStack = new ItemStack(item);
                    ingredients.set(i, Ingredient.of(itemStack));
                } else if (ingredientJson.has("potion")) {
                    // If it's a potion, create an ItemStack with the specified potion effect
                    Potion potion = Potion.byName(GsonHelper.getAsString(ingredientJson, "potion"));
                    ItemStack potionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), potion);
                    ingredients.set(i, Ingredient.of(potionStack));
                }
            }

            return new PotionMixing(recipeId, result, ingredients);
        }



        @Override
        public @Nullable PotionMixing fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            output.setTag(buf.readNbt()); // Read NBT data for the output item
            return new PotionMixing(id, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, PotionMixing recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItem(recipe.getResultItem()); // Write item without NBT data first
            buf.writeNbt(recipe.getResultItem().getTag()); // Write NBT data for the output item
        }

    }
}
