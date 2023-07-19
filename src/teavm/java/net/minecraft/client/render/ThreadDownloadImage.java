package net.minecraft.client.render;

import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

final class ThreadDownloadImage extends Thread {
	private String location;
	private ImageBufferDownload buffer;
	private ThreadDownloadImageData imageData;

	ThreadDownloadImage(ThreadDownloadImageData var1, String var2, ImageBufferDownload var3) {
		this.imageData = var1;
		this.location = var2;
		this.buffer = var3;
	}

	public final void run() {
		HttpURLConnection var1 = null;

		try {
			URL var2 = new URL(this.location);
			var1 = (HttpURLConnection)var2.openConnection();
			var1.setDoInput(true);
			var1.setDoOutput(false);
			var1.connect();
			if(var1.getResponseCode() == 404) {
				return;
			}

			if(this.buffer == null) {
				this.imageData.image = ImageIO.read(var1.getInputStream());
			} else {
				this.imageData.image = this.buffer.parseUserSkin(ImageIO.read(var1.getInputStream()));
			}

			return;
		} catch (Exception var5) {
			var5.printStackTrace();
		} finally {
			var1.disconnect();
		}

	}
}
