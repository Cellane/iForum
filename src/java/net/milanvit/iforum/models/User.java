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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import net.milanvit.iforum.helpers.Hash;

/**
 *
 * @author Milan
 */
@Entity
@Table (name = "user")
@XmlRootElement
@NamedQueries ({
	@NamedQuery (name = "User.findAll", query = "SELECT u FROM User u"),
	@NamedQuery (name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")})
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic (optional = false)
	@NotNull
	@Size (min = 1, max = 32)
	@Column (name = "username")
	private String username;
	
	@Basic (optional = false)
	@NotNull
	@Size (min = 1, max = 64)
	@Column (name = "password")
	private String password;
	
	@Basic (optional = false)
	@NotNull
	@Column (name = "age")
	private int age;
	
	@Basic (optional = false)
	@NotNull
	@Size (min = 1, max = 128)
	@Column (name = "avatar")
	private String avatar;
	
	@Basic (optional = false)
	@NotNull
	@Column (name = "sex")
	private boolean sex;
	
	@Column (name = "logincount")
	private Integer loginCount;
	
	@Column (name = "loginlast")
	@Temporal (TemporalType.TIMESTAMP)
	private Date loginLast;

	public User () {
	}

	public User (String username) {
		this.username = username;
	}

	public User (String username, String password, int age, String avatar, boolean sex) {
		this.username = username;
		this.password = Hash.toSHA256 (password);
		this.age = age;
		this.avatar = avatar;
		this.sex = sex;
	}

	public String getUsername () {
		return (username);
	}

	public void setUsername (String username) {
		this.username = username;
	}

	public String getPassword () {
		return (password);
	}

	public void setPassword (String password) {
		this.password = Hash.toSHA256 (password);
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

	public boolean getSex () {
		return (sex);
	}

	public void setSex (boolean sex) {
		this.sex = sex;
	}

	public Integer getLoginCount () {
		return (loginCount);
	}

	public void setLoginCount (Integer loginCount) {
		this.loginCount = loginCount;
	}

	public Date getLoginLast () {
		return loginLast;
	}

	public void setLoginLast (Date loginLast) {
		this.loginLast = loginLast;
	}

	@Override
	public int hashCode () {
		int hash = 0;

		hash += ((username != null) ? username.hashCode () : 0);

		return (hash);
	}

	@Override
	public boolean equals (Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
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
