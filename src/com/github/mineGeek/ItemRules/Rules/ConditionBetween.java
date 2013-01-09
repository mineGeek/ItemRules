package com.github.mineGeek.ItemRules.Rules;

/**
 * Evaluates numbers
 *
 */
public class ConditionBetween {

	
	/**
	 * Minimum int. Can be null to be n/a
	 */
	Integer min = null;
	
	
	/**
	 * Maximum int. Can be null or 0 to be n/a
	 */
	Integer max = null;
	
	
	/**
	 * Construct Me.
	 */
	public ConditionBetween() {}
	
	
	/**
	 * COnstructor taking args
	 * @param min
	 * @param max
	 */
	public ConditionBetween( Integer min, Integer max ) {
		this.min = min;
		this.max = max;
	}
	
	
	/**
	 * Returns min
	 * @return
	 */
	public Integer getMin() {
		return this.min;
	}


	/**
	 * ALlows you to set it!
	 * @param min
	 */
	public void setMin(Integer min) {
		this.min = min;
	}


	/**
	 * Return Max
	 * @return
	 */
	public Integer getMax() {
		return this.max;
	}


	/**
	 * Sets the max
	 * @param max
	 */
	public void setMax(Integer max) {
		this.max = max;
	}
	
	
	/**
	 * Returns true is min is less than value or
	 * if min is null.
	 * @param value
	 * @return
	 */
	public Boolean isMinOk( int value ) {
		
		if ( this.min == null || this.min == 0 ) return true;
		if ( value >= this.min ) return true;
		return false;
		
	}
	
	
	/**
	 * Returns true if max is null or zero or greater
	 * than value
	 * @param value
	 * @return
	 */
	public Boolean isMaxOk( int value ) {
		
		if ( this.max == null || this.max == 0 ) return true;
		if ( value <= this.max ) return true;
		return false;
	}
	
	
	/**
	 * Returns isMinOk && isMaxOk.
	 * @param value
	 * @return
	 */
	public Boolean meetsRequirements( int value ) {
		return this.isMinOk( value ) && this.isMaxOk( value );
	}
	

}
