package com.github.mineGeek.ItemRules.Rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRules.Config;
import com.github.mineGeek.ItemRules.Integration.FactionsPlayer;
import com.github.mineGeek.ItemRules.Integration.McMMOPlayer;
import com.github.mineGeek.ItemRules.Store.PlayerStoreItem;
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
	private static List<Rule> ruleList = new ArrayList<Rule>();
	
	
	/**
	 * main Map of rules accessible via their tag
	 * Key = rule.tag/ Value = rule
	 */
	private static Map<String, Rule> ruleByTagList = new HashMap<String, Rule>();
	
	
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
		/*
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
			
		} */
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
	 * Returns a list of all rules that apply to a player grouped by action
	 * @param player
	 * @param XPLevel
	 * @param itemLevel
	 * @return
	 */
	public static Map<String, RuleItem> getPlayerRules( Player player ) {
		
		Map<String, RuleItem> rules = new HashMap<String, RuleItem>();
		
		if ( !ruleList.isEmpty() ) {
			
			PlayerStoreItem ps = Players.get(player);

			/**
			 * Automatically apply rules
			 */
			
			for ( Rule x : Rules.ruleList ) {
		
				if ( x.getAuto() ) {
					
					if ( x.appliesToPlayer( ps ) ) {
						
						Map<String, RuleItem> r = x.getItems();
						
						if ( !r.isEmpty() ) {
							
							for( String y : r.keySet() ) {
								rules.put(y, r.get(y) );
							}
							
						}
						
						
					}
					
				}
			}
			
			/**
			 * Apply manual rule (may override existing rules)
			 */
			if ( !ps.getManualRules().isEmpty() ) {
			
				for ( String x : ps.getManualRules() ) {
					
					if ( Rules.ruleByTagList.containsKey( x ) ) {
					
						if ( Rules.ruleByTagList.get(x).appliesToPlayer( ps ) ) {
						
							Map<String, RuleItem> r = Rules.ruleByTagList.get(x).getItems();
							
							if ( !r.isEmpty() ) {
								
								for ( String y : r.keySet() ) {
									rules.put(y, r.get(y) );
								}
								
							}
							
						}
						
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
	public static Rule getRule( String tagName ) {
		return Rules.ruleByTagList.get( tagName );
	}
	
	
	/**
	 * Removes a rule from the list. Will no longer be applied to players
	 * @param rule
	 */
	public static void removeRule( Rule rule ) {
		
		rule.close();
		Rules.ruleByTagList.remove( rule.getTag() );
		Rules.ruleList.remove( rule );
		
	}
	
	
	/**
	 * Adds a rule to the Rules list
	 * @param rule
	 */
	public static void addRule( Rule rule ) {
		
		Rules.ruleByTagList.put( rule.getTag(), rule );
		Rules.ruleList.add( rule );		
	}
	

	public static void addRule( String tag, ConfigurationSection config ) {
		
		Rules.addRule( tag, config, false );
		
	}
	
	
	/**
	 * Build, then add a rule from the config file
	 * @param tag
	 * @param config
	 */
	public static void addRule( String tag, ConfigurationSection config, boolean manual ) {
		
		Rule rule = null;
		
		if ( config.contains("inherit") ) {
			
			rule = Rules.ruleByTagList.get( config.getString("inherit"));
			
			if ( rule != null ) {
				
				rule = new Rule( tag, rule );
				
			}
			
		}
		
		if ( rule == null ) {
			
			 rule = new Rule( tag );
			
		}
		
		if ( McMMOPlayer.enabled ) {
		
			if ( config.contains("mcmmo") ) {
				
				ConditionMcMMO mcMMO = new ConditionMcMMO( config.getBoolean("mcmmo.party", false) );
				
				if ( config.contains("mcmmo.skills")) {
					for ( String x : config.getConfigurationSection("mcmmo.skills").getKeys( false ) ) {
						mcMMO.add( SkillType.valueOf( x.toUpperCase() ), config.getInt("mcmmo.skills." + x, 0) );
					}
				}
			
				rule.addApplicator( mcMMO );
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
				
				if ( factions != null ) rule.addApplicator( factions );
			}
			
		}
		
		rule.setWhitelistItems( config.getBoolean("explicit", false ) );
		rule.setAuto( config.getBoolean("auto", !manual ) );
		rule.setDescription( config.getString("description", "") );
		
		if ( config.contains("actions") ) 	rule.setActions( config.getStringList("actions") );
		if ( config.contains("items") ) 	rule.setItems( config.getStringList("items") );
		List<String> list = config.contains("worlds") ? config.getStringList("worlds") : new ArrayList<String>();
		if ( !list.isEmpty() ) rule.addApplicator( new ConditionWorld( true, list));
		list = config.contains("excludeWorlds") ? config.getStringList("excludeWorlds") : new ArrayList<String>();
		if ( !list.isEmpty() ) rule.addApplicator( new ConditionWorld( false, list));
		
		rule.addApplicator( new ConditionXP( config.contains("XPMin") ? config.getInt("XPMin") : null, config.contains("XPMax") ? config.getInt("XPMax") : null ) );
		rule.addApplicator( new ConditionItemLevel( config.contains("itemLevelMin") ? config.getInt("itemLevelMin") : null, config.contains("itemLevelMax") ? config.getInt("itemLevelMax") : null ) );
		
		Rules.addRule( rule );
		
		
	}
	
	
	/**
	 * Returns number of rules loaded
	 * @return
	 */
	public static int count() {
		return Rules.ruleList.size();
	}
	
	
	public static void close() {
		
		if ( ! ruleList.isEmpty() ) {
			for ( Rule r : ruleList ) {
				r.close();
			}
		}
		
		ruleList.clear();
		ruleByTagList.clear();
		
	}
	
}
