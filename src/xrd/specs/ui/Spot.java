package xrd.specs.ui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import scidraw.drawing.backends.DrawingSurfaceFactory;
import scidraw.drawing.backends.Surface;

public class Spot {
	
	public float x, y;
	public int boxSize;
	public String title;
	
	public Spot(float x, float y, int boxSize, String title) {
		this.x = x;
		this.y = y;
		this.boxSize = boxSize;
		this.title = title;
	}
	
	

	private static float scaleRatio, lineWidth;
	
	public static void drawMarkings(BufferedImage image, int rawHeight, List<Spot> spots)
	{
		
		int imageHeight = image.getHeight();
		scaleRatio = imageHeight / (float)rawHeight;
		

		
		Surface s = DrawingSurfaceFactory.createSaveableSurfaceFromG2DBufferedImage(image);
		//s.scale(1/scaleRatio, 1/scaleRatio);
		
		
		float highlightSize = 5*scaleRatio;
		lineWidth = 2.5f;
		s.setFontSize(14f);
		
		
		for (Spot r : spots)
		{
			
			float rx = (r.x*scaleRatio);
			float ry = (r.y*scaleRatio);
			
			
			s.setLineWidth(lineWidth);
			s.setSource(0, 0, 0, 192);
			s.rectangle(
					(int)(  rx - (highlightSize)  )+lineWidth, 
					(int)(  ry - (highlightSize)  )+lineWidth, 
					highlightSize*2, 
					highlightSize*2);
			s.stroke();
			
			
			s.setLineWidth(lineWidth);
			s.setSource(Color.white);
			s.rectangle(
					(int)(rx - (highlightSize)), 
					(int)(ry - (highlightSize)), 
					highlightSize*2, 
					highlightSize*2);
			s.stroke();
			
			boolean noRoomBelow = false, noRoomLeft = false, noRoomRight = false;
			
			
			if (image.getHeight() - ry < s.getFontHeight() + highlightSize) noRoomBelow = true;
			
			if (image.getWidth() - rx < s.getTextWidth(r.title)/2) noRoomRight = true;
			if (rx < s.getTextWidth(r.title)/2) noRoomLeft = true;
		
			if (noRoomLeft) {
				textRight(s, rx, ry, r.title, highlightSize);
			} else if (noRoomRight) {
				textLeft(s, rx, ry, r.title, highlightSize);
			} else if (noRoomBelow) {
				textAbove(s, rx, ry, r.title, highlightSize);
			} else {
				textBelow(s, rx, ry, r.title, highlightSize);
			}
			
			
			
		}
		
	}
	
	private static void text(Surface s, float x, float y, String text)
	{
		s.setSource(0, 0, 0, 192);
		s.writeText(text, x + lineWidth, y + lineWidth);
		
		s.setSource(Color.white);
		s.writeText(text, x, y);
	}
	
	
	private static void textRight(Surface s, float rx, float ry, String text, float highlightSize)
	{
		int ypos = (int)(ry + highlightSize);
		int xpos = (int)(rx + highlightSize + 3);
		
		text(s, xpos, ypos, text);
	}
	
	private static void textLeft(Surface s, float rx, float ry, String text, float highlightSize)
	{
		int ypos = (int)(ry + highlightSize);
		int xpos = (int)(rx - highlightSize - s.getTextWidth(text) - 3);
		
		text(s, xpos, ypos, text);
	}
	
	
	private static void textAbove(Surface s, float rx, float ry, String text, float highlightSize)
	{
		int ypos = (int)(ry - s.getFontLeading() - s.getFontDescent() - highlightSize - 1);
		int xpos = (int)(rx - (s.getTextWidth(text)/2f));
		
		text(s, xpos, ypos, text);
	}
	
	
	private static void textBelow(Surface s, float rx, float ry, String text, float highlightSize)
	{
		
		int ypos = (int)(ry + s.getFontAscent() + highlightSize + 1);
		int xpos = (int)(rx - (s.getTextWidth(text)/2f));
		
		text(s, xpos, ypos, text);
	}
	
}
