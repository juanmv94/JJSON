package JJSON;

import java.util.ArrayList;
import java.util.List;

public class Raiz {
    public List<Nodo> nodos;
    
    public Raiz(List<Nodo> n) {nodos=n;}
    
    public Nodo find(String s)
    {
        for (int i=0;i<nodos.size();i++)
            if (nodos.get(i).nombre.equals(s))
                return nodos.get(i);        
        return null;
    }
    
    public Nodo remove(String s)
    {
        for (int i=0;i<nodos.size();i++)
            if (nodos.get(i).nombre.equals(s))
                return nodos.remove(i);
        return null;
    }
    
    public Raiz copy()
    {
        List<Nodo> arr=new ArrayList<>();
        for (int i=0;i<nodos.size();i++)
        {
            arr.add(nodos.get(i).copy());
        }
        return new Raiz(arr);
    }
    
    public String toString(boolean pretty, int identation) {
        String res="";
        if (pretty) {
        	if (identation>0) res+='\n';
        	for (int n=0;n<identation;n++) res+=JJSON.JJSON_Ident;
        	identation++;
        }
        res+="{";
        if (!nodos.isEmpty())
        {
            for (int i=0;i<nodos.size();i++) {
            	if (pretty) {
            		res+='\n';
                	for (int n=0;n<identation;n++) res+=JJSON.JJSON_Ident;
            	}
            	res+=nodos.get(i).toString(pretty,identation);
                if (i!=nodos.size()-1) res+=",";
            }
        }
        if (pretty) {
        	identation--;
        	res+='\n';
        	for (int n=0;n<identation;n++) res+=JJSON.JJSON_Ident;
        }
        res+="}";
        return res;
    }
    
    public String toString(boolean pretty) {
    	return toString(pretty,0);
    }
    
    @Override
    public String toString() {
    	return toString(false);
    }
}
