package com.github.mineGeek.ItemRules.Integration;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.mineGeek.ItemRules.ItemRules;
import com.github.mineGeek.ItemRules.Utilities.Config;
import com.massivecraft.factions.entity.UPlayer;



/**
 * Handles communication with Factions API
 *
 */
public class FactionsPlayer {

	public static boolean enabled = false;
	
	public static void FactionsPlayerEnable( ItemRules itemRules ) {
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("Factions");
		
		if (plugin == null || ! plugin.isEnabled() ) {
			
			Bukkit.getLogger().info("Factions integration for [ItemRules] not enabled");
			enabled = false;

		} else {
			Bukkit.getLogger().info("Factions integration for [ItemRules] is enabled");
			Bukkit.getServer().getPluginManager().registerEvents( new FactionsEventListener(itemRules), itemRules );
			enabled = true;
		}
		
	}
	
	
	public static List<String> getPlayerNamesInFaction( String factionName ) {
		
		List<String> list = new ArrayList<String>();
		
		if ( Config.server().getOnlinePlayers().length > 0 ) {
			
			for( Player p : Config.server().getOnlinePlayers() ) {
				list.add( p.getName() );
			}
		}
		
		return list;
		
		
	}
	
	public static String getFactionName( Player player ) 
	{
		UPlayer uplayer = UPlayer.get(player);
		return uplayer.getFactionName();
		
	}
	
	
	
	public static Boolean isInFaction( Player player, List<String> factions ) {
		
		if ( !enabled || factions == null ) return false;
		UPlayer uplayer = UPlayer.get(player);
		return  factions.contains( uplayer.getFactionName() );
		
	}
	
	public static Boolean isFaction( Player player, String factionName ) {

		UPlayer uplayer = UPlayer.get(player);
		return ( uplayer.getFactionName() == factionName );
	}
	
	public static int getPower( Player player ) {
		UPlayer uplayer = UPlayer.get(player);
		return uplayer.getPowerRounded();
	}
	
	public static Boolean isPowerMin( Player player, Integer power ) {
		UPlayer uplayer = UPlayer.get(player);
		return uplayer.getPowerMinRounded() > power;
	}
	
	public static Boolean isPowerMax( Player player, Integer power ) {
		UPlayer uplayer = UPlayer.get(player);
		return uplayer.getPowerMaxRounded() > power;
		
	}	
	

	
}
