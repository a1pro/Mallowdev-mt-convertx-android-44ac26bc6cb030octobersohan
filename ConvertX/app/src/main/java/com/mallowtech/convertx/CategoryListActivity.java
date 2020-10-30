package com.mallowtech.convertx;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.mallowtech.adapter.CategoryListAdapter;
import com.mallowtech.helper.AppConstants;
import com.mallowtech.helper.DBHelper;

/**
 * The Class CategoryListActivity.
 */
public class CategoryListActivity extends Activity {
	
	/** The search button. */
	Button upArrowButton, searchButton;
	
	/** The cancel text view. */
	TextView categoryTitleText, cancelTextView;
	
	/** The search text. */
	EditText searchText;
	
	/** The category listgridview. */
	GridView categoryListgridview;
	
	/** The category list search. */
	ArrayList<String> categoryList, categoryListSearch;
	
	/** The database helper. */
	DBHelper databaseHelper;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_categorylist);

		// initialize database
		try {
			databaseHelper = new DBHelper(this, AppConstants.databaseName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		databaseHelper.openDataBase();

		// initialize variables
		upArrowButton = (Button) findViewById(R.id.uparrow_button);
		searchButton = (Button) findViewById(R.id.search_button);
		searchText = (EditText) findViewById(R.id.search_edittext);
		cancelTextView = (TextView) findViewById(R.id.cancel_button);
		categoryListgridview = (GridView) findViewById(R.id.categorylist_gridView);
		categoryTitleText = (TextView) findViewById(R.id.categorytitle_textView);

		// set the animation
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(CategoryListActivity.this, R.anim.slide_down);
		categoryListgridview.startAnimation(hyperspaceJumpAnimation);

		// implementing search functionality
		searchButton.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				searchText.setVisibility(View.VISIBLE);
				cancelTextView.setVisibility(View.VISIBLE);
				searchButton.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
			}
		});

		// implementing cancel functionality
		cancelTextView.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(cancelTextView.getWindowToken(), 0);
				searchText.setText("");
				searchText.setVisibility(View.GONE);
				cancelTextView.setVisibility(View.GONE);
				searchButton.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.ic_menu_search));
			}
		});

		// setting category list adapter
		categoryList = new ArrayList<String>();
		categoryListSearch = new ArrayList<String>();
		categoryList = databaseHelper.getCategoryNames();
		categoryListgridview.setAdapter(new CategoryListAdapter(CategoryListActivity.this, categoryList));
		categoryListgridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				categoryTitleText.setText((CharSequence) arg1.getTag());
				searchText.setVisibility(View.GONE);
				cancelTextView.setVisibility(View.GONE);
				Intent backIntent = new Intent(CategoryListActivity.this, ConverterActivity.class);
				backIntent.putExtra("SelectedCategoryName", categoryTitleText.getText().toString());
				AppConstants.fromfavoriteScreen = 1;
				startActivity(backIntent);
				finish();
			}
		});

		searchText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

				try {
					String searchString = searchText.getText().toString();
					int textLength = searchString.length();
					categoryListSearch.clear();
					String text;

					for (int i = 0; i < categoryList.size(); i++) {
						text = categoryList.get(i);

						if (textLength <= text.length()) {
							// compare the String in EditText with Names in the
							// ArrayList
							if (searchString.equalsIgnoreCase(text.substring(0, textLength)))
								categoryListSearch.add(categoryList.get(i));
						}
					}
					categoryListgridview.setAdapter(new CategoryListAdapter(CategoryListActivity.this, categoryListSearch));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// start Converter Activity
		upArrowButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {

					Intent backIntent = new Intent(CategoryListActivity.this, ConverterActivity.class);
					backIntent.putExtra("SelectedCategoryName", categoryTitleText.getText().toString());
					AppConstants.fromfavoriteScreen = 1;
					startActivity(backIntent);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}
