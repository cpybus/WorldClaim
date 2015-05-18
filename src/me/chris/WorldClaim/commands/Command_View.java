package me.chris.WorldClaim.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.chris.WorldClaim.HelperMethods;
import me.chris.WorldClaim.ParticleObject;
import me.chris.WorldClaim.Vars;

import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Command_View
{
	//View the claim standing on
	public static void view(Player p)
	{
		RegionManager RM = Vars.WGP.getRegionManager(p.getWorld());
		
		ArrayList<BlockVector2D> list = HelperMethods.getPlayersLocalChunk(p);
		ProtectedPolygonalRegion r = new ProtectedPolygonalRegion(UUID.randomUUID().toString(), list, 0, 256);
		
		ApplicableRegionSet set = RM.getApplicableRegions(r);
		
		if(set.size() == 0)
		{
			p.sendMessage("§a[WorldClaim] §cNo claim found.");
			return;
		}
		else
		{
			boolean wc = false;
			
			for (ProtectedRegion proR : set)
			{
				if (proR.getId().startsWith("worldclaim-"))
				{
					// Another player's claim
					
					Vars.particles.add(new ParticleObject(p, proR.getPoints(), 50));
					p.sendMessage("§a[WorldClaim] §cThe outline of this claimed region is now visible.");
					
					wc = true;
					break;
				}
			}
			
			if (wc == false)
			{
				p.sendMessage("§a[WorldClaim] §cNo claim found.");
				return;
			}
			
		}		
		
	}
	
	//View specific claim
	public static void view(Player p, String s)
	{
		RegionManager RM = Vars.WGP.getRegionManager(p.getWorld());
		
		ProtectedRegion ActualOldRegion = RM.getRegion("worldclaim-" + s);
		
		if(ActualOldRegion == null)
		{
			p.sendMessage("§a[WorldClaim] §cNo claim found.");
			return;
		}
		
		List<BlockVector2D> points = ActualOldRegion.getPoints();
		
		Vars.particles.add(new ParticleObject(p, points, 50));
		p.sendMessage("§a[WorldClaim] §cThe outline of that claim is now visible.");
	}
}
