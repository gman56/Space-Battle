//Garrett Slough
//SE350

package space;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import display.ConsoleItemImpl;
import display.ViewManager;
import utils.ColorMaker;
import utils.Point3D;
import utils.PolygonPlus;
import utils.SoundUtility;

public class CargoShip extends Ship implements Runnable {

	private int debrisClouds;
	private boolean isShipAtPort;
	private String curPortID;
	private int waitCount;
	
	
	public CargoShip (String id, String colorName, Point3D location, Point3D destination, 
			double maxStrength, double speed, int debrisClouds) throws NullParamException
	{
	
		super(id, colorName, location, destination, maxStrength, speed);
		
			
			//Color Switcher
		    cName = colorName;
		    
		    //Port Checker
		  	isPort = false;
		  	
			displayName = "Cargo Ship";
			
			isMissle = false;
			
			isFighter = false;
			


		
	    Color aColor = ColorMaker.makeColor(colorName); // “YELLOW”, “RED”, etc
		setDebris(debrisClouds);

		SpaceController.addship(this);
		
		
		ViewManager.getInstance().updateItem(
				new ConsoleItemImpl(id, location, aColor, 
						angle, shape, getInfoText(), isDestroyed(), isDamaged())); //false changed to isDestroyed()
		
		new Thread(this).start();
	}
	
	protected boolean getIsAtPort()
	{
		return isShipAtPort;
	}
	
	private boolean setIsAtPort(boolean val)
	{
		return isShipAtPort = val;
	}
	
	
	public void updateStatus() throws NullParamException
	{
		if (this.isDamaged())
		{
			if (getIsAtPort() == true)
			{
				if (SpaceController.getLocation(curPortID) == null)
				{
					curPortID = null;
					setIsAtPort(false);
					setDestination(SpaceController.makePoint());
					return;
					
				}
				else
				{
					Point3D p = SpaceController.getLocation(curPortID);
					
					if (p == null)
					{
						return;
					}
					else
					{
						addToCurrentStrength(0.25);
						setLocation(p);
						setDestination(p);
						return;
						
					}
					
					
				}
				
			
				
			}
			else // not at port / IS DAMAGED
			{
				 String port = SpaceController.getNearestSpacePort(getLocation(), getColorName());
				 Point3D p = SpaceController.getLocation(port);
				 if (p == null)
				 {
					 return;
				 }
				 else
				 {
					 setDestination(p);
					 
					 if (getLocation().distance(getDestination()) <= 10.0)
					 {
						 setDestination(getLocation());
						 setIsAtPort(true);
						 curPortID = port;
						 return;
					 }
					 else if (getLocation().distance(getDestination()) > 10.0)
					 {
						 return;
					 }
					 
				 }
			}
				
		}
		else // not damaged
		{
			if (getIsAtPort() == true) 
			{
				curPortID = null;
				setIsAtPort(false);
				setDestination(SpaceController.makePoint());
			}
			else // not at port
			{
				return;
			}
		}
		
	}
	
	


	@Override
	public void run() {
		
		if (this.isDestroyed())
		{
			throw new IllegalArgumentException("Ship is Destroyed");
		}
		while(this.isDestroyed() == false)
		{
			if (waitCount > 0)
			{
				waitCount--;
			}
			
			try {
				this.updateStatus();
			} catch (NullParamException e2) {
	            System.out.println(e2.getMessage());
				e2.printStackTrace();
			}
			
			
				try {
					this.move(1);
				} catch (NullParamException
						| space.DebrisCloud.NullParamException e11) {
		            System.out.println(e11.getMessage());
					e11.printStackTrace();
				}
			
			
			ViewManager.getInstance().updateItem(
					new ConsoleItemImpl(id, location, colorName, 
							angle, shape, getInfoText(), isDestroyed(), isDamaged())); // false changed to isDestroyed()
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		
		}
		
		
		
	}
	
	
	private int getDebris()
	 {
		return debrisClouds; 
	 }
	 
	 private void setDebris(int val) throws NullParamException
	 {
		if (val < 0)
		{
			throw new NullParamException("DebrisCloud must be greater than or equal to 0");
		}
		else 
		{
			debrisClouds = val;
		}
	 }
	 
	private int getWaitCount()
	{
		return waitCount;
	}
	
	private void setWaitCount(int val) throws NullParamException
	{
		if (val < 0)
		{
			throw new NullParamException("waitCount must be greater than or equal to 0");
		}
		else {
			waitCount = val;
		}
	}
	 
	public String getInfoText() 
	{

	
		
		String s = "Color:" + cName + System.getProperty("line.separator") 
				+ System.getProperty("line.separator") +
				"Location:" + getLocation() 
				+System.getProperty("line.separator") +"Destination:" + getDestination() 
				+ System.getProperty("line.separator")+ "Speed:" + getSpeed() 
				+ System.getProperty("line.separator") + "Angle:" + getAngle() 
				+ System.getProperty("line.separator") 
				+ System.getProperty("line.separator") + "Strength: " + getCurrentStrength() 
				+ System.getProperty("line.separator") + "Max Strength:" + getMaxStrength() 
				+ System.getProperty("line.separator") +  "Damaged:	" + isDamaged();
		
		return s;
		
				
		
	}
	
	
	
	public void reactToRadarLock(Point3D loc) throws NullParamException, space.DebrisCloud.NullParamException
	{
		if (waitCount > 0)
		{
			return;
		}
		if (getDebris() <= 0)
		{
			return;
		}
		if (getIsAtPort() == true)
		{
			return;
		}
		
		else
		{
			setDestination(SpaceController.makePoint());
			SoundUtility.getInstance().playSound("sounds" + File.separator + "Cloud.wav" );
			
			
			Point3D newP = SpaceController.makePointNear(getLocation(), 0.05);
			new DebrisCloud("Debris Cloud" + debrisClouds + "from" + getID(), newP, "GRAY", 3000, true, 1.0);
			debrisClouds--;
			
			Point3D newP1 = SpaceController.makePointNear(getLocation(), 0.05);
			new DebrisCloud("Debris Cloud" + debrisClouds + "from" + getID(), newP1, "GRAY", 3000, true, 1.0);
			debrisClouds--;
			
			setWaitCount(25);
		}
		
		
		
		
	}
	
	public void resetStrength() throws NullParamException 
	{
		setCurrentStrength(getMaxStrength()); 
	}
	
	
	
	
	
	
	
	
	
	
	
	//Initializer 
	{
		ArrayList<Point> sp = new ArrayList<>();
        sp.add(new Point(+0, -20));   // The Point here is java.awt.Point
        sp.add(new Point(+12, +16));  // The Point here is java.awt.Point
        sp.add(new Point(+0, +30));   // The Point here is java.awt.Point
        sp.add(new Point(-12, +16));  // The Point here is java.awt.Point
        shape = new PolygonPlus();
        for (Point sp1 : sp) {
            shape.addPoint(sp1.x, sp1.y);
        }
        
		
	}
	
	
	

	
	
}
