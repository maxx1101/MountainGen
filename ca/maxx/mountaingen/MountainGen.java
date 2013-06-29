package ca.maxx.mountaingen;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import ca.maxx.mountaingen.common.Common;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = MountainGen.modid, name = "Mountain Generator", version = "1.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)

public class MountainGen {
	
	public static final String modid = "MountainGen";
	
	public static Block[] mountainBlocks;
	public static Block randomMountainBlock;
	public static Block shatteredMountainBlock;
	
	public static int mountainBlockID;
	public static int randomMountainBlockID;
	public static int shatteredMountainBlockID;
	
	public static String configPath;
	public static String configImagesPath;
	public static File configFile;
	
	public static String imgPath;

	public static MinecraftServer server;
		
	
	@PreInit
    public void preInit(FMLPreInitializationEvent event) {
		configPath = event.getModConfigurationDirectory().toString() + "/MountainGen/";
		configImagesPath = configPath + "Images/";
		
		// Check to see if the config folder exists
		if (new File(configPath).exists() == false)
		{
			Common.logger(Level.INFO,"[MountainGen] Creating config folder: " + configPath);
		    boolean configPathResult = new File(configPath).mkdir();   
		    if(configPathResult){    
		       Common.logger(Level.INFO,"[MountainGen] Created config folder.");
		     }
		}
		
		//Check to see if the config/Images folder exists
		if (new File(configImagesPath).exists() == false)
		{
			Common.logger(Level.INFO,"[MountainGen] Creating config/Images folder: " + configImagesPath);
		    boolean configImagesPathresult = new File(configImagesPath).mkdir(); 
			if (configImagesPathresult)
			{
				Common.logger(Level.INFO,"[MountainGen] Created config/Images folder.");
				
				// Download height map images since this is a fresh install of the mod
			    Common.logger(Level.INFO,"[MountainGen] Downloading mountain height maps...");
			    Common.downloadHeightMaps("https://raw.github.com/maxx1101/MountainGen/master/config/MountainGen/Images/1.png", "1.png");
			    Common.downloadHeightMaps("https://raw.github.com/maxx1101/MountainGen/master/config/MountainGen/Images/2.jpeg", "2.jpeg");
			    Common.downloadHeightMaps("https://raw.github.com/maxx1101/MountainGen/master/config/MountainGen/Images/3.png", "3.png");
			    Common.downloadHeightMaps("https://raw.github.com/maxx1101/MountainGen/master/config/MountainGen/Images/4.png", "4.png");
			    Common.logger(Level.INFO,"[MountainGen] Downloading mountain height maps complete.");
			}
		}
		
		configFile = new File(configPath, modid+".cfg");
		Configuration config = new Configuration(configFile);
        config.load();

        shatteredMountainBlockID = config.getBlock("ShatteredMountainGenBlockID", 1099).getInt();
        randomMountainBlockID = config.getBlock("RandomMountainGenBlockID", 1100).getInt();
        mountainBlockID = config.getBlock("MountainGenBlockStartingID", 1101).getInt();

        config.save();
    }
	
