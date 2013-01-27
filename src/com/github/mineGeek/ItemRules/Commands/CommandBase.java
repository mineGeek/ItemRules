package com.github.mineGeek.ItemRules.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Wrapper for command line.er.. commands
 *
 */
abstract class CommandBase  implements CommandExecutor{

	protected String execMessage 	= null;
	protected CommandSender sender 	= null;
	
	
	/**
	 * Called from Bukkit when ItemRule command is used. Do some preliminary work and
	 * pass up the food chain.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		this.execMessage = null;
		this.sender = sender;
		
		Boolean result = exec( cmd.getName().toLowerCase(), args );
		
		if ( execMessage != null && execMessage.length() > 0 ) {
			
			sender.sendMessage( this.execMessage );
			
		}
		
		this.sender = null;
		return result;
		
		
	}
	
	
	/**
	 * Stub for kids to  @override 
	 * @param cmdName
	 * @param args
	 * @return
	 */
	protected Boolean exec( String cmdName, String[] args ) {
		
		return false;
	}	
	
	
	
}
