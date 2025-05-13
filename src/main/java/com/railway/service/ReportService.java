package com.railway.service;

import com.railway.dao.ReportDAO;
import com.railway.model.Booking;
import com.railway.model.Train;
import java.sql.Date;
import java.util.List;

public class ReportService {
    private ReportDAO reportDAO;
    
    public ReportService() {
        this.reportDAO = new ReportDAO();
    }
    
    public double generateRevenueReport(Date startDate, Date endDate) {
        return reportDAO.getRevenueByDateRange(startDate, endDate);
    }
    
    public int generateBookingCountReport(Date startDate, Date endDate) {
        return reportDAO.getBookingsCountByDateRange(startDate, endDate);
    }
    
    public List<Train> generatePopularTrainsReport(int limit) {
        return reportDAO.getMostPopularTrains(limit);
    }
    
    public double generateCancellationRateReport(Date startDate, Date endDate) {
        return reportDAO.getCancellationRate(startDate, endDate);
    }
    
    public List<Booking> generateBookingsByStatusReport(String status) {
        return reportDAO.getBookingsByStatus(status);
    }
}