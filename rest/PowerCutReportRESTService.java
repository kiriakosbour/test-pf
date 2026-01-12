/**
 * Created by M.Masikos on 21/6/2016.
   Contributors: Kleopatra Konstanteli
 */

package gr.deddie.pfr.rest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import gr.deddie.pfr.utilities.LocalizationUTIL;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gr.deddie.pfr.managers.POWERCUTREPORTDataManager;
import gr.deddie.pfr.managers.POWEROUTAGEDataManager;
import gr.deddie.pfr.model.Failure;
import gr.deddie.pfr.model.FailureAssignment;
import gr.deddie.pfr.model.FailureType;
import gr.deddie.pfr.model.KallikratikiNomarxia;
import gr.deddie.pfr.model.Landmark;
import gr.deddie.pfr.model.LatestFailureDTO;
import gr.deddie.pfr.model.MobileAppAnnouncement;
import gr.deddie.pfr.model.MobileAppAnnouncementDTO;
import gr.deddie.pfr.model.Otp;
import gr.deddie.pfr.model.Point;
import gr.deddie.pfr.model.PowerOutage;
import gr.deddie.pfr.model.Question;
import gr.deddie.pfr.model.QuestionList;
import gr.deddie.pfr.model.Supply;
import gr.deddie.pfr.model.SupplyDTO;
import gr.deddie.pfr.services.FailureService;
import gr.deddie.pfr.services.PowerCutReportService;
import gr.deddie.pfr.services.PowerOutageService;
import gr.deddie.pfr.services.SecurityService;
import gr.deddie.pfr.utilities.EncryptDecrypt;

@Path("/powercutreport")
public class PowerCutReportRESTService extends BaseResource{
    private static Logger logger = LogManager.getLogger(PowerCutReportRESTService.class);
    public static final String INTEGRITY_PROPERTY = "X-Integrity-Check";

    private static PowerCutReportService powerCutReportService;
    private static FailureService failureServiceManager;
    private static SecurityService securityService;
    private static POWERCUTREPORTDataManager powercutreportDataManager;
    private static POWEROUTAGEDataManager poweroutageDataManager;
    private static PowerOutageService powerOutageService;

