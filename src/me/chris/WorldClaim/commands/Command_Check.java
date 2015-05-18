package me.chris.WorldClaim.commands;

import java.util.ArrayList;
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

public class Command_Check
{
	public static void check(Player p)
	{
		RegionManager RM = Vars.WGP.getRegionManager(p.getWorld());
		
		ArrayList<BlockVector2D> list = HelperMethods.getPlayersLocalChunk(p);
		
		Vars.particles.add(new ParticleObject(p, list, 5));
		
		
		if (list == null)
		{
			p.sendMessage("§a[WorldClaim] §cInvalid selection.");
			return;
		}
		
		ProtectedPolygonalRegion r = new ProtectedPolygonalRegion(UUID.randomUUID().toString(), list, 0, 256);
		ApplicableRegionSet set = RM.getApplicableRegions(r);
		
		if (set.size() == 0)
		{
			p.sendMessage("§a[WorldClaim] §cThis chunk does not belong to anyone.");
		}
		else
		{
			String owners = "";
			for (ProtectedRegion protectedRegion : set)
			{
				for (String s1 : protectedRegion.getOwners().getPlayers())
				{
					owners = owners + s1 + ", ";
				}
				
			}
			
			if (owners.length() != 0)
			{
				owners = owners.substring(0, owners.length() - 2);
				p.sendMessage("§a[WorldClaim] §cThis chunk belongs to " + owners + ".");
			}
			else
			{
				p.sendMessage("§a[WorldClaim] §cThis chunk is claimed.");
			}
		}
	}
}
