package com.github.mineGeek.ItemRules.Rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.github.mineGeek.Integration.FactionsPlayer;
import com.github.mineGeek.Integration.McMMOPlayer;
import com.github.mineGeek.ItemRules.Config;
import com.github.mineGeek.ItemRules.ItemRules.Actions;
import com.github.mineGeek.ItemRules.Rules.RuleCollection.RuleRangeType;
import com.github.mineGeek.ItemRules.Store.Players;
import com.gmail.nossr50.datatypes.SkillType;


/**
 * Static object for interacting with rules
 *
 */
public class Rules {


	/**
	 * ArrayList of all rules
	 */
	private static List<RuleNew> ruleList = new ArrayList<RuleNew>();
	
	
	/**
	 * main Map of rules accessible via their tag
	 * Key = rule.tag/ Value = rule
	 */
	private static Map<String, RuleNew> ruleByTagList = new HashMap<String, RuleNew>();
	
	
	/**
	 * Gets a formatted string of rules that apply to player. Sort of a shite function.
	 * @param player
	 * @param doCan
	 * @param doCanNow
	 * @param doNext
	 * @param canColor
	 * @param nextColor
	 * @param cannotColor
	 * @return
	 */
	public static String getRuleList( Player player, Boolean doCan, Boolean doCanNow, Boolean doNext, ChatColor canColor, ChatColor nextColor, ChatColor cannotColor ) {

		List<String> can = new ArrayList<String>();
		List<String> cannot = new ArrayList<String>();
		List<String> next = new ArrayList<String>();

		if ( !ruleList.isEmpty() ) {
			
			for ( Rule x : Rules.ruleList ) {
				
				if ( x.getAutoAdd() || Players.get(player).hasManualRule( x.getTag() ) ) {
					if ( x.appliesToWorldName( player.getWorld().getName() ) ) {
				
						int il = Players.get(player).getItemLevel();
						int xp = Players.get(player).getXPLevel();
						
						if ( x.passesItemLevel( il ) && x.passesXPLevel( xp ) && ( doCan || doCanNow ) ) {
							
							if ( doCanNow ) {
								if ( x.passesItemLevel( il - 1 ) || x.passesXPLevel( xp-1)) can.add( x.getDescription() );
							} else {
								can.add( x.getDescription() );
							}
							
						} else {
							
							if ( x.passesItemLevel( il + 1 ) && x.passesXPLevel( xp + 1 ) && doNext ) {
								next.add( x.getDescription() );
							} else if ( !x.passesItemLevel( il ) && !x.passesXPLevel( xp ) && ( !doCan ) ){
								cannot.add( x.getDescription() );
							}
							
						}
						
						
					}
					
				}
				
			}
			
		}
		String r = "";
		Boolean o = false;
		
		if ( can.size() > 0 ) {
			
			r = canColor + Config.txtCanDoPrefix;
			for( String x : can ) {
				
				if ( o ) r = r + ", ";
				o = true;
				r = r + x;
			}
			r = r + ". ";
			o = false;
		}
		
		if ( next.size() > 0 ) {
			
			if ( r.length() > 0 ) r = r + " ";

			r = r + nextColor + Config.txtCanDoNextPrefix ;
			for ( String x : next ) {
				if ( o ) r = r + ", ";
				o = true;
				r = r + x;
			}
			
			r = r + ". ";
			o = false;
		}
		
		if ( cannot.size() > 0 ) {
			
			if ( r.length() > 0 ) r = r + " ";
			
			r = r + cannotColor + Config.txtCannotDoPrefix;
			for ( String x: cannot ) {
				if ( o ) r = r + ", ";
				o = true;
				r = r + x ;
			}
			
			r = r + ". ";
		}
		
		return r;
		
	}
	

	/**
	 * Gets list of all rules applicable to player grouped by action for quick reference
	 * @param player
	 * @return
	 */
	public static Map<Actions, List<Rule>> getPlayerRules( Player player ) {
		return Rules.getPlayerRules(player, Players.get(player).getXPLevel(), Players.get(player).getItemLevel() );
	}
	

