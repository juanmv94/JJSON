package JJSON;

public class Nodo {
    public String nombre;
    public Elemento elemento;
    
    public Nodo(String n, Elemento e) {nombre=n; elemento=e;}
    
    public Nodo copy()
    {
        return new Nodo(nombre,elemento.copy());
    }
    
    @Override
    public String toString() {
        return "\""+nombre+"\":"+elemento.toString();
    }
}
