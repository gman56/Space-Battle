//Garrett Slough

package space;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import display.ConsoleItemImpl;
import display.ViewManager;
import utils.ColorMaker;
import utils.Point3D;
import utils.PolygonPlus;

public class DebrisCloud implements Runnable {

	private String cloudID;
	private Point3D loc; 
	private PolygonPlus cloudShape;
	private int duration; 
	private Color cloudColor;
	private boolean radarVisible;
	
	private double sizeFactor;
	
	public DebrisCloud(String cloudID, Point3D loc, String cloudColor, int duration,
			boolean radarVisible, double sizeFactor) throws NullParamException
	{
		
	    Color aColor = ColorMaker.makeColor(cloudColor); // “YELLOW”, “RED”, etc

		
		setcloudID(cloudID);
		setloc(loc);
		setDuration(duration);
		setCloudColor(cloudColor);
		setRadarVisible(radarVisible);
		setSizeFactor(sizeFactor);
		
		 ArrayList<Point> sp = new ArrayList<>();
		 sp.add(new Point(0, -13));
		 sp.add(new Point(4, -15));
		 sp.add(new Point(7, -14));
		 sp.add(new Point(9, -9));
		 sp.add(new Point(14, -7));
		 sp.add(new Point(15, -4));
		 sp.add(new Point(13, 0));
		 sp.add(new Point(15, 4));
		 sp.add(new Point(14, 7));
		 sp.add(new Point(9, 9));
		 sp.add(new Point(7, 14));
		 sp.add(new Point(4, 15));
		 sp.add(new Point(0, 13));
		 sp.add(new Point(-4, 15));
		 sp.add(new Point(-7, 14));
		 sp.add(new Point(-9, 9));
		 sp.add(new Point(-14, 7));
		 sp.add(new Point(-15, 4));
		 sp.add(new Point(-13, 0));
		 sp.add(new Point(-15, -4));
		 sp.add(new Point(-14, -7));
		 sp.add(new Point(-9, -9));
		 sp.add(new Point(-7, -14));
		 sp.add(new Point(-4, -15));
		 cloudShape = new PolygonPlus();
		 for (Point sp1 : sp) {
		 cloudShape.addPoint(sp1.x, sp1.y);
		 } 
		
		cloudShape.scale(sizeFactor);
		
		
		
		ViewManager.getInstance().updateItem(
				new ConsoleItemImpl(cloudID, loc, aColor, Math.toRadians(Math.random() * 360.0), 
						cloudShape, getInfoText(), false, false)); 
		
		
		
		if (radarVisible == true)
		{
			SpaceController.addCloud(this);
		}
		
		new Thread(this).start();
		
		
		
		
	}
	
	public void run()
	{
		while (duration > 0)
		{
			ViewManager.getInstance().updateItem(
					new ConsoleItemImpl(cloudID, loc, cloudColor, Math.toRadians(Math.random() * 360.0), 
							cloudShape, getInfoText(), false, false)); 
			
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					int removeFromDuration = 50;
					duration = duration - removeFromDuration;
		
		}				
		
		
		if (duration <= 0)
		{
			ViewManager.getInstance().removeItem(cloudID);
			if (radarVisible == true)
			{
				SpaceController.removeCloud(cloudID);
				return;
			}
			else 
			{
				return;
			}
			
		}
		
		
	}
	
	
	public String getcloudID()
	{
		return cloudID; 
	}
	
	private void setcloudID(String id) throws NullParamException
	{
		if (id == null)
		{
			throw new NullParamException("ID not unique, try another");
		}
		else {
			cloudID = id;
		}
	
	}
	
	public Point3D getloc()
	{
		
		return loc;
	}
	
	private void setloc(Point3D x) throws NullParamException
	{
		if (x == null)
		{
			throw new NullParamException("Invalid Location");
		}
		else {
		loc = x;
		}
	}
	
	@SuppressWarnings("unused")
	private PolygonPlus getCloudShape()
	{
		return cloudShape; 
	}
	
	
	private int getDuration()
	{
		return duration;
	}
	
	private void setDuration(int x) throws NullParamException
	{
		if (x < 0)
		{
			throw new NullParamException("Invalid Duration/Cycles");
		}
		else {
		duration = x; 
		}
	}
	
	@SuppressWarnings("unused")
	private Color getCloudColor()
	{
		return cloudColor;
	}
	
	private void setCloudColor(String newColor) throws NullParamException
	{
		List<String> validStrings = Arrays.asList("YELLOW" , "PURPLE", "GREEN", "BLUE", "RED", "CYAN", "GRAY", "MAGENTA", "ORANGE", "PINK", "WHITE", "MAROON", "OLIVE","TEAL", "GOLD", "SKYBLUE", "VIOLET", "BROWN", "TAN");
		if (validStrings.contains(newColor))
				{
				Color changer = ColorMaker.makeColor(newColor); // “YELLOW”, “RED”, etc

				cloudColor = changer;
				}
		else
		{
			throw new NullParamException("Not a valid color");

		}
	}
	
	private boolean getRadarVisible()
	{
		return radarVisible; 
	}
	
	
	private void setRadarVisible(boolean x)
	{
		
		radarVisible = x;
	}
	
	@SuppressWarnings("unused")
	private double getSizeFactor()
	{
		
		return sizeFactor; 
	}
	
	private void setSizeFactor(double x) throws NullParamException
	{
		if (x < 0)
		{
			throw new NullParamException("Invalid Cloud Size");
		}
		sizeFactor = x; 
	}
	
	
	
	public String getInfoText()
	{
		
		String s = "Location" + getloc() 
				+ System.getProperty("line.separator") 
				+ System.getProperty("line.separator") +"Duration:" + getDuration() 
				+ System.getProperty("line.separator") +"Radar Visible:" + getRadarVisible(); 
		
		
		
		return s;
		
	}
	
	
	
	
	
	
	
public class NullParamException extends Exception{
		
		public NullParamException(String msg)
		{
			super(msg);
		}
		
	}
	
	
	
	
	
}




