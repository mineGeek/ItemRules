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
	
	@Override
	public Boolean isApplicable(PlayerStoreItem player) {
		
		if ( !this.whitelist.contains( FPlayers.i.get( player.getPlayer() ).getFaction().getTag() ) ) return false;
		if ( this.blacklist.contains( FPlayers.i.get( player.getPlayer() ).getFaction().getTag() ) ) return false;
		
		return !this.condition.meetsRequirements( FPlayers.i.get( player.getPlayer()).getPowerRounded() );
		
	}
	

}
