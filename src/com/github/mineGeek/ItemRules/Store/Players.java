package com.github.mineGeek.ItemRules.Store;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRestrictions.Utilities.Config;

/**
 * Static accessor for all active players
 *
 */
public class Players {

	/**
	 *  Username => IRPlayer mapping
	 */
	private static 	Map<String, IRPlayer>players = new HashMap<String, IRPlayer>();
	
	
	/**
	 * Where to persist player data.
	 * should be plugins/ItemRules/players
	 */
	public 	static 	String dataFolder;
	
	
	/**
	 * Accessor for player map using player object
	 * @param Player player
	 * @return PlayerStoreItem
	 */
	public static IRPlayer get( Player player ) {
		return get( player.getName() );
	}
	
	
	/**
	 * Accessor for player map using player name
	 * @param String playerName
	 * @return PlayerStoreItem
	 */
	public static IRPlayer get( String playerName ) {
		
		return players.get( playerName );
	}
	
	
	/**
	 * Adds player to active store, loads their saved data (if exists) 
	 * and sets players rules
	 * @param Player player
	 */
	public static void addPlayer( Player player ) {

		IRPlayer ui = new IRPlayer( dataFolder, player);
		players.put( player.getName(), ui );
		ui.loadRules();
	}
	
	
	/**
	 * Saves custom data and removes from active store
	 * @param playerName
	 */
	public static void removePlayer( String playerName ) {
		players.get( playerName ).save();
		players.get(playerName).close();
		players.remove( playerName );
	}
	
	
	/**
	 * Loads all players currently online
	 */
	public static void loadOnline() {
		
		if ( Config.server().getOnlinePlayers().length > 0 ) {
		
			for( Player p : Config.server().getOnlinePlayers() ) {
				addPlayer( p );
			}			
		}
	}
	

	/**
	 * Saves all players currently online
	 */
	public static void saveOnline() {
		
		Players.saveOnline( false );

	}
	
	
	/**
	 * Saves all peeps currently online.
	 * If close is specified, run close routine
	 * @param close
	 */
	public static void saveOnline( boolean close ) {
		
		if ( Config.server().getOnlinePlayers().length > 0 ) {
			for( Player p : Config.server().getOnlinePlayers() ) {
				Players.removePlayer( p.getName() );
			}			
		}
		
		if ( close ) players.clear();
		
	}
	
	
	/**
	 * Good Guy Close.
	 * @param saveFirst
	 */
	public static void close( boolean saveFirst ) {
		
		if ( saveFirst ) {
			saveOnline( true );
		}
		
		if ( players != null && !players.isEmpty() ) {
			
			for ( IRPlayer p : players.values() ) {
				p.close();
			}
			
			players.clear();	
		}
		
		

	}
	
	
	
}
