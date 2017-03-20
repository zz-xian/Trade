package com.xiaoxian.trade.mvp.contract;

import com.xiaoxian.trade.base.BasePresenter;
import com.xiaoxian.trade.base.BaseView;
import com.xiaoxian.trade.mvp.model.Sort;

import java.util.List;

public interface SortContract {
    interface IView extends BaseView {
        void showSort(List<Sort> sortList);

        void showSortError(String str);
    }

    interface IPresenter extends BasePresenter {
        void loadSort();
    }
}
