# android-book-listing-app

I created this app as part of the Udacity Code Reviewer training. It has a search box below the action bar. When you search, an AsyncTask fires up to call the Google Books Api, gets the JSON response, parses it, and returns the data as an ArrayList of custom Book objects. The lists is made with a RecyclerView and is populated with an Adapter. Next to the search box is a Spinner that allows you select the max results (10, 20, or 40). The Spinner uses a custom Layout that pulls the values from a string-array in the string resources file.

![Android Book Listing App uses the Google Books API](http://throw.rocks/android-projects/book-listing/book-listing.png)
