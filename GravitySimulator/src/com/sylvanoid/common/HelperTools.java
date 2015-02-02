package com.sylvanoid.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class HelperTools {

	public static Object fromString(String s) throws IOException,
			ClassNotFoundException {
		byte[] data = Base64Coder.decode(s);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
				data));
		Object o = ois.readObject();
		ois.close();
		return o;
	}

	public static String toString(Serializable o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return new String(Base64Coder.encode(baos.toByteArray()));
	}
	
	public static String[] splitString(String str, int maxLength) {
	    if (str==null) return null;
	    int len = str.length();
	    int fullChunks = len / maxLength;
	    boolean lastChunkEmpty = fullChunks*maxLength == len;
	    
	    int size = fullChunks;
	    if (!lastChunkEmpty) size++;
	    
	    int i = 0;
	    String[] chunks = new String[size];
	    for (i=0; i<fullChunks; i++) {
	    	chunks[i] = str.substring(i*maxLength, i*maxLength+maxLength);
	    }
	    
	    if (!lastChunkEmpty) chunks[i] = str.substring(i*maxLength, len);
	    
	    return chunks;
	}

}
