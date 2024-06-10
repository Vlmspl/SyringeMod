package net.vladitandlplayer.syringemod.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vladitandlplayer.syringemod.SyringeMod;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SyringeMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<PotionMixing>> POTION_MIXING_SERIALIZER =
            SERIALIZERS.register("potion_mixing", () -> PotionMixing.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
