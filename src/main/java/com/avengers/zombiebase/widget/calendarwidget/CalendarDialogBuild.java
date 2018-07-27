package com.avengers.zombiebase.widget.calendarwidget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.avengers.zombiebase.DialogBuilder;
import com.avengers.zombiebase.ToastOneUtil;
import com.avengers.zombiebase.tools.SpannableUtil;
import com.avengers.zombielibrary.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateMonthSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Jervishe on 2017/12/12.
 * 1.注意：CalendarDay只精确到天 如2017/12/12 00:00:00
 * 2.如果需要获取完整的一天结束的时间，请调用getLastSecondOnDay()转换,2017/12/12 23:59:59
 */
public class CalendarDialogBuild {

    public interface ICalendarDialogEvent {
        void onRangeSelected(CalendarDay startDay, CalendarDay endDay);

        void onRangeSelected(List<Date> list);

    }


    public static void showRangeDialog(final Context context,
                                       final ICalendarDialogEvent iCalendarDialogEvent,
                                       Date firstDate, Date lastDate) {
        AlertDialog.Builder alertDialogBuild = DialogBuilder.buildAlertDialog(context);
        final CalendarDay[] mStartDay = new CalendarDay[1];
        final CalendarDay[] mEndDay = new CalendarDay[1];
        final List<Date> listCalendarDay = new ArrayList<>();
        alertDialogBuild.setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mStartDay[0] != null) {
                    iCalendarDialogEvent.onRangeSelected(mStartDay[0], mEndDay[0]);
                    iCalendarDialogEvent.onRangeSelected(listCalendarDay);
                }
            }
        });

        View rootView = initCalendarDialogView(context, new ICalendarDialogEvent() {
            @Override
            public void onRangeSelected(CalendarDay startDay, CalendarDay endDay) {
                mStartDay[0] = startDay;
                mEndDay[0] = endDay;
            }

            @Override
            public void onRangeSelected(List<Date> list) {
                listCalendarDay.clear();
                listCalendarDay.addAll(list);
            }
        }, firstDate, lastDate);
        alertDialogBuild.setView(rootView);
        alertDialogBuild.show();
    }

    public static void showSelectionWeeksDialog(final Context context, final ICalendarDialogEvent iCalendarDialogEvent, Date firstDate, Date lastDate) {
        AlertDialog.Builder alertDialogBuild = DialogBuilder.buildAlertDialog(context);
        final CalendarDay[] mStartDay = new CalendarDay[1];
        final CalendarDay[] mEndDay = new CalendarDay[1];
        final List<Date> listCalendarDay = new ArrayList<>();
        alertDialogBuild.setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mStartDay[0] != null) {
                    iCalendarDialogEvent.onRangeSelected(mStartDay[0], mEndDay[0]);
                    iCalendarDialogEvent.onRangeSelected(listCalendarDay);
                }
            }
        });

        View rootView = initCalendarWeeksDialogView(context, new ICalendarDialogEvent() {
            @Override
            public void onRangeSelected(CalendarDay startDay, CalendarDay endDay) {
                mStartDay[0] = startDay;
                mEndDay[0] = endDay;
            }

            @Override
            public void onRangeSelected(List<Date> list) {
                listCalendarDay.clear();
                listCalendarDay.addAll(list);
            }
        }, firstDate, lastDate);
        alertDialogBuild.setView(rootView);
        alertDialogBuild.show();
    }

    static View initCalendarDialogView(Context context, final ICalendarDialogEvent iCalendarDialogEvent, Date firstDate, Date lastDate) {
        final SimpleDateFormat dayFormat = new SimpleDateFormat("MM月dd日");
        View dialogRootView = View.inflate(context, R.layout.calendar_dialog_view, null);
        final ViewSwitcher calendarValueSwitcher = dialogRootView.findViewById(R.id.calendar_value_switcher);
        final TextView calendarViewAValue = dialogRootView.findViewById(R.id.calendarView_start_value);
        final TextView calendarViewBValue = dialogRootView.findViewById(R.id.calendarView_end_value);
        MaterialCalendarView materialCalendarView = initCalendarView((Activity) context, iCalendarDialogEvent, dialogRootView, calendarValueSwitcher, calendarViewAValue, calendarViewBValue);
        materialCalendarView.selectRange(CalendarDay.from(firstDate), CalendarDay.from(lastDate));
        materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).setMaximumDate(new Date()).commit();
        showSelectedCalendarDayTitle(context, CalendarDay.from(firstDate), CalendarDay.from(lastDate),
                calendarValueSwitcher, calendarViewAValue, calendarViewBValue, dayFormat);
        return dialogRootView;
    }

    static View initCalendarWeeksDialogView(Context context, final ICalendarDialogEvent iCalendarDialogEvent, Date firstDate, Date lastDate) {
        final SimpleDateFormat dayFormat = new SimpleDateFormat("MM月dd日");
        View dialogRootView = View.inflate(context, R.layout.calendar_dialog_view, null);
        final ViewSwitcher calendarValueSwitcher = dialogRootView.findViewById(R.id.calendar_value_switcher);
        final TextView calendarViewAValue = dialogRootView.findViewById(R.id.calendarView_start_value);
        final TextView calendarViewBValue = dialogRootView.findViewById(R.id.calendarView_end_value);
        MaterialCalendarView materialCalendarView = initCalendarWeeksView((Activity) context, iCalendarDialogEvent, dialogRootView, calendarValueSwitcher, calendarViewAValue, calendarViewBValue);
        materialCalendarView.selectRange(CalendarDay.from(firstDate), CalendarDay.from(lastDate));
        materialCalendarView.state().edit().setFirstDayOfWeek(Calendar.MONDAY).setMaximumDate(new Date()).commit();
        showSelectedCalendarDayTitle(context, CalendarDay.from(firstDate), CalendarDay.from(lastDate),
                calendarValueSwitcher, calendarViewAValue, calendarViewBValue, dayFormat);

        return dialogRootView;
    }

    static View initCalendarMonthDialogView(Context context, final ICalendarDialogEvent iCalendarDialogEvent, Date firstDate, Date lastDate) {
        final SimpleDateFormat dayFormat = new SimpleDateFormat("MM月");
        View dialogRootView = View.inflate(context, R.layout.calendar_month_dialog_view, null);
        final ViewSwitcher calendarValueSwitcher = dialogRootView.findViewById(R.id.calendar_value_switcher);
        final TextView calendarViewAValue = dialogRootView.findViewById(R.id.calendarView_start_value);
        final TextView calendarViewBValue = dialogRootView.findViewById(R.id.calendarView_end_value);
        final View arrowAtoB = dialogRootView.findViewById(R.id.arrow_a_to_b);

        MaterialCalendarView materialCalendarView = initMonthCalendarView((Activity) context, iCalendarDialogEvent, dialogRootView, calendarValueSwitcher, calendarViewAValue, calendarViewBValue);
        materialCalendarView.selectMonthRange(CalendarDay.from(firstDate), getMonthLastDay(CalendarDay.from(lastDate)));
        //materialCalendarView.setSelectedDate(CalendarDay.from(firstDate));
        //materialCalendarView.state().edit().setMaximumDate(new Date()).commit();
        showSelectedCalendarDayTitle(context, CalendarDay.from(firstDate), getMonthLastDay(CalendarDay.from(lastDate)),
                calendarValueSwitcher, calendarViewAValue, calendarViewBValue, dayFormat);

        if (CalendarDay.from(firstDate).getMonth() == CalendarDay.from(lastDate).getMonth()) {
            calendarValueSwitcher.setDisplayedChild(0);
            TextView tv = calendarValueSwitcher.findViewById(R.id.calendarView_Str);
            tv.setText(CalendarDay.from(lastDate).getYear() + "年" + dayFormat.format(lastDate));
        }

        return dialogRootView;
    }

    private static MaterialCalendarView initCalendarView(final Activity context, final ICalendarDialogEvent iCalendarDialogEvent, View dialogRootView, final ViewSwitcher calendarValueSwitcher, final TextView calendarViewAValue, final TextView calendarViewBValue) {
        final SimpleDateFormat dayFormat = new SimpleDateFormat("MM月dd日");
        MaterialCalendarView materialCalendarView = dialogRootView.findViewById(R.id.calendarView);
        MaterialCalendarView.DAY_NAMES_ROW = 1;
        MaterialCalendarView.DEFAULT_DAYS_IN_WEEK = 7;
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);
        materialCalendarView.setTileWidthDp(42);
        materialCalendarView.setTileHeightDp(42);
        materialCalendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull List<CalendarDay> listCalendarDay) {
                showSelectedCalendarDayTitle(context, listCalendarDay.get(0),
                        listCalendarDay.get(listCalendarDay.size() - 1), calendarValueSwitcher, calendarViewAValue, calendarViewBValue, dayFormat);
                iCalendarDialogEvent.onRangeSelected(listCalendarDay.get(0), listCalendarDay.get(listCalendarDay.size() - 1));
                List<Date> dates = new ArrayList<>();
                for (CalendarDay calendarDay : listCalendarDay) {
                    dates.add(calendarDay.getDate());
                }
                iCalendarDialogEvent.onRangeSelected(dates);
            }
        });
        materialCalendarView.addDecorators(
                new MySelectorDecorator(context),
                new OneDayDecorator()
        );
        return materialCalendarView;
    }


    private static MaterialCalendarView initCalendarWeeksView(final Activity context, final ICalendarDialogEvent iCalendarDialogEvent, View dialogRootView, final ViewSwitcher calendarValueSwitcher, final TextView calendarViewAValue, final TextView calendarViewBValue) {
        final SimpleDateFormat dayFormat = new SimpleDateFormat("MM月dd日");
        MaterialCalendarView materialCalendarView = dialogRootView.findViewById(R.id.calendarView);
        MaterialCalendarView.DAY_NAMES_ROW = 1;
        MaterialCalendarView.DEFAULT_DAYS_IN_WEEK = 7;
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE_WEEK);
        materialCalendarView.setTileWidthDp(42);
        materialCalendarView.setTileHeightDp(42);
        materialCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_OTHER_MONTHS);

        materialCalendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull List<CalendarDay> listCalendarDay) {
                showSelectedCalendarDayTitle(context, listCalendarDay.get(0),
                        listCalendarDay.get(listCalendarDay.size() - 1), calendarValueSwitcher, calendarViewAValue, calendarViewBValue, dayFormat);
                iCalendarDialogEvent.onRangeSelected(listCalendarDay.get(0), listCalendarDay.get(listCalendarDay.size() - 1));
                List<Date> dates = new ArrayList<>();
                for (CalendarDay calendarDay : listCalendarDay) {
                    dates.add(calendarDay.getDate());
                }
                iCalendarDialogEvent.onRangeSelected(dates);
            }
        });
        materialCalendarView.addDecorators(
                new MySelectorDecorator(context),
                new OneDayDecorator()
        );

        return materialCalendarView;
    }

    //
    private static void showSelectedCalendarDayTitle(Context context, CalendarDay first, CalendarDay last,
                                                     ViewSwitcher calendarValueSwitcher,
                                                     TextView calendarViewAValue,
                                                     TextView calendarViewBValue,
                                                     SimpleDateFormat format) {
        final SpannableUtil spannableUtil = new SpannableUtil(context);
        calendarValueSwitcher.setDisplayedChild(1);
        String year = first.getYear() + "";
        String datetime = format.format(first.getDate());
        SpannableStringBuilder builderF = spannableUtil.getSpannableString(year + "", "\n" + datetime,
                R.style.text_12_fa3b3b_bold,
                R.style.text_18_fa3b3b_bold);
        calendarViewAValue.setText(builderF);

        String yearB = last.getYear() + "";
        String bDateTime = format.format(last.getDate());
        SpannableStringBuilder builderB = spannableUtil.getSpannableString(yearB + "", "\n" + bDateTime,
                R.style.text_12_fa3b3b_bold,
                R.style.text_18_fa3b3b_bold);
        calendarViewBValue.setText(builderB);

    }


    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    public static String getSelectedDatesString(CalendarDay date) {
        // CalendarDay date = materialCalendarView.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }


    public static void showMonthDialog(final Context context, final ICalendarDialogEvent iCalendarDialogEvent, Date firstDate, Date lastDate) {
        AlertDialog.Builder alertDialogBuild = DialogBuilder.buildAlertDialog(context);
        final CalendarDay[] mStartDay = new CalendarDay[1];
        final CalendarDay[] mEndDay = new CalendarDay[1];
        final List<Date> listCalendarDay = new ArrayList<>();
        alertDialogBuild.setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mStartDay[0] != null) {
                    iCalendarDialogEvent.onRangeSelected(mStartDay[0], mEndDay[0]);
                    iCalendarDialogEvent.onRangeSelected(listCalendarDay);
                } else {
                    ToastOneUtil.INSTANCE.showToastShort("请选择正确的时间");
                }
            }
        });

        View rootView = initCalendarMonthDialogView(context, new ICalendarDialogEvent() {
            @Override
            public void onRangeSelected(CalendarDay startDay, CalendarDay endDay) {
                mStartDay[0] = startDay;
                mEndDay[0] = endDay;
            }

            @Override
            public void onRangeSelected(List<Date> list) {
                listCalendarDay.clear();
                listCalendarDay.addAll(list);
            }
        }, firstDate, lastDate);
        alertDialogBuild.setView(rootView);
        alertDialogBuild.show();
    }


    private static MaterialCalendarView initMonthCalendarView(final Activity context, final ICalendarDialogEvent iCalendarDialogEvent, View dialogRootView, final ViewSwitcher calendarValueSwitcher, final TextView calendarViewAValue, final TextView calendarViewBValue) {
        final SimpleDateFormat monthFormat = new SimpleDateFormat("MM月");
        MaterialCalendarView materialCalendarView = dialogRootView.findViewById(R.id.calendarView);
        MaterialCalendarView.DAY_NAMES_ROW = 0;
        MaterialCalendarView.DEFAULT_DAYS_IN_WEEK = 6;
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);
        materialCalendarView.setOnDateMonthChangedListener(new OnDateMonthSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, @NonNull CalendarDay dateEnd, boolean selected) {
                if (!selected) {
                    calendarValueSwitcher.setDisplayedChild(0);
                    TextView tv = calendarValueSwitcher.findViewById(R.id.calendarView_Str);
                    tv.setText("请选择月份");
                    iCalendarDialogEvent.onRangeSelected(null, null);
                    iCalendarDialogEvent.onRangeSelected(new ArrayList<Date>());
                    return;
                }

                CalendarDay endCalendarDay = getMonthLastDay(dateEnd);
                showSelectedCalendarDayTitle(context, date, endCalendarDay, calendarValueSwitcher, calendarViewAValue, calendarViewBValue, monthFormat);
                if (endCalendarDay.getMonth() == date.getMonth()) {
                    calendarValueSwitcher.setDisplayedChild(0);
                    TextView tv = calendarValueSwitcher.findViewById(R.id.calendarView_Str);
                    tv.setText(endCalendarDay.getYear() + "年" + monthFormat.format(endCalendarDay.getDate()));
                } else {
                    calendarValueSwitcher.setDisplayedChild(1);
                    calendarViewBValue.setVisibility(View.VISIBLE);
                }


                iCalendarDialogEvent.onRangeSelected(date, endCalendarDay);
                List<Date> dates = new ArrayList<>();
                dates.add(date.getDate());
                dates.add(endCalendarDay.getDate());
                iCalendarDialogEvent.onRangeSelected(dates);
            }
        });


        materialCalendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull List<CalendarDay> listCalendarDay) {
                CalendarDay endCalendarDay = getMonthLastDay(listCalendarDay.get(listCalendarDay.size() - 1));
                showSelectedCalendarDayTitle(context, listCalendarDay.get(0), endCalendarDay,
                        calendarValueSwitcher,
                        calendarViewAValue,
                        calendarViewBValue, monthFormat);
                if (listCalendarDay.get(0).getMonth() == endCalendarDay.getMonth()) {
                    calendarValueSwitcher.setDisplayedChild(0);
                    TextView tv = calendarValueSwitcher.findViewById(R.id.calendarView_Str);
                    tv.setText(listCalendarDay.get(0).getYear() + "年" + monthFormat.format(endCalendarDay.getDate()));
                } else {
                    calendarValueSwitcher.setDisplayedChild(1);
                    calendarViewBValue.setVisibility(View.VISIBLE);
                }

                iCalendarDialogEvent.onRangeSelected(listCalendarDay.get(0), endCalendarDay);
                List<Date> dates = new ArrayList<>();
                for (CalendarDay calendarDay : listCalendarDay) {
                    dates.add(calendarDay.getDate());
                }
                dates.add(endCalendarDay.getDate());
                iCalendarDialogEvent.onRangeSelected(dates);

            }
        });

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR) - 5, Calendar.JANUARY, 1);

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);

        materialCalendarView.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .setCalendarDisplayMode(CalendarMode.YEARS)
                .commit();

        materialCalendarView.addDecorators(
                new MySelectorDecorator(context),
                new OneDayDecorator()
        );

        return materialCalendarView;
    }

    public static CalendarDay getMonthLastDay(CalendarDay lastDay) {
        Calendar calendar = Calendar.getInstance();
        lastDay.copyToMonthOnly(calendar);
        //跳到下个月1日
        calendar.add(Calendar.MONTH, 1);
        //用下个月的时间减一天，否则界面的月份填充就会有问题
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return CalendarDay.from(calendar);
    }

    /**
     * 通过CalendarDay 获取最后一天结束时的完整Calendar。如2018-5-31 23:59:59
     *
     * @param lastDay
     * @return
     */
    public static Calendar getLastSecondOnDay(CalendarDay lastDay) {
        Calendar calendar = Calendar.getInstance();
        lastDay.copyTo(calendar);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
        return calendar;
    }


}
