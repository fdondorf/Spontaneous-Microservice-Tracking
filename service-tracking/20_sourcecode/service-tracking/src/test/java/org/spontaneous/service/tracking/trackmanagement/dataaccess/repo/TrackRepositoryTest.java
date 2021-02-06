package org.spontaneous.service.tracking.trackmanagement.dataaccess.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spontaneous.service.tracking.common.builder.TrackEntityBuilder;
import org.spontaneous.service.tracking.trackmanagement.dataaccess.api.TrackEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

/**
 * Unit Test for the Track-Repository
 * 
 * @author fdondorf
 *
 */
@DataJpaTest
@TestPropertySource(properties = { "spring.config.location = classpath:application-test.yml" })
public class TrackRepositoryTest {

	private static final Float TOTAL_DISTANCE = 9999.99f;

	@Autowired
	private TrackRepository trackRepository;

	@MockBean
	private ResourceServerProperties resourceServerProperties;

	@BeforeEach
	public void setUp() {
		trackRepository.deleteAll();
	}

	@AfterEach
	public void tearDown() {
		trackRepository.deleteAll();
	}

	@Test
	public void testCreateTrack() {

		// Given
		TrackEntity trackEntity = TrackEntityBuilder.aDefaultTrackEntity().withDefaultSegments(3).withName("Track 1")
				.withUser(UUID.randomUUID()).build();

		// When
		TrackEntity savedTrack = trackRepository.save(trackEntity);

		// Then
		assertNotNull(savedTrack.getId());
	}

	@Test
	public void testFindTracks() {

		// Given
		UUID userId = UUID.randomUUID();
		List<TrackEntity> generatedTracks = createTracks(userId);

		// When
		List<TrackEntity> foundTracks = trackRepository.findTracksByUserId(userId);

		// Then
		assertNotNull(generatedTracks);
		assertEquals(generatedTracks.size(), foundTracks.size());
	}

	@Test
	public void testFindTrack() {

		// Given
		UUID userID = UUID.randomUUID();
		List<TrackEntity> generatedTracks = createTracks(userID);

		// When
		TrackEntity trackEntity = trackRepository.findById(generatedTracks.get(1).getId()).get();

		// Then
		assertNotNull(trackEntity);
		assertEquals("Track 1", trackEntity.getName());
		assertEquals(TOTAL_DISTANCE, trackEntity.getTotalDistance());
		assertNotNull(trackEntity.getTrackDetails());
	}

	private List<TrackEntity> createTracks(UUID userId) {
		List<TrackEntity> tracks = new ArrayList<TrackEntity>();
		TrackEntity trackEntity;
		for (int i = 0; i < 10; i++) {
			trackEntity = trackRepository.save(TrackEntityBuilder.aDefaultTrackEntity().withName("Track " + i)
					.withUser(userId).withTotalDistance(TOTAL_DISTANCE).withDefaultSegments(3).build());
			tracks.add(trackEntity);
		}

		return tracks;
	}

}
