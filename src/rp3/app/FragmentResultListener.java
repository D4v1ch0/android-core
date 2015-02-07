package rp3.app;

import android.os.Bundle;

public interface FragmentResultListener {

	void onFragmentResult(String tagName, int resultCode, Bundle data);
	
}
