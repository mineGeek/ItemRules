package com.github.mineGeek.ItemRules.Rules;


import com.github.mineGeek.ItemRules.Store.PlayerStoreItem;

public class ConditionItemLevel extends ConditionBetween implements Applicator {
	
	public ConditionItemLevel( Integer min, Integer max ) {
		super(min, max);
	}	

	private ApplicationResult isApplicable( int value ) {
		
		if ( !this.meetsRequirements( value ) ) return ApplicationResult.YES;
		return ApplicationResult.NONE;		
		
	}
	
	@Override
	public ApplicationResult isApplicable(PlayerStoreItem player) {
		
		return this.isApplicable( player.getItemLevel() );
		
	}

	@Override
	public void close() {
		return;
		
	}

	@Override
	public ApplicationResult willBeApplicable(PlayerStoreItem player) {
		return this.isApplicable( player.getItemLevel() + 1 );
	}

	@Override
	public ApplicationResult wasApplicable(PlayerStoreItem player) {
		return this.isApplicable( player.getItemLevel() - 1 );
	}
	

}
