package org.jempe.flashlight;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

	private static final String TAG =  "Flashlight Activity";
	private Camera mCamera;
    private CameraPreview mPreview;
    private Parameters mParameters;
    private List<String> mFlashModes;
    private FrameLayout mFramePreview;
	private WebView mFlashlightView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mCamera = getCameraInstance();
		
		// Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        mFramePreview = (FrameLayout) findViewById(R.id.camera_preview);
        mFramePreview.addView(mPreview);	
        
	    mParameters = mCamera.getParameters();
	    
	    mFlashModes = mParameters.getSupportedFlashModes();
	    
	    mFlashlightView = (WebView) findViewById(R.id.webView1);
		WebSettings webSettings = mFlashlightView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mFlashlightView.getSettings().setLoadWithOverviewMode(true);
        mFlashlightView.getSettings().setUseWideViewPort(true);
		mFlashlightView.setWebViewClient(new WebViewClient());
		mFlashlightView.setVerticalScrollBarEnabled(false);
		mFlashlightView.setOnTouchListener(new View.OnTouchListener() 
		{
            public boolean onTouch(View v, MotionEvent event) 
            {
                return(event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
		mFlashlightView.loadUrl("file:///android_asset/flashlight.html?v=6");
		mFlashlightView.addJavascriptInterface(new WebAppInterface(this), "Android");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
        switch (item.getItemId()) 
        {
	       	case R.id.action_settings:
	       		turnOn();
	       		return true;
	       	case R.id.action_settings2:
	       		turnOff();
	       		return true;
	        default:
	           return super.onOptionsItemSelected(item);
	    }
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	public void turnOn() 
	{
	    String flashMode = mParameters.getFlashMode();
	    
	    if ( ! Parameters.FLASH_MODE_TORCH.equals(flashMode))
	    {
	        // Turn on the flash
	        if (mFlashModes.contains(Parameters.FLASH_MODE_TORCH)) 
	        {
	          mParameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
	          mCamera.setParameters(mParameters);
	          // start preview with new settings

	          Log.d(TAG, "Turn On Flashlight");
	        } 
	        else 
	        {

	        }
	    }
	}

	public void turnOff() {
	    String flashMode = mParameters.getFlashMode();
	    
	    if ( ! Parameters.FLASH_MODE_OFF.equals(flashMode))
	    {
	        // Turn on the flash
	        if (mFlashModes.contains(Parameters.FLASH_MODE_OFF)) 
	        {
	          mParameters.setFlashMode(Parameters.FLASH_MODE_OFF);
	          mCamera.setParameters(mParameters);
	          // start preview with new settings

	          Log.d(TAG, "Turn Off Flashlight");
	        } 
	        else 
	        {

	        }
	    }
	}

	@Override
	protected void onPause() {
		super.onPause();
		mFramePreview.removeView(mPreview);	
		releaseCamera(); // release the camera immediately on pause event
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		Log.d(TAG, "on Resume");
		if (mCamera == null) 
		{
			Log.d(TAG, "Camera was null");
			mCamera = getCameraInstance();
			
			// Create our Preview view and set it as the content of our activity.
	        mPreview = new CameraPreview(this, mCamera);

	        mFramePreview.addView(mPreview);	
	        
		    mParameters = mCamera.getParameters();
		    
		    mFlashModes = mParameters.getSupportedFlashModes();
		}
	}

	public class WebAppInterface {
	    Context mContext;

	    /** Instantiate the interface and set the context */
	    WebAppInterface(Context c) {
	        mContext = c;
	    }

	    //@JavascriptInterface
	    public void turnOnNow() {
	    	Log.v("Photo", "Take Photo Now");
	    	turnOn();
	    }
	    
	    //@JavascriptInterface
	    public void turnOffNow() {
	    	Log.v("Photo", "Take Photo Now");
	    	turnOff();
	    }
	}
	
}
