package me.chris.WorldClaim;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class WorldClaimListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerJoin(PlayerJoinEvent event)
	{
		Player p = event.getPlayer();
		
		if (Vars.check_for_updates)
		{
			if (p.isOp())
			{
				String lastestVersion = "";
				try
				{
					lastestVersion = updateCheck();
				}
				catch (Throwable localThrowable)
				{
					
				}
				
				if (!lastestVersion.equalsIgnoreCase(Vars.pluginWithVersion))
				{
					p.sendMessage("§5=====================================================");
					p.sendMessage("§4 Warning!§f This isnt the lastest version of WorldClaim!");
					p.sendMessage("§c " + lastestVersion + "§f Is out! (This is §c" + Vars.pluginWithVersion + "§f)");
					p.sendMessage("§5=====================================================");
				}
			}
		}
		
	}
	
	public static String updateCheck() throws MyException
	{
		try
		{
			URL url = new URL("https://api.curseforge.com/servermods/files?projectids=83358");
			URLConnection conn = url.openConnection();
			conn.setReadTimeout(5000);
			conn.addRequestProperty("User-Agent", "Vault Update Checker");
			conn.setDoOutput(true);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final String response = reader.readLine();
			final JSONArray array = (JSONArray) JSONValue.parse(response);
			
			if (array.size() == 0)
			{
				throw new MyException("No files found, or Feed URL is bad.");
			}
			// Pull the last version from the JSON
			return ((String) ((JSONObject) array.get(array.size() - 1)).get("name")).trim();
		}
		catch (Exception e)
		{
			throw new MyException("There was an issue attempting to check for the latest version.");
		}
	}
}