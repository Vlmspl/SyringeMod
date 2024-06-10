package net.vladitandlplayer.syringemod.items.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.network.chat.Component;
import net.vladitandlplayer.syringemod.items.ModItems;

public class LavaSyringeItem extends SyringeItem {

    public LavaSyringeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!player.level.isClientSide) {
            if (target != null) {
                target.setSecondsOnFire(600);
            } else if (target == null) {
                if (player.isCrouching()) {
                    player.setSecondsOnFire(600);
                }
            }
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
