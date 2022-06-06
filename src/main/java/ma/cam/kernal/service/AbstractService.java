package ma.cam.kernal.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import ma.cam.kernal.constants.Constants;
import ma.cam.kernal.data.dao.AbstractDAO;
import ma.cam.kernal.data.dao.JDOMapping;
import ma.cam.kernal.dto.result.MessageOracle;
import ma.cam.kernal.dto.result.QueryResult;
import ma.cam.kernal.exceptions.BusinessException;
import ma.cam.kernal.exceptions.NotFoundException;
import ma.cam.kernal.exceptions.TechnicalException;

@Service

public class AbstractService {

	protected static final String AUNCUN_ELEMENT_TROUVE = "Auncun élément trouvé";
	@Autowired
	private AbstractDAO abstractDAO;

	public <T> T findOne(String namePackage, String namePs, Class<T> jdo, List<?> params) {
		try {
			QueryResult<T> queryResult = abstractDAO.executeQueryProcedure(namePackage, namePs, jdo, params);
			if (Constants.ERREUR_R.equals(queryResult.getTypeMessage())) {
				throw new BusinessException(queryResult.getMessage());
			}
			List<T> list = queryResult.getList();
			if (!CollectionUtils.isEmpty(list)) {
				return list.get(0);
			}

			throw new NotFoundException(AUNCUN_ELEMENT_TROUVE);

		} catch (Exception e) {
			// log.error
			throw new TechnicalException(e.getMessage(), e);
		}

	}

	public <T> List<T> find(String namePackage, String namePs, Class<T> jdo, List<?> params) {
		try {
			QueryResult<T> queryResult = abstractDAO.executeQueryProcedure(namePackage, namePs, jdo, params);
			if (Constants.ERREUR_R.equals(queryResult.getTypeMessage())) {
				throw new BusinessException(queryResult.getMessage());
			}
//			List<T> list = queryResult.getList();
//			if (!CollectionUtils.isEmpty(list)) {
//				return list;
//			}

			// throw new NotFoundException(AUNCUN_ELEMENT_TROUVE);

			return queryResult.getList();

		} catch (Exception e) {
			// log.error
			throw new TechnicalException(e.getMessage(), e);
		}

	}

	public <T> T findWithMapping(String namePackage, String namePs, List<?> params, List<JDOMapping> jdos,
			Function<Map<String, Object>, Optional<T>> messageOracleMapper) {
		MessageOracle msgOracle;
		try {
			msgOracle = abstractDAO.executeProcedureWithCursors(namePackage, namePs, params, jdos);
		} catch (Exception e) {
			// log.error
			throw new TechnicalException(e.getMessage(), e);
		}
		if (Constants.ERREUR_R.equals(msgOracle.getTypeMessage())) {
			throw new BusinessException(msgOracle.getMessage());
		} else {
			return messageOracleMapper.apply(msgOracle.getRetours()).map(t -> t)
					.orElseThrow(() -> new NotFoundException(AUNCUN_ELEMENT_TROUVE));
		}

	}

	public String executeWriteProcedure(String namePackage, String namePs, List<?> params) {
		try {
			MessageOracle msgOracle = abstractDAO.executeProcedure(namePackage, namePs, params);
			if (Constants.ERREUR_R.equals(msgOracle.getTypeMessage())) {
				throw new BusinessException(msgOracle.getMessage());
			}
			return msgOracle.getMessage();
		} catch (Exception e) {
			// log.error
			throw new TechnicalException(e.getMessage(), e);
		}
	}

	public <T> T executeWriteProcedureAndGet(String namePackage, String namePs, List<?> params, String returnParamName,
			Class<T> returnParamClass) {
		try {
			MessageOracle msgOracle = abstractDAO.executeProcedure(namePackage, namePs, params);
			if (Constants.ERREUR_R.equals(msgOracle.getTypeMessage())) {
				throw new BusinessException(msgOracle.getMessage());
			}
			return returnParamClass.cast(msgOracle.getRetours().get(returnParamName));
		} catch (Exception e) {
			// log.error
			throw new TechnicalException(e.getMessage(), e);
		}
	}
}
