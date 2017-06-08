package com.mg.others.utils;

public class MiiBase64 {
	private static String baseString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!+-";

	/**
	 * 编码字符串
	 * @return String 编码后的字符串
	 */
	public static String encode(String str){
		String res 	= "";
		int bloc 	= (int)(Math.random() * 3);

		String keyBase		= 		Base64
				.encodeStringToString(MiiBase64.baseString);
		String[] skeyArr 	= keyBase.split("");

		String strBase		= 		Base64
				.encodeStringToString(str);
		String[] strArr		= strBase.split("");



			for(int i=1;i<skeyArr.length;i++){
			if(i < strArr.length){
				if(bloc == 0){
					strArr[i] = strArr[i] + getRandString(false) + getRandString(false);
				}else if(bloc == 1){
					strArr[i] = getRandString(false) + strArr[i] + getRandString(false);
				}else{
					strArr[i] = getRandString(false) + getRandString(false) + strArr[i];
				}
			}
		}
		for(int i=1;i<strArr.length;i++){
			res += strArr[i];
		}
		return res + genTailStr(bloc);
	}


	/**
	 * 编码字符串
	 * @return String 编码后的字符串
	 */
	public static String encode2(String str){
		String res 	= "";
		int bloc 	= (int)(Math.random() * 3);

		String keyBase		= 		Base64
				.encodeStringToString(MiiBase64.baseString);
		String[] skeyArr 	= keyBase.split("");

		String strBase		= 		Base64
				.encodeStringToString(str);
		String[] strArr		= strBase.split("");



		for(int i=1;i<skeyArr.length;i++){
			if(i < strArr.length){
				if(bloc == 0){
					strArr[i] = strArr[i] + getRandString(false) + getRandString(false);
				}else if(bloc == 1){
					strArr[i] = getRandString(false) + strArr[i] + getRandString(false);
				}else{
					strArr[i] = getRandString(false) + getRandString(false) + strArr[i];
				}
			}
		}
		for(int i=1;i<strArr.length;i++){
			res += strArr[i];
		}
		return res + genTailStr(bloc);
	}

	/**
	 * 解码字符串
	 * @return String 解码后的字符串
	 */
	public static String decode(String str){
		String res 		= "";
		String tailStr 	= str.substring(str.length() - 3);
		int bloc 		= fetchNumberFromString(tailStr);
		String keyBase		= 		Base64
				.encodeStringToString(MiiBase64.baseString);

		String deStr		= str.substring(0, str.length() - 3);
		String[] deStrArr	= splitString(deStr);

		for(int i=0;i<keyBase.length();i++){
			if(i < deStrArr.length){
				deStrArr[i] = deStrArr[i].substring(bloc, bloc + 1);
			}
		}
		for (int i = 0; i < deStrArr.length; i++) {
			res += deStrArr[i];
		}
		return 		Base64
				.decodeStringToString(res);
	}

	/**
	 * 取字符串中，任意一个位置的字符
	 */
	public static String getRandString(boolean noNumber){
		String res = "";
		int sloc = mt_rand(MiiBase64.baseString.length()-3);
		if (noNumber) {
			res = MiiBase64.baseString.substring(sloc, sloc + 1);
			while (res.equals("0") ||res.equals("1") ||res.equals("2") ||res.equals("3") ||res.equals("4") ||res.equals("5") ||res.equals("6") ||res.equals("7") ||res.equals("8") ||res.equals("9")){
				sloc = mt_rand(MiiBase64.baseString.length()-3);
				res = MiiBase64.baseString.substring(sloc, sloc + 1);
			}
			return res;
		}else{
			return MiiBase64.baseString.substring(sloc, sloc + 1);
		}
	}

	/**
	 * 获取随机整数
	 */
	public static int mt_rand(int max){
		return (int)(Math.random() * (max + 1));
	}

	/**
	 * 生成末尾字符串
	 */
	public static String genTailStr(int bloc){
		String res = "";
		if(bloc == 0){
			res = bloc + getRandString(true) + getRandString(true);
		}else if(bloc == 1){
			res = getRandString(true) + bloc + getRandString(true);
		}else{
			res = getRandString(true) + getRandString(true) + bloc;
		}
		return res;
	}

	/**
	 * 提取字符串中的数字
	 */
	public static int fetchNumberFromString(String str){
		int res = 0;
		String[] strArr = str.split("");
		for(int i=0;i<strArr.length;i++){
			String bstr = strArr[i];
			if(bstr.equals("0") ||bstr.equals("1") ||bstr.equals("2") ||bstr.equals("3") ||bstr.equals("4") ||bstr.equals("5") ||bstr.equals("6") ||bstr.equals("7") ||bstr.equals("8") ||bstr.equals("9")){
				res = Integer.valueOf(bstr);
				break;
			}
		}
		return res;
	}

	public static String[] splitString(String str) {
		int count = str.length();
		int arrLength;
		boolean isMore = true;
		if (str.length() % 3 == 0) {
			arrLength = count / 3;
		} else {
			isMore = false;
			arrLength = (int) count / 3 + 1;
		}
		String[] arr = new String[arrLength];
		for (int i = 0; i < arr.length; i++) {
			if (isMore || (i != arr.length - 1)) {
				arr[i] = str.substring(3 * i, 3 * i + 3);
			} else {
				arr[i] = str.substring(3 * i);
			}
		}
		return arr;
	}

}