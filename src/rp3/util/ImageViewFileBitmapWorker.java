package rp3.util;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageViewFileBitmapWorker extends AsyncTask<String, Void, Bitmap> {		
	
	private final WeakReference<ImageView> imageViewReference;
	private final int reqHeight;
	private final int reqWidth;
	
	public ImageViewFileBitmapWorker(ImageView imageView, int reqWidth, int reqHeight){
		imageViewReference = new WeakReference<ImageView>(imageView);
		this.reqWidth =  reqWidth;
		this.reqHeight = reqHeight;
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {						
		String fileName = params[0];		
		return BitmapUtils.decodeSampledBitmapFromInternalStorage(fileName, reqWidth, reqHeight);		
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
