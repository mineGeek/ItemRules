package com.github.mineGeek.ItemRules.Integration;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.github.mineGeek.ItemRules.API;
import com.github.mineGeek.ItemRules.ItemRules;
import com.github.mineGeek.ItemRules.Store.Players;
import com.github.mineGeek.ItemRules.Utilities.Config;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.FactionsEventMembershipChange;


/**
 * Factions event listener. To monitor when someone joins/leaves a faction
 *
 */
public class FactionsEventListener implements Listener {

	ItemRules plugin;
	
	public FactionsEventListener( ItemRules plugin ) {
		super();
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onFactionChange( FactionsEventMembershipChange evt ) {
		
		UPlayer p = evt.getUPlayer();
		Players.get( p.getName() ).loadRules();
		
		final Player peep = Config.server().getPlayer( p.getName() );
		Config.server().getScheduler().scheduleSyncDelayedTask( this.plugin, new Runnable() {
		    @Override 
		    public void run() {
		         API.refreshPlayerRules( peep );
		    }
		}, 30L);	
		
	}
	
	public void close() {
		this.plugin = null;
	}
	
	
}
