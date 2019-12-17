#ifndef JJSON_H
#define	JJSON_H

#include <vector>
#include <string>

#define JJSON_Null 0
#define JJSON_String 1
#define JJSON_Vector 2
#define JJSON_Root 3
#define JJSON_bool 4
#define JJSON_Integer 5
#define JJSON_Float 6

using namespace std;

typedef struct JSONparseData {
	int position;
	bool integers;
	string json;
} JSONparseData;

class JJSON;
class Raiz;
class Nodo;
class Elemento;

class JJSON {
private:
    static string getJSONstr(JSONparseData *pd);
    static int getJSONint(JSONparseData *pd);
    static float getJSONfloat(JSONparseData *pd);
    static vector<Elemento>* getJSONvec(JSONparseData *pd);
    static Raiz* getJSONraiz(JSONparseData *pd);
    static Elemento getJSON(JSONparseData *pd);
    
public:
    static string JJSON_Ident;
    
	static Elemento parse(string json, bool integers);
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
    
	string toString(bool pretty, int identation);
	string toString(bool pretty);
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
    void set_bool(bool b, bool clearold);
    void set_integer(int e, bool clearold);
    void set_float(float f, bool clearold);
    
    char get_tipo();
    string* get_string();
    string get_unsc_string();
    vector<Elemento>* get_vector();
    Raiz* get_root();
    bool get_bool();
    int get_integer();
    float get_float();
    
    void clear();
    Elemento copy();
    
	string toString(bool pretty, int identation);
	string toString(bool pretty);
    string toString();
};

class Nodo {
public:
    string nombre;
    Elemento elemento;
    
    Nodo(string n, Elemento e);
    ~Nodo();

    Nodo copy();
    
	string toString(bool pretty, int identation);
	string toString(bool pretty);
    string toString();
};

#endif	/* JJSON_H */

