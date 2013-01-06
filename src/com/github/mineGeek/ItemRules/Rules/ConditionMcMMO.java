package com.github.mineGeek.ItemRules.Rules;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.github.mineGeek.ItemRules.Store.PlayerStoreItem;
import com.gmail.nossr50.datatypes.SkillType;
import com.gmail.nossr50.util.Users;

public class ConditionMcMMO implements Applicator {

	private Map<SkillType, ConditionBetween> skills = new HashMap<SkillType, ConditionBetween>();
	private boolean requiresParty = false;
	
	
	public ConditionMcMMO() {}
	
	public ConditionMcMMO( boolean requiresParty ) {
		this.requiresParty = requiresParty;
	}
	
	
	public void setRequiresParty( boolean value ) {
		this.requiresParty = value;
	}
	
	public boolean getRequiresParty() {
		return this.requiresParty;
	}
	
	public void add( SkillType skill, int min ) {
	
		this.skills.put( skill , new ConditionBetween( min, null ) );
		
	}
	
	@Override
	public Boolean isApplicable(PlayerStoreItem player) {
		
		boolean result = false;
		
		if ( this.requiresParty && !Users.getPlayer( player.getPlayer() ).inParty() ) return false;
		
		if ( !this.skills.isEmpty() ) {
			
			Iterator<Entry<SkillType, ConditionBetween>> skills = this.skills.entrySet().iterator();
			while ( skills.hasNext() ) {
				
				Entry<SkillType, ConditionBetween> row = skills.next();
				
				SkillType skill = row.getKey();
				ConditionBetween condition = row.getValue();
				
				result = result & !condition.isMinOk( Users.getPlayer(player.getPlayer()).getProfile().getSkillLevel( skill ) );
				
			}
			
		}
				
		return result;
	}
	

}
