package com.github.mineGeek.ItemRules.Rules;

import java.util.List;

import com.github.mineGeek.ItemRules.Store.PlayerStoreItem;

public class ConditionWorld extends ConditionStringList implements Applicator {

	private boolean whitelistMode = true;
	
	public ConditionWorld( boolean whitelistMode, List<String> worldNames ) {
		
		super( worldNames );
		this.whitelistMode = whitelistMode;
		
	}
	
	@Override
	public Boolean isApplicable(PlayerStoreItem player) {
		
		String name = player.getPlayer().getWorld().getName();
		
		if ( this.whitelistMode ) return this.isInList( name );
		if ( !this.whitelistMode ) return this.isNotInList( name );
		
		return false;
	}

}
