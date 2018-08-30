package com.test.hexun.mytest.rxjava;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.test.hexun.mytest.R;
import com.test.hexun.mytest.main.BaseActivity;
import com.test.hexun.mytest.rxjava.adapter.MyListAdapter;
import com.test.hexun.mytest.rxjava.bean.TimeBean;
import com.test.hexun.mytest.utils.LogBiz;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.R.id.inputExtractEditText;
import static android.R.id.list;

public class RxJavaTestActivity extends BaseActivity {

    List<String> list;
    @BindView(R.id.list_view)
    ListView listView;
    Observable<Long> timeObservable;
    Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_test);
        ButterKnife.bind(this);
        listView = (ListView) findViewById(R.id.list_view);
        list = new ArrayList<>();
        final MyListAdapter arrayAdapter = new MyListAdapter(this, R.layout.item_rxjava_list, R.id.text, list);
        listView.setAdapter(arrayAdapter);
        timeObservable = Observable.interval(1, TimeUnit.SECONDS).observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        subscription = timeObservable.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
//                LogBiz.i(aLong + "");
                if (aLong % 2 != 0 && aLong % 3 != 0) {
                    list.add(aLong + "");
                    arrayAdapter.notifyDataSetChanged();
                    listView.setSelection(arrayAdapter.getCount() - 1);
                }
            }
        });
        List<TimeBean> times = new ArrayList<>();
        times.add(new TimeBean(System.currentTimeMillis(), "haha1", 1));
        times.add(new TimeBean(System.currentTimeMillis(), "haha2", 2));
        times.add(new TimeBean(System.currentTimeMillis(), "haha3", 3));
        Observable.from(times).map(new Func1<TimeBean, String>() {
            @Override
            public String call(TimeBean o) {
                return o.introduce + "+" + o.id + "+" + o.time;
            }
        }).all(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                LogBiz.i(s);
                return s.startsWith("h");
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                LogBiz.i(aBoolean+"");
            }
        });
        final Integer[] i ={1,2,2,2,3,3,4,5,6};
        Observable.from(i).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return integer+"";
            }
        }).distinctUntilChanged().subscribe(new Action1<String>() {
            @Override
            public void call(String ints) {
                LogBiz.i(ints);
            }
        });
        Observable.just(1,2,2,2,3,3,4,5,6).collect(new Func0<ArrayList<Integer>>() {
            @Override
            public ArrayList<Integer> call() {
                return new ArrayList<Integer>();
            }
        }, new Action2<ArrayList<Integer>, Integer>() {
            @Override
            public void call(ArrayList<Integer> integers, Integer integer) {
                integers.add(integer);
            }
        }).subscribe(new Action1<ArrayList<Integer>>() {
            @Override
            public void call(ArrayList<Integer> integers) {
                Observable.from(integers).distinct().subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LogBiz.i(integer+"");
                    }
                });
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }
}
