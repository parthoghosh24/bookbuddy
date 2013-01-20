/**
 * @author Partho Ghosh
 * 
 * This class will fetch Epubs from system and pass it to various
 * parts of the system. Such as it will pass on list of epubs to root 
 * actvity,i.e, BookBuddy. This class will also extract the metadata as
 * well as the data from the epubs and pass it to the core system.
 * This class will also generate the required temporary output folder in which
 * unzipped epub files will exist when user opens the ebook as well as remove it 
 * when user closes the ebook.
 *  
 */

package com.partho.bookbuddy.epubengine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.partho.bookbuddy.AppConstants;
import com.partho.bookbuddy.Utils.BBFileUtil;
import com.partho.bookbuddy.Utils.BBZipUtil;
import com.partho.bookbuddy.db.models.Book;
import com.partho.bookbuddy.db.models.TableOfContents;
import com.partho.bookbuddy.epubengine.parser.BookSaxParser;
import com.partho.bookbuddy.epubengine.parser.TableOfContentsSaxParser;

public class EpubCore {

	private static final String TAG = "EpubCore";
	BBFileUtil mFileUtil;
	BBZipUtil mZipUtil;
	TableOfContents mTableofContents = new TableOfContents();
	String mBaseUrl = "";

	public EpubCore() {
		initUtils();
	}

	private void initUtils() {
		mFileUtil = new BBFileUtil();
		mZipUtil = new BBZipUtil();
	}

	public ArrayList<Book> listOfBooks() {

		ArrayList<Book> books = new ArrayList<Book>();
		ArrayList<String> pathList = mFileUtil.getEbookFileLocations(".epub");
		for (String pathName : pathList) {
			Book mBook = new Book();
			mBook.setFileName(pathName);
			books.add(mBook);
		}

		return books;

	}

	public String unzipEpub(String bookPath, String fileName) {
		String unzippedPath = "";
		unzippedPath = mZipUtil.unzipBook(bookPath, AppConstants.BOOKSROOT
				+ File.separator + fileName);
		return unzippedPath;
	}

	public boolean removeUnzippedEpubDir(String unzippedPath) {
		boolean result = false;
		return result;
	}

	public String getOPFFilePath(File containerFile, String unzippedBook) {
		String filePath = "";

		try {
			InputStream reader = new FileInputStream(containerFile);
			XmlPullParser containerFileParser = XmlPullParserFactory
					.newInstance().newPullParser();
			containerFileParser.setInput(reader, "UTF_8");
			int eventType = containerFileParser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {

				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (containerFileParser.getName().equalsIgnoreCase(
							"rootfile")) {
						Log.v(TAG,
								"Container path: "
										+ unzippedBook
										+ File.separator
										+ containerFileParser
												.getAttributeValue(null,
														"full-path"));
						filePath = unzippedBook
								+ File.separator
								+ containerFileParser.getAttributeValue(null,
										"full-path");
					}
					break;
				}

				eventType = containerFileParser.next();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		return filePath;

	}

	public Book loadBook(String unzippedBook, Book currentBook) {

		// get Content path
		Log.v(TAG, "unzippedBook " + unzippedBook);
		Log.v(TAG, "unzippedBook Meta " + unzippedBook
				+ AppConstants.CONTAINERFOLDER);
		File containerFile = new File(unzippedBook
				+ AppConstants.CONTAINERFOLDER);

		// Get content opf file
		String opfFilePath = getOPFFilePath(containerFile, unzippedBook);
		Log.i(TAG, "opffilePath " + opfFilePath);
		mBaseUrl = opfFilePath.substring(0, opfFilePath.lastIndexOf("/"));
		Log.i(TAG, "Base url " + mBaseUrl);
		String mTocPath = "";

		// Populate book bean
		if (!opfFilePath.isEmpty()) {
			currentBook.setOpfFileName(opfFilePath);
			BookSaxParser bookParser = new BookSaxParser();
			currentBook = bookParser.getBook(opfFilePath, currentBook);
		}

		// get Toc path

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(opfFilePath));

			Element ncxElement = (Element) document.getElementById("ncx");
			mTocPath = mBaseUrl + File.separator
					+ ncxElement.getAttributeNode("href").getValue();
			Log.i(TAG, "TOC path:" + mTocPath);

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!mTocPath.isEmpty()) {
			TableOfContentsSaxParser tocSaxParser = new TableOfContentsSaxParser();
			mTableofContents = tocSaxParser.getToc(mTocPath);

		}
		return currentBook;
	}



	public TableOfContents getToc() {
		return mTableofContents;
	}

	public String getBaseUrl() {
		return mBaseUrl;
	}

}
