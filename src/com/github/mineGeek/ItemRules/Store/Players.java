package com.github.mineGeek.ItemRules.Store;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRules.Config;

/**
 * Static accessor for all active players
 *
 */
public class Players {

	/**
	 *  Username => PlayerStoreItem mapping
	 */
	private static 	Map<String, PlayerStoreItem>players = new HashMap<String, PlayerStoreItem>();
	
	
	
	
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
	public static PlayerStoreItem get( Player player ) {
		return get( player.getName() );
	}
	
	
	
	
	/**
	 * Accessor for player map using player name
	 * @param String playerName
	 * @return PlayerStoreItem
	 */
	public static PlayerStoreItem get( String playerName ) {
		return players.get( playerName );
	}
	
	
	
	
	/**
	 * Adds player to active store, loads their saved data (if exists) 
	 * and sets players rules
	 * @param Player player
	 */
	public static void addPlayer( Player player ) {

		PlayerStoreItem ui = new PlayerStoreItem( dataFolder, player);
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
		
		if ( Config.server.getOnlinePlayers().length > 0 ) {
		
			for( Player p : Config.server.getOnlinePlayers() ) {
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
	
	public static void saveOnline( boolean close ) {
		
		if ( Config.server.getOnlinePlayers().length > 0 ) {
			for( Player p : Config.server.getOnlinePlayers() ) {
				Players.removePlayer( p.getName() );
			}			
		}
		
		if ( close ) players.clear();
		
	}
	
	public static void close( boolean saveFirst ) {
		
		if ( saveFirst ) {
			saveOnline( true );
		}
		
		if ( !players.isEmpty() ) {
			
			for ( PlayerStoreItem p : players.values() ) {
				p.close();
			}
			
		}
		
		players.clear();

	}
	
	
	
}
