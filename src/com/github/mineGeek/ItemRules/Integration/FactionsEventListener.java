package com.github.mineGeek.ItemRules.Integration;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.github.mineGeek.ItemRules.API;
import com.github.mineGeek.ItemRules.Config;
import com.github.mineGeek.ItemRules.ItemRules;
import com.github.mineGeek.ItemRules.Store.Players;
import com.massivecraft.factions.event.FPlayerJoinEvent;
import com.massivecraft.factions.event.FPlayerLeaveEvent;



public class FactionsEventListener implements Listener {

	
	ItemRules plugin;
	
	public FactionsEventListener( ItemRules plugin ) {
		super();
		this.plugin = plugin;
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onFactionJoin( FPlayerJoinEvent evt ) {
		
		Players.get( evt.getFPlayer().getName() ).loadRules();

		final Player peep = Config.server.getPlayer( evt.getFPlayer().getName() );
		
		Config.server.getScheduler().scheduleSyncDelayedTask( this.plugin, new Runnable() {
		    @Override 
		    public void run() {
		         API.refreshPlayerRules( peep );
		         //peep.sendMessage("Just updated the faction you joined");
		    }
		}, 30L);		
		

	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onFactionLeave( FPlayerLeaveEvent evt ) {
		
		Players.get( evt.getFPlayer().getName() ).loadRules();

		final Player peep = Config.server.getPlayer( evt.getFPlayer().getName() );
		
		Config.server.getScheduler().scheduleSyncDelayedTask( this.plugin, new Runnable() {
		    @Override 
		    public void run() {
		         API.refreshPlayerRules( peep );
		         //peep.sendMessage("Just updated the faction you left");
		    }
		}, 30L);		
		

	}
	
	public void close() {
		this.plugin = null;
	}
	
	
}
