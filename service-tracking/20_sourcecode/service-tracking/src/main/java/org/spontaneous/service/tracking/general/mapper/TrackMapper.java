package org.spontaneous.service.tracking.general.mapper;

import org.spontaneous.service.tracking.general.util.DateUtil;
import org.spontaneous.service.tracking.trackmanagement.dataaccess.api.TrackEntity;
import org.spontaneous.service.tracking.trackmanagement.dataaccess.api.TrackSegment;
import org.spontaneous.service.tracking.trackmanagement.dataaccess.api.Waypoint;
import org.spontaneous.service.tracking.trackmanagement.service.rest.api.GeoPointTO;
import org.spontaneous.service.tracking.trackmanagement.service.rest.api.SegmentTO;
import org.spontaneous.service.tracking.trackmanagement.service.rest.api.TrackTO;

public class TrackMapper {

	public static TrackEntity mapTrackModelToEntity(TrackTO trackTO) {

		TrackEntity trackEntity = new TrackEntity();
		trackEntity.setId(trackTO.getId());
		trackEntity.setUserId(trackTO.getUserId());
		trackEntity.setName(trackTO.getName());
		trackEntity.setCreationTime(DateUtil.getDateTimeFromTimestamp(trackTO.getCreationTime()));
		trackEntity.setTotalDistance(trackTO.getTotalDistance());
		trackEntity.setTotalDuration(trackTO.getTotalDuration());
		trackEntity.setAverageSpeed(null);

		for (SegmentTO segment : trackTO.getSegments()) {
			trackEntity.addTrackDetail(mapTrackSegmentToSegmentModel(segment));
		}

		return trackEntity;
	}

	public static TrackSegment mapTrackSegmentToSegmentModel(SegmentTO segmentModel) {

		TrackSegment trackSegment = new TrackSegment();
		trackSegment.setId(segmentModel.getId());
		trackSegment.setStartTime(DateUtil.parse(segmentModel.getStartTimeInMillis()));
		trackSegment.setEndTime(DateUtil.parse(segmentModel.getEndTimeInMillis()));

		for (GeoPointTO geoPoint : segmentModel.getWayPoints()) {
			trackSegment.addWaypoint(mapTrackSegmentToSegmentModel(geoPoint));
		}

		return trackSegment;
	}

	public static Waypoint mapTrackSegmentToSegmentModel(GeoPointTO geoPoint) {

		Waypoint waypoint = new Waypoint();
		waypoint.setId(geoPoint.getId());
		waypoint.setAccurracy(geoPoint.getAccurracy());
		waypoint.setAltitude(geoPoint.getAltitude());
		waypoint.setBearing(geoPoint.getBearing());
		waypoint.setDistance(geoPoint.getDistance());
		waypoint.setLatitude(geoPoint.getLatitude());
		waypoint.setLongitude(geoPoint.getLongitude());
		waypoint.setSegmentId(geoPoint.getSegmentId());
		waypoint.setSpeed(geoPoint.getSpeed());
		waypoint.setTime(geoPoint.getTime());

		return waypoint;
	}

	public static TrackTO mapTrackEntityToTrackModel(TrackEntity trackEntity) {

		TrackTO trackTO = new TrackTO();
		trackTO.setId(trackEntity.getId());
		trackTO.setName(trackEntity.getName());
		trackTO.setCreationTime(DateUtil.getDateTimeFromTimestamp(trackEntity.getCreationTime()));
		trackTO.setTotalDistance(trackEntity.getTotalDistance());
		trackTO.setTotalDuration(trackEntity.getTotalDuration());
		trackTO.setUserId(trackEntity.getUserId());

		for (TrackSegment segment : trackEntity.getTrackDetails()) {
			trackTO.getSegments().add(mapTrackSegmentToSegmentModel(segment));
		}
		return trackTO;
	}

	public static SegmentTO mapTrackSegmentToSegmentModel(TrackSegment trackSegment) {

		SegmentTO segmentModel = new SegmentTO();
		segmentModel.setId(trackSegment.getId());
		segmentModel.setStartTimeInMillis(DateUtil.parseToLong(trackSegment.getStartTime()));
		segmentModel.setEndTimeInMillis(DateUtil.parseToLong(trackSegment.getEndTime()));

		for (Waypoint waypoint : trackSegment.getWaypoints()) {
			segmentModel.getWayPoints().add(mapWaypointToGeoPointModel(waypoint));
		}

		return segmentModel;
	}

	public static GeoPointTO mapWaypointToGeoPointModel(Waypoint waypoint) {

		GeoPointTO geoPointModel = new GeoPointTO();
		geoPointModel.setId(waypoint.getId());
		geoPointModel.setAccurracy(waypoint.getAccurracy());
		geoPointModel.setAltitude(waypoint.getAltitude());
		geoPointModel.setBearing(waypoint.getBearing());
		geoPointModel.setDistance(waypoint.getDistance());
		geoPointModel.setLatitude(waypoint.getLatitude());
		geoPointModel.setLongitude(waypoint.getLongitude());
		geoPointModel.setSegmentId(waypoint.getSegmentId());
		geoPointModel.setSpeed(waypoint.getSpeed());
		geoPointModel.setTime(waypoint.getTime());

		return geoPointModel;
	}

}
