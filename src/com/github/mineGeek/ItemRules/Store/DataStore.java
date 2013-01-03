package com.github.mineGeek.ItemRules.Store;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class DataStore {

	public Map<String, Object> data = new HashMap<String, Object>();
	
	private String _fileName 	= "playerData";
	private String _fileExt 	= "bin";
	public 	String dataFolder;
	public 	Boolean active = false;
	
	public DataStore( String dataFolder ) {
		this.dataFolder = dataFolder;
	}
	
	public void set( String ColumnName, Object value ) {
		
		this.data.put(ColumnName, value);
	}
	
	public Object get( String columnName ) {
		
		return this.data.get( columnName );
		
	}
	
	public Integer getAsInteger( String columnName, Integer defaultValue ) {
		
		Object value = this.get( columnName );
		
		if ( value == null ) return defaultValue;
		return (Integer)this.get( columnName );
		
	}
	
	public Boolean getAsBoolean( String columnName, Boolean defaultValue ) {
		Object value = this.get( columnName );
		if ( value == null ) return defaultValue;
		return (Boolean)value;
	}
	
	public String getAsString( String columnName, String defaultValue ) {
		Object value = this.get( columnName );
		if ( value == null ) return defaultValue;
		return value.toString();
	}
	
	public String getFileName() {
		return this._fileName;
	}
	
	public void setFileName( String value ) {
		this._fileName = value;
	}
	
	public String getFileExt() {
		return this._fileExt;
	}
	
	public void setFileExt( String value ) {
		this._fileExt = value;
	}
	

	public String getFullFileName() {

		return this.dataFolder + File.separator + "players" +  File.separator + this.getFileName() + "." + this.getFileExt();
	}
	
	public Boolean save() {
		
    	try {
			 SLAPI.save( this.data, this.getFullFileName() );
			 return true;
			 
		} catch (Exception e) { e.printStackTrace(); }		
		
    	return false;
	}
	
	public Boolean load() {
		
		try {
			this.data = SLAPI.load( this.getFullFileName() );
			return true;
			
		} catch ( Exception e ) {}
		
		return false;
		
	}
	
}
