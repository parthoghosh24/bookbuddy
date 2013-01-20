/**
 *@author Partho Ghosh
 *
 * Zip Utility class for Book Buddy:
 * This class performs all zip based functionalities of the system.
 * Currently this class deals with EPUB only but it can be extended for similar
 * kind of zipped books tomorrow.
 */

package com.partho.bookbuddy.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.util.Log;

public class BBZipUtil {
	
private static final String TAG="BBZipUtil";
private static final int BUFFERVAL=1024;
BBFileUtil mFileUtil;
File unzippedFolder;

 public String unzipBook(String zipFile, String outputFolder)
 {
	 String outputPath="";
	 byte[] buffer = new byte[BUFFERVAL];
	 
	 try
	 {
		 mFileUtil = new BBFileUtil();
		 unzippedFolder = mFileUtil.createFolder(outputFolder);
		 outputPath=unzippedFolder.getAbsolutePath();
		 Log.v(TAG, "Output Folder path: "+outputPath);
		 ZipInputStream zipInput = new ZipInputStream(new FileInputStream(zipFile));
		 ZipEntry zEntry = zipInput.getNextEntry();
		 
		 while(zEntry!= null)
		 {
			 String fileName = zEntry.getName();
			 Log.v(TAG, "zEntry file "+fileName);
			 File newFile = new File(outputFolder+File.separator+fileName);
			 Log.v(TAG, "New File/Folder: "+newFile.getAbsolutePath());
			 
			 if(zEntry.isDirectory())
			 {
				 (new File(newFile.getAbsolutePath())).mkdirs(); 
			 }
			 else
			 {
				 (new File(newFile.getParent())).mkdirs();
				 FileOutputStream fileOutputStream = new FileOutputStream(newFile);
				 int length=0;
				 while((length=zipInput.read(buffer))>0)
				 {
					 fileOutputStream.write(buffer,0,length);
				 }
			 }
			 
			 
			 zEntry=zipInput.getNextEntry();
		 }		 
		 zipInput.close();
		 
	 }
	 catch(IOException ie)
	 {
		 ie.printStackTrace();
	 }
	 return outputPath;
	 
 }

}
