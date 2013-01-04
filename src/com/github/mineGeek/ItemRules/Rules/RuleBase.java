package com.github.mineGeek.ItemRules.Rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRules.ItemRules.Actions;
import com.github.mineGeek.ItemRules.Store.Users;


public class RuleBase implements RuleInterface {

	private String 	tag;
	private Integer XPMin;
	private Integer XPMax;
	private Integer itemLevelMin;
	private Integer itemLevelMax;
	private	List<String> worldNames = new ArrayList<String>();
	private	List<String> excludeWorldNames = new ArrayList<String>();
	private String 	description;
	private Boolean defaultValue;
	private List<String> items = new ArrayList<String>();
	private List<Actions> actions = new ArrayList<Actions>();
	private String restrictedMessage;
	private String unrestrictedMessage;
	private Boolean autoAdd = true;
	
	
	public RuleBase( RuleBase rule ) {
		
		this.items = new ArrayList<String>();
		this.setTag( rule.getTag() );
		this.setDescription( rule.getDescription() );
		this.setXPMin( rule.getXPMin() );
		this.setXPMax( rule.getXPMax() );
		
		this.setDefaultValue( rule.getDefaultValue() );
		this.setItems( rule.getItems() );
		
	}
	
	
	public RuleBase() {
		// TODO Auto-generated constructor stub
	}


	public String getTag() {
		return tag;
	}




	public void setTag(String tag) {
		this.tag = tag;
	}


	
	public void setAutoAdd( Boolean value ) {
		this.autoAdd = value;
	}
	
	public Boolean getAutoAdd() {
		return this.autoAdd;
	}


	public String getDescription() {
		return description;
	}




	public void setDescription(String description) {
		this.description = description;
	}


	public String getActionsAsString() {
		
		String result = null;
		Iterator<Actions> i = this.actions.iterator();
		
		while ( i.hasNext() ) {
			String y = i.next().toString().toLowerCase();
			if ( result == null ) {
				result = y;
			} else if ( i.hasNext() ) {
				result = result + ", " + y;
			} else {
				result = result + " & " + y;
			}
			
		}
		return result;
	}
	
	public void setUnrestrictedMessage( String value ) {
		this.unrestrictedMessage = value;
	}
	
	public String getUnrestrictedMessage( String material ) {
		return this.getFormattedMessage( this.unrestrictedMessage, material );
	}
	
	public String getUnrestrictedMessage() {
		return this.getFormattedMessage( this.unrestrictedMessage, "");
	}
	
	public void setRestrictedMessage( String value ) {
		this.restrictedMessage = value;
	}
	
	public String getRestrictedMessage() {
		return this.getRestrictedMessage( "" );
	}
	
	private String getFormattedMessage( String message, String material ) {
		
		try {
			List<Object> tokens = new ArrayList<Object>();
			tokens.add( this.getActionsAsString() + (material.length() > 0 ? " " + material : "" ) );
			tokens.add( ( this.getXPMin() != null ? this.getXPMin() : 0 ) );
			tokens.add( ( this.getXPMax() != null ? this.getXPMax() : 0 ) );
			tokens.add( ( this.getItemLevelMin() != null ? this.getItemLevelMin() : 0 ) );
			tokens.add( ( this.getItemLevelMax() != null ? this.getItemLevelMax() : 0 ) );
			return String.format( message, tokens.toArray() );
		} catch ( Exception e ) {
			return message;
		}
		
	}
	
	public String getRestrictedMessage( String material ) {
		return this.getFormattedMessage( this.restrictedMessage, material );
	}

	public void setWorldNames( List<String> value ) {
		this.worldNames = value;
	}
	
	public void addWorldNames( String value ) {
		this.worldNames.add( value.toLowerCase() );
	}
	
	public List<String> getWorldNames() {
		return this.worldNames;
	}
	
	public void clearWorldNames() {
		this.worldNames.clear();
	}
	
	public void setExcludeWorldNames( List<String> value ) {
		this.excludeWorldNames = value;
	}
	
	public void addExcludeWorldNames( String value ) {
		this.excludeWorldNames.add( value );
	}
	
	public void clearExcludeWorldNames() {
		this.excludeWorldNames.clear();
	}
	
	public Boolean appliesToWorldName( String value ) {
		
		if ( this.worldNames.isEmpty() && this.excludeWorldNames.isEmpty() ) return true;
		return this.worldNames.contains( value.toLowerCase() ) && ! this.excludeWorldNames.contains( value.toLowerCase() );
		
	}
	
	
	public Boolean appliesToItemLevel( int value ) {
		
		boolean minOk = true;
		boolean maxOk = true;
		
		if ( this.getItemLevelMin() != null && this.getItemLevelMin() > 0 ) {
			if ( this.getItemLevelMin() > value ) minOk = false;
		}
		
		if ( this.getItemLevelMax() != null && this.getItemLevelMax() > 0 ) {
			if ( this.getItemLevelMax() < value ) maxOk = false;
		}
		
		return minOk && maxOk;		
		
	}
	
