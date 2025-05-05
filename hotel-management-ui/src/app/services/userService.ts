import apiService from './api';

// Types
export interface User {
  id?: string;
  username: string;
  email: string;
  password?: string;
  role?: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
}

class UserService {
  private readonly BASE_PATH = '/user';

  // Register a new user
  async register(user: User): Promise<User> {
    return apiService.post<User>(`${this.BASE_PATH}/users`, user);
  }

  // Login user
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const response = await apiService.post<LoginResponse>(`${this.BASE_PATH}/users/login`, credentials);
    
    // Store token in localStorage
    if (response.token) {
      localStorage.setItem('token', response.token);
      
      // You could also decode the token and store user info
      // const decodedToken = jwtDecode(response.token);
      // localStorage.setItem('user', JSON.stringify(decodedToken));
    }
    
    return response;
  }

  // Get all users (admin only)
  async getAllUsers(): Promise<User[]> {
    return apiService.get<User[]>(`${this.BASE_PATH}/users`);
  }

  // Logout user
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = '/login';
  }

  // Check if user is authenticated
  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }
}

export default new UserService();