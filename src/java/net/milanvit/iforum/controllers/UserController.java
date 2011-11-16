/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.milanvit.iforum.controllers;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
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
import net.milanvit.iforum.controllers.exceptions.PreexistingEntityException;
import net.milanvit.iforum.controllers.exceptions.RollbackFailureException;
import net.milanvit.iforum.models.User;

/**
 *
 * @author Milan
 */
public class UserController implements Serializable {
	@Resource
	private UserTransaction userTransaction = null;
	
	@PersistenceUnit (unitName = "iForumPersistenceUnit")
	private EntityManagerFactory entityManagerFactory = null;

	public UserController (UserTransaction userTransaction, EntityManagerFactory entityManagerFactory) {
		this.userTransaction = userTransaction;
		this.entityManagerFactory = entityManagerFactory;
	}

	public EntityManager getEntityManager () {
		if (entityManagerFactory == null) {
			entityManagerFactory = Persistence.createEntityManagerFactory ("iForumPersistenceUnit");
		}
		
		return (entityManagerFactory.createEntityManager ());
	}

	public void create (User user) throws PreexistingEntityException, RollbackFailureException, Exception {
		Context context = new InitialContext ();
		EntityManager entityManager = null;
		
		userTransaction = (UserTransaction) context.lookup ("java:comp/UserTransaction");

		try {
			userTransaction.begin ();

			entityManager = getEntityManager ();
			entityManager.persist (user);

			userTransaction.commit ();
		} catch (Exception e) {
			try {
				userTransaction.rollback ();
			} catch (Exception ex) {
				throw (new RollbackFailureException ("An error occurred attempting to roll back the transaction.", ex));
			}

			if (findUser (user.getUsername ()) != null) {
				throw (new PreexistingEntityException ("User " + user + " already exists.", e));
			}

			throw (e);
		} finally {
			if (entityManager != null) {
				entityManager.close ();
			}
		}
	}

	public void edit (User user) throws NonexistentEntityException, RollbackFailureException, Exception {
		EntityManager entityManager = null;

		try {
			userTransaction.begin ();

			entityManager = getEntityManager ();
			user = entityManager.merge (user);

			userTransaction.commit ();
		} catch (Exception e) {
			try {
				userTransaction.rollback ();
			} catch (Exception ex) {
				throw (new RollbackFailureException ("An error occurred attempting to roll back the transaction.", ex));
			}

			String message = e.getLocalizedMessage ();
			
			if ((message == null) || (message.length () == 0)) {
				String id = user.getUsername ();

				if (findUser (id) == null) {
					throw (new NonexistentEntityException ("The user with id " + id + " no longer exists."));
				}
			}

			throw (e);
		} finally {
			if (entityManager != null) {
				entityManager.close ();
			}
		}
	}

	public void destroy (String id) throws NonexistentEntityException, RollbackFailureException, Exception {
		EntityManager entityManager = null;

		try {
			userTransaction.begin ();

			entityManager = getEntityManager ();
			User user;

			try {
				user = entityManager.getReference (User.class, id);
				user.getUsername ();
			} catch (EntityNotFoundException enfe) {
				throw (new NonexistentEntityException ("The user with id " + id + " no longer exists.", enfe));
			}

			entityManager.remove (user);
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

	public List<User> findUserEntities () {
		return (findUserEntities (true, -1, -1));
	}

	public List<User> findUserEntities (int maxResults, int firstResult) {
		return (findUserEntities (false, maxResults, firstResult));
	}

	private List<User> findUserEntities (boolean all, int maxResults, int firstResult) {
		EntityManager entityManager = getEntityManager ();

		try {
			CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder ().createQuery ();

			criteriaQuery.select (criteriaQuery.from (User.class));

			Query query = entityManager.createQuery (criteriaQuery);

			if (!all) {
				query.setMaxResults (maxResults);
				query.setFirstResult (firstResult);
			}

			return (query.getResultList ());
		} finally {
			entityManager.close ();
		}
	}

	public User findUser (String id) {
		EntityManager entityManager = getEntityManager ();

		try {
			return (entityManager.find (User.class, id));
		} finally {
			entityManager.close ();
		}
	}

	public int getUserCount () {
		EntityManager entityManager = getEntityManager ();

		try {
			CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder ().createQuery ();
			Root<User> root = criteriaQuery.from (User.class);

			criteriaQuery.select (entityManager.getCriteriaBuilder ().count (root));

			Query query = entityManager.createQuery (criteriaQuery);
			return ((Long) query.getSingleResult ()).intValue ();
		} finally {
			entityManager.close ();
		}
	}
}
