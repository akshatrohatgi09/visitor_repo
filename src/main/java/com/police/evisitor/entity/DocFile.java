package com.police.evisitor.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "t_documents")
public class DocFile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "doc_id")
	private Long docId;

	@OneToOne
	@JsonManagedReference
	@ToString.Exclude
	@JoinColumn(name = "document_type")
	private MasterDocument documentType;

	@Column(name = "visitor_id")
	private Long visitorId;

	@Column(name = "file_path")
	private String filePath;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "file_type")
	private String fileContentType;

	@Column(name = "file_size")
	private String fileSize;

	@Column(name = "file_pass")
	private String filePass;

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