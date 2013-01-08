package com.github.mineGeek.ItemRules.Integration;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.mineGeek.ItemRules.ItemRules;
import com.github.mineGeek.ItemRules.Store.Players;
import com.gmail.nossr50.datatypes.SkillType;
import com.gmail.nossr50.util.Users;


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
		
		for ( SkillType x : SkillType.values() ) {
			//prevent thrown errors from querying levels that don't exist?
			try {
				int level = Users.getProfile( player.getName() ).getSkillLevel(x);
				Players.get(player).setMcMMOLevel(x.toString(), level );
			} catch ( Exception e ) {}
		}
		
	}
	
	public static Boolean isPlayerInParty( Player player ) {
		return Users.getPlayer(player).inParty();
	}
	
	
}
