package com.example.sscp_wsc_13_gps_track;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.location.Location;
import android.util.Log;
import android.util.Pair;

public class RouteData {
	
	List<Pair<Double, Double>> coordinates;
	List<Double> distance;
	List<Note> routeNotes;
	List<Integer> contStop;
	Set<Integer> contStopSet;
	
	public RouteData(FullApp fullApp) {
		coordinates = new ArrayList<Pair<Double, Double>>(7443);
		distance = new ArrayList<Double>(7443);
		BufferedReader br = new BufferedReader(new InputStreamReader(fullApp.getResources().openRawResource(R.raw.au_wsc_13)));
		
		this.contStopSet = new HashSet<Integer>(10);
		
		String line = "";
		try {
			int i = 0;
			contStop = new ArrayList<Integer>(10);
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				coordinates.add(new Pair<Double, Double>(Double.parseDouble(data[0]), Double.parseDouble(data[1])));
				distance.add(Double.parseDouble(data[2]));
				if (data[4].equals("1")) {
					contStop.add(i);
					contStopSet.add(i);
				}
				i++;
			}
			contStop.add(i-1);
			contStopSet.add(i-1);
		} catch (NumberFormatException e) {} catch (IOException e) {}
		
		routeNotes = new ArrayList<Note>(10000);
		br = new BufferedReader(new InputStreamReader(fullApp.getResources().openRawResource(R.raw.au_wsc_13_route_notes)));
		try {
			line = br.readLine(); // get rid of first line
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				
				String desc = "   ";
				for (int i=3; i<data.length; i++) {
					desc += data[i];
				}
				
				if (data[2].equals("True")) {
					desc += " | TRAFFIC LIGHT";
				}
				
				if (!data[1].equals("-1")) {
					desc += " | SL:" + data[1];
				}
				
				routeNotes.add(new Note(Double.parseDouble(data[0]), desc));
			}
		} catch (NumberFormatException e) {} catch (IOException e) {}
	}

	public int getRouteIndex(Location loc) {
    	
    	double lat = loc.getLatitude();
    	double lon = loc.getLongitude();
    	
    	double min = 1000000;
    	int routeMin = 0;
    	for (int i=0; i<coordinates.size(); i++) {
    		Pair<Double, Double> dat = coordinates.get(i);
    		
    		double diff = Math.abs(dat.first - lon) + Math.abs(dat.second - lat);
    		
    		if (diff <= min) {
    			min = diff;
    			routeMin = i + 1;
    		}
    	}
    	
    	return routeMin;
    }
	
	public int getExpectedIdx(int curr, double hrsToDrive, double cruiseSpeed) {
		
		for (int i=curr + 1; i < distance.size(); i++) {
			double diff = distance.get(i) - distance.get(i-1);
			if (diff/cruiseSpeed >= hrsToDrive) {
				return i;
			} else {
				hrsToDrive = hrsToDrive - diff/cruiseSpeed;
				if (contStopSet.contains(i)) {
					if (hrsToDrive <= 0.5) {
						return i;
					} else {
						hrsToDrive = hrsToDrive - 0.5;
					}
				}
			}
		}
		
		return distance.size() -1;
	}
	
	public double getDistanceTraveled(int idx) {
		if (idx < 0 || idx >= distance.size()) { 
			return -1;
		}
		
		return distance.get(idx);
	}
	
	public int getNotesIndex(double dist_traveled) {
		if (dist_traveled > 1496.0) {
			dist_traveled -= 5.0;
		}
		
		if (dist_traveled > 2180) {
			dist_traveled -= 12.0;
		}
		
		if (dist_traveled > 2427) {
			dist_traveled += 5.0;
		}
		
    	double min = 1000000;
    	int routeMin = 0;
    	for (int i=0; i<routeNotes.size(); i++) {
    		Note dat = routeNotes.get(i);
    		
    		double diff = Math.abs(dat.d - dist_traveled);
    		
    		if (diff <= min) {
    			min = diff;
    			routeMin = i + 1;
    		}
    	}
    	
    	return routeMin;
    }
	
	public int getNotesIndex(int idx) {
		return getNotesIndex(getDistanceTraveled(idx));
    }
}
