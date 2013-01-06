package com.github.mineGeek.ItemRules.Rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRules.ItemRules.Actions;
import com.github.mineGeek.ItemRules.Rules.RuleCollection.RuleRangeType;
import com.github.mineGeek.ItemRules.Store.Players;


/**
 * Base rule
 *
 */
public class Rule {
	
	
	/**
	 * Tag (unique name) for rule
	 */
	private String 	tag;
	
	
	/**
	 * The minimum XP to qualify for rule
	 */
	private Integer XPMin;
	
	
	/**
	 * The maximum XP to qualify for rule
	 */
	private Integer XPMax;
	
	
	/**
	 * The minimum item level to qualify for rule
	 */
	private Integer itemLevelMin;
	
	
	/**
	 * The maximum item level to qualify for rule
	 */
	private Integer itemLevelMax;
	
	
	/**
	 * ArrayList<String> of world names this rule applies to
	 */
	private	List<String> worldNames = new ArrayList<String>();
	
	
	/**
	 * ArrayList<String> of names this rule is explicitly excluded from
	 */
	private	List<String> excludeWorldNames = new ArrayList<String>();
	
	
	/**
	 * Holds mcMMO skills and level requirements for rule
	 */
	private Map<String, Integer> mcMMOSkills = new HashMap<String, Integer>();
	
	
	/**
	 * If this rule requires an mcmmo party
	 */
	private Boolean mcMMOpartyRequired = false;
	
	
	/**
	 * The textual summary of rule. Shown to player when summarising what rules apply
	 */
	private String 	description;
	
	
	/**
	 * ArrayList<String> of items applicable to rule. Format is "5" or "5.0" or "5.2"
	 * If just "5", rule will apply to ALL wooden planks. "5.2" only to birch planks
	 */
	private List<String> items = new ArrayList<String>();
	
	
	/**
	 * Holds and stores rules XP and ItemLevel requirements
	 */
	private RuleCollection RangeChecks = new RuleCollection();
	
	
	/**
	 * ArrayList<Actions> that apply to rule
	 */
	private List<Actions> actions = new ArrayList<Actions>();
	

	/**
	 * The textual message sent to use when they are explicitly restricted.
	 * place holders can be used in text: e.g. %1$s = action + material
	 */
	private String restrictedMessage;
	

	/**
	 * The textual message sent to user when they are explicitly not restricted
	 * format as per restrictedMessage
	 */
	private String unrestrictedMessage;
	
	
	/**
	 * Automatically add this rule (when it applies) to player. Set auto: true if 
	 * this is a manually controlled rule
	 */
	private Boolean autoAdd = true;
	

	/**
	 * New form of DefaultValue. Allow = true will 
	 */
	private Boolean allow = false;
		
	
	/**
	 * Look. A constructor.
	 */
	public Rule() {	}

	
	/**
	 * Returns unique tag for rule
	 */
	public String getTag() {
		return tag;
	}


	/**
	 * Sets unique tag for rule
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	
	/**
	 * Returns textual summary of rule to be shown to player when... er...
	 * summarising rules.
	 */
	public String getDescription() {
		return this.getFormattedMessage( this.description, "");
	}
	
	
	/**
	 * Sets textual summary of rule that is shown to player when summarising
	 */
	public void setDescription(String value ) {
		this.description = value;
	}
	
	
	/**
	 * Returns Allow setting
	 */
	public Boolean getAllow() {
		return this.allow;
	}	
	
	
	/**
	 * Sets the Allow setting.
	 */
	public void setAllow( Boolean value ) {
		this.allow = value;
	}
	
	
	/**
	 * Returns AutoAdd. If true, this rule will auto apply
	 * to applicable players. Set to False to make this a 
	 * manually applied rule
	 */
	public Boolean getAutoAdd() {
		return this.autoAdd;
	}	
	
	
	/**
	 * Sets AutoAdd. If true, this rule will auto apply
	 * to applicable players. Set to False to make this a 
	 * manually applied rule
	 */
	public void setAutoAdd( Boolean value ) {
		this.autoAdd = value;
	}
		
	
	public RuleCollection getRanges() {
		return this.RangeChecks;
	}
	
