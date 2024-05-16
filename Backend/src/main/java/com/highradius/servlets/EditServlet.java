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
 * Servlet implementation class EditServlet
 */
@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
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
        Invoice updatedInvoice = new Invoice(slNo, customerOrderID, salesOrg, distributionChannel, division, releasedCreditValue,
                purchaseOrderType, companyCode, orderCreationDate, orderCreationTime, creditControlArea, soldToParty,
                orderAmount, requestedDeliveryDate, orderCurrency, creditStatus, customerNumber, amountInUsd,
                uniqueCustNumber);

        // Update the invoice in the database
        invoiceDaoImpl.updateInvoice(String.valueOf(customerOrderID), updatedInvoice);

        // Set the response type
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        // Send the response
        response.getWriter().write("Invoice updated successfully.");

	}
	

}
