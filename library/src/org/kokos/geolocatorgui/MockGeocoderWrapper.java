package org.kokos.geolocatorgui;

import android.content.Context;
import android.location.Address;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * For manual tests.
 */
public class MockGeocoderWrapper extends GeocoderWrapper {
    public MockGeocoderWrapper(Context context) {
        super(context);
    }

    @Override
    public boolean isGeocoderPresent() {
        return true;
    }

    @Override
    protected List<Address> getFromLocationName(String locationName) throws IOException {
        ArrayList<Address> addresses = new ArrayList<Address>(3);
        addresses.add(getAddress(1));
        addresses.add(getAddress(2));
        addresses.add(getAddress(3));
        return addresses;
    }

    private Address getAddress(int aNumber) {
        Address address = new Address(Locale.ENGLISH);
        address.setAddressLine(0, "Union Plaza");
        address.setAddressLine(1, aNumber + " Union St");
        address.setPostalCode("PC");
        address.setAdminArea("AA");
        address.setSubAdminArea("SAA");
        address.setLocality("New York");
        address.setSubLocality("Manhattan");
        address.setThoroughfare("TF");
        address.setSubThoroughfare("STF");
        address.setPremises("Premises");
        address.setCountryName("United States");
        address.setCountryCode("US");
        address.setLatitude(40);
        address.setLongitude(-90 + aNumber);
        address.setFeatureName("a building?");
        return address;
    }
}
