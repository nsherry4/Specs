package xrd.specs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import scidraw.drawing.common.Palette;

import eventful.Eventful;

import xrd.specs.clipboard.ImageSelection;
import xrd.specs.indexing.Indexing;
import xrd.specs.peaksearch.PeakSearch;
import xrd.specs.ui.Spot;
import xrdimage.XRDImage;
import xrdimage.XRDImage.PaletteScale;


public class SPEcsController extends Eventful
{

	private static final double ZOOM_SCALE = 1.5;
	
	private SPEcsModel model;
	private XRDImage sourceImage;
	private List<Spot> spots;
	
	private BufferedImage cachedScaledImage;
	private double cachedScaleSize = -1;
		
	
	public SPEcsController()
	{
		super();
		model = new SPEcsModel();
		spots = new ArrayList<Spot>();
	
	}
	
	
	public boolean loadXRDImage(String filename)
	{
	

		try {
			sourceImage = XRDImage.loadImage(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		invalidateCache();
		updateListeners();
		
		
		return true;
		
	}
	
	public boolean loadXRDPeakData(String filename)
	{
		spots = PeakSearch.getPeaks(filename);
		
		invalidateCache();
		updateListeners();
		
		return true;
		
	}
	
	public boolean loadXRDIndexData(String filename)
	{
		spots = Indexing.getReflections(filename);
		
		invalidateCache();
		updateListeners();
		
		return true;
	}

	private void invalidateCache()
	{
		cachedScaleSize = -1;
	}
	
	public void draw(Graphics2D g, int width, int height)
	{
		
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, width, height);

		
		BufferedImage image = null;
		if (hasImage())
		{
			
			int size = Math.min(width, height);
			double scale = size / (double)Math.min(sourceImage.getHeight(), sourceImage.getWidth());
			model.zoomLastFitScale = scale;
			
			if (cachedScaleSize != scale)
			{
				
				image = sourceImage.withPalette(model.palette, model.paletteScale);
				
				cachedScaleSize = scale;
				System.out.println("Scaling...");
				
				AffineTransform t = AffineTransform.getScaleInstance(scale, scale);
				
				cachedScaledImage = new BufferedImage((int)(image.getWidth() * scale), (int)(image.getHeight() * scale), BufferedImage.TYPE_4BYTE_ABGR);
				
				AffineTransformOp op = new AffineTransformOp(t, AffineTransformOp.TYPE_BILINEAR);			 
				op.filter(image, cachedScaledImage);
				
				Spot.drawMarkings(cachedScaledImage, image.getHeight(), spots);
				
			} else {
				image = cachedScaledImage;
			}
			
			
			g.drawImage(cachedScaledImage, 0, 0, null);
			
		}
		

				
	}
	
	public void setPalette(Palette palette)
	{
		
		
		if (!hasImage()){
			updateListeners();
			return;
		}
	
		model.palette = palette;
		
		invalidateCache();
		updateListeners();
		
	}
	
	
	public void setPaletteScale(PaletteScale scale)
	{
		model.paletteScale = scale;
		
		if (!hasImage()){
			updateListeners();
			return;
		}
	

		invalidateCache();
		updateListeners();
		
	}
	
	
	
	public PaletteScale getPaletteScale()
	{
		return model.paletteScale;
	}

	public void zoomIn()
	{
		model.zoomFit = false;
		
		double newZoom = model.zoomScale * ZOOM_SCALE;
		
		if (newZoom * Math.max(sourceImage.getHeight(), sourceImage.getWidth()) < 6000)
		{
			model.zoomScale *= ZOOM_SCALE;
		}
		
		updateListeners();
	}
	
	public void zoomOut()
	{	
		model.zoomFit = false;
		
		model.zoomScale /= ZOOM_SCALE;
		updateListeners();
	}
	
	public void zoomFit()
	{
		model.zoomFit = ! model.zoomFit;
		updateListeners();
	}
	
	public void zoomNormal()
	{
		model.zoomFit = false;
		
		model.zoomScale = 1.0;
		updateListeners();
	}
	
	public boolean getZoomFit()
	{
		return model.zoomFit;
	}
	
	public int getImageWidth(int parentWidth)
	{
		if (!hasImage()) return 1;
		
		if (!model.zoomFit) return (int)(sourceImage.getWidth() * model.zoomScale);
		return parentWidth;
	}
	
	public int getImageHeight(int parentHeight)
	{
		if (!hasImage()) return 1;
		
		if (!model.zoomFit) return (int)(sourceImage.getHeight() * model.zoomScale);
		return parentHeight;
	}
	

	public void copyImageToClipboard(int width, int height)
	{
	
		if (!hasImage()) return;
		
		width = getImageWidth(width);
		height = getImageHeight(height);
		
		BufferedImage i = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		draw((Graphics2D)i.getGraphics(), width, height);
		
		ImageSelection imageSel = new ImageSelection(i);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imageSel, null);
	}
	
	public void savePicture(OutputStream out) throws IOException
	{
		if (out == null) return;
		if (!hasImage()) return;
		
		BufferedImage i = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		draw((Graphics2D)i.getGraphics(), sourceImage.getWidth(), sourceImage.getHeight());
		
		invalidateCache();
		ImageIO.write(i, "png", out);
		
	}
	
	public boolean hasImage()
	{
		return sourceImage != null;
	}
	
	public Point getPixelAtRealPoint(Point p)
	{
		double scale;
		if (! model.zoomFit) {
			scale = model.zoomScale;
		} else {
			scale = model.zoomLastFitScale;
		}
		
		Point pixel = new Point((int)(p.x / scale), (int)(p.y / scale));
		
		if (pixel.x > sourceImage.getWidth() || pixel.y > sourceImage.getHeight()) return null;
		
		return pixel;
		
	}
	
	public double getValueAtPixel(Point pixel)
	{
		return sourceImage.getValueAtPoint(pixel.x, pixel.y);
		//return sourceImage.getProcessor().getPixel(pixel.x, pixel.y);
		//return 0;
		
	}


	public Palette getPaletteType() {
		return model.palette;
	}
	
}
