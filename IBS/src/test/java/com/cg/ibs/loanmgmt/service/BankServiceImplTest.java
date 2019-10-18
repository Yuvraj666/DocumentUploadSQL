package com.cg.ibs.loanmgmt.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cg.ibs.loanmgmt.bean.CustomerBean;
import com.cg.ibs.loanmgmt.bean.LoanMaster;
import com.cg.ibs.loanmgmt.bean.LoanType;
import com.cg.ibs.loanmgmt.exception.ExceptionMessages;
import com.cg.ibs.loanmgmt.exception.IBSException;

class BankServiceImplTest {
	private static BankServiceImpl bankService = new BankServiceImpl();

	private static LoanMaster loanMasterTemp;

	@BeforeEach
	void init() {
		loanMasterTemp = new LoanMaster();
	}

	@AfterEach
	void destroy() {
		loanMasterTemp = null;
	}

	// UPDATE_PRECLOSURE
	@Test
	void updatePreClosurePositiveTest() {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.HOME_LOAN);
		loanMasterTemp.setEmiAmount(78954.00);
		loanMasterTemp.setInterestRate(8.5f);
		loanMasterTemp.setLoanAmount(2000000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(1234);
		CustomerBean customerBean = new CustomerBean("Dev", "Goyal", "1234", "dgoyal");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(13);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		LoanMaster actual = bankService.updatePreClosure(loanMasterTemp);

		if (!((24 == actual.getNumberOfEmis()) && (null == actual.getNextEmiDate()))) {
			fail("Test Failed");
		}
	}

	@Test
	void updatePreClosureNegativeTest() {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.HOME_LOAN);
		loanMasterTemp.setEmiAmount(78954.00);
		loanMasterTemp.setInterestRate(8.5f);
		loanMasterTemp.setLoanAmount(2000000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(1234);
		CustomerBean customerBean = new CustomerBean("Dev", "Goyal", "1234", "dgoyal");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(13);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		LoanMaster actual = bankService.updatePreClosure(loanMasterTemp);

		if ((19 == actual.getNumberOfEmis()) && (LocalDate.now() == actual.getNextEmiDate())) {
			fail("Test Failed");
		}
	}

	// GET_DOCUMENTS_FOR_VERIFICATION
//	@Test
//	public void getDocumentsForVerificationTest() {
//		assertThrows(IOException.class, () -> {
//			bankService.getDocumentsForVerification();
//		});
//	}

	// VERIFY_LOAN
	@Test
	void verifyLoanPositiveTest() {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.HOME_LOAN);
		loanMasterTemp.setEmiAmount(78954.00);
		loanMasterTemp.setInterestRate(8.5f);
		loanMasterTemp.setLoanAmount(2000000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(1234);
		CustomerBean customerBean = new CustomerBean("Shreya", "Sinha", "1234", "ssinha");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(13);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		try {
			bankService.verifyLoan(loanMasterTemp);
		} catch (Exception exp) {
			fail("Test Failed");
		}

	}

	// GET_LOAN_DETAILS_FOR_VERIFICATION

	// WRITING A WRONG OBJECT IN FUNCTION
	@Test
	void getLoanDetailsForVerificationNeutralTest() throws IOException {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.HOME_LOAN);
		loanMasterTemp.setEmiAmount(78954.00);
		loanMasterTemp.setInterestRate(8.5f);
		loanMasterTemp.setLoanAmount(2000000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(1234);
		CustomerBean customerBean = new CustomerBean("Shreya", "Sinha", "1234", "ssinha");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(13);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./LoanDetails.dat"));
		out.writeObject(customerBean);
		out.close();
		assertThrows(ClassCastException.class, () -> {
			bankService.getLoanDetailsForVerification();
		});
	}

	@Test
	void getLoanDetailsForVerificationPositiveTest() throws FileNotFoundException, IOException {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.HOME_LOAN);
		loanMasterTemp.setEmiAmount(78954.00);
		loanMasterTemp.setInterestRate(8.5f);
		loanMasterTemp.setLoanAmount(2000000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(1234);
		CustomerBean customerBean = new CustomerBean("Shreya", "Sinha", "1234", "ssinha");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(13);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./LoanDetails.dat"));
		out.writeObject(loanMasterTemp);
		out.close();
		LoanMaster loanMasterActual = null;
		try {
			loanMasterActual = bankService.getLoanDetailsForVerification();
		} catch (ClassNotFoundException exp) {
			fail("Test Failed : " + exp.getMessage());
		}

		LoanMaster loanMasterExpected = new LoanMaster("1234", LoanType.HOME_LOAN, 2000000.00, 24, 30996.0,
				customerBean, 13, 24, LocalDate.of(2018, Month.SEPTEMBER, 8), LocalDate.of(2019, 10, 14));

		assertEquals(loanMasterExpected.getLoanNumber(), loanMasterActual.getLoanNumber());
	}

