package com.github.mineGeek.ItemRules.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


abstract class CommandBase  implements CommandExecutor{

	protected String execMessage 	= null;
	protected CommandSender sender 	= null;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		this.execMessage = null;
		this.sender = sender;
		
		Boolean result = exec( cmd.getName().toLowerCase(), args );
		
		if ( execMessage != null ) {
			
			sender.sendMessage( this.execMessage );
			
		}
		
		return result;
		
		
	}
	
	protected Boolean exec( String cmdName, String[] args ) {
		
		return false;
	}	
	
	
	
}
