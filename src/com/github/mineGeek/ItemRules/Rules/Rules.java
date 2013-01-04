package com.github.mineGeek.ItemRules.Rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRules.Config;
import com.github.mineGeek.ItemRules.ItemRules.Actions;
import com.github.mineGeek.ItemRules.Store.Users;

public class Rules {

	private static List<RuleInterface> ruleList = new ArrayList<RuleInterface>();
	private static Map<String, RuleInterface> ruleByTagList = new HashMap<String, RuleInterface>();
	
	
	public static String getRuleList( Player player, Boolean doCan, Boolean doCanNow, Boolean doNext, ChatColor canColor, ChatColor nextColor, ChatColor cannotColor ) {

		List<String> can = new ArrayList<String>();
		List<String> cannot = new ArrayList<String>();
		List<String> next = new ArrayList<String>();

		if ( !ruleList.isEmpty() ) {
			
			for ( RuleInterface x : Rules.ruleList ) {
				
				if ( x.getAutoAdd() || Users.get(player).hasManualRule( x.getTag() ) ) {
					if ( x.appliesToWorldName( player.getWorld().getName() ) ) {
				
						int il = Users.get(player).getItemLevel();
						int xp = Users.get(player).getXPLevel();
						
						Boolean ted = x.appliesToItemLevel(il);
						Boolean fred = x.appliesToXPLevel(xp);
						
						if ( x.appliesToItemLevel( il ) && x.appliesToXPLevel( xp ) && ( doCan || doCanNow ) ) {
							
							if ( doCanNow ) {
								if ( !x.appliesToItemLevel( il - 1 ) || !x.appliesToXPLevel( xp-1)) can.add( x.getUnrestrictedMessage() );
							} else {
								can.add( x.getUnrestrictedMessage() );
							}
							
						} else {
							
							if ( x.appliesToItemLevel( il + 1 ) && x.appliesToXPLevel( xp + 1 ) && doNext ) {
								next.add( x.getRestrictedMessage() );
							} else if ( !x.appliesToItemLevel( il ) || !x.appliesToXPLevel( xp ) && ( !doCan ) ){
								cannot.add( x.getRestrictedMessage() );
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
	
	public static Map<Actions, List<RuleInterface>> getPlayerRules( Player player ) {
		return Rules.getPlayerRules(player, Users.get(player).getXPLevel(), Users.get(player).getItemLevel() );
	}
	
	
	public static String getPlayerItemRules( Player player, ChatColor color, Boolean restricted, Boolean nextOnly ) {
		
		List<String> ruleSet = new ArrayList<String>();
		String result = null;
		Integer XP= 0;
		Integer IL = 0;
		
		if ( !ruleList.isEmpty() ) {
			
			XP = Users.get(player).getXPLevel();
			IL = Users.get(player).getItemLevel();
			
			if ( nextOnly ) {
				XP++;
				IL++;
			}
			
			
			for ( RuleInterface x : Rules.ruleList ) {
				
				if ( x.getAutoAdd() || Users.get(player).hasManualRule( x.getTag() ) ) {
					if ( x.appliesToWorldName( player.getWorld().getName() ) ) {						
						
						if ( x.isItemLevelMaxTooLow(IL) || x.isItemLevelMinTooHigh(IL) || x.isXPMaxTooLow(XP) || x.isXPMinTooHigh(XP)) {
							
							if ( restricted ) {
								ruleSet.add(x.getRestrictedMessage() );
							}
							
						} else if ( !restricted ) {
							ruleSet.add( x.getRestrictedMessage() );
						}
							
					}
				}
			}
			
		}		

		if ( !ruleSet.isEmpty() ) {
			int y = 1;
			for ( String x : ruleSet ) {
				
				y++;
				if ( result != null ) {
					if ( y > ruleSet.size() ) {
						result = result + " and " + x;
					} else {
						result = result + ", " + x;
					}
				} else {
					result = color + x;
				}
				
			}
			
		}
		
		return result;
		
	}
	
	public static Map<Actions, List<RuleInterface>> getPlayerRules( Player player, Integer XPLevel, Integer itemLevel ) {
		
		Map< Actions, List<RuleInterface>> actionRules = new HashMap< Actions, List<RuleInterface>>();
		if ( !ruleList.isEmpty() ) {
			
			for ( RuleInterface x : Rules.ruleList ) {
				
				if ( x.appliesToWorldName( player.getWorld().getName() ) ) {
					
					List<RuleInterface> ruleSet;
					
					
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
	
	
	public static List<RuleInterface> getPlayerRulesForAction( Actions action, Player player, Integer XPLevel, Integer itemLevel ) {
		
		List<RuleInterface> rules = new ArrayList<RuleInterface>();
		
		if ( !Rules.ruleList.isEmpty() ) {
			
			for ( RuleInterface x : Rules.ruleList ) {
				
				if ( x.getAutoAdd() || Users.get(player).hasManualRule(x.getTag())) {
					if ( x.isApplicable(action, player, XPLevel, itemLevel ) ) {
						rules.add( x );						
					}
				}
			}
		}
		
		return rules;
	}
	
	public static RuleInterface getRule( String tagName ) {
		return Rules.ruleByTagList.get( tagName );
	}
	
	public static void removeRule( RuleInterface rule ) {
		
		Rules.ruleByTagList.remove( rule.getTag() );
		Rules.ruleList.remove( rule );
		
	}
	
	public static void addRule( RuleInterface rule ) {
		
		Rules.ruleByTagList.put( rule.getTag(), rule );
		Rules.ruleList.add( rule );		
	}
	
	public static void addRule( String tag, ConfigurationSection config ) {
		
		RuleInterface rule;
		
		if ( config.contains("factions") || config.contains("powerMin") || config.contains("powerMax") ) {
			
			//Faction rule
			rule = new RuleFaction();
			
		} else if ( config.contains("mcmmo" ) ) {
			//mcmmo rule
			rule = new RuleBase();
		} else {
			
			rule = new RuleBase();
			
		}
		
		rule.setTag( tag );
		rule.setDefaultValue( config.getBoolean("defaultValue", false ) );
		rule.setRestrictedMessage( config.getString("restrictedMessage", ""));
		rule.setUnrestrictedMessage( config.getString("unrestrictedMessage", ""));
		rule.setAutoAdd( config.getBoolean("auto", true ) );
		if ( config.contains("actions") ) 	rule.setActionsFromStringList( config.getStringList("actions") );
		if ( config.contains("items") ) 	rule.setItems( config.getStringList("items") );
		if ( config.contains("worlds") )	rule.setWorldNames( config.getStringList("worlds") );
		if ( config.contains("XPMin") )		rule.setXPMin( config.getInt("XPMin", 0 ) );
		if ( config.contains("XPMax") )		rule.setXPMax( config.getInt("XPMax", 0 ) );
		if ( config.contains("itemLevelMin"))rule.setItemLevelMin( config.getInt("itemLevelMin", 0 ) );
		if ( config.contains("itemLevelMax"))rule.setItemLevelMax( config.getInt("itemLevelMax", 0 ) );
		
		Rules.addRule( rule );
		
		
	}
	
	public static int count() {
		return Rules.ruleList.size();
	}
	
	
}
