package me.chris.WorldClaim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import bsh.Interpreter;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

/**
 * @author Christopher Pybus
 * @date Mar 25, 2012
 * @file SimpleChatVariables.java
 * @package me.chris.SimpleChat
 * 
 * @purpose
 */

public class Vars
{
	public static FileConfiguration					config;
	public static Permission						perms;
	public static Economy							eco;
	public static Logger							log;
	public static WorldClaimMain					plugin;
	
	public static File								configFile;
	
	public static String pluginName;
	public static String versionNumber;
	
	public static String pluginWithVersion;
	
	public static HashMap<Player, CommandParking>	awaitingConfirmation;
	
	public static WorldGuardPlugin					WGP;
	
	public static HashMap<Player, Long>				waitList;
	public static ArrayList<ParticleObject>			particles;
	
	public static Interpreter						interpreter;
	
	// Yaml Variables
	public static int								MaximumChunks;
	public static boolean							Currency;					// true means item, false means money
	public static Material							Item;
	public static String							CostEquation;
	public static boolean							check_for_updates;
	
	public Vars(WorldClaimMain plugin)
	{
		Vars.plugin = plugin;
		log = Logger.getLogger("Minecraft");
		
		pluginName = plugin.getDescription().getName();
		versionNumber = plugin.getDescription().getVersion();
		
		pluginWithVersion = pluginName + " " + versionNumber;
		
		configFile = new File(plugin.getDataFolder(), "config.yml");
		
		config = new YamlConfiguration();
		
		awaitingConfirmation = new HashMap<Player, CommandParking>();
		
		waitList = new HashMap<Player, Long>();
		
		particles = new ArrayList<ParticleObject>();
		
		interpreter = new Interpreter();
		
		try
		{
			WGP = plugin.getWorldGuard();
		}
		catch (Exception e)
		{
			
		}
	}
	
	public static void importVariables()
	{
		int m = config.getInt("MaximumChunks", -2162);
		if (m == -2162)
		{
			m = 25;
			Vars.log.log(Level.SEVERE, "[WorldClaim] The MaximumChunks variable was not found in the YAML");
		}
		
		String a = config.getString("Currency", "-2162");
		if (a.equalsIgnoreCase("-2162"))
		{
			a = "item";
			Vars.log.log(Level.SEVERE, "[WorldClaim] The Currency variable was not found in the YAML");
		}
		
		String b = config.getString("Item", "-2162");
		if (b.equalsIgnoreCase("-2162"))
		{
			b = "Diamond";
			Vars.log.log(Level.SEVERE, "[WorldClaim] The Item variable was not found in the YAML");
		}
		
		String c = config.getString("CostEquation", "-2162");
		if (c.equalsIgnoreCase("-2162"))
		{
			c = "(2.5 * (x * x)) + (2.5 * x) + 10";
			Vars.log.log(Level.SEVERE, "[WorldClaim] The CostEquation variable was not found in the YAML");
		}
		
		String d = config.getString("check_for_updates", "-2162");
		if(d.equalsIgnoreCase("true"))
		{
			check_for_updates = true;
		}
		else if(d.equalsIgnoreCase("false"))
		{
			check_for_updates = false;
		}
		else
		{
			check_for_updates = true;
			Vars.log.log(Level.SEVERE, "[WorldClaim] The check_for_updates variable was not found in the YAML");
			
		}
		
		
		
		Vars.MaximumChunks = m;
		
		if (a.equalsIgnoreCase("item"))
		{
			Vars.Currency = true;
		}
		else if (a.equalsIgnoreCase("money"))
		{
			if (Vars.eco == null)
			{
				Vars.Currency = true;
				Vars.log.log(Level.SEVERE, "[WorldClaim] You indicated money for currency, however no economy plugin was found. Defaulting back to Items for transactions.");
			}
			else
			{
				Vars.Currency = false;
			}
			
		}
		else
		{
			Vars.log.log(Level.SEVERE, "[WorldClaim] The Currency variable is invalid. Defaulting to Item.");
			Vars.Currency = true;
		}
		
		if (Material.getMaterial(b) != null)
		{
			Vars.Item = Material.getMaterial(b);
		}
		else
		{
			Vars.log.log(Level.SEVERE, "[WorldClaim] The Item variable is invalid. Defaulting to Diamonds.");
			Vars.Item = Material.DIAMOND;
		}
		
		try
		{
			Vars.interpreter.eval("test = " + c.replaceAll("x", "1"));
			interpreter.get("test");
			Vars.CostEquation = c;
		}
		catch (Throwable t)
		{
			Vars.log.log(Level.SEVERE, "[WorldClaim] The CostEquation string is invalid. Defaulting to \"(2.5 * (x * x)) - (2.5 * x) + 10\".");
			Vars.CostEquation = "(2.5 * (x * x)) - (2.5 * x) + 10";
		}
	}
	
	public static void exportVariables()
	{
		config.set("MaximumChunks", Vars.MaximumChunks);
		if (Vars.Currency)
		{
			config.set("Currency", true);
		}
		else
		{
			config.set("Currency", false);
		}
		
		config.set("Item", Vars.Item.toString());
		
		config.set("CostEquation", Vars.CostEquation);
		
	}
}
