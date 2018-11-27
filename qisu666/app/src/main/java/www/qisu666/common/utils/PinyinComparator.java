package www.qisu666.common.utils;

import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<ArrayList<String>> {

    public int compare(ArrayList<String> list1, ArrayList<String> list2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
        if (list2.get(0).equals("#")) {
            return -1;
        } else if (list1.get(0).equals("#")) {
            return 1;
        } else {
            return list1.get(1).compareTo(list2.get(1));
        }
    }
}
