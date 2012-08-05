/**
 * 
 */
package com.paperfood.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Kushal Pandya <kushal.pandya04@gmail.com>
 *
 */
public class PaperFoodBook
{
	private int id;
	private String isbn;
	private String title;
	private String author;
	private String genre;
	private int quantity;
	private float price;
	
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
	 * @return the isbn
	 */
	public String getISBN()
	{
		return isbn;
	}
	/**
	 * @param isbn the isbn to set
	 */
	public void setISBN(String isbn)
	{
		this.isbn = isbn;
	}
	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	/**
	 * @return the author
	 */
	public String getAuthor()
	{
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author)
	{
		this.author = author;
	}
	/**
	 * @return the genre
	 */
	public String getGenre()
	{
		return genre;
	}
	/**
	 * @param genre the genre to set
	 */
	public void setGenre(String genre)
	{
		this.genre = genre;
	}
	/**
	 * @return the quantity
	 */
	public int getQuantity()
	{
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	/**
	 * @return the price
	 */
	public float getPrice()
	{
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(float price)
	{
		this.price = price;
	}
	
	/**
	 * Gets JSONObject representing this Object.
	 * @return JSONObject
	 * @throws JSONException
	 */
	public JSONObject toJSONObject() throws JSONException
	{
		JSONObject obj = new JSONObject();
		obj.put("id", this.id);
		obj.put("isbn", this.isbn);
		obj.put("title", this.title);
		obj.put("author", this.author);
		obj.put("genre", this.genre);
		obj.put("qty", this.quantity);
		obj.put("price", this.price);
		
		return obj;
	}
}
