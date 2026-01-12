package gr.deddie.pfr.model;

/**
 * Created by M.Masikos on 6/3/2017.
 */
public class Point {
    private Integer id;
    private String lon;
    private String lat;
    private Integer landmark_id;

    public Point() {
    }

    public Point(Integer id, String lon, String lat, Integer landmark_id) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
        this.landmark_id = landmark_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Integer getLandmark_id() {
        return landmark_id;
    }

    public void setLandmark_id(Integer landmark_id) {
        this.landmark_id = landmark_id;
    }
}
