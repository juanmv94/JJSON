package JJSON;

import java.util.ArrayList;

public class JJSON {
    
    private static int position;
    public static boolean JJSON_Integers=false;     //Read JSON numbers as integers
    public static int JJSON_Bad_Int=0;              //Value returned for incorrect JSON number
    public static float JJSON_Bad_Float=0;          //Value returned for incorrect JSON number
    
    private static String getJSONstr(String json)
    {
        int initpos=position;
        try
        {
            while (json.charAt(position)!='"')
            {
                if (json.charAt(position)=='\\') position+=2;
                else position++;
            }
            return json.substring(initpos, position++);
        }
        catch(StringIndexOutOfBoundsException e)
        {
            return json.substring(initpos, position);
        }
    }
    
    private static int getJSONint(String json)
    {
        int initpos=position;
        char cur;
        try
        {
            do {
                cur=json.charAt(++position);
            }
            while (Character.isDigit(cur) || cur=='-' || cur=='+');
        }
        catch(StringIndexOutOfBoundsException e) {}
        try
        {
            return Integer.parseInt(json.substring(initpos, position));
        }
        catch(NumberFormatException e)
        {
            return JJSON_Bad_Int;
        }
    }
    
    private static float getJSONfloat(String json)
    {
        int initpos=position;
        char cur;
        try
        {
            do {
                cur=json.charAt(++position);
            }
            while (Character.isDigit(cur) || cur=='-' || cur=='+' || cur=='.');
        }
        catch(StringIndexOutOfBoundsException e) {}
        try
        {
            return Float.parseFloat(json.substring(initpos, position));
        }
        catch(NumberFormatException e)
        {
            return JJSON_Bad_Float;
        }
    }
    
    private static ArrayList<Elemento> getJSONvec(String json)
    {
        ArrayList<Elemento> arr=new ArrayList<>();
        try
        {
            while (json.charAt(position)!=']')
            {
                arr.add(getJSON(json));
                while ((json.charAt(position)!=',') && (json.charAt(position)!=']')) position++;
            }
        }
        catch(StringIndexOutOfBoundsException e) {}
        return arr;   
    }
    
    private static Raiz getJSONraiz(String json)
    {
        ArrayList<Nodo> nodos=new ArrayList<>();
        try
        {
            while (true)
            {
                switch(json.charAt(position))
                {
                    case '"':
                        position++;
                        String nombre=getJSONstr(json);
                        while (json.charAt(position)!=':') position++;
                        Elemento e=getJSON(json);
                        nodos.add(new Nodo(nombre,e));
                        break;
                    case '}':
                        position++;
                        return new Raiz(nodos);
                    default:
                        position++;
                }
            }
        }
        catch(StringIndexOutOfBoundsException e)
        {
            return new Raiz(nodos);
        }
    }
    
    private static Elemento getJSON(String json)
    {
        try
        {
            while (true)
            {
                switch(json.charAt(position))
                {
                    case '[':
                        position++;
                        return new Elemento(getJSONvec(json));
                    case '{':
                        position++;
                        Raiz r=getJSONraiz(json);
                        return new Elemento(r);
                    case '"':
                        position++;
                        return new Elemento(getJSONstr(json),true);
                    case 't':
                        position+=4;
                        return new Elemento(true);
                    case 'f':
                        position+=5;
                        return new Elemento(false);
                    case 'n':
                        position+=4;
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
                        if (JJSON.JJSON_Integers)
                            return new Elemento(getJSONint(json));
                        else
                            return new Elemento(getJSONfloat(json));
                    default:
                        position++;
                }
            }
        }
        catch(StringIndexOutOfBoundsException e)
        {
            return new Elemento();
        }
    }
    
    public static Elemento parse(String json)
    {
        position=0;
        return getJSON(json);
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