	/**
	 * Returns a list of all rules that apply to a player grouped by action
	 * @param player
	 * @param XPLevel
	 * @param itemLevel
	 * @return
	 */
	public static Map<Actions, List<Rule>> getPlayerRules( Player player, Integer XPLevel, Integer itemLevel ) {
		
		Map< Actions, List<Rule>> actionRules = new HashMap< Actions, List<Rule>>();
		if ( !ruleList.isEmpty() ) {
			
			for ( Rule x : Rules.ruleList ) {
				
				if ( x.appliesToWorldName( player.getWorld().getName() ) ) {
					
					List<Rule> ruleSet;
					
					
					ruleSet = Rules.getPlayerRulesForAction(Actions.BREAK, player, XPLevel, itemLevel);
					if ( ruleSet != null ) actionRules.put( Actions.BREAK, ruleSet );
					
					ruleSet = Rules.getPlayerRulesForAction(Actions.CRAFT, player, XPLevel, itemLevel);
					if ( ruleSet != null ) actionRules.put( Actions.CRAFT, ruleSet );
					
					ruleSet = Rules.getPlayerRulesForAction(Actions.PICKUP, player, XPLevel, itemLevel);
					if ( ruleSet != null ) actionRules.put( Actions.PICKUP, ruleSet );					
					
					ruleSet = Rules.getPlayerRulesForAction(Actions.PLACE, player, XPLevel, itemLevel);
					if ( ruleSet != null ) actionRules.put( Actions.PLACE, ruleSet );					
					
					ruleSet = Rules.getPlayerRulesForAction(Actions.USE, player, XPLevel, itemLevel);
					if ( ruleSet != null ) actionRules.put( Actions.USE, ruleSet );
					
				}
				
			}
			
			
		}
		
		return actionRules;
		
	}

	
	/**
	 * Returns list of rules for a specific action. Used by getPlayerRules
	 * @param action
	 * @param player
	 * @param XPLevel
	 * @param itemLevel
	 * @return
	 */
	public static List<Rule> getPlayerRulesForAction( Actions action, Player player, Integer XPLevel, Integer itemLevel ) {
		
		List<Rule> rules = new ArrayList<Rule>();
		
		if ( !Rules.ruleList.isEmpty() ) {
			
			for ( RuleNew x : Rules.ruleList ) {
				
				if ( x.getAutoAdd() || Players.get(player).hasManualRule(x.getTag())) {
					if ( x.isApplicable(action, player, XPLevel, itemLevel ) ) {
						rules.add( x );						
					}
				}
			}
		}
		
		return rules;
	}
	

	/**
	 * Return a rule by its tag name
	 * @param tagName
	 * @return
	 */
	public static RuleNew getRule( String tagName ) {
		return Rules.ruleByTagList.get( tagName );
	}
	
	
	/**
	 * Removes a rule from the list. Will no longer be applied to players
	 * @param rule
	 */
	public static void removeRule( Rule rule ) {
		
		Rules.ruleByTagList.remove( rule.getTag() );
		Rules.ruleList.remove( rule );
		
	}
	
	
	/**
	 * Adds a rule to the Rules list
	 * @param rule
	 */
	public static void addRule( RuleNew rule ) {
		
		Rules.ruleByTagList.put( rule.getTag(), rule );
		Rules.ruleList.add( rule );		
	}
	

	/**
	 * Build, then add a rule from the config file
	 * @param tag
	 * @param config
	 */
	public static void addRule( String tag, ConfigurationSection config ) {
		
		RuleNew rule = new RuleNew( tag );
				
		if ( McMMOPlayer.enabled ) {
		
			if ( config.contains("mcmmo") ) {
				
				ConditionMcMMO mcMMO = new ConditionMcMMO( config.getBoolean("mcmmo.party", false) );
				
				if ( config.contains("mcmmo.skills")) {
					for ( String x : config.getConfigurationSection("mcmmo.skills").getKeys( false ) ) {
						mcMMO.add( SkillType.valueOf( x ), config.getInt("mcmmo.skills." + x, 0) );
					}
				}
				
			}
			
		}
		
		if ( FactionsPlayer.enabled ) {
			
			if ( config.contains("factions") ) {
				
				ConditionFactions factions = new ConditionFactions();
				
				if ( config.contains("factions.whitelisted") ) {
					factions.setWhitelist( config.getStringList("factions.whitelisted") );
				}
				
				if ( config.contains("factions.blacklisted") ) {
					factions.setBlacklist( config.getStringList("factions.blacklisted") );
				}
				
				if ( config.contains("factions.maxPower") || config.contains("factions.minPower")) {
					factions.setPower( config.getInt("factions.minPower"), config.getInt("factions.maxPower") );
				}
				
			}
			
		}
		
		rule.setWhitelistItems( config.getBoolean("explicit", false ) );
		rule.setAuto( config.getBoolean("auto", true ) );
		rule.setDescription( config.getString("description", "") );
		
		if ( config.contains("actions") ) 	rule.setActions( config.getStringList("actions") );
		if ( config.contains("items") ) 	rule.setItems( config.getStringList("items") );
		if ( config.contains("worlds") )	rule.addApplicator( new ConditionWorld( true, config.getStringList("worlds")));
		if ( config.contains("excludeworlds") )	rule.addApplicator( new ConditionWorld( false, config.getStringList("excludeworlds")));
		
		rule.addApplicator( new ConditionXP( config.getInt("XPMin"), config.getInt("XPMax") ) );
		rule.addApplicator( new ConditionItemLevel( config.getInt("itemLevelMin"), config.getInt("itemLevelMax") ) );
		
		Rules.addRule( rule );
		
		
	}
	
	
	/**
	 * Returns number of rules loaded
	 * @return
	 */
	public static int count() {
		return Rules.ruleList.size();
	}
	
	
}
