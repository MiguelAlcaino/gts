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

public class TempUserList
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
    
    public TempUserList()
    {
        this.setBaseURI(RequestProperties.TRACK_BASE_URI());
        this.setPageName(PAGE_TEMP_USER_LIST);
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
        return "Lista de usaurios temporales (Dentro de getNavigationDescription)";
    }

    public String getNavigationTab(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(ChangePassword.class);
        //return i18n.getString("ChangePassword.navTab","Change Password");
        return "Lista de usuarios temporales";
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
            	
            	try {
					ArrayList<TempUser> temp_users = TempUser.getTempUsers(reqState.getCurrentAccountID(), true);
					Iterator<TempUser> it_temp_users = temp_users.iterator();
					out.println("<span class='menuTitle'>Lista de usuarios temporales</span>\n");
	            	
	            	out.println("<hr />\n");
	            	out.println("<br />\n");
					out.println("<table style='width: 100%;' class='adminSelectTable_sortable' cellspacing='0' cellpadding='0' border='0'>");
					out.println("<thead>");
					out.println("<tr class='adminTableHeaderRow'>");
					out.println("<th class='adminTableHeaderCol_sort'>ID</th>");
					out.println("<th class='adminTableHeaderCol_sort'>Nombre</th>");
					out.println("<th class='adminTableHeaderCol_sort'>Apellido</th>");
					out.println("<th class='adminTableHeaderCol_sort'>Email</th>");
					out.println("<th class='adminTableHeaderCol_sort'>Acción</th>");
					out.println("</tr>");
					out.println("</thead>");
					out.println("<tbody>");
					int contador_tables = 0;
					while(it_temp_users.hasNext()){
						TempUser tu = it_temp_users.next();
						out.println("<tr>");
						out.println("<td class='adminTableBodyCol'>"+tu.getTempUserID()+"</td>");
						out.println("<td class='adminTableBodyCol'>"+tu.getNombre()+"</td>");
						out.println("<td class='adminTableBodyCol'>"+tu.getApellido()+"</td>");
						out.println("<td class='adminTableBodyCol'>"+tu.getEmail()+"</td>");
						out.println("<td class='adminTableBodyCol'><a href='javascript:void(0)' onclick='toggle_table("+contador_tables+")' class='toggle_tracking'>N° de tracking</a><br/><a href='Track?page="+PAGE_TEMP_USER_EDIT+"&id="+tu.getTempUserID()+"'>Editar</a><br/><a href='Track?page="+PAGE_TEMP_USER_DELETE+"&id="+tu.getTempUserID()+"'>Eliminar</a></td>");
						out.println("</tr>");
						
						ArrayList<TrackingNumber> tracking_numbers = TrackingNumber.getTrackingNumbersByTempUserId(tu.getTempUserID(), true);
						Iterator<TrackingNumber> it_tracking = tracking_numbers.iterator();
						
						out.println("<tr>");
						out.println("<td colspan=5>");
						if(tracking_numbers.size() == 0){
							out.println("<table class='table_trackings table_trackings_"+contador_tables+"' style='width: 100%;' class='adminSelectTable_sortable' cellspacing='0' cellpadding='0' border='0'>"
									+ "<tr>"
									+ "<td>Este usuario no tiene trackings asociados.</td>"
									+ "</tr>"
									+ "</table>");
						}else{
							out.println("<table class='table_trackings table_trackings_"+contador_tables+"' style='width: 100%;' class='adminSelectTable_sortable' cellspacing='0' cellpadding='0' border='0'>");
							out.println("<thead>");
							out.println("<tr class='adminTableHeaderRow'>");
							out.println("<th class='adminTableHeaderCol_sort'>N° Tracking</th>");
							out.println("<th class='adminTableHeaderCol_sort'>Activo</th>");
							out.println("<th class='adminTableHeaderCol_sort'>Dispositivo</th>");
							out.println("<th class='adminTableHeaderCol_sort'>Acción</th>");
							out.println("</tr>");
							out.println("</thead>");
							out.println("<tbody>");
							while(it_tracking.hasNext()){
								TrackingNumber tn = it_tracking.next();
								out.println("<tr>");
								out.println("<td class='adminTableBodyCol'>"+tn.getNumber()+"</td>");
								out.println("<td class='adminTableBodyCol'>"+((tn.isOn_off() == true) ? "Sí" : "No")+"</td>");
								out.println("<td class='adminTableBodyCol'>"+tn.getDeviceID()+"</td>");
								out.println("<td class='adminTableBodyCol'><a href='Track?page=tracking_number.edit&id="+tn.getTrackingNumberID()+"'>Editar</a></td>");
								out.println("</tr>");
								
							}
							
							out.println("</tbody>");
							out.println("</table>");
						}
						
						out.println("</td>");
						
						out.println("</tr>");
						out.println("<tr><td colspan=4>&nbsp;</td></tr>");
						contador_tables++;
					}
					
					out.println("</tbody>");
					out.println("</table>");
					out.println("<script>");
					out.println("$('.table_trackings').each(function(){$(this).hide();})");
					out.println("function toggle_table(num){"
							+ "$('.table_trackings_'+num).toggle('slow');"
							+ "console.log('hola');"
							+ "}");  

					out.println("</script>");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
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
