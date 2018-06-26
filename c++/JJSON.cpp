#include "JJSON.h"
#include <sstream>

////////////////
//JJSON
//////////////

bool JJSON::JJSON_Integers=false;
int JJSON::position;

string JJSON::getJSONstr(string json)
{
    int initpos=position;
    while (json.at(position)!='"')
    {
        if (json.at(position)=='\\') position+=2;
        else position++;
    }
    return json.substr(initpos, (position++)-initpos);
}

int JJSON::getJSONint(string json)
{
    int initpos=position;
    char cur;
    do {
        cur=json.at(++position);
    }
    while (((cur>='0')&&(cur<='9')) || cur=='-' || cur=='+');
    return atoi(json.substr(initpos, position-initpos).c_str());
}

float JJSON::getJSONfloat(string json)
{
    int initpos=position;
    char cur;
    do {
        cur=json.at(++position);
    }
    while (((cur>='0')&&(cur<='9')) || cur=='-' || cur=='+' || cur=='.');
    return atof(json.substr(initpos, position-initpos).c_str());
}

vector<Elemento>* JJSON::getJSONvec(string json)
{
    vector<Elemento>* arr=new vector<Elemento>();
        while (json.at(position)!=']')
        {
            arr->push_back(getJSON(json));
            while ((json.at(position)!=',') && (json.at(position)!=']')) position++;
        }
        return arr;
}

Raiz* JJSON::getJSONraiz(string json)
{
    vector<Nodo> nodos;
    while (true)
    {
        switch(json.at(position))
        {
            case '"':
            {
                position++;
                string nombre=getJSONstr(json);
                while (json.at(position)!=':') position++;
                Elemento e=getJSON(json);
                nodos.push_back(Nodo(nombre,e));
                break;
            }
            case '}':
            {
                position++;
                return new Raiz(nodos);
            }
            default:
                position++;
        }
    }
}

Elemento JJSON::getJSON(string json)
{
    while (true)
    {
        switch(json.at(position))
        {
            case '[':
            {
                position++;
                return Elemento(getJSONvec(json));
            }
            case '{':
            {
                position++;
                Raiz* r=getJSONraiz(json);
                return Elemento(r);
            }
            case '"':
            {
                position++;
                return Elemento(new string(getJSONstr(json)));
            }
            case 't':
            {
                position+=4;
                return Elemento(true);
            }
            case 'f':
            {
                position+=5;
                return Elemento(false);
            }
            case 'n':
            {
                position+=4;
                return Elemento();
            }
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
            {
                if (JJSON_Integers)
                    return Elemento(getJSONint(json));
                else
                    return Elemento(getJSONfloat(json));
            }
            default:
                position++;
        }
    }
}

Elemento JJSON::parse(string json)
{
    position=0;
    return getJSON(json);
}

////////////////
//Raiz
//////////////

Raiz::Raiz(vector<Nodo> n) : nodos(n) {}
Raiz::~Raiz() {}
    
Nodo* Raiz::find(string s)
{
    for (vector<Nodo>::iterator i=nodos.begin();i!=nodos.end();i++)
        if (i->nombre == s)
            return (Nodo*)(&(*i));        
    return NULL;
}

Nodo* Raiz::remove(string s)
{
    for (vector<Nodo>::iterator i=nodos.begin();i!=nodos.end();i++)
        if (i->nombre == s)
        {
            Nodo* ret=new Nodo(*i);
            nodos.erase(i);
            return ret;
        }
    return NULL;
}

bool Raiz::del(string s, bool clearelement)     //Exclusivo de la versión c++
{
    for (vector<Nodo>::iterator i=nodos.begin();i!=nodos.end();i++)
        if (i->nombre == s)
        {
            if (clearelement) i->elemento.clear();
            nodos.erase(i);
            return true;
        }
    return false;
}
    
Raiz* Raiz::copy()
{
    vector<Nodo> arr;
    for (vector<Nodo>::iterator i=nodos.begin();i!=nodos.end();i++)
        arr.push_back(i->copy());
    return new Raiz(arr);
}

void Raiz::clear()
{
    for (vector<Nodo>::iterator i=nodos.begin();i!=nodos.end();i++)
        i->elemento.clear();
    nodos.clear();
}
    
string Raiz::toString()
{
    string res="{";
    if (nodos.size()>0)
    {
        vector<Nodo>::iterator i;
        for (i=nodos.begin();i!=nodos.end()-1;i++)
            res+=i->toString()+",";
        res+=i->toString();
    }
    res+="}";
    return res;
}

////////////////
//Nodo
//////////////

Nodo::Nodo(string n, Elemento e) : nombre(n), elemento(e) {}

Nodo::~Nodo() {}
    
