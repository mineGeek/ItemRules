package com.github.mineGeek.ItemRules.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.mineGeek.ItemRules.AreaRule;
import com.github.mineGeek.ItemRules.Store.PlayerStoreItem;

public class onAreaRuleEntrance extends Event implements Cancellable {

	    private static final HandlerList handlers = new HandlerList();
	    private PlayerStoreItem player;
	    private AreaRule areaRule;
	    private Boolean cancelled = false;
	    
	    public onAreaRuleEntrance (AreaRule areaRule, PlayerStoreItem player ) {
	        this.player = player;
	        this.areaRule = areaRule;
	        this.cancelled = false;
	    }
	 
	    public AreaRule getAreaRule() {
	        return this.areaRule;
	    }
	    
	    public PlayerStoreItem getPlayer() {
	    	return this.player;
	    }
	 
	    public HandlerList getHandlers() {
	        return handlers;
	    }
	 
	    public static HandlerList getHandlerList() {
	        return handlers;
	    }

		@Override
		public boolean isCancelled() {
			return this.cancelled;
		}

		@Override
		public void setCancelled(boolean cancel) {
			this.cancelled = cancel;
			
		}
		
	
}
