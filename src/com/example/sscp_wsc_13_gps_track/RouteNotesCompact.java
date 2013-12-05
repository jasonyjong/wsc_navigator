package com.example.sscp_wsc_13_gps_track;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RouteNotesCompact extends Activity {
	
	protected RouteData route;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_notes_compact);
		
		Bundle b = getIntent().getExtras();
		final int note_idx = b.getInt("note_idx");
		
		final int exp_idx = b.getInt("exp_idx");
		
		this.route = ((FullApp)this.getApplication()).route;		
		
		ListView lv = (ListView) findViewById(R.id.notesList);
		ListAdapter customAdapter = new ListAdapter(this, R.layout.item_list_row, this.route, note_idx, exp_idx);
		lv.setAdapter(customAdapter);
		lv.setSelection(Math.max(0, note_idx - 5));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.route_notes, menu);
		return true;
	}
}
