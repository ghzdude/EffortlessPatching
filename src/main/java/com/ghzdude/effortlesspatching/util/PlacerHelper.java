package com.ghzdude.effortlesspatching.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import nl.requios.effortlessbuilding.EffortlessBuilding;
import nl.requios.effortlessbuilding.buildmode.BuildModes;
import nl.requios.effortlessbuilding.buildmode.ModeSettingsManager;
import nl.requios.effortlessbuilding.buildmodifier.ModifierSettingsManager;
import nl.requios.effortlessbuilding.network.AddUndoMessage;
import nl.requios.effortlessbuilding.network.RequestLookAtMessage;

public class PlacerHelper {

    public static boolean handleBlockPlace(PlayerInteractEvent.RightClickBlock event) {
        var player = event.getEntityPlayer();
        var buildMode = ModeSettingsManager.getModeSettings(player).getBuildMode();
        var modifierSettings = ModifierSettingsManager.getModifierSettings(player);

        var face = event.getFace();
        Vec3d hitVec = event.getHitVec();
        if (face == null || hitVec == null)
            return false;

        sendPackets(player, event.getHand(), face, event.getPos(), hitVec, event.getItemStack());

        return buildMode != BuildModes.BuildModeEnum.NORMAL || modifierSettings.doQuickReplace();
    }

    public static void handleBlockPlaceEmpty(PlayerInteractEvent.RightClickEmpty event) {
        var player = event.getEntityPlayer();
        if (!player.isCreative()) return;

        var buildMode = ModeSettingsManager.getModeSettings(player).getBuildMode();
        var modifierSettings = ModifierSettingsManager.getModifierSettings(player);

        var result = ForgeHooks.rayTraceEyes(player, 1024);
        if (result == null || result.typeOfHit != RayTraceResult.Type.BLOCK ||
                result.sideHit == null || result.hitVec == null)
            return;

        if (buildMode != BuildModes.BuildModeEnum.NORMAL || modifierSettings.doQuickReplace()) {
            sendPackets(player, event.getHand(), result.sideHit, event.getPos(), result.hitVec, event.getItemStack());
        }
    }

    public static void sendPackets(EntityPlayer player, EnumHand hand, EnumFacing sideHit, BlockPos posHit, Vec3d hitVec, ItemStack usedStack) {
        var currentState = player.world.getBlockState(posHit);
        var block = Block.getBlockFromItem(usedStack.getItem());
        var nextState = block.getStateForPlacement(player.world, posHit, sideHit,
                (float) hitVec.x, (float) hitVec.y, (float) hitVec.z,
                usedStack.getMetadata(), player, hand);
        if (!player.world.isRemote) {
            EffortlessBuilding.packetHandler.sendTo(new RequestLookAtMessage(true), (EntityPlayerMP) player);
            EffortlessBuilding.packetHandler.sendTo(new AddUndoMessage(posHit, currentState, nextState), (EntityPlayerMP) player);
        }
    }
}
