package com.github.mineGeek.ItemRules.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRules.Rules.RuleItem;
import com.github.mineGeek.ItemRules.Rules.Rules;
import com.github.mineGeek.ItemRules.Store.Players;





/**
 * Allows users to see which rules apply and do not apply.
 * covers:
 * 
 * can [all|current|next]
 * cant [all|current|next]
 *
 */
public class RulesAvailable extends CommandBase {

	
	
	
	
	/**
	 * Executes the command
	 */
	protected Boolean exec( String cmdName, String[] args ) {
			
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
		
		
		
		Boolean doCan 		= cmdName.equalsIgnoreCase("ircan") ? true : false;
		Boolean doCanNext 	= doCan ? true : false;				
		Boolean doCanNow	= doCan ? true : false;
		
		if ( doCan ) {
			
			if ( Players.get(player).getAppliedRules().isEmpty() ) {
				player.sendMessage("There are currently no rules applied to you right now!!");
			} else {
				player.sendMessage("there are currently " + Players.get(player).getAppliedRules().size()  + " rule(s) applied to you");
				
				List<String> rulenames = new ArrayList<String>();
				for ( RuleItem r : Players.get(player).getAppliedRules().values() ) {
					if ( !rulenames.contains(r.getTag())) rulenames.add(r.getTag());
				}
				
				player.sendMessage(" Rule applied: " + rulenames.toString() );
			}
			
		}		
		
		if (  args.length > ( 0 + argStart ) ) {
			

			
			if ( args[ argStart ].equalsIgnoreCase("all") ) {
				
				doCanNow = false;
				doCanNext = false;
					
			} else if ( args[ argStart ].equalsIgnoreCase("current" ) ) {
				
				doCanNow = true;
				doCanNext = true;
					
			} if ( args[argStart].equalsIgnoreCase("next") ) {
				
				doCanNow = doCan ? true : false;
				doCanNext = true;
			}
			
			
		}
		
		execMessage = Rules.getRuleList( player, doCan, doCanNow, doCanNext, ChatColor.GREEN, ChatColor.YELLOW, ChatColor.RED );

		
		return true;
	}

}
