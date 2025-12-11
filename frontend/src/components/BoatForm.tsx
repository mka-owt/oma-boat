"use client";

import { useState, useEffect } from "react";
import api from "@/services/api";

interface Boat {
  id: number;
  name: string;
  description: string;
  length?: number;
  year?: number;
}

interface BoatFormProps {
  boat: Boat | null;
  onClose: () => void;
  onSuccess: () => void;
}

export default function BoatForm({ boat, onClose, onSuccess }: BoatFormProps) {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [length, setLength] = useState<string>("");
  const [year, setYear] = useState<string>("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (boat) {
      setName(boat.name);
      setDescription(boat.description);
      setLength(boat.length?.toString() || "");
      setYear(boat.year?.toString() || "");
    }
  }, [boat]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const boatData = {
        name,
        description,
        length: length ? parseFloat(length) : null,
        year: year ? parseInt(year) : null,
      };
      if (boat) {
        await api.put(`/boats/${boat.id}`, boatData);
      } else {
        await api.post("/boats", boatData);
      }
      onSuccess();
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.message ||
        (err.response?.data?.errors
          ? Object.values(err.response.data.errors).flat().join(", ")
          : "Failed to save boat. Please try again.");
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
      onClick={onClose}
    >
      <div className="card shadow-xl max-w-md w-full" onClick={(e) => e.stopPropagation()}>
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold">{boat ? "Edit Boat" : "Add New Boat"}</h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 text-2xl font-bold"
          >
            ×
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="name" className="block text-sm font-medium mb-2">
              Name *
            </label>
            <input
              type="text"
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
              maxLength={100}
              className="input"
            />
          </div>

          <div>
            <label htmlFor="description" className="block text-sm font-medium mb-2">
              Description *
            </label>
            <textarea
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
              maxLength={500}
              rows={4}
              className="input resize-none"
            />
          </div>

          <div>
            <label htmlFor="length" className="block text-sm font-medium mb-2">
              Length (meters)
            </label>
            <input
              type="number"
              id="length"
              value={length}
              onChange={(e) => setLength(e.target.value)}
              min="0"
              step="0.1"
              className="input"
            />
          </div>

          <div>
            <label htmlFor="year" className="block text-sm font-medium mb-2">
              Year
            </label>
            <input
              type="number"
              id="year"
              value={year}
              onChange={(e) => setYear(e.target.value)}
              min="1900"
              max={new Date().getFullYear()}
              className="input"
            />
          </div>

          {error && <div className="error-box text-sm">{error}</div>}

          <div className="flex gap-3 pt-4">
            <button type="button" onClick={onClose} className="flex-1 btn btn-secondary">
              Cancel
            </button>
            <button
              type="submit"
              disabled={loading}
              className="flex-1 btn btn-primary disabled:bg-blue-400"
            >
              {loading ? "Saving..." : boat ? "Update" : "Create"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
