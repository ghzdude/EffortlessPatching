package com.ghzdude.effortlesspatching;

import com.ghzdude.effortlesspatching.util.PlacerHelper;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommonProxy {

    @SubscribeEvent
    public void BlockPlace(PlayerInteractEvent.RightClickBlock event) {
        if (PlacerHelper.handleBlockPlace(event)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void BlockPlaceEmpty(PlayerInteractEvent.RightClickEmpty event) {
        PlacerHelper.handleBlockPlaceEmpty(event);
    }
}
