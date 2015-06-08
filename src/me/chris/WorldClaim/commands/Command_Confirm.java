package me.chris.WorldClaim.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import me.chris.WorldClaim.CommandParking;
import me.chris.WorldClaim.CommandType;
import me.chris.WorldClaim.HelperMethods;
import me.chris.WorldClaim.ParticleObject;
import me.chris.WorldClaim.Vars;

import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;

public class Command_Confirm
{
	public static void confirm(Player p)
	{
		
		// Check if player is on the pending list (the list where the player already has a claim in the world
		if (!Vars.awaitingConfirmation.containsKey(p))
		{
			p.sendMessage("§a[WorldClaim] §cThere was no pending purchase for you.");
			return;
		}
		
		CommandParking cp = Vars.awaitingConfirmation.get(p);
		
		if(cp.t == CommandType.CLAIM_FIRSTCHUNK)
		{
			firstChunkBuy(p, cp.list, cp.costAmount);
		}
		else if(cp.t == CommandType.CLAIM_NOTFIRSTCHUNK)
		{
			notFirstChunkBuy(p, cp.directions, cp.list, cp.costAmount);
		}
		else if(cp.t == CommandType.UNCLAIM_LASTCHUNK)
		{
			unclaimLastChunk(p, cp.list);
		}
		else if(cp.t == CommandType.UNCLAIM_NOTLASTCHUNK)
		{
			unclaimChunk(p, cp.directions, cp.list);
		}
		else
		{
			
		}
		
		Vars.awaitingConfirmation.remove(p);
		
	}

	private static void firstChunkBuy(Player p, ArrayList<BlockVector2D> list, int costAmount)
	{
		RegionManager RM = Vars.WGP.getRegionManager(p.getWorld());
		
		if (finances(p, false, costAmount) == false)
		{
			p.sendMessage("§a[WorldClaim] §cYou do not have the necessary payment.");
			return;
		}
		
		ProtectedPolygonalRegion NewRegion = new ProtectedPolygonalRegion("worldclaim-" + p.getName().toLowerCase(), list, 0, 256);
		NewRegion.getOwners().addPlayer(p.getUniqueId());
		
		try
		{
			RM.addRegion(NewRegion);
			RM.save();
		}
		catch (StorageException e)
		{
			Vars.log.log(Level.SEVERE, "[WorldClaim] There was an unexpected internal error. Details below.");
			p.sendMessage("§a[WorldClaim] §cUnexpected internal error. You will not be charged.");
			e.printStackTrace();
			return;
		}
		
		Vars.particles.add(new ParticleObject(p, NewRegion.getPoints(), 10));
		finances(p, true, costAmount);
		p.sendMessage("§a[WorldClaim] §cYou have claimed this chunk. Please keep in mind that all claims henceforth have to be connected to this one.");
	}
	
