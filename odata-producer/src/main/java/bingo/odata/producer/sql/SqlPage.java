/**
 * This file created at 2012-2-14.
 *
 * Copyright (c) 2002-2012 Bingosoft, Inc. All rights reserved.
 */
package bingo.odata.producer.sql;

public class SqlPage {

    private int index;
    private int size;
    private int start;
    private int end;
    
    public static SqlPage ofStartAndSize(int start,int size){
    	SqlPage page = new SqlPage();
    	
    	page.start = start;
    	page.size  = size;
    	page.end   = start + size - 1;
    	page.index = start < size ? 1 : page.end / size;
    	
    	return page;
    }
    
    protected SqlPage(){
    	
    }
    
    /**
     * @param index page index,start from 1
     * @param size page size,start from 1
     */
    public SqlPage(int index,int size){
        this.index = index;
        this.size  = size;
        this.start = (index - 1) * size + 1;
        this.end   = size * index;
    }
    
    /**
     * page index
     */
    public int index(){
        return index;
    }
    
    /**
     * page size
     */
    public int size(){
        return size;
    }
    
    /**
     * start index,from 1 to n
     */
    public int start(){
        return start;
    }
    
    /**
     * end index, from 1 to n
     */
    public int end(){
        return end;
    }
}
