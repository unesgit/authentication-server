package ma.cam.kernal.data.dao;

import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ma.cam.kernal.constants.Constants;
import ma.cam.kernal.exceptions.TechnicalException;
import ma.cam.kernal.utils.ReflectionUtils;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

public final class DaoUtil {


//	static // avoid security issues
//	StringWriter buf = new StringWriter(4);
//	PrintWriter out = new PrintWriter(buf);
//	public static final String LINE_SEPARATOR = buf.toString();



	

	/**
	 * @param resultSet
	 * @param query
	 * @return
	 * @throws ServiceExecutionException
	 * @throws SQLException
	 */
	 static <T> List<T> resultSetToBeans(ResultSet resultSet, Class<T> clazz) throws Exception {
		List<T> beans = new ArrayList<>();
		int nombre = 0;
		boolean existe = false;
		try {
			ResultSetMetaData metaData = resultSet.getMetaData();
			while (resultSet.next()) {
				nombre++;
				T bean = resultToBean(resultSet, metaData, clazz);
				if (bean != null) {
					beans.add(bean);
				}
				if (nombre > 500 && !existe) {
					System.out.println(
							"#####################################################################################");
					System.out.println(
							"#####################################################################################");
					System.out.println(
							"#########################  Erreur de pagination  ####################################");
					System.out.println(
							"#####################################################################################");
					System.out.println("Class : " + clazz.toString());
					System.out.println(
							"#####################################################################################");
					existe = true;
				}
			}
			return beans;
		} catch (SQLException sqlex) {
			throw new SQLException(sqlex);
		} finally {
			resultSet.close();
		}
	}

	/**
	 * @param resultSet
	 * @param metaData
	 * @param query
	 * @return
	 * @throws Exception
	 * @throws ServiceExecutionException
	 * @throws SQLException
	 */
	@SuppressWarnings("deprecation")
	private static <T> T resultToBean(ResultSet resultSet, ResultSetMetaData metaData, Class<T> clazz)
			throws Exception {

		T bean = null;
		bean = clazz.newInstance();

		int colsCnt = 0;

		colsCnt = metaData.getColumnCount();

		for (int i = 1; i < colsCnt + 1; i++) {
			String column = null;

			column = metaData.getColumnName(i);
			column = getAttributeNameForObject(column);
			
//			if(column.indexOf(Constants.UNDERSCORE)>0) {
//				column = column.substring(0,column.indexOf(Constants.UNDERSCORE)).toLowerCase() + column.substring(column.indexOf(Constants.UNDERSCORE) + 1 ,column.indexOf(Constants.UNDERSCORE) + 2).toUpperCase() + column.substring(column.indexOf(Constants.UNDERSCORE) + 2 ,column.length()).toLowerCase();
//			}else {
//				column = column.toLowerCase();
//			}
			

			if (clazz != null) {
				Object value = null;
				Object lastBean = bean;

				value = resultSet.getObject(i);
				PropertyDescriptor disr = null;
				if (!"row_num".equalsIgnoreCase(column)) {
					try {
						disr = new PropertyDescriptor(column, clazz);
						Method getterMethod = disr.getReadMethod();
						Method setterMethod = disr.getWriteMethod();
						if (value != null) {
							setAttributeValue(lastBean, value, setterMethod, getterMethod.getReturnType());
						}
					} catch (Exception e) {
						throw new Exception(e);
					}
				}

			}
		}

		return bean;
	}

	private static Object executeMethod(Method method, Object target, Object[] args) throws Exception {
		int methodModifiers = method.getModifiers();
		if (!Modifier.isStatic(methodModifiers) && target == null) {
			return null;
		}
		return method.invoke(target, args);

	}

