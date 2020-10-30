package com.mallowtech.convertx;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

import android.R.drawable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mallowtech.adapter.UnitGridAdapter;
import com.mallowtech.helper.AppConstants;
import com.mallowtech.helper.DBHelper;
import com.mallowtech.helper.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class ConverterActivity.
 */
@SuppressLint("UseValueOf")
public class ConverterActivity extends Activity {
	
	/** The value string. */
	StringBuilder valueString;
	
	/** The m click listener. */
	ButtonClickListener mClickListener;
	
	/** The units cancel text. */
	TextView value1, value2, units1, units2, unitDescription1, unitDescription2,titleText,unitsCancelText;
	
	/** The delete button. */
	ImageButton deleteButton;
	
	/** The navigation button. */
	Button clearButton, exponentialButton, plusminusButton, swapButton,downArrowButton, navigationButton;
	
	/** The digits. */
	String DIGITS = AppConstants.Digits;
	
	/** The copy value2. */
	String category,value1Text,copyValue2 = "0";
	
	/** The exchange rate. */
	Double calculatedResult,exchangeRate = 0.0;
	
	/** The search layout clicked. */
	boolean fromSelected = false,toSelected = false,availableFavorite,backtohome = true,epowerPressed = false,searchLayoutClicked = false;
	
	/** The numbers layout. */
	LinearLayout fromLayout, toLayout, numbersLayout;
	
	/** The units grid. */
	GridView unitsGrid;
	
	/** The context. */
	Context context;
	
	/** The from favorite screen. */
	int numberOfDigits = 0,categoryUnit, fromUnitKey, toUnitKey,fromFavoriteScreen = 0;
	 
 	/** The list of operations. */
 	String fromUnit, toUnit, listOfOperations;
	
	/** The database helper. */
	DBHelper databaseHelper;
	
	/** The unitssearch list. */
	ArrayList<ArrayList<String>> unitsList,unitssearchList;
	
	/** The get operation values for adapter. */
	ArrayList<String> unitsSearchListOperation,getOperationValuesForAdapter;
	
	/** The operations. */
	String operations[] = null;
	
	/** The change unit layout. */
	RelativeLayout titleLayout, parentLayout,unitsLayout, changeUnitLayout;
	
	/** The unit search text. */
	EditText unitSearchText;
	
