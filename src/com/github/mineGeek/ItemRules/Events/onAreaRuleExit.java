package com.github.mineGeek.ItemRules.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.mineGeek.ItemRules.Rules.AreaRule;
import com.github.mineGeek.ItemRules.Store.IRPlayer;
/**
 * Event API for exiting an Area. Allows other plugins to 
 * monitor when an area is walked out of
 *
 */
public class onAreaRuleExit extends Event implements Cancellable {
	
    private static final HandlerList handlers = new HandlerList();
    private IRPlayer player;
    private AreaRule areaRule;
    private Boolean cancelled = false;
    
    public onAreaRuleExit (AreaRule areaRule, IRPlayer player ) {
        this.player = player;
        this.areaRule = areaRule;
        this.cancelled = false;
    }
 
    public AreaRule getAreaRule() {
        return this.areaRule;
    }
    
    public IRPlayer getPlayer() {
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
	
	public void close() {
		this.areaRule = null;
		this.player = null;
	}
}
