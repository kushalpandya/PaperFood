/**
 * PaperFood v0.1
 * 
 * Author: Kushal Pandya < https://github.com/kushalpandya >
 * License: GPLv3.
 * 
 * Servlet implementation class BooksCatalog for Handling Book Requests.
 */
package com.paperfood.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.paperfood.DatabaseManager;
import com.paperfood.PaperFoodSearchIndex;
import com.paperfood.entity.PaperFoodBook;

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
		HttpSession session = request.getSession(true);
		JSONObject resp = new JSONObject();
		String req_type = request.getParameter("type");
		String resp_type = "";
		if(req_type.equalsIgnoreCase("none")) //Request for all books.
		{
			DatabaseManager dm = new DatabaseManager();
			try
			{
				dm.open();
				PaperFoodBook[] books = dm.getAllBooks(req_type);
				JSONArray books_arr = new JSONArray();
				JSONObject temp;
				for(int i=0;i<books.length; i++)
				{
					if((i+1)%4 == 0 && i != 0)
					{
						temp = new JSONObject();
						temp = books[i].toJSONObject();
						temp.put("break", true);
						books_arr.put(temp);
					}
					else
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
		else if(req_type.equalsIgnoreCase("isbn")) //Request for single book with given ISBN.
		{
			String isbn = request.getParameter("value");
			DatabaseManager dm = new DatabaseManager();
			try
			{
				dm.open();
				PaperFoodBook book = dm.getBook(isbn);
				JSONObject bookjson = book.toJSONObject();
				if(book.getQuantity() > 0)
					bookjson.put("inStock", true);
				
				String useremail = (String) session.getAttribute("paperfooduseremail");
				resp.put("activeLogin", useremail != null);
					
				resp_type = "book";
				resp.put("book", bookjson);
				
				dm.close();
			}
			catch (Exception e)
			{
				resp_type = "fail";
				e.printStackTrace();
			}
		}
		else if(req_type.equalsIgnoreCase("search")) //Request for books via Search.
		{
			String key = request.getParameter("searchKey");
			PaperFoodSearchIndex index = new PaperFoodSearchIndex();
			DatabaseManager dm = new DatabaseManager();
			try
			{
				index.contruct();
				Integer[] id_arr = index.getMatchingBookID(key);
				if(id_arr.length > 0)
				{
					dm.open();
					PaperFoodBook[] books = dm.getBooksByIDs(id_arr);
					JSONArray books_arr = new JSONArray();
					JSONObject temp;
					for(int i=0;i<books.length; i++)
					{
						if((i+1)%4 == 0 && i != 0)
						{
							temp = new JSONObject();
							temp = books[i].toJSONObject();
							temp.put("break", true);
							books_arr.put(temp);
						}
						else
							books_arr.put(books[i].toJSONObject());
					}
					resp.put("result", books_arr);
					resp_type = "result";
				}
				else
					resp_type = "empty";
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
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
