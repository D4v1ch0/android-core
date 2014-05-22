package rp3.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import rp3.runtime.Session;

public abstract class FileUtils {
	
	public static void setBitmapFromInternalStorageAsync(ImageView imageView, String fileName){
		ImageViewFileBitmapWorker worker = new ImageViewFileBitmapWorker(imageView, imageView.getWidth(), imageView.getHeight());
		worker.execute(fileName);
	}
	
	public static void setBitmapFromInternalStorageAsync(ImageView imageView, String fileName, int reqWidth, int reqHeight){
		ImageViewFileBitmapWorker worker = new ImageViewFileBitmapWorker(imageView,reqWidth,reqHeight);
		worker.execute(fileName);
	}
	
	public static void setBitmapFromResourcesAsync(ImageView imageView, int resID){
		ImageViewResourceBitmapWorker worker = new ImageViewResourceBitmapWorker(imageView, imageView.getWidth(), imageView.getHeight());
		worker.execute(resID);
	}
	
	public static void setBitmapFromResourcesAsync(ImageView imageView, int resID, int reqWidth, int reqHeight){
		ImageViewResourceBitmapWorker worker = new ImageViewResourceBitmapWorker(imageView, reqWidth, reqHeight);
		worker.execute(resID);
	}
	
	public static Bitmap getBitmapFromInternalStorage(String fileName) throws IOException{
		FileInputStream is;
		
		is = Session.getContext().openFileInput(fileName);
		Bitmap myBitmap = BitmapFactory.decodeStream(is);
		is.close();
		return myBitmap;		
	}
	
	public static void deleteFromInternalStorage(String fileName){
		Session.getContext().deleteFile(fileName);
	}
	
	public static void saveInternalStorage(String fileName, Bitmap bitmap){
		//File file = new File(Session.getContext().getFilesDir(), fileName);
		
		FileOutputStream outputStream;

		try {
		  outputStream = Session.getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
		  bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
		  outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}
	
	
	
}
