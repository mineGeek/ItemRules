package com.github.mineGeek.ItemRules;


import org.bukkit.Server;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

import com.github.mineGeek.ItemRules.Rules.Rules;

public class Config {

	public static Server server;
	public static FileConfiguration c;
	
	public static Boolean XPLevelIncreasesItemLevel 	= true;
	public static Boolean XPLossReducesItemLevel 		= true;
	public static Boolean ItemLevelDefaultsToXPLevel	= true;
	
	public static void loadRulesFromConfig( MemorySection c) {
		
		if ( c.contains("rules") ) {
			
			for ( String x : c.getConfigurationSection( "rules").getKeys( false ) ) {
				
				Rules.addRule( x, c.getConfigurationSection("rules." + x) );
				
			}
			
		}
		
		Config.server.getLogger().info("ItemRules loaded " + Rules.count() + " rules total.");
		
	}
	
	
	public static void loadConfig() {
		
		Config.XPLevelIncreasesItemLevel 	= c.getBoolean("XP.levelIncreasesItemLevel", true );
		Config.XPLossReducesItemLevel 		= c.getBoolean("XP.levelDecreasesItemLevel", true );
		Config.XPLevelIncreasesItemLevel 	= c.getBoolean("XP.itemLevelDefaultsToXPLevel", true );
		
		/*
		List<Actions> actionList = new ArrayList<Actions>();
		actionList.add( Actions.CRAFT );
		actionList.add( Actions.PICKUP );
		actionList.add( Actions.USE );
		
		c.addDefault("rules.example.actions", actionList);
		
		List<String> worldList = new ArrayList<String>();
		worldList.add("world");
		
		c.addDefault("rules.example.worlds", worldList );
		
		worldList.clear();
		worldList.add("world_nether");
		
		c.addDefault("rules.example.excludeWorlds", worldList );
		c.addDefault("rules.example.XPMin", 0 );
		c.addDefault("rules.example.XPMax", 2 );
		
		List<String> itemList = new ArrayList<String>();
		itemList.add("268");
		itemList.add("5.2");
		
		c.addDefault("rules.example.items", itemList );
		
		actionList = new ArrayList<Actions>();
		actionList.add( Actions.CRAFT );
		actionList.add( Actions.PICKUP );
		actionList.add( Actions.USE );	
		
		
		c.addDefault("rules.example2.actions", actionList);
		
		worldList = new ArrayList<String>();
		worldList.add("world");
		
		c.addDefault("rules.example2.worlds", worldList );
		
		worldList.clear();
		worldList.add("world_nether");
		
		c.addDefault("rules.example2.excludeWorlds", worldList );
		c.addDefault("rules.example2.XPMin", 0 );
		c.addDefault("rules.example2.XPMax", 2 );
		
		itemList = new ArrayList<String>();
		itemList.add("276");
		itemList.add("278");		
		
		c.addDefault("rules.example2.items", itemList );
		*/
		c.options().copyDefaults( true );
		
	}
	
}
