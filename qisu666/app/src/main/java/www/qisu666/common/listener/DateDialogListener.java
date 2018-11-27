package www.qisu666.common.listener;

/**
 * Created by Coang on 2015/7/30.
 */
public interface DateDialogListener {

    void getYear(String year);
    void getMonth(String month);
    void getDay(String day);

    void isChange(boolean change);
}
