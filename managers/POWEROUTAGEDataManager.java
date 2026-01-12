package gr.deddie.pfr.managers;

import java.util.*;
import java.util.stream.Collectors;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;
import gr.deddie.pfr.dao.*;
import gr.deddie.pfr.utilities.LocalizationUTIL;
import javassist.NotFoundException;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.deddie.pfr.model.*;

import javax.naming.AuthenticationException;

/**
 * Created by M.Paschou on 15/12/2017.
 */

public class POWEROUTAGEDataManager {

	private static Logger logger = LogManager.getLogger(POWEROUTAGEDataManager.class);

	// for dd lists
	public List<PerifereiakiEnotita> getPerifereiakesEnotites() {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			return poweroutageMapper.getPerifereiakesEnotites();
		} catch (Exception e) {
			logger.error("exception in getPerifereiakesEnotites", e);
		} finally {
			sqlSession.close();
		}
		return null;
	}

	public List<KallikratikosOTA> getKallikratikosOTAList() {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			return poweroutageMapper.getKallikratikosOTAList();
		} catch (Exception e) {
			logger.error("exception in getKallikratikosOTAList", e);
		} finally {
			sqlSession.close();
		}
		return null;
	}

	public List<Perifereia> getPerifereies() {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			return poweroutageMapper.getPerifereies();
		} catch (Exception e) {
			logger.error("exception in getPerifereies", e);
		} finally {
			sqlSession.close();
		}
		return null;
	}

	public PowerOutage getPowerOutage(Long id, String token) {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		try {
			DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
			datasecurityMapper.setAppUserToken(token);
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			return poweroutageMapper.getPowerOutage(id, LocalizationUTIL.GREEK);
		} catch (Exception e) {
			logger.error("exception in getPowerOutage", e);
		} finally {
			sqlSession.close();
		}
		return null;
	}

	public PowerOutage getPowerOutage(String language, Long id) {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			return poweroutageMapper.getPowerOutage(id, language);
		} catch (Exception e) {
			logger.error("exception in getPowerOutage", e);
		} finally {
			sqlSession.close();
		}
		return null;
	}

	public PowerOutage getTALDPowerOutage(Long power_outage_dfr_id) {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			return poweroutageMapper.getTALDPowerOutage(power_outage_dfr_id, LocalizationUTIL.GREEK);
		} catch (Exception e) {
			logger.error("exception in getTALDPowerOutage", e);
		} finally {
			sqlSession.close();
		}
		return null;
	}

	// get ypostathmous YM
	public List<YpostathmosYM> getYpostYMGrammesMTExypPerioxesByVPD(String token) {
		List<YpostathmosYM> result = null;

		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		try {
			DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
			datasecurityMapper.setAppUserToken(token);
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			result = poweroutageMapper.getYpostathmoiYMGrammesMTExypPerioxesbyVPD();
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in getYpostYMGrammesMTExypPerioxesByVPD", e);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	// get ypostathmous YM
	public List<YpostathmosYM> getYpostYMGrammesMTExypDEByVPD(String token) {
		List<YpostathmosYM> result = null;

		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		try {
			DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
			datasecurityMapper.setAppUserToken(token);
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			result = poweroutageMapper.getYpostathmoiYMGrammesMTExypDEbyVPD();
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in getYpostYMGrammesMTExypDEByVPD", e);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	// get exyphretoymenes perioxes by grammiMT by VPD
	public List<ExyphretoumeniPerioxi> getExyphretoumenesPerioxesbyGrammiMTbyVPD(String token, GrammiMT grammiMT) {
		List<ExyphretoumeniPerioxi> result = null;

		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		try {
			DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
			datasecurityMapper.setAppUserToken(token);
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			result = poweroutageMapper.getExyphretoumenesPerioxesbyGrammiMTbyVPD(grammiMT.getYpostathmos_ym_id(), grammiMT.getMetasxhmatisths_ym_id(), grammiMT.getPylh_id());
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in getExyphretoumenesPerioxesbyGrammiMTbyVPD", e);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	// get exyphretoymenes dhmotikes enotites by grammiMT by VPD
	public List<ExyphretoumeniDhmEnothta> getExyphretoumenesDhmEnotitesbyGrammiMTbyVPD(String token, GrammiMT grammiMT) {
		List<ExyphretoumeniDhmEnothta> result = null;

		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		try {
			DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
			datasecurityMapper.setAppUserToken(token);
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			result = poweroutageMapper.getExyphretoumenesDhmEnotitesbyGrammiMTbyVPD(grammiMT.getYpostathmos_ym_id(), grammiMT.getMetasxhmatisths_ym_id(), grammiMT.getPylh_id());
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in getExyphretoumenesDhmEnotitesbyGrammiMTbyVPD", e);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	// get exyphretoymenes perioxes by VPD
	public List<ExyphretoumeniPerioxiDTO> getExyphretoumenesPerioxesbyStatusesbyVPD(String token, List<ExyphretoumeniPerioxi.ExyphretoymeniPerioxiStatus> statuses) {

		List<ExyphretoumeniPerioxiDTO> result = null;

		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		try {
			DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
			datasecurityMapper.setAppUserToken(token);
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			result = poweroutageMapper.getExyphretoumenesPerioxesbyStatusesbyVPD(statuses);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in getExyphretoumenesPerioxesbyVPD", e);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	// to insert new Power Outage
	public Integer insertPowerOutage(PowerOutage po, String token) throws Exception{
		List<PowerOutageAudit> powerOutageAuditList = new ArrayList<>();
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

		try {
			
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);


			// retrieve user based on token
			String user = pfrMapper.getUser(token);
			
			if (user == null) {
				throw new AuthenticationException("no active session found");
			} else {
				po.setCreator(user);
			}

			// insert powerOutage and get id
			poweroutageMapper.insertPowerOutage(po);

			// generate object with differences in power outage audit
			DiffNode diffNode = ObjectDifferBuilder.buildDefault().compare(po, new PowerOutage());

			// generate audits for changes in the power outage object
			if (diffNode.hasChanges()) {
				diffNode.visit(new DiffNode.Visitor()
				{
					@Override
					public void node(final DiffNode node, final Visit visit)
					{
						if (!node.isRootNode() && !node.hasChildren() && Arrays.stream(PowerOutage.auditedFields).anyMatch(node.getPropertyName()::equals)) {
							powerOutageAuditList.add(new PowerOutageAudit(node.getPropertyName(),node.getState().toString(),node.canonicalGet(po).toString(), user, po.getId()));
						}
					}
				});
			}

			if ((po.getExyphretoumeniPerioxiList() != null) && (po.getExyphretoumeniPerioxiList().size() > 0)) {
				// generate audits for changes in list with exyphretoumenes perioxes
				for (ExyphretoumeniPerioxi item : po.getExyphretoumeniPerioxiList()) {
					powerOutageAuditList.add(new PowerOutageAudit("ExyphretoumeniPerioxi", "ADDED", item.getId().toString(), user, po.getId()));
				}
				// insert related exyphretoymenes perioxes
				poweroutageMapper.insertPowerOutageExypPerioxes(po);
			}

			if ((po.getExyphretoumeniDhmEnothtaList() != null) && (po.getExyphretoumeniDhmEnothtaList().size() > 0)) {
				// generate audits for changes in list with exyphretoumenes dhmotikes enotites
				for (ExyphretoumeniDhmEnothta item : po.getExyphretoumeniDhmEnothtaList()) {
					powerOutageAuditList.add(new PowerOutageAudit("ExyphretoumeniDhmotikhEnothta", "ADDED", item.getId().toString(), user, po.getId()));
				}
				// insert related exyphretoymenes perioxes
				poweroutageMapper.insertPowerOutageExypDE(po);
			}

			// persist audits
			if (powerOutageAuditList.size() > 0) {
				poweroutageMapper.insertPowerOutageAudits(powerOutageAuditList);
			}



			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			logger.error("exception in insertPowerOutage", e);
			throw e;
		} finally {
			sqlSession.close();
		}

		return 1;
	}

	// to insert new Power Outage by TALD via DFR app
	public PowerOutage insertTALDPowerOutage(PowerOutage po, String token) throws Exception{
		List<PowerOutageAudit> powerOutageAuditList = new ArrayList<>();
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		String ivrMessage = "";

		try {

			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);

			// insert powerOutage and get id
			poweroutageMapper.insertPowerOutage(po);

			// generate object with differences in power outage audit
			DiffNode diffNode = ObjectDifferBuilder.buildDefault().compare(po, new PowerOutage());

			// generate audits for changes in the power outage object
			if (diffNode.hasChanges()) {
				diffNode.visit(new DiffNode.Visitor()
				{
					@Override
					public void node(final DiffNode node, final Visit visit)
					{
						if (!node.isRootNode() && !node.hasChildren() && Arrays.stream(PowerOutage.auditedFields).anyMatch(node.getPropertyName()::equals)) {
							powerOutageAuditList.add(new PowerOutageAudit(node.getPropertyName(),node.getState().toString(),node.canonicalGet(po).toString(), po.getCreator(), po.getId()));
						}
					}
				});
			}

			if ((po.getLektikoGenikonDiakoponList() != null) && (po.getLektikoGenikonDiakoponList().size() > 0)) {
				// generate audits for changes in list with exyphretoumenes perioxes
				for (LektikoGenikonDiakopon item : po.getLektikoGenikonDiakoponList()) {
					powerOutageAuditList.add(new PowerOutageAudit("LektikoGenikonDiakopon", "ADDED", item.getId().toString(), po.getCreator(), po.getId()));
				}
				// insert related exyphretoymenes perioxes
				poweroutageMapper.insertPowerOutageLektika(po);
			} else if ((po.getKallikratikiDhmotikiEnothtaList() != null) && (po.getKallikratikiDhmotikiEnothtaList().size() > 0)) {
				// generate audits for changes in KallikratikiDhmotikiEnothtaList
				for (KallikratikiDhmotikiEnothta item : po.getKallikratikiDhmotikiEnothtaList()) {
					powerOutageAuditList.add(new PowerOutageAudit("KallikratikiDhmotikiEnothta", "ADDED", item.getKlde(), po.getCreator(), po.getId()));
				}
				// insert related kallikratikes dhmotikes enothtes
				poweroutageMapper.insertPowerOutageKallikratikiDhmotikiEnothtaList(po);
			} else if ((po.getKallikratikosOTAList() != null) && (po.getKallikratikosOTAList().size() > 0)) {
				// generate audits for changes in KallikratikosOTAList
				for (KallikratikosOTA item : po.getKallikratikosOTAList()) {
					powerOutageAuditList.add(new PowerOutageAudit("KallikratikosOTA", "ADDED", item.getKlot(), po.getCreator(), po.getId()));
				}
				// insert related kallikratikoi OTA
				poweroutageMapper.insertPowerOutageKallikratikosOTAList(po);
			} else {
				// generate audits for changes in KallikratikiNomarxiaList
				for (KallikratikiNomarxia item : po.getKallikratikiNomarxiaList()) {
					powerOutageAuditList.add(new PowerOutageAudit("KallikratikiNomarxia", "ADDED", item.getKlno(), po.getCreator(), po.getId()));
				}
				// insert related kallikratikes nomarxies
				poweroutageMapper.insertPowerOutageKallikratikiNomarxiaList(po);
			}

			po.setText(generateIVRmessage(po));

			// insert powerOutageTALD info
			poweroutageMapper.insertTALDPowerOutage(po);

			// persist audits
			if (powerOutageAuditList.size() > 0) {
				poweroutageMapper.insertPowerOutageAudits(powerOutageAuditList);
			}

			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			logger.error("exception in insertTALDPowerOutage", e);
			throw e;
		} finally {
			sqlSession.close();
		}

		return po;
	}

	public Integer addPowerOutageExypPerioxes(PowerOutage po, List<ExyphretoumeniPerioxi> exyphretoumeniPerioxiList) {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		Integer result = null;
		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			result = poweroutageMapper.addPowerOutageExypPerioxes(po, exyphretoumeniPerioxiList);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in addPowerOutageExypPerioxes", e);
			result = -1;
		} finally {
			sqlSession.close();
		}

		return result;
	}

	public Integer addPowerOutageExypDE(PowerOutage po, List<ExyphretoumeniDhmEnothta> exyphretoumeniDhmEnothtaList) {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		Integer result = null;
		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			result = poweroutageMapper.addPowerOutageExypDE(po, exyphretoumeniDhmEnothtaList);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in addPowerOutageExypDE", e);
			result = -1;
		} finally {
			sqlSession.close();
		}

		return result;
	}

	public void addPowerOutageLektika(PowerOutage po, List<LektikoGenikonDiakopon> lektikoGenikonDiakoponList) throws Exception {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			poweroutageMapper.addPowerOutageLektika(po, lektikoGenikonDiakoponList);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in addPowerOutageLektika", e);
			throw new Exception("cannot.add.poweroutagelektika", e);
		} finally {
			sqlSession.close();
		}
	}

	public void addPowerOutageKallikratikesDhmotikesEnothtes(PowerOutage po, List<KallikratikiDhmotikiEnothta> kallikratikiDhmotikiEnothtaList) throws Exception {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			poweroutageMapper.addPowerOutageKallikratikesDhmotikesEnothtes(po, kallikratikiDhmotikiEnothtaList);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in addPowerOutageKallikratikesDhmotikesEnothtes", e);
			throw new Exception("cannot.add.poweroutageklde", e);
		} finally {
			sqlSession.close();
		}
	}

	public void addPowerOutageKallikratikoiOTA(PowerOutage po, List<KallikratikosOTA> kallikratikosOTAList) throws Exception {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			poweroutageMapper.addPowerOutageKallikratikoiOTA(po, kallikratikosOTAList);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in addPowerOutageKallikratikoiOTA", e);
			throw new Exception("cannot.add.poweroutageklot", e);
		} finally {
			sqlSession.close();
		}
	}

	public void addPowerOutageKallikratikesNomarxies(PowerOutage po, List<KallikratikiNomarxia> kallikratikiNomarxiaList) throws Exception {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			poweroutageMapper.addPowerOutageKallikratikesNomarxies(po, kallikratikiNomarxiaList);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in addPowerOutageKallikratikesNomarxies", e);
			throw new Exception("cannot.add.poweroutageklno", e);
		} finally {
			sqlSession.close();
		}
	}

	public Integer deletePowerOutageExypPerioxes(PowerOutage po, List<ExyphretoumeniPerioxi> exyphretoumeniPerioxiList) {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		Integer result = null;
		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			result = poweroutageMapper.deletePowerOutageExypPerioxes(po, exyphretoumeniPerioxiList);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in deletePowerOutageExypPerioxes", e);
			result = -1;
		} finally {
			sqlSession.close();
		}

		return result;
	}

	public Integer deletePowerOutageExypDE(PowerOutage po, List<ExyphretoumeniDhmEnothta> exyphretoumeniDhmEnothtaList) {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		Integer result = null;
		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			result = poweroutageMapper.deletePowerOutageExypDE(po, exyphretoumeniDhmEnothtaList);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in deletePowerOutageExypDE", e);
			result = -1;
		} finally {
			sqlSession.close();
		}

		return result;
	}

	public void deletePowerOutageLektika(PowerOutage po, List<LektikoGenikonDiakopon> lektikoGenikonDiakoponList) throws Exception {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			poweroutageMapper.deletePowerOutageLektika(po, lektikoGenikonDiakoponList);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in deletePowerOutageLektika", e);
			throw new Exception("cannot.delete.poweroutagelektika", e);
		} finally {
			sqlSession.close();
		}
	}

	public void deletePowerOutageKallikratikesDhmotikesEnothtes(PowerOutage po, List<KallikratikiDhmotikiEnothta> kallikratikiDhmotikiEnothtaList) throws Exception {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			poweroutageMapper.deletePowerOutageKallikratikesDhmotikesEnothtes(po, kallikratikiDhmotikiEnothtaList);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in deletePowerOutageKallikratikesDhmotikesEnothtes", e);
			throw new Exception("cannot.delete.poweroutageklde", e);
		} finally {
			sqlSession.close();
		}
	}

	public void deletePowerOutageKallikratikousOTA(PowerOutage po, List<KallikratikosOTA> kallikratikosOTAList) throws Exception {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			poweroutageMapper.deletePowerOutageKallikratikoiOta(po, kallikratikosOTAList);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in deletePowerOutageKallikratikousOTA", e);
			throw new Exception("cannot.delete.poweroutageklot", e);
		} finally {
			sqlSession.close();
		}
	}

	public void deletePowerOutageKallikratikesNomarxies(PowerOutage po, List<KallikratikiNomarxia> kallikratikiNomarxiaList) throws Exception {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			poweroutageMapper.deletePowerOutageKallikratikesNomarxies(po, kallikratikiNomarxiaList);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in deletePowerOutageKallikratikesNomarxies", e);
			throw new Exception("cannot.delete.poweroutageklno", e);
		} finally {
			sqlSession.close();
		}
	}

	// to update an existing Power Outage
	public Integer updatePowerOutage(PowerOutage newPO, String token) throws Exception {


		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		List<PowerOutageAudit> powerOutageAuditList = new ArrayList<>();

		try {

			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

			// retrieve user based on token

			String user = pfrMapper.getUser(token);

			if (user == null) {
				throw new AuthenticationException("no active session found");
			} else {
				newPO.setCreator(user);
			}

			PowerOutage oldPO = this.getPowerOutage(newPO.getId(), token);

			if (oldPO == null)
				throw new NotFoundException("not existing poweroutage");

			// generate object with differences in power outage audit
			DiffNode diffNode = ObjectDifferBuilder.buildDefault().compare(newPO, oldPO);

			// generate audits for changes in the power outage object
			if (diffNode.hasChanges()) {
				diffNode.visit(new DiffNode.Visitor()
				{
					@Override
					public void node(final DiffNode node, final Visit visit)
					{
						if (!node.isRootNode() && !node.hasChildren() && Arrays.stream(PowerOutage.auditedFields).anyMatch(node.getPropertyName()::equals)) {
//							logger.info("property: " + node.getPropertyName() + ", action: " + node.getState().toString() + ", newValue: " + node.canonicalGet(newPO));
							powerOutageAuditList.add(new PowerOutageAudit(node.getPropertyName(),node.getState().toString(),node.canonicalGet(newPO).toString(), user, newPO.getId()));
						}
					}
				});
			}

			List<ExyphretoumeniPerioxi> exyphretoumeniPerioxiListToBeRemoved = null;
			List<ExyphretoumeniPerioxi> exyphretoumeniPerioxiListToBeAdded = null;

			if (newPO.getExyphretoumeniPerioxiList() != null) {
				// identify changes in exyphretoumenes perioxes
				// generate lists with exyphretoumenes perioxes to be added or to be removed
				exyphretoumeniPerioxiListToBeRemoved = oldPO.getExyphretoumeniPerioxiList().stream().map(item -> new ExyphretoumeniPerioxi(item)).collect(Collectors.toList());
				exyphretoumeniPerioxiListToBeAdded = newPO.getExyphretoumeniPerioxiList().stream().map(item -> new ExyphretoumeniPerioxi(item)).collect(Collectors.toList());

				exyphretoumeniPerioxiListToBeRemoved.removeAll(newPO.getExyphretoumeniPerioxiList());
				exyphretoumeniPerioxiListToBeAdded.removeAll(oldPO.getExyphretoumeniPerioxiList());

				// generate audits for changes in list with exyphretoumenes perioxes
				for (ExyphretoumeniPerioxi item : exyphretoumeniPerioxiListToBeRemoved) {
					powerOutageAuditList.add(new PowerOutageAudit("ExyphretoumeniPerioxi", "REMOVED", item.getId().toString(), user, newPO.getId()));
				}

				for (ExyphretoumeniPerioxi item : exyphretoumeniPerioxiListToBeAdded) {
					powerOutageAuditList.add(new PowerOutageAudit("ExyphretoumeniPerioxi", "ADDED", item.getId().toString(), user, newPO.getId()));
				}
			}

			List<ExyphretoumeniDhmEnothta> exyphretoumeniDEListToBeRemoved = null;
			List<ExyphretoumeniDhmEnothta> exyphretoumeniDEListToBeAdded = null;

			if (newPO.getExyphretoumeniDhmEnothtaList() != null) {
				// identify changes in exyphretoumenes dhmotikes enothtes
				// generate lists with exyphretoumenes exyphretoymenes dhmotikes enothtes to be added or to be removed
				exyphretoumeniDEListToBeRemoved = oldPO.getExyphretoumeniDhmEnothtaList().stream().map(item -> new ExyphretoumeniDhmEnothta(item)).collect(Collectors.toList());
				exyphretoumeniDEListToBeAdded = newPO.getExyphretoumeniDhmEnothtaList().stream().map(item -> new ExyphretoumeniDhmEnothta(item)).collect(Collectors.toList());

				exyphretoumeniDEListToBeRemoved.removeAll(newPO.getExyphretoumeniDhmEnothtaList());
				exyphretoumeniDEListToBeAdded.removeAll(oldPO.getExyphretoumeniDhmEnothtaList());

				// generate audits for changes in list with exyphretoumenes perioxes
				for (ExyphretoumeniDhmEnothta item : exyphretoumeniDEListToBeRemoved) {
					powerOutageAuditList.add(new PowerOutageAudit("ExyphretoumeniDhmotikhEnothta", "REMOVED", item.getId().toString(), user, newPO.getId()));
				}

				for (ExyphretoumeniDhmEnothta item : exyphretoumeniDEListToBeAdded) {
					powerOutageAuditList.add(new PowerOutageAudit("ExyphretoumeniDhmotikhEnothta", "ADDED", item.getId().toString(), user, newPO.getId()));
				}
			}

			if (powerOutageAuditList.size() == 0)
				throw new NotFoundException("no changes found");

			if (newPO.getExyphretoumeniPerioxiList() != null) {
				// persist changes in power outage
				if (exyphretoumeniPerioxiListToBeRemoved.size() > 0)
					this.deletePowerOutageExypPerioxes(newPO, exyphretoumeniPerioxiListToBeRemoved);
				if (exyphretoumeniPerioxiListToBeAdded.size() > 0)
					this.addPowerOutageExypPerioxes(newPO, exyphretoumeniPerioxiListToBeAdded);
			}

			if (newPO.getExyphretoumeniDhmEnothtaList() != null) {
				// persist changes in power outage
				if (exyphretoumeniDEListToBeRemoved.size() > 0)
					this.deletePowerOutageExypDE(newPO, exyphretoumeniDEListToBeRemoved);
				if (exyphretoumeniDEListToBeAdded.size() > 0)
					this.addPowerOutageExypDE(newPO, exyphretoumeniDEListToBeAdded);
			}

			poweroutageMapper.updatePowerOutage(newPO);

			// persist audits
			if (powerOutageAuditList.size() > 0) {
				poweroutageMapper.insertPowerOutageAudits(powerOutageAuditList);
			}

			sqlSession.commit();

		} catch (Exception e) {
			sqlSession.rollback();
			logger.error("exception in updatePowerOutage", e);
			throw e;
		} finally {
			sqlSession.close();
		}

		return 1;
	}

	// to update an existing TALD Power Outage
	public PowerOutage updateTALDPowerOutage(PowerOutage oldPO, PowerOutage newPO, String token) throws Exception {

		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		List<PowerOutageAudit> powerOutageAuditList = new ArrayList<>();

		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

			// retrieve user based on token

			String user = pfrMapper.getUser(token);

			if (user == null) {
				throw new AuthenticationException("no active session found");
			} else {
				newPO.setCreator(user);
			}

			// generate object with differences in power outage audit
			DiffNode diffNode = ObjectDifferBuilder.buildDefault().compare(newPO, oldPO);

			if (newPO.getLektikoGenikonDiakoponList() == null) {
				newPO.setLektikoGenikonDiakoponList(new ArrayList<>());
			}
			if (newPO.getKallikratikiDhmotikiEnothtaList() == null) {
				newPO.setKallikratikiDhmotikiEnothtaList(new ArrayList<>());
			}

			if (newPO.getKallikratikosOTAList() == null) {
				newPO.setKallikratikosOTAList(new ArrayList<>());
			}

			if (newPO.getKallikratikiNomarxiaList() == null) {
				newPO.setKallikratikiNomarxiaList(new ArrayList<>());
			}

			// identify changes in lektika genikon diakopon
			// generate lists with lektika genikon diakopon to be added or to be removed
			List<LektikoGenikonDiakopon> lektikoGenikonDiakoponListToBeRemoved = oldPO.getLektikoGenikonDiakoponList().stream().map(item-> new LektikoGenikonDiakopon(item)).collect(Collectors.toList());
			List<LektikoGenikonDiakopon> lektikoGenikonDiakoponListToBeAdded = newPO.getLektikoGenikonDiakoponList().stream().map(item-> new LektikoGenikonDiakopon(item)).collect(Collectors.toList());

			lektikoGenikonDiakoponListToBeRemoved.removeAll(newPO.getLektikoGenikonDiakoponList());
			lektikoGenikonDiakoponListToBeAdded.removeAll(oldPO.getLektikoGenikonDiakoponList());

			// identify changes in kallikratikes dhmotikes enothtes
			// generate lists with kallikratikes dhmotikes enothtes to be added or to be removed
			List<KallikratikiDhmotikiEnothta> kallikratikiDhmotikiEnothtaListToBeRemoved = oldPO.getKallikratikiDhmotikiEnothtaList().stream().map(item-> new KallikratikiDhmotikiEnothta(item)).collect(Collectors.toList());
			List<KallikratikiDhmotikiEnothta> kallikratikiDhmotikiEnothtaListToBeAdded = newPO.getKallikratikiDhmotikiEnothtaList().stream().map(item-> new KallikratikiDhmotikiEnothta(item)).collect(Collectors.toList());

			kallikratikiDhmotikiEnothtaListToBeRemoved.removeAll(newPO.getKallikratikiDhmotikiEnothtaList());
			kallikratikiDhmotikiEnothtaListToBeAdded.removeAll(oldPO.getKallikratikiDhmotikiEnothtaList());

			// identify changes in kallikratikous ota
			// generate lists with kallikratikous ota to be added or to be removed
			List<KallikratikosOTA> kallikratikosOTAListToBeRemoved = oldPO.getKallikratikosOTAList().stream().map(item-> new KallikratikosOTA(item)).collect(Collectors.toList());
			List<KallikratikosOTA> kallikratikosOTAListToBeAdded = newPO.getKallikratikosOTAList().stream().map(item-> new KallikratikosOTA(item)).collect(Collectors.toList());

			kallikratikosOTAListToBeRemoved.removeAll(newPO.getKallikratikosOTAList());
			kallikratikosOTAListToBeAdded.removeAll(oldPO.getKallikratikosOTAList());

			// identify changes in kallikratikes nomarxies
			// generate lists with kallikratikes nomarxies to be added or to be removed
			List<KallikratikiNomarxia> kallikratikiNomarxiaListToBeRemoved = oldPO.getKallikratikiNomarxiaList().stream().map(item-> new KallikratikiNomarxia(item)).collect(Collectors.toList());
			List<KallikratikiNomarxia> kallikratikiNomarxiaListToBeAdded = newPO.getKallikratikiNomarxiaList().stream().map(item-> new KallikratikiNomarxia(item)).collect(Collectors.toList());

			kallikratikiNomarxiaListToBeRemoved.removeAll(newPO.getKallikratikiNomarxiaList());
			kallikratikiNomarxiaListToBeAdded.removeAll(oldPO.getKallikratikiNomarxiaList());

			// generate audits for changes in the power outage object
			if (diffNode.hasChanges()) {
				diffNode.visit(new DiffNode.Visitor()
				{
					@Override
					public void node(final DiffNode node, final Visit visit)
					{
//						logger.info(node.getPath().toString() + "," + node.getPropertyName());
						if (!node.isRootNode() && !node.hasChildren() && Arrays.stream(PowerOutage.auditedFields).anyMatch(node.getPath().toString()::equals)) {
//							logger.info("property: " + node.getPath().toString() + ", action: " + node.getState().toString() + ", newValue: " + node.canonicalGet(newPO));
							powerOutageAuditList.add(new PowerOutageAudit(node.getPath().toString(),node.getState().toString(),node.canonicalGet(newPO).toString(), user, newPO.getId()));
						}
					}
				});
			}

			// generate audits for changes in list with exyphretoumenes perioxes
			for (LektikoGenikonDiakopon item : lektikoGenikonDiakoponListToBeRemoved){
				powerOutageAuditList.add(new PowerOutageAudit("LektikoGenikonDiakopon","REMOVED",item.getId().toString(), user, newPO.getId()));
			}

			for (LektikoGenikonDiakopon item : lektikoGenikonDiakoponListToBeAdded){
				powerOutageAuditList.add(new PowerOutageAudit("LektikoGenikonDiakopon","ADDED",item.getId().toString(), user, newPO.getId()));
			}

			// generate audits for changes in list with kallikratikes dhmotikes enothtes
			for (KallikratikiDhmotikiEnothta item : kallikratikiDhmotikiEnothtaListToBeRemoved){
				powerOutageAuditList.add(new PowerOutageAudit("KallikratikiDhmotikiEnothta","REMOVED",item.getKlde(), user, newPO.getId()));
			}

			for (KallikratikiDhmotikiEnothta item : kallikratikiDhmotikiEnothtaListToBeAdded){
				powerOutageAuditList.add(new PowerOutageAudit("KallikratikiDhmotikiEnothta","ADDED",item.getKlde(), user, newPO.getId()));
			}

			// generate audits for changes in list with kallikratikous ota
			for (KallikratikosOTA item : kallikratikosOTAListToBeRemoved){
				powerOutageAuditList.add(new PowerOutageAudit("KallikratikosOTA","REMOVED",item.getKlot(), user, newPO.getId()));
			}

			for (KallikratikosOTA item : kallikratikosOTAListToBeAdded){
				powerOutageAuditList.add(new PowerOutageAudit("KallikratikosOTA","ADDED",item.getKlot(), user, newPO.getId()));
			}

			// generate audits for changes in list with kallikratikes nomarxies
			for (KallikratikiNomarxia item : kallikratikiNomarxiaListToBeRemoved){
				powerOutageAuditList.add(new PowerOutageAudit("KallikratikiNomarxia","REMOVED",item.getKlno(), user, newPO.getId()));
			}

			for (KallikratikiNomarxia item : kallikratikiNomarxiaListToBeAdded){
				powerOutageAuditList.add(new PowerOutageAudit("KallikratikiNomarxia","ADDED",item.getKlno(), user, newPO.getId()));
			}

			if (powerOutageAuditList.size() == 0)
				throw new NotFoundException("no.changes.found");

			// persist changes in lektikoGenikonDiakoponList
			if (lektikoGenikonDiakoponListToBeRemoved.size() > 0)
				this.deletePowerOutageLektika(newPO, lektikoGenikonDiakoponListToBeRemoved);
			if (lektikoGenikonDiakoponListToBeAdded.size() > 0)
				this.addPowerOutageLektika(newPO, lektikoGenikonDiakoponListToBeAdded);

			// persist changes in kallikratikiDhmotikiEnothtaList
			if (kallikratikiDhmotikiEnothtaListToBeRemoved.size() > 0)
				this.deletePowerOutageKallikratikesDhmotikesEnothtes(newPO, kallikratikiDhmotikiEnothtaListToBeRemoved);
			if (kallikratikiDhmotikiEnothtaListToBeAdded.size() > 0)
				this.addPowerOutageKallikratikesDhmotikesEnothtes(newPO, kallikratikiDhmotikiEnothtaListToBeAdded);

			// persist changes in kallikratikosOTAList
			if (kallikratikosOTAListToBeRemoved.size() > 0)
				this.deletePowerOutageKallikratikousOTA(newPO, kallikratikosOTAListToBeRemoved);
			if (kallikratikosOTAListToBeAdded.size() > 0)
				this.addPowerOutageKallikratikoiOTA(newPO, kallikratikosOTAListToBeAdded);

			// persist changes in power outage
			if (kallikratikiNomarxiaListToBeRemoved.size() > 0)
				this.deletePowerOutageKallikratikesNomarxies(newPO, kallikratikiNomarxiaListToBeRemoved);
			if (kallikratikiNomarxiaListToBeAdded.size() > 0)
				this.addPowerOutageKallikratikesNomarxies(newPO, kallikratikiNomarxiaListToBeAdded);

			newPO.setText(generateIVRmessage(newPO));
			poweroutageMapper.updatePowerOutage(newPO);
			poweroutageMapper.updateTALDPowerOutage(newPO);

			// persist audits
			if (powerOutageAuditList.size() > 0) {
				poweroutageMapper.insertPowerOutageAudits(powerOutageAuditList);
			}

			sqlSession.commit();

		} catch (Exception e) {
			sqlSession.rollback();
			logger.error("exception in updateTALDPowerOutage", e);
			throw e;
		} finally {
			sqlSession.close();
		}

		return newPO;
	}

	// For power outages search process
	public PowerOutageDTOsLimitedList getPowerOutagesByCriteriaByVPD(PowerOutageSearchParameters searchParameters, String token) {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		try {
			DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
			datasecurityMapper.setAppUserToken(token);

			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			List<PowerOutage> powerOutageDTOs = poweroutageMapper.getPowerOutageDTOsByCriteriaByVPD(searchParameters.getStart_date(),
					searchParameters.getEnd_date(),searchParameters.getGrammiMT(),
					searchParameters.getIs_active(),searchParameters.getIs_scheduled(),searchParameters.getCause(),
					PowerOutageDTOsLimitedList.OUTAGES_LIST_MAX_SIZE);

			PowerOutageDTOsLimitedList powerOutageDTOsLimitedList = new PowerOutageDTOsLimitedList();
			powerOutageDTOsLimitedList.setPartial_response(
					powerOutageDTOs.size() > PowerOutageDTOsLimitedList.OUTAGES_LIST_MAX_SIZE ? true : false);

			if (powerOutageDTOs.size() > PowerOutageDTOsLimitedList.OUTAGES_LIST_MAX_SIZE) {
				powerOutageDTOs.remove(PowerOutageDTOsLimitedList.OUTAGES_LIST_MAX_SIZE);
			}
			powerOutageDTOsLimitedList.setPoweroutages(powerOutageDTOs);
			
			return powerOutageDTOsLimitedList;
		} catch (Exception e) {
			logger.error("exception in getPowerOutageAnnouncementsByCriteriaByVPD", e);
		} finally {
			sqlSession.close();
		}

		return null;
	}

	// For power outages
	public List<PowerOutageTALDDTO> getCurrentPowerOutagesLimitedInfo(String token) {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		try {
			DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
			datasecurityMapper.setAppUserToken(token);

			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			return poweroutageMapper.getCurrentPowerOutagesLimitedInfo();

		} catch (Exception e) {
			logger.error("exception in getIVRPowerOutages", e);
		} finally {
			sqlSession.close();
		}

		return null;
	}

	public List<KallikratikiNomarxia> getKallikrNomarxiakesEnothtesOfOldNomarxia(Long oldno) {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			return poweroutageMapper.getKallikratikesNomarxiesOfOldNomarxia(oldno);
		} catch (Exception e) {
			logger.error("exception in getKallikrNomarxiakesEnothtesOfOldNomarxia", e);
			throw e;
		} finally {
			sqlSession.close();
		}
	}

	// For power outages
	public List<KallikratikiNomarxia> getKallikrNomarxiakesEnothtesOfOldNomarxia(KallikratikiNomarxia kallikratikiNomarxia) {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			return poweroutageMapper.getKallikratikesNomarxiakesEnothtesOfOldNomarxia(kallikratikiNomarxia);
		} catch (Exception e) {
			logger.error("exception in getKallikrNomarxiakesEnothtesOfOldNomarxia", e);
			throw e;
		} finally {
			sqlSession.close();
		}
	}

	// For power outages
	public List<PowerOutageIVRDTO> getPowerOutagesperNE(List<KallikratikiNomarxia> kallikratikiNomarxiaList) {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			return poweroutageMapper.getPowerOutagesperNE(kallikratikiNomarxiaList);
		} catch (Exception e) {
			logger.error("exception in getPowerOutagesperNE", e);
			throw e;
		} finally {
			sqlSession.close();
		}
	}

	// For power outages
	public List<PowerOutageIVRDTO> getPowerOutagesperOTA(KallikratikosOTA kallikratikosOTA) {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			return poweroutageMapper.getPowerOutagesperOTA(kallikratikosOTA);
		} catch (Exception e) {
			logger.error("exception in getPowerOutagesperOTA", e);
			throw e;
		} finally {
			sqlSession.close();
		}
	}

	// to insert new Exyphretoymenh Perioxh
	public Integer insertExyphretPerioxesAnaGrammh(ExyphretoumeniPerioxi ep) throws Exception {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);

			ep.setStatus(ExyphretoumeniPerioxi.ExyphretoymeniPerioxiStatus.INSERT);
			// insert Exyphretoymenh Perioxh
			poweroutageMapper.insertExyphretPerioxesAnaGrammh(ep);

			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			logger.error("exception in insertExyphretPerioxesAnaGrammh", e);
			throw e;
		} finally {
			sqlSession.close();
		}

		return 1;
	}

	// to insert new Exyphretoymenh Dhmotikh Enothta
	public Integer insertExyphretDhmotikhEnotitaAnaGrammh(ExyphretoumeniDhmEnothta ep) throws Exception {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);

			poweroutageMapper.insertExyphretDEAnaGrammh(ep);

			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			logger.error("exception in insertExyphretDhmotikhEnotitaAnaGrammh", e);
			throw e;
		} finally {
			sqlSession.close();
		}

		return 1;
	}

	// to delete Exyphretoymenes Perioxes apo Grammh
	public Integer deleteExyphretPerioxesAnaGrammh(ExyphretoumeniPerioxi ep, String token) throws Exception {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
			datasecurityMapper.setAppUserToken(token);

			// delete Exyphretoymenes Perioxes
			poweroutageMapper.updateExyphretPerioxesAnaGrammhStatus(ep, ExyphretoumeniPerioxi.ExyphretoymeniPerioxiStatus.DELETED);

			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			logger.error("exception in deleteExyphretPerioxesAnaGrammh", e);
			throw e;
		} finally {
			sqlSession.close();
		}

		return 1;
	}

	// to delete Exyphretoymenh Dhmotikh Enothta apo Grammh
	public Integer deleteExyphretDEAnaGrammh(ExyphretoumeniDhmEnothta exyphretoumeniDhmEnothta, String token) throws Exception {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
			datasecurityMapper.setAppUserToken(token);

			// delete Exyphretoymenes Dhmotikes Enothtes
			poweroutageMapper.deleteExyphretDEAnaGrammh(exyphretoumeniDhmEnothta);

			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			logger.error("exception in deleteExyphretPerioxesAnaGrammh", e);
			throw e;
		} finally {
			sqlSession.close();
		}

		return 1;
	}

	// to update status of Tmhma Grammhs
	public Integer updateTmhmaGrammhsStatus(List<ExyphretoumeniPerioxi> ep, ExyphretoumeniPerioxi.ExyphretoymeniPerioxiStatus newStatus, String token) throws Exception {
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

		try {

			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);
			DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
			datasecurityMapper.setAppUserToken(token);

			// update status of tmhma grammhs
			poweroutageMapper.updateListExyphretPerioxesAnaGrammhStatus(ep, newStatus);

			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			logger.error("exception in updateTmhmaGrammhsStatus", e);
			throw e;
		} finally {
			sqlSession.close();
		}

		return 1;
	}

	// to update existing Exyphret Perioxh Se Grammh
	public Integer updateExyphretPerioxesAnaGrammh(ExyphretoumeniPerioxi ep) throws Exception {

		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

		try {

			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);

			// update Exyphretoymenes Perioxes
			poweroutageMapper.updateExyphretPerioxesAnaGrammh(ep);

			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			logger.error("exception in updateExyphretPerioxesAnaGrammh", e);
			throw e;
		} finally {
			sqlSession.close();
		}

		return 1;
	}

	// to update existing Exyphret Dhmotikh Enothta Se Grammh MT
	public Integer updateExyphretDEAnaGrammh(ExyphretoumeniDhmEnothta exyphretoumeniDhmEnothta) throws Exception {

		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

		try {

			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);

			// update Exyphretoymenh DHmotikh Enothta
			poweroutageMapper.updateExyphretDEAnaGrammh(exyphretoumeniDhmEnothta);

			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			logger.error("exception in updateExyphretDEAnaGrammh", e);
			throw e;
		} finally {
			sqlSession.close();
		}

		return 1;
	}

	private String generateIVRmessage(PowerOutage po) {
		String ivrMessage = "";

		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

		try {
			POWEROUTAGEMapper poweroutageMapper = sqlSession.getMapper(POWEROUTAGEMapper.class);

			if ((po.getLektikoGenikonDiakoponList() != null) && (po.getLektikoGenikonDiakoponList().size() > 0)) {
				ivrMessage += (po.getLektikoGenikonDiakoponList().size() > 1 ? "Διακοπή στις περιοχές " : "Διακοπή στην περιοχή ");
				for (LektikoGenikonDiakopon item : po.getLektikoGenikonDiakoponList()) {
					ivrMessage += poweroutageMapper.getLektikoGenikonDiakopon(item.getId()).getText() + ", ";
				}
			} else if ((po.getKallikratikiDhmotikiEnothtaList() != null) && (po.getKallikratikiDhmotikiEnothtaList().size() > 0)) {
				ivrMessage += (po.getKallikratikiDhmotikiEnothtaList().size() > 1 ? "Διακοπή στις δημοτικές ενότητες " : "Διακοπή στην δημοτική ενότητα ");
				for (KallikratikiDhmotikiEnothta item : po.getKallikratikiDhmotikiEnothtaList()) {
					ivrMessage += poweroutageMapper.getKallikratikiDhmotikiEnotita(item.getKlde(), LocalizationUTIL.GREEK).getPeri() + ", ";
				}
			} else if ((po.getKallikratikosOTAList() != null) && (po.getKallikratikosOTAList().size() > 0)) {
				ivrMessage += (po.getKallikratikosOTAList().size() > 1 ? "Διακοπή στους δήμους: " : "Διακοπή στον δήμο: ");
				for (KallikratikosOTA item : po.getKallikratikosOTAList()) {
					ivrMessage += poweroutageMapper.getKallikratikosOTA(item.getKlot()).getPeri() + ", ";
				}
			} else if ((po.getKallikratikiNomarxiaList() != null) && (po.getKallikratikiNomarxiaList().size() > 0)) {
				ivrMessage += (po.getKallikratikiNomarxiaList().size() > 1 ? "Διακοπή στους νομούς " : "Διακοπή στον νομό ");
				for (KallikratikiNomarxia item : po.getKallikratikiNomarxiaList()) {
					ivrMessage += poweroutageMapper.getKallikratikiNomarxia(item.getKlno()).getPeri() + ", ";
				}
			}

			//ivrMessage = ivrMessage.substring(0, ivrMessage.length()-2);
//			SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MM/yyyy");
//			SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm");
			ivrMessage += ".";//" με εκτίμηση χρόνου αποκατάστασης μέχρι: "; //+ dateFmt.format(po.getEnd_date_announced()) + ", και ώρα: " + timeFmt.format(po.getEnd_date_announced());

			sqlSession.commit();
		} catch (Exception e) {
			logger.error("exception in generateIVRmessage", e);
			throw e;
		} finally {
			sqlSession.close();
		}

		return ivrMessage;
	}
}
