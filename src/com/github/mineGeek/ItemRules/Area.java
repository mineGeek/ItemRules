package com.github.mineGeek.ItemRules;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Utility class for staking out areas as a cuboid
 *
 */
public class Area {

	
	
	/**
	 * corner 1 of area
	 */
	public Location ne;
	
	
	
	
	/**
	 * opposite corner of cuboid
	 */
	public Location sw;
	
	
	
	
	
	/**
	 * Constuctor taking the 2 points of cuboid
	 * @param ne
	 * @param sw
	 */
	public Area( Location ne, Location sw ) {
		this.ne = ne;
		this.sw = sw;
	}
	
	
	
	
	
	/**
	 * Constructor that sets all points at once
	 * @param world
	 * @param neX
	 * @param neY
	 * @param neZ
	 * @param swX
	 * @param swY
	 * @param swZ
	 */
	public Area( World world, Integer neX, Integer neY, Integer neZ, Integer swX, Integer swY, Integer swZ ) {
		
		this( new Location( world, neX, neY, neZ) , new Location( world, swX, swY, swZ ) );
		
	}
	
	
	
	
	/**
	 * Constructor that sets all variables from lists
	 * @param world
	 * @param ne
	 * @param sw
	 */
	public Area( World world, List<Integer> ne, List<Integer> sw ) {
		this( world, ne.get(0), ne.get(1), ne.get(2), sw.get(0), sw.get(1), sw.get(2));
	}
	
	
	
	
	
	/**
	 * Constructor that sets all vars from a single list
	 * @param world
	 * @param loc
	 */
	public Area( World world, List<Integer> loc ) {
		this( world, loc.get(0), loc.get(1), loc.get(2), loc.get(3), loc.get(4), loc.get(5));
	}

	
	
	
	/**
	 * Will return true if location passed is within the cuboid
	 * @param l
	 * @return
	 */
	public Boolean intersectsWith( Location l ) {	
		
		/*
		Boolean maxX = (int)l.getX() <= Math.max(ne.getX(), sw.getX());
		Boolean minX = (int)l.getX() >= Math.min(ne.getX(), sw.getX());
		Boolean maxY = (int)l.getY() <= Math.max(ne.getY(), sw.getY());
		Boolean minY = (int)l.getY() >= Math.min(ne.getY(), sw.getY());
		Boolean maxZ = (int)l.getZ() <= Math.max(ne.getZ(), sw.getZ());
		Boolean minZ = (int)l.getZ() >= Math.min(ne.getZ(), sw.getZ());
		
		Boolean result = (maxX && minX) && (maxY && minY) && (maxZ && minZ);
		return result;
		 */
		//this should be faster.
		if ( l.getX() > Math.max( ne.getX(), sw.getX() ) ) 	return false;
		if ( l.getX() < Math.min( ne.getX(), sw.getX() ) ) 	return false;
		if ( l.getZ() > Math.max( ne.getZ(), sw.getZ() ) ) 	return false;
		if ( l.getZ() < Math.min( ne.getZ(), sw.getZ() ) ) 	return false;
		if ( l.getY() > Math.max( ne.getY(), sw.getY() ) ) 	return false;
		if ( l.getY() < Math.min( ne.getY(), sw.getY() ) ) 	return false;
		
		return true;
		
		
	}

	
	
}
