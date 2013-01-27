package com.github.mineGeek.ItemRules.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.mineGeek.ItemRestrictions.Utilities.Config;
import com.github.mineGeek.ItemRestrictions.Utilities.PlayerMessenger;
import com.github.mineGeek.ItemRules.ItemRules.Actions;
import com.github.mineGeek.ItemRules.Rules.AreaRule;
import com.github.mineGeek.ItemRules.Rules.AreaRules;
import com.github.mineGeek.ItemRules.Rules.RuleData;
import com.github.mineGeek.ItemRules.Rules.Rule;
import com.github.mineGeek.ItemRules.Rules.Rules;
import com.github.mineGeek.ItemRules.Rules.Rule.RuleMode;

/**
 * ItemRules extension to Bukkits player
 *
 */
public class IRPlayer extends DataStore {
	
	/**
	 * The Bukkit player
	 */
	
	String playerName = null;
	
	
	/**
	 * Reference to current item restrictions
	 * itemId => Rule Result
	 */
	Map<String, RuleData> rules;
	
	
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
	 * The mode of rules currently applied.
	 * Options are Default, Allow, Deny
	 */
	private RuleMode ruleMode = RuleMode.DEFAULT;

	
	private Map<String, List<String>> RuleMatrix = new HashMap<String, List<String>>();
	
	
	/**
	 * Constructor. Takes the folder to where the save file should be
	 * @param dataFolder
	 */
	public IRPlayer( String dataFolder ) {
		super(dataFolder);
	}
	
	
	/**
	 * Constructor. Takes folder where save data lives and reference to Bukkit player.
	 * @param dataFolder
	 * @param player
	 */
	public IRPlayer( String dataFolder, Player player ) {
		
		super( dataFolder );
		this.playerName = player.getName();
		this.setFileName( player.getName() );
		
		this.load();
		
		if ( Config.ItemLevelDefaultsToXPLevel ) {
			this.setXPLevel( player.getLevel(), false );
		}
		
	}
	
	
	/**
	 * Returns Bukkit player
	 * @return
	 */
	public Player getPlayer() {
		return Config.server().getPlayer( this.playerName );
	}
	
	
	/**
	 * Sets the players rule mode to Default, allow or deny.
	 * @param mode
	 */
	public void setRuleMode( RuleMode mode ) {
		this.ruleMode = mode;
	}
	
	
	/**
	 * Returns players rule mode. Default, Allow or Deny.
	 * @return
	 */
	public RuleMode getRuleMode() {
		return this.ruleMode;
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
		this.addManualRule( rule.getTag());
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
		//this.player.sendMessage("remove " + tag);
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
	
	
	public Map<String, List< String > > getRuleMatrix() {
		return this.RuleMatrix;
	}
	
	public void addRuleMatrixItem( String mode, String tag ) {
		
		if ( this.RuleMatrix.containsKey( mode ) ) {
			
			this.RuleMatrix.get( mode ).add( tag );
			
		} else {
		
			List<String> list = new ArrayList<String>();
			list.add( tag );
			this.RuleMatrix.put( mode, list );
			
		}
		
	}
	
	public void removeRuleMatrixItem( String mode, String tag ) {
		
		if ( this.RuleMatrix.containsKey( mode ) ) this.RuleMatrix.get( mode ).remove( tag );
		
	}
	
	public void clearRuleMatrixItem() {
		this.RuleMatrix.clear();
	}
	
	
	/**
	 * Load all rules, for no reason.
	 */
	public void loadRules(  ) {
		
		this.loadRules( null );
		
	}
	
	
	/**
	 * Refreshes all rules specific to player. Should be
	 * called when rules change or xp or item level changes
	 */
	public void loadRules( String reasonForChange ) {		
		
		
		Map<String, RuleData> newRule = Rules.getPlayerRules( this.getPlayer() );

		if ( this.rules != null) {
			if ( newRule != null ) {
				if ( this.rules.equals( newRule ) ) {
				}
			}			
		}
		
		
		this.rules = newRule;
		
		if ( this.rules != null && !this.rules.isEmpty() ) {
			
			List<String> rulenames = new ArrayList<String>();
			for ( RuleData r : this.rules.values() ) {
				if ( !rulenames.contains(r.ruleTag )) rulenames.add(r.ruleTag);
			}
			
		}
		
		
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
	 * Returns list of tags of the manually applied rules
	 * @return
	 */
	public List<String> getManualRules() {
		return this.manualRuleList;
	}
	
	
	
	/**
	 * Processes the user leaving an area. Used to remove any rules added
	 * by entering an area
	 * @param list
	 * @param location
	 */
	public boolean processExitRules( List<AreaRule> list, Location location ) {
		
		boolean result = false;
		
		if ( !this.inAreas.isEmpty() ) {
			if ( !list.isEmpty() ) {
				for ( AreaRule x : list ) {
					if ( !x.getArea().intersectsWith( location ) ) {
						x.applyExitRules( this.getPlayer() );
						this.inAreas.remove(x.getTag());
						result = true;
						if ( Config.debug_area_chunkExit ) this.getPlayer().sendMessage("Area Rule " + x.getTag() + " removed at " + (int)this.getPlayer().getLocation().getX() + ", " + (int)this.getPlayer().getLocation().getY() + ", " + (int)this.getPlayer().getLocation().getZ() + " [" + (int)x.getArea().ne.getX() + ", " + (int)x.getArea().ne.getY() + ", " + (int)x.getArea().ne.getZ() + "-" + (int)x.getArea().sw.getX() + ", " + (int)x.getArea().sw.getY() + ", " + (int)x.getArea().sw.getZ() + "]");
					}
				}
				
			}
		}
		
		return result;
		
	}
	

	/**
	 * Processes the user entering an area. Used to add rules to a player
	 * when they walk into a specified area
	 * @param list
	 */
	public boolean processEntranceRules( List<AreaRule> list ) {
		
		boolean result = false;
		
		if ( !list.isEmpty() ) {
			for ( AreaRule x : list ) {
				if ( !this.inAreas.contains( x.getTag() ) ) {
					if ( x.getArea().intersectsWith( this.getPlayer().getLocation() ) ) {
						x.applyEntranceRules( this.getPlayer() );
						this.inAreas.add( x.getTag() );
						result = true;
						if ( Config.debug_area_chunkExit ) this.getPlayer().sendMessage("Area Rule " + x.getTag() + " triggered at " + (int)this.getPlayer().getLocation().getX() + ", " + (int)this.getPlayer().getLocation().getY() + ", " + (int)this.getPlayer().getLocation().getZ() + " [" + (int)x.getArea().ne.getX() + ", " + (int)x.getArea().ne.getY() + ", " + (int)x.getArea().ne.getZ() + "-" + (int)x.getArea().sw.getX() + ", " + (int)x.getArea().sw.getY() + ", " + (int)x.getArea().sw.getZ() + "]");
					}
				}
			}
			
		}
		
		return result;
		
	}
	
	
	/**
	 * Sets and measures the players current location.
	 * If player walks into a chunk containing an area rule, 
	 * we start monitoring their movements. Once they actually pass into the area
	 * we Trigger the "Entrance" rules. When they leave, we trigger the "exit rules". If no area rules
	 * are specified, we don't even get here. If so, we cross reference the areas by chunk to radically
	 * reduce the amount of calculations during game.
	 * @param location
	 */
	public void playerLocation( Location location ) {
		
		String sig = location.getWorld().getName() + "|" + location.getChunk().getX() + "|" + location.getChunk().getZ();
		
		if ( !sig.equals(this.lastChunkSignature ) ) {		
			
			this.lastChunkSignature = sig;
			
			if ( Config.debug_area_chunkChange ) this.getPlayer().sendMessage(sig);
		}

		List<AreaRule> rules;
		
		if ( AreaRules.activeChunks.containsKey(sig) ) {
			rules = new ArrayList<AreaRule>(AreaRules.activeChunks.get(sig));
		} else {
			rules = new ArrayList<AreaRule>();
		}
		
		if ( this.activeAreaRules != null ) {
			if ( this.processExitRules( this.activeAreaRules, location) ) {
				rules.removeAll( this.activeAreaRules );
			}
		}
		
		if ( !rules.isEmpty() ) {
			if ( this.processEntranceRules( rules ) ) {
				this.activeAreaRules = rules;
			}
		}
		/*
		if ( !rules.isEmpty() ) {
		
			if ( !rules.containsAll( this.activeAreaRules ) ) {
				
				//remove existing rules
				this.processExitRules( this.activeAreaRules, location );
				this.activeAreaRules = rules;
				this.processEntranceRules( rules );
				
			}
		} else if ( this.activeAreaRules != null ) {
			this.processExitRules(this.activeAreaRules, location);
			this.activeAreaRules = null;
		}
		
		if ( this.activeAreaRules != null ) {
			//this.processExitRules( this.activeAreaRules, location );
		}
		
		this.activeAreaRules = AreaRules.activeChunks.get(sig);
		
		if ( this.activeAreaRules != null ) {
			this.processEntranceRules( this.activeAreaRules );	
		}
		*/		
		
	}
	
	
	/**
	 * Returns the rule data corresponding to the 
	 * material/item.
	 * @param material
	 * @param data
	 * @return
	 */
	public RuleData getRuleData( String material, String data ) {
		
		if ( this.rules.containsKey( material + "." + data ) ) return this.rules.get(material + "." + data);
		if ( this.rules.containsKey( material ) ) return this.rules.get( material );
		return null;
	}
	
	
	/**
	 * Returns true if players rules prevent them from performing
	 * the action with/to the material in question
	 * @param action
	 * @param material
	 * @param data
	 * @return
	 */
	public boolean isRestricted( Actions action, Material material, byte data ) {
		

		RuleMode mode = this.getRuleMode();
		
		RuleData item = this.getRuleData( String.valueOf( material.getId() )  , String.valueOf( data ) ) ;
		if ( item != null ) PlayerMessenger.SendPlayerMessage( this.getPlayer(), action.toString() + ": " + item + " material: " + material.getId() + "." + String.valueOf(data));
		if ( item == null )	{
			
			if ( mode == RuleMode.DENY ) {
				if ( Config.txtDefaultRestrictedMessage != null ) {
					
					Object[] args = { action.toString().toLowerCase(), material.toString().replace("_", " ").toLowerCase() };
					PlayerMessenger.SendPlayerMessage(this.getPlayer(), ChatColor.RED + String.format( Config.txtDefaultRestrictedMessage, args ) );
				}
				return true;
			} else {
				return false;
			}
		}
		
		boolean result = item.isRestricted( action );
		
		if ( result && item.getRestrictionMessage() != null ) {
						
			Object[] args = { action.toString().toLowerCase(),  material.toString().replace("_", " ").toLowerCase() };
			PlayerMessenger.SendPlayerMessage( this.getPlayer(), ChatColor.RED + "" + ChatColor.ITALIC + String.format(item.getRestrictionMessage(), args ) );
		}
		
		return result;
		

	}
	
	
	
	/**
	 * Sets the players XP level
	 * @param level
	 */
	public void setXPLevel( Integer level ) {
		this.setXPLevel(level, true);
	}
	
	
	/**
	 * Sets XP level
	 * triggers a refresh (unless refreshRules = false )
	 * @param level
	 * @param refreshRules
	 */
	public void setXPLevel( Integer level, Boolean refreshRules ) {
			
		Integer il = this.getItemLevel();
		
		if ( Config.XPLevelIncreasesItemLevel && level > il ) {
		
			this.setItemLevel(level);
		
		} else if ( Config.XPLossReducesItemLevel && level < il ) {
		
			this.setItemLevel(level);
			
		}
		
		if ( refreshRules )  loadRules();
		
	}
	
	
	/**
	 * Sets players ItemLevel
	 * @param level
	 */
	public void setItemLevel( Integer level ) {
		this.set("itemLevel", level );
	}
	
	
	/**
	 * returns players itemLevel
	 * @return
	 */
	public Integer getItemLevel() {
		return this.getAsInteger("itemLevel", 0 );
	}
	
	
	/**
	 * returns the players XP level
	 * @return
	 */
	public Integer getXPLevel() {
		return this.getPlayer().getLevel();
	}

	/**
	 * Good Guy Clear out.
	 */
	public void close() {
		
		if ( this.activeAreaRules != null && !this.activeAreaRules.isEmpty() ) this.activeAreaRules.clear();
		if ( this.data != null && !this.data.isEmpty() ) this.data.clear();
		if ( this.inAreas != null && !this.inAreas.isEmpty() ) this.inAreas.clear();
		if ( this.manualRuleList != null && !this.manualRuleList.isEmpty() ) this.manualRuleList.clear();
		if ( this.manualRules != null && !this.manualRules.isEmpty() ) this.manualRules.clear();
		if ( this.rules != null && !this.rules.isEmpty() ) this.rules.clear();
		
	}
	
}
