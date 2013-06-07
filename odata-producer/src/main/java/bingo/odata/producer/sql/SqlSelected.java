/**
 * created at 2013-3-4
 */
package bingo.odata.producer.sql;


public interface SqlSelected {

	boolean isSelectedField(String name);
	
	String[] getSelectedFields();
	
}
