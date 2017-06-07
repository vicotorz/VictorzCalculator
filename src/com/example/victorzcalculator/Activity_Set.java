package com.example.victorzcalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.Switch;
import android.widget.Toast;

public class Activity_Set extends Activity {
	boolean musicstate;// 声音打开的状态
	boolean viberstate;// 震动打开的状态
	int scale;// 计算精度
	Switch sw, music_sw;
	Button sure;
	Button cancel;
	NumberPicker np;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set);
		// 处理内容
		Intent intent = getIntent();

		sure = (Button) findViewById(R.id.set_sure);
		cancel = (Button) findViewById(R.id.set_cancel);

		music_sw = (Switch) findViewById(R.id.sound);
		sw = (Switch) findViewById(R.id.viber);

		np = (NumberPicker) findViewById(R.id.np);
		np.setMinValue(0);
		np.setMaxValue(6);

		// 重新设置震动状态
		Bundle bundle = intent.getExtras();
		musicstate = bundle.getBoolean("musicstate");
		viberstate = bundle.getBoolean("viberstate");
		scale = bundle.getInt("scale");
		np.setValue(scale);// 确定下来显示的精度

		if (musicstate) {
			music_sw.setChecked(true);
		} else {
			music_sw.setChecked(false);
		}

		if (viberstate) {
			sw.setChecked(true);
		} else {
			sw.setChecked(false);
		}

		// 确定计算精度的触发方法
		np.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				// TODO Auto-generated method stub
				scale = newVal;

			}

		});

		// 只有TRUE编程FALSE的情况
		music_sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if (isChecked) {

					Toast to1 = Toast.makeText(getBaseContext(), "声音打开",
							Toast.LENGTH_SHORT);
					to1.show();
					musicstate = true;

				} else {
					Toast to1 = Toast.makeText(getBaseContext(), "声音关闭",
							Toast.LENGTH_SHORT);
					to1.show();
					musicstate = false;

				}

			}
		});

		sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if (isChecked) {

					Toast to1 = Toast.makeText(getBaseContext(), "震动打开",
							Toast.LENGTH_SHORT);
					to1.show();
					viberstate = true;

				} else {
					Toast to1 = Toast.makeText(getBaseContext(), "震动关闭",
							Toast.LENGTH_SHORT);
					to1.show();
					viberstate = false;
				}
			}

		});

		// 确定按键的监听方法
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				Bundle bundle = new Bundle();
				// 处理
				bundle.putBoolean("viberstate", viberstate);
				bundle.putBoolean("musicstate", musicstate);
				bundle.putInt("scale", scale);
				i.putExtras(bundle);
				setResult(RESULT_OK, i);
				finish();

			}
		});

		// 取消的监听方法
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();

			}
		});

	}
}
