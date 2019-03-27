package org.aztec.autumn.common.localcache;

import java.io.Serializable;

public class UserEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String account;
	private Integer age;
	private Boolean sex;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Boolean getSex() {
		return sex;
	}
	public void setSex(Boolean sex) {
		this.sex = sex;
	}
	public UserEntity(Long id, String name, String account, Integer age,
			Boolean sex) {
		super();
		this.id = id;
		this.name = name;
		this.account = account;
		this.age = age;
		this.sex = sex;
	}
	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", name=" + name + ", account="
				+ account + ", age=" + age + ", sex=" + sex + "]";
	}
	
	
	
}
