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
import javax.persistence.Id;
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
@Table (name = "user")
@NamedQueries ({
	@NamedQuery (name = "User.findAll", query = "SELECT u FROM User u"),
	@NamedQuery (name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
	@NamedQuery (name = "User.findByAge", query = "SELECT u FROM User u WHERE u.age = :age"),
	@NamedQuery (name = "User.findByAvatar", query = "SELECT u FROM User u WHERE u.avatar = :avatar"),
	@NamedQuery (name = "User.findByLoginCount", query = "SELECT u FROM User u WHERE u.loginCount = :logincount"),
	@NamedQuery (name = "User.findByLoginLast", query = "SELECT u FROM User u WHERE u.loginLast = :loginlast"),
	@NamedQuery (name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
	@NamedQuery (name = "User.findBySex", query = "SELECT u FROM User u WHERE u.sex = :sex")})
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Basic (optional = false)
	@NotNull
	@Size (min = 1, max = 255)
	@Column (name = "username")
	private String username;
	
	@Basic (optional = false)
	@NotNull
	@Column (name = "age")
	private int age;
	
	@Basic (optional = false)
	@NotNull
	@Size (min = 1, max = 255)
	@Column (name = "avatar")
	private String avatar;
	
	@Column (name = "logincount")
	private Integer loginCount;
	
	@Column (name = "loginlast")
	@Temporal (TemporalType.TIMESTAMP)
	private Date loginLast;
	
	@Basic (optional = false)
	@NotNull
	@Size (min = 1, max = 255)
	@Column (name = "password")
	private String password;
	
	@Basic (optional = false)
	@NotNull
	@Column (name = "sex")
	private boolean sex;
	
	@OneToMany (cascade = CascadeType.ALL, mappedBy = "author", fetch = FetchType.EAGER)
	private Collection<Post> postCollection;
	
	@OneToMany (cascade = CascadeType.ALL, mappedBy = "author", fetch = FetchType.EAGER)
	private Collection<Thread> threadCollection;

	public User () {
	}

	public User (String username) {
		this.username = username;
	}

	public User (String username, int age, String avatar, String password, boolean sex) {
		this.username = username;
		this.age = age;
		this.avatar = avatar;
		this.password = password;
		this.sex = sex;
	}

	public String getUsername () {
		return (username);
	}

	public void setUsername (String username) {
		this.username = username;
	}

	public int getAge () {
		return (age);
	}

	public void setAge (int age) {
		this.age = age;
	}

	public String getAvatar () {
		return (avatar);
	}

	public void setAvatar (String avatar) {
		this.avatar = avatar;
	}

	public Integer getLoginCount () {
		return (loginCount);
	}

	public void setLoginCount (Integer loginCount) {
		this.loginCount = loginCount;
	}

	public Date getLoginLast () {
		return (loginLast);
	}

	public void setLoginLast (Date loginLast) {
		this.loginLast = loginLast;
	}

	public String getPassword () {
		return (password);
	}

	public void setPassword (String password) {
		this.password = password;
	}

	public boolean getSex () {
		return (sex);
	}

	public void setSex (boolean sex) {
		this.sex = sex;
	}

	public Collection<Post> getPostCollection () {
		return (postCollection);
	}

	public void setPostCollection (Collection<Post> postCollection) {
		this.postCollection = postCollection;
	}

	public Collection<Thread> getThreadCollection () {
		return (threadCollection);
	}

	public void setThreadCollection (Collection<Thread> threadCollection) {
		this.threadCollection = threadCollection;
	}

	@Override
	public int hashCode () {
		int hash = 0;

		hash += ((username != null) ? username.hashCode () : 0);

		return (hash);
	}

	@Override
	public boolean equals (Object object) {
		if (!(object instanceof User)) {
			return (false);
		}

		User other = (User) object;

		if (((this.username == null) && (other.username != null)) || ((this.username != null) && (!this.username.equals (other.username)))) {
			return (false);
		}

		return (true);
	}

	@Override
	public String toString () {
		return ("net.milanvit.iforum.models.User[ username=" + username + " ]");
	}
}
