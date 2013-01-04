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
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.github.mineGeek.ItemRules.API;
import com.github.mineGeek.ItemRules.ItemRules.Actions;
import com.github.mineGeek.ItemRules.Store.Users;



public class Listeners implements Listener {

	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin( PlayerJoinEvent evt ) {

		Users.addPlayer( evt.getPlayer() );
		//evt.getPlayer().sendMessage("Your level is " + Users.get(evt.getPlayer()).getXPLevel() + " your item level is " + Users.get( evt.getPlayer() ).getItemLevel() );
		API.printCurrentRulesToPlayer( evt.getPlayer() );
		
	}
	
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLeave(PlayerQuitEvent evt )
    {
    	Users.removePlayer( evt.getPlayer().getName() );
    	
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent evt) {
    	Users.get( evt.getPlayer() ).loadRules();
    }
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLevelChange( PlayerLevelChangeEvent evt ) {
		
		Users.get( evt.getPlayer() ).setXPLevel( evt.getPlayer().getLevel() );
		evt.getPlayer().sendMessage("Your level is " + Users.get(evt.getPlayer()).getXPLevel() + " your item level is " + Users.get( evt.getPlayer() ).getItemLevel() );

	}
	
	@EventHandler(priority = EventPriority.LOWEST )
    public void onRespawn(PlayerRespawnEvent evt) {
		
		Users.get( evt.getPlayer() ).setXPLevel( evt.getPlayer().getLevel() );
		evt.getPlayer().sendMessage("Your level is " + Users.get(evt.getPlayer()).getXPLevel() + " your item level is " + Users.get( evt.getPlayer() ).getItemLevel() );
    }
	
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractBlock(PlayerInteractEvent evt){
        
    	
    	if ( evt.isCancelled() ) return;
    	
    	try {
	    	if ( evt.getItem() != null && evt.getItem().getType() != null ) {
	    	
	    		if ( Users.get( evt.getPlayer() ).isRestricted(Actions.USE, evt.getItem().getType(), evt.getItem().getData().getData() ) ) {
	    			evt.setCancelled( true );
	    			evt.getPlayer().sendMessage( "You cant use that" );
	    		}
	    	}
    	} catch (Exception e ) {

    		e.printStackTrace();
    	}
    	
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractEntity(PlayerInteractEvent evt){
        
    	
    	if ( evt.isCancelled() ) return;
    	
    	try {
    		
    		if ( evt.getItem() == null ) return;

    		
    		if ( Users.get(evt.getPlayer() ).isRestricted( Actions.USE, evt.getItem().getType(), evt.getItem().getData().getData() ) ) {
    			evt.setCancelled( true );
    			evt.getPlayer().sendMessage( "You cant use that" );
    		}
    		
    	} catch ( Exception e ) {
    		e.printStackTrace();
    	}
    	
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onCraftItem(final CraftItemEvent evt) {

    	if(evt.isCancelled()) return;
    	
    	if ( evt.getWhoClicked() instanceof Player ) {

    		if ( Users.get( (Player)evt.getWhoClicked() ).isRestricted( Actions.CRAFT, evt.getRecipe().getResult().getType(), evt.getRecipe().getResult().getData().getData() ) ) {
	    		evt.setCancelled( true );
	    		((Player)evt.getWhoClicked()).sendMessage( "You cant craft that" );
	    	}    	
    	}

    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockDestroy(BlockBreakEvent evt)
    {
    	
    	if(evt.isCancelled()) return;

    	if ( Users.get( evt.getPlayer() ).isRestricted( Actions.BREAK, evt.getBlock().getType(), evt.getBlock().getData() ) ) {
    		evt.setCancelled( true );
    		evt.getPlayer().sendMessage( "You cant break that" );
    	}
	
        
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent evt)
    {
    	
        if(evt.isCancelled()) return;

        if ( Users.get( evt.getPlayer() ).isRestricted(Actions.PLACE, evt.getBlock().getType(), evt.getBlock().getData() ) ) {
        	
    		evt.setCancelled( true );
    		evt.getPlayer().sendMessage( "You cant place that" );
    	}
        
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPickup(PlayerPickupItemEvent evt)
    {
    	
        if(evt.isCancelled()) return;
        
    	if ( Users.get( evt.getPlayer() ).isRestricted( Actions.PICKUP, evt.getItem().getItemStack().getType(), evt.getItem().getItemStack().getData().getData()) ) {
    		evt.setCancelled( true );
    		evt.getPlayer().sendMessage( "You cant Pick that up" );
    	}        
        
    }    
    
	

}
