package com.example.victorzcalculator;

import java.math.BigDecimal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends Activity {
	public static MainActivity mainactivity;

	private SoundPool soundPool;
	private int music;// ����һ��������load������������suondID
	// ��Ӧ��music Id��ֵ
	boolean MUSIC_STATE = false;// ��������Ч�� TUREΪ����������FALSEΪ�ر�����
	private Vibrator vibrator; // ��Ч����
	boolean VIB_STATE = false;// ����������Ч�����ж� TRUE�ǿ��� FALSEΪ�ر���
	int scale = 2;// ���㾫��

	EditText Edt;
	// �����������
	Button ACbutton;
	Button delbutton;
	Button but0;
	Button but1;
	Button but2;
	Button but3;
	Button but4;
	Button but5;
	Button but6;
	Button but7;
	Button but8;
	Button but9;
	Button butchu;
	Button butcheng;
	Button butjian;
	Button butjia;
	Button butdot;
	Button butequal;

	// �¼Ӳ���--------------------
	Button butpower;// ������
	Button butextract;// ��������
	Button butpai;// PAI
	Button butfactorial;// �׳�����
	Button butE;// ��Ȼ����E
	Button butsin;
	Button butcos;
	Button buttan;
	Button butln;
	Button butlog;

	OnClickListener listener;// ��������
	OnCheckedChangeListener listener2;// ֵ�ı������

	Switch sw;// ֵ�ı����

	static final int REQUEST_CODE = 0;

	Button leftbracket;// ������
	Button rightbracket;// ������

	double number1;// ��������1
	double number2;// ��������2
	String act;// ���е���ز���
	double result;// ���

	// �������ʾ��Ҫ���⴦��
	StringBuffer str_display = new StringBuffer();// ��ʾ����

	// �ж��Ƿ�Ϊ�����ַ��ǲ�������
	public boolean isNumber(char c) {
		boolean flag = false;

		// �����е���������������
		switch (c) {
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
		case '��':
		case 'e':
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		return flag;
	}

	public boolean equljuge(char c) {
		boolean flag = false;
		switch (c) {
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
		case '!':
		case ')':
		case '��':
		case 'e':
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		return flag;
	}

	// �ж��Ƿ����С��������
	public boolean isdot(String str) {
		// �ҵ����һ����������Ȼ���ٽ�����ص��ж�
		boolean flag = true;
		if (str.length() != 0) {
			int loc = str.length() - 1;
			to: for (; loc >= 0; loc--) {
				if (str.charAt(loc) == '+' || str.charAt(loc) == '-'
						|| str.charAt(loc) == '*' || str.charAt(loc) == '/'
						|| loc == 0) {
					loc = loc + 1;
					break to;

				}
			}

			for (int i = loc; i < str.length(); i++) {
				if (str.charAt(i) == '.') {
					flag = false;
					break;
				}
			}
			// �������һ��������
			return flag;
		} else {
			return flag;
		}

	}

	// ���ڵȺ��ж�
	public boolean equljugecharge(int loc) {
		boolean flag = true;

		// TURE����ʾ FALSE��ʾ
		// ���ڼӺźͼ��ţ��ڿ�ʼ����ֻ������һ�Σ�0��+��1�Ͳ����ˣ�,������λ��������������
		// loc��2��loc-2��loc-1���ԣ�loc�����ˣ�

		if (loc > 1 && !equljuge(str_display.toString().charAt(loc - 1))) {
			flag = false;
		}
		return flag;
	}

	// ���ڳ˳��������ж�
	public boolean charge(int loc) {
		boolean flag = true;

		// TURE����ʾ FALSE��ʾ
		// ���ڼӺźͼ��ţ��ڿ�ʼ����ֻ������һ�Σ�0��+��1�Ͳ����ˣ�,������λ��������������
		// loc��2��loc-2��loc-1���ԣ�loc�����ˣ�

		if (loc > 1 && !equljuge(str_display.toString().charAt(loc - 1))) {
			// ��
			flag = false;
		}
		return flag;
	}

	// �ж��������磺+10��-10�������ŵ����
	// ���ڼӼ��������ж�
	public boolean judge(int loc) {
		boolean flag = true;
		String keyString = str_display.toString();
		// �жϵ�ǰ����ļӺźͼ����Ƿ��ܼ�������
		// ����Լ�
		if (loc >= 2) {
			if (keyString.charAt(loc - 1) == '+'
					|| keyString.charAt(loc - 1) == '-') {
				if (!equljuge(keyString.charAt(loc - 2))) {
					flag = false;
				}
			}
		}
		return flag;
	}

	// �����жϼӺ�
	public boolean judge_plus(int loc) {
		// ��Ҫ�����ж������ˣ���Ϊ��������ԼӺ���Ӱ�죬���Խ�ֹ��������
		// �����һ��λ�ò�����false
		// ���ǰ���Ƿ����ֵĲ�����false
		// ���ǰ�������ֿ���true
		boolean flag = true;
		String keyString = str_display.toString();
		// �жϵ�ǰ����ļӺźͼ����Ƿ��ܼ�������
		// Toast to2 = Toast.makeText(getBaseContext(), "ע��λ�ã�" + loc,
		// Toast.LENGTH_SHORT);
		// to2.show();

		if (loc >= 1 && !equljuge(keyString.charAt(loc - 1))) {
			// Toast to1 = Toast.makeText(getBaseContext(), "2��",
			// Toast.LENGTH_SHORT);
			// to1.show();
			flag = false;
		}
		// Toast to1 = Toast.makeText(getBaseContext(), "�жϼӺ�" + flag,
		// Toast.LENGTH_SHORT);
		// to1.show();
		return flag;

	}

	// ��������ʾ����
	public void showText(StringBuffer str) {

		StringBuffer string = new StringBuffer();
		// string = str.toString();// ˵���������������⣬ͬʱҲ�ı���str������ֵ

		// ��Ҫ������ֵ��������ͬһ��������һ���ѿռ�

		char cs[] = str.toString().toCharArray();// ��String�����ַ�������
		// ת�����ַ������Ժ�ʹ��StringBuffer�����Զ�ƴװ
		int length = cs.length - 1;
		for (int i = 0; i <= length; i++) {
			switch (str.charAt(i)) {
			case '+': {
				if (judge_plus(i)) {
					string.append(cs[i]);
				} else {
					str_display.deleteCharAt(str_display.length() - 1);
				}
				break;
			}
			case '-': {
				if (judge(i)) {
					string.append(cs[i]);
				} else {
					str_display.deleteCharAt(str_display.length() - 1);
				}
				break;
			}
			case '*': {
				if (!charge(i)) {
					// ����ʾ
					str_display.deleteCharAt(str_display.length() - 1);
				} else {
					string.append("��");
				}
				break;
			}
			case '/': {
				if (!charge(i)) {
					// ����ʾ
					str_display.deleteCharAt(str_display.length() - 1);
				} else {
					string.append("��");
				}
				break;
			}

			case '=': {
				if (!equljugecharge(i)) {
					// ����ʾ
					str_display.deleteCharAt(str_display.length() - 1);
				} else {
					string.append("\n");
					string.append("=");
				}
				break;
			}
			// ��Ҫ���⴦��һ��
			case '��': {
				string.append("��");
				break;
			}
			case 's': {
				string.append("sin");
				break;
			}
			case 'c': {
				string.append("cos");
				break;
			}
			case 't': {
				string.append("tan");
				break;
			}
			case 'l': {
				string.append("ln");
				break;
			}
			case 'g': {
				string.append("log");
				break;
			}
			default:
				string.append(cs[i]);
				break;
			}

		}
		if (str_display.length() != 1 && str.charAt(0) == '0'
				&& str.charAt(1) != '=' && str.charAt(1) != '.'
				&& str.charAt(1) != '/' && str.charAt(1) != '*'
				&& str.charAt(1) != '^' && str.charAt(1) != '!') {
			str_display.deleteCharAt(0);
			string.deleteCharAt(0);
		}
		Edt.setText(string.toString());
		string = null;

	}

	// �������Ų����ϼ�������
	public void checkbracket_others(StringBuffer str) {

		char cs[] = str.toString().toCharArray();
		int len = cs.length;

		StringBuffer string = new StringBuffer();
		int i = 0;
		for (; i < len - 1; i++) {

			if (isNumber(cs[i])
					&& (cs[i + 1] == '(' || cs[i + 1] == 's'
							|| cs[i + 1] == 'c' || cs[i + 1] == 't'
							|| cs[i + 1] == 'l' || cs[i + 1] == 'g'
							|| cs[i + 1] == '��' || cs[i + 1] == '��' || cs[i + 1] == 'e')) {
				// �Զ���ӳ˺�
				string.append(cs[i]);
				string.append('*');
			} else if (cs[i] == ')'
					&& (cs[i + 1] == '(' || cs[i + 1] == 's'
							|| cs[i + 1] == 'c' || cs[i + 1] == 't'
							|| cs[i + 1] == 'l' || cs[i + 1] == 'g'
							|| cs[i + 1] == '��' || cs[i + 1] == '��' || cs[i + 1] == 'e')) {
				string.append(cs[i]);
				string.append('*');
			} else if (cs[i] == ')' && isNumber(cs[i + 1])) {
				// ��������
				// �׳��쳣����ʾError�������
				try {
					throw new Exception();
				} catch (Exception e) {
					Edt.setText("Error");
					str_display.delete(0, str_display.length());
					str_display.append("0");
				}
			} else if ((cs[i] == '��' || cs[i] == 'e' || cs[i] == '!')
					&& isNumber(cs[i + 1])) {
				string.append(cs[i]);
				string.append("*");
			} else {
				// ����ƴ�Ӿ���
				string.append(cs[i]);
			}
		}

		// ִ�е������д���ֱ���׳��쳣
		string.append(cs[i]);

		// ����string_display
		str_display.delete(0, str_display.length());
		str_display.append(string);

	}

	/**
	 * ����Ⱥź�Ĳ�������ˢ�¹��� ȷ����һ������ʽ��
	 * */
	public void dosth() {
		int loc = 0;
		int count = 0;
		int len = str_display.length() - 1;
		to: for (int i = len; i > 0; i--) {
			if (str_display.charAt(i) == '=') {
				loc = i;
				count++;
				break to;
			}
		}

		if (count != 0) {
			StringBuffer s = new StringBuffer(str_display.substring(loc + 1,
					len + 1));
			str_display.delete(0, len + 1);
			str_display.append(s);

		}

	}

	/**
	 * 
	 * �����ַ�����ʽ�� �õ��ѵ�֪ʶ������û�����Ż��ü���һЩ
	 * */

	public void calculateString(StringBuffer str) {

		// �ڻ�ò�����a��ʱ����Ҫͨ�����������жϴ������и�ֵ�Ĳ���
		String a = str.toString();
		StringCaculate caculate = new StringCaculate(mainactivity);
		// �Ѿ��������
		// str_display.delete(0, str_display.length());
		str_display.append("=");

		// BigDecimal.setScale(int val) ���þ���
		try {
			BigDecimal r = caculate.parse(a).setScale(scale,
					BigDecimal.ROUND_HALF_UP);

			// �ų���ʾ���磺2+3=5.00�����������ȥ���������ľ��ȣ�ֻ�ڱ�Ҫʱ��ʾ
			if (scale == 2) {
				double ta = r.doubleValue() * 100 % 10;
				double tb = r.doubleValue() * 10 % 10;

				if (ta == 0 && tb != 0) {
					str_display.append(String.valueOf(r.setScale(1)));
				} else if (ta == 0 && tb == 0) {
					str_display.append(String.valueOf(r.setScale(0)));
				} else if (ta == 0) {
					System.out.println(r.setScale(1));
				} else {
					str_display.append(String.valueOf(r));
				}
			} else {
				str_display.append(String.valueOf(r));
			}

			showText(str_display);
		} catch (Exception e) {
			Edt.setText("Error");
			str_display.delete(0, str_display.length());
			str_display.append("0");
		}
	}

	// ���ƶԻ�����,ֻ�Ǳ�׼��ʾ��
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1:
			return new AlertDialog.Builder(MainActivity.mainactivity)
					.setTitle("���ߺͷ�����ַ��Ϣ")
					.setMessage(
							"���ߣ����ִ�ѧ���ѧԺ�ŵ�\n"
									+ "���������victorzsnail@qq.com\n"
									+ "����΢������Ӱ��magic\n"
									+ "�汾��Ϣ��2.0\n"
									+ "�汾���ܣ�Vi������2.0�汾������һЩ���õĿ�ѧ���㣬�����˲˵�ѡ��������û�ʹ�á�ͬʱ����˼�����ģʽ�л����ܡ���ѡ��򿪻�رհ��������������𶯣��û��Զ�����㾫�ȣ��߱��˸��������Ե�һ�����������������ܣ���������˼������������㹦�ܡ�\n"
									+ "���н׳�����֧�ֿɼ��㷶Χ�ڵ�С���׳ˡ�")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton("ȷ��", null).create();

		default:
			return null;

		}
	}

	// ===========================================================================================================
	/**
	 * ��ʽ��ʼ����
	 * 
	 * **/

	// �����������Ĳ���
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ���ر���
		setContentView(R.layout.activity);
		mainactivity = this;

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		VIB_STATE = bundle.getBoolean("viberstate");
		MUSIC_STATE = bundle.getBoolean("musicstate");
		scale = bundle.getInt("scale");
		str_display.append(bundle.getString("showString"));

		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
		music = soundPool.load(getBaseContext(), R.raw.click, 1);

		vibrator = (Vibrator) getBaseContext().getSystemService(
				Context.VIBRATOR_SERVICE);

		Edt = (EditText) findViewById(R.id.et);
		Edt.setTextSize(50);
		showText(str_display);

		ACbutton = (Button) findViewById(R.id.ac);
		delbutton = (Button) findViewById(R.id.del);

		but0 = (Button) findViewById(R.id.no0);

		but1 = (Button) findViewById(R.id.no1);
		but2 = (Button) findViewById(R.id.no2);
		but3 = (Button) findViewById(R.id.no3);
		but4 = (Button) findViewById(R.id.no4);
		but5 = (Button) findViewById(R.id.no5);
		but6 = (Button) findViewById(R.id.no6);
		but7 = (Button) findViewById(R.id.no7);
		but8 = (Button) findViewById(R.id.no8);
		but9 = (Button) findViewById(R.id.no9);

		butchu = (Button) findViewById(R.id.chu);
		butcheng = (Button) findViewById(R.id.cheng);
		butjian = (Button) findViewById(R.id.jian);
		butjia = (Button) findViewById(R.id.jia);
		butdot = (Button) findViewById(R.id.dotfunc);
		butequal = (Button) findViewById(R.id.equal);

		// �������ŵ�R�ļ���ע���ȡ
		leftbracket = (Button) findViewById(R.id.leftbracket);
		rightbracket = (Button) findViewById(R.id.rightbracket);

		// ������ʽ1���������-----------------------------------------------------------------

		listener = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (VIB_STATE == true) {
					vibrator.vibrate(25);
				}
				if (MUSIC_STATE == true) {
					soundPool.play(music, 10, 10, 0, 0, 1);
				}
				switch (arg0.getId()) {
				case R.id.no0:
					str_display.append("0");
					showText(str_display);
					break;
				case R.id.no1:
					str_display.append("1");
					showText(str_display);
					break;
				case R.id.no2:
					str_display.append("2");
					showText(str_display);
					break;
				case R.id.no3:
					str_display.append("3");
					showText(str_display);
					break;
				case R.id.no4:
					str_display.append("4");
					showText(str_display);
					break;
				case R.id.no5:
					str_display.append("5");
					showText(str_display);
					break;
				case R.id.no6:
					str_display.append("6");
					showText(str_display);
					break;
				case R.id.no7:
					str_display.append("7");
					showText(str_display);
					break;
				case R.id.no8:
					str_display.append("8");
					showText(str_display);
					break;
				case R.id.no9:
					str_display.append("9");
					showText(str_display);
					break;
				case R.id.dotfunc:
					if (isdot(str_display.toString())) {
						if (str_display.length() == 0) {
							str_display.append("0.");
							showText(str_display);
						} else {
							str_display.append(".");
							showText(str_display);
						}
					}
					break;

				case R.id.del:
					if (str_display.length() != 0) {
						// С������Ҫ�����ж�
						// A.1
						if (str_display.length() >= 3) {
							// A.1.1
							if (str_display.toString().startsWith(".",
									str_display.length() - 2)) {
								// ɾ��С�������
								str_display
										.deleteCharAt(str_display.length() - 1);
								str_display
										.deleteCharAt(str_display.length() - 1);
							} else if (str_display.toString().startsWith("s",
									str_display.length() - 2)
									|| str_display.toString().startsWith("c",
											str_display.length() - 2)
									|| str_display.toString().startsWith("t",
											str_display.length() - 2)
									|| str_display.toString().startsWith("l",
											str_display.length() - 2)
									|| str_display.toString().startsWith("g",
											str_display.length() - 2)
									|| str_display.toString().startsWith("��",
											str_display.length() - 2)) {
								str_display
										.deleteCharAt(str_display.length() - 1);
								str_display
										.deleteCharAt(str_display.length() - 1);
							} else {
								// A.1.3
								str_display
										.deleteCharAt(str_display.length() - 1);
							}

						} else {
							// A.2

							str_display.deleteCharAt(str_display.length() - 1);
							if (str_display.length() > 0
									&& (str_display
											.charAt(str_display.length() - 1) == 's'
											|| str_display.charAt(str_display
													.length() - 1) == 'c'
											|| str_display.charAt(str_display
													.length() - 1) == 't'
											|| str_display.charAt(str_display
													.length() - 1) == 'l'
											|| str_display.charAt(str_display
													.length() - 1) == 'g' || str_display
											.charAt(str_display.length() - 1) == '��'

									))
								str_display
										.deleteCharAt(str_display.length() - 1);
						}

						if (str_display.length() == 0) {
							str_display.append("0");
							showText(str_display);
						} else {
							showText(str_display);
						}
					} else {
					}
					break;
				case R.id.ac:
					str_display.delete(0, str_display.length());
					str_display.append("0");
					showText(str_display);
					break;
				case R.id.jia:
					str_display.append("+");
					showText(str_display);
					break;
				case R.id.jian:
					str_display.append("-");
					showText(str_display);
					break;
				case R.id.cheng:
					str_display.append("*");
					showText(str_display);
					break;
				case R.id.chu:
					str_display.append("/");
					showText(str_display);
					break;
				case R.id.equal:
					// �ж�ʽ�ӵ������ԣ��������в������Ĳ���Ҫ����Ե�

					// ��Ҫ�ж����ڵ�ʽ���Ƿ����ֱ�ӽ�����������
					if (str_display.toString().equals("+")
							|| str_display.toString().equals("-")) {

						// ������������ϼ�������������ܽ��н������ļ���
					} else {
						if (str_display.charAt(str_display.length() - 1) == '.'
								|| str_display.charAt(str_display.length() - 1) == '+'
								|| str_display.charAt(str_display.length() - 1) == '-'
								|| str_display.charAt(str_display.length() - 1) == '*'
								|| str_display.charAt(str_display.length() - 1) == '/'
								|| str_display.charAt(str_display.length() - 1) == '=') {
							str_display.deleteCharAt(str_display.length() - 1);
						}
						// �жϲ���������Ӵ�
						dosth();
						// �ж����������Ƿ��������ı��ʽ
						// ������ֺ�����֮��ֱ��������������Զ�����˺�
						checkbracket_others(str_display);
						// Toast to1 = Toast.makeText(getBaseContext(),
						// "��Ҫ������ַ�����" + str_display, Toast.LENGTH_SHORT);
						// to1.show();
						calculateString(str_display);
					}
					break;

				case R.id.leftbracket:
					str_display.append("(");
					showText(str_display);
					break;
				case R.id.rightbracket:
					if (str_display.toString().equals("")) {
						// ���ܿ�ͷ����
					} else {
						str_display.append(")");
						showText(str_display);
					}
					break;
				case R.id.mi:
					// ����ǰ���У�����������ǰ����ַ�����������
					if (str_display.length() > 0) {
						if (equljuge(str_display
								.charAt(str_display.length() - 1))) {

							str_display.append("^");
							showText(str_display);
						}
					}
					break;
				case R.id.jiecheng:
					if (str_display.length() > 0) {
						if (equljuge(str_display
								.charAt(str_display.length() - 1))) {
							str_display.append("!");
							showText(str_display);
						}
					}
					break;
				case R.id.kai:
					str_display.append("��");
					showText(str_display);
					break;
				case R.id.pai:
					str_display.append("��");
					showText(str_display);
					break;

				case R.id.E:
					str_display.append("e");
					showText(str_display);
					break;
				case R.id.sin:
					str_display.append("s");
					showText(str_display);
					break;
				case R.id.cos:
					str_display.append("c");
					showText(str_display);
					break;
				case R.id.tan:
					str_display.append("t");
					showText(str_display);
					break;
				case R.id.ln:
					str_display.append("l");
					showText(str_display);
					break;
				case R.id.log:
					str_display.append("g");
					showText(str_display);
					break;
				default:
					break;
				}

			}
		};

		ACbutton.setOnClickListener(listener);
		delbutton.setOnClickListener(listener);
		but0.setOnClickListener(listener);
		but1.setOnClickListener(listener);
		but2.setOnClickListener(listener);
		but3.setOnClickListener(listener);
		but4.setOnClickListener(listener);
		but5.setOnClickListener(listener);
		but6.setOnClickListener(listener);
		but7.setOnClickListener(listener);
		but8.setOnClickListener(listener);
		but9.setOnClickListener(listener);
		butjia.setOnClickListener(listener);
		butjian.setOnClickListener(listener);
		butcheng.setOnClickListener(listener);
		butchu.setOnClickListener(listener);
		butdot.setOnClickListener(listener);
		butequal.setOnClickListener(listener);
		leftbracket.setOnClickListener(listener);
		rightbracket.setOnClickListener(listener);

		// ������ʽ2���ı����-----------------------------------------------------------------

		listener2 = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub

				switch (arg0.getId()) {
				case R.id.sw:
					if (!sw.isChecked()) {
						Intent i = new Intent(MainActivity.this,
								MainActivity_Simple.class);
						Bundle bundle = new Bundle();
						bundle.putBoolean("viberstate", VIB_STATE);
						bundle.putBoolean("musicstate", MUSIC_STATE);
						bundle.putInt("scale", scale);
						bundle.putString("showString", str_display.toString());
						i.putExtras(bundle);
						startActivity(i);
						finish();
					}

					break;

				default:
					break;
				}

			}
		};

		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// ����
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// ����
			sw = (Switch) findViewById(R.id.sw);
			sw.setChecked(true);// ����ѡ��

			sw.setOnCheckedChangeListener(listener2);

			butpower = (Button) findViewById(R.id.mi);
			butpower.setOnClickListener(listener);
			butextract = (Button) findViewById(R.id.kai);
			butextract.setOnClickListener(listener);
			butpai = (Button) findViewById(R.id.pai);
			butpai.setOnClickListener(listener);
			butfactorial = (Button) findViewById(R.id.jiecheng);
			butfactorial.setOnClickListener(listener);
			butE = (Button) findViewById(R.id.E);
			butE.setOnClickListener(listener);
			butsin = (Button) findViewById(R.id.sin);
			butsin.setOnClickListener(listener);
			butcos = (Button) findViewById(R.id.cos);
			butcos.setOnClickListener(listener);
			buttan = (Button) findViewById(R.id.tan);
			buttan.setOnClickListener(listener);
			butln = (Button) findViewById(R.id.ln);
			butln.setOnClickListener(listener);
			butlog = (Button) findViewById(R.id.log);
			butlog.setOnClickListener(listener);
		}

	}

	// ==============================================================
	// �ֻ��˵�������ʵ�ֹ���================================================
	// ===============================================================

	/**
	 * 1.���ò˵�����������
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			// ��������������������
			super.openOptionsMenu(); // ����������Ϳ��Ե����˵�
		}
		return true; // ���һ��Ҫ�����Ժ󷵻� true�������ڵ����˵��󷵻�true������������super����������Ĭ��
	}

	/**
	 * 2.���ò˵����رպ���
	 * */

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// ��������������������

		super.onOptionsMenuClosed(menu);
	}

	// Ȼ���Ƕ�menu�˵������ã����£�

	/**
	 * ���ò˵����򿪺���
	 * */
	@Override
	public void openOptionsMenu() {
		super.openOptionsMenu();
	}

	// �˵��Ľ���
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, 1, 0, "����");
		menu.add(0, 2, 1, "����");
		menu.add(0, 3, 2, "����Vi");
		menu.add(0, 4, 3, "����Vi");
		menu.add(0, 5, 4, "�˳�");

		return true;

	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 1: // ���ù���

			// ��Ҫ�ص�Intent��Ĳ���
			Intent i = new Intent(getBaseContext(), Activity_Set.class);
			// ��Ҫ��Ŀǰ������״̬����Intent����
			Bundle bundle = new Bundle();
			bundle.putBoolean("viberstate", VIB_STATE);
			bundle.putBoolean("musicstate", MUSIC_STATE);
			bundle.putInt("scale", scale);
			i.putExtras(bundle);
			startActivityForResult(i, REQUEST_CODE);

			break;

		case 2: // ��������

			showDialog(1);

			break;
		case 3:
			// һ��������
			StartShareApp(MainActivity.this, "����", "����",
					"һ��ܺõļ��������--Vi������,�ܲ���!�Ƽ�����,�Ͽ�������Ŷ!");
			break;
		case 4:
			// ���۹���
			Uri uri = Uri.parse("market://details?id=" + getPackageName());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		case 5:
			finish();
			break;

		default:

			return super.onOptionsItemSelected(item);

		}

		return true;
	}

	// ����һ��������

	static public void StartShareApp(Context context,
			final String szChooserTitle, final String title, final String msg) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, msg);
		context.startActivity(Intent.createChooser(intent, szChooserTitle));
	}

	// ���������з������Ĳ���
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// ��������
				Bundle bundle = data.getExtras();

				VIB_STATE = bundle.getBoolean("viberstate");
				MUSIC_STATE = bundle.getBoolean("musicstate");
				scale = bundle.getInt("scale");
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
