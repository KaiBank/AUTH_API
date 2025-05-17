package com.kaiasia.app.service.Auth_api.dao;

import com.kaiasia.app.core.dao.CommonDAO;
import com.kaiasia.app.core.dao.PosgrestDAOHelper;
import com.kaiasia.app.service.Auth_api.model.request.Auth1Request;
import com.kaiasia.app.service.Auth_api.model.response.Auth1Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

//@Component
public class SessionIdDAO extends CommonDAO implements IAuthSessionDao {
	private static final Logger log = Logger.getLogger(SessionIdDAO.class);
	@Autowired
	private PosgrestDAOHelper posgrestDAOHelper;

	@Value("${kai.time2live}")
	private int time2livee;

	@Override
	public int insertSessionId(Auth1Request auth1Request) throws Exception {
		String sql = "INSERT INTO \n" + this.getTableName() +
				"(username, start_time, end_time, session_id, channel, location, phone, email, company_code, customer_id)\n" +
				"VALUES(:USERNAME, :START_TIME, :END_TIME,:SESSION_ID, :CHANNEL, :LOCATION,:PHONE, :EMAIL, :COMPANY_CODE, :CUSTOMER_ID);";
		HashMap<String, Object> param = new HashMap();
		param.put("USERNAME", auth1Request.getUsername());
		param.put("START_TIME", auth1Request.getStartTime());
		param.put("END_TIME", auth1Request.getEndTime());
		param.put("SESSION_ID", auth1Request.getSessionId());
		param.put("CHANNEL", auth1Request.getChannel());
		param.put("LOCATION", auth1Request.getLocation());
		param.put("PHONE", auth1Request.getPhone());
		param.put("EMAIL", auth1Request.getEmail());
		param.put("COMPANY_CODE", auth1Request.getCompanyCode());
		param.put("CUSTOMER_ID", auth1Request.getCustomerId());
		int result = posgrestDAOHelper.update(sql, param);
		return result;
	}

	@Override
	public boolean isSessionUsername(String customerId) throws Exception {
		// kiểm tra customer Id có trong bảng session không ?
		String sql = "SELECT COUNT(*) FROM " + this.getTableName() + " WHERE customer_id = :CUSTOMER_ID";

		HashMap<String, Object> param = new HashMap<>();
		param.put("CUSTOMER_ID", customerId);

		// Thực hiện truy vấn và lấy kết quả
		int count = posgrestDAOHelper.query4Int(sql, param);

		// Nếu count > 0, customerId tồn tại
		return count > 0;
	}

	//    @Transactional
	@Override
	public Auth1Response getAuthSessionId(String sessionId) throws Exception {
		String sql = "SELECT * " + "FROM " + this.getTableName() + " WHERE session_id = :SESSION_ID";
		HashMap<String, Object> param = new HashMap<>();
		param.put("SESSION_ID", sessionId);

		Auth1Response auth1Response = posgrestDAOHelper.querySingle(
				sql,
				param,
				new BeanPropertyRowMapper<>(Auth1Response.class)
		);

		return auth1Response; // Trả về thông tin session
	}

	@Override
	public int updateExpireSessionId(String sessionId) throws Exception {
		String sql = "UPDATE " + this.getTableName() + " SET end_time = :END_TIME WHERE session_id = :SESSION_ID";
		HashMap<String, Object> param = new HashMap<>();
		Date now = new Date();

		// Calculate new end time by adding time2livee (in minutes) to the current time
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.MINUTE, time2livee);
		Date newEndTime = calendar.getTime();

		param.put("SESSION_ID", sessionId);
		param.put("END_TIME", newEndTime); // thời điểm hiện tại + 30 minutes

		int result = posgrestDAOHelper.update(sql, param);
		return result;
	}

	@Override
	public int deleteExpireSessionId() throws Exception {
		Date date = new Date();
		long currentTimeMillis = date.getTime();

		long currentTimeSeconds = currentTimeMillis / 1000;

		String sql = "DELETE FROM " + this.getTableName() + " WHERE end_time < TO_TIMESTAMP(:currentTime)";

		HashMap<String, Object> param = new HashMap<>();
		param.put("currentTime", currentTimeSeconds);

		int result = posgrestDAOHelper.update(sql, param);
		return result;
	}

	@Override
	public int deleteSessionByCustomerId(String customerId) throws Exception {
		String sql = "DELETE FROM " + this.getTableName() + " WHERE customer_id = :CUSTOMER_ID";

		HashMap<String, Object> param = new HashMap<>();
		param.put("CUSTOMER_ID", customerId);

		int result = posgrestDAOHelper.update(sql, param);
		return result;
	}

	@Override
	public int expireSessionImmediately(String sessionId) {
		String sql = "UPDATE " + this.getTableName() + " SET end_time = NOW() WHERE session_id = :SESSION_ID";
		HashMap<String, Object> param = new HashMap<>();
		param.put("SESSION_ID", sessionId);
		int result = 0;

		try {
			result = posgrestDAOHelper.update(sql, param);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		log.info("Session " + sessionId + " has been expired immediately.");

		return result;
	}

}
