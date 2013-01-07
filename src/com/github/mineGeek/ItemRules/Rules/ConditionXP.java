package com.github.mineGeek.ItemRules.Rules;


import com.github.mineGeek.ItemRules.Store.PlayerStoreItem;

public class ConditionXP extends ConditionBetween implements Applicator {
	
	public ConditionXP( Integer min, Integer max ) {
		super(min, max);
	}	
	
	@Override
	public ApplicationResult isApplicable(PlayerStoreItem player) {
		
		Integer value = player.getXPLevel();
		
		if ( !this.meetsRequirements( value ) ) return ApplicationResult.YES;
		return ApplicationResult.NONE;
		

	}

	@Override
	public void close() {
		return;
		
	}


}
