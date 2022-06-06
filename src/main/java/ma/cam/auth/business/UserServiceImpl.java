package ma.cam.auth.business;

import static ma.cam.kernal.data.dao.ParamPs.V_CRITERIA;
import static ma.cam.kernal.data.dao.ParamPs.V_MESSAGE;
import static ma.cam.kernal.data.dao.ParamPs.V_TYPEMESSAGE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import ma.cam.auth.business.dbmapper.UserResultMapper;
import ma.cam.auth.dto.result.authentication.ApplicationResult;
import ma.cam.auth.dto.result.authentication.InfoUserResult;
import ma.cam.auth.dto.result.authentication.ProfilResult;
import ma.cam.auth.dto.result.authentication.UserResult;
import ma.cam.kernal.constants.Constants;
import ma.cam.kernal.data.dao.JDOMapping;
import ma.cam.kernal.data.dao.ParamPs;
import ma.cam.kernal.service.AbstractService;

@Service
public class UserServiceImpl extends AbstractService {

	public UserResult findByUserNameAndPassword(String username, String password) throws UsernameNotFoundException {

		int i = 1;

		List<ParamPs> params = new ArrayList<>();
		List<JDOMapping> jdos = new ArrayList<>();

		Map<String, Object> mapCriteria = new HashMap<>();
		mapCriteria.put("login", username);
		// ad017e9d6654bd0ccd978958fa1b6da6

		mapCriteria.put("motdepasse", DigestUtils.md5DigestAsHex(password.getBytes()));

		params.add(new ParamPs(V_CRITERIA, Constants.IN, i++, Constants.T_ELEMENT, mapCriteria));

		params.add(new ParamPs("V_INFOUSER", Constants.OUT, i++, Constants.CURSOR, null));
		jdos.add(new JDOMapping(i - 1, InfoUserResult.class));
		params.add(new ParamPs("V_APPLICATION", Constants.OUT, i++, Constants.CURSOR, null));
		jdos.add(new JDOMapping(i - 1, ApplicationResult.class));
		params.add(new ParamPs("V_PROFILS", Constants.OUT, i++, Constants.CURSOR, null));
		jdos.add(new JDOMapping(i - 1, ProfilResult.class));

		params.add(new ParamPs(V_TYPEMESSAGE, Constants.OUT, i++, Constants.STRING, Constants.EMPTY_STRING));
		params.add(new ParamPs(V_MESSAGE, Constants.OUT, i++, Constants.STRING, Constants.EMPTY_STRING));

		return findWithMapping("PK_CBK_HABILITATION", "PR_GET_USER_BY_CREDENTIALS", params, jdos,
				new UserResultMapper());

	}

}