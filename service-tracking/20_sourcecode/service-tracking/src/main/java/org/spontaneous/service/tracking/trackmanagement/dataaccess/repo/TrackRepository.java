package org.spontaneous.service.tracking.trackmanagement.dataaccess.repo;

import java.util.List;
import java.util.UUID;

import org.spontaneous.service.tracking.trackmanagement.dataaccess.api.TrackEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface TrackRepository extends CrudRepository<TrackEntity, Long>{

	@Query("Select new org.spontaneous.service.tracking.trackmanagement.dataaccess.api.TrackEntity("
			+ "t.id, t.name, t.creationTime, t.totalDistance, t.totalDuration, t.userId) from TrackEntity t "
			+ "where t.userId = :userId")
	List<TrackEntity> findTracksByUserId(@Param("userId") UUID userId);
	 
}
