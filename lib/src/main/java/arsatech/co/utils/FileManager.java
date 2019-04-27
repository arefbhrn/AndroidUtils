package arsatech.co.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class FileManager {

	public static boolean exists(String path) {
		return new File(path).exists();
	}

	public static boolean hideFolderFromGallery(String path) {
		File dir = new File(path);
		return dir.exists() && makeFile(path + ".nomedia");
	}

	public static boolean makeDirectory(String path) {
		File dir = new File(path);
		return !dir.exists() && dir.mkdir();
	}

	public static boolean makeDirectories(String path) {
		File dir = new File(path);
		return !dir.exists() && dir.mkdirs();
	}

	public static boolean makeFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				return file.createNewFile();
			} catch (IOException ignored) {
			}
		}
		return false;
	}

	public static boolean copy(String src, String des) {
		try {
			File in = new File(src);
			File out = new File(des);
			FileManager.makeFile(src);
			DataInputStream stream = new DataInputStream(new FileInputStream(in));
			byte[] buffer = new byte[(int) in.length()];
			stream.readFully(buffer);
			stream.close();
			DataOutputStream fos = new DataOutputStream(new FileOutputStream(out));
			fos.write(buffer);
			fos.flush();
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			return false; // swallow a 404
		} catch (IOException e) {
			return false; // swallow a 404
		}
	}

	public static boolean copy(File src, File des) {
		try {
			DataInputStream stream = new DataInputStream(new FileInputStream(src));
			byte[] buffer = new byte[(int) src.length()];
			stream.readFully(buffer);
			stream.close();
			DataOutputStream fos = new DataOutputStream(new FileOutputStream(des));
			fos.write(buffer);
			fos.flush();
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			return false; // swallow a 404
		} catch (IOException e) {
			return false; // swallow a 404
		}
	}

	public static void copyFile(String src, String des) throws IOException {
		File inputFile = new File(src);
		File outputFile = new File(des);
		FileInputStream inStream = new FileInputStream(inputFile);
		FileOutputStream outStream = new FileOutputStream(outputFile);
		FileChannel inChannel = inStream.getChannel();
		FileChannel outChannel = outStream.getChannel();
		inChannel.transferTo(0, inChannel.size(), outChannel);
		inStream.close();
		outStream.close();
	}

	public static void copyFile(File src, File des) throws IOException {
		FileInputStream inStream = new FileInputStream(src);
		FileOutputStream outStream = new FileOutputStream(des);
		FileChannel inChannel = inStream.getChannel();
		FileChannel outChannel = outStream.getChannel();
		inChannel.transferTo(0, inChannel.size(), outChannel);
		inStream.close();
		outStream.close();
	}

	public static void copyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (; ; ) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ignored) {
		}
	}

	public static boolean deleteFile(String path) {
		File file = new File(path);
		return file.exists() && file.delete();
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
			return dir.delete();
		} else if (dir != null && dir.isFile()) {
			return dir.delete();
		} else {
			return false;
		}
	}

	public static boolean move(String src, String des) {
		return copy(src, des) && deleteFile(src);
	}

	public static ArrayList<String> getFiles(String directory) {
		ArrayList<String> lstFiles = new ArrayList<>();
		File dir = new File(directory);
		if (!dir.isDirectory())
			return lstFiles;
		File[] files = dir.listFiles();
		for (File file : files)
			if (!file.isDirectory())
				lstFiles.add(file.getPath().split("/")[file.getPath().split("/").length - 1]);
		return lstFiles;
	}

	// searches subfolders too
	public static ArrayList<File> getListFiles(File parentDir) {
		ArrayList<File> inFiles = new ArrayList<>();
		File[] files = parentDir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				inFiles.addAll(getListFiles(file));
			} else {
				if (file.getName().endsWith(".csv")) {
					inFiles.add(file);
				}
			}
		}
		return inFiles;
	}

	public static long getPathSize(File f) {
		long size = 0;
		if (f.isDirectory()) {
			for (File file : f.listFiles()) {
				size += getPathSize(file);
			}
		} else {
			size = f.length();
		}
		return size;
	}

	public static boolean clearCache(Context context) {
		try {
			File dir = context.getCacheDir();
			return deleteDir(dir);
		} catch (Exception ignored) {
		}
		return false;
	}

	public static void openFile(Context context, File file) throws IOException, ActivityNotFoundException {
		if (!file.exists())
			throw new IOException();

		// Create URI
		Uri uri = Uri.fromFile(file);

		String extention = file.toString().substring(file.toString().lastIndexOf("."));

		Intent intent = new Intent(Intent.ACTION_VIEW);
		// Check what kind of file you are trying to open, by comparing the url with extensions.
		// When the if condition is matched, plugin sets the correct intent (mime) type,
		// so Android knew what application to use to open the file
		if (extention.equals(".doc") || extention.equals(".docx")) {
			// Word document
			intent.setDataAndType(uri, "application/msword");
		} else if (extention.equals(".pdf")) {
			// PDF file
			intent.setDataAndType(uri, "application/pdf");
		} else if (extention.equals(".ppt") || extention.equals(".pptx")) {
			// Powerpoint file
			intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		} else if (extention.equals(".xls") || extention.equals(".xlsx")) {
			// Excel file
			intent.setDataAndType(uri, "application/vnd.ms-excel");
		} else if (extention.equals(".zip") || extention.equals(".rar")) {
			// ZIP file
			intent.setDataAndType(uri, "application/zip");
		} else if (extention.equals(".rtf")) {
			// RTF file
			intent.setDataAndType(uri, "application/rtf");
		} else if (extention.equals(".wav") || extention.equals(".mp3")) {
			// WAV audio file
			intent.setDataAndType(uri, "audio/x-wav");
		} else if (extention.equals(".gif")) {
			// GIF file
			intent.setDataAndType(uri, "image/gif");
		} else if (extention.equals(".jpg") || extention.equals(".jpeg") ||
				extention.equals(".png")) {
			// JPG file
			intent.setDataAndType(uri, "image/jpeg");
		} else if (extention.equals(".txt")) {
			// Text file
			intent.setDataAndType(uri, "text/plain");
		} else if (extention.equals(".3gp") || extention.equals(".mpg") ||
				extention.equals(".mpeg") || extention.equals(".mpe") ||
				file.toString().contains(".mp4") || file.toString().contains(".avi")) {
			// Video files
			intent.setDataAndType(uri, "video/*");
		} else {
			//if you want you can also define the intent type for any other file

			//additionally use else clause below, to manage other unknown extensions
			//in this case, Android will show all applications installed on the device
			//so you can choose which application to use
			intent.setDataAndType(uri, "*/*");
		}

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		context.startActivity(intent);
	}

}
