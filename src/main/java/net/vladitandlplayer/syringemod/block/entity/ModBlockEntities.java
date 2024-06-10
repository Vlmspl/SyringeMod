package net.vladitandlplayer.syringemod.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vladitandlplayer.syringemod.SyringeMod;
import net.vladitandlplayer.syringemod.block.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SyringeMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<PotionMixerBlockEntity>> POTION_MIXER =
            BLOCK_ENTITIES.register("potion_mixer", () -> BlockEntityType.Builder.of(PotionMixerBlockEntity::new,
                    ModBlocks.POTION_MIXER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
