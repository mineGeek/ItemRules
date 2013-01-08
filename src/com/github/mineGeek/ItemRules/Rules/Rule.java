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
	private Map<String, RuleItem> items = new HashMap<String, RuleItem>();
	private List<Actions> actions = new ArrayList<Actions>();
	
	private boolean whitelistItems = false;
	private boolean autoProcess = true;
	
	private String tag;
	private String description;
	
	private RuleItem ruleItem;
	
	/**
	 * Copy constructor
	 * @param rule
	 */
	public Rule( String tag, Rule rule ) {
		
		this.tag = tag;
		
		this.appliesTo = rule.appliesTo;
		this.items = rule.items;
		this.actions = rule.actions;
		
		this.whitelistItems = rule.whitelistItems;
		this.autoProcess = rule.autoProcess;
		
		this.description = rule.description;
		this.ruleItem = new RuleItem(this);
		
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
	
	
	public RuleItem getRuleItem() {
		
		if ( this.ruleItem == null ) {
			this.ruleItem = new RuleItem( this );
		}

		return this.ruleItem;
	}
	
	public void addItem( String itemid) {
			
		this.items.put(itemid, this.getRuleItem() );
		
	}
	
	public void removeItem( String itemId ) {
		this.items.remove(itemId);
	}
	
	public void setItems( List<String> itemids ) {
		
		if ( !itemids.isEmpty() ) {
			
			for ( String x : itemids ) {
				this.addItem(x);
			}
			
		}
		
	}
	
	public Map<String, RuleItem> getItems() {
		
		return this.items;
		
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
	public boolean appliesToItem( String itemId, String data ) {
		
		if ( this.items.containsKey( itemId + "." + data ) ) return this.items.get( itemId + "." + data ).isWhitelisted();
		if ( this.items.containsKey( itemId ) ) return this.items.get( itemId + "." + data ).isWhitelisted();
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
	
	public void close() {
		
		if ( !this.appliesTo.isEmpty() ) {
			for( Applicator a : this.appliesTo ) {
				a.close();
			}
		}
		
		this.actions.clear();
		this.appliesTo.clear();
		this.items.clear();
		this.ruleItem = null;
		
	}
	
	
}
