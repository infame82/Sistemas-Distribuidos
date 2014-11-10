package com.uag.sd.weathermonitor.model.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectSerializer {

	public static byte[] serialize(Object o) throws IOException {
		if(o==null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		byte[] buffer = baos.toByteArray();
		baos.close();
		oos.close();
		return buffer;
	}
	
	public static Object unserialize(byte[] content) throws IOException, ClassNotFoundException {
		if(content==null || content.length==0) {
			return null;
		}
		ByteArrayInputStream b = new ByteArrayInputStream(content);
		ObjectInputStream o = new ObjectInputStream(b);
		Object object = o.readObject();
		b.close();
		return object;
	}
	
}
