package gr.deddie.pfr.services;

import gr.deddie.pfr.managers.AUTHDataManager;
import gr.deddie.pfr.managers.PFRDataManager;
import gr.deddie.pfr.model.MenuItem;
import gr.deddie.pfr.model.MenuItemDTO;
import gr.deddie.pfr.model.UserLoginInfo;
import gr.deddie.pfr.model.UserRoleDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

//import java.security.MessageDigest;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;

public class AuthService {

	private static Logger logger = LogManager.getLogger(AuthService.class);
	private static PFRDataManager pfrDataManager;
	private static AUTHDataManager authDataManager;

	public UserLoginInfo authenticate(String username, String password) throws Exception {
		pfrDataManager = new PFRDataManager();

		return pfrDataManager.authenticate(username, password);
	}

	public List<String> getUserRoles(String token) throws Exception {
		authDataManager = new AUTHDataManager();
		List<String> roles = null;

		roles = authDataManager.getUserRoles(token);

		return roles;
	}

	public List<UserRoleDTO> getUserRoleDTOs(String token) throws Exception {
		authDataManager = new AUTHDataManager();

		return authDataManager.getUserRoleDTOs(token);
	}

	public String getUser(String token) throws Exception {
		authDataManager = new AUTHDataManager();

		return authDataManager.getUser(token);
	}

	public MenuItemDTO buildMenuTree(String token, Integer appId) throws Exception {
		authDataManager = new AUTHDataManager();

		List<MenuItem> menuItems = authDataManager.getMenuItems(token,appId);

		if (menuItems.isEmpty()) {
			return null;
		}

		Integer rootId = null;
		MenuItemDTO menuRoot = null;

		for (Iterator<MenuItem> i = menuItems.iterator(); i.hasNext(); ) {
			MenuItem item = i.next();
			if (item.getParentId() == null) {
				menuRoot = new MenuItemDTO(item.getTitle(),item.getHref());
				rootId = item.getId();
				break;
			}
		}

		menuRoot = fillSubItems(menuItems,menuRoot,rootId);

		return menuRoot;
	}

	private MenuItemDTO fillSubItems(List<MenuItem> menuItems, MenuItemDTO rootItem, Integer parentId) {
		List<MenuItemDTO> subItems = new ArrayList<>();
		for (Iterator<MenuItem> i = menuItems.iterator(); i.hasNext(); ) {
			MenuItem item = i.next();
			if (item.getParentId() == parentId) {
				subItems.add(new MenuItemDTO(item.getTitle(),item.getHref()));
			}
		}
		rootItem.setSubItems(subItems);
		return rootItem;
	}

