package com.alinz.parkerdan.shareextension;

import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import android.graphics.Bitmap;
import java.io.InputStream;
import java.net.URI;

public class ShareModule extends ReactContextBaseJavaModule {

  public ShareModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "ReactNativeShareExtension";
  }

  @ReactMethod
  public void close() {
    getCurrentActivity().finish();
  }

  @ReactMethod
  public void data(Promise promise) {
    promise.resolve(processIntent());
  }

  public WritableMap processIntent() {
    WritableMap map = Arguments.createMap();

    String value = "";
    String type = "";
    String action = "";
    String url = "";
    Activity currentActivity = getCurrentActivity();

    if (currentActivity != null) {
      Intent intent = currentActivity.getIntent();
      action = intent.getAction();
      type = intent.getType();
      if (type == null) {
        type = "";
      }
      if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
        value = intent.getStringExtra(Intent.EXTRA_TEXT);
      } else if (Intent.ACTION_SEND.equals(action) && ("image/*".equals(type) || "image/jpeg".equals(type)
          || "image/png".equals(type) || "image/jpg".equals(type) || type.startsWith("application/"))) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        try {
          if(uri.toString().contains("google")){
            value = uri.toString();
          }else {
            url = uri.toString();
            value = "file://" + RealPathUtil.getFileFromUri(currentActivity, uri);
          }

        } catch (Exception e) {

        }

      } else {
        value = "";
        url = "";
      }
    } else {
      value = "";
      type = "";
      url = "";
    }
    map.putString("url",url);
    map.putString("type", type);
    map.putString("value", value);

    return map;
  }
}
