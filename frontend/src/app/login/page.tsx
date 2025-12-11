"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";

export default function LoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const { login, isAuthenticated } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (isAuthenticated) {
      router.push("/boats");
    }
  }, [isAuthenticated, router]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    const result = await login(username, password);
    if (result.success) {
      router.push("/boats");
    } else {
      setError(result.error || "Invalid username or password");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-blue-50 px-4">
      <div className="max-w-md w-full card shadow-xl">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold mb-2">Boat Application</h1>
          <h2 className="text-xl text-gray-600">Login</h2>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label htmlFor="username" className="block text-sm font-medium mb-2">
              Username
            </label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              autoFocus
              className="input"
            />
          </div>

          <div>
            <label htmlFor="password" className="block text-sm font-medium mb-2">
              Password
            </label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="input"
            />
          </div>

          {error && <div className="error-box text-sm">{error}</div>}

          <button type="submit" className="w-full btn btn-primary py-3">
            Login
          </button>
        </form>

        <div className="mt-6 text-center text-sm text-gray-500">
          <p className="mb-2">
            Admin: <span className="font-semibold">admin / admin123</span>
          </p>
        </div>
      </div>
    </div>
  );
}
