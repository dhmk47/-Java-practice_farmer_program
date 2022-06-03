package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import db.DBConnectionMgr;
import dto.MyProduct;
import dto.ProductKind;
import dto.User;
import dto.UserDtl;
import dto.UserMst;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServiceDao {
	private final DBConnectionMgr pool;
	private final Scanner sc;
	
	public int signupUser(String email, String name, String username, String password) {
		Connection con = null;
		String sql = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			con = pool.getConnection();
			sql = "INSERT INTO\r\n"
					+ "	user_mst\r\n"
					+ "VALUES(\r\n"
					+ "	0,\r\n"
					+ "	?,\r\n"
					+ "	?,\r\n"
					+ "	?,\r\n"
					+ "	?,\r\n"
					+ "	NOW(),\r\n"
					+ "	NOW()\r\n"
					+ ")";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.setString(2, name);
			pstmt.setString(3, username);
			pstmt.setString(4, password);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		
		return result;
	}
	
	public HashMap<String, User> signinUser(String username, String password) {
		Connection con = null;
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		HashMap<String, User> userMap = new HashMap<String, User>();
		
		try {
			con = pool.getConnection();
			sql = "SELECT\r\n"
					+ "	*\r\n"
					+ "FROM\r\n"
					+ "	user_mst um\r\n"
					+ "	LEFT OUTER JOIN user_dtl ud ON(ud.usercode = um.usercode)\r\n"
					+ "WHERE\r\n"
					+ "	um.usercode = (select\r\n"
					+ "						usercode\r\n"
					+ "					from\r\n"
					+ "						user_mst\r\n"
					+ "					where\r\n"
					+ "						username = ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			
			rs.next();
			try {
				rs.getInt(1);
				
				UserMst userMst = UserMst.builder()
						.usercode(rs.getInt(1))
						.email(rs.getString(2))
						.name(rs.getString(3))
						.username(rs.getString(4))
						.password(rs.getString(5))
						.create_date(rs.getTimestamp(6).toLocalDateTime())
						.update_date(rs.getTimestamp(7).toLocalDateTime())
						.build();
				
				if(!userMst.getPassword().equals(password)) {
					System.out.println("비밀번호를 틀렸습니다.");
				}else {
					UserDtl userDtl = UserDtl.builder()
							.usercode(rs.getInt(8))
							.money(rs.getInt(9))
							.build();
					userMap.put("um", userMst);
					userMap.put("ud", userDtl);
				}
				
								
			} catch (SQLDataException e) {
				System.out.println("해당 아이디는 존재하지 않습니다.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return userMap;
	}
	
	public ProductKind checkPurchaseProduct(String name) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		ProductKind produceKind = null;
		
		try {
			con = pool.getConnection();
			sql = "SELECT\r\n"
					+ "	*\r\n"
					+ "FROM\r\n"
					+ "	product_kind\r\n"
					+ "WHERE\r\n"
					+ "	NAME = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			
			rs.next();
			
			try {
				rs.getInt(1);
				
				produceKind = ProductKind.builder()
						.product_code(rs.getInt(1))
						.name(name)
						.price(rs.getInt(3))
						.season(rs.getString(4))
						.grow_day(rs.getInt(5))
						.build();
				
				return produceKind;
			} catch (SQLDataException e) {
				System.out.println(name + "이라는 상품은 구매할 수 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return produceKind;
	}
	
	public int purchaseProduct(ProductKind productKind, int amount, UserMst userMst,
			String name, MyProduct myProduct, int price, boolean haveProduct) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int result = 0;
//		int price = 0;
		int alreadyHaveProductAmount = 0;
//		Scanner sc = new Scanner(System.in);
		
//		if(checkIfAlreadyHaveProduct(userMst.getUsercode(), name)) {
		if(haveProduct) {
			try {
				alreadyHaveProductAmount = myProduct.getAmount();
				int setAmout = alreadyHaveProductAmount + amount;
				con = pool.getConnection();
				sql = "UPDATE\r\n"
						+ "	my_product\r\n"
						+ "SET\r\n"
						+ "	amount = ?\r\n"
						+ "WHERE\r\n"
						+ "	usercode = ?\r\n"
						+ "	AND NAME = ?";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, setAmout);
				pstmt.setInt(2, userMst.getUsercode());
				pstmt.setString(3, myProduct.getName());
				
				result = pstmt.executeUpdate();
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				pool.freeConnection(con, pstmt);
			}
		}else {
			try {
				con = pool.getConnection();
				sql = "INSERT INTO\r\n"
						+ "	my_product\r\n"
						+ "VALUES(\r\n"
						+ "	?,\r\n"
						+ "	?,\r\n"
						+ "	?,\r\n"
						+ "	?,\r\n"
						+ "	?,\r\n"
						+ "	?\r\n"
						+ ")";
				pstmt = con.prepareStatement(sql);
				
				pstmt.setInt(1, productKind.getProduct_code());
				pstmt.setString(2, productKind.getName());
				pstmt.setInt(3, price);
				pstmt.setInt(4, amount);
				pstmt.setString(5, productKind.getSeason());
				pstmt.setInt(6, userMst.getUsercode());
				
				result = pstmt.executeUpdate();
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				pool.freeConnection(con, pstmt);
			}
		}
		return result;
	}
	
	public HashMap<String, User> getUserInfo(int usercode) {
		Connection con = null;
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		HashMap<String, User> userMap = new HashMap<String, User>();
		
		try {
			con = pool.getConnection();
			sql = "SELECT\r\n"
					+ "	*\r\n"
					+ "FROM\r\n"
					+ "	user_mst um\r\n"
					+ "	LEFT OUTER JOIN user_dtl ud ON(ud.usercode = um.usercode)\r\n"
					+ "WHERE\r\n"
					+ "	um.usercode = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, usercode);
			rs = pstmt.executeQuery();
			rs.next();
			
			try {
				rs.getInt(1);
				
				User userMst = UserMst.builder()
						.usercode(rs.getInt(1))
						.email(rs.getString(2))
						.name(rs.getString(3))
						.username(rs.getString(4))
						.password(rs.getString(5))
						.create_date(rs.getTimestamp(6).toLocalDateTime())
						.update_date(rs.getTimestamp(7).toLocalDateTime())
						.build();
				
				UserDtl userDtl = UserDtl.builder()
						.money(rs.getInt(9))
						.build();
				
				userMap.put("um", userMst);
				userMap.put("ud", userDtl);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return userMap;
	}
	
	public int payMyMoney(int price, int usercode, int myMoney) {
		Connection con = null;
		String sql = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			con = pool.getConnection();
			sql = "UPDATE\r\n"
					+ "	user_dtl\r\n"
					+ "SET\r\n"
					+ "	money = ?\r\n"
					+ "WHERE\r\n"
					+ "	usercode = ?";
			pstmt = con.prepareStatement(sql);
			
			int payMoney = myMoney - price;
			
			pstmt.setInt(1, payMoney);
			pstmt.setInt(2, usercode);
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		
		return result;
	}
	
	public ArrayList<ProductKind> checkPurchaseableProduct() {
		ArrayList<ProductKind> productList = new ArrayList<ProductKind>();
		Connection con = null;
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = pool.getConnection();
			sql = "SELECT\r\n"
					+ "	*\r\n"
					+ "FROM\r\n"
					+ "	product_kind";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				try {
					rs.getInt(1);
					
					ProductKind productKind = ProductKind.builder()
							.product_code(rs.getInt(1))
							.name(rs.getString(2))
							.price(rs.getInt(3))
							.season(rs.getString(4))
							.grow_day(rs.getInt(5))
							.build();
					
					productList.add(productKind);
					
				} catch (SQLDataException e) {
					System.out.println("현재 구매할 수 있는 품목이 없습니다.");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		
		return productList;
	}
	
	public ArrayList<MyProduct> checkmyProduct(int usercode){
		ArrayList<MyProduct> productList = new ArrayList<MyProduct>();
		Connection con = null;
		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = pool.getConnection();
			sql = "SELECT\r\n"
					+ "	mp.name,\r\n"
					+ "	mp.price,\r\n"
					+ "	mp.amount,\r\n"
					+ "	mp.season\r\n"
					+ "FROM\r\n"
					+ "	My_product mp\r\n"
					+ "	LEFT OUTER JOIN user_mst um ON(um.usercode = mp.usercode)\r\n"
					+ "WHERE\r\n"
					+ "	mp.usercode = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, usercode);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				try {
					rs.getString(1);
					MyProduct myProduct = MyProduct.builder()
							.name(rs.getString(1))
							.price(rs.getInt(2))
							.amount(rs.getInt(3))
							.season(rs.getString(4))
							.build();
					
					productList.add(myProduct);
					
				} catch (SQLDataException e) {
					System.out.println("현재 판매 가능한 물품이 없습니다.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		
		return productList;
	}
	
	public boolean checkIfAlreadyHaveProduct(int usercode, String name) {
		Connection con = null;
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = true;
		
		try {
			con = pool.getConnection();
			sql = "SELECT\r\n"
					+ "	*\r\n"
					+ "FROM\r\n"
					+ "	my_product\r\n"
					+ "WHERE\r\n"
					+ "	usercode = ?\r\n"
					+ "	AND NAME = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, usercode);
			pstmt.setString(2, name);
			
			rs = pstmt.executeQuery();
			rs.next();
			
			try {
				rs.getInt(3);
			} catch (SQLDataException e) {
				flag = false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return flag;
	}
	
	public int sellMyProduct(int usercode, int amount, String name) {
		Connection con = null;
		String sql = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			con = pool.getConnection();
			sql = "UPDATE\r\n"
					+ "	my_product\r\n"
					+ "SET\r\n"
					+ "	amount = ?\r\n"
					+ "WHERE\r\n"
					+ "	usercode = ?\r\n"
					+ "	AND NAME = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, amount);
			pstmt.setInt(2, usercode);
			pstmt.setString(3, name);
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return result;
	}
	
	public int updateMyMoney(int income, int usercode) {
		Connection con = null;
		String sql = "";
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			con = pool.getConnection();
			sql = "UPDATE\r\n"
					+ "	user_dtl\r\n"
					+ "SET\r\n"
					+ "	money = ?\r\n"
					+ "WHERE\r\n"
					+ "	usercode = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, income);
			pstmt.setInt(2, usercode);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		
		return result;
	}
	
	public ArrayList<User> getAllUserInfo(){
		ArrayList<User> userList = new ArrayList<User>();
		
		Connection con = null;
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = pool.getConnection();
			sql = "SELECT\r\n"
					+ "	*\r\n"
					+ "FROM\r\n"
					+ "	user_mst um\r\n"
					+ "	LEFT OUTER JOIN user_dtl ud ON(ud.usercode = um.usercode)";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				try {
					rs.getInt(1);
					
					User userMst = UserMst.builder()
							.usercode(rs.getInt(1))
							.email(rs.getString(2))
							.name(rs.getString(3))
							.username(rs.getString(4))
							.password(rs.getString(5))
							.create_date(rs.getTimestamp(6).toLocalDateTime())
							.update_date(rs.getTimestamp(7).toLocalDateTime())
							.build();
					
					User userDtl = UserDtl.builder()
							.usercode(rs.getInt(8))
							.money(rs.getInt(9))
							.build();
					
					userList.add(userMst);
					userList.add(userDtl);
					
				} catch (SQLDataException e) {
					System.out.println("현재 등록된 회원이 없습니다.");
					
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			pool.freeConnection(con, pstmt, rs);
			
		}
		
		return userList;
	}
	
	public ArrayList<ProductKind> getAllProductKindInfo(){
		
		Connection con = null;
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		ArrayList<ProductKind> productList = new ArrayList<ProductKind>();
		
		
		try {
			con = pool.getConnection();
			sql = "SELECT\r\n"
					+ "	*\r\n"
					+ "FROM\r\n"
					+ "	product_kind";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				try {
					rs.getInt(1);
					
					ProductKind productKind = ProductKind.builder()
							.product_code(rs.getInt(1))
							.name(rs.getString(2))
							.price(rs.getInt(3))
							.season(rs.getString(4))
							.grow_day(rs.getInt(5))
							.build();
					
					productList.add(productKind);
					
				} catch (SQLDataException e) {
					System.out.println("아직 등록된 물품이 없습니다.");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			pool.freeConnection(con, pstmt, rs);
			
		}
		
		return productList;
		
	}
	
	public void addProductKindData() {
		Connection con = null;
		String sql = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			con = pool.getConnection();
			sql = "INSERT INTO\r\n"
					+ "	product_kind\r\n"
					+ "VALUES(\r\n"
					+ "	CODE,\r\n"
					+ "	NAME,\r\n"
					+ "	price,\r\n"
					+ "	season,\r\n"
					+ "	grow_day\r\n"
					+ ")";
			pstmt = con.prepareStatement(sql);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			pool.freeConnection(con, pstmt);
			
		}
	}
	
	public int addProductKind(int code, String name) {
		Connection con = null;
		String sql = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			int price = 0;
			String season = null;
			int grow_day = 0;
			
			con = pool.getConnection();
			sql = "INSERT INTO\r\n"
					+ "	product_kind\r\n"
					+ "VALUES(\r\n"
					+ "	?,\r\n"
					+ "	?,\r\n"
					+ "	?,\r\n"
					+ "	?,\r\n"
					+ "	?\r\n"
					+ ")";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, code);
			pstmt.setString(2, name);
			
			System.out.print("가격 입력: ");
			price = sc.nextInt();
			sc.nextLine();
			
			System.out.print("계절 입력: ");
			season = sc.nextLine();
			
			System.out.print("성장날짜 입력: ");
			grow_day = sc.nextInt();
			sc.nextLine();
			
			pstmt.setInt(3, price);
			pstmt.setString(4, season);
			pstmt.setInt(5, grow_day);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return result;
	}
	
	public int checkProductKind(String productName) {
		Connection con = null;
		String sql  = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = pool.getConnection();
			sql = "SELECT\r\n"
					+ "	*\r\n"
					+ "FROM\r\n"
					+ "	product_kind\r\n"
					+ "WHERE\r\n"
					+ "	product_code = (select\r\n"
					+ "							product_code\r\n"
					+ "						from\r\n"
					+ "							product_kind\r\n"
					+ "						where\r\n"
					+ "							NAME = ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, productName);
			rs = pstmt.executeQuery();
			
			rs.next();
			
			rs.getInt(1);
			
		} catch (SQLDataException e) {
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			pool.freeConnection(con, pstmt, rs);
		}
		
		return 1;
	}
	
	public int updateProductInfo(String modifyInfo, String product, boolean isString) {
		Connection con = null;
		String sql = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		String newStrTypeInfo = null;
		int newIntTypeInfo = 0;
		int oldPrice = 0;
		
		
		try {
			con = pool.getConnection();
			sql = "UPDATE\r\n"
					+ "	product_kind\r\n"
					+ "SET\r\n"
					+ "	" + modifyInfo + " = ?\r\n"
					+ "WHERE\r\n"
					+ "	product_code = (select\r\n"
					+ "							product_code\r\n"
					+ "						from\r\n"
					+ "							product_kind\r\n"
					+ "						where\r\n"
					+ "							NAME = ?)";
			pstmt = con.prepareStatement(sql);
			
			System.out.print("새로운 " + modifyInfo + "정보 입력: ");
			if(isString) {
				newStrTypeInfo = sc.nextLine();
				
				pstmt.setString(1, newStrTypeInfo);
			}else {
				newIntTypeInfo = sc.nextInt();
				sc.nextLine();
				
				pstmt.setInt(1, newIntTypeInfo);
				
				oldPrice = checkPurchaseProduct(product).getPrice();
				
			}
			
			pstmt.setString(2, product);
			result = pstmt.executeUpdate();
			
			
			if(result != 0 && modifyInfo.equals("price")) {
				
				if(oldPrice > newIntTypeInfo) {
					int compensation = oldPrice - newIntTypeInfo;
					ArrayList<User> userList = getAllUserInfo();
					
					userList.removeIf(s -> s instanceof UserMst);
					for(User user : userList) {
						int usercode = ((UserDtl)user).getUsercode();
						int userMoney = ((UserDtl)user).getMoney();
						MyProduct myProduct = getMyProductByProductName(product, usercode);
						
						if(myProduct != null) {
							int compensationUserMoney = (compensation * myProduct.getAmount()) + userMoney;
							
							if(updateMyMoney(compensationUserMoney, usercode) != 0);{
								System.out.println("usercode: " + usercode + " 회원의 보상 금액: " + compensation * myProduct.getAmount());
								
							}
							
						}
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		
		return result;
	}
	
	public MyProduct getMyProductByProductName(String productName, int usercode) {
		Connection con = null;
		StringBuilder sb = new StringBuilder();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MyProduct myProduct = null;
		
		try {
			con = pool.getConnection();
			sb.append("SELECT\r\n"
					+ "	*\r\n"
					+ "FROM\r\n"
					+ "	my_product\r\n"
					+ "WHERE\r\n"
					+ " 	usercode = ? AND\r\n"
					+ "	product_code = (select\r\n"
					+ "							product_code\r\n"
					+ "						from\r\n"
					+ "							my_product\r\n"
					+ "						where\r\n"
					+ "							NAME = ? AND usercode = ?)");
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setInt(1, usercode);
			pstmt.setString(2, productName);
			pstmt.setInt(3, usercode);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			myProduct = MyProduct.builder()
					.product_code(rs.getInt(1))
					.name(rs.getString(2))
					.price(rs.getInt(3))
					.amount(rs.getInt(4))
					.season(rs.getString(5))
					.usercode(rs.getInt(6))
					.build();
			
		} catch (SQLDataException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		
		return myProduct;
	}
	
	public void deleteProduct(String productName) {
		Connection con = null;
		StringBuilder sb = new StringBuilder();
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			con = pool.getConnection();
			sb.append("DELETE\r\n"
					+ "FROM\r\n"
					+ "	product_kind\r\n"
					+ "WHERE\r\n"
					+ "	product_code = (select\r\n"
					+ "							product_code\r\n"
					+ "						from\r\n"
					+ "							product_kind\r\n"
					+ "						where\r\n"
					+ "							NAME = ?)");
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, productName);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		
		if(result != 0) {
			System.out.println(productName + " 품목이 삭제 되었습니다.");
		}else {
			System.out.println("삭제 오류");
		}
	}
	
	public void checkDeletedUserProduct(int usercode) {
		Connection con = null;
		StringBuilder sb = new StringBuilder();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = pool.getConnection();
			sb.append("SELECT\r\n"
					+ "	*\r\n"
					+ "FROM\r\n"
					+ "	deleted_user_product\r\n"
					+ "WHERE\r\n"
					+ "	product_code = ?");
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setInt(1, usercode);
			
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {
				rs.getInt(1);
				
			}
			
		} catch(SQLDataException) {
			return;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
	}
	
}