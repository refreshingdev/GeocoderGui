package org.kokos.geolocatorgui.runner;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;
import org.kokos.geolocatorgui.LocationFromNameActivity;

import java.util.Arrays;

public class GeocoderGuiRunnerActivity extends Activity {

    public static final int REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onNameToLocationClick(View view) {
        Intent explicitIntent = new Intent(this, LocationFromNameActivity.class);
        startActivityForResult(explicitIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            TextView results = (TextView) findViewById(R.id.results);
            if (data != null) {
                Address address = data.getParcelableExtra(
                        LocationFromNameActivity.ADDRESSES_RESULT_EXTRA);
                results.setText(address.toString());
            }
        }
    }
}
