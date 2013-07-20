package ca.maxx.mountaingen;

import java.util.Calendar;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import ca.maxx.mountaingen.common.NoiseGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRandomMountain extends Block{

	int X = 0;
	int Y = 0;
	int Z = 0;
	public int l = 0;
	
	int nSize = 256;
	int nHillMin = 2;   //Smallest possible radius for the hill
    int nHillMax = 75;  //Largest possible radius for the hill
    int nNumHills = 200;    //Number of hills to add
    int nFlattening = 1;    //power to raise heightmap values to to flatten
    int uSeed = (int)Calendar.getInstance().getTimeInMillis();      //The seed
    double nMaxHeight = 0;
    
    private double[][] imgArray = new double[256][256];
		
	private String setInfo;
	private String setColor;
	
	public BlockRandomMountain(int par1) {
		super(par1, Material.glass);
        this.setCreativeTab(TabMountainGen.tabMountainGen);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon(MountainGen.modid + ":mountainRandomGenBlock");
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack)
    {
		int currX = par2;
		int currY = par3;
		int currZ = par4;
		    
     	//Get the block ID of the block below this block
    	//This is used to cover the mountain below y level 150
    	int coverBlockID = par1World.getBlockId(par2, par3-1, par4);
    	int coverBlockMetadata = par1World.getBlockMetadata(par2, par3-1, par4);

        int[] rgb;
        
        //Get the direction that the player is facing
		l = MathHelper.floor_double((double)(par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		
		//Clear max height
		nMaxHeight = 0;
		
		//Reset the imgArray to 0

        for (int x = 0; x < nSize; x++)
        {
            for (int y = 0; y < nSize; y++)
            {
            	imgArray[x][y] = 0;
            }
        }
		
		//Create the number of mountains specified...
		for (int i = 0; i < nNumHills; i++)
		{
		    createMountain();
		}

		Calendar cSeed = Calendar.getInstance();
		Random rSeed = new Random();
		long lSeed = cSeed.getTimeInMillis() * rSeed.nextLong();
		Random r = new Random(lSeed);
		int pSeed = r.nextInt(65000);
        		
        //Add noise
        NoiseGenerator ng = new NoiseGenerator(pSeed, 4, 1, 0.015, 0.65);
        
        for (int x = 0; x < nSize; x++)
        {
            for (int y = 0; y < nSize; y++)
            {
            	double dNum = ng.Noise(x, y);
            	//System.out.println(dNum);
                if (dNum <0)
                    dNum = dNum * -1;
                dNum = dNum * 1000;
                double dnum2 = imgArray[x][y];

                //System.out.println(x + "," + y + ": " + imgArray[x][y]);
                if (dnum2 > 0)
                {
                   // MessageBox.Show(imgArray[x, y] + "!");
                	setCell(x,y,(dNum * dnum2));
                    //System.out.println(" - " + (dNum * dnum2));
                }
            }
        }
		
		//South
		//-X
		//+Z
		if (l == 0)
		{        	
        	//Offset X so the mountain is in front of us
        	currX = currX + 128;
        	
			for(int y = 0; y < nSize; y++){
		        String[] xCoords = new String[nSize];
		        for(int x = 0; x < nSize; x++){
		            rgb = getPixelData(x, y);
		            
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
        	 	
			for(int y = 0; y < nSize; y++){
		        String[] xCoords = new String[nSize];
		        for(int x = 0; x < nSize; x++){
		            rgb = getPixelData(x, y);
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
        	
			for(int y = 0; y < nSize; y++){
		        String[] xCoords = new String[nSize];
		        for(int x = 0; x < nSize; x++){
		            rgb = getPixelData(x, y);
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
        	
			for(int y = 0; y < nSize; y++){
		        String[] xCoords = new String[nSize];
		        for(int x = 0; x < nSize; x++){
		            rgb = getPixelData(x, y);
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
    }
	
	private int[] getPixelData(int x, int y) 
	{
		double nResize = nMaxHeight / 256;
        int colour = (int)Math.ceil((imgArray[x][y] / nResize));

        if (colour > 255)
            colour = 255;

        if (colour < 0)
            colour = 0;

		int rgb[] = new int[] {
				colour, //red
				colour, //green
				colour  //blue
		};
		return rgb;
		}



    private void createMountain()
    {
        //Determine hill size
        int nRadius = randomRange(nHillMin, nHillMax);

        int x = 0;
        int y = 0;

        Random rDirection = new Random();

		Calendar cSeed = Calendar.getInstance();
		Random r = new Random(cSeed.getTimeInMillis());
		
        //Determine in which direction from the center the hill will be placed
        int nTheta = randomRange(0, r.nextInt(10)+1);

        //Determine how far from the center the hill will be placed
        int nDistance = randomRange(nRadius / 2, nSize / 2 - nRadius);

        x = (int)(nSize / 2.0 + Math.cos(nTheta) * nDistance);
        y = (int)(nSize / 2.0 + Math.sin(nTheta) * nDistance);

        //Square root the hill radius so we don't have to square root the distance
        int nRadiusSq = nRadius * nRadius;
        int nDistanceSq;
        int nHeight;

        //Find the range of coordinates affected by the hill
        int xMin = x - nRadius - 1;
        int xMax = x + nRadius + 1;

        //don't affect cells outside of bounds
        if (xMin < 0)
        {
            xMin = 0;
        }

        if (xMax >= nSize)
        {
            xMax = nSize - 1;
        }

        int yMin = y - nRadius - 1;
        int yMax = y + nRadius + 1;

        //don't affect cells outside of bounds
        if (yMin < 0)
        {
            yMin = 0;
        }

        if (yMax >= nSize)
        {
            yMax = nSize - 1;
        }

        // for each affected coordinate, determine the height of the hill at that point
        // and add it to that coordinate
        for (int xIndex = 0; xIndex < xMax; xIndex++)
        {
            for (int yIndex = 0; yIndex < yMax; yIndex++)
            {
                //determine how far from the center of the hill this point is
                nDistanceSq = (x - xIndex) * (x - xIndex) + (y - yIndex) * (y - yIndex);

                //determine the height of the hill at this point
                nHeight = nRadiusSq - nDistanceSq;

                //don't add negative hill values (e.g. outside the hill's radius)
                if (nHeight > 0)
                {
                    setCell(xIndex, yIndex, nHeight);
                }
            }
        }
    }
       
    //Sets the value of the point at X & Y
    private void setCell(int x, int y, double value)
    {
        imgArray[x][y] += value;
        if (imgArray[x][y] > nMaxHeight)
            nMaxHeight = imgArray[x][y];
    }
	
	private int randomRange(int min, int max)
	{
		Calendar cSeed = Calendar.getInstance();
		Random rSeed = new Random();
		
		long lSeed = cSeed.getTimeInMillis() * rSeed.nextLong();
		
		Random r = new Random(lSeed);
		//System.out.println("Create Mountain Range seed: " + lSeed);

		if (max < min)
			max = min + 10;
				
        return (r.nextInt(max-min)+min);
	}
	
	
}
