package com.mallowtech.convertx;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mallowtech.adapter.CheckBoxState;
import com.mallowtech.adapter.FavoriteAdapter;
import com.mallowtech.helper.AppConstants;
import com.mallowtech.helper.DBHelper;

/**
 * The Class FavoriteActivity.
 */
public class FavoriteActivity extends Activity {

	/** The title text. */
	TextView value1, value2,titleText;
	
	/** The units layout. */
	RelativeLayout titleLayout,fullDemoLayout,unitsLayout;
	
	/** The delete. */
	Button navigationButton, Ok, delete;
	
	/** The favorites list. */
	ListView favoritesList;
	
	/** The database helper. */
	DBHelper databaseHelper;
	
	/** The favorite details. */
	ArrayList<ArrayList<Integer>> favoriteDetails;
	
	/** The favorite to name. */
	ArrayList<String> favoriteFromName,favoriteToName;
	
	/** The primary id. */
	ArrayList<Integer> favoriteFromValue, favoriteToValue, favoriteId, primaryId;
	
	/** The favoritereturned list. */
	ArrayList<CheckBoxState> favoriteCheckBoxList,favoritereturnedList;
	
	/** The deletebutton pressed. */
	boolean showCheckBox,deletebuttonPressed;
	
	/** The favorite adapter. */
	FavoriteAdapter favoriteAdapter;
	
	/** The fadeout imageview. */
	ImageView fadeinImageview,fadeoutImageview;
    
