package canaries.kike.pocsign.api;

import android.support.annotation.NonNull;

public interface ResponseCallback<T> {

    void onSuccess(@NonNull T result);

    void onError();

    void onFailure();

}
