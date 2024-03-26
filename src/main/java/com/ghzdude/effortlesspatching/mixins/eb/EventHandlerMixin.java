package com.ghzdude.effortlesspatching.mixins.eb;

import net.minecraftforge.event.world.BlockEvent;
import nl.requios.effortlessbuilding.EventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("deprecation")
@Mixin(EventHandler.class)
public abstract class EventHandlerMixin {

    @Inject(method = "onBlockPlaced",
            remap = false,
            at = @At(value = "HEAD"),
            cancellable = true)
    private static void stopCancel(BlockEvent.PlaceEvent event, CallbackInfo ci) {
        ci.cancel();
    }
}
