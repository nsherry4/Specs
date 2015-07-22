package net.sciencestudio.xrd.specs;

import scidraw.drawing.common.Palette;
import net.sciencestudio.xrd.image.XRDImage.PaletteScale;


public class SPEcsModel
{

	public boolean zoomFit;
	public double zoomScale;
	public double zoomLastFitScale;
	
	public Palette palette;
	public PaletteScale paletteScale;
	
	public SPEcsModel()
	{
		palette = Palette.THERMAL;
		paletteScale = PaletteScale.LINEAR;
		zoomScale = 1.0;
		zoomLastFitScale = 1.0;
		zoomFit = false;
	}
	
}
