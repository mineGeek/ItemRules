package com.github.mineGeek.ItemRules.Rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mineGeek.ItemRules.Integration.Vault;
import com.github.mineGeek.ItemRules.ItemRules.Actions;
import com.github.mineGeek.ItemRules.Rules.Applicator.ApplicationResult;
import com.github.mineGeek.ItemRules.Store.IRPlayer;

/**
 * Basic rule that qualifies use of action and item
 *
 */
public class Rule {
	
	
	/**
	 * Measures how this rule is qualified against the player
	 */
	public enum AppliesToMode {NOW, NEXT, PREVIOUS};
	
	
	/**
	 * Describes what the result of the restriction is. 
	 * Allow = item is whitelisted
	 * Deny = item is blacklisted
	 * Previous options will remove any existing rules up to this one
	 */
	public enum RuleMode {DEFAULT, ALLOW, DENY, ALLOWPREVIOUS, DENYPREVIOUS};

	
	/**
	 * Criteria for this rule
	 */
	private List<Applicator> appliesTo = new ArrayList<Applicator>();
	
	
	/**
	 * Items allowed by this rule
	 */
	private Map<String, RuleData> allowedItems = new HashMap<String, RuleData>();
	
	
	/**
	 * Items restricted by rule
	 */
	private Map<String, RuleData> restrictedItems = new HashMap<String, RuleData>();
	
	
	/**
	 * Actions applied to rule
	 */
	private List<Actions> actions = new ArrayList<Actions>();
	
	
	/**
	 * Revaluate this rule each time they are loaded
	 * False for sticky or manual rules
	 */
	private boolean autoProcess = true;
	
	
	/**
	 * Unique tag for rule
	 */
	private String tag;
	
	
	private String description;
	
	/**
	 * Message to display when player is restricted
	 */
	private String restrictedMessage;
	
	
	/**
	 * Message to display when user is unrestricted
	 */
	private String unrestrictedMessage;
	
	
	/**
	 * Mode that applies to rule ( Default, Allow or Deny)
	 */
	private RuleMode ruleMode = RuleMode.DEFAULT;
	
	
	/**
	 * Copy constructor
	 * @param rule
	 */
	public Rule( String tag, Rule rule ) {
		
		this.tag = tag;

		this.ruleMode = rule.ruleMode;
		this.appliesTo = rule.appliesTo;
		this.allowedItems = rule.allowedItems;
		this.restrictedItems = rule.restrictedItems;
		this.restrictedMessage = rule.restrictedMessage;
		this.unrestrictedMessage = rule.unrestrictedMessage;		
		this.actions = rule.actions;		
		this.autoProcess = rule.autoProcess;		
		
	}
	
	
	/**
	 * Constructor with tag
	 * @param tag
	 */
	public Rule( String tag ) {
		this.tag = tag;
	}
	
	
	/**
	 * Returns tag
	 * @return
	 */
	public String getTag() {
		return this.tag;
	}
	
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription( String value ) {
		this.description = value;
	}
	

