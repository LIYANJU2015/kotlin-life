package org.cuieney.videolife.di.component;

import android.app.Activity;


import org.cuieney.videolife.di.PerFragment;
import org.cuieney.videolife.di.module.FragmentModule;
import org.cuieney.videolife.ui.fragment.music.MusicHomeFragment;

import dagger.Component;

/**
 * Created by cuieney on 16/8/7.
 */

@PerFragment
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();

    void inject(MusicHomeFragment musicFragment);
}
