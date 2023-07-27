package net.PeytonPlayz585;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.WebGL;
import org.teavm.jso.JSBody;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSError;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Session;
import net.minecraft.client.gui.GuiErrorScreen;
import net.PeytonPlayz585.storage.LocalStorageManager;

public class MinecraftMain {
	public static Thread thread = null;
	
	public static class AbortedLaunchException extends RuntimeException {
		// yee
	}
	
	public static HTMLElement rootElement = null;
    public static void main(String[] args) {
    	registerErrorHandler();
    	String[] e = getOpts();
    	try {
	    	try {
	    		WebGL.initializeContext(rootElement = Window.current().getDocument().getElementById(e[0]), e[1]);
	    	}catch(AbortedLaunchException ex) {
	    		return;
	    	}
    	}catch(Throwable ex2) {
    		StringWriter s = new StringWriter();
    		ex2.printStackTrace(new PrintWriter(s));
    		return;
    	}
    	run0();
    }

    private static void run0() {
    	System.out.println(" -------- starting minecraft -------- ");
    	LocalStorageManager.loadStorage();
    	run1();
    }
    
    private static void run1() {
    	Minecraft minecraft = new Minecraft(GL11.getCanvasWidth(), GL11.getCanvasHeight());
    	//minecraft.minecraftUri = "127.0.0.1:25565";
    	minecraft.session = new Session("PeytonPlayz595", "WebGL-Emulator");
    	Thread thread = new Thread (minecraft, "Minecraft main Thread");
    	thread.run();
    }

	@JSBody(params = { }, script = "return window.classicConfig;")
	public static native String[] getOpts();

	@JSBody(params = { }, script = "window.minecraftError = null; window.onerror = function(message, file, line, column, errorObj) { if(errorObj) { window.minecraftError = errorObj; window.minecraftErrorL = \"\"+line+\":\"+column; javaMethods.get(\"net.PeytonPlayz585.MinecraftMain.handleNativeError()V\").invoke(); } else { alert(\"a native browser exception was thrown but your browser does not support fith argument in onerror\"); } };")
	public static native void registerErrorHandler();

	@JSBody(params = { }, script = "return window.minecraftError;")
	public static native JSError getWindowError();
	
	@JSBody(params = { }, script = "return window.minecraftErrorL;")
	public static native String getWindowErrorL();
	
	public static void handleNativeError() {
		JSError e = getWindowError();
		StringBuilder str = new StringBuilder();
		str.append("Native Browser Exception\n");
		str.append("----------------------------------\n");
		str.append("  Line: ").append(getWindowErrorL()).append('\n');
		str.append("  Type: ").append(e.getName()).append('\n');
		str.append("  Message: ").append(e.getMessage()).append('\n');
		str.append("----------------------------------\n\n");
		str.append(e.getStack()).append('\n');
	}
	
	private static boolean isCrashed = false;

	public static void showDatabaseLockedScreen(String msg) {
		String s = rootElement.getAttribute("style");
		rootElement.setAttribute("style", (s == null ? "" : s) + "position:relative;");
		HTMLDocument doc = Window.current().getDocument();
		HTMLElement div = doc.createElement("div");
		div.setAttribute("style", "z-index:100;position:absolute;top:135px;left:10%;right:10%;bottom:30px;background-color:white;border:1px solid #cccccc;overflow-x:hidden;overflow-y:scroll;overflow-wrap:break-word;white-space:pre-wrap;font: 24px sans-serif;padding:10px;");
		rootElement.appendChild(div);
		div.appendChild(doc.createTextNode(msg));
	}
	
	@JSBody(params = { "v" }, script = "try { return \"\"+window[v]; } catch(e) { return \"<error>\"; }")
	private static native String getString(String var);
	
	@JSBody(params = { "v" }, script = "try { return \"\"+window.navigator[v]; } catch(e) { return \"<error>\"; }")
	private static native String getStringNav(String var);

	@JSBody(params = { "v" }, script = "try { return \"\"+window.screen[v]; } catch(e) { return \"<error>\"; }")
	private static native String getStringScreen(String var);

	@JSBody(params = { "v" }, script = "try { return \"\"+window.location[v]; } catch(e) { return \"<error>\"; }")
	private static native String getStringLocation(String var);
	
	@JSBody(params = { }, script = "for(var i = 0; i < window.minecraftOpts.length; ++i) { if(window.minecraftOpts[i].length > 2048) window.minecraftOpts[i] = \"[\" + Math.floor(window.minecraftOpts[i].length * 0.001) + \"k characters]\"; }")
	private static native void shortenMinecraftOpts();

	private static void addDebug(StringBuilder str, String var) {
		str.append("window.").append(var).append(" = ").append(getString(var)).append('\n');
	}
	
	private static void addDebugNav(StringBuilder str, String var) {
		str.append("window.navigator.").append(var).append(" = ").append(getStringNav(var)).append('\n');
	}
	
	private static void addDebugScreen(StringBuilder str, String var) {
		str.append("window.screen.").append(var).append(" = ").append(getStringScreen(var)).append('\n');
	}
	
	private static void addDebugLocation(StringBuilder str, String var) {
		str.append("window.location.").append(var).append(" = ").append(getStringLocation(var)).append('\n');
	}
	
	private static void addArray(StringBuilder str, String var) {
		str.append("window.").append(var).append(" = ").append(getArray(var)).append('\n');
	}
	
	@JSBody(params = { "v" }, script = "try { return (typeof window[v] !== \"undefined\") ? JSON.stringify(window[v]) : \"[\\\"<error>\\\"]\"; } catch(e) { return \"[\\\"<error>\\\"]\"; }")
	private static native String getArray(String var);
	
}
