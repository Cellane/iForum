/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.milanvit.iforum.controllers;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import net.milanvit.iforum.controllers.exceptions.NonexistentEntityException;
import net.milanvit.iforum.controllers.exceptions.RollbackFailureException;
import net.milanvit.iforum.models.Post;
import net.milanvit.iforum.models.User;
import net.milanvit.iforum.models.Thread;

/**
 *
 * @author Milan
 */
public class PostController implements Serializable {
	@Resource
	private UserTransaction userTransaction = null;
	
	@PersistenceUnit (unitName = "iForumPersistenceUnit")
	private EntityManagerFactory entityManagerFactory = null;

	public PostController (UserTransaction userTransaction, EntityManagerFactory entityManagerFactory) {
		this.userTransaction = userTransaction;
		this.entityManagerFactory = entityManagerFactory;
	}

	public EntityManager getEntityManager () {
		if (entityManagerFactory == null) {
			entityManagerFactory = Persistence.createEntityManagerFactory ("iForumPersistenceUnit");
		}
		
		return (entityManagerFactory.createEntityManager ());
	}

	public void create (Post post) throws RollbackFailureException, Exception {
		EntityManager entityManager = null;
		
		try {
			userTransaction.begin ();
			entityManager = getEntityManager ();
			
			User author = post.getAuthor ();
			Thread thread = post.getThread ();
			
			if (author != null) {
				author = entityManager.getReference (author.getClass (), author.getUsername ());
				post.setAuthor (author);
			}
			
			if (thread != null) {
				thread = entityManager.getReference (thread.getClass (), thread.getId ());
				post.setThread (thread);
			}
			
			entityManager.persist (post);
			
			if (author != null) {
				author.getPostCollection ().add (post);
				author = entityManager.merge (author);
			}
			
			if (thread != null) {
				thread.getPostCollection ().add (post);
				thread = entityManager.merge (thread);
			}
			
			userTransaction.commit ();
		} catch (Exception e) {
			try {
				userTransaction.rollback ();
			} catch (Exception ex) {
				throw (new RollbackFailureException ("An error occurred attempting to roll back the transaction.", ex));
			}
			
			throw (e);
		} finally {
			if (entityManager != null) {
				entityManager.close ();
			}
		}
	}

	public void edit (Post post) throws NonexistentEntityException, RollbackFailureException, Exception {
		EntityManager entityManager = null;
		
		try {
			userTransaction.begin ();
			entityManager = getEntityManager ();
			
			Post persistentPost = entityManager.find (Post.class, post.getId ());
			User authorOld = persistentPost.getAuthor ();
			User authorNew = post.getAuthor ();
			Thread threadOld = persistentPost.getThread ();
			Thread threadNew = post.getThread ();
			
			if (authorNew != null) {
				authorNew = entityManager.getReference (authorNew.getClass (), authorNew.getUsername ());
				post.setAuthor (authorNew);
			}
			
			if (threadNew != null) {
				threadNew = entityManager.getReference (threadNew.getClass (), threadNew.getId ());
				post.setThread (threadNew);
			}
			
			post = entityManager.merge (post);
			
			if ((authorOld != null) && (!authorOld.equals (authorNew))) {
				authorOld.getPostCollection ().remove (post);
				authorOld = entityManager.merge (authorOld);
			}
			
			if ((authorNew != null) && (!authorNew.equals (authorOld))) {
				authorNew.getPostCollection ().add (post);
				authorNew = entityManager.merge (authorNew);
			}
			
			if ((threadOld != null) && (!threadOld.equals (threadNew))) {
				threadOld.getPostCollection ().remove (post);
				threadOld = entityManager.merge (threadOld);
			}
			
			if ((threadNew != null) && (!threadNew.equals (threadOld))) {
				threadNew.getPostCollection ().add (post);
				threadNew = entityManager.merge (threadNew);
			}
			
			userTransaction.commit ();
		} catch (Exception e) {
			try {
				userTransaction.rollback ();
			} catch (Exception ex) {
				throw (new RollbackFailureException ("An error occurred attempting to roll back the transaction.", ex));
			}
			
			String message = e.getLocalizedMessage ();
			
			if ((message == null) || (message.length () == 0)) {
				Integer id = post.getId ();
				
				if (findPost (id) == null) {
					throw (new NonexistentEntityException ("The post with id " + id + " no longer exists."));
				}
			}
			throw e;
		} finally {
			if (entityManager != null) {
				entityManager.close ();
			}
		}
	}

	public void destroy (Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
		EntityManager entityManager = null;
		
		try {
			userTransaction.begin ();
			entityManager = getEntityManager ();
			
			Post post;
			
			try {
				post = entityManager.getReference (Post.class, id);
				post.getId ();
			} catch (EntityNotFoundException enfe) {
				throw (new NonexistentEntityException ("The post with id " + id + " no longer exists.", enfe));
			}
			
			User author = post.getAuthor ();
			Thread thread = post.getThread ();
			
			if (author != null) {
				author.getPostCollection ().remove (post);
				author = entityManager.merge (author);
			}
			
			if (thread != null) {
				thread.getPostCollection ().remove (post);
				thread = entityManager.merge (thread);
			}
			
			entityManager.remove (post);
			userTransaction.commit ();
		} catch (Exception e) {
			try {
				userTransaction.rollback ();
			} catch (Exception ex) {
				throw new RollbackFailureException ("An error occurred attempting to roll back the transaction.", ex);
			}
			
			throw (e);
		} finally {
			if (entityManager != null) {
				entityManager.close ();
			}
		}
	}

	public List<Post> findPostEntities () {
		return (findPostEntities (true, -1, -1));
	}

	public List<Post> findPostEntities (int maxResults, int firstResult) {
		return (findPostEntities (false, maxResults, firstResult));
	}

	private List<Post> findPostEntities (boolean all, int maxResults, int firstResult) {
		EntityManager entityManager = getEntityManager ();
		
		try {
			CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder ().createQuery ();
			Query query = null;
			
			criteriaQuery.select (criteriaQuery.from (Post.class));
			query = entityManager.createQuery (criteriaQuery);
			
			if (!all) {
				query.setMaxResults (maxResults);
				query.setFirstResult (firstResult);
			}
			
			return (query.getResultList ());
		} finally {
			entityManager.close ();
		}
	}

	public Post findPost (Integer id) {
		EntityManager entityManager = getEntityManager ();
		
		try {
			return (entityManager.find (Post.class, id));
		} finally {
			entityManager.close ();
		}
	}

	public int getPostCount () {
		EntityManager entityManager = getEntityManager ();
		
		try {
			CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder ().createQuery ();
			Root<Post> root = criteriaQuery.from (Post.class);
			Query query = null;
			
			criteriaQuery.select (entityManager.getCriteriaBuilder ().count (root));
			query = entityManager.createQuery (criteriaQuery);
			
			return (((Long) query.getSingleResult ()).intValue ());
		} finally {
			entityManager.close ();
		}
	}
	
	@PreDestroy
	public void destruct () {
		entityManagerFactory.close ();
	}
}