	@Init
	public void load(FMLInitializationEvent event)
	{
		//Register the Command Event Handler for possible future use
		MinecraftForge.EVENT_BUS.register(new CommandEventHandler());
		
		File folder = new File(configImagesPath);
		File[] listOfFiles = folder.listFiles();

		mountainBlocks = new Block[listOfFiles.length];
		
		for (int i = 0; i < listOfFiles.length; i++) 
		{
		 	if (listOfFiles[i].isFile()) 
		   	{
		   		if (listOfFiles[i].getName().contains(".png"))
		   		{
		   			if (isValidImageSize(configImagesPath + listOfFiles[i].getName()))
		   			{
		    			mountainBlocks[i] = new BlockMountain(mountainBlockID + i, listOfFiles[i].getName()).setUnlocalizedName("mountainGenBlock"+listOfFiles[i].getName());
			    		GameRegistry.registerBlock(mountainBlocks[i], "mountainGenBlock"+listOfFiles[i].getName());
			    		
			    		LanguageRegistry.addName(mountainBlocks[i], "Mountain Generator Block (" + listOfFiles[i].getName() + ")");
			    		Common.logger(Level.INFO, "File '" + listOfFiles[i].getName() + "' is a valid image, registered new mountain block!");
		   			}
		   		}
		   		else if (listOfFiles[i].getName().contains(".jpeg"))
		   		{
		   			if (isValidImageSize(configImagesPath + listOfFiles[i].getName()))
		   			{
		    			mountainBlocks[i] = new BlockMountain(mountainBlockID + i, listOfFiles[i].getName()).setUnlocalizedName("mountainGenBlock"+listOfFiles[i].getName());
			    		GameRegistry.registerBlock(mountainBlocks[i], "mountainGenBlock"+listOfFiles[i].getName());
			    		
			    		LanguageRegistry.addName(mountainBlocks[i], "Mountain Generator Block (" + listOfFiles[i].getName() + ")");
			    		Common.logger(Level.INFO, "File '" + listOfFiles[i].getName() + "' is a valid image, registered new mountain block!");
		   			}
		   		}
		   		else if (listOfFiles[i].getName().contains(".jpg"))
		   		{
		   			if (isValidImageSize(configImagesPath + listOfFiles[i].getName()))
		   			{
		    			mountainBlocks[i] = new BlockMountain(mountainBlockID + i, listOfFiles[i].getName()).setUnlocalizedName("mountainGenBlock"+listOfFiles[i].getName());
			    		GameRegistry.registerBlock(mountainBlocks[i], "mountainGenBlock"+listOfFiles[i].getName());
			    		
			    		LanguageRegistry.addName(mountainBlocks[i], "Mountain Generator Block (" + listOfFiles[i].getName() + ")");
			    		Common.logger(Level.INFO, "File '" + listOfFiles[i].getName() + "' is a valid image, registered new mountain block!");
		   			}
		   		}
		   		else
		   		{
		   			Common.logger(Level.WARNING, "File '" + listOfFiles[i].getName() + "' not recognized as a valid image file, skipping!");
		   		}
		 	}
		}
		   
		//Create the Random Mountain Generator Block
		randomMountainBlock = new BlockRandomMountain(randomMountainBlockID).setUnlocalizedName("mountainRandomGenBlock");
    	GameRegistry.registerBlock(randomMountainBlock, "mountainRandomGenBlock");
    	LanguageRegistry.addName(randomMountainBlock, "Random Mountain Generator Block");
    	
		//Create the Shattered Mountain Generator Block
		shatteredMountainBlock = new BlockShatteredMountain(shatteredMountainBlockID).setUnlocalizedName("mountainShatteredGenBlock");
    	GameRegistry.registerBlock(shatteredMountainBlock, "mountainShatteredGenBlock");
    	LanguageRegistry.addName(shatteredMountainBlock, "Shattered Mountain Generator Block");
    		
		LanguageRegistry.instance().addStringLocalization("itemGroup.tabMountainGen", "en_US", "Mountain Generator");
	}
	
	//Only images 256x256 in size or smaller are permitted
	public boolean isValidImageSize(String filename)
	{
		boolean bValidWidth = false;
		boolean bValidHeight = false;
		boolean bValidImageSize = false;
		
        
        try 
        {
    		File f1 = new File(filename);    		
        	BufferedImage img;
        	img = ImageIO.read(f1.toURI().toURL());
        	
        	if (img.getWidth() < 257)
        	{
        		bValidWidth = true;
        	}
        	
        	if (img.getHeight() < 257)
        	{
        		bValidHeight = true;
        	}
        	
        	if (bValidWidth)
        	{
        		if (bValidHeight)
        		{
        			bValidImageSize = true;
        		}
        	}
        }
        catch (Exception ex)
        {
        	Common.logger(Level.SEVERE, "Could not load image file '" + filename + "'");
        	Common.logger(Level.SEVERE, ex.toString());
        }
    	return bValidImageSize;
	}
}