    /** The anim fadeout. */
    Animation animFadein,animFadeout;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_favorite);
      
		//open database
		try {
			databaseHelper = new DBHelper(this, AppConstants.databaseName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		databaseHelper.openDataBase();
		
		//Initialization
		favoriteCheckBoxList = new ArrayList<CheckBoxState>();
		favoritereturnedList = new ArrayList<CheckBoxState>();
		favoriteFromName = new ArrayList<String>();
		favoriteToName = new ArrayList<String>();
		favoriteFromValue = new ArrayList<Integer>();
		favoriteToValue = new ArrayList<Integer>();
		favoriteId = new ArrayList<Integer>();
		primaryId = new ArrayList<Integer>();
		
        fullDemoLayout = (RelativeLayout) findViewById(R.id.fullDemo_layout);
		titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
		value1 = (TextView) findViewById(R.id.value1_textView);
		value2 = (TextView) findViewById(R.id.value2_textView);
		navigationButton = (Button) findViewById(R.id.navigation_button);
		delete = (Button) findViewById(R.id.delete_button);
		favoritesList=(ListView) findViewById(R.id.favorites_list);
		fadeinImageview=(ImageView) findViewById(R.id.fadein_imageView);
		fadeoutImageview=(ImageView) findViewById(R.id.fadeout_imageView);
		
		//setting animation for thumb imageview
		 animFadein = AnimationUtils.loadAnimation(FavoriteActivity.this,R.anim.fade_in);
		 animFadeout=AnimationUtils.loadAnimation(FavoriteActivity.this,R.anim.fade_out);
		 
		 animFadein. setAnimationListener(new AnimationListener() {

		        @Override
		        public void onAnimationStart(Animation animation) {

		        }

		        @Override
		        public void onAnimationRepeat(Animation animation) {

		        }

		        @Override
		        public void onAnimationEnd(Animation animation) {

		        	animFadeout.setAnimationListener(new AnimationListener() {

		                @Override
		                public void onAnimationStart(Animation animation) {

		                }

		                @Override
		                public void onAnimationRepeat(Animation animation) {

		                }

		                @Override
		                public void onAnimationEnd(Animation animation) {
		                    fadeinImageview.setImageResource(R.drawable.thumbprint);
		                    fadeinImageview.startAnimation(animFadein);
		                    fadeoutImageview.setImageResource(R.drawable.thumbprint);
		                    fadeoutImageview.startAnimation(animFadeout);
		                }
		            });
		        	fadeinImageview.setImageResource(R.drawable.thumbprint);
		        	fadeinImageview.startAnimation(animFadein);
		        	fadeoutImageview.setImageResource(R.drawable.thumbprint);
		        	fadeoutImageview.startAnimation(animFadeout);


		        }
		    });

		 fadeinImageview.startAnimation(animFadein);
		 fadeoutImageview.startAnimation(animFadeout);
		
		//back to home activity
		navigationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		//handling click event foe favorite list
		favoritesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try {
					ArrayList<String> favoriteFromNametoconverter=new ArrayList<String>();
					favoriteFromNametoconverter = databaseHelper.getFavoriteName(favoriteFromValue);
					ArrayList<String> favoriteToNameforconverter=new ArrayList<String>();
					favoriteToNameforconverter = databaseHelper.getToName(favoriteToValue);
					String categorynames=databaseHelper.getCategoryNamefromfavId(favoriteDetails.get(position).get(0));
					Intent intent=new Intent(FavoriteActivity.this,ConverterActivity.class);
					intent.putExtra("fromunit", favoriteFromNametoconverter.get(position));
					intent.putExtra("tounit", favoriteToNameforconverter.get(position));
					AppConstants.fromfavoriteScreen=2;
					intent.putExtra("SelectedCategoryName",categorynames);
					startActivity(intent);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});

		showCheckBox = false;
		deletebuttonPressed = false;

		try {
			//getting values from favorite table
			favoriteDetails = databaseHelper.getFavoriteDetail();
			
			//set the favorite list while adding favorite from converter.
			if ((favoriteDetails.size() > 0) && (!favoriteDetails.isEmpty())) 
			{
				favoritesList = (ListView) findViewById(R.id.favorites_list);
				titleLayout.setVisibility(View.VISIBLE);
				delete.setVisibility(View.VISIBLE);
				favoritesList.setVisibility(View.VISIBLE);
				fullDemoLayout.setVisibility(View.INVISIBLE);
				
			    
				favoritereturnedList = passMethod(favoriteDetails);
				favoriteAdapter = new FavoriteAdapter(FavoriteActivity.this, favoritereturnedList, showCheckBox);
				favoritesList.setAdapter(favoriteAdapter);
				fadeinImageview.setVisibility(View.GONE);
				fadeoutImageview.setVisibility(View.GONE);
			}

			else {
				fullDemoLayout.setVisibility(View.VISIBLE);
				titleLayout.setVisibility(View.VISIBLE);
				delete.setVisibility(View.INVISIBLE);
				fadeinImageview.setVisibility(View.VISIBLE);
				fadeoutImageview.setVisibility(View.VISIBLE);
			    value1.setText("0");
				value2.setText("0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//intent to go converter activity to add favorite
		Ok = (Button) findViewById(R.id.ok_button);
		Ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent goToConverter = new Intent(FavoriteActivity.this, ConverterActivity.class);
				startActivity(goToConverter);
				AppConstants.fromfavoriteScreen=0;
				finish();
			}
		});

		//deleting favorite list.
		delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					//on button first click displays list with checkbox,On button second click deletes the list.
					if (deletebuttonPressed == false) 
					{
						showCheckBox = true;
						deletebuttonPressed = true;
					} 
					else 
					{
						deletebuttonPressed = false;
						showCheckBox = false;
						//getting the  values from adapter and check which values are selected and delete those items by getting primary key.
						ArrayList<CheckBoxState> list = favoriteAdapter.checkState;
						for (int i = 0; i < list.size(); i++) 
						{
							CheckBoxState state = list.get(i);						
							if (state.isSelected()) 
							{
								primaryId.add(state.getPrimaryID());
							}
						}
						//deleting values in database.
						databaseHelper.deleteRowInFavorites(primaryId);
						favoriteDetails.clear();
						favoriteDetails = databaseHelper.getFavoriteDetail();
						favoritereturnedList =  passMethod(favoriteDetails);					
					}
					//set the adapter for remaining items.Displays demo screen when no values in list.
					if((favoritereturnedList.isEmpty())&&(favoritereturnedList.size()==0))
					{
						fullDemoLayout.setVisibility(View.VISIBLE);
						titleLayout.setVisibility(View.VISIBLE);
						favoritesList.setVisibility(View.INVISIBLE);
						delete.setVisibility(View.INVISIBLE);
						fadeinImageview.setVisibility(View.VISIBLE);
						fadeoutImageview.setVisibility(View.VISIBLE);
					    value1.setText("0");
						value2.setText("0");
					}
					else
					{				
					favoriteAdapter = new FavoriteAdapter(FavoriteActivity.this,favoritereturnedList,showCheckBox);
					favoritesList.setAdapter(favoriteAdapter);
					fadeinImageview.setVisibility(View.GONE);
					fadeoutImageview.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	// get from and to unit name from (integer)unit key value
	/**
	 * Pass method.
	 *
	 * @param favoriteDetails the favorite details
	 * @return the array list
	 */
	private ArrayList<CheckBoxState> passMethod(ArrayList<ArrayList<Integer>> favoriteDetails) {

		try {
			favoriteFromValue.clear();
			favoriteToValue.clear();
			favoriteId.clear();
			for (int i = 0; i < favoriteDetails.size(); i++) {

				favoriteFromValue.add(favoriteDetails.get(i).get(1));
				favoriteToValue.add(favoriteDetails.get(i).get(2));
				favoriteId.add(favoriteDetails.get(i).get(3));
			}

			favoriteFromName = databaseHelper.getFavoriteName(favoriteFromValue);
			favoriteToName = databaseHelper.getToName(favoriteToValue);

			favoriteCheckBoxList.clear();
			CheckBoxState stateObject;
			for (int i = 0; i < favoriteFromName.size(); i++) {
				stateObject = new CheckBoxState(favoriteFromName.get(i).toString(), favoriteToName.get(i).toString(), false, favoriteId.get(i));
				favoriteCheckBoxList.add(stateObject);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return favoriteCheckBoxList;
		
	}

}
