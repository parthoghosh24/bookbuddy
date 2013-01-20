/**
 * @author Partho Ghosh
 * 
 * Book Buddy is an intelligent ebook reader. It is more focused on education sector 
 * and knowledge sharing. The goal behind this system is to create a reading system
 * which is not just only regular old reader but also an intelligent guide, tutor, motivating,
 * knowledge sharing system and it is fun also. The system will be more like a buddy to the person using it.
 * 
 * Some proposed features for this system till now:
 * - Revision system
 * - Notes making
 * - Review/Preview(upcoming topics)
 * - Scheduler
 * - Awards/Achievements
 * - Integrated Social Network
 * - Reading reminder
 * - Integrated scratchpad 
 * - Tagged word knowledge(For e.g, get more information on PI or matrix)
 * 
 * Many of the above features like scheduler, reading reminder,revision system will be based
 * on our original pattern recognition system. This system will be detecting the user usage and reading
 * pattern and based on that will perform above and many things beyond the features listed.
 *  
 */

package com.partho.bookbuddy;

import java.io.File;
import java.util.ArrayList;
import com.partho.bookbuddy.db.models.Book;
import com.partho.bookbuddy.db.models.TableOfContents;
import com.partho.bookbuddy.epubengine.EpubCore;
import com.partho.bookbuddy.epubengine.EpubRenderer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

public class BookBuddy extends Activity {
	
	private static final String TAG="BookBuddy";

	ListView ebookList;
	ArrayList<Book>bookList;
	EpubCore mEpubCore;
	EpubRenderer mEpubRenderer;
	BookListAdapter mBookListAdapter;
	String unzippedBookPath;
	Context currentContext;
	Book currentBook;
	TableOfContents currentBookToc;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.bookbuddy);
        mEpubCore = new EpubCore();
        currentContext = BookBuddy.this;
        bookList = mEpubCore.listOfBooks();  
        ebookList =(ListView)findViewById(R.id.book_list);
        mBookListAdapter = new BookListAdapter(getApplicationContext());
        ebookList.setAdapter(mBookListAdapter);
        ebookList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View view, int pos,long id) {
								
				currentBook = bookList.get(pos);
				new UnzipTask().execute();							
				
			}
		});     
    }
    
    class UnzipTask extends AsyncTask<Void, Void, Void>
    {

    	private ProgressDialog dialog = new ProgressDialog(currentContext);
    	@Override
    	protected void onPreExecute() {    		
    		dialog.show();
    	} 
    	
    	
		@Override
		protected Void doInBackground(Void... params) {
						
			String getFileName = currentBook.getFileName();			
			String modifiedFileName = getFileName.replace(AppConstants.BOOKSROOT+File.separator, "");
			String zipFolderName= modifiedFileName.replace(".epub", "");		
			Log.v(TAG, "ModfiedFileName "+zipFolderName);			
			unzippedBookPath=mEpubCore.unzipEpub(getFileName, zipFolderName);
			Log.v(TAG,"unzippedPath-> "+unzippedBookPath);
			currentBook =mEpubCore.loadBook(unzippedBookPath, currentBook);
			Log.i(TAG, "CurrentBook: "+currentBook);			
			currentBookToc =mEpubCore.getToc();
			Log.i(TAG,"currentBookToc: "+currentBookToc);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {			
			if(dialog.isShowing())
			{
				dialog.dismiss();
				Intent openBook = new Intent(getApplicationContext(), EpubRenderer.class);				
				openBook.putExtra(AppConstants.INTENTBOOK, currentBook);
				openBook.putParcelableArrayListExtra(AppConstants.INTENTCHAPTERLIST, currentBookToc.getChapterList());
				openBook.putExtra(AppConstants.INTENTBASEURL, mEpubCore.getBaseUrl());
				startActivity(openBook);
			}
		}			
    	
    }
            
    class BookListAdapter extends BaseAdapter
    {
    	
    	private Context mContext;
    	
    	public BookListAdapter(Context context)
    	{
    		mContext=context;
    	}

		@Override
		public int getCount() {			
			return bookList.size();
		}

		@Override
		public Book getItem(int position) {			
			return bookList.get(position);
		}

		@Override
		public long getItemId(int position) {			
			return position;
		}
		
		class ViewHolder
		{
			TextView bookName;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder viewHolder;
			viewHolder = new ViewHolder();
			Book book = bookList.get(position);
			
			if(convertView == null)
			{
				convertView= LayoutInflater.from(mContext).inflate(R.layout.listitem, parent, false);
				viewHolder.bookName = (TextView)convertView.findViewById(R.id.book_name);
				String modifiedBookName = book.getFileName().replace(AppConstants.BOOKSROOT+File.separator, "");
				Log.v(TAG, "Book name mod: "+modifiedBookName);
				viewHolder.bookName.setText(modifiedBookName);
				convertView.setTag(viewHolder);
			}
			else
			{
				viewHolder = (ViewHolder)convertView.getTag();
			}
			return convertView;
		}
    	
    }

}
