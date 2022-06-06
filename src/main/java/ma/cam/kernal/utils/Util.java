package ma.cam.kernal.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Date;

import ma.cam.kernal.constants.Constants;


public class Util {

    /**
     * @param champ
     * @param size
     * @return
     */
    public static String formatChamp(Object champ, int size) {
	String resultat = "";
	int taille;

	if (champ instanceof String) {
	    resultat = (String) champ;
	    taille = size - resultat.length();

	    for (int i = 0; i < taille; i++)
		resultat = "0" + resultat;
	}

	return resultat;
    }

    /**
     * @param value
     * @return
     */
    public static String getValueBinding(final Object value) {
	if (value == null) {
	    return null;
	}
	if (value instanceof String) {
	    return (String) value;
	}
	if (value instanceof Long) {
	    if (Long.parseLong(value.toString()) == 0) {
		return null;
	    } else {
		return Constants.EMPTY_STRING + value;
	    }
	}
	if (value instanceof Double) {
	    if (Double.parseDouble(value.toString()) == 0) {
		return null;
	    } else {
		return Constants.EMPTY_STRING + value;
	    }
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
		return Constants.EMPTY_STRING + value;
	    }
	}
	if (value instanceof Date) {
	    return  Constants.format.format((Date) value);
	}
	// if(value instanceof BigDecimal){
	// if(BigDecimal.parseFloat(value.toString()) == 0){
	// return null;
	// }else{
	// return Constants.EMPTY_STRING + value;
	// }
	// }
	return null;
    }

    /**
     * @param date
     * @param format
     * @return
     */
    public static String dateToArabicString(Date date) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);

	int iDay = cal.get(Calendar.DAY_OF_MONTH);
	int iMounth = cal.get(Calendar.MONTH) + 1;
	int iYear = cal.get(Calendar.YEAR);

	String sTextReturn = iDay + " ";

	switch (iMounth) {
	case 1:
	    sTextReturn += "\u064A\u0646\u0627\u064A\u0631";

	    break;

	case 2:
	    sTextReturn += "\u0641\u0628\u0631\u0627\u064A\u0631";

	    break;

	case 3:
	    sTextReturn += "\u0645\u0627\u0631\u0633";

	    break;

	case 4:
	    sTextReturn += "\u0623\u0628\u0631\u064A\u0644";

	    break;

	case 5:
	    sTextReturn += "\u0645\u0627\u064A";

	    break;

	case 6:
	    sTextReturn += "\u064A\u0648\u0646\u064A\u0648";

	    break;

	case 7:
	    sTextReturn += "\u064A\u0648\u0644\u064A\u0648\u0632";

	    break;

	case 8:
	    sTextReturn += "\u063A\u0634\u062A";

	    break;

	case 9:
	    sTextReturn += "\u0634\u062A\u0646\u0628\u0631";

	    break;

	case 10:
	    sTextReturn += "\u0623\u0643\u062A\u0648\u0628\u0631";

	    break;

	case 11:
	    sTextReturn += "\u0646\u0648\u0646\u0628\u0631";

	    break;

	case 12:
	    sTextReturn += "\u062F\u062C\u0646\u0628\u0631";

	    break;

	default:
	    break;
	}

	sTextReturn += (" " + iYear);

	return sTextReturn;
    }

    /**
     * @param date
     * @return
     */
    public static String dateFrenshToString(Date date) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);

	int iDay = cal.get(Calendar.DAY_OF_MONTH);
	int iMounth = cal.get(Calendar.MONTH) + 1;
	int iYear = cal.get(Calendar.YEAR);

	String sTextReturn = iDay + " ";

	switch (iMounth) {
	case 1:
	    sTextReturn += "janvier";

	    break;

	case 2:
	    sTextReturn += "fï¿½vrier";

	    break;

	case 3:
	    sTextReturn += "mars";

	    break;

	case 4:
	    sTextReturn += "avril";

	    break;

	case 5:
	    sTextReturn += "mai";

	    break;

	case 6:
	    sTextReturn += "juin";

	    break;

	case 7:
	    sTextReturn += "juillet";

	    break;

	case 8:
	    sTextReturn += "aoï¿½t";

	    break;

	case 9:
	    sTextReturn += "septembre";

	    break;

	case 10:
	    sTextReturn += "octobre";

	    break;

	case 11:
	    sTextReturn += "novembre";

	    break;

	case 12:
	    sTextReturn += "dï¿½cembre";

	    break;

	default:
	    break;
	}

	sTextReturn += (" " + iYear);

	return sTextReturn;
    }

    /**
     * @param o
     * @return
     */
    public static boolean isNull(Object o) {
	if (o == null) {
	    return true;
	}

	if (o instanceof String) {
	    String s = (String) o;

	    return (s.trim().equals(Constants.EMPTY_STRING) || s.trim().equalsIgnoreCase(Constants.NULL_STRING));
	}

	return false;
    }

    /**
     * @param fFloat
     * @param sPatern
     * @return
     */
    public static String floatConverter(Float fFloat, String sPatern) {
	if (fFloat != null) {
	    DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
	    decimalFormat.applyPattern(sPatern);

	    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	    dfs.setDecimalSeparator(',');
	    dfs.setGroupingSeparator('.');
	    decimalFormat.setDecimalFormatSymbols(dfs);

	    return decimalFormat.format(fFloat);
	} else {
	    return "0";
	}
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

    /**
     * @param iInteger
     * @param sPatern
     * @return
     */
    public static String integerConverter(Integer iInteger, String sPatern) {
	if (iInteger != null) {
	    DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
	    decimalFormat.applyPattern(sPatern);

	    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	    dfs.setDecimalSeparator(',');
	    dfs.setGroupingSeparator('.');
	    decimalFormat.setDecimalFormatSymbols(dfs);

	    return decimalFormat.format(iInteger);
	} else {
	    return "0";
	}
    }

    public static float round(float what, int howmuch) {
	return (float) (((int) ((what * Math.pow(10, howmuch)) + .5)) / Math.pow(10, howmuch));
    }
}
