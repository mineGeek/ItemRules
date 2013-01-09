package com.github.mineGeek.ItemRules.Rules;

import java.util.List;

import org.bukkit.Bukkit;

import com.github.mineGeek.ItemRules.Integration.Vault;
import com.github.mineGeek.ItemRules.Store.PlayerStoreItem;

public class ConditionGroup extends ConditionStringList implements Applicator {

	private boolean appliesTo = true;
	
	public ConditionGroup( boolean appliesTo, List<String> groups ) {
		
		super( groups );
		this.appliesTo = appliesTo;
		
	}
	
	@Override
	public ApplicationResult isApplicable(PlayerStoreItem player) {
		
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
		
		return this.appliesTo ? ApplicationResult.NO : ApplicationResult.NONE;
	}
	
	@Override
	public void close() {
		super.close();
	}

	@Override
	public ApplicationResult willBeApplicable(PlayerStoreItem player) {
		return isApplicable( player );
	}

	@Override
	public ApplicationResult wasApplicable(PlayerStoreItem player) {
		return isApplicable( player );
	}
	
	

}
