package com.github.mineGeek.ItemRules.Events;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.github.mineGeek.ItemRestrictions.Utilities.Config;
import com.github.mineGeek.ItemRules.API;
import com.github.mineGeek.ItemRules.ItemRules.Actions;
import com.github.mineGeek.ItemRules.Store.Players;



public class Listeners implements Listener {

	
	/**]
	 * When player logs in. We add to the Player store, load
	 * any previous values, process applicable rules and 
	 * print them off for the user.
	 * @param evt
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin( PlayerJoinEvent evt ) {

		Players.addPlayer( evt.getPlayer() );
		API.printCurrentRulesToPlayer( evt.getPlayer() );
		
	}
	
	
	/**
	 * When player leaves, we save their data and flush from queue.
	 * @param evt
	 */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLeave(PlayerQuitEvent evt )
    {
    	Players.removePlayer( evt.getPlayer().getName() );
    	
    }
    

    /**
     * When the player changes worlds, we reload their rules
     * @param evt
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent evt) {
    	Players.get( evt.getPlayer() ).loadRules();
    }
	
    
    
    
    
    /**
     * When user changes XP level, we reload their rules and optionally
     * change their Item Level to match.
     * @param evt
     */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLevelChange( PlayerLevelChangeEvent evt ) {
		
		Players.get( evt.getPlayer() ).setXPLevel( evt.getPlayer().getLevel() );

	}
	
	
	
	
	
	/**
	 * When player respawns, set their location ( to update any exit area rules) and update
	 * their XP level (which will cause their rules to reload)
	 * @param evt
	 */
	@EventHandler(priority = EventPriority.LOWEST )
    public void onRespawn(PlayerRespawnEvent evt) {
		
		Players.get( evt.getPlayer() ).setXPLevel( evt.getPlayer().getLevel() );
		Players.get(evt.getPlayer()).playerLocation( evt.getRespawnLocation() );

    }
	
	
	
	
	
	/**
	 * Check if a player can use the item
	 * @param evt
	 */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractBlock(PlayerInteractEvent evt){
        
    	
    	if ( evt.isCancelled() ) return;
    	
    	try {
	    	if ( evt.getItem() != null && evt.getItem().getType() != null ) {
	    	
	    		if ( Players.get( evt.getPlayer() ).isRestricted(Actions.USE, evt.getItem().getType(), evt.getItem().getData().getData() ) ) {
	    			evt.setCancelled( true );
	    		}
	    	}
	    	if ( evt.getClickedBlock() != null ) {
	    	
	    		if ( Players.get( evt.getPlayer() ).isRestricted(Actions.USE, evt.getClickedBlock().getType(), evt.getClickedBlock().getData() ) ) {
	    			evt.setCancelled( true );
	    		}    		
	    		
	    		
	    	}
    	} catch (Exception e ) {

    		e.printStackTrace();
    	}
    	
    }
    
    
    
    
    
    /**
     * Check to see if the player can interact with the thing they are interacting with
     * @param evt
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractEntity(PlayerInteractEvent evt){
        
    	
    	if ( evt.isCancelled() ) return;
    	
    	try {
    		
    		if ( evt.getItem() == null ) return;

    		
    		if ( Players.get(evt.getPlayer() ).isRestricted( Actions.USE, evt.getItem().getType(), evt.getItem().getData().getData() ) ) {
    			evt.setCancelled( true );
    		}
    		
    	} catch ( Exception e ) {
    		e.printStackTrace();
    	}
    	
    }
    
    
    
    
    
    /**
     * Can the user actually craft that? You be the judge.
     * @param evt
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onCraftItem(final CraftItemEvent evt) {

    	if(evt.isCancelled()) return;
    	
    	if ( evt.getWhoClicked() instanceof Player ) {

    		if ( Players.get( (Player)evt.getWhoClicked() ).isRestricted( Actions.CRAFT, evt.getRecipe().getResult().getType(), evt.getRecipe().getResult().getData().getData() ) ) {
	    		evt.setCancelled( true );
	    	}    	
    	}

    }
    
    
    
    
    
    
    /**
     * Can the user break that block? Story at 11
     * @param evt
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockDestroy(BlockBreakEvent evt)
    {
    	
    	if(evt.isCancelled()) return;
    	
    	
    	if ( Players.get( evt.getPlayer() ).isRestricted( Actions.BREAK, evt.getBlock().getType(), evt.getBlock().getData() ) ) {
    		evt.setCancelled( true );
    	}
	
        
    }
    
    
    
    
    
    /**
     * Can the user place that there? Not on my watch!
     * @param evt
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent evt)
    {
    	
        if(evt.isCancelled()) return;

        if ( Players.get( evt.getPlayer() ).isRestricted(Actions.PLACE, evt.getBlock().getType(), evt.getBlock().getData() ) ) {
        	
    		evt.setCancelled( true );

    	}
        
    }
    
    
    
    
    
    /**
     * Can the user grab that item/block?
     * @param evt
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPickup(PlayerPickupItemEvent evt)
    {
    	
        if(evt.isCancelled()) return;
        
    	if ( Players.get( evt.getPlayer() ).isRestricted( Actions.PICKUP, evt.getItem().getItemStack().getType(), evt.getItem().getItemStack().getData().getData()) ) {
    		evt.setCancelled( true );

    	}        
        
    }
    
    
    /**
     * Monitoring this event chugs away at resources. So... only monitor:
     * IF there is an areaRule configured
     * IF the player moved at least a whole fricken block (not just their mouse)
     * ONLY if they are in a chunk that has an areaRule attached. Not bad eh?
     * @param evt
     */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onMovement(PlayerMoveEvent evt) {
		
		if ( !Config.monitorPlayerLocations ) return;
		if ( evt.getFrom().getBlock().equals( evt.getTo().getBlock() ) ) return;

		Players.get(evt.getPlayer()).playerLocation( evt.getTo());
		
	}    
    
	

}
