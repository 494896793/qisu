package www.qisu666.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/5/17.
 */
public class BankCardUtils {

    public static boolean isBankCard(String string) {

        if (string.length() < 16 || string.length() > 19) {
            return false;
        }
        Pattern regex = Pattern.compile("\\d*$");
        Matcher matcher = regex.matcher(string);
        if (!matcher.matches()) {
            return false;
        }
        String strStart = "10,18,30,35,37,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,58,60,62,65,68,69,84,87,88,90,94,95,98,99";
        if (strStart.indexOf(string.substring(0, 2)) == -1) {
            return false;
        }
        String lastStr = string.substring(string.length() - 1, string.length());
        String firstStr = string.substring(0, string.length() - 1);
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < firstStr.length(); i++) {
            data.add(firstStr.substring(i, i + 1));
        }
        List<Integer> arrOdd = new ArrayList<Integer>();
        List<Integer> arrOdd2 = new ArrayList<Integer>();
        List<Integer> arrEven = new ArrayList<Integer>();

        for (int j = 0; j < data.size(); j++) {
            if ((j + 1) % 2 == 1) {
                if (Integer.parseInt(data.get(j)) * 2 < 9) {
                    arrOdd.add(Integer.parseInt(data.get(j)) * 2);
                } else {
                    arrOdd2.add(Integer.parseInt(data.get(j)) * 2);
                }
            } else {
                arrEven.add(Integer.parseInt(data.get(j)));
            }
        }
        List<Integer> oddChild = new ArrayList<Integer>();
        List<Integer> oddChild2 = new ArrayList<Integer>();
        for (int h = 0; h < arrOdd2.size(); h++) {
            oddChild.add(arrOdd2.get(h) % 10);
            oddChild2.add(arrOdd2.get(h) / 10);
        }
        int sumOdd = 0;
        int sumEven = 0;
        int sumOddChild1 = 0;
        int sumOddChild2 = 0;
        int sumTotal = 0;
        for (int m = 0; m < arrOdd.size(); ++m) {
            sumOdd = sumOdd + arrOdd.get(m);
        }
        for (int n = 0; n < arrEven.size(); ++n) {
            sumEven = sumEven + arrEven.get(n);
        }
        for (int p = 0; p < oddChild.size(); ++p) {
            sumOddChild1 = sumOddChild1 + oddChild.get(p);
            sumOddChild2 = sumOddChild2 + oddChild2.get(p);
        }
        sumTotal = sumOdd + sumEven + sumOddChild1 + sumOddChild2;
        int result = sumTotal % 10;
        int k = result == 0 ? 10 : result;
        int luhm = 10 - k;
        if (Integer.parseInt(lastStr) == luhm) {
            return true;
        } else {
            return false;
        }
    }

}
