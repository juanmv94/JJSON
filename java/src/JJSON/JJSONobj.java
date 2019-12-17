package JJSON;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JJSONobj {
	public static Elemento parse(Object o) {
		if (o==null) return new Elemento();
		
		if (o instanceof String) {
			return new Elemento((String)o,false);
		} else if (o instanceof Character) {
			return new Elemento(o.toString(),false);
		} else if (o.getClass()==char.class) {
			new Elemento(Character.toString((char)o),false);
		} else if (o instanceof Integer) {
			return new Elemento(((Integer)o).intValue());
		} else if (o instanceof Long) {
			return new Elemento(((Long)o).intValue());
		} else if (o instanceof Short) {
			return new Elemento(((Short)o).intValue());
		} else if (o.getClass()==int.class || o.getClass()==long.class || o.getClass()==short.class) {
			new Elemento((int)o);
		} else if (o instanceof Float) {
			return new Elemento(((Float)o).floatValue());
		} else if (o instanceof Double) {
			return new Elemento(((Double)o).floatValue());
		} else if (o.getClass()==float.class || o.getClass()==double.class) {
			return new Elemento((float)o);
		} else if (o instanceof Boolean) {
			return new Elemento(((Boolean)o).booleanValue());
		} else if (o.getClass()==boolean.class) {
			return new Elemento((boolean)o);
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
		}
		
		try {
			List<Nodo> nodos=new ArrayList<>();
			PropertyDescriptor[] pds=Introspector.getBeanInfo(o.getClass()).getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				if (pd.getName().equals("class")) continue;
				Method m=pd.getReadMethod();
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
