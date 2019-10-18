package com.cg.ibs.loanmgmt.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cg.ibs.loanmgmt.bean.CustomerBean;
import com.cg.ibs.loanmgmt.bean.Document;
import com.cg.ibs.loanmgmt.bean.LoanMaster;
import com.cg.ibs.loanmgmt.bean.LoanType;
import com.cg.ibs.loanmgmt.exception.ExceptionMessages;
import com.cg.ibs.loanmgmt.exception.IBSException;
import com.cg.ibs.loanmgmt.util.OracleDataBaseUtil;

public class CustomerDaoImpl implements CustomerDao {

	private static LoanMaster loanMaster = new LoanMaster();
	private static CustomerBean customer = new CustomerBean();

	public LoanMaster updateEMI(LoanMaster loanMaster) {
		loanMaster.setNumberOfEmis(loanMaster.getNumberOfEmis() + 1);
		loanMaster.setNextEmiDate(loanMaster.getNextEmiDate().plusMonths(1));
		loanData.replace(loanMaster.getLoanNumber(), loanMaster);
		return loanMaster;
	}

	public LoanMaster getEMIDetails(String loanNumber) {
		loanMaster = null;
		if (loanData.containsKey(loanNumber)) {
			loanMaster = loanData.get(loanNumber);
		}
		return loanMaster;
	}

	@Override
	public CustomerBean getCustomerDetails(String userId) throws SQLException, IBSException {
		Connection connection = OracleDataBaseUtil.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(QueryMapper.GET_CUSTOMER_DETAILS);) {
			preparedStatement.setString(1, userId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					customer.setUCI(resultSet.getBigDecimal("uci").toBigInteger());
					customer.setFirstName(resultSet.getString("first_name"));
					customer.setLastName(resultSet.getString("last_name"));
					customer.setUserId(resultSet.getString("user_id"));
				}
			}

		}
		return customer;
	}

	// LoanDetails
	public List<LoanMaster> getHistory(String userId) throws IBSException { /* getting list of loans */
		List<LoanMaster> loanMasters = new ArrayList<>();
		Connection connection = OracleDataBaseUtil.getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(QueryMapper.SEL_THE_COMMON_ROWS);) {
			preparedStatement.setString(1, userId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				while (resultSet.next()) {
					LoanMaster loanMaster = new LoanMaster();
					CustomerBean customer = new CustomerBean();
					customer.setFirstName(resultSet.getString("first_name"));
					customer.setLastName(resultSet.getString("last_name"));
					customer.setUCI(resultSet.getBigDecimal("uci").toBigInteger());
					customer.setUserId(resultSet.getString("user_id"));
					loanMaster.setCustomerBean(customer);
					loanMaster.setLoanNumber(resultSet.getInt("loan_number"));
					loanMaster.setLoanAmount(resultSet.getDouble("loan_amount"));
					loanMaster.setLoanTenure(resultSet.getInt("loan_tenure"));
					loanMaster.setNumberOfEmis(resultSet.getInt("num_of_emis_paid"));
					loanMaster.setTotalNumberOfEmis(resultSet.getInt("total_num_of_emis"));
					loanMaster.setEmiAmount(resultSet.getDouble("emi_amount"));
					if (resultSet.getInt("type_id") == 1) {
						loanMaster.setLoanType(LoanType.HOME_LOAN);
					} else if (resultSet.getInt("type_id") == 2) {
						loanMaster.setLoanType(LoanType.EDUCATION_LOAN);
					} else if (resultSet.getInt("type_id") == 3) {
						loanMaster.setLoanType(LoanType.PERSONAL_LOAN);
					} else if (resultSet.getInt("type_id") == 4) {
						loanMaster.setLoanType(LoanType.VEHICLE_LOAN);
					}
					loanMaster.setAppliedDate(resultSet.getDate("applied_date").toLocalDate());
					loanMasters.add(loanMaster);
				}
			}
		}

		catch (

		SQLException e) {
			e.printStackTrace();
			throw new IBSException(ExceptionMessages.MESSAGEFORSQLEXCEPTION);
		}
		return loanMasters;
	}

	// PreClosure
	public LoanMaster getPreClosureLoanDetails(String loanNumber) {
		/* Fetch loan Details against the loan number */
		loanMaster = null;
		if (loanData.containsKey(loanNumber)) {
			loanMaster = loanData.get(loanNumber); // LoanData HashMap
		}

		return loanMaster;
	}

	@Override
	public boolean verifyLoanNumber(String loanNumber) { /* Verification of loan number (Pre Closure) */
		boolean check = false;
		if (loanData.containsKey(loanNumber)) {
			check = true;
		}
		return check;
	}

	public boolean sendPreClosureForVerification(LoanMaster loanMaster)
			throws FileNotFoundException, IOException { /* Send Loan for Pre Closure */
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./PreClosureDetails.dat"));
		out.writeObject(loanMaster);
		out.close();
		return true;

	}

	public boolean uploadDocument(Document document, LoanMaster loanMaster) throws IBSException {
		boolean isDone = false;
		FileInputStream inputStream = null;
		Connection connection = OracleDataBaseUtil.getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(QueryMapper.UPLOAD_DOCUMENT);) {
			if (null == inputStream) {
				File uploadFile = new File(document.getPathOfDocument());
				inputStream = new FileInputStream(uploadFile);
				preparedStatement.setBinaryStream(1, inputStream);
				preparedStatement.setBigDecimal(2, new BigDecimal(loanMaster.getCustomerBean().getUCI()));
				preparedStatement.executeUpdate();
				isDone = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}
		return isDone;
	}

	@Override
	public boolean verifyCustomer(String userId) throws IBSException {
		boolean check = false;
		Connection connection = OracleDataBaseUtil.getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(QueryMapper.VERIFY_CUSTOMER);) {
			preparedStatement.setString(1, userId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					if (resultSet.getString("user_id").equals(userId)) {
						check = true;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new IBSException(ExceptionMessages.MESSAGEFORSQLEXCEPTION);
			}

		} catch (SQLException exp) {
			exp.printStackTrace();
		}
		return check;
	}

	@Override
	public boolean sendLoanForVerification(LoanMaster loanMaster) throws IBSException {
		boolean check = false;
		BigDecimal uciTemp = null;
		Connection connection = OracleDataBaseUtil.getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(QueryMapper.GET_UCI);) {
			preparedStatement.setString(1, loanMaster.getCustomerBean().getUserId());
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (null == uciTemp) {
					if (resultSet.next()) {
						uciTemp = resultSet.getBigDecimal("uci");
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IBSException(ExceptionMessages.MESSAGEFORSQLEXCEPTION);
		}

		try (PreparedStatement preparedStatement = connection.prepareStatement(QueryMapper.INS_APP);) {
			System.out.println(loanMaster.getApplicationNumber());
			preparedStatement.setLong(1,loanMaster.getApplicationNumber());
			preparedStatement.setBigDecimal(2, uciTemp);
			preparedStatement.setString(3, "pending");
			check = true;
			System.out.println("sent");
			int i=preparedStatement.executeUpdate();
			connection.commit();
			System.out.println(i);
		} catch (SQLException exp) {
			exp.printStackTrace();

		}

		return check;
	}
}