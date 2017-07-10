package com.fpt.menulibs.provider;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
	private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";	
	private int stub_id;
	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;
	Handler handler = new Handler();// handler to display images in UI thread
	Context context;
	boolean withScaleOption = false;
	public ImageLoader(Context context, boolean withScaleOption, int stub_id) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
		this.context = context;
		this.withScaleOption = withScaleOption;
		this.stub_id = stub_id;
	}
	

	public void DisplayImage(String url, ImageView imageView) {/*
		url = Uri.encode(url, ALLOWED_URI_CHARS);
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}
	*/

		 Glide.with(context)
	        .load(url)
	        .placeholder(stub_id)
	        .error(stub_id)
	        //.fallback(stub_id)
	        .into(imageView);	
	}

	public void DisplayImage2(String url, ImageView imageView, int res) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			queuePhoto(url, imageView);
			imageView.setImageResource(res);
		}
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f, withScaleOption);
		if (b != null)
			return b;

		// Try to get in assert folder
		String fileName = "cache/" + String.valueOf(url.hashCode());
		AssetManager assetManager = context.getAssets();
		InputStream in;
		try {
			in = assetManager.open(fileName);
			in.close();
			Bitmap a = decodeFile(fileName);
			if (a != null)
				return a;
		} catch (IOException e) {

		}

		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			conn.disconnect();
			bitmap = decodeFile(f, withScaleOption);
			return bitmap;
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(String fileName) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			AssetManager assetManager = context.getAssets();
			InputStream in1 = assetManager.open(fileName);
			BitmapFactory.decodeStream(in1, null, o);
			in1.close();

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 200;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp > REQUIRED_SIZE) {
					if (width_tmp / 2 < REQUIRED_SIZE
							|| height_tmp / 2 < REQUIRED_SIZE)
						break;
					width_tmp /= 2;
					height_tmp /= 2;
					scale *= 2;
				}
				else
					break;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			InputStream in2 = assetManager.open(fileName);
			Bitmap bitmap = BitmapFactory.decodeStream(in2, null, o2);
			in2.close();
			return bitmap;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f, boolean withSetOption) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			o.inPreferredConfig = Config.RGB_565;
			o.inSampleSize=2;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			//-----------------
			//haunt9 modified
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap;
			if(withSetOption) {
				// Find the correct scale value. It should be the power of 2.
				final int REQUIRED_SIZE =50;
				int width_tmp = o.outWidth;
				int height_tmp = o.outHeight;
				int scale = 1;
				/*hhaunt9while (true) {
					if (width_tmp > REQUIRED_SIZE) {
						if (width_tmp / 2 < REQUIRED_SIZE
								|| height_tmp / 2 < REQUIRED_SIZE)
							break;
						width_tmp /= 2;
						height_tmp /= 2;
						scale *= 2;
					}
					else
						break;
				}*/
				
				//haunt9
				 while(o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
			            scale*=2;
				//
				// decode with inSampleSize
				BitmapFactory.Options o2 = new BitmapFactory.Options();
				o2.inSampleSize = scale;
				//minhmh 21012015 fix out of memory
				
				o2.inPreferredConfig = Config.RGB_565;
				o2.inDither=false;                     //Disable Dithering mode
				o2.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
				o2.inInputShareable=true;   
				//o2.inJustDecodeBounds = true;
				//end minh
				bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			} else {
				bitmap = BitmapFactory.decodeStream(stream2, null, null);
			}
			try {
				stream2.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
			return bitmap;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			try {
				if (imageViewReused(photoToLoad))
					return;
				Bitmap bmp = getBitmap(photoToLoad.url);
				memoryCache.put(photoToLoad.url, bmp);
				if (imageViewReused(photoToLoad))
					return;
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
				handler.post(bd);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else
				photoToLoad.imageView.setImageResource(stub_id);
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

}