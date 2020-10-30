package com.mallowtech.convertx;

import java.util.ArrayList;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mallowtech.adapter.socialListAdapter;
import com.mallowtech.helper.AppConstants;
import com.mallowtech.helper.Utilities;

/**
 * The Class SocialActivity.
 */
public class SocialActivity extends Activity {

	/** The navigation button. */
	Button navigationButton;
	
	/** The about app layout. */
	RelativeLayout aboutAppLayout;
	
	/** The social list. */
	ListView socialList;
	
	/** The social list names. */
	ArrayList<String> socialListNames;
	
	/** The social list images. */
	ArrayList<Integer> socialListImages;
	
	/** The follow twitter. */
	boolean followTwitter = false;

	//for share app
	/** The context. */
	Context context;
	
	/** The adapter. */
	SocialAuthAdapter adapter;
	
	/** The action bar width. */
	public static int actionBarWidth = 0;
	
	/** The layout inflater. */
	LayoutInflater layoutInflater;
	
	/** The share button. */
	Button shareButton;
	
	/** The share menu popup. */
	PopupWindow shareMenuPopup;
	
	/** The progress dialog. */
	ProgressDialog progressDialog;
	
	/** The activity. */
	Activity activity;
	
	/** The share dialog. */
	Dialog shareDialog;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_social);
		socialListImages = new ArrayList<Integer>();
		socialListNames = new ArrayList<String>();
		context = getApplicationContext();
		activity= SocialActivity.this;
		//for sharing the app
		adapter = new SocialAuthAdapter(new ResponseListener());

		//for message posting dialog
		shareDialog = new Dialog(Utilities.getDialogContext(activity));
		shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		shareDialog.setCancelable(true);
		shareDialog.setContentView(R.layout.dialog_share);

		//for progress dialog
		progressDialog = new ProgressDialog(Utilities.getDialogContext(activity));
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.setMessage(AppConstants.loadingMessage);

		//adding list values
		socialListNames.add(AppConstants.contactUs);
		socialListNames.add(AppConstants.rateApp);
		socialListNames.add(AppConstants.followFacebook);
		socialListNames.add(AppConstants.followTwitter);
		socialListNames.add(AppConstants.shareApp);
		//listNames.add("Our other apps");

		socialListImages.add(R.drawable.icon_contact);
		socialListImages.add(R.drawable.icon_rate);
		socialListImages.add(R.drawable.icon_facebook);
		socialListImages.add(R.drawable.icon_twitter);
		socialListImages.add(R.drawable.icon_share);
		//listImages.add(R.drawable.icon_ourapps);

		socialList = (ListView) findViewById(R.id.social_list);
		socialListAdapter socialAdapter = new socialListAdapter(SocialActivity.this,R.layout.social_listitem,socialListImages,socialListNames); 
		socialList.setAdapter(socialAdapter);

		//first line of social screen. 
		aboutAppLayout = (RelativeLayout) findViewById(R.id.aboutApp_layout);
		aboutAppLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent aboutThisApp = new Intent(SocialActivity.this,AboutThisAppActivity.class);
				startActivity(aboutThisApp);
			}
		});

		navigationButton = (Button)findViewById(R.id.navigation_button);
		navigationButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		//list on click events
		socialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try {
					if(socialListNames.get(position).toString().equalsIgnoreCase(AppConstants.contactUs))
					{
						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("plain/text");
						intent.putExtra(Intent.EXTRA_EMAIL, new String[] { AppConstants.contactMailId  });
						intent.putExtra(Intent.EXTRA_SUBJECT, AppConstants.mailSubject );
						startActivity(Intent.createChooser(intent, "Send mail..."));
					}
					else if(socialListNames.get(position).toString().equalsIgnoreCase(AppConstants.rateApp))
					{
						if(Utilities.getNetworkStatus(Utilities.getDialogContext(activity))){
							Uri uri = Uri.parse( AppConstants.rateAppUrl1+ context.getPackageName());
							Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
							try {
								startActivity(goToMarket);
							} catch (ActivityNotFoundException e) {
								startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.rateAppUrl2+ context.getPackageName())));
							}
						}
						else{
							Utilities.callAlert(Utilities.getDialogContext(activity));
						}
					}
					else if(socialListNames.get(position).toString().equalsIgnoreCase(AppConstants.followFacebook))
					{
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SocialActivity.this);
						alertDialogBuilder.setTitle(AppConstants.facebookTitle);
						alertDialogBuilder.setMessage(AppConstants.facebookMessage).setCancelable(false);
						alertDialogBuilder.setPositiveButton(AppConstants.okMessage, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								if(Utilities.getNetworkStatus(Utilities.getDialogContext(activity))){
									Uri uri = Uri.parse(AppConstants.facebookFollowUrl);
									Intent facebookIntent = new Intent(Intent.ACTION_VIEW, uri);
									startActivity(facebookIntent);
								}else{
									Utilities.callAlert(Utilities.getDialogContext(activity));
								}
							}
						});
						alertDialogBuilder.setNegativeButton(AppConstants.cancelMessage, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
					else if(socialListNames.get(position).toString().equalsIgnoreCase(AppConstants.followTwitter))
					{
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SocialActivity.this);
						alertDialogBuilder.setTitle(AppConstants.twitterTitle);
						alertDialogBuilder.setMessage(AppConstants.twitterMessage).setCancelable(false);
						alertDialogBuilder.setPositiveButton(AppConstants.okMessage, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								if(Utilities.getNetworkStatus(Utilities.getDialogContext(activity))){
									Intent twitterIntent = new Intent(Intent.ACTION_VIEW);	
									twitterIntent.setData(Uri.parse(AppConstants.twitterFollowUrl));	
									startActivity(twitterIntent);
								}
								else{
									Utilities.callAlert(Utilities.getDialogContext(activity));
								}
							}
						});
						alertDialogBuilder.setNegativeButton(AppConstants.cancelMessage, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();

					}
					else if(socialListNames.get(position).toString().equalsIgnoreCase(AppConstants.shareApp))
					{
						if(Utilities.getNetworkStatus(Utilities.getDialogContext(activity))){
							Context contextPopup = context;
							final DisplayMetrics displayMetrics = new DisplayMetrics();
							getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
							actionBarWidth = displayMetrics.widthPixels;
							layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							View layout = layoutInflater.inflate(R.layout.popup_layout, null);
							shareMenuPopup = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
							shareMenuPopup.setContentView(layout);
							@SuppressWarnings("deprecation")
							BitmapDrawable bm = new BitmapDrawable();
							shareMenuPopup.setBackgroundDrawable(bm);
							Button cancelButton = (Button) layout.findViewById(R.id.button_popup);
							TextView popUpTitle1 = (TextView) layout.findViewById(R.id.textView_popup2);
							popUpTitle1.setText(AppConstants.shareText);
							shareMenuPopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);

							ListView dropdownList = (ListView) layout.findViewById(R.id.listView_popup);
							String[] popupValues = { AppConstants.onFacebook,AppConstants.onTwitter , AppConstants.byEmail};

							PopupAdapter popUpAdapter = new PopupAdapter(contextPopup, popupValues,adapter);
							dropdownList.setAdapter(popUpAdapter);
							cancelButton.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									shareMenuPopup.dismiss();
								}
							});
						}
						else{
							Utilities.callAlert(Utilities.getDialogContext(activity));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}


	/**
	 * Listens Response from Library.
	 *
	 * @see ResponseEvent
	 */

	final class ResponseListener implements DialogListener {

		/* (non-Javadoc)
		 * @see org.brickred.socialauth.android.DialogListener#onComplete(android.os.Bundle)
		 */
		@SuppressLint({ "DefaultLocale", "CutPasteId" })
		@Override
		public void onComplete(Bundle values) {
			try {
				progressDialog.cancel();
				String	providerName = values.getString(SocialAuthAdapter.PROVIDER);

				TextView appText = (TextView)shareDialog.findViewById(R.id.app_textView);

				String con = providerName.substring(0, 1);
				providerName = providerName.replaceFirst(con, con.toUpperCase());
				appText.setText(providerName);

				Button postButton = (Button) shareDialog.findViewById(R.id.post_button);
				Button cancelButton = (Button) shareDialog.findViewById(R.id.cancel_button);

				EditText msgText = (EditText) shareDialog.findViewById(R.id.editText_post);
				msgText.setText(AppConstants.messageToPost+AppConstants.rateAppUrl2 + context.getPackageName());
				final EditText postText = (EditText) shareDialog.findViewById(R.id.editText_post);

				shareDialog.show();

				final Bitmap bitmap =BitmapFactory.decodeResource(context.getResources(),R.drawable.logo);
				postButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						try {
							shareDialog.dismiss();
							progressDialog.show();
							adapter.uploadImageAsync(postText.getText().toString(), "icon.png", bitmap, 0,new UploadImageListener());

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

				cancelButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						shareDialog.dismiss();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/* (non-Javadoc)
		 * @see org.brickred.socialauth.android.DialogListener#onError(org.brickred.socialauth.android.SocialAuthError)
		 */
		@Override
		public void onError(SocialAuthError error) {
			try {
				progressDialog.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/* (non-Javadoc)
		 * @see org.brickred.socialauth.android.DialogListener#onCancel()
		 */
		@Override
		public void onCancel() {
			try {
				progressDialog.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/* (non-Javadoc)
		 * @see org.brickred.socialauth.android.DialogListener#onBack()
		 */
		@Override
		public void onBack() {
			try {
				progressDialog.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * The listener interface for receiving uploadImage events.
	 * The class that is interested in processing a uploadImage
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addUploadImageListener<code> method. When
	 * the uploadImage event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see UploadImageEvent
	 */
	private final class UploadImageListener implements SocialAuthListener<Integer> {

		/* (non-Javadoc)
		 * @see org.brickred.socialauth.android.SocialAuthListener#onError(org.brickred.socialauth.android.SocialAuthError)
		 */
		@Override
		public void onError(SocialAuthError e) {
			try {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SocialActivity.this);
				alertDialogBuilder.setTitle(AppConstants.infoTitle);
				alertDialogBuilder.setMessage(AppConstants.messagePostFailed).setCancelable(false);
				alertDialogBuilder.setPositiveButton(AppConstants.okMessage, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogAlert, int id) {
						dialogAlert.cancel();

					}
				});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		/* (non-Javadoc)
		 * @see org.brickred.socialauth.android.SocialAuthListener#onExecute(java.lang.String, java.lang.Object)
		 */
		@Override
		public void onExecute(String arg0, Integer t) {
			try {
				Integer status = t;
				if (status.intValue() == 200 || status.intValue() == 201 || status.intValue() == 204){
					progressDialog.cancel();
				}
				else{
					progressDialog.cancel();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//adapter class for share pop-up with On Facebook, On Twitter, By Email
	/**
	 * The Class PopupAdapter.
	 */
	public class PopupAdapter extends BaseAdapter {

		/** The context. */
		Context context;
		
		/** The pop up values. */
		String[] popUpValues;
		
		/** The inflater. */
		LayoutInflater inflater;
		
		/** The adapter. */
		SocialAuthAdapter adapter;
		
		/** The providers. */
		private final Provider[] providers = new Provider[] { Provider.FACEBOOK, Provider.TWITTER };
		
		/**
		 * Instantiates a new popup adapter.
		 *
		 * @param value the value
		 * @param popupValues the popup values
		 * @param mAdapter the m adapter
		 */
		public PopupAdapter(Context value, String[] popupValues,SocialAuthAdapter mAdapter) {
			this.context=value;
			this.popUpValues = popupValues;
			this.adapter=mAdapter;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			if (popUpValues!=null) {
				return popUpValues.length;
			}
			return 0;
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
			try {
				ViewHolder holder=null;
				if (convertView == null) {
					holder = new ViewHolder();
					inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(R.layout.popup_listitem, parent, false);
					holder.text = (TextView) convertView.findViewById(R.id.text_popup);
					holder.text.setText(popUpValues[position]);
					convertView.setTag(holder);
				}

				else {
					holder = (ViewHolder) convertView.getTag();
				}

				holder.text.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if(shareMenuPopup.isShowing()){
							shareMenuPopup.dismiss();

							if(popUpValues[position].equalsIgnoreCase("By Email")){
								String mailBody ="<a href=" + AppConstants.rateAppUrl2 + context.getPackageName()+
										">ConvertX</a>" +										
										"- A very useful Units & Currency converter app for Android.<br><br>Check out this useful converter app for Android from Play Store - " 
										+AppConstants.rateAppUrl2 + context.getPackageName();
								Intent intent = new Intent(Intent.ACTION_SEND);
								intent.setType("text/html");
								intent.putExtra(Intent.EXTRA_SUBJECT, AppConstants.mailSubject);
								intent.putExtra(Intent.EXTRA_TEXT,Html.fromHtml(mailBody.toString()));
								startActivity(Intent.createChooser(intent, "Send mail..."));
							}
							else{
								progressDialog.show();
								if (providers[position].equals(Provider.TWITTER))
									adapter.addCallBack(Provider.TWITTER, AppConstants.twitterCallbackUrl);
								adapter.authorize(Utilities.getDialogContext(activity), providers[position]);
							}

						}

					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}
		
		/**
		 * The Class ViewHolder.
		 */
		class ViewHolder {
			
			/** The text. */
			TextView text;
		}

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		progressDialog.cancel();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		progressDialog.cancel();
	}
}
