"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";
import api from "@/services/api";
import BoatForm from "@/components/BoatForm";

interface Boat {
  id: number;
  name: string;
  description: string;
  length?: number;
  year?: number;
}

export default function BoatsPage() {
  const [boats, setBoats] = useState<Boat[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [editingBoat, setEditingBoat] = useState<Boat | null>(null);
  const { logout, isAuthenticated, isAdmin } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (!isAuthenticated) {
      router.push("/login");
      return;
    }
    fetchBoats();
  }, [isAuthenticated, router]);

  const fetchBoats = async () => {
    try {
      setLoading(true);
      const response = await api.get("/boats");
      setBoats(response.data);
      setError("");
    } catch (err: any) {
      setError("Failed to load boats. Please try again.");
      console.error("Error fetching boats:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = () => {
    setEditingBoat(null);
    setShowForm(true);
  };

  const handleEdit = (boat: Boat) => {
    setEditingBoat(boat);
    setShowForm(true);
  };

  const handleDelete = async (id: number) => {
    if (window.confirm("Are you sure you want to delete this boat?")) {
      try {
        await api.delete(`/boats/${id}`);
        fetchBoats();
      } catch (err: any) {
        setError("Failed to delete boat. Please try again.");
        console.error("Error deleting boat:", err);
      }
    }
  };

  const handleFormClose = () => {
    setShowForm(false);
    setEditingBoat(null);
  };

  const handleFormSuccess = () => {
    handleFormClose();
    fetchBoats();
  };

  const handleLogout = () => {
    logout();
    router.push("/login");
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 py-8">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold">Boat Management</h1>
          <button onClick={handleLogout} className="btn btn-secondary">
            Logout
          </button>
        </div>

        {error && <div className="mb-6 error-box">{error}</div>}

        <div className="mb-6">
          <button onClick={handleCreate} className="btn btn-primary px-6 py-3">
            Add New Boat
          </button>
        </div>

        {showForm && (
          <BoatForm boat={editingBoat} onClose={handleFormClose} onSuccess={handleFormSuccess} />
        )}

        {boats.length === 0 ? (
          <div className="card text-center">
            <p className="text-gray-500">No boats found. Click "Add New Boat" to create one.</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {boats.map((boat) => (
              <div key={boat.id} className="card hover:shadow-xl transition">
                <h3
                  onClick={() => router.push(`/boats/${boat.id}`)}
                  className="text-xl font-semibold mb-3 cursor-pointer hover:text-blue-600"
                >
                  {boat.name}
                </h3>
                <p className="text-gray-600 mb-4 line-clamp-3">{boat.description}</p>
                <div className="flex gap-2">
                  <button
                    onClick={() => handleEdit(boat)}
                    className="flex-1 btn btn-secondary text-sm"
                  >
                    Edit
                  </button>
                  {isAdmin && (
                    <button
                      onClick={() => handleDelete(boat.id)}
                      className="flex-1 btn btn-danger text-sm"
                    >
                      Delete
                    </button>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
