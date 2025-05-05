import apiService from './api';

// Types
export interface Room {
  id?: string;
  roomNumber: string;
  type: string;
  price: number;
  available: boolean;
}

export interface Hotel {
  id?: string;
  name: string;
  address: string;
  city: string;
  country: string;
  rating: number;
  description: string;
  rooms: Room[];
}

class HotelService {
  private readonly BASE_PATH = '/hotel';

  // Get all hotels
  async getAllHotels(): Promise<Hotel[]> {
    return apiService.get<Hotel[]>(`${this.BASE_PATH}/hotels`);
  }

  // Get hotel by ID
  async getHotelById(id: string): Promise<Hotel> {
    return apiService.get<Hotel>(`${this.BASE_PATH}/hotels/${id}`);
  }

  // Get hotels by city
  async getHotelsByCity(city: string): Promise<Hotel[]> {
    return apiService.get<Hotel[]>(`${this.BASE_PATH}/hotels/by-city?city=${city}`);
  }

  // Get hotels by country
  async getHotelsByCountry(country: string): Promise<Hotel[]> {
    return apiService.get<Hotel[]>(`${this.BASE_PATH}/hotels/by-country?country=${country}`);
  }

  // Get all rooms in a hotel
  async getRooms(hotelId: string): Promise<Room[]> {
    return apiService.get<Room[]>(`${this.BASE_PATH}/hotels/${hotelId}/rooms`);
  }

  // Get available rooms in a hotel
  async getAvailableRooms(hotelId: string): Promise<Room[]> {
    return apiService.get<Room[]>(`${this.BASE_PATH}/hotels/${hotelId}/rooms/available`);
  }

  // Get count of available rooms in a hotel
  async getAvailableRoomsCount(hotelId: string): Promise<number> {
    return apiService.get<number>(`${this.BASE_PATH}/hotels/${hotelId}/rooms/available/count`);
  }

  // Create a new hotel (admin only)
  async createHotel(hotel: Hotel): Promise<Hotel> {
    return apiService.post<Hotel>(`${this.BASE_PATH}/hotels`, hotel);
  }

  // Update a hotel (admin only)
  async updateHotel(id: string, hotel: Hotel): Promise<Hotel> {
    return apiService.put<Hotel>(`${this.BASE_PATH}/hotels/${id}`, hotel);
  }

  // Delete a hotel (admin only)
  async deleteHotel(id: string): Promise<void> {
    return apiService.delete<void>(`${this.BASE_PATH}/hotels/${id}`);
  }

  // Load dummy hotel data (admin only)
  async loadDummyData(): Promise<string> {
    return apiService.get<string>(`${this.BASE_PATH}/hotels/load`);
  }
}

export default new HotelService();