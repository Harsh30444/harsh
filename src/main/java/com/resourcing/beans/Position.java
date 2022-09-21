/**
 * @author Nikhil Gundla
 *
 */
package com.resourcing.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "Position")
@Data
public class Position {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PostId")
	private int postId; // Primary Key

	@Column(name = "DeptId")
	private int deptId;

	@Column(name = "PostName")
	private String postName;

	@Column(name = "DeptName")
	private String deptName;

	@Column(name = "IsActive")
	private char isActive;

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

}
