package edu.umich.verdict.util;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.umich.verdict.VerdictConf;
import edu.umich.verdict.VerdictContext;

public class TypeCasting {
	
	public static Double toDouble(Object obj) {
		if (obj instanceof Double) return (Double) obj;
		else if (obj instanceof Float) return ((Float) obj).doubleValue();
		else if (obj instanceof BigDecimal) return ((BigDecimal) obj).doubleValue();
		else if (obj instanceof Long) return ((Long) obj).doubleValue();
		else if (obj instanceof Integer) return ((Integer) obj).doubleValue();
		else {
			VerdictLogger.error("Cannot convert to double, just return 0: " + obj.toString());
			return new Double(0);
		}
	}
	
	public static long toLongint(Object obj) {
		if (obj instanceof Double) return Math.round((Double) obj);
		else if (obj instanceof Float) return Math.round((Float) obj);
		else if (obj instanceof BigDecimal) return ((BigDecimal) obj).toBigInteger().longValue();
		else if (obj instanceof Long) return ((Long) obj);
		else if (obj instanceof Integer) return ((Integer) obj);
		else {
			VerdictLogger.error("Cannot convert to long value, just returnning 0: " + obj.toString());
			return 0;
		}
	}
	
	/**
	 * 
	 * @param type should be java.sql.Types
	 * @return
	 */
	public static boolean isTypeNumeric(int type) {
		if (type == java.sql.Types.BIGINT || type == java.sql.Types.DECIMAL || type == java.sql.Types.DOUBLE
				|| type == java.sql.Types.FLOAT || type == java.sql.Types.INTEGER || type == java.sql.Types.NUMERIC
				|| type == java.sql.Types.REAL || type == java.sql.Types.SMALLINT || type == java.sql.Types.TINYINT) {
			return true;
		} else {
			return false;
		}
	}
	
	private static final Map<Integer, String> typeInt2TypeName;
	static {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(java.sql.Types.BIGINT, "BIGINT");
		map.put(java.sql.Types.BOOLEAN, "BOOLEAN");
		map.put(java.sql.Types.CHAR, "CHAR");
		map.put(java.sql.Types.DATE, "DATE");
		map.put(java.sql.Types.DECIMAL, "DECIMAL");
		map.put(java.sql.Types.DOUBLE, "DOUBLE");
		map.put(java.sql.Types.FLOAT, "FLOAT");
		map.put(java.sql.Types.INTEGER, "INTEGER");
		map.put(java.sql.Types.NUMERIC, "NUMERIC");
		map.put(java.sql.Types.REAL, "REAL");
		map.put(java.sql.Types.SMALLINT, "SMALLINT");
		map.put(java.sql.Types.TIME, "TIME");
		map.put(java.sql.Types.TIMESTAMP, "TIMESTAMP");
		map.put(java.sql.Types.TINYINT, "TINYINT");
		map.put(java.sql.Types.VARCHAR, "VARCHAR");
		typeInt2TypeName = Collections.unmodifiableMap(map);
	}
	
	public static String dbDatatypeName(int type) {
		return typeInt2TypeName.get(type);
	}
	
	public static boolean isHiveFamily(String dbName) {
		return (dbName.equals("impala") || dbName.equals("hive"))? true : false;
	}
	
	public static String dbDatatypeNameWithDefaultParam(VerdictContext vc, int type) {
		String dbName = vc.getDbms().getName();
		String typename = dbDatatypeName(type);
		
		if (type == java.sql.Types.VARCHAR) {
			if (isHiveFamily(dbName)) {
				return typename;
			} else {
				return typename + "(255)";
			}
		}
		else if (type == java.sql.Types.DECIMAL) {
			if (isHiveFamily(dbName)) {
				return typename;
			} else {
				return typename + "(10,5)";
			}
		}
		else if (type == java.sql.Types.NUMERIC) {
			if (isHiveFamily(dbName)) {
				return typename;
			} else {
				return typename + "(10,5)";
			}
		}
		else if (type == java.sql.Types.INTEGER) {
			if (isHiveFamily(dbName)) {
				return typename;
			} else {
				return typename + "(10)";
			}
		}
		else {
			return typename;
		}
	}

}
