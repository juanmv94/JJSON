#include "JJSON.h"
#include <sstream>
#include <cstdlib>
#include <stdexcept>

////////////////
//JJSON
//////////////

string JJSON::JJSON_Ident="  ";

string JJSON::getJSONstr(JSONparseData *pd)
{
    int initpos=pd->position;
    try
    {
        while (pd->json.at(pd->position)!='"')
        {
            if (pd->json.at(pd->position)=='\\') pd->position+=2;
            else pd->position++;
        }
        return pd->json.substr(initpos, (pd->position++)-initpos);
    }
    catch(const out_of_range& e)
    {
        return pd->json.substr(initpos, pd->position-initpos);
    }
}

int JJSON::getJSONint(JSONparseData *pd)
{
    int initpos=pd->position;
    char cur;
    try
    {
        do {
            cur=pd->json.at(++pd->position);
        }
        while (((cur>='0')&&(cur<='9')) || cur=='-' || cur=='+');
    }
    catch(const out_of_range& e) {}
    
    return atoi(pd->json.substr(initpos, pd->position-initpos).c_str());
}

float JJSON::getJSONfloat(JSONparseData *pd)
{
    int initpos=pd->position;
    char cur;
    try
    {
        do {
            cur=pd->json.at(++pd->position);
        }
        while (((cur>='0')&&(cur<='9')) || cur=='-' || cur=='+' || cur=='.');
    }
    catch(const out_of_range& e) {}
    
    return atof(pd->json.substr(initpos, pd->position-initpos).c_str());
}

vector<Elemento>* JJSON::getJSONvec(JSONparseData *pd)
{
    vector<Elemento>* arr=new vector<Elemento>();
    try
    {
        while (pd->json.at(pd->position)!=']')
        {
            arr->push_back(getJSON(pd));
            while ((pd->json.at(pd->position)!=',') && (pd->json.at(pd->position)!=']')) pd->position++;
        }
    }
    catch(const out_of_range& e) {}
    return arr;
}

Raiz* JJSON::getJSONraiz(JSONparseData *pd)
{
    vector<Nodo> nodos;
    try {
        while (true)
        {
            switch(pd->json.at(pd->position))
            {
                case '"':
                {
                    pd->position++;
                    string nombre=getJSONstr(pd);
                    while (pd->json.at(pd->position)!=':') pd->position++;
                    Elemento e=getJSON(pd);
                    nodos.push_back(Nodo(nombre,e));
                    break;
                }
                case '}':
                {
                    pd->position++;
                    return new Raiz(nodos);
                }
                default:
                    pd->position++;
            }
        }
    }
    catch(const out_of_range& e)
    {
        return new Raiz(nodos);
    }
}

Elemento JJSON::getJSON(JSONparseData *pd)
{
    try {
        while (true)
        {
            switch(pd->json.at(pd->position))
            {
                case '[':
                {
                    pd->position++;
                    return Elemento(getJSONvec(pd));
                }
                case '{':
                {
                    pd->position++;
                    Raiz* r=getJSONraiz(pd);
                    return Elemento(r);
                }
                case '"':
                {
                    pd->position++;
                    return Elemento(new string(getJSONstr(pd)));
                }
                case 't':
                {
                    pd->position+=4;
                    return Elemento(true);
                }
                case 'f':
                {
                    pd->position+=5;
                    return Elemento(false);
                }
                case 'n':
                {
                    pd->position+=4;
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
                    if (pd->integers)
                        return Elemento(getJSONint(pd));
                    else
                        return Elemento(getJSONfloat(pd));
                }
                default:
                    pd->position++;
            }
        }
    }
    catch(const out_of_range& e)
    {
        return Elemento();
    }
}

Elemento JJSON::parse(string json, bool integers)
{
    JSONparseData jp={0,integers,json};
    return getJSON(&jp);
}

Elemento JJSON::parse(string json)
{
    JSONparseData jp={0,false,json};
    return getJSON(&jp);
}

