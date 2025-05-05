import apiService from './api';

// Types
export interface AuditItem {
  id?: string;
  timestamp: string;
  service: string;
  action: string;
  details: string;
  userId?: string;
}

export interface HealthResponse {
  service: string;
  status: string;
  message: string;
}

class NotificationService {
  private readonly BASE_PATH = '/notification';

  // Get all audit events
  async getAllAuditEvents(): Promise<AuditItem[]> {
    return apiService.get<AuditItem[]>(`${this.BASE_PATH}/audit`);
  }

  // Get audit events by service
  async getAuditEventsByService(service: string): Promise<AuditItem[]> {
    return apiService.get<AuditItem[]>(`${this.BASE_PATH}/audit/${service}`);
  }

  // Check notification service health
  async checkServiceHealth(): Promise<HealthResponse> {
    return apiService.get<HealthResponse>(`${this.BASE_PATH}/test`);
  }

  // Check database health
  async checkDatabaseHealth(): Promise<HealthResponse> {
    return apiService.get<HealthResponse>(`${this.BASE_PATH}/test-db`);
  }

  // Check RabbitMQ health
  async checkRabbitMQHealth(): Promise<HealthResponse> {
    return apiService.get<HealthResponse>(`${this.BASE_PATH}/test-ipc`);
  }
}

export default new NotificationService();