package com.github.mineGeek.ItemRules.Store;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base object for saving/loading customer player data
 * from flat file
 *
 */
public class DataStore {

	
	/**
	 *  Map of columnName => Object to store
	 */
	public Map<String, Object> data = new HashMap<String, Object>();
		
	
	/**
	 * Default file name. Should be player.getName();
	 */
	private String fileName 	= "playerData";
		
	
	/**
	 * Default file extension.
	 */
	private String fileExt 		= "bin";
		
	
	/**
	 * Path to save/load from
	 */
	public 	String dataFolder;
	
	
	/**
	 * Constructor - takes the data folder to work from
	 * @param dataFolder
	 */
	public DataStore( String dataFolder ) {
		this.dataFolder = dataFolder;
	}
	
	
	/**
	 * Basic setting of an object to a column name. Whatever is set, is stored.
	 * @param ColumnName
	 * @param value
	 */
	public void set( String ColumnName, Object value ) {
		this.data.put(ColumnName, value);
	}

	
	/**
	 * Standard getting of object. Will return whatever has been set.
	 * @param columnName
	 * @return
	 */
	public Object get( String columnName ) {		
		return this.data.get( columnName );		
	}
	
	
	/**
	 * gets an Object as an Integer
	 * @param columnName
	 * @param defaultValue
	 * @return
	 */
	public Integer getAsInteger( String columnName, Integer defaultValue ) {
		
		Object value = this.get( columnName );		
		if ( value == null ) return defaultValue;
		return (Integer)this.get( columnName );
		
	}
	
	
	/**
	 * gets an object asserted to be a List<String>
	 * @param columnName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAsStringList( String columnName ) {
		
		Object value = this.get(columnName);
		
		try 				{ return ( ArrayList<String> )value; }
		catch (Exception e ){ return new ArrayList<String>(); }
		
	}
	
	
	/**
	 * Returns a map list of rules saved.
	 * @param columnName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String>> getAsRuleList( String columnName ) {
		
		Object value = this.get(columnName);
		
		try					{ return (Map<String, List<String>>)value; }
		catch (Exception e) { return new HashMap<String, List<String>>(); }
		
	}
	
	
	/**
	 * gets an Object as a Boolean
	 * @param columnName
	 * @param defaultValue
	 * @return
	 */
	public Boolean getAsBoolean( String columnName, Boolean defaultValue ) {
		
		Object value = this.get( columnName );
		if ( value == null ) return defaultValue;
		return (Boolean)value;
		
	}
	
	
	/**
	 * Gets an Object as a string
	 * @param columnName
	 * @param defaultValue
	 * @return
	 */
	public String getAsString( String columnName, String defaultValue ) {
		
		Object value = this.get( columnName );
		if ( value == null ) return defaultValue;
		return value.toString();
		
	}
	

	/**
	 * Returns current filename
	 * @return
	 */
	public String getFileName() {
		return this.fileName;
	}
	
	
	/**
	 * Sets filename
	 * @param value
	 */
	public void setFileName( String value ) {
		this.fileName = value;
	}
	

	/**
	 * gets file extension
	 * @return
	 */
	public String getFileExt() {
		return this.fileExt;
	}
	

	/**
	 * sets file extension
	 * @param value
	 */
	public void setFileExt( String value ) {
		this.fileExt = value;
	}
	
	
	/**
	 * Convenience method for returning full read/write path
	 * @return
	 */
	public String getFullFileName() {

		String result = this.dataFolder + File.separator + "players" +  File.separator + this.getFileName() + "." + this.getFileExt();
		return result;
	}
	
	
	/**
	 * saves objects to a flat file
	 * @return
	 */
	public Boolean save() {
		
    	try {
			 SLAPI.save( this.data, this.getFullFileName() );
			 return true;
			 
		} catch (Exception e) { 
			e.printStackTrace();
		}		
		
    	return false;
	}
	
	
	/**
	 * loads objects from a flat file.
	 * @return
	 */
	public Boolean load() {
		
		try {
			this.data = SLAPI.load( this.getFullFileName() );
			return true;
			
		} catch ( Exception e ) {}
		
		return false;
		
	}
	
}
