package com.github.mineGeek.ItemRules.Events;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;

import com.github.mineGeek.ItemRules.API;
import com.github.mineGeek.ItemRules.ItemRules.Actions;
import com.github.mineGeek.ItemRules.Store.IRPlayer;
import com.github.mineGeek.ItemRules.Store.Players;
import com.github.mineGeek.ItemRules.Utilities.Config;



public class Listeners implements Listener {

	
	private Map<InventoryType, Material> inventoryMaterialMap;
	
	public Listeners() {
		
		this.inventoryMaterialMap = new HashMap<InventoryType, Material>();	
		
		this.inventoryMaterialMap.put( InventoryType.ANVIL, Material.ANVIL);
		this.inventoryMaterialMap.put( InventoryType.BREWING, Material.BREWING_STAND );
		this.inventoryMaterialMap.put( InventoryType.CHEST, Material.CHEST);
		this.inventoryMaterialMap.put( InventoryType.DISPENSER, Material.DISPENSER );
		this.inventoryMaterialMap.put( InventoryType.ENCHANTING, Material.ENCHANTMENT_TABLE );
		this.inventoryMaterialMap.put( InventoryType.ENDER_CHEST, Material.ENDER_CHEST );
		this.inventoryMaterialMap.put( InventoryType.FURNACE, Material.FURNACE );
		this.inventoryMaterialMap.put( InventoryType.WORKBENCH, Material.WORKBENCH );
		
	}
	
	/**
	 * Convienence stub to get the MaterialId from an entity ( if possible )
	 * @param entity
	 * @return
	 */
	public Material getMaterialFromEntity( Entity entity ) {
		
		Class<?>[] interfaces = entity.getClass().getInterfaces();
		if ( interfaces.length == 1 ) {
			String s = interfaces[0].getSimpleName();
			Material mat = Material.matchMaterial(s);
			if ( mat != null ) return mat;
		}
		
		return null;
		
	}
	
	/**]
	 * When player logs in. We add to the Player store, load
	 * any previous values, process applicable rules and 
	 * print them off for the user.
	 * @param evt
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin( PlayerJoinEvent evt ) {

		Players.addPlayer( evt.getPlayer() );
		API.printNewUnrestrictedToPlayer( evt.getPlayer(), false ); 
		API.printRulesToPlayer( evt.getPlayer() );
		
	}
	
	
	/**
	 * When player leaves, we save their data and flush from queue.
	 * @param evt
	 */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLeave(PlayerQuitEvent evt )
    {
    	Players.removePlayer( evt.getPlayer().getName() );
    	
    }
    

