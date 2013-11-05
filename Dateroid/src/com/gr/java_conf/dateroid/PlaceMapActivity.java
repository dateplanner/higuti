package com.gr.java_conf.dateroid;

import com.gr.java_conf.dateroid.PlacesJSON.Radius;
import com.gr.java_conf.dateroid.PlacesJSON.Types;

import jap.gr.java_conf.dateroid.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PlaceMapActivity extends Activity {

	WebView placesMap;
	LocationManager locationManager;
	Criteria criteria;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_placemap);
		placesMap = (WebView)findViewById(R.id.placesMap_web);

		Intent intent = getIntent();
		if(intent == null){
			return;
		}
		Types mTypes = (Types)intent.getParcelableExtra("placeType");
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
        
		PlacesJSON placesJSON = new PlacesJSON();
		placesJSON.setLocation(location.getLatitude(), location.getLongitude());
		placesJSON.setRadius(Radius.ONE_THOUSAND);
		placesJSON.setTypes(mTypes);
		
		placesMap.addJavascriptInterface(placesJSON, "android");
		placesMap.requestFocusFromTouch();
		placesMap.setWebViewClient(new WebViewClient());
		placesMap.setWebChromeClient(new WebChromeClient());
		
		//webページをロード
		placesMap.loadUrl("file:///android_asset/places.html");	
		WebSettings webSettings = placesMap.getSettings();
		webSettings.setJavaScriptEnabled(true);
	}
}
