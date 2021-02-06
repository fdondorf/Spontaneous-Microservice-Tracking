package org.spontaneous.service.tracking.trackmanagement.dataaccess.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Convert
public class JSONAttributeConverter implements AttributeConverter<List<TrackSegment>, byte []> {

	private static final Logger LOG = LoggerFactory.getLogger(JSONAttributeConverter.class);
	
	private final static ObjectMapper objectMapper = new ObjectMapper();
	
	 @Override
	 public byte [] convertToDatabaseColumn(List<TrackSegment> meta) {
		 if (meta == null) return null;
		 
		 byte [] dbData = null;
		 try {
			 dbData = objectMapper.writeValueAsBytes(meta);
			 return dbData;
		 } catch (JsonProcessingException ex) {
			 LOG.error("Unexpected JsonProcessingException encoding list to json...");
			 return null;
		 }
	 }

	 @Override
	 public List<TrackSegment> convertToEntityAttribute(byte [] dbData) {
		 if (dbData == null) return null;
		 
		 List<TrackSegment> list = new ArrayList<>();
		 try {
			list = Arrays.asList(objectMapper.readValue(dbData, TrackSegment[].class));
		    return list;
		 } catch (IOException ex) {
			 LOG.error("Unexpected IOEx decoding json from database: " + dbData);
		    return null;
		 }
	 }
}
