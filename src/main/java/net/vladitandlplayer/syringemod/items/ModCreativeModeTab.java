package net.vladitandlplayer.syringemod.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab SYRINGE_MOD_TAB = new CreativeModeTab("syringe_mod_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.EMPTY_SYRINGE.get());
        }
    };
}
