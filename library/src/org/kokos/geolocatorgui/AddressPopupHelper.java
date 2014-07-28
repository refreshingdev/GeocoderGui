package org.kokos.geolocatorgui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

public class AddressPopupHelper {
    private final Context context;

    public AddressPopupHelper(Context context) {
        this.context = context;
    }

    public void show(Address address, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.location_info, null);
        TextView locationNameValueText = (TextView) contentView.findViewById(R.id.locationNameValue);
        locationNameValueText.setText(address.getLocality());
        TextView featureValueText = (TextView) contentView.findViewById(R.id.locationFeatureValue);
        featureValueText.setText(address.getFeatureName());

        PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }
}
