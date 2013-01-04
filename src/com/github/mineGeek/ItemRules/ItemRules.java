package com.github.mineGeek.ItemRules;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.mineGeek.ItemRules.Commands.RulesAvailable;
import com.github.mineGeek.ItemRules.Events.Listeners;
import com.github.mineGeek.ItemRules.Store.Users;

public class ItemRules extends JavaPlugin {

	public enum Actions {CRAFT, USE, PICKUP, BREAK, PLACE};
	
	@Override
	public void onDisable() {
		
		Users.saveOnline();
	}
	
	@Override
	public void onEnable() {
		
		Config.server = this.getServer();
		Config.c = this.getConfig();
		Config.loadConfig();
		this.saveConfig();
		
		Config.loadRulesFromConfig( this.getConfig() );
		this.checkDataFolders();
		Users.dataFolder = this.getDataFolder().getPath();;
		Users.loadOnline();
		
		this.getServer().getPluginManager().registerEvents( new Listeners(), this);
    	RulesAvailable ra = new RulesAvailable();
    	getCommand("ircan").setExecutor( ra );
    	getCommand("ircant").setExecutor( ra );		
		
		
	}
	
	public void checkDataFolders() {
		
    	File file = new File( this.getDataFolder() + File.separator + "players" );
    	
    	if ( !file.isDirectory() ) {
    		try {
    			file.mkdir();
    		} catch (Exception e ) {
    			this.getLogger().info("Failed making plugins/ItemRules/players");
    		}
    	}		
		
	}
	
	
}
