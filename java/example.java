package jsonjava;
import JJSON.*;
import java.util.ArrayList;

class JSONjava {
    
    public static void main(String[] args) {
        JJSON.JJSON_Integers=true;                                                  //Leemos los números del JSON como enteros
        String in="{ \"palabras\":[\"hola\",\"mundo\",\"lorem\",\"ipsum\"],\"procesa\":3}";
        Elemento obtenido = JJSON.parse(in);                                        //Leemos elemento del JSON
        
        int nelementos=obtenido.get_root().remove("procesa").elemento.get_integer(); //Obtenemos el número del nodo "procesa" y lo eliminamos
        Nodo nodopalabras=obtenido.get_root().find("palabras");                     //Obtenemos nodo "palabras"
        Elemento elnodopalabras=nodopalabras.elemento;                          //Obtenemos elemento del nodo
        obtenido.get_root().nodos.add(new Nodo("palabras originales",elnodopalabras.copy()));   //Copiamos "palabras" en un nuevo Nodo que insertamos en el JSON
        ArrayList<Elemento> elementos = elnodopalabras.get_vector();            //el elemento de "palabras" es un vector. Obtenemos sus elementos.
        nodopalabras.nombre="elementos restantes";                              //cambiamos el nombre del nodo "palabras"
        
        ArrayList<Elemento> resultados=new ArrayList<>();                       //Creamos un nuevo vector de elementos JSON
        for (int i=0;i<nelementos;i++)                                          //Para "procesa" elementos del vector "palabras"...
        {
            String palabra=elementos.remove(0).get_string();                          //Obtenemos su valor de string y lo eliminamos del vector
            int longitud=palabra.length();                                      //En nuestro ejemplo calculamos longitud de la palabra
            ArrayList<Nodo> arrayresultado=new ArrayList<>();                   //Creamos nuevo array de nodos JSON
            arrayresultado.add(new Nodo("palabra",new Elemento(palabra.substring(0, 3))));  //Insertamos nodo con la palabra limitada a 3 caracteres
            arrayresultado.add(new Nodo("longitud",new Elemento(longitud)));    //Insertamos nodo con la longitud de palabra
            resultados.add(new Elemento(new Raiz(arrayresultado)));             //Insertamos en el array resultados una nueva raiz con el array de nodos
        }
        obtenido.get_root().nodos.add(new Nodo("resultados",new Elemento(resultados)));   //insertamos un nuevo nodo con el vector resultados
        
        String out=obtenido.toString();                                         //Obtenemos el elemento obtenido como String JSON
        System.out.println(out);
    }
}
