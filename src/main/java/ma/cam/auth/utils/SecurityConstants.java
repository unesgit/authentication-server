package ma.cam.auth.utils;

/**
 * This class is used for indicating constants used for serurity.
 *
 * @author Achraf Talbi
 */
public abstract class SecurityConstants {
	
	

	private SecurityConstants() { }

	
	
	public static final String AUTHENTICATION_URL = "/api/auth/**";
    public static final String ACTUATOR_URL = "/actuator";
    public static final String AUTHENTICATION_HEADER_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String DETAILS_SITECODE = "DETAILS_SITECODE";
    public static final String JWT = "JWT";
    public static final int MAX_TIMEOUT_CALL = 10;
    
    
}
