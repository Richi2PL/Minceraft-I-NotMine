package net.minecraft.client;

import java.nio.IntBuffer;

import net.PeytonPlayz585.storage.LevelStorageManager;
import net.minecraft.client.controller.PlayerController;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.controller.PlayerControllerSP;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.container.GuiContainer;
import net.minecraft.client.gui.container.GuiInventory;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.player.MovementInputFromOptions;
import net.minecraft.client.render.EntityRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.texture.TextureLavaFX;
import net.minecraft.client.render.texture.TextureWaterFX;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.LevelLoader;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.generator.LevelGenerator;
import net.minecraft.game.physics.MovingObjectPosition;
import util.IProgressUpdate;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public final class Minecraft implements Runnable {
	public PlayerController playerController = new PlayerControllerSP(this);
	private boolean fullscreen = false;
	public int displayWidth;
	public int displayHeight;
	public Timer timer = new Timer(20.0F);
	public World theWorld;
	public RenderGlobal renderGlobal;
	public EntityPlayerSP thePlayer;
	public EffectRenderer effectRenderer;
	public Session session = null;
	public String minecraftUri;
	public volatile boolean isGamePaused = false;
	public RenderEngine renderEngine;
	public FontRenderer fontRenderer;
	public GuiScreen currentScreen = null;
	public LoadingScreenRenderer loadingScreen = new LoadingScreenRenderer(this);
	public EntityRenderer entityRenderer = new EntityRenderer(this);
	public int ticksRan = 0;
	private int leftClickCounter = 0;
	private int tempDisplayWidth;
	private int tempDisplayHeight;
	public String loadMapUser = null;
	public int loadMapID = 0;
	public GuiIngame ingameGUI;
	public boolean skipRenderWorld = false;
	public MovingObjectPosition objectMouseOver;
	public GameSettings options;
	public MouseHelper mouseHelper;
	private String server;
	private TextureWaterFX textureWaterFX;
	private TextureLavaFX textureLavaFX;
	public boolean running;
	public String debug;
	public boolean inventoryScreen;
	private int prevFrameTime;
	public boolean inGameHasFocus;
	
	public static Minecraft mc;

	public Minecraft(int var3, int var4) {
		new ModelBiped(0.0F);
		this.objectMouseOver = null;
		this.server = null;
		this.textureWaterFX = new TextureWaterFX();
		this.textureLavaFX = new TextureLavaFX();
		this.running = false;
		this.debug = "";
		this.inventoryScreen = false;
		this.prevFrameTime = 0;
		this.inGameHasFocus = false;
		this.tempDisplayWidth = var3;
		this.tempDisplayHeight = var4;
		this.fullscreen = false;
		new ThreadSleepForever(this, "Timer hack thread");
		this.displayWidth = var3;
		this.displayHeight = var4;
		this.fullscreen = false;
		mc = this;
	}

	public final void setServer(String var1, int var2) {
		this.server = var1;
	}

	public final void displayGuiScreen(GuiScreen var1) {
		if(!(this.currentScreen instanceof GuiErrorScreen)) {
			if(this.currentScreen != null) {
				this.currentScreen.onGuiClosed();
			}

			if(var1 == null && this.theWorld == null) {
				var1 = new GuiMainMenu();
			} else if(var1 == null && this.thePlayer.health <= 0) {
				var1 = new GuiGameOver();
			}

			this.currentScreen = (GuiScreen)var1;
			if(var1 != null) {
				this.inputLock();
				ScaledResolution var2 = new ScaledResolution(this.displayWidth, this.displayHeight);
				int var3 = var2.getScaledWidth();
				int var4 = var2.getScaledHeight();
				((GuiScreen)var1).setWorldAndResolution(this, var3, var4);
				this.skipRenderWorld = false;
			} else {
				this.setIngameFocus();
			}
		}
	}

	public final void shutdownMinecraftApplet() {
		GL11.destroyContext();
	}

	public final void run() {
		this.running = true;
		this.displayWidth = GL11.getCanvasWidth();
		this.displayHeight = GL11.getCanvasHeight();
		IntBuffer var24;
		this.mouseHelper = new MouseHelper();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearDepth((float)1.0D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		this.options = new GameSettings();
		this.renderEngine = new RenderEngine(this.options);
		this.fontRenderer = new FontRenderer(this.options, "/default.png", this.renderEngine);
		var24 = BufferUtils.createIntBuffer(256);
		var24.clear().limit(256);
		this.renderGlobal = new RenderGlobal(this, this.renderEngine);
		GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
		if(this.server != null && this.session != null) {
			World var31 = new World();
			var31.generate(8, 8, 8, new byte[512], new byte[512]);
			this.setLevel(var31);
		} else if(this.theWorld == null) {
			this.displayGuiScreen(new GuiMainMenu());
		}

		this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);
		this.ingameGUI = new GuiIngame(this);

		long var23 = System.currentTimeMillis();
		int var28 = 0;
		
		while(this.running) {
			if(this.theWorld != null) {
				this.theWorld.updateLighting();
			}

			if(this.isGamePaused) {
				float var29 = this.timer.renderPartialTicks;
				this.timer.updateTimer();
				this.timer.renderPartialTicks = var29;
			} else {
				this.timer.updateTimer();
			}

			for(int var30 = 0; var30 < this.timer.elapsedTicks; ++var30) {
				++this.ticksRan;
				this.runTick();
			}

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			this.playerController.setPartialTime(this.timer.renderPartialTicks);
			this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);

			if(GL11.getCanvasWidth() != this.displayWidth || GL11.getCanvasHeight() != this.displayHeight) {
				this.displayWidth = GL11.getCanvasWidth();
				this.displayHeight = GL11.getCanvasHeight();
				this.resize(this.displayWidth, this.displayHeight);
			}

			if(this.options.limitFramerate) {
				try {
					Thread.sleep(5L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			++var28;
			this.isGamePaused = this.currentScreen != null && this.currentScreen.doesGuiPauseGame();

			while(System.currentTimeMillis() >= var23 + 1000L) {
				this.debug = "FPS: " + var28 + ", Chunk Updates: " + WorldRenderer.chunksUpdated;
				WorldRenderer.chunksUpdated = 0;
				var23 += 1000L;
				var28 = 0;
			}
		}
	}

	public final void setIngameFocus() {
		if(GL11.isFocused()) {
			if(!this.inventoryScreen) {
				this.inventoryScreen = true;
				this.mouseHelper.grabMouse();
				this.displayGuiScreen((GuiScreen)null);
				this.prevFrameTime = this.ticksRan + 10000;
			}
		}
	}

	private void inputLock() {
		if(this.inventoryScreen) {
			if(this.thePlayer != null) {
				EntityPlayerSP var1 = this.thePlayer;
				var1.movementInput.resetKeyState();
			}

			this.inventoryScreen = false;
		}
	}

	public final void displayInGameMenu() {
		if(this.currentScreen == null) {
			this.displayGuiScreen(new GuiIngameMenu());
		}
	}

	private void clickMouse(int var1) {
		if(var1 != 0 || this.leftClickCounter <= 0) {
			if(var1 == 0) {
				this.entityRenderer.itemRenderer.equippedItemRender();
			}

			ItemStack var2;
			int var3;
			World var5;
			if(var1 == 1) {
				var2 = this.thePlayer.inventory.getCurrentItem();
				if(var2 != null) {
					var3 = var2.stackSize;
					EntityPlayerSP var7 = this.thePlayer;
					var5 = this.theWorld;
					ItemStack var4 = var2.getItem().onItemRightClick(var2, var5, var7);
					if(var4 != var2 || var4 != null && var4.stackSize != var3) {
						this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = var4;
						this.entityRenderer.itemRenderer.resetEquippedProgress();
						if(var4.stackSize == 0) {
							this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
						}
					}
				}
			}

			if(this.objectMouseOver == null) {
				if(var1 == 0 && !(this.playerController instanceof PlayerControllerCreative)) {
					this.leftClickCounter = 10;
				}

			} else {
				ItemStack var9;
				if(this.objectMouseOver.typeOfHit == 1) {
					if(var1 == 0) {
						Entity var14 = this.objectMouseOver.entityHit;
						EntityPlayerSP var12 = this.thePlayer;
						InventoryPlayer var11 = var12.inventory;
						var9 = var11.getStackInSlot(var11.currentItem);
						int var17 = var9 != null ? Item.itemsList[var9.itemID].getDamageVsEntity() : 1;
						if(var17 > 0) {
							var14.attackEntityFrom(var12, var17);
							var2 = var12.inventory.getCurrentItem();
							if(var2 != null && var14 instanceof EntityLiving) {
								EntityLiving var8 = (EntityLiving)var14;
								Item.itemsList[var2.itemID].hitEntity(var2);
								if(var2.stackSize <= 0) {
									var12.destroyCurrentEquippedItem();
								}
							}
						}

						return;
					}
				} else if(this.objectMouseOver.typeOfHit == 0) {
					int var10 = this.objectMouseOver.blockX;
					var3 = this.objectMouseOver.blockY;
					int var13 = this.objectMouseOver.blockZ;
					int var15 = this.objectMouseOver.sideHit;
					Block var6 = Block.blocksList[this.theWorld.getBlockId(var10, var3, var13)];
					if(var1 == 0) {
						this.theWorld.extinguishFire(var10, var3, var13, this.objectMouseOver.sideHit);
						if(var6 != Block.bedrock) {
							this.playerController.clickBlock(var10, var3, var13);
							return;
						}
					} else {
						var9 = this.thePlayer.inventory.getCurrentItem();
						int var16 = this.theWorld.getBlockId(var10, var3, var13);
						if(var16 > 0 && Block.blocksList[var16].blockActivated(this.theWorld, var10, var3, var13, this.thePlayer)) {
							return;
						}

						if(var9 == null) {
							return;
						}

						var16 = var9.stackSize;
						int var18 = var15;
						var5 = this.theWorld;
						if(var9.getItem().onItemUse(var9, var5, var10, var3, var13, var18)) {
							this.entityRenderer.itemRenderer.equippedItemRender();
						}

						if(var9.stackSize == 0) {
							this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
							return;
						}

						if(var9.stackSize != var16) {
							this.entityRenderer.itemRenderer.equipAnimationSpeed();
						}
					}
				}

			}
		}
	}

	private void resize(int var1, int var2) {
		this.displayWidth = var1;
		this.displayHeight = var2;
		if(this.currentScreen != null) {
			ScaledResolution var3 = new ScaledResolution(var1, var2);
			var2 = var3.getScaledWidth();
			var1 = var3.getScaledHeight();
			this.currentScreen.setWorldAndResolution(this, var2, var1);
		}

	}

	private void runTick() {
		mc = this;

		this.levelSave();
		
		if(this.playerController instanceof PlayerControllerCreative) {
			for(int var1 = 0; var1 < 9; var1++) {
				this.thePlayer.inventory.mainInventory[var1].stackSize = 64;
			}
		}
		
		if(!this.inventoryScreen) {
			this.mouseHelper.ungrabMouse();
			GL11.mouseSetGrabbed(false);
		}
		
		this.ingameGUI.addChatMessage();
		if(!this.isGamePaused && this.theWorld != null) {
			this.playerController.onUpdate();
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));

		if(this.currentScreen == null && this.thePlayer != null && this.thePlayer.health <= 0) {
			this.displayGuiScreen((GuiScreen)null);
		}

		if(this.currentScreen == null || this.currentScreen.allowUserInput) {
			label286:
			while(true) {
				while(true) {
					int var1;
					int var2;
					while(GL11.mouseNext()) {
						var1 = GL11.mouseGetEventDWheel();
						if(var1 != 0) {
							var2 = var1;
							InventoryPlayer var5 = this.thePlayer.inventory;
							if(var1 > 0) {
								var2 = 1;
							}

							if(var2 < 0) {
								var2 = -1;
							}

							for(var5.currentItem -= var2; var5.currentItem < 0; var5.currentItem += 9) {
							}

							while(var5.currentItem >= 9) {
								var5.currentItem -= 9;
							}
						}

						if(this.currentScreen == null) {
							if(!this.inventoryScreen && GL11.mouseGetEventButtonState()) {
								this.setIngameFocus();
							} else {
								if(GL11.mouseGetEventButton() == 0 && GL11.mouseGetEventButtonState()) {
									this.clickMouse(0);
									this.prevFrameTime = this.ticksRan;
								}

								if(GL11.mouseGetEventButton() == 1 && GL11.mouseGetEventButtonState()) {
									this.clickMouse(1);
									this.prevFrameTime = this.ticksRan;
								}

								if(GL11.mouseGetEventButton() == 2 && GL11.mouseGetEventButtonState() && this.objectMouseOver != null) {
									var2 = this.theWorld.getBlockId(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
									if(var2 == Block.grass.blockID) {
										var2 = Block.dirt.blockID;
									}

									if(var2 == Block.stairDouble.blockID) {
										var2 = Block.stairSingle.blockID;
									}

									if(var2 == Block.bedrock.blockID) {
										var2 = Block.stone.blockID;
									}

									this.thePlayer.inventory.getFirstEmptyStack(var2);
								}
							}
						} else if(this.currentScreen != null) {
							this.currentScreen.handleMouseInput();
						}
					}

					if(this.leftClickCounter > 0) {
						--this.leftClickCounter;
					}

					while(true) {
						while(true) {
							do {
								boolean var3;
								if(!GL11.keysNext()) {
									if(this.currentScreen == null) {
										if(GL11.mouseIsButtonDown(0) && (float)(this.ticksRan - this.prevFrameTime) >= this.timer.ticksPerSecond / 4.0F && this.inventoryScreen) {
											this.clickMouse(0);
											this.prevFrameTime = this.ticksRan;
										}

										if(GL11.mouseIsButtonDown(1) && (float)(this.ticksRan - this.prevFrameTime) >= this.timer.ticksPerSecond / 4.0F && this.inventoryScreen) {
											this.clickMouse(1);
											this.prevFrameTime = this.ticksRan;
										}
									}

									var3 = this.currentScreen == null && GL11.mouseIsButtonDown(0) && this.inventoryScreen;
									boolean var9 = false;
									if(!this.playerController.isInTestMode && this.leftClickCounter <= 0) {
										if(var3 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == 0) {
											var2 = this.objectMouseOver.blockX;
											int var8 = this.objectMouseOver.blockY;
											int var4 = this.objectMouseOver.blockZ;
											this.playerController.sendBlockRemoving(var2, var8, var4, this.objectMouseOver.sideHit);
											this.effectRenderer.addBlockHitEffects(var2, var8, var4, this.objectMouseOver.sideHit);
										} else {
											this.playerController.resetBlockRemoving();
										}
									}
									break label286;
								}

								EntityPlayerSP var10000 = this.thePlayer;
								int var10001 = GL11.getEventKey();
								var3 = GL11.getEventKeyState();
								var2 = var10001;
								EntityPlayerSP var6 = var10000;
								var6.movementInput.checkKeyForMovementInput(var2, var3);
							} while(!GL11.getEventKeyState());
								if(this.currentScreen != null) {
									this.currentScreen.handleKeyboardInput();
								} else {
									if(GL11.getEventKey() == 1) {
										this.displayInGameMenu();
									}

									if(this.playerController instanceof PlayerControllerCreative) {
										if(this.inventoryScreen) {
											if(GL11.getEventKey() == this.options.keyBindLoad.keyCode) {
												this.thePlayer.preparePlayerToSpawn();
											}

											if(GL11.getEventKey() == this.options.keyBindSave.keyCode) {
												this.theWorld.setSpawnLocation((int)this.thePlayer.posX, (int)this.thePlayer.posY, (int)this.thePlayer.posZ, this.thePlayer.rotationYaw);
												this.thePlayer.preparePlayerToSpawn();
											}
										}
									}

									if(GL11.getEventKey() == 33 && GL11.isKeyDown(6)) {
										this.options.thirdPersonView = !this.options.thirdPersonView;
									}

									if(GL11.getEventKey() == this.options.keyBindInventory.keyCode && !(this.playerController instanceof PlayerControllerCreative)) {
										this.displayGuiScreen(new GuiInventory(this.thePlayer.inventory));
									}

									if(GL11.getEventKey() == this.options.keyBindDrop.keyCode && this.inventoryScreen) {
										this.thePlayer.dropPlayerItemWithRandomChoice(this.thePlayer.inventory.decrStackSize(this.thePlayer.inventory.currentItem, 1), false);
									}
								}

								for(var1 = 0; var1 < 9; ++var1) {
									if(GL11.getEventKey() == var1 + 2) {
										this.thePlayer.inventory.currentItem = var1;
									}
								}

								if(GL11.getEventKey() == this.options.keyBindToggleFog.keyCode && this.inventoryScreen) {
									this.options.setOptionValue(4, !GL11.isKeyDown(42) && !GL11.isKeyDown(54) ? 1 : -1);
								}
						}
					}
				}
			}
		}

		if(this.currentScreen != null) {
			this.prevFrameTime = this.ticksRan + 10000;
		}

		if(this.currentScreen != null) {
			GuiScreen var7 = this.currentScreen;

			while(GL11.mouseNext()) {
				var7.handleMouseInput();
			}

			while(GL11.keysNext()) {
				var7.handleKeyboardInput();
			}

			if(this.currentScreen != null) {
				this.currentScreen.updateScreen();
			}
		}

		if(this.theWorld != null) {
			this.theWorld.difficultySetting = this.options.difficulty;
			if(!this.isGamePaused) {
				this.entityRenderer.updateRenderer();
			}

			if(!this.isGamePaused) {
				this.renderGlobal.updateClouds();
			}

			if(!this.isGamePaused) {
				this.theWorld.updateEntities();
			}

			if(!this.isGamePaused) {
				this.theWorld.tick();
			}

			if(!this.isGamePaused) {
				this.theWorld.randomDisplayUpdates((int)this.thePlayer.posX, (int)this.thePlayer.posY, (int)this.thePlayer.posZ);
			}

			if(!this.isGamePaused) {
				this.effectRenderer.updateEffects();
			}
		}

	}
	
	public int ticksUntilSave = 6000;
	public int ticksUntilSave2 = 100;
	public static int inventoryTicks = 0;

	private void levelSave() {
		if(this.theWorld == null) {
			ticksUntilSave = this.ticksRan + 6000;
			this.ticksUntilSave2 = this.ticksRan + 100;
		}
		
		if(this.ticksRan >= this.ticksUntilSave2 && this.theWorld != null && this.currentScreen instanceof GuiContainer) {
			this.ticksUntilSave2 = this.ticksRan + 100;
			LevelLoader loader = new LevelLoader();
			loader.save();
		}
		
		if(this.ticksRan >= this.ticksUntilSave) {
			LevelLoader loader = new LevelLoader();
			loader.save();
			ticksUntilSave = this.ticksRan + 6000;
		}
	}

	public final void generateLevel(int var1, int var2, int var3, int var4) {
		this.setLevel((World)null);
		System.gc();
		String var5 = this.session != null ? this.session.username : "anonymous";
		LevelGenerator var6 = new LevelGenerator(this.loadingScreen);
		var6.islandGen = var3 == 1;
		var6.floatingGen = var3 == 2;
		var6.flatGen = var3 == 3;
		var6.levelType = var4;
		var1 = 128 << var1;
		var3 = var1;
		short var8 = 64;
		if(var2 == 1) {
			var1 /= 2;
			var3 <<= 1;
		} else if(var2 == 2) {
			var1 /= 2;
			var3 = var1;
			var8 = 256;
		}

		World var7 = var6.generate(var5, var1, var3, var8);
		this.setLevel(var7);
	}

	public final void setLevel(World var1) {
		
		this.theWorld = var1;
		
		if(this.theWorld != null) {
			this.theWorld.setLevel();
		}
		
		if(var1 != null) {
			var1.load();
			//PlayerControllerCreative creative = new PlayerControllerCreative(this);
			//creative.onWorldChange(var1);
			this.playerController = new PlayerControllerSP(this);
			this.playerController.onWorldChange(var1);
			this.thePlayer = (EntityPlayerSP)var1.findSubclassOf(EntityPlayerSP.class);
			var1.playerEntity = this.thePlayer;
			if(this.thePlayer == null) {
				this.thePlayer = new EntityPlayerSP(this, var1, this.session);
				this.thePlayer.preparePlayerToSpawn();
				if(var1 != null) {
					var1.spawnEntityInWorld(this.thePlayer);
					var1.playerEntity = this.thePlayer;
				}
			}

			if(this.thePlayer != null) {
				this.thePlayer.movementInput = new MovementInputFromOptions(this.options);
				this.playerController.onRespawn(this.thePlayer);
			}

			if(this.renderGlobal != null) {
				this.renderGlobal.changeWorld(var1);
			}

			if(this.effectRenderer != null) {
				this.effectRenderer.clearEffects(var1);
			}

			this.textureWaterFX.textureId = 0;
			this.textureLavaFX.textureId = 0;
			int var4 = this.renderEngine.getTexture("/water.png");
			if(var1.defaultFluid == Block.waterMoving.blockID) {
				this.textureWaterFX.textureId = var4;
			} else {
				this.textureLavaFX.textureId = var4;
			}
		}
		
		if(this.thePlayer != null && LevelStorageManager.levelStorage != null) {
			this.thePlayer.readEntityFromNBT(LevelStorageManager.levelStorage);
		}

		System.gc();
	}
	
	public static Minecraft getMinecraft() {
		return mc;
	}
}
