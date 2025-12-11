"use client";

import React, { createContext, useState, useContext, useEffect, ReactNode } from "react";
import api from "@/services/api";
import { getRoleFromToken, isAdmin } from "@/utils/jwt";

interface AuthContextType {
  token: string | null;
  login: (username: string, password: string) => Promise<{ success: boolean; error?: string }>;
  logout: () => void;
  isAuthenticated: boolean;
  loading: boolean;
  role: string | null;
  isAdmin: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [token, setToken] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (typeof window !== "undefined") {
      const storedToken = localStorage.getItem("token");
      setToken(storedToken);
      if (storedToken) {
        api.defaults.headers.common["Authorization"] = `Bearer ${storedToken}`;
      }
    }
    setLoading(false);
  }, []);

  const role = getRoleFromToken(token);

  const login = async (username: string, password: string) => {
    try {
      const response = await api.post("/auth/login", { username, password });
      const { token, refreshToken } = response.data;
      if (typeof window !== "undefined") {
        localStorage.setItem("token", token);
        localStorage.setItem("refreshToken", refreshToken);
      }
      setToken(token);
      api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
      return { success: true };
    } catch (error: any) {
      return {
        success: false,
        error: error.response?.data?.message || "Login failed",
      };
    }
  };

  const logout = () => {
    if (typeof window !== "undefined") {
      localStorage.removeItem("token");
      localStorage.removeItem("refreshToken");
    }
    setToken(null);
    delete api.defaults.headers.common["Authorization"];
  };

  const value: AuthContextType = {
    token,
    login,
    logout,
    isAuthenticated: !!token,
    loading,
    role,
    isAdmin: isAdmin(role),
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
