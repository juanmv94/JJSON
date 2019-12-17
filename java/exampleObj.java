package JJSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class bean {
	private int exampleInteger=2;
	private String exampleString="Hola";
	private char exampleChar='Z';
	private Integer[] exampleArray = {5,6,7,null};
	private List<Long> exampleList = null;
	
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
	public char getExampleChar() {
		return exampleChar;
	}
	public void setExampleChar(char exampleChar) {
		this.exampleChar = exampleChar;
	}
	public Integer[] getExampleArray() {
		return exampleArray;
	}
	public void setExampleArray(Integer[] exampleArray) {
		this.exampleArray = exampleArray;
	}
	public List<Long> getExampleList() {
		return exampleList;
	}
	public void setExampleList(List<Long> exampleList) {
		this.exampleList = exampleList;
	}
	
}


class ExampleJJSONObject {

	public static void main(String[] args) {
		bean b=new bean();
		b.setExampleList(Arrays.asList(10L,20L,30L,40L,50L));
		System.out.println(JJSONobj.parse(b).toString(true));
	}

}
