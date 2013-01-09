package com.github.mineGeek.ItemRules.Rules;
import com.github.mineGeek.ItemRules.Store.IRPlayer;

/**
 * Qualifies the player against their XP level
 *
 */
public class ConditionXP extends ConditionBetween implements Applicator {
	
	/**
	 * Constructor taking the min/max criteria
	 * @param min
	 * @param max
	 */
	public ConditionXP( Integer min, Integer max ) {
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
		
		return this.isApplicable( player.getXPLevel() );
		

	}

	/**
	 * Good Guy Closure
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

		return this.isApplicable( player.getXPLevel() + 1 );
		
	}

	/**
	 * Returns ApplicationResult of evaluation
	 * @param value
	 * @return
	 */	
	@Override
	public ApplicationResult wasApplicable(IRPlayer player) {
		return this.isApplicable( player.getXPLevel() - 1 );
	}



}
