package xrd.specs.ui;


import javax.swing.JTabbedPane;


public class TabBar extends JTabbedPane
{

	
	public TabBar()
	{
		
	}
	
	public ImageView getCurrentImage()
	{
		return (ImageView)getSelectedComponent();
	}
	
	public void closeCurrentTab()
	{
		remove(getCurrentImage());
	}
	
}
