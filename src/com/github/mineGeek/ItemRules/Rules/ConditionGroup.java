package com.github.mineGeek.ItemRules.Rules;

import java.util.List;

import org.bukkit.Bukkit;

import com.github.mineGeek.ItemRules.Integration.Vault;
import com.github.mineGeek.ItemRules.Store.IRPlayer;


/**
 * Qualifies person for rule based on if they are/arent in a permission group.
 * Requires Vault.
 *
 */
public class ConditionGroup extends ConditionStringList implements Applicator {

	
	/**
	 * White/Blacklist mode
	 */
	private boolean appliesTo = true;
	
	
	/**
	 * Constructor taking all params!
	 * @param appliesTo
	 * @param groups
	 */
	public ConditionGroup( boolean appliesTo, List<String> groups ) {
		
		super( groups );
		this.appliesTo = appliesTo;
		
	}
	
	
	/**
	 * Returns ApplicationResult of evaluation
	 * @param value
	 * @return
	 */	
	@Override
	public ApplicationResult isApplicable(IRPlayer player) {
		
		if ( !Vault.enabled ) {
			Bukkit.getLogger().info("WARNING!!! Using a group condition in itemRules, but Vault is missing or not working!");
			return ApplicationResult.NO;
		}
		
		if ( !this.list.isEmpty() ) {
			
			for ( String x : this.list ) {
				
				if ( Vault.inGroup( player.getPlayer(), x ) ) {
					
					if ( this.appliesTo ) {
						return ApplicationResult.YES;
					} else {
						return ApplicationResult.NO;								
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
