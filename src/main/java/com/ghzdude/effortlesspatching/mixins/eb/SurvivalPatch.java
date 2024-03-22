package com.ghzdude.effortlesspatching.mixins.eb;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nl.requios.effortlessbuilding.helper.SurvivalHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SurvivalHelper.class)
public abstract class SurvivalPatch {

    @Inject(method = "breakBlock",
            remap = false,
            at = @At(value = "INVOKE", target = "Lnl/requios/effortlessbuilding/helper/SurvivalHelper;dropBlock(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/math/BlockPos;)V"),
            cancellable = true)
    private static void patchSurvivalBlockBreaking(World world, EntityPlayer player, BlockPos pos, boolean skipChecks, CallbackInfoReturnable<Boolean> cir) {
        if (player.isCreative()) world.setBlockToAir(pos);
        //todo fix breaking with tools
//        player.getHeldItemMainhand().onBlockDestroyed(world, world.getBlockState(pos), pos, player);
        cir.setReturnValue(true);
    }

    @Redirect(method = "placeBlock",
            remap = false,
            at = @At(value = "INVOKE", target = "Lnl/requios/effortlessbuilding/compatibility/CompatHelper;shrinkStack(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;)V"))
    private static void patchItemShrink(ItemStack index, ItemStack origStack, EntityPlayer curStack) {
        // shrinking is handled elsewhere
    }
}
