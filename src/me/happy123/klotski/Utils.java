package me.happy123.klotski;

import android.annotation.SuppressLint;
import java.util.*;

/*
 * implement for something useful utils
 */
public class Utils {
	
	/*
	 * deepcopy 2rd arrays
	 */
	@SuppressLint("NewApi")
	public static int[][] deepCopyInt(int[][] original) {
	    if (original == null) {
	        return null;
	    }

	    final int[][] result = new int[original.length][];
	    for (int i = 0; i < original.length; i++) {
	        result[i] = Arrays.copyOf(original[i], original[i].length);
	        // For Java versions prior to Java 6 use the next:
	        // System.arraycopy(original[i], 0, result[i], 0, original[i].length);
	    }
	    return result;
	}
}
