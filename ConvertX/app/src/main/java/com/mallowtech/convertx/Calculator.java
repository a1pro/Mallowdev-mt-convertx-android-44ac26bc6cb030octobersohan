package com.mallowtech.convertx;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.mallowtech.helper.AppConstants;

/**
 * The Class Calculator.
 */
public class Calculator {
	
	/** The Tolerance. */
	static String Tolerance = "0.000000000000000001";
	
	/** The result. */
	String result = "";

	/**
	 * Calculateusingoperands.
	 *
	 * @param operand1 the operand1
	 * @param operand2 the operand2
	 * @param operatorsymbol the operatorsymbol
	 * @return the string
	 */
	public String calculateusingoperands(String operand1, String operand2, char operatorsymbol) {

		Double answerValue = 0.0;
		try {
			switch (operatorsymbol) {
			case '+':
				answerValue = Double.parseDouble(operand1) + Double.parseDouble(operand2);
				result = Double.toString(answerValue);
				break;
			case '-':
				answerValue = Double.parseDouble(operand1) - Double.parseDouble(operand2);
				result = Double.toString(answerValue);
				break;
			case '*':
				answerValue = Double.parseDouble(operand1) * Double.parseDouble(operand2);
				result = Double.toString(answerValue);
				break;
			case '/':
				// check the operand is zero or not. If ts is zero than set to
				// error
				if (Double.parseDouble(operand2) != 0.0) {
					answerValue = Double.parseDouble(operand1) / Double.parseDouble(operand2);
					result = Double.toString(answerValue);
				} else {
					result = "Error";
				}
				break;

			case '^':
				// check the value is lessthan 0 means it return Error otherwise
				// return sqrt vale
				if (Double.parseDouble(operand1) > 0) {
					result = squareRoot(operand1);
				} else {
					result = "Error";
				}

				break;
			case '%':
				answerValue = Double.parseDouble(operand1) / 100;
				result = Double.toString(answerValue);
				break;
			}
		} catch (NumberFormatException e) {
			result = "Out of Scope";
			return result;
		}

		NumberFormat formatter = new DecimalFormat(AppConstants.defaultnumberFormat2);

		String finalResult = "";
		if (!result.equalsIgnoreCase("Error")) {
			finalResult = formatter.format(Double.parseDouble(result));
		} else {
			finalResult = result;
		}

		return finalResult;

	}

	/**
	 * Square root.
	 *
	 * @param operand1 the operand1
	 * @return the string
	 */
	private String squareRoot(String operand1) {
		double returnValue = 0;
		double squareRootNumber = Double.parseDouble(operand1);
		try {
			int comparison = Double.compare(squareRootNumber, 0);
			if (comparison == -1) {
				returnValue = Double.parseDouble(AppConstants.notANumber);
			} else if (comparison == 0) {
				returnValue = 0;
			}
			double half = Double.parseDouble("1") / Double.parseDouble("2");
			double guess = (squareRootNumber + Double.parseDouble("1")) * half;
			double next_guess;
			try {
				while (true) {
					next_guess = (((squareRootNumber / guess) + guess) * half);

					if (compareValue(next_guess, guess)) {
						break;
					} else if (Double.compare(guess, next_guess) == -1) {
						break;
					}
					guess = next_guess;
				}
			} catch (Exception exception) {
				// deliberately ignore exception and assume the last guess is
				// good enough
			}
			double double_value;
			if (Double.toString(guess).length() > 20) {
				double_value = Double.parseDouble(Double.toString(guess).substring(0, 20));
			} else {
				double_value = guess;
			}

			if (compareValue(guess, double_value)) {
				returnValue = double_value;
			} else {
				returnValue = guess;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return Double.toString(returnValue);
	}

	/**
	 * Compare value.
	 *
	 * @param next_guess the next_guess
	 * @param guess the guess
	 * @return true, if successful
	 */
	public boolean compareValue(double next_guess, double guess) {

		double tolerance = Double.parseDouble(Tolerance);

		double difference = abs(next_guess - guess);
		boolean returnValue = false;
		if (Double.compare(difference, tolerance) == -1) {
			returnValue = true;
		}
		return returnValue;
	}

	/**
	 * Abs.
	 *
	 * @param inputValue the input value
	 * @return the double
	 */
	private double abs(double inputValue) {
		double minusOne = Double.parseDouble("-1");
		double absValue = inputValue;

		if (Double.compare(inputValue, 0) == -1) {
			absValue = inputValue * minusOne;
		}

		return absValue;
	}
}
