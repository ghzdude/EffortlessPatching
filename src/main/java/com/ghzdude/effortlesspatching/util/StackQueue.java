package com.ghzdude.effortlesspatching.util;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import nl.requios.effortlessbuilding.compatibility.CompatHelper;

import java.util.ArrayList;

public class StackQueue extends ArrayList<ItemStack> {


    public ItemStack popStack() {
        if (isEmpty()) return ItemStack.EMPTY;
        var curStack = get(0);
        ItemStack returnable;
        if (!(curStack.getItem() instanceof ItemBlock) && CompatHelper.isItemBlockProxy(curStack)) {
            returnable = CompatHelper.getItemBlockFromStack(curStack);
        } else {
            returnable = curStack;
            if (curStack.isEmpty())
                remove(0);
        }
        return returnable;
    }
}
