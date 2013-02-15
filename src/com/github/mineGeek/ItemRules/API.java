package com.github.mineGeek.ItemRules;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRules.Store.Players;
import com.github.mineGeek.ItemRules.Utilities.Config;
import com.github.mineGeek.ItemRules.Rules.Rules;

/**
 * Provides ability for other plugins to interact with system
 *
 */
public class API {
	
	/**
	 * Manually adds a rule to the player. Rule should have auto set to false. 
	 * Useful to trigger events like removing all restrictions if a user is in
	 * their home region
	 * 
	 * @param player
	 * @param ruleName
	 */
	public static void addRuleToPlayer( Player player, String ruleName ) {
		Players.get(player).addManualRule( ruleName );
		Players.get(player).loadRules();
	}
	
	
	/**
	 * Removes a rule from a player that had one manually applied.
	 * @param player
	 * @param ruleName
	 */
	public static void removeRuleFromPlayer( Player player, String ruleName ) {
		Players.get(player).removeManualRule( ruleName );
		Players.get(player).loadRules();
	}
	

	/**
	 * Refreshes and reloads current rules for player. Useful if something may have been done to 
	 * alter a rule or a players qualification of a rule. Also must be called after a rule is
	 * manually added
	 * @param player
	 */
	public static void refreshPlayerRules( Player player ) {
		Players.get(player).loadRules();
	}
	

	/**
	 * Refreshes and reloads rules for list of player names.
	 * @param playerList
	 */
	public static void refreshPlayerRules( List<String> playerList ) {
		
		if ( !playerList.isEmpty() ) {
			
			for ( String x : playerList ) {
				Players.get(x).loadRules();
			}
		}
		
	}	
	
	
	/**
	 * Sets the players item level. Note that depending on settings (e.g. itemlevel is set
	 * to track with xp and they level up) the system may change this value
	 * @param player
	 * @param level
	 */
	public static void setPlayerItemLevel( Player player, Integer level ) {
		Players.get(player).setItemLevel(level);
	}
	

	/**
	 * Increases the players item level by 1 
	 * @param player
	 */
	public static void incrimentPlayerItemLevel( Player player ) {
		API.setPlayerItemLevel(player, Players.get(player).getItemLevel() + 1 );
	}
	
	
	/**
	 * Increases the players item level by amount ( can be positive or negative)
	 * @param player
	 * @param amount
	 */
	public static void incrimentPlayerItemLevel( Player player, Integer amount ) {
		API.setPlayerItemLevel(player, Players.get(player).getItemLevel() + amount );
	}
	

	/**
	 * Prints out players currently applied rules to them
	 * @param player
	 */
	public static void printRulesToPlayer( Player player ) {
		List<String> result = Rules.getPlayerRulesList(player, false, true, false);
		if ( result != null && result.size() > 0 ) { 
			for (String x : result ) {
				player.sendMessage(x);
			}
		} else {
			player.sendMessage( Config.txtNoRules );
		}
	}
	
	/**
	 * Prints out differences to rules applied to player since last change
	 * @param player
	 * @param printNoChangesOnEmpty
	 */
	public static void printChangedRulesToPlayer( Player player, boolean printNoChangesOnEmpty ) {
		if ( !API.printNewUnrestrictedToPlayer(player, false ) && !API.printNewRestrictionsToPlayer(player, false) && printNoChangesOnEmpty ) {
			player.sendMessage( ChatColor.YELLOW + Config.txtNoChangesToDisplay );
		}
	}
	
	
	/**
	 * Prints rules that were removed from player recently
	 * @param player
	 * @param printNoChangesOnEmpty
	 * @return
	 */
	public static boolean printNewUnrestrictedToPlayer( Player player, boolean printNoChangesOnEmpty ) {
		String message = Players.get(player).getNewAppliedRules("unrestricted");

		if ( message != null ) {
			player.sendMessage(message);
			return true;
		} else if ( printNoChangesOnEmpty ) {
			player.sendMessage( ChatColor.GREEN + Config.txtNoChangesToDisplay );
		}
		return false;
	}
	
	
	/**
	 * Prints rules that were recently applied to player
	 * @param player
	 * @param printNoChangesOnEmpty
	 * @return
	 */
	public static boolean printNewRestrictionsToPlayer( Player player, boolean printNoChangesOnEmpty ) {
		String message = Players.get(player).getNewAppliedRules("restricted");
		
		if ( message != null ) {
			player.sendMessage(message);
			return true;
		} else if ( printNoChangesOnEmpty ) {
			player.sendMessage( ChatColor.RED + Config.txtNoChangesToDisplay );
		}
		return false;
	}
	
}
