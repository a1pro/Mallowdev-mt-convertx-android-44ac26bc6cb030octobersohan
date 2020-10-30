package com.mallowtech.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mallowtech.convertx.R;
import com.mallowtech.convertx.SocialActivity;

/**
 * The Class socialListAdapter.
 */
public class socialListAdapter extends BaseAdapter {
	
	/** The context. */
	Context context;
	
	/** The social listitem. */
	int socialListitem;
	 
 	/** The list images. */
 	ArrayList<Integer> listImages;
	 
 	/** The list names. */
 	ArrayList<String> listNames;

	/**
	 * Instantiates a new social list adapter.
	 *
	 * @param socialActivity the social activity
	 * @param socialListitem the social listitem
	 * @param listImages the list images
	 * @param listNames the list names
	 */
	public socialListAdapter(SocialActivity socialActivity, int socialListitem, ArrayList<Integer> listImages, ArrayList<String> listNames) {
		this.context = socialActivity;
		this.socialListitem = socialListitem;
		this.listImages = listImages;
		this.listNames = listNames;
	}

	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return listNames.size();
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
		View view = inflater.inflate(R.layout.social_listitem, parent, false);
		try {
			ImageView socialListImage = (ImageView)view.findViewById(R.id.socialList_imageView);
			TextView socialListText = (TextView)view.findViewById(R.id.socialList_textView);
			
			socialListImage.setBackgroundResource(listImages.get(position));
			socialListText.setText(listNames.get(position));
		} catch (Exception e) {
			e.printStackTrace();
		}
 		return view;
	}

}
