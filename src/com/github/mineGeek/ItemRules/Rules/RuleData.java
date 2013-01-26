package com.github.mineGeek.ItemRules.Rules;

import com.github.mineGeek.ItemRules.ItemRules.Actions;

/**
 * Utility/Construct for an applied Rule
 *
 */
public class RuleData {

	/**
	 * Value returned from evaluating this item
	 * true = item is restricted. False. Not.
	 */
	public boolean value = false;
	
	
	/**
	 * The tag referencing the rule this came from
	 * For wuick lookup to get descriptions and such
	 */
	public String ruleTag;
	
	
	public RuleData( RuleData clone ) {
		
		this.value = clone.value;
		this.ruleTag = clone.ruleTag;
		
	}
	
	/**
	 * Constructor taking the tag from the rule 
	 * that created this.
	 * @param tag
	 */	
	public RuleData( String tag ) {
		this.ruleTag = tag;
	}
	
	
	/**
	 * Constructor taking tag and value in one call
	 * @param tag
	 * @param value
	 */
	public RuleData( String tag, boolean value ) {
		this.ruleTag = tag;
		this.value = value;
	}
	
	
	/**
	 * Guts of construct which will ultimatley decide
	 * if an action is restricted.
	 * @param action
	 * @return
	 */
	public boolean isRestricted( Actions action ) {
		
		if ( Rules.getRule( this.ruleTag ).appliesToAction(action) ) {
			return this.value;
		}
		
		return false;
		
	}
	
	
	/**
	 * Convenience method for denial
	 * @return
	 */
	public String getRestrictionMessage() {
		return Rules.getRule( this.ruleTag ).getRestrictedMessage();
	}
	
}
