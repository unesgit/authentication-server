package ma.cam.kernal.data.dao;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.cam.kernal.constants.Constants;
import ma.cam.kernal.dto.result.MessageOracle;
import ma.cam.kernal.dto.result.QueryResult;
import ma.cam.kernal.utils.ReflectionUtils;
import ma.cam.kernal.utils.Util;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

/**
 * 
 * @author sabir.hadoudou
 *
 */
@Component
public class AbstractDAO {

	/**
	 * 
	 */
	public static final String COUNT = "COUNT";
	public static final String V_COUNT = "V_COUNT";

	@Autowired
	private DataSource datasource;

	public <T> QueryResult<T> executeQueryProcedure(String namePackage, String namePs, Class<T> jdo, List<?> params)
			throws Exception {

		String spackage = (namePackage != null && !namePackage.equals("")) ? namePackage : null;
		String sprocedure = (namePs != null && !namePs.equals("")) ? namePs : null;
		String sCallName = "";

		CallableStatement statement = null;
		StringBuilder inter = new StringBuilder("?");

		if (spackage == null && sprocedure == null) {
			throw new Exception("Veuillez donner au moins le nom du package ou de la procédure!");
		} else if (spackage == null) {
			sCallName = sprocedure;
		} else {
			sCallName = spackage + "." + sprocedure;
		}
		if (params == null || (params != null && params.isEmpty())) {
			inter = new StringBuilder("");
		} else {

			int size = 0;
			StringBuilder tab = new StringBuilder(",");
			for (int i = 0; i < params.size(); i++) {
				String j = String.valueOf(((ParamPs) params.get(i)).getOrder());
				if (tab.indexOf("," + j + ",") < 0) {
					size++;
					tab.append(j + ",");
				}
			}
			tab = null;
			for (int i = 1; i < size; i++) {
				inter.append(",?");
			}
		}
		String sql = "{call " + sCallName + "(" + inter + ")}";


		try (Connection conn = datasource.getConnection()) {
			OracleConnection oraCon = OracleNativeJdbcExtractor.getNativeOracleConnection(conn);
			statement = conn.prepareCall(sql);
			QueryResult<T> result = performeQueryProcedure(oraCon, statement, params, jdo);
			return result;
		}

	}

