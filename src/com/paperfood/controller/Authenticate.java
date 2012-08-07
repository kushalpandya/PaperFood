package com.paperfood.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.paperfood.DatabaseManager;
import com.paperfood.entity.PaperFoodUser;
import com.paperfood.security.MD5Hash;

/**
 * Servlet implementation class Authenticate
 */
@WebServlet(description = "Servlet to Authenticate user.", urlPatterns = { "/Authenticate" })
public class Authenticate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Authenticate() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(true);
		JSONObject resp = new JSONObject();
		MD5Hash md5;
		String status = "";
		try
		{
			md5 = new MD5Hash();
			String req_type = request.getParameter("type");
			if(req_type.equalsIgnoreCase("login")) //Request of Login
			{
				String loginEmail = request.getParameter("loginEmail");
				String loginPass = md5.getStringHash(request.getParameter("loginPass"));
				boolean loginRemember = request.getParameter("loginRemember").equalsIgnoreCase("true");
				
				DatabaseManager dm = new DatabaseManager();
				dm.open();
				PaperFoodUser user = new PaperFoodUser();
				user = (PaperFoodUser) dm.getLoggedUser(loginEmail, loginPass);
				dm.close();
				if(user != null) //Credentials are valid, create session.
				{
					session.setAttribute("paperfooduseremail", user.getEmail());
					if(loginRemember)
					{
						int time = 60 * 60 * 24 * 30;
	                    Cookie c = new Cookie("paperfood", user.getEmail());
	                    c.setMaxAge(time);
	                    response.addCookie(c);
					}
					status = "success";
				}
				else
					status = "invalid";
			}
			else if(req_type.equalsIgnoreCase("cookielogin")) //Request for Cookie-based Login.
			{
				String loginEmail = request.getParameter("loginEmail");
				session.setAttribute("paperfooduseremail", loginEmail);
				status = "success";
			}
			else if(req_type.equalsIgnoreCase("sessionlogin")) //Request for Session-based Login.
			{
				String useremail = (String) session.getAttribute("paperfooduseremail");
				if(useremail != null)
					status = "success";
			}
			else if(req_type.equalsIgnoreCase("logout")) //Request for Logout.
			{
				session.invalidate();
				Cookie[] c = request.getCookies();
				if(c != null)
				{
					for (int i = 0; i < c.length; i++)
		            {
		                Cookie curr = c[i];
		                String cnm = curr.getName();
		                if (cnm.equalsIgnoreCase("paperfood"))
		                {
		                    curr.setMaxAge(0);
		                    response.addCookie(curr);
		                }
		            }
				}
				status = "success";
			}
		}
		catch(CommunicationsException e)
		{
			status = "unavailable";
		}
		catch (Exception e)
		{
			status = "fail";
			e.printStackTrace();
		}
		
		try
		{
			resp.put("status", status);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		out.println(resp);
	}

}
