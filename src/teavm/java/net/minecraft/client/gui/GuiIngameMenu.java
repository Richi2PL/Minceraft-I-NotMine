package net.minecraft.client.gui;

import org.lwjgl.opengl.GL11;

public final class GuiIngameMenu extends GuiScreen {
	public final void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4, "Options..."));
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 24, "Generate new level..."));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 48, "Save level.."));
		this.controlList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 72, "Load level.."));
		this.controlList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 120, "Back to game"));
		if(this.mc.session == null) {
			((GuiButton)this.controlList.get(2)).enabled = false;
			((GuiButton)this.controlList.get(3)).enabled = false;
		}

	}

	protected final void actionPerformed(GuiButton var1) {
		if(var1.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.options));
		}

		if(var1.id == 1) {
			this.mc.displayGuiScreen(new GuiNewLevel(this));
		}

		if(this.mc.session != null) {
			if(var1.id == 2) {
				this.mc.displayGuiScreen(new GuiSaveLevel(this));
			}

			if(var1.id == 3) {
				this.mc.displayGuiScreen(new GuiLoadLevel(this));
			}
		}

		if(var1.id == 4) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
			this.mc.mouseHelper.grabMouse();
			GL11.mouseSetGrabbed(true);
		}

	}

	public final void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		drawCenteredString(this.fontRenderer, "Game menu", this.width / 2, 40, 16777215);
		super.drawScreen(var1, var2, var3);
	}
}
