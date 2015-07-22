package net.sciencestudio.xrd.image.png;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import scidraw.drawing.common.Palette;

import net.sciencestudio.xrd.image.XRDImage;

public class PNGImage extends XRDImage {

	BufferedImage backing;
	
	public PNGImage(String filename) throws IOException {
		backing = ImageIO.read(new File(filename));
	}
	
	@Override
	public boolean supportsPalettes() {
		return false;
	}

	@Override
	public BufferedImage withPalette(Palette palette, PaletteScale scale) {
		return backing;
	}

	@Override
	public int getWidth() {
		return backing.getWidth();
	}

	@Override
	public int getHeight() {
		return backing.getWidth();
	}

	@Override
	public double getValueAtPoint(int x, int y) {
		return 0;
	}

}
