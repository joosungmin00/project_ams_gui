package ezen.ams.app;


import ezen.ams.gui.AMSFrame;

public class AMS {



	public static void main(String[] args) {
		AMSFrame frame = new AMSFrame("EZEN-BANK AMS");
		frame.init();
		frame.addEventListner();
		frame.pack();
		frame.setVisible(true);
//		화면 고정
		frame.setResizable(false);
		

	}

}
