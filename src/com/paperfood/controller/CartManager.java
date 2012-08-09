/**
 * PaperFood v0.1
 * 
 * Author: Kushal Pandya < https://github.com/kushalpandya >
 * License: GPLv3.
 * 
 * Servlet implementation class CartManager for Handling Shopping Cart.
 */
package com.paperfood.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.paperfood.DatabaseManager;
import com.paperfood.entity.PaperFoodBook;
import com.paperfood.entity.PaperFoodOrder;
import com.paperfood.entity.PaperFoodUser;

@WebServlet(description = "Servlet to manage user's cart.", urlPatterns = { "/CartManager" })
public class CartManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PaperFoodOrder order;
	private boolean isUserSet;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CartManager() {
        super();
        order = new PaperFoodOrder();
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
		String req_type = request.getParameter("type");
		String status = "";
		
		if(!isUserSet)
		{
			String useremail = (String) session.getAttribute("paperfooduseremail");
			if(useremail != null)
			{
				try
				{
					DatabaseManager dm = new DatabaseManager();
					dm.open();
					PaperFoodUser user = (PaperFoodUser) dm.getLoggedUserByEmail(useremail);
					dm.close();
					order.setUser(user);
					isUserSet = true;
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		try
		{
			if(req_type.equalsIgnoreCase("addtocart"))
			{
				DatabaseManager dm = new DatabaseManager();
				dm.open();
				PaperFoodBook book = dm.getBook(request.getParameter("isbn"));
				dm.close();
				order.addBook(book);
				
				resp.put("count", order.getItemCount());
				status = "success";
			}
			else if(req_type.equalsIgnoreCase("lastcartinfo"))
			{
				String useremail = (String) session.getAttribute("paperfooduseremail");
				if(useremail != null && order.getItemCount() > 0)
				{
					resp.put("count", order.getItemCount());
					status = "success";
				}
			}
			else if(req_type.equalsIgnoreCase("cartinfo"))
			{
				JSONObject cart_info = new JSONObject();
				cart_info.put("totalItems", order.getItemCount());
				cart_info.put("totalAmount", order.getAmount());
				resp.put("cartinfo", cart_info);
				status = "success";
			}
			else if(req_type.equalsIgnoreCase("checkout"))
			{
				if(order.getItemCount() > 0)
				{
					DatabaseManager dm = new DatabaseManager();
					dm.open();
					int o_id = dm.insertOrder(order);
					order.setId(o_id);
					dm.close();
					status = "success";
				}
				else
					status = "invalid";
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
