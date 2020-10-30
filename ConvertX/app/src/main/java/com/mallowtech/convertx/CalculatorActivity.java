package com.mallowtech.convertx;

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mallowtech.helper.AppConstants;
import com.mallowtech.helper.Utilities;

/**
 * The Class CalculatorActivity.
 */
public class CalculatorActivity extends Activity {
	
	/** The m click listener. */
	ButtonClickListener mClickListener;
	
	/** The second textview. */
	TextView firstTextView, secondTextview;
	
	/** The df. */
	DecimalFormat df = new DecimalFormat(AppConstants.defaultnumberFormat1);
	
	/** The recall clear value. */
	int recallClearValue = 0;
	
	/** The operator click count. */
	boolean previousClick = false, equalClick = true, operatorClickCount = true;
	
	/** The button pressed. */
	String buttonPressed;
	
	/** The digit. */
	int digit;
	
	/** The temp. */
	String temp;
	// Stored current operand values
	/** The input string. */
	String inputString = "";
	// Operands
	/** The operand1. */
	String operand1 = "";
	
	/** The operand2. */
	String operand2 = "";
	// Stored the current operator symbol
	/** The operator symbol. */
	char operatorSymbol;
	// Set the Boolean value for call the memory value or not
	/** The memory recall. */
	boolean memoryRecall = false;
	// Stored the memory value
	/** The memory value. */
	String memoryValue = "0";
	
	/** The calculate. */
	Calculator calculate = new Calculator();
	
	/** The home button. */
	Button mplusbutton, mminusbutton, mrcbutton, plusminusbutton, modulusbutton, squarerootbutton, dividebutton, multiplybutton, minusbutton, plusbutton, cancelbutton, dotbutton, equaltobutton,
			homeButton;
	
