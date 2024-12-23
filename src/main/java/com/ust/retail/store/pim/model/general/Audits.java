package com.ust.retail.store.pim.model.general;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.ust.retail.store.pim.model.security.UserModel;

import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class Audits {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_create", referencedColumnName = "user_id")
	protected UserModel userCreate;
	
	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	protected Date createdAt;

	@Column(nullable = false,updatable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	protected Date updatedAt;

}
