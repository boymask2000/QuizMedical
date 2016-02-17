package com.posbeu.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

public class FileUtils {
	public static File createImageFile(String suffix) throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);
		System.out.println(image.getAbsolutePath());
		// Save a file: path for use with ACTION_VIEW intents
	//	mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}
}
