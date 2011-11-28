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
import net.milanvit.iforum.controllers.exceptions.RollbackFailureException;
import net.milanvit.iforum.models.Thread;
import net.milanvit.iforum.models.User;
import net.milanvit.iforum.models.Post;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

/**
 *
 * @author Milan
 */
public class ThreadController implements Serializable {
	@Resource
	private UserTransaction userTransaction = null;
	
	@PersistenceUnit (unitName = "iForumPersistenceUnit")
	private EntityManagerFactory entityManagerFactory = null;

	public ThreadController (UserTransaction userTransaction, EntityManagerFactory entityManagerFactory) {
		this.userTransaction = userTransaction;
		this.entityManagerFactory = entityManagerFactory;
	}

	public EntityManager getEntityManager () {
		if (entityManagerFactory == null) {
			entityManagerFactory = Persistence.createEntityManagerFactory ("iForumPersistenceUnit");
		}

		return (entityManagerFactory.createEntityManager ());
	}

	public void create (Thread thread) throws RollbackFailureException, Exception {
		Context context = new InitialContext ();
		EntityManager entityManager = null;

		userTransaction = (UserTransaction) context.lookup ("java:comp/UserTransaction");

		if (thread.getPostCollection () == null) {
			thread.setPostCollection (new ArrayList<Post> ());
		}

		try {
			userTransaction.begin ();
			entityManager = getEntityManager ();

			User author = thread.getAuthor ();

			if (author != null) {
				author = entityManager.getReference (author.getClass (), author.getUsername ());
				thread.setAuthor (author);
			}

			Collection<Post> attachedPostCollection = new ArrayList<Post> ();

			for (Post postCollectionPostToAttach : thread.getPostCollection ()) {
				postCollectionPostToAttach = entityManager.getReference (postCollectionPostToAttach.getClass (), postCollectionPostToAttach.getId ());
				attachedPostCollection.add (postCollectionPostToAttach);
			}

			thread.setPostCollection (attachedPostCollection);
			entityManager.persist (thread);

			if (author != null) {
				author.getThreadCollection ().add (thread);
				author = entityManager.merge (author);
			}

			for (Post postCollectionPost : thread.getPostCollection ()) {
				Thread oldThreadOfPostCollectionPost = postCollectionPost.getThread ();

				postCollectionPost.setThread (thread);
				postCollectionPost = entityManager.merge (postCollectionPost);

				if (oldThreadOfPostCollectionPost != null) {
					oldThreadOfPostCollectionPost.getPostCollection ().remove (postCollectionPost);
					oldThreadOfPostCollectionPost = entityManager.merge (oldThreadOfPostCollectionPost);
				}
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

	public void edit (Thread thread) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
		Context context = new InitialContext ();
		EntityManager entityManager = null;
		
		userTransaction = (UserTransaction) context.lookup ("java:comp/UserTransaction");

		try {
			userTransaction.begin ();
			entityManager = getEntityManager ();

			Thread persistentThread = entityManager.find (Thread.class, thread.getId ());
			User authorOld = persistentThread.getAuthor ();
			User authorNew = thread.getAuthor ();
			Collection<Post> postCollectionOld = persistentThread.getPostCollection ();
			Collection<Post> postCollectionNew = thread.getPostCollection ();
			List<String> illegalOrphanMessages = null;

			for (Post postCollectionOldPost : postCollectionOld) {
				if (!postCollectionNew.contains (postCollectionOldPost)) {
					if (illegalOrphanMessages == null) {
						illegalOrphanMessages = new ArrayList<String> ();
					}

					illegalOrphanMessages.add ("You must retain Post " + postCollectionOldPost + " since its thread field is not nullable.");
				}
			}

			if (illegalOrphanMessages != null) {
				throw (new IllegalOrphanException (illegalOrphanMessages));
			}
			
			if (authorNew != null) {
				authorNew = entityManager.getReference (authorNew.getClass (), authorNew.getUsername ());
				thread.setAuthor (authorNew);
			}

			Collection<Post> attachedPostCollectionNew = new ArrayList<Post> ();

			for (Post postCollectionNewPostToAttach : postCollectionNew) {
				postCollectionNewPostToAttach = entityManager.getReference (postCollectionNewPostToAttach.getClass (), postCollectionNewPostToAttach.getId ());
				attachedPostCollectionNew.add (postCollectionNewPostToAttach);
			}

			postCollectionNew = attachedPostCollectionNew;
			thread.setPostCollection (postCollectionNew);
			thread = entityManager.merge (thread);

			if ((authorOld != null) && (!authorOld.equals (authorNew))) {
				authorOld.getThreadCollection ().remove (thread);
				authorOld = entityManager.merge (authorOld);
			}

			if ((authorNew != null) && (!authorNew.equals (authorOld))) {
				authorNew.getThreadCollection ().add (thread);
				authorNew = entityManager.merge (authorNew);
			}

			for (Post postCollectionNewPost : postCollectionNew) {
				if (!postCollectionOld.contains (postCollectionNewPost)) {
					Thread oldThreadOfPostCollectionNewPost = postCollectionNewPost.getThread ();

					postCollectionNewPost.setThread (thread);
					postCollectionNewPost = entityManager.merge (postCollectionNewPost);

					if ((oldThreadOfPostCollectionNewPost != null) && (!oldThreadOfPostCollectionNewPost.equals (thread))) {
						oldThreadOfPostCollectionNewPost.getPostCollection ().remove (postCollectionNewPost);
						oldThreadOfPostCollectionNewPost = entityManager.merge (oldThreadOfPostCollectionNewPost);
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
				Integer id = thread.getId ();

				if (findThread (id) == null) {
					throw (new NonexistentEntityException ("The thread with id " + id + " no longer exists."));
				}
			}

			throw (e);
		} finally {
			if (entityManager != null) {
				entityManager.close ();
			}
		}
	}

	public void destroy (Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
		EntityManager entityManager = null;

		try {
			userTransaction.begin ();
			entityManager = getEntityManager ();

			Thread thread;

			try {
				thread = entityManager.getReference (Thread.class, id);
				thread.getId ();
			} catch (EntityNotFoundException enfe) {
				throw (new NonexistentEntityException ("The thread with id " + id + " no longer exists.", enfe));
			}

			List<String> illegalOrphanMessages = null;
			Collection<Post> postCollectionOrphanCheck = thread.getPostCollection ();

			for (Post postCollectionOrphanCheckPost : postCollectionOrphanCheck) {
				if (illegalOrphanMessages == null) {
					illegalOrphanMessages = new ArrayList<String> ();
				}

				illegalOrphanMessages.add ("This Thread (" + thread + ") cannot be destroyed since the Post " + postCollectionOrphanCheckPost + " in its postCollection field has a non-nullable thread field.");
			}

			if (illegalOrphanMessages != null) {
				throw (new IllegalOrphanException (illegalOrphanMessages));
			}

			User author = thread.getAuthor ();

			if (author != null) {
				author.getThreadCollection ().remove (thread);
				author = entityManager.merge (author);
			}

			entityManager.remove (thread);
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

	public List<Thread> findThreadEntities () {
		return (findThreadEntities (true, -1, -1));
	}

	public List<Thread> findThreadEntities (int maxResults, int firstResult) {
		return (findThreadEntities (false, maxResults, firstResult));
	}

	private List<Thread> findThreadEntities (boolean all, int maxResults, int firstResult) {
		EntityManager entityManager = getEntityManager ();

		try {
			CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder ().createQuery ();
			Query query = null;

			criteriaQuery.select (criteriaQuery.from (Thread.class));
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

	public Thread findThread (Integer id) {
		EntityManager entityManager = getEntityManager ();

		try {
			return (entityManager.find (Thread.class, id));
		} finally {
			entityManager.close ();
		}
	}

	public int getThreadCount () {
		EntityManager entityManager = getEntityManager ();

		try {
			CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder ().createQuery ();
			Root<Thread> root = criteriaQuery.from (Thread.class);
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
