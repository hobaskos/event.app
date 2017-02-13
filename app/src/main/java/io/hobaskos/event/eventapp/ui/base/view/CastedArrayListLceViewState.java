package io.hobaskos.event.eventapp.ui.base.view;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.AbsParcelableLceViewState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 2/13/2017.
 */

public class CastedArrayListLceViewState<D extends List<D>, V extends MvpLceView<D>>
        extends AbsParcelableLceViewState<D, V> {

    public static final Parcelable.Creator<CastedArrayListLceViewState> CREATOR =
            new Parcelable.Creator<CastedArrayListLceViewState>() {
                @Override public CastedArrayListLceViewState createFromParcel(Parcel source) {
                    return new CastedArrayListLceViewState(source);
                }

                @Override public CastedArrayListLceViewState[] newArray(int size) {
                    return new CastedArrayListLceViewState[size];
                }
            };

    public CastedArrayListLceViewState() {
    }

    protected CastedArrayListLceViewState(Parcel source) {
        readFromParcel(source);
    }

    @Override public void writeToParcel(Parcel dest, int flags) {

        if (loadedData != null && !(loadedData instanceof ArrayList)) {
            throw new ClassCastException(
                    "You try to use CastedArrayListLceViewState which takes List<D> as argument but "
                            + "assumes that it's an instance of ArrayList<D>. Howerver, your loaded data is not an ArrayList"
                            + " but it's of type "
                            + loadedData.getClass().getCanonicalName()
                            + " which is not supported");
        }

        // Content
        dest.writeList(loadedData);

        super.writeToParcel(dest, flags);
    }

    @Override protected void readFromParcel(Parcel source) {

        loadedData = (D) source.readArrayList(getClassLoader());
        super.readFromParcel(source);
    }

    /**
     * The class loader used for deserializing the list of parcelable items
     */
    protected ClassLoader getClassLoader() {
        return getClass().getClassLoader();
    }
}
