package xrd.specs.indexing;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xrd.specs.ui.Spot;

public class Indexing {

		
	public static List<Spot> getReflections(String indexFile)
	{
		List<Spot> reflections = new ArrayList<Spot>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(indexFile));
			
			//get the reflection count
			for (int i = 0; i < 3; i++) reader.readLine();
			
			
			String reflectionCountString = reader.readLine();
			System.out.println(reflectionCountString);
			int reflectionCount = Integer.parseInt(  reflectionCountString.split(" +")[4]  );
			
			//read column header line
			reader.readLine();
			
			//read all reflections
			for (int i = 0; i < reflectionCount; i++)
			{
				//split the string by spaces, filtering out empty strings caused by consecutive spaces.
				List<String> reflectionLine = Arrays.asList(reader.readLine().split(" +"));
								
				float x = Float.parseFloat(  reflectionLine.get(1)  );
				float y = Float.parseFloat(  reflectionLine.get(2)  );
				
				int box = (int)Float.parseFloat(  reflectionLine.get(13)  );
				
				String hkl = reflectionLine.get(3) + ", " + reflectionLine.get(4) + ", " + reflectionLine.get(5);
				
				reflections.add(new Spot(x, y, box, hkl));
				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		return reflections;
	}
	
}
