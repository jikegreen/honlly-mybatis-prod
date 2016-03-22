package com.honlly.entity.domain;

import com.honlly.commons.BaseBean;

public class Config extends BaseBean {
	private static final long serialVersionUID = 2737951182161773619L;

	private String rootPackage;
	private String author;
	private String ignoreTablePrefix;
	private String tableNames;
	private String user;

	public String getRootPackage() {
		return rootPackage;
	}

	public void setRootPackage(String rootPackage) {
		this.rootPackage = rootPackage;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIgnoreTablePrefix() {
		return ignoreTablePrefix;
	}

	public void setIgnoreTablePrefix(String ignoreTablePrefix) {
		this.ignoreTablePrefix = ignoreTablePrefix;
	}

	public String getTableNames() {
		return tableNames;
	}

	public void setTableNames(String tableNames) {
		this.tableNames = tableNames;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
