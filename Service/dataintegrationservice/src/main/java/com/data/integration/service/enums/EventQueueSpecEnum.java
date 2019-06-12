package com.data.integration.service.enums;

public enum EventQueueSpecEnum {
	EVENT_TO_TRIGGER("EventToTrigger"),
	START("START"),
	REFRESH_USERPROFILE_JSON("REFRESH_USERPROFILE_JSON"),
	EVENT_INPUT_DATA("EventInputData"),
	ASYNC("Async"),
	UN_ZIP("unzip");
	private String key;

	private EventQueueSpecEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
