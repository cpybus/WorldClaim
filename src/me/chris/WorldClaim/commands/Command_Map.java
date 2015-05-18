package me.chris.WorldClaim.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import me.chris.WorldClaim.HelperMethods;
import me.chris.WorldClaim.Vars;

import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Command_Map
{
	public static void map(Player p)
	{
		long currentTime = new Date().getTime();
		
		if (Vars.waitList.containsKey(p))
		{
			Long lastUse = Vars.waitList.get(p);
			
			if ((currentTime - lastUse) < 300000)
			{
				if (Vars.perms.has(p, "WorldClaim.WaitListOverride"))
				{
					Vars.waitList.put(p, currentTime);
				}
				else
				{
					p.sendMessage("§a[WorldClaim] §cYou can only use this command once every five minutes.");
					return;
				}
			}
			else
			{
				Vars.waitList.put(p, currentTime);
			}
		}
		else
		{
			Vars.waitList.put(p, currentTime);
		}
		
		ArrayList<BlockVector2D> list = HelperMethods.getPlayersLocalChunk(p);
		
		p.sendMessage("§5=====================================================");
		p.sendMessage(compass(p, 1) + "  " + map(p, list, -3) + "  " + legend(1));
		p.sendMessage(compass(p, 2) + "  " + map(p, list, -2) + "  " + legend(2));
		p.sendMessage(compass(p, 3) + "  " + map(p, list, -1) + "  " + legend(3));
		p.sendMessage(compass(p, 4) + "  " + map(p, list, 0) + "  " + legend(4));
		p.sendMessage(compass(p, 5) + "  " + map(p, list, 1) + "  " + legend(5));
		p.sendMessage(compass(p, 6) + "  " + map(p, list, 2) + "  " + legend(6));
		p.sendMessage(compass(p, 7) + "  " + map(p, list, 3) + "  " + legend(7));
		p.sendMessage("§5=====================================================");
	}
	
	private static String legend(int i)
	{
		if (i == 1)
			return "§8- §7= Unclaimed";
		else if (i == 2)
			return "§a+ §7= Your";
		else if (i == 3)
			return "     §7Claims";
		else if (i == 4)
			return "§6+ §7= Others'";
		else if (i == 5)
			return "     §7Claims";
		else if (i == 6)
			return "§9+ §7= WG Regions";
		else
			return "";
		
	}
	
	private static String map(Player p, ArrayList<BlockVector2D> list, int i)
	{
		RegionManager RM = Vars.WGP.getRegionManager(p.getWorld());
		String line = "";
		
		ArrayList<BlockVector2D> templist = new ArrayList<BlockVector2D>();
		
		for (BlockVector2D d : list)
		{
			templist.add(d.add((15 * -16), (i * 16)).toBlockVector2D());
		}
		
		for (int index = 0; index < 32; index++)
		{
			ProtectedPolygonalRegion r = new ProtectedPolygonalRegion(UUID.randomUUID().toString(), templist, 0, 256);
			
			ApplicableRegionSet set = RM.getApplicableRegions(r);
			if (set.size() == 0)
			{
				line = line + "§8-";
			}
			else
			{
				boolean wc = false;
				
				for (ProtectedRegion proR : set)
				{
					if (proR.getId().equalsIgnoreCase("WorldClaim-" + p.getName()))
					{
						// Your claim
						line = line + "§a+";
						wc = true;
						break;
					}
					else if (proR.getId().startsWith("worldclaim-"))
					{
						// Another player's claim
						line = line + "§6+";
						wc = true;
						break;
					}
				}
				
				if (wc == false)
				{
					line = line + "§9+";
				}
				
			}
			
			ArrayList<BlockVector2D> temp2list = new ArrayList<BlockVector2D>();
			for (BlockVector2D d : templist)
			{
				temp2list.add(d.add(16, 0).toBlockVector2D());
			}
			templist = temp2list;
			
		}
		
		return line;
	}
	
	private static String compass(Player p, int i)
	{
		if (i == 1 || i == 2 || i > 5)
		{
			return "  §0-----";
		}
		else if (i == 3)
		{
			
			float yaw = Math.abs(p.getLocation().getYaw());
			
			if (yaw > 112.5 && yaw < 157.5) // 135-180
			{
				return "  §0-§6\\§fN§0--";
			}
			else if (yaw > 157.5 && yaw < 202.5) // 180-225
			{
				return "  §0--§6N§0--";
			}
			else if (yaw > 202.5 && yaw < 247.5) // 225-270
			{
				return "  §0--§fN§6/§0-";
			}
			else
			{
				return "  §0--§fN§0--";
			}
		}
		else if (i == 4)
		{
			float yaw = Math.abs(p.getLocation().getYaw());
			if (yaw > 67.5 && yaw < 112.5) // 90-135
			{
				// west
				return "  §0-§6W§7+§fE§0-";
			}
			else if (yaw > 247.5 && yaw < 292.5) // 270-315
			{
				return "  §0-§fW§7+§6E§0-";
			}
			else
			{
				return "  §0-§fW§7+§fE§0-";
			}
		}
		else if (i == 5)
		{
			float yaw = Math.abs(p.getLocation().getYaw());
			if ((yaw > 337.5 && yaw < 360) || (yaw > 0 && yaw < 22.5)) // 0-45
			{
				// south
				return "  §0--§6S§0--";
			}
			else if (yaw > 22.5 && yaw < 67.5) // 45-90
			{
				// west
				return "  §0-§6/§fS§0--";
			}
			else if (yaw > 292.5 && yaw < 337.5) // 315-360
			{
				return "  §0--§fS§6\\§0-";
			}
			else
			{
				return "  §0--§fS§0--";
			}
		}
		
		return "";
	}
	
	/*
	float yaw = p.getLocation().getYaw();
	if( (yaw > 337.5 && yaw < 360) || (yaw > 0 && yaw < 22.5)) //0-45
	{
		//south
		return "south";
	}
	else if( yaw > 22.5 && yaw < 67.5) //45-90
	{
		//west
		return "south-west";
	}
	else if( yaw > 67.5 && yaw < 112.5) //90-135
	{
		//west
		return "west";
	}
	else if (yaw > 112.5 && yaw < 157.5) // 135-180
	{
		return "north-west";
	}
	else if( yaw > 157.5 && yaw < 202.5) //180-225
	{
		return "north";
	}
	else if( yaw > 202.5 && yaw < 247.5) //225-270
	{
		return "north-east";
	}
	else if( yaw > 247.5 && yaw < 292.5) //270-315
	{
		return "east";
	}
	else if( yaw > 292.5 && yaw < 337.5) //315-360
	{
		return "south-east";
	}*/
	
}
