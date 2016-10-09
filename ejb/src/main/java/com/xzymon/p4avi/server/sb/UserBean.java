package com.xzymon.p4avi.server.sb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzymon.p4avi.server.model.User;

/**
 * Session Bean implementation class UserBean
 */
@WebService
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.BEAN)
public class UserBean implements UserRemote, UserLocal {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserBean.class);

	private static final String EMF_NAME = "aviprod";

	@PersistenceUnit(unitName = EMF_NAME)
	private EntityManagerFactory emf;

	@Resource
	private UserTransaction utx;

	/**
	 * Default constructor.
	 */
	public UserBean() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<User> getUsers() {
		showEMFPropertiesMap();

		EntityManager em = getEntityManager();
		TypedQuery<User> usersQuery = em.createQuery("from User", User.class);
		List<User> users = usersQuery.getResultList();

		return users;
	}

	@Override
	public String addUser(String name) {
		showEMFPropertiesMap();
		EntityManager em = getEntityManager();
		User user = new User();
		user.setName(name);
		try {
			utx.begin();
			em.joinTransaction();
			// em.getTransaction().begin();
			em.persist(user);
			utx.commit();
		} catch (NotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		// em.getTransaction().commit();
		return "User probably persisted";
	}

	private void showEMFPropertiesMap() {
		Map<String, Object> emfPropertiesMap = emf.getProperties();
		// Map<String, Object> emfPropertiesMap =
		// getEntityManagerFactory().getProperties();
		LOGGER.info("About to get properties for User EMF");
		for (Map.Entry<String, Object> property : emfPropertiesMap.entrySet()) {
			LOGGER.info(property.getKey() + "=" + property.getValue());
		}
		LOGGER.info("After getting properties for User EMF");
	}

	private EntityManager getEntityManager() {
		EntityManagerFactory emf = getEntityManagerFactory();
		if (emf == null) {
			throw new RuntimeException("emf is null!");
		}
		LOGGER.info("About to acquire new EntityManager instance");
		EntityManager em = emf.createEntityManager();
		LOGGER.info("new EntityManager instance acquired SUCCESSFULLY!");
		return em;
	}

	private EntityManagerFactory getEntityManagerFactory() {
		Map<String, Object> configOverrides = new HashMap<String, Object>();

		return Persistence.createEntityManagerFactory(EMF_NAME, configOverrides);
	}

}
