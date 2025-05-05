'use client';

import { useState, useEffect } from 'react';
import { useRouter, useParams } from 'next/navigation';
import Link from 'next/link';
import { format } from 'date-fns';
import hotelService, { Hotel, Room } from '../../services/hotelService';
import bookingService from '../../services/bookingService';
import userService from '../../services/userService';

export default function HotelDetailsPage() {
  const params = useParams();
  const id = params.id as string;
  const [hotel, setHotel] = useState<Hotel | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [selectedRoom, setSelectedRoom] = useState<Room | null>(null);
  const [checkInDate, setCheckInDate] = useState<string>(format(new Date(), 'yyyy-MM-dd'));
  const [checkOutDate, setCheckOutDate] = useState<string>(format(new Date(Date.now() + 86400000), 'yyyy-MM-dd')); // Tomorrow
  const [isBooking, setIsBooking] = useState(false);
  const [bookingSuccess, setBookingSuccess] = useState(false);
  const [bookingError, setBookingError] = useState('');
  const router = useRouter();

  useEffect(() => {
    const fetchHotel = async () => {
      try {
        setLoading(true);
        const data = await hotelService.getHotelById(id);
        setHotel(data);
      } catch (err: unknown) {
        setError(err instanceof Error ? err.message : 'Failed to fetch hotel details');
      } finally {
        setLoading(false);
      }
    };

    fetchHotel();
  }, [id]);

  const handleRoomSelect = (room: Room) => {
    setSelectedRoom(room);
    setBookingError('');
    setBookingSuccess(false);
  };

  const handleBooking = async () => {
    if (!selectedRoom) {
      setBookingError('Please select a room');
      return;
    }

    if (!userService.isAuthenticated()) {
      router.push('/auth/login');
      return;
    }

    try {
      setIsBooking(true);
      setBookingError('');

      // Check room availability first
      const availability = await bookingService.checkAvailability(
        hotel?.name || '',
        new Date(checkInDate),
        new Date(checkOutDate)
      );

      if (!availability.available) {
        setBookingError('Room is not available for the selected dates');
        return;
      }

      // Create booking
      await bookingService.createBooking({
        userId: 'current-user', // This would be replaced with actual user ID in a real app
        hotelName: hotel?.name || '',
        roomNumber: selectedRoom.roomNumber,
        checkInDate,
        checkOutDate,
        status: 'CONFIRMED'
      });

      setBookingSuccess(true);
      setSelectedRoom(null);
    } catch (err: unknown) {
      setBookingError(err instanceof Error ? err.message : 'Failed to book room');
    } finally {
      setIsBooking(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-[60vh]">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-indigo-500"></div>
      </div>
    );
  }

  if (error || !hotel) {
    return (
      <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
        <strong className="font-bold">Error!</strong>
        <span className="block sm:inline"> {error || 'Hotel not found'}</span>
        <div className="mt-4">
          <Link href="/hotels" className="text-indigo-600 hover:underline">
            Back to Hotels
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6">
        <Link href="/hotels" className="text-indigo-600 hover:underline flex items-center">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-5 w-5 mr-1"
            viewBox="0 0 20 20"
            fill="currentColor"
          >
            <path
              fillRule="evenodd"
              d="M9.707 16.707a1 1 0 01-1.414 0l-6-6a1 1 0 010-1.414l6-6a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l4.293 4.293a1 1 0 010 1.414z"
              clipRule="evenodd"
            />
          </svg>
          Back to Hotels
        </Link>
      </div>

      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        <div className="p-6">
          <h1 className="text-3xl font-bold mb-2">{hotel.name}</h1>

          <div className="flex items-center mb-4">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-5 w-5 text-gray-500 mr-1"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"
              />
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"
              />
            </svg>
            <span className="text-gray-700">{hotel.address}, {hotel.city}, {hotel.country}</span>
          </div>

          <div className="flex items-center mb-6">
            {Array.from({ length: 5 }).map((_, i) => (
              <svg
                key={i}
                xmlns="http://www.w3.org/2000/svg"
                className={`h-5 w-5 ${i < hotel.rating ? 'text-yellow-400' : 'text-gray-300'}`}
                viewBox="0 0 20 20"
                fill="currentColor"
              >
                <path
                  d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"
                />
              </svg>
            ))}
          </div>

          <p className="text-gray-700 mb-8">{hotel.description}</p>

          <h2 className="text-2xl font-bold mb-4">Available Rooms</h2>

          {hotel.rooms.filter(room => room.available).length === 0 ? (
            <p className="text-gray-500">No rooms available at this time.</p>
          ) : (
            <div>
              <div className="mb-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Check-in Date</label>
                    <input
                      type="date"
                      className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                      value={checkInDate}
                      onChange={(e) => setCheckInDate(e.target.value)}
                      min={format(new Date(), 'yyyy-MM-dd')}
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Check-out Date</label>
                    <input
                      type="date"
                      className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                      value={checkOutDate}
                      onChange={(e) => setCheckOutDate(e.target.value)}
                      min={format(new Date(checkInDate).getTime() + 86400000, 'yyyy-MM-dd')}
                    />
                  </div>
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {hotel.rooms.filter(room => room.available).map(room => (
                  <div
                    key={room.id}
                    className={`border rounded-lg p-4 cursor-pointer transition-colors ${
                      selectedRoom?.id === room.id
                        ? 'border-indigo-500 bg-indigo-50'
                        : 'hover:border-indigo-300'
                    }`}
                    onClick={() => handleRoomSelect(room)}
                  >
                    <h3 className="font-bold mb-2">Room {room.roomNumber}</h3>
                    <p className="text-gray-700 mb-2">Type: {room.type}</p>
                    <p className="text-indigo-600 font-bold">${room.price} / night</p>
                  </div>
                ))}
              </div>

              {selectedRoom && (
                <div className="mt-8 p-4 border rounded-lg bg-gray-50">
                  <h3 className="font-bold mb-4">Booking Summary</h3>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                    <div>
                      <p className="text-gray-600">Room:</p>
                      <p className="font-medium">Room {selectedRoom.roomNumber} ({selectedRoom.type})</p>
                    </div>
                    <div>
                      <p className="text-gray-600">Price:</p>
                      <p className="font-medium">${selectedRoom.price} / night</p>
                    </div>
                    <div>
                      <p className="text-gray-600">Check-in:</p>
                      <p className="font-medium">{checkInDate}</p>
                    </div>
                    <div>
                      <p className="text-gray-600">Check-out:</p>
                      <p className="font-medium">{checkOutDate}</p>
                    </div>
                  </div>

                  {bookingSuccess ? (
                    <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative mb-4">
                      <strong className="font-bold">Success!</strong>
                      <span className="block sm:inline"> Your booking has been confirmed.</span>
                    </div>
                  ) : bookingError ? (
                    <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4">
                      <strong className="font-bold">Error!</strong>
                      <span className="block sm:inline"> {bookingError}</span>
                    </div>
                  ) : null}

                  <button
                    onClick={handleBooking}
                    disabled={isBooking}
                    className={`w-full py-2 px-4 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 ${
                      isBooking ? 'opacity-50 cursor-not-allowed' : ''
                    }`}
                  >
                    {isBooking ? 'Processing...' : 'Book Now'}
                  </button>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
