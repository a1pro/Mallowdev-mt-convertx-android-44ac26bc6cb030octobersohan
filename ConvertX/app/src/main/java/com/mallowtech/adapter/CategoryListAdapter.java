package com.mallowtech.adapter;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mallowtech.convertx.R;
import com.mallowtech.helper.AppConstants;
import com.mallowtech.helper.DBHelper;

/**
 * The Class CategoryListAdapter.
 */
public class CategoryListAdapter extends BaseAdapter {
	
	/** The context. */
	Context context;
	
	/** The inflater. */
	LayoutInflater inflater;
	
	/** The category values. */
	ArrayList<String> categoryValues;
	
	/** The database. */
	SQLiteDatabase database;
	
	/** The category adapter db helper. */
	DBHelper categoryAdapterDbHelper;
	
	/** The db name. */
	String DB_NAME = AppConstants.databaseName;

	/**
	 * Instantiates a new category list adapter.
	 *
	 * @param context the context
	 * @param values the values
	 */
	public CategoryListAdapter(Context context, ArrayList<String> values) {
		this.context = context;
		this.categoryValues = values;
		try {
			categoryAdapterDbHelper = new DBHelper(context, DB_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		database = categoryAdapterDbHelper.openDataBase();

	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return categoryValues.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.gridlayout_listitem, arg2, false);
		try {
			TextView categoryText = (TextView) view.findViewById(R.id.text_textView);
			TextView unitsCountTextView = (TextView) view.findViewById(R.id.unitscount_textView);
			categoryText.setText(categoryValues.get(arg0));
			unitsCountTextView.setText(categoryAdapterDbHelper.getCategoryUnitsCount(categoryValues.get(arg0)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		view.setTag(categoryValues.get(arg0));
		return view;
	}

}
