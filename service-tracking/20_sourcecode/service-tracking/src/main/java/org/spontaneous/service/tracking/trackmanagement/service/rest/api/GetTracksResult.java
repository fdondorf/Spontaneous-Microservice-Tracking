package org.spontaneous.service.tracking.trackmanagement.service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class GetTracksResult {

	private List<TrackTO> tracks;

	public List<TrackTO> getTracks() {
		return tracks;
	}

	public void setTracks(List<TrackTO> tracks) {
		this.tracks = tracks;
	}
	
	public boolean addTrack(TrackTO trackModel) {
		if (tracks == null) {
			tracks = new ArrayList<TrackTO>();
		}
		return tracks.add(trackModel);
	}
	
}
