package com.greatapp.qpinion;

import java.util.ArrayList;

import android.text.TextUtils;

import com.greatapp.qpinion.constants.V;
import com.greatapp.qpinion.contacts.QpinionContact;
import com.greatapp.qpinion.group.QpinionGroup;

public class Tools {

	public static String[] getSplittedArray(String string,String splitter) {
		String[] splitted = null;
		if(string == null || splitter == null) {
			return null;
		}
		splitted = string.split(splitter);
		
		return splitted;
	}
	
	public static Integer[] getIntegerArrayFromStringArray(String[] strings) {
	    Integer[] intarray= new Integer[strings.length];
	    int i=0;
	    for(String str:strings){
	    	if(TextUtils.isEmpty(str)){
	    		intarray[i] = 0;
	    	} else { 
	           intarray[i]=Integer.parseInt(str.trim());
	    	}
	        i++;
	    }
	    return intarray;
	}

	public static String getMergedStringFromStringList(ArrayList<String> list) {
		String merged = "";
		if(list != null) {
			for(String item : list){
				merged = merged + item + V.SPLIT;
			}
		}
		return merged;
	}

	public static String getMergedPhoneStringFromContacts(ArrayList<QpinionContact> tagContacts) {
		String merged = "";
        if(tagContacts != null) {
        	for(QpinionContact contact: tagContacts) {
        		merged = merged + contact.getmPhoneNumber()+V.SPLIT;
        	}
        }
    	return merged;

	}

	public static String getMergedStringFromIntegerList(ArrayList<Integer> intList) {
		String merged = "";
        if(intList != null) {
        	for(Integer i : intList) {
        		merged = merged + i+V.SPLIT;
        	}
        }
    	return merged;
	}

	public static ArrayList<String> getGroupNamesFromList(ArrayList<QpinionGroup> loadedGroups) {
       ArrayList<String> list = new ArrayList<String>();
       for(QpinionGroup group : loadedGroups) {
               list.add(group.getName());
       }
       return list;
	}

	public static ArrayList<String> getArrayListFromStringArray(String[] stringArray) {
         ArrayList<String> list = new ArrayList<String>();
         for(String string : stringArray) {
        	 list.add(string);
         }
         return list;
	}
	
	public static String getEmijoByUnicode(int unicode){
	    return new String(Character.toChars(unicode));
	}
	
	public final static int e_monkey = 0x1F64A; 
	public final static int e_namaste	= 0x1F64F;
	public final static int e_smile = 0x1F60A;
    public final static int e_cancel = 0x274C;
public final static int e_add = 0x2795;
public final static int e_right_arrow = 0x27A1;
	public final static int e_ques_mark = 0x2754;
	public final static int e_exclametry = 0x2757;
	public final static int e_pencil = 0x270F;
	public final static int e_block  = 0x1F6AB;
	//public final static int one = 0x0031 0x20E3;
public final static int e_upfinger = 0x261D;
public final static int e_chat = 0x1F4AC;
public final static int e_pin = 0x1F4CC;
public final static int e_attach = 0x1F4CE;
public final static int	e_msg = 0x1F4E8;
public final static int	e_noti=0x1F514;
public final static int	e_clock = 0x1F550;
public final static int	e_refresh = 0x1F503;
public final static int e_check = 0x2705;
public final static int e_magifier = 0x1F50D;
}
