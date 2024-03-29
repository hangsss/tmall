package com.tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tmall.beans.Order;
import com.tmall.util.DBUtil;
import com.tmall.util.DateUtil;

public class OrderDAO {

	public int getTotal() {
		int total = 0;
		String sql = "SELECT COUNT(*) FROM `orders`";
		try (Connection c = DBUtil.getConnection()) {
			PreparedStatement ps = c.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				total = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return total;
	}

	public void add(Order bean) {
		String sql = "INSERT INTO `orders` (id, orderCode, address, post, receiver, mobile,"
				+ " userMessage, createDate, payDate, deliveryDate, confirmDate, uid, status)"
				+ "VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection c = DBUtil.getConnection()) {
			PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, bean.getOrderCode());
			ps.setString(2, bean.getAddress());
			ps.setString(3, bean.getPost());
			ps.setString(4, bean.getReceiver());
			ps.setString(5, bean.getMobile());
			ps.setString(6, bean.getUserMessage());
			ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
			ps.setTimestamp(8, DateUtil.d2t(bean.getPayDate()));
			ps.setTimestamp(9, DateUtil.d2t(bean.getDeliveryDate()));
			ps.setTimestamp(10, DateUtil.d2t(bean.getConfirmDate()));
			ps.setInt(11, bean.getUser().getId());
			ps.setString(12, bean.getStatus());

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();

			if (rs.next()) {
				bean.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(Order bean) {
		String sql = "UPDATE `orders` SET orderCode = ?, address = ?, post = ?, receiver = ?, mobile = ?,"
				+ "userMessage = ?, createDate = ?, payDate = ?, deliveryDate = ?,"
				+ " confirmDate = ?, uid = ?, status = ?" + "WHERE id = ?";
		try (Connection c = DBUtil.getConnection()) {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, bean.getOrderCode());
			ps.setString(2, bean.getAddress());
			ps.setString(3, bean.getPost());
			ps.setString(4, bean.getReceiver());
			ps.setString(5, bean.getMobile());
			ps.setString(6, bean.getUserMessage());
			ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
			ps.setTimestamp(8, DateUtil.d2t(bean.getPayDate()));
			ps.setTimestamp(9, DateUtil.d2t(bean.getDeliveryDate()));
			ps.setTimestamp(10, DateUtil.d2t(bean.getConfirmDate()));
			ps.setInt(11, bean.getUser().getId());
			ps.setString(12, bean.getStatus());
			ps.setInt(13, bean.getId());

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(int id) {
		String sql = "DELETE FROM `orders` WHERE id = ?";
		try (Connection c = DBUtil.getConnection()) {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Order get(int id) {

		Order bean = null;
		String sql = "SELECT * FROM `orders` WHERE id = ?";

		try (Connection c = DBUtil.getConnection()) {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				bean = new Order();
				bean.setId(id);
				bean.setOrderCode(rs.getString("orderCode"));
				bean.setAddress(rs.getString("address"));
				bean.setPost(rs.getString("post"));
				bean.setReceiver(rs.getString("receiver"));
				bean.setMobile(rs.getString("mobile"));
				bean.setUserMessage(rs.getString("userMessage"));
				bean.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
				bean.setPayDate(DateUtil.t2d(rs.getTimestamp("payDate")));
				bean.setDeliveryDate(DateUtil.t2d(rs.getTimestamp("deliveryDate")));
				bean.setConfirmDate(DateUtil.t2d(rs.getTimestamp("confirmDate")));
				bean.setUser(new UserDAO().get(rs.getInt("uid")));
				bean.setStatus(rs.getString("status"));
				bean.setTotal(new OrderItemDAO().getTotalCost(id));
				bean.setTotalNumber(new OrderItemDAO().getTotalNumber(id));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return bean;
	}

	public List<Order> list(int beg, int len) {
		List<Order> ls = new ArrayList<Order>();
		String sql = "SELECT * FROM `orders` LIMIT ?, ?";

		try (Connection c = DBUtil.getConnection()) {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, beg);
			ps.setInt(2, len);
			OrderItemDAO orderItemDAO = new OrderItemDAO();

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Order bean = new Order();
				bean.setId(rs.getInt("id"));
				bean.setOrderCode(rs.getString("orderCode"));
				bean.setAddress(rs.getString("address"));
				bean.setPost(rs.getString("post"));
				bean.setReceiver(rs.getString("receiver"));
				bean.setMobile(rs.getString("mobile"));
				bean.setUserMessage(rs.getString("userMessage"));
				bean.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
				bean.setPayDate(DateUtil.t2d(rs.getTimestamp("payDate")));
				bean.setDeliveryDate(DateUtil.t2d(rs.getTimestamp("deliveryDate")));
				bean.setConfirmDate(DateUtil.t2d(rs.getTimestamp("confirmDate")));
				bean.setUser(new UserDAO().get(rs.getInt("uid")));
				bean.setStatus(rs.getString("status"));
				bean.setTotal(orderItemDAO.getTotalCost(rs.getInt("id")));
				bean.setTotalNumber(orderItemDAO.getTotalNumber(rs.getInt("id")));
				bean.setOrderItems(orderItemDAO.list(bean.getId()));

				ls.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ls;
	}

	public List<Order> list(int uid) {
		return list(uid, 0, Short.MAX_VALUE);
	}

	public List<Order> list(int uid, int beg, int len) {
		List<Order> ls = new ArrayList<Order>();
		String sql = "SELECT * FROM `orders` WHERE uid = ? LIMIT ?, ?";

		try (Connection c = DBUtil.getConnection()) {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, uid);
			ps.setInt(2, beg);
			ps.setInt(3, len);
			OrderItemDAO orderItemDAO = new OrderItemDAO();

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Order bean = new Order();
				bean.setId(rs.getInt("id"));
				bean.setOrderCode(rs.getString("orderCode"));
				bean.setAddress(rs.getString("address"));
				bean.setPost(rs.getString("post"));
				bean.setReceiver(rs.getString("receiver"));
				bean.setMobile(rs.getString("mobile"));
				bean.setUserMessage(rs.getString("userMessage"));
				bean.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
				bean.setPayDate(DateUtil.t2d(rs.getTimestamp("payDate")));
				bean.setDeliveryDate(DateUtil.t2d(rs.getTimestamp("deliveryDate")));
				bean.setConfirmDate(DateUtil.t2d(rs.getTimestamp("confirmDate")));
				bean.setUser(new UserDAO().get(rs.getInt("uid")));
				bean.setStatus(rs.getString("status"));
				bean.setTotal(orderItemDAO.getTotalCost(rs.getInt("id")));
				bean.setTotalNumber(orderItemDAO.getTotalNumber(rs.getInt("id")));
				bean.setOrderItems(orderItemDAO.list(bean.getId()));

				ls.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ls;
	}
}
