package com.github.mineGeek.ItemRules.Commands;

import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRules.Rules.Rule;
import com.github.mineGeek.ItemRules.Rules.Rules;
import com.github.mineGeek.ItemRules.Store.Players;

public class ApplyRule extends CommandBase {

	/**
	 * Executes the command
	 */
	protected Boolean exec( String cmdName, String[] args ) {
			
		int argStart = 0;
		Player player = null;
		
		if ( !( sender instanceof Player ) ) {
			
			if ( args.length > 0 ) {
				
				player = sender.getServer().getPlayer( args[0] );
				argStart = 1;
				
				if ( player == null ) {
					execMessage = "The player " + args[0] + " is not online.";
					return true;
				}
				
			} else {
				
				execMessage = "You didn't specify the Player name. e.g. /irfull notch";
				return true;
				
			}
			
			
		} else {
			
			player = (Player)sender;
			
		}
		
		if (  args.length > ( 0 + argStart ) ) {
			
			Rule rule = Rules.getRule( args[argStart] );
			
			if ( rule != null && cmdName.equals("irapply") ) Players.get(player).addManualRule(rule);
			if ( rule != null && cmdName.equals("irrevoke") ) Players.get(player).removeManualRule( rule.getTag() );
			
			Players.get(player).loadRules();
		}
		
		
		return true;
	}
}
