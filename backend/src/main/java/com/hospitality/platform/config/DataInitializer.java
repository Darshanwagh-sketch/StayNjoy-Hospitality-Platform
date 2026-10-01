package com.hospitality.platform.config;

import com.hospitality.platform.entity.*;
import com.hospitality.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return; // Data already initialized
        }

        // Seed Users
        User superAdmin = new User();
        superAdmin.setEmail("admin@hospitality.com");
        superAdmin.setPassword(passwordEncoder.encode("admin123"));
        superAdmin.setFullName("Platform Administrator");
        superAdmin.setPhone("+91-9876543210");
        superAdmin.setRole(Role.SUPER_ADMIN);
        superAdmin.setActive(true);
        userRepository.save(superAdmin);

        User hotelAdmin = new User();
        hotelAdmin.setEmail("hoteladmin@tajlake.com");
        hotelAdmin.setPassword(passwordEncoder.encode("admin123"));
        hotelAdmin.setFullName("Rajesh Sharma");
        hotelAdmin.setPhone("+91-9812345678");
        hotelAdmin.setRole(Role.HOTEL_ADMIN);
        hotelAdmin.setActive(true);
        userRepository.save(hotelAdmin);

        User customer = new User();
        customer.setEmail("john@example.com");
        customer.setPassword(passwordEncoder.encode("user123"));
        customer.setFullName("Aarav Patel");
        customer.setPhone("+91-9988776655");
        customer.setRole(Role.CUSTOMER);
        customer.setActive(true);
        userRepository.save(customer);

        // Seed Amenities
        Amenity wifi = amenityRepository.save(new Amenity("High-Speed Wi-Fi", "Connectivity", "wifi"));
        Amenity pool = amenityRepository.save(new Amenity("Private Infinity Pool", "Recreation", "pool"));
        Amenity spa = amenityRepository.save(new Amenity("Ayurvedic Spa & Wellness", "Wellness", "spa"));
        Amenity dining = amenityRepository.save(new Amenity("Royal Fine Dining", "Cuisine", "utensils"));
        Amenity gym = amenityRepository.save(new Amenity("Fitness Center", "Recreation", "dumbbell"));
        Amenity breakfast = amenityRepository.save(new Amenity("Complimentary Gourmet Breakfast", "Cuisine", "coffee"));

        Set<Amenity> luxuryAmenities = new HashSet<>(Arrays.asList(wifi, pool, spa, dining, gym, breakfast));
        Set<Amenity> standardAmenities = new HashSet<>(Arrays.asList(wifi, gym, breakfast));

        // Seed Hotel 1 - Udaipur, Rajasthan
        Hotel hotel1 = new Hotel();
        hotel1.setName("Taj Lake Palace Resort & Spa");
        hotel1.setDescription("Floating serenely amidst Lake Pichola, experience regal heritage luxury, royal butler service, and majestic views of Udaipur City Palace.");
        hotel1.setAddress("Pichola Lake Island");
        hotel1.setCity("Udaipur");
        hotel1.setState("Rajasthan");
        hotel1.setCountry("India");
        hotel1.setZipCode("313001");
        hotel1.setRating(4.9);
        hotel1.setReviewCount(24);
        hotel1.setImageUrl("https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=80");
        hotel1.setCheckInTime("14:00");
        hotel1.setCheckOutTime("12:00");
        hotel1.setPolicies("Heritage dress code preferred. Smart casuals in dining halls.");
        hotel1.setActive(true);
        hotel1.setHotelAdmin(hotelAdmin);
        hotel1.setAmenities(luxuryAmenities);
        hotelRepository.save(hotel1);

        // Rooms for Udaipur Hotel
        Room room101 = new Room();
        room101.setHotel(hotel1);
        room101.setRoomNumber("101");
        room101.setRoomType(RoomType.DELUXE);
        room101.setPricePerNight(new BigDecimal("18500.00"));
        room101.setCapacity(2);
        room101.setDescription("Palace lake-facing deluxe room decorated with traditional Rajasthani artwork and carved marble bath.");
        room101.setImageUrl("https://images.unsplash.com/photo-1618773928121-c32242e63f39?auto=format&fit=crop&w=800&q=80");
        room101.setAvailable(true);
        roomRepository.save(room101);

        Room room102 = new Room();
        room102.setHotel(hotel1);
        room102.setRoomNumber("Royal Suite 201");
        room102.setRoomType(RoomType.PRESIDENTIAL);
        room102.setPricePerNight(new BigDecimal("42000.00"));
        room102.setCapacity(4);
        room102.setDescription("Grand Maharajah Suite with private jharokha balcony, crystal chandeliers, and dedicated royal butler.");
        room102.setImageUrl("https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=800&q=80");
        room102.setAvailable(true);
        roomRepository.save(room102);

        // Seed Hotel 2 - Goa
        Hotel hotel2 = new Hotel();
        hotel2.setName("The Leela Beach Resort Goa");
        hotel2.setDescription("Sprawled between Mobor Beach and the Sal River, offering lagoon views, private beach access, and lush palm foliage sanctuary.");
        hotel2.setAddress("Mobor Beach, Cavelossim");
        hotel2.setCity("Goa");
        hotel2.setState("Goa");
        hotel2.setCountry("India");
        hotel2.setZipCode("403731");
        hotel2.setRating(4.8);
        hotel2.setReviewCount(18);
        hotel2.setImageUrl("https://images.unsplash.com/photo-1540541338287-41700207dee6?auto=format&fit=crop&w=1200&q=80");
        hotel2.setCheckInTime("15:00");
        hotel2.setCheckOutTime("11:00");
        hotel2.setPolicies("Beachwear permitted at pool & beachside bar.");
        hotel2.setActive(true);
        hotel2.setHotelAdmin(hotelAdmin);
        hotel2.setAmenities(luxuryAmenities);
        hotelRepository.save(hotel2);

        Room room201 = new Room();
        room201.setHotel(hotel2);
        room201.setRoomNumber("Lagoon Villa 305");
        room201.setRoomType(RoomType.SUITE);
        room201.setPricePerNight(new BigDecimal("22500.00"));
        room201.setCapacity(3);
        room201.setDescription("Secluded lagoon villa featuring private outdoor plunge pool, daybed terrace, and tropical garden access.");
        room201.setImageUrl("https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&w=800&q=80");
        room201.setAvailable(true);
        roomRepository.save(room201);

        // Seed Hotel 3 - Jaipur, Rajasthan
        Hotel hotel3 = new Hotel();
        hotel3.setName("Rambagh Palace");
        hotel3.setDescription("The Jewel of Jaipur. Former residence of the Maharaja of Jaipur, featuring opulent gardens and historic royal architecture.");
        hotel3.setAddress("Bhawani Singh Road");
        hotel3.setCity("Jaipur");
        hotel3.setState("Rajasthan");
        hotel3.setCountry("India");
        hotel3.setZipCode("302005");
        hotel3.setRating(4.95);
        hotel3.setReviewCount(30);
        hotel3.setImageUrl("https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=1200&q=80");
        hotel3.setCheckInTime("14:00");
        hotel3.setCheckOutTime("12:00");
        hotel3.setPolicies("Valet parking and airport pickup available on request.");
        hotel3.setActive(true);
        hotel3.setHotelAdmin(hotelAdmin);
        hotel3.setAmenities(luxuryAmenities);
        hotelRepository.save(hotel3);

        Room room301 = new Room();
        room301.setHotel(hotel3);
        room301.setRoomNumber("Palace Room 112");
        room301.setRoomType(RoomType.EXECUTIVE);
        room301.setPricePerNight(new BigDecimal("28000.00"));
        room301.setCapacity(2);
        room301.setDescription("Opulent room with ornate high ceilings, antique furniture, and courtyard garden view.");
        room301.setImageUrl("https://images.unsplash.com/photo-1566665797739-1674de7a421a?auto=format&fit=crop&w=800&q=80");
        room301.setAvailable(true);
        roomRepository.save(room301);

        // Seed Hotel 4 - Mumbai, Maharashtra
        Hotel hotel4 = new Hotel();
        hotel4.setName("The St. Regis Mumbai");
        hotel4.setDescription("India's highest hotel in Lower Parel, offering exquisite city skyline views, Butler service, and Michelin-standard dining.");
        hotel4.setAddress("462 Senapati Bapat Marg");
        hotel4.setCity("Mumbai");
        hotel4.setState("Maharashtra");
        hotel4.setCountry("India");
        hotel4.setZipCode("400013");
        hotel4.setRating(4.85);
        hotel4.setReviewCount(16);
        hotel4.setImageUrl("https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?auto=format&fit=crop&w=1200&q=80");
        hotel4.setCheckInTime("15:00");
        hotel4.setCheckOutTime("12:00");
        hotel4.setPolicies("Smart casual dress code in executive lounges.");
        hotel4.setActive(true);
        hotel4.setHotelAdmin(hotelAdmin);
        hotel4.setAmenities(standardAmenities);
        hotelRepository.save(hotel4);

        Room room401 = new Room();
        room401.setHotel(hotel4);
        room401.setRoomNumber("Skyline Suite 4201");
        room401.setRoomType(RoomType.EXECUTIVE);
        room401.setPricePerNight(new BigDecimal("19500.00"));
        room401.setCapacity(2);
        room401.setDescription("Floor-to-ceiling glass suite looking over the Arabian Sea and Mumbai skyline.");
        room401.setImageUrl("https://images.unsplash.com/photo-1618773928121-c32242e63f39?auto=format&fit=crop&w=800&q=80");
        room401.setAvailable(true);
        roomRepository.save(room401);

        // Seed Sample Booking & Payment
        Booking booking = new Booking();
        booking.setBookingCode("BK-IND9988");
        booking.setUser(customer);
        booking.setRoom(room101);
        booking.setCheckInDate(LocalDate.now().plusDays(3));
        booking.setCheckOutDate(LocalDate.now().plusDays(6));
        booking.setGuestsCount(2);
        booking.setTotalPrice(new BigDecimal("55500.00"));
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setGuestName("Aarav Patel");
        booking.setGuestEmail("john@example.com");
        booking.setGuestPhone("+91-9988776655");
        booking.setSpecialRequests("Lake view room preferred");
        bookingRepository.save(booking);

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setTransactionId("TXN-UPI9988");
        payment.setAmount(new BigDecimal("55500.00"));
        payment.setPaymentMethod("UPI / CREDIT_CARD");
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        // Seed Sample Reviews
        Review review1 = new Review();
        review1.setHotel(hotel1);
        review1.setUser(customer);
        review1.setRating(5);
        review1.setComment("Floating over Lake Pichola feels like a dream! World-class hospitality by royal butler staff.");
        reviewRepository.save(review1);
    }
}
