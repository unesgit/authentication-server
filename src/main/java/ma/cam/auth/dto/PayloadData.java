package ma.cam.auth.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import lombok.Data;

@Data
public class PayloadData {

	private static final String GROUPS_LABEL = "GROUPS";
	private static final String WORKSTATION_ID = "WORKSTATION_ID";
	private static final String SITE_ID = "SITE_ID";

	@SuppressWarnings("unchecked")
	public PayloadData(Claims payload) {
		if (payload.containsKey(SITE_ID)) {
			this.siteCode = payload.get(SITE_ID).toString();
		}
		if (payload.containsKey(WORKSTATION_ID)) {
			this.workStation = payload.get(WORKSTATION_ID).toString();
		}
		if (payload.containsKey(GROUPS_LABEL) && payload.get(GROUPS_LABEL) != null) {
			this.groups = ((List<String>) payload.get(GROUPS_LABEL)).stream().map(String::toUpperCase)
					.collect(Collectors.toList());
		}
		this.userName = payload.getSubject();
	}

	private String siteCode;
	private List<String> groups = new ArrayList<>();
	private String workStation;
	private String userName;

}