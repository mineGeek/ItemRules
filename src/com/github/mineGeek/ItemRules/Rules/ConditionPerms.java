package com.github.mineGeek.ItemRules.Rules;

import java.util.List;

import com.github.mineGeek.ItemRules.Integration.Vault;
import com.github.mineGeek.ItemRules.Store.IRPlayer;

/**
 * Condition to qualify if the user has one or more permissions
 *
 */
public class ConditionPerms extends ConditionStringList implements Applicator {

	/**
	 * White or blacklist mode
	 */
	private boolean appliesTo = true;
	
	
	/**
	 * Constructor with all args accepted
	 * @param appliesTo
	 * @param perms
	 */
	public ConditionPerms( boolean appliesTo, List<String> perms ) {
		
		super( perms );
		this.appliesTo = appliesTo;
		
	}
	
	/**
	 * Returns ApplicationResult of evaluation
	 * @param value
	 * @return
	 */	
	@Override
	public ApplicationResult isApplicable(IRPlayer player) {
		
		if ( !this.list.isEmpty() ) {
			
			for ( String x : this.list ) {
				
				if ( Vault.hasPerm( player.getPlayer(), x )  ) {
					
					if ( !this.appliesTo ) {
						return ApplicationResult.NO;
					} else {
						return ApplicationResult.YES;
					}
					
				}
				
			}
			
		}
		
		return this.appliesTo ? ApplicationResult.NO : ApplicationResult.YES;
	}
	
	/**
	 * Good Guy Close
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
		return isApplicable( player );
	}

	
	/**
	 * Returns ApplicationResult of evaluation
	 * @param value
	 * @return
	 */	
	@Override
	public ApplicationResult wasApplicable(IRPlayer player) {
		return isApplicable( player );
	}
	
	

}