	private static void notFirstChunkBuy(Player p, ArrayList<String> directions, ArrayList<BlockVector2D> list, int costAmount)
	{
		RegionManager RM = Vars.WGP.getRegionManager(p.getWorld());
		
		
		
		if (finances(p, false, costAmount) == false)
		{
			p.sendMessage("§a[WorldClaim] §cYou do not have the necessary payment.");
			return;
		}
		
		ProtectedPolygonalRegion ActualOldRegion = ((ProtectedPolygonalRegion) RM.getRegion("WorldClaim-" + p.getName()));
		
		ProtectedPolygonalRegion NewRegion = new ProtectedPolygonalRegion(UUID.randomUUID().toString(), list, 0, 256);
		ProtectedPolygonalRegion OldRegion = new ProtectedPolygonalRegion(UUID.randomUUID().toString(), ActualOldRegion.getPoints(), 0, 256);
		
		List<BlockVector2D> newRegionPoints = HelperMethods.combineTwoRegions(p, directions, OldRegion, NewRegion);
		
		if (list == null)
		{
			Vars.log.log(Level.SEVERE, "[WorldClaim] Player " + p.getName() + " attempted to claim a chunk, but the plugin was unable to form a correct polygon.");
			p.sendMessage("§a[WorldClaim] §cPlugin was unable to group together your region and the selected chunk. Please try a different arrangement. Please note that you are not allowed to box in an unclaimed chunk.");
			try
			{
				RM.load();
			}
			catch (StorageException e)
			{
				e.printStackTrace();
			}
			
			return;
		}
		
		ProtectedPolygonalRegion CombinedRegion = new ProtectedPolygonalRegion("worldclaim-" + p.getName().toLowerCase(), newRegionPoints, 0, 256);
		
		for(String s : ActualOldRegion.getOwners().getPlayers())
			CombinedRegion.getOwners().addPlayer(s);
		
		for(UUID u : ActualOldRegion.getOwners().getUniqueIds())
			CombinedRegion.getOwners().addPlayer(u);
		
		for(String s : ActualOldRegion.getMembers().getPlayers())
			CombinedRegion.getMembers().addPlayer(s);
		
		for(UUID u : ActualOldRegion.getMembers().getUniqueIds())
			CombinedRegion.getMembers().addPlayer(u);
		
		CombinedRegion.getFlags().putAll(ActualOldRegion.getFlags());
		
		Vars.particles.add(new ParticleObject(p, newRegionPoints, 50));
		
		try
		{
			RM.addRegion(CombinedRegion);
			RM.save();
		}
		catch (StorageException e)
		{
			Vars.log.log(Level.SEVERE, "[WorldClaim] There was an unexpected internal error. Details below.");
			p.sendMessage("§a[WorldClaim] §cUnexpected internal error. You will not be charged.");
			e.printStackTrace();
			return;
		}
		
		finances(p, true, costAmount);
		p.sendMessage("§a[WorldClaim] §cYou have claimed this chunk, and it has been connected to any neighboring claims you own.");
	}
	
	
	private static boolean finances(Player p, boolean pay, int costAmount)
	{
		if (pay)
		{
			if (Vars.Currency) // Uses Items for transaction
			{
				HelperMethods.removeInventoryItems(p.getInventory(), Vars.Item, costAmount);
			}
			else
			// Uses Econ for transaction
			{
				Vars.eco.withdrawPlayer(Vars.plugin.getServer().getOfflinePlayer(p.getUniqueId()), costAmount);
			}
			return false;
		}
		else
		{
			if (Vars.Currency) // Uses Items for transaction
			{
				if (p.getInventory().contains(Vars.Item, costAmount))
				{
					return true;
					
				}
				else
				{
					return false;
				}
			}
			else
			// Uses Econ for transaction
			{
				
				if (Vars.eco.has(Vars.plugin.getServer().getOfflinePlayer(p.getUniqueId()), costAmount))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	private static void unclaimLastChunk(Player p, ArrayList<BlockVector2D> list)
	{
		RegionManager RM = Vars.WGP.getRegionManager(p.getWorld());
		
		RM.removeRegion("WorldClaim-" + p.getName());
		
		try
		{
			RM.save();
		}
		catch (StorageException e)
		{
			Vars.log.log(Level.SEVERE, "[WorldClaim] There was an unexpected internal error. Details below.");
			p.sendMessage("§a[WorldClaim] §cUnexpected internal error.");
			e.printStackTrace();
			return;
		}
		
		p.sendMessage("§a[WorldClaim] §cYour last claimed chunk in this world has been removed");
	}
	
	
	
	
	
	private static void unclaimChunk(Player p, ArrayList<String> directions, ArrayList<BlockVector2D> list)
	{
		RegionManager RM = Vars.WGP.getRegionManager(p.getWorld());
		
		ProtectedPolygonalRegion ActualOldRegion = ((ProtectedPolygonalRegion) RM.getRegion("WorldClaim-" + p.getName()));
		
		ProtectedPolygonalRegion ChunkToRemove = new ProtectedPolygonalRegion(UUID.randomUUID().toString(), list, 0, 256);
		ProtectedPolygonalRegion OldRegion = new ProtectedPolygonalRegion(UUID.randomUUID().toString(), ActualOldRegion.getPoints(), 0, 256);
		
		List<BlockVector2D> newRegionPoints = HelperMethods.combineTwoRegions(p, directions, OldRegion, ChunkToRemove);
		
		if (list == null)
		{
			Vars.log.log(Level.SEVERE, "[WorldClaim] Player " + p.getName() + " attempted to claim a chunk, but the plugin was unable to form a correct polygon.");
			p.sendMessage("§a[WorldClaim] §cPlugin was unable to group together your region and the selected chunk. Please try a different arrangement. Please note that you are not allowed to box in an unclaimed chunk.");
			try
			{
				RM.load();
			}
			catch (StorageException e)
			{
				e.printStackTrace();
			}
			
			return;
		}
		
		ProtectedPolygonalRegion CombinedRegion = new ProtectedPolygonalRegion("worldclaim-" + p.getName().toLowerCase(), newRegionPoints, 0, 256);
		
		for(String s : ActualOldRegion.getOwners().getPlayers())
			CombinedRegion.getOwners().addPlayer(s);
		
		for(UUID u : ActualOldRegion.getOwners().getUniqueIds())
			CombinedRegion.getOwners().addPlayer(u);
		
		for(String s : ActualOldRegion.getMembers().getPlayers())
			CombinedRegion.getMembers().addPlayer(s);
		
		for(UUID u : ActualOldRegion.getMembers().getUniqueIds())
			CombinedRegion.getMembers().addPlayer(u);
		
		CombinedRegion.getFlags().putAll(ActualOldRegion.getFlags());
		
		Vars.particles.add(new ParticleObject(p, newRegionPoints, 50));
		
		try
		{
			RM.addRegion(CombinedRegion);
			RM.save();
		}
		catch (StorageException e)
		{
			Vars.log.log(Level.SEVERE, "[WorldClaim] There was an unexpected internal error. Details below.");
			p.sendMessage("§a[WorldClaim] §cUnexpected internal error. You will not be charged.");
			e.printStackTrace();
			return;
		}
		
		p.sendMessage("§a[WorldClaim] §cYou have unclaimed this chunk.");
	}
}
