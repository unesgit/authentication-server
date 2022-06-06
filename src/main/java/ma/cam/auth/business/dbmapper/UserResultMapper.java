package ma.cam.auth.business.dbmapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import ma.cam.auth.dto.result.authentication.ApplicationResult;
import ma.cam.auth.dto.result.authentication.InfoUserResult;
import ma.cam.auth.dto.result.authentication.ProfilResult;
import ma.cam.auth.dto.result.authentication.UserResult;

public class UserResultMapper implements Function<Map<String, Object>, Optional<UserResult>> {

	@SuppressWarnings("unchecked")
	@Override
	public Optional<UserResult> apply(Map<String, Object> retours) {
		return Optional.ofNullable(
				UserResult.builder().userInfoResult(((List<InfoUserResult>) retours.get("V_INFOUSER")).get(0))//
						.applicationResultList((List<ApplicationResult>) retours.get("V_APPLICATION"))//
						.profilResultList((List<ProfilResult>) retours.get("V_PROFILS")).build());
	}

}
