package com.github.mineGeek.ItemRules.Rules;

import java.util.List;

import com.github.mineGeek.ItemRules.Store.PlayerStoreItem;

public class ConditionWorld extends ConditionStringList implements Applicator {

	private boolean applyTo = true;
	
	public ConditionWorld( boolean applyTo, List<String> worldNames ) {
		
		super( worldNames );
		this.applyTo = applyTo;
		
	}
	
	@Override
	public ApplicationResult isApplicable(PlayerStoreItem player) {
		
		String name = player.getPlayer().getWorld().getName();
		boolean inList = this.isInList( name );
		
		if ( this.applyTo && inList ) 	return ApplicationResult.NONE;
		if ( !this.applyTo && inList ) 	return ApplicationResult.NO;
		
		return ApplicationResult.NONE;
	}
	
	@Override
	public void close() {
		super.close();
	}

	@Override
	public ApplicationResult willBeApplicable(PlayerStoreItem player) {
		return this.isApplicable( player );
	}

	@Override
	public ApplicationResult wasApplicable(PlayerStoreItem player) {
		return this.isApplicable( player );
	}
	
	

}
