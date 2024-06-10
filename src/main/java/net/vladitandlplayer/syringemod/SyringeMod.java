package net.vladitandlplayer.syringemod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vladitandlplayer.syringemod.block.ModBlocks;
import net.vladitandlplayer.syringemod.block.entity.ModBlockEntities;
import net.vladitandlplayer.syringemod.items.ModItems;
import net.vladitandlplayer.syringemod.potion.ModPotions;
import net.vladitandlplayer.syringemod.recipe.ModRecipes;
import net.vladitandlplayer.syringemod.screen.ModMenuTypes;
import net.vladitandlplayer.syringemod.screen.PotionMixerScreen;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SyringeMod.MOD_ID)
public class SyringeMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "syringemod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SyringeMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModPotions.register(modEventBus);

        modEventBus.addListener(this::commonSetup);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModMenuTypes.POTION_MIXING_MENU.get(), PotionMixerScreen::new);
        }
    }


}
