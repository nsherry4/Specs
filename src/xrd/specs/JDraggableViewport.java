package xrd.specs;


import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;


import javax.swing.JViewport;


public class JDraggableViewport extends JViewport
{

	private Point startPoint, startPosition;
	
	public JDraggableViewport()
	{
		
		super();
		
		setAlignmentX(0.5f);
		setAlignmentY(0.5f);
		
		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e)
			{
							
				int offsetX, offsetY, pX, pY;
				
				offsetX = e.getPoint().x - startPoint.x;
				offsetY = e.getPoint().y - startPoint.y;
				
				pX = startPosition.x - offsetX;
				pY = startPosition.y - offsetY;
				
				if (pY < 0) pY = 0;
				if (pX < 0) pX = 0;
				Dimension extents = getExtentSize();
				Dimension viewSize = getViewSize();
				if (pY + extents.height > viewSize.height) pY = viewSize.height - extents.height;
				if (pX + extents.width > viewSize.width) pX = viewSize.width - extents.width;
				
				Point position = new Point(pX, pY);
				setViewPosition(position);
			}
		});
		
		addMouseListener(new MouseListener() {
		
			public void mouseReleased(MouseEvent e)
			{
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		
		
			public void mousePressed(MouseEvent e)
			{
				startPoint = e.getPoint();
				startPosition = getViewPosition();
				setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}
		
		
			public void mouseExited(MouseEvent e)
			{
				// TODO Auto-generated method stub
		
			}
		
		
			public void mouseEntered(MouseEvent e)
			{
				// TODO Auto-generated method stub
		
			}
		
		
			public void mouseClicked(MouseEvent e)
			{
				// TODO Auto-generated method stub
		
			}
		});
	}
	
}
