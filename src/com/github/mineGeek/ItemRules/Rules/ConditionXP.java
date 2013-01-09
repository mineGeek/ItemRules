package com.github.mineGeek.ItemRules.Rules;



import com.github.mineGeek.ItemRules.Store.PlayerStoreItem;

public class ConditionXP extends ConditionBetween implements Applicator {
	
	public ConditionXP( Integer min, Integer max ) {
		super(min, max);
	}	
	
	private ApplicationResult isApplicable( int value ) {
		
		if ( !this.meetsRequirements( value ) ) return ApplicationResult.YES;
		return ApplicationResult.NONE;		
		
	}	
	
	@Override
	public ApplicationResult isApplicable(PlayerStoreItem player) {
		
		return this.isApplicable( player.getXPLevel() );
		

	}

	@Override
	public void close() {
		return;
		
	}

	@Override
	public ApplicationResult willBeApplicable(PlayerStoreItem player) {
		ApplicationResult a = this.isApplicable( player.getXPLevel() + 1 );
		return a;
		//if (  a == ApplicationResult.NONE || a == ApplicationResult.YES ) return ApplicationResult.YES;
		//return ApplicationResult.NO;
		
	}

	@Override
	public ApplicationResult wasApplicable(PlayerStoreItem player) {
		return this.isApplicable( player.getXPLevel() - 1 );
	}



}
