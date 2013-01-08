package com.github.mineGeek.ItemRules.Rules;

import java.util.ArrayList;
import java.util.List;
import com.github.mineGeek.ItemRules.Store.PlayerStoreItem;
import com.massivecraft.factions.FPlayers;

public class ConditionFactions implements Applicator {

	
	private List<String> whitelist = new ArrayList<String>();
	private List<String> blacklist = new ArrayList<String>();
	private ConditionBetween condition;
	
	
	public ConditionFactions() {}
	
	public ConditionFactions( List<String> whitelist ) {
		this.setWhitelist( whitelist );
	}
	
	public ConditionFactions( List<String> whitelist, List<String> blacklist ) {
		this.setWhitelist( whitelist );
		this.setBlacklist( blacklist );
	}
	
	public ConditionFactions(List<String> whitelist, List<String>blacklist, Integer minPower, Integer maxPower ) {
		this.setWhitelist( whitelist );
		this.setBlacklist( blacklist );
		this.setPower( minPower, maxPower);
	}
	
	public void setWhitelist(List<String>value ) {
		this.whitelist = value;
	}
	
	public void setBlacklist( List<String> value ) {
		this.blacklist = value;
	}
	
	public void setPower( Integer min, Integer max ) {
		this.condition = new ConditionBetween( min, max );
	}
	
	private ApplicationResult isApplicable( int power ) {
		
		if ( this.condition != null && this.condition.meetsRequirements( power ) ) {
			return ApplicationResult.NONE;
		} else {
			return ApplicationResult.YES;
		}
		
	}
	
	@Override
	public ApplicationResult isApplicable(PlayerStoreItem player) {
		
		String factionName = FPlayers.i.get( player.getPlayer() ).getFaction().getTag();
		int power = FPlayers.i.get( player.getPlayer()).getPowerRounded();
		
		if ( this.blacklist.contains( factionName ) ) {
			return ApplicationResult.NO;
		}		
		
		if ( this.whitelist.contains( factionName ) ) {
			
			return isApplicable( power );
			
		}
		
		if ( isApplicable( power ) == ApplicationResult.NONE ) return ApplicationResult.NO;
		
		return ApplicationResult.NONE;
		
		
	}

	@Override
	public void close() {
		this.blacklist.clear();
		this.whitelist.clear();
		
	}

	@Override
	public ApplicationResult willBeApplicable(PlayerStoreItem player) {
		
		return this.isApplicable( FPlayers.i.get( player.getPlayer()).getPowerRounded() + 1 );

	}

	@Override
	public ApplicationResult wasApplicable(PlayerStoreItem player) {
		return this.isApplicable( FPlayers.i.get( player.getPlayer()).getPowerRounded() - 1 );
	}
	

}
