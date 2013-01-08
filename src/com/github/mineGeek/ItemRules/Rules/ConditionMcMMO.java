package com.github.mineGeek.ItemRules.Rules;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

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
	
	private ApplicationResult isApplicable( Player player, int adjustedLevel ) {
		
		if ( !this.skills.isEmpty() ) {
			
			Iterator<Entry<SkillType, ConditionBetween>> skills = this.skills.entrySet().iterator();
			while ( skills.hasNext() ) {
				
				Entry<SkillType, ConditionBetween> row = skills.next();
				
				SkillType skill = row.getKey();
				ConditionBetween condition = row.getValue();
				
				if ( !condition.isMinOk( Users.getPlayer( player ).getProfile().getSkillLevel( skill ) + adjustedLevel ) ) {
					return ApplicationResult.YES;
				}
				
			}
			
		}
		
		return ApplicationResult.NONE;
		
	}
	
	@Override
	public ApplicationResult isApplicable(PlayerStoreItem player) {
		
		
		if ( this.requiresParty && !player.getMcMMOinParty() ) return ApplicationResult.YES;
		
		return this.isApplicable( player.getPlayer(), 0 );

	}

	@Override
	public void close() {
		this.skills.clear();
		
	}

	@Override
	public ApplicationResult willBeApplicable(PlayerStoreItem player) {
		return this.isApplicable(player.getPlayer(), 1 );
	}

	@Override
	public ApplicationResult wasApplicable(PlayerStoreItem player) {
		return this.isApplicable(player.getPlayer(), -1 );
	}
	

}
