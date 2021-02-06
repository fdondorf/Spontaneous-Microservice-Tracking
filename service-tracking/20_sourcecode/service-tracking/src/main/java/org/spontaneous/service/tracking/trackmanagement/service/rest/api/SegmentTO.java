package org.spontaneous.service.tracking.trackmanagement.service.rest.api;
import java.util.ArrayList;
import java.util.List;

public class SegmentTO {

	private Long id;
	private Long trackId;
	private Long startTimeInMillis;
	private Long endTimeInMillis;
	private List<GeoPointTO> wayPoints = new ArrayList<GeoPointTO>();
	
	public SegmentTO() {;}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getTrackId() {
		return trackId;
	}
	
	public void setTrackId(Long trackId) {
		this.trackId = trackId;
	}
	
	public Long getStartTimeInMillis() {
		return startTimeInMillis;
	}

	public void setStartTimeInMillis(Long startTimeInMillis) {
		this.startTimeInMillis = startTimeInMillis;
	}

	public Long getEndTimeInMillis() {
		return endTimeInMillis;
	}

	public void setEndTimeInMillis(Long endTimeInMillis) {
		this.endTimeInMillis = endTimeInMillis;
	}

	public List<GeoPointTO> getWayPoints() {
		return wayPoints;
	}
	
	public void setWayPoints(List<GeoPointTO> wayPoints) {
		this.wayPoints = wayPoints;
	}
	
	public boolean addWayPoint(GeoPointTO geoPoint) {
		if (this.wayPoints == null) {
			this.wayPoints = new ArrayList<GeoPointTO>();
		}
		return this.wayPoints.add(geoPoint);
	}
	
}
