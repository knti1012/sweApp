package de.shop.ui.main;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import de.shop.R;

//TODO Activity oder FragmentActivity --> siehe Skript Folie 150!!!
public class Main extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Fragment navFragment = getFragmentManager().findFragmentById(R.id.nav);
        final Class<? extends Activity> mainActivity = navFragment == null || !navFragment.isInLayout()
        		                                       ? MainSmartphone.class
        		                                       : MainTablet.class;
        
		final Intent intent = new Intent(this, mainActivity);
		startActivity(intent);
    }
}
