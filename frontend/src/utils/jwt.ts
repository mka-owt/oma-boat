import { jwtDecode } from "jwt-decode";

interface JwtPayload {
  role?: string;
  sub?: string;
}

/**
 * Extracts the role from a JWT token
 */
export function getRoleFromToken(token: string | null): string | null {
  if (!token) return null;
  try {
    const decoded = jwtDecode<JwtPayload>(token);
    return decoded?.role || null;
  } catch (error) {
    console.error("Error decoding JWT:", error);
    return null;
  }
}

/**
 * Checks if the role is ADMIN
 */
export function isAdmin(role: string | null): boolean {
  return role === "ADMIN";
}
