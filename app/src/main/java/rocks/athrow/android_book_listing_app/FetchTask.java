package rocks.athrow.android_book_listing_app;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by josel on 6/1/2016.
 */
public class FetchTask extends AsyncTask<String, Void, ArrayList<Book>> {

    private static final String LOG_TAG = FetchTask.class.getSimpleName();
    private final Context mContext;
    public AsyncResponse delegate;

    public interface AsyncResponse {
        void processFinish(ArrayList<Book> output);
    }

    public FetchTask(Context context, AsyncResponse delegate){
        this.mContext = context;
        this.delegate = delegate;

    }


    @Override
    protected ArrayList<Book> doInBackground(String... params){

        ArrayList<Book> parsedResults;
        API mApi = new API(mContext);
        String jsonResults = mApi.callAPI();



        //Log.e(LOG_TAG, "results " + jsonResults);
        if ( jsonResults != null ) {
            try {
                JSONObject jsonObject = new JSONObject(jsonResults);
                JSONArray resultsArray = jsonObject.getJSONArray("items");
                int countResults = resultsArray.length();
                parsedResults = new ArrayList<>();
                // Loop through the resultsArray to parse each Json object needed
                for (int i = 0; i < countResults; i++) {
                    JSONObject bookRecord = resultsArray.getJSONObject(i);
                    // Get the volume info node from the book record
                    JSONObject bookVolumeInfo = bookRecord.getJSONObject("volumeInfo");
                    // Get the title from the volume info
                    String bookTitle = bookVolumeInfo.getString("title");
                    //String bookTitle = bookVolumeInfo.getString("title");
                    // Get the authors node
                    JSONArray bookAuthors = bookVolumeInfo.getJSONArray("authors");
                    // Count the authors
                    int countAuthors = bookAuthors.length();
                    Log.e(LOG_TAG, "countAuthors: " + countAuthors);
                    String bookAuthorsString = "";
                    for (int e = 0; e < countAuthors; e++) {
                        String author =  bookAuthors.getString(e);
                        if ( bookAuthorsString.isEmpty() ){ bookAuthorsString = author; }
                        else if ( e == countAuthors - 1 ){ bookAuthorsString = bookAuthorsString + " and " + author; }
                        else { bookAuthorsString = bookAuthorsString + ", " + author; }
                    }

                    Log.e(LOG_TAG, "Parsing string: " + bookTitle + " " + bookAuthorsString );
                    // Create a Book object
                    Book mBook = new Book(bookTitle, bookAuthorsString);

                    parsedResults.add(i, mBook);
                }
                // Return the results
                return parsedResults;
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    return null;
    }


    @Override
    protected void onPostExecute(ArrayList<Book> parsedResults){
        delegate.processFinish(parsedResults);
    }



}



