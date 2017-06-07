package com.example.victorzcalculator;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Stack;

import android.content.Context;
import android.widget.Toast;

/**
 * 
 * 
 * @version 1.0
 * @data:2014-11-10--13修改完成
 * @author 张迪
 */
public class StringCaculate {

	// 上下文
	Context context = null;

	private Stack<BigDecimal> numbers = new Stack<BigDecimal>();// 数字堆

	private Stack<Character> chs = new Stack<Character>();// 操作堆

	public StringCaculate(MainActivity mainactivity) {
		// TODO Auto-generated constructor stub
		context = mainactivity;

	}

	public StringCaculate(MainActivity_Simple mainactivity) {
		// TODO Auto-generated constructor stub
		context = mainactivity;
	}

	public StringCaculate(Activity_only_land mainactivity) {
		// TODO Auto-generated constructor stub
	}

	private boolean isNum(String num) {
		return num.matches("[0-9]");
	}

	/**
	 * 1A.比较当前操作符与栈顶元素操作符优先级，如果比栈顶元素优先级高，则返回true，否则返回false
	 * 
	 * @param str
	 *            需要进行比较的字符
	 * @return 比较结果 true代表比栈顶元素优先级高，false代表比栈顶元素优先级低
	 */
	private boolean compare(char str) {
		if (chs.empty()) {
			// 当为空时，显然 当前优先级最低，返回高
			return true;
		}
		char last = (char) chs.lastElement();
		switch (str) {
		case '^': {
			if (last == '+' || last == '-')
				return true;
			else
				return false;
		}
		case '*': {
			// '*/'优先级只比'+-'高
			if (last == '+' || last == '-')
				return true;
			else
				return false;
		}
		case '/': {
			if (last == '+' || last == '-')
				return true;
			else
				return false;
		}
		// '+-'为最低，一直返回false
		case '+':
			return false;
		case '-':
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 2B. 算calculate带参数方法
	 * 
	 * 做一些逻辑判断过程
	 * */

	public BigDecimal caculate(String st) {
		// 保证接下来的都是双目计算
		StringBuffer sb = new StringBuffer(specialcalculatefirst(st));
		StringBuffer num = new StringBuffer();
		String tem = null;
		char next;

		while (sb.length() > 0) {
			tem = sb.substring(0, 1);// 获取字符串的第一个字符
			sb.delete(0, 1);

			if (isNum(tem.trim()) || ".".equals(tem)) {
				// A.1如果是数字
				num.append(tem);// 如果是数字，将其放入num当中
			} else {
				// A.2如果不是数字
				// 这时遇到了情况是，当前的字符判断不是数字

				// A.2.1如果截取到的不是数字的时候
				/* 操作1 --数字完整性判断 */
				if (num.length() > 0 && !"".equals(num.toString().trim())) {
					// 当截取的字符不是数字时，则认为num中放置的时一个完整的数字，
					// 如123+1,当获取到+时，前面的123可以认为是一个完整的数
					BigDecimal bd = new BigDecimal(num.toString().trim());
					numbers.push(bd);// 一个完整的数字
					num.delete(0, num.length());
				}

				// A.2.2字符串为空的时候
				// 如果chs为空，这认为这时第一个字符直接放入
				/* 操作2 --这个时候应该用当前这个符号计算，但是不确定能不能用，看一下运算符堆中的情况 */
				if (!chs.isEmpty()) {
					// 例如,1+2+3,当截取到2,3之间的“+”与1,2之间的"+"优先级相等时，可以先计算1+2，使其变成3+3
					// 同样，1*2+3,当截取到2,3之间的“+”与1,2之间的"*"优先级小，可以先计算1*2，使其变成2+3

					// 先把优先级大的计算出来

					while (!compare(tem.charAt(0))) {
						caculate();// 开始计算
					}

				}
				/* 操作3-- */
				// 当数字栈也为空时,既运算式的第一个数字为负数,或正数时
				if (numbers.isEmpty()) {
					// 这个判断其实没啥用
					num.append(tem);
				} else {
					chs.push(new Character(tem.charAt(0)));
				}
				/* 操作4 */
				// 判断后一个字符是否为“-”号，为"-"号时，认为数字为负数
				// 例如 1*2*(-5)，因为此运算不计算()，因此将被改写为1*2*-5,如此情况，须将"-"认为是负数表达式而非减号

				next = sb.charAt(0);
				if (next == '-' || next == '+') {
					num.append(next);
					sb.delete(0, 1);
				}

			}
		}

		// 如果遇到3！,就不能保证到这里的num有值，所以还是需要添加判断
		// num现在是最后一个数
		// 由于前面将数字放入栈时，是通过获取符号为时处理，导致最后一个数字没有放入栈中，因此将最后的数字放入栈中
		BigDecimal bd = new BigDecimal(num.toString().trim());
		numbers.push(bd);
		// 此时符号栈上最多只有2个符号，并且栈顶得符号优先级高，做运算
		while (!chs.isEmpty()) {
			caculate();
		}

		return numbers.pop();
	}

	/**
	 * 3C.另一种计算方式
	 * 
	 * 两个数的运算
	 * **/

	private void caculate() {
		// 出现弹出为空的错误，numbers为空
		BigDecimal b = numbers.pop();// 弹出第二个运算数
		BigDecimal a = null;// 第一个运算数
		a = numbers.pop();// 弹出第一个运算数
		//
		char ope = chs.pop();
		BigDecimal result = null;// 运算结果
		switch (ope) {
		// 如果是加号或者减号，则
		case '+':
			result = a.add(b);
			// 将操作结果放入操作数栈
			numbers.push(result);
			break;
		case '-':
			// 将操作结果放入操作数栈
			result = a.subtract(b);
			numbers.push(result);
			break;
		case '*':
			result = a.multiply(b);
			// 将操作结果放入操作数栈
			numbers.push(result);
			break;
		case '/':
			// 将操作结果放入操作数栈
			result = a.divide(b, 6, BigDecimal.ROUND_DOWN);
			if (a.doubleValue() != 0
					&& result.intValue() == result.doubleValue()) {
				try {
					throw new Exception();
				} catch (Exception e) {
					// TODO Auto-generated catch block

					Toast to1 = Toast.makeText(context, "精确度不足",
							Toast.LENGTH_SHORT);
					to1.show();
				}
			}

			numbers.push(new BigDecimal(a.divide(b, 2, BigDecimal.ROUND_DOWN)
					.doubleValue()));

			break;

		case '^':
			// System.out.println("进去了");
			Double d = new Double(Math.pow(a.doubleValue(), b.doubleValue()));
			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			// System.out.println("d:=" + nf.format(d));
			numbers.push(new BigDecimal(nf.format(d)).setScale(6));
			// System.out.println("出来了");
			break;

		}

	}

	public BigDecimal specialcaculate(BigDecimal a, char c) {
		BigDecimal result = null;
		switch (c) {
		case '!':
			Double d = new Double(Gamma(a.doubleValue()));
			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			result = new BigDecimal(nf.format(d));
			break;
		case '√':
			result = new BigDecimal(Math.sqrt(a.doubleValue())).setScale(6,
					BigDecimal.ROUND_HALF_UP);
			break;
		case 's':

			result = new BigDecimal(Math.sin(a.doubleValue() * Math.PI / 180))
					.setScale(6, BigDecimal.ROUND_HALF_UP);
			break;
		case 'c':
			result = new BigDecimal(Math.cos(a.doubleValue() * Math.PI / 180))
					.setScale(6, BigDecimal.ROUND_HALF_UP);
			break;
		case 't':
			result = new BigDecimal(Math.tan(a.doubleValue() * Math.PI / 180))
					.setScale(6, BigDecimal.ROUND_HALF_UP);
			break;
		case 'l':
			result = new BigDecimal(Math.log(a.doubleValue())).setScale(6,
					BigDecimal.ROUND_HALF_UP);
			break;
		case 'g':
			result = new BigDecimal(Math.log10(a.doubleValue())).setScale(6,
					BigDecimal.ROUND_HALF_UP);
			break;
		default:
			break;
		}
		return result;

	}

	// 返回距离最近的不是数字的位置
	public int loc(String str, int loc, char to_lr) {
		int lo = -1;
		if (to_lr == 'l') {
			// (向前查找)
			one: for (int i = loc - 1; i >= 0; i--) {
				if ((str.charAt(i) != '0' && str.charAt(i) != '1'
						&& str.charAt(i) != '2' && str.charAt(i) != '3'
						&& str.charAt(i) != '4' && str.charAt(i) != '5'
						&& str.charAt(i) != '6' && str.charAt(i) != '7'
						&& str.charAt(i) != '8' && str.charAt(i) != '9'
						&& str.charAt(i) != '.' && str.charAt(i) != '+' && str
						.charAt(i) != '-') || (i == 0)) {

					if (i == 0) {
						lo = i;
						break one;
					} else {
						lo = i + 1;
						break one;
					}

				}
			}
		} else {
			// (向后查找)
			boolean flag = false;
			to: for (int i = loc + 1; i < str.length(); i++) {
				// 或真的就没有，直接到头了
				if (str.charAt(i) == '+' || str.charAt(i) == '-'
						|| str.charAt(i) == '*' || str.charAt(i) == '/') {
					try {
						if ((str.charAt(i - 1) == '√'
								|| str.charAt(i - 1) == 'l' || str
								.charAt(i - 1) == 'g') && str.charAt(i) == '-') {
							throw new Exception();
						}
					} catch (Exception e) {
					}

					if (str.charAt(i - 1) != '0' && str.charAt(i - 1) != '1'
							&& str.charAt(i - 1) != '2'
							&& str.charAt(i - 1) != '3'
							&& str.charAt(i - 1) != '4'
							&& str.charAt(i - 1) != '5'
							&& str.charAt(i - 1) != '6'
							&& str.charAt(i - 1) != '7'
							&& str.charAt(i - 1) != '8'
							&& str.charAt(i - 1) != '9') {
						flag = false;// 不准备进入
					} else {
						flag = true;// 这种是进去的
					}
				}

				if (((str.charAt(i) != '0' && str.charAt(i) != '1'
						&& str.charAt(i) != '2' && str.charAt(i) != '3'
						&& str.charAt(i) != '4' && str.charAt(i) != '5'
						&& str.charAt(i) != '6' && str.charAt(i) != '7'
						&& str.charAt(i) != '8' && str.charAt(i) != '9' && str
							.charAt(i) != '.') || flag)) {
					// 进来的都是不行的
					// 这样是有问题的，有特殊情况
					lo = i - 1;
					break to;
				}
				if (i == str.length() - 1) {
					lo = i;
					break to;
				}
			}

		}
		return lo;

	}

	/* 排查所有单目运算，比如！,√,s,c,t,l,g,,π，e */
	public String specialcalculatefirst(String string) {
		// 单目运算实际上是字符串的一些具体操作
		// 探后具体判断相应的字符串即可
		// 如果存在的话

		String str = string;
		int end = -1;
		if ((end = str.indexOf('π')) > 0 || str.indexOf('π') == 0) {
			str = str.replaceAll("π", "3.1415926535898");
		}

		if ((end = str.indexOf("e")) > 0 || str.indexOf('e') == 0) {
			str = str.replaceAll("e", "2.718281828459");
		}

		while ((end = str.indexOf("√")) > 0 || str.indexOf('√') == 0) {

			int first = loc(str, end, 'r');// 向后查找
			String oldStr = str.substring(end, first + 1);
			BigDecimal value;
			if (end == first - 1) {

				value = new BigDecimal(Double.parseDouble(String.valueOf(str
						.charAt(first))));
			} else {
				value = new BigDecimal(Double.parseDouble(String.valueOf(str
						.substring(end + 1, first + 1))));
			}

			str = str
					.replaceAll(oldStr, specialcaculate(value, '√').toString());
		}

		// 这里真的会出现操作符在0位置出现的情况
		while ((end = str.indexOf('s')) > 0 || str.indexOf('s') == 0) {
			int first = loc(str, end, 'r');// 向后查找
			String oldStr = str.substring(end, first + 1);

			BigDecimal value;
			if (end == first - 1) {
				value = new BigDecimal(Double.parseDouble(String.valueOf(str
						.charAt(first))));
			} else {
				value = new BigDecimal(Double.parseDouble(String.valueOf(str
						.substring(end + 1, first + 1))));
			}
			str = str
					.replaceAll(oldStr, specialcaculate(value, 's').toString());
		}

		while ((end = str.indexOf("c")) > 0 || str.indexOf('c') == 0) {

			int first = loc(str, end, 'r');// 向后查找
			String oldStr = str.substring(end, first + 1);
			BigDecimal value;
			if (end == first - 1) {
				value = new BigDecimal(Double.parseDouble(String.valueOf(str
						.charAt(first))));
			} else {
				value = new BigDecimal(Double.parseDouble(String.valueOf(str
						.substring(end + 1, first + 1))));
			}
			str = str
					.replaceAll(oldStr, specialcaculate(value, 'c').toString());
		}
		while ((end = str.indexOf("t")) > 0 || str.indexOf('t') == 0) {

			int first = loc(str, end, 'r');// 向后查找
			String oldStr = str.substring(end, first + 1);
			BigDecimal value;
			if (end == first - 1) {
				value = new BigDecimal(Double.parseDouble(String.valueOf(str
						.charAt(first))));
			} else {
				value = new BigDecimal(Double.parseDouble(String.valueOf(str
						.substring(end + 1, first + 1))));
			}
			str = str
					.replaceAll(oldStr, specialcaculate(value, 't').toString());
		}
		while ((end = str.indexOf("l")) > 0 || str.indexOf('l') == 0) {

			int first = loc(str, end, 'r');// 向后查找
			String oldStr = str.substring(end, first + 1);
			BigDecimal value;
			if (end == first - 1) {
				value = new BigDecimal(Double.parseDouble(String.valueOf(str
						.charAt(first))));
			} else {
				value = new BigDecimal(Double.parseDouble(String.valueOf(str
						.substring(end + 1, first + 1))));
			}
			str = str
					.replaceAll(oldStr, specialcaculate(value, 'l').toString());
		}
		while ((end = str.indexOf("g")) > 0 || str.indexOf('g') == 0) {

			int first = loc(str, end, 'r');// 向后查找
			String oldStr = str.substring(end, first + 1);
			BigDecimal value;
			if (end == first - 1) {
				value = new BigDecimal(Double.parseDouble(String.valueOf(str
						.charAt(first))));
			} else {
				value = new BigDecimal(Double.parseDouble(String.valueOf(str
						.substring(end + 1, first + 1))));
			}
			str = str
					.replaceAll(oldStr, specialcaculate(value, 'g').toString());
		}

		while ((end = str.indexOf("!")) > 0) {
			int first = loc(str, end, 'l');// 向前查找
			String oldStr = str.substring(first, end + 1);
			BigDecimal value;
			if (first == end - 1) {

				value = new BigDecimal(Double.parseDouble(String.valueOf(str
						.charAt(first))));
			} else {
				value = new BigDecimal(Double.parseDouble(String.valueOf(str
						.substring(first, end))));
			}

			// 没有替换成功

			str = str
					.replaceAll(oldStr, specialcaculate(value, '!').toString());
		}

		return str;

	}

	/**
	 * 4D. 功能描述
	 * 
	 * 解析，将带有括号的运算符变成没有带括号的字运算 去括号
	 */
	public BigDecimal parse(String st) {
		int start = 0;
		StringBuffer sts = new StringBuffer(st);
		int end = -1;
		while ((end = sts.indexOf(")")) > 0) {
			String s = sts.substring(start, end + 1);
			int first = s.lastIndexOf("(");
			// 确定了括号index的范围
			// 计算括号中的值(调用的是带参数的caculate方法)
			BigDecimal value = caculate(sts.substring(first + 1, end));
			// 将计算的值替换
			sts.replace(first, end + 1, value.toString());
		}
		return caculate(sts.toString());
	}

	public double Gamma(double xx) {
		double x, y, tmp, ser;
		double[] cof = new double[] { 76.18009172947146, -86.50532032941677,
				24.01409824083091, -1.231739572450155, 0.1208650973866179e-2,
				-0.5395239384953e-5 };
		int j;
		x = xx;
		y = x;
		tmp = x + 5.5;
		tmp -= (x + 0.5) * Math.log(tmp);
		ser = 1.000000000190015;
		for (j = 0; j <= 5; j++) {
			ser += cof[j] / (++y);
		}

		return xx * Math.exp((-tmp + Math.log(2.5066282746310005 * ser / x)));
	}
}