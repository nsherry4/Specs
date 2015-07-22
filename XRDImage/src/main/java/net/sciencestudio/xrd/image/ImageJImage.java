package net.sciencestudio.xrd.image;

import ij.ImagePlus;
import ij.io.FileInfo;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.awt.image.IndexColorModel;
import java.util.List;

import scidraw.drawing.common.Palette;


public abstract class ImageJImage extends ColourableImage {

	protected ImagePlus backing;
	
	protected void applyPalette(ImagePlus backing, Palette palette, PaletteScale scale)
	{
		applyPalette(backing, palette.toSpectrum(), scale);
	}
	
	
	protected void applyPalette(ImagePlus backing, List<Color> palette, PaletteScale scale)
	{
		
		
		int size = palette.size();
		int requiredSize = 256;
		
		FileInfo fi = new FileInfo();
		fi.reds = new byte[size];
		fi.greens = new byte[size];
		fi.blues = new byte[size];
		
		//int[] spectrum = new int[size];
				
		int index;
		double maxIndex = requiredSize;
		if (scale == PaletteScale.LOG) maxIndex = (int)Math.log1p(requiredSize) + 1;
		if (scale == PaletteScale.SQUARE) maxIndex = requiredSize * requiredSize;
		
		for (int i = 0; i < requiredSize; i++){

			//calculate the position into the palette list we should look
			double percent;

			percent = i / (float)maxIndex;
			if (scale == PaletteScale.LOG) percent = Math.log1p(i) / maxIndex;
			if (scale == PaletteScale.SQUARE) percent = (i * i) / maxIndex;
			
			index = (int)(percent * size);
			
			
			fi.reds[i] =(byte)(palette.get(index).getRed());
			fi.greens[i] =(byte)(palette.get(index).getGreen());
			fi.blues[i] =(byte)(palette.get(index).getBlue());
		}
		
		
		//LUT lut = new LUT(8, requiredSize, fi.reds, fi.greens, fi.blues);
		//lut.getRGBs(spectrum);
		
		ImageProcessor ip = backing.getProcessor();
		ip.setColorModel(new IndexColorModel(8, requiredSize, fi.reds, fi.greens, fi.blues));
		
	}
	
	
	@Override
	public int getWidth() {
		return backing.getWidth();
	}

	@Override
	public int getHeight() {
		return backing.getHeight();
	}



	
	
}
