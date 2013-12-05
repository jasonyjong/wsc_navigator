package com.example.sscp_wsc_13_gps_track;
import com.example.sscp_wsc_13_gps_track.RouteData;

import android.app.Application;


public class FullApp extends Application {
	
	public RouteData route;

	@Override
	public void onCreate() {
		super.onCreate();
		this.route = new RouteData(this);
	}
}
