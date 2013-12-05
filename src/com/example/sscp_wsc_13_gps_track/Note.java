package com.example.sscp_wsc_13_gps_track;

public class Note {

	Double d;
	String note;
	
	public Note (Double d, String note) {
		this.d = d;
		this.note = note;
	}
	
	@Override
	public String toString() {
		return d + ": " + note;
	}
}
