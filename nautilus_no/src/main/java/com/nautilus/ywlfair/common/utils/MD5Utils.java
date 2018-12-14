package com.nautilus.ywlfair.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	public static String md5(File file) {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			byte[] b = new byte[8192];
			int length = -1;
			MessageDigest md = MessageDigest.getInstance("MD5");
			while ((length = fin.read(b)) != -1) {
				md.update(b, 0, length);
			}
			return md5(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static String md5(byte b[]) {
		int i;
		StringBuffer buf = new StringBuffer("");
		for (byte element : b) {
			i = element;
			if (i < 0) {
				i += 256;
			}
			if (i < 16) {
				buf.append("0");
			}
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}

	public static String md5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			return md5(b);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
