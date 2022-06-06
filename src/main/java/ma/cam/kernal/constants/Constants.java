package ma.cam.kernal.constants;

import java.text.SimpleDateFormat;

public class Constants {

	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 24 * 60 * 60l;
	public static final String SIGNING_KEY = "camprojet123r";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTHORITIES_KEY = "scopes";

	public static final String dateFormat = "dd/MM/yyyy";

	public static final String STRING = "S";// "String";
	public static final String MODE_SUPPRESSION = "S";// "String";
	public static final String INT = "I";// "Int";
	public static final String LONG = "L";// "Long";
	public static final String DOUBLE = "D";// "Double";
	public static final String BIG_DECIMAL = "BD";// "BigDecimal";
	public static final String CSTREAM = "CS";// "CharacterStream";
	public static final String DATE = "DT";// "Date";
	public static final String CURSOR = "CURSOR";// "String";
	public static final String O_ELEMENT = "O_ELEMENT";
	public static final String T_ELEMENT = "T_ELEMENT"; // Table Element
	public static final String C_ELEMENT = "C_ELEMENTS"; // Table Element
	//
	// Senses
	public static final String IN = "IN";
	public static final String OUT = "OUT";
	public static final String INOUT = "INOUT";

	public static final String TECH_ERROR = "Erreur technique";
	public static final String BUSINESS_ERROR = "Erreur fonctionelle";
	public static final String CURSOR_NAME = "CURSOR";
	public static final String COUNT_NAME = "COUNT";

	public static final String EMPTY_STRING = "";
	public static final String NULL_STRING = "null";

	public static final String FLAG_OUI = "O";
	public static final String FLAG_NON = "N";

	public static final SimpleDateFormat format = new SimpleDateFormat(dateFormat);

	public static final String UNDERSCORE = "_";
	public static final String STATUT_OK = "OK";
	public static final String STATUT_KO = "999";

	public static final String ERREUR_R = "R";

	public static final String MSG_SUCCESS = "Opération effectuée avec succès";
	public static final String MSG_ERREUR = "Opération échouée";

	public static final String REFRESH = "REFRESH";

	public static final String GED_DB_WKF = "WKF";
	public static final String INDEX_GED_TYPE_DOCUMENT = "1";
	public static final String INDEX_GED_WKF_DEMNADE_GED = "0";
	public static final String INDEX_GED_PROFIL_CREATION = "2";
	public static final String POINT_VIRGULE = ";";

	public static final String POST_REQUEST = "POST";
	public static final String GET_REQUEST = "GET";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final String AUTHORISATION = "application/json";
	public static final String BEARER = "Bearer ";

}
