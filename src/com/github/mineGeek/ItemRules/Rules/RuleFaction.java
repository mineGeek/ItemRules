package com.github.mineGeek.ItemRules.Rules;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRules.ItemRules.Actions;
import com.github.mineGeek.ItemRules.Store.Users;

public class RuleFaction extends RuleBase {

	private List<String> factionNames = new ArrayList<String>();
	private double powerMin = 0;
	private double powerMax = 0;
	private boolean powerMinSet = false;
	private boolean powerMaxSet = false;
	
	public RuleFaction(RuleBase rule) {
		super(rule);
	}
	
	public RuleFaction() {
		// TODO Auto-generated constructor stub
	}

	public void setFactionNames( List<String> factions ) {
		this.factionNames = factions;
	}
	
	public void addFactionName( String name ) {
		this.factionNames.add( name );
	}
	
	public void clearFactionNames() {
		this.factionNames.clear();
	}
	
	public void removeFactionName( String name ) {
		this.factionNames.remove( name );
	}

	public double getPowerMin() {
		return powerMin;
	}

	public void setPowerMin(double powerMin) {
		this.powerMin = powerMin;
		this.powerMinSet = true;
	}

	public double getPowerMax() {
		return powerMax;
	}

	public void setPowerMax(double powerMax) {
		this.powerMax = powerMax;
		this.powerMaxSet = true;
	}
	
	public Boolean appliesToPowerMin( double value ) {
		
		boolean minOk = true;
		
		if ( this.powerMinSet ) {
			if ( this.getPowerMin() < value ) minOk = false;
		}
		
		return minOk;
		
	}
	
	public Boolean appliesToPowerMax( double value ) {
		
		boolean maxOk = true;
		if ( this.powerMaxSet ) {
			if ( this.getPowerMax() > value ) maxOk = false;
		}
		
		return maxOk;
		
	}
	
	public Boolean appliesToPower( double min, double max ) {
		
		return this.appliesToPowerMin( min ) && this.appliesToPowerMax( max );
		
	}
	
	public Boolean appliesToPower( Player player ) {
		
		return this.appliesToPowerMax( Users.get(player).getFactionPowerMin() ) && this.appliesToPowerMax( Users.get(player).getFactionPowerMax() );
		
	}
	
	@Override
	public boolean isApplicable( Actions action, Player player, Integer XPLevel, Integer itemLevel ) {
		
		if( super.isApplicable(action, player, XPLevel, itemLevel ) ) {
		
			if ( this.factionNames.contains( Users.get( player ).getFactionName() ) ) {
				
				if ( this.appliesToPower( Users.get(player).getFactionPowerMin(), Users.get(player).getFactionPowerMax() ) ) {
					
					return true;
					
				}
				
			}
		}
		
		return false;
	}
	
	@Override
	public Boolean isRestricted( Player player, Material material, byte data ) {
		
		if ( !super.isRestricted( material, data) ) {
			return this.appliesToPower( player );
		}
		
		return false;
		
	}	
	

}
