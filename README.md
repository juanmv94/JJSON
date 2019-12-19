# JJSON

A simple JSON parser and generator for both Java and C++.

## About

JJSON was initially made as a simple Java JSON parser for *Zara code challenge 2018* and later become a full Java library with String generation support. Then, it was ported to C++.

## Getting Started

JJSON is an easy way to manage JSON data in Java/C++ thanks to the following classes:

* **Root (Raiz):**

>{Node, Node, ...}

* **Node (Nodo):**

>"name" : element

* **Element (Elemento)** with the following categories:
 * **String\*:** *"hello world"*
 * **Number (Integer):** *123*
 * **Number (Floating point):** *123.45*
 * **Vector \*:** *[Elemento, Elemento, ...]*
 * **Root \*:** *{Nodo, Nodo, ...}*

(" \* " means pointer for the C++ version)

## What can I do with this lib

You can process JSON data in string format, generate it, or even modify a parsed JSON to generate an altered one.

## How to use
Check the example for both platforms to understand how does it work. This example does the following:

* takes a number and some strings from a JSON vector
* deletes the number node from the JSON
* copies the string vector into another
* creates a results vector
* it does the following operations the read number of times:
 * gets the first string from the vector and deletes it
 * Gets its first 3 characters, calculates its length (number), and stores this values in a new root.
 * Adds the new root to the results vector.
* Prints the result as JSON string

There's also an aditional example for the Java version for bean (Object with getters) parsing, which is a Java exclusive feature of JJSON.


### The JJSON class

The JJSON class has only static members, and you shouldn't create an instance of it. It has the following public properties:

* string **JJSON_Ident**: the identation character(s) for pretty printing. It is 2 white spaces by default.
* long **JJSON_Bad_Integer** (Java only): value returned when integer parsing fails caused by a wrong number format (Example: `"number":-53+3+64`). It is 0 by default.
* double **JJSON_Bad_Float** (Java only): value returned when floating point parsing fails caused by a wrong number format (Example: `"number":-53+3+64`). It is 0 by default.

Functions:

* Elemento **parse(string json)**: returns a JJSON element from a JSON string. Numbers are parsed as floating point which is the recomended.
* Elemento **parse(string json, boolean integers)**: returns a JJSON element from a JSON string. Numbers are parsed as integers or floating point depending on the **integers** parameter.

The following JJSON funtions manages string escaping:

* string **escape(string in)**: escapes a string. That means replacing for example the **"** character from a string with **\\"**. This is needed for JSON strings. The constructor for unescaped strings and the **set\_unsc\_string** function from *Elemento* calls this.
* string **unescape(string in)**: unescapes a string. That means replacing for example **\\"** from a string with the **"** character. The **get\_unsc\_string()** function from *Elemento* calls this.

### Constructors (to generate new JSON content)

* **Raiz()**: it takes a Node array(Java) / vector(C++) that you should create first.
* **Elemento()**: it's contructor can take one of its valid categories elements listed here:
 * **String(\*):** *"hello world"*
 * **Char:** *'A'*
 * **Number(long, int, short):** *123*
 * **Number(double, float):** *123.45*
 * **Vector/List of Elemento \*:** *[Elemento, Elemento, ...]*
 * **Root \*:** *{Nodo, Nodo, ...}*

