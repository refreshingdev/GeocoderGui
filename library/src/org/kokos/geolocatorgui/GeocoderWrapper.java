package org.kokos.geolocatorgui;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import org.kokos.util.Consumer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Asynchrony for the Geocoder.
 */
public class GeocoderWrapper {
    private final Context context;
    private NotifyingAsyncTask asyncTask;

    public GeocoderWrapper(Context context) {
        this.context = context;
    }

    public synchronized NotifyingAsyncTask getLocationsFromNameAsync(
            final String locationName) {
        Log.d(getClass().getSimpleName(), "getLocationsFromNameAsync " + locationName);

        if (asyncTask != null) {
            asyncTask.cancel(true);
        }

        asyncTask = new NotifyingAsyncTask() {
            @Override
            protected List<Address> doInBackground(Void... params) {
                if (isGeocoderPresent()) {
                    List<Address> addresses;
                    try {
                        addresses = getFromLocationName(locationName);
                    } catch (IOException e) {
                        Log.d(getClass().getSimpleName(), e.getMessage());
                        // we may not rethrow, because AsynchTask doesn't deal with exceptions
                        return null; // null signals an error to postExecuteConsumer
                        // and we are not interested what it is (probably network error)
                    }
                    if (addresses == null) addresses = Collections.emptyList();
                    return addresses;
                } else {
                    return null;
                    // not distinguishable from IOException on getFromLocationName(),
                    // therefore isGeocoderPresent() should be used by clients
                }
            }
        };

        return asyncTask;
    }

    protected List<Address> getFromLocationName(String locationName) throws IOException {
        return new Geocoder(context).getFromLocationName(locationName, 5);
    }

    public boolean isGeocoderPresent() {
        return Geocoder.isPresent();
    }

    public abstract static class NotifyingAsyncTask extends AsyncTask<Void, Void, List<Address>> {
        private Consumer<List<Address>> postExecuteConsumer;

        @Override
        protected void onPostExecute(List<Address> addresses) {
            if (postExecuteConsumer != null)
                postExecuteConsumer.consume(addresses);
        }

        /**
         * @param postExecuteConsumer receives null if geocoder is not present or an error occured,
         *  or empty list if there are no results
         */
        public void setPostExecuteConsumer(Consumer<List<Address>> postExecuteConsumer) {
            this.postExecuteConsumer = postExecuteConsumer;
        }
    }
}
