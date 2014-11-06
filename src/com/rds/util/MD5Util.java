package com.rds.util;

import java.security.MessageDigest;

public class MD5Util {
	public final static String MD5(String s) {
		try{
    		MessageDigest md = MessageDigest.getInstance("MD5");
    	    md.update(s.getBytes("utf-8"));    	    
    	    StringBuffer buf=new StringBuffer();    	    
    	    for(byte b:md.digest())
    	    	buf.append(String.format("%02x", b&0xff) );    	     
    	    return buf.toString();
    	}catch( Exception e ){
    		e.printStackTrace(); return null;
    	}  
    }
}
