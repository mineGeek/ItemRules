package com.github.mineGeek.ItemRules.Store;

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
	
	public UserStoreItem( String dataFolder ) {
		super(dataFolder);
	}
	
	public UserStoreItem( String dataFolder, Player player ) {
		
		super( dataFolder );
		this.player = player;
		this.setFileName( player.getName() );
		
		this.load();
		
		if ( Config.ItemLevelDefaultsToXPLevel ) {
			this.setXPLevel( player.getLevel() );
		} else {
			
			this.loadRules();
		}		
		
	}
	
	public void loadRules() {
		
		this.rules = Rules.getPlayerRules( this.player );
		
	}
	
	public boolean isRestricted( Actions action, Material material, byte data ) {
		
		if ( this.rules.containsKey( action ) ) {
			Iterator<RuleInterface> r = this.rules.get( action ).iterator();
			
			while ( r.hasNext() ) {
				
				if ( r.next().isRestricted(material, data) ) return true;
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
	
		
		this.set("XPLevel", level);
		Integer il = this.getItemLevel();
		if ( Config.XPLevelIncreasesItemLevel && level > il ) {
			this.setItemLevel(level);
		} else if ( Config.XPLossReducesItemLevel && level < il ) {
			this.setItemLevel(level);
		}
		
		this.loadRules();
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
