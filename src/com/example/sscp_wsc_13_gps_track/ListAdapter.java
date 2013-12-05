package com.example.sscp_wsc_13_gps_track;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<Note> {
	private RouteData route;
	private int highlightIdx, expIdx;

	public ListAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	}
	
	public ListAdapter(Context context, int resource, RouteData route, int idx, int exp_idx) {
	    super(context, resource, route.routeNotes);
	
	    this.route = route;
	    this.highlightIdx = idx;
	    this.expIdx = exp_idx;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
	    View v = convertView;
	
	    if (v == null) {
	
	        LayoutInflater vi;
	        vi = LayoutInflater.from(getContext());
	        v = vi.inflate(R.layout.item_list_row, null);
	    }
	
	    Note p = route.routeNotes.get(position);
	
	    if (p != null) {
	
	        TextView tt = (TextView) v.findViewById(R.id.distance);
	        TextView tt1 = (TextView) v.findViewById(R.id.description);
	        tt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
	        tt1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
	
	        if (tt != null) {
	            tt.setText(p.d + "");
	        }
	        if (tt1 != null) {
	
	            tt1.setText(p.note);
	        }
	    }
	    
	    if (position == this.highlightIdx) {
	    	v.setBackgroundColor(Color.RED);
	    } else if (position == this.expIdx) {
	    	v.setBackgroundColor(Color.GREEN);
	    } else {
	    	v.setBackgroundColor(Color.BLACK);
	    }
	
	    return v;
	
	}
	
}