	@SuppressWarnings("unchecked")
	private <T> QueryResult<T> performeQueryProcedure(OracleConnection con, CallableStatement statement, List<?> params,
			Class<T> jdo) throws Exception {

		String retour[] = null;
		QueryResult<T> result = new QueryResult<>();
		int i = 0;
		if (params != null && !params.isEmpty()) {
			retour = new String[params.size()];
			Iterator<?> it = params.iterator();
			while (it.hasNext()) {
				ParamPs param = (ParamPs) it.next();
				if (param.getOrder() == 0)
					throw new Exception("L'ordre des Parametres est obligatoire!!");
				if (param.getType() == null) {

					throw new Exception("Type du Parametre :" + param.getOrder() + "non renseigné!!");

				} else if (param.getType().equalsIgnoreCase(Constants.STRING)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						statement.setString(param.getOrder(), (String) param.getValue());
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.VARCHAR);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}
				} else if (param.getType().equalsIgnoreCase(Constants.INT)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setInt(param.getOrder(), (Integer) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.LONG)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setLong(param.getOrder(), (Long) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}
				} else if (param.getType().equalsIgnoreCase(Constants.DOUBLE)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setDouble(param.getOrder(), (Double) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.BIG_DECIMAL)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setBigDecimal(param.getOrder(), (BigDecimal) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.DATE)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setDate(param.getOrder(),
									new java.sql.Date(((java.util.Date) param.getValue()).getTime()));
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.DATE);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.DATE);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}
				} else if (param.getType().equalsIgnoreCase(Constants.T_ELEMENT)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							Object[][] ro = null;
							ro = new Object[ReflectionUtils.getSizeMap((Map<String, Object>) param.getValue())][2];
							ro = ReflectionUtils.convertMapToArrayObject((Map<String, Object>) param.getValue());

							Array arr = con.createARRAY(Constants.T_ELEMENT, ro);

							statement.setArray(param.getOrder(), arr);
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.ARRAY, Constants.T_ELEMENT);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {

						throw new Exception("Sens du Paramétre :" + Constants.OUT + " avec type Param "
								+ Constants.T_ELEMENT + " non pris en charge");

					} else {
						throw new Exception("Sens du Paramétre :" + param.getOrder() + " non pris en charge !!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.CSTREAM)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setBinaryStream(param.getOrder(), ((InputStream) param.getValue()), 0);
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						throw new Exception("Sens du Parametre :" + Constants.OUT + " avec type Param "
								+ Constants.CSTREAM + " non pris en charge");
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge !!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.CURSOR)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setBinaryStream(param.getOrder(), ((InputStream) param.getValue()), 0);
						} else {
							statement.setNull(param.getOrder(), OracleTypes.CURSOR);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), OracleTypes.CURSOR);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge !!");
					}

				} else {
					throw new Exception(
							"Type du Parametre :" + param.getOrder() + "non pris en charge!! ( ," + Constants.STRING
									+ "," + Constants.INT + "," + Constants.LONG + "," + Constants.DOUBLE + ")");
				}
			}
		}
		statement.execute();
		if (i >= 2) {
			int fin = (retour[i - 1] != null) ? Integer.valueOf(retour[i - 1]).intValue() : 0;
			int avfin = (retour[i - 2] != null) ? Integer.valueOf(retour[i - 2]).intValue() : 0;
			String typeMsg = statement.getString(avfin);
			String msgTxt = statement.getString(fin);

			// if (typeMsg != null && !"".equalsIgnoreCase(typeMsg))
			// throw new Exception(msgTxt);

			result.setTypeMessage(typeMsg);
			result.setMessage(msgTxt);
			if (avfin >= 1) {
				for (int j = 0; j < i; j++) {
					int index = Integer.valueOf(retour[j]).intValue();
					ParamPs param = (ParamPs) params.get(index - 1);
					if (param != null
							&& (param.getName().equalsIgnoreCase(COUNT) || param.getName().equalsIgnoreCase(V_COUNT))) {
						int rtr = statement.getInt(index);
						result.setCount(rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.CURSOR)
							&& param.getName().equalsIgnoreCase(Constants.CURSOR_NAME)) {

						Object objet;
						try {
							objet = statement.getObject(index);
							if (objet != null) {
								ResultSet rtr = (ResultSet) objet;
								// Mapping des valeurs du CURSOR
								List<T> list = DaoUtil.resultSetToBeans(rtr, jdo);

								result.setList(list);
							}
						} catch (SQLException e) {
							if (e.getMessage().indexOf("Cursor is Closed") >= 0
									|| e.getMessage().toUpperCase().indexOf("CURSOR IS CLOSED") >= 0)
								;
							else
								throw e;
						}
					}
				}
			}
		}
		return result;

	}

	public MessageOracle executeProcedure(String namePackage, String namePs, List<?> params) throws Exception {

		String spackage = (namePackage != null && !namePackage.equals("")) ? namePackage : null;
		String sprocedure = (namePs != null && !namePs.equals("")) ? namePs : null;
		String sCallName = "";
		MessageOracle msgOracle = new MessageOracle();
		CallableStatement statement = null;
		String inter = "?";
		if (spackage == null && sprocedure == null) {
			throw new Exception("Veuillez donner au moins le nom du package ou de la procédure!");
		} else if (spackage == null) {
			sCallName = sprocedure;
		} else {
			sCallName = spackage + "." + sprocedure;
		}
		if (params == null || (params != null && params.isEmpty())) {
			inter = "";
		} else {
			int size = 0;
			StringBuffer tab = new StringBuffer(",");
			for (int i = 0; i < params.size(); i++) {
				String j = String.valueOf(((ParamPs) params.get(i)).getOrder());
				if (tab.indexOf("," + j + ",") < 0) {
					size++;
					tab.append(j + ",");
				}
			}
			tab = null;
			for (int i = 1; i < size; i++) {
				inter = inter + ",?";
			}
		}
		String sql = "{call " + sCallName + "(" + inter + ")}";

		try (Connection conn = datasource.getConnection()) {
			OracleConnection oraCon = OracleNativeJdbcExtractor.getNativeOracleConnection(conn);
			statement = conn.prepareCall(sql);

			msgOracle = performeProcedure(oraCon, statement, params);


			return msgOracle;
		}
	}

	@SuppressWarnings("unchecked")
	private MessageOracle performeProcedure(OracleConnection conn, CallableStatement statement, List<?> params)
			throws SQLException, Exception, ParseException {
		String retour[] = null;
		MessageOracle msgOracle = new MessageOracle();
		int i = 0;
		if (params != null && !params.isEmpty()) {
			retour = new String[params.size()];
			Iterator<?> it = params.iterator();
			while (it.hasNext()) {
				ParamPs param = (ParamPs) it.next();
				if (param.getOrder() == 0)
					throw new Exception("L'ordre des Parametres est obligatoire!!");
				if (param.getType() == null) {

					throw new Exception("Type du Parametre :" + param.getOrder() + "non renseigné!!");

				} else if (param.getType().equalsIgnoreCase(Constants.STRING)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						statement.setString(param.getOrder(), (String) param.getValue());

					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.VARCHAR);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {
						statement.setString(param.getOrder(), (String) param.getValue());
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.VARCHAR);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}
				} else if (param.getType().equalsIgnoreCase(Constants.INT)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setInt(param.getOrder(), (Integer) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setInt(param.getOrder(), (Integer) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.LONG)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setLong(param.getOrder(), Long.parseLong(param.getValue().toString()));
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setLong(param.getOrder(), (Long) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}
				} else if (param.getType().equalsIgnoreCase(Constants.DOUBLE)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setDouble(param.getOrder(), (Double) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setDouble(param.getOrder(), (Double) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.BIG_DECIMAL)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setBigDecimal(param.getOrder(), (BigDecimal) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setBigDecimal(param.getOrder(), (BigDecimal) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.DATE)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setDate(param.getOrder(),
									new java.sql.Date(((java.util.Date) param.getValue()).getTime()));
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.DATE);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.DATE);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setDate(param.getOrder(),
									new java.sql.Date(((java.util.Date) param.getValue()).getTime()));
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.DATE);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.DATE);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.T_ELEMENT)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							Object[][] ro = null;
							ro = new Object[ReflectionUtils.getSizeMap((Map<String, Object>) param.getValue())][2];
							ro = ReflectionUtils.convertMapToArrayObject((Map<String, Object>) param.getValue());

							// Get Oracle Connection
							Array arr = conn.createARRAY(Constants.T_ELEMENT, ro);
							statement.setArray(param.getOrder(), arr);
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.ARRAY, Constants.T_ELEMENT);
						i++;
					} else {
						throw new Exception("Sens du Paramétre :" + param.getOrder() + " non pris en charge !!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.C_ELEMENT)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							Object[][][] ro = null;
							final List<Map<String, Object>> lm = (List<Map<String, Object>>) param.getValue();
							if (lm.size() > 0) {
								final Map<String, Object> fElement = lm.get(0);
								ro = new Object[lm.size()][ReflectionUtils.getSizeMap(fElement)][2];
								ro = DaoUtil.convertListMapsToArrayObject(lm);
							}
							// Get Oracle Connection
							Array arr = conn.createARRAY(Constants.C_ELEMENT, ro);
							statement.setArray(param.getOrder(), arr);
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						/*
						 * throw new Exception("Sens du Paramétre :" + SenseParam.OUT +
						 * " avec type Param " + Constants.CSTREAM + " non pris en charge");
						 */
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.ARRAY, Constants.C_ELEMENT);
						i++;
					} else {
						throw new Exception("Sens du Paramétre :" + param.getOrder() + " non pris en charge !!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.CSTREAM)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setBinaryStream(param.getOrder(), param.getStream(), param.getSize());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						throw new Exception("Sens du Parametre :" + Constants.OUT + " avec type Param "
								+ Constants.CSTREAM + " non pris en charge");
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge !!");
					}

				} else {
					throw new Exception("Type du Parametre :" + param.getOrder() + "non pris en charge!! ( ,"
							+ Constants.STRING + "," + Constants.INT + "," + Constants.LONG + "," + Constants.DOUBLE
							+ "," + Constants.BIG_DECIMAL + ")");
				}
			}
		}
		// fin construction de l'appel avec parametres
		statement.execute();
		// recupérer les paramétres en sortie
		if (i >= 2) {

			int fin = (retour[i - 1] != null) ? Integer.valueOf(retour[i - 1]).intValue() : 0;
			int avfin = (retour[i - 2] != null) ? Integer.valueOf(retour[i - 2]).intValue() : 0;

			String typeMsg = statement.getString(avfin);
			String msgTxt = statement.getString(fin);
			msgOracle.setTypeMessage(typeMsg);
			msgOracle.setMessage(msgTxt);
			msgOracle.setRetours(new HashMap<String, Object>());
			if (avfin >= 2) {
				for (int j = 0; j < i; j++) {
					int index = Integer.valueOf(retour[j]).intValue();
					ParamPs param = (ParamPs) params.get(index - 1);
					if (param != null && param.getType().equalsIgnoreCase(Constants.STRING)) {
						String rtr = statement.getString(index);

						msgOracle.getRetours().put(param.getName(), rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.INT)) {
						int rtr = statement.getInt(index);
						msgOracle.getRetours().put(param.getName(), "" + rtr);

					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.LONG)) {
						long rtr = statement.getLong(index);
						msgOracle.getRetours().put(param.getName(), "" + rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.DOUBLE)) {
						double rtr = statement.getDouble(index);
						msgOracle.getRetours().put(param.getName(), "" + rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.BIG_DECIMAL)) {
						BigDecimal rtr = statement.getBigDecimal(index);
						msgOracle.getRetours().put(param.getName(), "" + rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.DATE)) {
						Date rtr = statement.getDate(index);
						msgOracle.getRetours().put(param.getName(), rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.T_ELEMENT)) {
						if (statement.getObject(index) != null) {
							Object[] rtr = (Object[]) ((Array) statement.getObject(index)).getArray();

							final Map<String, Object> result = ReflectionUtils.convertArrayObjectToMap(rtr);
							// msgOracle.getRetours().put(param.getName(),
							// result);
							msgOracle.setRetours(result);
						}
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.C_ELEMENT)) {
						if (statement.getObject(index) != null) {
							Object[] rtr = (Object[]) ((Array) statement.getObject(index)).getArray();

							final Map<String, Object> result = ReflectionUtils.convertArrayObjectToMap(rtr);
							// msgOracle.getRetours().put(param.getName(),
							// result);
							msgOracle.setRetours(result);
						}
					}
				}
			}
		}
		return msgOracle;

	}

	public int executeCountProcedure(String namePackage, String namePs, List<?> params) throws Exception {

		String count = "0";
		try {
			MessageOracle msgOracle = executeProcedure(namePackage, namePs, params);

			if (msgOracle.getTypeMessage() != null)
				throw new Exception(msgOracle.getMessage());

			count = (String) msgOracle.getRetours().get(Constants.COUNT_NAME);

		} catch (SQLException e) {
			throw new Exception(e);
		}

		return Integer.valueOf(count).intValue();
	}

	public MessageOracle executeProcedureWithCursors(String namePackage, String namePs, List<?> params,
			List<JDOMapping> jdos) throws Exception {

		String spackage = (namePackage != null && !namePackage.equals("")) ? namePackage : null;
		String sprocedure = (namePs != null && !namePs.equals("")) ? namePs : null;
		String sCallName = "";
		MessageOracle msgOracle = new MessageOracle();
		CallableStatement statement = null;
		StringBuilder inter = new StringBuilder("?");
		if (spackage == null && sprocedure == null) {
			throw new Exception("Veuillez donner au moins le nom du package ou de la procédure!");
		} else if (spackage == null) {
			sCallName = sprocedure;
		} else {
			sCallName = spackage + "." + sprocedure;
		}
		if (params == null || (params != null && params.isEmpty())) {
			inter = new StringBuilder("");
		} else {
			int size = 0;
			StringBuilder tab = new StringBuilder(",");
			for (int i = 0; i < params.size(); i++) {
				String j = String.valueOf(((ParamPs) params.get(i)).getOrder());
				if (tab.indexOf("," + j + ",") < 0) {
					size++;
					tab.append(j + ",");
				}
			}
			tab = null;
			for (int i = 1; i < size; i++) {
				inter.append(",?");
			}
		}
		String sql = "{call " + sCallName + "(" + inter + ")}";


		try (Connection conn = datasource.getConnection()) {

			OracleConnection oraCon = OracleNativeJdbcExtractor.getNativeOracleConnection(conn);
			statement = conn.prepareCall(sql);
			msgOracle = performeProcedureWithCursors(oraCon, statement, params, jdos);
			return msgOracle;
		}
	}

	@SuppressWarnings("unchecked")
	private MessageOracle performeProcedureWithCursors(OracleConnection conn, CallableStatement statement,
			List<?> params, List<JDOMapping> jdos) throws SQLException, Exception, ParseException {
		String retour[] = null;
		MessageOracle msgOracle = new MessageOracle();
		int i = 0;
		if (params != null && !params.isEmpty()) {
			retour = new String[params.size()];
			Iterator<?> it = params.iterator();
			while (it.hasNext()) {
				ParamPs param = (ParamPs) it.next();
				if (param.getOrder() == 0)
					throw new Exception("L'ordre des Parametres est obligatoire!!");
				if (param.getType() == null) {

					throw new Exception("Type du Parametre :" + param.getOrder() + "non renseigné!!");

				} else if (param.getType().equalsIgnoreCase(Constants.STRING)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						statement.setString(param.getOrder(), (String) param.getValue());

					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.VARCHAR);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {
						statement.setString(param.getOrder(), (String) param.getValue());
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.VARCHAR);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}
				} else if (param.getType().equalsIgnoreCase(Constants.INT)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setInt(param.getOrder(), (Integer) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setInt(param.getOrder(), (Integer) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.LONG)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setLong(param.getOrder(), Long.parseLong(param.getValue().toString()));
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setLong(param.getOrder(), (Long) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}
				} else if (param.getType().equalsIgnoreCase(Constants.DOUBLE)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setDouble(param.getOrder(), (Double) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setDouble(param.getOrder(), (Double) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.BIG_DECIMAL)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setBigDecimal(param.getOrder(), (BigDecimal) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setBigDecimal(param.getOrder(), (BigDecimal) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.DATE)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setDate(param.getOrder(),
									new java.sql.Date(((java.util.Date) param.getValue()).getTime()));
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.DATE);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.DATE);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setDate(param.getOrder(),
									new java.sql.Date(((java.util.Date) param.getValue()).getTime()));
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.DATE);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.DATE);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.T_ELEMENT)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							Object[][] ro = null;
							ro = new Object[ReflectionUtils.getSizeMap((Map<String, Object>) param.getValue())][2];
							ro = ReflectionUtils.convertMapToArrayObject((Map<String, Object>) param.getValue());

							// Get Oracle Connection
							Array arr = conn.createARRAY(Constants.T_ELEMENT, ro);
							statement.setArray(param.getOrder(), arr);
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.ARRAY, Constants.T_ELEMENT);
						i++;
					} else {
						throw new Exception("Sens du Paramétre :" + param.getOrder() + " non pris en charge !!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.C_ELEMENT)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							Object[][][] ro = null;
							final List<Map<String, Object>> lm = (List<Map<String, Object>>) param.getValue();
							if (lm.size() > 0) {
								final Map<String, Object> fElement = lm.get(0);
								ro = new Object[lm.size()][ReflectionUtils.getSizeMap(fElement)][2];
								ro = DaoUtil.convertListMapsToArrayObject(lm);
							}
							// Get Oracle Connection
							Array arr = conn.createARRAY(Constants.C_ELEMENT, ro);
							statement.setArray(param.getOrder(), arr);
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						/*
						 * throw new Exception("Sens du Paramétre :" + SenseParam.OUT +
						 * " avec type Param " + Constants.CSTREAM + " non pris en charge");
						 */
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.ARRAY, Constants.C_ELEMENT);
						i++;
					} else {
						throw new Exception("Sens du Paramétre :" + param.getOrder() + " non pris en charge !!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.CSTREAM)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setBinaryStream(param.getOrder(), param.getStream(), param.getSize());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						throw new Exception("Sens du Parametre :" + Constants.OUT + " avec type Param "
								+ Constants.CSTREAM + " non pris en charge");
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge !!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.CURSOR)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setBinaryStream(param.getOrder(), ((InputStream) param.getValue()), 0);
						} else {
							statement.setNull(param.getOrder(), OracleTypes.CURSOR);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), OracleTypes.CURSOR);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge !!");
					}

				} else {
					throw new Exception("Type du Parametre :" + param.getOrder() + "non pris en charge!! ( ,"
							+ Constants.STRING + "," + Constants.INT + "," + Constants.LONG + "," + Constants.DOUBLE
							+ "," + Constants.BIG_DECIMAL + ")");
				}
			}
		}
		// fin construction de l'appel avec parametres
		statement.execute();
		// recupérer les paramétres en sortie
		if (i >= 2) {

			int fin = (retour[i - 1] != null) ? Integer.valueOf(retour[i - 1]).intValue() : 0;
			int avfin = (retour[i - 2] != null) ? Integer.valueOf(retour[i - 2]).intValue() : 0;

			String typeMsg = statement.getString(avfin);
			String msgTxt = statement.getString(fin);
			msgOracle.setTypeMessage(typeMsg);
			msgOracle.setMessage(msgTxt);
			msgOracle.setRetours(new HashMap<String, Object>());
			if (avfin >= 2) {
				for (int j = 0; j < i; j++) {
					int index = Integer.valueOf(retour[j]).intValue();
					ParamPs param = (ParamPs) params.get(index - 1);
					if (param != null && param.getType().equalsIgnoreCase(Constants.STRING)) {
						String rtr = statement.getString(index);

						msgOracle.getRetours().put(param.getName(), rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.INT)) {
						int rtr = statement.getInt(index);
						msgOracle.getRetours().put(param.getName(), "" + rtr);

					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.LONG)) {
						long rtr = statement.getLong(index);
						msgOracle.getRetours().put(param.getName(), "" + rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.DOUBLE)) {
						double rtr = statement.getDouble(index);
						msgOracle.getRetours().put(param.getName(), "" + rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.BIG_DECIMAL)) {
						BigDecimal rtr = statement.getBigDecimal(index);
						msgOracle.getRetours().put(param.getName(), "" + rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.DATE)) {
						Date rtr = statement.getDate(index);
						msgOracle.getRetours().put(param.getName(), rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.T_ELEMENT)) {
						if (statement.getObject(index) != null) {
							Object[] rtr = (Object[]) ((Array) statement.getObject(index)).getArray();

							final Map<String, Object> result = ReflectionUtils.convertArrayObjectToMap(rtr);
							// msgOracle.getRetours().put(param.getName(),
							// result);
							msgOracle.setRetours(result);
						}
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.C_ELEMENT)) {
						if (statement.getObject(index) != null) {
							Object[] rtr = (Object[]) ((Array) statement.getObject(index)).getArray();

							final Map<String, Object> result = ReflectionUtils.convertArrayObjectToMap(rtr);
							// msgOracle.getRetours().put(param.getName(),
							// result);
							msgOracle.setRetours(result);
						}
					}
					//
					if (param != null && param.getType().equalsIgnoreCase(Constants.CURSOR)) {

						Object objet;
						try {
							objet = statement.getObject(index);
							if (objet != null) {
								ResultSet rtr = (ResultSet) objet;
								// Mapping des valeurs du CURSOR
								if (!Util.isNull(jdos)) {
									for (JDOMapping jdo : jdos) {
										if (param.getOrder() == jdo.getIndex()) {
											List<?> list = DaoUtil.resultSetToBeans(rtr, jdo.getJdo());
											msgOracle.getRetours().put(param.getName(), list);
											break;
										}
									}
								}
							}
						} catch (SQLException e) {
							if (e.getMessage().indexOf("Cursor is Closed") >= 0
									|| e.getMessage().toUpperCase().indexOf("CURSOR IS CLOSED") >= 0)
								;
							else
								throw e;
						}
					}
				}
			}
		}
		return msgOracle;

	}

}
