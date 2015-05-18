package me.chris.WorldClaim;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;

import me.chris.WorldClaim.commands.CommandHandler;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class WorldClaimMain extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		try
		{
			getWorldGuard();
		}
		catch(Throwable t)
		{
			Vars.log.log(Level.SEVERE, "[WorldClaim] NO WORLDGUARD PLUGIN FOUND!");
			Vars.log.log(Level.SEVERE, "[WorldClaim] Disabling plugin!");
			getServer().getPluginManager().disablePlugin(this);
			return; // Maybe you want throw an exception instead
		}
		
		try
		{
			getWorldEdit();
		}
		catch(Throwable t)
		{
			Vars.log.log(Level.SEVERE, "[WorldClaim] NO WORLDEDIT PLUGIN FOUND!");
			Vars.log.log(Level.SEVERE, "[WorldClaim] Disabling plugin!");
			getServer().getPluginManager().disablePlugin(this);
			return; // Maybe you want throw an exception instead
		}
		
		new Vars(this);
		
		try
		{
			firstRun();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		loadYamls();
		
		if (!setupPermissions())
		{
			Vars.log.log(Level.SEVERE, "[WorldClaim] No Permission plugin found! Disabling plugin!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		if (!setupEconomy())
		{
			Vars.log.log(Level.SEVERE, "[WorldClaim] No Economy plugin found!");
			return;
		}
		
		getServer().getPluginManager().registerEvents(new WorldClaimListener(), this);
		
		CommandHandler commandHandler = new CommandHandler();
		getCommand("WorldClaim").setExecutor(commandHandler);
		getCommand("wc").setExecutor(commandHandler);
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				ArrayList<ParticleObject> p = new ArrayList<ParticleObject>();
				p.addAll(Vars.particles);
				for(ParticleObject pl : p)
				{
					HelperMethods.spawnParticles(pl.p, pl.list);
					pl.addCount();
					
					if(pl.counter > pl.max)
					{
						Vars.particles.remove(pl);
					}
				}
				
			}
		}, 0L, 10L);
		
		Vars.log.log(Level.INFO, "[WorldClaim] Version " + Vars.versionNumber);
		Vars.log.log(Level.INFO, "[WorldClaim] Started successfully.");
	}
	
	@Override
	public void onDisable()
	{
		Vars.log.log(Level.INFO, "[WorldClaim] Stopped.");
	}
	
	private void firstRun() throws Exception
	{
		if (!Vars.configFile.exists())
		{
			Vars.log.log(Level.INFO, "[WorldClaim] No config.yml file found. Attempting to make one. ");
			Vars.configFile.getParentFile().mkdirs();
			copy(getResource("config.yml"), Vars.configFile);
			Vars.log.log(Level.INFO, "[WorldClaim] File Made Successfully ");
		}
		else
		{
			Vars.log.log(Level.INFO, "[WorldClaim] Config Found. Using it.  ");
		}
		
	}
	
	private void copy(InputStream in, File file)
	{
		try
		{
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void loadYamls()
	{
		try
		{
			Vars.config.load(Vars.configFile);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Vars.importVariables();
	}
	
	public void saveYamls()
	{
		Vars.exportVariables();
		
		try
		{
			Vars.config.save(Vars.configFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private Boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null)
		{
			Vars.perms = permissionProvider.getProvider();
		}
		return (Vars.perms != null);
	}
	
	private boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(
				net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null)
		{
			Vars.eco = economyProvider.getProvider();
		}
		
		return (Vars.eco != null);
	}
	
	public WorldGuardPlugin getWorldGuard() throws Exception
	{
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
		
		// WorldGuard may not be loaded
		if (plugin == null || !(plugin instanceof WorldGuardPlugin))
		{
			throw new Exception();
		}
		
		return (WorldGuardPlugin) plugin;
	}
	
	public WorldEditPlugin getWorldEdit() throws Exception
	{
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");
		
		// WorldGuard may not be loaded
		if (plugin == null || !(plugin instanceof WorldEditPlugin))
		{
			throw new Exception();
		}
		
		return (WorldEditPlugin) plugin;
	}
}
