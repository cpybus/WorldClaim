package me.chris.WorldClaim;

import java.util.List;

import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector2D;

public class ParticleObject
{
	Player p;
	List<BlockVector2D> list;
	int counter;
	int	max;
	
	public ParticleObject(Player p, List<BlockVector2D> list2, int i)
	{
		this.p = p;
		this.list = list2;
		counter = 0;
		max = i;
	}
	
	public void addCount()
	{
		counter++;
	}
}
