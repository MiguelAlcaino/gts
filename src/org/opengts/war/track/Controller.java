package org.opengts.war.track;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Controller {
	public HttpSession session;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	public void redirect(String page, HttpServletResponse response){
		try {
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<meta HTTP-EQUIV=\"REFRESH\" CONTENT=\"0; URL=./Track?page="+page+"\"></meta>");
			out.println("</html>");
			return ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
