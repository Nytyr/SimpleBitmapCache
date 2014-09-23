/*
 * Copyright 2014 Nytyr [hi at nytyr dot me]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.nytyr.simplebitmapcache.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DiskCache implements Cache {

    private Context context;

    private final static String TAG = "DiskCache";

    public DiskCache(Context context){
        this.context = context;
    }

    @Override
    public void put(String key, Bitmap value) {
        Log.i(TAG, "Write disk cache. Filename = " + key);
        FileOutputStream out = null;
        try {
            out = context.openFileOutput(getCleanName(key), Context.MODE_PRIVATE);
            value.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Bitmap get(String key) {
        Log.i(TAG, "Read disk cache. Filename = " + key);
        try {
            File filePath = context.getFileStreamPath(getCleanName(key));
            FileInputStream fi = new FileInputStream(filePath);
            return BitmapFactory.decodeStream(fi);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getCleanName(String name) {
        return name.replaceAll("/", "");
    }
}
