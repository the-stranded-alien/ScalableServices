import apiService from './api';
import { format } from 'date-fns';

// Types
export interface Booking {
  id?: number;
  userId: string;
  hotelName: string;
  roomNumber: string;
  checkInDate: string;
  checkOutDate: string;
  status: string;
}

export interface AvailabilityResponse {
  hotelName: string;
  checkInDate: string;
  checkOutDate: string;
  available: boolean;
}

class BookingService {
  private readonly BASE_PATH = '/booking';

  // Create a new booking
  async createBooking(booking: Booking): Promise<Booking> {
    return apiService.post<Booking>(`${this.BASE_PATH}/booking`, booking);
  }

  // Get all bookings
  async getAllBookings(): Promise<Booking[]> {
    return apiService.get<Booking[]>(`${this.BASE_PATH}/booking`);
  }

  // Get booking by ID
  async getBookingById(id: number): Promise<Booking> {
    return apiService.get<Booking>(`${this.BASE_PATH}/booking/${id}`);
  }

  // Delete/cancel a booking
  async cancelBooking(id: number): Promise<void> {
    return apiService.delete<void>(`${this.BASE_PATH}/booking/${id}`);
  }

  // Check room availability
  async checkAvailability(
    hotelName: string,
    checkInDate: Date,
    checkOutDate: Date
  ): Promise<AvailabilityResponse> {
    const formattedCheckIn = format(checkInDate, 'yyyy-MM-dd');
    const formattedCheckOut = format(checkOutDate, 'yyyy-MM-dd');

    return apiService.get<AvailabilityResponse>(
      `${this.BASE_PATH}/booking/availability?hotelName=${hotelName}&checkIn=${formattedCheckIn}&checkOut=${formattedCheckOut}`
    );
  }
}

const bookingService = new BookingService();
export default bookingService;
