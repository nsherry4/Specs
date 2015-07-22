package net.sciencestudio.xrd.spe.pngconvert;



import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import scidraw.drawing.common.Palette;

import net.sciencestudio.xrd.image.XRDImage;
import net.sciencestudio.xrd.image.XRDImage.PaletteScale;
import net.sciencestudio.xrd.image.pngencode.PngEncoderB;


/*
 * 
 * Converts SPE images into PNG images
 * Nathaniel Sherry
 * 2009-05-29
 * 
 */

public class Spe2Png
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

		if (args.length != 2) {
			error();
		}

		String inFile = args[0];
		String outFile = args[1];

		
		try {
			
			XRDImage image = XRDImage.loadImage(inFile);
			PngEncoderB encoder = new PngEncoderB(image.withPalette(Palette.THERMAL, PaletteScale.LINEAR));
			encoder.setCompressionLevel(9);
			byte[] encodedImage = encoder.pngEncode();
			OutputStream out = new FileOutputStream(outFile);
			out.write(encodedImage);
			out.close();
						
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
/*			
			byte[] buffer = SpeImage.getInputBuffer(new SpeHeader(new FileInputStream(inFile)));

			SpeImage img = null;
			img = new SpeImage(new FileInputStream(inFile), buffer);

			
			
			//img.bin(1);
			
			// remove background
			//List<Double> data = Background.ImageBackgroundRemoval(img, 0);
			List<Double> data = img.getImageData();
			
			// data = ListCalculations.logList_threaded(data); //logs the data, this makes spots brighter
			// double min = ListCalculations.min(data, false); //false makes it look for the lowest *non-zero*
			// value. Corrects for dead spots
			// data = ListCalculations.subtractFromList_threaded(data, min, 0.0); //lowers all values by min,
			// with a lower bound at 0.0



			DrawingRequest dr = new DrawingRequest();

			dr.dataWidth = img.header.xdim;
			dr.dataHeight = img.header.ydim;
			dr.imageWidth = img.header.xdim;
			dr.imageHeight = img.header.ydim;
			dr.yflip = false; // map is flipped by default since the primary purpose of this drawing code is
			// to show upside-down XRF scans
			dr.maxYIntensity = img.header.getSaturationValue();



			List<AbstractPalette> colourRules = new ArrayList<AbstractPalette>();
			
			// Examples of how to change the way the map is coloured. Map checks a list of palettes in
			// order until oen of them
			// returns a non-null colour object.

			// == turns saturated sections white ==
			//colourRules.add( new SaturationPalette(new Colour(1.0, 1.0, 1.0, 1.0), null) );
			//colourRules.add(new ThermalScalePalette(coloursteps, false));
			
			// == turns saturated sections white, unsaturated sections black ==
			//colourRules.add( new SaturationPalette(new Colour(1.0, 1.0, 1.0, 1.0), new Colour(0.0, 0.0, 0.0, 1.0)) );
			
			// == uses monochrome scale, turns saturated points red ==
			//colourRules.add( new SaturationPalette(new Colour(0.0, 1.0, 0.0, 1.0), null) );
			//colourRules.add( new ThermalScalePalette(true) );

			

			// == applies a standard thermal scale ==
			colourRules.add(new ThermalScalePalette(256, false));

			
			
			System.out.println(new Date());
			
			SaveableSurface backend = null;
			for (int i = 0; i < 10; i++){
				backend = DrawingSurfaceFactory.createSaveableSurface(SurfaceType.RASTER,
						(int) dr.imageWidth, (int) dr.imageHeight);
	
				Map map = new Map(backend, dr);
				map.draw(data, new ThreadedRasterMapPainter(colourRules, data));
			}
			System.out.println(new Date());
			
			
			
			backend.write(new FileOutputStream(new File(outFile)));
*/
			/*
			
			List<Colour> indexedScale = Spectrums.ThermalScale(coloursteps);
			
			byte red[] = new byte[coloursteps];
			byte green[] = new byte[coloursteps];
			byte blue[] = new byte[coloursteps];
			
			for (int i = 0; i < coloursteps; i++){
				red[i] = (byte)Math.round(indexedScale.get(i).red * 255);
				green[i] = (byte)Math.round(indexedScale.get(i).green * 255);
				blue[i] = (byte)Math.round(indexedScale.get(i).blue * 255);
			}
			
			
			IndexColorModel colormodel = new IndexColorModel(8, coloursteps, red, green, blue );
			BufferedImage indexedImage = new BufferedImage((int) dr.imageWidth, (int) dr.imageHeight, BufferedImage.TYPE_BYTE_INDEXED, colormodel);
			
			BufferedImage input = ImageIO.read(  new FileInputStream(new File(outFile))  );

			indexedImage.createGraphics().drawImage(input, 0, 0, (int) dr.imageWidth, (int) dr.imageHeight , 0, 0, (int) dr.imageWidth, (int) dr.imageHeight , null);
			
			
			ImageIO.write(indexedImage, "png", new FileOutputStream(new File(outFile + "_")));

*/





		System.exit(0);

	}


	private static void error()
	{
		System.err.println("Usage: spe2png <Input SPE File> <Output File (PNG)>");
		System.exit(-1);
	}



}
