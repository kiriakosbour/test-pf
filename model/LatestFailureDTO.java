package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LatestFailureDTO extends Failure{

    public LatestFailureDTO(Failure f) {
        this.setStatus(f.getStatus());
        this.setSupply_no(f.getSupply_no());
    }
}
