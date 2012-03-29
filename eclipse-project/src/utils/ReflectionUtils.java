package utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtils {
	static void setFinalStatic(Field field, String newValue) throws Exception {
		field.setAccessible(true);

		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		Field stringValue = String.class.getDeclaredField("value");
	    stringValue.setAccessible(true);
	    stringValue.set(field.get(null), newValue.toCharArray());
	}

	public static void setFinalStatic(Class classe, String fieldname, String newValue) throws SecurityException, NoSuchFieldException, Exception {
		setFinalStatic(classe.getDeclaredField(fieldname), newValue);
	}
}
