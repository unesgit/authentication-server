package ma.cam.kernal.converter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ConverterUtil {
	
	private ConverterUtil() {}

	
	/**
	 * The default buffer size to use.
	 */
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	

	// read toByteArray
	// -----------------------------------------------------------------------
	/**
	 * Get the contents of an <code>InputStream</code> as a <code>byte[]</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 *
	 * @param input the <code>InputStream</code> to read from
	 * @return the requested byte array
	 * @throws IOException if an I/O error occurs
	 */
	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	/**
	 * Get the contents of a <code>Reader</code> as a <code>byte[]</code> using the
	 * default character encoding of the platform.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 *
	 * @param input the <code>Reader</code> to read from
	 * @return the requested byte array
	 * @throws IOException if an I/O error occurs
	 */
	public static byte[] toByteArray(Reader input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	/**
	 * Get the contents of a <code>Reader</code> as a <code>byte[]</code> using the
	 * specified character encoding.
	 * <p>
	 * Character encoding names can be found at
	 * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 *
	 * @param input    the <code>Reader</code> to read from
	 * @param encoding the encoding to use, null means platform default
	 * @return the requested byte array
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static byte[] toByteArray(Reader input, String encoding) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output, encoding);
		return output.toByteArray();
	}

	// read char[]
	// -----------------------------------------------------------------------
	/**
	 * Get the contents of an <code>InputStream</code> as a character array using
	 * the default character encoding of the platform.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 *
	 * @param is the <code>InputStream</code> to read from
	 * @return the requested character array
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static char[] toCharArray(InputStream is) throws IOException {
		CharArrayWriter output = new CharArrayWriter();
		copy(is, output);
		return output.toCharArray();
	}

	/**
	 * Get the contents of an <code>InputStream</code> as a character array using
	 * the specified character encoding.
	 * <p>
	 * Character encoding names can be found at
	 * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 *
	 * @param is       the <code>InputStream</code> to read from
	 * @param encoding the encoding to use, null means platform default
	 * @return the requested character array
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static char[] toCharArray(InputStream is, String encoding) throws IOException {
		CharArrayWriter output = new CharArrayWriter();
		copy(is, output, encoding);
		return output.toCharArray();
	}

	/**
	 * Get the contents of a <code>Reader</code> as a character array.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 *
	 * @param input the <code>Reader</code> to read from
	 * @return the requested character array
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static char[] toCharArray(Reader input) throws IOException {
		CharArrayWriter sw = new CharArrayWriter();
		copy(input, sw);
		return sw.toCharArray();
	}

	// read toString
	// -----------------------------------------------------------------------
	/**
	 * Get the contents of an <code>InputStream</code> as a String using the default
	 * character encoding of the platform.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 *
	 * @param input the <code>InputStream</code> to read from
	 * @return the requested String
	 * @throws IOException if an I/O error occurs
	 */
	public static String toString(InputStream input) throws IOException {
		StringWriter sw = new StringWriter();
		copy(input, sw);
		return sw.toString();
	}

	/**
	 * Get the contents of an <code>InputStream</code> as a String using the
	 * specified character encoding.
	 * <p>
	 * Character encoding names can be found at
	 * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 *
	 * @param input    the <code>InputStream</code> to read from
	 * @param encoding the encoding to use, null means platform default
	 * @return the requested String
	 * @throws IOException if an I/O error occurs
	 */
	public static String toString(InputStream input, String encoding) throws IOException {
		StringWriter sw = new StringWriter();
		copy(input, sw, encoding);
		return sw.toString();
	}

	/**
	 * Get the contents of a <code>Reader</code> as a String.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 *
	 * @param input the <code>Reader</code> to read from
	 * @return the requested String
	 * @throws IOException if an I/O error occurs
	 */
	public static String toString(Reader input) throws IOException {
		StringWriter sw = new StringWriter();
		copy(input, sw);
		return sw.toString();
	}

	// -----------------------------------------------------------------------
	/**
	 * Get the contents of an <code>InputStream</code> as a list of Strings, one
	 * entry per line, using the default character encoding of the platform.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 *
	 * @param input the <code>InputStream</code> to read from, not null
	 * @return the list of Strings, never null
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static List<String> readLines(InputStream input) throws IOException {
		InputStreamReader reader = new InputStreamReader(input);
		return readLines(reader);
	}

	/**
	 * Get the contents of an <code>InputStream</code> as a list of Strings, one
	 * entry per line, using the specified character encoding.
	 * <p>
	 * Character encoding names can be found at
	 * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 *
	 * @param input    the <code>InputStream</code> to read from, not null
	 * @param encoding the encoding to use, null means platform default
	 * @return the list of Strings, never null
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static List<String> readLines(InputStream input, String encoding) throws IOException {
		if (encoding == null) {
			return readLines(input);
		} else {
			InputStreamReader reader = new InputStreamReader(input, encoding);
			return readLines(reader);
		}
	}

	/**
	 * Get the contents of a <code>Reader</code> as a list of Strings, one entry per
	 * line.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 *
	 * @param input the <code>Reader</code> to read from, not null
	 * @return the list of Strings, never null
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static List<String> readLines(Reader input) throws IOException {
		BufferedReader reader = new BufferedReader(input);
		List<String> list = new ArrayList<>();
		String line = reader.readLine();
		while (line != null) {
			list.add(line);
			line = reader.readLine();
		}
		return list;
	}
	
	// copy from InputStream
		// -----------------------------------------------------------------------
		/**
		 * Copy bytes from an <code>InputStream</code> to an <code>OutputStream</code>.
		 * <p>
		 * This method buffers the input internally, so there is no need to use a
		 * <code>BufferedInputStream</code>.
		 *
		 * @param input  the <code>InputStream</code> to read from
		 * @param output the <code>OutputStream</code> to write to
		 * @return the number of bytes copied
		 * @throws IOException if an I/O error occurs
		 * @since Commons IO 1.1
		 */
		private static int copy(InputStream input, OutputStream output) throws IOException {
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int count = 0;
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
				count += n;
				output.flush();
			}
			return count;
		}

		/**
		 * Copy bytes from an <code>InputStream</code> to chars on a <code>Writer</code>
		 * using the default character encoding of the platform.
		 * <p>
		 * This method buffers the input internally, so there is no need to use a
		 * <code>BufferedInputStream</code>.
		 * <p>
		 * This method uses {@link InputStreamReader}.
		 *
		 * @param input  the <code>InputStream</code> to read from
		 * @param output the <code>Writer</code> to write to
		 * @throws IOException if an I/O error occurs
		 * @since Commons IO 1.1
		 */
		private static void copy(InputStream input, Writer output) throws IOException {
			InputStreamReader in = new InputStreamReader(input);
			copy(in, output);
		}

		/**
		 * Copy bytes from an <code>InputStream</code> to chars on a <code>Writer</code>
		 * using the specified character encoding.
		 * <p>
		 * This method buffers the input internally, so there is no need to use a
		 * <code>BufferedInputStream</code>.
		 * <p>
		 * Character encoding names can be found at
		 * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
		 * <p>
		 * This method uses {@link InputStreamReader}.
		 *
		 * @param input    the <code>InputStream</code> to read from
		 * @param output   the <code>Writer</code> to write to
		 * @param encoding the encoding to use, null means platform default
		 * @throws IOException if an I/O error occurs
		 * @since Commons IO 1.1
		 */
		private static void copy(InputStream input, Writer output, String encoding) throws IOException {
			if (encoding == null) {
				copy(input, output);
			} else {
				InputStreamReader in = new InputStreamReader(input, encoding);
				copy(in, output);
			}
		}

		// copy from Reader
		// -----------------------------------------------------------------------
		/**
		 * Copy chars from a <code>Reader</code> to a <code>Writer</code>.
		 * <p>
		 * This method buffers the input internally, so there is no need to use a
		 * <code>BufferedReader</code>.
		 *
		 * @param input  the <code>Reader</code> to read from
		 * @param output the <code>Writer</code> to write to
		 * @return the number of characters copied
		 * @throws IOException if an I/O error occurs
		 * @since Commons IO 1.1
		 */
		private static int copy(Reader input, Writer output) throws IOException {
			char[] buffer = new char[DEFAULT_BUFFER_SIZE];
			int count = 0;
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
				count += n;
				output.flush();
			}
			return count;
		}

		/**
		 * Copy chars from a <code>Reader</code> to bytes on an
		 * <code>OutputStream</code> using the default character encoding of the
		 * platform, and calling flush.
		 * <p>
		 * This method buffers the input internally, so there is no need to use a
		 * <code>BufferedReader</code>.
		 * <p>
		 * Due to the implementation of OutputStreamWriter, this method performs a
		 * flush.
		 * <p>
		 * This method uses {@link OutputStreamWriter}.
		 *
		 * @param input  the <code>Reader</code> to read from
		 * @param output the <code>OutputStream</code> to write to
		 * @throws IOException if an I/O error occurs
		 * @since Commons IO 1.1
		 */
		private static void copy(Reader input, OutputStream output) throws IOException {
			OutputStreamWriter out = new OutputStreamWriter(output);
			copy(input, out);
			// XXX Unless anyone is planning on rewriting OutputStreamWriter, we
			// have to flush here.
			out.flush();
		}

		/**
		 * Copy chars from a <code>Reader</code> to bytes on an
		 * <code>OutputStream</code> using the specified character encoding, and calling
		 * flush.
		 * <p>
		 * This method buffers the input internally, so there is no need to use a
		 * <code>BufferedReader</code>.
		 * <p>
		 * Character encoding names can be found at
		 * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
		 * <p>
		 * Due to the implementation of OutputStreamWriter, this method performs a
		 * flush.
		 * <p>
		 * This method uses {@link OutputStreamWriter}.
		 *
		 * @param input    the <code>Reader</code> to read from
		 * @param output   the <code>OutputStream</code> to write to
		 * @param encoding the encoding to use, null means platform default
		 * @throws IOException if an I/O error occurs
		 * @since Commons IO 1.1
		 */
		private static void copy(Reader input, OutputStream output, String encoding) throws IOException {
			if (encoding == null) {
				copy(input, output);
			} else {
				OutputStreamWriter out = new OutputStreamWriter(output, encoding);
				copy(input, out);
				// XXX Unless anyone is planning on rewriting OutputStreamWriter,
				// we have to flush here.
				out.flush();
			}
		}


}
