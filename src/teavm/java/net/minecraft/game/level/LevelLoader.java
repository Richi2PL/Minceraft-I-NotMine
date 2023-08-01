package net.minecraft.game.level;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import net.PeytonPlayz585.nbt.NBTTagCompound;
import net.PeytonPlayz585.nbt.NBTTagList;
import net.PeytonPlayz585.nbt.NBTTagShort;
import net.PeytonPlayz585.storage.LevelStorageManager;
import net.minecraft.client.Minecraft;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityPainting;
import net.minecraft.game.entity.animal.EntityPig;
import net.minecraft.game.entity.animal.EntitySheep;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.monster.EntityCreeper;
import net.minecraft.game.entity.monster.EntityGiantZombie;
import net.minecraft.game.entity.monster.EntitySkeleton;
import net.minecraft.game.entity.monster.EntitySpider;
import net.minecraft.game.entity.monster.EntityZombie;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.tileentity.TileEntity;
import net.minecraft.game.level.block.tileentity.TileEntityChest;
import net.minecraft.game.level.block.tileentity.TileEntityFurnace;

public class LevelLoader {

	public final World load() {
		String[] randomText = new String[]{"Hi from PeytonPlayz585", "You Eagler!", "Setting up World", ":)", "Isn't Indev the best version?", "I hate Microsoft!", "Notch is the best!", "PeytonPlayz585!", "Random text lol...", "Spam ping Winix!", "Ghost ping Winix!", "DM Winix for no reason!", "Spam ping Winix lol!", "Ghost ping Winix!", "DM Winix for no reason!", "PeytonPlayz585's Birthday is 10/11", "Yee!", "WebGL 2.0!", "ShadowCraft!"};
		Minecraft.getMinecraft().loadingScreen.displayProgressMessage("Loading level");
		Minecraft.getMinecraft().loadingScreen.displayLoadingString(randomText[(int)(Math.random() * (double)randomText.length)]);
		Minecraft.getMinecraft().loadingScreen.setLoadingProgress(25);
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Minecraft.getMinecraft().loadingScreen.displayLoadingString("Reading..");
		Minecraft.getMinecraft().loadingScreen.setLoadingProgress(50);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		NBTTagCompound var13 = LevelStorageManager.levelStorage;
		NBTTagCompound var2 = var13.getCompoundTag("About");
		NBTTagCompound var3 = var13.getCompoundTag("Map");
		NBTTagCompound var4 = var13.getCompoundTag("Environment");
		NBTTagList var5 = var13.getTagList("Entities");
		short var6 = var3.getShort("Width");
		short var7 = var3.getShort("Length");
		short var8 = var3.getShort("Height");
		World var9 = new World();
		Minecraft.getMinecraft().loadingScreen.displayLoadingString("Preparing level..");
		Minecraft.getMinecraft().loadingScreen.setLoadingProgress(75);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		NBTTagList var10 = var3.getTagList("Spawn");
		var9.xSpawn = ((NBTTagShort)var10.tagAt(0)).shortValue;
		var9.ySpawn = ((NBTTagShort)var10.tagAt(1)).shortValue;
		var9.zSpawn = ((NBTTagShort)var10.tagAt(2)).shortValue;
		var9.authorName = var2.getString("Author");
		var9.name = var2.getString("Name");
		var9.createTime = var2.getLong("CreatedOn");
		var9.cloudColor = var4.getInteger("CloudColor");
		var9.skyColor = var4.getInteger("SkyColor");
		var9.fogColor = var4.getInteger("FogColor");
		var9.skyBrightness = var4.getByte("SkyBrightness");
		if(var9.skyBrightness < 0) {
			var9.skyBrightness = 0;
		}

		if(var9.skyBrightness > 16) {
			var9.skyBrightness = var9.skyBrightness * 15 / 100;
		}

		var9.cloudHeight = var4.getShort("CloudHeight");
		var9.groundLevel = var4.getShort("SurroundingGroundHeight");
		var9.waterLevel = var4.getShort("SurroundingWaterHeight");
		var9.defaultFluid = var4.getByte("SurroundingWaterType");
		var9.worldTime = var4.getShort("TimeOfDay");
		var9.skylightSubtracted = var9.getSkyBrightness();
		var9.generate(var6, var8, var7, var3.getByteArray("Blocks"), var3.getByteArray("Data"));
		Minecraft.getMinecraft().loadingScreen.displayLoadingString("Preparing entities..");
		Minecraft.getMinecraft().loadingScreen.setLoadingProgress(100);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for(int var16 = 0; var16 < var5.tagCount(); ++var16) {
			try {
				var3 = (NBTTagCompound)var5.tagAt(var16);
				String var19 = var3.getString("id");
				Entity var21 = this.loadEntity(var9, var19);
				if(var21 != null) {
					var21.readFromNBT(var3);
					var9.spawnEntityInWorld(var21);
				} else {
					System.out.println("Skipping unknown entity id \"" + var19 + "\"");
				}
			} catch (Exception var12) {
				System.out.println("Error reading entity");
				var12.printStackTrace();
			}
		}

		NBTTagList var17 = var13.getTagList("TileEntities");

		for(int var18 = 0; var18 < var17.tagCount(); ++var18) {
			try {
				var4 = (NBTTagCompound)var17.tagAt(var18);
				int var22 = var4.getInteger("Pos");
				String var14 = var4.getString("id");
				Object var20 = var14.equals("Chest") ? new TileEntityChest() : (var14.equals("Furnace") ? new TileEntityFurnace() : null);
				if(var20 != null) {
					int var15 = var22 % 1024;
					int var23 = (var22 >> 10) % 1024;
					var22 = (var22 >> 20) % 1024;
					((TileEntity)var20).readFromNBT(var4);
					var9.setBlockTileEntity(var15, var23, var22, (TileEntity)var20);
				} else {
					System.out.println("Skipping unknown tile entity id \"" + var14 + "\"");
				}
			} catch (Exception var11) {
				System.out.println("Error reading tileentity");
				var11.printStackTrace();
			}
		}

		return var9;
	}

