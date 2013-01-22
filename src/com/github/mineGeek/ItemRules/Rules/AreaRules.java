package com.github.mineGeek.ItemRules.Rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import com.github.mineGeek.ItemRestrictions.Utilities.Area;
import com.github.mineGeek.ItemRestrictions.Utilities.Config;

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
		
		int nex = (int) Math.floor( rule.getArea().ne.getX() / 16 );
		int swx = (int) Math.floor( rule.getArea().sw.getX() / 16 );
		
		int nez = (int) Math.floor( rule.getArea().ne.getZ() / 16 );
		int swz = (int) Math.floor( rule.getArea().sw.getZ() / 16 );		
		
		int xMax = Math.max(nex, swx );
		int xMin = Math.min(nex, swx);

		int zMax = Math.max(nez, swz );
		int zMin = Math.min(nez, swz);		
		
		for ( int x = xMin; x <= xMax; x++ ) {
			
			for ( int z = zMin; z <= zMax; z++ ) {
				
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
		
		String worldName = config.getString("world", "world");
		List<Integer> ne = config.getIntegerList("ne");
		List<Integer> sw = config.getIntegerList("sw");		
		
		if ( ne.size() < 3 || sw.size() < 3 ) {
			
			Config.server().getLogger().info( " ======> [ItemRules] configuration error: Area rule '" + tag + "' has invalid ne/sw xyz coordinates");
			return;
		}
		
		Area area = new Area( Config.server().getWorld(worldName), ne.get(0) , ne.get(1), ne.get(2), sw.get(0), sw.get(1), sw.get(2) );

		rule.setTag( tag );
		rule.setArea( area );
		
		rule.setOnEntranceMessage( config.getString( "messages.onEntrance") );
		rule.setOnExitMessage( config.getString("messages.onExit") );
		
		rule.setEntranceRules( config.getStringList("entrance") );
		rule.setExitRules( config.getStringList("exit") );
		
		AreaRules.addArea( rule );

		
		
	}
	
	
	public static void close() {
		
		if ( AreaRules.activeChunks!= null && !AreaRules.activeChunks.isEmpty() ) {
			
			for( String x : AreaRules.activeChunks.keySet() ) {
				
				if ( !AreaRules.activeChunks.get(x).isEmpty() ) {
					
					for ( AreaRule y : AreaRules.activeChunks.get(x) ) {
						y.close();
					}
					
					AreaRules.activeChunks.get(x).clear();
					
				}
				
			}
			
		}
		
		AreaRules.activeChunks.clear();
	}
	
}
