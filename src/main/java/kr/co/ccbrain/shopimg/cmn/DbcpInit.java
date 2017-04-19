package kr.co.ccbrain.shopimg.cmn;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.sql.DataSource;

import kr.co.ccbrain.shopimg.util.AES256Util;

public class DbcpInit extends org.apache.commons.dbcp.BasicDataSource {

	@Override
	protected synchronized DataSource createDataSource() throws SQLException {

		return super.createDataSource();
	}

	@Override
	public synchronized void setDriverClassName(String driverClassName) {

		super.setDriverClassName(driverClassName);
	}

	@Override
	public synchronized void setUrl(String url) {

		super.setUrl(url);
	}

	@Override
	public void setUsername(String username) {

		AES256Util aes256;
		try {
			aes256 = new AES256Util();
			username = aes256.decryptAES(username);
		} catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}

		super.setUsername(username);
	}

	@Override
	public void setPassword(String password) {

		AES256Util aes256;
		try {
			aes256 = new AES256Util();
			password = aes256.decryptAES(password);
		} catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}

		super.setPassword(password);
	}

	@Override
	public synchronized void setInitialSize(int initialSize) {

		super.setInitialSize(initialSize);
	}

}
