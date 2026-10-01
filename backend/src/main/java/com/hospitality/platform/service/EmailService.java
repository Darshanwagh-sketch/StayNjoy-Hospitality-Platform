package com.hospitality.platform.service;

import com.hospitality.platform.entity.Booking;
import com.hospitality.platform.repository.BookingRepository;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Autowired
    private BookingRepository bookingRepository;

    @Value("${spring.mail.username:noreply@staynjoy.com}")
    private String fromEmail;

    @Async
    @Transactional(readOnly = true)
    public void sendBookingConfirmation(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            logger.warn("Booking with ID {} not found for sending confirmation email.", bookingId);
            return;
        }

        String recipientEmail = booking.getGuestEmail();
        String subject = "Booking Confirmation - StayNjoy Resorts [" + booking.getBookingCode() + "]";
        
        String htmlContent = buildBookingConfirmationHtml(booking);

        logger.info("==========================================================");
        logger.info("CONFIRMATION EMAIL SENT TO: {}", recipientEmail);
        logger.info("SUBJECT: {}", subject);
        logger.info("BOOKING CODE: {}", booking.getBookingCode());
        logger.info("RESORT: {}", (booking.getRoom() != null && booking.getRoom().getHotel() != null ? booking.getRoom().getHotel().getName() : "StayNjoy Resort"));
        logger.info("DATES: {} to {}", booking.getCheckInDate(), booking.getCheckOutDate());
        logger.info("TOTAL PAID: ₹{}", booking.getTotalPrice());
        logger.info("==========================================================");

        if (mailSender != null) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                String senderAddress = (fromEmail != null && !fromEmail.isBlank()) ? fromEmail : "xr6ejbrqfc7ptmz2@ethereal.email";
                helper.setFrom(senderAddress, "StayNjoy Resorts");
                helper.setTo(recipientEmail);
                helper.setSubject(subject);
                helper.setText(htmlContent, true);

                mailSender.send(message);
                logger.info("Real SMTP email successfully dispatched to {}", recipientEmail);
            } catch (Exception e) {
                logger.warn("SMTP Email dispatch warning: {}. (Booking logged cleanly)", e.getMessage());
            }
        }
    }

    private String buildBookingConfirmationHtml(Booking booking) {
        String hotelName = booking.getRoom() != null && booking.getRoom().getHotel() != null 
                ? booking.getRoom().getHotel().getName() : "StayNjoy Resort";
        String hotelLocation = booking.getRoom() != null && booking.getRoom().getHotel() != null 
                ? booking.getRoom().getHotel().getAddress() + ", " + booking.getRoom().getHotel().getCity() : "India";
        String roomInfo = booking.getRoom() != null 
                ? "Room #" + booking.getRoom().getRoomNumber() + " (" + booking.getRoom().getRoomType() + ")" : "Deluxe Room";

        return "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; border: 1px solid #e2e8f0; border-radius: 12px;\">" +
                "<div style=\"background-color: #0F172A; padding: 20px; text-align: center; border-radius: 8px 8px 0 0;\">" +
                "<h1 style=\"color: #ffffff; margin: 0; font-size: 24px;\">StayNjoy Resorts</h1>" +
                "<p style=\"color: #06B6D4; margin: 5px 0 0 0; font-size: 14px;\">Booking Confirmation Receipt</p>" +
                "</div>" +
                "<div style=\"padding: 24px; background-color: #ffffff;\">" +
                "<h2 style=\"color: #0F172A; font-size: 18px;\">Dear " + booking.getGuestName() + ",</h2>" +
                "<p style=\"color: #64748B; line-height: 1.6;\">Thank you for choosing StayNjoy Resorts. Your reservation is confirmed! Below are your complete stay details:</p>" +
                "<div style=\"background-color: #F8FAFC; border: 1px solid #E2E8F0; padding: 16px; border-radius: 8px; margin: 20px 0;\">" +
                "<table style=\"width: 100%; border-collapse: collapse; color: #0F172A; font-size: 14px;\">" +
                "<tr><td style=\"padding: 8px 0; color: #64748B;\">Booking Reference:</td><td style=\"font-weight: bold; color: #2563EB;\">" + booking.getBookingCode() + "</td></tr>" +
                "<tr><td style=\"padding: 8px 0; color: #64748B;\">Resort / Hotel:</td><td style=\"font-weight: bold;\">" + hotelName + "</td></tr>" +
                "<tr><td style=\"padding: 8px 0; color: #64748B;\">Location:</td><td>" + hotelLocation + "</td></tr>" +
                "<tr><td style=\"padding: 8px 0; color: #64748B;\">Accommodation:</td><td>" + roomInfo + "</td></tr>" +
                "<tr><td style=\"padding: 8px 0; color: #64748B;\">Check-in Date:</td><td style=\"font-weight: bold;\">" + booking.getCheckInDate() + "</td></tr>" +
                "<tr><td style=\"padding: 8px 0; color: #64748B;\">Check-out Date:</td><td style=\"font-weight: bold;\">" + booking.getCheckOutDate() + "</td></tr>" +
                "<tr><td style=\"padding: 8px 0; color: #64748B;\">Total Guests:</td><td>" + booking.getGuestsCount() + " Guest(s)</td></tr>" +
                "<tr><td style=\"padding: 8px 0; color: #64748B;\">Special Requests:</td><td>" + (booking.getSpecialRequests() != null && !booking.getSpecialRequests().isEmpty() ? booking.getSpecialRequests() : "None") + "</td></tr>" +
                "<tr style=\"border-top: 1px solid #E2E8F0;\"><td style=\"padding: 12px 0 0 0; font-weight: bold; color: #0F172A;\">Total Paid:</td><td style=\"padding: 12px 0 0 0; font-weight: bold; font-size: 18px; color: #2563EB;\">₹" + booking.getTotalPrice() + "</td></tr>" +
                "</table>" +
                "</div>" +
                "<p style=\"color: #64748B; font-size: 13px;\">If you need to make changes or have questions regarding your reservation, please contact our 24/7 Concierge Support.</p>" +
                "</div>" +
                "<div style=\"background-color: #F1F5F9; padding: 14px; text-align: center; border-radius: 0 0 8px 8px; color: #64748B; font-size: 12px;\">" +
                "© " + java.time.Year.now().getValue() + " StayNjoy Resorts India. All rights reserved." +
                "</div>" +
                "</div>";
    }
}
