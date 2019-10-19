package com.cg.ibs.loanmgmt.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	public long generateLoanNumber() throws IBSException {
		Connection connection = OracleDataBaseUtil.getConnection();
		long newLoanNumber = 0;
		try (PreparedStatement preparedStatement = connection.prepareStatement(QueryMapper.GENERATE_LOAN_NUM);) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					newLoanNumber = resultSet.getLong(1);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IBSException(ExceptionMessages.MESSAGEFORSQLEXCEPTION);
		}
		return newLoanNumber;
	}

	@Override
	public boolean saveLoan(LoanMaster loanMaster) throws SQLException {
		boolean confirm=false;
		connection = OracleDataBaseUtil.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(QueryMapper.INS_LOAN);) {
			preparedStatement.setString(1, "approved");
			preparedStatement.setLong(2, loanMaster.getApplicationNumber());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try (PreparedStatement preparedStatement = connection.prepareStatement(QueryMapper.APPROVE_LOAN);) {
			System.out.println(loanMaster.getApplicationNumber());
			System.out.println(loanMaster.getLoanNumber());
			preparedStatement.setLong(1, loanMaster.getApplicationNumber());
			preparedStatement.setLong(2, loanMaster.getLoanNumber());
			preparedStatement.executeUpdate();
			confirm=true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return confirm;
	}

	@Override
	public Map<Long, LoanMaster> getLoanDetailsForVerification()
			throws IOException, ClassNotFoundException, IBSException {
		Connection connection = OracleDataBaseUtil.getConnection();
		Map<Long, LoanMaster> pendingLoans = new HashMap<>();
		try (PreparedStatement preparedStatement = connection.prepareStatement(QueryMapper.GET_PENDING_LOAN);) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					LoanMaster pendingLoan = new LoanMaster();
					CustomerBean customer = new CustomerBean();
					pendingLoan.setApplicationNumber(resultSet.getLong("applicant_num"));
					customer.setFirstName(resultSet.getString("first_name"));
					customer.setLastName(resultSet.getString("last_name"));
					customer.setUCI(resultSet.getBigDecimal("uci").toBigInteger());
					customer.setUserId(resultSet.getString("user_id") );
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
				} // document need to be downloaded

			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IBSException(ExceptionMessages.MESSAGEFORSQLEXCEPTION);
		}
		return pendingLoans;
	}

	@Override
	public boolean downloadDocument(long applicationNumber) throws IBSException {
		boolean check=false;
		Connection connection = OracleDataBaseUtil.getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(QueryMapper.DOWNLOAD_DOCUMENT);) {
			preparedStatement.setLong(1, applicationNumber);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					Blob getDocument = resultSet.getBlob("document");
					FileOutputStream fileOutputStream =new FileOutputStream("./download/" + applicationNumber + ".pdf");
					fileOutputStream.write(getDocument.getBytes(1,(int)getDocument.length()));
					fileOutputStream.flush();
					fileOutputStream.close();
					check=true;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new IBSException(ExceptionMessages.MESSAGEFORSQLEXCEPTION);
		}
		return check;
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