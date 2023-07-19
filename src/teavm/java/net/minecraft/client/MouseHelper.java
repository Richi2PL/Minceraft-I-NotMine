package net.minecraft.client;

import org.lwjgl.opengl.GL11;

public class MouseHelper {

	public void grabMouse() {
		GL11.mouseSetGrabbed(true);
		deltaX = 0;
		deltaY = 0;
	}

	public void ungrabMouse() {
		GL11.mouseSetCursorPosition(GL11.getCanvasWidth() / 2, GL11.getCanvasHeight() / 2);
		GL11.mouseSetGrabbed(false);
	}

	public void mouseXYChange() {
		if(GL11.isPointerLocked2()) {
			deltaX = GL11.mouseGetDX();
			deltaY = GL11.mouseGetDY();
		}else {
			deltaX = 0;
			deltaY = 0;
		}
	}
	
	public int deltaX;
	public int deltaY;
}