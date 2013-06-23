package ca.maxx.mountaingen;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import scala.Console;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class BlockMountain extends Block{

	int X = 0;
	int Y = 0;
	int Z = 0;
	public int l = 0;
	
	String imgPath = "./img.png";
	
	private String setInfo;
	private String setColor;
	
	public BlockMountain(int par1, String imgPath, String description) {
		super(par1, Material.glass);
        this.setCreativeTab(TabMountainGen.tabMountainGen);
        this.imgPath = imgPath;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon(MountainGen.modid + ":" + this.getUnlocalizedName2());
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack)
    {
		int currX = par2;
		int currY = par3;
		int currZ = par4;
		
		Configuration config = new Configuration(MountainGen.configFile);
        config.load();
        String imgFile = config.get(Configuration.CATEGORY_GENERAL, "ImagePath", "1.png").getString();
        config.save();
		
    	File f1 = new File(MountainGen.configImagesPath + imgFile);
    
     	//Get the block ID of the block below this block
    	//This is used to cover the mountain below level 150
    	int coverBlockID = par1World.getBlockId(par2, par3-1, par4);
    	
    	
    	BufferedImage img;
    	URL url = MountainGen.class.getResource(imgPath);

        int[] rgb;
        try {
        img = ImageIO.read(f1.toURI().toURL());
		
		l = MathHelper.floor_double((double)(par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		
		//South
		//-X
		//+Z
        if (l == 0)
        {        	
        	for(int y = 0; y < img.getHeight(); y++){
                String[] xCoords = new String[img.getWidth()];
                for(int x = 0; x < img.getWidth(); x++){
                    rgb = getPixelData(img, x, y);
                    
                    if ((rgb[0]>=currY) || (rgb[0]>=50))
                    {
                    	if (currY > 50)
                    	{
                    		currY = 50;
                    	}
                    	
                    	for(int yIndex=currY;yIndex<rgb[0]-1;yIndex++)
                    	{
                    		par1World.setBlock(currX-x, yIndex, currZ+y, 1);
                    	}
                    	
                    	if(rgb[0] < 150)
                    	{
                    		if (coverBlockID == Block.grass.blockID)
                    		{
	                    		par1World.setBlock(currX-x, rgb[0]-1, currZ+y, 3);
	                        	par1World.setBlock(currX-x, rgb[0], currZ+y, 2);
                    		}
                    		else
                    		{
	                    		par1World.setBlock(currX-x, rgb[0]-1, currZ+y, coverBlockID);
	                        	par1World.setBlock(currX-x, rgb[0], currZ+y, coverBlockID);
                    		}
                    	}
                    	else if (rgb[0] >149 && rgb[0]<180)
                    	{
                    		par1World.setBlock(currX-x, rgb[0]-1, currZ+y, 1);
                        	par1World.setBlock(currX-x, rgb[0], currZ+y, 1);
                    	}
                    	else
                    	{
                    		par1World.setBlock(currX-x, rgb[0]-1, currZ+y, 80);
                        	par1World.setBlock(currX-x, rgb[0], currZ+y, 78);
                    	}
                    }
                }
            }
        }

        //West
		//-X
		//-Z
        if (l == 1)
        {      	
        	for(int y = 0; y < img.getHeight(); y++){
                String[] xCoords = new String[img.getWidth()];
                for(int x = 0; x < img.getWidth(); x++){
                    rgb = getPixelData(img, x, y);
                    if ((rgb[0]>=currY) || (rgb[0]>=50))
                    {
                    	if (currY > 50)
                    	{
                    		currY = 50;
                    	}
                    	
                    	for(int yIndex=currY;yIndex<rgb[0]-1;yIndex++)
                    	{
                    		par1World.setBlock(currX-x, yIndex, currZ-y, 1);
                    	}
                    	
                    	if(rgb[0] < 150)
                    	{
                    		if (coverBlockID == Block.grass.blockID)
                    		{
		                		par1World.setBlock(currX-x, rgb[0]-1, currZ-y, 3);
		                    	par1World.setBlock(currX-x, rgb[0], currZ-y, 2);
                    		}
                    		else
                    		{
		                		par1World.setBlock(currX-x, rgb[0]-1, currZ-y, coverBlockID);
		                    	par1World.setBlock(currX-x, rgb[0], currZ-y, coverBlockID);
                    		}
                    	}
                    	else if (rgb[0] >149 && rgb[0]<180)
                    	{
	                		par1World.setBlock(currX-x, rgb[0]-1, currZ-y, 1);
	                    	par1World.setBlock(currX-x, rgb[0], currZ-y, 1);
                    	}
                    	else
                    	{
	                		par1World.setBlock(currX-x, rgb[0]-1, currZ-y, 80);
	                    	par1World.setBlock(currX-x, rgb[0], currZ-y, 78);
                    	}
                    }
                }
            }
        }

        //North
		//+X
		//-Z
        if (l == 2)
        {
        	for(int y = 0; y < img.getHeight(); y++){
                String[] xCoords = new String[img.getWidth()];
                for(int x = 0; x < img.getWidth(); x++){
                    rgb = getPixelData(img, x, y);
                    if ((rgb[0]>=currY) || (rgb[0]>=50))
                    {
                    	if (currY > 50)
                    	{
                    		currY = 50;
                    	}
                    		
                    	for(int yIndex=currY;yIndex<rgb[0]-1;yIndex++)
                    	{
                    		par1World.setBlock(currX+x, yIndex, currZ-y, 1);
                    	}
                    	
                    	if(rgb[0] < 150)
                    	{
                    		if (coverBlockID == Block.grass.blockID)
                    		{
	                    		par1World.setBlock(currX+x, rgb[0]-1, currZ-y, 3);
	                        	par1World.setBlock(currX+x, rgb[0], currZ-y, 2);
                    		}
                    		else
                    		{
	                    		par1World.setBlock(currX+x, rgb[0]-1, currZ-y, coverBlockID);
	                        	par1World.setBlock(currX+x, rgb[0], currZ-y, coverBlockID);
                    		}
                    	}
                    	else if (rgb[0] >149 && rgb[0]<180)
                    	{
                    		par1World.setBlock(currX+x, rgb[0]-1, currZ-y, 1);
                        	par1World.setBlock(currX+x, rgb[0], currZ-y, 1);
                    	}
                    	else
                    	{
                    		par1World.setBlock(currX+x, rgb[0]-1, currZ-y, 80);
                        	par1World.setBlock(currX+x, rgb[0], currZ-y, 78);
                    	}
                    }
                }
            }
        }

        //East
		//+X
		//+Z
        if (l == 3)
        {
        	for(int y = 0; y < img.getHeight(); y++){
                String[] xCoords = new String[img.getWidth()];
                for(int x = 0; x < img.getWidth(); x++){
                    rgb = getPixelData(img, x, y);
                    if ((rgb[0]>=currY) || (rgb[0]>=50))
                    {
                    	if (currY > 50)
                    	{
                    		currY = 50;
                    	}
                    	for(int yIndex=currY;yIndex<rgb[0]-1;yIndex++)
                    	{
                    		par1World.setBlock(currX+x, yIndex, currZ+y, 1);
                    	}
                    	
                    	if(rgb[0] < 150)
                    	{

                    		if (coverBlockID == Block.grass.blockID)
                    		{
	                    		par1World.setBlock(currX+x, rgb[0]-1, currZ+y, 3);
	                        	par1World.setBlock(currX+x, rgb[0], currZ+y, 2);
                    		}
                    		else
                    		{
	                    		par1World.setBlock(currX+x, rgb[0]-1, currZ+y, coverBlockID);
	                        	par1World.setBlock(currX+x, rgb[0], currZ+y, coverBlockID);
                    		}
                    	}
                    	else if (rgb[0] >149 && rgb[0]<180)
                    	{
                    		par1World.setBlock(currX+x, rgb[0]-1, currZ+y, 1);
                        	par1World.setBlock(currX+x, rgb[0], currZ+y, 1);
                    	}
                    	else
                    	{
                    		par1World.setBlock(currX+x, rgb[0]-1, currZ+y, 80);
                        	par1World.setBlock(currX+x, rgb[0], currZ+y, 78);
                    	}
                    }
                }
            }
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private static int[] getPixelData(BufferedImage img, int x, int y) {
		int argb = img.getRGB(x, y);

		int rgb[] = new int[] {
		    (argb >> 16) & 0xff, //red
		    (argb >>  8) & 0xff, //green
		    (argb      ) & 0xff  //blue
		};
		return rgb;
		}


}
