package com.reactnativeblobjsihelper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.JavaScriptContextHolder;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.blob.BlobModule;

@ReactModule(name = BlobJsiHelperModule.NAME)
public class BlobJsiHelperModule extends ReactContextBaseJavaModule {
    public static final String NAME = "BlobJsiHelper";

    public BlobJsiHelperModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    public boolean install() {
        try {
            System.loadLibrary("reactnativeblobjsihelper");
            JavaScriptContextHolder jsContext = getReactApplicationContext().getJavaScriptContextHolder();
            nativeInstall(jsContext.get(), this);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public byte[] getBlobBuffer(String blobId, int offset, int size) {
      Log.d(NAME, "Resolving Blob #" + blobId + "... (" + offset + "..." + size + ")");
      BlobModule blobModule = getReactApplicationContext().getNativeModule(BlobModule.class);
      if (blobModule == null) throw new RuntimeException("React Native's BlobModule was not found!");
      byte[] bytes = blobModule.resolve(blobId, offset, size);
      if (bytes == null) {
        throw new RuntimeException("Failed to resolve Blob #" + blobId + "! Not found.");
      }
      Log.d(NAME, "Resolved Blob #" + blobId + "! Size: " + bytes.length);
      return bytes;
    }

    public static native void nativeInstall(long jsiPointer, BlobJsiHelperModule instance);
}
