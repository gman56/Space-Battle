//Garrett Slough

package space;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import display.ConsoleItemImpl;
import display.ViewManager;
import utils.ColorMaker;
import utils.Point3D;
import utils.PolygonPlus;
import utils.SoundUtility;

public class SpacePort extends Ship implements Runnable
{

	private final double angle = 0;
	private int numShots;
	private int shotDelay;
	
	
	public SpacePort (String id, String colorName, Point3D location, Point3D destination, 
					  double maxStrength, double speed, int numShots) throws space.CargoShip.NullParamException, NullParamException 
	{
		super(id, colorName, location, destination, maxStrength, speed);
		//Color Switcher
	    cName = colorName;
	    
	    //Port Checker
	  	isPort = true;
	  	
		displayName = "SpacePort";
		
		isMissle = false;
		
		isFighter = false;
		
		isNuke = false;


	    Color aColor = ColorMaker.makeColor(colorName); // “YELLOW”, “RED”, etc
		setShotsRemaining(numShots);

		SpaceController.addship(this);
		
		ViewManager.getInstance().updateItem(
				new ConsoleItemImpl(id, location, aColor,angle, shape, getInfoText(), isDestroyed(), isDamaged()));
		
		new Thread(this).start();
		
		
		
		
	}
	
	
	
	public void run()
	{
		if (this.isDestroyed())
		{
			throw new IllegalArgumentException("Ship is Destroyed");
		}
		while(this.isDestroyed() == false)
		{
			
			if (shotDelay > 0)
			{
				shotDelay--; 
			}
			
					
					try {
						this.move(1);
					} catch (NullParamException
							| space.DebrisCloud.NullParamException e12) {
			            System.out.println(e12.getMessage());
						e12.printStackTrace();
					}
					
				
				if (getCurrentStrength() != getMaxStrength())
				{
					double newStrength;
					double x = getCurrentStrength();
					
					newStrength = x + 0.2;
					
					try {
						
						setCurrentStrength(newStrength);
						
					} catch (space.CargoShip.NullParamException e) {
			            System.out.println(e.getMessage());
						e.printStackTrace();
					}
					
				}
			
			
			ViewManager.getInstance().updateItem(
					new ConsoleItemImpl(getID(), getLocation(), colorName, 
							angle, shape, getInfoText(), isDestroyed(), isDamaged()));
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		
		}
		
		
	}
	
	
	
	public String getInfoText() 
	{
		
		String s = "Color:" + cName 
				+ System.getProperty("line.separator") 
				+ System.getProperty("line.separator") +"Location:" + getLocation() 
				+ System.getProperty("line.separator") +"Destination:" + getDestination() 
				+ System.getProperty("line.separator")+ "Speed:" + getSpeed() 
				+ System.getProperty("line.separator") + "Angle:" + getAngle() 
				+ System.getProperty("line.separator") 
				+ System.getProperty("line.separator") + "Strength: " + getCurrentStrength() 
				+ System.getProperty("line.separator") + "Max Strength:" + getMaxStrength() 
				+ System.getProperty("line.separator") +  "Damaged:	" + isDamaged() 
				+ System.getProperty("line.separator") +  "Shots:	" + getShotsRemaining();

		
		return s;
		
	}
	
	private int getShotDelay()
	{
		return shotDelay;
	}
	
	private void setShotDelay(int val) throws NullParamException
	{
		if (shotDelay < 0)
		{
			throw new NullParamException("Shot Delay cannot be less than 0");
		}
		else
		{
			shotDelay = val;
		}
	}
	
	
	
	private int getShotsRemaining()
	{
		return numShots; 
	}
	 
	private void setShotsRemaining(int val) throws NullParamException
	{
		if (val < 0)
		{
			throw new NullParamException("Railshots must be greater than or equal to 0");
		}
		else 
		{
			numShots = val;
		}
	}
	
	private void addShots(int val) throws NullParamException
	{
		if (val < 0)
		{
			throw new NullParamException("Railshots must be greater than or equal to 0");
		}
		else 
		{
			numShots += val;
		}
	}
	
	public void applyDamage(double val) throws space.DebrisCloud.NullParamException 
	, NullParamException, space.CargoShip.NullParamException
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
				SpaceController.processDetonation(getID(), getLocation(), 100, 400); 
					
				 new DebrisCloud("Ship Debris Cloud from " + getID(), getLocation(), cName , 4000, false, 3.0);  
				 SoundUtility.getInstance().playSound("sounds" + File.separator + "Blast.wav");
				 return;
					
			}
				
		}
	
	public void reactToRadarLock(Point3D loc) throws NullParamException, space.DebrisCloud.NullParamException
	{
		int x = 0;
		

		if (getShotsRemaining() <= 0)
		{
			return;
		}
		if (getShotDelay() > 0)
		{
			return;
		}
		else
		{
			for (int i=1; i<=10; i++)
			{
				addShots(10);
			
			SoundUtility.getInstance().playSound("sounds" + File.separator + "Rail.wav");
			double xCoord = (loc.getX())  * (0.975 +(Math.random() * 0.05));
			double yCoord = (loc.getY())  * (0.975 +(Math.random() * 0.05));
			double zCoord = (loc.getZ())  * (0.975 +(Math.random() * 0.05));
			
			Point3D newP = new Point3D(xCoord, yCoord, zCoord);
			
			x++;
			String uniqueId = Integer.toString(x);
			
			SpaceController.processDetonation("Rail gun shot "+ uniqueId, newP, 50, 30.0);

			
			new DebrisCloud("Rail Shot " + numShots+ "from " + getID(),newP, cName, 3000, true, 0.25);
			
			numShots--;
		}
			
			setShotDelay(50);

		}
	}
	
	
	protected void resetStrength() throws NullParamException 
	{
		setCurrentStrength(getMaxStrength()); 
	}
		
		
		
		
	//Initializer
	{	
	ArrayList<Point> sp = new ArrayList<>();
	sp.add(new Point(0, -20));
	sp.add(new Point(6, -6));
	sp.add(new Point(20, 0));
	sp.add(new Point(6, 6));
	sp.add(new Point(0, 20));
	sp.add(new Point(-6, 6));
	sp.add(new Point(-20, 0));
	sp.add(new Point(-6, -6));
	shape = new PolygonPlus();
	for (Point sp1 : sp) {
	shape.addPoint(sp1.x, sp1.y);
	} 
	}
	
}

		
	




	

