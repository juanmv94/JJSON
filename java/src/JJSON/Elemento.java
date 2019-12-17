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
    
    private String string=null;
    private List<Elemento> vector=null;
    private Raiz raiz=null;
    private Boolean boleano=null;
    private Integer numero_entero=null;
    private Float numero=null;
    
    private char tipo=JJSON_Null;    
    
    public Elemento() {set_null();}
    public Elemento(String s, boolean escaped) {if (escaped) set_string(s); else set_unsc_string(s);}
    public Elemento(List<Elemento> a) {set_vector(a);}
    public Elemento(Raiz r){set_root(r);}
    public Elemento(boolean b){set_boolean(b);}
    public Elemento(int e) {set_integer(e);}
    public Elemento(float f) {set_float(f);}
    
    public void set_null() {clear();tipo=JJSON_Null;}
    public void set_string(String s) {clear(); string=s; tipo=JJSON_String;}
    public void set_unsc_string(String unsc_s) {clear(); string=JJSON.escape(unsc_s); tipo=JJSON_String;}
    public void set_vector(List<Elemento> a) {clear(); vector=a;tipo=JJSON_Vector;}
    public void set_root(Raiz r){clear(); raiz=r;tipo=JJSON_Root;}
    public void set_boolean(boolean b){clear(); boleano=b;tipo=JJSON_Boolean;}
    public void set_integer(int e) {clear(); numero_entero=e;tipo=JJSON_Integer;}
    public void set_float(float f) {clear(); numero=f;tipo=JJSON_Float;}
    
    public char get_tipo() {return tipo;}
    public String get_string() {return string;}
    public String get_unsc_string() {return JJSON.unescape(string);}
    public List<Elemento> get_vector() {return vector;}
    public Raiz get_root(){return raiz;}
    public boolean get_boolean(){return boleano;}
    public int get_integer() {return numero_entero;}
    public float get_float() {return numero;}
    
    public Elemento copy()
    {
        switch(tipo)
        {
            case JJSON_Float:
                return new Elemento(numero);
            case JJSON_Integer:
                return new Elemento(numero_entero);
            case JJSON_Boolean:
                return new Elemento(boleano);
            case JJSON_String:
                return new Elemento(string,true);
            case JJSON_Root:
                return new Elemento(raiz.copy());
            case JJSON_Vector:
                ArrayList<Elemento> arr=new ArrayList<>();
                for (int i=0;i<vector.size();i++)
                    arr.add(vector.get(i).copy());
                return new Elemento(arr);
            case JJSON_Null:
            default:
                return new Elemento();                
        }
    }
    
    private void clear()
    {
        switch(tipo)
        {
            case JJSON_Float:
                numero=null;
                return;
            case JJSON_Integer:
                numero_entero=null;
                return;
            case JJSON_Boolean:
                boleano=null;
                return;
            case JJSON_String:
                string=null;
                return;
            case JJSON_Root:
                raiz=null;
                return;
            case JJSON_Vector:
                vector=null;
                return;
            case JJSON_Null:
            default:
                return;                
        }
    }
    
    public String toString(boolean pretty, int identation) {
        switch(tipo)
        {
            case JJSON_Float:
                return numero.toString();
            case JJSON_Integer:
                return numero_entero.toString();
            case JJSON_Boolean:
                if (boleano)
                    return "true";
                else
                    return "false";
            case JJSON_String:
                return "\""+string+"\"";
            case JJSON_Root:
                return raiz.toString(pretty,identation);
            case JJSON_Vector:
                String res="[";
                if (vector.size()>0)
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
