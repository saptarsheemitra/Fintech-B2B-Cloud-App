package com.highradius.servlets;

import com.highradius.implementation.InvoiceDaoImpl;
import com.highradius.model.Invoice;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * Servlet implementation class AddServlet
 */
@WebServlet("/AddServlet")
public class AddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		InvoiceDaoImpl invoiceDaoImpl = new InvoiceDaoImpl();
		// Retrieve invoice data from the request parameters
        int slNo = Integer.parseInt(request.getParameter("slNo"));
        int customerOrderID = Integer.parseInt(request.getParameter("customerOrderID"));
        int salesOrg = Integer.parseInt(request.getParameter("salesOrg"));
        String distributionChannel = request.getParameter("distributionChannel");
        String division = request.getParameter("division");
        float releasedCreditValue = Float.parseFloat(request.getParameter("releasedCreditValue"));
        String purchaseOrderType = request.getParameter("purchaseOrderType");
        int companyCode = Integer.parseInt(request.getParameter("companyCode"));
        String orderCreationDate = request.getParameter("orderCreationDate");
        String orderCreationTime = request.getParameter("orderCreationTime");
        String creditControlArea = request.getParameter("creditControlArea");
        int soldToParty = Integer.parseInt(request.getParameter("soldToParty"));
        float orderAmount = Float.parseFloat(request.getParameter("orderAmount"));
        String requestedDeliveryDate = request.getParameter("requestedDeliveryDate");
        String orderCurrency = request.getParameter("orderCurrency");
        String creditStatus = request.getParameter("creditStatus");
        int customerNumber = Integer.parseInt(request.getParameter("customerNumber"));
        float amountInUsd = Float.parseFloat(request.getParameter("amountInUsd"));
        long uniqueCustNumber = Long.parseLong(request.getParameter("uniqueCustNumber"));

        // Create a new Invoice object
        Invoice newInvoice = new Invoice(slNo, customerOrderID, salesOrg, distributionChannel, division, releasedCreditValue,
                purchaseOrderType, companyCode, orderCreationDate, orderCreationTime, creditControlArea, soldToParty,
                orderAmount, requestedDeliveryDate, orderCurrency, creditStatus, customerNumber, amountInUsd,
                uniqueCustNumber);

        // Add the new invoice to the database
        try {
			invoiceDaoImpl.insertInvoice(newInvoice);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Set the response type
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        // Send the response
        response.getWriter().write("Invoice added successfully.");
	}

}
