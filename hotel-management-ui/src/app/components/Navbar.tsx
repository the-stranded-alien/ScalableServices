'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import userService from '../services/userService';

export default function Navbar() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const router = useRouter();

  useEffect(() => {
    // Check authentication status on component mount and when localStorage changes
    const checkAuth = () => {
      setIsLoggedIn(userService.isAuthenticated());
    };

    checkAuth();

    // Listen for storage events (for when user logs in/out in another tab)
    window.addEventListener('storage', checkAuth);
    
    return () => {
      window.removeEventListener('storage', checkAuth);
    };
  }, []);

  const handleLogout = () => {
    userService.logout();
    setIsLoggedIn(false);
    router.push('/auth/login');
  };

  return (
    <nav className="bg-indigo-600">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <Link href="/" className="text-white font-bold text-xl">
                Hotel Management
              </Link>
            </div>
            <div className="hidden md:block">
              <div className="ml-10 flex items-baseline space-x-4">
                <Link href="/" className="text-white hover:bg-indigo-500 px-3 py-2 rounded-md text-sm font-medium">
                  Home
                </Link>
                <Link href="/hotels" className="text-white hover:bg-indigo-500 px-3 py-2 rounded-md text-sm font-medium">
                  Hotels
                </Link>
                {isLoggedIn && (
                  <>
                    <Link href="/bookings" className="text-white hover:bg-indigo-500 px-3 py-2 rounded-md text-sm font-medium">
                      My Bookings
                    </Link>
                    <Link href="/audit" className="text-white hover:bg-indigo-500 px-3 py-2 rounded-md text-sm font-medium">
                      Audit Log
                    </Link>
                  </>
                )}
              </div>
            </div>
          </div>
          <div className="hidden md:block">
            <div className="ml-4 flex items-center md:ml-6">
              {isLoggedIn ? (
                <button
                  onClick={handleLogout}
                  className="text-white hover:bg-indigo-500 px-3 py-2 rounded-md text-sm font-medium"
                >
                  Logout
                </button>
              ) : (
                <div className="flex space-x-4">
                  <Link href="/auth/login" className="text-white hover:bg-indigo-500 px-3 py-2 rounded-md text-sm font-medium">
                    Login
                  </Link>
                  <Link href="/auth/register" className="bg-white text-indigo-600 hover:bg-gray-100 px-3 py-2 rounded-md text-sm font-medium">
                    Register
                  </Link>
                </div>
              )}
            </div>
          </div>
          <div className="-mr-2 flex md:hidden">
            <button
              onClick={() => setIsMenuOpen(!isMenuOpen)}
              className="bg-indigo-600 inline-flex items-center justify-center p-2 rounded-md text-white hover:bg-indigo-500 focus:outline-none"
            >
              <span className="sr-only">Open main menu</span>
              {/* Icon for menu toggle */}
              <svg
                className={`${isMenuOpen ? 'hidden' : 'block'} h-6 w-6`}
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                aria-hidden="true"
              >
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16" />
              </svg>
              <svg
                className={`${isMenuOpen ? 'block' : 'hidden'} h-6 w-6`}
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                aria-hidden="true"
              >
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>
      </div>

      {/* Mobile menu */}
      <div className={`${isMenuOpen ? 'block' : 'hidden'} md:hidden`}>
        <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3">
          <Link href="/" className="text-white hover:bg-indigo-500 block px-3 py-2 rounded-md text-base font-medium">
            Home
          </Link>
          <Link href="/hotels" className="text-white hover:bg-indigo-500 block px-3 py-2 rounded-md text-base font-medium">
            Hotels
          </Link>
          {isLoggedIn && (
            <>
              <Link href="/bookings" className="text-white hover:bg-indigo-500 block px-3 py-2 rounded-md text-base font-medium">
                My Bookings
              </Link>
              <Link href="/audit" className="text-white hover:bg-indigo-500 block px-3 py-2 rounded-md text-base font-medium">
                Audit Log
              </Link>
            </>
          )}
        </div>
        <div className="pt-4 pb-3 border-t border-indigo-700">
          <div className="px-2 space-y-1">
            {isLoggedIn ? (
              <button
                onClick={handleLogout}
                className="text-white hover:bg-indigo-500 block px-3 py-2 rounded-md text-base font-medium w-full text-left"
              >
                Logout
              </button>
            ) : (
              <>
                <Link href="/auth/login" className="text-white hover:bg-indigo-500 block px-3 py-2 rounded-md text-base font-medium">
                  Login
                </Link>
                <Link href="/auth/register" className="text-white hover:bg-indigo-500 block px-3 py-2 rounded-md text-base font-medium">
                  Register
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}