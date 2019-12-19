package examples;

import java.util.Arrays;
import java.util.List;

import JJSON.*;

class bean {
	private int exampleInteger=2;
	private String exampleString="Hola";
	private char[] exampleArray = {'a','Z','\\'};
	private List<Long> exampleList = null;
	private boolean exampleBoolean = false;
	private Float exampleFloat = 2.5F;
	
	public int getExampleInteger() {
		return exampleInteger;
	}
	public void setExampleInteger(int exampleInteger) {
		this.exampleInteger = exampleInteger;
	}
	public String getExampleString() {
		return exampleString;
	}
	public void setExampleString(String exampleString) {
		this.exampleString = exampleString;
	}
	public char[] getExampleArray() {
		return exampleArray;
	}
	public void setExampleArray(char[] exampleArray) {
		this.exampleArray = exampleArray;
	}
	public List<Long> getExampleList() {
		return exampleList;
	}
	public void setExampleList(List<Long> exampleList) {
		this.exampleList = exampleList;
	}
	public boolean isExampleBoolean() {
		return exampleBoolean;
	}
	public void setExampleBoolean(boolean exampleBoolean) {
		this.exampleBoolean = exampleBoolean;
	}
	public Float getExampleFloat() {
		return exampleFloat;
	}
	public void setExampleFloat(Float exampleFloat) {
		this.exampleFloat = exampleFloat;
	}
}


class ExampleJJSONObject {

	public static void main(String[] args) {
		bean b=new bean();
		b.setExampleList(Arrays.asList(10L,20L,null));
		System.out.println(JJSONobj.parse(b).toString(true));
	}

}
