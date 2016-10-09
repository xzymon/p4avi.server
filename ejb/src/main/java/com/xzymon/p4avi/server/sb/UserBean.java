package com.xzymon.p4avi.server.sb;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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
import com.xzymon.p4avi.server.utils.Resources;

/**
 * Session Bean implementation class UserBean
 */
@WebService
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.BEAN)
public class UserBean implements UserRemote, UserLocal {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserBean.class);

	/**
	 * Default constructor.
	 */
	public UserBean() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<User> getUsers() {

		EntityManager em = getEntityManager();
		TypedQuery<User> usersQuery = em.createQuery("from User", User.class);
		List<User> users = usersQuery.getResultList();

		LOGGER.info("users.size = " + users.size());
		for(User user: users){
			LOGGER.info(user.toString());
		}
		
		return users;
	}

	@Override
	public String addUser(String name) {
		EntityManager em = getEntityManager();
		User user = new User();
		user.setName(name);
		try {
			//utx.begin();
			//em.joinTransaction();
			em.getTransaction().begin();
			LOGGER.info("Formalnie transakcja jest rozpoczęta - czy na pewno? :" + em.getTransaction().isActive());
			em.persist(user);
			em.getTransaction().commit();
			LOGGER.info("Formalnie transakcja jest zakończona - czy na pewno? :" + em.getTransaction().isActive());
			//utx.commit();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		// em.getTransaction().commit();
		return "User probably persisted";
	}

	public void showEMFPropertiesMap() {
		Map<String, Object> emfPropertiesMap = Resources.getEntityManagerFactoryProdInstance().getProperties();
		// Map<String, Object> emfPropertiesMap =
		// getEntityManagerFactory().getProperties();
		String[] array = new String[emfPropertiesMap.size()];
		List<String> sortedMapKeys = Arrays.asList(emfPropertiesMap.keySet().toArray(array));
		Collections.sort(sortedMapKeys);
		LOGGER.info("About to get properties for User EMF");
		Iterator<String> sortedKeysIterator = sortedMapKeys.iterator();
		String key, value;
		while(sortedKeysIterator.hasNext()){
			key = sortedKeysIterator.next();
			value = (String) emfPropertiesMap.get(key);
			LOGGER.info(key + "=" + value);
		}
		LOGGER.info("After getting properties for User EMF");
	}

	private EntityManager getEntityManager() {
		EntityManagerFactory emf = Resources.getEntityManagerFactoryProdInstance();
		if (emf == null) {
			throw new RuntimeException("emf is null!");
		}
		LOGGER.info("About to acquire new EntityManager instance");
		EntityManager em = emf.createEntityManager();
		LOGGER.info("new EntityManager instance acquired SUCCESSFULLY!");
		return em;
	}

}
