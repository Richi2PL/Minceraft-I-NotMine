package net.minecraft.client.gui;

import java.io.File;
import java.io.FilenameFilter;

final class FilenameFilterLevel implements FilenameFilter {
	FilenameFilterLevel(GuiLevelDialog var1) {
	}

	public final boolean accept(File var1, String var2) {
		return var2.toLowerCase().endsWith(".mclevel");
	}
}
