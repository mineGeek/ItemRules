package com.github.mineGeek.ItemRules.Rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mineGeek.ItemRules.ItemRules.Actions;
import com.github.mineGeek.ItemRules.Store.PlayerStoreItem;

public class RuleNew {

	private List<Applicator> appliesTo = new ArrayList<Applicator>();
	private Map<String, Boolean> items = new HashMap<String, Boolean>();
	private List<Actions> actions = new ArrayList<Actions>();
	
	private boolean whitelistItems = false;
	private boolean autoProcess = true;
	
	private String tag;
	private String description;
	
	public RuleNew( String tag ) {
		this.tag = tag;
	}
	
	public String getTag() {
		return this.tag;
	}
	
	public void setDescription( String value ) {
		this.description = value;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setAuto( boolean value ) {
		this.autoProcess = value;
	}
	
	public boolean getAuto() {
		return this.autoProcess;
	}
	
	public void setWhitelistItems( boolean value ) {
		
		
		if ( !this.items.isEmpty() && value != this.whitelistItems) {
			
			for( String x : this.items.keySet() ) {
				this.items.put(x, value);
			}
			
		}
		
		this.whitelistItems = value;
		
	}
	
	public boolean getWhitelistItems() {
		return this.whitelistItems;
	}
	
	public void addItem( String itemid) {
		
		this.items.put(itemid, this.whitelistItems);
		
	}
	
	public void setItems( List<String> itemids ) {
		
		if ( !itemids.isEmpty() ) {
			
			for ( String x : itemids ) {
				this.addItem(x);
			}
			
		}
		
	}
	
	public void setActions( List<String> actions ) {
		
		if ( !actions.isEmpty() ) {
			for( String x : actions ) {
				this.actions.add( Actions.valueOf(x) );
			}
		}		
		
	}
	
	public void addApplicator( Applicator app ) {
		this.appliesTo.add( app );
	}
	
	public boolean appliesToPlayer( PlayerStoreItem player ) {
		
		if ( player.getPlayer().hasPermission("itemRules.bypass." + this.getTag() ) ) return false;
		
		boolean result = false;
		
		for( Applicator x : this.appliesTo ) {
			if ( x.isApplicable(player) ) return true;
		}
		
		return result;
	}
	

	/**
	 * Returns true if the item and data passed is applicable to rule
	 */
	public boolean appliesToItem( String itemId, String data ) {
		
		if ( this.items.containsKey( itemId + "." + data ) ) return this.items.get(itemId + "." + data );
		if ( this.items.containsKey( itemId ) ) return this.items.get( itemId );
		return this.getWhitelistItems();
		
	}
	
	
	/**
	 * Returns true if the itemid and data passed is applicable to rule 
	 */
	public boolean appliesToItem( int itemId, byte data ) {
		return appliesToItem( String.valueOf( itemId ), String.valueOf( data ) );
	}
	
	
	/**
	 * Returns true if the itemid passed is applicable to rule
	 */
	public boolean appliesToItem( int itemId ) {
		return this.appliesToItem( String.valueOf( itemId ), "");
	}
	
	public boolean appliesToAction( Actions action ) {
		return this.actions.contains( action );
	}
	

	
	
}
