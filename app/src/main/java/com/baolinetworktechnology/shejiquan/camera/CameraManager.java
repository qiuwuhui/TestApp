/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baolinetworktechnology.shejiquan.camera;

import java.io.IOException;

import com.baolinetworktechnology.shejiquan.utils.WindowsUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;

//import com.google.zxing.client.android.PreferencesActivity;

/**
 * This object wraps the Camera service object and expects to be the only one
 * talking to it. The implementation encapsulates the steps needed to take
 * preview-sized images, which are used for both preview and decoding.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class CameraManager {

	private static final String TAG = CameraManager.class.getSimpleName();

	private static int MIN_FRAME_WIDTH = 240;
	private static int MIN_FRAME_HEIGHT = 240;

	private static int MAX_FRAME_WIDTH = 260;
	private static int MAX_FRAME_HEIGHT = 260;

	private final Context mContext;
	private final CameraConfigurationManager mConfigManager;
	private Camera mCamera;
	private Rect mFramingRect;
	private Rect mFramingRectInPreview;
	private boolean mInitialized;
	private boolean mPreviewing;
	private boolean mReverseImage;
	private int mRequestedFramingRectWidth;
	private int mRequestedFramingRectHeight;
	/**
	 * Preview frames are delivered here, which we pass on to the registered
	 * handler. Make sure to clear the handler so it will only receive one
	 * message.
	 */
	private final PreviewCallback previewCallback;
	/**
	 * Autofocus callbacks arrive here, and are dispatched to the Handler which
	 * requested them.
	 */
	private final AutoFocusCallback autoFocusCallback;

	public static final String KEY_REVERSE_IMAGE = "preferences_reverse_image";

	public CameraManager(Context context) {
		this.mContext = context;
		this.mConfigManager = new CameraConfigurationManager(context);
		MIN_FRAME_WIDTH = MIN_FRAME_HEIGHT = (int) WindowsUtil.dip2px(context,
				240);
		MAX_FRAME_WIDTH = MAX_FRAME_HEIGHT = (int) WindowsUtil.dip2px(context,
				260);
		previewCallback = new PreviewCallback(mConfigManager);
		autoFocusCallback = new AutoFocusCallback();
	}

	/**
	 * Opens the camera driver and initializes the hardware parameters.
	 * 
	 * @param holder
	 *            The surface object which the camera will draw preview frames
	 *            into.
	 * @throws IOException
	 *             Indicates the camera driver failed to open.
	 */
	public void openDriver(SurfaceHolder holder) throws IOException {
		Camera theCamera = mCamera;
		if (theCamera == null) {
			theCamera = Camera.open();
			if (theCamera == null) {
				throw new IOException();
			}
			mCamera = theCamera;
		}
		theCamera.setPreviewDisplay(holder);

		if (!mInitialized) {
			mInitialized = true;
			mConfigManager.initFromCameraParameters(theCamera);
			if (mRequestedFramingRectWidth > 0
					&& mRequestedFramingRectHeight > 0) {
				setManualFramingRect(mRequestedFramingRectWidth,
						mRequestedFramingRectHeight);
				mRequestedFramingRectWidth = 0;
				mRequestedFramingRectHeight = 0;
			}
		}
		mConfigManager.setDesiredCameraParameters(theCamera);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		mReverseImage = prefs.getBoolean(KEY_REVERSE_IMAGE, false);
	}

	/**
	 * Closes the camera driver if still in use.
	 */
	public void closeDriver() {
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
			// Make sure to clear these each time we close the camera, so that
			// any scanning rect
			// requested by intent is forgotten.
			mFramingRect = null;
			mFramingRectInPreview = null;
		}
	}

	/**
	 * Asks the camera hardware to begin drawing preview frames to the screen.
	 */
	public void startPreview() {
		Camera theCamera = mCamera;
		if (theCamera != null && !mPreviewing) {
			theCamera.startPreview();
			mPreviewing = true;
		}
	}

	/**
	 * Tells the camera to stop drawing preview frames.
	 */
	public void stopPreview() {
		if (mCamera != null && mPreviewing) {
			mCamera.stopPreview();
			previewCallback.setHandler(null, 0);
			autoFocusCallback.setHandler(null, 0);
			mPreviewing = false;
		}
	}

	/**
	 * A single preview frame will be returned to the handler supplied. The data
	 * will arrive as byte[] in the message.obj field, with width and height
	 * encoded as message.arg1 and message.arg2, respectively.
	 * 
	 * @param handler
	 *            The handler to send the message to.
	 * @param message
	 *            The what field of the message to be sent.
	 */
	public void requestPreviewFrame(Handler handler, int message) {
		Camera theCamera = mCamera;
		if (theCamera != null && mPreviewing) {
			previewCallback.setHandler(handler, message);
			theCamera.setOneShotPreviewCallback(previewCallback);
		}
	}

	/**
	 * Asks the camera hardware to perform an autofocus.
	 * 
	 * @param handler
	 *            The Handler to notify when the autofocus completes.
	 * @param message
	 *            The message to deliver.
	 */
	public void requestAutoFocus(Handler handler, int message) {
		if (mCamera != null && mPreviewing) {
			autoFocusCallback.setHandler(handler, message);
			try {
				mCamera.autoFocus(autoFocusCallback);
			} catch (RuntimeException re) {
				// Have heard RuntimeException reported in Android 4.0.x+;
				// continue?
				Log.w(TAG, "Unexpected exception while focusing", re);
			}
		}
	}

	/**
	 * Calculates the framing rect which the UI should draw to show the user
	 * where to place the barcode. This target helps with alignment as well as
	 * forces the user to hold the device far enough away to ensure the image
	 * will be in focus.
	 * 
	 * @return The rectangle to draw on screen in window coordinates.
	 */
	public Rect getFramingRect() {
		if (mFramingRect == null) {
			if (mCamera == null) {
				return null;
			}
			Point screenResolution = mConfigManager.getScreenResolution();
			if (screenResolution == null)
				return null;
			int width = screenResolution.x * 3 / 4;
			if (width < MIN_FRAME_WIDTH) {
				width = MIN_FRAME_WIDTH;
			} else if (width > MAX_FRAME_WIDTH) {
				width = MAX_FRAME_WIDTH;
			}
			int height = screenResolution.y * 3 / 4;
			if (height < MIN_FRAME_HEIGHT) {
				height = MIN_FRAME_HEIGHT;
			} else if (height > MAX_FRAME_HEIGHT) {
				height = MAX_FRAME_HEIGHT;
			}
			int leftOffset = (screenResolution.x - width) / 2;
			int topOffset = (screenResolution.y - height) * 2 / 5;
			mFramingRect = new Rect(leftOffset, topOffset, leftOffset + width,
					topOffset + height);
			Log.d(TAG, "Calculated framing rect: " + mFramingRect);
		}
		return mFramingRect;
	}

	/**
	 * Like {@link #getFramingRect} but coordinates are in terms of the preview
	 * frame, not UI / screen.
	 */
	public Rect getFramingRectInPreview() {
		if (mFramingRectInPreview == null) {
			Rect framingRect = getFramingRect();
			if (framingRect == null) {
				return null;
			}
			Rect rect = new Rect(framingRect);
			Point cameraResolution = mConfigManager.getCameraResolution();
			Point screenResolution = mConfigManager.getScreenResolution();
			/*
			 * 横屏 rect.left = rect.left * cameraResolution.x /
			 * screenResolution.x; rect.right = rect.right * cameraResolution.x
			 * / screenResolution.x; rect.top = rect.top * cameraResolution.y /
			 * screenResolution.y; rect.bottom = rect.bottom *
			 * cameraResolution.y / screenResolution.y;
			 */

			/* 竖屏 */
			rect.left = rect.left * cameraResolution.y / screenResolution.x;
			rect.right = rect.right * cameraResolution.y / screenResolution.x;
			rect.top = rect.top * cameraResolution.x / screenResolution.y;
			rect.bottom = rect.bottom * cameraResolution.x / screenResolution.y;
			mFramingRectInPreview = rect;
		}
		return mFramingRectInPreview;
	}

	/**
	 * Allows third party apps to specify the scanning rectangle dimensions,
	 * rather than determine them automatically based on screen resolution.
	 * 
	 * @param width
	 *            The width in pixels to scan.
	 * @param height
	 *            The height in pixels to scan.
	 */
	public void setManualFramingRect(int width, int height) {
		if (mInitialized) {
			Point screenResolution = mConfigManager.getScreenResolution();
			if (width > screenResolution.x) {
				width = screenResolution.x;
			}
			if (height > screenResolution.y) {
				height = screenResolution.y;
			}
			int leftOffset = (screenResolution.x - width) / 2;
			int topOffset = (screenResolution.y - height) / 2;
			mFramingRect = new Rect(leftOffset, topOffset, leftOffset + width,
					topOffset + height);
			Log.d(TAG, "Calculated manual framing rect: " + mFramingRect);
			mFramingRectInPreview = null;
		} else {
			mRequestedFramingRectWidth = width;
			mRequestedFramingRectHeight = height;
		}
	}

	/**
	 * A factory method to build the appropriate LuminanceSource object based on
	 * the format of the preview buffers, as described by Camera.Parameters.
	 * 
	 * @param data
	 *            A preview frame.
	 * @param width
	 *            The width of the image.
	 * @param height
	 *            The height of the image.
	 * @return A PlanarYUVLuminanceSource instance.
	 */
	public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data,
			int width, int height) {
		Rect rect = getFramingRectInPreview();
		if (rect == null) {
			return null;
		}
		// Go ahead and assume it's YUV rather than die.
		return new PlanarYUVLuminanceSource(data, width, height, rect.left,
				rect.top, rect.width(), rect.height(), mReverseImage);
	}

	public void openFlashLight() {
		if (mCamera != null) {
			Parameters parameter = mCamera.getParameters();
			parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
			mCamera.setParameters(parameter);
		}
	}

	public void offFlashLight() {
		if (mCamera != null) {
			Parameters parameter = mCamera.getParameters();
			parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
			mCamera.setParameters(parameter);
		}
	}

	public void switchFlashLight() {
		if (mCamera != null) {
			Parameters parameter = mCamera.getParameters();
			if (parameter.getFlashMode().equals(Parameters.FLASH_MODE_TORCH)) {
				parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
			} else {
				parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
			}

			mCamera.setParameters(parameter);
		}
	}
}
