package ezen.ams.gui;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JOptionPane;

import ezen.ams.domain.Account;
import ezen.ams.domain.AccountRepository;
import ezen.ams.domain.AccountType;
import ezen.ams.domain.JdbcAccountRepository;
import ezen.ams.domain.MinusAccount;

public class AMSFrame extends Frame {

//	변수 선언
	Choice choice;
	Label accountListl, accountNuml, accountNamel, passwordl, borrowl, inputl, bottom1, bottom2;
	TextField accountListt, accountNumt, AccountNamet, passwordt, borrowt, inputt;
	Button researchb, deleteb, searchb, createb, alllistb;
	TextArea textAreat;

	GridBagLayout gridBagLayout;
	GridBagConstraints gridBagConstraints;

	private AccountRepository repository;

	public AMSFrame(String title) {

		try {
			repository = new JdbcAccountRepository();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(0);
		}
		accountListl = new Label("계좌종류", Label.CENTER);
		accountNuml = new Label("계좌번호", Label.CENTER);
		accountNamel = new Label("예금주명", Label.CENTER);
		passwordl = new Label("비밀번호", Label.CENTER);
		borrowl = new Label("대출금액", Label.CENTER);
		inputl = new Label("입금금액", Label.CENTER);
		bottom1 = new Label("계좌목록", Label.CENTER);
		bottom2 = new Label("(단위 : 원)", Label.CENTER);

		accountListt = new TextField();
		accountNumt = new TextField();
		AccountNamet = new TextField();
		passwordt = new TextField();
		borrowt = new TextField();
		inputt = new TextField();
		textAreat = new TextArea();

		researchb = new Button("조회");
		deleteb = new Button("삭제");
		searchb = new Button("검색");
		createb = new Button("신규등록");
		alllistb = new Button("전체조회");

		choice = new Choice();
//        choice.add("전체");
//        choice.add("입출금계좌");
//        choice.add("마이너스계좌");
		AccountType[] accountTypes = AccountType.values();
		for (AccountType accountType : accountTypes) {
			choice.add(accountType.getName());
		}

	}

//    배치
	public void init() {

		gridBagLayout = new GridBagLayout();
		gridBagConstraints = new GridBagConstraints();
		setLayout(gridBagLayout);

//    	 라벨
		addComponent(accountListl, 0, 0, 1, 1, 1.0);// 계좌종류
		addComponent(accountNuml, 0, 1, 1, 1, 1.0);// 계좌번호
		addComponent(accountNamel, 0, 2, 1, 1, 1.0);// 예금주명
		addComponent(passwordl, 0, 3, 1, 1, 1.0);// 비밀번호
		addComponent(borrowl, 0, 4, 1, 1, 1.0);// 대출금액
		addComponent(inputl, 3, 3, 1, 1, 1.0);// 입금금액
		addComponent(bottom1, 0, 5, 1, 1, 1.0);// 계좌목록
		addComponent(bottom2, 5, 5, 1, 1, 1.0);// 단위

//    	 텍스트필드
		addComponent(accountNumt, 1, 1, 2, 1, 1.0);// 계좌번호
		addComponent(AccountNamet, 1, 2, 2, 1, 1.0);// 예금주명
		addComponent(passwordt, 1, 3, 2, 1, 1.0);// 비밀번호
		addComponent(borrowt, 1, 4, 2, 1, 1.0);// 대출금액
		addComponent(inputt, 4, 3, 2, 1, 1.0);// 입금금액

//    	 버튼
		addComponent(researchb, 3, 1, 1, 1, 0.0);// 조회
		addComponent(deleteb, 4, 1, 1, 1, 0.0);// 삭제
		addComponent(searchb, 3, 2, 1, 1, 0.0);// 검색
		addComponent(createb, 3, 4, 1, 1, 0.0);// 신규등록
		addComponent(alllistb, 4, 4, 1, 1, 0.0);// 전체조회

//    	 초이스
		addComponent(choice, 1, 0, 1, 1, 0.0);// 초이스

//    	 박스
		addComponent(textAreat, 0, 6, 10, 0, 1.0);// 박스

	}

//  xyz메소드
	private void addComponent(Component component, int gridx, int gridy, int gridwidth, int gridheight,
			double weightx) {
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.weightx = 1; // 가중치
		gridBagConstraints.weighty = 0.0;
		gridBagConstraints.gridx = gridx;
		gridBagConstraints.gridy = gridy;
		gridBagConstraints.gridwidth = gridwidth;
		gridBagConstraints.gridheight = gridheight;
		gridBagLayout.setConstraints(component, gridBagConstraints);
		add(component);
	}

//    박스명 바꾸기 \t 으로 가로 길이 맞추기
	private void printHeader() {
		textAreat.append("-----------------------------------------------------------------------\n");
		textAreat.append("계좌종류         계좌번호   예금주   비밀번호    잔액            대출금액\n");
		textAreat.append("=======================================================================\n");
	}

