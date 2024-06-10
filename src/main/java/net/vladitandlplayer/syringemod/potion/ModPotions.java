package net.vladitandlplayer.syringemod.potion;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vladitandlplayer.syringemod.SyringeMod;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, SyringeMod.MOD_ID);

    public static final RegistryObject<Potion> FORTIFIED_REMEDY_POTION =
            POTIONS.register("fortified_remedy_potion", () -> new Potion(
                    new MobEffectInstance(MobEffects.REGENERATION, 7200, 2),
                    new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 7200, 2)));

    public static final RegistryObject<Potion> NOCTURNAL_SPRINT_POTION =
            POTIONS.register("nocturnal_sprint_potion", () -> new Potion(
                    new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 7200, 2),
                    new MobEffectInstance(MobEffects.NIGHT_VISION, 7200, 0)));

    public static final RegistryObject<Potion> DEEP_DIVE_POTION =
            POTIONS.register("deep_dive_potion", () -> new Potion(
                    new MobEffectInstance(MobEffects.WATER_BREATHING, 7200, 0),
                    new MobEffectInstance(MobEffects.NIGHT_VISION, 7200, 0)));

    public static final RegistryObject<Potion> VENOM_POTION =
            POTIONS.register("venom_potion", () -> new Potion(
                    new MobEffectInstance(MobEffects.POISON, 7200, 2),
                    new MobEffectInstance(MobEffects.WITHER, 7200, 2)));

    public static final RegistryObject<Potion> AGILE_POTION =
            POTIONS.register("agile_potion", () -> new Potion(
                    new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 7200, 2),
                    new MobEffectInstance(MobEffects.DAMAGE_BOOST, 7200, 2)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}