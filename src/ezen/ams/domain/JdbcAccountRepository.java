package ezen.ams.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.net.aso.a;


/**
 * RDB를 통해 은행계좌 목록 저장 및 관리(검색, 수정, 삭제) 구현체
 * 
 * @author 주성민
 *
 */

public class JdbcAccountRepository implements AccountRepository{
   
	
//	나중에 propertie 파일로 변경할 것임...
   private static String driver = "oracle.jdbc.driver.OracleDriver";
   private static String url = "jdbc:oracle:thin:@localhost:1521:xe";
   private static String userid = "qwer";
   private static String password = "qwer";

   private Connection con;
   
   public JdbcAccountRepository() throws Exception {
      Class.forName(driver);
      con = DriverManager.getConnection(url, userid, password);
   }
   
   /**
    * 전체계좌 목록수 반환
    * @return 목록수
    * @throws SQLException 
    */
   public int getCount() {
      int count = 0;
      StringBuilder sb = new StringBuilder();
      sb.append(" SELECT *")
        .append(" FROM account");
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      
      try {
         pstmt = con.prepareStatement(sb.toString());
         rs = pstmt.executeQuery();
         if(rs.next()) {
            count = rs.getInt("count");
         }
      } catch (Exception e) {
         // 컴파일 예외를 런타임 예외로 변환
         throw new RuntimeException(e.getMessage());
      } finally {
         try {
            if (pstmt != null)
               pstmt.close();
            if (con != null)
               con.close();
         } catch (Exception e) {

         }
      }
      return count;
   }
   /**
    * 
    * @return 전체계좌목록
 * @throws ClassNotFoundException 
    */
   public List<Account> getAccounts() {
       List<Account> list = null;
       StringBuilder sb = new StringBuilder();
       sb.append("SELECT ACCOUNT_NUM, ACCOUNT_OWNER, PASSWD, REST_MONEY, BORROW_MONEY")
           .append(" FROM account");

//       Connection con = null;
       PreparedStatement pstmt = null;
       ResultSet rs = null;

       try {
           Class.forName(driver);
           con = DriverManager.getConnection(url, userid, password);
           pstmt = con.prepareStatement(sb.toString());
           rs = pstmt.executeQuery();
           list = new ArrayList<Account>();
           while (rs.next()) {
               String accountNum = rs.getString("ACCOUNT_NUM");
               String accountOwner = rs.getString("ACCOUNT_OWNER");
               int passwd = rs.getInt("PASSWD");
               int restMoney = rs.getInt("REST_MONEY");
               int borrowMoney = rs.getInt("BORROW_MONEY");

               if (borrowMoney != 0) {
                   MinusAccount ma = new MinusAccount();
                   ma.setAccountNum(accountNum);
                   ma.setAccountOwner(accountOwner);
                   ma.setPasswd(passwd);
                   ma.setRestMoney(restMoney);
                   ma.setBorrowMoney(borrowMoney);
                   list.add(ma);
               } else {
                   Account account = new Account();
                   account.setAccountNum(accountNum);
                   account.setAccountOwner(accountOwner);
                   account.setPasswd(passwd);
                   account.setRestMoney(restMoney);
                   list.add(account);
               }
           }
       } catch (Exception e) {
           throw new RuntimeException(e.getMessage());
       } finally {
           try {
               if (rs != null) rs.close();
               if (pstmt != null) pstmt.close();
               if (con != null) con.close();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }

       return list;
   }








         

   /**
    * 신규계좌 등록
    * @param account
    * @return 등록여부
    */
   public boolean addAccount(Account account) {
      boolean ok = false;
      StringBuilder sb = new StringBuilder();
      sb.append("INSERT INTO account(account_num, account_owner, passwd, rest_money, borrow_money)")
         .append("VALUES(account_num_seq.NEXTVAL, ?, ?, ?, ?)");

      PreparedStatement pstmt = null;

      try {
         con = DriverManager.getConnection(url, userid, password);
         pstmt = con.prepareStatement(sb.toString());
         pstmt.setString(1, account.getAccountOwner());
         pstmt.setInt(2, account.getPasswd());
         pstmt.setLong(3, account.getRestMoney());

         if (account instanceof MinusAccount) {
            MinusAccount minusAccount = (MinusAccount) account;
            pstmt.setLong(4, minusAccount.getBorrowMoney());
         } else {
            pstmt.setLong(4, 0);
         }

         pstmt.executeUpdate();
         ok = true;
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         try {
            if (pstmt != null)
               pstmt.close();
            if (con != null)
               con.close();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      return ok;
   }
   /**
    * 
    * @param accountNum 검색 계좌번호
    * @return 검색된 계좌
 * @throws ClassNotFoundException, SQLException 
    */
   public Account searchAccount(String ACCOUNT_NUM){
      Account account = null;
      
      StringBuilder sb = new StringBuilder();
      sb.append(" SELECT ACCOUNT_NUM,ACCOUNT_OWNER, PASSWD, REST_MONEY, BORROW_MONEY")
        .append(" FROM account")
        .append(" WHERE ACCOUNT_NUM = ? ");
      
      Connection con = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      
      try {
         Class.forName(driver);
         con = DriverManager.getConnection(url, userid, password);
         pstmt = con.prepareStatement(sb.toString());
         pstmt.setString(1, ACCOUNT_NUM);
         rs = pstmt.executeQuery();
         
         if(rs.next()) {
            String accountNum = rs.getString("ACCOUNT_NUM");
              String accountOwner = rs.getString("ACCOUNT_OWNER");
              int passwd = rs.getInt("PASSWD");
              int restMoney = rs.getInt("REST_MONEY");
              int borrowMoney = rs.getInt("BORROW_MONEY");
              
              if(borrowMoney != 0 ) {
                 MinusAccount ma = new MinusAccount();
                 ma.setAccountNum(accountNum);
                 ma.setAccountOwner(accountOwner);
                 ma.setPasswd(passwd);
                 ma.setRestMoney(restMoney);
                 ma.setBorrowMoney(borrowMoney);
                 account = ma;
              }else {
                 account = new Account();
                 account.setAccountNum(accountNum);
                 account.setAccountOwner(accountOwner);
                 account.setPasswd(passwd);
                 account.setRestMoney(restMoney);
                 return account;
              }
         }
      } catch(Exception e){
            throw new RuntimeException(e.getMessage());
      }finally {
         try {
            if(rs != null)rs.close();
            if(pstmt != null)pstmt.close();
            if(con != null)con.close();
         }catch (Exception e) {}
      }
      return account;
}
   
   /**
    * 
    * @param accountOwner 검색 예금주명
    * @return 검색된 예금주
    */
   public List<Account> searchAccountByOwner(String accountOwner) {
       List<Account> list = null;
       StringBuilder sb = new StringBuilder();
       sb.append("SELECT ACCOUNT_NUM, ACCOUNT_OWNER, PASSWD, REST_MONEY, BORROW_MONEY ")
         .append("FROM account ")
         .append("WHERE ACCOUNT_OWNER = ?");

       PreparedStatement pstmt = null;
       ResultSet rs = null;

       try {
          con = DriverManager.getConnection(url, userid, password);
           pstmt = con.prepareStatement(sb.toString());
           pstmt.setString(1, accountOwner);
           rs = pstmt.executeQuery();
           list = new ArrayList<>();

           while (rs.next()) {
               String accountNum = rs.getString("ACCOUNT_NUM");
               String owner = rs.getString("ACCOUNT_OWNER");
               int passwd = rs.getInt("PASSWD");
               long restMoney = rs.getLong("REST_MONEY");
               int borrowMoney = rs.getInt("BORROW_MONEY");

               if (borrowMoney != 0) {
                   MinusAccount ma = new MinusAccount();
                   ma.setAccountNum(accountNum);
                   ma.setAccountOwner(owner);
                   ma.setPasswd(passwd);
                   ma.setRestMoney(restMoney);
                   ma.setBorrowMoney(borrowMoney);
                   list.add(ma);
               } else {
                   Account account = new Account();
                   account.setAccountNum(accountNum);
                   account.setAccountOwner(owner);
                   account.setPasswd(passwd);
                   account.setRestMoney(restMoney);
                   list.add(account);
               }
           }
       } catch (Exception e) {
           throw new RuntimeException(e.getMessage());
       } finally {
           try {
               if (rs != null) rs.close();
               if (pstmt != null) pstmt.close();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }

       return list;
   }
   
   /**
    * 계좌삭제
    * @return
    */
   public boolean removeAccount(String accountNum) {
       boolean ok = false;
       StringBuilder sb = new StringBuilder();
       sb.append("DELETE FROM account ")
       .append("WHERE account_num = ?");
       
       PreparedStatement pstmt = null;
       Connection con = null;
       
       try {
           con = DriverManager.getConnection(url, userid, password);
           pstmt = con.prepareStatement(sb.toString());
           pstmt.setString(1, accountNum);
           pstmt.executeUpdate();
           ok = true;
       } catch (SQLException e) {
           throw new RuntimeException(e.getMessage());
       } finally {
           try {
               if (pstmt != null) pstmt.close();
               if (con != null) con.close();
           } catch (SQLException e) {
               throw new RuntimeException(e.getMessage());
           }
       }
       return ok;
   }
   
   public void close() {
	   if(con != null) {
		   try {
			   con.close();
		   }catch (Exception e) {
			e.printStackTrace();
		}
	   }
   }

   
}