package com.police.evisitor.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_visitors")
public class Visitor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "visitor_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users")
	private User user;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hotel")
	private Hotel hotel;

	@JoinColumn(name = "files_id")
	private String filesId;
	
	@JoinColumn(name = "photo_id")
	private String photoId;

	@Column(name = "room_no")
	private String roomNo;

	@Column(name = "check_in_date")
	private LocalDateTime checkInDate;

	@Column(name = "check_out_date")
	private LocalDateTime checkOutDate;

	@Column(name = "coming_location")
	private String comingLocation;

	@Column(name = "going_location")
	private String goingLocation;

	@Column(name = "visit_reason_id")
	private Integer visitReasonType;

	@Column(name = "visit_reason")
	private String visitReason;

	@Column(name = "note")
	private String note;

	@Column(name = "visitor_first_name")
	private String visitorFirstName;

	@Column(name = "visitor_last_name")
	private String visitorLastName;

	@Column(name = "visitor_mobile")
	private String visitorMobile;

	@Column(name = "visitor_mail")
	private String visitorMail;

	@Column(name = "visitor_relative_name")
	private String visitorRelativeName;

	@Column(name = "visitor_dob")
	private LocalDate visitorDob;

	@Column(name = "visitor_gender")
	private String visitorGender;

	@Column(name = "nationality_cd")
	private Integer nationalityCd;

	@Column(name = "nationality_name")
	private String nationalityName;

	@Column(name = "state_cd")
	private Integer stateCd;

	@Column(name = "state_name")
	private String stateName;

	@Column(name = "district_cd")
	private Integer districtCd;

	@Column(name = "district_name")
	private String districtName;

	@Column(name = "ps_cd")
	private Integer psCd;

	@Column(name = "ps_name")
	private String psName;

	@Column(name = "visitor_address")
	private String visitorAddress;

	@Column(name = "pincode")
	private Integer pincode;

	@Column(name = "document_no")
	private String documentNo;

	@Column(name = "document_type")
	private Integer documentType;

	@Column(name = "created_by", updatable = false)
	private String createdBy;

	@CreationTimestamp
	@Column(name = "created_on", updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "updated_by")
	private String updatedBy;

	@UpdateTimestamp
	@Column(name = "updated_on")
	private LocalDateTime updatedOn;

	@Column(name = "record_status")
	private String recordStatus;
}