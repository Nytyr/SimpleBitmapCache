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
package me.nytyr.simplebitmapcache.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import me.nytyr.simplebitmapcache.io.FlushedInputStream;

public class HttpBitmapGetter implements BitmapGetter {

    public final static String TAG = "HttpBitmapGetter";

    private int httpTimeout;

    public HttpBitmapGetter() {
        this.httpTimeout = 30000;
    }

    public HttpBitmapGetter(int timeout) {
        this.httpTimeout = timeout;
    }

    @Override
    public Bitmap get(final String URL) {
        // Making HTTP request
        try {

            final HttpParams httpParams = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParams, httpTimeout);

            HttpConnectionParams.setSoTimeout(httpParams, httpTimeout);
            HttpConnectionParams.setTcpNoDelay(httpParams, true);

            ConnManagerParams.setTimeout(httpParams, httpTimeout);

            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpGet httpPost = new HttpGet(URL);


            HttpResponse httpResponse = httpClient.execute(httpPost);
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            HttpEntity httpEntity = httpResponse.getEntity();
            final FlushedInputStream is = new FlushedInputStream(httpEntity.getContent());

            if (statusCode != 200) {
                Log.e(TAG, "Error downloading image. Status code > " + statusCode);
                return null;
            }
            return BitmapFactory.decodeStream(is);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
