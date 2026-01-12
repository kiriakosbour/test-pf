package gr.deddie.pfr.model;

import java.util.List;

public class PowerOutageDTOsLimitedList {
	public static final Long OUTAGES_LIST_MAX_SIZE = 20000L;

	private Boolean partial_response;
    private List<PowerOutage> poweroutages;
    
	public Boolean getPartial_response() {
		return partial_response;
	}
	public void setPartial_response(Boolean partial_response) {
		this.partial_response = partial_response;
	}

	public List<PowerOutage> getPoweroutages() {
		return poweroutages;
	}

	public void setPoweroutages(List<PowerOutage> poweroutages) {
		this.poweroutages = poweroutages;
	}
}
