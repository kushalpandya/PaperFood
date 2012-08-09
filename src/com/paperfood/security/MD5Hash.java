/**
 * PaperFood v0.1
 * 
 * Author: Kushal Pandya < https://github.com/kushalpandya >
 * License: GPLv3.
 * 
 * Class to get MD5 hash of String and File.
 */
package com.paperfood.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hash
{
	private MessageDigest md;
	private byte[] digest;
	private BigInteger bigInt;
	private String hash;
	
	/**
	 * Create MD5Hash object and getInstance of MD5 algorithm.
	 * @throws NoSuchAlgorithmException
	 */
	public MD5Hash() throws NoSuchAlgorithmException
	{
		md = MessageDigest.getInstance("MD5");
		md.reset();
	}
	
	/**
	 * Get 32 character String representing MD5 Hash of given key.
	 * @param key String for which Hash is to be generated.
	 * @return String generated 32 character MD5 Hash.
	 */
	public String getStringHash(String key)
	{
		String source = key;
		md.update(source.getBytes());
		digest = md.digest();
		bigInt = new BigInteger(1,digest);
		hash = bigInt.toString(16);
		while(hash.length()<32)
			hash = "0" + hash;
		
		return hash;
	}
	
	/**
	 * Get 32 character String representing MD5 Hash of given File source.
	 * @param source File for which Hash is to be generated.
	 * @return String generated 32 character MD5 Hash.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String getFileHash(File source) throws FileNotFoundException, IOException
	{
		@SuppressWarnings("unused")
		int i = 0;
		InputStream is = new FileInputStream(source);
		is = new DigestInputStream(is, md);
		while((i=is.read())!=-1)
		{
			is.read();
		}
		is.close();
		digest = md.digest();
		bigInt = new BigInteger(1,digest);
		hash = bigInt.toString(16);
		while(hash.length()<32)
			hash = "0" + hash;
		
		return hash;
	}
}
