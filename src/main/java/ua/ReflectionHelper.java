package ua;

import java.lang.reflect.Field;

public class ReflectionHelper {
	
	public Object GetPrivateField(String name, Class<?> fieldClass, Object instance)
	{
		try
		{
			Field unknownField = fieldClass.getDeclaredField(name);
			
			unknownField.setAccessible(true);
			
			return unknownField.get(instance);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
