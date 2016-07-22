// ----------------------------------------------------------------------------
// Copyright 2007-2014, GeoTelematic Solutions, Inc.
// All rights reserved
// ----------------------------------------------------------------------------
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// ----------------------------------------------------------------------------
// Change History:
//  2007/01/25  Martin D. Flynn
//     -Initial release
//  2007/06/03  Martin D. Flynn
//     -Added I18N support
//  2007/06/13  Martin D. Flynn
//     -Added support for browsers with disabled cookies
//  2007/07/27  Martin D. Flynn
//     -Added 'getNavigationTab(...)'
//  2010/04/11  Martin D. Flynn
//     -Not displayed as an option if the logn "Password" field is hidden
// ----------------------------------------------------------------------------
package org.opengts.war.track.page;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.opengts.util.*;
import org.opengts.dbtools.*;
import org.opengts.db.*;
import org.opengts.db.tables.*;
import org.opengts.war.tools.*;
import org.opengts.war.track.*;

public class TempUserEdit
    extends WebPageAdaptor
    implements Constants
{
 
    // ------------------------------------------------------------------------

    public  static final String COMMAND_PWD_CHANGE  = "chg";
    
    public  static final String PARM_PWD_SUBMIT     = PARM_PASSWORD + "_subchg";
    public  static final String PARM_OLD_PASSWD     = PARM_PASSWORD + "_old";    
    public  static final String PARM_NEW1_PASSWD    = PARM_PASSWORD + "_nw1";    
    public  static final String PARM_NEW2_PASSWD    = PARM_PASSWORD + "_nw2";    

    // button types
    public  static final String PARM_BUTTON_CANCEL  = "b_cancel";

    // ------------------------------------------------------------------------
    // WebPage interface
    
    public TempUserEdit()
    {
        this.setBaseURI(RequestProperties.TRACK_BASE_URI());
        this.setPageName(PAGE_MIGUEL);
        this.setPageNavigation(new String[] { PAGE_LOGIN, PAGE_MENU_TOP });
        this.setLoginRequired(true);
    }

    // ------------------------------------------------------------------------

    public String getMenuName(RequestProperties reqState)
    {
        return "";
    }

    public String getMenuDescription(RequestProperties reqState, String parentMenuName)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(ChangePassword.class);
        //return super._getMenuDescription(reqState,i18n.getString("ChangePassword.menuDesc","Change your password"));
        return "Dentro de getMenuDesription";
    }
   
    public String getMenuHelp(RequestProperties reqState, String parentMenuName)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(ChangePassword.class);
        //return super._getMenuHelp(reqState,i18n.getString("ChangePassword.menuHelp","Change your login password"));
        return "Dentro de getMenuHelp";
    }

    // ------------------------------------------------------------------------

    public String getNavigationDescription(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(ChangePassword.class);
        //return super._getNavigationDescription(reqState,i18n.getString("ChangePassword.navDesc","Change Password"));
        return "N° tracking - Descripción navegación (Dentro de getNavigationDescription)";
    }

    public String getNavigationTab(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(ChangePassword.class);
        //return i18n.getString("ChangePassword.navTab","Change Password");
        return "Agregar usuario temporal";
    }

    // ------------------------------------------------------------------------

    public void writePage(
        final RequestProperties reqState,
        final String pageMsg)
        throws IOException
    {
        final PrivateLabel privLabel = reqState.getPrivateLabel();
        final I18N   i18n     = privLabel.getI18N(ChangePassword.class);
        final Locale locale   = reqState.getLocale();
        final String pageName = this.getPageName();
        String m = "";
        boolean error = false;

       
        
        /* JavaScript */
        HTMLOutput HTML_JS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                MenuBar.writeJavaScript(out, pageName, reqState);
                out.write("<script src=\"https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=places\"></script>\n");
                
            }
        };
        
        
        
        /* write frame */
        HTMLOutput HTML_CONTENT = new HTMLOutput(CommonServlet.CSS_CONTENT_FRAME, m) {
            public void write(PrintWriter out) throws IOException {
            	
            	HttpServletRequest request = reqState.getHttpServletRequest();
            	User user = reqState.getCurrentUser();
            	
            	
            	
            	if (reqState.getCommandName().equals("edit_temp_user")) {
            		String submit = AttributeTools.getRequestString(request, "submit_edit_temp_user", "");
            		 if (SubmitMatch(submit,"Guardar" )) {
            			 
            			 String email = AttributeTools.getRequestString(request, "temp_user_email", "");
            			 String nombre = AttributeTools.getRequestString(request, "temp_user_nombre", "");
            			 String apellido = AttributeTools.getRequestString(request, "temp_user_apellido", "");
            			 String device_id = AttributeTools.getRequestString(request, "device_id", "");
            			 String number = AttributeTools.getRequestString(request, "tracking_number_number", "");
            			 int on_off = AttributeTools.getRequestInt(request, "tracking_number_on_off", 0);
            			 String password = AttributeTools.getRequestString(request, "tracking_number_password", "");
            			 
            			 double startLatitude = AttributeTools.getRequestDouble(request, "tracking_number_start_latitude", 0);
            			 double startLongitude = AttributeTools.getRequestDouble(request, "tracking_number_start_longitude", 0);
            			 double endLatitude = AttributeTools.getRequestDouble(request, "tracking_number_end_latitude", 0);
            			 double endLongitude = AttributeTools.getRequestDouble(request, "tracking_number_end_longitude", 0);
            			 
            			 String startTime = AttributeTools.getRequestString(request, "tracking_number_start_time", "");
            			 String endTime = AttributeTools.getRequestString(request, "tracking_number_end_time", "");
            			 
            			 
            				 TempUser tu;
							try {
								tu = TempUser.getTempUserByEmail(email);
							
            				 if(tu == null){
            					 tu = new TempUser();
            					 tu.setEmail(email);
            					 tu.setNombre(nombre);
            					 tu.setApellido(apellido);
            					 tu.insert();
            					 //Track.writeMessageResponse(reqState, "Registro cargado correctamente");
                                 //return;
            				 }
            				 
            				 TrackingNumber tn = new TrackingNumber();
            				 
            				 tn.setDeviceID(device_id);
            				 tn.setTempUserID(tu.getTempUserID());
            				 //tn.setAccountID(user.getAccountID());
            				 //tn.setAccountID(reqState.getCurrentAccountID());
            				 //tn.setAccountID("tracking");
            				 tn.setTempPassword(password);
            				 
            				 tn.setStartLatitude(startLatitude);
            				 tn.setStartLongitude(startLongitude);
            				 tn.setEndLatitude(endLatitude);
            				 tn.setEndLongitude(endLongitude);
            				 
            				 
            				 tn.setStartTime(Timestamp.valueOf(startTime));
            				 tn.setEndTime(Timestamp.valueOf(endTime));
            				 
            				 tn.setNumber(number);
            				 
                			 if(on_off == 1){
                				 tn.setOn_off(true);
                			 }else{
                				 tn.setOn_off(false);
                			 }
            				 tn.insert();
            				 //out.write("Email ingersado: "+email+"<br />".);
            				 //out.write("Nombre: "+tu.getNombre());
            				 Track.writeMessageResponse(reqState, "Registro cargado correctamente");
                             return;
                			 //out.write(tn.getQueryInsert()+"<br />");
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								Track.writeMessageResponse(reqState, "SQL ERROR: "+e.getMessage());
							} catch (DBException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e){
								e.printStackTrace();
							}
            			 
            		 }
            		
            		
            		
            		
            	}else{
            		
            	}
            	
            	//cl.contact.db.DbConnection c_connection = new cl.contact.db.DbConnection();
            	/*
            	DBConnection dbc = DBConnection.getDefaultConnection(); 
            	try{
            		Statement st = dbc.createStatement();
            		ResultSet rs = st.executeQuery("SELECT * FROM Miguel");
            		while(rs.next()){
            			String id = rs.getString("nombre");
            			out.write("ID: "+id);
            		}
            	}catch(Exception e){
            		out.write(e.getMessage());
            	}
            	*/
            	
            	//TempUser tu;
            	//tu = new TempUser();
            	//TrackingNumber tr = new TrackingNumber(4);
            	//tu.setTempUserID(2);
            	//tu.setEmail("sopapiglobo");
            	//tu.setNombre("miguel");
            	//tu.setApellido("alcaino");
            	try {
            		 
            	Account acc = Account.getAccount(reqState.getCurrentAccountID());
            	OrderedSet<String> devices = Device.getDeviceIDsForAccount(acc.getAccountID(), user, false);
            	int temp_user_id = AttributeTools.getRequestInt(request, "id", 0);
            	//AttributeTools.clearSessionAttributes(request);
            	out.println("<span class='menuTitle'>Registro de n° de tracking</span>\n");
            	
            	out.println("<hr />\n");
            	out.println("<form method='post' action='./Track?page="+PAGE_TEMP_USER_EDIT+"&page_cmd=edit_temp_user'>");
            		out.println("<table>");
            			out.println("<tbody>");
            				out.println("<tr>");
            					out.println("<td>");
            						out.println("Nombre Usuario:");
            					out.println("</td>");
            					out.println("<td>");
            						out.println("<input required='required' type='text' name='temp_user_nombre' />");
            					out.println("</td>");
    						out.println("</tr>");
    						out.println("<tr>");
	        					out.println("<td>");
	        						out.println("Apellido Usuario:");
	        					out.println("</td>");
	        					out.println("<td>");
	        						out.println("<input required='required' type='text' name='temp_user_apellido' />");
	        					out.println("</td>");
							out.println("</tr>");
							out.println("<tr>");
	        					out.println("<td>");
	        						out.println("Dispositivo:");
	        					out.println("</td>");
	        					out.println("<td>");
	        						out.println("<select name='device_id' >");
	        						Iterator<String> it= devices.iterator();
	        	            		while(it.hasNext()){
	        	            			String next = it.next();
	        	            			out.println("<option value='"+next+"'>"+next+"</option>");
	        	            		}
	        						out.println("</select>");
	        						
	        					out.println("</td>");
							out.println("</tr>");
							out.println("<tr>");
	        					out.println("<td>");
	        						out.println("Email Usuario:");
	        					out.println("</td>");
	        					out.println("<td>");
	        						out.println("<input required='required' type='email' name='temp_user_email' />");
	        					out.println("</td>");
        					out.println("</tr>");
        					
        					//Generación de number 
        					String number_generado = null;
        					while(number_generado == null){
        						number_generado = TrackingNumber.generarNumber();
        					}
        					//Fin de la generación
        					out.println("<tr>");
	        					out.println("<td>");
	        						out.println("Numero tracking:");
	        					out.println("</td>");
	        					out.println("<td>");
	        						out.println("<input required='required' type='text' name='tracking_number_number' value='"+number_generado+"' /> ");
	        					out.println("</td>");
							out.println("</tr>");
							out.println("<tr>");
							out.println("<tr>");
	        					out.println("<td>");
	        						out.println("Estado:");
	        					out.println("</td>");
	        					out.println("<td>");
	        						out.println("<select name='tracking_number_on_off' >");
	        							out.println("<option value ='1'>Activado</option>");
	        							out.println("<option value ='0'>Desactivado</option>");
	        						out.println("</select>");
	        					out.println("</td>");
							out.println("</tr>");
							out.println("<tr>");
	        					out.println("<td>");
	        						out.println("Password:");
	        					out.println("</td>");
	        					out.println("<td>");
	        						out.println("<input id='tracking_number_password' required='required' type='password' name='tracking_number_password' /><a href='#' id='generar_password_button'>Generar password</a> <div id='password_generada'></div>");
	        					out.println("</td>");
							out.println("</tr>");
							out.println("<tr>");
	        					out.println("<td>");
	        						out.println("Punto de inicio:");
	        					out.println("</td>");
	        					out.println("<td>");
	        						out.println("<input id='start_coordinate' value='' readonly='readonly' class=\"tooltip\"  type='text' name='start_coordinate' />");
	        						out.println("<input class='lat' type='hidden' value='' name='tracking_number_start_latitude' />");
	        						out.println("<input class='lng' type='hidden' value='' name='tracking_number_start_longitude' />");
	        					out.println("</td>");
							out.println("</tr>");
							/*
							out.println("<tr>");
	        					out.println("<td>");
	        						out.println("Longitud inicio:");
	        					out.println("</td>");
	        					out.println("<td>");
	        						//out.println("<input type='text' name='tracking_number_start_longitude' />");
	        					out.println("</td>");
							out.println("</tr>");
							*/
							out.println("<tr>");
	        					out.println("<td>");
	        						out.println("Punto final:");
	        					out.println("</td>");
	        					out.println("<td>");
	        						out.println("<input id='end_coordinate' value='' readonly='readonly' class=\"tooltip\"  type='text' name='end_coordinate' />");
	        						out.println("<input class='lat' type='hidden' value='' name='tracking_number_end_latitude' />");
	        						out.println("<input class='lng' type='hidden' value='' name='tracking_number_end_longitude' />");
	        					out.println("</td>");
							out.println("</tr>");
							/*
							out.println("<tr>");
	        					out.println("<td>");
	        						out.println("Longitud final:");
	        					out.println("</td>");
	        					out.println("<td>");
	        						//out.println("<input type='text' name='tracking_number_end_longitude' />");
	        					out.println("</td>");
							out.println("</tr>");
							*/
							
							Calendar cal = Calendar.getInstance();
					    	cal.getTime();
					    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    	//System.out.println( );
							out.println("<tr>");
	        					out.println("<td>");
	        						out.println("Fecha y hora de inicio:");
	        					out.println("</td>");
	        					out.println("<td>");
	        						out.println("<input type='text' id='tracking_number_start_time' name='tracking_number_start_time' readonly='readonly' value='"+sdf.format(cal.getTime()) +"' />");
	        					out.println("</td>");
							out.println("</tr>");
							out.println("<tr>");
	        					out.println("<td>");
	        						out.println("Fecha y hora final:");
	        					out.println("</td>");
	        					out.println("<td>");
	        						out.println("<input type='text' id='tracking_number_end_time' name='tracking_number_end_time' readonly='readonly' />");
	        					out.println("</td>");
							out.println("</tr>");
							out.println("<tr>");
	        					out.println("<td>");
	        						out.println("&nbsp;");
	        					out.println("</td>");
	        					out.println("<td>");
	        						out.println("<input type='submit' value='Guardar' name='submit_edit_temp_user' />");
	        					out.println("</td>");
							out.println("</tr>");
							out.println("</tbody>");
					out.println("<table>");
            	out.println("</form>");
            	out.println("<script>");
            	out.println(" $('.tooltip').tooltipster({\n"
            			+ "interactive: true,\n"
            			+ "position: 'right',\n"
            			+ "functionReady: function(){\n"
            			+ "initialize($(this));"
            			+ "},\n"
            			+ "content: $('<div style=\"width:860px; height:380px;\" id=\"canvas\"></div>')\n"
            			+ "});\n");
            	/*
            	out.println(" $('#end_coordinate').tooltipster({\n"
            			+ "interactive: true,\n"
            			+ "functionReady: function(){\n"
            			+ "initialize($(this));"
            			+ "},\n"
            			+ "content: $('<div style=\"width:860px; height:400px;\" id=\"canvas\">hola</div>')\n"
            			+ "});\n");
            			*/
            	out.println("	var map;\n"
            			//+ "var markers = [];\n"
            			+ "		var initial_position = new google.maps.LatLng(-33.02438898007206, -71.55208826065063);\n"
            			+ "	function initialize(element) {\n"
            			+ "		var mapOptions = {\n"
            			+ "			zoom: 16,\n"
            			+ "			center: initial_position,\n"
            			+ "			mapTypeId: google.maps.MapTypeId.ROADMAP\n"
            			+ "		};\n"
            			+ "		map = new google.maps.Map(document.getElementById('canvas'),\n"
            			+ "		mapOptions);\n"
            			+ "\n"
            			+ "	if(element.val() == ''){\n"
            			+ "		marker = new google.maps.Marker({\n"
            			+ "		position: initial_position,\n"
            			+ "		map: map\n"
            			+ "		});\n"
            			+ "	}else{\n"
            			+ "		lat = element.next('.lat').val();\n"
            			+ "		lng = element.next('.lat').next().val();\n"
            			+ "		position_new = new google.maps.LatLng(lat,lng);\n"
            			+ "		marker = new google.maps.Marker({\n"
            			+ "		position: position_new,\n"
            			+ "		map: map\n"
            			+ "		});\n"
            			+ "		marker.setPosition(position_new);\n"
            			+ "}\n"
            			
            			
            			
            			+ ""
            			+ "	google.maps.event.addListener(map, 'click', function(e) {\n"
            			+ "	//placeMarker(e.latLng, map);\n"
            			//+ "$('#start_coordinate').val(e.latLng)\n"
            			+ "	element.val(e.latLng.lat()+', '+e.latLng.lng());\n"
            			+ "	element.next('.lat').val(e.latLng.lat());\n"
            			+ "	element.next('.lat').next().val(e.latLng.lng());\n"
            			+ "	addMarker(e.latLng)\n"
            			+ "	});\n"
            			
            			

            			+ "}\n");
            			
            	out.println("function addMarker(location) {\n"
            			+ "marker.setPosition(location)\n"
            			+ "}\n"    
            			+ ""
            			
            			//Incialización de datetimepickers
            			+ "$('#tracking_number_start_time').datetimepicker({\n"
            			+ "format: 'Y-m-d H:i:s',"
            			+ "minDate: '0'"
            			+ "});\n"
            			+ ""
            			+ "$('#tracking_number_end_time').datetimepicker({\n"
            			+ "format: 'Y-m-d H:i:s',"
            			+ "minDate: '0'"
            			+ "});\n"
            			+ ""
            			
            			//Incialización de generador de password
            			+ " $('#generar_password_button').pGenerator({"
            			+ "'bind': 'click',"
            			+ "'passwordElement': '#tracking_number_password',"
            			+ "'displayElement': '#password_generada',"
            			+ "'passwordLength': 12,"
            			+ "'uppercase': true,"
            			+ "'lowercase': true,"
            			+ "'numbers':   true,"
            			+ "'specialChars': false,"
            			+ "});"
        
            			+ "");
	

            	out.println("</script>\n");
            	out.println("<hr />\n");
					//out.write ("IDDDDDDDDDDDDDDDDDDD:"+tu.insert());
            		
            		//out.write(acc.getDescription());
            		
            		/*
            		String[] at = acc.getDeviceTitles(locale);
            		for(String hola : at){
            			out.write("TITLE DEVICE: "+hola);
            		}
            		*/
            		
            		
				} catch (Exception e) {
					out.write(e.getMessage());
				} // insertado en la base de datos
            	
            	//tu.sync();
            	
                //out.write("<div>ID del nuevo usuario: "+tu.getTempUserID()+"</div>");
            }
        };

        /* onload alert message? */
        String onload = null;
        if (error && !StringTools.isBlank(m)) {
            // assumed that 'm' does not contain double-quotes
            onload = "javascript:alert(&quot;" + m + "&quot;);";
        }

        /* write frame */
        CommonServlet.writePageFrame(
            reqState,
            onload,null,                // onLoad/onUnload
            HTMLOutput.NOOP,            // Style sheets
            HTML_JS,                    // JavaScript
            null,                       // Navigation
            HTML_CONTENT);              // Content

    }

}
