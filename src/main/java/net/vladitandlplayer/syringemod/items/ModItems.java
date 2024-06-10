package net.vladitandlplayer.syringemod.items;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vladitandlplayer.syringemod.SyringeMod;
import net.vladitandlplayer.syringemod.items.custom.*;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SyringeMod.MOD_ID);

    public static final RegistryObject<Item> EMPTY_SYRINGE = ITEMS.register("empty_syringe",
            () -> new EmptySyringeItem(new Item.Properties().tab(ModCreativeModeTab.SYRINGE_MOD_TAB))
    );
    public static final RegistryObject<Item> WATER_SYRINGE = ITEMS.register("water_syringe",
            () -> new WaterSyringeItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.SYRINGE_MOD_TAB))
    );
    public static final RegistryObject<Item> LAVA_SYRINGE = ITEMS.register("lava_syringe",
            () -> new LavaSyringeItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.SYRINGE_MOD_TAB))
    );
    public static final RegistryObject<Item> VENOM_SYRINGE = ITEMS.register("venom_syringe",
            () -> new VenomSyringeItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.SYRINGE_MOD_TAB))
    );
    public static final RegistryObject<Item> MILK_SYRINGE = ITEMS.register("milk_syringe",
            () -> new MilkSyringeItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.SYRINGE_MOD_TAB))
    );
    public static final RegistryObject<Item> FORTIFIED_REMEDY_SYRINGE = ITEMS.register("fortified_remedy_syringe",
            () -> new FortifiedRemedySyringeItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.SYRINGE_MOD_TAB))
    );
    public static final RegistryObject<Item> NOCTURNAL_SPRINT_SYRINGE = ITEMS.register("nocturnal_sprint_syringe",
            () -> new NocturnalSprintSyringeItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.SYRINGE_MOD_TAB))
    );
    public static final RegistryObject<Item> DEEP_DIVE_SYRINGE = ITEMS.register("deep_dive_syringe",
            () -> new DeepDiveSyringeItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.SYRINGE_MOD_TAB))
    );
    public static final RegistryObject<Item> AGILE_SYRINGE = ITEMS.register("agile_syringe",
            () -> new AgileSyringeItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.SYRINGE_MOD_TAB))
    );
    public static final RegistryObject<Item> ALCHEMIC_INFUSION_BLEND = ITEMS.register("alchemic_infusion_blend",
            () -> new Item(new Item.Properties().stacksTo(64).tab(ModCreativeModeTab.SYRINGE_MOD_TAB))
    );
    public static final RegistryObject<Item> WHISK = ITEMS.register("whisk",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.SYRINGE_MOD_TAB))
    );


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
