package com.github.mineGeek.ItemRules.Rules;

public class RuleRange {

	private Integer min;
	private Integer max;
	
	
	public RuleRange() {}
	
	public RuleRange( Integer min, Integer max) {
		this.min = min;
	}	
	
	
	public int getMin() {
		return min;
	}


	public void setMin(Integer min) {
		this.min = min;
	}


	public int getMax() {
		return max;
	}


	public void setMax(Integer max) {
		this.max = max;
	}


	public boolean isApplicable( Integer value ) {
		
		boolean minOk = true;
		boolean maxOk = true;
		
		if ( this.min != null && this.min > 0 ) {
			if ( min > value ) minOk = false;
		}
		
		if ( this.max != null && this.max > 0 ) {
			if ( this.max < value ) maxOk = false;
		}
		
		return minOk && maxOk;
		
	}
	
	public Boolean isMinOk( Integer value ) {
		
		if ( this.min != null && this.min > value ) return false;
		return true;
		
	}
	
	public Boolean isMaxOk( Integer value ) {
		
		if ( this.max != null && this.max < value ) return false;
		return true;
	}
	
	public Boolean meetsRequirements( Integer value ) {
		return this.isMinOk( value ) && this.isMaxOk( value );
	}
	
	
	
}
