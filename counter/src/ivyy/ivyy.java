package ivyy;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.regex.*;


//C4





public class ivyy {
	String exps;
	ArrayList<String> list= new ArrayList<String>();
	String val;

	Pattern Number= Pattern.compile("^[0-9]+$");


	public void judge(String expression)
	{
		if(expression.length() >9 &&expression.substring(0,9).equals("!simplify"))   //给变量赋值
		{
			expression = expression.replaceAll("!simplify\\s*", "");
			String[] simp = expression.split("[=\\s]");
			boolean fl = false;
			for(int i=0;i<list.size();i++)
			{
				for(int j=0; j<simp.length; j+=2)
					if(list.get(i).equals(simp[j]))
						{
							list.set(i, simp[j+1]);
							fl = true;
						}
			}


			if(fl == true)
			{
				simplify();
				System.out.println(list);
			}
			else
				System.out.println("Error,no variable");

		}

		else if(expression.length() >3 && expression.substring(0,4).equals("!d/d"))		//求导
		{
			val = expression.replaceAll("!d/d\\s*", "");
			for(int i=0;i<list.size();i++)
			{
				if(list.get(i).equals(val))
					{
					derivative(i);
					break;
					}

			}
			simplify();
			System.out.println(list);

		}
		else					//输入的是表达式
		{
			expression = expression.replaceAll("\\s", "");
			exps = expression;
			expression();
			simplify();
			System.out.println(list);
		}
	}

	public void expression()		//处理表达式，存入list
	{
		int num = 0,num1;
		list.clear();
		for(int i=0;i<exps.length();i++)
		{
			if(exps.charAt(i) == '+' || exps.charAt(i) == '-' || exps.charAt(i) == '*')
			{
				num1 = i;
				list.add(exps.substring(num,num1));
				list.add(exps.substring(num1,num1+1));
				num = num1+1;
			}
			else if(i == exps.length()-1)
			{
				list.add(exps.substring(num,i+1));
			}

		}
	}

	public void simplify()			//化简并计算表达式
	{
	    Matcher matcher1;
	    Matcher matcher2;
		for(int i=1;i<list.size()-1;i++)
		{
			if(list.get(i).equals("*"))
			{
			    matcher1 = Number.matcher(list.get(i-1));
			    matcher2 = Number.matcher(list.get(i+1));
				if(matcher1.find() && matcher2.find())
				{
					int temp1,temp2;
					temp1 = Integer.parseInt(list.get(i-1));
					temp2 = Integer.parseInt(list.get(i+1));
					temp1 = temp1*temp2;
					list.set(i-1,String.valueOf(temp1));
					list.remove(i);
					list.remove(i);
					i=i-1;
				}
			}
		}



			for(int i=1;i<list.size()-1;i++)
			{
				matcher1 = Number.matcher(list.get(i-1));
			    matcher2 = Number.matcher(list.get(i+1));
				if(list.get(i).equals("+") || list.get(i).equals("-"))
				{
					if(matcher1.find() && matcher2.find() && judge(i))
					{
						int temp1,temp2;
						temp1 = Integer.parseInt(list.get(i-1));
						temp2 = Integer.parseInt(list.get(i+1));
						if(list.get(i).equals("+"))
							list.set(i-1, String.valueOf(temp1+temp2));
						else
							list.set(i-1, String.valueOf(temp1-temp2));
						list.remove(i);
						list.remove(i);
						i=i-1;
					}
				}
			}

	}

	public boolean judge(int i)      //判断加法减法的旁边有没有乘法
	{
		if(i-2>0 )
		{
			if(list.get(i-2).equals("*") )
				return false;
		}
		if(i+2<list.size())
		{
			if(list.get(i+2).equals("*"))
				return false;
		}
		return true;
	}


	public void derivative(int num)			//求导
	{
		int[] array1 = new int[list.size()];			//剔除不含求导的变量的式子
		int shu=1;
		array1[0]=0;
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).equals("+")||list.get(i).equals("-"))
			{
				array1[shu] = i;
				shu++;
			}
		}

		for(int i=0;i<shu;i++)
		{
			boolean fl = true;
			if(shu == 1)
			{
				boolean f2 = true;
				for(int k=0;k<list.size();k++)
				{
					if(list.get(k).equals(val))
					{
						fl = false;
						f2 = false;
						break;
					}
				}
				if(f2 == true)
					list.clear();
			}
			else if(i==0)
			{
				for(int k=0;k<array1[i+1];k++)
				if(list.get(k).equals(val))
				{
					fl =false;
					break;
				}
			}
			else if(i == shu-1)
			{
				for(int k=array1[i];k<list.size();k++)
				{
					if(list.get(k).equals(val))
					{
						fl = false;
						break;
					}
				}
			}
			else
			{for(int k=array1[i]+1;k<array1[i+1];k++)
			{
				if(list.get(k).equals(val))
				{
					fl =false;
					break;
				}
			}
			}

			if(fl == true && i == shu-1)
			{
				int leng = list.size();
				for(int w = array1[i];w<leng;w++)
				{
					list.remove(array1[i]);
				}
			}

			if(fl == true)
			{
				if(i == 0)
				{
					list.remove(0);
					list.remove(0);
					for(int t=0;t<shu;t++)
						array1[t]=array1[t]-2;
				}
				else{
				int tempnum = array1[i+1]-array1[i];
				for(int w=array1[i];w<array1[i+1];w++)
				{
					list.remove(array1[i]);
				}
				for(int w=0;w<shu;w++)
					array1[w]=array1[w]-tempnum;
				}
			}
		}



		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).equals(val))
			{
				int temp = i;
				int aa=1;
				while(true)
				{
					boolean tempo = true;
					while(i != list.size()-1 && list.get(i+1).equals("*") )
					{
						if(list.get(i+2).equals(val))
						{
							aa++;
							if(tempo == true)
							{
							list.remove(i+1);
							list.remove(i+1);
							tempo = false;

							}
							else
								i=i+2;
						}
						else
							i=i+2;
					}

						if(aa ==1)
						{
							if(temp != 0 && list.get(temp-1).equals("*"))
							{
								list.remove(temp-1);
								list.remove(temp-1);
								i=i-2;
							}
							else if(temp< list.size()-1 && list.get(temp+1).equals("*"))
							{
								list.remove(temp);
								list.remove(temp);
								i=i-2;
							}
							else
								list.set(i, "1");
						}
						else
						{
								String op = String.valueOf(aa);
								list.add(temp,"*");
								list.add(temp,op);
								i=i+2;
						}

					break;
				}
			}
		}
	}

	public void getstr()
	{
		Scanner sc = new Scanner(System.in);
		while(!sc.equals("exit"))
		{
		String expression = sc.nextLine();
		judge(expression);
		}
		sc.close();
	}

	public static void main(String[] args) {
		ivyy a = new ivyy();
		a.getstr();
	}

}
