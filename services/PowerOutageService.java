package gr.deddie.pfr.services;

//import static gr.deddie.pfr.model.Message.SUCCESSFUL_REGISTRATION_MESSAGE_ID;

import java.util.*;
import java.util.stream.Collectors;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;
import gr.deddie.pfr.dao.MyBatisConnectionFactory;
import gr.deddie.pfr.dao.PFRMapper;
import gr.deddie.pfr.managers.PFRDataManager;
import gr.deddie.pfr.model.*;
import gr.deddie.pfr.utilities.RestClient;
import javassist.NotFoundException;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.deddie.pfr.managers.POWEROUTAGEDataManager;

import javax.naming.AuthenticationException;

import static java.util.stream.Collectors.toList;

/**
 * Created by M.Paschou on 15/12/2017.
 */

public class PowerOutageService {
	
	private static Logger logger = LogManager.getLogger(PowerOutageService.class);
	private static POWEROUTAGEDataManager poweroutageDataManager;
    private static PFRDataManager pfrDataManager;

    public List<PerifereiakiEnotita> getPerifereiakesEnotites() throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();
        List<PerifereiakiEnotita> perifereiakesEnotites = poweroutageDataManager.getPerifereiakesEnotites();

        if (perifereiakesEnotites != null) {
            logger.info(perifereiakesEnotites.size());
        }
        return perifereiakesEnotites;
    }

    public List<KallikratikosOTA> getKallikratikosOTAList() throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();
        List<KallikratikosOTA> kallikratikosOTAList = poweroutageDataManager.getKallikratikosOTAList();

        if (kallikratikosOTAList != null) {
            logger.info(kallikratikosOTAList.size());
        }
        return kallikratikosOTAList;
    }

    public List<Perifereia> getPerifereies() throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();
        List<Perifereia> perifereies = poweroutageDataManager.getPerifereies();

        if (perifereies != null) {
            logger.info(perifereies.size());
        }
        return perifereies;
    }

    public List<YpostathmosYM> getYpostYMGrammesMTExypPerioxesByVPD(String token) throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();
        List<YpostathmosYM> ypostathmosYMList = poweroutageDataManager.getYpostYMGrammesMTExypPerioxesByVPD(token);

        return ypostathmosYMList;
    }

    public List<YpostathmosYM> getYpostYMGrammesMTExypDEByVPD(String token) throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();
        List<YpostathmosYM> ypostathmosYMList = poweroutageDataManager.getYpostYMGrammesMTExypDEByVPD(token);

        return ypostathmosYMList;
    }

    public List<ExyphretoumeniPerioxi> getExyphretoumenesPerioxesbyGrammiMTbyVPD(String token, GrammiMT grammiMT) throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();
        List<ExyphretoumeniPerioxi> exyphretoumenesPerioxes = poweroutageDataManager.getExyphretoumenesPerioxesbyGrammiMTbyVPD(token, grammiMT);

        return exyphretoumenesPerioxes;
    }

    public List<ExyphretoumeniDhmEnothta> getExyphretoumenesDhmEnotitesbyGrammiMTbyVPD(String token, GrammiMT grammiMT) throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();
        List<ExyphretoumeniDhmEnothta> exyphretoumeniDhmEnothtaList = poweroutageDataManager.getExyphretoumenesDhmEnotitesbyGrammiMTbyVPD(token, grammiMT);

        return exyphretoumeniDhmEnothtaList;
    }

    public List<ExyphretoumeniPerioxiDTO> getReadyOrRecordedExyphretoumenesPerioxesbyVPD(String token) throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();
        List<ExyphretoumeniPerioxi.ExyphretoymeniPerioxiStatus> statuses = new ArrayList<>();
        statuses.add(ExyphretoumeniPerioxi.ExyphretoymeniPerioxiStatus.READY_TO_RECORD);
        statuses.add(ExyphretoumeniPerioxi.ExyphretoymeniPerioxiStatus.RECORDED);
        List<ExyphretoumeniPerioxiDTO> exyphretoumenesPerioxes = poweroutageDataManager.getExyphretoumenesPerioxesbyStatusesbyVPD(token, statuses);

        return exyphretoumenesPerioxes;
    }

	// New Power Outage announcement
    public Integer insertPowerOutage(PowerOutage po, String token) throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();

        Integer result = poweroutageDataManager.insertPowerOutage(po, token);
        
        logger.info("insertPowerOutage result: " + result);
            
        return result;
    }

    // add or edit Power Outage announcement made by TALD via DFR app
    public Integer addoreditTALDPowerOutage(PowerOutage newPo, String token) throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();
        pfrDataManager = new PFRDataManager();

        final String url = "http://10.10.61.85:8080/Deddie-tts/resources/wavs/save";

        // retrieve user based on token
        newPo.setCreator(pfrDataManager.getUser(token));

        PowerOutage oldPO = poweroutageDataManager.getTALDPowerOutage(newPo.getPower_outage_dfr_id());

        //fill the field end_date_announced, it rounds up to next :30 or :00
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newPo.getEnd_date());
        calendar.get(Calendar.MINUTE);
        int round = 30 - (calendar.get(Calendar.MINUTE) % 30);
        calendar.add(Calendar.MINUTE, round);
        newPo.setEnd_date_announced(calendar.getTime());

        if (oldPO == null) {
            newPo = poweroutageDataManager.insertTALDPowerOutage(newPo, token);
        } else {
            //dfr app is not aware of PFR power outage id
            newPo.setId(oldPO.getId());
            newPo = poweroutageDataManager.updateTALDPowerOutage(oldPO, newPo, token);
        }

        // post IVR message to VOXEO server
        try {
            PowerOutage powerOutage = new PowerOutage();
            powerOutage.setId(newPo.getId());
            powerOutage.setText(newPo.getText());
            String apantisi = RestClient.doPost(url, powerOutage, String.class);
        } catch (Exception e) {
            logger.error("Failed to upload message to IVR", e);
        }

        return 1;
    }

    // Update Power Outage announcement
    public Integer updatePowerOutage(PowerOutage newPO, String token) throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();

        Integer result = poweroutageDataManager.updatePowerOutage(newPO, token);

        logger.info("updatePowerOutage result: " + result);

        return result;
    }

	// For power outage search process 
	public PowerOutageDTOsLimitedList getPowerOutagesByCriteriaByVPD (PowerOutageSearchParameters searchParameters, String token) throws Exception {
		poweroutageDataManager = new POWEROUTAGEDataManager();
        PowerOutageDTOsLimitedList powerOutageDTOsLimitedList;

        powerOutageDTOsLimitedList = poweroutageDataManager.getPowerOutagesByCriteriaByVPD(searchParameters, token);
        if (powerOutageDTOsLimitedList  != null) {
            logger.info("powerOutageDTOsLimitedList - partial_response = " + powerOutageDTOsLimitedList .getPartial_response());
            logger.info("powerOutageDTOsLimitedList - list size = " + powerOutageDTOsLimitedList .getPoweroutages().size());
        }
        return powerOutageDTOsLimitedList ;
    }

    // For power outage
    public List<PowerOutageTALDDTO> getPowerOutagesforIVR (String token) throws Exception {

        poweroutageDataManager = new POWEROUTAGEDataManager();

        return poweroutageDataManager.getCurrentPowerOutagesLimitedInfo(token);
    }

    public List<PowerOutageIVRDTO> getPowerOutagesperNEperDE (KallikratikiNomarxia kallikratikiNomarxia, KallikratikosOTA kallikratikosOTA) throws Exception {

        poweroutageDataManager = new POWEROUTAGEDataManager();

        // return the members of the old nomarxia in which the kallikratiki nomarxiaki enothta belongs
        List<KallikratikiNomarxia> kallikratikiNomarxiaList = poweroutageDataManager.getKallikrNomarxiakesEnothtesOfOldNomarxia(kallikratikiNomarxia);

        if (kallikratikosOTA == null) {
            return poweroutageDataManager.getPowerOutagesperNE(kallikratikiNomarxiaList);
        } else {
            return poweroutageDataManager.getPowerOutagesperOTA(kallikratikosOTA);
        }
    }

    public List<PowerOutage> getPowerOutagesperNE (String language, List<KallikratikiNomarxia> kallikratikiNomarxiaList) throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();

        List<PowerOutageIVRDTO> powerOutageIVRDTOList = poweroutageDataManager.getPowerOutagesperNE(kallikratikiNomarxiaList);
        logger.info("outages=" + powerOutageIVRDTOList);
        return powerOutageIVRDTOList.stream().map(poweroutage -> poweroutageDataManager.getPowerOutage(language, poweroutage.getId())).collect(Collectors.toList());
    }

    public List<PowerOutage.PowerOutageCause> getPowerOutageCauses() throws Exception {

        return Arrays.asList(PowerOutage.PowerOutageCause.values());
    }

    // New Exyphretoumeni Perioxi se Grammh
    public Integer insertExyphretPerioxesAnaGrammh(ExyphretoumeniPerioxi ep) throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();

        Integer result = poweroutageDataManager.insertExyphretPerioxesAnaGrammh(ep);

        logger.info("insertExyphretPerioxesAnaGrammh result: " + result);

        return result;
    }

    // New Exyphretoumeni Dhmotikh ENothta se Grammh
    public Integer insertExyphretDhmotikhEnotitaAnaGrammh(ExyphretoumeniDhmEnothta ep) throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();

        Integer result = poweroutageDataManager.insertExyphretDhmotikhEnotitaAnaGrammh(ep);

        logger.info("insertExyphretDhmotikhEnotitaAnaGrammh result: " + result);

        return result;
    }

    // Delete Exyphretoumenes Perioxes se Grammh
    public Integer deleteExyphretPerioxesAnaGrammh(ExyphretoumeniPerioxi ep, String token) throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();

        Integer result = poweroutageDataManager.deleteExyphretPerioxesAnaGrammh(ep, token);

        logger.info("deleteExyphretPerioxesAnaGrammh result: " + result);

        return result;
    }

    // Delete Exyphretoumenes Dhmotikes Enothtes se Grammh
    public Integer deleteExyphretDEAnaGrammh(ExyphretoumeniDhmEnothta exyphretoumeniDhmEnothta, String token) throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();

        Integer result = poweroutageDataManager.deleteExyphretDEAnaGrammh(exyphretoumeniDhmEnothta, token);

        logger.info("deleteExyphretDEAnaGrammh result: " + result);

        return result;
    }

    // Update status of Tmhma Grammhs
    public Integer updateTmhmaGrammhsStatus(List<ExyphretoumeniPerioxi> ep, ExyphretoumeniPerioxi.ExyphretoymeniPerioxiStatus newStatus, String token) throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();

        return poweroutageDataManager.updateTmhmaGrammhsStatus(ep, newStatus, token);
    }

    // Update exyphretoymenes perioxes ana grammh MT
    public Integer updateExyphretPerioxesAnaGrammh(ExyphretoumeniPerioxi ep) throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();

        Integer result = poweroutageDataManager.updateExyphretPerioxesAnaGrammh(ep);

        logger.info("updateExyphretPerioxesAnaGrammh result: " + result);

        return result;
    }

    // Update exyphretoymenes dhmotikes enothtes ana grammh MT
    public Integer updateExyphretDEAnaGrammh(ExyphretoumeniDhmEnothta exyphretoumeniDhmEnothta) throws Exception {
        poweroutageDataManager = new POWEROUTAGEDataManager();

        Integer result = poweroutageDataManager.updateExyphretDEAnaGrammh(exyphretoumeniDhmEnothta);

        logger.info("updateExyphretDEAnaGrammh result: " + result);

        return result;
    }
}
