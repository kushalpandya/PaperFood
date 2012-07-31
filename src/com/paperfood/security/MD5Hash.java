package com.paperfood.security;

import java.security.*;
import java.math.*;
import java.io.*;

public class MD5Hash
{
	private MessageDigest md;
	private byte[] digest;
	private BigInteger bigInt;
	private String hash;
	
	public MD5Hash() throws NoSuchAlgorithmException
	{
		md = MessageDigest.getInstance("MD5");
		md.reset();
	}
	
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