	private static boolean setAttributeValue(Object proxiedObject, Object value, Method attributeMethod,
			Class attributeClass) throws Exception {
		if (proxiedObject == null || value == null)
			return true;
		Class valueClass = value.getClass();
		if (attributeClass.equals(Boolean.class) || attributeClass.equals(Boolean.TYPE)) {
			if (valueClass.equals(java.math.BigInteger.class)) {
				int intValue = ((java.math.BigInteger) value).intValue();
				executeMethod(attributeMethod, proxiedObject, new Object[] { new Boolean(intValue == 1) });
			} else if (valueClass.equals(java.math.BigDecimal.class)) {
				int intValue = ((java.math.BigDecimal) value).intValue();
				executeMethod(attributeMethod, proxiedObject, new Object[] { new Boolean(intValue == 1) });
			} else if (valueClass.equals(String.class)) {
				executeMethod(attributeMethod, proxiedObject,
						new Object[] { new Boolean(value.equals("Y") || value.equals("T")) });
			} else {
				executeMethod(attributeMethod, proxiedObject,
						new Object[] { new Boolean(((Integer) value).intValue() == 1) });
			}
			return true;
		} else if (attributeClass.equals(String.class)) {
			if (valueClass.equals(CLOB.class)) {
				executeMethod(attributeMethod, proxiedObject, new Object[] { DaoUtil.toString((CLOB) value) });
			} else if (valueClass.equals(java.math.BigDecimal.class)) {
				int intValue = ((java.math.BigDecimal) value).intValue();
				executeMethod(attributeMethod, proxiedObject, new Object[] { String.valueOf(intValue) });
			} else
				executeMethod(attributeMethod, proxiedObject, new Object[] { cleanText((String) value )});

			return true;
		} else if (attributeClass.equals(Double.class) || attributeClass.equals(Double.TYPE)) {
			if (valueClass.equals(Float.class))
				executeMethod(attributeMethod, proxiedObject,
						new Object[] { new Double(((Float) value).doubleValue()) });
			else if (valueClass.equals(java.math.BigDecimal.class))
				executeMethod(attributeMethod, proxiedObject,
						new Object[] { new Double(((java.math.BigDecimal) value).doubleValue()) });
			else
				executeMethod(attributeMethod, proxiedObject, new Object[] { (Double) value });
			return true;
		} else if (attributeClass.equals(Float.class) || attributeClass.equals(Float.TYPE)) {
			if (valueClass.equals(Double.class))
				executeMethod(attributeMethod, proxiedObject,
						new Object[] { new Float(((Double) value).doubleValue()) });
			else if (valueClass.equals(java.math.BigDecimal.class))
				executeMethod(attributeMethod, proxiedObject,
						new Object[] { new Float(((java.math.BigDecimal) value).floatValue()) });
			else
				executeMethod(attributeMethod, proxiedObject, new Object[] { (Float) value });
			return true;
		} else if (attributeClass.equals(Integer.class) || attributeClass.equals(Integer.TYPE)) {
			if (valueClass.equals(Long.class))
				executeMethod(attributeMethod, proxiedObject, new Object[] { new Integer(((Long) value).intValue()) });
			else if (valueClass.equals(java.math.BigInteger.class))
				executeMethod(attributeMethod, proxiedObject,
						new Object[] { new Integer(((java.math.BigInteger) value).intValue()) });
			else if (valueClass.equals(java.math.BigDecimal.class))
				executeMethod(attributeMethod, proxiedObject,
						new Object[] { new Integer(((java.math.BigDecimal) value).intValue()) });
			else if (valueClass.equals(Short.class))
				executeMethod(attributeMethod, proxiedObject, new Object[] { new Integer(((Short) value).intValue()) });
			else
				executeMethod(attributeMethod, proxiedObject, new Object[] { (Integer) value });
			return true;
		} else if (attributeClass.equals(Short.class) || attributeClass.equals(Short.TYPE)) {
			if (valueClass.equals(Integer.class))
				executeMethod(attributeMethod, proxiedObject,
						new Object[] { new Short(((Integer) value).shortValue()) });
			else if (valueClass.equals(Long.class))
				executeMethod(attributeMethod, proxiedObject, new Object[] { new Short(((Long) value).shortValue()) });
			else
				executeMethod(attributeMethod, proxiedObject, new Object[] { (Short) value });
			return true;
		} else if (attributeClass.equals(Long.class) || attributeClass.equals(Long.TYPE)) {
			if (valueClass.equals(Integer.class))
				executeMethod(attributeMethod, proxiedObject, new Object[] { new Long(((Integer) value).longValue()) });
			else if (valueClass.equals(Short.class))
				executeMethod(attributeMethod, proxiedObject, new Object[] { new Long(((Short) value).longValue()) });
			else if (valueClass.equals(java.math.BigDecimal.class))
				executeMethod(attributeMethod, proxiedObject,
						new Object[] { new Long(((java.math.BigDecimal) value).longValue()) });
			else if (valueClass.equals(java.math.BigInteger.class))
				executeMethod(attributeMethod, proxiedObject,
						new Object[] { new Long(((java.math.BigInteger) value).longValue()) });
			else
				executeMethod(attributeMethod, proxiedObject, new Object[] { (Long) value });
			return true;
		} else if (attributeClass.equals(BigDecimal.class)) {
			if (valueClass.equals(BigDecimal.class))
				executeMethod(attributeMethod, proxiedObject, new Object[] { (BigDecimal) value });
			else
				executeMethod(attributeMethod, proxiedObject, new Object[] { value });
			return true;
		} else if (attributeClass.equals(java.util.Date.class)) {
			if (valueClass.equals(java.sql.Timestamp.class))
				executeMethod(attributeMethod, proxiedObject,
						new Object[] { new java.util.Date(((java.sql.Timestamp) value).getTime()) });
			else if (valueClass.equals(java.sql.Date.class))
				executeMethod(attributeMethod, proxiedObject, new Object[] { (java.sql.Date) value });
			else if (java.util.Calendar.class.isAssignableFrom(valueClass))
				executeMethod(attributeMethod, proxiedObject, new Object[] { ((java.util.Calendar) value).getTime() });
			else
				executeMethod(attributeMethod, proxiedObject, new Object[] { value });
			return true;
		} else if (attributeClass.equals(java.sql.Date.class)) {
			if (valueClass.equals(java.sql.Timestamp.class))
				executeMethod(attributeMethod, proxiedObject,
						new Object[] { new java.sql.Date(((java.sql.Timestamp) value).getTime()) });
			else if (valueClass.equals(java.util.Date.class))
				executeMethod(attributeMethod, proxiedObject,
						new Object[] { new java.sql.Date(((java.util.Date) value).getTime()) });
			else if (java.util.Calendar.class.isAssignableFrom(valueClass))
				executeMethod(attributeMethod, proxiedObject,
						new Object[] { new java.sql.Date(((java.util.Calendar) value).getTime().getTime()) });
			else
				executeMethod(attributeMethod, proxiedObject, new Object[] { value });
			return true;
		} else if (java.util.Calendar.class.isAssignableFrom(attributeClass)) {
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			if (valueClass.equals(java.sql.Timestamp.class)) {
				calendar.setTime(new java.util.Date(((java.sql.Timestamp) value).getTime()));
				executeMethod(attributeMethod, proxiedObject, new Object[] { calendar });
			} else if (valueClass.equals(java.util.Date.class)) {
				calendar.setTime((java.sql.Date) value);
				executeMethod(attributeMethod, proxiedObject, new Object[] { calendar });
			} else if (valueClass.equals(java.sql.Date.class)) {
				calendar.setTime(((java.sql.Date) value));
				executeMethod(attributeMethod, proxiedObject, new Object[] { calendar });
			} else {
				executeMethod(attributeMethod, proxiedObject, new Object[] { value });
			}

			return true;
		} else if (attributeClass.equals(byte[].class)) {

			if (valueClass.equals(Clob.class)) {
				executeMethod(attributeMethod, proxiedObject, new Object[] { clobToBytes((Clob) value) });
			} else if (valueClass.equals(Blob.class)) {

				executeMethod(attributeMethod, proxiedObject, new Object[] { blobToBytes((Blob) value) });
			} else if (valueClass.equals(char[].class)) {

				executeMethod(attributeMethod, proxiedObject, new Object[] { charsToBytes((char[]) value) });
			} else if (valueClass.equals(String.class)) {
				executeMethod(attributeMethod, proxiedObject, new Object[] { ((String) value).getBytes() });
			} else {
				executeMethod(attributeMethod, proxiedObject, new Object[] { (byte[]) value });
			}

			return true;
		} else if (attributeClass.equals(Blob.class)) {
			try {

				if (valueClass.equals(Clob.class))
					executeMethod(attributeMethod, proxiedObject, new Object[] { clobToBytes((Clob) value) });
				else if (valueClass.equals(Blob.class)) {
					executeMethod(attributeMethod, proxiedObject, new Object[] { blobToBytes((BLOB) value) });
				} else if (valueClass.equals(char[].class))
					executeMethod(attributeMethod, proxiedObject, new Object[] { charsToBytes((char[]) value) });
				else if (valueClass.equals(String.class))
					executeMethod(attributeMethod, proxiedObject, new Object[] { ((String) value).getBytes() });
				else
					executeMethod(attributeMethod, proxiedObject, new Object[] { value });
			} catch (Exception ex) {
				throw new Exception(ex);
			}
			return true;
		} else if (attributeClass.equals(char[].class)) {
			try {
				if (valueClass.equals(Blob.class))
					executeMethod(attributeMethod, proxiedObject, new Object[] { blobToChars((Blob) value) });
				else if (valueClass.equals(byte[].class))
					executeMethod(attributeMethod, proxiedObject, new Object[] { bytesToChars((byte[]) value) });
				else if (valueClass.equals(Clob.class))
					executeMethod(attributeMethod, proxiedObject, new Object[] { clobToChars((Clob) value) });
				else if (valueClass.equals(String.class))
					executeMethod(attributeMethod, proxiedObject, new Object[] { stringToChars((String) value) });
				else
					executeMethod(attributeMethod, proxiedObject, new Object[] { value });
			} catch (Exception ex) {
				throw new Exception(ex);
			}
			return true;
		} else if (attributeClass.equals(Object.class)) {
			executeMethod(attributeMethod, proxiedObject, new Object[] { value });
			return true;
		}
		return false;
	}

