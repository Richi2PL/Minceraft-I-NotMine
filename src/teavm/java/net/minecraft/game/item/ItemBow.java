package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.level.World;

public final class ItemBow extends Item {
	public ItemBow(int var1) {
		super(5);
		this.maxStackSize = 1;
	}

	public final ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
		if(var3.inventory.consumeInventoryItem(Item.arrow.shiftedIndex)) {
			var2.spawnEntityInWorld(new EntityArrow(var2, var3));
		}

		return var1;
	}
}
