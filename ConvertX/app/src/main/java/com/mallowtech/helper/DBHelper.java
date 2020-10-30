package com.mallowtech.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mallowtech.convertx.BuildConfig;

public class DBHelper extends SQLiteOpenHelper {
	public static String DB_NAME;
	public static SQLiteDatabase database = null;
	public final Context context;
	private String DB_PATH;
	private static final String[] CURRENCY_COLUMNS = {"ZEXCHANGERATE","ZFROMCURRENCY","ZTOCURRENCY"};

	@SuppressWarnings("static-access")
	public DBHelper(Context context2, String dbName) throws IOException {
		super(context2, dbName, null, 1);
		this.context = context2;
		this.DB_NAME = dbName;
		String packageName = context.getPackageName();
		DB_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases/";

	}

	public SQLiteDatabase openDataBase() {
		String path = DB_PATH + DB_NAME;
		try {
			if (database == null) {
				createDataBase();
				database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return database;
	}

	private void createDataBase() {
		try {
			boolean dbExist = checkDataBase();
			if (!dbExist) {
				this.getReadableDatabase();
				try {
					copyDataBase();
				} catch (IOException e) {
					throw new Error("Error copying database!");
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void copyDataBase() throws IOException {
		InputStream externalDbStream = context.getAssets().open(DB_NAME);

		String outFileName = DB_PATH + DB_NAME;

		OutputStream localDbStream = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = externalDbStream.read(buffer)) > 0) {
			localDbStream.write(buffer, 0, bytesRead);
		}
		localDbStream.close();
		externalDbStream.close();


	}

	private boolean checkDataBase() {
		boolean checkdb = false;
		try {
			String myPath = DB_PATH + DB_NAME;
			File dbfile = new File(myPath);
			checkdb = dbfile.exists();
		} catch (SQLiteException e) {
			Log.e(this.getClass().toString(),"Database doesn't exist");
		}
		return checkdb;
	}

	public synchronized void close() {
		if (database != null) {
			database.close();
		}
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}
	public void deleteCurrencyEntries()
	{
		try {
			SQLiteDatabase database = this.getWritableDatabase();

			String unitsQuery = "DELETE FROM ZCURRENCYCONVERSION";
			Cursor unitsCursor = database.rawQuery(unitsQuery, null);
			if(unitsCursor.getCount()==0)
			{
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	public void insertCurrencyEntries(ArrayList<String> convertFrom, ArrayList<String> convertTo, ArrayList<String> convertValue) {
		try {
			SQLiteDatabase database = this.getWritableDatabase();
			for (int i = 0; i < convertValue.size(); i++)
			{	ContentValues values = new ContentValues();
			values.put(CURRENCY_COLUMNS[0], convertValue.get(i).toString());
			values.put(CURRENCY_COLUMNS[1], convertFrom.get(i).toString());
			values.put(CURRENCY_COLUMNS[2], convertTo.get(i).toString());
			if(database.insert("ZCURRENCYCONVERSION", null, values)!=-1)
			{
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<ArrayList<String>> getUnits(String categoryName) {
		ArrayList<ArrayList<String>> unitsList = new ArrayList<ArrayList<String>>();
		try {
			SQLiteDatabase database = this.getWritableDatabase();

			String unitsQuery = "SELECT ZNAME,ZSYMBOL,ZUNITKEY FROM ZUNIT WHERE ZCATEGORY =(SELECT Z_PK FROM ZCONVERTXCATEGORY  WHERE ZNAME='"+categoryName+"') ORDER BY ZNAME";
			Cursor unitsCursor = database.rawQuery(unitsQuery, null);

			unitsList.clear();
			if (unitsCursor.moveToFirst()) {
				do {
					ArrayList<String> unitsItem = new ArrayList<String>();
					unitsItem.add(unitsCursor.getString(0));
					unitsItem.add(unitsCursor.getString(1));
					unitsItem.add(unitsCursor.getString(2));

					unitsList.add(unitsItem);
				} while (unitsCursor.moveToNext());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return unitsList;
	}

	public String getListOfOperations(String fromUnit, String toUnit,String categoryName) {

		String listOfOperations="";
		try {
			Cursor operationsCursor = database.rawQuery(
					"SELECT  ZLISTOFOPERATIONS  from ZCONVERSIONDATA "
							+ "WHERE ZFROMUNIT = ? AND ZTOUNIT = ? AND ZCATEGORY=?"
							, new String[]{fromUnit, toUnit,categoryName});
			if(operationsCursor.moveToFirst()){
				do{
					listOfOperations = operationsCursor.getString(0);
				}while(operationsCursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listOfOperations;
	}

	public Double getExchangeRateforcurrency(String fromUnit, String toUnit) {

		Double excahngeRate=0.0;
		String convercionDataFrom=null;
		String conversionDataTo=null;
		try {
			if(!fromUnit.equalsIgnoreCase(toUnit))
			{
				String operationsQuery ="SELECT ZEXCHANGERATE FROM ZCURRENCYCONVERSION WHERE ZFROMCURRENCY='EUR' AND ZTOCURRENCY='"+toUnit+"'";
				Cursor operationsCursor = database.rawQuery(operationsQuery, null);

				if(operationsCursor.moveToFirst()){
					do{
						convercionDataFrom = operationsCursor.getString(0);
					}while(operationsCursor.moveToNext());
				}

				String operationsQuery1 ="SELECT ZEXCHANGERATE FROM ZCURRENCYCONVERSION WHERE ZFROMCURRENCY='EUR' AND ZTOCURRENCY='"+fromUnit+"'";
				Cursor operationsCursor1 = database.rawQuery(operationsQuery1, null);

				if(operationsCursor1.moveToFirst()){
					do{
						conversionDataTo = operationsCursor1.getString(0);
					}while(operationsCursor1.moveToNext());
				}
				if(convercionDataFrom == null)
				{
					convercionDataFrom="0";
				}
				if(conversionDataTo ==null)
				{
					conversionDataTo="0";
				}
				excahngeRate=Double.parseDouble(convercionDataFrom)/Double.parseDouble(conversionDataTo);
			}
			else
			{
				excahngeRate=1.0;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return excahngeRate;
	}


	public ArrayList<String> getCategoryNames() {

		ArrayList<String> categoryName = new ArrayList<String>();
		try {
			SQLiteDatabase database = this.getWritableDatabase();

			String sectionQuery = "SELECT ZNAME FROM ZCONVERTXCATEGORY ORDER BY ZNAME";
			Cursor sectionCursor = database.rawQuery(sectionQuery, null);

			if (sectionCursor.moveToFirst()) {
				do {
					categoryName.add(sectionCursor.getString(0));

				} while (sectionCursor.moveToNext());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return categoryName;

	}

	public String getCategoryUnitsCount(String string) {
		String categoryId = null;
		String unitCount = null;
		try {
			SQLiteDatabase database = this.getWritableDatabase();
			String sectionQuery = "select z_pk from zconvertxcategory where zname='"+string+"'";
			Cursor sectionCursor = database.rawQuery(sectionQuery, null);
			if (sectionCursor.moveToFirst()) {
				do {
					categoryId = sectionCursor.getString(0);
				} while (sectionCursor.moveToNext());
			}
			String sectionQuery1 = "SELECT count(*) FROM ZUNIT WHERE ZCATEGORY=" + categoryId;
			Cursor sectionCursor1 = database.rawQuery(sectionQuery1, null);
			if (sectionCursor1.moveToFirst()) {
				do {
					unitCount = sectionCursor1.getString(0);
				} while (sectionCursor1.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		return unitCount;
	}
	public String getCategoryNamefromfavId(int catogoryId) {
		String categoryNameusingid = "";
		try {
			SQLiteDatabase database = this.getWritableDatabase();

			String sectionQuery = "SELECT ZNAME FROM ZCONVERTXCATEGORY WHERE Z_PK ="+catogoryId;
			Cursor sectionCursor = database.rawQuery(sectionQuery, null);

			if (sectionCursor.moveToFirst()) {
				do {
					categoryNameusingid = sectionCursor.getString(0);

				} while (sectionCursor.moveToNext());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return categoryNameusingid;

	}


	public int getCategoryKey(String titleTextValue) {
		int categoryKeyValue = 0;
		try {
			SQLiteDatabase database = this.getWritableDatabase();

			String sectionQuery = "SELECT Z_PK FROM ZCONVERTXCATEGORY WHERE ZNAME ='"+titleTextValue+"'";
			Cursor sectionCursor = database.rawQuery(sectionQuery, null);

			if (sectionCursor.moveToFirst()) {
				do {
					categoryKeyValue = sectionCursor.getInt(0);

				} while (sectionCursor.moveToNext());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return categoryKeyValue;
	}





	public int getKey(String fromUnitsDescription) {
		int fromKeyValue = 0;
		try {
			SQLiteDatabase database = this.getWritableDatabase();

			String sectionQuery = "SELECT Z_PK FROM ZUNIT WHERE ZNAME ='"+fromUnitsDescription+"'";
			Cursor sectionCursor = database.rawQuery(sectionQuery, null);

			if (sectionCursor.moveToFirst()) {
				do {
					fromKeyValue =sectionCursor.getInt(0);

				} while (sectionCursor.moveToNext());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fromKeyValue;
	}

	//inserting into favorites table.
	public void insertFavoriteDetails(int categoryUnit, int fromUnitKey,
			int toUnitKey) {
		try {
			SQLiteDatabase database = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("ZCATEGORY",categoryUnit);
			values.put("ZFROMUNIT",fromUnitKey);
			values.put("ZTOUNIT", toUnitKey);
			database.insert("ZFAVORITE", null, values);
			database.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//to get all values from favorites table to display.
	public ArrayList<ArrayList<Integer>> getFavoriteDetail() {
		ArrayList<ArrayList<Integer>> favoriteItems = new ArrayList<ArrayList<Integer>>();
		try {
			SQLiteDatabase database = this.getWritableDatabase();

			String sectionQuery = "SELECT ZCATEGORY,ZFROMUNIT,ZTOUNIT,Z_PK FROM ZFAVORITE";
			Cursor sectionCursor = database.rawQuery(sectionQuery, null);

			if (sectionCursor.moveToFirst()) {
				do {
					ArrayList<Integer> favorites = new ArrayList<Integer>();
					favorites.add(sectionCursor.getInt(0));
					favorites.add(sectionCursor.getInt(1));
					favorites.add(sectionCursor.getInt(2));
					favorites.add(sectionCursor.getInt(3));

					favoriteItems.add(favorites);
				} while (sectionCursor.moveToNext());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return favoriteItems;
	}

	//get name and symbol  for fromUnit values to display in favorites list
	public ArrayList<String> getFavoriteName(ArrayList<Integer> favoriteFromValue) {
		ArrayList<String> favorite = new ArrayList<String>();
		try {
			for(int i=0;i<favoriteFromValue.size();i++)
			{
				SQLiteDatabase database = this.getWritableDatabase();

				String sectionQuery = "SELECT ZNAME,ZSYMBOL FROM ZUNIT WHERE Z_PK = '"+favoriteFromValue.get(i)+"'"; 
				Cursor sectionCursor = database.rawQuery(sectionQuery, null);

				// instructorNmae.clear();
				if (sectionCursor.moveToFirst()) {
					do {
						//ArrayList<String>  favoriteItems = new ArrayList<String>();
						String firstValue = sectionCursor.getString(0) + "(" + sectionCursor.getString(1) +")";
						favorite.add(firstValue);



						//favorite.add(favoriteItems);
					} while (sectionCursor.moveToNext());

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return favorite;
	}

	//get name and symbol  for toUnit values to display in favorites list
	public ArrayList<String> getToName(ArrayList<Integer> favoriteToValue) {
		ArrayList<String>  favoriteItems = new ArrayList<String>();
		try {
			for(int i=0;i<favoriteToValue.size();i++)
			{
				SQLiteDatabase database = this.getWritableDatabase();

				String sectionQuery = "SELECT ZNAME,ZSYMBOL FROM ZUNIT WHERE Z_PK = '"+favoriteToValue.get(i)+"'"; 
				Cursor sectionCursor = database.rawQuery(sectionQuery, null);

				// instructorNmae.clear();
				if (sectionCursor.moveToFirst()) {
					do {

						String firstValue = sectionCursor.getString(0) + "(" + sectionCursor.getString(1) +")";
						favoriteItems.add(firstValue);



					} while (sectionCursor.moveToNext());

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return favoriteItems;
	}

	//check values present or not in favorites table	
	public boolean checkFavoritePresent(int categoryUnit, int fromUnitKey,
			int toUnitKey) {
		boolean available = false;
		try {
			SQLiteDatabase database = this.getWritableDatabase();
			String sectionQuery = "SELECT ZCATEGORY,ZFROMUNIT,ZTOUNIT FROM ZFAVORITE WHERE (ZCATEGORY='"+categoryUnit+"' AND ZFROMUNIT='"+fromUnitKey+"' AND ZTOUNIT='"+toUnitKey+"')";
			Cursor sectionCursor = database.rawQuery(sectionQuery, null);
			if (sectionCursor.getCount() != 0) {
				available = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return available;

	}

	//deleting rows in favorite table.
	public void deleteRowInFavorites(ArrayList<Integer> primaryId) {
		try {
			SQLiteDatabase database = this.getWritableDatabase();
			for(int i=0;i<primaryId.size();i++)
			{
				database.delete("ZFAVORITE","Z_PK"+"="+primaryId.get(i),null);
			}
			database.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

