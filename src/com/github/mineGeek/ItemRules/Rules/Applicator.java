package com.github.mineGeek.ItemRules.Rules;

import com.github.mineGeek.ItemRules.Store.IRPlayer;

public interface Applicator {

	public enum ApplicationResult {NONE, YES, NO};
	public ApplicationResult isApplicable( IRPlayer player );
	public ApplicationResult willBeApplicable( IRPlayer player );
	public ApplicationResult wasApplicable( IRPlayer player );
	public void close();
	
}
