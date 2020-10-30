package com.mallowtech.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.mallowtech.convertx.FavoriteActivity;
import com.mallowtech.convertx.R;
import com.mallowtech.helper.DBHelper;

/**
 * The Class FavoriteAdapter.
 */
public class FavoriteAdapter extends BaseAdapter implements ListAdapter {
	
	/** The context. */
	Context context;
	
	/** The show check box. */
	boolean showCheckBox;
	
	/** The check state. */
	public ArrayList<CheckBoxState> checkState;
	
	/** The database helper. */
	DBHelper databaseHelper;

	/**
	 * Instantiates a new favorite adapter.
	 *
	 * @param favoriteActivity the favorite activity
	 * @param stateList the state list
	 * @param showCheckBox2 the show check box2
	 */
	public FavoriteAdapter(FavoriteActivity favoriteActivity,
			ArrayList<CheckBoxState> stateList, boolean showCheckBox2) {
		this.context = favoriteActivity;
		this.checkState = stateList;
		this.showCheckBox = showCheckBox2;
	}

	/**
	 * The Class ViewHolder.
	 */
	private class ViewHolder
	{
		
		/** The from text. */
		TextView fromText;
		
		/** The to text. */
		TextView toText;
		
		/** The check. */
		CheckBox check;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return checkState.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder =null;
		if (convertView == null) {

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.favorite_listitem, parent, false);
		
		holder = new ViewHolder();
		holder.fromText=(TextView) convertView.findViewById(R.id.from_textView);
		holder.toText=(TextView) convertView.findViewById(R.id.to_textView);
		holder.check = (CheckBox)convertView.findViewById(R.id.delete_checkbox);
		convertView.setTag(holder);
		
		if(showCheckBox==true)
		{
			holder.check.setVisibility(View.VISIBLE);
		}
		else 
		{
			holder.check.setVisibility(View.INVISIBLE);
		}
	
		holder.check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					CheckBox cb = (CheckBox) v;
					CheckBoxState _state = (CheckBoxState) cb.getTag();

					_state.setSelected(cb.isChecked());
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				}
		});
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		try {
			CheckBoxState state = checkState.get(position);
			holder.fromText.setText(state.getFromName());
			holder.toText.setText(state.getToName());
			holder.check.setChecked(state.isSelected());
			holder.check.setTag(state);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return convertView;
		
	}

}
