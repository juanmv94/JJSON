package JJSON;

import java.util.ArrayList;
import java.util.List;

class JJSONparseData {
	public int position=0;
	public String json="";
	public boolean integers=false;
	
	JJSONparseData(String json) {
		this.json=json;
	}
	
	JJSONparseData(String json, boolean integers) {
		this.integers=integers;
		this.json=json;
	}
}

public class JJSON {
    public static String JJSON_Ident="  ";          //Space for pretty identation
    public static int JJSON_Bad_Int=0;              //Value returned for incorrect JSON number
    public static float JJSON_Bad_Float=0;          //Value returned for incorrect JSON number
    
    private static String getJSONstr(JJSONparseData pd)
    {
        int initpos=pd.position;
        try
        {
            while (pd.json.charAt(pd.position)!='"')
            {
                if (pd.json.charAt(pd.position)=='\\') pd.position+=2;
                else pd.position++;
            }
            return pd.json.substring(initpos, pd.position++);
        }
        catch(StringIndexOutOfBoundsException e)
        {
            return pd.json.substring(initpos, pd.position);
        }
    }
    
    private static int getJSONint(JJSONparseData pd)
    {
        int initpos=pd.position;
        char cur;
        try
        {
            do {
                cur=pd.json.charAt(++pd.position);
            }
            while (Character.isDigit(cur) || cur=='-' || cur=='+');
        }
        catch(StringIndexOutOfBoundsException e) {}
        try
        {
            return Integer.parseInt(pd.json.substring(initpos, pd.position));
        }
        catch(NumberFormatException e)
        {
            return JJSON_Bad_Int;
        }
    }
    
    private static float getJSONfloat(JJSONparseData pd)
    {
        int initpos=pd.position;
        char cur;
        try
        {
            do {
                cur=pd.json.charAt(++pd.position);
            }
            while (Character.isDigit(cur) || cur=='-' || cur=='+' || cur=='.');
        }
        catch(StringIndexOutOfBoundsException e) {}
        try
        {
            return Float.parseFloat(pd.json.substring(initpos, pd.position));
        }
        catch(NumberFormatException e)
        {
            return JJSON_Bad_Float;
        }
    }
    
    private static List<Elemento> getJSONvec(JJSONparseData pd)
    {
        List<Elemento> arr=new ArrayList<>();
        try
        {
            while (pd.json.charAt(pd.position)!=']')
            {
                arr.add(getJSON(pd));
                while ((pd.json.charAt(pd.position)!=',') && (pd.json.charAt(pd.position)!=']')) pd.position++;
            }
        }
        catch(StringIndexOutOfBoundsException e) {}
        return arr;   
    }
    
    private static Raiz getJSONraiz(JJSONparseData pd)
    {
        List<Nodo> nodos=new ArrayList<>();
        try
        {
            while (true)
            {
                switch(pd.json.charAt(pd.position))
                {
                    case '"':
                    	pd.position++;
                        String nombre=getJSONstr(pd);
                        while (pd.json.charAt(pd.position)!=':') pd.position++;
                        Elemento e=getJSON(pd);
                        nodos.add(new Nodo(nombre,e));
                        break;
                    case '}':
                    	pd.position++;
                        return new Raiz(nodos);
                    default:
                    	pd.position++;
                }
            }
        }
        catch(StringIndexOutOfBoundsException e)
        {
            return new Raiz(nodos);
        }
    }
    
    private static Elemento getJSON(JJSONparseData pd)
    {
        try
        {
            while (true)
            {
                switch(pd.json.charAt(pd.position))
                {
                    case '[':
                    	pd.position++;
                        return new Elemento(getJSONvec(pd));
                    case '{':
                    	pd.position++;
                        Raiz r=getJSONraiz(pd);
                        return new Elemento(r);
                    case '"':
                    	pd.position++;
                        return new Elemento(getJSONstr(pd),true);
                    case 't':
                    	pd.position+=4;
                        return new Elemento(true);
                    case 'f':
                    	pd.position+=5;
                        return new Elemento(false);
                    case 'n':
                    	pd.position+=4;
                        return new Elemento();
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                    case '-':
                    case '+':
                        if (pd.integers)
                            return new Elemento(getJSONint(pd));
                        else
                            return new Elemento(getJSONfloat(pd));
                    default:
                        pd.position++;
                }
            }
        }
        catch(StringIndexOutOfBoundsException e)
        {
            return new Elemento();
        }
    }
    
    public static Elemento parse(String json, boolean integers)
    {
        return getJSON(new JJSONparseData(json, integers));
    }
    
    public static Elemento parse(String json)
    {
    	return getJSON(new JJSONparseData(json));
    }
    
    public static String escape(String in)
    {
        String out = new String();
        for (int i=0; i<in.length(); i++)
        {
            switch (in.charAt(i))
            {
                case '\r':
                out+="\\r";
                break;
                case '\n':
                out+="\\n";
                break;
                case '\t':
                out+="\\t";
                break;
                case '\b':
                out+="\\b";
                break;
                case '\f':
                out+="\\f";
                break;
                case '\"':
                out+="\\\"";
                break;
                case '\\':
                out+="\\\\";
                break;
                default:
                out+=in.charAt(i);
            }
        }
        return out;
    }
    
    public static String unescape(String in)
    {
        String out = new String();
        boolean special=false;
        for (int i=0; i<in.length(); i++)
        {
            if (special)
            {
                switch (in.charAt(i))
                {
                    case 'r':
                    out+='\r';
                    break;
                    case 'n':
                    out+='\n';
                    break;
                    case 't':
                    out+='\t';
                    break;
                    case 'b':
                    out+='\b';
                    break;
                    case 'f':
                    out+='\f';
                    break;
                    default:
                    out+=in.charAt(i);
                }
                special=false;
            }
            else
            {
                if (in.charAt(i)=='\\')
                    special=true;
                else
                    out+=in.charAt(i);
            }
        }
        return out;
    }
}
