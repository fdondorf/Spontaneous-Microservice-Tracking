package org.spontaneous.service.tracking.trackmanagement.service.rest;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spontaneous.service.tracking.general.mapper.TrackMapper;
import org.spontaneous.service.tracking.general.service.api.exception.TechnicalException;
import org.spontaneous.service.tracking.general.service.api.rest.Header;
import org.spontaneous.service.tracking.trackmanagement.dataaccess.api.TrackEntity;
import org.spontaneous.service.tracking.trackmanagement.dataaccess.repo.TrackRepository;
import org.spontaneous.service.tracking.trackmanagement.service.rest.api.DeleteTrackResult;
import org.spontaneous.service.tracking.trackmanagement.service.rest.api.GetTrackDetailsResult;
import org.spontaneous.service.tracking.trackmanagement.service.rest.api.GetTracksResult;
import org.spontaneous.service.tracking.trackmanagement.service.rest.api.TrackTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secure")
public class TrackingController extends AbstractClientAuthController {

	private static final String PREFIX_ACTIVITY_NAME = "Aktivität ";

	private static final Logger LOG = LoggerFactory.getLogger(TrackingController.class);

	@Autowired
	private TrackRepository trackRepository;

	/**
	 * Returns Tracks, wrapped in a ResponseEntity.
	 *
	 * @param headerData the HeaderData
	 * @param principal  the requested Principal
	 * @return UserInfo wrapped in a ResponseEntitiy
	 * @throws UserPrincipalNotFoundException
	 */
	@RequestMapping(value = "/v1/tracks", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetTracksResult> getTracks(@RequestBody Header headerData, Principal principal)
			throws UserPrincipalNotFoundException {

		LOG.info("Calling Controller 'tracks'");

		checkHeader(headerData);
		// validateInputRequestData(headerData);

		GetTracksResult result = new GetTracksResult();

		// User validation
		OAuth2Authentication authUser = getAuthUser(principal);
		if (authUser == null)
			throw new UserPrincipalNotFoundException("For the given principal no user was found...");

		UUID userId = getUserIdFromAuthentication(principal);

		Iterable<TrackEntity> tracks = trackRepository.findTracksByUserId(userId);
		Iterator<TrackEntity> iter = tracks.iterator();
		TrackEntity trackEntity;
		while (iter.hasNext()) {
			trackEntity = iter.next();
			result.addTrack(TrackMapper.mapTrackEntityToTrackModel(trackEntity));
		}

		return new ResponseEntity<GetTracksResult>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/v1/tracks/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetTrackDetailsResult> getTrackDetails(@PathVariable Long id, @RequestBody Header headerData,
			Principal principal) throws UserPrincipalNotFoundException {

		LOG.info("Calling Controller 'trackdetails'");

		// TODO: Validate Params and Security, AppVersion
		checkHeader(headerData);
		// validateInputRequestData(headerData);

		// User validation
		OAuth2Authentication authUser = getAuthUser(principal);
		if (authUser == null)
			throw new UserPrincipalNotFoundException("For the given principal no user was found...");

		// Get data
		GetTrackDetailsResult result = new GetTrackDetailsResult();
		Optional<TrackEntity> trackEntity = trackRepository.findById(id);
		if (trackEntity.isPresent()) {
			result.setTrackDetails(TrackMapper.mapTrackEntityToTrackModel(trackEntity.get()));
		}

		return new ResponseEntity<GetTrackDetailsResult>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/v1/updateTrack", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateTrack(@RequestBody TrackTO track, Principal principal)
			throws UserPrincipalNotFoundException {

		LOG.info("Calling Controller 'updateTrack'");

		// TODO: Validate AppVersion
		// validateInputRequestData(track);
		checkHeader(track);

		// User validation
		OAuth2Authentication authUser = getAuthUser(principal);
		if (authUser == null)
			throw new UserPrincipalNotFoundException("For the given principal no user was found...");

		// Save data
		track.setUserId(getUserIdFromAuthentication(principal));
		TrackEntity trackEntity = TrackMapper.mapTrackModelToEntity(track);

		// Set name if it is empty
		if (trackEntity.getName() == null) {
			trackEntity.setName(PREFIX_ACTIVITY_NAME + trackEntity.getId());
		}

		trackEntity = trackRepository.save(trackEntity);
		if (trackEntity == null)
			throw new ApplicationContextException("Error during creation of new track...");

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/v1/track", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> createTrack(@RequestBody TrackTO track, Principal principal)
			throws UserPrincipalNotFoundException {

		LOG.info("Calling Controller 'createTrack' with params " + track.toString());

		checkHeader(track);
		// validateInputRequestData(track);

		// User validation
		OAuth2Authentication authUser = getAuthUser(principal);
		if (authUser == null)
			throw new UserPrincipalNotFoundException("For the given principal no user was found...");

		// Save data
		track.setUserId(getUserIdFromAuthentication(principal));
		TrackEntity trackEntity = TrackMapper.mapTrackModelToEntity(track);

		// Set name if it is empty
		if (trackEntity.getName() == null) {
			trackEntity.setName(PREFIX_ACTIVITY_NAME + trackEntity.getId());
		}

		trackEntity = trackRepository.save(trackEntity);
		if (trackEntity == null)
			throw new ApplicationContextException("Error during creation of new track...");

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/v1/tracks/delete/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DeleteTrackResult> deleteTrack(@PathVariable Long id, @RequestBody Header headerData,
			Principal principal) throws UserPrincipalNotFoundException {

		LOG.info("Calling Controller 'deleteTrack' with id [" + id + "]");

		// TODO: Validate AppVersion
		checkHeader(headerData);
		// validateInputRequestData(headerData);

		// User validation
		OAuth2Authentication authUser = getAuthUser(principal);
		if (authUser == null)
			throw new UserPrincipalNotFoundException("For the given principal no user was found...");

		// Delete track
		try {
			trackRepository.deleteById(id);
		} catch (Exception exc) {
			LOG.error("Eror during deleteting track with id [" + id + "]");
			throw new TechnicalException(exc.getMessage(), exc);
		}

		DeleteTrackResult result = new DeleteTrackResult();
		result.setTrackId(id);
		return new ResponseEntity<DeleteTrackResult>(result, HttpStatus.OK);
	}

}
