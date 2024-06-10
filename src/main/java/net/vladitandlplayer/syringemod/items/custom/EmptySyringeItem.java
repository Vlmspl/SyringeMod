package net.vladitandlplayer.syringemod.items.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import net.vladitandlplayer.syringemod.items.ModItems;

public class EmptySyringeItem extends Item {

    public EmptySyringeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack leftHandItem = player.getItemInHand(InteractionHand.OFF_HAND);
        ItemStack itemInHand = player.getItemInHand(hand);

        ItemStack filledSyringe = getFilledSyringe(leftHandItem);
        if (filledSyringe != null) {
            // Create a syringe item with the appropriate content
            handleSyringeConversion(player, itemInHand, leftHandItem, filledSyringe);
            return InteractionResultHolder.sidedSuccess(itemInHand, level.isClientSide());
        }

        // Return success without doing anything if the left hand item is not suitable for filling a syringe
        return InteractionResultHolder.pass(itemInHand);
    }

    public static ItemStack getFilledSyringe(ItemStack offHandItem) {
        if (offHandItem.getItem() == Items.LAVA_BUCKET) {
            return new ItemStack(ModItems.LAVA_SYRINGE.get());
        } else if (offHandItem.getItem() == Items.POTION && getPotionID(offHandItem).equals("minecraft:water")) {
            return new ItemStack(ModItems.WATER_SYRINGE.get());
        } else if (offHandItem.getItem() == Items.POTION && getPotionID(offHandItem).equals("syringemod:venom_potion")) {
            return new ItemStack(ModItems.VENOM_SYRINGE.get());
        } else if (offHandItem.getItem() == Items.MILK_BUCKET) {
            return new ItemStack(ModItems.MILK_SYRINGE.get());
        } else if (offHandItem.getItem() == Items.POTION && getPotionID(offHandItem).equals("syringemod:fortified_remedy_potion")) {
            return new ItemStack(ModItems.FORTIFIED_REMEDY_SYRINGE.get());
        } else if (offHandItem.getItem() == Items.POTION && getPotionID(offHandItem).equals("syringemod:nocturnal_sprint_potion")) {
            return new ItemStack(ModItems.NOCTURNAL_SPRINT_SYRINGE.get());
        } else if (offHandItem.getItem() == Items.POTION && getPotionID(offHandItem).equals("syringemod:deep_dive_potion")) {
            return new ItemStack(ModItems.DEEP_DIVE_SYRINGE.get());
        } else if (offHandItem.getItem() == Items.POTION && getPotionID(offHandItem).equals("syringemod:agile_potion")) {
            return new ItemStack(ModItems.AGILE_SYRINGE.get());
        }
        return null;
    }

    private static String getPotionID(ItemStack itemStack) {
        return itemStack.getTag().getString("Potion");
    }

    private void handleSyringeConversion(Player player, ItemStack itemInHand, ItemStack leftHandItem, ItemStack newSyringe) {
        if (itemInHand.getCount() > 1) {
            // If the stack has more than one item, decrease the stack size by one

            itemInHand.shrink(1);
            // Try to add the new syringe to the player's inventory
            if (!player.getInventory().add(newSyringe)) {
                // If the inventory is full, drop the new syringe on the ground
                player.drop(newSyringe, false);
            }
        } else {
            // If the stack only has one item, replace it with the new syringe
            player.setItemInHand(InteractionHand.MAIN_HAND, newSyringe);
        }

        // Decrease the water/lava bucket count or set it to an empty bucket
        if (!player.isCreative()) {
            if (leftHandItem.getItem() == Items.LAVA_BUCKET || leftHandItem.getItem() == Items.MILK_BUCKET) {
                player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.BUCKET, 1));
            } else if (leftHandItem.getItem() == Items.POTION) {
                player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.GLASS_BOTTLE, 1));
            }
        }

    }
}
