package com.police.evisitor.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

	@OneToOne
	@JsonManagedReference
	@ToString.Exclude
	@JoinColumn(name = "users")
	private User user;

	@OneToOne
	@JsonManagedReference
	@ToString.Exclude
	@JoinColumn(name = "hotel")
	private Hotel hotel;

	@OneToOne
	@JsonManagedReference
	@ToString.Exclude
	@JoinColumn(name = "files")
	private DocFile docFile;

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

	@Column(name = "visitor_name")
	private String visitorName;

	@Column(name = "visitor_mobile")
	private String visitorMobile;

	@Column(name = "visitor_mail")
	private String visitorMail;

	@Column(name = "visitor_dob")
	private LocalDate visitorDob;

	@Column(name = "visitor_gender")
	private String visitorGender;

	@Column(name = "nationality_cd")
	private Integer nationalityCd;

	@Column(name = "state_cd")
	private Integer stateCd;

	@Column(name = "district_cd")
	private Integer districtCd;

	@Column(name = "ps_cd")
	private Integer psCd;

	@Column(name = "visitor_address")
	private String visitorAddress;

	@ManyToOne
	@JoinColumn(name = "visitor_ref")
	@JsonBackReference
	private Visitor visitorRef;

	@Column(name = "document_no")
	private String documentNo;

	@Column(name = "nationality_name")
	private String nationalityName;

	@Column(name = "state_name")
	private String stateName;

	@Column(name = "district_name")
	private String districtName;

	@Column(name = "ps_name")
	private String psName;

	@OneToMany(mappedBy = "visitorRef", cascade = CascadeType.PERSIST)
	@JsonManagedReference
	private List<Visitor> subVisitors;

	@Column(name = "created_by", updatable = false)
	private String createdBy;

	@Column(name = "created_on", updatable = false)
	@CreationTimestamp
	private LocalDateTime createdOn;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_on")
	@UpdateTimestamp
	private LocalDateTime updatedOn;

	@Column(name = "record_status")
	private String recordStatus;

}