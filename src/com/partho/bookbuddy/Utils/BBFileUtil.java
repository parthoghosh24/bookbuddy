/**
 * @author Partho Ghosh
 * 
 * File Utility class for Book Buddy:
 * This class will comprise of all the needed File handling methods in the
 * system.
 */

package com.partho.bookbuddy.Utils;

import java.io.File;
import java.util.ArrayList;

import com.partho.bookbuddy.AppConstants;

import android.os.Environment;
import android.util.Log;

public class BBFileUtil {

	private static final String TAG = "BBFileUtil";
	public File ebookFiles[];
	File fileSystemRoot;

	public ArrayList<String> getEbookFileLocations(String extension) {
		ArrayList<String> pathList = new ArrayList<String>();		
		
		Log.v(TAG, "In BooksRoot "+AppConstants.BOOKSROOT);
		
		File ebooksDir = new File(AppConstants.BOOKSROOT);
		File[] books = ebooksDir.listFiles();
		for (File book : books) {
			if (!extension.isEmpty() && book.getName().contains(extension)) {
				pathList.add(book.getAbsolutePath());
			}

		}

		return pathList;

	}

	public File getFileSystemRoot() {
		File root = Environment.getExternalStorageDirectory();
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			root = Environment.getExternalStorageDirectory();
		}
		return root;
	}

	public File createFolder(String path) {
		File outputFolder = new File(path);
		outputFolder.mkdir();
		return outputFolder;
	}
	
	public void deleteDir(File dir)
	{
		if(dir.isDirectory())
		{
			for(File file:dir.listFiles())
			{
				deleteDir(file);
			}
		}
		dir.delete();
	}

}
