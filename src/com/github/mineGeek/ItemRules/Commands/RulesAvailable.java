package com.github.mineGeek.ItemRules.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRules.Rules.RuleInterface;
import com.github.mineGeek.ItemRules.Rules.Rules;

public class RulesAvailable extends CommandBase {

	protected Boolean exec( String cmdName, String[] args ) {
		
		Boolean result = true;
			
		Integer argStart = 0;
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

		String emptyMessage = null;

		Boolean can = ( cmdName.equalsIgnoreCase( "ircan" ) ? true : false );
		
		List<RuleInterface> unrestricted 		= new ArrayList<RuleInterface>();
		List<RuleInterface> restricted 			= new ArrayList<RuleInterface>();
		List<RuleInterface> unrestrictednext 	= new ArrayList<RuleInterface>();		
		
		if ( ( args.length > ( 0 + argStart ) ) && !args[ argStart ].equalsIgnoreCase("current" ) ) {
			
			if ( args[ argStart ].equalsIgnoreCase("all") ) {
				
				unrestricted = Rules.getPlayerItemRules(player, false, false);
				restricted = Rules.getPlayerItemRules(player, true, false);
				unrestrictednext = Rules.getPlayerItemRules(player, true, true);
				emptyMessage = can ? "do nothing" : "be limited!";
					
			} else if ( args[ argStart ].equalsIgnoreCase("current" ) ) {
				
				unrestricted = Rules.getPlayerItemRules(player, false, false);
				unrestrictednext = Rules.getPlayerItemRules(player, true, true);
				emptyMessage = can ? "nothing new" : "have any new restrictions (at this level)";
					
			} if ( args[argStart].equalsIgnoreCase("next") ) {
				
				unrestrictednext = Rules.getPlayerItemRules(player, true, true);
				emptyMessage = can ? "do the same as this level" : "do the same things at this level";
					
			}
			
			
		} else {

			emptyMessage = can ? "nothing" : "anything!";
			unrestricted = Rules.getPlayerItemRules(player, false, false);
			unrestrictednext = Rules.getPlayerItemRules(player, true, true);
			if ( execMessage.length() == 0 ) execMessage = can ? "You have no restrictions" : "You seem to have too many restrictions to even count.";
			
		}
		
		
		if ( execMessage == null || execMessage.length() == 0 ) execMessage = emptyMessage;		
		
		
		return result;
	}

}
