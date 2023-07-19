package net.minecraft.client.gui;

import java.awt.Dialog;
import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import net.minecraft.client.PlayerLoader;
import net.minecraft.game.level.World;

public class GuiLoadLevel extends GuiScreen implements Runnable {
	private GuiScreen parent;
	private boolean finished = false;
	private boolean loaded = false;
	private String[] levels = null;
	private String status = "";
	protected String title = "Load level";
	private boolean frozen = false;
	private File selectedFile;

	public GuiLoadLevel(GuiScreen var1) {
		this.parent = var1;
	}

	public final void updateScreen() {
		if(this.selectedFile != null) {
			if(!this.selectedFile.getName().endsWith(".mclevel")) {
				this.selectedFile = new File(this.selectedFile.getAbsolutePath() + ".mclevel");
			}

			this.openLevel(this.selectedFile);
			this.selectedFile = null;
			this.mc.displayGuiScreen((GuiScreen)null);
		}

	}

	public void run() {
		try {
			this.status = "Getting level list..";
			URL var1 = new URL("http://" + this.mc.minecraftUri + "/listmaps.jsp?user=" + this.mc.session.username);
			BufferedReader var3 = new BufferedReader(new InputStreamReader(var1.openConnection().getInputStream()));
			this.levels = var3.readLine().split(";");
			if(this.levels.length >= 5) {
				this.setLevels(this.levels);
				this.loaded = true;
				return;
			}

			this.status = this.levels[0];
			this.finished = true;
		} catch (Exception var2) {
			var2.printStackTrace();
			this.status = "Failed to load levels";
			this.finished = true;
		}

	}

	protected void setLevels(String[] var1) {
		for(int var2 = 0; var2 < 5; ++var2) {
			((GuiButton)this.controlList.get(var2)).enabled = !var1[var2].equals("-");
			((GuiButton)this.controlList.get(var2)).displayString = var1[var2];
			((GuiButton)this.controlList.get(var2)).visible = true;
		}

		((GuiButton)this.controlList.get(5)).visible = true;
	}

	public void initGui() {
		(new Thread(this)).start();

		for(int var1 = 0; var1 < 5; ++var1) {
			this.controlList.add(new GuiButton(var1, this.width / 2 - 100, this.height / 6 + var1 * 24, "---"));
			((GuiButton)this.controlList.get(var1)).visible = false;
		}

		this.controlList.add(new GuiButton(5, this.width / 2 - 100, this.height / 6 + 120 + 12, "Load file..."));
		this.controlList.add(new GuiButton(6, this.width / 2 - 100, this.height / 6 + 168, "Cancel"));
		((GuiButton)this.controlList.get(5)).visible = false;
	}

	protected final void actionPerformed(GuiButton var1) {
		if(!this.frozen) {
			if(var1.enabled) {
				if(this.loaded && var1.id < 5) {
					this.openLevel(var1.id);
				}

				if(this.finished || this.loaded && var1.id == 5) {
					this.frozen = true;
					GuiLevelDialog var2 = new GuiLevelDialog(this);
					var2.setDaemon(true);
					var2.start();
				}

				if(this.finished || this.loaded && var1.id == 6) {
					this.mc.displayGuiScreen(this.parent);
				}

			}
		}
	}

	protected FileDialog saveFileDialog() {
		return new FileDialog((Dialog)null, "Load level", 0);
	}

	protected void openLevel(int var1) {
		this.mc.displayGuiScreen((GuiScreen)null);
		this.mc.setIngameFocus();
	}

	public final void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
		if(!this.loaded) {
			drawCenteredString(this.fontRenderer, this.status, this.width / 2, this.height / 2 - 4, 16777215);
		}

		super.drawScreen(var1, var2, var3);
	}

	protected void openLevel(File var1) {
		try {
			FileInputStream var4 = new FileInputStream(var1);
			World var2 = (new PlayerLoader(this.mc, this.mc.loadingScreen)).load(var4);
			var4.close();
			this.mc.setLevel(var2);
		} catch (IOException var3) {
			var3.printStackTrace();
		}
	}

	static File a(GuiLoadLevel var0, File var1) {
		return var0.selectedFile = var1;
	}

	static boolean unknown(GuiLoadLevel var0, boolean var1) {
		return var0.frozen = false;
	}
}
