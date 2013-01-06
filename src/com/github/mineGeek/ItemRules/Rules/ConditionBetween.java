package com.github.mineGeek.ItemRules.Rules;

public class ConditionBetween {

	Integer min = null;
	Integer max = null;
	
	public ConditionBetween() {}
	
	public ConditionBetween( Integer min, Integer max ) {
		this.min = min;
		this.max = max;
	}
	
	public int getMin() {
		return this.min;
	}


	public void setMin(int min) {
		this.min = min;
	}


	public int getMax() {
		return this.max;
	}


	public void setMax(int max) {
		this.max = max;
	}
	
	public Boolean isMinOk( int value ) {
		
		if ( this.min != null && this.min > value ) return false;
		return true;
		
	}
	
	public Boolean isMaxOk( int value ) {
		
		if ( this.max != null && this.max < value ) return false;
		return true;
	}
	
	public Boolean meetsRequirements( int value ) {
		return this.isMinOk( value ) && this.isMaxOk( value );
	}
	

}