    /**
     * When the player changes worlds, we reload their rules
     * @param evt
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent evt) {
    	Players.get( evt.getPlayer() ).loadRules();
    }
	

    /**
     * When user changes XP level, we reload their rules and optionally
     * change their Item Level to match.
     * @param evt
     */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLevelChange( PlayerLevelChangeEvent evt ) {
		
		Players.get( evt.getPlayer() ).setXPLevel( evt.getPlayer().getLevel() );

	}
	
	
	/**
	 * When player respawns, set their location ( to update any exit area rules) and update
	 * their XP level (which will cause their rules to reload)
	 * @param evt
	 */
	@EventHandler(priority = EventPriority.LOWEST )
    public void onRespawn(PlayerRespawnEvent evt) {
		
		/**
		 * Really lame hack as Bukkit.getPlayer( name ) will
		 * return Null right now?
		 */
		final String peep = evt.getPlayer().getName();
		IRPlayer p = Players.get( peep );
		p.setXPLevel( evt.getPlayer().getLevel(), false );
		p.playerLocation( evt.getRespawnLocation() );
		
		Config.server().getScheduler().scheduleSyncDelayedTask( Config.server().getPluginManager().getPlugin("ItemRules"), new Runnable() {
		    @Override 
		    public void run() {
		    	API.refreshPlayerRules( Config.server().getPlayer( peep ) );
		    }
		}, 20L);		
		


    }
	
	
	/**
	 * Handles shooting bows and arrows.
	 * @param evt
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onShootBow( EntityShootBowEvent evt ) {
		
		if ( evt.isCancelled() ) return;
		
		if ( evt.getEntity() instanceof Player ) {
			
			Player p = (Player)evt.getEntity();
			if ( Players.get( p ).isRestricted( Actions.USE, evt.getBow().getType(), evt.getBow().getData().getData() ) ) {
				evt.setCancelled( true );
			} else {
				Material m = this.getMaterialFromEntity( evt.getProjectile() );
				
				if ( m != null ) {
					if ( Players.get( p ).isRestricted( Actions.USE, m, (byte)0)) {
						evt.setCancelled( true );
					}
				}
			}
			
		}
		
	}
	
	
	
	/**
	 * Handle getting into carts
	 * @param evt
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onVehicleEnter(VehicleEnterEvent evt) {
		
		if ( evt.isCancelled() ) return;
		
		if ( evt.getEntered() instanceof Player ) {
			
			Player p = (Player)evt.getEntered();
			Material m = this.getMaterialFromEntity( evt.getVehicle() );
			
			if ( m != null ) {
			
				if ( Players.get(p).isRestricted( Actions.USE, m, (byte)0)) {
					evt.setCancelled( true );
				}
			}
			
			
		}
		
	}
	
	
	/**
	 * Handle getting into some inventory
	 * @param evt
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryOpen( InventoryOpenEvent evt ) {
		
		if ( evt.getPlayer() instanceof Player ) {
		
			Player p = (Player)evt.getPlayer();
			
			if ( !this.inventoryMaterialMap.isEmpty() ) {
				
				Material m = this.inventoryMaterialMap.get( evt.getView().getType() );
				
				if ( m != null ) {
					if ( Players.get( p ).isRestricted( Actions.USE, m, (byte)0 ) ) {
						evt.setCancelled( true );
					}
				}
				
			}
			
		}
		
	}
	
	/**
	 * When a player closes their inventory, check to make sure they didn't equip armor
	 * they shouldn't have!
	 * @param evt
	 */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClose(InventoryCloseEvent evt) {
    	
        if (evt.getView().getType() == InventoryType.CRAFTING && evt.getPlayer() instanceof Player ) {
        	
        	Players.get( (Player)evt.getPlayer() ).checkInventory();
        	
        }
            
    }	
	
    
    /**
     * Handle filling up a bucket
     * @param evt
     */
    @SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerFillBucket(PlayerBucketFillEvent evt ) {
    	
    	if ( evt.isCancelled() ) return;
    	
    	if ( ! Players.get( evt.getPlayer() ).isRestricted( Actions.USE, evt.getBucket(), (byte)0 ) ) {
    		
    		if ( Players.get( evt.getPlayer() ).isRestricted( Actions.PICKUP, evt.getBlockClicked().getType(), evt.getBlockClicked().getData() ) ) {
    			evt.setCancelled( true );
    		}
    		
    	} else {
    		evt.setCancelled( true );
    		
    	}
    	
    	if ( evt.isCancelled() ) {
    		try {
    			evt.getPlayer().sendBlockChange( evt.getBlockClicked().getLocation(), evt.getBlockClicked().getType(), evt.getBlockClicked().getData());
				if ( evt.getBucket().getId() == Material.BUCKET.getId() || evt.getBucket().getId() == Material.WATER_BUCKET.getId() || evt.getBucket().getId() == Material.LAVA_BUCKET.getId() ) {
					ItemStack i = new ItemStack( evt.getBucket().getId(), 1 );
					evt.getPlayer().setItemInHand( i );
					evt.getPlayer().updateInventory();
				}
    		} catch (Exception e ){}    		
    	}
    	
    }
    
    
    /**
     * Handle emptying a bucket
     * @param evt
     */
    @SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerEmptyBucket(PlayerBucketEmptyEvent evt ) {
    	
    	if ( evt.isCancelled() ) return;
    	
    	if ( Players.get( evt.getPlayer() ).isRestricted( Actions.USE, evt.getBucket(), (byte)0 ) ) {
    		evt.setCancelled( true );
    		try {
    			evt.getPlayer().sendBlockChange( evt.getBlockClicked().getLocation(), evt.getBlockClicked().getType(), evt.getBlockClicked().getData());
				if ( evt.getBucket().getId() == Material.BUCKET.getId() || evt.getBucket().getId() == Material.WATER_BUCKET.getId() || evt.getBucket().getId() == Material.LAVA_BUCKET.getId() ) {
					ItemStack i = new ItemStack( evt.getBucket().getId(), 1 );
					evt.getPlayer().setItemInHand( i );
					evt.getPlayer().updateInventory();
				}
    		} catch (Exception e ){}
    	}
    	
    }    
    
    
    /**
     * Handle fishing
     * @param evt
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerFish(PlayerFishEvent evt) {
    	
    	if ( evt.isCancelled() ) return;
    	
    	if ( Players.get( evt.getPlayer() ).isRestricted( Actions.USE, evt.getPlayer().getItemInHand().getType(), evt.getPlayer().getItemInHand().getData().getData() ) ) {
    		evt.setCancelled( true );
    	}
    }
    
    
	/**
	 * Check if a player can use the item
	 * @param evt
	 */
    @SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractBlock(PlayerInteractEvent evt){
        
    	
    	if ( evt.isCancelled() ) return;
    	
    	try {
	    	if ( evt.getItem() != null && evt.getItem().getType() != null ) {
	    	
	    		if ( Players.get( evt.getPlayer() ).isRestricted(Actions.USE, evt.getItem().getType(), evt.getItem().getData().getData() ) ) {
	    			evt.setCancelled( true );

	    			if ( evt.getItem().getType().getId() == Material.BUCKET.getId() || evt.getItem().getType().getId() == Material.WATER_BUCKET.getId() || evt.getItem().getType().getId() == Material.LAVA_BUCKET.getId() ) {
	    				
	    				evt.getPlayer().sendBlockChange( evt.getClickedBlock().getLocation(), evt.getClickedBlock().getType(), evt.getClickedBlock().getData());
	    				ItemStack i = new ItemStack( evt.getItem().getType().getId(), 1 );
	    				evt.getPlayer().setItemInHand( i );
	    				evt.getPlayer().updateInventory();
	    			}
	    			
	    		}
	    	}
	    	if ( evt.getClickedBlock() != null ) {
	    	
	    		if ( Players.get( evt.getPlayer() ).isRestricted(Actions.USE, evt.getClickedBlock().getType(), evt.getClickedBlock().getData() ) ) {
	    			evt.setCancelled( true );
	    		}    		
	    		
	    		
	    	}
	    	
    	} catch (Exception e ) {

    		e.printStackTrace();
    	}

    }
    
    
    /**
     * Checks to see if the player can actually damage another entity with what they have in their hand
     * @param evt
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageByEntityEvent evt) {
    	
    	if ( evt.isCancelled() ) return;
    	
    	//can player use item?
    	
    	if ( evt.getDamager() instanceof Player ) {
    	
    		Player player = (Player)evt.getDamager();
    		
			if ( Players.get( player ).isRestricted( Actions.USE, player.getItemInHand().getType(), player.getItemInHand().getData().getData() ) ) {
				evt.setCancelled( true );
			}
    	}
    	
    }

    
    /**
     * Check to see if the player can interact with the thing they are interacting with
     * @param evt
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractEntity(PlayerInteractEvent evt){
        
    	if ( evt.isCancelled() ) return;
    	
    	try {
    		
    		if ( evt.getItem() == null ) return;

    		
    		if ( Players.get(evt.getPlayer() ).isRestricted( Actions.USE, evt.getItem().getType(), evt.getItem().getData().getData() ) ) {
    			evt.setCancelled( true );
    		}
    		
    	} catch ( Exception e ) {
    		e.printStackTrace();
    	}
    	
    }
    

    /**
     * Can the user actually craft that? You be the judge.
     * @param evt
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onCraftItem(final CraftItemEvent evt) {

    	if(evt.isCancelled()) return;
    	
    	if ( evt.getWhoClicked() instanceof Player ) {

    		if ( Players.get( (Player)evt.getWhoClicked() ).isRestricted( Actions.CRAFT, evt.getRecipe().getResult().getType(), evt.getRecipe().getResult().getData().getData() ) ) {
	    		evt.setCancelled( true );
	    	}    	
    	}

    }
    
    
    /**
     * Can the user break that block? Story at 11
     * @param evt
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockDestroy(BlockBreakEvent evt)
    {
    	
    	if(evt.isCancelled()) return;
    	
    	
    	if ( Players.get( evt.getPlayer() ).isRestricted( Actions.BREAK, evt.getBlock().getType(), evt.getBlock().getData() ) ) {
    		evt.setCancelled( true );
    	} else if ( Players.get( evt.getPlayer() ).isRestricted( Actions.USE, evt.getPlayer().getItemInHand().getType(), evt.getPlayer().getItemInHand().getData().getData() ) ) {
    		evt.setCancelled( true );
    	}
	

    }
    
    
    /**
     * Takes care of breaking entities as they are different from blocks
     * @param evt
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onHangingBreak( HangingBreakByEntityEvent evt ) {
    	
    	if(evt.isCancelled()) return;
    	
    	if ( evt.getRemover() instanceof Player ) {
    	
    		Player player = (Player) evt.getRemover();

    		Material m = Material.getMaterial( evt.getEntity().getType().toString() );	
    		
    		if ( m != null ) {
		    	if ( Players.get( player ).isRestricted( Actions.BREAK, m, (byte)0 ) ) {
		    		evt.setCancelled( true );
		    	}
    		}
    	
    	}
    	
    }

    
    /**
     * Can the user place that there? Not on my watch!
     * @param evt
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent evt)
    {
    	
        if(evt.isCancelled()) return;

        if ( Players.get( evt.getPlayer() ).isRestricted(Actions.PLACE, evt.getBlock().getType(), evt.getBlock().getData() ) ) {
        	
    		evt.setCancelled( true );

    	}
        
    }
    
    
    /**
     * Can the user grab that item/block?
     * @param evt
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPickup(PlayerPickupItemEvent evt)
    {
    	
        if(evt.isCancelled()) return;
        
    	if ( Players.get( evt.getPlayer() ).isRestricted( Actions.PICKUP, evt.getItem().getItemStack().getType(), evt.getItem().getItemStack().getData().getData()) ) {
    		evt.setCancelled( true );

    	}        
        
    }
    
    
    /**
     * Monitoring this event chugs away at resources. So... only monitor:
     * IF there is an areaRule configured
     * IF the player moved at least a whole fricken block (not just their mouse)
     * ONLY if they are in a chunk that has an areaRule attached. Not bad eh?
     * @param evt
     */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onMovement(PlayerMoveEvent evt) {
		
		if ( !Config.monitorPlayerLocations ) return;
		if ( evt.getFrom().getBlock().equals( evt.getTo().getBlock() ) ) return;

		Players.get(evt.getPlayer()).playerLocation( evt.getTo());
		
	}    
    
	

}
