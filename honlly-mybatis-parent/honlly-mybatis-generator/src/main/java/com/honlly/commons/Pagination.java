package com.honlly.commons;

public class Pagination extends BaseBean {
	private static final long serialVersionUID = -5871263750693828476L;

	/** 不分页的skip */
	public static final int SKIP_NO_PAGING = -1;
	/** 不分页的limit */
	public static final int LIMIT_NO_PAGING = -1;

	/** 分页起始行，默认为-1，表示不分页，查询全部记录  */
	private int skip = SKIP_NO_PAGING;
	/** 每页显示行数，默认为-1，表示不分页，查询全部记录  */
	private int limit = LIMIT_NO_PAGING;
	/** 是否查询总记录数标志，默认为true */
	private boolean countTotals = true;

	public Pagination(){
	}

	public Pagination(int skip, int limit){
		this.skip = skip;
		this.limit = limit;
	}

	public Pagination(int skip, int limit, boolean countTotals){
		this(skip, limit);
		this.countTotals = countTotals;
	}

	public int getSkip() {
		return skip;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	public int getLimit() {
		return limit;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public boolean isCountTotals() {
		return countTotals;
	}

	public void setCountTotals(boolean countTotals) {
		this.countTotals = countTotals;
	}

	public boolean isInvalid() {
		return skip < 0 || limit < 0;
	}

}
