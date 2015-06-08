package me.chris.WorldClaim.commands;

import me.chris.WorldClaim.Vars;

import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Command_AddMember
{
	public static void addmember(Player p, String s)
	{
		RegionManager RM = Vars.WGP.getRegionManager(p.getWorld());
		
		ProtectedRegion ActualOldRegion = RM.getRegion("worldclaim-" + p.getName());
		
		if(ActualOldRegion == null)
		{
			p.sendMessage("§a[WorldClaim] §cNo claim found.");
			return;
		}
		
		ActualOldRegion.getMembers().addPlayer(s.toLowerCase());
		
		try
		{
			RM.save();
		}
		catch (StorageException e)
		{
			
		}
		
		p.sendMessage("§a[WorldClaim] §cPlayer(s) added.");
	}
	
	public static void addmember(Player p, String[] s)
	{
		RegionManager RM = Vars.WGP.getRegionManager(p.getWorld());
		
		ProtectedRegion ActualOldRegion = RM.getRegion("worldclaim-" + p.getName());
		
		if(ActualOldRegion == null)
		{
			p.sendMessage("§a[WorldClaim] §cNo claim found.");
			return;
		}
		
		//Start at 1 because index=0 is "addmember"
		for(int index = 1; index < s.length; index++)
		{
			ActualOldRegion.getOwners().addPlayer(s[index].toLowerCase());
		}
		
		try
		{
			RM.save();
		}
		catch (StorageException e)
		{
			
		}
		
		p.sendMessage("§a[WorldClaim] §cPlayer(s) added.");
	}
}
