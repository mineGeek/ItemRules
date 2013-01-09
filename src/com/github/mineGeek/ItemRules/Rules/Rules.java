package com.github.mineGeek.ItemRules.Rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRestrictions.Utilities.Config;
import com.github.mineGeek.ItemRules.Integration.FactionsPlayer;
import com.github.mineGeek.ItemRules.Integration.McMMOPlayer;
import com.github.mineGeek.ItemRules.Rules.Rule.AppliesToMode;
import com.github.mineGeek.ItemRules.Rules.Rule.RuleMode;
import com.github.mineGeek.ItemRules.Store.IRPlayer;
import com.github.mineGeek.ItemRules.Store.Players;
import com.gmail.nossr50.datatypes.SkillType;


/**
 * Static object for interacting with rules
 *
 */
public class Rules {


	/**
	 * Stores aliases between tags and lists of item id's. eg. 'woodenstuff' => 268, 269
	 */
	public static Map<String, List<String>> itemAliases = new HashMap<String, List<String>>();
	
	
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
	 * Gets a formatted string of rules that apply to player.
	 * Sort of a shite (AKA messy ) function.
	 * @param player
	 * @param doCan
	 * @param doCanNow
	 * @param doNext
	 * @param canColor
	 * @param nextColor
	 * @param cannotColor
	 * @return
	 */
	public static List<String> getRuleList( Player player, Boolean doCan, Boolean doCanNow, Boolean doNext, ChatColor canColor, ChatColor nextColor, ChatColor cannotColor ) {

		List<String> can = new ArrayList<String>();
		List<String> cannot = new ArrayList<String>();
		List<String> next = new ArrayList<String>();
		
		boolean applies;
		boolean applied;
		boolean appliesNext;
		String 	not;
		String	is;
		
		if ( !ruleList.isEmpty() ) {
			
			IRPlayer ps = Players.get(player);
			
			for ( Rule x : Rules.ruleList ) {
		
				if ( x.getAuto() ) {
					
					applied 	= x.appliesToPlayer( ps, AppliesToMode.PREVIOUS );
					applies 	= x.appliesToPlayer( ps );
					appliesNext = x.appliesToPlayer( ps, AppliesToMode.NEXT );
					is 			= x.getRestrictedMessage();
					not			= x.getUnrestrictedMessage();
					
					if ( applies ) {
					
						if ( !appliesNext && doNext ) { //will not be applied next level!
							
							next.add( not );
							
						} else if ( !doCan ) {
							
							cannot.add( is );
						}
					} else if ( doCan ){
					
						can.add( not );
						
						if ( doNext && appliesNext ) {
							next.add( is );
						} else if ( doCanNow && !applied ) {
							//is new
							can.add( not );
						}
						
					}
					
				}
				
			}

		}
		
		String r = "";
		List<String> result = new ArrayList<String>();
		Boolean o = false;
		
		if ( can.size() > 0 ) {
			
			r = canColor + Config.txtCanDoPrefix;
			for( String x : can ) {
				
				if ( o ) r = r + ", ";
				o = true;
				r = r + x;
			}
			r = r + ". ";
			result.add( r );
			r = "";
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
			result.add( r );
			r = "";
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
			
			result.add( r );
			r = r + ". ";
		}
		
		return result;
		
	}
		

