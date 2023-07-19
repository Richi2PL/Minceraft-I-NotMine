package net.minecraft.client.render.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import net.PeytonPlayz585.math.MathHelper;
import net.minecraft.game.level.block.Block;

public class TextureGearsFX extends TextureFX {
	private int gearRotation = 0;
	private int[] gearColor = new int[1024];
	private int[] gearMiddleColor = new int[1024];
	private int gearRotationDir;

	public TextureGearsFX(int var1) {
		super(Block.cog.blockIndexInTexture + var1);
		this.gearRotationDir = (var1 << 1) - 1;
		this.gearRotation = 2;

		try {
			BufferedImage var3 = ImageIO.read(TextureGearsFX.class.getResource("/misc/gear.png"));
			var3.getRGB(0, 0, 32, 32, this.gearColor, 0, 32);
			var3 = ImageIO.read(TextureGearsFX.class.getResource("/misc/gearmiddle.png"));
			var3.getRGB(0, 0, 16, 16, this.gearMiddleColor, 0, 16);
		} catch (IOException var2) {
			var2.printStackTrace();
		}
	}

	public final void onTick() {
		this.gearRotation = this.gearRotation + this.gearRotationDir & 63;
		float var1 = MathHelper.sin((float)this.gearRotation / 64.0F * (float)Math.PI * 2.0F);
		float var2 = MathHelper.cos((float)this.gearRotation / 64.0F * (float)Math.PI * 2.0F);

		for(int var3 = 0; var3 < 16; ++var3) {
			for(int var4 = 0; var4 < 16; ++var4) {
				float var5 = ((float)var3 / 15.0F - 0.5F) * 31.0F;
				float var6 = ((float)var4 / 15.0F - 0.5F) * 31.0F;
				float var7 = var2 * var5 - var1 * var6;
				var5 = var2 * var6 + var1 * var5;
				int var11 = (int)(var7 + 16.0F);
				int var10 = (int)(var5 + 16.0F);
				int var12 = 0;
				if(var11 >= 0 && var10 >= 0 && var11 < 32 && var10 < 32) {
					var12 = this.gearColor[var11 + (var10 << 5)];
					var10 = this.gearMiddleColor[var3 + (var4 << 4)];
					if(var10 >>> 24 > 128) {
						var12 = var10;
					}
				}

				var10 = var12 >> 16 & 255;
				var11 = var12 >> 8 & 255;
				int var8 = var12 & 255;
				var12 = var12 >>> 24 > 128 ? 255 : 0;
				int var9 = var3 + (var4 << 4);
				this.imageData[var9 << 2] = (byte)var10;
				this.imageData[(var9 << 2) + 1] = (byte)var11;
				this.imageData[(var9 << 2) + 2] = (byte)var8;
				this.imageData[(var9 << 2) + 3] = (byte)var12;
			}
		}

	}
}
