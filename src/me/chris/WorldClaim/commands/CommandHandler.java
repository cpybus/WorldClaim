package me.chris.WorldClaim.commands;

import me.chris.WorldClaim.Vars;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Chris
 */
public class CommandHandler implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command cmd, String idk, String[] args)
	{
		if (!(sender instanceof Player))
		{
			return true;
		}
		
		Player p = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("WorldClaim") || cmd.getName().equalsIgnoreCase("wc"))
		{
			if (args.length == 0)
			{
				p.sendMessage("§5=====================================================");
				p.sendMessage("§a Welcome to §cWorldClaim §aPlugin §9(Version " + Vars.versionNumber + ")");
				p.sendMessage("§a Designed and Programmed by §9Hotshot2162");
				p.sendMessage("§5=====================================================");
			}
			else if (args.length == 1)
			{
				if (args[0].equalsIgnoreCase("check"))
				{
					if (Vars.perms.has(p, "WorldClaim.Check"))
					{
						Command_Check.check(p);
					}
					else
					{
						p.sendMessage("§a[WorldClaim] §cYou do not have permission");
					}
				}
				else if (args[0].equalsIgnoreCase("claim"))
				{
					if (Vars.perms.has(p, "WorldClaim.Claim"))
					{
						Command_Claim.claim(p);
					}
					else
					{
						p.sendMessage("§a[WorldClaim] §cYou do not have permission");
					}
				}
				else if (args[0].equalsIgnoreCase("confirm"))
				{
					if (Vars.perms.has(p, "WorldClaim.Confirm"))
					{
						Command_Confirm.confirm(p);
					}
					else
					{
						p.sendMessage("§a[WorldClaim] §cYou do not have permission");
					}
				}
				else if (args[0].equalsIgnoreCase("map"))
				{
					if (Vars.perms.has(p, "WorldClaim.Map"))
					{
						Command_Map.map(p);
					}
					else
					{
						p.sendMessage("§a[WorldClaim] §cYou do not have permission");
					}
				}
				else if (args[0].equalsIgnoreCase("unclaim"))
				{
					if (Vars.perms.has(p, "WorldClaim.Unclaim"))
					{
						Command_UnClaim.unclaim(p);
					}
					else
					{
						p.sendMessage("§a[WorldClaim] §cYou do not have permission");
					}
				}
				else if (args[0].equalsIgnoreCase("view"))
				{
					if (Vars.perms.has(p, "WorldClaim.View"))
					{
						Command_View.view(p);
					}
					else
					{
						p.sendMessage("§a[WorldClaim] §cYou do not have permission");
					}
				}
				else if (args[0].equalsIgnoreCase("help"))
				{
					if (Vars.perms.has(p, "WorldClaim.help"))
					{
						Command_Help.help(p);
					}
					else
					{
						p.sendMessage("§a[WorldClaim] §cYou do not have permission");
					}
				}
				else
				{
					p.sendMessage("§a[WorldClaim] §cInvalid command.");
				}
			}
			else if(args.length == 2)
			{
				if (args[0].equalsIgnoreCase("view"))
				{
					if (Vars.perms.has(p, "WorldClaim.View"))
					{
						Command_View.view(p, args[1]);
					}
					else
					{
						p.sendMessage("§a[WorldClaim] §cYou do not have permission");
					}
				}
				else if (args[0].equalsIgnoreCase("addmember"))
				{
					if (Vars.perms.has(p, "WorldClaim.AddMember"))
					{
						Command_AddMember.addmember(p, args[1]);
					}
					else
					{
						p.sendMessage("§a[WorldClaim] §cYou do not have permission");
					}
				}
				else
				{
					p.sendMessage("§a[WorldClaim] §cInvalid command.");
				}
			}
			else if(args.length >= 3)
			{
				if (args[0].equalsIgnoreCase("addmember"))
				{
					if (Vars.perms.has(p, "WorldClaim.AddMember"))
					{
						Command_AddMember.addmember(p, args);
					}
					else
					{
						p.sendMessage("§a[WorldClaim] §cYou do not have permission");
					}
				}
				else
				{
					p.sendMessage("§a[WorldClaim] §cInvalid command.");
				}
			}
			else
			{
				p.sendMessage("§a[WorldClaim] §cInvalid command.");
			}
		}
		return true;
	}
}
