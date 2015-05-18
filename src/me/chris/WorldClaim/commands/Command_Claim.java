package me.chris.WorldClaim.commands;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

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

public class Command_Claim
{
	public static void claim(Player p)
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
		
		if (set.size() != 0)
		{
			returnOwners(p, set);
			return;
		}
		
		if (RM.getRegion("worldclaim-" + p.getName().toLowerCase()) == null) // First Chunk
		{
			firstChunkBuy(p, list);
		}
		else // Not first chunk
		{
			notFirstChunkBuy(p, r, list);
		}
		
	}
	
	// Private methods from here on
	
	private static void returnOwners(Player p, ApplicableRegionSet set)
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
	
	private static void firstChunkBuy(Player p, ArrayList<BlockVector2D> list)
	{
		int costAmount = 0;
		
		try
		{
			Vars.interpreter.eval("cost = " + Vars.CostEquation.replaceAll("x", "1"));
			costAmount = (int) Double.parseDouble(Vars.interpreter.get("cost").toString());
		}
		catch (Throwable t)
		{
			Vars.log.log(Level.SEVERE, "[WorldClaim] A player attempted a transaction and the CostEquation string could not be parsed properly.");
			Vars.log.log(Level.SEVERE, "[WorldClaim] Shutting down plugin.");
			p.sendMessage("§a[WorldClaim] §cInternal Error.");
			Vars.plugin.getServer().getPluginManager().disablePlugin(Vars.plugin);
			return;
		}
		
		if (Vars.Currency) // Uses Items for transaction
		{
			if (p.getInventory().contains(Vars.Item, costAmount))
			{
				p.sendMessage("§a[WorldClaim] §cAre you sure you want to purchase this 1 chunk for " + costAmount + " " + Vars.Item.toString().toLowerCase()
						+ "? Do \"/wc confirm\" to confirm your purchase.");
				
				Vars.particles.add(new ParticleObject(p, list, 5));				
				Vars.awaitingConfirmation.put(p, new CommandParking(p, CommandType.CLAIM_FIRSTCHUNK, list, costAmount));
				
			}
			else
			{
				p.sendMessage("§a[WorldClaim] §cYou do not have " + costAmount + " " + Vars.Item.toString().toLowerCase() + " in your inventory.");
			}
		}
		else
		// Uses Econ for transaction
		{
			
			if (Vars.eco.has(Vars.plugin.getServer().getOfflinePlayer(p.getUniqueId()), costAmount))
			{
				p.sendMessage("§a[WorldClaim] §cAre you sure you want to purchase this 1 chunk for " + costAmount + " " + Vars.eco.currencyNamePlural()
						+ "? Do \"/wc confirm\" to confirm your purchase.");
				
				Vars.particles.add(new ParticleObject(p, list, 5));
				Vars.awaitingConfirmation.put(p, new CommandParking(p, CommandType.CLAIM_FIRSTCHUNK, list, costAmount));
				
			}
			else
			{
				p.sendMessage("§a[WorldClaim] §cYou do not have " + costAmount + " " + Vars.eco.currencyNamePlural() + ".");
			}
		}
		
	}
	
	private static void notFirstChunkBuy(Player p, ProtectedPolygonalRegion r, ArrayList<BlockVector2D> list)
	{
		ArrayList<String> o = HelperMethods.checkForNeighboringRegion(p, r);
		int currentChunks = HelperMethods.calculateChunks(p);
		
		if (!o.get(0).equalsIgnoreCase("true"))
		{
			p.sendMessage("§a[WorldClaim] §cAny claim after the first must be connected to the first claim.");
			return;
		}
		
		o.remove(0);
		if (HelperMethods.canPurchaseAnother(p) == false)
		{
			p.sendMessage("§a[WorldClaim] §cYou have reached the maximum number of claimable chunks.");
			
			return;
		}
		
		int costAmount = 0;
		
		try
		{
			Vars.interpreter.eval("cost = " + Vars.CostEquation.replaceAll("x", ""+(currentChunks+1)));
			costAmount = (int) Double.parseDouble(Vars.interpreter.get("cost").toString());
			
		}
		catch (Throwable t)
		{
			Vars.log.log(Level.SEVERE, "[WorldClaim] A player attempted a transaction and the CostEquation string could not be parsed properly.");
			Vars.log.log(Level.SEVERE, "[WorldClaim] Shutting down plugin.");
			p.sendMessage("§a[WorldClaim] §cInternal Error.");
			Vars.plugin.getServer().getPluginManager().disablePlugin(Vars.plugin);
			return;
		}
		
		if (Vars.Currency) // Uses Items for transaction
		{
			if (p.getInventory().contains(Vars.Item, costAmount))
			{
				p.sendMessage("§a[WorldClaim] §cAre you sure you want to purchase this 1 chunk for " + costAmount + " " + Vars.Item.toString().toLowerCase()
						+ "? Do \"/wc confirm\" to confirm your purchase.");
				
				Vars.particles.add(new ParticleObject(p, list, 5));				
				Vars.awaitingConfirmation.put(p, new CommandParking(p, CommandType.CLAIM_NOTFIRSTCHUNK, o, list, costAmount));
				
			}
			else
			{
				p.sendMessage("§a[WorldClaim] §cYou do not have " + costAmount + " " + Vars.Item.toString().toLowerCase() + " in your inventory.");
			}
		}
		else
		// Uses Econ for transaction
		{
			
			if (Vars.eco.has(Vars.plugin.getServer().getOfflinePlayer(p.getUniqueId()), costAmount))
			{
				p.sendMessage("§a[WorldClaim] §cAre you sure you want to purchase this 1 chunk for " + costAmount + " " + Vars.eco.currencyNamePlural()
						+ "? Do \"/wc confirm\" to confirm your purchase.");
				
				Vars.particles.add(new ParticleObject(p, list, 5));
				Vars.awaitingConfirmation.put(p, new CommandParking(p, CommandType.CLAIM_NOTFIRSTCHUNK, o, list, costAmount));
				
			}
			else
			{
				p.sendMessage("§a[WorldClaim] §cYou do not have " + costAmount + " " + Vars.eco.currencyNamePlural() + ".");
			}
		}
		
	}
	
}
