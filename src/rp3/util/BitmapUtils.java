package rp3.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import rp3.runtime.Session;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public abstract class BitmapUtils {
	
	
	
	public static Bitmap decodeBitmapFromBase64(String data){		
		byte[] imageAsBytes = Base64.decode(data.getBytes(), Base64.DEFAULT);
	    return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
	}

	public static Bitmap decodeSampledBitmapFromInternalStorage(String fileName,
	        int reqWidth, int reqHeight) {
		
		if(reqWidth == 0 || reqHeight == 0){
			FileInputStream is;
			try {
				is = Session.getContext().openFileInput(fileName);
				Bitmap myBitmap = BitmapFactory.decodeStream(is);				
				is.close();				
				return myBitmap;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		
			return null;
		}
		else{
			final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(fileName, options);
	
		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	
		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    
		    return BitmapFactory.decodeFile(fileName, options);
		}		
	}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

		if(reqWidth == 0 || reqHeight == 0)
		{
			return BitmapFactory.decodeResource(res, resId);
		}
		else{
			// First decode with inJustDecodeBounds=true to check dimensions
		    final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeResource(res, resId, options);
	
		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	
		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    return BitmapFactory.decodeResource(res, resId, options);
		}	    
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

}
