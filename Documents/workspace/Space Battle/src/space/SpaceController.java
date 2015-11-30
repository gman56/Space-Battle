//Garrett Slough

package space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import display.ViewManager;
import space.Ship.NullParamException;
import utils.Point3D;

public class SpaceController {
	
	public final static int xSize = 800;
	public final static int ySize = 900; 
	public final static int zSize = 700;
	
	static Map<String, Ship> activeShips = new ConcurrentHashMap<String, Ship>();
	

	
	static Map<String, DebrisCloud> activeClouds = new ConcurrentHashMap<String, DebrisCloud>();
	
	


	public synchronized static void addship(Ship ship) {
		
			activeShips.put(ship.getID(), ship);
	
		}

	public synchronized static void removeShip(String key) {
		activeShips.remove(key);
	}
	
	public synchronized static void addCloud(DebrisCloud cloud)
	{
		activeClouds.put(cloud.getcloudID(), cloud);
		
	}
	public synchronized static void removeCloud(String key)
	{
		activeClouds.remove(key);
		
	}
	
	public static String reacquireRadarLock(String idIn, Point3D p3D, String targetIn, 
									String colorNameIn, double rangeIn)
	{
		if (Math.random() < 0.9)
		{
			return targetIn;
		}
		
		List<String> list = performRadarSweep(idIn, p3D, colorNameIn, rangeIn);
		
		list.remove(idIn);
		
		if (list.isEmpty() == true)
		{
			return null;
		}
		
		int idx = (int)(Math.random() * list.size());
		
		return list.get(idx);
		
		
		
		
	}
	
	public static ArrayList<String> performRadarSweep(String idIn, Point3D sourceIn, String colorNameIn, double rangeIn)
	{
		ArrayList<String> results = new ArrayList<String>();
		
		for (Ship s : activeShips.values())
		{ 

			if (s.isMissle() == false)
			{
				if(s.getID() != idIn && s.getColorName() != colorNameIn && sourceIn.distance(s.getLocation()) < rangeIn )
				{
					results.add(s.getID());
				}
			}
	
		}
		
		for (DebrisCloud c : activeClouds.values())
		{
			if (sourceIn.distance(c.getloc()) < rangeIn)
			{
				results.add(c.getcloudID());
			}
			
		}
		
		return results;
		
		
	}
	
	
	
	
	
	
	public static Object setRadarLock(String identifierIn, Point3D pIn) throws NullParamException, space.DebrisCloud.NullParamException
	{
		for (Ship s : activeShips.values())
		{ 
			if (identifierIn != s.getID())
			{
				return null;
			}
			else if (identifierIn == s.getID()) 
			{
				s.reactToRadarLock(pIn);
			}
		}
		return null;
	}
	
	
	public static boolean fightersRemain()
	{
		if (activeShips.isEmpty() == true)
		{
			return false;
		}
		else
		{
			for (Ship ship : activeShips.values())
			{
				if (ship.isFighter() == true)
				{
					return true;
				}
				else
				{
				}
			}
		}
		return false;
	}
	
	public static boolean isDestroyed(String id)
	{
		if (activeShips.containsKey(id))
		{
			Ship ship = activeShips.get(id);
			boolean isDestroy = ship.isDestroyed();
			return isDestroy;
			
		}
		else
		{
			return true; 
		}
		
	}
	
	
	public static void processDetonation(String id, Point3D location, double detonationRange, 
										double damageMax) throws NullParamException, space.DebrisCloud.NullParamException 
	{
		
		
		
		for (Ship ship : activeShips.values())
		{
			
			if (ship == null) 
			{
				return;
			}
			
			else 
				{
					Point3D shipLoc = ship.getLocation();
					double d = shipLoc.distance(location);
					
					//if (d > detonationRange)
					//{
						
					//}
					//else
					 if (d <= detonationRange )
					{
						if (ship.getID() == id || ship.isDestroyed())
						{
							//return;
						}
						
						else if (ship.getID() != id && !ship.isDestroyed())
						{
							if ( d > 0.0)
							{
								double damagePercent = (detonationRange - d) / detonationRange;
								double damage = (0.5 * damageMax) + (Math.random() * 0.5 * damageMax);
								double calcDamage = (damage * damagePercent);
								
								ship.applyDamage(calcDamage);
								return;
							}
							else if (d == 0.0)
							{
								double damagePercent = 1.0;
								double damage = (0.5 * damageMax) + (Math.random() * 0.5 * damageMax);
								double calcDamage = (damage * damagePercent);				
								
								ship.applyDamage(calcDamage);
								return;
							}
						}
						
					}
					
				}
		}
	
	}
	
	public static Point3D makePoint()
	{
		int xCoord = (int) (Math.random() * xSize);
		int yCoord = (int) (Math.random() * ySize);
		int zCoord = (int) (Math.random() * zSize);
		return new Point3D(xCoord, yCoord, zCoord);
	}
	
