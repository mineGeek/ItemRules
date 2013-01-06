package com.github.mineGeek.ItemRules.Rules;

import com.github.mineGeek.ItemRules.Store.PlayerStoreItem;

public class ConditionItemLevel extends ConditionBetween implements Applicator {
	
	public ConditionItemLevel( Integer min, Integer max ) {
		super(min, max);
	}	
	
	@Override
	public Boolean isApplicable(PlayerStoreItem player) {
		
		int 	value = player.getItemLevel();
		boolean minOk = true;
		boolean maxOk = true;
		
		if ( this.min != null && this.min > 0 ) {
			if ( min > value ) minOk = false;
		}
		
		if ( this.max != null && this.max > 0 ) {
			if ( this.max < value ) maxOk = false;
		}
		
		return minOk && maxOk;

	}	

}
