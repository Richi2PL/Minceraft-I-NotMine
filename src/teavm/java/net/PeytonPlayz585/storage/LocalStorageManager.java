package net.PeytonPlayz585.storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.WebGL;

import net.PeytonPlayz585.minecraft.Base64;
import net.PeytonPlayz585.nbt.NBTBase;
import net.PeytonPlayz585.nbt.NBTTagCompound;

public class LocalStorageManager {

	public static NBTTagCompound gameSettingsStorage = null;
	public static NBTTagCompound profileSettingsStorage = null;
	
	public static void loadStorage() {
		byte[] g = WebGL.loadLocalStorage("g");
		byte[] p = WebGL.loadLocalStorage("p");
		
		if(g != null) {
			try {
				NBTBase t = NBTBase.readTag(new DataInputStream(new ByteArrayInputStream(g)));
				if(t != null && t instanceof NBTTagCompound) {
					gameSettingsStorage = (NBTTagCompound)t;
				}
			}catch(IOException e) {
				;
			}
		}
		
		if(p != null) {
			try {
				NBTBase t = NBTBase.readTag(new DataInputStream(new ByteArrayInputStream(p)));
				if(t != null && t instanceof NBTTagCompound) {
					profileSettingsStorage = (NBTTagCompound)t;
				}
			}catch(IOException e) {
				;
			}
		}

		if(gameSettingsStorage == null) gameSettingsStorage = new NBTTagCompound();
		if(profileSettingsStorage == null) profileSettingsStorage = new NBTTagCompound();
		
	}
	
	public static void saveStorageG() throws IOException {
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		NBTBase.writeTag(gameSettingsStorage, new DataOutputStream(s));
		WebGL.saveLocalStorage("g", s.toByteArray());
	}
	
	public static void saveStorageP() throws IOException {
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		NBTBase.writeTag(profileSettingsStorage, new DataOutputStream(s));
		WebGL.saveLocalStorage("p", s.toByteArray());
	}
	
	public static String dumpConfiguration() {
		try {
			ByteArrayOutputStream s = new ByteArrayOutputStream();
			NBTBase.writeTag(gameSettingsStorage, new DataOutputStream(s));
			return Base64.encodeBase64String(s.toByteArray());
		} catch(Throwable e) {
			return "<error>";
		}
	}

}