package com.ms.branch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ms.common.Constant;
import com.ms.common.Util;

@Repository
public class BranchDAO {

	private JdbcTemplate jdbc;
	
	@Autowired
	public void setJdbcTemplate(@Qualifier("dataSource")DataSource dataSource) {
	    this.jdbc = new JdbcTemplate(dataSource);
	}
	
	public static Logger log = LogManager.getLogger(BranchDAO.class);

	public Map<Boolean,Object> getBranchDetails(int statusCode) {
		Map<Boolean,Object> result = new LinkedHashMap<Boolean, Object>();
		
		try {
			StringBuffer query = new StringBuffer().append(
					"SELECT b.seqid, b.branchName, b.address, b.postcode, d.districtname, s.stateName, b.status FROM masp.branch b, masp.district d, state s ")
					.append("WHERE b.districtid = d.seqid AND d.stateid = s.seqid AND b.status = ?");
			List<Map<String, Object>> rows = jdbc.queryForList(query.toString(), statusCode);
			if (rows.size() > 0) {
				for (Map<String, Object> row : rows) {
					String id = Util.trimString((String) row.get("seqid"));
					String branchName = Util.trimString((String) row.get("branchName"));
					String address = Util.trimString((String) row.get("address"));
					int postcode = (int) row.get("postcode");
					String district = Util.trimString((String) row.get("districtname"));
					String state = Util.trimString((String) row.get("stateName"));
					String status = Util.getStatusDesc((int) row.get("status"));
					
					Branch data = new Branch(id, branchName, address,
							postcode, district, state, status);
					result.put(true, data);
					
				}
			} else {
				log.info("Get Branches Info with status code: No record found." );
				result.put(false,Constant.NO_RECORD_FOUND);
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			result.put(false,Constant.DATABASE_CONNECTION_LOST);
		} catch (Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			result.put(false,Constant.UNKNOWN_ERROR_OCCURED);
		}
		return result;
	}

	public Map<Boolean,Object> getBranchDetails(String seqid) {
		Map<Boolean,Object> result = new LinkedHashMap<Boolean, Object>();
		try {
			StringBuffer query = new StringBuffer().append(
					"SELECT b.seqid, b.branchName, b.address, b.postcode, d.districtname, s.stateName, b.status FROM masp.branch b, masp.district d, masp.state s ")
					.append("WHERE b.seqid = ? AND b.districtid = d.seqid AND d.stateid = s.seqid");
			List<Map<String, Object>> rows = jdbc.queryForList(query.toString(), seqid);
			if (rows.size() > 0) {
				for (Map<String, Object> row : rows) {
					String id = Util.trimString((String) row.get("seqid"));
					String branchName = Util.trimString((String) row.get("branchName"));
					String address = Util.trimString((String) row.get("address"));
					int postcode = (int) row.get("postcode");
					String district = Util.trimString((String) row.get("districtname"));
					String state = Util.trimString((String) row.get("stateName"));
					String status = String.valueOf((int) row.get("status"));
					Branch data = new Branch(id, branchName, address,
							postcode, district, state, status);
					result.put(true, data);
				}
			} else {
				log.info("Get Branch Details with ID: No record found.");
				result.put(false,Constant.NO_RECORD_FOUND);
			}
		} catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			result.put(false,Constant.DATABASE_CONNECTION_LOST);
		} catch (Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			result.put(false,Constant.UNKNOWN_ERROR_OCCURED);
		}
		return result;
	}