Nodo Nodo::copy()
{
    return Nodo(nombre,elemento.copy());
}

string Nodo::toString() {
        return "\""+nombre+"\":"+elemento.toString();
}

////////////////
//Elemento
//////////////

Elemento::Elemento()
{
    set_null(false);
}
Elemento::Elemento(string* s)
{
    set_string(s,false);
}
Elemento::Elemento(vector<Elemento>* a)
{
    set_vector(a,false);
}
Elemento::Elemento(Raiz* r)
{
    set_root(r,false);
}
Elemento::Elemento(bool b)
{
    set_boolean(b,false);
}
Elemento::Elemento(int e)
{
    set_integer(e,false);
}
Elemento::Elemento(float f)
{
    set_float(f,false);
}
Elemento::~Elemento()
{
    //clear();
}

void Elemento::clear()
{
    if (tipo==JJSON_Root)
    {
        Raiz* r=(Raiz*)elemento;
        r->clear();
        delete r;
    }
    else if (tipo==JJSON_String)
        delete (string*)elemento;
    else if (tipo==JJSON_Vector)
    {
        vector<Elemento>* v=(vector<Elemento>*)elemento;
        for (vector<Elemento>::iterator i=v->begin();i!=v->end();i++)
            i->clear();
        delete v;
    }
    tipo=JJSON_Null;
}
    
void Elemento::set_null(bool clearold)
{
    if (clearold) clear();
    else tipo=JJSON_Null;
}
void Elemento::set_string(string* s,bool clearold)
{
    if (clearold) clear();
    tipo=JJSON_String;
    elemento=s;
}
void Elemento::set_vector(vector<Elemento>* a, bool clearold)
{
    if (clearold) clear();
    tipo=JJSON_Vector;
    elemento=a;
}
void Elemento::set_root(Raiz* r, bool clearold)
{
    if (clearold) clear();
    tipo=JJSON_Root;
    elemento=r;
}
void Elemento::set_boolean(bool b, bool clearold)
{
    if (clearold) clear();
    tipo=JJSON_Boolean;
    *(bool*)(&elemento)=b;
}
void Elemento::set_integer(int e, bool clearold)
{
    if (clearold) clear();
    tipo=JJSON_Integer;
    *(int*)(&elemento)=e;
}
void Elemento::set_float(float f, bool clearold)
{
    if (clearold) clear();
    tipo=JJSON_Float;
    *(float*)(&elemento)=f;
}
    
char Elemento::get_tipo()
{
    return tipo;
}
string Elemento::get_string()
{
    return *(string*)elemento;
}
vector<Elemento>* Elemento::get_vector()
{
    return (vector<Elemento>*)(elemento);
}
Raiz* Elemento::get_root()
{
    return (Raiz*)elemento;
}
bool Elemento::get_boolean()
{
    return *(bool*)(&elemento);
}
int Elemento::get_integer()
{
    return *(int*)(&elemento);
}
float Elemento::get_float()
{
    return *(float*)(&elemento);
}
    
Elemento Elemento::copy()
{
    switch(tipo)
    {
        case JJSON_Float:
            return Elemento(get_float());
        case JJSON_Integer:
            return Elemento(get_integer());
        case JJSON_Boolean:
            return Elemento(get_boolean());
        case JJSON_String:
            return Elemento(new string(get_string()));
        case JJSON_Root:
            return Elemento(get_root()->copy());
        case JJSON_Vector:
        {
            vector<Elemento>* arr= new vector<Elemento>();
            vector<Elemento>* orig=(vector<Elemento>*)elemento;
            for (vector<Elemento>::iterator i=orig->begin();i!=orig->end();i++)
                arr->push_back(i->copy());
            return Elemento(arr);
        }
        case JJSON_Null:
        default:
            return Elemento();                
        }
}
    
string Elemento::toString()
{
    switch(tipo)
       {
            case JJSON_Float:
            {
                ostringstream ss;
                ss << get_float();
                return ss.str();
            }
            case JJSON_Integer:
            {
                ostringstream ss;
                ss << get_integer();
                return ss.str();
            }
            case JJSON_Boolean:
            {
                if (get_boolean())
                    return "true";
                else
                    return "false";
            }
            case JJSON_String:
                return "\""+get_string()+"\"";
            case JJSON_Root:
                return get_root()->toString();
            case JJSON_Vector:
            {
                string res="[";
                vector<Elemento>* orig=(vector<Elemento>*)elemento;
                if (orig->size()>0)
                {
                    for (vector<Elemento>::iterator i=orig->begin();i!=orig->end()-1;i++)
                        res+=i->toString()+",";
                    res+=orig->at(orig->size()-1).toString();
                }
                res+="]";
                return res;
            }
            case JJSON_Null:
                return "null";
            default:
                return "";                
        }
}
