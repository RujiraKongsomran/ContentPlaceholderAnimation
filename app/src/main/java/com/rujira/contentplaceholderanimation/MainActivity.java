package com.rujira.contentplaceholderanimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.rujira.contentplaceholderanimation.Adapter.MyAdapter;
import com.rujira.contentplaceholderanimation.Model.Model;
import com.rujira.contentplaceholderanimation.Retrofit.IMyApi;
import com.rujira.contentplaceholderanimation.Retrofit.RetrofitClient;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.supercharge.shimmerlayout.ShimmerLayout;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    IMyApi myApi;
    RecyclerView recyclerView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ShimmerLayout shimmerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        myApi = retrofit.create(IMyApi.class);

        shimmerLayout = (ShimmerLayout) findViewById(R.id.shimmerLayout);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch data
        fetchData();

    }

    private void fetchData() {
        shimmerLayout.startShimmerAnimation();

        compositeDisposable.add(myApi.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Model>>() {
                    @Override
                    public void accept(List<Model> models) throws Exception {
                        displayData(models);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void displayData(List<Model> models) {
        MyAdapter adapter = new MyAdapter(this, models);
        recyclerView.setAdapter(adapter);
        shimmerLayout.stopShimmerAnimation();
        shimmerLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
