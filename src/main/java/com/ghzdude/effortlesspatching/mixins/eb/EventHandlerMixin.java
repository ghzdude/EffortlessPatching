package com.ghzdude.effortlesspatching.mixins.eb;

import net.minecraftforge.event.world.BlockEvent;
import nl.requios.effortlessbuilding.EventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EventHandler.class)
public abstract class EventHandlerMixin {

//    @Redirect(method = "onBlockPlaced",
//            remap = false,
//            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/world/BlockEvent$PlaceEvent;setCanceled(Z)V"))
//    private static void stopCancel(BlockEvent.PlaceEvent instance, boolean b) {
//        // don't cancel the event
//    }

    @Inject(method = "onBlockPlaced",
            remap = false,
            at = @At(value = "HEAD"),
            cancellable = true)
    private static void stopCancel(BlockEvent.PlaceEvent event, CallbackInfo ci) {
        ci.cancel();
    }
}
