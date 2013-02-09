package com.github.mineGeek.ItemRules.Integration;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.mineGeek.ItemRules.ItemRules;
import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.api.PartyAPI;




/**
 * Wrapper for the McMMO player
 * @author Samuel.Sweet
 *
 */
public class McMMOPlayer {

	public static boolean enabled = false;
	
	public static void McMMOEnable( ItemRules itemRules ) {
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("mcMMO");
		
		if (plugin == null || ! plugin.isEnabled() ) {
			
			Bukkit.getLogger().info("mcMMO integration for [ItemRules] is not enabled");
			enabled = false;

		} else {
			Bukkit.getLogger().info("mcMMO integration for [ItemRules] is enabled");
			Bukkit.getServer().getPluginManager().registerEvents( new McMMOEventListener(itemRules), itemRules );
			enabled = true;
		}
		
		
		
	}
	
	
	
	public static void loadPlayerSkills( Player player ) {
		
		if ( !enabled ) return;
		if ( !player.isOnline() ) return;
		
	}
	
	public static int getSkillLevel( Player player, String skill ) {
		
		int lvl = 0;
		if ( enabled ) lvl = ExperienceAPI.getLevel(player, skill);
		return lvl;
	}
	
	public static Boolean isPlayerInParty( Player player ) {
		if ( enabled ) return PartyAPI.inParty(player);
		return false;
	}
	
	
}
