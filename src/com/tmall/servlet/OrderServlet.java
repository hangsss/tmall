package com.tmall.servlet;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmall.beans.Order;
import com.tmall.util.Page;

@SuppressWarnings("serial")
public class OrderServlet extends BackServlet {

	public String add(HttpServletResponse response, HttpServletRequest request, Page page) {
		return "";
	}

	public String delete(HttpServletResponse response, HttpServletRequest request, Page page) {
		return "";
	}

	public String edit(HttpServletResponse response, HttpServletRequest request, Page page) {
		return "";
	}

	public String update(HttpServletResponse response, HttpServletRequest request, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Order o = orderDAO.get(id);

		switch (request.getParameter("operaty")) {
		case "delivery":
			o.setDeliveryDate(new Date());
			o.setStatus("待收货");
			break;
		}
		orderDAO.update(o);

		return "@admin_Order_list";
	}

	public String list(HttpServletResponse response, HttpServletRequest request, Page page) {
		List<Order> os = orderDAO.list(page.getBegin(), page.getSingleCount());

		int total = orderDAO.getTotal();
		page.setTotalCount(total);

		request.setAttribute("theos", os);
		request.setAttribute("page", page);

		return "admin/listOrder.jsp";
	}

}