	@Test
	void getLoanDetailsForVerificationNegativeTest() throws FileNotFoundException, IOException {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.HOME_LOAN);
		loanMasterTemp.setEmiAmount(78954.00);
		loanMasterTemp.setInterestRate(8.5f);
		loanMasterTemp.setLoanAmount(2000000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(1234);
		CustomerBean customerBean = new CustomerBean("Shreya", "Sinha", "1234", "ssinha");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(13);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./LoanDetails.dat"));
		out.writeObject(loanMasterTemp);
		out.close();
		LoanMaster loanMasterActual = null;
		try {
			loanMasterActual = bankService.getLoanDetailsForVerification();
		} catch (ClassNotFoundException exp) {
			fail("Test Failed : " + exp.getMessage());
		}

		LoanMaster loanMasterExpected = new LoanMaster("1235", LoanType.HOME_LOAN, 2000000.00, 24, 30996.0,
				customerBean, 13, 24, LocalDate.of(2018, Month.SEPTEMBER, 8), LocalDate.of(2019, 10, 14));

		assertNotEquals(loanMasterExpected.getLoanNumber(), loanMasterActual.getLoanNumber());
	}

	// GET_PRECLOSURE_DETAILS_FOR_VERIFICATION
	@Test
	void getPreClosureDetailsForVerificationNeutralTest() throws FileNotFoundException, IOException {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.HOME_LOAN);
		loanMasterTemp.setEmiAmount(78954.00);
		loanMasterTemp.setInterestRate(8.5f);
		loanMasterTemp.setLoanAmount(2000000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(1234);
		CustomerBean customerBean = new CustomerBean("Shreya", "Sinha", "1234", "ssinha");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(13);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./PreClosureDetails.dat"));
		out.writeObject(customerBean);
		out.close();
		assertThrows(ClassCastException.class, () -> {
			bankService.getPreClosureDetailsForVerification();
		});
	}

	@Test
	void getPreClosureDetailsForVerificationPositiveTest() throws FileNotFoundException, IOException {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.HOME_LOAN);
		loanMasterTemp.setEmiAmount(78954.00);
		loanMasterTemp.setInterestRate(8.5f);
		loanMasterTemp.setLoanAmount(2000000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(1234);
		CustomerBean customerBean = new CustomerBean("Shreya", "Sinha", "1234", "ssinha");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(13);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./PreClosureDetails.dat"));
		out.writeObject(loanMasterTemp);
		out.close();
		LoanMaster loanMasterActual = null;
		try {
			loanMasterActual = bankService.getPreClosureDetailsForVerification();
		} catch (ClassNotFoundException exp) {
			fail("Test Failed : " + exp.getMessage());
		}

		LoanMaster loanMasterExpected = new LoanMaster("1234", LoanType.HOME_LOAN, 2000000.00, 24, 30996.0,
				customerBean, 13, 24, LocalDate.of(2018, Month.SEPTEMBER, 8), LocalDate.of(2019, 10, 14));

		assertEquals(loanMasterExpected.getLoanNumber(), loanMasterActual.getLoanNumber());
	}

	@Test
	void getPreClosureDetailsForVerificationNeutralTest2() throws FileNotFoundException, IOException {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.HOME_LOAN);
		loanMasterTemp.setEmiAmount(78954.00);
		loanMasterTemp.setInterestRate(8.5f);
		loanMasterTemp.setLoanAmount(2000000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(1234);
		CustomerBean customerBean = new CustomerBean("Shreya", "Sinha", "1234", "ssinha");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(13);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./PreClosureDetailsXYZ.dat"));
		out.writeObject(loanMasterTemp);
		out.close();
		LoanMaster loanMasterActual = null;
		assertThrows(ClassCastException.class, () -> {
			bankService.getPreClosureDetailsForVerification();
		});

	}

	@Test
	void getPreClosureDetailsForVerificationNegativeTest() throws FileNotFoundException, IOException {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.HOME_LOAN);
		loanMasterTemp.setEmiAmount(78954.00);
		loanMasterTemp.setInterestRate(8.5f);
		loanMasterTemp.setLoanAmount(2000000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(1234);
		CustomerBean customerBean = new CustomerBean("Shreya", "Sinha", "1234", "ssinha");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(13);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./PreClosureDetails.dat"));
		out.writeObject(loanMasterTemp);
		out.close();
		LoanMaster loanMasterActual = null;
		try {
			loanMasterActual = bankService.getPreClosureDetailsForVerification();
		} catch (ClassNotFoundException exp) {
			fail("Test Failed : " + exp.getMessage());
		}

		LoanMaster loanMasterExpected = new LoanMaster("1235", LoanType.HOME_LOAN, 2000000.00, 24, 30996.0,
				customerBean, 13, 24, LocalDate.of(2018, Month.SEPTEMBER, 8), LocalDate.of(2019, 10, 14));

		assertNotEquals(loanMasterExpected.getLoanNumber(), loanMasterActual.getLoanNumber());
	}

	@Test
	void getFilesAvailableTest() {
		List<String> filesActual = bankService.getFilesAvailable();
		assertNotNull(filesActual);
	}

	@Test
	void downloadDocumentPositiveTest() throws IBSException {
		String destPath = "C:\\Users\\Lenovo\\Desktop\\IBSLoanManagementCopyDocTest";
		String fileName = "AadharCard";
		boolean actual = bankService.downloadDocument(destPath, fileName);
		assertEquals(false, actual);
	}

}
