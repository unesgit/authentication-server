package ma.cam.kernal.data.dao;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.OracleConnection;

/**
 * 
 * @author LAHRICHI Otmane
 * 
 */
public class OracleNativeJdbcExtractor {

	private static final String TOMCAT_CONNECTION = "org.apache.tomcat.dbcp.dbcp.PoolableConnection";
	private static final String WEBSPHERE_CONNECTION = "com.ibm.ws.rsadapter.jdbc.WSJdbcConnection";
	private static final String WEBSPHERE_JDBC_UTIL = "com.ibm.ws.rsadapter.jdbc.WSJdbcUtil";
//    private static final String WEBSPHERE_CONNECTION_PROXY = "com.ibm.ejs.cm.proxy.ConnectionProxy";

	/**
	 * Retourne la connexion native Oracle sous-jacente au pool de connexion
	 * 
	 * @param poolConn La connexion du pool
	 * @return OracleConnection La connexion Oracle sous-jacente au pool
	 * @throws SQLException
	 * @throws DaoException
	 */
	@SuppressWarnings("rawtypes")
	public static OracleConnection getNativeOracleConnection(Connection poolConn) throws SQLException {
		OracleConnection oraConn = null;
		if (poolConn != null) {
			try {
				oraConn = (OracleConnection) poolConn.getMetaData().getConnection();
			} catch (ClassCastException e) {
				try {
					final Connection connection = poolConn.getMetaData().getConnection();
					final String sConnectionType = poolConn.getMetaData().getConnection().getClass().getName();
					if (WEBSPHERE_CONNECTION.equalsIgnoreCase(sConnectionType)) {
						Class WSJdbcUtil = Class.forName(WEBSPHERE_JDBC_UTIL);
						Class WSJdbcConnection = Class.forName(WEBSPHERE_CONNECTION);
						final Method method = WSJdbcUtil.getDeclaredMethod("getNativeConnection", WSJdbcConnection);
						if (method != null) {
							oraConn = (OracleConnection) method.invoke(WSJdbcUtil, connection);
						}
					} else if (TOMCAT_CONNECTION.equalsIgnoreCase(sConnectionType)) {
						final Method method = connection.getClass().getMethod("getDelegate", (Class[]) null);
						if (method != null) {
							oraConn = (OracleConnection) method.invoke(connection, (Object[]) null);
						}
					} else {
						return null;
					}
				} catch (Exception ex) {
					throw new SQLException();
				}
			}
		}
		return oraConn;
	}

}
