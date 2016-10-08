package rs.petcom.master.dal.document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DocumentPayment {
	private long DocumentPaymentID;
	private long DocumentID;
	private int PaymentMethodID;
	private double Paid;
	public long getDocumentPaymentID() {
		return DocumentPaymentID;
	}
	public void setDocumentPaymentID(long documentPaymentID) {
		DocumentPaymentID = documentPaymentID;
	}
	public long getDocumentID() {
		return DocumentID;
	}
	public void setDocumentID(long documentID) {
		DocumentID = documentID;
	}
	public int getPaymentMethodID() {
		return PaymentMethodID;
	}
	public void setPaymentMethodID(int paymentMethodID) {
		PaymentMethodID = paymentMethodID;
	}
	public double getPaid() {
		return Paid;
	}
	public void setPaid(double paid) {
		Paid = paid;
	}

	public static void insert(long documentId, int paymentMethodId, double paid, Connection connection) throws SQLException{
		String query = "insert into documentpayment (DocumentID,PaymentMethodID,Paid) values(?,?,?)";
		PreparedStatement ps = connection.prepareStatement(query);
		ps.setLong(1, documentId);
		ps.setInt(2, paymentMethodId);
		ps.setDouble(3, paid);
		ps.executeUpdate();
	}
}
