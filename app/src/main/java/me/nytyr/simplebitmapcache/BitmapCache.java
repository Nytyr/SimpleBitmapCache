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
package me.nytyr.simplebitmapcache;

import android.graphics.Bitmap;
import android.util.Log;

import me.nytyr.simplebitmapcache.cache.Cache;
import me.nytyr.simplebitmapcache.handlers.BitmapReceiver;
import me.nytyr.simplebitmapcache.http.BitmapGetter;
import me.nytyr.simplebitmapcache.utils.ThreadTask;

public class BitmapCache {

    public final static String TAG = "BitmapCache";

    private Cache cache;
    private BitmapGetter bitmapGetter;

    public BitmapCache(Cache cache, BitmapGetter bitmapGetter) {
        this.cache = cache;
        this.bitmapGetter = bitmapGetter;
    }

    public void get(final String URL, final BitmapReceiver receiver) {
        new ThreadTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                if (URL == null) {
                    return null;
                }
                final Bitmap bitmap = cache.get(URL);
                if (bitmap != null) {
                    Log.i(TAG, "Getting image from cache... "+URL);
                    return bitmap;
                }

                Log.d(TAG, "Getting image from internet... "+URL);
                final Bitmap internetBitmap = bitmapGetter.get(URL);
                if (internetBitmap != null) {
                    cache.put(URL, internetBitmap);
                }
                return internetBitmap;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                receiver.onReceive(result);
            }

        }.run();
    }

}
