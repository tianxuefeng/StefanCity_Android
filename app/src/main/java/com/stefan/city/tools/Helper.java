package com.stefan.city.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.rockeagle.framework.core.files.REFileHandle;
import com.stefan.city.R;

/**
 * Helper
 * @author 日期：2014-8-16下午08:59:30
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class Helper {
	static long secondInMillis = 1000;
	static long minuteInMillis = secondInMillis * 60;
	static long hourInMillis = minuteInMillis * 60;
	static long dayInMillis = hourInMillis * 24;
	

	public static boolean deleteFolder(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteFolder(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    } 
	public static boolean testVersion(Context context) {
		
		return true;
	}

	public static boolean saveInternalFileToExternalStorage(Context context,
			String internalFileName, File target) {
		FileOutputStream fos = null;
		FileInputStream fis = null;
		try {

			fis = context.openFileInput(internalFileName);
			fos = new FileOutputStream(target);
			int BUFF_SIZE = 1024;
			byte[] buff = new byte[BUFF_SIZE];
			int byteCount = 0;
			while ((byteCount = fis.read(buff, 0, BUFF_SIZE)) > 0) {
				fos.write(buff, 0, byteCount);

			}
			return true;
		} catch (FileNotFoundException e) {
			Helper.manageException(e, context);
		} catch (IOException e) {
			Helper.manageException(e, context);
		} finally {
			try {
				if (fos != null)
					fos.close();
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				Helper.manageException(e, context);
			}
		}
		return false;
	}

	public static File getExternalFolder(Context context, String subFolderName) {
		File folder = getExternalFolder(context);
		if (folder == null)
			return null;

		File subFolder = new File(folder.getAbsolutePath() + "/"
				+ subFolderName);
		if (!subFolder.exists())
			subFolder.mkdir();
		return subFolder;
	}

	private static File getExternalFolder(Context context) {
		if (!Helper.isExternalStorageWritable()) {
			Helper.doMessageDialog(context, context.getString(R.string.error),
					context.getString(R.string.storage_unavailable));
			return null;
		}
		return Environment.getExternalStorageDirectory();
	}
	public static void doMessageDialog(Context context, String title,
			String message) {
		doMessageDialog(context, title, message, null);
	}
	public static void doMessageDialog(Context context, String title,
			String message, OnClickListener onClick) {
		Thread contextThread = context. getMainLooper(). getThread();
		if (contextThread != Thread.currentThread())
			return;
		
		AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title)
				.setMessage(message)
				.setPositiveButton(android.R.string.ok, onClick).create();
		dialog.show();

	}

	public static String getPuzzleMetaFile(String puzzleFile, int puzzleSize) {
		return puzzleFile + Integer.toString(puzzleSize) + ".meta";
	}

	public static String getPuzzleFileName(Context context) {
		int number = 1;
		String fileName;
		do {
			fileName = String.format("Puzzle_%d.pfy", number++);

		} while (existPuzzle(context, fileName));
		return fileName;
	}

	public static boolean existPuzzle(Context context, String fileName) {
		String[] files = Helper.getPuzzleFiles(context);
		for (String s : files) {
			if (s.equals(fileName)) {
				return true;
			}
		}
		return false;
	}

	public static String[] getPuzzleFiles(Context context) {
		String[] files = context.fileList();
		Vector<String> puzzleFiles = new Vector<String>();
		for (String file : files)
			if (file.endsWith(".pfy"))
				puzzleFiles.add(file);
		String[] ret = new String[0];
		return puzzleFiles.toArray(ret);
	}

	public static Bitmap getBitmap(Context context, String file) {
		FileInputStream fis;
		Bitmap bmp = null;
		try {
			fis = context.openFileInput(file);
			bmp = BitmapFactory.decodeStream(fis);
			fis.close();

		} catch (FileNotFoundException e) {
			manageException(e, context);
		} catch (IOException e) {
			manageException(e, context);
		}
		return bmp;
	}
	
	public static void saveBitmap(String path, Bitmap bitmap) throws IOException {
		OutputStream os = null;
		os = new FileOutputStream(new File(path));
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
		if(os != null) {
			os.close();
		}
	}
	
	public static Bitmap getBitmap(String file) {
		FileInputStream fis = null;
		Bitmap bmp = null;
		try {
			fis = new FileInputStream(file);
			bmp = BitmapFactory.decodeStream(fis);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return bmp;
	}
	
	public static Bitmap[] getTiles(String path, int size) {
		File file = new File(path);
		String[] files = file.list();
		if(files == null || files.length == 0) {
			return null;
		}
		if(size != files.length) {
			REFileHandle.deleteDir(path, false);
			return null;
		}
		Bitmap[] bitmaps = new Bitmap[files.length]; 
		for (int i = 0; i < files.length; i++) {
			bitmaps[i] = getBitmap(files[i]);
		}
		return bitmaps;
	}

	public static byte[] getData(Context context, String file) {
		FileInputStream fis;
		int BUFF_SIZE = 1024;

		try {
			fis = context.openFileInput(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buff = new byte[BUFF_SIZE];
			int byteCount = 0;
			while ((byteCount = fis.read(buff, 0, BUFF_SIZE)) > 0) {
				out.write(buff, 0, byteCount);
			}
			fis.close();
			return out.toByteArray();
		} catch (FileNotFoundException e) {
			manageException(e, context);
		} catch (IOException e) {
			manageException(e, context);
		}
		return new byte[0];
	}

	public static String getElapsedTime(long duration) {

		long elapsedDays = duration / dayInMillis;
		duration = duration % dayInMillis;

		long elapsedHours = duration / hourInMillis;
		duration = duration % hourInMillis;

		long elapsedMinutes = duration / minuteInMillis;
		duration = duration % minuteInMillis;

		long elapsedSeconds = duration / secondInMillis;

		if (elapsedDays == 0 && elapsedHours == 0){
//			return String.format("%dm - %ds", elapsedMinutes, elapsedSeconds);
			if(elapsedMinutes == 0 && elapsedSeconds > 0) {
				return String.format("%d秒", elapsedSeconds);
			} else if(elapsedMinutes > 0) {
				return String.format("%d分钟 - %d秒", elapsedMinutes, elapsedSeconds);
			} else {
				return "完美!";
			}
		}	
		if (elapsedDays == 0)
			return String.format("%d小时 - %d分钟 - %d秒", elapsedHours,
					elapsedMinutes, elapsedSeconds);

		return String.format("%d天 - %d小时 - %d分钟 - %d秒", elapsedDays, elapsedHours,
				elapsedMinutes, elapsedSeconds);
	}

	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();

		return Environment.MEDIA_MOUNTED.equals(state);
	}

	public static void manageException(Exception e, Context context) {
		doMessageDialog(context, context.getString(R.string.error),
				e.getLocalizedMessage());

	}

	public static String[] getExternalPuzzles(Context context) {
		Vector<String> files = new Vector<String>();
		File root = getExternalFolder(context);

		addFiles(root, files);

		return files.toArray(new String[files.size()]);

	}

	private static void addFiles(File root, Vector<String> files) {
		if (root == null)
			return;
		File[] listFiles = root.listFiles();
		if (listFiles == null)
			return;
		for (File f : listFiles) {

			if (f.isDirectory())
				addFiles(f, files);
			else if (f.getName().endsWith(".pfy"))
				files.add(f.getAbsolutePath());

		}

	}
	public static String importFile(Context context, StringBuffer messages, String file) {
		File source = new File(file);
		FileOutputStream fos = null;
		try {
			String targetFileName = source.getName();
			if (Helper.existPuzzle(context, targetFileName)) {
				String newFile = Helper.getPuzzleFileName(context);
				if (messages.length() > 0)
					messages.append("\r\n");
				messages.append(context.getString(R.string.file_renamed,
						targetFileName, newFile));
				targetFileName = newFile;
			}
			fos = context.openFileOutput(targetFileName, Context.MODE_PRIVATE);
			Helper.copyFile(source, fos);
			return targetFileName;
		} catch (Exception e) {
			if (messages.length() > 0)
				messages.append("\r\n");
			messages.append(e.getLocalizedMessage());
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					if (messages.length() > 0)
						messages.append("\r\n");
					messages.append(e.getLocalizedMessage());
				}
		}
		return null;
	}

	public static void copyFile(File in, File out) throws Exception {
		FileOutputStream fos = new FileOutputStream(out);
		copyFile(in, fos);
		fos.close();
	}
	public static void copyFile(File in, FileOutputStream fos) throws Exception {
		FileInputStream fis = new FileInputStream(in);
		try {
			byte[] buf = new byte[1024];
			int i = 0;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (fis != null)
				fis.close();
		}
	}

	public static void safeDeleteFile(Context context, String file) {
		try {
			context.deleteFile(file);
		} catch (Exception e) {
		}

	}
}
