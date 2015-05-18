package me.chris.WorldClaim;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector2D;

public class CommandParking
{
	
	public Player p;
	public ArrayList<String> directions;
	public ArrayList<BlockVector2D> list;
	public CommandType t;
	public int costAmount;
	
	public  CommandParking(Player p, CommandType t, ArrayList<String> directions, ArrayList<BlockVector2D> list, int costAmount)
	{
		this.p = p;
		this.directions = directions;
		this.list = list;
		this.t = t;
		this.costAmount = costAmount;
	}
	
	public  CommandParking(Player p, CommandType t, ArrayList<BlockVector2D> list, int costAmount)
	{
		this.p = p;
		this.directions = null;
		this.list = list;
		this.t = t;
		this.costAmount = costAmount;
	}
	
	public  CommandParking(Player p, CommandType t, ArrayList<String> directions, ArrayList<BlockVector2D> list)
	{
		this.p = p;
		this.directions = directions;
		this.list = list;
		this.t = t;
	}
	
	public  CommandParking(Player p, CommandType t, ArrayList<BlockVector2D> list)
	{
		this.p = p;
		this.directions = null;
		this.list = list;
		this.t = t;
	}
	
}

