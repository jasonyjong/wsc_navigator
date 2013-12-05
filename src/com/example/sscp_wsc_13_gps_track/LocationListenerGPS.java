package com.example.sscp_wsc_13_gps_track;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.EditText;

public class LocationListenerGPS implements LocationListener {
	
	final DecimalFormat df = new DecimalFormat();
	final DecimalFormat df2 = new DecimalFormat();

	
	EditText lonText, latText, timeText, routeText, distCT, distTraveledText, distRemain, contName, expectedIdx;
	
	int previousUnixTime, previousRoute, expectedRouteIdx;
	double distTraveled;
	Double cruiseSpeed;
	String previousTime;
	
	SimpleDateFormat sdf;
	RouteData route;
	
	String[] cont_stop_name = {"Katherine", "Dunmarra", "Tennant Creek", "Ti Tree", "Alice Springs", "Kulera", "Coober Pedy", "Glendambo", "Port Augusta", "Adelaide"};
	/*
	 * 321.30674
		633.98237
		 990.0102
		1303.0229
		1498.0687
		1772.7948
		2187.7958
		2441.0219
		 2729.701
	 */

	public LocationListenerGPS(RouteData route, EditText lonText, EditText latText, EditText timeText, 
			EditText routeText, EditText distCT, EditText distTraveledText, EditText distRemain, EditText contName, EditText expectedIdx, Double cruiseSpeed) {
		super();
		
		this.lonText = lonText;
		this.latText = latText;
		this.timeText = timeText;
		this.routeText = routeText;
		this.distCT = distCT;
		this.distTraveledText = distTraveledText;
		this.contName = contName;
		this.distRemain = distRemain;
		this.expectedIdx = expectedIdx;
		
		this.previousUnixTime = 0;
		this.previousTime = "None";
		this.previousRoute = 0;
		this.distTraveled = 0;
		this.expectedRouteIdx = 0;
		this.cruiseSpeed = cruiseSpeed;
		
		this.sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
		
		this.route = route;
		df.setMaximumFractionDigits(1);
		df2.setMaximumFractionDigits(3);
	}

    @Override
    public void onLocationChanged(Location loc) {
		if (loc != null) {
			this.lonText.setText(""+loc.getLongitude());
			this.latText.setText(""+loc.getLatitude());
			
			this.previousUnixTime = getUnixTime();
			this.previousTime = sdf.format(new Date());
			this.timeText.setText(this.previousTime);
			this.previousRoute = this.route.getRouteIndex(loc);
			this.routeText.setText(this.previousRoute + "");
			this.distTraveled = route.getDistanceTraveled(this.previousRoute);
			this.distTraveledText.setText(df2.format(this.distTraveled));
			this.distRemain.setText(df2.format(3008.77778934722 - this.distTraveled));
			
			double hrsUnix = ((double) this.previousUnixTime) / 3600.0;
			double hrsToFive = (17.0 - 9.5 - hrsUnix) % 24.0;
			if (hrsToFive < 0) {
				hrsToFive = hrsToFive + 24.0;
			}
			this.expectedRouteIdx = this.route.getExpectedIdx(this.previousRoute, hrsToFive, this.cruiseSpeed);
			this.expectedIdx.setText(this.expectedRouteIdx + "");
			
			List<Integer> cont_stop = route.contStop;
			for (int i=0; i < cont_stop.size(); i++) {
				double dist_cont = route.getDistanceTraveled(cont_stop.get(i));
				if (dist_cont >= this.distTraveled) {
					Double ds = dist_cont - this.distTraveled;
					this.distCT.setText(df.format(ds));
					this.contName.setText(cont_stop_name[i]);
					break;
				}
			}
		} else {
			this.lonText.setText("Not found");
			this.latText.setText("Not found");
		}
    }
    
    public void newCruiseSpeed(Double cruiseSpeed) {
    	this.cruiseSpeed = cruiseSpeed;
    	double hrsUnix = ((double) this.previousUnixTime) / 3600.0;
		double hrsToFive = (17.0 - 9.5 - hrsUnix) % 24.0;
		if (hrsToFive < 0) {
			hrsToFive = hrsToFive + 24.0;
		}
		this.expectedRouteIdx = this.route.getExpectedIdx(this.previousRoute, hrsToFive, this.cruiseSpeed);
		this.expectedIdx.setText(this.expectedRouteIdx + "");
    }
    
    public int getUnixTime() {
    	return (int) (System.currentTimeMillis() / 1000L);
    }
    
    public int getCurrentRouteIndex() {
    	return this.previousRoute;
    }
    
    public int getExpectedRouteIndex() {
    	return this.expectedRouteIdx;
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
