package gr.deddie.pfr.rest;

import java.util.Arrays;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.naming.AuthenticationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.deddie.pfr.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.deddie.pfr.services.PowerOutageService;

/**
 * Created by M.Paschou on 15/12/2017.
 */

@Path("/poweroutage")
public class PowerOutageRESTService extends BaseResource{
	
	private static Logger logger = LogManager.getLogger(PowerOutageRESTService.class);
	private static final String AUTHORIZATION_PROPERTY = "X-Auth-Token";
	private static PowerOutageService powerOutageService;

	/*get all perifereiakes enotites*/
	@RolesAllowed({"pfr_outage_mgt", "pfr_affected_areas_per_line_mgt"})
	@GET
	@Path("/getPerifereiakesEnotites")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPerifereiakesEnotites(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		powerOutageService = new PowerOutageService();
		List<PerifereiakiEnotita> perifereiakesEnotites;
		logger.info("Service \'getPerifereiakesEnotites\' has been invoked!");

		try {
			perifereiakesEnotites = powerOutageService.getPerifereiakesEnotites();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(perifereiakesEnotites).build();
	}

	/*get all kallikratikoi ota*/
	@RolesAllowed({"pfr_outage_mgt", "pfr_mgt_affect_dhm_enot_per_line"})
	@GET
	@Path("/getKallikratikoiOTA")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKallikratikoiOTA(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		powerOutageService = new PowerOutageService();
		List<KallikratikosOTA> kallikratikosOTAList;
		logger.info("Service \'getKallikratikoiOTA\' has been invoked!");

		try {
			kallikratikosOTAList = powerOutageService.getKallikratikosOTAList();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(kallikratikosOTAList).build();
	}

	/*get all perifereies*/
	@RolesAllowed({"pfr_admin", "pfr_callcenter_api"})
	@GET
	@Path("/getPerifereies")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPerifereies() {
		powerOutageService = new PowerOutageService();
		List<Perifereia> perifereies;
		logger.info("Service \'getPerifereiakesEnotites\' has been invoked!");

		try {
			perifereies = powerOutageService.getPerifereies();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(perifereies).build();
	}

	//Get exyphretoumenes perioxes by grammiMT by VPD
	@RolesAllowed("pfr_affected_areas_per_line_mgt")
	@POST
	@Path("/getExyphretoumenesPerioxesbyGrammiMTbyVPD")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getExyphretoumenesPerioxesbyGrammiMTbyVPD(@HeaderParam(AUTHORIZATION_PROPERTY) String token, GrammiMT grammiMT) {
		powerOutageService = new PowerOutageService();
		List<ExyphretoumeniPerioxi> exyphretoumenesPerioxes;
		logger.info("Service \'getExyphretoumenesPerioxesbyGrammiMTbyVPD\' has been invoked!");
		try {
			exyphretoumenesPerioxes = powerOutageService.getExyphretoumenesPerioxesbyGrammiMTbyVPD(token, grammiMT);
			logger.info("found " + Integer.toString(exyphretoumenesPerioxes.size()) + " exyphretoumenes perioxes");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(exyphretoumenesPerioxes).build();
	}

	//Get dhmotikes enotites by grammiMT by VPD
	@RolesAllowed("pfr_mgt_affect_dhm_enot_per_line")
	@POST
	@Path("/getDhmotikesEnotitesbyGrammiMTbyVPD")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getDhmotikesEnotitesbyGrammiMTbyVPD(@HeaderParam(AUTHORIZATION_PROPERTY) String token, GrammiMT grammiMT) {
		powerOutageService = new PowerOutageService();
		List<ExyphretoumeniDhmEnothta> exyphretoumeniDhmEnothtaList;
		logger.info("Service \'getDhmotikesEnotitesbyGrammiMTbyVPD\' has been invoked!");
		try {
			exyphretoumeniDhmEnothtaList = powerOutageService.getExyphretoumenesDhmEnotitesbyGrammiMTbyVPD(token, grammiMT);
			logger.info("found " + (exyphretoumeniDhmEnothtaList == null ? "null" : Integer.toString(exyphretoumeniDhmEnothtaList.size())) + " exyphretoumenes perioxes");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(exyphretoumeniDhmEnothtaList).build();
	}

	//Get grammes MT and exyphretoumenes perioxes by VPD
	@RolesAllowed({"pfr_outage_mgt", "pfr_affected_areas_per_line_mgt"})
	@GET
	@Path("/getYpostYMGrammesMTExypPerioxesByVPD")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getYpostYMGrammesMTExypPerioxesByVPD(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		powerOutageService = new PowerOutageService();
		List<YpostathmosYM> ypostathmosYMList;
		logger.info("Service \'getYpostYMGrammesMTExypPerioxesByVPD\' has been invoked!");
		try {
			ypostathmosYMList = powerOutageService.getYpostYMGrammesMTExypPerioxesByVPD(token);
			logger.info("found " + Integer.toString(ypostathmosYMList.size()) + " corresponding ypostathmoi YM");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(ypostathmosYMList).build();
	}

	//Get grammes MT and exyphretoumenes dhmotikes enothtes by VPD
	@RolesAllowed({"pfr_outage_mgt", "pfr_mgt_affect_dhm_enot_per_line"})
	@GET
	@Path("/getYpostYMGrammesMTExypDhmEnothtesByVPD")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getYpostYMGrammesMTExypDhmEnothtesByVPD(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		powerOutageService = new PowerOutageService();
		List<YpostathmosYM> ypostathmosYMList;
		logger.info("Service \'getYpostYMGrammesMTExypDhmEnothtesByVPD\' has been invoked!");
		try {
			ypostathmosYMList = powerOutageService.getYpostYMGrammesMTExypDEByVPD(token);
			logger.info("found " + (ypostathmosYMList!=null ? Integer.toString(ypostathmosYMList.size()) : "null") + " corresponding ypostathmoi YM");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(ypostathmosYMList).build();
	}

    /*Insert Power Outage*/
    @RolesAllowed("pfr_outage_mgt")
	@POST
	@Path("/insertPowerOutage")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response insertPowerOutage(@HeaderParam(AUTHORIZATION_PROPERTY) String token, PowerOutage po) {
		logger.info("Operation \'/insertPowerOutage\' has been invoked!");
		powerOutageService = new PowerOutageService();
		Integer result;
		    
		try {
			result = powerOutageService.insertPowerOutage(po, token);
			logger.info("insertPowerOutage response: " + result);

		} catch (AuthenticationException e) {
			logger.error("authentication exception in insertPowerOutage", e);
			return errorResponse(Response.Status.UNAUTHORIZED, e.getMessage());
		} catch (Exception e) {
			logger.error("exception in insertPowerOutage", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		
		return Response.ok(result).build();
	}

	/*Insert Power Outage with affected dhmotikes enothtes*/
	@RolesAllowed("pfr_outage_mgt")
	@POST
	@Path("/insertPowerOutagewithDE")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response insertPowerOutagewithDE(@HeaderParam(AUTHORIZATION_PROPERTY) String token, PowerOutage po) {
		logger.info("Operation \'/insertPowerOutagewithDE\' has been invoked!");
		powerOutageService = new PowerOutageService();
		Integer result;

		try {
			result = powerOutageService.insertPowerOutage(po, token);
			logger.info("insertPowerOutagewithDE response: " + result);

		} catch (AuthenticationException e) {
			logger.error("authentication exception in insertPowerOutagewithDE", e);
			return errorResponse(Response.Status.UNAUTHORIZED, e.getMessage());
		} catch (Exception e) {
			logger.error("exception in insertPowerOutagewithDE", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(result).build();
	}

	/*Update Power Outage*/
	@RolesAllowed("pfr_outage_mgt")
	@PUT
	@Path("/updatePowerOutage")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response updatePowerOutage(@HeaderParam(AUTHORIZATION_PROPERTY) String token, PowerOutage po) {
		logger.info("Operation \'/updatePowerOutage\' has been invoked!");
		powerOutageService = new PowerOutageService();
		Integer result;

		try {
			result = powerOutageService.updatePowerOutage(po, token);
			logger.info("updatePowerOutage response: " + result);
		} catch (AuthenticationException e) {
			logger.error("authentication exception in updatePowerOutage", e);
			return errorResponse(Response.Status.UNAUTHORIZED, e.getMessage());
		} catch (javassist.NotFoundException e) {
			logger.error("NotFound exception in updatePowerOutage", e);
			return errorResponse(Response.Status.NOT_FOUND, e.getMessage());
		} catch (Exception e) {
			logger.error("exception in updatePowerOutage", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(result).build();
	}

	/*Update Power Outage with DE*/
	@RolesAllowed("pfr_outage_mgt")
	@PUT
	@Path("/updatePowerOutagewithDE")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response updatePowerOutagewithDE(@HeaderParam(AUTHORIZATION_PROPERTY) String token, PowerOutage po) {
		logger.info("Operation \'/updatePowerOutagewithDE\' has been invoked!");
		powerOutageService = new PowerOutageService();
		Integer result;

		try {
			result = powerOutageService.updatePowerOutage(po, token);
			logger.info("updatePowerOutagewithDE response: " + result);
		} catch (AuthenticationException e) {
			logger.error("authentication exception in updatePowerOutagewithDE", e);
			return errorResponse(Response.Status.UNAUTHORIZED, e.getMessage());
		} catch (javassist.NotFoundException e) {
			logger.error("NotFound exception in updatePowerOutagewithDE", e);
			return errorResponse(Response.Status.NOT_FOUND, e.getMessage());
		} catch (Exception e) {
			logger.error("exception in updatePowerOutagewithDE", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(result).build();
	}

	/*Search for power outage announcements */
    @RolesAllowed("pfr_outage_mgt")
	@POST
	@Path("/getPowerOutagesByCriteriaByVPD")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response getPowerOutagesByCriteriaByVPD(@HeaderParam(AUTHORIZATION_PROPERTY) String token, PowerOutageSearchParameters searchParameters) {
    	powerOutageService = new PowerOutageService();
		PowerOutageDTOsLimitedList powerOutageDTOsLimitedList;
		logger.info("Service \'getPowerOutagesByCriteriaByVPD\' has been invoked!");

		try {

			if ((searchParameters.getStart_date() == null) && (searchParameters.getEnd_date() != null)) {
				return  errorResponse(Response.Status.BAD_REQUEST, "start_date cannot be null when end_date is not null");
			}

			powerOutageDTOsLimitedList = powerOutageService.getPowerOutagesByCriteriaByVPD(searchParameters, token);
	         
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(powerOutageDTOsLimitedList).build();
	}

	@RolesAllowed({"pfr_outage_mgt", "pfr_callcenter_api"})
	@GET
	@Path("/getPowerOutageCauses")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getStatuses(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		powerOutageService = new PowerOutageService();
		logger.info("Service \'getPowerOutageCauses\' has been invoked!");

		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(powerOutageService.getPowerOutageCauses());
			return Response.ok(json).build();
		} catch (NotFoundException e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.NOT_FOUND, "no cause found");
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	/* Insert Exyphretoymenes Perioxes Ana Grammh */
	@RolesAllowed("pfr_affected_areas_per_line_mgt")
	@POST
	@Path("/insertExyphretPerioxesAnaGrammh")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response insertExyphretPerioxesAnaGrammh(@HeaderParam(AUTHORIZATION_PROPERTY) String token,
													ExyphretoumeniPerioxi ep) {
		logger.info("Operation \'/insertExyphretPerioxesAnaGrammh\' has been invoked!");
		powerOutageService = new PowerOutageService();
		Integer result;

		try {
			result = powerOutageService.insertExyphretPerioxesAnaGrammh(ep);
			logger.info("insertExyphretPerioxesAnaGrammh response: " + result);

		} catch (Exception e) {
			logger.error("exception in insertExyphretPerioxesAnaGrammh", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(result).build();
	}

	/* Insert Dhmotikh Enotita Ana Grammh */
	@RolesAllowed("pfr_mgt_affect_dhm_enot_per_line")
	@POST
	@Path("/insertExypDhmotikhEnotitaAnaGrammh")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response insertExypDhmotikhEnotitaAnaGrammh(@HeaderParam(AUTHORIZATION_PROPERTY) String token,
													ExyphretoumeniDhmEnothta ep) {
		logger.info("Operation \'/insertDhmotikhEnotitaAnaGrammh\' has been invoked!");
		powerOutageService = new PowerOutageService();
		Integer result;

		try {
			result = powerOutageService.insertExyphretDhmotikhEnotitaAnaGrammh(ep);
			logger.info("insertDhmotikhEnotitaAnaGrammh response: " + result);

		} catch (Exception e) {
			logger.error("exception in insertDhmotikhEnotitaAnaGrammh", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(result).build();
	}

	@RolesAllowed("pfr_affected_areas_per_line_mgt")
	@PUT
	@Path("/deleteExyphretPerioxesAnaGrammh")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response deleteExyphretPerioxesAnaGrammh(@HeaderParam(AUTHORIZATION_PROPERTY) String token, ExyphretoumeniPerioxi ep) {
		logger.info("Operation \'/deleteExyphretPerioxesAnaGrammh\' has been invoked!");
		powerOutageService = new PowerOutageService();
		Integer result;

		try {
			result = powerOutageService.deleteExyphretPerioxesAnaGrammh(ep, token);
			logger.info("deleteExyphretPerioxesAnaGrammh response: " + result);

		} catch (Exception e) {
			logger.error("exception in deleteExyphretPerioxesAnaGrammh", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(result).build();
	}

	@RolesAllowed("pfr_mgt_affect_dhm_enot_per_line")
	@PUT
	@Path("/deleteExyphretDEAnaGrammh")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response deleteExyphretDEAnaGrammh(@HeaderParam(AUTHORIZATION_PROPERTY) String token, ExyphretoumeniDhmEnothta exyphretoumeniDhmEnothta) {
		logger.info("Operation \'/deleteExyphretDEAnaGrammh\' has been invoked!");
		powerOutageService = new PowerOutageService();
		Integer result;

		try {
			result = powerOutageService.deleteExyphretDEAnaGrammh(exyphretoumeniDhmEnothta, token);
			logger.info("deleteExyphretDEAnaGrammh response: " + result);

		} catch (Exception e) {
			logger.error("exception in deleteExyphretDEAnaGrammh", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(result).build();
	}

	@RolesAllowed("pfr_affected_areas_per_line_mgt")
	@PUT
	@Path("/updateStatusTmhmatosGrammhs")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response updateTmhmaGrammhsStatus(@HeaderParam(AUTHORIZATION_PROPERTY) String token, List<ExyphretoumeniPerioxi> ep) {
		logger.info("Operation \'/updateTmhmaGrammhsStatus\' has been invoked!");
		powerOutageService = new PowerOutageService();
		Integer result;

		if ((ep == null) || ep.isEmpty()) {
			return errorResponse(Response.Status.BAD_REQUEST, "empty list with exyphretoumenes perioxes");
		}

		ExyphretoumeniPerioxi.ExyphretoymeniPerioxiStatus newStatus = ep.get(0).getStatus();
		for (ExyphretoumeniPerioxi item:ep) {
			if (newStatus.getValue() != item.getStatus().getValue())
				return errorResponse(Response.Status.BAD_REQUEST, "at least one exyphretoumei perioxi with different status");
		}

		try {
			result = powerOutageService.updateTmhmaGrammhsStatus(ep, newStatus,  token);
			logger.info("updateTmhmaGrammhsStatus response: " + result);

		} catch (Exception e) {
			logger.error("exception in updateTmhmaGrammhsStatus", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(result).build();
	}


	@RolesAllowed("pfr_affected_areas_per_line_mgt")
	@PUT
	@Path("/updateExyphretPerioxesAnaGrammh")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response updateExyphretPerioxesAnaGrammh(ExyphretoumeniPerioxi ep) {
		logger.info("Operation \'/updateExyphretPerioxesAnaGrammh\' has been invoked!");
		powerOutageService = new PowerOutageService();
		Integer result;

		try {
			result = powerOutageService.updateExyphretPerioxesAnaGrammh(ep);
			logger.info("updateExyphretPerioxesAnaGrammh response: " + result);

		} catch (Exception e) {
			logger.error("exception in updateExyphretPerioxesAnaGrammh", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(result).build();
	}


	@RolesAllowed("pfr_mgt_affect_dhm_enot_per_line")
	@PUT
	@Path("/updateExyphretDEAnaGrammh")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response updateExyphretDEAnaGrammh(ExyphretoumeniDhmEnothta exyphretoumeniDhmEnothta) {
		logger.info("Operation \'/updateExyphretDEAnaGrammh\' has been invoked!");
		powerOutageService = new PowerOutageService();
		Integer result;

		try {
			result = powerOutageService.updateExyphretDEAnaGrammh(exyphretoumeniDhmEnothta);
			logger.info("updateExyphretDEAnaGrammh response: " + result);

		} catch (Exception e) {
			logger.error("exception in updateExyphretDEAnaGrammh", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(result).build();
	}


	/*Search for power outage announcements */
	@RolesAllowed({"pfr_admin", "pfr_callcenter_api"})
	@GET
	@Path("/api/callcenter/getPowerOutages")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response getPowerOutagesforIVR(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		PowerOutageService powerOutageService = new PowerOutageService();

		logger.info("Service \'getPowerOutagesforIVR\' has been invoked!");

		try {
			return Response.ok(powerOutageService.getPowerOutagesforIVR(token)).build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

	}

	/*Search for power outage announcements per nomarxiaki enotita per dimotiki enotita*/
	@PermitAll
	@GET
	@Path("/api/IVR/getPowerOutagesperNEperDE")
	@Consumes({MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_FORM_URLENCODED + ";charset=utf-8"})
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response getPowerOutagesforIVR(@HeaderParam(AUTHORIZATION_PROPERTY) String token, @QueryParam("nomarxiaki_enothta_id") String nomarxiaki_enothta_id, @QueryParam("ota_id") String ota_id) {
		PowerOutageService powerOutageService = new PowerOutageService();

		if (nomarxiaki_enothta_id == null) {
			logger.error("nomarxiaki enotita id cannot be null");
			return errorResponse(Response.Status.BAD_REQUEST, "nomarx.enotita.id.cannot.be.null");
		}

		KallikratikiNomarxia kallikratikiNomarxia = new KallikratikiNomarxia(nomarxiaki_enothta_id);
		KallikratikosOTA kallikratikosOTA = null;

		if (Arrays.stream(KallikratikiNomarxia.KLNOMePemptoEpipedoAnalysis.values()).filter(item -> item.getValue().equals(nomarxiaki_enothta_id)).count() > 0) {
			if ((ota_id == null) || (ota_id == "")) {
				logger.error("ota id cannot be null for attiki, thessaloniki, axaia");
				return errorResponse(Response.Status.BAD_REQUEST, "ota.id.cannot.be.null");
			}

			kallikratikosOTA = new KallikratikosOTA(ota_id);
		} else {
			// klno does not have 5o epipedo analysis
			if ((ota_id != null) && (ota_id != "")) {
				logger.error("ota id should be null for prefectures not included in attiki, thessaloniki, axaia");
				return errorResponse(Response.Status.BAD_REQUEST, "ota.id.should.be.null");
			}
		}

		try {
			return Response.ok(powerOutageService.getPowerOutagesperNEperDE(kallikratikiNomarxia, kallikratikosOTA)).build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

	}


	/*Insert Power Outage*/
	@RolesAllowed("pfr_outage_mgt")
	@POST
	@Path("/api/dfr/addoreditPowerOutage")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response addoreditTALDPowerOutage(@HeaderParam(AUTHORIZATION_PROPERTY) String token, PowerOutage po) {
		logger.info("Operation \'/addoreditTALDPowerOutage\' has been invoked!");
		powerOutageService = new PowerOutageService();
		Integer result;

		int counter = 0;
		if ((po.getLektikoGenikonDiakoponList() != null) && (po.getLektikoGenikonDiakoponList().size()>0)) {
			counter ++;
		}
		if ((po.getKallikratikiDhmotikiEnothtaList() != null) && (po.getKallikratikiDhmotikiEnothtaList().size()>0)) {
			counter ++;
		}
		if ((po.getKallikratikosOTAList() != null) && (po.getKallikratikosOTAList().size()>0)) {
			counter ++;
		}
		if ((po.getKallikratikiNomarxiaList() != null) && (po.getKallikratikiNomarxiaList().size()>0)) {
			counter ++;
		}

		if (counter == 0) {
			logger.error("no.ephreazomenes.perioxes.found");
			return errorResponse(Response.Status.BAD_REQUEST, "no.ephreazomenes.perioxes.found");
		}

		if (counter >1) {
			logger.error("multiple.categories.of.ephreazomenes.perioxes.found");
			return errorResponse(Response.Status.BAD_REQUEST, "multiple.categories.of.ephreazomenes.perioxes.found");
		}

		try {
			result = powerOutageService.addoreditTALDPowerOutage(po, token);
			logger.info("addoreditTALDPowerOutage response: " + result);
		} catch (AuthenticationException e) {
			logger.error("authentication exception in addoreditTALDPowerOutage", e);
			return errorResponse(Response.Status.UNAUTHORIZED, e.getMessage());
		} catch (javassist.NotFoundException e) {
			logger.error("no.changes.found", e);
			return errorResponse(Response.Status.BAD_REQUEST, "no.changes.found");
		} catch (Exception e) {
			logger.error("exception in addoreditTALDPowerOutage", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(result).build();
	}

	//Get lektika exyphretoumenon perioxon for callcenter by VPD
	@RolesAllowed("pfr_affected_areas_per_line_rec")
	@GET
	@Path("/callcenter/getExyphretoumenesPerioxesbyVPD")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getExyphretoumenesPerioxesbyVPD(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		powerOutageService = new PowerOutageService();
		List<ExyphretoumeniPerioxiDTO> exyphretoumenesPerioxes;
		logger.info("Service \'getExyphretoumenesPerioxesbyVPD\' has been invoked!");
		try {
			exyphretoumenesPerioxes = powerOutageService.getReadyOrRecordedExyphretoumenesPerioxesbyVPD(token);
			logger.info("found " + Integer.toString(exyphretoumenesPerioxes.size()) + " exyphretoumenes perioxes");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(exyphretoumenesPerioxes).build();
	}

}
