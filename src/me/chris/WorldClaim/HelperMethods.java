package me.chris.WorldClaim;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class HelperMethods
{
	public static ArrayList<BlockVector2D> getPlayersLocalChunk(Player p)
	{
		ArrayList<BlockVector2D> list = new ArrayList<BlockVector2D>();
		
		double xMin = round(p.getLocation().getX() - 16, 16);
		
		double xMax = round(p.getLocation().getX(), 16) - 1;
		
		double zMin = round(p.getLocation().getZ() - 16, 16);
		
		double zMax = round(p.getLocation().getZ(), 16) - 1;
		
		list.add(new BlockVector2D(xMin, zMin));
		list.add(new BlockVector2D(xMax, zMin));
		list.add(new BlockVector2D(xMax, zMax));
		list.add(new BlockVector2D(xMin, zMax));
		
		return list;
	}
	
	public static double round(double num, int multipleOf)
	{
		return Math.round((num + multipleOf / 2) / multipleOf) * multipleOf;
	}
	
	public static void spawnParticles(Player p, List<BlockVector2D> list)
	{
		Player[] ps = { p };
		double y = p.getLocation().getY();
		for (int index = 0; index < list.size(); index++)
		{
			BlockVector2D currentPoint = list.get(index);
			BlockVector2D nextPoint;
			
			if ((index + 1) > (list.size() - 1))
			{
				nextPoint = list.get(0);
			}
			else
			{
				nextPoint = list.get(index + 1);
			}
			
			if (currentPoint.getX() == nextPoint.getX())
			{
				if (currentPoint.getZ() > nextPoint.getZ())
				{
					BlockVector2D cp = currentPoint;
					currentPoint = nextPoint;
					nextPoint = cp;
					
				}
				
				double z = currentPoint.getZ();
				do
				{
					ParticleEffect.REDSTONE.display(0.01f, 0.01f, 0.01f, 0, 1, new Location(p.getWorld(), currentPoint.getX() + .5, y, z + .5), ps);
					ParticleEffect.REDSTONE.display(0.01f, 0.01f, 0.01f, 0, 1, new Location(p.getWorld(), currentPoint.getX() + .5, y + 1, z + .5), ps);
					ParticleEffect.REDSTONE.display(0.01f, 0.01f, 0.01f, 0, 1, new Location(p.getWorld(), currentPoint.getX() + .5, y + 2, z + .5), ps);
					z = z + .5;
					
				}
				while (z < nextPoint.getZ());
			}
			else if (currentPoint.getZ() == nextPoint.getZ())
			{
				if (currentPoint.getX() > nextPoint.getX())
				{
					BlockVector2D cp = currentPoint;
					currentPoint = nextPoint;
					nextPoint = cp;
					
				}
				
				double x = currentPoint.getX();
				do
				{
					ParticleEffect.REDSTONE.display(0.01f, 0.01f, 0.01f, 0, 1, new Location(p.getWorld(), x + .5, y, currentPoint.getZ() + .5), ps);
					ParticleEffect.REDSTONE.display(0.01f, 0.01f, 0.01f, 0, 1, new Location(p.getWorld(), x + .5, y + 1, currentPoint.getZ() + .5), ps);
					ParticleEffect.REDSTONE.display(0.01f, 0.01f, 0.01f, 0, 1, new Location(p.getWorld(), x + .5, y + 2, currentPoint.getZ() + .5), ps);
					x = x + .5;
					
				}
				while (x < nextPoint.getX());
			}
			else
			{
				break;
			}
		}
		
		
	}
	
	public static ArrayList<String> checkForNeighboringRegion(Player p, ProtectedPolygonalRegion r)
	{
		RegionManager RM = Vars.WGP.getRegionManager(p.getWorld());
		
		ProtectedCuboidRegion northRegion = new ProtectedCuboidRegion("NorthRegion", r.getMinimumPoint().add(0, 0, -16).toBlockPoint(), r.getMaximumPoint().add(0, 0, -16).toBlockPoint());
		ProtectedCuboidRegion eastRegion = new ProtectedCuboidRegion("EastRegion", r.getMinimumPoint().add(16, 0, 0).toBlockPoint(), r.getMaximumPoint().add(16, 0, 0).toBlockPoint());
		ProtectedCuboidRegion southRegion = new ProtectedCuboidRegion("SouthRegion", r.getMinimumPoint().add(0, 0, 16).toBlockPoint(), r.getMaximumPoint().add(0, 0, 16).toBlockPoint());
		ProtectedCuboidRegion westRegion = new ProtectedCuboidRegion("WestRegion", r.getMinimumPoint().add(-16, 0, 0).toBlockPoint(), r.getMaximumPoint().add(-16, 0, 0).toBlockPoint());
		
		ArrayList<String> directions = new ArrayList<String>();
		boolean found = false;
		
		ApplicableRegionSet setNorth = RM.getApplicableRegions(northRegion);
		if (setNorth.size() != 0)
		{
			for (ProtectedRegion proR : setNorth)
			{
				if (proR.getId().equalsIgnoreCase("WorldClaim-" + p.getName()))
				{
					found = true;
					directions.add("north");
					break;
				}
			}
		}
		
		ApplicableRegionSet setEast = RM.getApplicableRegions(eastRegion);
		if (setEast.size() != 0)
		{
			for (ProtectedRegion proR : setEast)
			{
				if (proR.getId().equalsIgnoreCase("WorldClaim-" + p.getName()))
				{
					found = true;
					directions.add("east");
					break;
				}
			}
		}
		
		ApplicableRegionSet setSouth = RM.getApplicableRegions(southRegion);
		if (setSouth.size() != 0)
		{
			for (ProtectedRegion proR : setSouth)
			{
				if (proR.getId().equalsIgnoreCase("WorldClaim-" + p.getName()))
				{
					found = true;
					directions.add("south");
					break;
				}
			}
		}
		
		ApplicableRegionSet setWest = RM.getApplicableRegions(westRegion);
		if (setWest.size() != 0)
		{
			for (ProtectedRegion proR : setWest)
			{
				if (proR.getId().equalsIgnoreCase("WorldClaim-" + p.getName()))
				{
					found = true;
					directions.add("west");
					break;
				}
			}
		}
		
		if (found)
		{
			directions.add(0, "true");
			return directions;
		}
		else
		{
			directions.add(0, "false");
			return directions;
		}
	}
	
	public static void removeInventoryItems(Inventory inv, Material type, int amount)
	{
		int amountNeedToRemove = amount;
		
		int counter = 0;
		for (ItemStack is : inv.getContents())
		{
			if (is != null && is.getType() == type)
			{
				int stackAmount = is.getAmount();
				
				// Stack isnt big enough, or stack exactly equals the amount we need to remove.
				if (stackAmount <= amountNeedToRemove)
				{
					inv.setItem(counter, new ItemStack(Material.AIR));
					amountNeedToRemove = amountNeedToRemove - stackAmount;
				}
				// Stack is too big
				else
				{
					is.setAmount(stackAmount - amountNeedToRemove);
					amountNeedToRemove = 0;
				}
				
				if (amountNeedToRemove == 0)
				{
					break;
				}
			}
			
			counter++;
		}
	}
	
	public static ArrayList<BlockVector2D> combineTwoRegions(Player p, ArrayList<String> direction, ProtectedPolygonalRegion r1, ProtectedPolygonalRegion r2)
	{
		for (String s : direction)
		{
			if (s.equalsIgnoreCase("north"))
			{
				BlockVector min = r2.getMinimumPoint();
				BlockVector max = r2.getMaximumPoint();
				
				ArrayList<BlockVector2D> list = new ArrayList<BlockVector2D>();
				list.add(new BlockVector2D(min.getBlockX(), min.getBlockZ() - 1));
				list.add(new BlockVector2D(max.getBlockX(), min.getBlockZ() - 1));
				list.add(new BlockVector2D(max.getBlockX(), max.getBlockZ()));
				list.add(new BlockVector2D(min.getBlockX(), max.getBlockZ()));
				
				r2 = new ProtectedPolygonalRegion(r2.getId(), list, 0, 256);
				
			}
			else if (s.equalsIgnoreCase("east"))
			{
				BlockVector min = r2.getMinimumPoint();
				BlockVector max = r2.getMaximumPoint();
				
				ArrayList<BlockVector2D> list = new ArrayList<BlockVector2D>();
				list.add(new BlockVector2D(min.getBlockX(), min.getBlockZ()));
				list.add(new BlockVector2D(max.getBlockX() + 1, min.getBlockZ()));
				list.add(new BlockVector2D(max.getBlockX() + 1, max.getBlockZ()));
				list.add(new BlockVector2D(min.getBlockX(), max.getBlockZ()));
				
				r2 = new ProtectedPolygonalRegion(r2.getId(), list, 0, 256);
			}
			else if (s.equalsIgnoreCase("south"))
			{
				BlockVector min = r2.getMinimumPoint();
				BlockVector max = r2.getMaximumPoint();
				
				ArrayList<BlockVector2D> list = new ArrayList<BlockVector2D>();
				list.add(new BlockVector2D(min.getBlockX(), min.getBlockZ()));
				list.add(new BlockVector2D(max.getBlockX(), min.getBlockZ()));
				list.add(new BlockVector2D(max.getBlockX(), max.getBlockZ() + 1));
				list.add(new BlockVector2D(min.getBlockX(), max.getBlockZ() + 1));
				
				r2 = new ProtectedPolygonalRegion(r2.getId(), list, 0, 256);
			}
			else if (s.equalsIgnoreCase("west"))
			{
				BlockVector min = r2.getMinimumPoint();
				BlockVector max = r2.getMaximumPoint();
				
				ArrayList<BlockVector2D> list = new ArrayList<BlockVector2D>();
				list.add(new BlockVector2D(min.getBlockX() - 1, min.getBlockZ()));
				list.add(new BlockVector2D(max.getBlockX(), min.getBlockZ()));
				list.add(new BlockVector2D(max.getBlockX(), max.getBlockZ()));
				list.add(new BlockVector2D(min.getBlockX() - 1, max.getBlockZ()));
				
				r2 = new ProtectedPolygonalRegion(r2.getId(), list, 0, 256);
			}
			else
			{
				Vars.log.log(Level.SEVERE, "[WorldClaim] UNEXPECTED INTERNAL ERROR.");
				p.sendMessage("§a[WorldClaim] §cUnexpected internal error.");
				p.removeMetadata("WorldClaim-NewClaim", Vars.plugin);
				p.removeMetadata("WorldClaim-AdditionToExistingClaim", Vars.plugin);
				p.removeMetadata("WorldClaim-Direction", Vars.plugin);
				return null;
			}
		}
		
		//List<BlockVector2D> allPoints = r1.getPoints();
		
		ArrayList<BlockVector2D> allPoints = new ArrayList<BlockVector2D>(r1.getPoints());
		
		for (BlockVector2D d : r2.getPoints())
		{
			if (allPoints.contains(d))
			{
				allPoints.remove(d);
			}
			else
			{
				allPoints.add(d);
			}
		}
		
		ArrayList<BlockVector2D> finalList = null;
		for (int index = 0; index < allPoints.size(); index++)
		{
			ArrayList<BlockVector2D> newList = new ArrayList<BlockVector2D>();
			ArrayList<BlockVector2D> remainingPoints = new ArrayList<BlockVector2D>();
			remainingPoints.addAll(allPoints);
			
			BlockVector2D startingPoint = remainingPoints.get(index);
			remainingPoints.remove(startingPoint);
			
			newList.add(startingPoint);
			
			finalList = mapPoint(startingPoint, startingPoint, "", remainingPoints, newList);
			
			if (finalList != null)
			{
				break;
			}
		}
		
		return finalList;
		
	}
	
	public static ArrayList<BlockVector2D> mapPoint(BlockVector2D startingPoint, BlockVector2D currentPoint, String previousDirection, List<BlockVector2D> remainingPoints,
			ArrayList<BlockVector2D> newList)
	{
		ArrayList<BlockVector2D> possibleMatches = new ArrayList<BlockVector2D>();
		
		for (BlockVector2D point : remainingPoints)
		{
			// Must lie on the same X or Z line as current point.
			if (currentPoint.getX() == point.getX() && currentPoint.getZ() == point.getZ())
			{
				System.out.println("   Exact match to current point found? ");
			}
			else if (currentPoint.getX() == point.getX() || currentPoint.getZ() == point.getZ())
			{
				// Must not go in the same direction as previous segment
				if (previousDirection.equalsIgnoreCase("") || previousDirection.equalsIgnoreCase(computeCardinateDirection(currentPoint, point)) == false)
				{
					possibleMatches.add(point);
					
				}
			}
		}
		
		if (possibleMatches.size() == 0)
		{
			if (remainingPoints.size() == 0)
			{
				if (currentPoint.getX() == startingPoint.getX() || currentPoint.getZ() == startingPoint.getZ())
				{
					// Must not go in the same direction as previous segment
					if (previousDirection.equalsIgnoreCase("") || previousDirection.equalsIgnoreCase(computeCardinateDirection(currentPoint, startingPoint)) == false)
					{
						return newList;
					}
				}
			}
			else
			{
				// The program returned no results even though there are still points remaining in the pool.
				// This indicates that a new starting point is needed because first point is in a tricky position.
				return null;
			}
		}
		else if (possibleMatches.size() == 1)
		{
			newList.add(possibleMatches.get(0));
			remainingPoints.remove(possibleMatches.get(0));
			
			return mapPoint(startingPoint, possibleMatches.get(0), computeCardinateDirection(currentPoint, possibleMatches.get(0)), remainingPoints, newList);
		}
		else if (possibleMatches.size() > 1)
		{
			double smallestDistance = Double.MAX_VALUE - 10.0;
			int indexOfLeastDistance = -1;
			int counter = 0;
			for (BlockVector2D b : possibleMatches)
			{
				double distance = Math.sqrt(((b.getX() - currentPoint.getBlockX()) * (b.getX() - currentPoint.getBlockX()))
						+ ((b.getZ() - currentPoint.getBlockZ()) * (b.getZ() - currentPoint.getBlockZ())));
				
				if (distance < smallestDistance)
				{
					smallestDistance = distance;
					indexOfLeastDistance = counter;
				}
				counter++;
			}
			
			// Match is whichever point is the least distant;
			newList.add(possibleMatches.get(indexOfLeastDistance));
			remainingPoints.remove(possibleMatches.get(indexOfLeastDistance));
			
			return mapPoint(startingPoint, possibleMatches.get(indexOfLeastDistance), computeCardinateDirection(currentPoint, possibleMatches.get(indexOfLeastDistance)), remainingPoints,
					newList);
		}
		
		return null;
	}
	
	public static String computeCardinateDirection(BlockVector2D startingPoint, BlockVector2D endingPoint)
	{
		if (startingPoint.getX() == endingPoint.getX())
		{
			if (startingPoint.getZ() > endingPoint.getZ())
			{
				return "north";
			}
			else
			{
				return "south";
			}
		}
		else if (startingPoint.getZ() == endingPoint.getZ())
		{
			if (startingPoint.getX() > endingPoint.getX())
			{
				return "west";
			}
			else
			{
				return "east";
			}
		}
		else
		{
			return null;
		}
	}
	
	public static boolean canPurchaseAnother(Player p)
	{
		RegionManager RM = Vars.WGP.getRegionManager(p.getWorld());
		
		ProtectedPolygonalRegion r = (ProtectedPolygonalRegion) RM.getRegion("worldclaim-" + p.getName().toLowerCase());
		
		if (calculateChunks(r) < Vars.MaximumChunks)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static int calculateChunks(Player p)
	{
		RegionManager RM = Vars.WGP.getRegionManager(p.getWorld());
		
		ProtectedPolygonalRegion r = (ProtectedPolygonalRegion) RM.getRegion("worldclaim-" + p.getName().toLowerCase());
		
		return (int) calculateChunks(r);
	}
	
	private static double calculateChunks(ProtectedPolygonalRegion r)
	{
		BlockVector max = r.getMaximumPoint().add(1, 0, 1).toBlockVector();
		BlockVector min = r.getMinimumPoint();
		
		int counter = 0;
		
		for (double x = min.getX(); x < max.getX(); x++)
		{
			for (double z = min.getZ(); z < max.getZ(); z++)
			{
				if (r.contains(new BlockVector2D(x, z)))
				{
					counter++;
					continue;
				}
			}
		}
		
		if (counter != 0)
		{
			return counter / 256.0;
		}
		else
		{
			return 0;
		}
	}
}
