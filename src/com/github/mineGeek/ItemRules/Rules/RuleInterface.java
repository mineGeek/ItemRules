package com.github.mineGeek.ItemRules.Rules;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRules.ItemRules.Actions;

public interface RuleInterface {

	public void setActions(List<Actions> actions);
	public void setActionsFromStringList(List<String> actions);
	public void setItems(List<String> items);
	
	public Boolean appliesToWorldName( String value );	
	
	public Boolean appliesToItem( int itemId );
	public Boolean appliesToItem( int itemId, byte data );
	public Boolean appliesToItem( String itemId, String data );
	
	public boolean isApplicable( Actions action, Player player, Integer XPLevel, Integer ItemLevel );	
	
	public Boolean appliesToAction( Actions action );
	public boolean isActionApplicable( Actions action, Player player );
	
	public boolean isBypassed( Player player );
	
	public Boolean isRestricted( Player player );
	public Boolean isRestricted( Material material, byte data );
	public Boolean isRestricted( Player player, Material material, byte data );
	
	public void setWorldNames( List<String> value );
	
	public Boolean appliesToXPLevel( int value );
	public Boolean isXPMinTooHigh( int value );
	public Boolean isXPMaxTooLow( int value );
	public void setXPMin( Integer value );
	public void setXPMax( Integer value );
	public Boolean appliesToItemLevel( int value );
	public Boolean isItemLevelMinTooHigh( int value );
	public Boolean isItemLevelMaxTooLow( int value );
	public void setItemLevelMin( Integer value );
	public void setItemLevelMax( Integer value );
	
	public void setTag( String value );
	public void setDefaultValue( Boolean value );
	
	public String getRestrictionMessage();
	public void setRestrictionMessage( String value );
	
}