	/** The deletebutton. */
	ImageButton deletebutton;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_calculator);

		// Initialization
		deletebutton = (ImageButton) findViewById(R.id.calculatorbutton_delete);
		dotbutton = (Button) findViewById(R.id.calculatordot_button);
		cancelbutton = (Button) findViewById(R.id.calculatorcancel_button);
		equaltobutton = (Button) findViewById(R.id.equals_button);
		plusbutton = (Button) findViewById(R.id.plus_button);
		minusbutton = (Button) findViewById(R.id.minus_button);
		multiplybutton = (Button) findViewById(R.id.multiply_button);
		dividebutton = (Button) findViewById(R.id.divide_button);
		modulusbutton = (Button) findViewById(R.id.modules_button);
		squarerootbutton = (Button) findViewById(R.id.sqrt_button);
		mplusbutton = (Button) findViewById(R.id.mplus_button);
		mminusbutton = (Button) findViewById(R.id.mminus_button);
		mrcbutton = (Button) findViewById(R.id.mrc_button);
		plusminusbutton = (Button) findViewById(R.id.calcplusminus_button);
		firstTextView = (TextView) findViewById(R.id.displayprevious_textView);
		secondTextview = (TextView) findViewById(R.id.displayAnswer_textView);
		homeButton = (Button) findViewById(R.id.navigation_button);

		// back to home page
		homeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		// Set the listener for all the buttons
		mClickListener = new ButtonClickListener();

		int idList[] = { R.id.calcualtor9_button, R.id.calculator8_button, R.id.calculator7_button, R.id.calculator6_button, R.id.calcultor5_button, R.id.calculator4_button, R.id.calculator3_button,
				R.id.calculator2_button, R.id.calculator1_button, R.id.calculator0_button, };// R.id.button_clear,R.id.button_delete,R.id.button_exp

		for (int id : idList) {
			View v = findViewById(id);
			v.setOnClickListener(mClickListener);
		}

		dotbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					temp = ((Button) arg0).getText().toString();
					// check button click sound
					Utilities.buttonClickSound(CalculatorActivity.this);
					// check the input string is having . value or not
					if (secondTextview.getText().toString().contains("Out of Scope") || secondTextview.getText().toString().contains("Error")) {
						inputString = "";
					}
					if (inputString.toString().length() - getInputStringLengthCount() < 20) {
						if (inputString.contains(".")) {
						} else {
							if (inputString.equalsIgnoreCase("")) {
								inputString = inputString + "0.";// inputString+"0.";

							} else {
								inputString = inputString + ".";
							}
						}
					}

					// Check for operand1 exist, then add display the
					// displayLabel value
					if (!(operand1.equalsIgnoreCase("") || operatorSymbol == '0')) {
						firstTextView.setText(operand1 + operatorSymbol);
					}
					// set the inputString to displayAnswer value
					secondTextview.setText(inputString.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		cancelbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// check button click sound
				Utilities.buttonClickSound(CalculatorActivity.this);
				temp = ((Button) v).getText().toString();
				checkMemoryRecall();
				secondTextview.setText("0");
				inputString = "";
				operand1 = "";
				operand2 = "";
				operatorSymbol = '0';
				firstTextView.setText("");

			}
		});

		// implementing delete functionality
		deletebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					// check button click sound
					Utilities.buttonClickSound(CalculatorActivity.this);
					temp = "delete";
					checkMemoryRecall();
					// Check the display text value,If its not a numeric then
					// set to zero
					if (secondTextview.getText().toString().equalsIgnoreCase("Error") || secondTextview.getText().toString().equalsIgnoreCase("NaN")
							|| secondTextview.getText().toString().equalsIgnoreCase("Out of Scope")) {
						secondTextview.setText("0");
						inputString = "";
					}

					// Check the display label text is not empty or zero
					if (inputString.equalsIgnoreCase("") && operatorSymbol != '0' && !operand1.equalsIgnoreCase("") && !secondTextview.getText().toString().equalsIgnoreCase("0")) {
						operand1 = "";
						operatorSymbol = '0';
						firstTextView.setText("");
					}

					if (!secondTextview.getText().toString().equalsIgnoreCase("0")) {
						// get the display label value length for reduce the
						// last letter
						int endPosition = secondTextview.getText().toString().length();
						inputString = secondTextview.getText().toString();

						if (endPosition != 0) {
							// construct the new string range is 0 to string
							// length - 1
							inputString = inputString.substring(0, endPosition - 1);

						}
						secondTextview.setText(inputString.toString());
					}

					// check the display text is empty then set to zero
					if (secondTextview.getText().toString().equalsIgnoreCase("")) {
						secondTextview.setText("0");
					}
					// check the display text is - then set to zero
					if (secondTextview.getText().toString().equalsIgnoreCase("-")) {
						secondTextview.setText("0");
						inputString = "";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

		equaltobutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				temp = ((Button) v).getText().toString();
				// check button click sound
				Utilities.buttonClickSound(CalculatorActivity.this);
				// checkDotSymbol();
				checkMemoryRecall();
				// Call for calculation
				setOperand();
			}
		});

		plusbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					temp = ((Button) v).getText().toString();
					// check button click sound
					Utilities.buttonClickSound(CalculatorActivity.this);
					checkMemoryRecall();
					if (secondTextview.getText().toString().contains("Out of Scope") || secondTextview.getText().toString().contains("Error")) {
						secondTextview.setText("0");
						firstTextView.setText("");
					} else {
						firstTextView.setText(secondTextview.getText().toString() + "+");
						setOperand();
						// set the current operator
						operatorSymbol = '+';
						secondTextview.setText("0");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		minusbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					temp = ((Button) v).getText().toString();
					// check button click sound
					Utilities.buttonClickSound(CalculatorActivity.this);
					checkMemoryRecall();
					if (secondTextview.getText().toString().contains("Out of Scope") || secondTextview.getText().toString().contains("Error")) {
						secondTextview.setText("0");
						firstTextView.setText("");
					} else {
						firstTextView.setText(secondTextview.getText().toString() + "-");
						setOperand();
						// set the current operator
						operatorSymbol = '-';
						secondTextview.setText("0");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		multiplybutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					temp = ((Button) v).getText().toString();
					// check button click sound
					Utilities.buttonClickSound(CalculatorActivity.this);
					checkMemoryRecall();
					if (secondTextview.getText().toString().contains("Out of Scope") || secondTextview.getText().toString().contains("Error")) {
						secondTextview.setText("0");
						firstTextView.setText("");
					} else {
						firstTextView.setText(secondTextview.getText().toString() + "x");
						setOperand();
						// set the current operator
						operatorSymbol = '*';
						secondTextview.setText("0");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		dividebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					temp = "/";
					// check button click sound
					Utilities.buttonClickSound(CalculatorActivity.this);
					checkMemoryRecall();
					if (secondTextview.getText().toString().contains("Out of Scope") || secondTextview.getText().toString().contains("Error")) {
						secondTextview.setText("0");
						firstTextView.setText("");
					} else {
						firstTextView.setText(secondTextview.getText().toString() + "/");
						// Call for set the operand value
						setOperand();
						// set the current operator
						operatorSymbol = '/';
						secondTextview.setText("0");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		modulusbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					temp = ((Button) v).getText().toString();
					// check button click sound
					Utilities.buttonClickSound(CalculatorActivity.this);
					checkMemoryRecall();
					/*
					 * 1. 100 % 2. 7 * 2 % 3. 50 % 4 + 7
					 */
					// Check first operand is empty or not
					// check for 1 scenario
					if (!(secondTextview.getText().toString().equalsIgnoreCase("Error")) && !(secondTextview.getText().toString().equalsIgnoreCase("Out of Scope"))) {
						if (operand1.equalsIgnoreCase("") || operatorSymbol == '0') {
							operand1 = secondTextview.getText().toString();
							operatorSymbol = '%';
							// call for calculation
							calculate();
							// Get the result and stored in operand one
							operand1 = "";
							// clear the input string value
							inputString = "";
							if (operand2.equalsIgnoreCase("")) {
								firstTextView.setText("");
							}
						} else {
							// Inter change the value for 2 scenario
							operand2 = operand1;
							operand1 = secondTextview.getText().toString();
							// Inter change the operator symbol for 2 scenario
							char tempOperatorSymbol = operatorSymbol;
							operatorSymbol = '%';
							calculate();
							// //Inter change the value for 2 scenario
							operand1 = operand2;
							// Inter change the operator symbol for 2 scenario
							operatorSymbol = tempOperatorSymbol;
							operand2 = "";
							setOperand();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		squarerootbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					temp = "^";
					// check button click sound
					Utilities.buttonClickSound(CalculatorActivity.this);
					checkMemoryRecall();
					
					if (secondTextview.getText().toString().equalsIgnoreCase("Out of Scope") || secondTextview.getText().toString().equalsIgnoreCase("Error")) {
						secondTextview.setText("0");
						firstTextView.setText("");
					}
					// check the display label value is not equal to zero
					if (Double.parseDouble(secondTextview.getText().toString()) != 0.0) {
						// check for 1 scenario
						if (operand1.equalsIgnoreCase("") || operatorSymbol == '0') {
							operand1 = secondTextview.getText().toString();
							operatorSymbol = '^';
							calculate();
							operand1 = "";
							// self.operand1 = self.inputString;
							inputString = "";
							if (operand2.equalsIgnoreCase("")) {
								firstTextView.setText("");
							}

						} else {
							// Inter change the value for 2 scenario
							operand2 = operand1;
							operand1 = secondTextview.getText().toString();
							// Inter change the operator symbol for 2 scenario
							char tempOperatorSymbol = operatorSymbol;
							operatorSymbol = '^';
							calculate();
							// Inter change the value for 2 scenario
							operand1 = operand2;
							// Inter change the operator symbol for 2 scenario
							operatorSymbol = tempOperatorSymbol;
							operand2 = "";
						}

					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		});

		mplusbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					temp = ((Button) v).getText().toString();
					// check button click sound
					Utilities.buttonClickSound(CalculatorActivity.this);
					// Check the memroy recalled or not
					checkMemoryRecall();
					// add the value to memory
					if (secondTextview.getText().toString().equalsIgnoreCase("Out of Scope") || secondTextview.getText().toString().equalsIgnoreCase("Error")) {
						secondTextview.setText("0");
						firstTextView.setText("");
					}
					if (Double.parseDouble(secondTextview.getText().toString()) != 0.0) {
						String checkString;
						try {
							Double checkRange = Double.parseDouble(memoryValue) + Double.parseDouble(secondTextview.getText().toString());
							checkString = checkForErrorOutput(checkRange.toString());
						} catch (NumberFormatException e) {
							checkString = "Out of Scope";
							e.printStackTrace();
						}

						if (checkString.equalsIgnoreCase("Error"))
							memoryValue = "Error";
						else {
							memoryValue = checkString;
						}

						inputString = "";
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		});
		mminusbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					temp = ((Button) v).getText().toString();
					// check button click sound
					Utilities.buttonClickSound(CalculatorActivity.this);
					// Check the memroy recalled or not
					checkMemoryRecall();
					if (secondTextview.getText().toString().equalsIgnoreCase("Out of Scope") || secondTextview.getText().toString().equalsIgnoreCase("Error")) {
						secondTextview.setText("0");
						firstTextView.setText("");
					}
					// add the value to memory
					if (Double.parseDouble(secondTextview.getText().toString()) != 0.0) {
						String checkString;
						try {
							Double checkRange = Double.parseDouble(memoryValue) - Double.parseDouble(secondTextview.getText().toString());
							checkString = checkForErrorOutput(checkRange.toString());
						} catch (NumberFormatException e) {
							checkString = "Out of Scope";
							e.printStackTrace();
						}

						if (checkString.equalsIgnoreCase("Error"))
							memoryValue = "Error";
						else {
							memoryValue = checkString;
						}

						inputString = "";
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		});

		plusminusbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					temp = "+/-";
					// check button click sound
					Utilities.buttonClickSound(CalculatorActivity.this);
					checkMemoryRecall();
					if (secondTextview.getText().toString().equalsIgnoreCase("Out of Scope") || secondTextview.getText().toString().equalsIgnoreCase("Error")) {
						secondTextview.setText("0");
						firstTextView.setText("");
					}
					if (Double.parseDouble(secondTextview.getText().toString()) != 0.0) {
						Double minusSymbol = Double.parseDouble("-1");
						String changeSign = Double.toString(minusSymbol * Double.parseDouble(secondTextview.getText().toString()));
						if (changeSign.contains(".")) {
							String[] temp = changeSign.split("\\.");
							if (temp[1].equalsIgnoreCase("0")) {
								changeSign = temp[0];
							}
						}
						secondTextview.setText(changeSign);
						if (operatorSymbol != '0' && inputString.equals("")) {
							operand1 = secondTextview.getText().toString();
						} else {
							inputString = changeSign;
						}
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		});

		mrcbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				temp = ((Button) v).getText().toString();
				// check button click sound
				Utilities.buttonClickSound(CalculatorActivity.this);
				// Check the memory recall or not, If recalled the erase the
				// memroy value
				if (memoryRecall == false) {
					secondTextview.setText(memoryValue.toString());
					memoryRecall = true;
					inputString = "";
					if (operatorSymbol != '0') {
						// Changed here for set the memory value to inputString
						// when pressed the operator
						inputString = secondTextview.getText().toString();
					}
				} else {
					memoryRecall = false;
					memoryValue = "0";
					secondTextview.setText("0");
				}
			}
		});
	}

	/**
	 * Sets the operand.
	 */
	public void setOperand() {
		if (operand1.equalsIgnoreCase("") || operatorSymbol == '0') {
			operand1 = secondTextview.getText().toString();
			inputString = "";
		} else {
			if (operand2.equalsIgnoreCase("")) {
				operand2 = inputString;
				firstTextView.setText(operand1 + operatorSymbol + operand2);
				inputString = "";
			}
		}
		// check the operand two is not equal to empty, If its not empty call
		// the calculation operation
		if (!operand2.equalsIgnoreCase("")) {
			calculate();
		}
	}

	/**
	 * Calculate.
	 */
	public void calculate() {

		try {
			// Check the operator symbol if symbol %/^ else other
			if (operatorSymbol == '%' || operatorSymbol == '^') {
				inputString = calculate.calculateusingoperands(operand1, operand2, operatorSymbol);
			} else {
				inputString = calculate.calculateusingoperands(operand1, operand2, operatorSymbol);
			}

			// performing calculation operation
			inputString = checkForErrorOutput(inputString);
			secondTextview.setText(inputString.toString());

			// Check the operator and set the operand one value
			if (operatorSymbol != '%' && operatorSymbol != '^') {
				if (secondTextview.getText().toString().equalsIgnoreCase("Error")) {
					operand1 = "0";
				} else {
					operand1 = secondTextview.getText().toString();
				}
				operand2 = "";
				inputString = "";
				// self.displayLabel.text = self.displayAnswer.text;//@"";
			}

			// After performing operation reset the operator symbol
			operatorSymbol = '0';
			// Finally release the calculator object
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Check for error output.
	 *
	 * @param checkString the check string
	 * @return the string
	 */
	public String checkForErrorOutput(String checkString) {
		try {
			if (checkString.contains(".")) {
				String[] splitArray = checkString.split("\\.");
				String wholeString = splitArray[0];
				if (splitArray[1].equalsIgnoreCase("0")) {
					checkString = wholeString;
				}

				if (wholeString.length() > 20) {
					checkString = AppConstants.outOfScope;
				} else if (splitArray.length > 1) {
					String decimalString = splitArray[1];
					if (wholeString.equalsIgnoreCase("0") && decimalString.contains("000000000000000000")) {
						checkString = AppConstants.outOfScope;
					} else if (wholeString.equalsIgnoreCase("-0") && !decimalString.contains("000000000000000000")) {
						checkString = AppConstants.outOfScope;
					}
				}
				if ((checkString.length() - getInputStringLengthCount() > 20)) {
					checkString = checkString.substring(0, (19 + getInputStringLengthCount()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return checkString;
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
				Utilities.buttonClickSound(CalculatorActivity.this);
				buttonPressed = ((Button) v).getText().toString();
				// Get the button tag value
				digit = Integer.parseInt(buttonPressed);
				// Check memory recalled or not
				checkMemoryRecall();
				// Check for error output.If its error then set to empty
				if (secondTextview.getText().toString().equalsIgnoreCase("Error") || secondTextview.getText().toString().equalsIgnoreCase("Out of Scope")) {
					inputString = "";
				}

				// check the input data length
				if (inputString.length() - getInputStringLengthCount() < 20) {
					// check zero for while append the inputSting value
					if (inputString.equalsIgnoreCase("0")) {
						inputString = "";
					}
					inputString = inputString + digit;
					// set the inputString to displayAnswer value
					secondTextview.setText(inputString.toString());
				} else {
					Toast.makeText(CalculatorActivity.this, AppConstants.numberLimitReached, Toast.LENGTH_SHORT).show();
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Check memory recall.
	 */
	public void checkMemoryRecall() {
		// Check the memory recalled
		if (memoryRecall == true) {
			memoryRecall = false;
		}
	}

	/**
	 * Gets the input string length count.
	 *
	 * @return the input string length count
	 */
	public int getInputStringLengthCount() {
		// check input string is having - and . for input length count
		int lengthofminusDot = 0;
		try {
			if (inputString.contains("-") || inputString.contains(".")) {
				lengthofminusDot = 1;
			} else if (inputString.contains("-") && inputString.contains(".")) {
				lengthofminusDot = 2;
			} else {
				lengthofminusDot = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lengthofminusDot;
	}

}
