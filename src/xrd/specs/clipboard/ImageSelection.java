package xrd.specs.clipboard;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


public class ImageSelection implements Transferable
{

	private Image image;
	
	
	public ImageSelection(Image image)
	{
		this.image = image;
	}
	
	public Object getTransferData(DataFlavor flavour) throws UnsupportedFlavorException, IOException
	{	
		if (!DataFlavor.imageFlavor.equals(flavour)) {
            throw new UnsupportedFlavorException(flavour);
        }
		
		return image;

	}


	public DataFlavor[] getTransferDataFlavors()
	{
		return new DataFlavor[]{DataFlavor.imageFlavor};
	}


	public boolean isDataFlavorSupported(DataFlavor flavour)
	{
		return DataFlavor.imageFlavor.equals(flavour);
	}

}