For string constructors, the string can be escaped or not. In Java, you pass a second boolean argument "*escaped*" to the constructor, while in c++ you use the **string** constructor for unescaped strings, and the **string\*** constructor for escaped strings (this allows to easy reuse existing string pointers of escaped JSON strings, but you must be careful when clearing and don't make pointers to be deleted twice).

* **Nodo()**: it takes a string, and Elemento. For C++ users, remember that a Elemento is contained in the Nodo, and it's not a pointer to it.

### Nodo
##### Java and C++
* **string nombre**
* **Elemento elemento**
* **copy()**: makes a copy (clone) of the actual node. It also copies its element.
* **toString()**: Prints the node and its element in JSON string format. 

### Raiz (array of nodes)
##### Java and C++
* **Array/vector<Nodo> nodos**:
* **find(string s)**: finds a Node by its name and returns it
* **remove(string s)**: finds a Node in the root by its name, deletes it from the root, and returns it (it copies it in C++)
* **copy()**: makes a copy (clone) of the actual root. It also copies their sub-elements.
* **toString()**: Prints the root and their sub-elements in JSON string format. 

##### Only C++
* **clear()**: it calls **clear()** in all their node elements and empties the root.
* **del(string s, bool clearelement)**: same as **remove()** but it doesn't gives you a copy of the removed element. Second boolean argument calls **clear()** in the found node if true.

### Elemento
##### Java and C++
* **set_\*\*\*()**: changes the actual element content.
* **get_tipo()**: checks the kind of data stored in the element. Posible values are:
  * *JJSON_Null*=0
  * *JJSON_String*=1
  * *JJSON_Vector*=2
  * *JJSON_Root*=3
  * *JJSON_Boolean*=4
  * *JJSON_Integer*=5
  * *JJSON_Float*=6
* **get_\*\*\*()**: gets the actual element content. For **JJSON_Integer** you can get the stored **long** value or cast it to **int**. For **JJSON_Float** you can get the stored **double** value or cast it to **float**.
* **copy()**: makes a copy (clone) of the actual element. It also copies their sub-elements.
* **toString()**: Prints the element value as minified JSON string.
* **toString(boolean pretty)**: Prints the element value as pretty/minified JSON string depending on the **pretty** parameter.

##### Only C++
* **clear()**: it deletes pointers for string, root, and vector pointed elements and all it's subelements.

### C++ Notes
The C++ implementation of JJSON compiles with the C++98 standard.

##### Memory deallocation
In C++ you need to take care of memory allocation with the Root/Element **clear()** functions.

This functions clears and frees memory  for the Root/Element itself and all theirs sub-elements (if exists), and it also empties the roots and sets the elements to null. Take care that if you have a duplicated reference to one of their subelements, it won't be valid anymore (additionally, if there is another reference in the same JJSON, *delete* will be called twice for the same pointer. Avoid it!), but you can use the **copy()** function to make a copy of it and their sub-elements. **clear()** is also called in the following functions.

* **set_\*\*\*()** functions from elemento gets and aditional boolean parameter than the Java version to specify if you want to **clear()** before changing the content.

* **del()** function from Raiz gets a second boolean parameter to specify if you want to **clear()** the element contained in the found node to be deleted.

If you use the **remove()** function instead of **del()**, you must clear and delete the returned Node* by yourself.

##### Elemento content
In the Elemento class, every kind of data is stored in the **void\* elemento** variable, it doesn't matter if they are pointers, or numbers. This saves a lot of memory in big JSONs when comparing to the Java version, but if you don't check **get_tipo()** and read an incorrect type of data, you will read garbage instead of **null**.

### The JJSONobj class (Java only)
This class has a single static function **parse(Object o)** that allows you to parse a Java bean object (an object from a class providing getters for it's values) into a JJSON **Elemento** of Root type. It also parses Lists and arrays into **Elemento** of Vector type.

## Reliability
If a malformed JSON string is passed to the lib, it will try to auto-complete it at the end of the string returning a JSON object that may differ from the expected one.

Java and C++ return different values for incorrect number fields in the JSON, like `"number":-53+3+64`.
In this example, C++ parses `"number":-53` and Java parses `"number":JJSON_Bad_Int/Float`. This is because C++ atoll and atof functions doesn't throw exceptions and gives results for the malformed number strings, while Java throws *StringIndexOutOfBoundsException* that is catched by the lib returning these predefined values.

Any JSON element starting with **t**,**f**,or **n** will be read as **true**, **false**, and **null**. Why to lose processing time checking undefined values?

You should always check the type of data from a **Elemento** before retrieving it unless you have the certainty of having the right one. If you get the incorrect type of data in C++ you can get bad pointers, or bad numbers. If you get the incorrect data in Java you can get a ClassCastException (or NullPointerException if geting from a null value **Elemento**).

You should not modify static properties of JJSON (**JJSON_Ident**, **JJSON_Bad_Integer**, **JJSON_Bad_Float**) in case they are being used by JJSON parsing/string printing with multithreading.
