package com.github.mineGeek.ItemRules.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRules.Rules.Rules;


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
		
		
		
		Boolean doCan 		= cmdName.equalsIgnoreCase("ircan") ? true : false;
		Boolean doCanNext 	= doCan ? true : false;				
		Boolean doCanNow	= doCan ? true : false;
		
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
		
		List<String> result = Rules.getRuleList( player, doCan, doCanNow, doCanNext, ChatColor.GREEN, ChatColor.YELLOW, ChatColor.RED );

		boolean toPlayer = ( ( sender instanceof Player || sender instanceof CommandSender) );
		
		for ( String x : result ) {
			
			if ( toPlayer) {
				player.sendMessage(x);
			} else {
				sender.sendMessage(x);
			}
		}
		
		
		return true;
	}

}
