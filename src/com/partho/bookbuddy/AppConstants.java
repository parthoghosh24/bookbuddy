/** @author Partho Ghosh
 * 
 *  Contains Application constants
 */

package com.partho.bookbuddy;

import java.io.File;

import android.os.Environment;

public class AppConstants {
	
	public static final String BOOKSROOT= Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"Books";
	public static final String INTENTBOOK="book";
	public static final String INTENTCHAPTERLIST="chapterList";
	public static final String INTENTBASEURL="baseUrl";
	public static final String CONTAINERFOLDER="/META-INF/container.xml";

}
