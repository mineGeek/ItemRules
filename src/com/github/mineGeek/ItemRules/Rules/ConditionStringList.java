package com.github.mineGeek.ItemRules.Rules;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for checking against string lists
 *
 */
public class ConditionStringList {

	/**
	 * List of strings
	 */
	protected List<String> list = new ArrayList<String>();

	/**
	 * Constructor
	 */
	public ConditionStringList(){}
	
	
	/**
	 * List to check with constructor
	 * @param list
	 */
	public ConditionStringList( List<String> list ) {
		this.setList(list);
	}
	
	
	/**
	 * Set the list to check
	 * @param list
	 */
	public void setList( List<String> list ) {
		this.list = list;
	}
	
	
	/**
	 * Add an item to the list
	 * @param item
	 */
	public void add( String item ) {
		this.list.add( item );
	}
	
	
	/**
	 * Checks to see if item is in list
	 * @param item
	 * @return
	 */
	public boolean isInList( String item ) {
		return this.list.contains( item );
	}
	
	
	/**
	 * Returns True if item is not in list
	 * @param item
	 * @return
	 */
	public boolean isNotInList( String item ) {
		return !this.list.contains( item );
	}
	
	
	/**
	 * Empties list
	 */
	public void close() {
		this.list.clear();
	}
	
	
}
