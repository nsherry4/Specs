package net.sciencestudio.xrd.image.spe;

import ij.io.Opener;

import java.awt.image.BufferedImage;

import scidraw.drawing.common.Palette;

import net.sciencestudio.xrd.image.ImageJImage;

public class SPEImage extends ImageJImage {
	
	public SPEImage(String filename, Palette palette)
	{
		Opener opener = new Opener();
		backing = opener.openImage(filename);
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

		return value;
	}
	
	
}
