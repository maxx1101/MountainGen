package ca.maxx.mountaingen.common;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;

import ca.maxx.mountaingen.MountainGen;

public class Common {

	static FMLLog log = new FMLLog();
	
	//Method to download height maps 
	public static void downloadHeightMaps(String fileURL, String filename)
	{
		OutputStream out = null;
	    URLConnection conn = null;
	    InputStream in = null;

	    try 
	    {
	        URL url = new URL(fileURL);
	        out = new BufferedOutputStream(new FileOutputStream(MountainGen.configImagesPath + filename));
	        conn = url.openConnection();
	        in = conn.getInputStream();
	        byte[] buffer = new byte[1024];

	        int numRead;
	        long numWritten = 0;

	        while ((numRead = in.read(buffer)) != -1) 
	        {
	            out.write(buffer, 0, numRead);
	            numWritten += numRead;
	        }

	        logger(Level.INFO,"[MountainGen] Downloaded: '" + filename + "'" + " - Size: " + numWritten + " bytes.");
	    } 
	        
	    catch (Exception exception) 
	    {
	    	exception.printStackTrace();
	    } 
	    finally 
	    {
	        try 
	        {
	        	if (in != null) 
	        	{
	                in.close();
	            }
	            if (out != null) {
	                out.close();
	            }
	        } 
	        catch (IOException ex) 
	        {
	        	logger(Level.SEVERE,"[MountainGen] problem downloading mountain height map: ");
	        	logger(Level.SEVERE,ex.toString());
	        }
	    }
	}
	
	//Generic logging method
	public static void logger(Level level, String message)
	{
		log.log(level, message);
	}
}
