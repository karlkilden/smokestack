package se.bm.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.deltaspike.core.util.ReflectionUtils;

@SuppressWarnings("unchecked")
public class ContextualMapper<K, T> {

	public static <K, T>Map<K, T> getContextualMap(Class<K> keyClazz, Class<T> clazz) {

		Object keyValue = null;
		Map<K, T> map = new HashMap<>();
		for (T t : BeanProvider.getContextualReferences(clazz, false)) {
			for (Method m : ReflectionUtils.getAllDeclaredMethods(clazz)) {
				if (m.getReturnType() == keyClazz) {
					try {
						keyValue = m.invoke(t, new Object[]{});
					} catch (Exception e) {
						ExceptionUtils.throwAsRuntimeException(e);
					}
					map.put((K) keyValue, t);
				}
			}

		}
		return map;
	}

}
