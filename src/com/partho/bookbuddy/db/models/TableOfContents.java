package com.partho.bookbuddy.db.models;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class TableOfContents {

	private String uid;
	private String chapterDepth;
	private ArrayList<Chapter> chapterList = new ArrayList<TableOfContents.Chapter>();
	
	public String getUid() {
		return uid;
	}


	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public ArrayList<Chapter> getChapterList()
	{
		return chapterList;
	}
	
	public Chapter getChapterItem(int index)
	{
		return chapterList.get(index);
	}
	
	public void addChapterItem(Chapter item)
	{
		chapterList.add(item);
	}


	public String getChapterDepth() {
		return chapterDepth;
	}


	public void setChapterDepth(String chapterDepth) {
		this.chapterDepth = chapterDepth;
	}


	public String toString()
	{
		return chapterList.toString();
	}
	
	
	public static class Chapter implements Parcelable
	{
		private String sequence;
		private String title;
		private String url;
		private String anchor;
		
		public Chapter()
		{
			
		}
		
		public Chapter(Parcel in)
		{
			String[] chapterArray = new String[4];
		    in.readStringArray(chapterArray);
		    this.sequence=chapterArray[0];
		    this.title=chapterArray[1];
		    this.url=chapterArray[2];
		    this.anchor=chapterArray[3];
		}
		
		public String getSequence() {
			return sequence;
		}
		public void setSequence(String sequence) {
			this.sequence = sequence;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getAnchor() {
			return anchor;
		}
		public void setAnchor(String anchor) {
			this.anchor = anchor;
		}
		
		public String toString()
		{
			return "s: "+sequence+" t: "+title+" u: "+url+" a: "+anchor;
		}
		@Override
		public int describeContents() {			
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeStringArray(new String[]{
				this.sequence,
				this.title,
				this.url,
				this.anchor
			});
			
		}
		
	  public static final Parcelable.Creator<Chapter> CREATOR= new Parcelable.Creator<Chapter>(){

		@Override
		public Chapter createFromParcel(Parcel source) {			
			return new Chapter(source);
		}

		@Override
		public Chapter[] newArray(int size) {
			return new Chapter[size];
		}
		  
	  };
	}
}
