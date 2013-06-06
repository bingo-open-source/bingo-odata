/**
 * created at 2013-3-4
 */
package bingo.odata.producer.query;


public interface QuerySelected {

	boolean isSelectedField(String name);
	
	String[] getSelectedFields();
	
}
