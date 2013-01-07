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
	public ApplicationResult isApplicable(PlayerStoreItem player) {
		
		String name = player.getPlayer().getWorld().getName();
		boolean inList = this.isInList(name);
		if ( this.whitelistMode && inList ) return ApplicationResult.NONE;
		if ( !this.whitelistMode && inList ) return ApplicationResult.NO;
		
		return ApplicationResult.NONE;
	}
	
	@Override
	public void close() {
		super.close();
	}

}
