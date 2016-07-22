package org.opengts.war.track;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;

import org.opengts.db.tables.Device;
import org.opengts.db.tables.EventData;
import org.opengts.db.tables.TempUser;
import org.opengts.db.tables.TrackingNumber;
import org.opengts.dbtools.DBException;
import org.opengts.war.tools.CommonServlet;

public class TempUserController extends Controller{
	private String pageName;
	private String path_template = "/jsp/prueba.jsp";
	private String HTML_CONTENT = "";
	private String HTML_JAVASCRIPT = "";
	private String HTML_CSS = "";
	public static Map<String, String> paginas =  new HashMap<String, String>() {
        {
        	put("temp_user_login", "tempUserLogin");
        	put("temp_user_login_enter", "tempUserLoginEnter");
        	put("temp_user_tracking", "tempUserTracking");
        }
        ;
    };
	
	
	public TempUserController(HttpServletRequest request, HttpServletResponse response){
		this.pageName = request.getParameter("page");
		this.request = request;
		this.response = response;
		this.session = request.getSession(true);
	}
	
	public void executePage(){
		try {
			String name_method = (String) TempUserController.paginas.get(this.pageName) + "Action";
			
			
			
			Method method = this.getClass().getMethod(name_method, HttpServletRequest.class);
			method.invoke(this, request);
			
			request.setAttribute("javascript", this.HTML_JAVASCRIPT);
			request.setAttribute("css", this.HTML_CSS);
			request.setAttribute("content", this.HTML_CONTENT);
			
			request.getRequestDispatcher(this.path_template).include(this.request, this.response);
			//Method meth = getClass().getDeclaredMethod(name_method);
			//meth.invoke(null);
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void tempUserLoginAction(HttpServletRequest request){
		
		request.setAttribute("miguel",this.pageName);
		this.HTML_CONTENT = ""
				+ "<form action='./Track?page=temp_user_login_enter' method='post'>"
				+ "<table  cellpadding='0' cellspacing='0' border='0'>"
				+ "<tbody>"
				+ "<tr>"
				+ "<td class='accountLoginFieldLabel'>Email</td>"
				+ "<td class='accountLoginFieldValue'><input id='temp_user_email' class='"+CommonServlet.CSS_TEXT_INPUT+"' type='email' name='temp_user_email' size='24' maxlength='32' /></td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td class='accountLoginFieldLabel'>Password</td>"
				+ "<td class='accountLoginFieldValue'><input class='"+CommonServlet.CSS_TEXT_INPUT+"' type='password' name='temp_user_password' value='' size='24' maxlength='32' /></td>"
				+ "</tr>"
				+ "<tr><td colspan='2'> <input type='submit' value='Ingresar' /></td></tr>"
				+ "</tbody>"
				+ "</table>"
				+ "</form>";
		//request.getRequestDispatcher(this.path_template).include(this.request, this.response);
	}
	
	public void tempUserLoginEnterAction(HttpServletRequest request){
		try{
			String temp_user_email = request.getParameter("temp_user_email");
			String tracking_number_temp_password = request.getParameter("temp_user_password");
    		TempUser tu = TempUser.getTempUserByEmail(temp_user_email);
    		if(tu == null){
    			//Track.writeMessageResponse(reqState, "Usted no se enceuntra dentro de la base de datos");
    			//this.redirect("temp_user_login", response);
                //return;
    			throw new Exception("Usuario no encontrado en la Base de datos");
    		}else{
    			TrackingNumber tn = TrackingNumber.getTrackingNumberByTempUserIDAndTrackingNumberPassword(tu.getTempUserID(), tracking_number_temp_password);
    			if(tn == null){
    				//Track.writeMessageResponse(reqState, "Usted no tiene ningún tracking number asociado");
    				//return;
    				throw new Exception("Usted no tiene ningún tracking asociado "+tu.getTempUserID()+" "+tracking_number_temp_password);
    			}else{
    				//out.println("<div>Estoy dentro</div>");
    				//this.HTML_CONTENT += "<div>Estoy Dentro!!</div>";
    				this.session.setAttribute("isLogged", true);
    				this.session.setAttribute("temp_user", tu);
    				this.session.setAttribute("tracking_number", tn);
    				this.redirect("temp_user_tracking", response);
    			}
    			
    		}
    		}catch(Exception e){
    			//out.println("ERROR");
    			this.HTML_CONTENT = e.getMessage();
    		}
	}
	
	public void tempUserTrackingAction(HttpServletRequest request){
		TempUser tu = (TempUser) this.session.getAttribute("temp_user");
		TrackingNumber tn = (TrackingNumber) this.session.getAttribute("tracking_number");
		this.HTML_CONTENT += "startTime: "+tn.getStartTime().getTime();
		this.HTML_CONTENT += "<br />endTime: "+tn.getEndTime().getTime()+"<br />";
		
		try {
			//Device device = Device.getDevice(tu.getAccount(), tn.getDeviceID());
			//1406916663
			EventData[] events = EventData.getRangeEvents(tu.getAccountID(), tn.getDeviceID(), tn.getStartTime().getTime()/1000, tn.getEndTime().getTime()/1000, null, true, null, 0, true, null);
			
			//EventData[] events = EventData.getRangeEvents(tu.getAccountID(), tn.getDeviceID(), 1404234651, 1406916663, null, true, null, 0, true, null);
			if(events == null){
				throw new Exception("No se han encontrado dispositivos gps");
			}else{
				this.HTML_JAVASCRIPT += "<script src=\"https://maps.googleapis.com/maps/api/js?v=3.exp\"></script>\n";
				this.HTML_JAVASCRIPT += "<script>\n"
						+ "function initialize() {\n"
						+ "var mapOptions = {\n"
						+ "zoom: 14,\n"
						+ "center: new google.maps.LatLng("+events[0].getLatitude()+", "+events[0].getLongitude()+"),\n"
						+ "mapTypeId: google.maps.MapTypeId.TERRAIN\n"
						+ "};"
						+ "var map = new google.maps.Map(document.getElementById('map-canvas'),\n"
						+ "mapOptions);\n"
						+ "var marker = new google.maps.Marker({\n"
						+ "position: new google.maps.LatLng("+events[0].getLatitude()+", "+events[0].getLongitude()+"),\n"
						+ "map: map,\n"
						+ "title: 'Punto de partida'\n"
						+ "});";
				
				this.HTML_JAVASCRIPT += "var flightPlanCoordinates = [\n";
				
				for (int i = 0 ; i < events.length ; i++) {
		            //System.out.println("Elemento en indice " + i + ": " + arreglo[i]);
					this.HTML_JAVASCRIPT += "new google.maps.LatLng("+events[i].getLatitude()+", "+events[i].getLongitude()+"),\n"; 
		        }
				/*
				+ "new google.maps.LatLng(37.772323, -122.214897),"
				+ "new google.maps.LatLng(21.291982, -157.821856),"
				+ "new google.maps.LatLng(-18.142599, 178.431),"
				+ "new google.maps.LatLng(-27.46758, 153.027892)"
				*/
				this.HTML_JAVASCRIPT += "];\n";
				
						
				this.HTML_JAVASCRIPT += "var flightPath = new google.maps.Polyline({\n"
						+ "path: flightPlanCoordinates,\n"
						+ "geodesic: true,\n"
						+ "strokeColor: '#FF0000',\n"
						+ "strokeOpacity: 1.0,\n"
						+ "strokeWeight: 2\n"
						+ "});\n"
						+ "flightPath.setMap(map);\n"
						+ "}\n"
						+ "google.maps.event.addDomListener(window, 'load', initialize);"
						+ "</script>";
				
				this.HTML_CSS = "<style type=\"text/css\"> \n"
						+ "#map-canvas {\n"
						+ "height: 400px;\n"
						+ "width: 860px;\n"
						+ "margin: 0px;\n"
						+ "padding: 0px\n"
						+ "}\n"
						+ "</style>";
	      		

				this.HTML_CONTENT += "HOLAAAAAAA";
				this.HTML_CONTENT += "<div id=\"map-canvas\"></div>";
				
				
			
			}
		} catch (DBException e) {
			// TODO Auto-generated catch block
			this.HTML_CONTENT = "Error: "+e.getMessage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.HTML_CONTENT = "Error: "+e.getMessage();
		}	
			
		
		
	}
	
}
