package com.github.mineGeek.ItemRules.Rules;

import java.util.List;
import com.github.mineGeek.ItemRules.Store.PlayerStoreItem;

public class ConditionPerms extends ConditionStringList implements Applicator {

	private boolean appliesTo = true;
	
	public ConditionPerms( boolean appliesTo, List<String> perms ) {
		
		super( perms );
		this.appliesTo = appliesTo;
		
	}
	
	@Override
	public ApplicationResult isApplicable(PlayerStoreItem player) {
		
		if ( !this.list.isEmpty() ) {
			
			for ( String x : this.list ) {
				
				if ( player.getPlayer().hasPermission( x ) ) {
					
					if ( !this.appliesTo ) {
						return ApplicationResult.NO;
					} else {
						return ApplicationResult.YES;
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