	/** The tounit descfrom fav. */
	String selectedCategoryName = AppConstants.defaultCategoryname,fromUnitfromfav = "",toUnitfromFav = "",fromUnitDescfromFav = "",tounitDescfromFav = "";

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_converter);
		context = this.getApplicationContext();

		try {
			databaseHelper = new DBHelper(this,AppConstants.databaseName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		databaseHelper.openDataBase();
		
		// initializing variables
		downArrowButton = (Button) findViewById(R.id.arrow_button);
		unitsLayout = (RelativeLayout) findViewById(R.id.units_layout);
		titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
		toLayout = (LinearLayout) findViewById(R.id.toLayout);
		numbersLayout = (LinearLayout) findViewById(R.id.numbers_layout);
		parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		navigationButton = (Button) findViewById(R.id.navigation_button);
		titleText = (TextView) findViewById(R.id.title_textView);
		changeUnitLayout = (RelativeLayout) findViewById(R.id.searchLayout);
		unitSearchText = (EditText) findViewById(R.id.unitssearch_edittext);
		unitsCancelText = (TextView) findViewById(R.id.unitscancel_button);
		fromLayout = (LinearLayout) findViewById(R.id.fromLayout);
		toLayout = (LinearLayout) findViewById(R.id.toLayout);
		numbersLayout = (LinearLayout) findViewById(R.id.numbers_layout);
		unitsGrid = (GridView) findViewById(R.id.units_gridView);
		value1 = (TextView) findViewById(R.id.value1_textView);
		value2 = (TextView) findViewById(R.id.value2_textView);
		units1 = (TextView) findViewById(R.id.units1_textView);
		units2 = (TextView) findViewById(R.id.units2_textView);
		unitDescription1 = (TextView) findViewById(R.id.units_desc_textView);
		unitDescription2 = (TextView) findViewById(R.id.units_desc2_textView);
		deleteButton = (ImageButton) findViewById(R.id.button_delete);
		clearButton = (Button) findViewById(R.id.button_clear);
		exponentialButton = (Button) findViewById(R.id.button_exp);
		plusminusButton = (Button) findViewById(R.id.button_plusMinus);
		swapButton = (Button) findViewById(R.id.button_swap);

		//set the animation for units and numbers layout
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate);
		unitsGrid.startAnimation(hyperspaceJumpAnimation);
		Animation hyperspaceJumpAnimation_number = AnimationUtils.loadAnimation(context, R.anim.rotate);
		numbersLayout.startAnimation(hyperspaceJumpAnimation_number);
		
		// start CategoryList activity
		downArrowButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent categoryListIntent = new Intent(ConverterActivity.this, CategoryListActivity.class);
				startActivity(categoryListIntent);
				finish();
			}
		});

		// getting selected category name from categorylistactivity
		try {
			Bundle backIntentBundle = getIntent().getExtras();
			if (backIntentBundle != null) {
				fromFavoriteScreen = AppConstants.fromfavoriteScreen;
				if (fromFavoriteScreen == 1) {
					selectedCategoryName = backIntentBundle.getString("SelectedCategoryName");
					titleText.setText(selectedCategoryName);
				} else if (fromFavoriteScreen == 2) {
					selectedCategoryName = backIntentBundle.getString("SelectedCategoryName");
					titleText.setText(selectedCategoryName);
					fromUnitfromfav = backIntentBundle.getString("fromunit");
					toUnitfromFav = backIntentBundle.getString("tounit");
					String fromunitDescaraay[] = fromUnitfromfav.split("\\(");
					fromUnitDescfromFav = fromunitDescaraay[1].substring(0, fromunitDescaraay[1].length() - 1);
					String tounitDescaraay[] = toUnitfromFav.split("\\(");
					tounitDescfromFav = tounitDescaraay[1].substring(0, tounitDescaraay[1].length() - 1);
					fromUnitfromfav = fromunitDescaraay[0];
					toUnitfromFav = tounitDescaraay[0];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// display favorites pop up menu for from lyout
		fromLayout.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				try {
					if (unitsGrid.getVisibility() == View.GONE || unitsGrid.getVisibility() == View.INVISIBLE) {
						PopupMenu pop = new PopupMenu(ConverterActivity.this, arg0);
						pop.getMenuInflater().inflate(R.menu.pop_menu, pop.getMenu());

						pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem arg0) {
								if (arg0.getTitle().toString().equalsIgnoreCase(AppConstants.favPopupMenu1)) {
									String titleTextValue = titleText.getText().toString();
									String fromUnitsDescription = unitDescription1.getText().toString();
									String toUnitsDescription = unitDescription2.getText().toString();
									categoryUnit = databaseHelper.getCategoryKey(titleTextValue);
									fromUnitKey = databaseHelper.getKey(fromUnitsDescription);
									toUnitKey = databaseHelper.getKey(toUnitsDescription);

									availableFavorite = databaseHelper.checkFavoritePresent(categoryUnit, fromUnitKey, toUnitKey);
									if (availableFavorite == true) {
										Toast.makeText(ConverterActivity.this, AppConstants.popupToast, Toast.LENGTH_SHORT).show();
									} else {
										final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConverterActivity.this);
										alertDialog.setTitle(AppConstants.favPopupTitle);
										alertDialog.setMessage(AppConstants.favPopupMsg);

										alertDialog.setPositiveButton(AppConstants.favPopupYes, new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int id) {
												databaseHelper.insertFavoriteDetails(categoryUnit, fromUnitKey, toUnitKey);
											}

										});

										alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int id) {
												dialog.cancel();
											}
										});
										AlertDialog alert11 = alertDialog.create();
										alert11.show();
									}

								}
								if (arg0.getTitle().toString().equalsIgnoreCase(AppConstants.favPopupMenu3)) {
									value1.setText(copyValue2);
									calculatedResult = computeResult();
									value2.setText(trimOutputValue(Double.toString(calculatedResult)));
								}
								return true;
							}
						});
						pop.show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		});
		
		// display favorites pop up for to layout
		toLayout.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				try {
					if (unitsGrid.getVisibility() == View.GONE || unitsGrid.getVisibility() == View.INVISIBLE) {
						PopupMenu pop = new PopupMenu(ConverterActivity.this, arg0);
						pop.getMenuInflater().inflate(R.menu.to_pop_menu, pop.getMenu());
						pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem arg0) {
								if (arg0.getTitle().toString().equalsIgnoreCase(AppConstants.favPopupMenu1)) {
									String titleTextValue = titleText.getText().toString();
									String fromUnitsDescription = unitDescription1.getText().toString();
									String toUnitsDescription = unitDescription2.getText().toString();
									categoryUnit = databaseHelper.getCategoryKey(titleTextValue);

									fromUnitKey = databaseHelper.getKey(fromUnitsDescription);

									toUnitKey = databaseHelper.getKey(toUnitsDescription);

									availableFavorite = databaseHelper.checkFavoritePresent(categoryUnit, fromUnitKey, toUnitKey);
									if (availableFavorite == true) {
										Toast.makeText(ConverterActivity.this, "Already Available in favorites", Toast.LENGTH_SHORT).show();
									} else {
										final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConverterActivity.this);
										alertDialog.setTitle(AppConstants.favPopupTitle);
										alertDialog.setMessage(AppConstants.favPopupMsg);

										alertDialog.setPositiveButton(AppConstants.favPopupYes, new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int id) {

												databaseHelper.insertFavoriteDetails(categoryUnit, fromUnitKey, toUnitKey);
											}

										});

										alertDialog.setNegativeButton(AppConstants.favPopupNo, new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int id) {
												dialog.cancel();
											}
										});
										final AlertDialog alert11 = alertDialog.create();
										alert11.show();
									}
								}
								if (arg0.getTitle().toString().equalsIgnoreCase(AppConstants.favPopupMenu2)) {
									copyValue2 = value2.getText().toString();
								}
								return true;
							}
						});
						pop.show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;

			}
		});

		// operation to implement plus minus operation
		plusminusButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					Utilities.buttonClickSound(ConverterActivity.this);
					value1Text = value1.getText().toString();
					if (!value1.getText().toString().contentEquals("0")) {
						String valueTemp = value1.getText().toString();

						if (valueTemp.contains("E+")) {
							String[] splitArray = valueTemp.split("\\+");
							if (!splitArray[1].equalsIgnoreCase("00")) {

								value1.setText(splitArray[0] + "-" + splitArray[1]);
							}
						} else if (valueTemp.contains("E-")) {

							String[] splitArray = valueTemp.split("E");

							if (splitArray[1].charAt(2) != '0')
							{
								value1.setText(splitArray[0] + "E+" + splitArray[1].charAt(1) + splitArray[1].charAt(2));
							}
						} else {
							double value1TextDouble = (Double.parseDouble(value1Text) * -1);
							value1.setText(new BigDecimal(Double.toString(value1TextDouble)).stripTrailingZeros().toPlainString());
						}
					} else {
						value1.setText("0");
						value2.setText("0");
					}
					calculatedResult = computeResult();
					value2.setText(trimOutputValue(Double.toString(calculatedResult)));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		});
        
		//implement exponential operation
		exponentialButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					Utilities.buttonClickSound(ConverterActivity.this);
					if (Double.parseDouble(value1.getText().toString()) != 0.0) {
						if (!value1.getText().toString().contains("E")) {
							epowerPressed = true;
							String evalue = value1.getText().toString();
							if (evalue.charAt(evalue.length() - 1) == '.') {
								value1.setText(evalue.replace(".", "E+00"));
							} else {
								value1.setText(value1.getText().toString() + "E+00");
							}
						}
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		});
        
		//implement delete functionality
		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					Utilities.buttonClickSound(ConverterActivity.this);
					valueString = new StringBuilder(value1.getText());
					if (!value1.getText().subSequence(0, 1).toString().contains("-")) {
						if (value1.getText().toString().contains("E+")) {
							String deleteString = value1.getText().toString();
							String[] array = deleteString.split("\\+");
							String afterplus = array[1];
							char firstOperand = afterplus.charAt(0);
							char secondOperand = afterplus.charAt(1);
							if (firstOperand == '0' && secondOperand == '0') {
								value1.setText(array[0].substring(0, array[0].length() - 1));
								calculatedResult = computeResult();
								value2.setText(trimOutputValue(Double.toString(calculatedResult)));
							} else if (firstOperand != '0') {
								value1.setText(array[0] + "+0" + firstOperand);
								calculatedResult = computeResult();
								value2.setText(trimOutputValue(Double.toString(calculatedResult)));
							} else {
								value1.setText(array[0] + "+00");
								calculatedResult = computeResult();
								value2.setText(trimOutputValue(Double.toString(calculatedResult)));
							}

						} else if (value1.getText().toString().contains("E-")) {
							String deleteString = value1.getText().toString();
							String[] array = deleteString.split("E");
							String afterplus = array[1];
							char firstOperand = afterplus.charAt(1);
							char secondOperand = afterplus.charAt(2);
							if (firstOperand == '0' && secondOperand == '0') {
								value1.setText(array[0].substring(0, array[0].length() - 1));
								calculatedResult = computeResult();
								value2.setText(trimOutputValue(Double.toString(calculatedResult)));
							} else if (firstOperand != '0') {
								value1.setText(array[0] + "E-0" + firstOperand);
								calculatedResult = computeResult();
								value2.setText(trimOutputValue(Double.toString(calculatedResult)));
							} else {
								value1.setText(array[0] + "E+00");
								calculatedResult = computeResult();
								value2.setText(trimOutputValue(Double.toString(calculatedResult)));
							}

						} else {
							if (value1.length() > 1) {
								valueString.deleteCharAt(valueString.length() - 1);
								value1.setText(valueString);
								calculatedResult = computeResult();
								value2.setText(trimOutputValue(Double.toString(calculatedResult)));
							} else if (value1.length() == 1) {
								value1.setText("0");
								value2.setText("0");

							}
						}

					} else if ((value1.getText().subSequence(0, 1).toString().contains("-")) && value1.getText().toString().contains("E")) {
						if (value1.getText().toString().contains("E+")) {
							String deleteString = value1.getText().toString();
							String[] array = deleteString.split("\\+");
							String afterplus = array[1];
							char firstOperand = afterplus.charAt(0);
							char secondOperand = afterplus.charAt(1);
							if (firstOperand == '0' && secondOperand == '0') {
								value1.setText(array[0].substring(0, array[0].length() - 1));
								calculatedResult = computeResult();
								value2.setText(trimOutputValue(Double.toString(calculatedResult)));
							} else if (firstOperand != '0') {
								value1.setText(array[0] + "+0" + firstOperand);
								calculatedResult = computeResult();
								value2.setText(trimOutputValue(Double.toString(calculatedResult)));
							} else {
								value1.setText(array[0] + "+00");
								calculatedResult = computeResult();
								value2.setText(trimOutputValue(Double.toString(calculatedResult)));
							}

						} else if (value1.getText().toString().contains("E-")) {
							String deleteString = value1.getText().toString();
							String[] array = deleteString.split("E");
							String afterplus = array[1];
							char firstOperand = afterplus.charAt(1);
							char secondOperand = afterplus.charAt(2);
							if (firstOperand == '0' && secondOperand == '0') {
								value1.setText(array[0].substring(0, array[0].length() - 1));
								calculatedResult = computeResult();
								value2.setText(trimOutputValue(Double.toString(calculatedResult)));
							} else if (firstOperand != '0') {
								value1.setText(array[0] + "E-0" + firstOperand);
								calculatedResult = computeResult();
								value2.setText(trimOutputValue(Double.toString(calculatedResult)));
							} else {
								value1.setText(array[0] + "E+00");
								calculatedResult = computeResult();
								value2.setText(trimOutputValue(Double.toString(calculatedResult)));
							}

						} else {
							if (value1.length() > 1) {
								valueString.deleteCharAt(valueString.length() - 1);
								value1.setText(valueString);
								calculatedResult = computeResult();
								value2.setText(trimOutputValue(Double.toString(calculatedResult)));
							} else if (value1.length() == 1) {
								value1.setText("0");
								value2.setText("0");

							}
						}

					} else if (value1.getText().subSequence(0, 1).toString().contains("-")) {
						if (value1.length() > 2) {
							valueString.deleteCharAt(valueString.length() - 1);
							value1.setText(valueString);
							calculatedResult = computeResult();
							value2.setText(trimOutputValue(Double.toString(calculatedResult)));
						} else if (value1.length() == 2) {
							value1.setText("0");
							value2.setText("0");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
        
		//implement clear functionality
		clearButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Utilities.buttonClickSound(ConverterActivity.this);
					valueString = new StringBuilder(value1.getText());
					if (valueString.length() > 0) {
						valueString.delete(0, valueString.length());
						value1.setText("0");
						value2.setText("0");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//implement swap functionality
		swapButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Utilities.buttonClickSound(ConverterActivity.this);
					String firstUnit = units1.getText().toString();
					String secondUnit = units2.getText().toString();
					units2.setText(firstUnit);
					units1.setText(secondUnit);

					String firstUnitDescription = unitDescription1.getText().toString();
					String secondUnitDescription = unitDescription2.getText().toString();
					unitDescription2.setText(firstUnitDescription);
					unitDescription1.setText(secondUnitDescription);

					fromUnit = unitDescription1.getText().toString();
					toUnit = unitDescription2.getText().toString();
					if (!selectedCategoryName.equalsIgnoreCase("Currency Conversion")) {
						listOfOperations = databaseHelper.getListOfOperations(fromUnit, toUnit, titleText.getText().toString());
						if (listOfOperations.contains(";")) {
							operations = listOfOperations.split(";");
						} else {
							operations = new String[] { listOfOperations };
						}
					} else {
						exchangeRate = databaseHelper.getExchangeRateforcurrency(units1.getText().toString(), units2.getText().toString());
					}
					calculatedResult = computeResult();
					value2.setText(trimOutputValue(Double.toString(calculatedResult)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
        
		//load the default content
		displayInitialContent();
        
		//from layout functionality
		fromLayout.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				try {
					RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) unitsGrid.getLayoutParams();
					params.addRule(RelativeLayout.BELOW, R.id.units_layout);
					unitsGrid.setLayoutParams(params);
					unitSearchText.setText("");
					if (numbersLayout.getVisibility() == View.VISIBLE) {
						navigationButton.setBackgroundDrawable(getResources().getDrawable(drawable.ic_menu_search));
						backtohome = false;
						titleText.setText(AppConstants.changeUnitText);
						downArrowButton.setVisibility(View.GONE);
						numbersLayout.setVisibility(View.GONE);
						unitsGrid.setVisibility(View.VISIBLE);
						Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate);
						unitsGrid.startAnimation(hyperspaceJumpAnimation);
					} else if (unitsGrid.getVisibility() == View.VISIBLE && fromSelected) {
						backtohome = true;
						Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate);
						numbersLayout.startAnimation(hyperspaceJumpAnimation);
						numbersLayout.setVisibility(View.VISIBLE);
						numbersLayout.startAnimation(hyperspaceJumpAnimation);
						unitsGrid.setVisibility(View.GONE);
						titleText.setText(selectedCategoryName);
						navigationButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_button));
						downArrowButton.setVisibility(View.VISIBLE);
					} else {
						navigationButton.setBackgroundDrawable(getResources().getDrawable(drawable.ic_menu_search));
						backtohome = false;
						titleText.setText("Change Unit");
						downArrowButton.setVisibility(View.INVISIBLE);
						numbersLayout.setVisibility(View.GONE);
						unitsGrid.setVisibility(View.VISIBLE);
						Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate);
						unitsGrid.startAnimation(hyperspaceJumpAnimation);
					}
					if (!selectedCategoryName.equalsIgnoreCase("Currency Conversion")) {
						getOperationValuesForAdapter = new ArrayList<String>();
						getOperationValuesForAdapter = getListofOperationsForAdapter(unitsList, value1.getText().toString(), selectedCategoryName, unitDescription1.getText()
								.toString());
						unitsGrid.setAdapter(new UnitGridAdapter(context, unitsList, getOperationValuesForAdapter));
					} else {
						getOperationValuesForAdapter = new ArrayList<String>();
						getOperationValuesForAdapter = getListofOperationsForAdapter(unitsList, value1.getText().toString(), selectedCategoryName, units1.getText().toString());
						unitsGrid.setAdapter(new UnitGridAdapter(context, unitsList, getOperationValuesForAdapter));
					}
					fromSelected = true;
					toSelected = false;
				} catch (NotFoundException e) {
					e.printStackTrace();
				}
			}
		});
        
		//implement to layout functionality
		toLayout.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				try {
					RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) unitsGrid.getLayoutParams();
					params.addRule(RelativeLayout.BELOW, R.id.units_layout);
					unitsGrid.setLayoutParams(params);
					unitSearchText.setText("");

					if (numbersLayout.getVisibility() == View.VISIBLE) {
						navigationButton.setBackgroundDrawable(getResources().getDrawable(drawable.ic_menu_search));
						backtohome = false;
						titleText.setText(AppConstants.changeUnitText);
						downArrowButton.setVisibility(View.GONE);
						numbersLayout.setVisibility(View.GONE);
						unitsLayout.setVisibility(View.VISIBLE);
						unitsGrid.setVisibility(View.VISIBLE);
						Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate);
						unitsGrid.startAnimation(hyperspaceJumpAnimation);
					} else if (unitsGrid.getVisibility() == View.VISIBLE && toSelected) {
						backtohome = true;
						numbersLayout.setVisibility(View.VISIBLE);
						Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate);
						numbersLayout.startAnimation(hyperspaceJumpAnimation);
						unitsGrid.setVisibility(View.GONE);
						titleText.setText(selectedCategoryName);
						navigationButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_button));
						downArrowButton.setVisibility(View.VISIBLE);
					} else {
						backtohome = false;
						navigationButton.setBackgroundDrawable(getResources().getDrawable(drawable.ic_menu_search));
						titleText.setText("Change Unit");
						downArrowButton.setVisibility(View.INVISIBLE);
						numbersLayout.setVisibility(View.GONE);
						unitsGrid.setVisibility(View.VISIBLE);
						Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate);
						unitsGrid.startAnimation(hyperspaceJumpAnimation);
					}

					if (!selectedCategoryName.equalsIgnoreCase(AppConstants.currencyConversiontext)) {
						getOperationValuesForAdapter = new ArrayList<String>();
						getOperationValuesForAdapter = getListofOperationsForAdapter(unitsList, value1.getText().toString(), selectedCategoryName, unitDescription1.getText()
								.toString());
						unitsGrid.setAdapter(new UnitGridAdapter(context, unitsList, getOperationValuesForAdapter));
						
					} else {
						getOperationValuesForAdapter = new ArrayList<String>();
						getOperationValuesForAdapter = getListofOperationsForAdapter(unitsList, value1.getText().toString(), selectedCategoryName, units1.getText().toString());
						unitsGrid.setAdapter(new UnitGridAdapter(context, unitsList, getOperationValuesForAdapter));
					}
					toSelected = true;
					fromSelected = false;
				} catch (NotFoundException e) {
					e.printStackTrace();
				}

			}
		});

		// back to home activity 
		navigationButton.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				try {
					if (backtohome) {
						RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) unitsGrid.getLayoutParams();
						params.addRule(RelativeLayout.BELOW, R.id.units_layout);
						unitsGrid.setLayoutParams(params);
						finish();
					} else {
						unitsLayout.setVisibility(View.GONE);
						changeUnitLayout.setVisibility(View.VISIBLE);
						RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) unitsGrid.getLayoutParams();
						params.addRule(RelativeLayout.BELOW, R.id.searchLayout);
						unitsGrid.setLayoutParams(params);
						navigationButton.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
					}
				} catch (NotFoundException e) {
					e.printStackTrace();
				}

			}
		});

		// implementing unitsSerachFunctionality
		unitSearchText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

				try {
					String searchString = unitSearchText.getText().toString();
					int textLength = searchString.length();
					unitssearchList.clear();
					unitsSearchListOperation.clear();
					String text;

					for (int i = 0; i < unitsList.size(); i++) {
						text = unitsList.get(i).get(0);

						if (textLength <= text.length()) {
							if (searchString.equalsIgnoreCase(text.substring(0, textLength)))
								unitssearchList.add(unitsList.get(i));
							unitsSearchListOperation.add(getOperationValuesForAdapter.get(i));
						}
					}
					unitsGrid.setAdapter(new UnitGridAdapter(context, unitssearchList, unitsSearchListOperation));
					searchLayoutClicked = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//implement unit cancel text functionality
		unitsCancelText.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				try {
					navigationButton.setBackgroundDrawable(getResources().getDrawable(drawable.ic_menu_search));
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(unitsCancelText.getWindowToken(), 0);
					unitSearchText.setText("");
					changeUnitLayout.setVisibility(View.GONE);
					unitsLayout.setVisibility(View.VISIBLE);
					unitsGrid.setVisibility(View.VISIBLE);
					Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate);
					unitsGrid.startAnimation(hyperspaceJumpAnimation);
					RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) unitsGrid.getLayoutParams();
					params.addRule(RelativeLayout.BELOW, R.id.units_layout);
					unitsGrid.setLayoutParams(params);
					unitsGrid.setAdapter(new UnitGridAdapter(context, unitsList, unitsSearchListOperation));
				} catch (NotFoundException e) {
					e.printStackTrace();
				}

			}
		});
        
		//implementing units grid functionality
		unitsGrid.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
					for (int j = 0; j < unitsList.size(); j++) {
						if (unitsList.get(j).get(0).equalsIgnoreCase(view.getTag().toString())) {
							if (fromSelected) {
								units1.setText(unitsList.get(j).get(1));
								unitDescription1.setText(unitsList.get(j).get(0));
							} else {
								units2.setText(unitsList.get(j).get(1));
								unitDescription2.setText(unitsList.get(j).get(0));
							}

							changeUnitLayout.setVisibility(View.GONE);
							unitsGrid.setVisibility(View.GONE);
							unitsLayout.setVisibility(View.VISIBLE);
							numbersLayout.setVisibility(View.VISIBLE);
							Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate);
							numbersLayout.startAnimation(hyperspaceJumpAnimation);
							navigationButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_button));
							titleText.setText(selectedCategoryName);
							downArrowButton.setVisibility(View.VISIBLE);
							backtohome = true;

							fromUnit = unitDescription1.getText().toString();
							toUnit = unitDescription2.getText().toString();
							if (!selectedCategoryName.equalsIgnoreCase("Currency Conversion")) {
								listOfOperations = databaseHelper.getListOfOperations(fromUnit, toUnit, selectedCategoryName);
								if (listOfOperations.contains(";")) {
									operations = listOfOperations.split(";");
								} else {
									operations = new String[] { listOfOperations };
								}
							} else {
								exchangeRate = databaseHelper.getExchangeRateforcurrency(units1.getText().toString(), units2.getText().toString());
							}
							calculatedResult = computeResult();
							value2.setText(trimOutputValue(Double.toString(calculatedResult)));
						}
					}

				} catch (NotFoundException e) {
					e.printStackTrace();
				}
			}

		});

		// Set the listener for all the buttons
		mClickListener = new ButtonClickListener();
		int idList[] = { R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
				R.id.button_dot };
		for (int id : idList) {
			View v = findViewById(id);
			v.setOnClickListener(mClickListener);
		}

	}

	/**
	 * Gets the listof operations for adapter.
	 *
	 * @param unitsList the units list
	 * @param operand the operand
	 * @param charSequence the char sequence
	 * @param unitDescription1 the unit description1
	 * @return the listof operations for adapter
	 */
	public ArrayList<String> getListofOperationsForAdapter(ArrayList<ArrayList<String>> unitsList, String operand, String charSequence, String unitDescription1) {
		ArrayList<ArrayList<String>> unitsListforAdapter;
		String categoryName;
		String fromUnitValue;
		String firstOperand;
		unitsListforAdapter = unitsList;
		categoryName = charSequence;
		fromUnitValue = unitDescription1;
		firstOperand = operand;
		if (!categoryName.equalsIgnoreCase(AppConstants.currencyConversiontext)) {
			for (int i = 0; i < unitsListforAdapter.size(); i++) {
				try {
					String operations = databaseHelper.getListOfOperations(fromUnitValue, unitsListforAdapter.get(i).get(0).toString(), categoryName);
					String operations1[] = null;
					if (operations.contains(";")) {
						operations1 = operations.split(";");
					} else {
						operations1 = new String[] { operations };
					}
					Double valToCalculate = Double.parseDouble(firstOperand);
					Double calculatedValue = new Double(0);
					for (int j = 0; j < operations1.length; j++) {
						String singleOperation[] = operations1[j].split(" ");
						if (singleOperation[0].equalsIgnoreCase("mul")) {
							calculatedValue = valToCalculate * Double.parseDouble(singleOperation[1]);
							valToCalculate = calculatedValue;

						} else {
							calculatedValue = valToCalculate / Double.parseDouble(singleOperation[1]);

							valToCalculate = calculatedValue;

						}
					}
					String finalAnswer = Double.toString(calculatedValue);
					getOperationValuesForAdapter.add(trimOutputValue(finalAnswer) + " " + unitsListforAdapter.get(i).get(1));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			for (int i = 0; i < unitsListforAdapter.size(); i++) {
				double exchangeRate = databaseHelper.getExchangeRateforcurrency(fromUnitValue, unitsListforAdapter.get(i).get(1).toString());
				Double calculatedValueforcurrency = new Double(0);
				calculatedValueforcurrency = Double.parseDouble(firstOperand) * exchangeRate;
				getOperationValuesForAdapter.add(trimOutputValue(Double.toString(calculatedValueforcurrency)) + " " + unitsListforAdapter.get(i).get(1));
			}
		}
		return getOperationValuesForAdapter;
	}

	/**
	 * Display initial content.
	 */
	public void displayInitialContent() {

		try {
			unitsList = databaseHelper.getUnits(selectedCategoryName);
			unitssearchList = new ArrayList<ArrayList<String>>();
			unitsSearchListOperation = new ArrayList<String>();

			value1.setText("0");
			value2.setText("0");
			if (AppConstants.fromfavoriteScreen == 0 || AppConstants.fromfavoriteScreen == 1) {
				units1.setText(unitsList.get(0).get(1));
				units2.setText(unitsList.get(unitsList.size() - 1).get(1));
			} else {
				units1.setText(fromUnitDescfromFav);
				units2.setText(tounitDescfromFav);
			}
			if (AppConstants.fromfavoriteScreen == 0 || AppConstants.fromfavoriteScreen == 1) {
				unitDescription1.setText(unitsList.get(0).get(0));
				unitDescription2.setText(unitsList.get(unitsList.size() - 1).get(0));
			} else {
				unitDescription1.setText(fromUnitfromfav);
				unitDescription2.setText(toUnitfromFav);
			}

			fromUnit = unitDescription1.getText().toString();
			toUnit = unitDescription2.getText().toString();
			if (!selectedCategoryName.equalsIgnoreCase("Currency Conversion")) {
				listOfOperations = databaseHelper.getListOfOperations(fromUnit, toUnit, selectedCategoryName);
				if (listOfOperations.contains(";")) {
					operations = listOfOperations.split(";");
				} else {
					operations = new String[] { listOfOperations };
				}

			} else {
				exchangeRate = databaseHelper.getExchangeRateforcurrency(units1.getText().toString(), units2.getText().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initiate pop up window.
	 *
	 * @param view the view
	 */
	protected void initiatePopUpWindow(View view) {
		try {
			PopupMenu fromPop = new PopupMenu(ConverterActivity.this, view);
			fromPop.getMenuInflater().inflate(R.menu.pop_menu, fromPop.getMenu());
			fromPop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem arg0) {
					Toast.makeText(getApplicationContext(), arg0.getTitle(), Toast.LENGTH_SHORT).show();
					return true;
				}
			});
			fromPop.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Compute result.
	 *
	 * @return the double
	 */
	protected double computeResult() {
		Double calculatedValue = new Double(0);
		try {
			Double valToCalculate = Double.parseDouble(value1.getText().toString());

			if (!selectedCategoryName.equalsIgnoreCase("Currency Conversion")) {
				for (int opLength = 0; opLength < operations.length; opLength++) {
					String singleOperation[] = operations[opLength].split(" ");
					if (singleOperation[0].equalsIgnoreCase(AppConstants.mulFunc)) {
						calculatedValue = valToCalculate * Double.parseDouble(singleOperation[1]);
						valToCalculate = calculatedValue;

					} else if (singleOperation[0].equalsIgnoreCase(AppConstants.divFunc)) {
						calculatedValue = valToCalculate / Double.parseDouble(singleOperation[1]);
						valToCalculate = calculatedValue;

					} else if (singleOperation[0].equalsIgnoreCase(AppConstants.addFunc)) {
						calculatedValue = valToCalculate + Double.parseDouble(singleOperation[1]);
						valToCalculate = calculatedValue;
					} else if (singleOperation[0].equalsIgnoreCase(AppConstants.subFunc)) {
						calculatedValue = valToCalculate - Double.parseDouble(singleOperation[1]);
						valToCalculate = calculatedValue;
					} else {
						valToCalculate = calculatedValue;
					}
				}
			} else {
				if (valToCalculate != 0.0 && exchangeRate != 0.0) {
					calculatedValue = valToCalculate * exchangeRate;
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return calculatedValue;
	}

	/**
	 * The listener interface for receiving buttonClick events.
	 * The class that is interested in processing a buttonClick
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addButtonClickListener<code> method. When
	 * the buttonClick event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ButtonClickEvent
	 */
	private class ButtonClickListener implements OnClickListener {

		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			try {
				Utilities.buttonClickSound(ConverterActivity.this);
				String buttonPressed = ((Button) v).getText().toString();
				String inputText = value1.getText().toString();

				if (DIGITS.contains(buttonPressed)) {
					if (buttonPressed.equals(".")) {
						if (inputText.length() == 1) {
							value1.setText(inputText + buttonPressed);
						}
						else if (value1.getText().toString().contains(".") || value1.getText().toString().contains("E")) {

						} else {
							value1.append(buttonPressed);
						}

					}

					else if (value1.getText().toString().contains("E+")) {
						String temp = value1.getText().toString();
						String[] econtent = temp.split("\\+");
						String afterplus = econtent[1];
						char firstOperand = afterplus.charAt(0);
						char secondOperand = afterplus.charAt(1);
						if (firstOperand == '0' && secondOperand == '0') {
							value1.setText(econtent[0] + "+0" + buttonPressed);
						} else if (firstOperand == '0') {
							value1.setText(econtent[0] + "+" + secondOperand + buttonPressed);
						} else {
							Toast.makeText(ConverterActivity.this,AppConstants.numberLimitReached, Toast.LENGTH_SHORT).show();
						}

					} else if (value1.getText().toString().contains("E-")) {
						String temp = value1.getText().toString();
						String[] econtent = temp.split("E");
						String afterplus = econtent[1];
						char firstOperand = afterplus.charAt(1);
						char secondOperand = afterplus.charAt(2);
						if (firstOperand == '0' && secondOperand == '0') {
							value1.setText(econtent[0] + "E-0" + buttonPressed);
						} else if (firstOperand == '0') {
							value1.setText(econtent[0] + "E-" + secondOperand + buttonPressed);
						} else {
							Toast.makeText(ConverterActivity.this,AppConstants.numberLimitReached, Toast.LENGTH_SHORT).show();
						}

					} else {
						// condition to limit numbers to appear in input.
						if ((inputText.length() == 7) && (!inputText.contains(".")) && (!inputText.contains("-"))) {
							Toast.makeText(ConverterActivity.this,AppConstants.numberLimitReached, Toast.LENGTH_SHORT)// 7
									.show();
						} else if ((inputText.length() == 8) && ((inputText.contains("-")) && (!inputText.contains(".")))) {
							Toast.makeText(ConverterActivity.this, AppConstants.numberLimitReached, Toast.LENGTH_SHORT)// 8
									// with
									// -
									.show();
						} else if ((inputText.length() == 8) && ((!inputText.contains("-")) && (inputText.contains(".")))) {
							Toast.makeText(ConverterActivity.this, AppConstants.numberLimitReached, Toast.LENGTH_SHORT)// 8
									// with
									// .
									.show();
						}

						else if ((inputText.length() == 9) && (inputText.contains(".")) && (inputText.contains("-"))) {
							Toast.makeText(ConverterActivity.this, AppConstants.numberLimitReached, Toast.LENGTH_SHORT)// 9
									.show();
						}

						// to avoid after clearing the content,0 is taken as number
						else if (inputText.subSequence(0, 1).equals("0"))
						{
							if (inputText.length() >= 2) {
								if (inputText.subSequence(0, 2).equals("0.")) {
									valueString = new StringBuilder(value1.getText());
									value1.setText(valueString);
									value1.append(buttonPressed);
								}
							} else {
								valueString = new StringBuilder(value1.getText());
								valueString.deleteCharAt(0);
								value1.setText(valueString);
								value1.append(buttonPressed);
							}

						}

						else {
							value1.append(buttonPressed);
						}

					}
				}

				calculatedResult = computeResult();
				value2.setText(trimOutputValue(Double.toString(calculatedResult)));
			} catch (Exception e) {
				e.printStackTrace();
			} 

		}
	}

	/**
	 * Trim output value.
	 *
	 * @param calculatedResult2 the calculated result2
	 * @return the string
	 */
	public String trimOutputValue(String calculatedResult2) {
		String returnValue = "";
		try {
			BigDecimal bg;

			String outputValue;
			MathContext mc = new MathContext(7); // 3 precision
			bg = new BigDecimal(calculatedResult2, mc);// 1234E4
			outputValue = bg.toString();

			if (outputValue.contains("E")) {
				String[] econtentTemp = outputValue.split("E");

				int indexofdot = econtentTemp[0].indexOf('.');

				String substringAfterdot = outputValue.substring(indexofdot + 1, econtentTemp[0].length());
				if (!substringAfterdot.equals("") && substringAfterdot != null) {
					String[] digitsfrom1to9 = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
					boolean contains = false;
					for (String item : digitsfrom1to9) {
						if (substringAfterdot.contains(item)) {
							contains = true;
							break; // No need to look further.
						}
					}

					returnValue = outputValue;
					if (!contains) {
						String trimOutput = outputValue.substring(0, indexofdot);
						returnValue = trimOutput + "E" + econtentTemp[1];
					}
				}
			} else {
				int indexofdot = outputValue.indexOf('.');


				String substringAfterdot = outputValue.substring(indexofdot + 1, outputValue.length());
				if (!substringAfterdot.equals("") && substringAfterdot != null) {
					String[] digitsfrom1to9 = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
					boolean contains = false;
					for (String item : digitsfrom1to9) {
						if (substringAfterdot.contains(item)) {
							contains = true;
							break; // No need to look further.
						}
					}

					returnValue = outputValue;
					if (!contains) {
						String trimOutput = outputValue.substring(0, indexofdot);
						returnValue = trimOutput;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnValue;
	}

}
