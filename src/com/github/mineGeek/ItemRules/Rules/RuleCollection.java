package com.github.mineGeek.ItemRules.Rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleCollection {

	private Map<RuleRangeType, RuleRange> range = new HashMap<RuleRangeType, RuleRange>();
	private List<RuleRange> rangeList = new ArrayList<RuleRange>();
	
	public enum RuleRangeType {XP, ITEM_LEVEL, MCMMO_SKILL, FACTION_POWER};
	
	public RuleCollection() {}
	
	public RuleCollection( RuleRangeType type, RuleRange range ) {
		
		this.add( type, range );
	
	}
	
	public RuleCollection( RuleRangeType type, Integer min, Integer max ) {
		
		this.add( type, new RuleRange(min, max) );
		
	}
	
	public void add( RuleRangeType type, RuleRange range ) {
		this.range.put( type, range );
		this.rangeList.add( range );
	}
	
	public void add( List<RuleRangeType> types, List<RuleRange> ranges ) {
		
		if ( !types.isEmpty() ) {
			
			for( int x = 0; x < types.size(); x++ ) {
				this.add( types.get(x), ranges.get(x) );
			}
			
		}
	}
	
	public void clear() {
		this.range.clear();
		this.rangeList.clear();
	}
	
	public void set( Map<RuleRangeType, RuleRange> range ) {
		
		this.range = range;
		this.rangeList = new ArrayList<RuleRange>();
		
		if ( !range.isEmpty() ) {
			for (RuleRange r : range.values() ) {
				this.rangeList.add( r );
			}
		}
		
	}
	
	public Boolean isApplicable( Integer value ) {
		
		Boolean result = true;
		
		if ( !this.rangeList.isEmpty() ) {
			
			for( RuleRange r : this.rangeList ) {
				result = result & r.isApplicable(value);
			}
			
		}
		
		return result;
		
	}
	
	public Boolean meetsRequirements( Integer value) {
		
		Boolean result = true;
		
		if ( !this.rangeList.isEmpty() ) {
			
			for( RuleRange r : this.rangeList ) {
				result = result & r.meetsRequirements( value );
			}
			
		}
		
		return result;		
		
	}
	
	
	
}
