package com.ghzdude.effortlesspatching;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import nl.requios.effortlessbuilding.compatibility.CompatHelper;

import java.util.ArrayList;

public class StackQueue extends ArrayList<ItemStack> {

    public ItemStack popStack(int amount, boolean isCreative) {
        if (amount <= 0 || isEmpty()) return ItemStack.EMPTY;
        var curStack = get(0);
        ItemStack returnable;
        if (isCreative) {
            returnable = curStack.copy();
            returnable.setCount(amount);
            return returnable;
        }
        if (!(curStack.getItem() instanceof ItemBlock) && CompatHelper.isItemBlockProxy(curStack)) {
            returnable = CompatHelper.getItemBlockFromStack(curStack).splitStack(amount);
        } else {
            returnable = curStack.splitStack(amount);
            if (get(0).isEmpty()) remove(0);
        }
        return returnable;
    }
}
