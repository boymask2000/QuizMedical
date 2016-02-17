package com.posbeu.utils;

import android.graphics.Bitmap;

public class ImageHandler {
	public static String bitmap2string(Bitmap bitmap) {

		Base64CODEC cod = new Base64CODEC();
		String base64String = cod.convertToBase64(bitmap);
		
		return base64String;
	}
	public static Bitmap string2bitmap( String val){
		Base64CODEC cod = new Base64CODEC();
		return cod.convertToBitmap(val);
	}
}
