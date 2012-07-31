/**
 * 
 */
package com.paperfood;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
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
		pst.setInt(1, order.getUser().getId());
		pst.setString(2, getCurrentDate());
		pst.setString(3, "PROCESSING");
		pst.executeUpdate();
		
		//Get Last Inserted Order's ID
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("SELECT last_insert_rowid()");
		rs.next();
		last_id = rs.getInt(0);
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
	private String getCurrentDate()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd-DD-yyyy HH:mm:ss");
		Date now = new Date();
		return sdf.format(now);
	}
}
