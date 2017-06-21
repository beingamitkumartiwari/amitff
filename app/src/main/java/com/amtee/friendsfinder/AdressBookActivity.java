package com.amtee.friendsfinder;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Contacts.Data;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amtee.friendsfinder.adapter.ContactsDetailsAdapter;
import com.amtee.friendsfinder.pojo.ContactPojo;
import com.amtee.friendsfinder.voolyfile.ApplicationFL;
import com.amtee.friendsfinder.voolyfile.ConnectionCheck;
import com.amtee.friendsfinder.voolyfile.CustomJsonObjectRequest;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdressBookActivity extends Activity implements
		OnItemClickListener {

	LinearLayout main_layout;
	String phoneNumber;
	String name;
	ListView lv;
	String contactId;
	EditText searchContact;
	ContactsDetailsAdapter adapter;
	String numb = "";
	ProgressDialog progressDialog;

	//  ads content

	MyPref myprefs;
	LinearLayout myAppLayoutl, progressBar;
	Dialog myDialog;
	InterstitialAd mInterstitialAd, interstitialAd1;
	AdRequest adRequest, adRequest1;
	String urlJsonArray = "http://www.mpaisa.info/MtechAppsPromotion.asmx/appsAppId?appName=AirCallAnswer";
	Handler addMobHandler = new Handler();
	Handler handler = new Handler();
	AlertDialog alertDialog;
	View view1;
	ImageView iv_promotedappimage;
	RelativeLayout rl_promotionback;
	TextView tv_promotedappdesc;
	Button bt_yes;
	Button bt_no;
	String playstorelink;
	String backcampid;
	LinearLayout myAdd;
	SimpleDateFormat simpleDateFormat;
	boolean check = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		progressDialog = ProgressDialog.show(AdressBookActivity.this, "",
				"Loading. Please wait...", true);
		setContentView(R.layout.activity_contact_list);
		myprefs=new MyPref(this);
		searchContact = (EditText) findViewById(R.id.EditText_contact_search);
		lv = (ListView) findViewById(R.id.lv);
		Thread t = new Thread() {
			public void run() {
				ContactPojo.listOfAllContacts = (ArrayList<ContactPojo>) getNumber(AdressBookActivity.this
						.getContentResolver());
				adapter = new ContactsDetailsAdapter(getApplicationContext(),
						ContactPojo.listOfAllContacts);
				try {
					runOnUiThread(new Runnable() {
						public void run() {
							lv.setAdapter(adapter);
							progressDialog.dismiss();
						}
					});
				} catch (Exception e) {

				}

			}

		};
		t.start();
		lv.setOnItemClickListener(this);
		searchContact();
		connectionCheck();
		adMobFullPageAd();
		adMobBannerAd();
	}

	public List<ContactPojo> getNumber(ContentResolver cr) {
		List<ContactPojo> listOfContacts = new ArrayList<ContactPojo>();
		String sortOrder = Contacts.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";
		Cursor phones = cr.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, sortOrder);
		while (phones.moveToNext()) {

			ContactPojo contactPojo = new ContactPojo();
			name = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			contactPojo.setName(name);
			phoneNumber = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			contactPojo.setPhNumber(phoneNumber);
			contactPojo.setPhotoUri(getContactPhotoUri(phoneNumber));
			listOfContacts.add(contactPojo);
		}
		phones.close();

		return listOfContacts;

	}

	private Uri getContactPhotoUri(String strAddress) {
		Uri uri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(strAddress));
		ContentResolver contentResolver = getContentResolver();
		Cursor contactLookup = contentResolver.query(uri, new String[] {
				Data._ID, ContactsContract.PhoneLookup.DISPLAY_NAME }, null,
				null, null);

		try {
			if (contactLookup != null && contactLookup.getCount() > 0) {
				contactLookup.moveToNext();

				contactId = contactLookup.getString(contactLookup
						.getColumnIndex(Data._ID));
			} else {
				return null;
			}
		} finally {
			if (contactLookup != null) {
				contactLookup.close();
			}
		}

		Uri photoUri = ContentUris.withAppendedId(Contacts.CONTENT_URI,
				Long.parseLong(contactId));
		photoUri = Uri.withAppendedPath(photoUri,
				Contacts.Photo.CONTENT_DIRECTORY);
		return photoUri;

	}

	@Override
	public void onItemClick(AdapterView<?> av, View v, int pos, long arg3) {
		ContactPojo appItem = ContactPojo.listOfAllContacts.get(pos);
		numb = appItem.getPhNumber();
		finish();
	}

	private void searchContact() {
		lv.setTextFilterEnabled(true);

		searchContact.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable name) {
				String text = searchContact.getText().toString()
						.toLowerCase(Locale.getDefault());
				adapter.filter(text);
			}
		});

	}

	@Override
	public void finish() {
		Intent data = new Intent();
		data.putExtra("phoneNumber", numb);
		setResult(RESULT_OK, data);
		super.finish();
	}

	//// this medhod used for ads

	private void setimageForBack() {
		alertDialog = new AlertDialog.Builder(this).create();
		view1 = getLayoutInflater().inflate(R.layout.exit_dialog, null);
		alertDialog.setView(view1);
		iv_promotedappimage = (ImageView) view1.findViewById(R.id.imageview_promotedappimage);
		tv_promotedappdesc = (TextView) view1.findViewById(R.id.textview_promoteddappdesc);
		rl_promotionback = (RelativeLayout) view1.findViewById(R.id.relative_promotionback);
		bt_yes = (Button) view1.findViewById(R.id.button_yes);
		bt_no = (Button) view1.findViewById(R.id.button_no);
		ImageLoader m = ApplicationFL.getInstance().getImageLoader();
		m.get(myprefs.getPromotedappimage(), new ImageLoader.ImageListener() {
			@Override
			public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
				iv_promotedappimage.setImageBitmap(response.getBitmap());
			}

			@Override
			public void onErrorResponse(VolleyError error) {
			}
		});
		playstorelink = myprefs.getPromotedappurl();
		backcampid = myprefs.getBackCampaignAppId();
		tv_promotedappdesc.setText(myprefs.getPromotedappdesc());
	}

	//request for all ad id's
	private void requestforAdMobID() {
		ApplicationFL.getInstance().addToRequestQueue(requestforAdmob(urlJsonArray), "GETID");
	}

	private CustomJsonObjectRequest requestforAdmob(String url) {
		CustomJsonObjectRequest customJsonobjectRequest = new CustomJsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String AdBuddizFullPage = response.getString("AdBuddizFullPage");
					myprefs.setAddbuddies(AdBuddizFullPage);
					System.out.println("AdBuddizFullPage" + AdBuddizFullPage);

					String AdmobFullPage = response.getString("AdmobFullPage");
					myprefs.setAddmob(AdmobFullPage);
					System.out.println("AdMobFullPage" + AdmobFullPage);

					String BannerAd = response.getString("BannerAd");
					myprefs.setAddbanner(BannerAd);
					System.out.println("BannerAd" + BannerAd);
					myprefs.setShowbackads(response.getString("ShowNativAds"));
					if (response.getString("ShowNativAds").equals("1")) {
						System.out.println("response code " + response.getString("promotedappid"));
						myprefs.setBackCampaignAppId(response.getString("promotedappid"));
						myprefs.setPromotedappimage(response.getString("promotedappimage"));
						myprefs.setPromotedappurl(response.getString("promotedappurl"));
						myprefs.setPromotedappdesc(response.getString("promotedappdesc"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new com.android.volley.Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println("ankitatest error" + error);
			}
		});
		customJsonobjectRequest.setRetryPolicy(
				new DefaultRetryPolicy(
						DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
						DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
						DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
				)
		);
		return customJsonobjectRequest;
	}

	private void policyAlert() {
		myDialog = new Dialog(AdressBookActivity.this);
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		myDialog.setContentView(R.layout.policy_dialog);
		myDialog.setCancelable(false);
		myDialog.setCanceledOnTouchOutside(false);
		myDialog.show();
		Window window = myDialog.getWindow();
		window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
		window.setBackgroundDrawable(new ColorDrawable(
				Color.TRANSPARENT));
		final CheckBox doNotShowAgain = (CheckBox) myDialog
				.findViewById(R.id.dontshowagainpolicy);
		TextView policyRead = (TextView) myDialog.findViewById(R.id.policy_tv);
		String text = "When you click on advertisements delivered by Micro Mini Apps, you will be directed to a third party’s webpage and we may pass certain of your information to the third parties operating or hosting these pages, including your email address, phone number and a list of the apps on your device. This policy will tell you what data we collect and how we use it for our games and apps. For more information on how we collect, use and share your data please read Privacy policy. If you do not wish to receive these ads delivered by Micro Mini Apps you can delete the apps any time.";
		int start = text.indexOf("Privacy policy");
		SpannableString spannableString = new SpannableString(text);

		spannableString.setSpan(new MyClickableSpan(), start, start + 14,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);
		spannableString.setSpan(italicSpan, start, start + 14,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
		spannableString.setSpan(boldSpan, start, start + 14,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		policyRead.setText(spannableString);
		policyRead.setMovementMethod(LinkMovementMethod.getInstance());
		Button accept = (Button) myDialog.findViewById(R.id.acceptPolicy);
		accept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myDialog.dismiss();
				if (doNotShowAgain.isChecked()) {
					myprefs.setPolicyRead(true);
				}
			}
		});

		Button decline = (Button) myDialog.findViewById(R.id.declinePolicy);
		decline.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myDialog.dismiss();
				finish();
			}
		});
	}


	private class MyClickableSpan extends ClickableSpan {
		public void onClick(View textView) {
			myDialog.dismiss();
			Intent iPolicy = new Intent(getApplicationContext(),
					PrivacyPolicy_Activity.class);
			startActivity(iPolicy);
			finish();
		}

		@Override
		public void updateDrawState(TextPaint textPaint) {
			textPaint.setColor(Color.BLUE);
			textPaint.setUnderlineText(true);
		}
	}

	public void connectionCheck() {
		ConnectionCheck connectionCheck = new ConnectionCheck(getApplicationContext());
		interstitialAd1 = new InterstitialAd(getApplicationContext());
		if (connectionCheck.isConnectionAvailable()) {
			interstitialAd1.setAdUnitId(myprefs.getAddmob());
			System.out.println("Admob getting " + myprefs.getAddmob());
			adRequest1 = new AdRequest.Builder().build();
			interstitialAd1.loadAd(adRequest1);
			addAdmobAdListner1();
//            adMobBannerAd();
			if (addMobHandler != null) {
				addMobHandler.postDelayed(mAddIsLoaded, 8 * 1000);
			} else {
				addMobHandler = new Handler();
				addMobHandler.postDelayed(mAddIsLoaded, 8 * 1000);
			}
		} else {
			myAppLayoutl.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
		}
	}

	Runnable mAddIsLoaded = new Runnable() {
		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (!interstitialAd1.isLoaded()) {
						myAppLayoutl.setVisibility(View.VISIBLE);
						progressBar.setVisibility(View.GONE);
					}
				}
			});
		}
	};

	//dcgiudgsfoiuasgdiuagd

	public void adMobFullPageAd() {
		mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId(myprefs.getAddmob());
		requestNewInterstitial();
		mInterstitialAd.setAdListener(new AdListener() {

			@Override
			public void onAdClosed() {
				requestNewInterstitial();
			}
		});
	}

	private void requestNewInterstitial() {
		AdRequest adRequest = new AdRequest.Builder().build();
		mInterstitialAd.loadAd(adRequest);
	}

	Runnable mShowFullPageAdTask = new Runnable() {
		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (myprefs.getCHECKFORADS()) {
						if (mInterstitialAd.isLoaded())
							mInterstitialAd.show();
					}
				}
			});
		}
	};


	private void adMobBannerAd() {
		myprefs = new MyPref(getApplicationContext());
		myAdd = (LinearLayout) findViewById(R.id.myAdd);
		final AdView adView = new AdView(this);
		adView.setAdUnitId(myprefs.getAddbanner());
		adView.setAdSize(AdSize.BANNER);
		myAdd.addView(adView);
		final AdListener listener = new AdListener() {
			@Override
			public void onAdLoaded() {
				adView.setVisibility(View.VISIBLE);
				super.onAdLoaded();
			}
		};
		adView.setAdListener(listener);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}

	// jhbiushcoihsdf

	private void addAdmobAdListner1() {
		interstitialAd1.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				if (interstitialAd1.isLoaded()) {
					interstitialAd1.show();
				} else {
				}
			}

//			@Override
//			public void onAdFailedToLoad(int errorCode) {
//				myAppLayoutl.setVisibility(View.VISIBLE);
//				progressBar.setVisibility(View.GONE);
//			}
//
//			@Override
//			public void onAdClosed() {
//				super.onAdClosed();
//				myAppLayoutl.setVisibility(View.VISIBLE);
//				progressBar.setVisibility(View.GONE);
//			}

			@Override
			public void onAdLeftApplication() {
				super.onAdLeftApplication();
			}

			@Override
			public void onAdOpened() {
				super.onAdOpened();
			}
		});
	}

	//deswfgiewugfo.wesfgtuawodfs

	@Override
	protected void onPause() {
		super.onPause();
		if (addMobHandler != null) {
			addMobHandler.removeCallbacks(mAddIsLoaded);
			addMobHandler = null;
		}
		if (handler != null) {
			handler.removeCallbacks(mShowFullPageAdTask);
			handler = null;
		}
	}


	@Override
	protected void onResume() {
		super.onResume();
		if (handler != null) {
			handler.postDelayed(mShowFullPageAdTask, 45 * 1000);
		} else {
			handler = new Handler();
			handler.postDelayed(mShowFullPageAdTask, 45 * 1000);
		}
	}

}
