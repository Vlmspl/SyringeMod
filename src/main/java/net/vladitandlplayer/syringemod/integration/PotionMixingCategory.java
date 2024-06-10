package net.vladitandlplayer.syringemod.integration;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.vladitandlplayer.syringemod.SyringeMod;
import net.vladitandlplayer.syringemod.block.ModBlocks;
import net.vladitandlplayer.syringemod.recipe.PotionMixing;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PotionMixingCategory implements IRecipeCategory<PotionMixing> {
    public final static ResourceLocation UID = new ResourceLocation(SyringeMod.MOD_ID, "potion_mixing");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(SyringeMod.MOD_ID, "textures/gui/potion_mixer_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public PotionMixingCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.POTION_MIXER.get()));
    }

    @Override
    public RecipeType<PotionMixing> getRecipeType() {
        return JEISyringeModPlugin.POTION_MIXING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.potion_mixer.recipe_title");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PotionMixing recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 86, 15).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 116, 15).addIngredients(recipe.getIngredients().get(1));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 101, 46).addItemStack(recipe.getResultItem());
    }
}
