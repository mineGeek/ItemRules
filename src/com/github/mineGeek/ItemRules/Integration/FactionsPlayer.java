package com.github.mineGeek.ItemRules.Integration;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.mineGeek.ItemRules.Config;
import com.github.mineGeek.ItemRules.ItemRules;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;



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
		
		if ( Config.server.getOnlinePlayers().length > 0 ) {
			
			for( Player p : Config.server.getOnlinePlayers() ) {
				list.add( p.getName() );
			}
		}
		
		return list;
		
		
	}
	

	
	public static Boolean isInFaction( Player player, List<String> factions ) {
		
		if ( !enabled || factions == null ) return false;
		return  factions.contains( FPlayers.i.get(player).getFaction().getTag() );
		
	}
	
	public static Boolean isFaction( Player player, String factionName ) {
		FPlayer p = FPlayers.i.get( player );
		return ( p.getFactionId() == factionName );
	}
	
	public static Double getPower( Player player ) {
		FPlayer p = FPlayers.i.get( player );
		return p.getPower();
	}
	
	public static Boolean isPowerMin( Player player, Integer power ) {
		FPlayer p = FPlayers.i.get( player );
		return p.getPowerMinRounded() > power;
	}
	
	public static Boolean isPowerMax( Player player, Integer power ) {
		FPlayer p = FPlayers.i.get( player );
		return p.getPowerMaxRounded() > power;
	}	
	

	
}
