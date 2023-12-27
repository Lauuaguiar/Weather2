package org.example.control;

import org.example.model.Location;
import org.example.model.POI;

import java.util.List;

public interface POISupplier {

    public List<POI> getPOIs(Location location);
}
