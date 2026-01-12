package gr.deddie.pfr.rest;

import gr.deddie.pfr.model.UserLoginInfo;
import gr.deddie.pfr.services.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public class AuthRESTService extends BaseResource{

	private static Logger logger = LogManager.getLogger(AuthRESTService.class);
	private static AuthService authServiceManager;
	private static final String AUTHORIZATION_PROPERTY = "X-Auth-Token";

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json; charset=UTF-8")
	public Response requestlogin (@FormParam("username") String username, @FormParam("password") String password) throws Exception {
		UserLoginInfo userLoginInfo;

		if ((username == null) || (username.equals(""))) {
			return errorResponse(Response.Status.NOT_FOUND, "EMPTY_USR");
		}
		if ((password == null) || (password.equals(""))) {
			return errorResponse(Response.Status.NOT_FOUND, "EMPTY_PWD");
		}

		try {
			userLoginInfo = authServiceManager.authenticate(username, password);

			if (userLoginInfo != null) {
				return Response.ok(userLoginInfo).build();
			} else {
				return errorResponse(Response.Status.NOT_FOUND, "USR_OR_PWD_INCORRECT");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@PermitAll
	@GET
	@Path("/getMenuTree")
	@Produces("application/json; charset=UTF-8")
	public Response getMenuTree (@QueryParam("app_id") Integer appId, @HeaderParam(AUTHORIZATION_PROPERTY) String token) throws Exception {
		authServiceManager = new AuthService();

		try {
			return Response.ok(authServiceManager.buildMenuTree(token, appId)).build();
		} catch (Exception e) {
			logger.error("exception in getMenuTree", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@RolesAllowed({"pfr_user", "pfr_callcenter"})
	@GET
	@Path("/getUserRoles")
	@Produces("application/json; charset=UTF-8")
	public Response getUserRoles (@HeaderParam(AUTHORIZATION_PROPERTY) String token) throws Exception {
		authServiceManager = new AuthService();

		try {
			return Response.ok(authServiceManager.getUserRoleDTOs(token)).build();
		} catch (Exception e) {
			logger.error("exception in getUserRoles", e);
			return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

//	@POST
//	@Path("/logout")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces("application/json; charset=UTF-8")
//	public String logout (String requestParams) throws Exception {
//
//			PreparedStatement stmt = null;
//	        Connection conn = null;
//	        ResultSet rs = null;
//	        String result = "";
//	        PreparedStatement stmtIns = null;
//
//	            try {
//
//	            	HashMap<String, String> jsonMap = UtilMethods.jsonMap(requestParams);
//
//	            	if ( jsonMap.get("usr") == null || jsonMap.get("usr").equals("") )
//	        			return "EMPTY_USR";
//
//	            	conn = Database.getDBConnection();
//
//	                String query = "update sec_sessions set end_date=sysdate, last_upd=sysdate where end_date is null and id in ( " +
//	                			   "select s.id from sec_sessions s "+
//									"join sec_users usr on usr.id =s.sec_user_id "+
//									" where usr.usr = ? )";
//
//	                stmt = conn.prepareStatement(query);
//	                stmt.setString(1, jsonMap.get("usr"));
//
//	                int  res = stmt.executeUpdate();
//	                if ( res > 0 )
//	                	result = "SUCCESS";
//
//	            } catch (Exception e) {
//	            	result = "ERROR";
//	                throw new Exception(e);
//
//	            }
//	            finally{
//
//	                if ( rs != null ){
//	                  rs.close();
//	                  rs = null;
//	                }
//	                if ( stmt != null){
//	                    stmt.close();
//	                    stmt = null;
//	                }
//
//	                if ( stmtIns != null){
//	                	stmtIns.close();
//	                	stmtIns = null;
//	                }
//
//	                if ( conn != null ){
//	                    conn.close();
//	                    conn = null;
//	                }
//
//
//	            }//
//
//
//	            return result;
//
//
//		    }//logout
	
	
}
