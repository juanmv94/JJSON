#ifndef JJSON_H
#define	JJSON_H

#include <vector>
#include <string>

#define JJSON_Null 0
#define JJSON_String 1
#define JJSON_Vector 2
#define JJSON_Root 3
#define JJSON_Boolean 4
#define JJSON_Integer 5
#define JJSON_Float 6

using namespace std;

class JJSON;
class Raiz;
class Nodo;
class Elemento;

class JJSON {
private:
    static int position;
    
    static string getJSONstr(string json);
    static int getJSONint(string json);
    static float getJSONfloat(string json);
    static vector<Elemento>* getJSONvec(string json);
    static Raiz* getJSONraiz(string json);
    static Elemento getJSON(string json);
    
public:
    static bool JJSON_Integers;     //Read JSON numbers as integers
    
    static Elemento parse(string json);
    
    //string escape utilities
    static string* escape(string in);
    static string unescape(string in);
};

class Raiz {
public:
    vector<Nodo> nodos;
    
    Raiz(vector<Nodo> n);
    ~Raiz();
    
    Nodo* find(string s);
    Nodo* remove(string s);
    bool del(string s, bool clearelement);      //Exclusivo de la versión c++
    
    void clear();
    Raiz* copy();
    
    string toString();
};

class Elemento {
private:
    char tipo;
    void* elemento;     //Lo usamos para almacenar cualquier cosa en sus 32-64bit (Debe poder almacenar punteros)
    
public:
    Elemento();
    Elemento(string* s);
    Elemento(string unsc_s);
    Elemento(vector<Elemento>* a);
    Elemento(Raiz* r);
    Elemento(bool b);
    Elemento(int e);
    Elemento(float f);
    ~Elemento();
    
    void set_null(bool clearold);
    void set_string(string* s, bool clearold);
    void set_unsc_string(string unsc_s, bool clearold);
    void set_vector(vector<Elemento>* a, bool clearold);
    void set_root(Raiz* r, bool clearold);
    void set_boolean(bool b, bool clearold);
    void set_integer(int e, bool clearold);
    void set_float(float f, bool clearold);
    
    char get_tipo();
    string* get_string();
    string get_unsc_string();
    vector<Elemento>* get_vector();
    Raiz* get_root();
    bool get_boolean();
    int get_integer();
    float get_float();
    
    void clear();
    Elemento copy();
    
    string toString();
};

class Nodo {
public:
    string nombre;
    Elemento elemento;
    
    Nodo(string n, Elemento e);
    ~Nodo();

    Nodo copy();
    
    string toString();
};

#endif	/* JJSON_H */

