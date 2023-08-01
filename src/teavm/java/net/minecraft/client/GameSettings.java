package net.minecraft.client;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import net.PeytonPlayz585.nbt.NBTTagCompound;
import net.PeytonPlayz585.storage.LocalStorageManager;

public final class GameSettings {
	private static final String[] RENDER_DISTANCES = new String[]{"FAR", "NORMAL", "SHORT", "TINY"};
	private static final String[] DIFFICULTIES = new String[]{"Peaceful", "Easy", "Normal", "Hard"};
	public boolean music = true;
	public boolean sound = true;
	public boolean invertMouse = false;
	public boolean showFPS = false;
	public int renderDistance = 0;
	public boolean fancyGraphics = true;
	public boolean anaglyph = false;
	public boolean limitFramerate = false;
	public KeyBinding keyBindForward = new KeyBinding("Forward", 17);
	public KeyBinding keyBindLeft = new KeyBinding("Left", 30);
	public KeyBinding keyBindBack = new KeyBinding("Back", 31);
	public KeyBinding keyBindRight = new KeyBinding("Right", 32);
	public KeyBinding keyBindJump = new KeyBinding("Jump", 57);
	public KeyBinding keyBindInventory = new KeyBinding("Inventory", 23);
	public KeyBinding keyBindDrop = new KeyBinding("Drop", 16);
	private KeyBinding keyBindChat = new KeyBinding("Chat", 20);
	public KeyBinding keyBindToggleFog = new KeyBinding("Toggle fog", 33);
	public KeyBinding keyBindSave = new KeyBinding("Save location", 28);
	public KeyBinding keyBindLoad = new KeyBinding("Load location", 19);
	public KeyBinding[] keyBindings = new KeyBinding[]{this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindToggleFog, this.keyBindSave, this.keyBindLoad};
	public int numberOfOptions = 9;
	public int difficulty = 2;
	public boolean thirdPersonView = false;

	public GameSettings() {
		this.loadOptions();
	}

	public final String setKeyBindingString(int var1) {
		return this.keyBindings[var1].keyDescription + ": " + GL11.getKeyName(this.keyBindings[var1].keyCode);
	}

	public final void setKeyBinding(int var1, int var2) {
		this.keyBindings[var1].keyCode = var2;
		this.saveOptions();
	}

	public final void setOptionValue(int var1, int var2) {
		if(var1 == 0) {
			this.music = !this.music;
		}

		if(var1 == 1) {
			this.sound = !this.sound;
		}

		if(var1 == 2) {
			this.invertMouse = !this.invertMouse;
		}

		if(var1 == 3) {
			this.showFPS = !this.showFPS;
		}

		if(var1 == 4) {
			this.renderDistance = this.renderDistance + var2 & 3;
		}

		if(var1 == 5) {
			this.fancyGraphics = !this.fancyGraphics;
		}

		if(var1 == 6) {
			this.anaglyph = !this.anaglyph;
		}

		if(var1 == 7) {
			this.limitFramerate = !this.limitFramerate;
		}

		if(var1 == 8) {
			this.difficulty = this.difficulty + var2 & 3;
		}

		this.saveOptions();
	}

	public final String setOptionString(int var1) {
		return var1 == 0 ? "Music: " + (this.music ? "ON" : "OFF") : (var1 == 1 ? "Sound: " + (this.sound ? "ON" : "OFF") : (var1 == 2 ? "Invert mouse: " + (this.invertMouse ? "ON" : "OFF") : (var1 == 3 ? "Show FPS: " + (this.showFPS ? "ON" : "OFF") : (var1 == 4 ? "Render distance: " + RENDER_DISTANCES[this.renderDistance] : (var1 == 5 ? "View bobbing: " + (this.fancyGraphics ? "ON" : "OFF") : (var1 == 6 ? "3d anaglyph: " + (this.anaglyph ? "ON" : "OFF") : (var1 == 7 ? "Limit framerate: " + (this.limitFramerate ? "ON" : "OFF") : (var1 == 8 ? "Difficulty: " + DIFFICULTIES[this.difficulty] : ""))))))));
	}

	private void loadOptions() {
		NBTTagCompound settingsFile = LocalStorageManager.gameSettingsStorage;
		
		if(settingsFile.tagMap.size() == 0) {
			return;
		} else {
			if(settingsFile.hasKey("music")) {
				this.music = settingsFile.getBoolean("music");
			}
			
			if(settingsFile.hasKey("sound")) {
				this.sound = settingsFile.getBoolean("sound");
			}
			
			if(settingsFile.hasKey("invertYMouse")) {
				this.invertMouse = settingsFile.getBoolean("invertYMouse");
			}
			
			if(settingsFile.hasKey("showFrameRate")) {
				this.showFPS = settingsFile.getBoolean("showFrameRate");
			}
			
			if(settingsFile.hasKey("viewDistance")) {
				this.renderDistance = settingsFile.getInteger("viewDistance");
			}
			
			if(settingsFile.hasKey("bobView")) {
				this.fancyGraphics = settingsFile.getBoolean("bobView");
			}
			
			if(settingsFile.hasKey("anaglyph3d")) {
				this.anaglyph = settingsFile.getBoolean("anaglyph3d");
			}
			
			if(settingsFile.hasKey("limitFramerate")) {
				this.limitFramerate = settingsFile.getBoolean("music");
			}
			
			if(settingsFile.hasKey("difficulty")) {
				this.difficulty = settingsFile.getInteger("difficulty");
			}
			
			for(int i = 0; i < keyBindings.length; ++i) {
				String k = "key_" + keyBindings[i].keyDescription;
				if(settingsFile.hasKey(k)) keyBindings[i].keyCode = (int)settingsFile.getShort(k) & 0xFFFF;
			}
		}
	}

	public final void saveOptions() {
		NBTTagCompound settingsFile = LocalStorageManager.gameSettingsStorage;
		settingsFile.setBoolean("music", this.music);
		settingsFile.setBoolean("sound", this.sound);
		settingsFile.setBoolean("invertYMouse", this.invertMouse);
		settingsFile.setBoolean("showFrameRate", this.showFPS);
		settingsFile.setInteger("viewDistance", this.renderDistance);
		settingsFile.setBoolean("bobView", this.fancyGraphics);
		settingsFile.setBoolean("anaglyph3d", this.anaglyph);
		settingsFile.setBoolean("limitFramerate", this.limitFramerate);
		settingsFile.setInteger("difficulty", this.difficulty);
		for(int i = 0; i < keyBindings.length; ++i) {
			String k = "key_" + keyBindings[i].keyDescription;
			settingsFile.setShort(k, (short)keyBindings[i].keyCode);
		}
		try {
			LocalStorageManager.saveStorageG();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
