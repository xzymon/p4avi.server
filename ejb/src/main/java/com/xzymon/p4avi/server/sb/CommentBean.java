package com.xzymon.p4avi.server.sb;

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

import com.xzymon.p4avi.server.model.Comment;

/**
 * Session Bean implementation class CommentBean
 */
@WebService
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.BEAN)
public class CommentBean implements CommentRemote, CommentLocal {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommentBean.class);
	
	@PersistenceUnit(unitName="aviextr")
	private EntityManagerFactory emf;
	
	@Resource
	private UserTransaction utx;
	
    /**
     * Default constructor. 
     */
    public CommentBean() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public List<Comment> getComments() {
		showEMFPropertiesMap();
		
		EntityManager em = getEntityManager();
		TypedQuery<Comment> commentsQuery = em.createQuery("from Comment", Comment.class);
		List<Comment> comments = commentsQuery.getResultList();
		
		return comments;
	}

	@Override
	public String addComment(String message) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		showEMFPropertiesMap();
		EntityManager em = getEntityManager();
		Comment comment = new Comment();
		comment.setMessage(message);
		utx.begin();
		em.joinTransaction();
		//em.getTransaction().begin();
		em.persist(comment);
		utx.commit();
		//em.getTransaction().commit();
		return "Comment probably persisted";
	}

	private void showEMFPropertiesMap(){
		Map<String, Object> emfPropertiesMap = emf.getProperties();
		LOGGER.info("About to get properties for Comment EMF");
		for(Map.Entry<String, Object> property: emfPropertiesMap.entrySet()){
			LOGGER.info(property.getKey() + "=" + property.getValue());
		}
		LOGGER.info("After getting properties for Comment EMF");
	}
	
	private EntityManager getEntityManager(){
		EntityManager em = emf.createEntityManager();
		return em;
	}
}
