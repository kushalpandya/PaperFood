/**
 * PaperFood v0.1
 * 
 * Author: Kushal Pandya < https://github.com/kushalpandya >
 * License: GPLv3.
 * 
 * Entity class PaperFoodOrder to represent an Order by User in PaperFood.
 */
package com.paperfood.entity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.paperfood.DatabaseManager;

public class PaperFoodOrder
{
	private int id;
	private PaperFoodUser user;
	private HashMap<Integer, Integer> cart;
	private int itemcount;
	
	public PaperFoodOrder()
	{
		this.cart = new HashMap<Integer, Integer>();
		this.itemcount = 0;
	}
	
	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	/**
	 * @return the user
	 */
	public PaperFoodUser getUser()
	{
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(PaperFoodUser user)
	{
		this.user = user;
	}
	
	/**
	 * Add a book to cart.
	 * @param book PaperFoodBook to be added to the cart.
	 */
	public void addBook(PaperFoodBook book)
	{
		if(cart.containsKey(book.getId()))
		{
			int qty = cart.get(book.getId());
			cart.put(book.getId(), qty+1);
		}
		else
			cart.put(book.getId(), 1);
		
		this.itemcount++;
	}
	
	/**
	 * Get cart having all the books.
	 * @return HashMap for cart having books.
	 */
	public HashMap<Integer, Integer> getBooks()
	{
		return cart;
	}
	
	/**
	 * Get total number of items in the cart.
	 * @return int total items in the cart.
	 */
	public int getItemCount()
	{
		return this.itemcount;
	}
	
	/**
	 * Gets total payable amount for this cart.
	 * @return float total amount.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	public float getAmount() throws ClassNotFoundException, SQLException
	{
		float total = 0;
		int currKey;
		Set cartItems = cart.keySet();
		Iterator itemsItr = cartItems.iterator();
		DatabaseManager dm = new DatabaseManager();
		dm.open();
		
		//Insert values in order_details
		while(itemsItr.hasNext())
		{
			currKey = (Integer) itemsItr.next();
			total += dm.getBookPrice(currKey);
		}
		
		return total;
	}
}
