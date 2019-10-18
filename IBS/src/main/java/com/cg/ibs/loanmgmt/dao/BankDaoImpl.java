package com.cg.ibs.loanmgmt.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.cg.ibs.loanmgmt.bean.CustomerBean;
import com.cg.ibs.loanmgmt.bean.LoanMaster;
import com.cg.ibs.loanmgmt.bean.LoanStatus;
import com.cg.ibs.loanmgmt.bean.LoanType;
import com.cg.ibs.loanmgmt.exception.ExceptionMessages;
import com.cg.ibs.loanmgmt.exception.IBSException;
import com.cg.ibs.loanmgmt.util.OracleDataBaseUtil;
import com.sun.istack.internal.logging.Logger;

public class BankDaoImpl implements BankDao {
	private static final Logger LOGGER = Logger.getLogger(BankDaoImpl.class);
	LoanMaster loanMaster = new LoanMaster();
	Connection connection;

	@Override
	public boolean saveLoan(LoanMaster loanMaster) throws SQLException {
		connection = OracleDataBaseUtil.getConnection();
		String uciTemp = "";
		String sql = "select uci from customers where user_ID =?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setString(1, loanMaster.getCustomerBean().getUserId());
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					uciTemp = resultSet.getString("uci");
				}
			}
		} catch (SQLException e) {
			LOGGER.info("SQL exception is coming.");
			e.printStackTrace();
		}
		try (PreparedStatement preparedStatement = connection.prepareStatement(QueryMapper.INS_LOAN);) {

			preparedStatement.setInt(1, Integer.valueOf(loanMaster.getLoanNumber()));
			preparedStatement.setBigDecimal(2, new BigDecimal(uciTemp));
			preparedStatement.setDouble(3, loanMaster.getLoanAmount());
			preparedStatement.setInt(4, loanMaster.getLoanTenure());
			preparedStatement.setDouble(5, loanMaster.getLoanAmount());
			java.sql.Date date = java.sql.Date.valueOf(loanMaster.getAppliedDate());
			preparedStatement.setDate(6, date);
			preparedStatement.setInt(7, loanMaster.getTotalNumberOfEmis());
			preparedStatement.setInt(8, loanMaster.getNumberOfEmis());
			if (loanMaster.getLoanType() == LoanType.HOME_LOAN) {
				preparedStatement.setInt(9, 1);
			} else if (loanMaster.getLoanType() == LoanType.EDUCATION_LOAN) {
				preparedStatement.setInt(9, 2);
			} else if (loanMaster.getLoanType() == LoanType.PERSONAL_LOAN) {
				preparedStatement.setInt(9, 3);
			} else if (loanMaster.getLoanType() == LoanType.VEHICLE_LOAN) {
				preparedStatement.setInt(9, 4);
			}
			preparedStatement.setDouble(10, loanMaster.getEmiAmount());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public Map<Long, LoanMaster> getLoanDetailsForVerification() throws IOException, ClassNotFoundException, IBSException {
		Connection connection = OracleDataBaseUtil.getConnection();
		Map<Long, LoanMaster> pendingLoans = new HashMap<>();
		try (PreparedStatement preparedStatement = connection.prepareStatement(QueryMapper.GET_PENDING_LOAN);) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while(resultSet.next()) {
				LoanMaster pendingLoan = new LoanMaster();
				CustomerBean customer = new CustomerBean();
				pendingLoan.setApplicationNumber(resultSet.getLong("applicant_num"));
				customer.setFirstName(resultSet.getString("first_name"));
				customer.setLastName(resultSet.getString("last_name"));
				customer.setUCI(resultSet.getBigDecimal("uci").toBigInteger());
				customer.setUserId(resultSet.getString("user_ID"));
				pendingLoan.setCustomerBean(customer);
				pendingLoan.setLoanAmount(resultSet.getDouble("loan_amount"));
				pendingLoan.setLoanTenure(resultSet.getInt("loan_tenure"));
				pendingLoan.setInterestRate(resultSet.getFloat("interest_rate"));
				pendingLoan.setEmiAmount(resultSet.getDouble("emi_amount"));
				if (resultSet.getInt("type_id") == 1) {
					pendingLoan.setLoanType(LoanType.HOME_LOAN);
				} else if (resultSet.getInt("type_id") == 2) {
					pendingLoan.setLoanType(LoanType.EDUCATION_LOAN);
				} else if (resultSet.getInt("type_id") == 3) {
					pendingLoan.setLoanType(LoanType.PERSONAL_LOAN);
				} else if (resultSet.getInt("type_id") == 4) {
					pendingLoan.setLoanType(LoanType.VEHICLE_LOAN);
				}
				pendingLoan.setLoanStatus(LoanStatus.PENDING);
				pendingLoan.setTotalNumberOfEmis(resultSet.getInt("total_num_of_emis"));
				pendingLoan.setAppliedDate(resultSet.getDate("applied_date").toLocalDate());
				pendingLoans.put(pendingLoan.getApplicationNumber(), pendingLoan); // Storing details in map
				}//document need to be downloaded

			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IBSException(ExceptionMessages.MESSAGEFORSQLEXCEPTION);
		}
		return pendingLoans;
	}

	@Override
	public boolean copyDocument(String srcPath, String destPath) {

		boolean isDone = false;
		File srcFile = new File(srcPath);
		File destFile = new File(destPath);
		if (srcFile.exists()) {
			try (FileInputStream fin = new FileInputStream(srcFile);
					FileOutputStream fout = new FileOutputStream(destFile)) {
				byte[] data = new byte[1024];
				while (fin.read(data) > -1) {
					fout.write(data);
				}
				isDone = true;
			} catch (IOException e) {
				// raise a user defined exception
			}
		} else {
			// throw your exception
		}
		return isDone;
	}

	@Override
	public LoanMaster updatePreClosure(LoanMaster loanMaster) { /*
																 * Updating EMI after approval of PreClosure
																 */
//		loanMaster.setNumberOfEmis(loanMaster.getTotalNumberOfEmis());
//		loanMaster.setNextEmiDate(null);
//		loanData.replace(loanMaster.getLoanNumber(), loanMaster);
		connection = OracleDataBaseUtil.getConnection();
		String sql = "update loan set ? = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setInt(1, loanMaster.getNumberOfEmis());
			preparedStatement.setInt(2, loanMaster.getTotalNumberOfEmis());
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
//					uciTemp = resultSet.getString("uci");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return loanMaster;
	}

	@Override
	public LoanMaster getPreClosureDetailsForVerification()
			throws IOException, ClassNotFoundException { /* Fetches Details for verification */
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("./PreClosureDetails.dat"));
		loanMaster = (LoanMaster) in.readObject();
		in.close();
		return loanMaster;
	}
}