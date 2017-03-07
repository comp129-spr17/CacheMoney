package MiniGamePack;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

import GamePack.ImageRelated;
import GamePack.SizeRelated;
import InterfacePack.Sounds;

public class MathProblem extends JPanel{
	private JLabel[] lblProblems;
	private JTextField txtAnswer;
	private JButton btnSubmit;
	private int answer;
	private SizeRelated sizeRelated;
	private int thisHeight;
	private Random random;
	private int smallestFact;
	private ArrayList<SetAnswer> SetAnswers;
	private int[] pList;
	private MouseListener mListener;
	private KeyListener kListener;
	private int gettingAns;
	private ImageRelated imageRelated;
	private boolean isAnsweredAlready;
	public MathProblem(SizeRelated sizeRelated){
		this.sizeRelated = sizeRelated;
		init();
		initProblemTypes();
	}
	interface SetAnswer{
		void setAnswer(int type, int a, int b);
	}
	private void init(){
		SetAnswers = new ArrayList<>();
		imageRelated = ImageRelated.getInstance();
		setLayout(null);
		thisHeight = sizeRelated.getDicePanelHeight()/15;
		setSize(sizeRelated.getDicePanelWidth()-40,thisHeight);
		lblProblems = new JLabel[5];
		txtAnswer = new JTextField(4);
		btnSubmit = new JButton("Submit");
		random = new Random();
		pList = new int[3];
		initLocation();
	}
	private void initProblemTypes(){
		SetAnswers.add(new SetAnswer() {public void setAnswer(int code, int a, int b) {	setProblemImages(a, b, '+');
				answer = a+b;}});
		SetAnswers.add(new SetAnswer() {public void setAnswer(int code, int a, int b) {	setProblemImages(a, b, '-');
		answer = a-b;}});
		SetAnswers.add(new SetAnswer() {public void setAnswer(int code, int a, int b) {	setProblemImages(a, b, '*');
		answer = a*b;}});
		SetAnswers.add(new SetAnswer() {public void setAnswer(int code, int a, int b) {	setProblemImages(a, b, '/');
		answer = a/b;}});
		SetAnswers.add(new SetAnswer() {public void setAnswer(int code, int a, int b) {	setProblemImages(a, b, '^');
		answer = (int)Math.pow(a, b);}});
		SetAnswers.add(new SetAnswer() {public void setAnswer(int code, int a, int b) {	setProblemImages(a, b, '%');
		answer = a%b;}});
	}
	private void setProblemImages(int a, int b, char c){
		lblProblems[0].setText(a+"");
		lblProblems[1].setText(c+"");
		lblProblems[2].setText(b+"");
	}
	private void initLocation(){
//		lblProblems[0].setBounds(20, sizeRelated.getDicePanelHeight()*(5+nthProb)/15, width, height);
		for(int i=0; i<5; i++){
			lblProblems[i] = new JLabel();
			add(lblProblems[i]);
		}
		lblProblems[3].setText("=");
		lblProblems[0].setBounds(0, 0, 30, thisHeight);
		lblProblems[1].setBounds(35, 0, 20, thisHeight);
		lblProblems[2].setBounds(60, 0, 30, thisHeight);
		lblProblems[3].setBounds(97, 0, 20, thisHeight);
		txtAnswer.setBounds(120, 0, 50, thisHeight);
		btnSubmit.setBounds(180, 0, 90, thisHeight);
		add(txtAnswer);
		add(btnSubmit);
		lblProblems[4].setBounds(273, 0, 50, thisHeight);
	}
	public int[] getProblem(){
		pList[0] = random.nextInt(6);
		pList[1] = random.nextInt(100)+1;
		if(pList[0] == 3)
			pList[2] = pList[1] / getFactNum(pList[1]);
		else if(pList[0] == 4)
			pList[2] = random.nextInt(4);
		else if(pList[0] == 5)
			pList[2] = random.nextInt(pList[1])+1;
		else
			pList[2] = random.nextInt(100)+1;
		return pList;
	}
	public void setProblem(){
		getProblem();
		setAnswer(pList[0],pList[1],pList[2]);
	}
	public void setProblem(int type, int a, int b){
		pList[0] = type;
		pList[1] = a;
		pList[2] = b;
		setAnswer(type, a, b);
	}
	private int getFactNum(int num){
		smallestFact = 1;
		for(int i=2; i<=num;i++){
			if(num%i == 0)
				return i;
		}
		return smallestFact;
	}
	public void setAnswer(int type, int a, int b){
		SetAnswers.get(type).setAnswer(type, a, b);
	}
	public boolean isAnswer(){
		try{
			gettingAns = Integer.parseInt(txtAnswer.getText());
		}catch(NumberFormatException e){
			return false;
		}
		return gettingAns == answer;
	}
	public void evalAnswer(boolean isAns, int playerNum){
		System.out.println("Ans : " + answer + " Getting Ans : "+ gettingAns);
		if(!isAnsweredAlready && isAns){
			isAnsweredAlready = true;
			disableTxtAndBtn();
			txtAnswer.setText(answer+"");
			lblProblems[4].setIcon(imageRelated.getSmallPieceImg(playerNum));
			Sounds.waitingRoomJoin.playSound();
		}else if(!isAnsweredAlready && !isAns){
			lblProblems[4].setIcon(imageRelated.getWrongAnswer());
			Sounds.buttonCancel.playSound();
		}
		lblProblems[4].setVisible(true);
	}
	public void addListners(MouseListener mListener, KeyListener kListener){
		this.mListener = mListener;
		this.kListener = kListener;
		txtAnswer.addKeyListener(kListener);
		btnSubmit.addMouseListener(mListener);
		btnSubmit.addKeyListener(kListener);
	}
	public void removeListners(){
		btnSubmit.removeMouseListener(mListener);
		btnSubmit.removeKeyListener(kListener);
		txtAnswer.removeKeyListener(kListener);
	}
	public void disableTxtAndBtn(){
		btnSubmit.setEnabled(false);
		txtAnswer.setEnabled(false);
	}
	public boolean isBtnSubmitEnabled(){
		return btnSubmit.isEnabled();
	}
	public boolean isTxtAnswerEnabled(){
		return txtAnswer.isEnabled();
	}
	public void enableTxtAndBtn(){
		btnSubmit.setEnabled(true);
		txtAnswer.setEnabled(true);
	}
	public void clearProblems(){
		enableTxtAndBtn();
		txtAnswer.setText("");
		lblProblems[4].setVisible(false);
		gettingAns = -999;
		isAnsweredAlready = false;
	}
}
