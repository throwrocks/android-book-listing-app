package rocks.athrow.android_book_listing_app;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import rocks.athrow.android_book_listing_app.dummy.DummyContent;

public class BookListActivity extends AppCompatActivity implements BookListFragment.OnListFragmentInteractionListener {
    private static final String LOG_TAG = BookListActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(LOG_TAG, "onCreate: " + true);


        setContentView(R.layout.activity_book_list);
    }


    @Override
    public void onListFragmentInteraction(ContentValues[] mValues) {

    }
}
