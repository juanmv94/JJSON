package JJSON;

import java.util.ArrayList;
import java.util.List;

public class Elemento {
    public static final char JJSON_Null=0;
    public static final char JJSON_String=1;
    public static final char JJSON_Vector=2;
    public static final char JJSON_Root=3;
    public static final char JJSON_Boolean=4;
    public static final char JJSON_Integer=5;
    public static final char JJSON_Float=6;
    
    private Object ob=null;
    private char tipo=JJSON_Null;
    
    public Elemento() {set_null();}
    public Elemento(String s, boolean escaped) {if (escaped) set_string(s); else set_unsc_string(s);}
    public Elemento(char c) {set_char(c);}
    public Elemento(List<Elemento> a) {set_vector(a);}
    public Elemento(Raiz r){set_root(r);}
    public Elemento(boolean b){set_boolean(b);}
    public Elemento(long l) {set_long(l);}
    public Elemento(int i) {set_int(i);}
    public Elemento(short s) {set_short(s);}
    public Elemento(double d) {set_double(d);}
    public Elemento(float f) {set_float(f);}
    
    public void set_null() {ob=null;tipo=JJSON_Null;}
    public void set_string(String s) {ob=s; tipo=JJSON_String;}
    public void set_unsc_string(String unsc_s) {ob=JJSON.escape(unsc_s); tipo=JJSON_String;}
    public void set_char(char c) {ob=JJSON.escape(Character.toString(c)); tipo=JJSON_String;}
    public void set_vector(List<Elemento> a) {ob=a;tipo=JJSON_Vector;}
    public void set_root(Raiz r){ob=r;tipo=JJSON_Root;}
    public void set_boolean(boolean b){ob=b;tipo=JJSON_Boolean;}
    public void set_long(long l) {ob=l;tipo=JJSON_Integer;}
    public void set_int(int i) {ob=Long.valueOf(i);tipo=JJSON_Integer;}
    public void set_short(short s) {ob=Long.valueOf(s);tipo=JJSON_Integer;}
    public void set_double(double d) {ob=d;tipo=JJSON_Float;}
    public void set_float(float f) {ob=Double.valueOf(f);tipo=JJSON_Float;}
    
    public char get_tipo() {return tipo;}
    public String get_string() {return (String)ob;}
    public String get_unsc_string() {return JJSON.unescape((String)ob);}
    public List<Elemento> get_vector() {return (List<Elemento>)ob;}
    public Raiz get_root(){return (Raiz)ob;}
    public boolean get_boolean(){return (boolean)ob;}
    public long get_long() {return (long)ob;}
    public int get_integer() {return (int)(long)ob;}
    public double get_double() {return (double)ob;}
    public float get_float() {return (float)(double)ob;}
    
    public Elemento copy()
    {
        switch(tipo)
        {
            case JJSON_Float:
                return new Elemento((double)ob);
            case JJSON_Integer:
                return new Elemento((long)ob);
            case JJSON_Boolean:
                return new Elemento((boolean)ob);
            case JJSON_String:
                return new Elemento((String)ob,true);
            case JJSON_Root:
                return new Elemento(((Raiz)ob).copy());
            case JJSON_Vector:
                ArrayList<Elemento> arr=new ArrayList<>();
                List<Elemento> vector=(List<Elemento>)ob;
                for (int i=0;i<vector.size();i++)
                    arr.add(vector.get(i).copy());
                return new Elemento(arr);
            case JJSON_Null:
            default:
                return new Elemento();                
        }
    }
    
    public String toString(boolean pretty, int identation) {
        switch(tipo)
        {
            case JJSON_Float:
            case JJSON_Integer:
            case JJSON_Boolean:
                return ob.toString();
            case JJSON_String:
                return "\""+(String)ob+"\"";
            case JJSON_Root:
                return ((Raiz)ob).toString(pretty,identation);
            case JJSON_Vector:
            	List<Elemento> vector=(List<Elemento>)ob;
                String res="[";
                if (!vector.isEmpty())
                {
                    for (int i=0;i<vector.size()-1;i++) {
                        res+=vector.get(i).toString(pretty,identation)+",";
                        if (pretty) res+=' ';
                    }
                    res+=vector.get(vector.size()-1).toString(pretty,identation);
                }
                res+="]";
                return res;
            case JJSON_Null:
                return "null";
            default:
                return "";                
        }
    }
    
    public String toString(boolean pretty) {
    	return toString(pretty,0);
    }
    
    @Override
    public String toString() {
    	return toString(false);
    }
}
