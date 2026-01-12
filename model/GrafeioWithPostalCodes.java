package gr.deddie.pfr.model;

import java.util.List;

/**
 * Created by M.Masikos on 5/1/2017.
 */
public class GrafeioWithPostalCodes extends Grafeio{
    private List<PostalCode> postalCodeList;

    public List<PostalCode> getPostalCodeList() {
        return postalCodeList;
    }

    public void setPostalCodeList(List<PostalCode> postalCodeList) {
        this.postalCodeList = postalCodeList;
    }
}
