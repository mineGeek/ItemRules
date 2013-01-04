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

		UserStoreItem ui = new UserStoreItem( dataFolder, player);
		players.put( player.getName(), ui );
		ui.loadRules();
	}
	
	public static void removePlayer( String playerName ) {
		players.get( playerName ).save();
		players.remove( playerName );
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
