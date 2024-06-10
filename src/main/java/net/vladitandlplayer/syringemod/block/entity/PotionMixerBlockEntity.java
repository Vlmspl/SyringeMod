package net.vladitandlplayer.syringemod.block.entity;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fml.common.Mod;
import net.vladitandlplayer.syringemod.SyringeMod;
import net.vladitandlplayer.syringemod.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.vladitandlplayer.syringemod.recipe.PotionMixing;
import net.vladitandlplayer.syringemod.screen.PotionMixerMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PotionMixerBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;
    private int blendAmount = 0;
    private int maxBlendAmount = 2;

    public PotionMixerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POTION_MIXER.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> PotionMixerBlockEntity.this.progress;
                    case 1 -> PotionMixerBlockEntity.this.maxProgress;
                    case 2 -> PotionMixerBlockEntity.this.blendAmount;
                    case 3 -> PotionMixerBlockEntity.this.maxBlendAmount;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> PotionMixerBlockEntity.this.progress = value;
                    case 1 -> PotionMixerBlockEntity.this.maxProgress = value;
                    case 2 -> PotionMixerBlockEntity.this.blendAmount = value;
                    case 3 -> PotionMixerBlockEntity.this.maxBlendAmount = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("menu.potion_mixer.name");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new PotionMixerMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("potion_mixer.progress", this.progress);
        nbt.putInt("potion_mixer.blend_amount", this.blendAmount);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("potion_mixer.progress");
        blendAmount = nbt.getInt("potion_mixer.blend_amount");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PotionMixerBlockEntity pEntity) {
        if(level.isClientSide()) {
            return;
        }
        //Blend check
        if (pEntity.itemHandler.getStackInSlot(0).getItem() == ModItems.ALCHEMIC_INFUSION_BLEND.get() && pEntity.blendAmount == 0) {
            pEntity.blendAmount = pEntity.maxBlendAmount;
            pEntity.itemHandler.extractItem(0, 1, false);
        }

        //Progress check
        if(hasRecipe(pEntity)) {
            pEntity.progress++;
            setChanged(level, pos, state);

            if(pEntity.progress >= pEntity.maxProgress) {
                craftItem(pEntity);
            }
        } else {
            pEntity.resetProgress();
            setChanged(level, pos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(PotionMixerBlockEntity pEntity) {
        Level level = pEntity.level;
        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for (int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        Optional<PotionMixing> recipe = level.getRecipeManager()
                .getRecipeFor(PotionMixing.Type.INSTANCE, inventory, level);

        if (pEntity.blendAmount > 0 && recipe.isPresent()) {
            // Extract the potion ingredients
            ItemStack potion1 = pEntity.itemHandler.extractItem(1, 1, false);
            ItemStack potion2 = pEntity.itemHandler.extractItem(2, 1, false);

            // Check if the extracted items are potions
            if (isPotion(potion1) && isPotion(potion2)) {
                // Create the output potion item with the correct NBT tag
                ItemStack output = new ItemStack(recipe.get().getResultItem().getItem());
                output.setTag(recipe.get().getResultItem().getTag());

                // Adjust count if needed
                output.grow(pEntity.itemHandler.getStackInSlot(3).getCount());
                pEntity.itemHandler.setStackInSlot(3, output);
                pEntity.blendAmount -= 1;
                pEntity.resetProgress();
            } else {
                // Handle error: Invalid potion ingredients
                SyringeMod.LOGGER.error("Invalid potion ingredients for crafting " + String.valueOf(hasRecipe(pEntity)) + " " + String.valueOf(pEntity.blendAmount) + " " + String.valueOf(recipe.isPresent()));
            }
        }
    }



    // Helper method to check if an item is a potion
    private static boolean isPotion(ItemStack itemStack) {
        return itemStack.getItem() instanceof PotionItem;
    }


    private static boolean hasRecipe(PotionMixerBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());

        // Copy items from the item handler to the container
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i).copy());
        }

        // Check if the inventory contains enough blend and a valid recipe
        boolean hasBlend = entity.blendAmount > 0;
        boolean hasValidRecipe = level.getRecipeManager()
                .getRecipeFor(PotionMixing.Type.INSTANCE, inventory, level)
                .isPresent();

        return hasBlend && hasValidRecipe;
    }


    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        return inventory.getItem(3).getItem() == stack.getItem() || inventory.getItem(3).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(3).getMaxStackSize() > inventory.getItem(3).getCount();
    }
}