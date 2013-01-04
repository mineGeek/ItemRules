package com.github.mineGeek.ItemRules.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRules.Config;
import com.github.mineGeek.ItemRules.ItemRules.Actions;
import com.github.mineGeek.ItemRules.Rules.RuleInterface;
import com.github.mineGeek.ItemRules.Rules.Rules;

public class UserStoreItem extends DataStore {
	
	Player player = null;
	
	Map<Actions, List<RuleInterface>> rules;
	Map<String, RuleInterface> manualRules = new HashMap<String, RuleInterface>();
	List<String> manualRuleList = new ArrayList<String>();
	
	public UserStoreItem( String dataFolder ) {
		super(dataFolder);
	}
	
	public UserStoreItem( String dataFolder, Player player ) {
		
		super( dataFolder );
		this.player = player;
		this.setFileName( player.getName() );
		
		this.load();
		
		if ( Config.ItemLevelDefaultsToXPLevel ) {
			this.setXPLevel( player.getLevel(), false );
		}
		
	}
	
	public void addManualRule( String tag ) {
		
		RuleInterface rule = Rules.getRule( tag );
		
		if ( rule != null ) {
			this.manualRuleList.add( tag );
			this.manualRules.put(tag ,rule);
		}
		
	}
	
	public void addManualRule( RuleInterface rule ) {
		this.manualRules.put( rule.getTag(), rule);
		this.manualRuleList.add( rule.getTag() );
	}
	
	public void removeManualRule( String tag ) {
		this.manualRules.remove(tag);
		this.manualRuleList.remove(tag);
	}
	
	public void clearManualRules() {
		this.manualRules.clear();
		this.manualRuleList.clear();
	}
	
	public void loadRules() {
		
		this.rules = Rules.getPlayerRules( this.player );
		
	}
	
	public void setManualList() {
		
		this.set("manualList", this.manualRuleList );
		
	}
	
	public void loadManualList() {
		
		this.manualRuleList = this.getAsStringList("manualList");
		
	}
	
	public Boolean hasManualRule( String value ) {
		
		if ( this.manualRules.containsKey( value ) ) return true;
		return false;
		
	}
	
	public boolean isRestricted( Actions action, Material material, byte data ) {
		
		if ( this.rules.containsKey( action ) ) {
			Iterator<RuleInterface> r = this.rules.get( action ).iterator();
			
			while ( r.hasNext() ) {
				
				if ( r.next().isRestricted( this.player, material, data) ) return true;
			}
			
		}
		
		return false;		
	}
	
	public void incrimentLevel( Integer level ) {
		
		this.setXPLevel( this.getXPLevel() + level );
		
	}
	
	public void incrimentLevel() {
		this.incrimentLevel( 1 );
	}
	
	public void setXPLevel( Integer level ) {
		this.setXPLevel(level, true);
	}
	
	public void setXPLevel( Integer level, Boolean refreshRules ) {
	
		
		this.set("XPLevel", level);
		Integer il = this.getItemLevel();
		if ( Config.XPLevelIncreasesItemLevel && level > il ) {
			this.setItemLevel(level);
		} else if ( Config.XPLossReducesItemLevel && level < il ) {
			this.setItemLevel(level);
		}
		
		if ( refreshRules)  loadRules();
	}
	
	
	public void setItemLevel( Integer level ) {
		this.set("itemLevel", level );
	}
	
	public Integer getItemLevel() {
		return this.getAsInteger("itemLevel", 0 );
	}
	
	public String getFactionName() {
		return "hi";
	}
	
	public double getFactionPowerMin() {
		return 0;
	}
	
	public double getFactionPowerMax() {
		return 0;
	}
	
	public void setPreviousLevel( Integer level ) {
		this.set("previousLevel", level);
	}
	
	public Integer getPreviousLevel() {
		return this.getAsInteger("previousLevel", 0);
	}
	
	public Integer getXPLevel() {
		return this.getAsInteger("XPLevel", 0 );
	}
	

}
