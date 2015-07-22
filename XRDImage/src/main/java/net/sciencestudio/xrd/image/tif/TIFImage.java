package net.sciencestudio.xrd.image.tif;

import ij.ImageStack;
import ij.io.Opener;
import ij.process.ImageProcessor;
import ij.process.StackProcessor;

import java.awt.image.BufferedImage;

import scidraw.drawing.common.Palette;

import net.sciencestudio.xrd.image.ImageJImage;

public class TIFImage extends ImageJImage{
	
	public TIFImage(String filename, Palette palette) {
		Opener opener = new Opener();
		backing = opener.openImage(filename);
		
		ImageProcessor ip = backing.getProcessor().duplicate();
		StackProcessor sp = new StackProcessor(backing.getStack(), ip);
		ImageStack s2 = null;
		s2 = sp.rotateRight();
		backing.setStack(null, s2);
		
		applyPalette(backing, palette, PaletteScale.LINEAR);
	}

	@Override
	public boolean supportsPalettes() {
		return true;
	}

	@Override
	public BufferedImage withPalette(Palette palette, PaletteScale scale) {
		
		applyPalette(backing, palette, scale);
		BufferedImage image = new BufferedImage(backing.getWidth(), backing.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		image.getGraphics().drawImage(backing.getBufferedImage(), 0, 0, null);

		return image;
	}

	

	@Override
	public double getValueAtPoint(int x, int y) {
		
		int value = backing.getProcessor().getPixel(x, y);
		float floatvalue = Float.intBitsToFloat(value);
		return floatvalue;
	}
	

	
}
