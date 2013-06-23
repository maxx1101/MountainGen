package ca.maxx.mountaingen;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = Mod_MountainGen.modid, name = "Mountain Generator", version = "1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)

public class Mod_MountainGen {
	
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
			System.out.println("[MountainGen] Creating config folder: " + configPath);
		    boolean configPathResult = new File(configPath).mkdir();  
		    boolean configImagesPathresult = new File(configImagesPath).mkdir();  
		    if(configPathResult && configImagesPathresult){    
		       System.out.println("[MountainGen] Created config folder.");  
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
	}
}
