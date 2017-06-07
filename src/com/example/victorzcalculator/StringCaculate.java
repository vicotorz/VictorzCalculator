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
 * @data:2014-11-10--13�޸����
 * @author �ŵ�
 */
public class StringCaculate {

	// ������
	Context context = null;

	private Stack<BigDecimal> numbers = new Stack<BigDecimal>();// ���ֶ�

	private Stack<Character> chs = new Stack<Character>();// ������

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
	 * 1A.�Ƚϵ�ǰ��������ջ��Ԫ�ز��������ȼ��������ջ��Ԫ�����ȼ��ߣ��򷵻�true�����򷵻�false
	 * 
	 * @param str
	 *            ��Ҫ���бȽϵ��ַ�
	 * @return �ȽϽ�� true�����ջ��Ԫ�����ȼ��ߣ�false�����ջ��Ԫ�����ȼ���
	 */
	private boolean compare(char str) {
		if (chs.empty()) {
			// ��Ϊ��ʱ����Ȼ ��ǰ���ȼ���ͣ����ظ�
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
			// '*/'���ȼ�ֻ��'+-'��
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
		// '+-'Ϊ��ͣ�һֱ����false
		case '+':
			return false;
		case '-':
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 2B. ��calculate����������
	 * 
	 * ��һЩ�߼��жϹ���
	 * */

	public BigDecimal caculate(String st) {
		// ��֤�������Ķ���˫Ŀ����
		StringBuffer sb = new StringBuffer(specialcalculatefirst(st));
		StringBuffer num = new StringBuffer();
		String tem = null;
		char next;

		while (sb.length() > 0) {
			tem = sb.substring(0, 1);// ��ȡ�ַ����ĵ�һ���ַ�
			sb.delete(0, 1);

			if (isNum(tem.trim()) || ".".equals(tem)) {
				// A.1���������
				num.append(tem);// ��������֣��������num����
			} else {
				// A.2�����������
				// ��ʱ����������ǣ���ǰ���ַ��жϲ�������

				// A.2.1�����ȡ���Ĳ������ֵ�ʱ��
				/* ����1 --�����������ж� */
				if (num.length() > 0 && !"".equals(num.toString().trim())) {
					// ����ȡ���ַ���������ʱ������Ϊnum�з��õ�ʱһ�����������֣�
					// ��123+1,����ȡ��+ʱ��ǰ���123������Ϊ��һ����������
					BigDecimal bd = new BigDecimal(num.toString().trim());
					numbers.push(bd);// һ������������
					num.delete(0, num.length());
				}

				// A.2.2�ַ���Ϊ�յ�ʱ��
				// ���chsΪ�գ�����Ϊ��ʱ��һ���ַ�ֱ�ӷ���
				/* ����2 --���ʱ��Ӧ���õ�ǰ������ż��㣬���ǲ�ȷ���ܲ����ã���һ����������е���� */
				if (!chs.isEmpty()) {
					// ����,1+2+3,����ȡ��2,3֮��ġ�+����1,2֮���"+"���ȼ����ʱ�������ȼ���1+2��ʹ����3+3
					// ͬ����1*2+3,����ȡ��2,3֮��ġ�+����1,2֮���"*"���ȼ�С�������ȼ���1*2��ʹ����2+3

					// �Ȱ����ȼ���ļ������

					while (!compare(tem.charAt(0))) {
						caculate();// ��ʼ����
					}

				}
				/* ����3-- */
				// ������ջҲΪ��ʱ,������ʽ�ĵ�һ������Ϊ����,������ʱ
				if (numbers.isEmpty()) {
					// ����ж���ʵûɶ��
					num.append(tem);
				} else {
					chs.push(new Character(tem.charAt(0)));
				}
				/* ����4 */
				// �жϺ�һ���ַ��Ƿ�Ϊ��-���ţ�Ϊ"-"��ʱ����Ϊ����Ϊ����
				// ���� 1*2*(-5)����Ϊ�����㲻����()����˽�����дΪ1*2*-5,���������뽫"-"��Ϊ�Ǹ������ʽ���Ǽ���

				next = sb.charAt(0);
				if (next == '-' || next == '+') {
					num.append(next);
					sb.delete(0, 1);
				}

			}
		}

		// �������3��,�Ͳ��ܱ�֤�������num��ֵ�����Ի�����Ҫ����ж�
		// num���������һ����
		// ����ǰ�潫���ַ���ջʱ����ͨ����ȡ����Ϊʱ�����������һ������û�з���ջ�У���˽��������ַ���ջ��
		BigDecimal bd = new BigDecimal(num.toString().trim());
		numbers.push(bd);
		// ��ʱ����ջ�����ֻ��2�����ţ�����ջ���÷������ȼ��ߣ�������
		while (!chs.isEmpty()) {
			caculate();
		}

		return numbers.pop();
	}

	/**
	 * 3C.��һ�ּ��㷽ʽ
	 * 
	 * ������������
	 * **/

	private void caculate() {
		// ���ֵ���Ϊ�յĴ���numbersΪ��
		BigDecimal b = numbers.pop();// �����ڶ���������
		BigDecimal a = null;// ��һ��������
		a = numbers.pop();// ������һ��������
		//
		char ope = chs.pop();
		BigDecimal result = null;// ������
		switch (ope) {
		// ����ǼӺŻ��߼��ţ���
		case '+':
			result = a.add(b);
			// ������������������ջ
			numbers.push(result);
			break;
		case '-':
			// ������������������ջ
			result = a.subtract(b);
			numbers.push(result);
			break;
		case '*':
			result = a.multiply(b);
			// ������������������ջ
			numbers.push(result);
			break;
		case '/':
			// ������������������ջ
			result = a.divide(b, 6, BigDecimal.ROUND_DOWN);
			if (a.doubleValue() != 0
					&& result.intValue() == result.doubleValue()) {
				try {
					throw new Exception();
				} catch (Exception e) {
					// TODO Auto-generated catch block

					Toast to1 = Toast.makeText(context, "��ȷ�Ȳ���",
							Toast.LENGTH_SHORT);
					to1.show();
				}
			}

			numbers.push(new BigDecimal(a.divide(b, 2, BigDecimal.ROUND_DOWN)
					.doubleValue()));

			break;

		case '^':
			// System.out.println("��ȥ��");
			Double d = new Double(Math.pow(a.doubleValue(), b.doubleValue()));
			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			// System.out.println("d:=" + nf.format(d));
			numbers.push(new BigDecimal(nf.format(d)).setScale(6));
			// System.out.println("������");
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
		case '��':
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

	// ���ؾ�������Ĳ������ֵ�λ��
	public int loc(String str, int loc, char to_lr) {
		int lo = -1;
		if (to_lr == 'l') {
			// (��ǰ����)
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
			// (������)
			boolean flag = false;
			to: for (int i = loc + 1; i < str.length(); i++) {
				// ����ľ�û�У�ֱ�ӵ�ͷ��
				if (str.charAt(i) == '+' || str.charAt(i) == '-'
						|| str.charAt(i) == '*' || str.charAt(i) == '/') {
					try {
						if ((str.charAt(i - 1) == '��'
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
						flag = false;// ��׼������
					} else {
						flag = true;// �����ǽ�ȥ��
					}
				}

				if (((str.charAt(i) != '0' && str.charAt(i) != '1'
						&& str.charAt(i) != '2' && str.charAt(i) != '3'
						&& str.charAt(i) != '4' && str.charAt(i) != '5'
						&& str.charAt(i) != '6' && str.charAt(i) != '7'
						&& str.charAt(i) != '8' && str.charAt(i) != '9' && str
							.charAt(i) != '.') || flag)) {
					// �����Ķ��ǲ��е�
					// ������������ģ����������
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

	/* �Ų����е�Ŀ���㣬���磡,��,s,c,t,l,g,,�У�e */
	public String specialcalculatefirst(String string) {
		// ��Ŀ����ʵ�������ַ�����һЩ�������
		// ̽������ж���Ӧ���ַ�������
		// ������ڵĻ�

		String str = string;
		int end = -1;
		if ((end = str.indexOf('��')) > 0 || str.indexOf('��') == 0) {
			str = str.replaceAll("��", "3.1415926535898");
		}

		if ((end = str.indexOf("e")) > 0 || str.indexOf('e') == 0) {
			str = str.replaceAll("e", "2.718281828459");
		}

		while ((end = str.indexOf("��")) > 0 || str.indexOf('��') == 0) {

			int first = loc(str, end, 'r');// ������
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
					.replaceAll(oldStr, specialcaculate(value, '��').toString());
		}

		// ������Ļ���ֲ�������0λ�ó��ֵ����
		while ((end = str.indexOf('s')) > 0 || str.indexOf('s') == 0) {
			int first = loc(str, end, 'r');// ������
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

			int first = loc(str, end, 'r');// ������
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

			int first = loc(str, end, 'r');// ������
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

			int first = loc(str, end, 'r');// ������
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

			int first = loc(str, end, 'r');// ������
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
			int first = loc(str, end, 'l');// ��ǰ����
			String oldStr = str.substring(first, end + 1);
			BigDecimal value;
			if (first == end - 1) {

				value = new BigDecimal(Double.parseDouble(String.valueOf(str
						.charAt(first))));
			} else {
				value = new BigDecimal(Double.parseDouble(String.valueOf(str
						.substring(first, end))));
			}

			// û���滻�ɹ�

			str = str
					.replaceAll(oldStr, specialcaculate(value, '!').toString());
		}

		return str;

	}

	/**
	 * 4D. ��������
	 * 
	 * ���������������ŵ���������û�д����ŵ������� ȥ����
	 */
	public BigDecimal parse(String st) {
		int start = 0;
		StringBuffer sts = new StringBuffer(st);
		int end = -1;
		while ((end = sts.indexOf(")")) > 0) {
			String s = sts.substring(start, end + 1);
			int first = s.lastIndexOf("(");
			// ȷ��������index�ķ�Χ
			// ���������е�ֵ(���õ��Ǵ�������caculate����)
			BigDecimal value = caculate(sts.substring(first + 1, end));
			// �������ֵ�滻
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