	public void allList() {
		textAreat.setText("");
		printHeader();
		List<Account> list = repository.getAccounts();
		for (Account account : list) {
			if (account instanceof MinusAccount) {
				textAreat.append("마이너스 계좌" + account.toString() + "\n");
			} else {
				textAreat.append("입출금 계좌" + account.toString() + "\n");
			}
		}
	}

	public void delete() {
		String accountNum = accountNumt.getText();
		repository.removeAccount(accountNum);
		JOptionPane.showMessageDialog(this, "해당 계좌가 삭제되었습니다.");
	}

	public void research() {
		textAreat.setText("");
		printHeader();
		String accountNum = accountNumt.getText();
		Account account = repository.searchAccount(accountNum);

		if (account != null) {
			if (account instanceof MinusAccount) {
				textAreat.append("마이너스 계좌" + account.toString() + "\n");
			} else {
				textAreat.append("입출금 계좌" + account.toString() + "\n");
			}
			JOptionPane.showMessageDialog(this, "해당 계좌를 조회하였습니다.");
		} else {
			JOptionPane.showMessageDialog(this, "해당 계좌를 찾을 수 없습니다.");
		}
	}

	public void search() {
		textAreat.setText("");
		printHeader();
		String accountName = AccountNamet.getText();
		List<Account> accounts = repository.searchAccountByOwner(accountName);

		if (!accounts.isEmpty()) {
			for (Account account : accounts) {
				if (account instanceof MinusAccount) {
					textAreat.append("마이너스 계좌" + account.toString() + "\n");
				} else {
					textAreat.append("입출금 계좌" + account.toString() + "\n");
				}
			}
			JOptionPane.showMessageDialog(this, "해당 계좌를 검색하였습니다.");
		} else {
			JOptionPane.showMessageDialog(this, "해당하는 계좌를 찾을 수 없습니다.");
		}
	}

	public void selectAccountType(AccountType accountType) {
		switch (accountType) {
		case GENERAL_ACCOUNT:
			borrowt.setEnabled(false);
			break;
		}

	}

	public void addAccount() {
		Account account = null;

//		계좌번호는 자동 생성하기 때문에 입력 받지 않아도 됨
//		String accountNum = accNumTF.getText();
//		if(!Validator.hasText(accountNum)) {
//			accNumTF.setText("계좌번호 입력혀라...");
//		}

		// 편의상 정상 입력되었다 가정
		String accountOwner = AccountNamet.getText();
		int password = Integer.parseInt(passwordt.getText());
		long inputMoney = Long.parseLong(inputt.getText());

		String selectedItem = choice.getSelectedItem();

		// 입출금 계좌 등록
		if (selectedItem.equals(AccountType.GENERAL_ACCOUNT.getName())) {
			account = new Account(accountOwner, password, inputMoney);
		} else if (selectedItem.equals(AccountType.MINUS_ACCOUNT.getName())) {
			long loanMoney = Long.parseLong(borrowt.getText());
			account = new MinusAccount(accountOwner, password, inputMoney, loanMoney);
		}

		repository.addAccount(account);
		JOptionPane.showMessageDialog(this, "정상 등록 처리되었습니다.");

	}

//	종료 메소드
	private void exit() {
		((JdbcAccountRepository) repository).close();
		setVisible(false);
		dispose();
		System.exit(0);
	}

	public void addEventListner() {
		class ActionHandler implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object eventSource = e.getSource();
				if (eventSource == createb) {
					addAccount();
				} else if (eventSource == alllistb) {
					allList();
				} else if (eventSource == deleteb) {
					delete();
				} else if (eventSource == searchb) {
					search();
				} else if (eventSource == researchb) {
					research();
				}
			}
		}

		ActionListener actionListener = new ActionHandler();

		// 종료 처리
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

		// 창이 열릴때
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
//  					allList();
			}
		});

		// 계좌 등록
		createb.addActionListener(actionListener);

		// 계좌 삭제
		deleteb.addActionListener(actionListener);

		// 계좌 조회
		researchb.addActionListener(actionListener);

		// 계좌 검색
		searchb.addActionListener(actionListener);

		// 계좌 선택
		choice.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (choice.getSelectedItem().equals("입출금계좌")) {
						selectAccountType(AccountType.GENERAL_ACCOUNT);
					}
				}

			}
		});
		// 전체계좌 조회
		alllistb.addActionListener(actionListener);

	}

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
