package bingo.odata.consumer.ext;

import bingo.odata.ODataConstants;

public class Page {

	private int pageSize = 10;
	
	private int page = 1;
	
	public Page() {
	}
	
	public Page(int page) {
		this.page = page;
	}
	
	public Page(int page, int pageSize) {
		this.page = page;
		this.pageSize = pageSize;
	}
	
	public int getSkip() {
		return (page - 1) * pageSize;
	}
	
	public int getTop() {
		return pageSize;
	}
	
	public String toQueryString() {
		return ODataConstants.QueryOptions.SKIP + "=" + getSkip() + 
				"&" + ODataConstants.QueryOptions.TOP + "=" + getTop();
	}
}
