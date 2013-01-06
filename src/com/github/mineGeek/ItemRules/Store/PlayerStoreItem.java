package com.github.mineGeek.ItemRules.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.mineGeek.Integration.McMMOPlayer;
import com.github.mineGeek.ItemRules.AreaRule;
import com.github.mineGeek.ItemRules.AreaRules;
import com.github.mineGeek.ItemRules.Config;
import com.github.mineGeek.ItemRules.ItemRules.Actions;
import com.github.mineGeek.ItemRules.PlayerMessenger;
import com.github.mineGeek.ItemRules.Rules.Rule;
import com.github.mineGeek.ItemRules.Rules.Rules;

/**
 * Wrapper for essentially extending the player
 * supporting file persistence
 *
 */
public class PlayerStoreItem extends DataStore {
	
	
	
	
	/**
	 * The Bukkit player
	 */
	Player player = null;
	
	
	
	
	/**
	 * Refernce to currently applicable rules
	 * Action => Rules that apply to action
	 */
	Map<Actions, List<Rule>> rules;
	
	
	
	
	/**
	 * Reference to currently active rules that
	 * were added to player via the API or and area
	 */
	Map<String, Rule> manualRules = new HashMap<String, Rule>();
	
	
	
	
	/**
	 * A simple lookup of rule names manually added
	 */
	List<String> manualRuleList = new ArrayList<String>();
	
	
	
	
	/**
	 * List of current active rules for the players area
	 * used to reduce the amount of location lookups
	 */
	private List<AreaRule> activeAreaRules = null;
	
	
	
	
	/**
	 * A reference to the last known chunk player was in.
	 * used to reduce location calculations
	 */
	private String lastChunkSignature;
	
	
	
	
	/**
	 * The areas that the player is currently "in". When user enters
	 * an area, the name of the area is stored here and the 
	 * entrance rules run. When player leaves area, the exit rules are run
	 */
	private List<String> inAreas = new ArrayList<String>();
	
	
	
	
	/**
	 * Local storage of McMMO skills and levels
	 */
	private Map<String, Integer> mcmmoSkills = new HashMap<String, Integer>();
	
	
	
	
	/**
	 * Local mcmmo flag that is set when they are in a party
	 */
	private Boolean mcmmoInParty = false;
	
	
	/**
	 * Constructor. Takes the folder to where the save file should be
	 * @param dataFolder
	 */
	public PlayerStoreItem( String dataFolder ) {
		super(dataFolder);
	}
	
	
	
	
	/**
	 * Constructor. Takes folder where save data lives and reference to Bukkit player.
	 * @param dataFolder
	 * @param player
	 */
	public PlayerStoreItem( String dataFolder, Player player ) {
		
		super( dataFolder );
		this.player = player;
		this.setFileName( player.getName() );
		
		this.load();
		
		if ( Config.ItemLevelDefaultsToXPLevel ) {
			this.setXPLevel( player.getLevel(), false );
		}
		
		if ( McMMOPlayer.enabled ) {
			McMMOPlayer.loadPlayerSkills(player);
			this.mcmmoInParty = McMMOPlayer.isPlayerInParty( player );
		}
		
	}
	
	
	public Player getPlayer() {
		return this.player;
	}
	
	
	/**
	 * Adds a rule to player based on its tag. Used by API
	 * Note that loadRules() will need to be run in order
	 * for any changes to take place 
	 * @param tag
	 */
	public void addManualRule( String tag ) {
		
		Rule rule = Rules.getRule( tag );
		
		if ( rule != null ) {
			this.manualRuleList.add( tag );
			this.manualRules.put(tag ,rule);
		}
		
	}
	
	
	
	
	
	/**
	 * Adds manual rule.
	 * Note that loadRules() will need to be run in order
	 * for any changes to take place 
	 * @param rule
	 */
	public void addManualRule( Rule rule ) {
		this.manualRules.put( rule.getTag(), rule);
		this.manualRuleList.add( rule.getTag() );
	}
	
	
	
	
	/**
	 * Removes the rule from the player
	 * Note that loadRules() will need to be run in order
	 * for any changes to take place
	 * @param tag
	 */
	public void removeManualRule( String tag ) {
		this.manualRules.remove(tag);
		this.manualRuleList.remove(tag);
	}

	
	
	
	
