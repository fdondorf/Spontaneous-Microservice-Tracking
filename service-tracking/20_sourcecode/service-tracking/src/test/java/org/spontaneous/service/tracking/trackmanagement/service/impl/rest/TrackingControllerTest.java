package org.spontaneous.service.tracking.trackmanagement.service.impl.rest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spontaneous.service.tracking.AbstractIntegrationTest;
import org.spontaneous.service.tracking.TrackingBootApp;
import org.spontaneous.service.tracking.common.builder.TrackEntityBuilder;
import org.spontaneous.service.tracking.general.mapper.TrackMapper;
import org.spontaneous.service.tracking.general.service.api.rest.Header;
import org.spontaneous.service.tracking.trackmanagement.dataaccess.api.TrackEntity;
import org.spontaneous.service.tracking.trackmanagement.dataaccess.repo.TrackRepository;
import org.spontaneous.service.tracking.trackmanagement.service.rest.api.TrackTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.github.dozermapper.core.Mapper;

/**
 * @author Flo Dondorf
 */
@SpringBootTest
@ContextConfiguration(classes = TrackingBootApp.class, initializers = ConfigFileApplicationContextInitializer.class)
@WebAppConfiguration
@TestPropertySource(properties = { "spring.config.location = classpath:application-test.yml" })
public class TrackingControllerTest extends AbstractIntegrationTest {

	private static final UUID USER_ID = UUID.fromString("401097a0-030e-47a8-b287-5a7e10a93025"); // UUID.randomUUID();

	private static final String PASSWORD_TEST_USER = "test";

	private static final String EMAIL_TEST_USER = "test@test.de";

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private TrackRepository trackRepository;

	@Autowired
	private Mapper mapper;

	private List<TrackEntity> tracks = new ArrayList<TrackEntity>();

	@BeforeEach
	public void setup() throws Exception {

		super.setup();

		// Delete all user and tracks
		this.trackRepository.deleteAll();

		// Create tracks
		createTracks(USER_ID);

	}

	@AfterEach
	public void tearDown() {
		// Delete all tracks
		this.trackRepository.deleteAll();
	}

	@Test
	public void createTrackTest() throws Exception {

		// Given
		String token = getToken(EMAIL_TEST_USER, PASSWORD_TEST_USER);

		// When
		TrackEntity trackEntity = TrackEntityBuilder.aDefaultTrackEntity().withDefaultSegments(2).withUser(USER_ID)
				.build();
		TrackTO trackTO = TrackMapper.mapTrackEntityToTrackModel(trackEntity);
		trackTO = addHeader(trackTO, "android");

		ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.post("/secure/v1/track")
				.with(bearerToken(token)).content(json(trackTO)).contentType(contentType));

		// Then
		result.andExpect(MockMvcResultMatchers.status().isOk());
		assertTrue(Matchers.iterableWithSize(4).matches(trackRepository.findAll()));

		revokeToken(token);
	}

	@Test
	public void updateTrackTest() throws Exception {

		// Given
		String token = getToken(EMAIL_TEST_USER, PASSWORD_TEST_USER);

		// When
		TrackEntity trackEntity = TrackEntityBuilder.aDefaultTrackEntity().withDefaultSegments(2).withUser(USER_ID)
				.build();
		TrackTO trackTO = TrackMapper.mapTrackEntityToTrackModel(trackEntity);
		trackTO.setUserId(trackEntity.getUserId());
		trackTO = addHeader(trackTO, "android");

		ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.post("/secure/v1/updateTrack")
				.with(bearerToken(token)).content(json(trackTO)).contentType(contentType));

		// Then
		result.andExpect(MockMvcResultMatchers.status().isOk());
		assertTrue(Matchers.iterableWithSize(4).matches(trackRepository.findAll()));

		revokeToken(token);
	}

	@Test
	public void deleteTrackTest() throws Exception {

		// Given
		String token = getToken(EMAIL_TEST_USER, PASSWORD_TEST_USER);

		// When
		ResultActions result = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/secure/v1/tracks/delete/{id}", tracks.get(0).getId())
						.with(bearerToken(token)).content(json(createHeader())).contentType(contentType));

		// Then {"trackId":21}
		result.andExpect(MockMvcResultMatchers.status().isOk());
		result.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("trackId")));
		result.andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString(String.valueOf(tracks.get(0).getId()))));
		assertTrue(Matchers.iterableWithSize(2).matches(trackRepository.findAll()));

		revokeToken(token);
	}

	@Test
	public void getTracksTest() throws Exception {

		// Given
		String token = getToken(EMAIL_TEST_USER, PASSWORD_TEST_USER);

		// When
		ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.post("/secure/v1/tracks")
				.with(bearerToken(token)).content(json(createHeader())).contentType(contentType));

		// Then
		result.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Track 0")))
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Track 1")))
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Track 2")))
				.andExpect(MockMvcResultMatchers.content()
						.string(Matchers.containsString("\"userId\":" + "\"" + USER_ID + "\"")));

		revokeToken(token);
	}

	private Header createHeader() {
		Header header = new Header();
		header = addHeader(header, "android");
		return header;
	}

	/*************************************************
	 * Create Testdata
	 **********************************************/

	private void createTracks(UUID userId) {
		tracks.clear();
		for (int i = 0; i < 3; i++) {
			this.tracks.add(trackRepository.save(TrackEntityBuilder.aDefaultTrackEntity().withName("Track " + i)
					.withUser(userId).withDefaultSegments(3).build()));
		}
	}

}
