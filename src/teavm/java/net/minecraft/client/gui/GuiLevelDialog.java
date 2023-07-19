//package net.minecraft.client.gui;
//
//import java.awt.FileDialog;
//import java.io.File;
//
//final class GuiLevelDialog extends Thread {
//	private GuiLoadLevel screen;
//
//	GuiLevelDialog(GuiLoadLevel var1) {
//		this.screen = var1;
//	}
//
//	public final void run() {
//		try {
//			FileDialog var1 = this.screen.saveFileDialog();
//			File var2 = new File(this.screen.mc.mcDataDir, "saves");
//			var2.mkdir();
//			String var5 = var2.toString();
//			if(!var5.endsWith(File.separator)) {
//				var5 = var5 + File.separator;
//			}
//
//			var1.setDirectory(var5);
//			FilenameFilterLevel var6 = new FilenameFilterLevel(this);
//			var1.setFilenameFilter(var6);
//			var1.setLocationRelativeTo(this.screen.mc.mcCanvas);
//			var1.setVisible(true);
//			if(var1.getFile() != null) {
//				var5 = var1.getDirectory();
//				if(!var5.endsWith(File.separator)) {
//					var5 = var5 + File.separator;
//				}
//
//				GuiLoadLevel.a(this.screen, new File(var5 + var1.getFile()));
//			}
//		} finally {
//			GuiLoadLevel.unknown(this.screen, false);
//		}
//
//	}
//}
