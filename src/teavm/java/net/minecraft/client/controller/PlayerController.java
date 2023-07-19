package net.minecraft.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public class PlayerController {
	protected final Minecraft mc;
	public boolean isInTestMode = false;

	public PlayerController(Minecraft var1) {
		this.mc = var1;
	}

	public void onWorldChange(World var1) {
	}

	public void clickBlock(int var1, int var2, int var3) {
		this.sendBlockRemoved(var1, var2, var3);
	}

	public boolean sendBlockRemoved(int var1, int var2, int var3) {
		this.mc.effectRenderer.addBlockDestroyEffects(var1, var2, var3);
		World var4 = this.mc.theWorld;
		Block var5 = Block.blocksList[var4.getBlockId(var1, var2, var3)];
		byte var6 = var4.getBlockMetadata(var1, var2, var3);
		boolean var7 = var4.setBlockWithNotify(var1, var2, var3, 0);
		if(var5 != null && var7) {
			var5.onBlockDestroyedByPlayer(var4, var1, var2, var3, var6);
		}

		return var7;
	}

	public void sendBlockRemoving(int var1, int var2, int var3, int var4) {
	}

	public void resetBlockRemoving() {
	}

	public void setPartialTime(float var1) {
	}

	public float getBlockReachDistance() {
		return 5.0F;
	}

	public void onUpdate() {
	}

	public boolean shouldDrawHUD() {
		return true;
	}

	public void onRespawn(EntityPlayer var1) {
	}
}
