package org.jempe.flashlight;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    
    private static final String TAG = "CameraPreview";

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        
	    Parameters parameters = mCamera.getParameters();
	    if (parameters == null) 
	    {
	      return;
	    }
	    
	    
	    
	    List<String> flashModes = parameters.getSupportedFlashModes();
	    // Check if camera flash exists
	    if (flashModes == null) 
	    {
	      return;
	    }
	    
	    String flashMode = parameters.getFlashMode();
	    
	    if ( ! Parameters.FLASH_MODE_TORCH.equals(flashMode))
	    {
	        // Turn on the flash
	        if (flashModes.contains(Parameters.FLASH_MODE_TORCH)) 
	        {
	          parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
	          mCamera.setParameters(parameters);
	          // start preview with new settings
	          try {
	        	  mCamera.setDisplayOrientation(90);
	              mCamera.setPreviewDisplay(mHolder);
	              mCamera.startPreview();

	          } catch (Exception e){
	              Log.d(TAG, "Error starting camera preview: " + e.getMessage());
	          }
	          Log.d(TAG, "Turn On Flashlight");
	        } 
	        else 
	        {

	        }
	    }

    }
}
