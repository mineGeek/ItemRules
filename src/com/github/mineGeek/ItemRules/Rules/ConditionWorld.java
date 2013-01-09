package com.github.mineGeek.ItemRules.Rules;

import java.util.List;

import com.github.mineGeek.ItemRules.Store.IRPlayer;

/**
 * Qualifies the user against a list of world names.
 *
 */
public class ConditionWorld extends ConditionStringList implements Applicator {

	private boolean applyTo = true;
	
	/**
	 * Constructor that takes a list of worldnames to check at a later date
	 * ApplyTo is a white/blacklist mode
	 * @param applyTo
	 * @param worldNames
	 */
	public ConditionWorld( boolean applyTo, List<String> worldNames ) {
		
		super( worldNames );
		this.applyTo = applyTo;
		
	}
	
	
	/**
	 * Returns ApplicationResult of evaluation
	 * @param value
	 * @return
	 */	
	@Override
	public ApplicationResult isApplicable(IRPlayer player) {
		
		String name = player.getPlayer().getWorld().getName();
		boolean inList = this.isInList( name );
		
		if ( this.applyTo && inList ) 	return ApplicationResult.NONE;
		if ( !this.applyTo && inList ) 	return ApplicationResult.NO;
		
		return ApplicationResult.NONE;
	}
	
	
	/**
	 * Good Guy Close.
	 */
	@Override
	public void close() {
		super.close();
	}

	
	/**
	 * Returns ApplicationResult of evaluation
	 * @param value
	 * @return
	 */	
	@Override
	public ApplicationResult willBeApplicable(IRPlayer player) {
		return this.isApplicable( player );
	}

	
	/**
	 * Returns ApplicationResult of evaluation
	 * @param value
	 * @return
	 */	
	@Override
	public ApplicationResult wasApplicable(IRPlayer player) {
		return this.isApplicable( player );
	}
	
	

}
