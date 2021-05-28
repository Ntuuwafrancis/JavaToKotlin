package com.sriyank.javatokotlindemo.app;

import android.app.Application;


public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
//		Realm.init(this); // should only be done once when app starts

//		RealmConfiguration config = new RealmConfiguration.Builder()
//												.build();

//		RealmConfiguration config = new RealmConfiguration.Builder()
//				.name("myrealm.realm")
//				.allowWritesOnUiThread(true)
//				.build();
//
//		Realm.setDefaultConfiguration(config);
	}

}
