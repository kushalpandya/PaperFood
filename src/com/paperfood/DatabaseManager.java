/**
 * 
 */
package com.paperfood;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

import com.paperfood.entity.PaperFoodBook;
import com.paperfood.entity.PaperFoodOrder;
import com.paperfood.entity.PaperFoodUser;

/**
 * @author Kushal Pandya <kushal.pandya04@gmail.com>
 *
 */
public class DatabaseManager
{
	private String DRIVER;
	private String DATABASE;
	
	private Connection con;
	
	
	public DatabaseManager()
	{
		ResourceBundle rb = ResourceBundle.getBundle("com.paperfood.Config");
		DRIVER = rb.getString("DatabaseVendorDriver");
		DATABASE = rb.getString("DatabaseURL");
	}
	
	/**
	 * Opens database connection with PaperFood database.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void open() throws ClassNotFoundException, SQLException
	{
		Class.forName(DRIVER);
		con = DriverManager.getConnection(DATABASE);
	}
	
	/**
	 * Inserts new user in PaperFood Database.
	 * @param user PaperFoodUser
	 * @throws SQLException
	 */
	public void insertUser(PaperFoodUser user) throws SQLException
	{
		PreparedStatement pst = con.prepareStatement("INSERT INTO users(fname, lname, email, password) VALUES(?,?,?,?)");
		pst.setString(1, user.getFirstName());
		pst.setString(2, user.getLastName());
		pst.setString(3, user.getEmail());
		pst.setString(4, user.getPassword());
		pst.executeUpdate();
		pst.close();
	}
	
	/**
	 * Inserts new book in PaperFood Database.
	 * @param book PaperFoodBook
	 * @throws SQLException
	 */
	public void insertBook(PaperFoodBook book) throws SQLException
	{
		PreparedStatement pst = con.prepareStatement("INSERT INTO books(isbn, title, author, genre, qty, price) VALUES(?,?,?,?,?,?)");
		pst.setString(1, book.getISBN());
		pst.setString(2, book.getTitle());
		pst.setString(3, book.getAuthor());
		pst.setString(4, book.getGenre());
		pst.setInt(5, book.getQuantity());
		pst.setFloat(6, book.getPrice());
		pst.executeUpdate();
		pst.close();
	}
	
	/**
	 * Inserts new order in PaperFood Database.
	 * @param order PaperFoodOrder
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	public void insertOrder(PaperFoodOrder order) throws SQLException
	{
		int currKey, last_id;
		
		//Create a new order
		PreparedStatement pst = con.prepareStatement("INSERT INTO orders(u_id, order_date, status) VALUES(?,?,?)");
		ResultSet rs;
		pst.setInt(1, order.getUser().getId());
		pst.setDate(2, getCurrentDate());
		pst.setString(3, "PROCESSING");
		pst.executeUpdate();
		
		//Get Last Inserted Order's ID
		Statement st = con.createStatement();
		if(DRIVER.contains("sqlite")) //Do it for SQLite
			rs = st.executeQuery("SELECT last_insert_rowid()");
		else						//Do it for MySQL
			rs = st.executeQuery("SELECT LAST_INSERT_ID()");
		
		rs.next();
		last_id = rs.getInt(1);
		rs.close();
		st.close();
		
		//Create Iterator over HashMap of Cart Items (bought Books)
		pst.clearParameters();
		Set cartItems = order.getBooks().keySet();
		Iterator itemsItr = cartItems.iterator();
		
		//Insert values in order_details
		while(itemsItr.hasNext())
		{
			currKey = (Integer) itemsItr.next();
			pst = con.prepareStatement("INSERT INTO orders_detail VALUES(?,?,?)");
			pst.setInt(1, last_id);
			pst.setInt(2, currKey);
			pst.setInt(3, order.getBooks().get(currKey));
			pst.executeUpdate();
		}
		pst.close();
	}
	
	/**
	 * Gets array of PaperFoodBook object based on given criteria, if criteria is "none", all books will be returned.
	 * @param criteria Criteria for filtering books, "none" for all books, or filter query for filter.
	 * @return PaperFoodBook[] for given criteria.
	 * @throws SQLException
	 */
	public PaperFoodBook[] getAllBooks(String criteria) throws SQLException
	{
		if(criteria.equalsIgnoreCase("none"))
		{
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM books");
			ArrayList<PaperFoodBook> books = new ArrayList<>();
			PaperFoodBook temp_book;
			while(rs.next())
			{
				temp_book = new PaperFoodBook();
				temp_book.setId(rs.getInt("b_id"));
				temp_book.setISBN(rs.getString("isbn"));
				temp_book.setTitle(rs.getString("title"));
				temp_book.setAuthor(rs.getString("author"));
				temp_book.setGenre(rs.getString("genre"));
				temp_book.setQuantity(rs.getInt("qty"));
				temp_book.setPrice(rs.getFloat("price"));
				
				books.add(temp_book);
			}
			rs.close();
			st.close();
			return books.toArray(new PaperFoodBook[books.size()]);
		}
		else
			return null;
	}
	
