/**
 * 
 */
package com.paperfood.entity;

import java.util.HashMap;

/**
 * @author Kushal Pandya <kushal.pandya04@gmail.com>
 *
 */
public class PaperFoodOrder
{
	private int id;
	private PaperFoodUser user;
	private HashMap<Integer, Integer> cart;
	
	public PaperFoodOrder()
	{
		this.cart = new HashMap<Integer, Integer>();
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
	
	public void addBook(PaperFoodBook book)
	{
		if(cart.containsKey(book.getId()))
		{
			int qty = cart.get(book.getId());
			cart.put(book.getId(), qty+1);
		}
		else
			cart.put(book.getId(), 1);
	}
	
	public HashMap<Integer, Integer> getBooks()
	{
		return cart;
	}
}
