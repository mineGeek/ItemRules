package com.github.mineGeek.ItemRules.Rules;

import java.util.ArrayList;
import java.util.List;

import com.github.mineGeek.ItemRules.Integration.FactionsPlayer;
import com.github.mineGeek.ItemRules.Store.IRPlayer;



/**
 * Qualifies player against Factions settings.
 * Requires Factions. Dur.
 *
 */
public class ConditionFactions implements Applicator {

	
	/**
	 * List of factions this rule applies to
	 */
	private List<String> whitelist = new ArrayList<String>();
	
	
	/**
	 * List of factions exempt from rule
	 */
	private List<String> blacklist = new ArrayList<String>();
	
	
	/**
	 * condition for power level
	 */
	private ConditionBetween condition;
	
	
	/**
	 * Woah. A Constructor.
	 */
	public ConditionFactions() {}
	
	
	/**
	 * COnstructor taking whitelist
	 * @param whitelist
	 */
	public ConditionFactions( List<String> whitelist ) {
		this.setWhitelist( whitelist );
	}
	
	
	/**
	 * Constructor taking multi lists!
	 * @param whitelist
	 * @param blacklist
	 */
	public ConditionFactions( List<String> whitelist, List<String> blacklist ) {
		this.setWhitelist( whitelist );
		this.setBlacklist( blacklist );
	}
	
	/**
	 * Super dooper constructor
	 * @param whitelist
	 * @param blacklist
	 * @param minPower
	 * @param maxPower
	 */
	public ConditionFactions(List<String> whitelist, List<String>blacklist, Integer minPower, Integer maxPower ) {
		this.setWhitelist( whitelist );
		this.setBlacklist( blacklist );
		this.setPower( minPower, maxPower);
	}
	
	
	/**
	 * Set it yourself
	 * @param value
	 */
	public void setWhitelist(List<String>value ) {
		this.whitelist = value;
	}
	
	
	/**
	 * DIY in setting blacklists.
	 * @param value
	 */
	public void setBlacklist( List<String> value ) {
		this.blacklist = value;
	}
	
	
	/**
	 * Power level to apply
	 * @param min
	 * @param max
	 */
	public void setPower( Integer min, Integer max ) {
		this.condition = new ConditionBetween( min, max );
	}
	
	
	/**
	 * Returns ApplicationResult of evaluation
	 * @param value
	 * @return
	 */
	private ApplicationResult isApplicable( int power ) {
		
		if ( this.condition != null && this.condition.meetsRequirements( power ) ) {
			return ApplicationResult.NONE;
		} else {
			return ApplicationResult.YES;
		}
		
	}
	
	
	/**
	 * Returns ApplicationResult of evaluation
	 * @param value
	 * @return
	 */	
	@Override
	public ApplicationResult isApplicable(IRPlayer player) {
		
		String factionName = FactionsPlayer.getFactionName( player.getPlayer() );
		int power = FactionsPlayer.getPower( player.getPlayer() );
		
		if ( this.whitelist.contains( factionName ) ) {
			
			return isApplicable( power );
			
		} else if ( this.blacklist.contains( factionName ) ) {
			
			return ApplicationResult.NO;
			
		} else if ( this.blacklist.size() > 0 ) {
			
			return isApplicable( power );
			
		}
		

		
		if ( isApplicable( power ) == ApplicationResult.NONE ) return ApplicationResult.NO;
		
		return ApplicationResult.NONE;
		
		
	}

	
	/**
	 * Good Guy Bye
	 */
	@Override
	public void close() {
		if ( this.blacklist != null ) this.blacklist.clear();
		if ( this.whitelist != null ) this.whitelist.clear();
		
	}

	
	/**
	 * Returns ApplicationResult of evaluation
	 * @param value
	 * @return
	 */	
	@Override
	public ApplicationResult willBeApplicable(IRPlayer player) {
		
		return this.isApplicable( FactionsPlayer.getPower(player.getPlayer()) + 1);

	}

	
	/**
	 * Returns ApplicationResult of evaluation
	 * @param value
	 * @return
	 */	
	@Override
	public ApplicationResult wasApplicable(IRPlayer player) {
		return this.isApplicable( FactionsPlayer.getPower(player.getPlayer()) - 1 );
	}
	
	

}
