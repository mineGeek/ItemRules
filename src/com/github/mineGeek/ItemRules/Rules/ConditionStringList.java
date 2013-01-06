package com.github.mineGeek.ItemRules.Rules;

import java.util.ArrayList;
import java.util.List;

public class ConditionStringList {

	private List<String> list = new ArrayList<String>();

	public ConditionStringList(){}
	
	public ConditionStringList( List<String> list ) {
		this.setList(list);
	}
	
	public void setList( List<String> list ) {
		this.list = list;
	}
	
	public void add( String item ) {
		this.list.add( item );
	}
	
	public boolean isInList( String item ) {
		return this.list.contains( item );
	}
	
	public boolean isNotInList( String item ) {
		return !this.list.contains( item );
	}
	
	
	
	
}
