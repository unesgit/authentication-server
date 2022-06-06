package ma.cam.kernal.data.dao;

import java.io.InputStream;
import java.io.Serializable;

import oracle.jdbc.OracleTypes;

/**
 * @author olahrichi
 * 
 */
public class ParamPs implements Serializable {
	
	public static final String V_MESSAGE 	= "V_MESSAGE";
	public static final String V_TYPEMESSAGE = "V_TYPEMESSAGE";
	public static final String V_CRITERIA 	= "V_CRITERIA";
    /**
     * 
     */
    private static final long serialVersionUID = -871271754444013373L;
    private String name;
    /**
     * IN, OUT OR INOUT (@see SenseParam)
     */
    private String sens;// IN, OUT OR INOUT (@see SenseParam)
    /**
     * Ordre du Paramtre
     */
    private int order;
    /**
     * String, Int, Long, Double, CharacterStream, Date Or String (@see
     * TypeParam)
     */
    private String type;// String, Int, Long, Double, CharacterStream, Date Or
    // String (@see TypeParam)
    /**
     * Valeur du parametre
     */
    private Object value;
    //
    private InputStream stream;
    private int size;
    //
    private OracleTypes cursor;

    /**
     * @param name
     * @param sens
     * @param order
     * @param type
     * @param value
     */
    public ParamPs(String name, String sens, int order, String type, Object value) {
	super();
	this.name = name;
	this.sens = sens;
	this.order = order;
	this.type = type;
	this.value = value;
    }

    /**
     * @param name
     * @param sens
     * @param order
     * @param type
     * @param value
     * @param size
     */
    public ParamPs(String name, String sens, int order, String type, InputStream value, int size) {
	super();
	this.name = name;
	this.sens = sens;
	this.order = order;
	this.type = type;
	this.stream = value;
	this.size = size;
    }

    /**
     * @param name
     * @param sens
     * @param order
     * @param cursor
     * @param value
     */
    public ParamPs(String name, String sens, int order, OracleTypes cursor, Object value) {
	super();
	this.name = name;
	this.sens = sens;
	this.order = order;
	this.cursor = cursor;
	this.value = value;

    }

    // Getter && Setter

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the sens
     */
    public String getSens() {
	return sens;
    }

    /**
     * @param sens
     *            the sens to set
     */
    public void setSens(String sens) {
	this.sens = sens;
    }

    /**
     * @return the order
     */
    public int getOrder() {
	return order;
    }

    /**
     * @param order
     *            the order to set
     */
    public void setOrder(int order) {
	this.order = order;
    }

    /**
     * @return the type
     */
    public String getType() {
	return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
	this.type = type;
    }

    /**
     * @return the value
     */
    public Object getValue() {
	return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(Object value) {
	this.value = value;
    }

    /**
     * @return the stream
     */
    public InputStream getStream() {
	return stream;
    }

    /**
     * @param stream
     *            the stream to set
     */
    public void setStream(InputStream stream) {
	this.stream = stream;
    }

    /**
     * @return the size
     */
    public int getSize() {
	return size;
    }

    /**
     * @param size
     *            the size to set
     */
    public void setSize(int size) {
	this.size = size;
    }

    /**
     * @return the cursor
     */
    public OracleTypes getCursor() {
	return cursor;
    }

    /**
     * @param cursor
     *            the cursor to set
     */
    public void setCursor(OracleTypes cursor) {
	this.cursor = cursor;
    }
}