	/**
	 * Clears out all manual rules from player
	 * Note that loadRules() will need to be run in order
	 * for any changes to take place
	 */
	public void clearManualRules() {
		this.manualRules.clear();
		this.manualRuleList.clear();
	}
	
	
	
	
	
	/**
	 * Refreshes all rules specific to player. Should be
	 * called when rules change or xp or item level changes
	 */
	public void loadRules() {
		
		this.rules = Rules.getPlayerRules( this.player );
		
	}
	
	
	
	
	
	/**
	 * Saves list of rules manually added so they are applied
	 * once player is loaded again
	 */
	public void setManualList() {
		
		this.set("manualList", this.manualRuleList );
		
	}
	
	
	
	
	/**
	 * Retrieves list of manually added rules from datastore
	 */
	public void loadManualList() {
		
		this.manualRuleList = this.getAsStringList("manualList");
		
	}
	
	
	
	
	/**
	 * Checks to see if player has rule manually applied to them
	 * @param value
	 * @return
	 */
	public Boolean hasManualRule( String value ) {
		
		if ( this.manualRules.containsKey( value ) ) return true;
		return false;
		
	}
	
	
	
	
	/**
	 * Processes the user leaving an area. Used to remove any rules added
	 * by entering an area
	 * @param list
	 * @param location
	 */
	public void processExitRules( List<AreaRule> list, Location location ) {
		
		if ( !this.inAreas.isEmpty() ) {
			if ( !list.isEmpty() ) {
				for ( AreaRule x : list ) {
					if ( !x.getArea().intersectsWith( location ) ) {
						x.applyExitRules( this.player );
						this.inAreas.remove(x.getTag());
					}
				}
			}
		}
		
	}
	
	
	
	
	
	
	/**
	 * Processes the user entering an area. Used to add rules to a player
	 * when they walk into a specified area
	 * @param list
	 */
	public void processEntranceRules( List<AreaRule> list ) {
		
		
		if ( !list.isEmpty() ) {
			for ( AreaRule x : list ) {
				if ( !this.inAreas.contains(x.getTag() ) ) {
					if ( x.getArea().intersectsWith( this.player.getLocation() ) ) {
						x.applyEntranceRules( this.player );
						this.inAreas.add(x.getTag());
					}
				}
			}
		}
		
	}
	
	
	
	
	
	/**
	 * Sets and measures the players current location. If player walks into a chunk containing
	 * an area rule, we start monitoring their movements. Once they actually pass into the area
	 * we Trigger the "Entrance" rules. When they leave, we trigger the "exit rules". If no arearules
	 * are specified, we don't even get here. If so, we cross reference the areas by chunk to radically
	 * reduce the amount of calculations during game.
	 * @param location
	 */
	public void playerLocation( Location location ) {
		
		String sig = location.getWorld().getName() + "|" + location.getChunk().getX() + "|" + location.getChunk().getZ();
		
		if ( !sig.equals(this.lastChunkSignature ) ) {		
			
			this.lastChunkSignature = sig;
		}
		
		if ( this.activeAreaRules != null ) {
			this.processExitRules( this.activeAreaRules, location );
		}
		
		this.activeAreaRules = AreaRules.activeChunks.get(sig);
		
		if ( this.activeAreaRules != null ) {
			this.processEntranceRules( this.activeAreaRules );	
		}		
			
		
		
	}
	
	
	
	
	
