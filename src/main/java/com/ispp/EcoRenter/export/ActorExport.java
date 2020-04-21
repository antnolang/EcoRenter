package com.ispp.EcoRenter.export;

public class ActorExport {
	
	private String myName,mySurname,myTelephone,myUsername,myEmail,MySmallholdingsTtile,myRentOutStartDate,Mycomments;
	
	
	
	public ActorExport(String name, String surname, String telephone, String username,String email,String smallholdings,String rentouts,String comments) {
		
		
		this.myName= name;
		this.mySurname = surname;
		this.myTelephone = telephone;
		this.myUsername = username;
		this.myEmail = email;
		this.MySmallholdingsTtile = smallholdings;
		this.myRentOutStartDate = rentouts;
		this.Mycomments = comments;
		
	}



	public String getMyName() {
		return myName;
	}



	public void setMyName(String myName) {
		this.myName = myName;
	}



	public String getMySurname() {
		return mySurname;
	}



	public void setMySurname(String mySurname) {
		this.mySurname = mySurname;
	}



	public String getMyTelephone() {
		return myTelephone;
	}



	public void setMyTelephone(String myTelephone) {
		this.myTelephone = myTelephone;
	}



	public String getMyUsername() {
		return myUsername;
	}



	public void setMyUsername(String myUsername) {
		this.myUsername = myUsername;
	}



	public String getMyEmail() {
		return myEmail;
	}



	public void setMyEmail(String myEmail) {
		this.myEmail = myEmail;
	}



	public String getMySmallholdingsTtile() {
		return MySmallholdingsTtile;
	}



	public void setMySmallholdingsTtile(String mySmallholdingsTtile) {
		MySmallholdingsTtile = mySmallholdingsTtile;
	}



	public String getMyRentOutStartDate() {
		return myRentOutStartDate;
	}



	public void setMyRentOutStartDate(String myRentOutStartDate) {
		this.myRentOutStartDate = myRentOutStartDate;
	}



	public String getMycomments() {
		return Mycomments;
	}



	public void setMycomments(String mycomments) {
		Mycomments = mycomments;
	}


	
	
}