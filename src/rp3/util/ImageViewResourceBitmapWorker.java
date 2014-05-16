package rp3.util;

import java.lang.ref.WeakReference;

import rp3.runtime.Session;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageViewResourceBitmapWorker extends AsyncTask<Integer, Void, Bitmap> {		
	
	private final WeakReference<ImageView> imageViewReference;
	private final int reqHeight;
	private final int reqWidth;
	
	public ImageViewResourceBitmapWorker(ImageView imageView, int reqWidth, int reqHeight){
		imageViewReference = new WeakReference<ImageView>(imageView);
		this.reqWidth =  reqWidth;
		this.reqHeight = reqHeight;
	}
	
	@Override
	protected Bitmap doInBackground(Integer... params) {						
		int data = params[0];
		return BitmapUtils.decodeSampledBitmapFromResource(Session.getContext().getResources(), data, reqWidth, reqHeight);
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		if (imageViewReference != null && result != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(result);
            }
        }
	}
	
}