	protected Entity loadEntity(World var1, String var2) {
		return (Entity)(var2.equals("Pig") ? new EntityPig(var1) : (var2.equals("Sheep") ? new EntitySheep(var1) : (var2.equals("Creeper") ? new EntityCreeper(var1) : (var2.equals("Skeleton") ? new EntitySkeleton(var1) : (var2.equals("Spider") ? new EntitySpider(var1) : (var2.equals("Zombie") ? new EntityZombie(var1) : (var2.equals("Giant") ? new EntityGiantZombie(var1) : (var2.equals("Item") ? new EntityItem(var1) : (var2.equals("Painting") ? new EntityPainting(var1) : null)))))))));
	}

	public final void save() {
//		if(this.guiLoading != null) {
//			this.guiLoading.displayProgressMessage("Saving level");
//		}

//		if(this.guiLoading != null) {
//			this.guiLoading.displayLoadingString("Preparing level..");
//		}
		
		World var1 = Minecraft.getMinecraft().theWorld;

		NBTTagCompound var3 = new NBTTagCompound();
		var3.setInteger("CloudColor", var1.cloudColor);
		var3.setInteger("SkyColor", var1.skyColor);
		var3.setInteger("FogColor", var1.fogColor);
		var3.setByte("SkyBrightness", (byte)var1.skyBrightness);
		var3.setShort("CloudHeight", (short)var1.cloudHeight);
		var3.setShort("SurroundingGroundHeight", (short)var1.groundLevel);
		var3.setShort("SurroundingWaterHeight", (short)var1.waterLevel);
		var3.setByte("SurroundingGroundType", (byte)Block.grass.blockID);
		var3.setByte("SurroundingWaterType", (byte)var1.defaultFluid);
		var3.setShort("TimeOfDay", (short)var1.worldTime);
		NBTTagCompound var4 = new NBTTagCompound();
		var4.setShort("Width", (short)var1.width);
		var4.setShort("Length", (short)var1.length);
		var4.setShort("Height", (short)var1.height);
		var4.setByteArray("Blocks", var1.blocks);
		var4.setByteArray("Data", var1.data);
		NBTTagList var5 = new NBTTagList();
		var5.setTag(new NBTTagShort((short)var1.xSpawn));
		var5.setTag(new NBTTagShort((short)var1.ySpawn));
		var5.setTag(new NBTTagShort((short)var1.zSpawn));
		var4.setTag("Spawn", var5);
		NBTTagCompound var15 = new NBTTagCompound();
		var15.setString("Author", var1.authorName);
		var15.setString("Name", var1.name);
		var15.setLong("CreatedOn", var1.createTime);
//		if(this.guiLoading != null) {
//			this.guiLoading.displayLoadingString("Preparing entities..");
//		}

		NBTTagList var6 = new NBTTagList();
		Iterator<?> var7 = var1.entityMap.entities.iterator();

		while(var7.hasNext()) {
			Entity var8 = (Entity)var7.next();
			NBTTagCompound var9 = new NBTTagCompound();
			var8.writeToNBT(var9);
			if(!var9.emptyNBTMap()) {
				var6.setTag(var9);
			}
		}

		NBTTagList var16 = new NBTTagList();
		Iterator<?> var17 = var1.map.keySet().iterator();

		while(var17.hasNext()) {
			int var19 = ((Integer)var17.next()).intValue();
			NBTTagCompound var10 = new NBTTagCompound();
			var10.setInteger("Pos", var19);
			TileEntity var20 = (TileEntity)var1.map.get(Integer.valueOf(var19));
			var20.writeToNBT(var10);
			var16.setTag(var10);
		}

		NBTTagCompound var18 = new NBTTagCompound();
		var18.setKey("MinecraftLevel");
		var18.setCompoundTag("About", var15);
		var18.setCompoundTag("Map", var4);
		var18.setCompoundTag("Environment", var3);
		var18.setTag("Entities", var6);
		var18.setTag("TileEntities", var16);
		
		var18.setFloat("player-x", Minecraft.getMinecraft().thePlayer.posX);
		var18.setFloat("player-y", Minecraft.getMinecraft().thePlayer.posY);
		var18.setFloat("player-z", Minecraft.getMinecraft().thePlayer.posZ);
		
		Minecraft.getMinecraft().thePlayer.writeEntityToNBT(var18);
		
//		if(this.guiLoading != null) {
//			this.guiLoading.displayLoadingString("Writing..");
//		}
		
		LevelStorageManager.levelStorage = var18;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					LevelStorageManager.saveLevelData();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).run(); 
	}
}
