package com.github.mineGeek.ItemRules;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.mineGeek.ItemRules.Commands.RulesAvailable;
import com.github.mineGeek.ItemRules.Events.Listeners;
import com.github.mineGeek.ItemRules.Integration.FactionsPlayer;
import com.github.mineGeek.ItemRules.Integration.McMMOPlayer;
import com.github.mineGeek.ItemRules.Rules.Rules;
import com.github.mineGeek.ItemRules.Store.Players;

/**
 * Main entry point for plugin
 *
 */
public class ItemRules extends JavaPlugin {

	/**
	 * Standard enum for actions
	 *
	 */
	public enum Actions {CRAFT, USE, PICKUP, BREAK, PLACE};
	
	
	/**
	 * Save Players datastore when we get disabled.
	 */
	@Override
	public void onDisable() {
		
		Players.close( true );
		AreaRules.close();
		Rules.close();
		PlayerMessenger.close();
		Config.close();
		
		
	}
	
	/**
	 * Set up plugin.
	 */
	@Override
	public void onEnable() {
		
		Config.server = this.getServer();
		Config.c = this.getConfig();
		
		
    	/**
    	 * Intergrators
    	 */
    	FactionsPlayer.FactionsPlayerEnable( this );
    	McMMOPlayer.McMMOEnable( this );		
		
		
		Config.loadConfig();
		this.saveConfig();
		
		Config.loadRulesFromConfig( this.getConfig() );
		Config.loadAreaRulesFromConfig( this.getConfig() );
		
		this.checkDataFolders();
		Players.dataFolder = this.getDataFolder().getPath();;
		Players.loadOnline();
		
		this.getServer().getPluginManager().registerEvents( new Listeners(), this);
    	RulesAvailable ra = new RulesAvailable();
    	getCommand("ircan").setExecutor( ra );
    	getCommand("ircant").setExecutor( ra );
    	

	}
	
		
	/**
	 * Make sure local file structure is sorted!
	 */
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
