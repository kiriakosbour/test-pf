package gr.deddie.pfr.services;

import gr.deddie.pfr.managers.GRAFEIODataManager;
import gr.deddie.pfr.model.GrafeioWithPostalCodes;
import gr.deddie.pfr.model.Grafeio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by M.Masikos on 5/1/2017.
 */
public class GrafeioService {
	private static Logger logger = LogManager.getLogger(GrafeioService.class);
	private static GRAFEIODataManager grafeioDataManager;

	public List<GrafeioWithPostalCodes> getGrafeiaWithPostalCodes(String token) throws Exception {
		grafeioDataManager = new GRAFEIODataManager();
		List<GrafeioWithPostalCodes> grafeioWithPostalCodesList = null;

		grafeioWithPostalCodesList = grafeioDataManager.getGrafeiaWithPostalCodes(token);
		if (grafeioWithPostalCodesList != null) {
			logger.info(grafeioWithPostalCodesList.toString());
		}
		return grafeioWithPostalCodesList;
	}

	public Map<String, List<Grafeio>> getLoggedOnUserAndRestActiveGrafeia(String token) throws Exception {
		grafeioDataManager = new GRAFEIODataManager();
		List<Grafeio> restActiveGrafeia = null;
		List<Grafeio> loggedOnUserActiveGrafeia = null;

		loggedOnUserActiveGrafeia = grafeioDataManager.getLoggedOnUserActiveGrafeia(token);
		restActiveGrafeia = grafeioDataManager.getGrafeiaActive("DEDDHE_GLOBAL");
		restActiveGrafeia.removeAll(loggedOnUserActiveGrafeia);

		Map<String, List<Grafeio>> activeGrafeia = new HashMap<>();
		activeGrafeia.put("restActiveGrafeia", restActiveGrafeia);
		activeGrafeia.put("loggedOnUserActiveGrafeia", loggedOnUserActiveGrafeia);

		return activeGrafeia;
	}

	public List<Grafeio> getActiveGrafeia() throws Exception {
		grafeioDataManager = new GRAFEIODataManager();
		List<Grafeio> grafeioActiveList = null;

		grafeioActiveList = grafeioDataManager.getGrafeiaActive("DEDDHE_GLOBAL");
		if (grafeioActiveList != null) {
			logger.info(grafeioActiveList.size());
		}
		return grafeioActiveList;
	}
}
