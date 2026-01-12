package gr.deddie.pfr.services;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import gr.deddie.pfr.rest.PowerCutReportRESTService;
import gr.deddie.pfr.utilities.LocalizationUTIL;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.deddie.pfr.managers.PFRDataManager;
import gr.deddie.pfr.managers.POWERCUTREPORTDataManager;
import gr.deddie.pfr.model.Failure;
import gr.deddie.pfr.model.FailureHistory;
import gr.deddie.pfr.model.FailureType;
import gr.deddie.pfr.model.KallikratikiNomarxia;
import gr.deddie.pfr.model.LatestFailureDTO;
import gr.deddie.pfr.model.Supply;
import gr.deddie.pfr.model.SupplyDTO;
import gr.deddie.pfr.model.UserDataRole;

public class PowerCutReportService {
    private static POWERCUTREPORTDataManager powercutreportDataManager;
    private static FailureService failureService;
    private static PFRDataManager pfrDataManager;
    private static Logger logger = LogManager.getLogger(PowerCutReportService.class);

    public String en4EnglishElseGreek(String language) {
        if ( language != null && language.equals(LocalizationUTIL.ENGLISH) ){
            return LocalizationUTIL.ENGLISH;
        }else {
            return LocalizationUTIL.GREEK;
        }
    }

    public void validateRecalledFailure (Failure f) throws Exception {

        pfrDataManager = new PFRDataManager();

        Failure failure = pfrDataManager.getFailure(f.getId(), UserDataRole.NO_SECURITY_TOKEN);
        if (failure == null) {
            logger.info("no.failure.found");
            throw new ValidationException("no.failure.found");
        }

        if ((failure.getInput_channel() != null) && (failure.getInput_channel() != Failure.InputChannel.WEBSITE) && (failure.getInput_channel() != Failure.InputChannel.MOBILE_APP_IOS) && (failure.getInput_channel() != Failure.InputChannel.MOBILE_APP_ANDROID)) {
            logger.info("only.failures.submitted.in.website.are.allowed.to.be.recalled");
            throw new ValidationException("only.failures.submitted.in.website.are.allowed.to.be.recalled");
        }

        // failure retrieved from the dB has no associated supply
        if (failure.getFailureType().getId() != FailureType.DANGEROUS_STATE_ID && failure.getSupply_no() == null) {
            logger.info("supply_no.does.not.match.failure_id");
            throw new ValidationException("supply_no.does.not.match.failure_id");
        }

        // fill in dto the supply_no_partial field
        if (failure.getFailureType().getId() != FailureType.DANGEROUS_STATE_ID) {
        	if (f.getSupply_no().length() == 8) {
        		f.setSupply_no_partial(f.getSupply_no());
        	} else {
        		f.setSupply_no_partial(f.getSupply_no().substring(1,9));
        	}

        	if (!failure.getSupply_no_partial().equals(f.getSupply_no_partial())) {
        		logger.info("supply_no.does.not.match.failure_id");
        		throw new ValidationException("supply_no.does.not.match.failure_id");
        	}
        } else {
        	if (!failure.getCell().equals(f.getOtp().getCell())) {
        		logger.info("cell.does.not.match.failure_id");
        		throw new ValidationException("cell.does.not.match.failure_id");
        	}
        }

        FailureHistory latestEvent = failure.getEvents().stream().sorted(Comparator.comparingLong(FailureHistory::getId).reversed()).findFirst().orElse(null);
        if (latestEvent == null) {
            logger.error("failure_id.has.no.events");
            throw new Exception("failure_id.has.no.events");
        } else if (latestEvent.getStatus().equals(Failure.FailureStatus.RECALLED_BY_ANNOUNCER.toString())) {
            logger.info("failure.is.already.recalled");
            throw new ValidationException("failure.is.already.recalled");
        } else if (!latestEvent.getStatus().equals(Failure.FailureStatus.ANNOUNCED.toString()) && !latestEvent.getStatus().equals(Failure.FailureStatus.IN_PROGRESS.name())) {
            logger.info("recall.is.not.allowed");
            throw new ValidationException("recall.is.not.allowed");
        }
    }

    public List<KallikratikiNomarxia> getPerifereiakesEnotites() {
        powercutreportDataManager = new POWERCUTREPORTDataManager();
        return powercutreportDataManager.getPerifereiakesEnotites();
    }

    public SupplyDTO getSupplyInfo (String no) throws Exception {
        failureService = new FailureService();
        pfrDataManager = new PFRDataManager();
        Supply supply;

        try {
            Map<String, Object> supplyInfo = failureService.getSupply(no);
            supply = (Supply) supplyInfo.get("supply");

        } catch (Exception ex) {
            // no supply matches the submitted supply_no
            if (ex.getMessage() == null) {
                throw new Exception("supply.not.found");
            } else {
                throw ex;
            }
        }

        if (supply == null) {
            throw new Exception("supply.not.found");
        }
        SupplyDTO supplyDTO = new SupplyDTO(supply.getNo(), null, supply.getLastTenant(), maskTenantName(supply.getLastTenantName()), supply.getDisconnectedForDebt(), null);
        Failure latestOpenFailure = pfrDataManager.getSupplysActiveFailures(supply.getNo()).stream().max(Comparator.comparing(Failure::getId)).orElse(null);
        if (latestOpenFailure != null) {
            supplyDTO.setLatestOpenFailure(new LatestFailureDTO(latestOpenFailure));
        }
        return supplyDTO;
    }

    public Failure getSupplysLatestOpenFailure (Supply supply) {
        pfrDataManager = new PFRDataManager();

        return pfrDataManager.getSupplysActiveFailures(supply.getNo()).stream().max(Comparator.comparing(Failure::getId)).orElse(null);
    }

    private String maskTenantName (String tenantName) {
        if ((tenantName != null) && (tenantName.length() > 2)) {
            String[] parts = StringUtils.split(tenantName);
            for (int i=0; i < parts.length; i++) {
                StringBuilder mask = new StringBuilder();
                for (int j=1; j<=parts[i].length()-3; j++) {
                    mask.append('*');
                }
                parts[i] = StringUtils.overlay(parts[i],mask.toString(),2,parts[i].length()-1);
            }

            StringBuilder maskedTenantName = new StringBuilder();
            for (String part:parts) {
                maskedTenantName.append(part).append(StringUtils.SPACE);
            }
            return maskedTenantName.toString();
        }
        return tenantName;
    }

}
