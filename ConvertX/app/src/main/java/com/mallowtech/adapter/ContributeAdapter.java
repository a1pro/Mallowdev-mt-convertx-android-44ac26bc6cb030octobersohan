package com.mallowtech.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mallowtech.convertx.R;

/**
 * The Class ContributeAdapter.
 */
public class ContributeAdapter extends BaseAdapter {

	/** The context. */
	Context context;
	 
 	/** The rupees. */
 	ArrayList<String> content,rupees;

	/**
	 * Instantiates a new contribute adapter.
	 *
	 * @param context the context
	 * @param terms the terms
	 * @param rupee the rupee
	 */
	public ContributeAdapter(Context context,ArrayList<String> terms,ArrayList<String> rupee) {
		this.context=context;
		this.content=terms;
		this.rupees=rupee;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return content.size();
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
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.contribute_listitem, parent, false);
		try {
			TextView contentText=(TextView) view.findViewById(R.id.good_textview);
			TextView rupeeButton=(TextView) view.findViewById(R.id.goodpurchase_button);
			contentText.setText(content.get(position));
			rupeeButton.setText(rupees.get(position));
		} catch (Exception e) {
			e.printStackTrace();
		}
 		return view;
	}

}



