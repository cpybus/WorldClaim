package me.chris.WorldClaim.commands;

import java.util.ArrayList;
import java.util.UUID;

import me.chris.WorldClaim.CommandParking;
import me.chris.WorldClaim.CommandType;
import me.chris.WorldClaim.HelperMethods;
import me.chris.WorldClaim.ParticleObject;
import me.chris.WorldClaim.Vars;

import org.bukkit.entity.Player;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Command_UnClaim
{
	public static void unclaim(Player p)
	{
		RegionManager RM = Vars.WGP.getRegionManager(p.getWorld());
		
		ArrayList<BlockVector2D> list = HelperMethods.getPlayersLocalChunk(p);
		
		// Check for valid selection returned
		if (list == null)
		{
			p.sendMessage("§a[WorldClaim] §cInvalid selection.");
			return;
		}
		
		ProtectedPolygonalRegion r = new ProtectedPolygonalRegion(UUID.randomUUID().toString(), list, 0, 256);
		ApplicableRegionSet set = RM.getApplicableRegions(r);
		
		if (set.size() == 0)
		{
			p.sendMessage("§a[WorldClaim] §cThis chunk is not claimed.");
			return;
		}
		
		boolean found = false;
		
		for (ProtectedRegion proR : set)
		{
			if (proR.getId().equalsIgnoreCase("WorldClaim-" + p.getName()))
			{
				found = true;
				break;
			}
		}
		
		if (found == false)
		{
			p.sendMessage("§a[WorldClaim] §cThis chunk is not yours.");
			return;
		}
		
		ArrayList<String> o = HelperMethods.checkForNeighboringRegion(p, r);
		if (o.get(0).equalsIgnoreCase("true")) // Not last chunk. Remove 1 from group
		{
			o.remove(0);
			
			if(o.size() == 4)
			{
				p.sendMessage("§a[WorldClaim] §cYou cannot unclaim a chunk in the center of your claimed chunks.");
				return;
			}
			
			p.sendMessage("§a[WorldClaim] §cAre you sure you want to unclaim this 1 chunk? Do \"/wc confirm\" to confirm. (Note, you will not be compensated).");
			
			Vars.particles.add(new ParticleObject(p, list, 5));
			Vars.awaitingConfirmation.put(p, new CommandParking(p, CommandType.UNCLAIM_NOTLASTCHUNK, o, list));
		}
		else
		// last chunk. delete the region.
		{
			p.sendMessage("§a[WorldClaim] §cThis is your last claimed chunk. If you unclaim it, you will be allowed to setup a claimed region elsewhere in the world. "
					+ "Do \"/wc confirm\" to confirm. (Note, you will not be compensated).");
			
			Vars.particles.add(new ParticleObject(p, list, 5));
			Vars.awaitingConfirmation.put(p, new CommandParking(p, CommandType.UNCLAIM_LASTCHUNK, list));
			
		}
		
	}
}
