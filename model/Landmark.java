package gr.deddie.pfr.model;

import java.util.List;

/**
 * Created by M.Masikos on 6/3/2017.
 */
public class Landmark {
    public enum LandmarkType {MARKER, POLYGON}

    private Integer id;
    private LandmarkType type;
    private List<Point> pointsList;
    private Long failure_id;

    public Landmark() {
    }

    public Landmark(Integer id, LandmarkType type, List<Point> pointsList, Long failure_id) {
        this.id = id;
        this.type = type;
        this.pointsList = pointsList;
        this.failure_id = failure_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LandmarkType getType() {
        return type;
    }

    public void setType(LandmarkType type) {
        this.type = type;
    }

    public List<Point> getPointsList() {
        return pointsList;
    }

    public void setPointsList(List<Point> pointsList) {
        this.pointsList = pointsList;
    }

    public Long getFailure_id() {
        return failure_id;
    }

    public void setFailure_id(Long failure_id) {
        this.failure_id = failure_id;
    }

//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
}
