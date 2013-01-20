package com.partho.bookbuddy.db.models;

public class Category {
	
	private long _id;
	private String categoryName;
	private long score;
	
	public void setId(long _id)
	{
		this._id = _id;
	}
	
	public long getId()
	{
		return _id;
	}
	
	public void setCategoryName(String categoryName)
	{
		this.categoryName= categoryName;
	}
	
	public String getCategoryName()
	{
		return categoryName;
	}
	
	public void setScore(long score)
	{
		this.score=score;
	}
	
	public long getScore()
	{
		return score;
	}
	

}
