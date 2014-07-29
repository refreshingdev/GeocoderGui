package org.kokos.geolocatorgui;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.kokos.util.KDialogs;
import org.kokos.util.Consumer;
import org.kokos.util.KArrayAdapter;

import java.util.ArrayList;
import java.util.List;


public class LocationFromNameActivity extends Activity {

    public static final int ERROR_RESULT = -1;
    public static final int OK_RESULT = 0;
    public static final String ADDRESSES_RESULT_EXTRA = "addresses";
    private GeocoderWrapper geocoderWrapper;
    private TextView noResultsOrErrorText;
    private EditText locationNameEdit;
    private ImageButton retryButton;
    private ProgressBar spinner;
    private ListView resultsList;
    private List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        geocoderWrapper = new GeocoderWrapper(this);
        if (geocoderWrapper.isGeocoderPresent()) {
            setContentView(R.layout.activity_location_from_name);
            //geocoderWrapper = new MockGeocoderWrapper(this);
            noResultsOrErrorText = (TextView) findViewById(R.id.noResultsOrError);
            resultsList = (ListView) findViewById(R.id.resultsList);
            locationNameEdit = (EditText) findViewById(R.id.locationNameEdit);
            spinner = (ProgressBar) findViewById(R.id.spinner);
            retryButton = (ImageButton) findViewById(R.id.retryButton);
            initializeLocationEditListener();
        } else {
            displayErrorDialogAndGoBack(R.string.geocoderNotAvailable);
        }
    }

    private void initializeLocationEditListener() {
        AfterTextChangedListener afterTextChangedListener = new AfterTextChangedListener() {
            @Override
            public void afterTextChanged(CharSequence newText) {
                handleTextChanged(newText);
            }
        };

        final DelayedTextChangedHandler textChangedHandler = new DelayedTextChangedHandler(
                locationNameEdit, afterTextChangedListener);
        textChangedHandler.startListening();

        resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //displayAddressPopup(position); // TODO finish this feature
                returnAddressToCallerActivity(position);
            }
        });
    }

    private void handleTextChanged(CharSequence newText) {
        retryButton.setVisibility(View.GONE);
        resultsList.setVisibility(View.INVISIBLE);
        if (spinner.getVisibility() == View.GONE) {
            spinner.setVisibility(View.INVISIBLE);
        }

        if (newText.length() >= 3) {
            spinner.setVisibility(View.VISIBLE);
            String locationName = locationNameEdit.getText().toString();
            GeocoderWrapper.NotifyingAsyncTask asyncTask
                    = geocoderWrapper.getLocationsFromNameAsync(locationName);
            asyncTask.setPostExecuteConsumer(new Consumer<List<Address>>() {

                @Override
                public void consume(List<Address> addresses) {
                    spinner.setVisibility(View.INVISIBLE);
                    LocationFromNameActivity.this.addresses = addresses;
                    displayResults();
                }
            });
            asyncTask.execute();
        }
    }

    public void onReloadClick(View view) {
        handleTextChanged(locationNameEdit.getText());
    }

    private void displayResults() {
        if (addresses == null) {
            // error
            spinner.setVisibility(View.GONE);
            retryButton.setVisibility(View.VISIBLE);

            resultsList.setVisibility(View.GONE);
            noResultsOrErrorText.setVisibility(View.VISIBLE);
            noResultsOrErrorText.setText(R.string.geocoderError);
        } else if (addresses.isEmpty()) {
            // no results
            resultsList.setVisibility(View.GONE);
            noResultsOrErrorText.setVisibility(View.VISIBLE);
            noResultsOrErrorText.setText(R.string.noLocationResult);
        } else {
            // successful search
            noResultsOrErrorText.setVisibility(View.GONE);
            resultsList.setVisibility(View.VISIBLE);
            resultsList.setAdapter(new KArrayAdapter<String>(this, getShortAdressTexts(addresses)));
        }
        //resultsList.getParent().requestLayout(); // TODO needed?
    }

    private List<String> getShortAdressTexts(List<Address> addresses) {
        ArrayList<String> result = new ArrayList<String>(addresses.size());
        for (Address address : addresses) {
            result.add(formatAddress(address));
        }
        return result;
    }

    private String formatAddress(Address address) {
        StringBuilder result = new StringBuilder();
        if (!TextUtils.isEmpty(address.getFeatureName())) {
            result.append(address.getFeatureName()).append(", ");
        }
        if (!TextUtils.isEmpty(address.getLocality())
                && !address.getLocality().equals(address.getFeatureName())) {
            result.append(address.getLocality()).append(", ");
        }

        if (!TextUtils.isEmpty(address.getCountryCode())) {
            result.append(address.getCountryCode());
        } else {
            // for some reason (probably error) some places eg. Mount Rainier don't have countryCode set,
            // but country is in the last address line
            String lastAddressLine = address.getAddressLine(address.getMaxAddressLineIndex());
            if (!TextUtils.isEmpty(lastAddressLine)) {
                result.append(lastAddressLine);
            }
        }
        return result.toString();
    }

    private void displayAddressPopup(int position) {
        new AddressPopupHelper(this).show(addresses.get(position), (ViewGroup)resultsList.getParent());
    }

    private void returnAddressToCallerActivity(int position) {
        // TODO display dialog with more address info, if okayed than return the address
        Intent data = new Intent();
        data.putExtra(ADDRESSES_RESULT_EXTRA, addresses.get(position));
        setResult(OK_RESULT, data);
        finish();
    }

    private void displayErrorDialogAndGoBack(int messageId) {
        new KDialogs(this).informAndDo(messageId, new Runnable() {
            @Override
            public void run() {
                setResult(ERROR_RESULT);
                finish();
            }
        });
    }
}
