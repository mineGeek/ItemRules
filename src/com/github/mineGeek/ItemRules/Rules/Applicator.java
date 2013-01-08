package com.github.mineGeek.ItemRules.Rules;

import com.github.mineGeek.ItemRules.Store.PlayerStoreItem;

public interface Applicator {

	public enum ApplicationResult {NONE, YES, NO};
	public ApplicationResult isApplicable( PlayerStoreItem player );
	public ApplicationResult willBeApplicable( PlayerStoreItem player );
	public ApplicationResult wasApplicable( PlayerStoreItem player );
	public void close();
	
}
