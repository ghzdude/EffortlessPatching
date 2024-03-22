package com.ghzdude.effortlesspatching.mixins.eb;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nl.requios.effortlessbuilding.helper.SurvivalHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SurvivalHelper.class)
public abstract class SurvivalPatch {

    @Inject(method = "breakBlock",
            remap = false,
            at = @At(value = "INVOKE", target = "Lnl/requios/effortlessbuilding/helper/SurvivalHelper;dropBlock(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/math/BlockPos;)V"),
            cancellable = true)
    private static void test(World world, EntityPlayer player, BlockPos pos, boolean skipChecks, CallbackInfoReturnable<Boolean> cir) {
        if (player.isCreative()) world.setBlockToAir(pos);
        //todo fix breaking with tools
//        player.getHeldItemMainhand().onBlockDestroyed(world, world.getBlockState(pos), pos, player);
        cir.setReturnValue(true);
    }
}
