"use client";

import { useState, useEffect } from "react";
import { useParams, useRouter } from "next/navigation";
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

export default function BoatDetailPage() {
  const params = useParams();
  const router = useRouter();
  const { logout, isAuthenticated, isAdmin } = useAuth();
  const [boat, setBoat] = useState<Boat | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showEditForm, setShowEditForm] = useState(false);

  useEffect(() => {
    if (!isAuthenticated) {
      router.push("/login");
      return;
    }
    fetchBoat();
  }, [params.id, isAuthenticated, router]);

  const fetchBoat = async () => {
    try {
      setLoading(true);
      const response = await api.get(`/boats/${params.id}`);
      setBoat(response.data);
      setError("");
    } catch (err: any) {
      setError("Failed to load boat details. Please try again.");
      console.error("Error fetching boat:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = () => {
    setShowEditForm(true);
  };

  const handleDelete = async () => {
    if (window.confirm("Are you sure you want to delete this boat?")) {
      try {
        await api.delete(`/boats/${params.id}`);
        router.push("/boats");
      } catch (err: any) {
        setError("Failed to delete boat. Please try again.");
        console.error("Error deleting boat:", err);
      }
    }
  };

  const handleFormClose = () => {
    setShowEditForm(false);
  };

  const handleFormSuccess = () => {
    handleFormClose();
    fetchBoat();
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

  if (error && !boat) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="max-w-md w-full card">
          <div className="mb-4 error-box">{error}</div>
          <button onClick={() => router.push("/boats")} className="w-full btn btn-secondary">
            Back to List
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-4xl mx-auto px-4 py-8">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold">Boat Details</h1>
          <button onClick={handleLogout} className="btn btn-secondary">
            Logout
          </button>
        </div>

        {error && <div className="mb-6 error-box">{error}</div>}

        {boat && (
          <div className="card shadow-lg">
            <div className="flex justify-between items-start mb-6">
              <h2 className="text-2xl font-bold">{boat.name}</h2>
              <div className="flex gap-2">
                <button onClick={handleEdit} className="btn btn-secondary">
                  Edit
                </button>
                {isAdmin && (
                  <button onClick={handleDelete} className="btn btn-danger">
                    Delete
                  </button>
                )}
              </div>
            </div>

            <div className="mb-6">
              <h3 className="text-lg font-semibold mb-2">Description</h3>
              <p className="text-gray-600">{boat.description}</p>
            </div>

            {(boat.length || boat.year) && (
              <div className="mb-6 grid grid-cols-2 gap-4">
                {boat.length && (
                  <div>
                    <h3 className="text-sm font-semibold text-gray-700 mb-1">Length</h3>
                    <p className="text-gray-600">{boat.length} m</p>
                  </div>
                )}
                {boat.year && (
                  <div>
                    <h3 className="text-sm font-semibold text-gray-700 mb-1">Year</h3>
                    <p className="text-gray-600">{boat.year}</p>
                  </div>
                )}
              </div>
            )}

            <div className="pt-6 border-t border-gray-200">
              <button onClick={() => router.push("/boats")} className="btn btn-secondary px-6">
                Back to List
              </button>
            </div>
          </div>
        )}

        {showEditForm && (
          <BoatForm boat={boat} onClose={handleFormClose} onSuccess={handleFormSuccess} />
        )}
      </div>
    </div>
  );
}