	public Boolean isXPMinTooHigh( int value ) {
		
		if ( this.getXPMin() != null ) {
			if ( this.getXPMin() > value ) return true;
		}
		
		return false;
		
	}
	
	public Boolean isXPMaxTooLow( int value ) {
		
		if ( this.getXPMax() != null ) {
			if ( this.getXPMax() < value ) return true;
		}
		
		return false;
		
	}	
	
	public Boolean appliesToXPLevel( int value ) {
		
		boolean minOk = true;
		boolean maxOk = true;
		
		if ( this.getXPMin() != null  ) {
			if ( this.getXPMin() > value ) minOk = false;
		}
		
		if ( this.getXPMax() != null && this.getXPMax() > 0 ) {
			if ( this.getXPMax() < value ) maxOk = false;
		}
		
		return minOk && maxOk;
		
	}
	
	public Boolean isItemLevelMinTooHigh( int value ) {
		
		if ( this.getItemLevelMin() != null ) {
			return this.getItemLevelMin() > value;
		}
		
		return false;
		
	}
	
	public Boolean isItemLevelMaxTooLow( int value ) {
		
		if ( this.getItemLevelMax() != null ) {
			return this.getItemLevelMax() < value ;
		}
		return false;
	}
	

	protected Boolean getDefaultValue() {
		return defaultValue;
	}




	public void setDefaultValue(Boolean value) {
		this.defaultValue = value;
	}


	
	public Boolean appliesToItem( String itemId, String data ) {
		if ( this.items.contains( itemId + "." + data ) ) return true;
		return this.items.contains( itemId );		
	}
	
	
	
	public Boolean appliesToItem( int itemId, byte data ) {
		return appliesToItem( String.valueOf( itemId ), String.valueOf( data ) );
	}
	
	public Boolean appliesToItem( int itemId ) {
		return this.appliesToItem( String.valueOf( itemId ), "");
	}
	


	public List<String> getItems() {
		return items;
	}


	public void setItems(List<String> items) {
		this.items = items;
	}	



	public Boolean appliesToAction( Actions action ) {
		return this.actions.contains(action);
	}
	
	
	public List<Actions> getActions() {
		return actions;
	}



	public void setActions(List<Actions> actions) {
		this.actions = actions;
	}
	
	public void addAction( Actions action ) {
		this.actions.add( action );
	}
	
	public void setActionsFromStringList( List<String> actions ) {
		
		if ( !actions.isEmpty() ) {
			for( String x : actions ) {
				this.addAction( Actions.valueOf(x) );
			}
		}
		
	}	

	public void setItemLevelMin( Integer value ) {
		this.itemLevelMin = value;
	}
	
	
	public Integer getItemLevelMin() {
		return this.itemLevelMin;
	}
	
	public void setItemLevelMax( Integer value ) {
		this.itemLevelMax = value;
	}
	
	public Integer getItemLevelMax() {
		return this.itemLevelMax;
	}
	
	
	public void setXPMin( Integer value ) {
		this.XPMin = value;
	}
	
	
	
	public Integer getXPMin() {
		return this.XPMin;
	}
	
	
	public void setXPMax( Integer value ) {
		this.XPMax = value;
	}
	
	
	
	public Integer getXPMax() {
		return this.XPMax;
	}	
	

	public boolean isBypassed( Player player ) {
		
		Boolean ted = player.hasPermission("itemRules.bypass." + this.getTag() );
		return ted;
		
		
	}
	
	
	public boolean isActionApplicable( Actions action, Player player ) {
		
		if ( this.isBypassed( player ) ) return false;
		if ( !this.actions.contains( action ) ) return false;
		
		return true;
	}
	
	public boolean isApplicable( Actions action, Player player, Integer XPLevel, Integer itemLevel ) {
		
		if ( !this.isActionApplicable(action, player) ) return false;
		if ( !this.appliesToXPLevel( XPLevel ) ) return false;
		if ( !this.appliesToItemLevel( itemLevel ) ) return false;
		
		return true;
		
	}
	
	public Boolean isRestricted( Player player ) {
		
		return false;
		
	}
	
	public Boolean isRestricted( Material material, byte data ) {
		
		return this.appliesToItem( material.getId(), data );
		
	}
	
	public Boolean isRestricted( Player player, Material material, byte data ) {
		
		if ( this.appliesToXPLevel( player.getLevel() ) ) {
			if ( this.appliesToItemLevel( Users.get(player).getItemLevel() ) ) {
				return this.appliesToItem( material.getId(), data );
			}
		}
		
		return false;
		
	}
	
}
