package rocks.athrow.android_book_listing_app;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {
    private static final String LOG_TAG = BookListAdapter.class.getSimpleName();
    private final ArrayList<Book> mValues;
    private final BookListFragment.OnListFragmentInteractionListener mListener;

    public BookListAdapter(ArrayList<Book> items, BookListFragment.OnListFragmentInteractionListener listener) {
        Log.e(LOG_TAG, "constructor: " + true);
        Log.e(LOG_TAG, "items: " + items);
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_booklist, parent, false);
        Log.e(LOG_TAG, "onCreateViewHolder: " + true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.e(LOG_TAG, "onBindViewHolder: " + true);

            Log.e(LOG_TAG, "onBindViewHolder: " + mValues.size());
            holder.mItem = mValues.get(position);
            holder.mTitleView.setText(mValues.get(position).title);
            String authorsString = "by " + mValues.get(position).authors;
            holder.mAuthorsView.setText(authorsString);

            Log.e(LOG_TAG, "setAdapter: " + mValues.get(position));
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        //mListener.onListFragmentInteraction(holder.mItem);
                    }
                }
            });

    }
    @Override
    public int getItemCount() {

        if ( mValues != null ) {
            Log.e(LOG_TAG, "getItemCount: " + mValues.size());
            return mValues.size();
        }else{
            Log.e(LOG_TAG, "getItemCount: " + "zero");
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mAuthorsView;
        public Book mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mAuthorsView = (TextView) view.findViewById(R.id.authors);
        }


    }
}
