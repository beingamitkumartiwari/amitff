package com.amtee.friendsfinder.pojo;

import java.util.ArrayList;

import android.net.Uri;

public class ContactPojo {

	private String name="";
	private String phNumber="";
	private Uri photoUri=null;
	
	public static ArrayList<ContactPojo> listOfAllContacts = new ArrayList<ContactPojo>();
	
	
	public Uri getPhotoUri() {
		return photoUri;
	}
	public void setPhotoUri(Uri photoUri) {
		this.photoUri = photoUri;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhNumber() {
		return phNumber;
	}
	public void setPhNumber(String phNumber) {
		this.phNumber = phNumber;
	}
	
	@Override
	public String toString() {
		return "Name"+name+ "  PhoneNumber"+phNumber+ "  PhotoUri"+photoUri;
	}
	
}