	/**
	 * Core process to see if player has access to the item/material for specified action.
	 * If not, send a message to player
	 * @param action
	 * @param material
	 * @param data
	 * @return
	 */
	public boolean isRestricted( Actions action, Material material, byte data ) {
		
		Boolean result 	= Config.defaultValue;
		String message 	= "you cannot do that.";
		Boolean isRestricted = false;
		// Check automatically configured actions
		
		if ( this.rules.containsKey( action ) ) {
			Iterator<Rule> r = this.rules.get( action ).iterator();	
			while ( r.hasNext() ) {
				
				result = false;
				Rule rule = r.next();
				if ( rule.passesMcMMO( this.player ) ) {
					if ( rule.isRestricted( material, data) ) {
						isRestricted = true;
					}
				} else {
					isRestricted = true;
				}
				
				if ( isRestricted ) {
					
					result = true;
					message = rule.getRestrictedMessage( action, material );			
					break;
				}				
			}
			
		}
		

		
		//check against any manually added actions that may override auto actions
		
		if ( !this.manualRuleList.isEmpty() ) {
			
			for ( String x : this.manualRuleList ) {
				
				if ( this.manualRules.get(x).passesMcMMO( this.player ) ) {
				
					if ( this.manualRules.get(x).isRestricted( material, data) ) {
						
						//player is denied
						result 	= true;
						message = this.manualRules.get(x).getRestrictedMessage(action, material);
						break;
						
					} else if ( result ) {
						
						if ( this.manualRules.get(x).appliesToItem( material.getId(), data) ) {
							
							//player was denied, but this rule overrides it.
							return false;
						}
					}
				
				} else if ( result && this.manualRules.get(x).appliesToItem( material.getId(), data ) ) {
					return false;
				}
				
			}
			
		}
		
		if ( result ) PlayerMessenger.SendPlayerMessage( this.player, ChatColor.RED + Config.txtCannotDoPrefix + message );
		
		return result;		
	}
	
	
	
	
	
	/**
	 * Incriments players XP level the specified amount
	 * @param level
	 */
	public void incrimentLevel( Integer level ) {
		
		this.setXPLevel( this.getXPLevel() + level );
		
	}
	
	
	
	
	
	/**
	 * convienence method for incrimenting players XP level by 1
	 */
	public void incrimentLevel() {
		this.incrimentLevel( 1 );
	}
	
	
	
	
	/**
	 * Sets the players XP level
	 * @param level
	 */
	public void setXPLevel( Integer level ) {
		this.setXPLevel(level, true);
	}
	
	
	
	
	
	/**
	 * Sets XP level and triggers a refresh (unless refreshRules = false )
	 * @param level
	 * @param refreshRules
	 */
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
	
	
	
	
	
	/**
	 * Sets players ItemLevel
	 * @param level
	 */
	public void setItemLevel( Integer level ) {
		this.set("itemLevel", level );
	}
	
	
	
	
	
	/**
	 * returns players current item level
	 * @return
	 */
	public Integer getItemLevel() {
		return this.getAsInteger("itemLevel", 0 );
	}
	

	
	
	/**
	 * Sets and stores the players previous item level
	 * @param level
	 */
	public void setPreviousLevel( Integer level ) {
		this.set("previousLevel", level);
	}
	
	
	
	
	
	/**
	 * gets players previos item level
	 * @return
	 */
	public Integer getPreviousLevel() {
		return this.getAsInteger("previousLevel", 0);
	}
	
	
	
	
	/**
	 * returns the currently stored xp level
	 * @return
	 */
	public Integer getXPLevel() {
		return this.getAsInteger("XPLevel", 0 );
	}

	
	
	
	
	/**
	 * Convienence method for current faction name
	 * @return
	 */
	public String getFactionName() {
		return "hi";
	}
	
	
	
	
	
	/**
	 * Convienence method for getting current faction power
	 * @return
	 */
	public double getFactionPowerMin() {
		return 0;
	}
	
	
	
	
	
	/**
	 * Convienence method for getting current faction power
	 * @return
	 */
	public double getFactionPowerMax() {
		return 0;
	}	
	
	
	
	

	public void setMcMMOLevel( String skill, Integer level ) {
		this.mcmmoSkills.put(skill, level);
	}
	
	public Integer getMcMMOLevel( String skill ) {
		
		Integer i = this.mcmmoSkills.get(skill);
		if ( i !=null ) return i;
		return 0;
		
	}
	
	public void setMcMMOInParty( Boolean value ) {
		this.mcmmoInParty = value;
	}
	
	public Boolean getMcMMOinParty() {
		return this.mcmmoInParty;
	}

	public Map<String, Integer> getMcMMOLevels() {
		return this.mcmmoSkills;
	}

}