    /*get kallikratikes nomarxiakes enotites*/
    @PermitAll
    @GET
    @Path("/getPerifereiakesEnotites")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPerifereiakesEnotites() {
        powerCutReportService = new PowerCutReportService();

        try {
            return Response.ok(powerCutReportService.getPerifereiakesEnotites()).build();
        } catch (Exception e) {
            logger.error("Error in getNomarxiakesEnotites rest service: ", e);
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /*get old nomarxies*/
    @PermitAll
    @GET
    @Path("/getNomarxies")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNomarxies() {
        powercutreportDataManager = new POWERCUTREPORTDataManager();

        try {
            return Response.ok(powercutreportDataManager.getNomarxies(LocalizationUTIL.GREEK)).build();
        } catch (Exception e) {
            logger.error("Error in getNomarxies rest service: ", e);
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /*get old nomarxies*/
    @PermitAll
    @GET
    @Path("/v2/getNomarxies")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNomarxiesv2(@HeaderParam(LocalizationUTIL.LANGUAGE_PROPERTY) String language) {

        powercutreportDataManager = new POWERCUTREPORTDataManager();
        powerCutReportService = new PowerCutReportService();

        try {

            String correctedLanguage = powerCutReportService.en4EnglishElseGreek(language);

            List<KallikratikiNomarxia> kallikratikiNomarxiaList = powercutreportDataManager.getNomarxies(correctedLanguage);

            return Response.ok(kallikratikiNomarxiaList)
                    .header(INTEGRITY_PROPERTY, hashGenerator(kallikratikiNomarxiaList))
                    .build();
        } catch (Exception e) {
            logger.error("Error in getNomarxies rest service: ", e);
            return errorResponseWithHash(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /*Search for power outage announcements per nomarxiaki enotita or old nomarxia*/
    @PermitAll
    @GET
    @Path("/getPowerOutagesperNE")
    @Consumes({MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_FORM_URLENCODED + ";charset=utf-8"})
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public Response
    getPowerOutagesforPowerCutReport(@QueryParam("nomarxiaki_enothta_id") String nomarxiaki_enothta_id) {
        powerOutageService = new PowerOutageService();
        poweroutageDataManager = new POWEROUTAGEDataManager();

        if (nomarxiaki_enothta_id == null) {
            logger.error("nomarxiaki enotita id cannot be null");
            return errorResponse(Response.Status.BAD_REQUEST, "nomarx.enotita.id.cannot.be.null");
        }

        List<KallikratikiNomarxia> kallikratikiNomarxiaList = new ArrayList<>();

        // if id < 0101, then it is old nomarxia
        // in that case we retrieve the new nomarxiakes enotites
        if (Long.valueOf(nomarxiaki_enothta_id) <100 ) {
            poweroutageDataManager.getKallikrNomarxiakesEnothtesOfOldNomarxia(Long.valueOf(nomarxiaki_enothta_id)).stream()
                    .collect(Collectors.toCollection(() -> kallikratikiNomarxiaList));
        } else {
            kallikratikiNomarxiaList.add(new KallikratikiNomarxia(nomarxiaki_enothta_id));
        }

        try {
            return Response.ok(powerOutageService.getPowerOutagesperNE(LocalizationUTIL.GREEK, kallikratikiNomarxiaList)).build();
        } catch (Exception e) {
            logger.error("Exception in getPowerOutagesperNE rest service: ", e);
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    /*Search for power outage announcements per nomarxiaki enotita or old nomarxia*/
    @PermitAll
    @GET
    @Path("/v2/getPowerOutagesperNE")
    @Consumes({MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_FORM_URLENCODED + ";charset=utf-8"})
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public Response getPowerOutagesforPowerCutReportv2(@HeaderParam(LocalizationUTIL.LANGUAGE_PROPERTY) String language, @QueryParam("nomarxiaki_enothta_id") String nomarxiaki_enothta_id) {
        powerOutageService = new PowerOutageService();
        poweroutageDataManager = new POWEROUTAGEDataManager();
        powerCutReportService = new PowerCutReportService();

        try {
            String correctedLanguage = powerCutReportService.en4EnglishElseGreek(language);

            if (nomarxiaki_enothta_id == null) {
                logger.error("nomarxiaki enotita id cannot be null");
                return errorResponseWithHash(Response.Status.BAD_REQUEST, "nomarx.enotita.id.cannot.be.null");
            }

            List<KallikratikiNomarxia> kallikratikiNomarxiaList = new ArrayList<>();

            // if id < 0101, then it is old nomarxia
            // in that case we retrieve the new nomarxiakes enotites
            if (Long.valueOf(nomarxiaki_enothta_id) <100 ) {
                poweroutageDataManager.getKallikrNomarxiakesEnothtesOfOldNomarxia(Long.valueOf(nomarxiaki_enothta_id)).stream()
                        .collect(Collectors.toCollection(() -> kallikratikiNomarxiaList));
            } else {
                kallikratikiNomarxiaList.add(new KallikratikiNomarxia(nomarxiaki_enothta_id));
            }

            List<PowerOutage> powerOutageList = powerOutageService.getPowerOutagesperNE(correctedLanguage, kallikratikiNomarxiaList);

            return Response.ok(powerOutageList)
                    .header(INTEGRITY_PROPERTY, hashGenerator(powerOutageList))
                    .build();
        } catch (Exception e) {
            logger.error("Exception in getPowerOutagesperNE rest service: ", e);
            return errorResponseWithHash(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    /*get power supply user info, check debt issues and open outage ticket for the supply*/
    @PermitAll
    @GET
    @Path("/getSupplyInfo")
    @Consumes({MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_FORM_URLENCODED + ";charset=utf-8"})
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public Response getSupplyInfo(@QueryParam("supply_no") String no) {
        PowerCutReportService powerCutReportService = new PowerCutReportService();

        if (no.length() < 8) {
            logger.error("the length of the supply no cannot be less than 8 characters");
            return errorResponse(Response.Status.BAD_REQUEST, "supply.no.cannot.be.less.than.8.chars");
        }

        try {
            return Response.ok(powerCutReportService.getSupplyInfo(no)).build();
        }
        catch (Exception e) {
            if (e.getMessage() == "supply.not.found") {
                return errorResponse(Response.Status.BAD_REQUEST, e.getMessage());
            } else {
                logger.error("Exception in getSupplyInfo rest service: ", e);
                return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    /*get power supply user info, check debt issues and open outage ticket for the supply*/
    @PermitAll
    @POST
    @Path("/v2/getSupplyInfo")
    @Consumes({MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_FORM_URLENCODED + ";charset=utf-8"})
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public Response getSupplyInfov2(@HeaderParam(INTEGRITY_PROPERTY) String integrityHash, InputStream inputStream) {
        PowerCutReportService powerCutReportService = new PowerCutReportService();

        try {
            String jsonString = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .map(line -> line.trim())
                    .collect(Collectors.joining(""));

            if (!integrityHash.equals(hashGenerator(jsonString))) {
                logger.info("integrity.check.failed");
                return errorResponseWithHash(Response.Status.FORBIDDEN, "access.not.allowed");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> supply = objectMapper.readValue(jsonString, Map.class);

            //decrypt input
            EncryptDecrypt decrypt = new EncryptDecrypt();
            supply.put("supply_no", decrypt.decrypt(supply.get("supply_no")));
            String no = supply.get("supply_no");

            if (no.length() < 8) {
                logger.error("the length of the supply no cannot be less than 8 characters");
                return errorResponseWithHash(Response.Status.BAD_REQUEST, "supply.no.cannot.be.less.than.8.chars");
            }

            SupplyDTO supplyDTO = powerCutReportService.getSupplyInfo(no);
            EncryptDecrypt encrypt = new EncryptDecrypt();
            supplyDTO.setLastTenantName(encrypt.encrypt(supplyDTO.getLastTenantName()));
            supplyDTO.setNo(encrypt.encrypt(supplyDTO.getNo()));
            LatestFailureDTO latestFailureDTO = supplyDTO.getLatestOpenFailure();
            if (latestFailureDTO != null) {
                latestFailureDTO.setSupply_no(encrypt.encrypt(latestFailureDTO.getSupply_no()));
                supplyDTO.setLatestOpenFailure(latestFailureDTO);
            }

            return Response.ok(supplyDTO)
                    .header(INTEGRITY_PROPERTY, hashGenerator(supplyDTO))
                    .build();
        }
        catch (Exception e) {
            if (e.getMessage() == "supply.not.found") {
                return errorResponseWithHash(Response.Status.BAD_REQUEST, e.getMessage());
            } else {
                logger.error("Exception in getSupplyInfo rest service: ", e);
                return errorResponseWithHash(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @PermitAll
    @POST
    @Path("/insertFailure")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public Response insertFailure(Failure f) {
        failureServiceManager = new FailureService();
        securityService = new SecurityService();
        powerCutReportService = new PowerCutReportService();
        Long result;

        try {

            // set otp type in dto
            f.getOtp().setType(Otp.OtpType.FAILURE_ANNOUNCEMENT);
            Otp otp = securityService.getOtpOnlyIfActive(f.getOtp());
            if (otp == null) {
                logger.error("no.active.otp.for.cell");
                return errorResponse(Response.Status.FORBIDDEN, "no.active.otp.for.cell");
            }

            Supply supply = (Supply) failureServiceManager.getSupply(f.getSupply_no()).get("supply");
            if (supply == null) {
                return errorResponse(Response.Status.BAD_REQUEST, "no.supply.found");
            }

            if (supply.getDisconnectedForDebt()) {
                return errorResponse(Response.Status.FORBIDDEN, "is.disconnected.for.debt");
            }

            logger.info(supply.getNo());
            if (powerCutReportService.getSupplysLatestOpenFailure(supply) != null) {
                return errorResponse(Response.Status.FORBIDDEN, "failure.recovery.in.progress");
            }

            f.setOtp(otp);
            //persist only the 8 digits of the supply
            f.setSupply_no(supply.getNo());
            f.setAddress(supply.getAddress());
            f.setPostal_code(supply.getPostal_code());
            f.setKlot(supply.getKlot());
            f.setFailureType(new FailureType(FailureType.ISOLATED_FAILURE_ID));
            f.setArea(supply.getArea());
            f.setCreator(f.getCell());
            if (f.getInput_channel() == null)  {
                f.setInput_channel(Failure.InputChannel.WEBSITE);
            }
            List<Question> questionList = f.getQuestions();
            questionList.add(new Question( Long.valueOf(3), null, null, null, "ΝΑΙ"));
            f.setQuestions(questionList);
            if ((supply.getPoint().x != 0) && (supply.getPoint().y != 0)) {
                List<Point> pointList = new ArrayList<>();
                pointList.add(new Point(null, String.valueOf(supply.getPoint().y), String.valueOf(supply.getPoint().x), null));
                List<Landmark> landmarkList = new ArrayList<>();
                landmarkList.add(new Landmark(null, Landmark.LandmarkType.MARKER, pointList, null));
                f.setLandmarks(landmarkList);
            } else {
                f.setLandmarks(new ArrayList<>());
            }
            List<FailureAssignment> failureAssignmentList = new ArrayList<>();
            failureAssignmentList.add(new FailureAssignment(supply.getGrafeio(), "ΠΣ ΕΡΜΗΣ"));
            f.setAssignments(failureAssignmentList);

            result = failureServiceManager.insertFailureNoSecurity(f, LocalizationUTIL.GREEK);
            logger.info("insertFailure response: " + result);

        } catch (Exception e) {
            logger.error("exception in insertFailure", e);
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return Response.ok(result).build();
    }

    @PermitAll
    @POST
    @Path("/v2/insertFailure")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public Response insertFailureV2(@HeaderParam(INTEGRITY_PROPERTY) String integrityHash, @HeaderParam("Accept-Language") String language, InputStream inputStream) {
        failureServiceManager = new FailureService();
        securityService = new SecurityService();
        powerCutReportService = new PowerCutReportService();

        try {
            String correctedLanguage = powerCutReportService.en4EnglishElseGreek(language);
            String jsonString = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .map(line -> line.trim())
                    .collect(Collectors.joining(""));

            if (!integrityHash.equals(hashGenerator(jsonString))) {
                logger.info("integrity.check.failed");
                return errorResponseWithHash(Response.Status.FORBIDDEN, "access.not.allowed");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Failure f = objectMapper.readValue(jsonString, Failure.class);

            //decrypt input
            EncryptDecrypt decrypt = new EncryptDecrypt();
            f.setName(decrypt.decrypt(f.getName()));
            f.setCell(decrypt.decrypt(f.getCell()));
            f.setEmail(decrypt.decrypt(f.getEmail()));
            f.setSupply_no(decrypt.decrypt(f.getSupply_no()));

            // set otp type in dto
            f.getOtp().setType(Otp.OtpType.FAILURE_ANNOUNCEMENT);
            Otp otpDTO = f.getOtp();
            otpDTO.setCell(decrypt.decrypt(otpDTO.getCell()));
            f.setOtp(otpDTO);
            Otp otp = securityService.getOtpOnlyIfActive(f.getOtp());
            if (otp == null) {
                logger.error("no.active.otp.for.cell");
                return errorResponseWithHash(Response.Status.FORBIDDEN, "no.active.otp.for.cell");
            }

            Supply supply = (Supply) failureServiceManager.getSupply(f.getSupply_no()).get("supply");
            if (supply == null) {
                return errorResponseWithHash(Response.Status.BAD_REQUEST, "no.supply.found");
            }

            if (supply.getDisconnectedForDebt()) {
                return errorResponseWithHash(Response.Status.FORBIDDEN, "is.disconnected.for.debt");
            }

            logger.info(supply.getNo());
            if (powerCutReportService.getSupplysLatestOpenFailure(supply) != null) {
                return errorResponseWithHash(Response.Status.FORBIDDEN, "failure.recovery.in.progress");
            }

            f.setOtp(otp);
            //persist only the 8 digits of the supply
            f.setSupply_no(supply.getNo());
            f.setAddress(supply.getAddress());
            f.setPostal_code(supply.getPostal_code());
            f.setFailureType(new FailureType(FailureType.ISOLATED_FAILURE_ID));
            f.setArea(supply.getArea());
            f.setCreator(f.getCell());
            if (f.getInput_channel() == null)  {
                f.setInput_channel(Failure.InputChannel.WEBSITE);
            }
            List<Question> questionList = f.getQuestions();
            questionList.add(new Question( Long.valueOf(3), null, null, null, "ΝΑΙ"));
            f.setQuestions(questionList);
            if ((supply.getPoint().x != 0) && (supply.getPoint().y != 0)) {
                List<Point> pointList = new ArrayList<>();
                pointList.add(new Point(null, String.valueOf(supply.getPoint().y), String.valueOf(supply.getPoint().x), null));
                List<Landmark> landmarkList = new ArrayList<>();
                landmarkList.add(new Landmark(null, Landmark.LandmarkType.MARKER, pointList, null));
                f.setLandmarks(landmarkList);
            } else {
                f.setLandmarks(new ArrayList<>());
            }
            List<FailureAssignment> failureAssignmentList = new ArrayList<>();
            failureAssignmentList.add(new FailureAssignment(supply.getGrafeio(), "ΠΣ ΕΡΜΗΣ"));
            f.setAssignments(failureAssignmentList);

            Long result = failureServiceManager.insertFailureNoSecurity(f, correctedLanguage);
            logger.info("insertFailure response: " + result);

            return Response.ok(result)
                    .header(INTEGRITY_PROPERTY, hashGenerator(result))
                    .build();

        } catch (Exception e) {
            logger.error("exception in insertFailure", e);
            return errorResponseWithHash(Response.Status.INTERNAL_SERVER_ERROR, "system.error");
        }


    }
    /*
    @PermitAll
    @PUT
    @Path("/recallFailure")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public Response recallFailure(Failure f) {
        failureServiceManager = new FailureService();
        securityService = new SecurityService();
        powerCutReportService = new PowerCutReportService();

        try {
            if ((f.getSupply_no() == null) || (f.getId() == null) || (f.getOtp().getPin() == null) || (f.getOtp().getCell() == null)) {
                logger.info("parameters.are.missing");
                return errorResponse(Response.Status.BAD_REQUEST, "parameters.are.missing");
            }

            // set otp type in dto
            f.getOtp().setType(Otp.OtpType.FAILURE_RECALL);
            Otp otp = securityService.getOtpOnlyIfActive(f.getOtp());
            if (otp == null) {
                logger.error("no.active.otp.for.cell");
                return errorResponse(Response.Status.FORBIDDEN, "no.active.otp.for.cell");
            }

            // fill failure's otp
            f.setOtp(otp);

            powerCutReportService.validateRecalledFailure(f);

            // fill failure's input channel field
            if (f.getInput_channel() == null) {
                f.setInput_channel(Failure.InputChannel.WEBSITE);
            }

            failureServiceManager.markFailureAsRecalled(f);

        } catch (ValidationException e) {
            return errorResponse(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error("Exception in recallFailure: ", e);
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return Response.ok(1).build();
    }
    */
    
    @PermitAll
    @PUT
    @Path("/recallFailure")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public Response recallFailure(Failure f) {
        failureServiceManager = new FailureService();
        securityService = new SecurityService();
        powerCutReportService = new PowerCutReportService();

        try {
        	if (f.getId() == null) {	
        		logger.info("parameters.are.missing");
                return errorResponse(Response.Status.BAD_REQUEST, "parameters.are.missing");
            } 
            
        	f.setFailureType(new FailureType(failureServiceManager.getFailureType(f)));
        	
        	if ((f.getFailureType().getId() != FailureType.DANGEROUS_STATE_ID && f.getSupply_no() == null) || (f.getId() == null) || (f.getOtp().getPin() == null) || (f.getOtp().getCell() == null)) {	
        		logger.info("parameters.are.missing");
                return errorResponse(Response.Status.BAD_REQUEST, "parameters.are.missing");
            }

            // set otp type in dto
            f.getOtp().setType(Otp.OtpType.FAILURE_RECALL);
            Otp otp = securityService.getOtpOnlyIfActive(f.getOtp());
            if (otp == null) {
                logger.error("no.active.otp.for.cell");
                return errorResponse(Response.Status.FORBIDDEN, "no.active.otp.for.cell");
            }

            // fill failure's otp
            f.setOtp(otp);

            powerCutReportService.validateRecalledFailure(f);

            // fill failure's input channel field
            if (f.getInput_channel() == null) {
                f.setInput_channel(Failure.InputChannel.WEBSITE);
            }

            failureServiceManager.markFailureAsRecalled(f);

        } catch (ValidationException e) {
            return errorResponse(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error("Exception in recallFailure: ", e);
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return Response.ok(1).build();
    }
    
    @PermitAll
    @PUT
    @Path("/v2/recallFailure")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public Response recallFailureV2(@HeaderParam(INTEGRITY_PROPERTY) String integrityHash, InputStream inputStream) {
        failureServiceManager = new FailureService();
        securityService = new SecurityService();
        powerCutReportService = new PowerCutReportService();

        try {
            String jsonString = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .map(line -> line.trim())
                    .collect(Collectors.joining(""));

            // check integrity
            if (!integrityHash.equals(hashGenerator(jsonString))) {
                logger.info("integrity.check.failed");
                return errorResponseWithHash(Response.Status.FORBIDDEN, "access.not.allowed");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Failure f = objectMapper.readValue(jsonString, Failure.class);

            // decrypt input
            EncryptDecrypt decrypt = new EncryptDecrypt();
            f.setSupply_no(decrypt.decrypt(f.getSupply_no()));
            f.getOtp().setCell(decrypt.decrypt(f.getOtp().getCell()));

            if ((f.getSupply_no() == null) || (f.getId() == null) || (f.getOtp().getPin() == null) || (f.getOtp().getCell() == null)) {
                logger.info("parameters.are.missing");
                return errorResponseWithHash(Response.Status.BAD_REQUEST, "parameters.are.missing");
            }

            // set otp type in dto
            f.getOtp().setType(Otp.OtpType.FAILURE_RECALL);
            Otp otp = securityService.getOtpOnlyIfActive(f.getOtp());
            if (otp == null) {
                logger.error("no.active.otp.for.cell");
                return errorResponseWithHash(Response.Status.FORBIDDEN, "no.active.otp.for.cell");
            }

            // fill failure's otp
            f.setOtp(otp);

            powerCutReportService.validateRecalledFailure(f);

            // fill failure's input channel field
            if (f.getInput_channel() == null) {
                f.setInput_channel(Failure.InputChannel.WEBSITE);
            }

            failureServiceManager.markFailureAsRecalled(f);

            return Response.ok(1)
                    .header(INTEGRITY_PROPERTY, hashGenerator(1))
                    .build();

        } catch (ValidationException e) {
            return errorResponseWithHash(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error("Exception in recallFailure: ", e);
            return errorResponseWithHash(Response.Status.INTERNAL_SERVER_ERROR, "system.error");
        }

    }

    @PermitAll
    @POST
    @Path("/validateFailure")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public Response validateFailure(Failure f) {
        powerCutReportService = new PowerCutReportService();

        if ((f.getSupply_no() == null) || (f.getId() == null)) {
            logger.info("parameters.are.missing");
            return errorResponse(Response.Status.BAD_REQUEST, "parameters.are.missing");
        }

        try {
            powerCutReportService.validateRecalledFailure(f);
            return Response.ok(powerCutReportService.getSupplyInfo(f.getSupply_no())).build();
        }
        catch (ValidationException e) {
            return errorResponse(Response.Status.BAD_REQUEST, e.getMessage());
        }
        catch (Exception e) {
            if (e.getMessage() == "supply.not.found") {
                logger.error("Exception in validateFailure rest service: ", e);
                return errorResponse(Response.Status.BAD_REQUEST, e.getMessage());
            } else {
                logger.error("Exception in validateFailure rest service: ", e);
                return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @PermitAll
    @POST
    @Path("/v2/validateFailure")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public Response validateFailureV2(@HeaderParam(INTEGRITY_PROPERTY) String integrityHash, InputStream inputStream) {
        powerCutReportService = new PowerCutReportService();
        try {
            String jsonString = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .map(line -> line.trim())
                    .collect(Collectors.joining(""));

            // check integrity
            if (!integrityHash.equals(hashGenerator(jsonString))) {
                logger.info("integrity.check.failed");
                return errorResponseWithHash(Response.Status.FORBIDDEN, "access.not.allowed");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Failure f = objectMapper.readValue(jsonString, Failure.class);

            //decrypt inputs
            EncryptDecrypt encryptDecrypt = new EncryptDecrypt();
            f.setSupply_no(encryptDecrypt.decrypt(f.getSupply_no()));

            if ((f.getSupply_no() == null) || (f.getId() == null)) {
                logger.info("parameters.are.missing");
                return errorResponseWithHash(Response.Status.BAD_REQUEST, "parameters.are.missing");
            }

            powerCutReportService.validateRecalledFailure(f);
            SupplyDTO supplyDTO = powerCutReportService.getSupplyInfo(f.getSupply_no());
            // encrypt response
            supplyDTO.setLastTenantName(encryptDecrypt.encrypt(supplyDTO.getLastTenantName()));
            supplyDTO.setNo(encryptDecrypt.encrypt(supplyDTO.getNo()));
            return Response.ok(supplyDTO)
                    .header(INTEGRITY_PROPERTY, hashGenerator(supplyDTO))
                    .build();
        }
        catch (ValidationException e) {
            return errorResponseWithHash(Response.Status.BAD_REQUEST, e.getMessage());
        }
        catch (Exception e) {
            if (e.getMessage() == "supply.not.found") {
                logger.error("Exception in validateFailure rest service: ", e);
                return errorResponseWithHash(Response.Status.BAD_REQUEST, e.getMessage());
            } else {
                logger.error("Exception in validateFailure rest service: ", e);
                return errorResponseWithHash(Response.Status.INTERNAL_SERVER_ERROR, "system.error");
            }
        }
    }

    @PermitAll
    @GET
    @Path("/getAnnouncements")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")

    public Response getAnnouncements(@QueryParam("version") String version, @QueryParam("os") MobileAppAnnouncement.DeviceOS os) {
        powercutreportDataManager = new POWERCUTREPORTDataManager();

        if ((version == null) || (os == null)) {
            logger.info("parameters.are.missing");
            return errorResponse(Response.Status.BAD_REQUEST, "parameters.are.missing");
        }

        try {
            List<MobileAppAnnouncementDTO> announcements = powercutreportDataManager.getAnnouncements(version, os, LocalizationUTIL.GREEK);
            return Response.ok(announcements).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @PermitAll
    @GET
    @Path("/v2/getAnnouncements")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")

    public Response getAnnouncementsV2(
            @HeaderParam(LocalizationUTIL.LANGUAGE_PROPERTY) String language,
            @QueryParam("version") String version,
            @QueryParam("os") MobileAppAnnouncement.DeviceOS os) {

        powercutreportDataManager = new POWERCUTREPORTDataManager();
        powerCutReportService = new PowerCutReportService();

        try {
            String correctedLanguage = powerCutReportService.en4EnglishElseGreek(language);

            if ((version == null) || (os == null)) {
                logger.info("parameters.are.missing");
                return errorResponseWithHash(Response.Status.BAD_REQUEST, "parameters.are.missing");
            }

            List<MobileAppAnnouncementDTO> announcements = powercutreportDataManager.getAnnouncements(version, os, correctedLanguage);
            return Response.ok(announcements)
                    .header(INTEGRITY_PROPERTY, hashGenerator(announcements))
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return errorResponseWithHash(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
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
    
    @PermitAll
    @POST
    @Path("/insertNetworkHazardReport")
    @Consumes(MediaType.MULTIPART_FORM_DATA + ";charset=utf-8")
    public Response insertNetworkHazardReport(@Context HttpServletRequest request) {
        failureServiceManager = new FailureService();
        securityService = new SecurityService();
        Long result = 0L;
        
        ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
        
        List<FileItem> files = new ArrayList<FileItem>();
        Failure nhr = new Failure();

        try {
        	
        	if (ServletFileUpload.isMultipartContent(request)) {
                //Use shared FileCleaningTracker from ServletContext
                FileCleaningTracker fileCleaningTracker = (FileCleaningTracker)
                    request.getServletContext().getAttribute("fileCleaningTracker");     		
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setRepository(new File("/u01/tempfiles/pfr"));
                factory.setFileCleaningTracker(fileCleaningTracker);
                ServletFileUpload upload = new ServletFileUpload(factory);
		    	List<FileItem> items = null;
		   
		    	try {
		    		request.setCharacterEncoding("UTF-8");
		    		items = upload.parseRequest(new ServletRequestContext(request));
		    	} catch (FileUploadException | UnsupportedEncodingException e) {
                    logger.error("Error in parsing request payload: ",e);
					return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		    	}
		    
		    	if (items != null) {
		    		Iterator<FileItem> iter = items.iterator();
		    		while (iter.hasNext()) {
		    			FileItem item = iter.next();
		    			if (!item.isFormField() && item.getSize() > 0) {
		    				logger.info("Received file with name " + item.getName() + " for input field " + item.getFieldName());
		    				try {
		    					files.add(item);
		    				} catch (Exception e) {
                                logger.error("Cannot add file to list: ",e);
		    					return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		    				}
		    				
		    			} else {
		    				if (item.getFieldName().equals("network_hazard_report")) {
		    					nhr = mapper.readValue(item.getString("UTF-8"), Failure.class);
		    				
		    					// set otp type in dto
		    		            nhr.getOtp().setType(Otp.OtpType.FAILURE_ANNOUNCEMENT);
		    		            Otp otp = securityService.getOtpOnlyIfActive(nhr.getOtp());
		    		            if (otp == null) {
		    		                logger.error("no.active.otp.for.cell");
		    		                return errorResponse(Response.Status.FORBIDDEN, "no.active.otp.for.cell");
		    		            }

		    		            nhr.setOtp(otp);
		    		            nhr.setFailureType(new FailureType(FailureType.DANGEROUS_STATE_ID));
		    		            nhr.setCreator(nhr.getCell());
		    		            if (nhr.getInput_channel() == null)  {
		    		            	nhr.setInput_channel(Failure.InputChannel.WEBSITE);
		    		            }
		    		            
		    		            QuestionList question_list = mapper.readValue(nhr.getQuestions_serialized(), QuestionList.class);	
		    		            //List<Question> questionList = mapper.reader().forType(new TypeReference<List<Question>>(){}).readValue(nhr.getQuestions_serialized());
		    		            
		    		            List<Question> questionList = question_list.getQuestions();
		    		            nhr.setQuestions(questionList);
		    		            
		    		            if ((!"0".equals(nhr.getLatitude())) && (!"0".equals(nhr.getLongitute()))) {
		    		                List<Point> pointList = new ArrayList<>();
		    		                pointList.add(new Point(null, nhr.getLongitute(), nhr.getLatitude(),  null));
		    		                List<Landmark> landmarkList = new ArrayList<>();
		    		                landmarkList.add(new Landmark(null, Landmark.LandmarkType.MARKER, pointList, null));
		    		                nhr.setLandmarks(landmarkList);
		    		            } else {
		    		                nhr.setLandmarks(new ArrayList<>());
		    		            }
		    		            

		    				}
		    				
		    			}
		    	   }
                    result = failureServiceManager.insertNetworkHazardReport(nhr, files, LocalizationUTIL.GREEK);
		        }
		    	
        	}

        } catch (Exception e) {
            logger.error("exception in insertNetworkHazardReport", e);
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return Response.ok(result).build();
    }

    @PermitAll
    @POST
    @Path("/v2/insertNetworkHazardReport")
    @Consumes(MediaType.MULTIPART_FORM_DATA + ";charset=utf-8")
    public Response insertNetworkHazardReportV2(@Context HttpServletRequest request, @HeaderParam("Accept-Language") String language) {
        failureServiceManager = new FailureService();
        securityService = new SecurityService();
        powerCutReportService = new PowerCutReportService();
        Long result = 0L;
        
        ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
        
        List<FileItem> files = new ArrayList<FileItem>();
        Failure nhr = new Failure();

        try {
            String correctedLanguage = powerCutReportService.en4EnglishElseGreek(language);
        	
        	if (ServletFileUpload.isMultipartContent(request)) {
                //Use shared FileCleaningTracker from ServletContext
                FileCleaningTracker fileCleaningTracker = (FileCleaningTracker)
                    request.getServletContext().getAttribute("fileCleaningTracker");          		
		    	DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setRepository(new File("/u01/tempfiles/pfr"));
                factory.setFileCleaningTracker(fileCleaningTracker);                
		    	ServletFileUpload upload = new ServletFileUpload(factory);
		    	List<FileItem> items = null;
		   
		    	try {
		    		request.setCharacterEncoding("UTF-8");
		    		items = upload.parseRequest(new ServletRequestContext(request));
		    	} catch (FileUploadException | UnsupportedEncodingException e) {
		    		logger.error("Error in parsing request payload: ",e);
					return errorResponseWithHash(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		    	}
		    	if (items != null) {
		    		Iterator<FileItem> iter = items.iterator();
		    		while (iter.hasNext()) {
		    			FileItem item = iter.next();
		    			if (!item.isFormField() && item.getSize() > 0) {
		    				logger.info("Received file with name " + item.getName() + " for input field " + item.getFieldName());
		    				try {
		    					files.add(item);
		    				} catch (Exception e) {
		    					logger.error("Cannot add file to list: ",e);
		    					return errorResponseWithHash(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		    				}
		    				
		    			} else {
		    				if (item.getFieldName().equals("network_hazard_report")) {
		    					 if (!request.getHeader(INTEGRITY_PROPERTY).equals(hashGenerator(item.getString("UTF-8")))) {
		    			        	logger.info("integrity.check.failed");
		    			            return errorResponseWithHash(Response.Status.FORBIDDEN, "access.not.allowed");
		    			        }
		    					 
		    					nhr = mapper.readValue(item.getString("UTF-8"), Failure.class);
		    					
		    					/***************/
		    		            EncryptDecrypt decrypt = new EncryptDecrypt();
		    		            nhr.setName(decrypt.decrypt(nhr.getName()));
		    		            nhr.setCell(decrypt.decrypt(nhr.getCell()));
		    		            nhr.setEmail(decrypt.decrypt(nhr.getEmail()));		    		            
		    		            
		    		            // set otp type in dto
		    		            nhr.getOtp().setType(Otp.OtpType.FAILURE_ANNOUNCEMENT);
		    		            Otp otpDTO = nhr.getOtp();
		    		            otpDTO.setCell(decrypt.decrypt(otpDTO.getCell()));
		    		            nhr.setOtp(otpDTO);
		    		            Otp otp = securityService.getOtpOnlyIfActive(nhr.getOtp());
		    		            /***************/
		    		            
		    					// set otp type in dto
		    		          //// nhr.getOtp().setType(Otp.OtpType.FAILURE_ANNOUNCEMENT);
		    		          //// Otp otp = securityService.getOtpOnlyIfActive(nhr.getOtp());
		    		            if (otp == null) {
		    		                logger.error("no.active.otp.for.cell");
		    		                return errorResponseWithHash(Response.Status.FORBIDDEN, "no.active.otp.for.cell");
		    		            }

		    		            nhr.setOtp(otp);
		    		            nhr.setFailureType(new FailureType(FailureType.DANGEROUS_STATE_ID));
		    		            nhr.setCreator(nhr.getCell());
		    		            if (nhr.getInput_channel() == null)  {
		    		            	nhr.setInput_channel(Failure.InputChannel.WEBSITE);
		    		            }
		    		            
		    		            QuestionList question_list = mapper.readValue(nhr.getQuestions_serialized(), QuestionList.class);	
		    		            //List<Question> questionList = mapper.reader().forType(new TypeReference<List<Question>>(){}).readValue(nhr.getQuestions_serialized());
		    		            
		    		            List<Question> questionList = question_list.getQuestions();
		    		            nhr.setQuestions(questionList);
		    		            
		    		            if ((nhr.getLatitude()!= "0") && (nhr.getLongitute() != "0")) {
		    		                List<Point> pointList = new ArrayList<>();
		    		                pointList.add(new Point(null, nhr.getLongitute(), nhr.getLatitude(),  null));
		    		                List<Landmark> landmarkList = new ArrayList<>();
		    		                landmarkList.add(new Landmark(null, Landmark.LandmarkType.MARKER, pointList, null));
		    		                nhr.setLandmarks(landmarkList);
		    		            } else {
		    		                nhr.setLandmarks(new ArrayList<>());
		    		            }
		    				}
		    				
		    			}
		    	   }
                    result = failureServiceManager.insertNetworkHazardReport(nhr, files, correctedLanguage);
		        }
		    	
        	}

            return Response.ok(result)
                            .header(INTEGRITY_PROPERTY, hashGenerator(result))
                            .build();
        } catch (Exception e) {
            logger.error("exception in insertNetworkHazardReport", e);
            return errorResponseWithHash(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
    
}
