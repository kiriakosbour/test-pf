package gr.deddie.pfr.services;

import gr.deddie.pfr.managers.AUTHDataManager;
import gr.deddie.pfr.managers.PFRDataManager;
import gr.deddie.pfr.model.Failure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class WorkforceService {
    private static Logger logger = LogManager.getLogger(WorkforceService.class);
    private static PFRDataManager pfrDataManager;
    private static AUTHDataManager authDataManager;

    public List<Failure> getActiveFailuresByTexniths(String token) throws Exception {
        pfrDataManager = new PFRDataManager();
        authDataManager = new AUTHDataManager();

        String am = authDataManager.getAM(token);
        return pfrDataManager.getActiveFailuresWithCoordsperTexnith(am);
    }
}
