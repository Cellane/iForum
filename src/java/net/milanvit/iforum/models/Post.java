/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.milanvit.iforum.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
@Table (name = "post")
@NamedQueries ({
	@NamedQuery (name = "Post.findAll", query = "SELECT p FROM Post p"),
	@NamedQuery (name = "Post.findById", query = "SELECT p FROM Post p WHERE p.id = :id")})
public class Post implements Serializable {
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
    @Lob
    @Size (min = 1, max = 65535)
    @Column (name = "post")
	private String post;
	
	@JoinColumn (name = "author", referencedColumnName = "username")
    @ManyToOne (optional = false, fetch = FetchType.EAGER)
	private User author;
	
	@JoinColumn (name = "thread", referencedColumnName = "id")
    @ManyToOne (optional = false, fetch = FetchType.EAGER)
	private Thread thread;

	public Post () {
	}

	public Post (Integer id) {
		this.id = id;
	}

	public Post (Integer id, Date created, String post) {
		this.id = id;
		this.created = created;
		this.post = post;
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

	public String getPost () {
		return (post);
	}

	public void setPost (String post) {
		this.post = post;
	}

	public User getAuthor () {
		return (author);
	}

	public void setAuthor (User author) {
		this.author = author;
	}

	public Thread getThread () {
		return (thread);
	}

	public void setThread (Thread thread) {
		this.thread = thread;
	}

	@Override
	public int hashCode () {
		int hash = 0;
		
		hash += ((id != null) ? id.hashCode () : 0);
		
		return (hash);
	}

	@Override
	public boolean equals (Object object) {
		if (!(object instanceof Post)) {
			return (false);
		}
		
		Post other = (Post) object;
		
		if (((this.id == null) && (other.id != null)) || ((this.id != null) && (!this.id.equals (other.id)))) {
			return (false);
		}
		
		return (true);
	}

	@Override
	public String toString () {
		return ("net.milanvit.iforum.models.Post[ id=" + id + " ]");
	}
}
