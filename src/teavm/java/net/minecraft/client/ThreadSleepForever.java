package net.minecraft.client;

final class ThreadSleepForever extends Thread {
	ThreadSleepForever(Minecraft var1, String var2) {
		super(var2);
		this.setDaemon(true);
		this.start();
	}

	public final void run() {
		while(true) {
			try {
				Thread.sleep(2147483647L);
			} catch (InterruptedException var1) {
			}
		}
	}
}
