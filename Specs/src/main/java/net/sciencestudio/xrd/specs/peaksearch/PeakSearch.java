package net.sciencestudio.xrd.specs.peaksearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fava.functionable.FStringInput;

import net.sciencestudio.xrd.specs.ui.Spot;

public class PeakSearch
{

	public static List<Spot> getPeaks(String peakFile)
	{
		List<Spot> spots = new ArrayList<Spot>();
		
		BufferedReader reader = null;
		try
		{

			List<String> lines = FStringInput.lines(new File(peakFile)).toSink();
			int totalPoints = readInt(lines.remove(0), 0);
			
			System.out.println(totalPoints);
			
			int pointCount = 0;
			for (String line : lines)
			{
				List<String> words = FStringInput.words(line).toSink();
				
				pointCount++;
				if (pointCount >= totalPoints) break;
				
				Spot spot = new Spot(
						readFloat(words.get(0), 0f), 
						readFloat(words.get(1), 0f), 
						readInt(words.get(4), 10),
						(int)Math.round(readFloat(words.get(2), 0f)) + ""
					);
				spots.add(spot);
			}

		}

		// handle read errors
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
			// Close resource.
			try 
			{ 
				//System.out.println("close it");
				if (reader != null){ 
					reader.close();
				}
			} catch (IOException e) {}
			
		}
		
		return spots;
	}
	
	private static Float readFloat(String floatString, Float fallback)
	{
		try {
			return Float.parseFloat(floatString.trim());
		} catch (Exception e) {
			return fallback;
		}
	}
	
	private static Integer readInt(String intString, Integer fallback)
	{
		try {
			return Integer.parseInt(intString.trim());
		} catch (Exception e) {
			return fallback;
		}
	}
	
	
}
