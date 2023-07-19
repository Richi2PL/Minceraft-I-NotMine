package net.minecraft.client.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import net.minecraft.client.GameSettings;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public final class FontRenderer {
	private int[] charWidth = new int[256];
	private int fontTextureName = 0;
	private int fontDisplayLists;
	private IntBuffer buffer = BufferUtils.createIntBuffer(1024);

	public FontRenderer(GameSettings var1, String var2, RenderEngine var3) {
		BufferedImage var4;
		try {
			var4 = ImageIO.read(RenderEngine.class.getResourceAsStream(var2));
		} catch (IOException var15) {
			throw new RuntimeException(var15);
		}

		int var5 = var4.getWidth();
		int var6 = var4.getHeight();
		int[] var7 = new int[var5 * var6];
		var4.getRGB(0, 0, var5, var6, var7, 0, var5);

		int var8;
		int var9;
		int var11;
		int var13;
		int var14;
		for(int var17 = 0; var17 < 128; ++var17) {
			var6 = var17 % 16;
			var8 = var17 / 16;
			var9 = 0;

			for(boolean var10 = false; var9 < 8 && !var10; ++var9) {
				var11 = (var6 << 3) + var9;
				var10 = true;

				for(int var12 = 0; var12 < 8 && var10; ++var12) {
					var13 = ((var8 << 3) + var12) * var5;
					var14 = var7[var11 + var13] & 255;
					if(var14 > 128) {
						var10 = false;
					}
				}
			}

			if(var17 == 32) {
				var9 = 4;
			}

			this.charWidth[var17] = var9;
		}

		this.fontTextureName = var3.getTexture(var2);
		this.fontDisplayLists = GL11.glGenLists(288);
		Tessellator var18 = Tessellator.instance;

		for(var6 = 0; var6 < 256; ++var6) {
			GL11.glNewList(this.fontDisplayLists + var6, GL11.GL_COMPILE);
			var18.startDrawingQuads();
			var8 = var6 % 16 << 3;
			var9 = var6 / 16 << 3;
			var18.addVertexWithUV(0.0F, 7.99F, 0.0F, (float)var8 / 128.0F, ((float)var9 + 7.99F) / 128.0F);
			var18.addVertexWithUV(7.99F, 7.99F, 0.0F, ((float)var8 + 7.99F) / 128.0F, ((float)var9 + 7.99F) / 128.0F);
			var18.addVertexWithUV(7.99F, 0.0F, 0.0F, ((float)var8 + 7.99F) / 128.0F, (float)var9 / 128.0F);
			var18.addVertexWithUV(0.0F, 0.0F, 0.0F, (float)var8 / 128.0F, (float)var9 / 128.0F);
			var18.draw();
			GL11.glTranslatef((float)this.charWidth[var6], 0.0F, 0.0F);
			GL11.glEndList();
		}

		for(var6 = 0; var6 < 32; ++var6) {
			var8 = (var6 & 8) << 3;
			var9 = (var6 & 1) * 191 + var8;
			int var19 = ((var6 & 2) >> 1) * 191 + var8;
			var11 = ((var6 & 4) >> 2) * 191 + var8;
			boolean var20 = var6 >= 16;
			if(var1.anaglyph) {
				var13 = (var11 * 30 + var19 * 59 + var9 * 11) / 100;
				var14 = (var11 * 30 + var19 * 70) / 100;
				int var16 = (var11 * 30 + var9 * 70) / 100;
				var11 = var13;
				var19 = var14;
				var9 = var16;
			}

			var6 += 2;
			if(var20) {
				var11 /= 4;
				var19 /= 4;
				var9 /= 4;
			}

			GL11.glColor4f((float)var11 / 255.0F, (float)var19 / 255.0F, (float)var9 / 255.0F, 1.0F);
		}

	}

	public final void drawStringWithShadow(String var1, int var2, int var3, int var4) {
		this.renderString(var1, var2 + 1, var3 + 1, var4, true);
		this.drawString(var1, var2, var3, var4);
	}

	public final void drawString(String var1, int var2, int var3, int var4) {
		this.renderString(var1, var2, var3, var4, false);
	}

	private void renderString(String var1, int var2, int var3, int var4, boolean var5) {
		if(var1 != null) {
			char[] var8 = var1.toCharArray();
			if(var5) {
				var4 = (var4 & 16579836) >> 2;
			}

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureName);
			float var6 = (float)(var4 >> 16 & 255) / 255.0F;
			float var7 = (float)(var4 >> 8 & 255) / 255.0F;
			float var9 = (float)(var4 & 255) / 255.0F;
			GL11.glColor4f(var6, var7, var9, 1.0F);
			this.buffer.clear();
			GL11.glPushMatrix();
			GL11.glTranslatef((float)var2, (float)var3, 0.0F);

			for(int var10 = 0; var10 < var8.length; ++var10) {
				for(; var8[var10] == 38 && var8.length > var10 + 1; var10 += 2) {
					int var11 = "0123456789abcdef".indexOf(var8[var10 + 1]);
					if(var11 < 0 || var11 > 15) {
						var11 = 15;
					}

					this.buffer.put(this.fontDisplayLists + 256 + var11 + (var5 ? 16 : 0));
					if(this.buffer.remaining() == 0) {
						this.buffer.flip();
						GL11.glCallLists(this.buffer);
						this.buffer.clear();
					}
				}

				this.buffer.put(this.fontDisplayLists + var8[var10]);
				if(this.buffer.remaining() == 0) {
					this.buffer.flip();
					GL11.glCallLists(this.buffer);
					this.buffer.clear();
				}
			}

			this.buffer.flip();
			GL11.glCallLists(this.buffer);
			GL11.glPopMatrix();
		}
	}

	public final int getStringWidth(String var1) {
		if(var1 == null) {
			return 0;
		} else {
			char[] var4 = var1.toCharArray();
			int var2 = 0;

			for(int var3 = 0; var3 < var4.length; ++var3) {
				if(var4[var3] == 38) {
					++var3;
				} else {
					var2 += this.charWidth[var4[var3]];
				}
			}

			return var2;
		}
	}
}
