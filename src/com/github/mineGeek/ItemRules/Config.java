package com.github.mineGeek.ItemRules;


import org.bukkit.Server;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

import com.github.mineGeek.ItemRules.Rules.Rules;

/**
 * Utility wrapper for configuration
 *
 */
public class Config {

	/**
	 * The server
	 */
	public static Server server;
	
	
	/**
	 * The actual config file from getConfig()
	 */
	public static FileConfiguration c;
	
	
	/**
	 * If true, will increase item level when XP level increases
	 */
	public static Boolean XPLevelIncreasesItemLevel 	= true;
		
	
	/**
	 * If true, will decrease item level when XP level decreases
	 */
	public static Boolean XPLossReducesItemLevel 		= true;
		
	
	/**
	 * If true, will start off player with an item level equal to their
	 * XP level
	 */
	public static Boolean ItemLevelDefaultsToXPLevel	= true;
	
	
	/**
	 * Default mode for restrictions. If false, rules act as 
	 * blacklist (the default mode). If true, it acts as a whitelist
	 */
	public static Boolean defaultAllow					= false;
		
	
	/**
	 * The textual prefix for when we show the player a list of things
	 * they can do
	 */
	public static String txtCanDoPrefix = "You can ";
	

	/**
	 * the textual prefix for when we show the player what they can do next
	 */
	public static String txtCanDoNextPrefix = "Next you can ";
	
	
	/**
	 * The textual prefix for when we show the player what they cannot do
	 */
	public static String txtCannotDoPrefix = "You cannot yet ";
	

	/**
	 * Whether or not to monitor player locations. Will go to true if there
	 * are any areaRules set up
	 */
	public static Boolean monitorPlayerLocations		= false;
	
	
	/**
	 * Loads all areaRules from the config
	 * @param c
	 */
	public static void loadAreaRulesFromConfig( MemorySection c ) {
		
		if ( c.contains("arearules") ) {
			for ( String x : c.getConfigurationSection("arearules").getKeys( false ) ) {
				AreaRules.addRule(x, c.getConfigurationSection("arearules." + x) );
			}
		}
		
		if ( !AreaRules.activeChunks.isEmpty() ) {
			//monitor movement!
			Config.monitorPlayerLocations = true;			
		}
		
	}
	
	
	
	/**
	 * Load all rules from the config
	 * @param c
	 */
	public static void loadRulesFromConfig( MemorySection c) {
		
		if ( c.contains("rules") ) {
			
			for ( String x : c.getConfigurationSection( "rules").getKeys( false ) ) {
				
				Rules.addRule( x, c.getConfigurationSection("rules." + x) );
				
			}
			
		}
		
		if ( c.contains("manualRules") ) {
			
			for ( String x : c.getConfigurationSection( "manualRules").getKeys( false ) ) {
				
				Rules.addRule( x, c.getConfigurationSection("manualRules." + x), true );
				
			}
			
		}		
		
		Config.server.getLogger().info("ItemRules loaded " + Rules.count() + " rules total.");
		
	}
	
	
	
	
	/**
	 * Load other variables from config
	 */
	public static void loadConfig() {
		
		Config.XPLevelIncreasesItemLevel 	= c.getBoolean("XP.levelIncreasesItemLevel", true );
		Config.XPLossReducesItemLevel 		= c.getBoolean("XP.levelDecreasesItemLevel", true );
		Config.XPLevelIncreasesItemLevel 	= c.getBoolean("XP.itemLevelDefaultsToXPLevel", true );
		Config.defaultAllow					= c.getBoolean("defaultAllow", false );

	}
	
	
	public static void close() {
		Config.c = null;
		Config.server = null;
		
	}
	
	
}