	//	public static String encryptPassword(String password)
//	{
//	    String sha1 = "";
//	    try
//	    {
//	        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
//	        crypt.reset();
//	        crypt.update(password.getBytes("UTF-8"));
//	        sha1 = UtilMethods.byteToHex(crypt.digest());
//	    }
//	    catch(Exception e)
//	    {
//	    	logger.error(e.getMessage());
//	    }
//	    return sha1;
//	}
//
//
//	public static void lastUpdSecUsers (String token) throws Exception {
//
//		if ( token == null || token.equals("") )
//			return;
//
//		PreparedStatement stmt = null;
//        Connection conn = null;
//
//            try {
//
//                conn = Database.getDBConnection();
//
//                String query = "update sec_sessions set last_upd = sysdate where token=? ";
//
//                stmt = conn.prepareStatement(query);
//                stmt.setString(1, token);
//            	stmt.executeUpdate();
//
//            } catch (Exception e) {
//            	SystemLogger.logger.error(e.getMessage());
//                throw new Exception(e);
//
//            }
//            finally{
//
//                if ( stmt != null){
//                    stmt.close();
//                    stmt = null;
//                }
//
//
//                if ( conn != null ){
//                    conn.close();
//                    conn = null;
//                }
//
//
//            }//
//
//
//	    }//lastUpdSecUsers
//
//	public static int insertLDAPUsers() {
//
//		int countInserted = 0;
//		try {
//			User[] allLDAPUsers = ActiveDirectoryService.getUsers(ActiveDirectoryService.getConnection("redmine", "fb[[6S?;", "DEDDIE.GR"));
//
//			//countInserted = allLDAPUsers.length;
//
//			ArrayList<String> storeSqlStatements = new ArrayList<String>();
//			String insertStatement = "insert into SEC_LDAP_USERS (FIRST_NAME, LAST_NAME, DOMAIN, EMAIL, USER_NAME, POSITION, DEPARTMENT, COMPANY) values (";
//			String userDisplayName = null;
//			String userFirstName = null;
//			String userLastName = null;
//			String userDomain = null;
//			String userEmail = null;
//			String userName = null;
//			String position = null;
//			String department = null;
//			String company = null;
//
//			ArrayList<DbColumnDefinition> dbCols = new ArrayList<DbColumnDefinition>();
//			dbCols.add(new DbColumnDefinition("FIRST_NAME", "N"));
//			dbCols.add(new DbColumnDefinition("LAST_NAME", "N"));
//			dbCols.add(new DbColumnDefinition("DOMAIN", "N"));
//			dbCols.add(new DbColumnDefinition("EMAIL", "N"));
//			dbCols.add(new DbColumnDefinition("USER_NAME", "N"));
//			dbCols.add(new DbColumnDefinition("POSITION", "N"));
//			dbCols.add(new DbColumnDefinition("DEPARTMENT", "N"));
//			dbCols.add(new DbColumnDefinition("COMPANY", "N"));
//
//			String generatedInsertStatement = Database.generateInsertStatement(null, null, dbCols, "SEC_LDAP_USERS");
//
//			for (int idx = 0; idx < allLDAPUsers.length; idx++) {
//
//				userDisplayName = allLDAPUsers[idx].getAttributeValue("displayName")==null?"NULL":allLDAPUsers[idx].getAttributeValue("displayName");
//				if (userDisplayName != "NULL" && userDisplayName.contains(" ")) {
//					userLastName = "'"+(userDisplayName.split(" "))[0]+"'";
//					userFirstName = "'"+(userDisplayName.split(" "))[1]+"'";
//				} else {
//					userLastName = "NULL";
//					userFirstName = "NULL";
//				}
//				userDomain = allLDAPUsers[idx].getAttributeValue("memberOf")==null?"NULL":allLDAPUsers[idx].getAttributeValue("memberOf").trim().replaceAll("[']", Matcher.quoteReplacement("''"));
//				if (userDomain != "NULL")
//					userDomain = "'"+userDomain.substring(userDomain.indexOf("DC=")+3, userDomain.lastIndexOf(","))+"'";
//				userEmail = allLDAPUsers[idx].getAttributeValue("mail")==null?"NULL":"'"+allLDAPUsers[idx].getAttributeValue("mail").trim().replaceAll("[']", Matcher.quoteReplacement("''"))+"'";
//				userName = allLDAPUsers[idx].getAttributeValue("sAMAccountName")==null?"NULL":"'"+allLDAPUsers[idx].getAttributeValue("sAMAccountName").trim().replaceAll("[']", Matcher.quoteReplacement("''"))+"'";
//				position = allLDAPUsers[idx].getAttributeValue("title")==null?"NULL":"'"+allLDAPUsers[idx].getAttributeValue("title").trim().replaceAll("[']", Matcher.quoteReplacement("''"))+"'";
//				department = allLDAPUsers[idx].getAttributeValue("department")==null?"NULL":"'"+allLDAPUsers[idx].getAttributeValue("department").trim().replaceAll("[']", Matcher.quoteReplacement("''"))+"'";
//				company = allLDAPUsers[idx].getAttributeValue("company")==null?"NULL":"'"+allLDAPUsers[idx].getAttributeValue("company").trim().replaceAll("[']", Matcher.quoteReplacement("''"))+"'";
//
//				//storeSqlStatements.add( insertStatement + userFirstName + "," + userLastName + "," + userDomain + "," + userEmail + "," + userName + "," + position + "," + department + "," + company + ")");
//
//				countInserted += Database.executeDbUpdateStatement(insertStatement + userFirstName + "," + userLastName + "," + userDomain + "," + userEmail + "," + userName + "," + position + "," + department + "," + company + ")", null);
//
//				//SystemLogger.logger.info(insertStatement + userFirstName + "," + userLastName + "," + userDomain + "," + userEmail + "," + userName + "," + position + "," + department + "," + company + ")");
//			}
//
//			//BatchSqlExecutionThread dbWriter = new BatchSqlExecutionThread(storeSqlStatements, 1, 10000);
//			//dbWriter.run();
//		}
//		catch (Exception e) {
//			SystemLogger.logger.error(e.toString());
//			SystemLogger.logger.debug(UtilMethods.stackTraceToString(e));
//			countInserted = 0;
//		}
//
//		return countInserted;
//
//	}
//
//	public static int deleteLDAPUsers() {
//		int countDeleted = 0;
//		try {
//			countDeleted = Database.executeDbUpdateStatement("delete from SEC_LDAP_USERS", null);
//		}
//		catch (Exception e) {
//			SystemLogger.logger.error(e.toString());
//			SystemLogger.logger.debug(UtilMethods.stackTraceToString(e));
//			countDeleted = 0;
//		}
//
//		return countDeleted;
//	}
}
