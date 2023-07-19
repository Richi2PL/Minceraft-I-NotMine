package net.minecraft.client;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;

public class MinecraftApplet extends Applet {
	private Canvas mcCanvas;
	private Minecraft mc;
	private Thread mcThread = null;

	public void init() {
		this.mcCanvas = new CanvasMinecraftApplet(this);
		boolean var1 = false;
		if(this.getParameter("fullscreen") != null) {
			var1 = this.getParameter("fullscreen").equalsIgnoreCase("true");
		}

		this.mc = new Minecraft(this.mcCanvas, this, this.getWidth(), this.getHeight(), var1);
		this.mc.minecraftUri = this.getDocumentBase().getHost();
		if(this.getDocumentBase().getPort() > 0) {
			this.mc.minecraftUri = this.mc.minecraftUri + ":" + this.getDocumentBase().getPort();
		}

		if(this.getParameter("username") != null && this.getParameter("sessionid") != null) {
			this.mc.session = new Session(this.getParameter("username"), this.getParameter("sessionid"));
			if(this.getParameter("mppass") != null) {
				this.getParameter("mppass");
			}
		}

		if(this.getParameter("loadmap_user") != null && this.getParameter("loadmap_id") != null) {
			this.mc.loadMapUser = this.getParameter("loadmap_user");
			this.mc.loadMapID = Integer.parseInt(this.getParameter("loadmap_id"));
		} else if(this.getParameter("server") != null && this.getParameter("port") != null) {
			this.mc.setServer(this.getParameter("server"), Integer.parseInt(this.getParameter("port")));
		}

		this.mc.appletMode = true;
		this.setLayout(new BorderLayout());
		this.add(this.mcCanvas, "Center");
		this.mcCanvas.setFocusable(true);
		this.validate();
	}

	public final void startMainThread() {
		if(this.mcThread == null) {
			this.mcThread = new Thread(this.mc, "Minecraft main thread");
			this.mcThread.start();
		}
	}

	public void start() {
		this.mc.isGamePaused = false;
	}

	public void stop() {
		this.mc.isGamePaused = true;
	}

	public void destroy() {
		this.shutdown();
	}

	public final void shutdown() {
		if(this.mcThread != null) {
			Minecraft var1 = this.mc;
			var1.running = false;

			try {
				this.mcThread.join(1000L);
			} catch (InterruptedException var3) {
				try {
					this.mc.shutdownMinecraftApplet();
				} catch (Exception var2) {
					var2.printStackTrace();
				}
			}

			this.mcThread = null;
		}
	}
}
