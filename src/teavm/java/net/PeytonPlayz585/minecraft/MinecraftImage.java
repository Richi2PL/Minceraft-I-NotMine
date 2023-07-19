package net.PeytonPlayz585.minecraft;

public class MinecraftImage {

	public final int[] data;
	public final int w;
	public final int h;
	public final boolean alpha;

	public MinecraftImage(int pw, int ph, boolean palpha) {
		this.w = pw;
		this.h = ph;
		this.alpha = palpha;
		this.data = new int[pw * ph];
	}

	public MinecraftImage(int[] pdata, int pw, int ph, boolean palpha) {
		if (pdata.length != pw * ph) {
			throw new IllegalArgumentException("array size does not equal image size");
		}
		this.w = pw;
		this.h = ph;
		this.alpha = palpha;
		if (!palpha) {
			for (int i = 0; i < pdata.length; ++i) {
				pdata[i] = pdata[i] | 0xFF000000;
			}
		}
		this.data = pdata;
	}

	public MinecraftImage getSubImage(int x, int y, int pw, int ph) {
		int[] img = new int[pw * ph];
		for (int i = 0; i < ph; ++i) {
			System.arraycopy(data, (i + y) * this.w + x, img, i * pw, pw);
		}
		return new MinecraftImage(img, pw, ph, alpha);
	}

}