'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import notificationService, { AuditItem } from '../services/notificationService';
import userService from '../services/userService';

export default function AuditPage() {
  const [auditEvents, setAuditEvents] = useState<AuditItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [serviceFilter, setServiceFilter] = useState('');
  const [services, setServices] = useState<string[]>([]);
  const router = useRouter();

  useEffect(() => {
    // Check if user is authenticated
    if (!userService.isAuthenticated()) {
      router.push('/auth/login');
      return;
    }

    const fetchAuditEvents = async () => {
      try {
        setLoading(true);
        const data = await notificationService.getAllAuditEvents();
        setAuditEvents(data);

        // Extract unique services for filtering
        const uniqueServices = Array.from(new Set(data.map(event => event.service)));
        setServices(uniqueServices);
      } catch (err: unknown) {
        setError(err instanceof Error ? err.message : 'Failed to fetch audit events');
      } finally {
        setLoading(false);
      }
    };

    fetchAuditEvents();
  }, [router]);

  const handleServiceFilterChange = async (e: React.ChangeEvent<HTMLSelectElement>) => {
    const service = e.target.value;
    setServiceFilter(service);

    try {
      setLoading(true);

      if (service) {
        const data = await notificationService.getAuditEventsByService(service);
        setAuditEvents(data);
      } else {
        const data = await notificationService.getAllAuditEvents();
        setAuditEvents(data);
      }
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Failed to filter audit events');
    } finally {
      setLoading(false);
    }
  };

  const formatTimestamp = (timestamp: string) => {
    const date = new Date(timestamp);
    return date.toLocaleString();
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-[60vh]">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-indigo-500"></div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">System Audit Log</h1>

      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-6" role="alert">
          <strong className="font-bold">Error!</strong>
          <span className="block sm:inline"> {error}</span>
        </div>
      )}

      {/* Service Filter */}
      <div className="mb-8">
        <label htmlFor="service-filter" className="block text-sm font-medium text-gray-700 mb-1">
          Filter by Service
        </label>
        <select
          id="service-filter"
          className="w-full md:w-64 px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
          value={serviceFilter}
          onChange={handleServiceFilterChange}
        >
          <option value="">All Services</option>
          {services.map(service => (
            <option key={service} value={service}>{service}</option>
          ))}
        </select>
      </div>

      {auditEvents.length === 0 ? (
        <div className="text-center py-8">
          <p className="text-gray-500">No audit events found.</p>
        </div>
      ) : (
        <div className="bg-white shadow overflow-hidden sm:rounded-lg">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Timestamp
                </th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Service
                </th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Action
                </th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Details
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {auditEvents.map((event) => (
                <tr key={event.id}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {formatTimestamp(event.timestamp)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-indigo-100 text-indigo-800">
                      {event.service}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {event.action}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-500">
                    {event.details}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
