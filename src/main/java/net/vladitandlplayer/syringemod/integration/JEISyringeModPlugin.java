package net.vladitandlplayer.syringemod.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.vladitandlplayer.syringemod.SyringeMod;
import net.vladitandlplayer.syringemod.recipe.PotionMixing;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEISyringeModPlugin implements IModPlugin {
    public static RecipeType<PotionMixing> POTION_MIXING_TYPE =
            new RecipeType<>(PotionMixingCategory.UID, PotionMixing.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(SyringeMod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                PotionMixingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<PotionMixing> recipesInfusing = rm.getAllRecipesFor(PotionMixing.Type.INSTANCE);
        registration.addRecipes(POTION_MIXING_TYPE, recipesInfusing);
    }
}