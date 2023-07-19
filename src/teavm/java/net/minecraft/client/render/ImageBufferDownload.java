package net.minecraft.client.render;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;

public class ImageBufferDownload {
	private int[] imageData;
	private int imageWidth;
	private int imageHeight;

	public BufferedImage parseUserSkin(BufferedImage var1) {
		this.imageWidth = 64;
		this.imageHeight = 32;
		BufferedImage var2 = new BufferedImage(this.imageWidth, this.imageHeight, 2);
		Graphics var3 = var2.getGraphics();
		var3.drawImage(var1, 0, 0, (ImageObserver)null);
		var3.dispose();
		this.imageData = ((DataBufferInt)var2.getRaster().getDataBuffer()).getData();
		this.setAreaOpaque(0, 0, 32, 16);
		this.setAreaTransparent(32, 0, 64, 32);
		this.setAreaOpaque(0, 16, 64, 32);
		return var2;
	}

	private void setAreaTransparent(int var1, int var2, int var3, int var4) {
		byte var5 = 32;
		byte var11 = 64;
		byte var10 = 0;
		byte var9 = 32;
		ImageBufferDownload var8 = this;
		var2 = var9;

		boolean var10000;
		label43:
		while(true) {
			if(var2 >= var11) {
				var10000 = false;
				break;
			}

			for(int var6 = var10; var6 < var5; ++var6) {
				int var7 = var8.imageData[var2 + var6 * var8.imageWidth];
				if(var7 >>> 24 < 128) {
					var10000 = true;
					break label43;
				}
			}

			++var2;
		}

		if(!var10000) {
			for(var1 = 32; var1 < 64; ++var1) {
				for(var2 = 0; var2 < 32; ++var2) {
					this.imageData[var1 + var2 * this.imageWidth] &= 16777215;
				}
			}

		}
	}

	private void setAreaOpaque(int var1, int var2, int var3, int var4) {
		for(var1 = 0; var1 < var3; ++var1) {
			for(int var5 = var2; var5 < var4; ++var5) {
				this.imageData[var1 + var5 * this.imageWidth] |= -16777216;
			}
		}

	}
}