	/**
	 * Sets RuleMode ( Default, Deny or Allow)
	 * @param mode
	 */
	public void setRuleMode( RuleMode mode ) {
		this.ruleMode = mode;
	}
	
	
	/**
	 * Returns RuleMode
	 * @return
	 */
	public RuleMode getRuleMode() {
		return this.ruleMode;
	}
	
	
	/**
	 * Returns RestrictedMessage
	 * Displayed when this rule is restricting a player
	 * @param value
	 */
	public void setRestrictedMessage( String value ) {
		this.restrictedMessage = value;
	}
	
	
	/**
	 * Returns Restricted Message 
	 * @return
	 */
	public String getRestrictedMessage() {
		return this.restrictedMessage;
	}
	
	
	/**
	 * Sets Unrestricted Message
	 * @param value
	 */
	public void setUnrestrictedMessage( String value ) {
		this.unrestrictedMessage = value;
	}
	
	
	/**
	 * Returns Unrestricted Message
	 * @return
	 */
	public String getUnrestrictedMessage() {
		return this.unrestrictedMessage;
	}
	
	
	/**
	 * Sets Auto mode
	 * If true, rule is reevaluated each loadRule()
	 * @param value
	 */
	public void setAuto( boolean value ) {
		this.autoProcess = value;
	}
	
	
	/**
	 * Returns Auto mode
	 * @return
	 */
	public boolean getAuto() {
		return this.autoProcess;
	}
	
	
	/**
	 * Adds items as restricted
	 * @param list
	 */
	public void addRestrictedItems( List<String> list ) {
		
		if ( !list.isEmpty() ) {
			for (String x : list ) {
				this.addRestrictedItem(x);
			}
		}
	}
	
	
	/**
	 * Add item as restricted
	 * @param itemId
	 */
	public void addRestrictedItem( String itemId ) {
		
		this.restrictedItems.put(itemId, new RuleData( this.getTag(), true ) );
		
	}
	
	
	/**
	 * Remove an item from the restricted list
	 * @param itemId
	 */
	public void removeRestrictedItem( String itemId ) {
		this.restrictedItems.remove( itemId );
	}
	
	
	/**
	 * Sets the entire restricted item list
	 * @param itemIds
	 */
	public void setRestrictedItems( List<String> itemIds ) {
		
		if ( !itemIds.isEmpty() ) {
			for ( String x : itemIds ) {
				this.addRestrictedItem( x );
			}
		}
		
	}
	
	
	/**
	 * Returns list of restricted items
	 * @return
	 */
	public Map<String, RuleData> getRestrictedItems() {
		 return this.restrictedItems;
	}
	
	
	/**
	 * Adds item to allowed list
	 * @param list
	 */
	public void addAllowedItems( List<String> list ) {
		
		if ( !list.isEmpty() ) {
			for ( String x : list ) {
				this.addAllowedItem(x);
			}
		} 
		
	}
	
	
	/**
	 * Adds item to list of allowed items
	 * @param itemid
	 */
	public void addAllowedItem( String itemid) {
			
		this.allowedItems.put(itemid, new RuleData( this.getTag(), false ) );
		
	}
	
	
	/**
	 * Removes item from list of alled stuff
	 * @param itemId
	 */
	public void removeAllowedItem( String itemId ) {
		this.allowedItems.remove(itemId);
	}
	
	
	/**
	 * Sets the entire allowed items list
	 * @param itemids
	 */
	public void setAllowedItems( List<String> itemids ) {
		
		if ( !itemids.isEmpty() ) {
			
			for ( String x : itemids ) {
				this.addAllowedItem(x);
			}
			
		}
		
	}
	
	
	/**
	 * Returns the list of items allowed by rule
	 * @return
	 */
	public Map<String, RuleData> getAllowedItems() {
		
		return this.allowedItems;
		
	}
	
	
	/**
	 * Sets the list of actions this rule applies to
	 * @param actions
	 */
	public void setActions( List<String> actions ) {
		
		if ( !actions.isEmpty() ) {
			for( String x : actions ) {
				this.actions.add( Actions.valueOf(x.toUpperCase()) );
			}
		}		
		
	}
	
	
	/**
	 * Adds an Applicator ( criteria) to this rule
	 * @param app
	 */
	public void addApplicator( Applicator app ) {
		this.appliesTo.add( app );
	}
	
	
	/**
	 * Returns true if player qualifies to have this
	 * rule applied
	 * @param player
	 * @return
	 */
	public boolean appliesToPlayer( IRPlayer player ) {
		return this.appliesToPlayer(player, AppliesToMode.NOW );
	}
	
	
	/**
	 * Returns true if rule applied to player
	 * @param player
	 * @param method
	 * @return
	 */
	public boolean appliesToPlayer( IRPlayer player, AppliesToMode method ) {
		

		if ( Vault.hasPerm( player.getPlayer() , "itemRules.bypass." + this.getTag() ) ) return false;
		
		
		
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
		
		return this.getRuleMode() != RuleMode.DENY;
		
		
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

	
	/**
	 * Returns true if Action applied to rule
	 * @param action
	 * @return
	 */
	public boolean appliesToAction( Actions action ) {
		return this.actions.contains( action );
	}
	
	
	/**
	 * Good Guy Closure
	 */
	public void close() {
		
		if ( this.appliesTo != null && !this.appliesTo.isEmpty() ) {
			for( Applicator a : this.appliesTo ) {
				a.close();
			}
			this.appliesTo.clear();
		}
		
		if ( this.actions != null) this.actions.clear();
		if ( this.allowedItems != null ) this.allowedItems.clear();
		if ( this.restrictedItems != null ) this.restrictedItems.clear();
		
	}
	
	
}
