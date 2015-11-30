//Garrett Slough

package space;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import display.ConsoleItemImpl;
import display.ViewManager;
import utils.ColorMaker;
import utils.Point3D;
import utils.PolygonPlus;
import utils.SoundUtility;

public class FighterShip extends Ship implements Runnable
{
	private int numMissiles;
	private double radarRange;
	
	
	Set<String> targeted = new HashSet<String>();
	
	Map<String, String> missilesToTargets = new HashMap<String, String>();
	
	private final double MISSILE_SPEED = 10.0;
	//private final double NUKE_SPEED = 4.5;

	
	
	
	
	public FighterShip(String id, String colorName, Point3D location, Point3D destination, 
			double maxStrength, double speed, int numMissiles, double radarRange) throws NullParamException
	{
		super(id, colorName, location, destination, maxStrength, speed);
		
		setNumMissiles(numMissiles);
		setRadarRange(radarRange);
		
		//Color Switcher
	    cName = colorName;
	    Color aColor = ColorMaker.makeColor(colorName); // “YELLOW”, “RED”, etc

	    
	    //Port Checker
	  	isPort = false;
	  	
		displayName = "Fighter";
		
		isMissle = false;
		
		isFighter = true;
		
		isNuke = false;

		
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
			return;
		}
		while (this.isDestroyed() == false)
		{
			try {
				move(1);
			} catch (NullParamException | space.DebrisCloud.NullParamException e20) {
	            System.out.println(e20.getMessage());
				e20.printStackTrace();
			}
			
			ViewManager.getInstance().updateItem(
					new ConsoleItemImpl(id, location, getColor(), 
							angle, shape, getInfoText(), isDestroyed(), isDamaged()));
			
			updateMissiles();
			try {
				scanForTargets();
			} catch (NullParamException | space.DebrisCloud.NullParamException e25) {
	            System.out.println(e25.getMessage());
				e25.printStackTrace();
			}
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e21) {
	            System.out.println(e21.getMessage());
				e21.printStackTrace();
			}
			
		}
		
	}
	
	public void updateMissiles()
	{
		if (targeted.isEmpty())
		{
			return;
		}
		
		 ArrayList<String> tmpList;
		 synchronized (missilesToTargets) {
		 tmpList = new ArrayList<>(missilesToTargets.keySet());
		 } 
		 
		 for (String s : tmpList)
		 {
			 if (SpaceController.isDestroyed(s))
			 {
				 targeted.remove(missilesToTargets.get(s));
				 missilesToTargets.remove(s);
			 }
		 }
		
		
	}
	
	
	public void scanForTargets() throws NullParamException, space.DebrisCloud.NullParamException
	{
		if (numMissiles <= 0)
		{
			return;
		}
		
		ArrayList<String> targets = SpaceController.performRadarSweep(
									id, location, cName, radarRange);
		
		for (String tName : targets)
		{
			if (targets.isEmpty())
			{
				return;
			}
			
			if(targeted.contains(tName))
			{
				//return
			}
			else
			{
				Point3D pT = SpaceController.getLocation(tName);
				
				if (pT == null)
				{
					//return
				}
				else
				{
					
					if (getLocation().distance(pT) <= 50.0)
					{
						//return
					}
					else
					{
						targeted.add(tName);
						String uniqueId = UUID.randomUUID().toString();
						
						missilesToTargets.put(uniqueId, tName);
						SoundUtility.getInstance().playSound(
								"sounds" + File.separator + "Launch.wav");
						
						pT = SpaceController.getLocation(tName);
						
						if (pT == null)
						{
							//return
						}
						
						else 
						{
							new Missile(uniqueId,cName,location,pT, 1.0, MISSILE_SPEED,
									tName,30.0,100,500.0,radarRange);
							
							numMissiles--;
							
							SpaceController.setRadarLock(tName, getLocation());
							
						}
						
					}
					
				}
				
			}
			
			
		
		}
	}
		
		
		
	
	
	
	public void reactToRadarLock(Point3D p)
	{
		
	}
	
	public int getNumMissiles()
	{
		return numMissiles;
	}
	
	public void setNumMissiles(int val) throws NullParamException
	{
		if (val < 0)
		{
			throw new NullParamException("Missles cannot be negative");
		}
		
		numMissiles = val;
	}
	
	public double getRadarRange()
	{
		return radarRange;
	}
	
	public void setRadarRange(double val) throws NullParamException
	{
		if (val < 0)
		{
			throw new NullParamException("Cannot be a negative Radar range");
		}
		
		radarRange = val;
	}
	
	public Map<String, String> getMissilesToTargets()
	{
		return missilesToTargets;
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
				+ System.getProperty("line.separator") +  "Radar Range:		" + getRadarRange()
				+ System.getProperty("line.separator") +  "Num Missles:		" + getNumMissiles()
				+ System.getProperty("line.separator") +  "Missles in Flight:		" + getMissilesToTargets();


		
		return s;
	}
	
	
	
	
	
	
	{
		ArrayList<Point> sp = new ArrayList<>();
		sp.add(new Point(+0, -16));
		sp.add(new Point(+4, -8));
		sp.add(new Point(+10, -12));
		sp.add(new Point(+16, +8));
		sp.add(new Point(+8, +0));
		sp.add(new Point(+6, +2));
		sp.add(new Point(+0, +25));
		sp.add(new Point(-6, +2));
		sp.add(new Point(-8, +0));
		sp.add(new Point(-16, +8));
		sp.add(new Point(-10, -12));
		sp.add(new Point(-4, -8));
		
		shape = new PolygonPlus();
		for (Point sp1 : sp) {
		shape.addPoint(sp1.x, sp1.y);
		} 
		
		
	}
	
	
	
	
	
	
	
	
}
