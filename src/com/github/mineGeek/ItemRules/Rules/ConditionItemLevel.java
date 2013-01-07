package com.github.mineGeek.ItemRules.Rules;

import com.github.mineGeek.ItemRules.Store.PlayerStoreItem;

public class ConditionItemLevel extends ConditionBetween implements Applicator {
	
	public ConditionItemLevel( Integer min, Integer max ) {
		super(min, max);
	}	
	
	@Override
	public ApplicationResult isApplicable(PlayerStoreItem player) {
		
		int value = player.getItemLevel();
		
		if ( !this.meetsRequirements( value ) ) return ApplicationResult.YES;
		return ApplicationResult.NONE;
		
	}

	@Override
	public void close() {
		return;
		
	}	

}
