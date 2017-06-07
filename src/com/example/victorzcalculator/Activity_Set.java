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
	boolean musicstate;// �����򿪵�״̬
	boolean viberstate;// �𶯴򿪵�״̬
	int scale;// ���㾫��
	Switch sw, music_sw;
	Button sure;
	Button cancel;
	NumberPicker np;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set);
		// ��������
		Intent intent = getIntent();

		sure = (Button) findViewById(R.id.set_sure);
		cancel = (Button) findViewById(R.id.set_cancel);

		music_sw = (Switch) findViewById(R.id.sound);
		sw = (Switch) findViewById(R.id.viber);

		np = (NumberPicker) findViewById(R.id.np);
		np.setMinValue(0);
		np.setMaxValue(6);

		// ����������״̬
		Bundle bundle = intent.getExtras();
		musicstate = bundle.getBoolean("musicstate");
		viberstate = bundle.getBoolean("viberstate");
		scale = bundle.getInt("scale");
		np.setValue(scale);// ȷ��������ʾ�ľ���

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

		// ȷ�����㾫�ȵĴ�������
		np.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				// TODO Auto-generated method stub
				scale = newVal;

			}

		});

		// ֻ��TRUE���FALSE�����
		music_sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if (isChecked) {

					Toast to1 = Toast.makeText(getBaseContext(), "������",
							Toast.LENGTH_SHORT);
					to1.show();
					musicstate = true;

				} else {
					Toast to1 = Toast.makeText(getBaseContext(), "�����ر�",
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

					Toast to1 = Toast.makeText(getBaseContext(), "�𶯴�",
							Toast.LENGTH_SHORT);
					to1.show();
					viberstate = true;

				} else {
					Toast to1 = Toast.makeText(getBaseContext(), "�𶯹ر�",
							Toast.LENGTH_SHORT);
					to1.show();
					viberstate = false;
				}
			}

		});

		// ȷ�������ļ�������
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				Bundle bundle = new Bundle();
				// ����
				bundle.putBoolean("viberstate", viberstate);
				bundle.putBoolean("musicstate", musicstate);
				bundle.putInt("scale", scale);
				i.putExtras(bundle);
				setResult(RESULT_OK, i);
				finish();

			}
		});

		// ȡ���ļ�������
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();

			}
		});

	}
}
