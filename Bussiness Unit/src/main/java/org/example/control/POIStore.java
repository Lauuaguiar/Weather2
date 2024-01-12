package org.example.control;
import org.example.model.Location;
import org.example.model.POI;
public interface POIStore {
    void save(POI poi, Location location);
}