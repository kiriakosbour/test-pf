package gr.deddie.pfr.utilities;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import javax.validation.ValidationException;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

public class Util {
    private static final String COOKIE_SEPARATOR = "|";
    private static final String passwordRegexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%])[0-9a-zA-Z!@#$%]{6,20}$";

    public static void validatePassword(String pass) throws ValidationException {
        if (pass == null || !pass.matches(passwordRegexp)) {
            throw new ValidationException("Invalid Password Format");
        }
    }

    public static String convertToUppercase(String str) {
        if (str == null) {
            return str;
        }
        return str.toUpperCase().replaceAll("Ά", "Α").replaceAll("Ή", "Η").replaceAll("Ό", "Ο").replaceAll("Ί", "Ι").replaceAll("Ώ", "Ω").replaceAll("Έ", "Ε").replaceAll("Ύ", "Υ");
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public boolean isChecksumValid(String data, byte[] checksum) throws Exception {
        byte[] chk1 = createChecksum(data);
        if (new String(checksum).equals(new String(chk1))) {
            return true;
        }
        return false;
    }

    public byte[] createChecksum(String data) throws Exception {
        MessageDigest complete = MessageDigest.getInstance("MD5");
        complete.update(data.getBytes("UTF-8"));
        return complete.digest();
    }

    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR) - 2000;
    }

     /**
     * crop and maybe reverse value
     */

    public static String cropAndReverseValue(String field, Integer maxChars, boolean reverse, String delimiter) {

        // in case the input field value is empty or null do nothing
        if (StringUtils.isBlank(field)) {
            return field;
        }

        if (field.length() > maxChars) {
            // endIndex is exclusive
            field = field.substring(0, maxChars + 1);
        }

        if (reverse) {
            // split to the parts
            String[] tokens = field.split(delimiter);

            if (tokens.length == 1) {
                return tokens[0];
            }

            StringBuilder updatedField = new StringBuilder();

            for (int i = tokens.length - 1; i >= 0; i--) {
                updatedField.append(tokens[i]).append(delimiter);
            }

            field = updatedField.toString().trim();
        }

        return field;
    }

    public static String convertEmptyValueToNull(String value) {

        if (value != null && value.trim().isEmpty()) {
            return null;
        }

        return value;
    }

    public static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            return ((String) value).trim().isEmpty();
        }
        if (value instanceof Collection) {
            return ((Collection<?>) value).isEmpty();
        }
        if (value instanceof Map) {
            return ((Map<?, ?>) value).isEmpty();
        }
        return value.getClass().isArray() && (Array.getLength(value) == 0);
    }

    public static boolean isNotEmpty(Object value) {
        return !isEmpty(value);
    }

    public static boolean doubleValidation(String value) {
        String reg = "([0-9]*)\\.([0-9]*)";

        if (isNotEmpty(value)) {
            return Pattern.compile(reg).matcher(value.trim()).matches();
        } else {
            return false;
        }
    }

    /**
     * checking if it's a character
     *
     * @param value
     *
     * @return
     */
    public static boolean isGreekOrLatinIncludingEmptyOrDashCharacter(String value) {
        String regex_is_input_valid = "^[A-Za-zΑ-ΩΫΪα-ωϋϊ\\-\\s]+$";

        if (isNotEmpty(value)) {
            return Pattern.compile(regex_is_input_valid).matcher(value.trim()).matches();
        } else {
            return false;
        }
    }

    /**
     * this method returns true if both dates are null or both dates are same dates in case it's required to throw exception in case of null dates, use
     * DateUtils of apache.common instead
     *
     * @param date1
     * @param date2
     *
     * @return
     */
    public static boolean isSameDate(Date date1, Date date2) {
        return date1 == null ? date2 == null : (date2 != null && date1.compareTo(date2) == 0);
    }

    public static boolean isSameDateIgnoringTime(Date date1, Date date2) {
        return date1 == null ? date2 == null : (date2 != null && DateUtils.isSameDay(date1, date2));
    }

    public static long[] stringToArrayLong(String s, String splitter) {
        String[] partsAsString = s.split(splitter);
        long[] partsAsLong = new long[partsAsString.length];
        for (int i = 0; i < partsAsLong.length; i++) {
            partsAsLong[i] = Long.parseLong(partsAsString[i]);
        }
        return partsAsLong;
    }

    /**
     * vaidates the email according to a pattern. Returns false if the email is empty or is not according to the pattern
     * @param email
     * @return
     */
    public static boolean isEmailValid(String email) {
        if (Util.isEmpty(email)) {
            return false;
        }
        String regex_email_pattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regex_email_pattern).matcher(email.trim()).matches();
    }
}
