package com.example.sscp_wsc_13_gps_track;

import com.example.sscp_wsc_13_gps_track.R;

import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	Button refreshButton, routeButton, routeNotesButton;
	LocationManager mlocManager;
	LocationListenerGPS ll;
	EditText lonText, latText, timeText, routeText, distCT, distTraveled, contName, distRemain, expectedIdx, cruiseSpeedText;
	RouteData route;
	double cruiseSpeed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.mlocManager =
				(LocationManager)getSystemService(Context.LOCATION_SERVICE);
		this.lonText = (EditText)this.findViewById(R.id.lon);
		this.latText = (EditText)this.findViewById(R.id.lat);
		this.timeText = (EditText)this.findViewById(R.id.timeText);
		this.routeText = (EditText)this.findViewById(R.id.routeIdxText);
		this.distCT = (EditText)this.findViewById(R.id.distCT);
		this.distTraveled = (EditText)this.findViewById(R.id.dT);
		this.contName = (EditText)this.findViewById(R.id.contName);
		this.distRemain = (EditText)this.findViewById(R.id.distRemain);
		this.expectedIdx = (EditText)this.findViewById(R.id.expectedIdx);
		this.cruiseSpeedText = (EditText)this.findViewById(R.id.cruiseSpeed);
		this.cruiseSpeed = 75.0;
		
		this.refreshButton = (Button)this.findViewById(R.id.refreshButton);
		this.refreshButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		      UpdateGPS();
		    }
		  });
			
		this.route = ((FullApp)this.getApplication()).route;
		this.ll = new LocationListenerGPS(this.route, lonText, latText, timeText, routeText, distCT, distTraveled, distRemain, contName, this.expectedIdx, this.cruiseSpeed);
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 20, ll);
		
		this.routeNotesButton = (Button)this.findViewById(R.id.routeNotesCompact);
		this.routeNotesButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Intent in = new Intent (MainActivity.this, RouteNotesCompact.class);
		    	in.putExtra("note_idx", route.getNotesIndex(ll.getCurrentRouteIndex()));
		    	in.putExtra("exp_idx", route.getNotesIndex(ll.getExpectedRouteIndex()));
		    	//in.putExtra("note_idx", 240);
		    	MainActivity.this.startActivity(in);
		    }
		  });
	}
	
	private void UpdateGPS() {
		String speedText = cruiseSpeedText.getText().toString();
    	try {
    		double speed = Double.parseDouble(speedText);
    		this.cruiseSpeed = speed;
    		this.ll.newCruiseSpeed(this.cruiseSpeed);
    	} catch (NumberFormatException e) {
    		this.cruiseSpeedText.setText("75");
    		this.cruiseSpeed = 75;
    	}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
