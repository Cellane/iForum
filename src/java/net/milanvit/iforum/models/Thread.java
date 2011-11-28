/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.milanvit.iforum.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Milan
 */
@Entity
@Table (name = "thread")
@NamedQueries ({
	@NamedQuery (name = "Thread.findAll", query = "SELECT t FROM Thread t"),
	@NamedQuery (name = "Thread.findById", query = "SELECT t FROM Thread t WHERE t.id = :id"),
	@NamedQuery (name = "Thread.findByCreated", query = "SELECT t FROM Thread t WHERE t.created = :created"),
	@NamedQuery (name = "Thread.findByLocked", query = "SELECT t FROM Thread t WHERE t.locked = :locked"),
	@NamedQuery (name = "Thread.findByTitle", query = "SELECT t FROM Thread t WHERE t.title = :title")})
public class Thread implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Basic (optional = false)
	@Column (name = "id")
	private Integer id;
	
	@Basic (optional = false)
	@NotNull
	@Column (name = "created")
	@Temporal (TemporalType.TIMESTAMP)
	private Date created;
	
	@Basic (optional = false)
	@NotNull
	@Column (name = "locked")
	private boolean locked;
	
	@Basic (optional = false)
	@NotNull
	@Lob
	@Size (min = 1, max = 65535)
	@Column (name = "post")
	private String post;
	
	@Basic (optional = false)
	@NotNull
	@Size (min = 1, max = 255)
	@Column (name = "title")
	private String title;
	
	@OneToMany (cascade = CascadeType.ALL, mappedBy = "thread", fetch = FetchType.EAGER)
	private Collection<Post> postCollection;
	
	@JoinColumn (name = "author", referencedColumnName = "username")
	@ManyToOne (optional = false, fetch = FetchType.EAGER)
	private User author;

	public Thread () {
	}

	public Thread (Integer id) {
		this.id = id;
	}

	public Thread (Integer id, Date created, boolean locked, String post, String title) {
		this.id = id;
		this.created = created;
		this.locked = locked;
		this.post = post;
		this.title = title;
	}

	public Integer getId () {
		return (id);
	}

	public void setId (Integer id) {
		this.id = id;
	}

	public Date getCreated () {
		return (created);
	}

	public void setCreated (Date created) {
		this.created = created;
	}

	public boolean getLocked () {
		return (locked);
	}

	public void setLocked (boolean locked) {
		this.locked = locked;
	}

	public String getPost () {
		return (post);
	}

	public void setPost (String post) {
		this.post = post;
	}

	public String getTitle () {
		return (title);
	}

	public void setTitle (String title) {
		this.title = title;
	}

	public Collection<Post> getPostCollection () {
		return (postCollection);
	}

	public void setPostCollection (Collection<Post> postCollection) {
		this.postCollection = postCollection;
	}

	public User getAuthor () {
		return (author);
	}

	public void setAuthor (User author) {
		this.author = author;
	}

	@Override
	public int hashCode () {
		int hash = 0;

		hash += (id != null ? id.hashCode () : 0);

		return (hash);
	}

	@Override
	public boolean equals (Object object) {
		if (!(object instanceof Thread)) {
			return (false);
		}

		Thread other = (Thread) object;

		if (((this.id == null) && (other.id != null)) || ((this.id != null) && (!this.id.equals (other.id)))) {
			return (false);
		}

		return (true);
	}

	@Override
	public String toString () {
		return ("net.milanvit.iforum.models.Thread[ id=" + id + " ]");
	}
}
