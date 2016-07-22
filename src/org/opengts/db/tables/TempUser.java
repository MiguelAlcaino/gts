package org.opengts.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.opengts.dbtools.DBConnection;
import org.opengts.dbtools.DBException;

import java.sql.Statement;
import java.util.ArrayList;

public class TempUser {
	
	private int tempUserID;
	private String accountID;
	private String email;
	private String nombre;
	private String apellido;
	private Account account;
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public TempUser(){
		this.tempUserID = 0;
	}
	
	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getApellido() {
		return apellido;
	}



	public void setApellido(String apellido) {
		this.apellido = apellido;
	}


	public int getTempUserID() {
		return tempUserID;
	}



	public void setTempUserID(int tempUserID) {
		this.tempUserID = tempUserID;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}
	
	public int insert() 
			throws SQLException 
			{
		DBConnection dbc = DBConnection.getDefaultConnection(); 
    	
		Statement st;
		
			st = dbc.createStatement();
			String query = "INSERT INTO TempUser(accountID, email, nombre, apellido) VALUES('"+this.accountID+"' ,'"+this.email+"', '"+this.nombre+"', '"+this.apellido+"')";
			st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet generatedKeys = st.getGeneratedKeys();
			if (null != generatedKeys && generatedKeys.next()) {
			     this.tempUserID = generatedKeys.getInt(1);
			}
		
		
		//while(rs.next()){
			//String id = rs.getString("nombre");
			//out.write("ID: "+id);
		return this.tempUserID;
	}
	
	public TempUser update() throws SQLException{
		DBConnection dbc = DBConnection.getDefaultConnection();
		Connection conn;
		
			conn = dbc.getConnection();
		
		String query = "UPDATE TempUser SET email = ?, nombre = ?, apellido = ? WHERE tempUserID = ?";
		PreparedStatement ps = conn.prepareStatement(query);
		
		ps.setString(1, this.getEmail());
		ps.setString(2, this.getNombre());
		ps.setString(3, this.getApellido());
		ps.setInt(4, this.tempUserID);
		
		ps.executeUpdate();
		
		return this;
	}
	
	public boolean sync(){
		
		DBConnection dbc = DBConnection.getDefaultConnection(); 
    	try{
    		Statement st = dbc.createStatement();
    		ResultSet rs = st.executeQuery("SELECT * FROM TempUser WHERE tempUserID = "+this.tempUserID);
    		while(rs.next()){
    			this.email = rs.getString("email");
    			this.nombre = rs.getString("nombre");
    			this.apellido = rs.getString("apellido");
    			this.tempUserID = rs.getInt("tempUserID");
    		}
    	}catch(Exception e){
    		//out.write(e.getMessage());
    	}
		return true;
	}
	
	/**
	 * Método estático que trae una instancia de la clase TempUser desde la base de datos según su ID
	 * @param tempUserID
	 * @return
	 * @throws SQLException 
	 * @throws DBException 
	 */
	public static TempUser getTempUserByID(int tempUserID) throws SQLException, DBException{
		DBConnection dbc = DBConnection.getDefaultConnection();
		TempUser tu = new TempUser();
    	try{
    		Statement st = dbc.createStatement();
    		ResultSet rs = st.executeQuery("SELECT * FROM TempUser WHERE tempUserID = "+tempUserID);
    		while(rs.next()){
    			tu.accountID = rs.getString("accountID");
    			tu.email = rs.getString("email");
    			tu.nombre = rs.getString("nombre");
    			tu.apellido = rs.getString("apellido");
    			tu.tempUserID = rs.getInt("tempUserID");
    			tu.account = Account.getAccount(tu.accountID);
    		}
    	}catch(DBException e){
    		throw new DBException("DBException: TempUser GetTempUserByID");
    		//out.write(e.getMessage());
    	}
		return tu;
	}
	
	public static TempUser getTempUserByEmail(String email) 
			throws 
			SQLException, 
			DBException,
			Exception
			{
		if(email == ""){
			throw new Exception("No se ingreso ningun email.");
		}else{
			DBConnection dbc = DBConnection.getDefaultConnection();
			TempUser tu = new TempUser();
	    		Statement st;
				try {
					st = dbc.createStatement();
					ResultSet rs = st.executeQuery("SELECT * FROM TempUser WHERE email = '"+email+"'");
		    		int contador = 0;
		    		while(rs.next()){
		    			tu.accountID = rs.getString("accountID");
		    			tu.email = rs.getString("email");
		    			tu.nombre = rs.getString("nombre");
		    			tu.apellido = rs.getString("apellido");
		    			tu.tempUserID = rs.getInt("tempUserID");
		    			tu.account = Account.getAccount(tu.accountID);
		    			contador++;
		    		}
		    		if(contador == 0){
		    			return null;
		    		}
				} catch (DBException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					throw new DBException("DBException: TempUser getTempUserByEmail ");
				}
	    		
			return tu;
		}
	}
	
	public static ArrayList<TempUser> getTempUsers(String accountID, boolean activos) 
			throws 
			SQLException
			{
		ArrayList<TempUser> temp_users = new ArrayList<TempUser>();
		DBConnection dbc = DBConnection.getDefaultConnection();
		Statement st;
		int contador = 0;
		
			st = dbc.createStatement();
		
		ResultSet rs = st.executeQuery("SELECT * FROM TempUser WHERE accountID = '"+accountID+"'");
		
		while(rs.next()){
			TempUser temp_user = new TempUser();
			temp_user.accountID = rs.getString("accountID");
			temp_user.email = rs.getString("email");
			temp_user.nombre = rs.getString("nombre");
			temp_user.apellido = rs.getString("apellido");
			temp_user.tempUserID = rs.getInt("tempUserID");
			temp_users.add(temp_user);
			contador++;
		}

		return temp_users;
		
		
	}
	
	public static boolean deleteTempUser(int id) throws SQLException{
		
		DBConnection dbc = DBConnection.getDefaultConnection();
		Statement st = dbc.createStatement();
		st.executeUpdate("DELETE FROM TempUser WHERE tempUserID = "+id);
		return true;
	}
	
	public static boolean deleteTempUser(TempUser temp_user) throws SQLException{
		
		DBConnection dbc = DBConnection.getDefaultConnection();
		Statement st = dbc.createStatement();
		st.executeQuery("DELETE FROM TempUser WHERE tempUserID = "+temp_user.tempUserID);
		return true;
	}
	
}