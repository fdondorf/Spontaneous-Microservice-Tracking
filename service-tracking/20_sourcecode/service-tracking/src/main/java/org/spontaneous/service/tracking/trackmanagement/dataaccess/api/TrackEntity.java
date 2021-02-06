package org.spontaneous.service.tracking.trackmanagement.dataaccess.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.spontaneous.service.tracking.general.dataaccess.api.ApplicationPersistenceEntity;

/**
 * The {@link ApplicationPersistenceEntity persistent entity} for a track.
 *
 * @author fdondorf
 */
@Entity
@Table(name = "TRACK")
public class TrackEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	private Float totalDistance;

	private Long totalDuration;

	@Column(name = "CREATION_TIME", columnDefinition = "TIMESTAMP", nullable = true)
	private LocalDateTime creationTime;

	@Column(name = "user_id")
	@Type(type = "uuid-char")
	private UUID userId;

	private Long averageSpeed;

	@Column(name = "TRACK_DETAILS")
	@Convert(converter = JSONAttributeConverter.class)
	private List<TrackSegment> trackDetails = new ArrayList<TrackSegment>();

	public TrackEntity() {
		super();
	}

	public TrackEntity(Long id, String name, LocalDateTime creationTime, Float totalDistance, Long totalDuration,
			UUID userId) {
		this.id = id;
		this.name = name;
		this.creationTime = creationTime;
		this.totalDistance = totalDistance;
		this.totalDuration = totalDuration;
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(unique = true)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TrackSegment> getTrackDetails() {
		return this.trackDetails;
	}

	public void setTrackDetails(List<TrackSegment> trackDetails) {
		this.trackDetails = trackDetails;
	}

	public boolean addTrackDetail(TrackSegment tracgSegment) {
		if (trackDetails == null) {
			trackDetails = new ArrayList<TrackSegment>();
		}
		return trackDetails.add(tracgSegment);
	}

	public Float getTotalDistance() {
		return this.totalDistance;
	}

	public void setTotalDistance(Float totalDistance) {
		this.totalDistance = totalDistance;
	}

	public Long getTotalDuration() {
		return this.totalDuration;
	}

	public void setTotalDuration(Long totalDuration) {
		this.totalDuration = totalDuration;
	}

	public LocalDateTime getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public Long getAverageSpeed() {
		return this.averageSpeed;
	}

	public void setAverageSpeed(Long averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

}
