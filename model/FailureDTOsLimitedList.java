package gr.deddie.pfr.model;

import java.util.List;

public class FailureDTOsLimitedList {
	public static final Long FAILURES_LIST_MAX_SIZE = 20000L;

	private Boolean partial_response;
    private List<FailureDTO> announcements;

	public Boolean getPartial_response() {
		return partial_response;
	}
	public void setPartial_response(Boolean partial_response) {
		this.partial_response = partial_response;
	}
	public List<FailureDTO> getAnnouncements() {
		return announcements;
	}
	public void setAnnouncements(List<FailureDTO> announcements) {
		this.announcements = announcements;
	}

}
