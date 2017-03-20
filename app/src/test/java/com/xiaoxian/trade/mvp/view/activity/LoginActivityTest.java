package com.xiaoxian.trade.mvp.view.activity;

import android.widget.EditText;

import com.xiaoxian.trade.BuildConfig;
import com.xiaoxian.trade.R;
import com.xiaoxian.trade.di.component.DaggerLoginActivityComponent;
import com.xiaoxian.trade.di.module.LoginActivityModule;
import com.xiaoxian.trade.mvp.presenter.LoginPresenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LoginActivityTest {
    @Test
    public void testLoginActivity() {
        LoginActivity activity = Robolectric.setupActivity(LoginActivity.class);
        LoginActivityModule module = new LoginActivityModule(activity);
        LoginPresenter presenter = mock(LoginPresenter.class);
        Mockito.when(module.providePresenter()).thenReturn(presenter);
        DaggerLoginActivityComponent.builder()
                .loginActivityModule(module).build();

        ((EditText) activity.findViewById(R.id.login_user_name)).setText("小贤");
        ((EditText) activity.findViewById(R.id.login_user_pass)).setText("123456");
        activity.findViewById(R.id.login_button).performClick();

        verify(presenter).login("小贤", "123456");
    }
}