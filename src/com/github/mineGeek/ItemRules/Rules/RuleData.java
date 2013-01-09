package com.github.mineGeek.ItemRules.Rules;

import com.github.mineGeek.ItemRules.ItemRules.Actions;

public class RuleData {

	public boolean value = false;
	public String ruleTag;
	
	public RuleData( String tag ) {
		this.ruleTag = tag;
	}
	
	public RuleData( String tag, boolean value ) {
		this.ruleTag = tag;
		this.value = value;
	}
	
	public boolean isRestricted( Actions action ) {
		
		if ( Rules.getRule( this.ruleTag ).appliesToAction(action) ) {
			return this.value;
		}
		
		return false;
		
	}
	
	public String getRestrictionMessage() {
		return Rules.getRule( this.ruleTag ).getRestrictedMessage();
	}
	
}
