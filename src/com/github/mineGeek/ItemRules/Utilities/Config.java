package com.github.mineGeek.ItemRules.Utilities;


import org.bukkit.Bukkit;
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
	 * Text to display to player when there are no changes to rules to display
	 */
	public static String txtNoChangesToDisplay = "There are no changes to your rules.";
	
	
	/**
	 * text to display to player if there are no rules to display
	 */
	public static String txtNoRules = "You have no rules applied to you.";
	
	
	/**
	 * textual prefix for displaying changes in player rules that
	 * have just been lifted
	 */
	public static String txtCanNowDo = "You can now ";
	
	
	/**
	 * Textual prefix for displaying changes in player rules who can
	 * no longer do x
	 */
	public static String txtCanNoLongerDo = "You can no longer ";
	
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
	
	
	/**
	 * The text for default restriction message
	 */
	public static String txtDefaultRestrictedMessage = "use this";
	
	
	/**
	 * Text for default cando message
	 */
	public static String txtDefaultUnrestrictedMessage = "use this";
	
	
	/**
	 * Timeout of restriction messages to prevent flooding player
	 */
	public static int spamPlayerMessageTimeout = 1500;
	
	/**
	 * Debugging parameters
	 */
	public static boolean debug_area_chunkEntrance = false;
	public static boolean debug_area_chunkExit = false;
	public static boolean debug_area_chunkChange = false;
	
	/**
	 * Whether or not to monitor player locations. Will go to true if there
	 * are any areaRules set up
	 */
	public static Boolean monitorPlayerLocations		= false;
	
	/**
	 * Reference to Bukkit server
	 * @return
	 */
	public static Server server() {
		return Bukkit.getServer();
	}
	
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
		
		/**
		 * DO not monitor player movement unless we 
		 * have area rules active.
		 */
		if ( !AreaRules.activeChunks.isEmpty() ) {
			Config.monitorPlayerLocations = true;			
		}
		
	}
	
	
	
	/**
	 * Load all rules from the config
	 * @param c
	 */
	public static void loadRulesFromConfig( MemorySection c) {
		
		/**
		 * Load aliases
		 */
		if ( c.contains("aliases") ) {
			
			for ( String x : c.getConfigurationSection("aliases").getKeys( false ) ) {
				
				Rules.addItemAlias( x, c.getStringList("aliases." + x) );
				
			}
			
		}
		
		/**
		 * Load automatically applied rules
		 */
		if ( c.contains("rules") ) {
			
			for ( String x : c.getConfigurationSection( "rules").getKeys( false ) ) {
				
				Rules.addRule( x, c.getConfigurationSection( "rules." + x ) );
				
			}
			
		}
		
		/**
		 * Load manually applied rules
		 */
		if ( c.contains("manualRules") ) {
			
			for ( String x : c.getConfigurationSection( "manualRules").getKeys( false ) ) {
				
				Rules.addRule( x, c.getConfigurationSection("manualRules." + x), true );
				
			}
			
		}		
		
		Config.server().getLogger().info("ItemRules loaded " + Rules.count() + " rules total.");
		
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
		Config.txtCanNoLongerDo				= c.getString("text.CanNoLongerDo", "You can no longer ");
		Config.txtCanNowDo					= c.getString("text.CanNowDo", "You can now ");
		Config.txtDefaultRestrictedMessage	= c.getString("text.DefaultRestrictedMessage", "You cannot do that");
		Config.txtDefaultUnrestrictedMessage= c.getString("text.DefaultUnrestrictedMessage", "");
		Config.txtNoRules					= c.getString("text.NoRulesToDisplay", "You have no rules applied to you.");
		Config.txtNoChangesToDisplay		= c.getString("text.NoChangesToYourRules", "There are no changes to your rules.");
		Config.spamPlayerMessageTimeout		= c.getInt( "playerMessageTimeout", 1500 );
		
		Config.debug_area_chunkEntrance		= c.getBoolean("debug.area.chunkEntrance", false);
		Config.debug_area_chunkExit			= c.getBoolean("debug.area.chunkExit", false);
		Config.debug_area_chunkChange		= c.getBoolean("debug.area.chunkChange", false);
		
	}
	
	/**
	 * Good guy brings closure
	 */
	public static void close() {
		Config.c = null;
		
	}
	
	
}
