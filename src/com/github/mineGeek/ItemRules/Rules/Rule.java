package com.github.mineGeek.ItemRules.Rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mineGeek.ItemRules.ItemRules.Actions;
import com.github.mineGeek.ItemRules.Rules.Applicator.ApplicationResult;
import com.github.mineGeek.ItemRules.Store.PlayerStoreItem;

public class Rule {
	
	public enum AppliesToMode {NOW, NEXT, PREVIOUS};

	private List<Applicator> appliesTo = new ArrayList<Applicator>();
	private Map<String, RuleData> allowedItems = new HashMap<String, RuleData>();
	private Map<String, RuleData> restrictedItems = new HashMap<String, RuleData>();
	
	private List<Actions> actions = new ArrayList<Actions>();
	
	private boolean whitelistItems = false;
	private boolean autoProcess = true;
	
	private String tag;
	private String description;
	private String restrictedMessage;
	private String unrestrictedMessage;
	
	
	/**
	 * Copy constructor
	 * @param rule
	 */
	public Rule( String tag, Rule rule ) {
		
		this.tag = tag;

		this.appliesTo = rule.appliesTo;
		this.allowedItems = rule.allowedItems;
		this.restrictedItems = rule.restrictedItems;
		this.restrictedMessage = rule.restrictedMessage;
		this.unrestrictedMessage = rule.unrestrictedMessage;
		
		this.actions = rule.actions;
		
		this.whitelistItems = rule.whitelistItems;
		this.autoProcess = rule.autoProcess;
		
		this.description = rule.description;
		
	}
	
	
	public Rule( String tag ) {
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
	
	public void setRestrictedMessage( String value ) {
		this.restrictedMessage = value;
	}
	
	public String getRestrictedMessage() {
		return this.restrictedMessage;
	}
	
	public void setUnrestrictedMessage( String value ) {
		this.unrestrictedMessage = value;
	}
	
	public String getUnrestrictedMessage() {
		return this.unrestrictedMessage;
	}
	
	public void setAuto( boolean value ) {
		this.autoProcess = value;
	}
	
	public boolean getAuto() {
		return this.autoProcess;
	}
	
	public void setWhitelistItems( boolean value ) {
		
		this.whitelistItems = value;
		
	}
	
	public boolean getWhitelistItems() {
		return this.whitelistItems;
	}
	
	public void addRestrictedItem( String itemId ) {
		
		this.restrictedItems.put(itemId, new RuleData( this.getTag(), true ) );
		
	}
	
	public void removeRestrictedItem( String itemId ) {
		this.restrictedItems.remove( itemId );
	}
	
	public void setRestrictedItems( List<String> itemIds ) {
		
		if ( !itemIds.isEmpty() ) {
			for ( String x : itemIds ) {
				this.addRestrictedItem( x );
			}
		}
		
	}
	
	public Map<String, RuleData> getRestrictedItems() {
		 return this.restrictedItems;
	}
	
	public void addAllowedItem( String itemid) {
			
		this.allowedItems.put(itemid, new RuleData( this.getTag(), false ) );
		
	}
	
	public void removeAllowedItem( String itemId ) {
		this.allowedItems.remove(itemId);
	}
	
	public void setAllowedItems( List<String> itemids ) {
		
		if ( !itemids.isEmpty() ) {
			
			for ( String x : itemids ) {
				this.addAllowedItem(x);
			}
			
		}
		
	}
	
	public Map<String, RuleData> getAllowedItems() {
		
		return this.allowedItems;
		
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
		return this.appliesToPlayer(player, AppliesToMode.NOW );
	}
	
	public boolean appliesToPlayer( PlayerStoreItem player, AppliesToMode method ) {
		
		if ( player.getPlayer().hasPermission("itemRules.bypass." + this.getTag() ) ) return false;
		
		if ( this.appliesTo.isEmpty() ) return true; //? Doesn't apply to anyone so make it!?!?
		
		ApplicationResult result = null;
		
		for( Applicator x : this.appliesTo ) {
			
			
			if ( method == AppliesToMode.NOW ) {
				result = x.isApplicable(player);
			} else if ( method == AppliesToMode.NEXT ) {
				result = x.willBeApplicable(player);
			} else if ( method == AppliesToMode.PREVIOUS ) {
				result = x.wasApplicable(player);
			}
			
			if ( result == ApplicationResult.YES ) {
				return true;
			} else if( result == ApplicationResult.NO ) {
				return false;
			}
		}
		
		return false;
	}
	

	/**
	 * Returns true if the item and data passed is applicable to rule
	 */
	public boolean isRestrictedItem( String itemId, String data ) {
		
		String i = itemId + "." + data;
		
		if ( this.restrictedItems.containsKey( i ) ) 		return true;
		if ( this.restrictedItems.containsKey( itemId ) ) 	return true;
		if ( this.allowedItems.containsKey(i) ) 			return false;
		if ( this.allowedItems.containsKey(itemId)) 		return false;
		
		return false;
		
		//if ( this.allowedItems.containsKey( itemId + "." + data ) ) return this.allowedItems.get( itemId + "." + data ).isWhitelisted();
		//if ( this.allowedItems.containsKey( itemId ) ) return this.allowedItems.get( itemId + "." + data ).isWhitelisted();
		//return this.getWhitelistItems();
		
	}
	
	
	/**
	 * Returns true if the itemid and data passed is applicable to rule 
	 */
	public boolean isRestrictedItem( int itemId, byte data ) {
		return isRestrictedItem( String.valueOf( itemId ), String.valueOf( data ) );
	}
	
	
	/**
	 * Returns true if the itemid passed is applicable to rule
	 */
	public boolean isRestrictedItem( int itemId ) {
		return this.isRestrictedItem( String.valueOf( itemId ), "");
	}

	public boolean appliesToAction( Actions action ) {
		return this.actions.contains( action );
	}
	
	public void close() {
		
		if ( !this.appliesTo.isEmpty() ) {
			for( Applicator a : this.appliesTo ) {
				a.close();
			}
		}
		
		if ( this.actions != null) this.actions.clear();
		if ( this.appliesTo != null) this.appliesTo.clear();
		if ( this.allowedItems != null ) this.allowedItems.clear();
		if ( this.restrictedItems != null ) this.restrictedItems.clear();
		
	}
	
	
}
