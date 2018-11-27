package www.qisu666.common.utils;

import android.support.v4.util.LongSparseArray;
import android.support.v4.util.SparseArrayCompat;

import java.util.ArrayList;
import java.util.Set;

public class ArraysUtils {
	public static final String TAG = ArraysUtils.class.getSimpleName();

	public static <T> T getFirstNotNull(ArrayList<T> arrays) {
		T result = null;
		if(arrays != null){
			for (T item : arrays) {
				if (item != null) {
					result = item;
					break;
				}
			}
		}
		return result;
	}
	
	public static <T> T getFirstNotNull(T[] arrays){
		if(arrays != null){
			for(T t : arrays){
				if(t != null) return t;
			}
		}
		return null;
	}
	
	public static <T> T getFirstNotNull(LongSparseArray<T> arrays){
		T result = null;
		if(arrays != null){
			int size = arrays.size();
			for(int i = 0; i < size; i ++){
				result = arrays.valueAt(i);
				if(result != null) break;
			}
		}
		return result;
	}

	public static <T> T getFirstNotNull(SparseArrayCompat<T> arrays){
		T result = null;
		int size = arrays.size();
		for(int i = 0; i < size; i ++){
			result = arrays.valueAt(i);
			if(result != null) break;
		}
		return result;
	}
	
	public static <T> ArrayList<T> toArray(LongSparseArray<T> pArray){
		ArrayList<T> result = new ArrayList<T>();
		
		if(pArray == null) return result;
		int len = pArray.size();
		for(int i =0; i < len; i ++){
			T item = pArray.valueAt(i);
			result.add(item);
		}
		
		return result;
	}
	
	public static <T> void toArray(LongSparseArray<T> src, ArrayList<T> out){
		
		if(src == null || out == null) return;
		int len = src.size();
		for(int i =0; i < len; i ++){
			T item = src.valueAt(i);
			out.add(item);
		}
	}
	
	public static <T> ArrayList<T> toArray(SparseArrayCompat<T> pArray){
		ArrayList<T> result = new ArrayList<T>();
		
		if(pArray == null) return result;
		int len = pArray.size();
		for(int i =0; i < len; i ++){
			T item = pArray.valueAt(i);
			result.add(item);
		}
		
		return result;
	}
	
	public static <T> void copy(SparseArrayCompat<T> src, SparseArrayCompat<T> out){
		if(src != null && out != null){
			int len = src.size();
			for(int i = 0; i < len; i ++){
				out.put(src.keyAt(i), src.valueAt(i));
			}
		}
	}
	
	public static <T> void copy(ArrayList<T> src, ArrayList<T> out){
		if(src != null && out != null){
			for(T t : src){
				out.add(t);
			}
		}
	}
	
	
	public static <T> ArrayList<T> toArray(Set<T> set){
		ArrayList<T> result = new ArrayList<T>();
		if(set == null) return result;
		for(T t : set){
			result.add(t);
		}
		return result;
	}
	
	public static <T> void toArray(Set<T> set, ArrayList<T> out){
		if(set == null || out == null) return;
		for(T t : set){
			out.add(t);
		}
	}

}
