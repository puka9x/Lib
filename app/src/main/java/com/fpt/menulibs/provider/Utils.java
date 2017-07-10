package com.fpt.menulibs.provider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

@SuppressLint("SimpleDateFormat")
public class Utils {
	public static void startActivityWithoutTransition(Activity currentActivity,
			Class<? extends Activity> newActivitiy) {
		startActivityWithoutTransition(currentActivity, new Intent(
				currentActivity, newActivitiy));
	}
    public static boolean isGingerbreadOrLater()
    {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD;
    }
	// If targetLocation is exist, it does not copy.
	public static void copyDirectory(File sourceLocation, File targetLocation)
			throws IOException {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists() && !targetLocation.mkdirs()) {
				throw new IOException("Cannot create dir "
						+ targetLocation.getAbsolutePath());
			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(
						targetLocation, children[i]));
			}
		} else {

			// make sure the directory we plan to store the recording in exists
			File directory = targetLocation.getParentFile();
			if (directory != null && !directory.exists() && !directory.mkdirs()) {
				throw new IOException("Cannot create dir "
						+ directory.getAbsolutePath());
			}

			//if (!targetLocation.exists()) {
				InputStream in = new FileInputStream(sourceLocation);
				OutputStream out = new FileOutputStream(targetLocation);

				// Copy the bits from instream to outstream
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			//}
		}
	}

	public static void startActivityWithoutTransition(Activity currentActivity,
			Intent intent) {
		currentActivity.startActivity(intent);
		disablePendingTransition(currentActivity);
		currentActivity.finish();
	}

	public static void disablePendingTransition(Activity activity) {

		// Activity.overridePendingTransition() was introduced in Android 2.0.
		// Use reflection to maintain
		// compatibility with 1.5.
		try {
			Method method = Activity.class.getMethod(
					"overridePendingTransition", int.class, int.class);
			method.invoke(activity, 0, 0);
		} catch (Throwable x) {
			// Ignored
		}
	}

	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		boolean connected = networkInfo != null && networkInfo.isConnected();

		boolean wifiConnected = connected
				&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
		// boolean wifiRequired = isWifiRequiredForDownload(context);

		// return connected && (!wifiRequired || wifiConnected);
		return connected && (wifiConnected);
	}

	private static String uniqueID = null;
	private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

	public synchronized static String getDeviceID(Context context) {
		if (uniqueID == null) {
			SharedPreferences sharedPrefs = context.getSharedPreferences(
					PREF_UNIQUE_ID, Context.MODE_PRIVATE);
			uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
			if (uniqueID == null) {
				uniqueID = UUID.randomUUID().toString();
				Editor editor = sharedPrefs.edit();
				editor.putString(PREF_UNIQUE_ID, uniqueID);
				editor.commit();
			}
		}
		return uniqueID;
	}

	
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");

	/**
	 * Check valid of email address
	 * 
	 * @param email
	 *            email
	 * @return true - correct, false - incorrect
	 */
	public static boolean isEmailValid(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	public static final String md5(final String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			String str = hexString.toString();
			return str;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String toBase64fromString(String text) {
		byte bytes[] = text.getBytes();
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}


	@SuppressLint("SimpleDateFormat")
	public static boolean ShowNotifiBeforeTime(String time){
		boolean result = false;
		try {
			String date = time;
		    String now = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date());
		    SimpleDateFormat dateFormat = new SimpleDateFormat(
		            "dd/MM/yyyy hh:mm:ss");
		    Date convertedDate = new Date();
		    Date convertedNow = new Date();
		    try {
		        convertedDate = dateFormat.parse(date);
		        convertedNow = dateFormat.parse(now);
		        long timecurrent = convertedNow.getTime() - convertedDate.getTime();
		        if (timecurrent > 0) {
		        	 result = false;
		        } else {
		        	 result = true;
		        }
		    } catch (Exception e) {
		    	
		    }
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return result;
	}
}
