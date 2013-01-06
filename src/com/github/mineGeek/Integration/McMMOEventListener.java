package com.github.mineGeek.Integration;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.github.mineGeek.ItemRules.ItemRules;
import com.github.mineGeek.ItemRules.Store.Players;

import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;
import com.gmail.nossr50.events.party.McMMOPartyChangeEvent;

public class McMMOEventListener implements Listener {
	
	@SuppressWarnings("unused")
	private ItemRules itemRules;
	
	public McMMOEventListener( ItemRules plugin ) {
		this.itemRules = plugin;
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onMcMMOExperience( McMMOPlayerLevelUpEvent evt) {
		
		Players.get( evt.getPlayer() ).setMcMMOLevel( evt.getSkill().name(), evt.getSkillLevel() );
		Players.get( evt.getPlayer() ).loadRules();
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onMcMMOPartyChange( McMMOPartyChangeEvent evt ) {
		
		Players.get(evt.getPlayer()).setMcMMOInParty( ( evt.getNewParty() != null ) );
		Players.get(evt.getPlayer() ).loadRules();
		
	}
	
	

}
