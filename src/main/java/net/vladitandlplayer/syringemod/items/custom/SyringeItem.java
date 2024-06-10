package net.vladitandlplayer.syringemod.items.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.vladitandlplayer.syringemod.items.ModItems;

public class SyringeItem extends Item {

    public SyringeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // Get the targeted living entity
        LivingEntity target = getTargetedLivingEntity(player);

        if (target != null) {
            // Use syringe on the target living entity
            InteractionResult result = interactLivingEntity(player.getItemInHand(hand), player, target, hand);
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
        } else {
            // Use syringe on the player
            InteractionResult result = interactLivingEntity(player.getItemInHand(hand), player, player, hand);
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!player.level.isClientSide) {
            // Apply effect to the target living entity
            applyEffect(stack, target);
            player.displayClientMessage(Component.translatable("message.syringemod.syringe_used"), true);
        }
        return InteractionResult.SUCCESS;
    }

    private void applyEffect(ItemStack stack, LivingEntity target) {
        // Determine which effect to apply based on the liquid in the syringe
        if (hasLiquid(stack, "water")) {
            // Apply water effect (e.g., extinguish if on fire)
            if (target.isOnFire()) {
                target.clearFire();
            }
        } else if (hasLiquid(stack, "lava")) {
            // Apply lava effect (e.g., set on fire)
            target.setSecondsOnFire(600); // 30 seconds
        }
        // Add more cases for other liquids and their effects
    }

    // Method to check if the syringe contains a specific liquid
    private boolean hasLiquid(ItemStack stack, String liquid) {
        if (stack.hasTag() && stack.getTag().contains("Liquid")) {
            return stack.getTag().getString("Liquid").equals(liquid);
        }
        return false;
    }

    // Method to get the targeted entity using ray tracing
    private LivingEntity getTargetedLivingEntity(Player player) {
        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(player.getLookAngle().scale(100)); // Define the distance to look

        EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(
                player.level,
                player,
                start,
                end,
                player.getBoundingBox().expandTowards(player.getDeltaMovement()).inflate(1.0D),
                (entity) -> entity instanceof LivingEntity && entity != player
        );

        // Check if hitResult is null before trying to get the entity
        return (hitResult != null && hitResult.getEntity() instanceof LivingEntity) ? (LivingEntity) hitResult.getEntity() : null;
    }
}
