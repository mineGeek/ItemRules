package com.github.mineGeek.ItemRules.Rules;

import com.github.mineGeek.ItemRules.Store.IRPlayer;


/**
 * Qualifies player on their itemlevel (really for when XP alone isn't enough!)
 *
 */
public class ConditionItemLevel extends ConditionBetween implements Applicator {
	
	
	/**
	 * Constructor taking min/max values
	 * @param min
	 * @param max
	 */
	public ConditionItemLevel( Integer min, Integer max ) {
		super(min, max);
	}	

	
	/**
	 * Returns ApplicationResult of evaluation
	 * @param value
	 * @return
	 */	
	private ApplicationResult isApplicable( int value ) {
		
		if ( !this.meetsRequirements( value ) ) return ApplicationResult.YES;
		return ApplicationResult.NONE;		
		
	}
	
	
	/**
	 * Returns ApplicationResult of evaluation
	 * @param value
	 * @return
	 */	
	@Override
	public ApplicationResult isApplicable(IRPlayer player) {
		
		return this.isApplicable( player.getItemLevel() );
		
	}

	
	/**
	 * Good Guys ByeBye
	 */
	@Override
	public void close() {
		return;
		
	}

	
	/**
	 * Returns ApplicationResult of evaluation
	 * @param value
	 * @return
	 */	
	@Override
	public ApplicationResult willBeApplicable(IRPlayer player) {
		return this.isApplicable( player.getItemLevel() + 1 );
	}

	
	/**
	 * Returns ApplicationResult of evaluation
	 * @param value
	 * @return
	 */	
	@Override
	public ApplicationResult wasApplicable(IRPlayer player) {
		return this.isApplicable( player.getItemLevel() - 1 );
	}
	

}
