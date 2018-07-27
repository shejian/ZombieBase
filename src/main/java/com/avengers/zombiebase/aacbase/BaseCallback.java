package com.avengers.zombiebase.aacbase;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bty.retrofit.net.callAdapter.LifeCallAdapterFactory;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by duo.chen on 2018/7/24
 */
public class BaseCallback<T extends IBeanResponse> extends LifeCallAdapterFactory.LifeCallback<T> {

    private WeakReference<Repository<? extends IReqParam, T>> repositoryRef;

    public BaseCallback(@Nullable LifecycleOwner lifecycleOwner, Repository<? extends IReqParam, T> repository) {
        super(lifecycleOwner);
        this.repositoryRef = new WeakReference<Repository<? extends IReqParam, T>>(repository);
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (response.body() != null) {
            if ("200".equals(response.body().getStatus())) {
                if (repositoryRef.get() != null) {
                    repositoryRef.get().saveData(response.body());
                    repositoryRef.get().getNetWorkState().postValue(NetworkState.Companion.getLOADED());
                }
            } else {
                if (repositoryRef.get() != null) {
                    if(repositoryRef.get().haveData()) {
                        repositoryRef.get().getNetWorkState().postValue(NetworkState.Companion.cachedError("failed have data"));
                    } else {
                        repositoryRef.get().getNetWorkState().postValue(NetworkState.Companion.error("failed" + response.body().getMessage()));
                    }
                }
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        if (repositoryRef.get() != null) {
            repositoryRef.get().getNetWorkState().postValue(NetworkState.Companion.error("failed"));
        }
    }

}