	public void addRangeCheck( RuleRangeType type, Integer min, Integer max ) {
		this.RangeChecks.add( type, new RuleRange( min, max ) );
	}
	
	
	public void clearRangeChecks() {
		this.RangeChecks.clear();		
	}
	
	
	/**
	 * Returns a string of applicable actions. Used within player messages
	 * @return
	 */
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
	
		
	/**
	 * Sets the message users recieve when getting a message
	 * that this rule is unrestricted.
	 * Optional placeholders:
	 * %1$s = Action and/or Material
	 * %2$d = Minimum XP required
	 * %3$d = Maximum XP required
	 * %4$d = Minimum item level required
	 * %5$d = Maximum item level required
	 * If format if screwed, the unformatted text will be returned.
	 */
	public void setUnrestrictedMessage( String value ) {
		this.unrestrictedMessage = value;
	}
	

	/**
	 * Returns a formatted unrestricted message optionally including
	 * the material this message applies to
	 * @param material
	 * @return
	 */
	public String getUnrestrictedMessage( String material ) {
		return this.getFormattedMessage( this.unrestrictedMessage, material );
	}
	

	/**
	 * Returns formatted unrestricted message but with no parameters
	 */
	public String getUnrestrictedMessage() {
		return this.getFormattedMessage( this.unrestrictedMessage, "");
	}
	
	
	/**
	 * Sets the unrestricted message
	 * Optional placeholders:
	 * %1$s = Action and/or Material
	 * %2$d = Minimum XP required
	 * %3$d = Maximum XP required
	 * %4$d = Minimum item level required
	 * %5$d = Maximum item level required
	 * If format if screwed, the unformatted text will be returned.
	 */
	public void setRestrictedMessage( String value ) {
		this.restrictedMessage = value;
	}
	
	
	/**
	 * Returns the formatted text a user is shown when they are restricted
	 * from this rule.
	 */
	public String getRestrictedMessage() {
		return this.getRestrictedMessage( "" );
	}
	

	/**
	 * Returns a formatted string used in displaying messages to user.
	 * Optional placeholders:
	 * %1$s = Action and/or Material
	 * %2$d = Minimum XP required
	 * %3$d = Maximum XP required
	 * %4$d = Minimum item level required
	 * %5$d = Maximum item level required
	 * If format if screwed, the unformatted text will be returned.
	 * @param message
	 * @param ActionMaterial
	 * @return
	 */
	private String getFormattedMessage( String message, String ActionMaterial ) {
		
		try {
			List<Object> tokens = new ArrayList<Object>();
			tokens.add( ActionMaterial );
			tokens.add( ( this.getXPMin() != null ? this.getXPMin() : 0 ) );
			tokens.add( ( this.getXPMax() != null ? this.getXPMax() : 0 ) );
			tokens.add( ( this.getItemLevelMin() != null ? this.getItemLevelMin() : 0 ) );
			tokens.add( ( this.getItemLevelMax() != null ? this.getItemLevelMax() : 0 ) );
			return String.format( message, tokens.toArray() );
		} catch ( Exception e ) {
			return message;
		}
		
	}
	
	
	/**
	 * Returns a formatted restriction message based on the action and/or material
	 * Optional placeholders:
	 * %1$s = Action and/or Material
	 * %2$d = Minimum XP required
	 * %3$d = Maximum XP required
	 * %4$d = Minimum item level required
	 * %5$d = Maximum item level required
	 * If format if screwed, the unformatted text will be returned.
	 */
	public String getRestrictedMessage( Actions action, Material material ) {
		return this.getFormattedMessage( this.restrictedMessage, action.toString().toLowerCase() + " " + material.toString().toLowerCase().replace("_", " ") );
	}
	

	/**
	 * Returns a formatted restriction message based on the material
	 * Optional placeholders:
	 * %1$s = Action and/or Material
	 * %2$d = Minimum XP required
	 * %3$d = Maximum XP required
	 * %4$d = Minimum item level required
	 * %5$d = Maximum item level required
	 * If format if screwed, the unformatted text will be returned.
	 */
	public String getRestrictedMessage( String material ) {
		return this.getFormattedMessage( this.restrictedMessage, this.getActionsAsString() + (material.length() > 0 ? " " + material : "" ) );
	}

	
	/**
	 * Explicitly sets the world names this rule applies to
	 */
	public void setWorldNames( List<String> value ) {
		this.worldNames = value;
	}
	
	
	/**
	 * Add a world to the rule whitelist 
	 * @param value
	 */
	public void addWorldNames( String value ) {
		this.worldNames.add( value.toLowerCase() );
	}
	
	
	/**
	 * Returns ArrayList<String> of worlds rule applies to
	 * @return
	 */
	public List<String> getWorldNames() {
		return this.worldNames;
	}
	

