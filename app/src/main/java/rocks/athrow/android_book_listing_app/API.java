package rocks.athrow.android_book_listing_app;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by josel on 6/1/2016.
 */
class API {
    private static final String LOG_TAG = API.class.getSimpleName();

    private final Context mContext;

    private static final String apiURI = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=10";

    // Constructor
    public API(Context context){
        this.mContext = context;

    }

    /**
     * callAPI
     * @return
     */
    public String callAPI (){
        Log.e(LOG_TAG, "callApiString " + apiURI);
        String results = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            Log.e(LOG_TAG, "buildTheURL " + true);
            // Build the URL
            Uri builtUri = Uri.parse(apiURI).buildUpon().build();
            URL url = new URL(builtUri.toString());
            // Establish the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                results = null;
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging ->  + "\n"
                buffer.append(line);
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                results = null;
            }
            results = buffer.toString();
        } catch (IOException v) {
            results = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //Log.e(LOG_TAG, "results: " + results);
        return results;
    }
}
