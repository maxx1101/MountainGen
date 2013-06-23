package ca.maxx.mountaingen;

import java.io.File;
import java.util.logging.Level;

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

@Mod(modid = MountainGen.modid, name = "Mountain Generator", version = "1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)

public class MountainGen {
	
	public static final String modid = "MountainGen";
	
	public static Block mountainBlock;
	public static int mountainBlockID;
	public static Property imgProperty;
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

        mountainBlockID = config.getBlock("MountainGenBlock", 900).getInt();
        imgProperty = config.get(Configuration.CATEGORY_GENERAL, "ImagePath", "1.png");
        
        imgProperty.comment = "Path to image file. '1.png' is the image located in the mod file";
        
        imgPath = imgProperty.getString();

        config.save();
    }
	
	@Init
	public void load(FMLInitializationEvent event)
	{
		//Register the Command Event Handler for possible future use
		MinecraftForge.EVENT_BUS.register(new CommandEventHandler());
		
		mountainBlock = new BlockMountain(mountainBlockID, imgPath, "Mountains!").setUnlocalizedName("mountainGenBlock");
		
		GameRegistry.registerBlock(mountainBlock, "mountainGenBlock");
		
		LanguageRegistry.addName(mountainBlock, "Mountain Generator Block");
		LanguageRegistry.instance().addStringLocalization("itemGroup.tabMountainGen", "en_US", "Mountain Generator");
	}
	
}
