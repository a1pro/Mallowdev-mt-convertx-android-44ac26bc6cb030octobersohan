package com.mallowtech.adapter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mallowtech.convertx.R;

/**
 * The Class UnitGridAdapter.
 */
public class UnitGridAdapter extends BaseAdapter {

	/** The context. */
	Context context;
	
	/** The units list. */
	ArrayList<ArrayList<String>> unitsList;
	
	/** The units operations. */
	ArrayList<String> unitsOperations;
	
	/** The inflater. */
	LayoutInflater inflater;
	
	/**
	 * Instantiates a new unit grid adapter.
	 *
	 * @param context the context
	 * @param unitsList the units list
	 * @param unitsoperations the unitsoperations
	 */
	public UnitGridAdapter(Context context, ArrayList<ArrayList<String>> unitsList, ArrayList<String> unitsoperations) {// String
																														// unitDescription1
		this.context = context;
		this.unitsList = unitsList;
		this.unitsOperations = unitsoperations;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return unitsList.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.units_grid_item, parent, false);
		}

		TextView unitNameText = (TextView) convertView.findViewById(R.id.unitName_text);
		TextView unitSymbolText = (TextView) convertView.findViewById(R.id.unitSymbol_text);
		unitNameText.setText(unitsList.get(position).get(0));
		unitSymbolText.setText(unitsOperations.get(position));
		convertView.setTag(unitsList.get(position).get(0));
		return convertView;
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