	private static String toString(Clob fromClob) throws Exception {
		StringBuffer baos = new StringBuffer();
		return toCharArrayImpl(fromClob, baos);
	}

	private static String toCharArrayImpl(Clob fromClob, StringBuffer baos) throws Exception {

		char[] buf = new char[4000 * 100];
		Reader is = null;
		try {
			is = fromClob.getCharacterStream();

			for (;;) {
				int dataSize = is.read(buf);
				if (dataSize == -1)
					break;

				baos.append(buf, 0, dataSize);
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ex) {
					throw new IOException(ex);
				}
			}
		}
		return baos.toString();
	}



	/**
	 * Clob to bytes.
	 *
	 * @param value the value
	 * @return the byte[]
	 * @throws BaseException the base exception
	 */
	private static byte[] clobToBytes(Clob value) throws Exception {
		return clobToBytes(value, 1, value.length());
	}

	/**
	 * Blob to bytes.
	 *
	 * @param value the value
	 * @return the byte[]
	 * @throws BaseException the base exception
	 */
	private static byte[] blobToBytes(Blob value) throws Exception {
		return blobToBytes(value, 1, value.length());
	}

	/**
	 * Clob to chars.
	 *
	 * @param value the value
	 * @return the char[]
	 * @throws BaseException the base exception
	 */
	private static char[] clobToChars(Clob value) throws Exception {
		return clobToChars(value, 1, value.length());
	}

	/**
	 * Blob to chars.
	 *
	 * @param value the value
	 * @return the char[]
	 * @throws BaseException the base exception
	 */
	private static char[] blobToChars(Blob value) throws Exception {
		return blobToChars(value, 1, value.length());
	}

	/**
	 * Clob to bytes.
	 *
	 * @param value  un object clob
	 * @param start  octet de départ
	 * @param length the length
	 * @return tableau d'octet
	 * @throws BaseException the base exception
	 */
	private static byte[] clobToBytes(Clob value, long start, long length) throws Exception {
		return value.getSubString(start, (int) length).getBytes();
	}

	/**
	 * Blob to bytes.
	 *
	 * @param value  un object blob
	 * @param start  octet de départ
	 * @param length the length
	 * @return tableau d'octet
	 * @throws BaseException the base exception
	 */
	private static byte[] blobToBytes(Blob value, long start, long length) throws Exception {
		return value.getBytes(start, (int) length);
	}

	/**
	 * Chars to bytes.
	 *
	 * @param value un tableau de caractères
	 * @return tableau d'octet
	 */
	private static byte[] charsToBytes(char[] value) {
		return new String(value).getBytes();
	}

	/**
	 * String to chars.
	 *
	 * @param value une chaine de caractéres
	 * @return tableau de caractères
	 */
	private static char[] stringToChars(String value) {
		char[] result = new char[value.length()];
		value.getChars(0, value.length(), result, 0);
		return result;
	}

	/**
	 * Clob to chars.
	 *
	 * @param value  un object clob
	 * @param start  octet de départ
	 * @param length the length
	 * @return tableau de caractères
	 * @throws BaseException the base exception
	 */
	private static char[] clobToChars(Clob value, long start, long length) throws Exception {
		String strValue;
		strValue = value.getSubString(start, (int) length);
		return stringToChars(strValue);
	}

	/**
	 * Blob to chars.
	 *
	 * @param value  un object clob
	 * @param start  octet de départ
	 * @param length the length
	 * @return tableau d'octets
	 * @throws BaseException the base exception
	 */
	private static char[] blobToChars(Blob value, long start, long length) throws Exception {
		String strValue;
		strValue = new String(value.getBytes(start, (int) length));
		return stringToChars(strValue);
	}

	/**
	 * Bytes to chars.
	 *
	 * @param value un tableau d'octets
	 * @return tableau de caractères
	 */
	private static char[] bytesToChars(byte[] value) {
		String strValue = new String(value);
		return stringToChars(strValue);
	}

	/**
	 * Input stream to bytes.
	 *
	 * @param is     flôt à convertir
	 * @param length longeur du flôt
	 * @return byte[] un tableau d'octets
	 * @throws BaseException the base exception
	 */
	private static byte[] inputStreamToBytes(InputStream is, long length) throws Exception {
		DataInputStream in = null;
		byte bytes[] = new byte[(int) length];
		int cnt = 0;
		in = new DataInputStream(new BufferedInputStream(is));
		while (in.available() != 0) {
			bytes[cnt++] = in.readByte();
		}
		return bytes;
	}

	/**
	 * Input stream to chars.
	 *
	 * @param is     flôt à convertir
	 * @param length longeur du flôt
	 * @return char[] un tableau de characters
	 * @throws BaseException the base exception
	 */
	private static char[] inputStreamToChars(InputStream is, long length) throws Exception {
		DataInputStream in = null;
		char chars[] = new char[(int) length];
		int cnt = 0;
		in = new DataInputStream(new BufferedInputStream(is));
		while (in.available() != 0) {
			chars[cnt++] = in.readChar();
		}
		return chars;
	}




	// lineIterator
	// -----------------------------------------------------------------------
	/**
	 * Return an Iterator for the lines in a <code>Reader</code>.
	 * <p>
	 * <code>LineIterator</code> holds a reference to the open <code>Reader</code>
	 * specified here. When you have finished with the iterator you should close the
	 * reader to free internal resources. This can be done by closing the reader
	 * directly, or by calling
	 *
	 * @param reader the <code>Reader</code> to read from, not null
	 * @return an Iterator of the lines in the reader, never null
	 *         {@link LineIterator#close()} or
	 *         {@link LineIterator#closeQuietly(LineIterator)}.
	 *         <p>
	 *         The recommended usage pattern is:
	 * 
	 *         <pre>
	 *         try {
	 *         	LineIterator it = IOUtils.lineIterator(reader);
	 *         	while (it.hasNext()) {
	 *         		String line = it.nextLine();
	 *         		/// do something with line
	 *         	}
	 *         } finally {
	 *         	IOUtils.closeQuietly(reader);
	 *         }
	 *         </pre>
	 * 
	 * @since Commons IO 1.2
	 */
	public static LineIterator lineIterator(Reader reader) {
		return new LineIterator(reader);
	}

	/**
	 * Return an Iterator for the lines in an <code>InputStream</code>, using the
	 * character encoding specified (or default encoding if null).
	 * <p>
	 * <code>LineIterator</code> holds a reference to the open
	 * <code>InputStream</code> specified here. When you have finished with the
	 * iterator you should close the stream to free internal resources. This can be
	 * done by closing the stream directly, or by calling
	 *
	 * @param input    the <code>InputStream</code> to read from, not null
	 * @param encoding the encoding to use, null means platform default
	 * @return an Iterator of the lines in the reader, never null
	 * @throws IOException if an I/O error occurs, such as if the encoding is
	 *                     invalid {@link LineIterator#close()} or
	 *                     {@link LineIterator#closeQuietly(LineIterator)}.
	 *                     <p>
	 *                     The recommended usage pattern is:
	 * 
	 *                     <pre>
	 *                     try {
	 *                     	LineIterator it = IOUtils.lineIterator(stream, &quot;UTF-8&quot;);
	 *                     	while (it.hasNext()) {
	 *                     		String line = it.nextLine();
	 *                     		/// do something with line
	 *                     	}
	 *                     } finally {
	 *                     	IOUtils.closeQuietly(stream);
	 *                     }
	 *                     </pre>
	 * 
	 * @since Commons IO 1.2
	 */
	public static LineIterator lineIterator(InputStream input, String encoding) throws IOException {
		Reader reader = null;
		if (encoding == null) {
			reader = new InputStreamReader(input);
		} else {
			reader = new InputStreamReader(input, encoding);
		}
		return new LineIterator(reader);
	}

	// -----------------------------------------------------------------------
	/**
	 * Convert the specified string to an input stream, encoded as bytes using the
	 * default character encoding of the platform.
	 * 
	 * @param input the string to convert
	 * @return an input stream
	 * @since Commons IO 1.1
	 */
	public static InputStream toInputStream(String input) {
		byte[] bytes = input.getBytes();
		return new ByteArrayInputStream(bytes);
	}

	/**
	 * Convert the specified string to an input stream, encoded as bytes using the
	 * specified character encoding.
	 * <p>
	 * Character encoding names can be found at
	 * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 *
	 * @param input    the string to convert
	 * @param encoding the encoding to use, null means platform default
	 * @return an input stream
	 * @throws IOException if the encoding is invalid
	 * @since Commons IO 1.1
	 */
	public static InputStream toInputStream(String input, String encoding) throws IOException {
		byte[] bytes = encoding != null ? input.getBytes(encoding) : input.getBytes();
		return new ByteArrayInputStream(bytes);
	}

	// write byte[]
	// -----------------------------------------------------------------------
	/**
	 * Writes bytes from a <code>byte[]</code> to an <code>OutputStream</code>.
	 *
	 * @param data   the byte array to write, do not modify during output, null
	 *               ignored
	 * @param output the <code>OutputStream</code> to write to
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static void write(byte[] data, OutputStream output) throws IOException {
		if (data != null) {
			output.write(data);
		}
	}

	/**
	 * Writes bytes from a <code>byte[]</code> to chars on a <code>Writer</code>
	 * using the default character encoding of the platform.
	 * <p>
	 * This method uses {@link String#String(byte[])}.
	 *
	 * @param data   the byte array to write, do not modify during output, null
	 *               ignored
	 * @param output the <code>Writer</code> to write to
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static void write(byte[] data, Writer output) throws IOException {
		if (data != null) {
			output.write(new String(data));
		}
	}

	/**
	 * Writes bytes from a <code>byte[]</code> to chars on a <code>Writer</code>
	 * using the specified character encoding.
	 * <p>
	 * Character encoding names can be found at
	 * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method uses {@link String#String(byte[], String)}.
	 *
	 * @param data     the byte array to write, do not modify during output, null
	 *                 ignored
	 * @param output   the <code>Writer</code> to write to
	 * @param encoding the encoding to use, null means platform default
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static void write(byte[] data, Writer output, String encoding) throws IOException {
		if (data != null) {
			if (encoding == null) {
				write(data, output);
			} else {
				output.write(new String(data, encoding));
			}
		}
	}

	// write char[]
	// -----------------------------------------------------------------------
	/**
	 * Writes chars from a <code>char[]</code> to a <code>Writer</code> using the
	 * default character encoding of the platform.
	 *
	 * @param data   the char array to write, do not modify during output, null
	 *               ignored
	 * @param output the <code>Writer</code> to write to
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static void write(char[] data, Writer output) throws IOException {
		if (data != null) {
			output.write(data);
		}
	}

	/**
	 * Writes chars from a <code>char[]</code> to bytes on an
	 * <code>OutputStream</code>.
	 * <p>
	 * This method uses {@link String#String(char[])} and
	 *
	 * @param data   the char array to write, do not modify during output, null
	 *               ignored
	 * @param output the <code>OutputStream</code> to write to
	 * @throws IOException if an I/O error occurs {@link String#getBytes()}.
	 * @since Commons IO 1.1
	 */
	public static void write(char[] data, OutputStream output) throws IOException {
		if (data != null) {
			output.write(new String(data).getBytes());
		}
	}

	/**
	 * Writes chars from a <code>char[]</code> to bytes on an
	 * <code>OutputStream</code> using the specified character encoding.
	 * <p>
	 * Character encoding names can be found at
	 * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method uses {@link String#String(char[])} and
	 *
	 * @param data     the char array to write, do not modify during output, null
	 *                 ignored
	 * @param output   the <code>OutputStream</code> to write to
	 * @param encoding the encoding to use, null means platform default
	 * @throws IOException if an I/O error occurs {@link String#getBytes(String)}.
	 * @since Commons IO 1.1
	 */
	public static void write(char[] data, OutputStream output, String encoding) throws IOException {
		if (data != null) {
			if (encoding == null) {
				write(data, output);
			} else {
				output.write(new String(data).getBytes(encoding));
			}
		}
	}

	// write String
	// -----------------------------------------------------------------------
	/**
	 * Writes chars from a <code>String</code> to a <code>Writer</code>.
	 *
	 * @param data   the <code>String</code> to write, null ignored
	 * @param output the <code>Writer</code> to write to
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static void write(String data, Writer output) throws IOException {
		if (data != null) {
			output.write(data);
		}
	}

	/**
	 * Writes chars from a <code>String</code> to bytes on an
	 * <code>OutputStream</code> using the default character encoding of the
	 * platform.
	 * <p>
	 * This method uses {@link String#getBytes()}.
	 *
	 * @param data   the <code>String</code> to write, null ignored
	 * @param output the <code>OutputStream</code> to write to
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static void write(String data, OutputStream output) throws IOException {
		if (data != null) {
			output.write(data.getBytes());
		}
	}

	/**
	 * Writes chars from a <code>String</code> to bytes on an
	 * <code>OutputStream</code> using the specified character encoding.
	 * <p>
	 * Character encoding names can be found at
	 * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method uses {@link String#getBytes(String)}.
	 *
	 * @param data     the <code>String</code> to write, null ignored
	 * @param output   the <code>OutputStream</code> to write to
	 * @param encoding the encoding to use, null means platform default
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static void write(String data, OutputStream output, String encoding) throws IOException {
		if (data != null) {
			if (encoding == null) {
				write(data, output);
			} else {
				output.write(data.getBytes(encoding));
			}
		}
	}

	// write StringBuffer
	// -----------------------------------------------------------------------
	/**
	 * Writes chars from a <code>StringBuffer</code> to a <code>Writer</code>.
	 *
	 * @param data   the <code>StringBuffer</code> to write, null ignored
	 * @param output the <code>Writer</code> to write to
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static void write(StringBuffer data, Writer output) throws IOException {
		if (data != null) {
			output.write(data.toString());
		}
	}

	/**
	 * Writes chars from a <code>StringBuffer</code> to bytes on an
	 * <code>OutputStream</code> using the default character encoding of the
	 * platform.
	 * <p>
	 * This method uses {@link String#getBytes()}.
	 *
	 * @param data   the <code>StringBuffer</code> to write, null ignored
	 * @param output the <code>OutputStream</code> to write to
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static void write(StringBuffer data, OutputStream output) throws IOException {
		if (data != null) {
			output.write(data.toString().getBytes());
		}
	}

	/**
	 * Writes chars from a <code>StringBuffer</code> to bytes on an
	 * <code>OutputStream</code> using the specified character encoding.
	 * <p>
	 * Character encoding names can be found at
	 * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method uses {@link String#getBytes(String)}.
	 *
	 * @param data     the <code>StringBuffer</code> to write, null ignored
	 * @param output   the <code>OutputStream</code> to write to
	 * @param encoding the encoding to use, null means platform default
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static void write(StringBuffer data, OutputStream output, String encoding) throws IOException {
		if (data != null) {
			if (encoding == null) {
				write(data, output);
			} else {
				output.write(data.toString().getBytes(encoding));
			}
		}
	}

	// writeLines
	// -----------------------------------------------------------------------
	/**
	 * Writes the <code>toString()</code> value of each item in a collection to an
	 * <code>OutputStream</code> line by line, using the default character encoding
	 * of the platform and the specified line ending.
	 *
	 * @param lines      the lines to write, null entries produce blank lines
	 * @param lineEnding the line separator to use, null is system default
	 * @param output     the <code>OutputStream</code> to write to, not null, not
	 *                   closed
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
//	public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output) throws IOException {
//		if (lines == null) {
//			return;
//		}
//		if (lineEnding == null) {
//			lineEnding = LINE_SEPARATOR;
//		}
//		for (Iterator<?> it = lines.iterator(); it.hasNext();) {
//			Object line = it.next();
//			if (line != null) {
//				output.write(line.toString().getBytes());
//			}
//			output.write(lineEnding.getBytes());
//		}
//	}

	/**
	 * Writes the <code>toString()</code> value of each item in a collection to an
	 * <code>OutputStream</code> line by line, using the specified character
	 * encoding and the specified line ending.
	 * <p>
	 * Character encoding names can be found at
	 * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 *
	 * @param lines      the lines to write, null entries produce blank lines
	 * @param lineEnding the line separator to use, null is system default
	 * @param output     the <code>OutputStream</code> to write to, not null, not
	 *                   closed
	 * @param encoding   the encoding to use, null means platform default
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
//	public static void writeLines(Collection lines, String lineEnding, OutputStream output, String encoding)
//			throws IOException {
//		if (encoding == null) {
//			writeLines(lines, lineEnding, output);
//		} else {
//			if (lines == null) {
//				return;
//			}
//			if (lineEnding == null) {
//				lineEnding = LINE_SEPARATOR;
//			}
//			for (Iterator it = lines.iterator(); it.hasNext();) {
//				Object line = it.next();
//				if (line != null) {
//					output.write(line.toString().getBytes(encoding));
//				}
//				output.write(lineEnding.getBytes(encoding));
//			}
//		}
//	}

	/**
	 * Writes the <code>toString()</code> value of each item in a collection to a
	 * <code>Writer</code> line by line, using the specified line ending.
	 *
	 * @param lines      the lines to write, null entries produce blank lines
	 * @param lineEnding the line separator to use, null is system default
	 * @param writer     the <code>Writer</code> to write to, not null, not closed
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
//	public static void writeLines(Collection lines, String lineEnding, Writer writer) throws IOException {
//		if (lines == null) {
//			return;
//		}
//		if (lineEnding == null) {
//			lineEnding = LINE_SEPARATOR;
//		}
//		for (Iterator<?> it = lines.iterator(); it.hasNext();) {
//			Object line = it.next();
//			if (line != null) {
//				writer.write(line.toString());
//			}
//			writer.write(lineEnding);
//		}
//	}

	
	/**
	 * Unconditionally close an <code>Reader</code>.
	 * <p>
	 * Equivalent to {@link Reader#close()}, except any exceptions will be ignored.
	 * This is typically used in finally blocks.
	 * 
	 * @param input the Reader to close, may be null or already closed
	 */
	 static void closeQuietly(Reader input) {
		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	/**
	 * Unconditionally close a <code>Writer</code>.
	 * <p>
	 * Equivalent to {@link Writer#close()}, except any exceptions will be ignored.
	 * This is typically used in finally blocks.
	 * 
	 * @param output the Writer to close, may be null or already closed
	 */
	 static void closeQuietly(Writer output) {
		try {
			if (output != null) {
				output.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	/**
	 * Unconditionally close an <code>InputStream</code>.
	 * <p>
	 * Equivalent to {@link InputStream#close()}, except any exceptions will be
	 * ignored. This is typically used in finally blocks.
	 * 
	 * @param input the InputStream to close, may be null or already closed
	 */
	 static void closeQuietly(InputStream input) {
		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	/**
	 * Unconditionally close an <code>OutputStream</code>.
	 * <p>
	 * Equivalent to {@link OutputStream#close()}, except any exceptions will be
	 * ignored. This is typically used in finally blocks.
	 * 
	 * @param output the OutputStream to close, may be null or already closed
	 */
	 static void closeQuietly(OutputStream output) {
		try {
			if (output != null) {
				output.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}
	
	// content equals
	// -----------------------------------------------------------------------
	/**
	 * Compare the contents of two Streams to determine if they are equal or not.
	 * <p>
	 * This method buffers the input internally using
	 * <code>BufferedInputStream</code> if they are not already buffered.
	 *
	 * @param input1 the first stream
	 * @param input2 the second stream
	 * @return true if the content of the streams are equal or they both don't
	 *         exist, false otherwise
	 * @throws IOException if an I/O error occurs
	 */
	public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
		if (!(input1 instanceof BufferedInputStream)) {
			input1 = new BufferedInputStream(input1);
		}
		if (!(input2 instanceof BufferedInputStream)) {
			input2 = new BufferedInputStream(input2);
		}

		int ch = input1.read();
		while (-1 != ch) {
			int ch2 = input2.read();
			if (ch != ch2) {
				return false;
			}
			ch = input1.read();
		}

		int ch2 = input2.read();
		return (ch2 == -1);
	}

	/**
	 * Compare the contents of two Readers to determine if they are equal or not.
	 * <p>
	 * This method buffers the input internally using <code>BufferedReader</code> if
	 * they are not already buffered.
	 *
	 * @param input1 the first reader
	 * @param input2 the second reader
	 * @return true if the content of the readers are equal or they both don't
	 *         exist, false otherwise
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static boolean contentEquals(Reader input1, Reader input2) throws IOException {
		if (!(input1 instanceof BufferedReader)) {
			input1 = new BufferedReader(input1);
		}
		if (!(input2 instanceof BufferedReader)) {
			input2 = new BufferedReader(input2);
		}

		int ch = input1.read();
		while (-1 != ch) {
			int ch2 = input2.read();
			if (ch != ch2) {
				return false;
			}
			ch = input1.read();
		}

		int ch2 = input2.read();
		return (ch2 == -1);
	}
	
	public static String getValueBinding(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			return (String) value;
		}
		if (value instanceof Long) {
			// if (Long.parseLong(value.toString()) == 0) {
			// return "0";
			// } else {
			return Constants.EMPTY_STRING + value;
			// }
		}
		if (value instanceof Double) {
			// if (Double.parseDouble(value.toString()) == 0) {
			// return null;
			// } else {
			final String returnedValue = Constants.EMPTY_STRING + value;
			return returnedValue.replace(".", ",");
			// }
		}
		if (value instanceof Integer) {
			if (Integer.parseInt(value.toString()) == 0) {
				return null;
			} else {
				return Constants.EMPTY_STRING + value;
			}
		}
		if (value instanceof Float) {
			if (Float.parseFloat(value.toString()) == 0) {
				return null;
			} else {
				final String returnedValue = Constants.EMPTY_STRING + value;
				return returnedValue.replace(".", ",");
			}
		}

		if (value instanceof BigDecimal) {
			// if (BigDecimal.parseFloat(value.toString()) == 0) {
			// return null;
			// } else {

			final String returnedValue = bigDecimalConverter((BigDecimal) value, "################.00000");
			return returnedValue.replace(".", ",");
			// }
		}

		if (value instanceof Date) {
			return Constants.format.format((Date) value);
		}

		if (value instanceof Boolean) {
			return ((Boolean) value).toString();
		}

		return null;
	}
	
	/**
	 * @param bdBigDecimal
	 * @param sPatern
	 * @return
	 */
	public static String bigDecimalConverter(BigDecimal bdBigDecimal, String sPatern) {

		if (bdBigDecimal != null) {
			DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
			decimalFormat.applyPattern(sPatern);

			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setDecimalSeparator(',');
			dfs.setGroupingSeparator('.');
			decimalFormat.setDecimalFormatSymbols(dfs);

			return decimalFormat.format(bdBigDecimal);
		} else {
			return "0";
		}
	}
	
	private static String getAttributeNameForObject(String inputString) {
		String outputString = "";
		char c; 

		for (int i = 0; i < inputString.length(); i++) {
		    c = inputString.charAt(i);
		    if(c== Constants.UNDERSCORE.charAt(0)) {
		    	i++;
		    	outputString += Character.toUpperCase(inputString.charAt(i));
		    }else {
		    	outputString += Character.toLowerCase(c);
		    }
		    
		}
		
		return outputString;
	}
	/**
	 * @param data
	 * @return
	 */
	private static String cleanText(String data) {
		if(data == null) {
			return null;
		}
		Pattern p = Pattern.compile("\\\\u(\\p{XDigit}{4})");
		Matcher m = p.matcher(data);
		StringBuffer buf = new StringBuffer(data.length());
		while (m.find()) {
		  String ch = String.valueOf((char) Integer.parseInt(m.group(1), 16));
		  m.appendReplacement(buf, Matcher.quoteReplacement(ch));
		}
		m.appendTail(buf);
		return buf.toString();
	}
	
	public static Object[][][] convertListMapsToArrayObject(List<Map<String, Object>> lMaps) {
		try {
			Object[][][] recObj = null;
			final int tailleL = lMaps.size();
			final int tailleR = getMaxSizeMaps(lMaps);
			recObj = new Object[tailleL][tailleR][2];

			for (int i = 0; i < tailleL; i++) {
				final Map<String, Object> resource = (Map<String, Object>) lMaps.get(i);
				Set<Entry<String, Object>> t = resource.entrySet();
				int j = 0;

				for (Entry<String, Object> e : t) {

					String tKey = e.getKey();
					final String cle = tKey.toUpperCase();
					String valeur = null;
					//
					final Object oValeur = resource.get(tKey);
					if (oValeur != null) {
						if (oValeur instanceof Double || oValeur instanceof BigDecimal || oValeur instanceof Float) {
							valeur = (Constants.EMPTY_STRING + oValeur).replace('.', ',');
						} else {
							valeur = Constants.EMPTY_STRING + oValeur;
						}

						if (tKey.startsWith("date_")) {
							if (e.getValue() != null) {
								if (resource.get(tKey) instanceof Date) {
									valeur = Constants.format.format((Date) resource.get(tKey));
								} else if (resource.get(tKey) instanceof String) {
									valeur = (String) resource.get(tKey);
								} else {
									valeur = Constants.EMPTY_STRING + resource.get(tKey);
								}
							} else {
								resource.put(tKey, Constants.EMPTY_STRING);
							}
						}

						if (tKey.startsWith("flag_")) {
							if (oValeur != null) {
								valeur = new Boolean(Constants.EMPTY_STRING + oValeur) ? "1" : "0";
							}
						}

						recObj[i][j][0] = cle;
						recObj[i][j][1] = valeur;
						j++;
					}
				}
			}

			return recObj;
		} catch (Exception e) {
			throw new TechnicalException(e.getMessage(), e);
		}
	}
	
	private static int getMaxSizeMaps(List<Map<String, Object>> resources) {
		int maxSize = 0;

		for (Map<String, Object> map : resources) {
			final int taille = ReflectionUtils.getSizeMap(map);
			if (taille > maxSize) {
				maxSize = taille;
			}
		}
		return maxSize;
	}
}