	/**
	 * Empties out all whitelisted worlds from rule
	 */
	public void clearWorldNames() {
		this.worldNames.clear();
	}
	
	
	/**
	 * Sets the ArrayList<String> of worlds this rule will not load for
	 * @param value
	 */
	public void setExcludeWorldNames( List<String> value ) {
		this.excludeWorldNames = value;
	}
	

	/**
	 * Add a world to the exclusion list
	 * @param value
	 */
	public void addExcludeWorldNames( String value ) {
		this.excludeWorldNames.add( value );
	}
	

	/**
	 * Empties out the excluded world list
	 */
	public void clearExcludeWorldNames() {
		this.excludeWorldNames.clear();
	}
	
	
	/**
	 * Returns true if rule applies to value
	 */
	public Boolean appliesToWorldName( String value ) {
		
		if ( this.worldNames.isEmpty() && this.excludeWorldNames.isEmpty() ) return true;
		return this.worldNames.contains( value.toLowerCase() ) && ! this.excludeWorldNames.contains( value.toLowerCase() );
		
	}
	
	
	/**
	 * Returns true if value is within the itemlevel min/max range
	 */
	public Boolean passesItemLevel( int value ) {
		
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
	
	
	/**
	 * Returns true if value is higher than XPMin
	 */
	public Boolean isXPMinTooHigh( int value ) {
		
		if ( this.getXPMin() != null ) {
			if ( this.getXPMin() > value ) return true;
		}
		
		return false;
		
	}
	
	
	/**
	 * Returns true if value is lower than the XPMax (if set and more than 0)
	 */
	public Boolean isXPMaxTooLow( int value ) {
		
		if ( this.getXPMax() != null ) {
			if ( this.getXPMax() < value ) return true;
		}
		
		return false;
		
	}	
	

	/**
	 * Returns true if value is within the XP range of rule
	 */
	public Boolean passesXPLevel( int value ) {
		
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
	
	
	/**
	 * Returns true if value is more than the XP minimum for rule
	 */
	public Boolean isItemLevelMinTooHigh( int value ) {
		
		if ( this.getItemLevelMin() != null ) {
			return this.getItemLevelMin() > value;
		}
		
		return false;
		
	}
	
	
	/**
	 * Returns true if value is less than the XP Max for rule (if set)
	 */
	public Boolean isItemLevelMaxTooLow( int value ) {
		
		if ( this.getItemLevelMax() != null ) {
			return this.getItemLevelMax() < value ;
		}
		return false;
	}

	
	/**
	 * Returns true if the item and data passed is applicable to rule
	 */
	public Boolean appliesToItem( String itemId, String data ) {
		
		if ( this.items.contains( itemId + "." + data ) ) return true;
		return this.items.contains( itemId );
		
	}
	
	
	/**
	 * Returns true if the itemid and data passed is applicable to rule 
	 */
	public Boolean appliesToItem( int itemId, byte data ) {
		return appliesToItem( String.valueOf( itemId ), String.valueOf( data ) );
	}
	
	
	/**
	 * Returns true if the itemid passed is applicable to rule
	 */
	public Boolean appliesToItem( int itemId ) {
		return this.appliesToItem( String.valueOf( itemId ), "");
	}

	
	/**
	 * Returns list of all items applied to rule
	 * @return
	 */
	public List<String> getItems() {
		return items;
	}

	
	/**
	 * Sets all the items this rule applies to in one call
	 */
	public void setItems(List<String> items) {
		this.items = items;
	}	

	
	/**
	 * Returns true if action passed is applicable to rule
	 */
	public Boolean appliesToAction( Actions action ) {
		return this.actions.contains(action);
	}
	
	
	/**
	 * Returns full list of Actions applied to rule
	 * @return
	 */
	public List<Actions> getActions() {
		return actions;
	}
	
	
	/**
	 * Sets all applicable actions in one call. Neato
	 */
	public void setActions(List<Actions> actions) {
		this.actions = actions;
	}
	
	
	/**
	 * Add an action to the rule
	 * @param action
	 */
	public void addAction( Actions action ) {
		this.actions.add( action );
	}
	
	
	/**
	 * Set actions from a list of strings
	 */
	public void setActionsFromStringList( List<String> actions ) {
		
		if ( !actions.isEmpty() ) {
			for( String x : actions ) {
				this.addAction( Actions.valueOf(x) );
			}
		}
		
	}	


	/**
	 * Sets the minimum item level applicable to rule
	 */
	public void setItemLevelMin( Integer value ) {
		this.itemLevelMin = value;
	}
	
	
	/**
	 * Returns the minimum item level
	 * @return
	 */
	public Integer getItemLevelMin() {
		return this.itemLevelMin;
	}
	
	
	/**
	 * Sets the maximum item level
	 */
	public void setItemLevelMax( Integer value ) {
		this.itemLevelMax = value;
	}

	
	/**
	 * Returns the maximum item level
	 * @return
	 */
	public Integer getItemLevelMax() {
		return this.itemLevelMax;
	}
	
	
	/**
	 * Sets the minimum XP level
	 */
	public void setXPMin( Integer value ) {
		this.XPMin = value;
	}
	

	/**
	 * Returns the Maximum XP required
	 * @return
	 */
	public Integer getXPMin() {
		return this.XPMin;
	}
	

	/**
	 * Sets the maximum XP required (null or 0 to not apply)
	 */
	public void setXPMax( Integer value ) {
		this.XPMax = value;
	}

	
	/**
	 * Returns max XP required
	 * @return
	 */
	public Integer getXPMax() {
		return this.XPMax;
	}	
	
	
	
	public void addMcMMOSkill( String skill, Integer minLevel ) {
		this.mcMMOSkills.put(skill, minLevel );
	}
	
	
	
	public Boolean isMcMMOSkillApplicable( String skill ) {
		
		return this.mcMMOSkills.containsKey( skill );
		
	}
	
	
	
	
	public Boolean passesMcMMOSkillRequirement(Map<String, Integer> skills ) {
		
		Boolean result = true;
		
		if ( !this.mcMMOSkills.isEmpty() ) {
			Iterator<Entry<String, Integer>> i = this.mcMMOSkills.entrySet().iterator();
			result = false;
			while ( i.hasNext() ) {
				
				Entry<String, Integer> s = i.next();
				if ( skills.containsKey( s.getKey() ) ) {
					return s.getValue() <= skills.get(s.getKey() );
				}
			}
		}
		
		
		return result;
	}
	
	
	
	
	public Boolean appliesToMcMMOParty() {
		return this.mcMMOpartyRequired;
	}
	
	
	
	
	public void setRequiresMcMMOParty( Boolean value ) {
		this.mcMMOpartyRequired = value;
	}
	
	
	
	
	
	public Boolean isInMcMMOParty(Player player) {
		return Players.get(player).getMcMMOinParty();
	}
	
	
	
	
	public Boolean passesMcMMO( Player player ) {
		
		if ( !this.mcMMOpartyRequired && this.mcMMOSkills.isEmpty() ) return true;
		if ( this.mcMMOpartyRequired && !this.isInMcMMOParty(player) ) return false;
		return this.passesMcMMOSkillRequirement( Players.get(player).getMcMMOLevels() );
		
	}
	
	

	/**
	 * Returns true if player has itemRules.bypass.TAGNAME perm or itemRules.bypass.*
	 */
	public boolean isBypassed( Player player ) {
		
		return player.hasPermission("itemRules.bypass." + this.getTag() );
		
	}
	
	
	
	
	/**
	 * Returns true if action passed applies. Hmmm.
	 */
	public boolean isActionApplicable( Actions action, Player player ) {
		
		if ( this.isBypassed( player ) ) return false;
		if ( !this.actions.contains( action ) ) return false;
		
		return true;
	}
	
	
	
	
	
	/**
	 * Returns true if the rule applies to parameters ( but doesn't check item)
	 */
	public boolean isApplicable( Actions action, Player player, Integer XPLevel, Integer itemLevel ) {
		
		if ( !this.isActionApplicable(action, player) ) return false;
		if ( this.passesXPLevel( XPLevel ) && this.passesItemLevel( itemLevel ) ) return false;
		
		return true;
		
	}
	
	
	
	
	
	/**
	 * FIIK why this is like this. To be overridden?
	 * TODO: Investigate. 
	 */
	public Boolean isRestricted( Player player ) {
		
		return false;
		
	}
	
	
	
	
	/**
	 * Returns true ( or NOT Allow()) if rule is applied to item passed
	 */
	public Boolean isRestricted( Material material, byte data ) {
		
		if ( this.appliesToItem( material.getId(), data ) ) {
			return !this.getAllow();
		}
		return false;
		
	}
	
	
	
	
	
	/**
	 * Returns true ( or NOT allow() ) if material/item is restricted
	 */
	public Boolean isRestricted( Player player, Material material, byte data ) {
		
		Boolean result = !this.getAllow();
		
		if ( this.passesXPLevel( player.getLevel() ) ) {
			if ( this.passesItemLevel( Players.get(player).getItemLevel() ) ) {
				result = this.appliesToItem( material.getId(), data );
			}
		}
		
		return result;
		
	}
	
}
