package com.iafenvoy.jupiter.malilib.util;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Objects;

/**
 * A wrapper around ItemStack, that implements hashCode() and equals().
 * Whether or not the NBT data is considered by those methods,
 * depends on the checkNBT argument to the constructor.
 */
public class ItemType {
    private final boolean checkNBT;
    private ItemStack stack;

    public ItemType(ItemStack stack) {
        this(stack, true, true);
    }

    public ItemType(ItemStack stack, boolean copy, boolean checkNBT) {
        this.stack = stack.isEmpty() ? ItemStack.EMPTY : (copy ? stack.copy() : stack);
        this.checkNBT = checkNBT;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public boolean checkNBT() {
        return this.checkNBT;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.stack.getItem().hashCode();

        if (this.checkNBT()) {
            result = prime * result + (this.stack.getNbt() != null ? this.stack.getNbt().hashCode() : 0);
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;

        ItemType other = (ItemType) obj;

        if (this.stack.isEmpty() || other.stack.isEmpty()) {
            return this.stack.isEmpty() == other.stack.isEmpty();
        } else {
            if (this.stack.getItem() != other.stack.getItem()) {
                return false;
            }

            return !this.checkNBT() || Objects.equals(this.stack.getNbt(), other.stack.getNbt());
        }
    }

    @Override
    public String toString() {
        if (this.checkNBT()) {
            Identifier rl = Registries.ITEM.getId(this.stack.getItem());
            return rl + " " + this.stack.getNbt();
        } else {
            return Registries.ITEM.getId(this.stack.getItem()).toString();
        }
    }
}
