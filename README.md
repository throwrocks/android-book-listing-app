# Android Book Listing

This project is part of the Udacity Android Basics Nanodegree. You can read the project description [here](https://github.com/udacity/Project-Descriptions-for-Review/blob/master/Beginner%20Android/Book_Listing.md).

The app provides a search field to query the Google Books API and display the results in a list. The app calls the API, gets the JSON response, parses it, and returns the data as an ArrayList of custom Book objects. The list is implemented with a RecyclerView and populated with a RecyclerView.Adapter. You can specify the number of results to retrieve from the API with a spinner. The spinner implements a custom layout that gets the options from a string-array in the strings resources file.

You can see how how the app looks below.

![Android Book Listing App uses the Google Books API](http://throw.rocks/android-projects/book-listing/book-listing-20160830.png)
