package JJSON;

import java.util.ArrayList;

public class Raiz {
    public ArrayList<Nodo> nodos;
    
    public Raiz(ArrayList<Nodo> n) {nodos=n;}
    
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
        ArrayList<Nodo> arr=new ArrayList<>();
        for (int i=0;i<nodos.size();i++)
        {
            arr.add(nodos.get(i).copy());
        }
        return new Raiz(arr);
    }
    
    @Override
    public String toString() {
        String res="{";
        if (nodos.size()>0)
        {
            for (int i=0;i<nodos.size()-1;i++)
                res+=nodos.get(i).toString()+",";
            res+=nodos.get(nodos.size()-1).toString();
        }
        res+="}";
        return res;
    }
}
