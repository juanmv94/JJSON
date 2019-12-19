package JJSON;

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;

public class JJSONobj {
	public static Elemento parse(Object o) {
		if (o==null) return new Elemento();
		
		if (o instanceof String) {
			return new Elemento((String)o,false);
		} else if (o instanceof Character) {
			return new Elemento((Character)o);
		} else if (o instanceof Integer) {
			return new Elemento((Integer)o);
		} else if (o instanceof Long) {
			return new Elemento((Long)o);
		} else if (o instanceof Short) {
			return new Elemento(((Short)o).intValue());
		} else if (o instanceof Float) {
			return new Elemento(((Float)o));
		} else if (o instanceof Double) {
			return new Elemento((Double)o);
		} else if (o instanceof Boolean) {
			return new Elemento(((Boolean)o).booleanValue());
		} else if (o instanceof Collection<?>) {
			List<Elemento> l=new ArrayList<>();
			for (Object ob : (Collection)o) {
				l.add(parse(ob));
			}
			return new Elemento(l);
		} else if (o instanceof Object[]) {
			List<Elemento> l=new ArrayList<>();
			for (Object ob : (Object[])o) {
				l.add(parse(ob));
			}
			return new Elemento(l);
		} else if (o.getClass().isArray()) {
			try {
				List<Elemento> l=new ArrayList<>();
				Constructor<Elemento> c=Elemento.class.getConstructor(o.getClass().getComponentType());
				for (int i=0; i<Array.getLength(o); i++) {
					l.add(c.newInstance(Array.get(o, i)));
				}
				return new Elemento(l);
			} catch (NoSuchMethodException e) {
				return new Elemento();
			} catch (SecurityException e) {
				return new Elemento();
			} catch (ArrayIndexOutOfBoundsException e) {
				return new Elemento();
			} catch (InstantiationException e) {
				return new Elemento();
			} catch (IllegalAccessException e) {
				return new Elemento();
			} catch (IllegalArgumentException e) {
				return new Elemento();
			} catch (InvocationTargetException e) {
				return new Elemento();
			}
		}
		
		try {
			List<Nodo> nodos=new ArrayList<>();
			PropertyDescriptor[] pds=Introspector.getBeanInfo(o.getClass()).getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				if (pd.getName().equals("class")) continue;
				Method m=pd.getReadMethod();
				if(!m.isAccessible()) {
				      m.setAccessible(true);
				 }
				Object ob=m.invoke(o);
				nodos.add(new Nodo(pd.getName(),parse(ob)));
			}
			return new Elemento(new Raiz(nodos));
			
		} catch (IntrospectionException e) {
			return new Elemento();
		} catch (SecurityException e) {
			return new Elemento();
		} catch (IllegalAccessException e) {
			return new Elemento();
		} catch (IllegalArgumentException e) {
			return new Elemento();
		} catch (InvocationTargetException e) {
			return new Elemento();
		}
	}
}
