/**
 * 
 */
package com.paperfood.entity;

/**
 * @author Kushal Pandya <kushal.pandya04@gmail.com>
 *
 */
public class PaperFoodUser
{
	private int id;
	private String firstname;
	private String lastname;
	private String email;
	private String password;
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
	 * @return the firstname
	 */
	public String getFirstName()
	{
		return firstname;
	}
	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstName(String firstname)
	{
		this.firstname = firstname;
	}
	/**
	 * @return the lastname
	 */
	public String getLastName()
	{
		return lastname;
	}
	/**
	 * @param lastname the lastname to set
	 */
	public void setLastName(String lastname)
	{
		this.lastname = lastname;
	}
	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}
	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}
}