	/**
	 * Returns a list of all rules that apply to a player
	 * @param player
	 * @param XPLevel
	 * @param itemLevel
	 * @return
	 */
	public static Map<String, RuleData> getPlayerRules( Player player ) {
		
		Map<String, RuleData> rules = new HashMap<String, RuleData>();
		RuleMode mode = RuleMode.DEFAULT;
		
		if ( !ruleList.isEmpty() ) {
			
			IRPlayer ps = Players.get(player);

			/**
			 * Automatically apply rules
			 */
			
			for ( Rule x : Rules.ruleList ) {
		
				if ( x.getAuto() ) {
					
					if ( x.appliesToPlayer( ps ) ) {
						
						if ( x.getRuleMode() != RuleMode.DEFAULT ) {
							mode = x.getRuleMode();
							if ( mode == RuleMode.ALLOWPREVIOUS || mode == RuleMode.DENYPREVIOUS ) {
								boolean newValue = mode == RuleMode.ALLOWPREVIOUS ? true : false;
								if ( !rules.isEmpty() ) {
									for ( RuleData data : rules.values() ) {
										data.value = newValue;
									}
								}
								mode = mode == RuleMode.ALLOWPREVIOUS ? RuleMode.ALLOW : RuleMode.DENY;
							}
						}
						
						Map<String, RuleData> r = x.getAllowedItems();
						
						if ( !r.isEmpty() ) {
							
							for( String y : r.keySet() ) {
								rules.put(y, r.get(y) );
							}
							
						}
						
						r = x.getRestrictedItems();
						
						if ( !r.isEmpty() ) {
							
							for ( String y : r.keySet() ) {
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
						
							Map<String, RuleData> r = Rules.ruleByTagList.get(x).getAllowedItems();							
							
							if ( Rules.ruleByTagList.get(x).getRuleMode() != RuleMode.DEFAULT ) {
								mode = Rules.ruleByTagList.get(x).getRuleMode();
								if ( mode == RuleMode.ALLOWPREVIOUS || mode == RuleMode.DENYPREVIOUS ) {
									boolean newValue = mode == RuleMode.ALLOWPREVIOUS ? true : false;
									if ( !rules.isEmpty() ) {
										for ( RuleData data : rules.values() ) {
											data.value = newValue;
										}
									}
									mode = mode == RuleMode.ALLOWPREVIOUS ? RuleMode.ALLOW : RuleMode.DENY;
								}								
							}
							
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
		Players.get(player).setRuleMode(mode);
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
				
				if ( config.contains("factions.appliesTo") ) {
					factions.setWhitelist( config.getStringList("factions.appliesTo") );
				}
				
				if ( config.contains("factions.exclude") ) {
					factions.setBlacklist( config.getStringList("factions.exclude") );
				}
				
				if ( config.contains("factions.maxPower") || config.contains("factions.minPower")) {
					factions.setPower( config.getInt("factions.minPower"), config.getInt("factions.maxPower") );
				}
				
				if ( factions != null ) rule.addApplicator( factions );
			}
			
		}
		
		
		if ( config.contains( "mode" ) ) {
			
			rule.setRuleMode( RuleMode.valueOf( config.getString("mode").toUpperCase()) );
			
		}
		
		rule.setAuto( config.getBoolean("auto", !manual ) );
		
		if ( config.contains( "messages.restricted") ) rule.setRestrictedMessage( config.getString("messages.restricted", null) );
		if ( config.contains( "messages.unrestricted") ) rule.setUnrestrictedMessage( config.getString("messages.unrestricted", null) );
		
		if ( config.contains("actions") ) 	rule.setActions( config.getStringList("actions") );
		//if ( config.contains("items") ) 	rule.setAllowedItems( config.getStringList("items") );
		
		if ( config.contains("items.allow") ) Rules.loadItemsFromList( config.getStringList("items.allow"), rule, true );
		if ( config.contains("items.restrict") ) Rules.loadItemsFromList( config.getStringList("items.restrict"), rule, false );
		
		if ( config.contains("itemsAdd") ) {
			
			for( String x : config.getStringList("itemsAdd") ) {
				rule.addAllowedItem( x );
			}
			
		}
		
		if ( config.contains("itemsRemove") ) {
			
			for( String x : config.getStringList("itemsRemove") ) {
				rule.removeAllowedItem( x );
			}
			
		}
		
		if ( config.contains("permissions.applyto") ) rule.addApplicator( new ConditionPerms( true, config.getStringList("permissions.applyto") ) );
		if ( config.contains("permissions.exclude") ) rule.addApplicator( new ConditionPerms( false, config.getStringList("permissions.exclude") ) );		
			
		if ( config.contains("groups.applyto") ) rule.addApplicator( new ConditionGroup( true, config.getStringList("groups.applyto") ) );
		if ( config.contains("groups.exclude") ) rule.addApplicator( new ConditionGroup( false, config.getStringList("groups.exclude") ) );		
		
		if ( config.contains("worlds.applyto") ) rule.addApplicator( new ConditionWorld( true, config.getStringList("worlds.applyto") ) );
		if ( config.contains("worlds.exclude") ) rule.addApplicator( new ConditionWorld( true, config.getStringList("worlds.exclude") ) );
		
		if ( config.contains("xp.min") || config.contains("xp.max") ) {
			rule.addApplicator( new ConditionXP( config.contains("xp.min") ? config.getInt("xp.min") : null, config.contains("xp.max") ? config.getInt("xp.max") : null ) );	
		}

		if ( config.contains("itemLevel.min") || config.contains("itemLevel.max") ) {
			rule.addApplicator( new ConditionItemLevel( config.contains("itemLevel.min") ? config.getInt("itemLevel.min") : null, config.contains("itemLevel.max") ? config.getInt("itemLevel.max") : null ) );	
		}
		
		Rules.addRule( rule );
		
		
	}
	
	/**
	 * Add an item alias.
	 * This way you can refer to user made lists of item ids by
	 * a name instead of the itemids.
	 * @param tag
	 * @param items
	 */
	public static void addItemAlias( String tag, List<String> items ) {
		
		itemAliases.put( tag, items );
		
	}
	
	
	/**
	 * Returns the list of item ids for the tag
	 * @param tag
	 * @return
	 */
	public static List<String> getItemsFromAlias( String tag ) {
		
		return itemAliases.get( tag);
		
	}
	
	
	/**
	 * Loads the items from the alias
	 * @param list
	 * @param rule
	 * @param isAllowedItem
	 */
	public static void loadItemsFromList( List<String> list, Rule rule, boolean isAllowedItem ) {
		
		if ( !list.isEmpty() ) {

			List<String> aliases;
			
			for ( String x : list ) {
				
				aliases = getItemsFromAlias( x );
				
				if ( aliases == null ) {
					// no alias. Just add
					if ( isAllowedItem ) {
						rule.addAllowedItem( x );
					} else {
						rule.addRestrictedItem( x );
					}
				} else {
					if ( isAllowedItem ) {
						rule.addAllowedItems( aliases );
					} else {
						rule.addRestrictedItems( aliases );
					}
				}
				
			}
			
		}
		
	}
	
	
	/**
	 * Returns number of rules loaded
	 * @return
	 */
	public static int count() {
		return Rules.ruleList.size();
	}
	
	
	/**
	 * Good Guy Closure
	 */
	public static void close() {
		
		if ( ruleList != null && !ruleList.isEmpty() ) {
			for ( Rule r : ruleList ) {
				r.close();
			}
			ruleList.clear();
		}
		
		if ( itemAliases != null ) itemAliases.clear();
		if ( ruleByTagList != null ) ruleByTagList.clear();
		
	}
	
}
