package com.ghzdude.effortlesspatching.mixins.eb;

import com.ghzdude.effortlesspatching.StackQueue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import nl.requios.effortlessbuilding.buildmode.BuildModes;
import nl.requios.effortlessbuilding.buildmode.ModeSettingsManager;
import nl.requios.effortlessbuilding.buildmodifier.BuildModifiers;
import nl.requios.effortlessbuilding.compatibility.CompatHelper;
import nl.requios.effortlessbuilding.render.BlockPreviewRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(BuildModes.class)
public abstract class BuildModifiersMixin {

    @Redirect(method = "onBlockPlacedMessage",
            remap = false,
            at = @At(value = "INVOKE", target = "Lnl/requios/effortlessbuilding/buildmodifier/BuildModifiers;onBlockPlaced(Lnet/minecraft/entity/player/EntityPlayer;Ljava/util/List;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/util/math/Vec3d;Z)V"))
    private static void BlockPlacePatch(EntityPlayer player, List<BlockPos> startBlocks, EnumFacing sideHit, Vec3d hitVec, boolean isStart) {
        World world = player.world;
        List<BlockPos> toPlace = BuildModifiers.findCoordinates(player, startBlocks);
        var mode = ModeSettingsManager.getModeSettings(player).getBuildMode();
        if (mode == BuildModes.BuildModeEnum.NORMAL)
            return;

        if (world.isRemote) {
            BlockPreviewRenderer.onBlocksPlaced();
            return;
        }

        hitVec = hitVec.subtract(
                ((int) hitVec.x),
                ((int) hitVec.y),
                ((int) hitVec.z)
        );

        //Get itemstack
        ItemStack placingStack = player.getHeldItem(EnumHand.MAIN_HAND);
        EnumHand activeHand = EnumHand.MAIN_HAND;
        if (placingStack.isEmpty() || !CompatHelper.isItemBlockProxy(placingStack)) {
            placingStack = player.getHeldItem(EnumHand.OFF_HAND);
            activeHand = EnumHand.OFF_HAND;
        }

        StackQueue usableStacks = new StackQueue();
        NonNullList<ItemStack> mainInventory = player.inventory.mainInventory;
        for (ItemStack invStack : mainInventory) {
            if (!placingStack.hasTagCompound() && !invStack.isEmpty() &&
                    invStack.getItem() == placingStack.getItem() &&
                    invStack.getMetadata() == placingStack.getMetadata()) {

                usableStacks.add(invStack);
            }
        }

        for (int i = isStart ? 0 : 1; i < toPlace.size(); i++) {
            BlockPos pos = toPlace.get(i);
            if (!world.isBlockLoaded(pos, true))
                continue;

            ItemStack copy = usableStacks.popStack(1, player.isCreative());

//            IBlockState state = Block.getBlockFromItem(copy.getItem())
//                    .getStateForPlacement(world, pos, sideHit, (float) hitVec.x, (float) hitVec.y, (float) hitVec.z,
//                            copy.getMetadata(), player, activeHand);

            int j = copy.getCount(), k = copy.getItemDamage();
            copy.onItemUse(player, world, pos, activeHand, sideHit, (float) hitVec.x, (float) hitVec.y, (float) hitVec.z);
            if (player.isCreative()) {
                copy.setCount(j);
                copy.setItemDamage(k);
            }
//                SurvivalHelper.placeBlock(world, player, pos, state, copy,
//                        sideHit, hitVec, false, false, false);
        }
    }
}
