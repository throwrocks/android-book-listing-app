package rocks.athrow.android_book_listing_app;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BookListFragment extends Fragment {
    private static final String LOG_TAG = BookListFragment.class.getSimpleName();
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private static ArrayList<Book> mValues;
    private BookListAdapter mAdapter = new BookListAdapter(mValues, mListener);
    private RecyclerView recyclerView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookListFragment() {
        Log.e(LOG_TAG, "constructor: " + true);

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BookListFragment newInstance(int columnCount) {
        Log.e(LOG_TAG, "instantiate: " + true);
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG_TAG, "onCreate: " + true);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(LOG_TAG, "onActivityCreated: " + true);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int searchFieldWidth = width - 318;
        Log.e(LOG_TAG, "searchFieldWidth: " + searchFieldWidth);
        final EditText searchField = (EditText) getActivity().findViewById(R.id.search_field);
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(searchFieldWidth, LinearLayout.LayoutParams.MATCH_PARENT); // Width , height
        searchField.setLayoutParams(lparams);
        final Spinner maxResultsField = (Spinner) getActivity().findViewById(R.id.max_results_spinner);
        //Log.e(LOG_TAG, "searchField: " + searchField);
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
                    Log.e(LOG_TAG, "editorAction: " + true);
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        String maxResultsString = maxResultsField.getSelectedItem().toString();
                        int maxResults = Integer.parseInt(maxResultsString);
                        Log.e(LOG_TAG, "search: " + true);
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
        }
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.max_results, R.layout.spinner_max_results);
        adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        maxResultsField.setAdapter(adapter);
        maxResultsField.setOnItemSelectedListener(new MaxResultsSpinnerListener());
    }

    private void getBooks(String searchCriteria, int maxResults){
        Log.e(LOG_TAG, "getBooks: " + true);
        FetchTask fetch = new FetchTask(getActivity(), new FetchTask.AsyncResponse(){
            @Override
            public void processFinish(ArrayList<Book> output) {
                Log.e(LOG_TAG, "processFinish: " + mValues);
                mValues = output;
                mAdapter = new BookListAdapter(mValues, mListener);
                recyclerView.setAdapter(mAdapter);
                if ( mValues == null ){
                    Toast toast = Toast.makeText(getContext(),"Nothing found :(", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        }, searchCriteria, maxResults);
        fetch.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booklist_list, container, false);
        Log.e(LOG_TAG, "columnCount: " + mColumnCount);
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

    public class MaxResultsSpinnerListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String selected = parent.getItemAtPosition(pos).toString();
            int maxResults = Integer.parseInt(selected);
            final EditText searchField = (EditText) getActivity().findViewById(R.id.search_field);
            String searchCriteria = searchField.getText().toString();
            Log.e(LOG_TAG, "max selected: " + true);
            Log.e(LOG_TAG, "search criteria: " + searchCriteria.isEmpty());
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



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ArrayList<Book> mValues);
    }
}
