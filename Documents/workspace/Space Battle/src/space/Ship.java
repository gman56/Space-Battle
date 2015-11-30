//Garrett Slough

package space;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import display.ViewManager;
import utils.ColorMaker;
import utils.Point3D;
import utils.PolygonPlus;
import utils.SoundUtility;

public class Ship {

	protected String id;
	protected Color colorName;
	protected Point3D location;
	protected Point3D destination;
	protected double maxStrength;
	protected double speed;
	protected double angle; //decimal 135.0
	protected String cName;
	protected double currentStrength;
	protected boolean isPort;
	protected boolean isFighter;
	protected String displayName;
	
	protected PolygonPlus shape;
	
	protected boolean isMissle;
	protected boolean isNuke;
	
	public Ship(String id, String colorName, Point3D location, Point3D destination, 
				double maxStrength, double speed) throws NullParamException
	{
		
		// Color Switcher
		  String c = colorName;
		    cName = c;
		    
		
	    setID(id);
	    setColor(colorName);
		setLocation(location);
		setDestination(destination);
		setMaxStrength(maxStrength);
		setSpeed(speed);		
		setCurrentStrength(maxStrength);
		
		
	}
	
	public void reactToRadarLock(Point3D loc) throws NullParamException, space.DebrisCloud.NullParamException 
	{
		
	}
	
	public String getInfoText()
	{
		return null;
	}

	
	
	public boolean isMissle()
	{
		return isMissle;
	}
	
	public boolean isFighter()
	{
		return isFighter;
	}
	
	
	public void applyDamage(double val) throws NullParamException, space.DebrisCloud.NullParamException
	{
		double strength;
		strength = getCurrentStrength() - val;
		setCurrentStrength(strength);
		
		if (strength > 0)
		{
			return;
		}
		else if (strength <= 0)
		{
			SpaceController.removeShip(getID());   
			ViewManager.getInstance().removeItem(getID()); 
			SpaceController.processDetonation(getID(), getLocation(), 50, 200); 
			
			 new DebrisCloud("Ship Debris Cloud from " + getID(), getLocation(), cName , 4000, false, 1.5);  
			 SoundUtility.getInstance().playSound("sounds" + File.separator + "Blast.wav");
			 return;
			
		}
		
	}
	
	
	public void move(int cycles) throws NullParamException, space.DebrisCloud.NullParamException  
	{
		Point3D locOld = getLocation(); 
		double distanceTraveled = (getSpeed()*cycles);
		
		double distanceToDest = getLocation().distance(getDestination());
		
		if (distanceToDest == 0.0)
		{
			return;
		}
		else if(distanceToDest != 0.0)
		{
			if (distanceTraveled >= distanceToDest)
			{
				setLocation(getDestination());
				setDestination(SpaceController.makePoint());
				
			}
			else
			{
				double delta = distanceTraveled / distanceToDest;
				double newXCoord = getLocation().getX() + (getDestination().getX() - getLocation().getX()) * delta;
				double newYCoord = getLocation().getY() + (getDestination().getY() - getLocation().getY()) * delta;
				double newZCoord = getLocation().getZ() + (getDestination().getZ() - getLocation().getZ()) * delta;
			
				
				Point3D locNew = new Point3D(newXCoord, newYCoord, newZCoord);
				setLocation(locNew);
				
				double nx = locNew.getX() - locOld.getX();
				double ny = locNew.getY() - locOld.getY();
				
				double newAngle = Math.atan2(ny, nx) + (Math.PI /2.0);
				
				setAngle(newAngle);
				
			}
		}
	}
	
	
	protected String getDisplayName()
	{
		return displayName;
	} 
	
	protected boolean isSpacePort()
	{
		return isPort;
		
	}
	
	
	public boolean isDamaged()
	{
		if (currentStrength < maxStrength){
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public boolean isDestroyed()
	{
		if (currentStrength <= 0)
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	public Point3D getLocation()
	{
		
		return location;
	}
	
	protected void setLocation(Point3D loc) throws NullParamException
	{
		if (loc != null)
		{
			location = loc;

		}
		else
		{
			throw new NullParamException("Location Invalid");
		}
	}
	
	protected Point3D getDestination()
	{
		return destination;
	}
	
	public void setDestination(Point3D newDest) throws NullParamException
	{
		if (newDest != null) 
			destination = newDest;
		else {
			throw new NullParamException("Try new Coordinates");
		}
	}
	
	 protected Color getColor()
	 {
		return colorName; 
	 }
	 
	 protected String getColorName()
	 {
		return cName; 
	 }
	 
	 protected void setColor(String newColor) throws NullParamException
	 {
		List<String> validStrings = Arrays.asList("YELLOW" , "PURPLE", "GREEN", "BLUE", "RED", "CYAN", "GRAY", "MAGENTA", "ORANGE", "PINK", "WHITE", "MAROON", "OLIVE","TEAL", "GOLD", "SKYBLUE", "VIOLET", "BROWN", "TAN");
		if (validStrings.contains(newColor))
				{
				Color changer = ColorMaker.makeColor(newColor); // “YELLOW”, “RED”, etc
				colorName = changer;
				 
				}
		else
		{
			throw new NullParamException("Not a valid color");

		}
	
	 }
	 
	 
	 protected double getAngle()
	 {

		    return angle;
	}
	 
	
	 protected void setAngle(double val)
	 {
		        angle = val;
		     
	 }
	 
	 
	 protected double getSpeed()
	 {
		 
		return speed; 
	 }
	 
	 protected void setSpeed(double val) throws NullParamException
	 {
		 if (val >= 0){
		 speed = val;
		 }
		 else
		 {
			 throw new NullParamException("Speed not valid");
		 }
	 }
	
	 protected double getMaxStrength()
		{
			return maxStrength;
		}
		
		protected void setMaxStrength(double val) throws NullParamException
		{
			if (val <= 0)
			{
				throw new NullParamException("MaxStrength cannot be negative or zero"); 
			}
			maxStrength = val;
		}
		
		protected double getCurrentStrength()
		{
			return currentStrength;
		}
		
		protected void setCurrentStrength(double val) throws NullParamException
		{
			if (val > 50000)
			{
				throw new NullParamException("Current Strength must be greater than or equal to 0");
			}
			else{
			currentStrength = val; 
			}
		}
		
		protected void addToCurrentStrength(double val) throws NullParamException
		{
			if (val > 50000)
			{
				throw new NullParamException("Current Strength must be greater than or equal to 0");
			}
			else{
				currentStrength += val; 
			}
			
		}
		
		
		
		protected String getID()
		{
			return id;
		}
		
		protected synchronized void setID(String x) throws NullParamException
		{
			if (x == null)
			{
				throw new NullParamException("ID not unique, try another");
			}
			else {
				id = x;
			}
		}
		
		
		
		
		
	
	
		public class NullParamException extends Exception{
			
			public NullParamException(String msg)
			{
				super(msg);
			}
			
		}
	
	
	
}
