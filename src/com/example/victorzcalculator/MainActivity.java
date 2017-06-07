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
	private int music;// 定义一个整型用load（）；来设置suondID
	// 对应的music Id的值
	boolean MUSIC_STATE = false;// 控制声音效果 TURE为开启声音，FALSE为关闭声音
	private Vibrator vibrator; // 震动效果器
	boolean VIB_STATE = false;// 用来控制震动效果的判断 TRUE是开震动 FALSE为关闭震动
	int scale = 2;// 计算精度

	EditText Edt;
	// 点击监听部分
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

	// 新加部分--------------------
	Button butpower;// 幂运算
	Button butextract;// 开方运算
	Button butpai;// PAI
	Button butfactorial;// 阶乘运算
	Button butE;// 自然对数E
	Button butsin;
	Button butcos;
	Button buttan;
	Button butln;
	Button butlog;

	OnClickListener listener;// 监听部分
	OnCheckedChangeListener listener2;// 值改变监听器

	Switch sw;// 值改变监听

	static final int REQUEST_CODE = 0;

	Button leftbracket;// 左括号
	Button rightbracket;// 右括号

	double number1;// 计算因子1
	double number2;// 计算因子2
	String act;// 进行的相关操作
	double result;// 结果

	// 这里的显示需要特殊处理
	StringBuffer str_display = new StringBuffer();// 显示作用

	// 判断是否为给定字符是不是数字
	public boolean isNumber(char c) {
		boolean flag = false;

		// 这里有点遗忘，不经常用
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
		case 'π':
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
		case 'π':
		case 'e':
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		return flag;
	}

	// 判断是否存在小数点的情况
	public boolean isdot(String str) {
		// 找到最后一个操作数，然后再进行相关的判断
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
			// 查找最后一个操作数
			return flag;
		} else {
			return flag;
		}

	}

	// 用于等号判断
	public boolean equljugecharge(int loc) {
		boolean flag = true;

		// TURE不显示 FALSE显示
		// 对于加号和减号，在开始部分只能输入一次（0是+，1就不行了）,在其他位置至多输入两次
		// loc≥2（loc-2，loc-1可以，loc不行了）

		if (loc > 1 && !equljuge(str_display.toString().charAt(loc - 1))) {
			flag = false;
		}
		return flag;
	}

	// 用于乘除的冗余判断
	public boolean charge(int loc) {
		boolean flag = true;

		// TURE不显示 FALSE显示
		// 对于加号和减号，在开始部分只能输入一次（0是+，1就不行了）,在其他位置至多输入两次
		// loc≥2（loc-2，loc-1可以，loc不行了）

		if (loc > 1 && !equljuge(str_display.toString().charAt(loc - 1))) {
			// 真
			flag = false;
		}
		return flag;
	}

	// 判断输入诸如：+10或-10有正负号的情况
	// 用于加减的冗余判断
	public boolean judge(int loc) {
		boolean flag = true;
		String keyString = str_display.toString();
		// 判断当前输入的加号和减号是否能继续输入
		// 真可以加
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

	// 特殊判断加号
	public boolean judge_plus(int loc) {
		// 需要特殊判断正好了，因为后面操作对加号有影响，所以禁止输入正号
		// 如果第一个位置不可以false
		// 如果前面是非数字的不可以false
		// 如果前面是数字可以true
		boolean flag = true;
		String keyString = str_display.toString();
		// 判断当前输入的加号和减号是否能继续输入
		// Toast to2 = Toast.makeText(getBaseContext(), "注意位置：" + loc,
		// Toast.LENGTH_SHORT);
		// to2.show();

		if (loc >= 1 && !equljuge(keyString.charAt(loc - 1))) {
			// Toast to1 = Toast.makeText(getBaseContext(), "2错",
			// Toast.LENGTH_SHORT);
			// to1.show();
			flag = false;
		}
		// Toast to1 = Toast.makeText(getBaseContext(), "判断加号" + flag,
		// Toast.LENGTH_SHORT);
		// to1.show();
		return flag;

	}

	// 调整和显示数字
	public void showText(StringBuffer str) {

		StringBuffer string = new StringBuffer();
		// string = str.toString();// 说明这样引用有问题，同时也改变了str过来的值

		// 需要这样赋值，不能让同一个对象公用一个堆空间

		char cs[] = str.toString().toCharArray();// 将String换成字符串数组
		// 转换成字符数组以后使用StringBuffer进行自动拼装
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
					// 不显示
					str_display.deleteCharAt(str_display.length() - 1);
				} else {
					string.append("×");
				}
				break;
			}
			case '/': {
				if (!charge(i)) {
					// 不显示
					str_display.deleteCharAt(str_display.length() - 1);
				} else {
					string.append("÷");
				}
				break;
			}

			case '=': {
				if (!equljugecharge(i)) {
					// 不显示
					str_display.deleteCharAt(str_display.length() - 1);
				} else {
					string.append("\n");
					string.append("=");
				}
				break;
			}
			// 需要特殊处理一下
			case '√': {
				string.append("√");
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

	// 处理括号不符合计算的情况
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
							|| cs[i + 1] == '√' || cs[i + 1] == 'π' || cs[i + 1] == 'e')) {
				// 自动添加乘号
				string.append(cs[i]);
				string.append('*');
			} else if (cs[i] == ')'
					&& (cs[i + 1] == '(' || cs[i + 1] == 's'
							|| cs[i + 1] == 'c' || cs[i + 1] == 't'
							|| cs[i + 1] == 'l' || cs[i + 1] == 'g'
							|| cs[i + 1] == '√' || cs[i + 1] == 'π' || cs[i + 1] == 'e')) {
				string.append(cs[i]);
				string.append('*');
			} else if (cs[i] == ')' && isNumber(cs[i + 1])) {
				// 按错误处理
				// 抛出异常（显示Error的情况）
				try {
					throw new Exception();
				} catch (Exception e) {
					Edt.setText("Error");
					str_display.delete(0, str_display.length());
					str_display.append("0");
				}
			} else if ((cs[i] == 'π' || cs[i] == 'e' || cs[i] == '!')
					&& isNumber(cs[i + 1])) {
				string.append(cs[i]);
				string.append("*");
			} else {
				// 正常拼接就行
				string.append(cs[i]);
			}
		}

		// 执行到这里有错误直接抛出异常
		string.append(cs[i]);

		// 处理string_display
		str_display.delete(0, str_display.length());
		str_display.append(string);

	}

	/**
	 * 处理等号后的操作数的刷新工作 确定下一个操作式子
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
	 * 计算字符串的式子 用到堆的知识，现在没有括号还好计算一些
	 * */

	public void calculateString(StringBuffer str) {

		// 在获得操作数a的时候，需要通过函数进行判断处理后进行赋值的操作
		String a = str.toString();
		StringCaculate caculate = new StringCaculate(mainactivity);
		// 已经处理掉了
		// str_display.delete(0, str_display.length());
		str_display.append("=");

		// BigDecimal.setScale(int val) 设置精度
		try {
			BigDecimal r = caculate.parse(a).setScale(scale,
					BigDecimal.ROUND_HALF_UP);

			// 排除显示诸如：2+3=5.00的情况。尽量去掉后面多余的精度，只在必要时显示
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

	// 绘制对话框功能,只是标准提示框
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1:
			return new AlertDialog.Builder(MainActivity.mainactivity)
					.setTitle("作者和反馈地址信息")
					.setMessage(
							"作者：吉林大学软件学院张迪\n"
									+ "意见反馈：victorzsnail@qq.com\n"
									+ "新浪微博：无影风magic\n"
									+ "版本信息：2.0\n"
									+ "版本介绍：Vi计算器2.0版本加入了一些常用的科学计算，增加了菜单选项，方便了用户使用。同时添加了计算器模式切换功能。可选择打开或关闭按键声音，按键震动，用户自定义计算精度，具备了更富互动性的一键分享和评价软件功能，另外加入了计算器横屏计算功能。\n"
									+ "其中阶乘运算支持可计算范围内的小数阶乘。")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton("确定", null).create();

		default:
			return null;

		}
	}

	// ===========================================================================================================
	/**
	 * 正式开始部分
	 * 
	 * **/

	// 创建绘出组件的部分
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
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

		// 左右括号的R文件主注册获取
		leftbracket = (Button) findViewById(R.id.leftbracket);
		rightbracket = (Button) findViewById(R.id.rightbracket);

		// 监听方式1，点击监听-----------------------------------------------------------------

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
						// 小数点需要特殊判断
						// A.1
						if (str_display.length() >= 3) {
							// A.1.1
							if (str_display.toString().startsWith(".",
									str_display.length() - 2)) {
								// 删除小数点情况
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
									|| str_display.toString().startsWith("√",
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
											.charAt(str_display.length() - 1) == '√'

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
					// 判断式子的完整性，如果最后有不完整的部分要求忽略掉

					// 需要判断现在的式子是否符合直接进入计算的条件
					if (str_display.toString().equals("+")
							|| str_display.toString().equals("-")) {

						// 这种情况不符合计算的条件，不能进行接下来的计算
					} else {
						if (str_display.charAt(str_display.length() - 1) == '.'
								|| str_display.charAt(str_display.length() - 1) == '+'
								|| str_display.charAt(str_display.length() - 1) == '-'
								|| str_display.charAt(str_display.length() - 1) == '*'
								|| str_display.charAt(str_display.length() - 1) == '/'
								|| str_display.charAt(str_display.length() - 1) == '=') {
							str_display.deleteCharAt(str_display.length() - 1);
						}
						// 判断并处理计算子串
						dosth();
						// 判断最后出来的是否是完整的表达式
						// 检查数字和括号之间直接相连的情况，自动加入乘号
						checkbracket_others(str_display);
						// Toast to1 = Toast.makeText(getBaseContext(),
						// "将要计算的字符串是" + str_display, Toast.LENGTH_SHORT);
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
						// 不能开头就有
					} else {
						str_display.append(")");
						showText(str_display);
					}
					break;
				case R.id.mi:
					// 不能前面有，而且如果添加前面的字符必须是数字
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
					str_display.append("√");
					showText(str_display);
					break;
				case R.id.pai:
					str_display.append("π");
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

		// 监听方式2，改变监听-----------------------------------------------------------------

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
			// 横屏
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// 竖屏
			sw = (Switch) findViewById(R.id.sw);
			sw.setChecked(true);// 设置选中

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
	// 手机菜单按键的实现过程================================================
	// ===============================================================

	/**
	 * 1.设置菜单键触发函数
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			// 在这里做你想做的事情
			super.openOptionsMenu(); // 调用这个，就可以弹出菜单
		}
		return true; // 最后，一定要做完以后返回 true，或者在弹出菜单后返回true，其他键返回super，让其他键默认
	}

	/**
	 * 2.设置菜单键关闭函数
	 * */

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// 在这里做你想做的事情

		super.onOptionsMenuClosed(menu);
	}

	// 然后是对menu菜单的配置，如下：

	/**
	 * 设置菜单键打开函数
	 * */
	@Override
	public void openOptionsMenu() {
		super.openOptionsMenu();
	}

	// 菜单的建立
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, 1, 0, "设置");
		menu.add(0, 2, 1, "关于");
		menu.add(0, 3, 2, "分享Vi");
		menu.add(0, 4, 3, "评价Vi");
		menu.add(0, 5, 4, "退出");

		return true;

	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 1: // 设置功能

			// 需要回调Intent里的参数
			Intent i = new Intent(getBaseContext(), Activity_Set.class);
			// 需要将目前的设置状态交给Intent处理
			Bundle bundle = new Bundle();
			bundle.putBoolean("viberstate", VIB_STATE);
			bundle.putBoolean("musicstate", MUSIC_STATE);
			bundle.putInt("scale", scale);
			i.putExtras(bundle);
			startActivityForResult(i, REQUEST_CODE);

			break;

		case 2: // 关于我们

			showDialog(1);

			break;
		case 3:
			// 一键分享功能
			StartShareApp(MainActivity.this, "分享到", "分享到",
					"一款很好的计算器软件--Vi计算器,很不错!推荐给你,赶快来试试哦!");
			break;
		case 4:
			// 评价功能
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

	// 处理一键分享函数

	static public void StartShareApp(Context context,
			final String szChooserTitle, final String title, final String msg) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, msg);
		context.startActivity(Intent.createChooser(intent, szChooserTitle));
	}

	// 处理设置中返回来的参数
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// 处理数据
				Bundle bundle = data.getExtras();

				VIB_STATE = bundle.getBoolean("viberstate");
				MUSIC_STATE = bundle.getBoolean("musicstate");
				scale = bundle.getInt("scale");
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
