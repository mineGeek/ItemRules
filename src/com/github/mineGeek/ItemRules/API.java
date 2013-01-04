package com.github.mineGeek.ItemRules;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRules.Store.Users;
import com.github.mineGeek.ItemRules.Rules.Rules;

public class API {

	
	public static void addRuleToPlayer( Player player, String ruleName ) {
		Users.get(player).addManualRule( ruleName );
		Users.get(player).loadRules();
	}
	
	public static void removeRuleFromPlayer( Player player, String ruleName ) {
		Users.get(player).removeManualRule( ruleName );
		Users.get(player).loadRules();
	}
	
	public static void refreshPlayerRules( Player player ) {
		Users.get(player).loadRules();
	}
	
	public static void setPlayerItemLevel( Player player, Integer level ) {
		Users.get(player).setItemLevel(level);
	}
	
	public static void incrimentPlayerItemLevel( Player player ) {
		API.setPlayerItemLevel(player, Users.get(player).getItemLevel() + 1 );
	}
	
	public static void incrimentPlayerItemLevel( Player player, Integer amount ) {
		API.setPlayerItemLevel(player, Users.get(player).getItemLevel() + amount );
	}
	
	public static void printCurrentRulesToPlayer( Player player ) {
		player.sendMessage( Rules.getRuleList(player, true, true, true, ChatColor.GREEN, ChatColor.YELLOW, ChatColor.RED) );
	}
	
	public static void printCurrentRestrictionsToPlayer( Player player ) {
		player.sendMessage( Rules.getRuleList(player, false, false, true, ChatColor.GREEN, ChatColor.YELLOW, ChatColor.RED) );
	}
	
}
