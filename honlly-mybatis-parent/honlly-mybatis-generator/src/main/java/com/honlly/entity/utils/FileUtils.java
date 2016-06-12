package com.honlly.entity.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
	public static void save(String path, String data) {
		FileWriter out = null;
		try {
			File file = new File(path);
			File dir = new File(path.substring(0, path.lastIndexOf("/")));
			if(!dir.exists()) {
				dir.mkdirs();
			}
			out = new FileWriter(file);
			out.write(data);
			out.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
