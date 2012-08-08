package com.paperfood;

import java.sql.SQLException;
import java.util.ArrayList;

import com.paperfood.entity.PaperFoodBook;

public class PaperFoodSearchIndex
{
	private ArrayList<String> index_source;
	private ArrayList<Integer> index_id;
	
	public PaperFoodSearchIndex()
	{
		index_source = new ArrayList<>();
		index_id = new ArrayList<>();
	}
	
	/**
	 * Constructs search index from Books stored in the database.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void contruct() throws ClassNotFoundException, SQLException
	{
		index_source.clear();
		index_id.clear();
		DatabaseManager dm = new DatabaseManager();
		dm.open();
		PaperFoodBook[] books = dm.getAllBooks("none");
		String idx_parts;
		for(int i=0; i<books.length; i++)
		{
			idx_parts = new String();
			idx_parts = books[i].getTitle() + " " +
						books[i].getAuthor() + " " +
						books[i].getGenre() + " ";
			index_source.add(idx_parts.toLowerCase());
			index_id.add(books[i].getId());
		}
	}
	
	/**
	 * Gets array of all book IDs which match with given key.
	 * @param key Search key.
	 * @return Integer[] of Books IDs for given key.
	 */
	public Integer[] getMatchingBookID(String key)
	{
		System.out.println(key);
		ArrayList<Integer> id_arr = new ArrayList<>();
		for(int i=0; i<index_source.size(); i++)
		{
			System.out.println(index_source.get(i));
			if(index_source.get(i).contains(key.toLowerCase()))
				id_arr.add(index_id.get(i));
		}
		return id_arr.toArray(new Integer[id_arr.size()]);
	}
}
