package com.ghzdude.effortlesspatching;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.requios.effortlessbuilding.EffortlessBuilding;
import nl.requios.effortlessbuilding.buildmode.BuildModes;
import nl.requios.effortlessbuilding.buildmode.ModeSettingsManager;
import nl.requios.effortlessbuilding.buildmodifier.ModifierSettingsManager;
import nl.requios.effortlessbuilding.network.AddUndoMessage;
import nl.requios.effortlessbuilding.network.RequestLookAtMessage;

public class CommonProxy {

    @SubscribeEvent
    public void BlockPlace(PlayerInteractEvent.RightClickBlock event) {
        var player = event.getEntityPlayer();
        var buildMode = ModeSettingsManager.getModeSettings(player).getBuildMode();
        var modifierSettings = ModifierSettingsManager.getModifierSettings(player);
        var face = event.getFace();
        if (face == null) return;

        if (buildMode != BuildModes.BuildModeEnum.NORMAL || modifierSettings.doQuickReplace()) {
            event.setCanceled(true);
        }
        
//        var hitVec = ForgeHooks.rayTraceEyeHitVec(player, 1024);
        var hitVec = event.getHitVec();
        if (hitVec == null) return;

        var blockPos = event.getPos();
        var currentState = event.getWorld().getBlockState(blockPos);
        var stack = event.getItemStack();
        var block = Block.getBlockFromItem(stack.getItem());
        var nextState = block.getStateForPlacement(event.getWorld(), blockPos, event.getFace(),
                        (float) hitVec.x, (float) hitVec.y, (float) hitVec.z,
                        stack.getMetadata(), event.getEntityPlayer(), event.getHand());
        if (!player.world.isRemote) {
            EffortlessBuilding.packetHandler.sendTo(new RequestLookAtMessage(true), (EntityPlayerMP) player);
            EffortlessBuilding.packetHandler.sendTo(new AddUndoMessage(event.getPos(), currentState, nextState), (EntityPlayerMP) player);
        }
    }
}