	public 	Map<Boolean,Object> getBranchDetails() {
		Map<Boolean,Object> result = new LinkedHashMap<Boolean, Object>();
		try {
			StringBuffer query = new StringBuffer().append(
					"SELECT b.seqid, b.branchName, b.address, b.postcode, d.districtname, s.stateName, b.status FROM masp.branch b, masp.district d, masp.state s ")
					.append("WHERE b.districtid = d.seqid AND d.stateid = s.seqid");

			List<Map<String, Object>> rows = jdbc.queryForList(query.toString());
			if (rows.size() > 0) {
				List<Branch> data = new ArrayList<Branch>();
				for (Map<String, Object> row : rows) {
					String seqid = Util.trimString((String) row.get("seqid"));
					String branchName = Util.trimString((String) row.get("branchName"));
					String district = Util.trimString((String) row.get("districtname"));
					String state = Util.trimString((String) row.get("stateName"));
					String status = Util.getStatusDesc((int) row.get("status"));
					data.add(new Branch(seqid, branchName, district, state, status));
				}
				result.put(true, data);
			}
			else {
				log.info("Get Branches Details As Admin: No record found.");
				result.put(false,Constant.NO_RECORD_FOUND);
			}
		} catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			result.put(false,Constant.DATABASE_CONNECTION_LOST);
		} catch (Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			result.put(false,Constant.UNKNOWN_ERROR_OCCURED);
		}
		return result;
	}

	public String deleteBranch(String branchID) {
		String message = null;
		try {
			StringBuffer query = new StringBuffer().append("UPDATE masp.branch SET status = ? WHERE seqid = ?");
			int result = jdbc.update(query.toString(), Constant.REMOVED_STATUS_CODE, branchID);
			if (result > 0) {
				message = null;
			} else {
				message = "Unable to remove. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			message = Constant.DATABASE_CONNECTION_LOST;
		}
		catch (Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			message = "Unable to remove due to unexpected error occured. Please try again later.";
		}
		return message;
	}

	public String updateStatus(int statusCode, String branchId) {
		String message = null;
		try {
			StringBuffer query = new StringBuffer().append("UPDATE masp.branch SET status = ? WHERE seqid = ?");
			int result = jdbc.update(query.toString(), statusCode, branchId);
			if (result > 0) {
				message = null;
			} else {
				message = "Unable to update the status. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			message = Constant.DATABASE_CONNECTION_LOST;
		}
		catch (Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			message = "Unable to update the status due to unexpected error occured. Please try again later.";
		}
		return message;
	}
	
	public String addBranch(String seqid, NewBranchForm form) {
		try {
			StringBuffer query = new StringBuffer()
					.append("INSERT INTO masp.branch (seqid,branchName,address,postcode,districtid) values(?,?,?,?,?)");
			int result = jdbc.update(query.toString(), seqid, form.getBranchname(), form.getAddress(),
					form.getPostcode(), form.getDistrict());
			if (result > 0) {
				return null;
			} else {
				return "Unable to create branch. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return Constant.DATABASE_CONNECTION_LOST;
		}
		catch (Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			return "Unable to add new branch due to unexpected error occured. Please try again later.";
		}
	}

	public String updateBranch(String seqid,NewBranchForm form) {
		try {
			StringBuffer query = new StringBuffer().append(
					"UPDATE masp.branch SET branchName = ?, address = ?, postcode = ?, districtid = ?, status = ? WHERE seqid = ? AND status != ?");
			int result = jdbc.update(query.toString(), form.getBranchname(), form.getAddress(), form.getPostcode(),
					form.getDistrict(), form.getStatus(),seqid,Constant.REMOVED_STATUS_CODE);
			if (result > 0) {
				return null;
			} else {
				log.error("Unable to locate branch in database.");
				return "Unable to update the details. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return Constant.DATABASE_CONNECTION_LOST;
		}
		catch (Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			return "Unexpected error occured, unable to update the information.";
		}
	}

	public List<State> retrieveAllState() {
		List<State> states = null;
		try {
			StringBuffer query = new StringBuffer().append("SELECT seqid, stateName FROM masp.state");
			List<Map<String, Object>> rows = jdbc.queryForList(query.toString());
			if (rows.size() > 0) {
				states = new LinkedList<State>();
				for (Map<String, Object> row : rows) {
					String seqid = Util.trimString((String) row.get("seqid"));
					String name = Util.trimString((String) row.get("stateName"));

					State state = new State(seqid, name);
					states.add(state);
				}
				return states;
			}
			else {
				return null;
			}
		} catch (Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			return null;
		}
	}

	public List<District> retrieveDistricts(String stateId) {
		List<District> districts = null;
		try {
			StringBuffer query = new StringBuffer()
					.append("SELECT seqid, districtname FROM masp.district where stateid = ?");
			List<Map<String, Object>> rows = jdbc.queryForList(query.toString(), stateId);
			if (rows.size() > 0) {
				districts = new LinkedList<District>();
				for (Map<String, Object> row : rows) {
					String seqid = Util.trimString((String) row.get("seqid"));
					String name = Util.trimString((String) row.get("districtname"));

					District state = new District(seqid, name);
					districts.add(state);
				}
				return districts;
			}
			else {
				return null;
			}
		} catch (Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			return null;
		}
	}

	public Boolean findBranchByName(String branchName) {
		try {
			StringBuffer query = new StringBuffer().append("SELECT seqid from masp.branch where branchname = ?");
			List<Map<String, Object>> records = jdbc.queryForList(query.toString(), branchName);
			if (records.size() > 0) {
				return false;
			} else {
				return true;
			}
		} catch (Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return false;
		}
	}
	
	//backend
	public String getBranchName(String branchid) {
		try {
			StringBuffer query = new StringBuffer().append("SELECT branchname from masp.branch where seqid = ?");
			String name = jdbc.queryForObject(query.toString(),String.class,branchid);
			if(Util.trimString(name) != "") {
				return name;
			}
			else {
				return null;
			}
		} catch (Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return null;
		}
	}
}
