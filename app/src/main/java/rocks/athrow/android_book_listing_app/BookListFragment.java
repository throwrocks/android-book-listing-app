package rocks.athrow.android_book_listing_app;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class BookListFragment extends Fragment {
    // --Commented out by Inspection (6/4/2016 9:21 AM):private static final String LOG_TAG = BookListFragment.class.getSimpleName();
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private static ArrayList<Book> mValues;
    private BookListAdapter mAdapter = new BookListAdapter(mValues);
    private RecyclerView recyclerView;
    // Constructor
    public BookListFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set listeners on input fields and other setups
        final EditText searchField = (EditText) getActivity().findViewById(R.id.search_field);
        final Spinner maxResultsField = (Spinner) getActivity().findViewById(R.id.max_results_spinner);
        // Automatically set the width of the search EditText
        // So when the device is rotated to landscape the field is enlarged
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int searchFieldWidth = width - 318;
        final LinearLayout.LayoutParams searchFieldParams = new LinearLayout.LayoutParams(searchFieldWidth, LinearLayout.LayoutParams.MATCH_PARENT); // Width , height
        searchField.setLayoutParams(searchFieldParams);
        // Set the listeners on the SearchField
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
                        String maxResultsString = maxResultsField.getSelectedItem().toString();
                        int maxResults = Integer.parseInt(maxResultsString);
                        searchField.setCursorVisible(false);
                        String searchCriteria = searchField.getText().toString();
                        getBooks(searchCriteria,maxResults);
                        View view = getActivity().getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        handled = true;
                    }
                    return handled;
                }
            });
        // Setup the Max Results Spinner
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.max_results, R.layout.spinner_max_results);
        adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        maxResultsField.setAdapter(adapter);
        maxResultsField.setOnItemSelectedListener(new MaxResultsSpinnerListener());
    }

    /**
     * getBooks
     * This methods handles calling the FetchTask method to query the Google Books API
     * @param searchCriteria the search text entered into the SearchField EditText
     * @param maxResults the number of results to be returned from the API selected
     *                   in the MaxResults Spinner
     */
    private void getBooks(String searchCriteria, int maxResults){
        final LinearLayout noConnection = (LinearLayout) getActivity().findViewById(R.id.no_connection);
        final LinearLayout emptyResults = (LinearLayout) getActivity().findViewById(R.id.empty_results);

        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if ( isConnected ){
            noConnection.setVisibility(View.GONE);
            FetchTask fetch = new FetchTask(getActivity(), new FetchTask.AsyncResponse(){
            @Override
            public void processFinish(ArrayList<Book> output) {
                mValues = output;
                mAdapter = new BookListAdapter(mValues);
                recyclerView.setAdapter(mAdapter);
                // If nothing found, show a message
                if ( mValues == null ){
                    emptyResults.setVisibility(View.VISIBLE);

                }else{
                    emptyResults.setVisibility(View.GONE);

                }
            }
        }, searchCriteria, maxResults);
        fetch.execute();
     }else{
            emptyResults.setVisibility(View.GONE);
            noConnection.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booklist_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }
        return view;
    }

    /**
     * MaxResultsSpinnerListener
     * Handle a new Spinner selection
     * Selecting a new value will run the getBooks method to query the Google Books API
     */
    private class MaxResultsSpinnerListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String selected = parent.getItemAtPosition(pos).toString();
            int maxResults = Integer.parseInt(selected);
            final EditText searchField = (EditText) getActivity().findViewById(R.id.search_field);
            String searchCriteria = searchField.getText().toString();
            if ( !searchCriteria.isEmpty() ){ getBooks(searchCriteria,maxResults); }
        }
        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // --Commented out by Inspection (6/4/2016 9:21 AM):void onListFragmentInteraction(ArrayList<Book> mValues);
    }
}
