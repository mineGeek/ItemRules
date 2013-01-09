package com.github.mineGeek.ItemRestrictions.Utilities;


import org.bukkit.Server;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

import com.github.mineGeek.ItemRules.Rules.AreaRules;
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
	 * The textual prefix for when we show the player a list of things
	 * they can do
	 */
	public static String txtCanDoPrefix = "Unrestricted: ";
	

	/**
	 * the textual prefix for when we show the player what they can do next
	 */
	public static String txtCanDoNextPrefix = "Will change next: ";
	
	
	/**
	 * The textual prefix for when we show the player what they cannot do
	 */
	public static String txtCannotDoPrefix = "Restricted: ";
	

	public static String txtDefaultRestrictedMessage = "use this";
	
	public static String txtDefaultUnrestrictedMessage = "use this";
	
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
		
		if ( c.contains("areaRules") ) {
			for ( String x : c.getConfigurationSection("areaRules").getKeys( false ) ) {
				AreaRules.addRule(x, c.getConfigurationSection("areaRules." + x) );
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
		
		if ( c.contains("aliases") ) {
			
			for ( String x : c.getConfigurationSection("aliases").getKeys( false ) ) {
				
				Rules.addItemAlias( x, c.getStringList("aliases." + x) );
				
			}
			
		}
		
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
		Config.txtCanDoPrefix				= c.getString("text.CanDoPrefix", "Not restricted: ");
		Config.txtCanDoNextPrefix			= c.getString("text.CanDoNextPrefix", "Next rules change: ");
		Config.txtCannotDoPrefix			= c.getString("text.CannotDoPrefix", "Restricted: ");
		Config.txtDefaultRestrictedMessage	= c.getString("text.DefaultRestrictedMessage", "You cannot do that");
		Config.txtDefaultUnrestrictedMessage= c.getString("text.DefaultUnrestrictedMessage", "");
		
	}
	
	
	public static void close() {
		Config.c = null;
		Config.server = null;
		
	}
	
	
}
