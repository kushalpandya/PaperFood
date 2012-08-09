/**
 * PaperFood v0.1
 * 
 * Author: Kushal Pandya < https://github.com/kushalpandya >
 * License: GPLv3.
 * 
 * Servlet implementation class RegisterUser for Handling Registration Process. 
 */
package com.paperfood.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.paperfood.DatabaseManager;
import com.paperfood.entity.PaperFoodUser;
import com.paperfood.security.MD5Hash;

@WebServlet("/RegisterUser")
public class RegisterUser extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterUser()
	{
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		JSONObject resp = new JSONObject();
		MD5Hash md5;
		String status = "";
		try
		{
			md5 = new MD5Hash();
			
			PaperFoodUser user = new PaperFoodUser();
			user.setFirstName(request.getParameter("txtFname"));
			user.setLastName(request.getParameter("txtLname"));
			user.setEmail(request.getParameter("txtEmail"));
			user.setPassword(md5.getStringHash(request.getParameter("txtPass")));
			
			DatabaseManager dm = new DatabaseManager();
			dm.open();
			if(dm.isUserMailExist(user.getEmail()))
				status = "invalid";
			else
			{
				dm.insertUser(user);
				status = "success";
			}
			dm.close();
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
