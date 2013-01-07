package com.github.mineGeek.ItemRules.Rules;

import com.github.mineGeek.ItemRules.ItemRules.Actions;

public class RuleItem {

	private Rule rule;
	
	public RuleItem() {}

	
	public RuleItem( Rule rule ) {
		this.rule = rule;
	}
	
	public void setRule( Rule rule ) {
		this.rule = rule;
	}
	
	public boolean isWhitelisted() {
		return this.rule.getWhitelistItems();
	}
	
	public String getTag() {
		return this.rule.getTag();
	}
	
	
	public boolean isRestricted( Actions action ) {
		
		if ( this.rule.appliesToAction(action) ) return !this.isWhitelisted();
		return this.isWhitelisted();
		
	}
	
}
