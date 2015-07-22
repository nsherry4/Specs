package net.sciencestudio.xrd.specs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;

import swidget.dialogues.fileio.SwidgetIO;

import eventful.EventfulListener;

import net.sciencestudio.xrd.specs.JDraggableViewport;
import net.sciencestudio.xrd.specs.SPEcsController;


public class ImageView extends JPanel
{

	public SPEcsController controller;
	private JPanel canvas;
	private JLabel coordInfo;
	private JScrollPane canvasScroller;
	private JFrame owner;
	
	public ImageView(JFrame owner)
	{
		
		controller = new SPEcsController();
		this.owner = owner;
		 
		initGUI();
	
		controller.addListener(new EventfulListener() {
			
			
			public void change()
			{
				JViewport viewport = canvasScroller.getViewport();				
				
				Dimension extents = viewport.getExtentSize();
				Dimension viewSize = viewport.getViewSize();
				
				Dimension d = new Dimension(
						controller.getImageWidth(extents.width), controller.getImageHeight(extents.height)
				);

				
				Point p = viewport.getViewPosition();
				double ratio = d.getWidth() / viewSize.width;
				
				double pX = p.x;
				double pY = p.y;
				pX += (extents.width / 2.0);
				pY += (extents.height / 2.0);
				pX *= ratio;
				pY *= ratio;
				pX -= (extents.width / 2.0);
				pY -= (extents.height / 2.0);
				
				if (pY < 0) pY = 0;
				if (pX < 0) pX = 0;
				if (pY + extents.height > d.height) pY = d.height - extents.height;
				if (pX + extents.width > d.width) pX = d.width - extents.width;
				
				Point position = new Point((int)pX, (int)pY);
				
				canvas.setPreferredSize(d);
				canvas.setMinimumSize(d);
				canvas.setMaximumSize(d);
				canvas.setSize(d);
				
				canvasScroller.revalidate();

				viewport.setViewPosition(position);
				
			}
		});
		
		controller.updateListeners();
		
		
		 
	}
	

	private void initGUI()
	{

		
		setLayout(new BorderLayout());

		
		canvasScroller = new JScrollPane();
		final JViewport viewport = new JDraggableViewport();

		canvas = new JPanel(){
			
			@Override
			public void paintComponent(Graphics g)
			{
				paintCanvasEvent((Graphics2D)g);
			}
			
		};
		canvas.setDoubleBuffered(true);
		viewport.add(canvas);
		canvasScroller.setViewport(viewport);
		canvasScroller.getHorizontalScrollBar().setBlockIncrement(100);
		canvasScroller.getVerticalScrollBar().setBlockIncrement(100);
		add(canvasScroller, BorderLayout.CENTER);
		

		coordInfo = new JLabel("X: , Y: , Value: ");
		coordInfo.setHorizontalAlignment(SwingConstants.CENTER);
		coordInfo.setFont(coordInfo.getFont().deriveFont(Font.PLAIN));
		
		add(coordInfo, BorderLayout.SOUTH);
		
		viewport.addMouseMotionListener(new MouseMotionAdapter() {
		
			@Override
			public void mouseMoved(MouseEvent e)
			{
				int x, y;
				x = viewport.getViewPosition().x + e.getPoint().x;
				y = viewport.getViewPosition().y + e.getPoint().y;
				
				Point point = new Point(x, y);
				Point pixel = controller.getPixelAtRealPoint(point);
				if (pixel == null) return;
				coordInfo.setText("X: " + pixel.x + ", Y: " + pixel.y + ", Value: " + controller.getValueAtPixel(pixel));
				
			}

		});
		
		controller.addListener(new EventfulListener() {
		
			public void change()
			{
				canvas.repaint();
			}
		});
		
		this.setVisible(true);

		
	}
	

	public void paintCanvasEvent(Graphics2D g)
	{		
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		controller.draw(g, getImageWidth(), getImageHeight());

	}
	
	
	public int getImageWidth()
	{
		return controller.getImageWidth(canvas.getWidth());
	}

	public int getImageHeight()
	{
		return controller.getImageHeight(canvas.getHeight());
	}
	
	/*
	public int getImageSize()
	{
		int width = controller.getImageWidth(canvas.getWidth());
		int height = controller.getImageHeight(canvas.getHeight());
		
		return Math.min(width, height);
	}
	*/
	
	public void actionSaveImage()
	{
		if (! controller.hasImage()) return;
		
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			controller.savePicture(outStream);
			SwidgetIO.saveFile(owner, "Save Picture As...", "png", "PNG Images", "~/", outStream);

		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, new JLabel("Error: Could not save file."));
		}
	}
	
}
