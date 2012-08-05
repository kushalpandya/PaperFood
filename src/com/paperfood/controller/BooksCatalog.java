package com.paperfood.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.paperfood.DatabaseManager;
import com.paperfood.entity.PaperFoodBook;

/**
 * Servlet implementation class BooksCatalog
 */
@WebServlet(description = "Servlet to Retrieve Books JSON based on given criteria", urlPatterns = { "/BooksCatalog" })
public class BooksCatalog extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BooksCatalog()
    {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		JSONObject resp = new JSONObject();
		String req_type = request.getParameter("type");
		String resp_type = "";
		if(req_type.equalsIgnoreCase("none"))
		{
			DatabaseManager dm = new DatabaseManager();
			try
			{
				dm.open();
				PaperFoodBook[] books = dm.getBooks(req_type);
				JSONArray books_arr = new JSONArray();
				for(int i=0;i<books.length; i++)
				{
					books_arr.put(books[i].toJSONObject());
				}
				resp_type = "catalog";
				resp.put("catalog", books_arr);
				
				dm.close();
			}
			catch (Exception e)
			{
				resp_type = "fail";
				e.printStackTrace();
			}
		}
		else// if(req_type.equalsIgnoreCase("search"))
		{
			//String query = request.getParameter("query");
			resp_type = "na";
		}
		
		try
		{
			resp.put("type", resp_type);
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
