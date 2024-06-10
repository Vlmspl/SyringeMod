package net.vladitandlplayer.syringemod.items.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.vladitandlplayer.syringemod.items.ModItems;

public class FortifiedRemedySyringeItem extends SyringeItem {

    public FortifiedRemedySyringeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!player.level.isClientSide) {
            if (target != null) {
                target.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 10800, 3));
                target.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 10800, 3));
            } else if (target == null) {
                if (player.isCrouching()) {
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 10800, 3));
                    player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 10800, 3));
                }

            }

            // Display message to player

            if (!player.isCreative()) {
                ItemStack emptySyringe = new ItemStack(ModItems.EMPTY_SYRINGE.get());
                if (!player.getInventory().add(emptySyringe)) {
                    // If the inventory is full, drop the item into the world
                    player.drop(emptySyringe, false);
                }
                stack.shrink(1);
                player.inventoryMenu.broadcastChanges();
            }
        }

        return InteractionResult.SUCCESS;
    }

}
