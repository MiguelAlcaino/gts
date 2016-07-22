package org.opengts.db.tables;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import org.opengts.dbtools.DBConnection;

public class TrackingNumber {
	private int trackingNumberID;
	private String deviceID;
	private int tempUserID;
	private double startLatitude;
	private double startLongitude;
	private double endLatitude;
	private double endLongitude;
	private Timestamp startTime;
	private Timestamp endTime;
	private String number;
	private boolean on_off;
	private String tempPassword;
	

	public TrackingNumber(){
		this.trackingNumberID = 0;
		this.deviceID = "";
		this.tempUserID = 0;
		this.startLatitude = 0;
		this.startLongitude = 0;
		this.endLatitude = 0;
		this.endLongitude = 0;
		this.startTime = null;
		this.endTime = null;
		this.on_off = false;
		this.tempPassword = "";
	}
	
	public TrackingNumber(int trackingNumberID, int deviceID, int tempUserID,
			double startLatitude, double startLongitude, double endLatitude,
			double endLongitude, Timestamp startTime, Timestamp endTime, String number,
			boolean on_off, String tempPassword) {
		this.trackingNumberID = trackingNumberID;
		this.deviceID = "";
		this.tempUserID = tempUserID;
		this.startLatitude = startLatitude;
		this.startLongitude = startLongitude;
		this.endLatitude = endLatitude;
		this.endLongitude = endLongitude;
		this.startTime = startTime;
		this.endTime = endTime;
		this.number = number;
		this.on_off = on_off;
		this.tempPassword = tempPassword;
	}

	
	public int getTrackingNumberID() {
		return trackingNumberID;
	}
	
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public int getTempUserID() {
		return tempUserID;
	}
	public void setTempUserID(int tempUserID) {
		this.tempUserID = tempUserID;
	}
	public double getStartLatitude() {
		return startLatitude;
	}
	public void setStartLatitude(double startLatitude) {
		this.startLatitude = startLatitude;
	}
	public double getStartLongitude() {
		return startLongitude;
	}
	public void setStartLongitude(double startLongitude) {
		this.startLongitude = startLongitude;
	}
	public double getEndLatitude() {
		return endLatitude;
	}
	public void setEndLatitude(double endLatitude) {
		this.endLatitude = endLatitude;
	}
	public double getEndLongitude() {
		return endLongitude;
	}
	public void setEndLongitude(double endLongitude) {
		this.endLongitude = endLongitude;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public void setStartTime(String startTime){
		if(startTime == ""){
			this.startTime = null;
		}else{
			this.startTime = Timestamp.valueOf(startTime);
		}
		
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public void setEndTime(String endTime){
		if(endTime == ""){
			this.endTime = null;
		}else{
			this.endTime = Timestamp.valueOf(endTime);
		}	
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public boolean isOn_off() {
		return on_off;
	}
	public void setOn_off(boolean on_off) {
		this.on_off = on_off;
	}
	public String getTempPassword() {
		return tempPassword;
	}
	public void setTempPassword(String tempPassword) {
		this.tempPassword = tempPassword;
	}
	
	public int insert() throws SQLException {
		DBConnection dbc = DBConnection.getDefaultConnection(); 
    	
    		//Statement st = dbc.createStatement();
    		int on_off;
    		if(this.on_off){
    			on_off = 1;
    		}else{
    			on_off = 0;
    		}
    		Connection conn = dbc.getConnection();
    		
    		
    		//String query = "INSERT INTO TrackingNumber(deviceID, tempUserID, accountID, startLatitude, startLongitude, endLatitude, endLongitude, startTime, endTime, number, on_off, tempPassword)"
    		//		+ "VALUES('"+this.deviceID+"', "+this.tempUserID+", '"+this.accountID+"' , "+this.startLatitude+", "+this.startLongitude+", "+this.endLatitude+", "+this.endLongitude+", '"+this.startTime+"', '"+this.endTime+"', '"+this.number+"', "+on_off+", '"+this.tempPassword+"')";
    		//String query = "INSERT INTO TrackingNumber(deviceID, tempUserID, accountID, startLatitude, startLongitude, endLatitude, endLongitude, startTime, endTime, number, on_off, tempPassword)";
    		//query += " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    		String query = "INSERT INTO TrackingNumber("
    				+ "deviceID," //1
    				+ "tempUserID,"//2
    				//+ "accountID,"//3
    				+ "startTime,"//4
    				+ "endTime,"//5
    				+ "number,"//6
    				+ "on_off,"//7
    				+ "tempPassword,"//8
    				+ "startLatitude,"//9
    				+ "startLongitude,"//10
    				+ "endLatitude,"//11
    				+ "endLongitude,"
    				+ "created_at )"//12
    				
    				+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    		
    		PreparedStatement preparedSt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    		preparedSt.setString(1, this.deviceID);
    		preparedSt.setInt(2, this.tempUserID);
    		//preparedSt.setString(3, this.accountID);
    		//preparedSt.setDouble(4, this.startLatitude);
    		//preparedSt.setDouble(5, this.startLongitude);
    		//preparedSt.setDouble(6, this.endLatitude);
    		//preparedSt.setDouble(7, this.endLongitude);
    		//preparedSt.setTimestamp(8, this.startTime);
    		preparedSt.setTimestamp(3, this.startTime);
    		preparedSt.setTimestamp(4, Timestamp.valueOf("2014-07-28 17:37:39"));
    		//preparedSt.setTimestamp(5, this.endTime);
    		preparedSt.setString(5, this.number);
    		preparedSt.setInt(6, on_off);
    		preparedSt.setString(7, this.tempPassword);
    		preparedSt.setDouble(8, this.startLatitude);
    		preparedSt.setDouble(9, this.startLongitude);
    		preparedSt.setDouble(10, this.endLatitude);
    		preparedSt.setDouble(11, this.endLongitude);
    		
    		java.util.Date date= new java.util.Date();
    		preparedSt.setTimestamp(12, new Timestamp(date.getTime()));
    		
    		//preparedSt.setTimestamp(13, this.startTime);
    		//preparedSt.setTimestamp(14, this.endTime);
    		
    		preparedSt.executeUpdate();
    		
    		//st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
    		//ResultSet generatedKeys = st.getGeneratedKeys();
    		ResultSet generatedKeys = preparedSt.getGeneratedKeys();
    		if (null != generatedKeys && generatedKeys.next()) {
    		     this.tempUserID = generatedKeys.getInt(1);
    		}else{
    			throw new SQLException("SQLException: No se puede rescatar la generatedKey");
    		}
    		//while(rs.next()){
    			//String id = rs.getString("nombre");
    			//out.write("ID: "+id);
		return this.tempUserID;
	}
	
	public static TrackingNumber getTrackingNumberById(int tracking_number_id) throws Exception
			{
		DBConnection dbc = DBConnection.getDefaultConnection();
		TrackingNumber tn = new TrackingNumber();
		Statement st = dbc.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM TrackingNumber WHERE trackingNumberID = "+tracking_number_id);
		int contador = 0;
		while(rs.next()){
			tn.trackingNumberID = rs.getInt("trackingNumberID");
			tn.deviceID = rs.getString("deviceID");
			tn.tempUserID = rs.getInt("tempUserID");
			tn.startLatitude = rs.getDouble("startLatitude");
			tn.startLongitude = rs.getDouble("startLongitude");
			tn.endLatitude = rs.getDouble("endLatitude");
			tn.endLongitude = rs.getDouble("endLongitude");
			tn.startTime = rs.getTimestamp("startTime");
			tn.endTime = rs.getTimestamp("endTime");
			tn.on_off = rs.getInt("on_off") == 1 ? true : false;
			tn.tempPassword = rs.getString("tempPassword");
			tn.number = rs.getString("number");
			contador++;
		}
		if(contador == 0){
			throw new Exception("Exception: TrackingNumber GetTrackingNumberById. Usuario no encontrado.");
			
		}
		
		return tn;
	}
	
	public static TrackingNumber getTrackingNumberByTempUserIDAndTrackingNumberPassword(int temp_user_id, String tracking_password) throws Exception  
			 
			{
		
		DBConnection dbc = DBConnection.getDefaultConnection();
		TrackingNumber tn = new TrackingNumber();
		Statement st = dbc.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM TrackingNumber WHERE TempUserID = "+temp_user_id+" AND tempPassword = '"+tracking_password+"'");
		int contador = 0;
		while(rs.next()){
			tn.trackingNumberID = rs.getInt("trackingNumberID");
			tn.deviceID = rs.getString("deviceID");
			tn.tempUserID = rs.getInt("tempUserID");
			tn.startLatitude = rs.getDouble("startLatitude");
			tn.startLongitude = rs.getDouble("startLongitude");
			tn.endLatitude = rs.getDouble("endLatitude");
			tn.endLongitude = rs.getDouble("endLongitude");
			tn.startTime = rs.getTimestamp("startTime");
			tn.endTime = rs.getTimestamp("endTime");
			tn.on_off = rs.getInt("on_off") == 1 ? true : false;
			tn.tempPassword = rs.getString("tempPassword");
			tn.number = rs.getString("number");
			contador++;
		}
		if(contador == 0){
			throw new Exception("Exception: TrackingNumber getTrackingNumberByTempUserIDAndTrackingNumberPassword. Usuario no encontrado.");
		}
		
		return tn;
		
	}
	
	//public String getQueryInsert(){
	//	return "INSERT INTO TempUser(deviceID, tempUserID, startLatitude, startLongitude, endLatitude, endLongitude, startTime, endTime, number, on-off, tempPassword)"+ "VALUES('"+this.deviceID+"', "+this.tempUserID+", "+this.startLatitude+", "+this.startLongitude+", "+this.endLatitude+", "+this.endLongitude+", '"+this.startTime+"', '"+this.endTime+"', '"+this.number+"', "+on_off+", '"+this.tempPassword+"')";
	//}
	
	public static String generarNumber() throws SQLException{
		String numero_generado = TrackingNumber.randomString(12);
		DBConnection dbc = DBConnection.getDefaultConnection();
		Statement st = dbc.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM TrackingNumber WHERE number = '"+numero_generado+"'");
		int contador = 0;
		while(rs.next()){
			contador++;
		}
		if(contador == 0){
			return numero_generado;
		}else{
			return null;
		}
		
	}
	
	private static String randomString( int len ) 
	{
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder( len );
		for( int i = 0; i < len; i++ ) 
	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		return sb.toString();
	}
	
	public static ArrayList<TrackingNumber> getTrackingNumbersByTempUserId(int temp_user_id, boolean activo) throws SQLException{
		ArrayList<TrackingNumber> tracking_numbers = new ArrayList<TrackingNumber>();
		DBConnection dbc = DBConnection.getDefaultConnection();
		Statement st = dbc.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM TrackingNumber WHERE TempUserID = "+temp_user_id);
		int contador = 0;
		while(rs.next()){
			TrackingNumber tn = new TrackingNumber();
			tn.trackingNumberID = rs.getInt("trackingNumberID");
			tn.deviceID = rs.getString("deviceID");
			tn.tempUserID = rs.getInt("tempUserID");
			tn.startLatitude = rs.getDouble("startLatitude");
			tn.startLongitude = rs.getDouble("startLongitude");
			tn.endLatitude = rs.getDouble("endLongitude");
			tn.endLongitude = rs.getDouble("endLongitude");
			tn.startTime = rs.getTimestamp("startTime");
			tn.endTime = rs.getTimestamp("endTime");
			tn.number = rs.getString("number");
			tn.on_off = (rs.getInt("on_off") == 1) ? true:false;
			tn.tempPassword = rs.getString("tempPassword");
			
			tracking_numbers.add(tn);
			contador++;
		}
		
			return tracking_numbers;
		
	}
	
	public TrackingNumber update() throws SQLException{
		DBConnection dbc = DBConnection.getDefaultConnection();
		Connection conn = dbc.getConnection();
		String query = "UPDATE TrackingNumber SET "
				+ "deviceID = ?," //1
				//+ "accountID,"//
				+ "startTime = ?,"//2
				+ "endTime = ?,"//3
				+ "number = ?,"//4
				+ "on_off = ?,"//5
				+ "tempPassword = ?,"//6
				+ "startLatitude = ?,"//7
				+ "startLongitude = ?,"//8
				+ "endLatitude = ?,"//9
				+ "endLongitude = ?"//10
				+ " WHERE trackingNUmberID = ?"; //11
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, this.deviceID);
		ps.setTimestamp(2, this.startTime);
		ps.setTimestamp(3, this.endTime);
		ps.setString(4, this.number);
		ps.setInt(5, ((this.on_off == true) ? 1 : 0) );
		ps.setString(6, this.tempPassword);
		ps.setDouble(7, this.startLatitude);
		ps.setDouble(8, this.startLongitude);
		ps.setDouble(9, this.endLatitude);
		ps.setDouble(10, this.endLongitude);
		ps.setInt(11, this.trackingNumberID);
		ps.executeUpdate();
		return this;
	}
	
}
