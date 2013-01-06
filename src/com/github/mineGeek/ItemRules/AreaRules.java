package com.github.mineGeek.ItemRules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Collection/Controller of areaRules
 *
 */
public class AreaRules {

	
	
	
	/**
	 * List of all areaRules cross referenced by world|chunkX|chunkZ tokens
	 */
	public static Map<String, List<AreaRule>> activeChunks = new HashMap<String, List<AreaRule>>();
	
	
	
	
	
	/**
	 * Adds a configured areaRule to the system
	 * @param rule
	 */
	public static void addArea( AreaRule rule ) {
		
		String sig;
		List<AreaRule> ar;
		
		double fromX = 0;
		double toX = 0;
		double fromZ = 0;
		double toZ = 0;
		
		/**
		 * dont use chunk.get*() as it may not yet be loaded
		 */
		if ( rule.getArea().ne.getX() > rule.getArea().sw.getX() ) {
			fromX = rule.getArea().ne.getX()/16 ;
			toX = rule.getArea().sw.getX()/16;
		} else {
			fromX = rule.getArea().sw.getX()/16;
			toX = rule.getArea().ne.getX() /16 ;			
		}

		if ( rule.getArea().ne.getZ() > rule.getArea().sw.getZ() ) {
			fromZ = rule.getArea().ne.getZ()/16;
			toZ = rule.getArea().sw.getZ()/16;
		} else {
			fromZ = rule.getArea().sw.getZ()/16;
			toZ = rule.getArea().ne.getZ()/16;			
		}
		
		int fX = (int) Math.floor( fromX );
		int tX = (int) Math.floor( toX );
		int fZ = (int) Math.floor( fromZ );
		int tZ = (int) Math.floor( toZ );
				
		for ( int x = fX; x <= tX; x++ ) {
			
			for ( int z = fZ; z <= tZ; z++ ) {
				
				sig = rule.getArea().ne.getWorld().getName() + "|" + x + "|" + z;
				
				if ( activeChunks.containsKey( sig ) ) {
					ar = activeChunks.get(sig);
				} else {
					ar = new ArrayList<AreaRule>();
				}
				ar.add( rule );
				activeChunks.put(sig, ar );
				
			}
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * create an areaRule and add to controller from the config
	 * @param tag
	 * @param config
	 */
	public static void addRule( String tag, ConfigurationSection config ) {
		
		AreaRule rule = new AreaRule();
		
		String worldName = config.getString("worldName", "world");
		List<Integer> ne = config.getIntegerList("ne");
		List<Integer> sw = config.getIntegerList("sw");		
		
		
		Area area = new Area( Config.server.getWorld(worldName), ne.get(0) , ne.get(1), ne.get(2), sw.get(0), sw.get(1), sw.get(2) );

		rule.setTag( tag );
		rule.setArea( area );
		rule.setEntranceRules( config.getStringList("entrance") );
		rule.setExitRules( config.getStringList("exit") );
		
		AreaRules.addArea( rule );

		
		
	}	
	
}
