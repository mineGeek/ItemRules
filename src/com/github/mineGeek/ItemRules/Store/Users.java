package com.github.mineGeek.ItemRules.Store;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRules.Config;

public class Users {

	private static 	Map<String, UserStoreItem>players = new HashMap<String, UserStoreItem>();
	public 	static 	String dataFolder;
	public 	static 	Boolean active = false;
	
	public static UserStoreItem get( Player player ) {
		return get( player.getName() );
	}
	
	public static UserStoreItem get( String playerName ) {
		return players.get( playerName );
	}
	
	public static void addPlayer( Player player ) {
		players.put( player.getName(), new UserStoreItem( dataFolder, player ) );
	}
	
	public static void removePlayer( Player player ) {
		players.get(player.getName()).save();
		players.remove( player.getName() );
	}
	
	public static void loadOnline() {
		
		if ( Config.server.getOnlinePlayers().length > 0 ) {
		
			for( Player p : Config.server.getOnlinePlayers() ) {
				addPlayer( p );
			}
			
		}
	}
	
	public static void saveOnline() {
		
		if ( Config.server.getOnlinePlayers().length > 0 ) {
		
			for( Player p : Config.server.getOnlinePlayers() ) {
				get( p.getName() ).save() ;
			}
			
		}
	}
	
	
	
}
