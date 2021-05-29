package com.ms.branch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

	public ResponseBranchInfo getBranchDetails(int statusCode) {
		ResponseBranchInfo result = null;
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
					ResponseBranchInfo.Result branchDetails = new ResponseBranchInfo.Result(id, branchName, address,
							postcode, district, state, status);
					result = new ResponseBranchInfo(branchDetails);
				}
			} else {
				result = new ResponseBranchInfo("No record Found.");
			}
		} catch (Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			result = new ResponseBranchInfo("Unable to retrieve record.");
		}
		return result;
	}

	public ResponseBranchInfo getBranchDetails(String seqid) {
		ResponseBranchInfo result = null;
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
					String status = Util.getStatusDesc((int) row.get("status"));
					ResponseBranchInfo.Result branchDetails = new ResponseBranchInfo.Result(id, branchName, address,
							postcode, district, state, status);
					result = new ResponseBranchInfo(branchDetails);
				}
			} else {
				result = new ResponseBranchInfo("No record Found.");
			}
		} catch (Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			result = new ResponseBranchInfo("Unable to retrieve record.");
		}
		return result;
	}

	public ResponseBranchInfo getBranchDetails() {
		ResponseBranchInfo result = null;
		try {
			StringBuffer query = new StringBuffer().append(
					"SELECT b.seqid, b.branchName, b.address, b.postcode, d.districtname, s.stateName, b.status FROM masp.branch b, masp.district d, masp.state s ")
					.append("WHERE b.districtid = d.seqid AND d.stateid = s.seqid");

			List<Map<String, Object>> rows = jdbc.queryForList(query.toString());
			if (rows.size() > 0) {
				List<ResponseBranchInfo.Result> resultList = new ArrayList<ResponseBranchInfo.Result>();
				for (Map<String, Object> row : rows) {
					String seqid = Util.trimString((String) row.get("seqid"));
					String branchName = Util.trimString((String) row.get("branchName"));
					String address = Util.trimString((String) row.get("address"));
					int postcode = (int) row.get("postcode");
					String district = Util.trimString((String) row.get("districtname"));
					String state = Util.trimString((String) row.get("stateName"));
					String status = Util.getStatusDesc((int) row.get("status"));
					resultList.add(new ResponseBranchInfo.Result(seqid, branchName, address, postcode, district, state,
							status));
				}

				result = new ResponseBranchInfo(resultList);
				return result;
			}
		} catch (Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return new ResponseBranchInfo("Unable to retrieve records.");
		}
		return new ResponseBranchInfo("No records.");
	}

	public String deleteBranch(String branchID) {
		String message = null;
		try {
			StringBuffer query = new StringBuffer().append("UPDATE masp.branch SET status = ? WHERE seqid = ?");
			int result = jdbc.update(query.toString(), Constant.REMOVED_STATUS_CODE, branchID);
			if (result > 0) {
				message = "Branch removed.";
			} else {
				message = "Unable to remove.";
			}
		} catch (Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			message = "Error occured, unable to remove.";
		}
		return message;
	}

	public String updateStatus(int statusCode, String branchId) {
		String message = null;
		try {
			StringBuffer query = new StringBuffer().append("UPDATE masp.branch SET status = ? WHERE seqid = ?");
			int result = jdbc.update(query.toString(), statusCode, branchId);
			if (result > 0) {
				message = "Status updated";
			} else {
				message = "Unable to update the status";
			}
		} catch (Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			message = "Error occured, unable to update the status.";
		}
		return message;
	}

	public List<States.Result> retrieveAllState() {
		List<States.Result> states = null;
		try {
			StringBuffer query = new StringBuffer().append("SELECT seqid, stateName FROM masp.state");
			List<Map<String, Object>> rows = jdbc.queryForList(query.toString());
			if (rows.size() > 0) {
				states = new LinkedList<States.Result>();
				for (Map<String, Object> row : rows) {
					String seqid = Util.trimString((String) row.get("seqid"));
					String name = Util.trimString((String) row.get("stateName"));

					States.Result state = new States.Result(seqid, name);
					states.add(state);
				}
			}
		} catch (Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			states = null;
		}
		return states;
	}

	public List<Districts.Result> retrieveDistricts(String stateId) {
		List<Districts.Result> districts = null;
		try {
			StringBuffer query = new StringBuffer()
					.append("SELECT seqid, districtname FROM masp.district where stateid = ?");
			List<Map<String, Object>> rows = jdbc.queryForList(query.toString(), stateId);
			if (rows.size() > 0) {
				districts = new LinkedList<Districts.Result>();
				for (Map<String, Object> row : rows) {
					String seqid = Util.trimString((String) row.get("seqid"));
					String name = Util.trimString((String) row.get("districtname"));

					Districts.Result state = new Districts.Result(seqid, name);
					districts.add(state);
				}
			}
		} catch (Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			districts = null;
		}
		return districts;
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
		} catch (Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			return "Unexpected error occured. Please try again later.";
		}
	}

	public String updateBranch(String seqid, NewBranchForm form) {
		try {
			StringBuffer query = new StringBuffer().append(
					"UPDATE masp.branch SET branchName = ?, address = ?, postcode = ?, districtid = ? WHERE seqid = ?");
			int result = jdbc.update(query.toString(), form.getBranchname(), form.getAddress(), form.getPostcode(),
					form.getDistrict(), seqid);
			if (result > 0) {
				return "Update successful.";
			} else {
				log.error("Unable to locate staff account.");
				return "Unable to update the details. Please try again later.";
			}
		} catch (Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			return "Unexpected error occured, unable to update the information.";
		}
	}

}
