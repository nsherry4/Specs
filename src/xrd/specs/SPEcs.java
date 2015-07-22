package xrd.specs;

import swidget.Swidget;
import swidget.icons.IconFactory;

import xrd.specs.ui.MainWindow;


public class SPEcs
{
	
	public static void main(String args[])
	{
		
		System.setProperty("sun.java2d.pmoffscreen", "false");
		Swidget.initialize();
		IconFactory.customPath = "/xrd/specs/icons/";
		
		
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				new MainWindow();
			}
		});
	}
	
}