	/**
	 * Gets Book based on given ISBN number.
	 * @param isbn of the book.
	 * @return PaperFoodBook object for this book.
	 * @throws SQLException
	 */
	public PaperFoodBook getBook(String isbn) throws SQLException
	{
		PreparedStatement pst =  con.prepareStatement("SELECT * FROM books WHERE isbn like ?");
		pst.setString(1, isbn);
		ResultSet rs = pst.executeQuery();
		if(rs.next())
		{
			PaperFoodBook book = new PaperFoodBook();
			book.setId(rs.getInt("b_id"));
			book.setISBN(rs.getString("isbn"));
			book.setTitle(rs.getString("title"));
			book.setAuthor(rs.getString("author"));
			book.setGenre(rs.getString("genre"));
			book.setPrice(rs.getFloat("price"));
			book.setQuantity(rs.getInt("qty"));
			
			rs.close();
			pst.close();
			return book;
		}
		else
			return null;
	}
	
	/**
	 * Updates order status in PaperFood Database.
	 * @param order_id Id for which order is to be updated. 
	 * @param status Status of order, can be PROCESSING -> SHIPPED -> DELIVERED.
	 * @throws SQLException
	 */
	public void updateOrderStatus(int order_id, String status) throws SQLException
	{
		PreparedStatement pst = con.prepareStatement("UPDATE orders SET status = ? WHERE o_id = ?");
		pst.setString(1, status);
		pst.setInt(2, order_id);
		pst.executeUpdate();
		pst.close();
	}
	
	/**
	 * Checks if given email address exists in users table.
	 * @param email User email to be checked for.
	 * @return boolean whether email exists.
	 * @throws SQLException
	 */
	public boolean isUserMailExist(String email) throws SQLException
	{
		PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE email = ?");
		pst.setString(1, email);
		ResultSet rs = pst.executeQuery();
		if(rs.next())
		{
			rs.close();
			pst.close();
			return true;
		}
		else
		{
			rs.close();
			pst.close();
			return false;
		}
	}
	
	/**
	 * Returns PaperFoodUser object if Login email and password matches with anyone in the database.
	 * @param email Address of login user.
	 * @param password MD5 hash of password sent from user.
	 * @return PaperFoodUser object is login is valid, null if invalid.
	 * @throws SQLException
	 */
	public Object getLoggedUser(String email, String password) throws SQLException
	{
		PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?");
		pst.setString(1, email);
		pst.setString(2, password);
		ResultSet rs = pst.executeQuery();
		if(rs.next())
		{
			PaperFoodUser user = new PaperFoodUser();
			user.setId(rs.getInt("u_id"));
			user.setFirstName(rs.getString("fname"));
			user.setLastName(rs.getString("lname"));
			user.setEmail(rs.getString("email"));
			
			return user;
		}
		else
			return null;
	}
	
	/**
	 * Returns PaperFoodUser object for given Email.
	 * @param email Address of login user.
	 * @param password MD5 hash of password sent from user.
	 * @return PaperFoodUser object is login is valid, null if invalid.
	 * @throws SQLException
	 */
	public Object getLoggedUserByEmail(String email) throws SQLException
	{
		PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE email = ?");
		pst.setString(1, email);
		ResultSet rs = pst.executeQuery();
		if(rs.next())
		{
			PaperFoodUser user = new PaperFoodUser();
			user.setId(rs.getInt("u_id"));
			user.setFirstName(rs.getString("fname"));
			user.setLastName(rs.getString("lname"));
			user.setEmail(rs.getString("email"));
			
			return user;
		}
		else
			return null;
	}
	
	/**
	 * Closes this connection with PaperFood Database.
	 * @throws SQLException
	 */
	public void close() throws SQLException
	{
		con.close();
	}
	
	/**
	 * Gets current Date and Time in DD-MM-YYYY HH:mm:ss format.
	 * @return String date.
	 */
	private Date getCurrentDate()
	{
		//SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date now = new Date(System.currentTimeMillis());
		//return sdf.format(now);
		return now;
	}
}
