package com.github.mineGeek.ItemRules;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.github.mineGeek.ItemRestrictions.Utilities.Config;
import com.github.mineGeek.ItemRestrictions.Utilities.PlayerMessenger;
import com.github.mineGeek.ItemRules.Commands.ApplyRule;
import com.github.mineGeek.ItemRules.Commands.RulesAvailable;
import com.github.mineGeek.ItemRules.Events.Listeners;
import com.github.mineGeek.ItemRules.Integration.FactionsPlayer;
import com.github.mineGeek.ItemRules.Integration.McMMOPlayer;
import com.github.mineGeek.ItemRules.Integration.Vault;
import com.github.mineGeek.ItemRules.Rules.AreaRules;
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
	 * Scheduled task to periodically clean up the Messenger queue
	 */
	@SuppressWarnings("unused")
	private BukkitTask messengerGC;
	
	
	/**
	 * Save Players datastore when we get disabled.
	 */
	@Override
	public void onDisable() {
		
		try { Players.close( true ); }
		catch (Exception e ) {}
		try{ AreaRules.close(); }
		catch( Exception e ) {}
		try{ Rules.close(); }
		catch ( Exception e ) {}
		try { PlayerMessenger.close(); }
		catch (Exception e) {}
		try { Config.close(); }
		catch( Exception e ) {}
		
	}
	
	
	/**
	 * Set up plugin.
	 */
	@Override
	public void onEnable() {
		
		this.saveDefaultConfig();		
		Config.c = this.getConfig();
		
		
    	/**
    	 * Intergrators
    	 */
		Vault.VaultEnable();
    	FactionsPlayer.FactionsPlayerEnable( this );
    	McMMOPlayer.McMMOEnable( this );
		
		/**
		 * Configuration
		 */
		Config.loadConfig();		
		Config.loadRulesFromConfig( this.getConfig() );
		Config.loadAreaRulesFromConfig( this.getConfig() );
		
		/**
		 * Player data
		 */
		this.checkDataFolders();
		Players.dataFolder = this.getDataFolder().getPath();;
		Players.loadOnline();
		
		/**
		 * Commands and Listeners
		 */
		this.getServer().getPluginManager().registerEvents( new Listeners(), this);
    	RulesAvailable ra = new RulesAvailable();
    	getCommand("rules").setExecutor( ra );
    	ApplyRule ar = new ApplyRule();
    	getCommand("apply").setExecutor( ar );
    	getCommand("revoke").setExecutor( ar );
    	
    	/**
    	 * Tasks
    	 */
    	this.messengerGC = Config.server().getScheduler().runTaskTimerAsynchronously( this, new Runnable() {
    	    @Override  
    	    public void run() {
    	    	PlayerMessenger.clean();
    	    }
    	}, 3600L, 3600L); 	
    	

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
