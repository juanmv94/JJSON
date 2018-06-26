package JJSON;

import java.util.ArrayList;

public class JJSON {
    
    private static int position;
    public static boolean JJSON_Integers=false;     //Read JSON numbers as integers
    
    private static String getJSONstr(String json)
    {
        int initpos=position;
        while (json.charAt(position)!='"')
        {
            if (json.charAt(position)=='\\') position+=2;
            else position++;
        }
        return json.substring(initpos, position++);
    }
    
    private static int getJSONint(String json)
    {
        int initpos=position;
        char cur;
        do {
            cur=json.charAt(++position);
        }
        while (Character.isDigit(cur) || cur=='-' || cur=='+');
        return Integer.parseInt(json.substring(initpos, position));
    }
    
    private static float getJSONfloat(String json)
    {
        int initpos=position;
        char cur;
        do {
            cur=json.charAt(++position);
        }
        while (Character.isDigit(cur) || cur=='-' || cur=='+' || cur=='.');
        return Float.parseFloat(json.substring(initpos, position));
    }
    
    private static ArrayList<Elemento> getJSONvec(String json)
    {
        ArrayList<Elemento> arr=new ArrayList<>();
        while (json.charAt(position)!=']')
        {
            arr.add(getJSON(json));
            while ((json.charAt(position)!=',') && (json.charAt(position)!=']')) position++;
        }
        return arr;   
    }
    
    private static Raiz getJSONraiz(String json)
    {
        ArrayList<Nodo> nodos=new ArrayList<>();
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
    
    private static Elemento getJSON(String json)
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
                    return new Elemento(getJSONstr(json));
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
    
    public static Elemento parse(String json)
    {
        position=0;
        return getJSON(json);
    }
}
