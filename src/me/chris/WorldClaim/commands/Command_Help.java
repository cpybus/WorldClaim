package me.chris.WorldClaim.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Command_Help
{
	public static void help(Player p)
	{
		
		p.sendMessage("§5==================§c [ WorldClaim Help ] §5==================");
		p.sendMessage("§c/WorldClaim §e- States the general info.");
		p.sendMessage("§c/WorldClaim §7help §e- Brings up the help menu.");
		p.sendMessage("§c/WorldClaim §7claim §e- Claims a chunk. ");
		p.sendMessage("§c/WorldClaim §7unclaim §e- Unclaims a chunk.");
		p.sendMessage("§c/WorldClaim §7map §e- Views a map of the area.");
		p.sendMessage("§c/WorldClaim §7view [playername] §e- Allows you to see a claim. ");
		p.sendMessage("§c/WorldClaim §7addmember [name] §e- Adds a member to your claim ");
		p.sendMessage("§c/WorldClaim §7check §e- Checks your current chunk. ");
		p.sendMessage("§c/WorldClaim §7confirm §e- Will confirm a previous action.");
		p.sendMessage("§3NOTE: You may substitue /wc for /worldclaim.");
		
		/*
		new FancyMessage("§c/WorldClaim §e- States the general info.")
			.tooltip("This command does nothing but state the \nauthor and the current version")
			.suggest("/WorldClaim")
			.send(p);
		new FancyMessage("§c/WorldClaim §7help §e- Brings up the help menu.")
			.tooltip("This will bring up the help menu")
			.suggest("/WorldClaim help")
			.send(p);
		new FancyMessage("§c/WorldClaim §7claim §e- Claims a chunk. ")
			.tooltip("This command allows a player to claim a chunk in exchange for \nmoney or items. Please note that all chunks claimed have to \nbe connected, therefore whereever you caim your first \nchunk is the area that all your claims will be.\n However, you can always unclaim, but you will not \nbe compensated.")
			.suggest("/WorldClaim claim")
			.send(p);
		new FancyMessage("§c/WorldClaim §7unclaim §e- Unclaims a chunk.")
			.tooltip("This will unclaim the chunk you're standing \non. If it is the last chunk, you claim will be \ndeleted. You will not be compensated. ")
			.suggest("/WorldClaim unclaim")
			.send(p);
		new FancyMessage("§c/WorldClaim §7map §e- Views a map of the area.")
			.tooltip("Will bring up a map of the area. This command \nis resource intensive, so you may only use it \nonce every 5 minutes..")
			.suggest("/WorldClaim map")
			.send(p);
		new FancyMessage("§c/WorldClaim §7view [playername] §e- Allows you to see a claim. ")
			.tooltip("Will spawn particles at eye level around the outline of \nyour claim. If a playername isnt specified, it will \nuse the claim you're standing on")
			.suggest("/WorldClaim view")
			.send(p);
		new FancyMessage("§c/WorldClaim §7addmember [name] §e- Adds a member to your claim ")
			.tooltip("Will add a member to you claim. You can have \nmultiple players listed. Just seperate \nwith spaces.")
			.suggest("/WorldClaim view")
			.send(p);
		new FancyMessage("§c/WorldClaim §7check §e- Checks your current chunk. ")
			.tooltip("Will check your current chunk for ownership.")
			.suggest("/WorldClaim view")
			.send(p);
		new FancyMessage("§c/WorldClaim §7confirm §e- Will confirm a previous action.")
			.tooltip("Simply confirms that you \nwant to do something")
			.suggest("/WorldClaim view")
			.send(p);
		new FancyMessage("NOTE: You may substitue /wc for /worldclaim.").color(ChatColor.DARK_AQUA).style(ChatColor.ITALIC)
			.send(p);*/
	}
}
