package ezen.ams.app;

import ezen.ams.domain.Account;
import ezen.ams.exception.NotBalanceException;

public class AMSExample {

	public static void main(String[] args) {
		Account account = new Account("김기정", 1111, 10000);
		try {
			long restMoney = account.deposit(1000);//입력받는 값
			System.out.println("정산처리 : " + account.getRestMoney());
			
			restMoney = account.withdraw(20000);//입력받는 값
			System.out.println("출금 후 잔액: " + restMoney);
		} catch (NotBalanceException e) {
			System.out.println(e.getMessage());
			
		}
		
		
	}

}
