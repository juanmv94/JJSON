#include <iostream>
#include "JJSON.h"

using namespace std;

int main(int argc, char** argv) {
    
    JJSON::JJSON_Integers=true;                                                                         //Leemos los números del JSON como enteros
    string in="{ \"palabras\":[\"\\\"hola\",\"mundo\\\"\",\"lorem\",\"\\nipsum\",\"noprocesa\"],\"procesa\":4}";

    Elemento obtenido = JJSON::parse(in);
    
    Nodo* el=obtenido.get_root()->remove("procesa");                                                    //Obtenemos una copia del nodo "procesa" y lo eliminamos
    int nelementos=el->elemento.get_integer();                                                          //Obtenemos numero de elementos (nodo "procesa")
    //el->elemento.clear();                                                                             //Si procesa contuviera un string, vector, o raiz liberaríamos sus memorias.
    delete el;                                                                                          //Ya no necesitamos la copia                                           
    Nodo* nodopalabras=obtenido.get_root()->find("palabras");                                           //Obtenemos nodo "palabras"
    obtenido.get_root()->nodos.push_back(Nodo("palabras originales",nodopalabras->elemento.copy()));   //Copiamos "palabras" en un nuevo Nodo que insertamos en el JSON
    //nodopalabras=obtenido.get_root()->find("palabras");                                                 //Al modificar el vector de nodos de la raiz, el puntero nodopalabras anterior podría dejar de ser valido?
    vector<Elemento>* elementos = nodopalabras->elemento.get_vector();                                   //el elemento de "palabras" es un vector. Obtenemos sus elementos.
    nodopalabras->nombre="elementos restantes";                                                          //cambiamos el nombre del nodo "palabras"
    
    vector<Elemento>* resultados= new vector<Elemento>;                                               //Creamos un nuevo vector de elementos JSON
    for (int i=0;i<nelementos;i++)                                 //Para "procesa" elementos del vector "palabras"...
    {
        vector<Elemento>::iterator it=elementos->begin();
        string palabra=it->get_unsc_string();                     //Obtenemos su valor de string
        it->clear();                                              //Liberamos memoria del string en el vector
        elementos->erase(it);                                     //Eliminamos elemento del vector
        cout << "Procesando palabra: " << palabra << "\n";
        int longitud=palabra.length();                                      //En nuestro ejemplo calculamos longitud de la palabra
        vector<Nodo> arrayresultado;                                       //Creamos nuevo array de nodos JSON
        arrayresultado.push_back(Nodo("palabra",Elemento(palabra.substr(0, 3))));  //Insertamos nodo con la palabra limitada a 3 caracteres
        arrayresultado.push_back(Nodo("longitud",Elemento(longitud)));    //Insertamos nodo con la longitud de palabra
        resultados->push_back(Elemento(new Raiz(arrayresultado)));             //Insertamos en el array resultados una nueva raiz con el array de nodos
    }
    obtenido.get_root()->nodos.push_back(Nodo("resultados",Elemento(resultados)));   //insertamos un nuevo nodo con el vector resultados
    string out=obtenido.toString();
    obtenido.clear();
    cout << out << "\n";
    return 0;
}