// 출처: https://m.blog.naver.com/PostView.nhn?blogId=hujinone22&logNo=179253333&proxyReferer=https%3A%2F%2Fwww.google.co.kr%2F

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
public class Stand extends Frame implements Runnable, ActionListener, ItemListener
{
	private static final long serialVersionUID = 001L;
	TextField tf, tfyear, tfmonth, tfday, tfhr, tfmin, tfsec, tffile;
	Button bts, btcl, btac, btdi, btfile, bterr, bterr2;
	Label lb, lbyear, lbmonth, lbday, lbhr, lbmin, lbsec, lbfile, lberr, lberr2;
	CheckboxGroup cgr;
	Checkbox ck1, ck2, ck3;
	Dialog dr, drerr, drerr2;
	boolean alsetted = false, edittf = true, altemp = false;
	Panel pn[];
	FileDialog frd;
	Choice choic;
	int tmout = 0, reyear = 0, remonth = 0, reday = 0, rehr = 0, remin = 0, resec = 0, lang = 0;
	public Stand()
	{
		pn = new Panel[8];
		choic = new Choice();
		for(int i=0; i<8; i++)
		{
			pn[i] = new Panel();
			pn[i].setLayout(new FlowLayout());
		}
		choic.add("English");
		choic.add("한글");
		choic.addItemListener(this);
		tf = new TextField(18);
		lb = new Label("Timer");
		tf.setEditable(false);
		bts = new Button("Exit");
		btfile = new Button("File...");
		bts.addActionListener(this);
		btfile.addActionListener(this);
		cgr = new CheckboxGroup();
		ck1 = new Checkbox("Slow", cgr, false);
		ck2 = new Checkbox("Normal", cgr, true);
		ck3 = new Checkbox("Fast", cgr, false);
		ck3.setForeground(Color.red);
		lbyear = new Label("Year");
		lbmonth = new Label("Month");
		lbday = new Label("Day");
		lbhr = new Label("Hour");
		lbmin = new Label("Minute");
		lbsec = new Label("Second");
		lbfile = new Label("Sound");
		lberr = new Label("------------------------");
		lberr2 = new Label("------------------------");
		btcl = new Button("Cancel");
		btac = new Button("Accept");
		btdi = new Button("Alarm Set");
		bterr = new Button("Close");
		bterr2 = new Button("Close");
		tfyear = new TextField(4);
		tfmonth = new TextField(2);
		tfday = new TextField(2);
		tfhr = new TextField(2);
		tfmin = new TextField(2);
		tfsec = new TextField(2);
		tffile = new TextField("basic.wav", 10);
		dr = new Dialog(this, false);
		drerr = new Dialog(this, true);
		drerr2 = new Dialog(dr);
		frd = new FileDialog(dr, "Sound Files", FileDialog.LOAD);
		dr.setLayout(new GridLayout(8, 1));
		drerr.setLayout(new FlowLayout());
		drerr2.setLayout(new FlowLayout());
		dr.setSize(200, 350);
		dr.setLocation(300, 300);
		drerr.setSize(250, 100);
		drerr.setLocation(300, 300);
		drerr2.setSize(250, 100);
		drerr2.setLocation(300, 300);
		dr.setTitle("Alarm Setting");
		drerr.add(lberr);
		drerr.add(bterr);
		drerr2.add(lberr2);
		drerr2.add(bterr2);
		pn[0].add(lbyear);
		pn[0].add(tfyear);
		pn[1].add(lbmonth);
		pn[1].add(tfmonth);
		pn[2].add(lbday);
		pn[2].add(tfday);
		pn[3].add(lbhr);
		pn[3].add(tfhr);
		pn[4].add(lbmin);
		pn[4].add(tfmin);
		pn[5].add(lbsec);
		pn[5].add(tfsec);
		pn[6].add(lbfile);
		pn[6].add(tffile);
		pn[7].add(btcl);
		pn[7].add(btac);
		pn[7].add(btfile);
		for(int i=0; i<8; i++)
		{
			dr.add(pn[i]);
		}
		btcl.addActionListener(this);
		btac.addActionListener(this);
		btdi.addActionListener(this);
		bterr.addActionListener(this);
		bterr2.addActionListener(this);
		setTitle("Countdown Timer");
		setSize(200, 150);
		setLocation(200, 200);
		setLayout(new FlowLayout());
		add(lb);
		add(choic);
		add(tf);
		add(ck1);
		add(ck2);
		add(ck3);
		add(bts);
		add(btdi);
		setVisible(true);
		addWindowListener(new windowClose());
		new Thread(this).start();
	}
	public static void main(String[] args) 
	{
		new Stand();
	}
	public void Sound(String file, boolean loop, boolean plays)
	{
		Clip clip;
		try
		{
			File fr = new File(file);
			AudioInputStream ais = AudioSystem.getAudioInputStream(fr);
			tmout = 1000;
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			if(plays == false) clip.stop();
			if(loop) clip.loop(-1);			
		}
		catch(FileNotFoundException e)
		{
			alsetted = false; btdi.setForeground(Color.black);
			if(lang == 1) lberr.setText("파일을 찾을 수 없습니다.");
			else lberr.setText("Cannot found the file you selected.");
			drerr.setVisible(true);
		}
		catch(NumberFormatException e)
		{
			alsetted = false; btdi.setForeground(Color.black);
			if(lang == 1) lberr.setText("숫자를 입력하여야 합니다.");
			else lberr.setText("Inserted wrong numbers.");
			drerr.setVisible(true);
		}
		catch(IOException e)
		{
			alsetted = false; btdi.setForeground(Color.black);
			if(lang == 1) lberr.setText("입출력 오류가 발생하였습니다.");
			else lberr.setText("IO Error occured.");
			drerr.setVisible(true);
		} 
		catch (UnsupportedAudioFileException e) 
		{
			alsetted = false; btdi.setForeground(Color.black);
			if(lang == 1) lberr.setText("선택된 파일은 이 프로그램에서 지원하지 않습니다.");
			else lberr.setText("Selected file do not supported.");
			drerr.setVisible(true);
		} 
		catch (Exception e) 
		{
			alsetted = false; btdi.setForeground(Color.black);
			if(lang == 1) lberr.setText("예기치 않은 오류가 발생하였습니다.");
			else lberr.setText("Error occured.");
			drerr.setVisible(true);
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e)
	{
		if(e.getSource() == choic)
		{
			lang = choic.getSelectedIndex();
			if(lang == 0)
			{

				lbyear.setText("Year");
				lbmonth.setText("Month");
				lbday.setText("Day");
				lbhr.setText("Hour");
				lbmin.setText("Minute");
				lbsec.setText("Second");
				lbfile.setText("Sound");
				btcl.setLabel("Cancel");
				btac.setLabel("Accept");
				btdi.setLabel("Alarm Set");
				bterr.setLabel("Close");
				bterr2.setLabel("Close");
				bts.setLabel("Exit");
				ck1.setLabel("slow");
				ck2.setLabel("normal");
				ck3.setLabel("fast");
			}
			else if(lang == 1)
			{
				lbyear.setText("년도");
				lbmonth.setText("월");
				lbday.setText("일");
				lbhr.setText("시");
				lbmin.setText("분");
				lbsec.setText("초");
				lbfile.setText("효과음");
				btcl.setLabel("취소");
				btac.setLabel("적용");
				btdi.setLabel("알람 설정");
				bterr.setLabel("닫기");
				bterr2.setLabel("닫기");
				bts.setLabel("종료");
				ck1.setLabel("느림");
				ck2.setLabel("보통");
				ck3.setLabel("빠름");
			}
		}
	}
	@Override
	public void run() 
	{
		while(true)
		{
			if(tmout >= 0) tmout--;
			Calendar ca = Calendar.getInstance();
			tf.setText(ca.get(Calendar.YEAR) + "." + (ca.get(Calendar.MONTH) + 1) + "." + ca.get(Calendar.DAY_OF_MONTH) + "||" + ca.get(Calendar.HOUR) + ":" + ca.get(Calendar.MINUTE) + ":" + ca.get(Calendar.SECOND) + ":" + ca.get(Calendar.MILLISECOND));
			if(edittf)
			{
				tfyear.setText(Integer.toString(ca.get(Calendar.YEAR)));
				tfmonth.setText(Integer.toString(ca.get(Calendar.MONTH) + 1));
				tfday.setText(Integer.toString(ca.get(Calendar.DATE)));
				tfhr.setText(Integer.toString(ca.get(Calendar.HOUR_OF_DAY)));
				tfmin.setText(Integer.toString(ca.get(Calendar.MINUTE)));
				tfsec.setText(Integer.toString(ca.get(Calendar.SECOND)));
			}
			try
			{
				if(ck1.getState())
				{
					Thread.sleep(100);
				}
				else if(ck2.getState())
				{
					Thread.sleep(50);
				}
				if(alsetted)
				{
					if(tmout <= 0 && ca.get(Calendar.YEAR) == reyear && (ca.get(Calendar.MONTH) + 1) == remonth && ca.get(Calendar.DATE) == reday && ca.get(Calendar.HOUR_OF_DAY) == rehr && ca.get(Calendar.MINUTE) == remin && ca.get(Calendar.SECOND) == resec)
					{
						Sound(tffile.getText(), false, true);
						alsetted = false;
						btdi.setForeground(Color.black);
					}
				}
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			catch(NumberFormatException e)
			{
				alsetted = false; btdi.setForeground(Color.black);
				if(lang == 1) lberr.setText("Inserted wrong numbers.");
				else lberr.setText("Inserted wrong numbers.");
				drerr.setVisible(true);
			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == bts)
		{
			System.exit(0);
		}
		else if(e.getSource() == btdi)
		{
			dr.setVisible(true); edittf = false;
			if(ck3.getState() == true) ck1.setState(true);
		}
		else if(e.getSource() == btcl)
		{
			dr.setVisible(false); edittf = true;
			alsetted = false; btdi.setForeground(Color.black);
			btdi.setForeground(Color.black);
		}
		else if(e.getSource() == btac)
		{
			try 
			{
				reyear = Integer.parseInt(tfyear.getText());
				remonth = Integer.parseInt(tfmonth.getText());
				reday = Integer.parseInt(tfday.getText());
				rehr = Integer.parseInt(tfhr.getText());
				remin = Integer.parseInt(tfmin.getText());
				resec = Integer.parseInt(tfsec.getText());
				dr.setVisible(false); edittf = true;
				alsetted = true;
				btdi.setForeground(Color.red);
				Sound(tffile.getText(), false, false);
			} 
			catch (NumberFormatException e1) 
			{
				alsetted = false; btdi.setForeground(Color.black);
				if(lang == 1) lberr2.setText("숫자를 입력하여야 합니다.");
				else lberr2.setText("Inserted wrong numbers.");
				drerr2.setVisible(true);
			}
		}
		else if(e.getSource() == btfile)
		{
			frd.setVisible(true);
			tffile.setText(frd.getDirectory() + frd.getFile());
		}
		else if(e.getSource() == bterr)
		{
			drerr.setVisible(false);
		}
		else if(e.getSource() == bterr2)
		{
			drerr2.setVisible(false);
		}
	}
	class windowClose extends WindowAdapter
	{
		@Override
		public void windowClosing(WindowEvent e)
		{
			System.exit(0);
		}
	}
}
