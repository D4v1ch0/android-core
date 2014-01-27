package rp3.content;

import android.content.AsyncTaskLoader;
import android.content.Context;

public abstract class SimpleObjectLoader<T> extends AsyncTaskLoader<T> {
	
	private T resultValue;
	
	public SimpleObjectLoader(Context context) {
		super(context);		
	}


    /* Runs on a worker thread */
    @Override
    public abstract T loadInBackground();

    /* Runs on the UI thread */
    @Override
    public void deliverResult(T value) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            value = null;
            return;
        }      
        resultValue = value;

        if (isStarted()) {
            super.deliverResult(resultValue);
        }       
    }

    /**
     * Starts an asynchronous load of the contacts list data. When the result is ready the callbacks
     * will be called on the UI thread. If a previous load has been completed and is still valid
     * the result may be passed to the callbacks immediately.
     * <p/>
     * Must be called from the UI thread
     */
    @Override
    protected void onStartLoading() {
        if (resultValue != null) {
            deliverResult(resultValue);
        }
        if (takeContentChanged() || resultValue == null) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(T value) {
        value = null;
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();              
    }
}
