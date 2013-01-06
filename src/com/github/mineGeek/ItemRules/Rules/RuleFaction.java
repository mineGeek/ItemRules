package com.github.mineGeek.ItemRules.Rules;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRules.ItemRules.Actions;
import com.github.mineGeek.ItemRules.Store.Players;





/**
 * Factions specific rules
 *
 */
public class RuleFaction extends Rule {

	
	
	
	
	/**
	 * List of faction names this rule applies to
	 */
	private List<String> factionNames = new ArrayList<String>();
	
	
	
	
	
	/**
	 * The power levels this rule applies to
	 */
	private double powerMin = 0;
	private double powerMax = 0;
	private boolean powerMinSet = false;
	private boolean powerMaxSet = false;
	
	
	
	
	
	/**
	 * Constructor
	 */
	public RuleFaction() {
		super();
	}
	

	
	
	
	
	/**
	 * sets list of faction names to this rule
	 * @param factions
	 */
	public void setFactionNames( List<String> factions ) {
		this.factionNames = factions;
	}
	
	
	
	
	
	/**
	 * Add a faction name to this rule
	 * @param name
	 */
	public void addFactionName( String name ) {
		this.factionNames.add( name );
	}
	
	
	
	
	
	/**
	 * Clears all faction names from rule
	 */
	public void clearFactionNames() {
		this.factionNames.clear();
	}
	
	
	
	
	/**
	 * Removes a specific faction name from rule
	 * @param name
	 */
	public void removeFactionName( String name ) {
		this.factionNames.remove( name );
	}

	
	
	
	
	/**
	 * Returns minimum power level required by rule
	 * @return
	 */
	public double getPowerMin() {
		return powerMin;
	}

	
	
	
	
	/**
	 * Sets Minium Power for rule
	 * @param powerMin
	 */
	public void setPowerMin(double powerMin) {
		this.powerMin = powerMin;
		this.powerMinSet = true;
	}

	
	
	
	
	/**
	 * Returns Max Power for rule
	 * @return
	 */
	public double getPowerMax() {
		return powerMax;
	}

	
	
	
	
	/**
	 * Sets Max Power for rule
	 * @param powerMax
	 */
	public void setPowerMax(double powerMax) {
		this.powerMax = powerMax;
		this.powerMaxSet = true;
	}
	
	
	
	
	
	/**
	 * Returns true if value applies to Min Power
	 * @param value
	 * @return
	 */
	public Boolean appliesToPowerMin( double value ) {
		
		boolean minOk = true;
		
		if ( this.powerMinSet ) {
			if ( this.getPowerMin() < value ) minOk = false;
		}
		
		return minOk;
		
	}
	
	
	
	
	
	/**
	 * Returns True is value applies to Max Power
	 * @param value
	 * @return
	 */
	public Boolean appliesToPowerMax( double value ) {
		
		boolean maxOk = true;
		if ( this.powerMaxSet ) {
			if ( this.getPowerMax() > value ) maxOk = false;
		}
		
		return maxOk;
		
	}
	
	
	
	
	
	/**
	 * Convienince method. Will return true if Min or Max values
	 * apply to this rule
	 * @param min
	 * @param max
	 * @return
	 */
	public Boolean appliesToPower( double min, double max ) {
		
		return this.appliesToPowerMin( min ) && this.appliesToPowerMax( max );
		
	}
	
	
	
	
	
	/**
	 * Convienence method will return true if Players power settings apply to rule
	 * @param player
	 * @return
	 */
	public Boolean appliesToPower( Player player ) {
		
		return this.appliesToPowerMax( Players.get(player).getFactionPowerMin() ) && this.appliesToPowerMax( Players.get(player).getFactionPowerMax() );
		
	}
	
	
	
	
	
	/**
	 * Returnd true if rule applies to action and player
	 */
	@Override
	public boolean isApplicable( Actions action, Player player, Integer XPLevel, Integer itemLevel ) {
		
		if( super.isApplicable(action, player, XPLevel, itemLevel ) ) {
		
			if ( this.factionNames.contains( Players.get( player ).getFactionName() ) ) {
				
				if ( this.appliesToPower( Players.get(player).getFactionPowerMin(), Players.get(player).getFactionPowerMax() ) ) {
					
					return true;
					
				}
				
			}
		}
		
		return false;
	}
	
	
	
	
	
	/**
	 * Returns true if player is restricted from using material/item
	 */
	@Override
	public Boolean isRestricted( Player player, Material material, byte data ) {
		
		if ( !super.isRestricted( material, data) ) {
			return this.appliesToPower( player );
		}
		
		return false;
		
	}
	
	
	

}
