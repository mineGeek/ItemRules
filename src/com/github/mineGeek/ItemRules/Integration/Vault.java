package com.github.mineGeek.ItemRules.Integration;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.github.mineGeek.ItemRestrictions.Utilities.Config;

import net.milkbowl.vault.permission.Permission;

public class Vault {

	public static Permission perm;
	public static boolean enabled;
	
	public static boolean VaultEnable() {
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("Vault");
		
		if (plugin == null || ! plugin.isEnabled() ) {
			
			Bukkit.getLogger().info("Vault integration for [ItemRules] is not enabled");
			enabled = false;

		} else {
			Bukkit.getLogger().info("Vault integration for [ItemRules] is enabled");
	        RegisteredServiceProvider<Permission> rsp = Config.server().getServicesManager().getRegistration(Permission.class);
	        perm = rsp.getProvider();
	        enabled = perm != null;				
			
			
		}		
		
		return enabled;
	}
	
	public static boolean inGroup( Player player, String groupName ) {
		
		if ( enabled ) {
			
			return perm.playerInGroup(player, groupName );
			
		}
		
		return false;
		
	}
	
}
