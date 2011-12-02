/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.milanvit.iforum.controllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import net.milanvit.iforum.controllers.exceptions.IllegalOrphanException;
import net.milanvit.iforum.controllers.exceptions.NonexistentEntityException;
import net.milanvit.iforum.controllers.exceptions.PreexistingEntityException;
import net.milanvit.iforum.controllers.exceptions.RollbackFailureException;
import net.milanvit.iforum.models.Post;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import net.milanvit.iforum.models.Thread;
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

		if (user.getPostCollection () == null) {
			user.setPostCollection (new ArrayList<Post> ());
		}

		if (user.getThreadCollection () == null) {
			user.setThreadCollection (new ArrayList<Thread> ());
		}

		try {
			userTransaction.begin ();
			entityManager = getEntityManager ();

			Collection<Post> attachedPostCollection = new ArrayList<Post> ();
			Collection<Thread> attachedThreadCollection = new ArrayList<Thread> ();

			for (Post postCollectionPostToAttach : user.getPostCollection ()) {
				postCollectionPostToAttach = entityManager.getReference (postCollectionPostToAttach.getClass (), postCollectionPostToAttach.getId ());
				attachedPostCollection.add (postCollectionPostToAttach);
			}

			user.setPostCollection (attachedPostCollection);

			for (Thread threadCollectionThreadToAttach : user.getThreadCollection ()) {
				threadCollectionThreadToAttach = entityManager.getReference (threadCollectionThreadToAttach.getClass (), threadCollectionThreadToAttach.getId ());
				attachedThreadCollection.add (threadCollectionThreadToAttach);
			}

			user.setThreadCollection (attachedThreadCollection);
			entityManager.persist (user);

			for (Post postCollectionPost : user.getPostCollection ()) {
				User oldAuthorOfPostCollectionPost = postCollectionPost.getAuthor ();

				postCollectionPost.setAuthor (user);
				postCollectionPost = entityManager.merge (postCollectionPost);

				if (oldAuthorOfPostCollectionPost != null) {
					oldAuthorOfPostCollectionPost.getPostCollection ().remove (postCollectionPost);
					oldAuthorOfPostCollectionPost = entityManager.merge (oldAuthorOfPostCollectionPost);
				}
			}

			for (Thread threadCollectionThread : user.getThreadCollection ()) {
				User oldAuthorOfThreadCollectionThread = threadCollectionThread.getAuthor ();

				threadCollectionThread.setAuthor (user);
				threadCollectionThread = entityManager.merge (threadCollectionThread);

				if (oldAuthorOfThreadCollectionThread != null) {
					oldAuthorOfThreadCollectionThread.getThreadCollection ().remove (threadCollectionThread);
					oldAuthorOfThreadCollectionThread = entityManager.merge (oldAuthorOfThreadCollectionThread);
				}
			}

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

	public void edit (User user) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
		Context context = new InitialContext ();
		EntityManager entityManager = null;

		userTransaction = (UserTransaction) context.lookup ("java:comp/UserTransaction");

		try {
			userTransaction.begin ();
			entityManager = getEntityManager ();

			User persistentUser = entityManager.find (User.class, user.getUsername ());
			Collection<Post> postCollectionOld = persistentUser.getPostCollection ();
			Collection<Post> postCollectionNew = user.getPostCollection ();
			Collection<Thread> threadCollectionOld = persistentUser.getThreadCollection ();
			Collection<Thread> threadCollectionNew = user.getThreadCollection ();
			List<String> illegalOrphanMessages = null;

			for (Post postCollectionOldPost : postCollectionOld) {
				if (!postCollectionNew.contains (postCollectionOldPost)) {
					if (illegalOrphanMessages == null) {
						illegalOrphanMessages = new ArrayList<String> ();
					}

					illegalOrphanMessages.add ("You must retain Post " + postCollectionOldPost + " since its author field is not nullable.");
				}
			}

			for (Thread threadCollectionOldThread : threadCollectionOld) {
				if (!threadCollectionNew.contains (threadCollectionOldThread)) {
					if (illegalOrphanMessages == null) {
						illegalOrphanMessages = new ArrayList<String> ();
					}

					illegalOrphanMessages.add ("You must retain Thread " + threadCollectionOldThread + " since its author field is not nullable.");
				}
			}

			if (illegalOrphanMessages != null) {
				throw (new IllegalOrphanException (illegalOrphanMessages));
			}

			Collection<Post> attachedPostCollectionNew = new ArrayList<Post> ();
			Collection<Thread> attachedThreadCollectionNew = new ArrayList<Thread> ();

			for (Post postCollectionNewPostToAttach : postCollectionNew) {
				postCollectionNewPostToAttach = entityManager.getReference (postCollectionNewPostToAttach.getClass (), postCollectionNewPostToAttach.getId ());
				attachedPostCollectionNew.add (postCollectionNewPostToAttach);
			}

			postCollectionNew = attachedPostCollectionNew;
			user.setPostCollection (postCollectionNew);

			for (Thread threadCollectionNewThreadToAttach : threadCollectionNew) {
				threadCollectionNewThreadToAttach = entityManager.getReference (threadCollectionNewThreadToAttach.getClass (), threadCollectionNewThreadToAttach.getId ());
				attachedThreadCollectionNew.add (threadCollectionNewThreadToAttach);
			}

			threadCollectionNew = attachedThreadCollectionNew;
			user.setThreadCollection (threadCollectionNew);
			user = entityManager.merge (user);

			for (Post postCollectionNewPost : postCollectionNew) {
				if (!postCollectionOld.contains (postCollectionNewPost)) {
					User oldAuthorOfPostCollectionNewPost = postCollectionNewPost.getAuthor ();

					postCollectionNewPost.setAuthor (user);
					postCollectionNewPost = entityManager.merge (postCollectionNewPost);

					if ((oldAuthorOfPostCollectionNewPost != null) && (!oldAuthorOfPostCollectionNewPost.equals (user))) {
						oldAuthorOfPostCollectionNewPost.getPostCollection ().remove (postCollectionNewPost);
						oldAuthorOfPostCollectionNewPost = entityManager.merge (oldAuthorOfPostCollectionNewPost);
					}
				}
			}

			for (Thread threadCollectionNewThread : threadCollectionNew) {
				if (!threadCollectionOld.contains (threadCollectionNewThread)) {
					User oldAuthorOfThreadCollectionNewThread = threadCollectionNewThread.getAuthor ();

					threadCollectionNewThread.setAuthor (user);
					threadCollectionNewThread = entityManager.merge (threadCollectionNewThread);

					if ((oldAuthorOfThreadCollectionNewThread != null) && (!oldAuthorOfThreadCollectionNewThread.equals (user))) {
						oldAuthorOfThreadCollectionNewThread.getThreadCollection ().remove (threadCollectionNewThread);
						oldAuthorOfThreadCollectionNewThread = entityManager.merge (oldAuthorOfThreadCollectionNewThread);
					}
				}
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

	public void destroy (String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
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

			List<String> illegalOrphanMessages = null;
			Collection<Post> postCollectionOrphanCheck = user.getPostCollection ();
			Collection<Thread> threadCollectionOrphanCheck = user.getThreadCollection ();

			for (Post postCollectionOrphanCheckPost : postCollectionOrphanCheck) {
				if (illegalOrphanMessages == null) {
					illegalOrphanMessages = new ArrayList<String> ();
				}

				illegalOrphanMessages.add ("This User (" + user + ") cannot be destroyed since the Post " + postCollectionOrphanCheckPost + " in its postCollection field has a non-nullable author field.");
			}

			for (Thread threadCollectionOrphanCheckThread : threadCollectionOrphanCheck) {
				if (illegalOrphanMessages == null) {
					illegalOrphanMessages = new ArrayList<String> ();
				}

				illegalOrphanMessages.add ("This User (" + user + ") cannot be destroyed since the Thread " + threadCollectionOrphanCheckThread + " in its threadCollection field has a non-nullable author field.");
			}

			if (illegalOrphanMessages != null) {
				throw (new IllegalOrphanException (illegalOrphanMessages));
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
			Query query = null;

			criteriaQuery.select (criteriaQuery.from (User.class));
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
