package rocks.athrow.android_book_listing_app;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class BookListActivity extends AppCompatActivity implements BookListFragment.OnListFragmentInteractionListener {
    private static final String LOG_TAG = BookListActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(LOG_TAG, "onCreate: " + true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_book_list);
        final EditText searchField = (EditText) findViewById(R.id.search_field);
        Log.e(LOG_TAG, "searchField: " + searchField);
        // Set the listeners on the search field
        if ( searchField != null ){

            searchField.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                   searchField.setCursorVisible(true);
                }
            });

            searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchField.setCursorVisible(false);
                    Context context = getApplicationContext();
                    CharSequence text = "Search!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    handled = true;
                }
                return handled;
            }
            });
        }
    }


    @Override
    public void onListFragmentInteraction(ArrayList<Book> mValues) {

    }
}
