package com.alex.dailynews.data;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {
    // Authority to access content provider
    public static final String AUTHORITY = "com.alex.dailynews";

    // The base content URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "favorites" directory
    public static final String PATH_ARTICLES = "articles";
    // This is the path for "My Blogs And Websites" directory
    public static final String PATH_MYDATA = "mydata";
    public static final Uri URI_CONTENT = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MYDATA);

    /* ContractValues is an inner class that defines the contents of the favorites table */
    public static final class ContractValues implements BaseColumns {

        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.dailynews.articles";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.dailynews.article";

        // public static final Uri BASE_URI = Uri.parse("content://"+AUTHORITY);
        // ContractValues content URI
        public static final Uri CONTENT_URI1 = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ARTICLES).build();
        public static final Uri CONTENT_URI2 = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MYDATA).build();

        // Table names
        public static final String TABLE_NEWS = "NewsTable";
        public static final String TABLE_SOURCES = "SourcesTable";
        public static final String TABLE_FAVORITE = "FavoriteTable";
        public static final String TABLE_MYDATA = "mydata";

        // Table contents
        // public static final String ID = "id";
        public static final String AUTHOR = "author";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String URL = "url";
        public static final String URL_TO_IMAGE = "urlToImage";
        public static final String PUBLISHED_AT = "publishedAt";
        public static final String SOURCES_NAME = "sourcesName";
        public static final String SOURCES_ID = "sourcesId";

        public static final String SOURCE = "source";
        public static final String IS_SELECTED = "isSelected";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_URL = "url";

        public static Uri buildDirUri() {
            return BASE_CONTENT_URI.buildUpon().appendPath("articles").build();
        }

        /**
         * Matches: /items/[_id]/
         */
        public static Uri buildItemUri(long TITLE) {
            return BASE_CONTENT_URI.buildUpon().appendPath("articles").appendPath(Long.toString(TITLE)).build();
        }

        /**
         * Read item ID item detail URI.
         */
        public static long getItemId(Uri itemUri) {
            return Long.parseLong(itemUri.getPathSegments().get(1));
        }
    }
}