string* JJSON::escape(string in)
{
    string *out = new string();
    out->reserve(in.size());
    for (int i=0; i<in.size(); i++)
    {
        switch (in[i])
        {
            case '\r':
            *out+="\\r";
            break;
            case '\n':
            *out+="\\n";
            break;
            case '\t':
            *out+="\\t";
            break;
            case '\b':
            *out+="\\b";
            break;
            case '\f':
            *out+="\\f";
            break;
            case '\"':
            *out+="\\\"";
            break;
            case '\\':
            *out+="\\\\";
            break;
            default:
            *out+=in[i];
        }
    }
    return out;
}
string JJSON::unescape(string in)
{
    string out;
    out.reserve(in.size()/2);
    bool special=false;
    for (int i=0; i<in.size(); i++)
    {
        if (special)
        {
            switch (in[i])
            {
                case 'r':
                out+='\r';
                break;
                case 'n':
                out+='\n';
                break;
                case 't':
                out+='\t';
                break;
                case 'b':
                out+='\b';
                break;
                case 'f':
                out+='\f';
                break;
                default:
                out+=in[i];
            }
            special=false;
        }
        else
        {
            if (in[i]=='\\')
                special=true;
            else
                out+=in[i];
        }
    }
    return out;
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

string Raiz::toString(bool pretty, int identation)
{
    string res="";
	if (pretty) {
		if (identation>0) res+='\n';
		for (int n=0;n<identation;n++) res+=JJSON::JJSON_Ident;
    	identation++;
    }
    res+='{';
    if (!nodos.empty())
    {
        vector<Nodo>::iterator i;
        for (i=nodos.begin();i!=nodos.end();i++) {
			if (pretty) {
				res+='\n';
				for (int n=0;n<identation;n++) res+=JJSON::JJSON_Ident;
			}
			res+=i->toString(pretty,identation);
            if (i!=nodos.end()-1) res+=",";
		}
    }
	if (pretty) {
		identation--;
		res+='\n';
		for (int n=0;n<identation;n++) res+=JJSON::JJSON_Ident;
	}
    res+='}';
    return res;
}
string Raiz::toString(bool pretty)
{
	return toString(pretty,0);
}
string Raiz::toString()
{
	return toString(false);
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

string Nodo::toString(bool pretty, int identation)
{
	string res="\""+nombre+"\":";
	if (pretty) res+=' ';
	res+=elemento.toString(pretty,identation);
	return res;
}
    
string Nodo::toString(bool pretty)
{
	return toString(pretty,0);
}

string Nodo::toString()
{
	return toString(false);
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
Elemento::Elemento(string unsc_s)
{
    set_unsc_string(unsc_s,false);
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
    set_bool(b,false);
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

void Elemento::set_unsc_string(string unsc_s,bool clearold)
{
    if (clearold) clear();
    tipo=JJSON_String;
    elemento=JJSON::escape(unsc_s);
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
void Elemento::set_bool(bool b, bool clearold)
{
    if (clearold) clear();
    tipo=JJSON_bool;
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
string* Elemento::get_string()
{
    return (string*)elemento;
}
string Elemento::get_unsc_string()
{
    return JJSON::unescape(*(string*)elemento);
}
vector<Elemento>* Elemento::get_vector()
{
    return (vector<Elemento>*)(elemento);
}
Raiz* Elemento::get_root()
{
    return (Raiz*)elemento;
}
bool Elemento::get_bool()
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
        case JJSON_bool:
            return Elemento(get_bool());
        case JJSON_String:
            return Elemento(new string(*get_string()));
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
    
string Elemento::toString(bool pretty, int identation)
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
            case JJSON_bool:
            {
                if (get_bool())
                    return "true";
                else
                    return "false";
            }
            case JJSON_String:
                return '\"'+*get_string()+'\"';
            case JJSON_Root:
                return get_root()->toString(pretty,identation);
            case JJSON_Vector:
            {
                string res="[";
                vector<Elemento>* orig=(vector<Elemento>*)elemento;
                if (orig->size()>0)
                {
                    for (vector<Elemento>::iterator i=orig->begin();i!=orig->end()-1;i++) {
                        res+=i->toString(pretty, identation)+',';
						if (pretty) res+=' ';
					}
                    res+=orig->at(orig->size()-1).toString(pretty, identation);
                }
                res+=']';
                return res;
            }
            case JJSON_Null:
                return "null";
            default:
                return "";                
        }
}

string Elemento::toString(bool pretty)
{
	return toString(pretty,0);
}

string Elemento::toString()
{
	return toString(false);
}
