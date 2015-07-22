package java.awt.image;

import java.awt.image.ColorModel;

import scidraw.drawing.common.Palette;

public class IndexedColorModel extends ColorModel
{

	public IndexedColorModel(int bits, Palette p)
	{
		super(bits);
	}

	@Override
	public int getAlpha(int pixel)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBlue(int pixel)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getGreen(int pixel)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRed(int pixel)
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
