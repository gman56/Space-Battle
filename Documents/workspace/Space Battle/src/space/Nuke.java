/*package space;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

import display.ConsoleItemImpl;
import display.ViewManager;
import space.Ship.NullParamException;
import utils.ColorMaker;
import utils.Point3D;
import utils.PolygonPlus;
import utils.SoundUtility;

public class Nuke extends Ship implements Runnable {
	
	private String target;
	private double detonationRange;
	private int flightDuration;
	private double maxDamage;
	private double radarRange;

	public Nuke(String id, String colorName, Point3D location, Point3D destination, 
			double maxStrength, double speed, String target, double detonationRange,
			int flightDuration, double maxDamage, double radarRange) throws NullParamException
	{
		super(id, colorName, location, destination, maxStrength, speed);

		setTarget(target);
		setDetonationRange(detonationRange);
		setFlightDuration(flightDuration);
		setMaxDamage(maxDamage);
		setRadarRange(radarRange);
		
		
		//Color Switcher
	    cName = colorName;
	    Color aColor = ColorMaker.makeColor(colorName); // “YELLOW”, “RED”, etc

	    
	    //Port Checker
	  	isPort = false;
	  	
		displayName = "Nuke";
		
		isMissle = false;
		
		isFighter = false;
		
		isNuke = true;
		
		
		SpaceController.addship(this);

		ViewManager.getInstance().updateItem(
				new ConsoleItemImpl(id, location, aColor, 
						angle, shape, getInfoText(), isDestroyed(), isDamaged()));
		
		new Thread(this).start();
		 
		
	
	}
	
	@Override
	public void run()
	{
		if (this.isDestroyed())
		{
			throw new IllegalArgumentException("Ship is Destroyed");

		}
		while(this.isDestroyed() == false)
		{
			String newTarget = SpaceController.reacquireNukeRadarLock(id, location, target, cName, radarRange);
			Point3D p = SpaceController.getLocation(newTarget);
			
			if (p != null)
			{
				try 
				{
					setTarget(newTarget);
					
				} catch (NullParamException e4) {
		            System.out.println(e4.getMessage());
					e4.printStackTrace();
				}
					try 
					{
						setDestination(p);
						
					} catch (NullParamException e5) {
			            System.out.println(e5.getMessage());
						e5.printStackTrace();
					}
			}
			
			else if (p == null)
			{
				
				
			}
			
			try {
				this.move(1);
			} catch (NullParamException | space.DebrisCloud.NullParamException e10) {
	            System.out.println(e10.getMessage());
				e10.printStackTrace();
			}
			
			
			if (this.isDestroyed() == true)
			{
				//return
			}
			else if (this.isDestroyed() == false)
			{
				
				ViewManager.getInstance().updateItem(
						new ConsoleItemImpl(id, location, getColor(), 
								angle, shape, getInfoText(), isDestroyed(), isDamaged()));
				
				try {
					Thread.sleep(55);
				} catch (InterruptedException e7) {
		            System.out.println(e7.getMessage());
					e7.printStackTrace();
				}

				
			}
		
			
			
			
			
		}
		
	}
	
	public void move(int cycles) throws NullParamException, space.DebrisCloud.NullParamException
	{
		flightDuration--;
		
		if (flightDuration == 0)
		{
			detonate();
			return;
		}
		
		Point3D p = SpaceController.getLocation(target);
		
		if (p == null)
		{
			detonate();
			return;
		}
		
		setDestination(p);
		
		Point3D locOld = getLocation(); 
		double distanceTraveled = (getSpeed()*cycles);
		
		double distanceToDest = getLocation().distance(getDestination());
		
		if (distanceTraveled >= distanceToDest)
		{
			setLocation(getDestination());
			detonate();
			return;
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
	
	public void applyDamage(double val) throws NullParamException, space.DebrisCloud.NullParamException
	{
		double strength;
		strength = getCurrentStrength() - val;
		setCurrentStrength(strength);
		
		if (strength <= 0)
		{
			detonate();
		}
		
	}
	
	public void detonate() throws NullParamException, space.DebrisCloud.NullParamException
	{
		setCurrentStrength(0);
		SpaceController.removeShip(id);
		ViewManager.getInstance().removeItem(id);
		
		new DebrisCloud("Ship Debris Cloud from " + getID(), getLocation(), cName , 2000, false, 0.5);  
		SoundUtility.getInstance().playSound("sounds" + File.separator + "MissileBlast.wav");		
	
		SpaceController.processDetonation(id, location, detonationRange, maxDamage);
	}
	
	
	public String getInfoText() 
	{

	
		
		String s = "Color:" + cName + System.getProperty("line.separator") 
				+ System.getProperty("line.separator") +"Location:		" + getLocation() 
				+ System.getProperty("line.separator") +"Destination:	" + getDestination() 
				+ System.getProperty("line.separator")+ "Speed:		" + getSpeed() 
				+ System.getProperty("line.separator") + "Angle:	" + getAngle() 
				+ System.getProperty("line.separator") 
				+ System.getProperty("line.separator") + "Strength:		" + getCurrentStrength() 
				+ System.getProperty("line.separator") + "Max Strength:		" + getMaxStrength() 
				+ System.getProperty("line.separator") +  "Damaged:		" + isDamaged()
				+ System.getProperty("line.separator") 
				+ System.getProperty("line.separator") + "Target:	" + getTarget() 
				+ System.getProperty("line.separator") + "Duration:		" + getFlightDuration() 
				+ System.getProperty("line.separator") +  "Detonation Range:	" + getDetonationRange()
				+ System.getProperty("line.separator") +  "Radar Range:		" + getRadarRange()
				+ System.getProperty("line.separator") +  "Max Damage:		" + getMaxDamage();

		
		return s;
		
				
		
	}
	
	private int getFlightDuration()
	{
		return flightDuration;
	}
	
	private void setFlightDuration(int val) throws NullParamException
	{
		if (val < 0 )
		{
			throw new NullParamException("Flight Duration cannot be negative");
		}
		else
		{
			flightDuration = val;
		}
	}
	private double getRadarRange()
	{
		return radarRange;
	}
	
	private void setRadarRange(double val) throws NullParamException
	{
		if (val < 0 )
		{
			throw new NullParamException("Radar Range cannot be negative");
		}
		else
		{
			radarRange = val;
		}
	}
	
	private double getMaxDamage()
	{
		return maxDamage;
	}
	
	private void setMaxDamage(double val) throws NullParamException
	{
		if (val < 0 )
		{
			throw new NullParamException("Max Damage cannot be negative");
		}
		else
		{
			maxDamage = val;
		}
	}
	private double getDetonationRange()
	{
		return detonationRange;
	}
	
	private void setDetonationRange(double val) throws NullParamException
	{
		if (val < 0 )
		{
			throw new NullParamException("Detonation Range cannot be negative");
		}
		else
		{
			detonationRange = val;
		}
	}
	private String getTarget()
	{
		return target;
	}
	
	private void setTarget(String s) throws NullParamException
	{
		if (s != null)
		{
			target = s;
		}
		else {
			throw new NullParamException("Target cannot be null");
		}
	}
	
	
	
	
	{
		ArrayList<Point> sp = new ArrayList<>();
		sp.add(new Point(+0, -10));   // The Point here is java.awt.Point
        sp.add(new Point(+2, +16));  // The Point here is java.awt.Point
        sp.add(new Point(+0, +10));   // The Point here is java.awt.Point
        sp.add(new Point(-12, +6));  // The Point here is java.awt.Point
		
		shape = new PolygonPlus();
		for (Point sp1 : sp) {
		shape.addPoint(sp1.x, sp1.y);
		} 
		
		
	}
	
	
}
*/