	public static Point3D makePointNear(Point3D somePoint, double percent)
	{
		int xVar = (int) (SpaceController.xSize * percent);
		int yVar = (int) (SpaceController.ySize * percent);
		int zVar = (int) (SpaceController.zSize * percent);
		
		int xCoord = (int) (somePoint.getX() * (1-(percent/2))) + (int) (Math.random() * xVar);
		int yCoord = (int) (somePoint.getY() * (1-(percent/2))) + (int) (Math.random() * yVar);
		int zCoord = (int) (somePoint.getZ() * (1-(percent/2))) + (int) (Math.random() * zVar);
				
		return new Point3D(xCoord, yCoord, zCoord);
	}
	
	
	public static Point3D getLocation(String id)
	{
		
		if (activeShips.containsKey(id))
		{
			
			
				Ship ship = activeShips.get(id);
				String x = ship.getID();

				
				if (x == id)
				{
					return ship.getLocation();
				}
				else
				{
					return null;
				}
			}
			
			else if (activeClouds.containsKey(id))
			{
				for (DebrisCloud clouds : activeClouds.values())
				{
					
					if (clouds.getcloudID() == id)
					{
						return clouds.getloc();
					}
					else
					{
						return null;
					}
				}
			
		}
		return null;
	}
		
		
		
		

	
	public static String getNearestSpacePort(Point3D loc, String cName)
	{
			
		
		Map<String, Double> leastDistanceMap = new HashMap<String, Double>();
		
		for (Ship ship : activeShips.values())
		{
			boolean isPort = ship.isSpacePort();
			String rightColor = ship.getColorName();
			if (isPort == true && rightColor == cName)
			{
				Point3D shipLoc = ship.getLocation();
				String shipID = ship.getID();
				double d = shipLoc.distance(loc);				
				leastDistanceMap.put(shipID, d);
					
			}
				
		}
		
		String str = "My string";
		Double min =Double.valueOf(Double.POSITIVE_INFINITY );
		for(Map.Entry<String,Double> e:leastDistanceMap.entrySet()){
		    if(min.compareTo(e.getValue())>0){
		        str=e.getKey();
		        min=e.getValue();
		    }
		}
		return str; //Returns smallest value's key from leastDistanceMap
	}
	
		
	
	
	public static HashMap<String, HashMap<String, Integer>> getElementCounts()
	{
		
		
		HashMap<String, HashMap<String, Integer>> counts = new HashMap<>();
		for (Ship ship : activeShips.values())
		{
			

			String cName = ship.getColorName();

			if (!counts.containsKey(cName))
			{
				counts.put(cName, new HashMap<String,Integer>());
				HashMap<String, Integer> hm = counts.get(cName);
				String type = ship.getDisplayName();
				
				if (!hm.containsKey(type))
				{
						hm.put(type, 0);
						int x = hm.get(type);
						x++;
						hm.put(type, x);
					}
			
			}
		}
		return counts;
			
		
	}
	
	
	static class InfoThread implements Runnable 
	{
		@Override
		public void run() 
		{
			long start = System.currentTimeMillis();
			while (true) 
			{
				long loopStart = System.currentTimeMillis();
				HashMap<String, HashMap<String, Integer>> counts = getElementCounts();
				StringBuilder sb = new StringBuilder();
				ArrayList<String> ar = new ArrayList<>(counts.keySet());
				Collections.sort(ar);
				for (String side : ar) 
				{
					if (sb.length() != 0) 
					{
						sb.append(" ");
					}
					sb.append(side).append(": ");
					HashMap<String, Integer> hm = counts.get(side);
					ArrayList<String> k = new ArrayList<>(hm.keySet());
					Collections.sort(k);
					for (String field : k) {
						sb.append(field).append(": ").append(hm.get(field)).append(" ");
					}
				}
				long timePassed = System.currentTimeMillis() - start;
				String timeString = makeTimeString(timePassed);
				ViewManager.getInstance().updateInfo("[" + timeString + "] " + sb.toString());
				try {
					Thread.sleep(1000 - (loopStart - System.currentTimeMillis()));
				} catch (InterruptedException ex) {
					System.out.println(ex.getMessage());
				}
			} // end while
		} // end run 
		
		private String makeTimeString(long num)
		{
			String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(num),
				    TimeUnit.MILLISECONDS.toMinutes(num) % TimeUnit.HOURS.toMinutes(1),
				    TimeUnit.MILLISECONDS.toSeconds(num) % TimeUnit.MINUTES.toSeconds(1));
			
			return hms;
		}
		
		
	}
	
	
	
	
	static {
		
		InfoThread x = new InfoThread();
		
		new Thread(x).start();
		
		
		
		
		
	}
	
	
	
}


	


