package gr.deddie.pfr.rest;

/**
 * Created by M.Masikos on 12/10/2016.
 */

import gr.deddie.pfr.services.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.internal.util.Base64;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * This filter verify the access permissions for a user
 * based on token provided in request
 * */
@Provider
public class SecurityInterceptor extends BaseResource implements javax.ws.rs.container.ContainerRequestFilter{

    @Context
    private ResourceInfo resourceInfo;

    private static Logger logger = LogManager.getLogger(SecurityInterceptor.class);
    private static AuthService authServiceManager;
    private static final String AUTHORIZATION_PROPERTY = "X-Auth-Token";
//    private Response ACCESS_DENIED = errorResponse(Response.Status.UNAUTHORIZED, "You cannot access this resource");
//    private Response ACCESS_FORBIDDEN = errorResponse(Response.Status.FORBIDDEN, "Access blocked for all users !!");

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();

        if(requestContext.getRequest().getMethod().equals("OPTIONS")) {
            //throw new WebApplicationException(Response.Status.OK);
            requestContext.abortWith(Response.status(Response.Status.OK).build());
            return;
        }

        //Access allowed for all
        if( ! method.isAnnotationPresent(PermitAll.class)) {
            //Access denied for all
            if (method.isAnnotationPresent(DenyAll.class)) {
                requestContext.abortWith(errorResponse(Response.Status.FORBIDDEN, "Access blocked for all users !!"));
                return;
            }

            //Get request headers
            final MultivaluedMap<String, String> headers = requestContext.getHeaders();

            //Fetch authorization header
            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

            //If no authorization information present; block access
            if (authorization == null || authorization.isEmpty()) {
                requestContext.abortWith(errorResponse(Response.Status.UNAUTHORIZED, "You cannot access this resource"));
                return;
            }

            //Get token
            final String token = authorization.get(0);

            //Verify user access
            if (method.isAnnotationPresent(RolesAllowed.class)) {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

                //Is user valid?
                if (!isUserAllowed(token, rolesSet)) {
                    requestContext.abortWith(errorResponse(Response.Status.UNAUTHORIZED, "You cannot access this resource"));
                    return;
                }
                //user is valid
                else {
                    return;
                }
            }

            //the rest of the requests are denied
            requestContext.abortWith(errorResponse(Response.Status.UNAUTHORIZED, "You cannot access this resource"));
            return;
        }
    }

    private boolean isUserAllowed(final String token, final Set<String> rolesSet)
    {
        authServiceManager = new AuthService();
        boolean isAllowed = false;
        List<String> userRoles = null;

        //Step 1. Fetch password from database and match with password in argument
        //If both match then get the defined role for user from database and continue; else return isAllowed [false]
        //Access the database and do this part yourself
        //String userRole = userMgr.getUserRole(username);

        try {
            userRoles = authServiceManager.getUserRoles(token);
        } catch (Exception e) {
            logger.error("error = " + e.getStackTrace().toString());
            return isAllowed;
        }

        if(userRoles != null)
        {
            for (String role : userRoles) {
                //Step 2. Verify user role
                if(rolesSet.contains(role))
                {
                    isAllowed = true;
                    break;
                }
            }
        }
        return isAllowed;
    }
}
