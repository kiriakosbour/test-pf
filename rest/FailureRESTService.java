package gr.deddie.pfr.rest;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
//import javax.json.JsonArray;
import javax.naming.AuthenticationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import gr.deddie.pfr.model.*;
import gr.deddie.pfr.services.PowerCutReportService;
import gr.deddie.pfr.utilities.LocalizationUTIL;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gr.deddie.pfr.managers.PFRDataManager;
import gr.deddie.pfr.services.FailureService;
import gr.deddie.pfr.utilities.JacksonConfig;

/**
 * Created by M.Masikos on 17/6/2016.
 */

@Path("/")
public class FailureRESTService extends BaseResource {
	private static Logger logger = LogManager.getLogger(FailureRESTService.class);
	public static final String AUTHORIZATION_PROPERTY = "X-Auth-Token";
	private static FailureService failureServiceManager;
	private static PFRDataManager pfrDataManager;
	private static PowerCutReportService powerCutReportService;
    public static final String INTEGRITY_PROPERTY = "X-Integrity-Check";

	@RolesAllowed("pfr_admin")
	@POST
	@Path("/deleteMessage")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response deleteMessage(@HeaderParam(AUTHORIZATION_PROPERTY) String token, Message m) {
		failureServiceManager = new FailureService();
		Integer result;
		    
		try {
			result = failureServiceManager.deleteMessage(m);
				
		} catch (Exception e) {
			logger.error("exception in deleteMessage", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		
		return Response.ok(result).build();
	}

	@RolesAllowed("pfr_admin")
	@POST
	@Path("/deleteFailureType")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response deleteFailureType(@HeaderParam(AUTHORIZATION_PROPERTY) String token, FailureType ft) {
		failureServiceManager = new FailureService();
		Integer result;
		    
		try {
			result = failureServiceManager.deleteFailureType(ft);
				
		} catch (Exception e) {
			logger.error("exception in deleteFailureType", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		
		return Response.ok(result).build();
	}

	@RolesAllowed("pfr_admin")
	@POST
	@Path("/updateFailureType")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response updateFailureType(@HeaderParam(AUTHORIZATION_PROPERTY) String token, FailureType ft) {
		failureServiceManager = new FailureService();
		Integer result;
		    
		try {
			result = failureServiceManager.updateFailureType(ft);
				
		} catch (Exception e) {
			logger.error("exception in updateFailureType", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		
		return Response.ok(result).build();
	}

	@RolesAllowed("pfr_admin")
	@POST
	@Path("/insertFailureType")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response insertFailureType(FailureType ft) {
		failureServiceManager = new FailureService();
		Integer result;
		    
		try {
			result = failureServiceManager.insertFailureType(ft);
				
		} catch (Exception e) {
			logger.error("exception in insertFailureType", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		
		return Response.ok(result).build();
	}

	@RolesAllowed("pfr_admin")
	@POST
	@Path("/updateMessage")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response updateMessage(Message m) {
		failureServiceManager = new FailureService();
		Integer result;
		    
		try {
			result = failureServiceManager.updateMessage(m);
				
		} catch (Exception e) {
			logger.error("exception in updateMessage", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		
		return Response.ok(result).build();
	}

	@RolesAllowed("pfr_admin")
	@POST
	@Path("/insertMessage")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response insertMessage(@HeaderParam(AUTHORIZATION_PROPERTY) String token, Message m) {
		failureServiceManager = new FailureService();
		Integer result;
		    
		try {
				
			result = failureServiceManager.insertMessage(m);

		} catch (Exception e) {
			logger.error("exception in insertMessage", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		
		return Response.ok(result).build();
	}

	@RolesAllowed({"pfr_user", "pfr_callcenter", "pfr_announcement_entry"})
	@POST
	@Path("/insertFailure")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response insertFailure(@HeaderParam(AUTHORIZATION_PROPERTY) String token, Failure f) {
		failureServiceManager = new FailureService();
		Long result;
		    
		try {
			result = failureServiceManager.insertFailure(f, token);
				
		} catch (NotFoundException e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.BAD_REQUEST, "no grafeio assigned to the postal code");
		} catch (Exception e) {
			logger.error("exception in insertFailure", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		
		return Response.ok(result).build();
	}

	@RolesAllowed({"pfr_user", "pfr_callcenter"})
	@POST
	@Path("/updateFailures")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateFailures(@HeaderParam(AUTHORIZATION_PROPERTY) String token, List<Failure> failures) {
		failureServiceManager = new FailureService();
		Integer result;
		    
		try {
			result = failureServiceManager.updateFailures(failures, token);
		} catch (Exception e) {
			logger.error("exception in updateFailures", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(result).build();
	}

	@RolesAllowed("pfr_user")
	@POST
	@Path("/assignFailuresToOtherUnits")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response insertFailureAssignments(@HeaderParam(AUTHORIZATION_PROPERTY) String token, List<FailureAssignment> failureAssignments) {
		failureServiceManager = new FailureService();
		Integer result;

		try {
			result = failureServiceManager.addFailuresAssignments(failureAssignments);

		} catch (Exception e) {
			logger.error("exception in insertFailureAssignments", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		if (result == failureAssignments.size()) {
			return Response.ok(result).build();
		} else {
			return errorResponse(Response.Status.PARTIAL_CONTENT, "Η ενέργεια καταχωρήθηκε μόνο για " +
					result.toString() + " από τις " + String.valueOf(failureAssignments.size()) + " αναγγελίες");
		}

	}

	@RolesAllowed("pfr_user")
	@POST
	@Path("/updateFailuresGroup")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateFailuresGroup(@HeaderParam(AUTHORIZATION_PROPERTY) String token, Map<String, Object> data) {
		failureServiceManager = new FailureService();
		pfrDataManager = new PFRDataManager();
		Integer result;
		List<FailureDTO> failureDTOS = null;
		JacksonConfig jacksonConfig = new JacksonConfig();

		//read values from the "map" parameter
		ObjectMapper objectMapper = jacksonConfig.getContext(FailureDTO.class);
		try {
			failureDTOS = Arrays.asList(objectMapper.readValue(objectMapper.writeValueAsString(data.get("failureDTOs")), FailureDTO[].class));
		} catch (Exception ex) {
			logger.error("updateFailuresGroup bad request: cannot read failures", ex);
			return errorResponse(Response.Status.BAD_REQUEST, "cannot.read.generalFailureGroupFailures");
		}

		Long generalFailureGroupId = data.get("generalFailureGroupId") == null ? null : Long.parseLong(data.get("generalFailureGroupId").toString());
		String generalFailureGroupLabel = (String) data.get("generalFailureGroupLabel");
		GeneralFailureGroup generalFailureGroup;

		if (failureDTOS.size() == 0) {
			logger.error("updateFailuresGroup bad request: empty set of failures");
			return errorResponse(Response.Status.BAD_REQUEST, "empty.set.of.failures");
		}

		// existing group
		if (generalFailureGroupId != null) {
			generalFailureGroup = pfrDataManager.getGeneralFailureGroup(generalFailureGroupId);

			if (generalFailureGroup == null) {
				logger.error("updateFailuresGroup bad request: not existing group");
				return errorResponse(Response.Status.BAD_REQUEST, "not.existing.group");
			}
		// generate new group
		} else if (generalFailureGroupLabel != null) {
			//generalFailureGroup = pfrDataManager.getGeneralFailureGroup(generalFailureGroupLabel);

			//if (generalFailureGroup == null) {
				try {
					generalFailureGroup = failureServiceManager.addGeneralFailureGroup(generalFailureGroupLabel, token);
				} catch (AuthenticationException ex) {
					logger.error("addGeneralFailureGroup authentication error: user is not loggedin", ex);
					return errorResponse(Response.Status.UNAUTHORIZED, "user.is.not.loggedin");
				} catch (Exception ex) {
					logger.error("addGeneralFailureGroup error: cannot add general failure group", ex);
					return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, "cannot.add.general.failure.group");
				}
			//}
		// delete logically the group of the requested failures
		} else {
			// (generalFailureGroupId == null) && (generalFailureGroupLabel == null)
			// empty the group if generalFailureGroup is null
			generalFailureGroup = null;
		}

		try {
			result = failureServiceManager.updateFailuresGroup(failureDTOS, generalFailureGroup, token);
			if (result == 0) {
				return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, "no.failure.group.updated");
			} else {
				return Response.ok(result).build();
			}

		} catch (Exception e) {
			logger.error("exception in updateFailuresGroup", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@RolesAllowed("pfr_user")
	@POST
	@Path("/markFailuresAsGeneral")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response markFailuresAsGeneral(@HeaderParam(AUTHORIZATION_PROPERTY) String token, List<FailureDTO> failures) {
		failureServiceManager = new FailureService();
		Integer result = 0;

		try {
			result = failureServiceManager.alterFailuresGeneralFlag(failures, true);
		}
		catch (Exception e) {
			logger.error("exception in markFailuresAsGeneral", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(result).build();
	}

	@RolesAllowed("pfr_user")
	@POST
	@Path("/markFailuresAsNotGeneral")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response markFailuresAsNotGeneral(@HeaderParam(AUTHORIZATION_PROPERTY) String token, List<FailureDTO> failures) {
		failureServiceManager = new FailureService();
		Integer result = 0;

		try {
			result = failureServiceManager.alterFailuresGeneralFlag(failures,false);
		}
		catch (Exception e) {
			logger.error("exception in markFailuresAsNotGeneral", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(result).build();
	}

	@RolesAllowed("pfr_user")
	@POST
	@Path("/markFailuresAsRecovered")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response markFailuresAsRecovered(@HeaderParam(AUTHORIZATION_PROPERTY) String token, List<FailureDTO> recoveredFailures) {
		failureServiceManager = new FailureService();
		Integer result = 0;

		try {
			result = failureServiceManager.markFailuresAsRecovered(recoveredFailures, token);
		}
		catch (AuthenticationException e) {
			logger.error("exception in markFailuresAsRecovered", e);
			return errorResponse(Response.Status.UNAUTHORIZED, e.getMessage());
		}
		catch (Exception e) {
			logger.error("exception in markFailuresAsRecovered", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(result).build();
	}

	@RolesAllowed("pfr_admin")
	@POST
	@Path("/markGeneralFailuresinaPostalCodeAsRecovered")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response markGeneralFailuresinaPostalCodeAsRecovered(@HeaderParam(AUTHORIZATION_PROPERTY) String token, Map<String, String> data) {
		failureServiceManager = new FailureService();
		Integer result = 0;

		try {
			result = failureServiceManager.markGeneralFailuresinaPostalCodeAsRecovered(token, data.get("postal_code"), data.get("last_update_comments"));
		}
		catch (Exception e) {
			logger.error("exception in markGeneralFailuresinaPostalCodeAsRecovered", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(result).build();
	}

	@RolesAllowed({"pfr_admin", "pfr_user"})
	@GET
	@Path("/getFailuresAnnouncements")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFailuresAnnouncements(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		failureServiceManager = new FailureService();
		List<Failure> failures;

		try {
			failures = failureServiceManager.getFailuresAnnouncements();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(failures).build();
	}

	@RolesAllowed("pfr_powercut_map_view")
	@GET
	@Path("/getActiveFailuresWithCoords")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActiveFailuresWithCoords(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		failureServiceManager = new FailureService();
		List<Failure> failures;

		try {
			failures = failureServiceManager.getActiveFailuresWithCoords(token);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(failures).build();
	}

	@RolesAllowed({"pfr_admin", "pfr_user", "pfr_announcement_entry", "pfr_callcenter_test", "pfr_callcenter"})
	@GET
	@Path("/getFailureTypes")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getFailureTypes(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		failureServiceManager = new FailureService();
		List<FailureType> failureTypes;

		try {
			failureTypes = failureServiceManager.getFailureTypes();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(failureTypes).build();

	}

	@RolesAllowed({"pfr_admin", "pfr_user", "pfr_announcement_entry"})
	@GET
	@Path("/getFailureHistory/{failure_id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getFailureHistory(@HeaderParam(AUTHORIZATION_PROPERTY) String token, @PathParam("failure_id") String failure_id) {
		failureServiceManager = new FailureService();
		List<FailureHistory> failureHistoryList;

		try {
			failureHistoryList = failureServiceManager.getFailureHistory(failure_id);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(failureHistoryList).build();

	}

	@RolesAllowed({"pfr_admin", "pfr_user", "pfr_announcement_entry", "pfr_callcenter"})
	@GET
	@Path("/getFailure/{failure_id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getFailure(@HeaderParam(AUTHORIZATION_PROPERTY) String token, @PathParam("failure_id") String failure_id) {
		failureServiceManager = new FailureService();

		try {
			Long failureId = Long.valueOf(failure_id);
			Failure failure = failureServiceManager.getFailure(token, failureId);
			if (failure == null) {
				return errorResponse(Response.Status.NOT_FOUND, "No failure found with requested id or you are not allowed to view this failure");
			}

			return Response.ok(failure).build();
		}
		catch (NumberFormatException e) {
			logger.error("exception in getFailure, passed parameter is not Long", e);
			return errorResponse(Response.Status.BAD_REQUEST, "id.is.not.long");
		}catch (Exception e) {
			logger.error("exception in getFailure rest service", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@RolesAllowed("pfr_user")
	@GET
	@Path("/getFailureDTOs")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getFailureDTO(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		failureServiceManager = new FailureService();
		List<FailureDTO> failureDTOs;

		try {
			failureDTOs = failureServiceManager.getFailureDTOs();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(failureDTOs).build();
	}

	@RolesAllowed("pfr_user")
	@GET
	@Path("/getActiveRestFailureDTOsByGraf")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getActiveRestFailureDTOsByGraf(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		failureServiceManager = new FailureService();
		List<FailureDTO> failureDTOs;

		try {
			failureDTOs = failureServiceManager.getActiveRestNonGeneralFailureDTOsByGraf(token);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(failureDTOs).build();
	}

	@RolesAllowed("pfr_user")
	@GET
	@Path("/getRecalledFailureDTOsByGraf")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getRecalledFailureDTOsByGraf(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		failureServiceManager = new FailureService();
		List<FailureDTO> failureDTOs;

		try {
			failureDTOs = failureServiceManager.getFailureDTOsRecalledInLast24hByGraf(token);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(failureDTOs).build();
	}

	@RolesAllowed({"pfr_user", "pfr_callcenter"})
	@GET
	@Path("/getResponsibleGrafsperPostalCode")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getResponsibleGrafsperPostalCode(@HeaderParam(AUTHORIZATION_PROPERTY) String token, @QueryParam("postal_code") String postal_code) {
		failureServiceManager = new FailureService();
		Map<String, List<Grafeio>> responsibleGrafeia;

		if (postal_code.isEmpty()) {
			return errorResponse(Response.Status.BAD_REQUEST, "postal_code is empty");
		}

		try {
			responsibleGrafeia = failureServiceManager.getResponsibleGrafsperPostalCode(token, postal_code, LocalizationUTIL.GREEK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(responsibleGrafeia).build();
	}
	

	@PermitAll
	@GET
	@Path("/getResponsibleGrafsperPostalCodeOpen")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getResponsibleGrafsperPostalCodeOpen(@QueryParam("postal_code") String postal_code) {
		failureServiceManager = new FailureService();
		Map<String, List<Grafeio>> responsibleGrafeia;
		String token = UserDataRole.NO_SECURITY_TOKEN;
		
		if (postal_code.isEmpty()) {
			return errorResponse(Response.Status.BAD_REQUEST, "postal_code is empty");
		}

		try {
			responsibleGrafeia = failureServiceManager.getResponsibleGrafsperPostalCode(token, postal_code, LocalizationUTIL.GREEK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(responsibleGrafeia).build();
	}

	@PermitAll
	@GET
	@Path("/v2/getResponsibleGrafsperPostalCodeOpen")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getResponsibleGrafsperPostalCodeOpenV2(@HeaderParam(LocalizationUTIL.LANGUAGE_PROPERTY) String language,@QueryParam("postal_code") String postal_code) {
		powerCutReportService = new PowerCutReportService();
		failureServiceManager = new FailureService();
		Map<String, List<Grafeio>> responsibleGrafeia;
		String token = UserDataRole.NO_SECURITY_TOKEN;
		
		if (postal_code.isEmpty()) {
			//return errorResponse(Response.Status.BAD_REQUEST, "postal_code is empty");
			return errorResponseWithHash(Response.Status.BAD_REQUEST, "postal_code is empty");
		}

		try {
			String correctedLanguage = powerCutReportService.en4EnglishElseGreek(language);

			responsibleGrafeia = failureServiceManager.getResponsibleGrafsperPostalCode(token, postal_code, correctedLanguage);
			return Response.ok(responsibleGrafeia)
			          .header(INTEGRITY_PROPERTY, hashGenerator(responsibleGrafeia))
			          .build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			//return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
			return errorResponseWithHash(Response.Status.BAD_REQUEST, e.getMessage());
		}
	}

	@RolesAllowed({"pfr_user", "pfr_callcenter"})
	@GET
	@Path("/getActiveFailureDTOsBySupplyNoOrPostalCode")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getActiveFailureDTOsBySupplyNoOrPostalCode(@HeaderParam(AUTHORIZATION_PROPERTY) String token, @QueryParam("supply_no") String supply_no, @QueryParam("postal_code") String postal_code) {
		failureServiceManager = new FailureService();
		List<FailureDTO> failureDTOs;

		if (postal_code.isEmpty())
			postal_code = null;
		if (supply_no.isEmpty())
			supply_no =null;

		try {
			failureDTOs = failureServiceManager.getActiveFailureDTOsBySupplyNoOrPostalCode(token, supply_no, postal_code);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(failureDTOs).build();
	}
	
	@RolesAllowed("pfr_user")
	@GET
	@Path("/getActiveDangerousStatesByGraf")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getActiveDangerousStatesByGraf(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		failureServiceManager = new FailureService();
		List<DangerousStateDTO> dangerousStateDTOS;

		try {
			dangerousStateDTOS = failureServiceManager.getActiveDangerousStatesByGraf(token);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(dangerousStateDTOS).build();
	}

	@RolesAllowed("pfr_user")
	@GET
	@Path("/getPhoto/{file_id}")
	@Produces("txt/plain" + ";charset=utf-8")
	public Response getPhoto(@PathParam("file_id") long file_id) {
		pfrDataManager = new PFRDataManager();

		try {
			NetworkHazardPhoto networkHazardPhoto = pfrDataManager.getPhoto(file_id);

			if (networkHazardPhoto == null) {
				return Response.status(Response.Status.NOT_FOUND).entity("photo.not.found").build();
			}

			//File file = new File("/u01/tempfiles/pfr/" + networkHazardPhoto.getName());
			//FileUtils.writeByteArrayToFile(file, networkHazardPhoto.getPhoto());

			return Response.ok(networkHazardPhoto.getPhoto())
					.header("Content-Disposition","attachment;  filename*=UTF-8''" + URLEncoder.encode(networkHazardPhoto.getName(), "UTF-8"))
					.build();
		} catch (Exception e) {
			logger.error("error in getPhoto: ", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error.in.getPhoto").build();
		}
	}

	@RolesAllowed("pfr_user")
	@GET
	@Path("/getActiveGeneralFailuresByPostalCodeByGraf")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getActiveGeneralFailuresByPostalCodeByGraf(@HeaderParam(AUTHORIZATION_PROPERTY) String token, @QueryParam("postal_code") String postal_code) {
		failureServiceManager = new FailureService();
		List<FailureDTO> failureDTOs;
		String json = null;

		try {
			failureDTOs = failureServiceManager.getActiveGeneralFailuresByPostalCodeByGraf(token, postal_code);

		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(failureDTOs).build();
	}

	@RolesAllowed("pfr_user")
	@GET
	@Path("/getActiveGeneralFailuresSumsByGraf")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getActiveGeneralFailuresSumsByGraf(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		failureServiceManager = new FailureService();
		List<FailureStatsDTO> generalFailureSums;

		try {
			generalFailureSums = failureServiceManager.getActiveGeneralFailuresSumsByGraf(token);
		} catch (Exception e) {
			logger.error("exception in getActiveGeneralFailuresSumsByGraf: ", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(generalFailureSums).build();
	}

	@RolesAllowed("pfr_user")
	@GET
	@Path("/getFailureDTOsLimitedList")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getFailureDTOsLimited(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		failureServiceManager = new FailureService();
		FailureDTOsLimitedList failureDTOsLimitedList;

		try {
			failureDTOsLimitedList = failureServiceManager.getFailureDTOsLimitedList();
		} catch (Exception e) {
			logger.error("exception in getFailureDTOsLimitedList", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(failureDTOsLimitedList).build();
	}

	@RolesAllowed("pfr_user")
	@GET
	@Path("/getFailureDTOsByIDs")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getFailureDTOsByIDs(@HeaderParam(AUTHORIZATION_PROPERTY) String token, @QueryParam("id") final List<Long> ids) {
		failureServiceManager = new FailureService();
		List<FailureDTO> failureDTOs;

		try {
			failureDTOs = failureServiceManager.getFailureDTOsByIDs(ids);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(failureDTOs).build();
	}

	@RolesAllowed({"pfr_admin", "pfr_user", "pfr_callcenter"})
	@GET
	@Path("/getMessages")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getMessages(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		failureServiceManager = new FailureService();
		List<Message> messages;

		try {
			messages = failureServiceManager.getMessages();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(messages).build();
	}

	@RolesAllowed({"pfr_admin", "pfr_user", "pfr_callcenter"})
	@GET
	@Path("/getStatuses")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getStatuses(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		failureServiceManager = new FailureService();

		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(failureServiceManager.getStatuses());
			return Response.ok(json).build();
		} catch (NotFoundException e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.NOT_FOUND, "no status found");
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@PermitAll
	@GET
	@Path("/getKLOT")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getKLOT() {
		pfrDataManager = new PFRDataManager();

		try {
			return Response.ok(pfrDataManager.getKLOT(LocalizationUTIL.GREEK)).build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@PermitAll
	@GET
	@Path("/v2/getKLOT")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getKLOTV2(@HeaderParam(LocalizationUTIL.LANGUAGE_PROPERTY) String language) {
		pfrDataManager = new PFRDataManager();
		powerCutReportService = new PowerCutReportService();

		try {
			String correctedLanguage = powerCutReportService.en4EnglishElseGreek(language);

			List<KallikratikosOTA> klotList = pfrDataManager.getKLOT(correctedLanguage);
			return Response.ok(klotList)
	                    .header(INTEGRITY_PROPERTY, hashGenerator(klotList))
	                    .build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponseWithHash(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	@RolesAllowed({"pfr_admin", "pfr_user", "pfr_callcenter"})
	@GET
	@Path("/getKLOTbyVPD")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getKLOTbyVPD(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		pfrDataManager = new PFRDataManager();

		try {
			return Response.ok(pfrDataManager.getKLOTbyVPD(token)).build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@RolesAllowed("pfr_user")
	@GET
	@Path("/getGeneralFailureGroups")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getGeneralFailureGroups(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		failureServiceManager = new FailureService();

		try {
			return Response.ok(failureServiceManager.getGeneralFailureGroups(token)).build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@RolesAllowed({"pfr_user", "pfr_callcenter"})
	@POST
	@Path("/getFailureDTOsByCriteria")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response getFailureDTOsByCriteria(@HeaderParam(AUTHORIZATION_PROPERTY) String token, FailureSearchParameters searchParameters) {
		failureServiceManager = new FailureService();
		FailureDTOsLimitedList failureDTOsLimitedList;

		try {
			failureDTOsLimitedList = failureServiceManager.getFailureDTOsByCriteria(searchParameters, token);
	         
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(failureDTOsLimitedList).build();
	}

	@RolesAllowed({"pfr_user", "pfr_callcenter"})
	@GET
	@Path("/getSupply")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getSupply(@HeaderParam(AUTHORIZATION_PROPERTY) String token, @QueryParam("supply_no") final String supply_no) {

		if ((supply_no.length() != Supply.SUPPLY_NO_LENGTH_A) && (supply_no.length() != Supply.SUPPLY_NO_LENGTH_B)) {
			return errorResponse(Response.Status.BAD_REQUEST, "supply_no must consist of 8 or 11 chars");
		}

		failureServiceManager = new FailureService();
		Map<String, Object> supplyInfo;

		try {
			supplyInfo = failureServiceManager.getSupply(supply_no);
		} catch (Exception e) {
			if (e.getMessage() == null) {
				return  errorResponse(Response.Status.NOT_FOUND, "no supply with no " + supply_no);
			}
			else {
				return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
			}
		}

		return Response.ok(supplyInfo).build();
	}

	@RolesAllowed("pfr_user")
	@GET
	@Path("/getValidAnnouncements")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")

	public Response getValidAnnouncements(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		failureServiceManager = new FailureService();
		List<Announcement> announcements;

		try {
			announcements = failureServiceManager.getValidAnnouncements();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(announcements).build();
	}

	@RolesAllowed({"pfr_user", "pfr_callcenter"})
	@GET
	@Path("/getTexnitesByGraf")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getTexnitesByGraf(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		failureServiceManager = new FailureService();
		List<Misthotos> texnites;

		try {
			texnites = failureServiceManager.getTexnitesByGraf(token);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(texnites).build();
	}

	@RolesAllowed({"pfr_user", "pfr_callcenter"})
	@GET
	@Path("/getContractorTexnitesByGraf")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getContractorTexnitesByGraf(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
		failureServiceManager = new FailureService();
		List<ContractorMisthotos> contractorTexnites;

		try {
			contractorTexnites = failureServiceManager.getTexnitesContractorByGraf(token);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(contractorTexnites).build();
	}

	@RolesAllowed("pfr_failures_book_admin")
	@POST
	@Path("/getFailuresBookContent")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response getFailuresBookContent(@HeaderParam(AUTHORIZATION_PROPERTY) String token, FailuresBookSearchParameters searchParameters) {
		failureServiceManager = new FailureService();
		List<FailureDTO> failureDTOList = null;

		try {
			failureDTOList = failureServiceManager.getFailuresBookContent(searchParameters, token);

		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(failureDTOList).build();
	}
	
	@PermitAll
	@GET
	@Path("/getQuestionsForNetworkHazard")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getQuestionsForNetworkHazard() {
		failureServiceManager = new FailureService();
		List<Question> questions = null;

		try {
			questions = failureServiceManager.getQuestionsForNetworkHazard(LocalizationUTIL.GREEK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(questions).build();
	}
	
	@PermitAll
	@GET
	@Path("/v2/getQuestionsForNetworkHazard")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getQuestionsForNetworkHazardV2(@HeaderParam(LocalizationUTIL.LANGUAGE_PROPERTY) String language) {
		failureServiceManager = new FailureService();
		powerCutReportService = new PowerCutReportService();
		List<Question> questions = null;

		try {
			String correctedLanguage = powerCutReportService.en4EnglishElseGreek(language);

			questions = failureServiceManager.getQuestionsForNetworkHazard(correctedLanguage);
			return Response.ok(questions)
			        .header(INTEGRITY_PROPERTY, hashGenerator(questions))
			        .build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponseWithHash(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	@PermitAll
	@POST
	@Path("/getFailureType")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getFailureType(Failure f) {
		failureServiceManager = new FailureService();
		Integer result;
		try {
			result = failureServiceManager.getFailureType(f);
			
			if (result == null) {
				return errorResponse(Response.Status.NOT_FOUND, "no.failure.found");
			} else {
				result = result == FailureType.DANGEROUS_STATE_ID? 1:0;
			}
		} catch (Exception e) {
			logger.error("exception in getFailureType", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return Response.ok(result).build();
	}

	@RolesAllowed("pfr_callcenter_monitoring")
	@GET
	@Path("/getCallcenterPerformanceMetrics")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getCallcenterPerformanceMetrics(@QueryParam("requestedDate") Long requestedDate) {
		pfrDataManager = new PFRDataManager();

		try {
			Date reqDate = new Date(requestedDate);
			List<CallcenterPerformanceMetricsDTO> metrics = pfrDataManager.getCallcenterPerformanceMetrics(reqDate);
			Calendar today = Calendar.getInstance();
			Calendar requestedDay = Calendar.getInstance();
			requestedDay.setTime(reqDate);
			if (DateUtils.isSameDay(today, requestedDay)) {
				metrics = metrics.stream()
						.filter(item-> Integer.parseInt(item.getTimePeriod().substring(0,2))<today.get(Calendar.HOUR_OF_DAY))
						.collect(Collectors.toList());
			}

			return Response.ok(metrics).build();
		} catch (Exception e) {
			logger.error("exception in getCallcenterPerformanceMetrics", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@PermitAll
	@GET
	@Path("/api/IVR/hasActiveFailures")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
	public Response hasActiveFailures(@QueryParam("token") String token, @QueryParam("supplyNumber") String supplyNumber){
		try {
			pfrDataManager = new PFRDataManager();
			if (StringUtils.isBlank(token) || !pfrDataManager.isIVRTokenValid(token)){
				return Response.status(Response.Status.FORBIDDEN).entity("invalid.token").build();
			}

			if (StringUtils.isBlank(supplyNumber) || !supplyNumber.matches("\\d{11}")){
				return Response.status(Response.Status.BAD_REQUEST).entity("invalid.supply.number").build();
			}
			String folio = supplyNumber.substring(1,9);
			Supply supply = pfrDataManager.getSupply(folio);

			if (supply == null){
				return Response.status(Response.Status.NOT_FOUND).entity("supply.not.found").build();
			}

			List<Failure> supplyActiveFailures = pfrDataManager.getSupplysActiveFailures(folio);

			Map<String,Boolean> hasActiveFailures = new HashMap<>();
			hasActiveFailures.put("hasActiveFailures", !supplyActiveFailures.isEmpty());
			return Response.ok(hasActiveFailures).build();

		} catch (Exception e){
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
    public static String hashGenerator (Object object) throws NoSuchAlgorithmException, IOException {
    	ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    	Jsonb jsonb = JsonbBuilder.create();
        String jsonString = jsonb.toJson(object);

    	MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] jsonByteArray = jsonString.getBytes("UTF-8");
        byte[] messageDigestBytes = messageDigest.digest(jsonByteArray);
        return Base64.getEncoder().encodeToString(messageDigestBytes);
    }

    public static String hashGenerator (String jsonString) throws NoSuchAlgorithmException, UnsupportedEncodingException, JsonProcessingException {
    	MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] jsonByteArray = jsonString.getBytes("UTF-8");
        byte[] messageDigestBytes = messageDigest.digest(jsonByteArray);
        return Base64.getEncoder().encodeToString(messageDigestBytes);
    }
    
}
