package ca.maxx.mountaingen;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import ca.maxx.mountaingen.common.Common;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMountain extends Block{

	int X = 0;
	int Y = 0;
	int Z = 0;
	public int l = 0;
	
	String imgPath = "./img.png";
	
	private String setInfo;
	private String setColor;
	
	public BlockMountain(int par1, String imgPath) {
		super(par1, Material.glass);
        this.setCreativeTab(TabMountainGen.tabMountainGen);
        this.imgPath = imgPath;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon(MountainGen.modid.toLowerCase() + ":mountainGenBlock");
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack)
    {
		int currX = par2;
		int currY = par3;
		int currZ = par4;
		
    	File f1 = new File(MountainGen.configImagesPath + this.imgPath);
    
    	Common.logger(Level.INFO, par1World.getChunkFromBlockCoords(par2, par4).getChunkCoordIntPair().chunkXPos + ", " + par1World.getChunkFromBlockCoords(par2, par4).getChunkCoordIntPair().chunkZPos);
    	
    	
     	//Get the block ID of the block below this block
    	//This is used to cover the mountain below y level 150
    	int coverBlockID = par1World.getBlockId(par2, par3-1, par4);
    	int coverBlockMetadata = par1World.getBlockMetadata(par2, par3-1, par4);

    	BufferedImage img;
        int[] rgb;
        
        try {
        img = ImageIO.read(f1.toURI().toURL());
		
		l = MathHelper.floor_double((double)(par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		
		//South
		//-X
		//+Z
        if (l == 0)
        {
        	//Offset X so the mountain is in front of us
        	currX = currX + 128;
        	
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
                    	if(rgb[0] < 80)
                    	{
                    		if (coverBlockID == Block.grass.blockID)
                    		{
	                    		par1World.setBlock(currX-x, rgb[0]-5, currZ+y, 3);
	                    		par1World.setBlock(currX-x, rgb[0]-4, currZ+y, 3);
	                    		par1World.setBlock(currX-x, rgb[0]-3, currZ+y, 3);
	                    		par1World.setBlock(currX-x, rgb[0]-2, currZ+y, 3);
	                    		par1World.setBlock(currX-x, rgb[0]-1, currZ+y, 3);
	                        	par1World.setBlock(currX-x, rgb[0], currZ+y, 2);
                    		}
                    		else
                    		{
	                    		par1World.setBlock(currX-x, rgb[0]-5, currZ+y, coverBlockID, coverBlockMetadata,1);
	                    		par1World.setBlock(currX-x, rgb[0]-4, currZ+y, coverBlockID, coverBlockMetadata,1);
	                    		par1World.setBlock(currX-x, rgb[0]-3, currZ+y, coverBlockID, coverBlockMetadata,1);
	                    		par1World.setBlock(currX-x, rgb[0]-2, currZ+y, coverBlockID, coverBlockMetadata,1);
	                    		par1World.setBlock(currX-x, rgb[0]-1, currZ+y, coverBlockID, coverBlockMetadata,1);
	                        	par1World.setBlock(currX-x, rgb[0], currZ+y, coverBlockID, coverBlockMetadata,1);
                    		}
                    	}
                    	else if(rgb[0] >79 &&rgb[0] < 150)
                    	{
                    		if (coverBlockID == Block.grass.blockID)
                    		{
	                    		par1World.setBlock(currX-x, rgb[0]-1, currZ+y, 3);
	                        	par1World.setBlock(currX-x, rgb[0], currZ+y, 2);
                    		}
                    		else
                    		{
	                    		par1World.setBlock(currX-x, rgb[0]-1, currZ+y, coverBlockID, coverBlockMetadata,1);
	                        	par1World.setBlock(currX-x, rgb[0], currZ+y, coverBlockID, coverBlockMetadata,1);
                    		}
                    	}
                    	else if (rgb[0] >149 && rgb[0]<180)
                    	{
                    		par1World.setBlock(currX-x, rgb[0]-1, currZ+y, 1);
                        	par1World.setBlock(currX-x, rgb[0], currZ+y, 1);
                    	}
                    	else
                    	{
                    		par1World.setBlock(currX-x, rgb[0]-6, currZ+y, 80);
                    		par1World.setBlock(currX-x, rgb[0]-5, currZ+y, 80);
                    		par1World.setBlock(currX-x, rgb[0]-4, currZ+y, 80);
                    		par1World.setBlock(currX-x, rgb[0]-3, currZ+y, 80);
                    		par1World.setBlock(currX-x, rgb[0]-2, currZ+y, 80);
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
        	//Offset Z so the mountain is in front of us
        	currZ = currZ + 128;
        	
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
                    	if(rgb[0] < 80)
                    	{
                    		if (coverBlockID == Block.grass.blockID)
                    		{
		                		par1World.setBlock(currX-x, rgb[0]-5, currZ-y, 3);
		                		par1World.setBlock(currX-x, rgb[0]-4, currZ-y, 3);
		                		par1World.setBlock(currX-x, rgb[0]-3, currZ-y, 3);
		                		par1World.setBlock(currX-x, rgb[0]-2, currZ-y, 3);
		                		par1World.setBlock(currX-x, rgb[0]-1, currZ-y, 3);
		                    	par1World.setBlock(currX-x, rgb[0], currZ-y, 2);
                    		}
                    		else
                    		{
		                		par1World.setBlock(currX-x, rgb[0]-5, currZ-y, coverBlockID, coverBlockMetadata,1);
		                		par1World.setBlock(currX-x, rgb[0]-4, currZ-y, coverBlockID, coverBlockMetadata,1);
		                		par1World.setBlock(currX-x, rgb[0]-3, currZ-y, coverBlockID, coverBlockMetadata,1);
		                		par1World.setBlock(currX-x, rgb[0]-2, currZ-y, coverBlockID, coverBlockMetadata,1);
		                		par1World.setBlock(currX-x, rgb[0]-1, currZ-y, coverBlockID, coverBlockMetadata,1);
		                    	par1World.setBlock(currX-x, rgb[0], currZ-y, coverBlockID, coverBlockMetadata,1);
                    		}
                    	}
                    	else if(rgb[0] >79 &&rgb[0] < 150)
                    	{
                    		if (coverBlockID == Block.grass.blockID)
                    		{
		                		par1World.setBlock(currX-x, rgb[0]-1, currZ-y, 3);
		                    	par1World.setBlock(currX-x, rgb[0], currZ-y, 2);
                    		}
                    		else
                    		{
		                		par1World.setBlock(currX-x, rgb[0]-1, currZ-y, coverBlockID, coverBlockMetadata,1);
		                    	par1World.setBlock(currX-x, rgb[0], currZ-y, coverBlockID, coverBlockMetadata,1);
                    		}
                    	}
                    	else if (rgb[0] >149 && rgb[0]<180)
                    	{
	                		par1World.setBlock(currX-x, rgb[0]-1, currZ-y, 1);
	                    	par1World.setBlock(currX-x, rgb[0], currZ-y, 1);
                    	}
                    	else
                    	{
	                		par1World.setBlock(currX-x, rgb[0]-6, currZ-y, 80);
	                		par1World.setBlock(currX-x, rgb[0]-5, currZ-y, 80);
	                		par1World.setBlock(currX-x, rgb[0]-4, currZ-y, 80);
	                		par1World.setBlock(currX-x, rgb[0]-3, currZ-y, 80);
	                		par1World.setBlock(currX-x, rgb[0]-2, currZ-y, 80);
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
        	//Offset X so the mountain is in front of us
        	currX = currX - 128;
        	
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

                    	
                    	if(rgb[0] < 80)
                    	{
                    		if (coverBlockID == Block.grass.blockID)
                    		{
	                    		par1World.setBlock(currX+x, rgb[0]-5, currZ-y, 3);
	                    		par1World.setBlock(currX+x, rgb[0]-4, currZ-y, 3);
	                    		par1World.setBlock(currX+x, rgb[0]-3, currZ-y, 3);
	                    		par1World.setBlock(currX+x, rgb[0]-2, currZ-y, 3);
	                    		par1World.setBlock(currX+x, rgb[0]-1, currZ-y, 3);
	                        	par1World.setBlock(currX+x, rgb[0], currZ-y, 2);
                    		}
                    		else
                    		{
	                    		par1World.setBlock(currX+x, rgb[0]-5, currZ-y, coverBlockID, coverBlockMetadata,1);
	                    		par1World.setBlock(currX+x, rgb[0]-4, currZ-y, coverBlockID, coverBlockMetadata,1);
	                    		par1World.setBlock(currX+x, rgb[0]-3, currZ-y, coverBlockID, coverBlockMetadata,1);
	                    		par1World.setBlock(currX+x, rgb[0]-2, currZ-y, coverBlockID, coverBlockMetadata,1);
	                    		par1World.setBlock(currX+x, rgb[0]-1, currZ-y, coverBlockID, coverBlockMetadata,1);
	                        	par1World.setBlock(currX+x, rgb[0], currZ-y, coverBlockID, coverBlockMetadata,1);
                    		}
                    	}
                    	else if(rgb[0] >79 &&rgb[0] < 150)
                    	{
                    		if (coverBlockID == Block.grass.blockID)
                    		{
	                    		par1World.setBlock(currX+x, rgb[0]-1, currZ-y, 3);
	                        	par1World.setBlock(currX+x, rgb[0], currZ-y, 2);
                    		}
                    		else
                    		{
	                    		par1World.setBlock(currX+x, rgb[0]-1, currZ-y, coverBlockID, coverBlockMetadata,1);
	                        	par1World.setBlock(currX+x, rgb[0], currZ-y, coverBlockID, coverBlockMetadata,1);
                    		}
                    	}
                    	else if (rgb[0] >149 && rgb[0]<180)
                    	{
                    		par1World.setBlock(currX+x, rgb[0]-1, currZ-y, 1);
                        	par1World.setBlock(currX+x, rgb[0], currZ-y, 1);
                    	}
                    	else
                    	{
                    		par1World.setBlock(currX+x, rgb[0]-6, currZ-y, 80);
                    		par1World.setBlock(currX+x, rgb[0]-5, currZ-y, 80);
                    		par1World.setBlock(currX+x, rgb[0]-4, currZ-y, 80);
                    		par1World.setBlock(currX+x, rgb[0]-3, currZ-y, 80);
                    		par1World.setBlock(currX+x, rgb[0]-2, currZ-y, 80);
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
        	//Offset Z so the mountain is in front of us
        	currZ = currZ - 128;
        	
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

                    	if(rgb[0] < 80)
                    	{

                    		if (coverBlockID == Block.grass.blockID)
                    		{
	                    		par1World.setBlock(currX+x, rgb[0]-5, currZ+y, 3);
	                    		par1World.setBlock(currX+x, rgb[0]-4, currZ+y, 3);
	                    		par1World.setBlock(currX+x, rgb[0]-3, currZ+y, 3);
	                    		par1World.setBlock(currX+x, rgb[0]-2, currZ+y, 3);
	                    		par1World.setBlock(currX+x, rgb[0]-1, currZ+y, 3);
	                        	par1World.setBlock(currX+x, rgb[0], currZ+y, 2);
                    		}
                    		else
                    		{
	                    		par1World.setBlock(currX+x, rgb[0]-5, currZ+y, coverBlockID, coverBlockMetadata,1);
	                    		par1World.setBlock(currX+x, rgb[0]-4, currZ+y, coverBlockID, coverBlockMetadata,1);
	                    		par1World.setBlock(currX+x, rgb[0]-3, currZ+y, coverBlockID, coverBlockMetadata,1);
	                    		par1World.setBlock(currX+x, rgb[0]-2, currZ+y, coverBlockID, coverBlockMetadata,1);
	                    		par1World.setBlock(currX+x, rgb[0]-1, currZ+y, coverBlockID, coverBlockMetadata,1);
	                        	par1World.setBlock(currX+x, rgb[0], currZ+y, coverBlockID, coverBlockMetadata,1);
                    		}
                    	}
                    	else if(rgb[0] >79 &&rgb[0] < 150)
                    	{

                    		if (coverBlockID == Block.grass.blockID)
                    		{
	                    		par1World.setBlock(currX+x, rgb[0]-1, currZ+y, 3);
	                        	par1World.setBlock(currX+x, rgb[0], currZ+y, 2);
                    		}
                    		else
                    		{
	                    		par1World.setBlock(currX+x, rgb[0]-1, currZ+y, coverBlockID, coverBlockMetadata,1);
	                        	par1World.setBlock(currX+x, rgb[0], currZ+y, coverBlockID, coverBlockMetadata,1);
                    		}
                    	}
                    	else if (rgb[0] >149 && rgb[0]<180)
                    	{
                    		par1World.setBlock(currX+x, rgb[0]-1, currZ+y, 1);
                        	par1World.setBlock(currX+x, rgb[0], currZ+y, 1);
                    	}
                    	else
                    	{
                    		par1World.setBlock(currX+x, rgb[0]-6, currZ+y, 80);
                    		par1World.setBlock(currX+x, rgb[0]-5, currZ+y, 80);
                    		par1World.setBlock(currX+x, rgb[0]-4, currZ+y, 80);
                    		par1World.setBlock(currX+x, rgb[0]-3, currZ+y, 80);
                    		par1World.setBlock(currX+x, rgb[0]-2, currZ+y, 80);
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
