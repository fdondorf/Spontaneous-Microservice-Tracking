package org.spontaneous.service.tracking.trackmanagement.service.rest.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.spontaneous.service.tracking.general.service.api.rest.Header;

/**
 * Model containing the data of a track
 * @author fdondorf
 *
 */
public class TrackTO extends Header {

	private Long id;
	private String name;
	private Float totalDistance;
	private Long totalDuration;
	private Long creationTime;
	private UUID userId;
	private List<SegmentTO> segments = new ArrayList<SegmentTO>();
	
	public TrackTO(){;}
			
	public TrackTO(Long id, String name, Float totalDistance, Long totalDuration, Long creationTime, UUID userId) {
		super();
		this.id = id;
		this.name = name;
		this.totalDistance = totalDistance;
		this.totalDuration = totalDuration;
		this.creationTime = creationTime;
		this.userId = userId;
	}
	
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
	
	public Float getTotalDistance() {
		return totalDistance;
	}
	
	public void setTotalDistance(Float totalDistance) {
		this.totalDistance = totalDistance;
	}
	
	public Long getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(Long totalDuration) {
		this.totalDuration = totalDuration;
	}

	public Long getCreationTime() {
		return creationTime;
	}
	
	public void setCreationTime(Long creationTime) {
		this.creationTime = creationTime;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public List<SegmentTO> getSegments() {
		return segments;
	}

	public void setSegments(List<SegmentTO> segments) {
		this.segments = segments;
	}
	
	public boolean addSegment(SegmentTO segment) {
		if (this.segments == null) {
			this.segments = new ArrayList<SegmentTO>();
		}
		return this.segments.add(segment);
	}

	@Override
	public String toString() {
		return "TrackModel [id=" + id + ", name=" + name + ", totalDistance=" + totalDistance + ", totalDuration="
				+ totalDuration + ", creationTime=" + creationTime + ", userId=" + userId + ", segments=" + segments
				+ "]";
	}

}
