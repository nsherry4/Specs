package net.sciencestudio.xrd.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import scidraw.drawing.common.Palette;

import net.sciencestudio.xrd.image.png.PNGImage;
import net.sciencestudio.xrd.image.spe.SPEImage;
import net.sciencestudio.xrd.image.tif.TIFImage;

public abstract class XRDImage {

	
	public static enum PaletteScale{LINEAR, LOG, SQUARE}
	
	public static XRDImage loadImage(String filename) throws IOException
	{
		return loadImage(filename, Palette.THERMAL);
	}
	
	public static XRDImage loadImage(String filename, Palette palette) throws IOException
	{
		
		String lcFilename = filename.toLowerCase();
		
		File file = new File(filename);
		if (!file.exists()) throw new FileNotFoundException();
		
		
		if (lcFilename.endsWith(".spe"))
		{
			
			return new SPEImage(filename, palette);
			
		}
		
		
		if (lcFilename.endsWith(".tif") || lcFilename.endsWith(".tiff"))
		{
			
			return new TIFImage(filename, palette);
			
		}
		
		
		if (lcFilename.endsWith(".png"))
		{
			
			return new PNGImage(filename);
			
		}
		
		
		throw new UnsupportedEncodingException();
		
		
	}
	
	
	public abstract boolean supportsPalettes();

	public abstract BufferedImage withPalette(Palette palette, PaletteScale scale);

	public abstract int getWidth();
	public abstract int getHeight();
	
	public abstract double getValueAtPoint(int x, int y);
	
}
