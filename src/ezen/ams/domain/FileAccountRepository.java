package ezen.ams.domain;

import java.io.File;
import java.util.List;

/**
 * RDB를 통해
 * 
 * @author 주성민
 *
 */

public class FileAccountRepository implements AccountRepository {

	File file;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Account> getAccounts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addAccount(Account account) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Account searchAccount(String accountNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Account> searchAccountByOwner(String accountOwner) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAccount(String accountNum) {
		// TODO Auto-generated method stub
		return false;
	}

}
