package JJSON;

public class Nodo {
    public String nombre;
    public Elemento elemento;
    
    public Nodo(String n, Elemento e) {nombre=n; elemento=e;}
    
    public Nodo copy()
    {
        return new Nodo(nombre,elemento.copy());
    }
    
    
    public String toString(boolean pretty, int identation) {
        String res="\""+nombre+"\":";
        if (pretty) res+=' ';
        res+=elemento.toString(pretty,identation);
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
