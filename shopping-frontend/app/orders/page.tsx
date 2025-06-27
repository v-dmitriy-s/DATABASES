// File: app/orders/page.tsx
"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import Image from "next/image";
import { Order, OrderStatus } from "../types/order";
import { OrderService } from "../services/orderService";

export default function OrdersPage() {
    const [orders, setOrders] = useState<Order[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const router = useRouter();

    useEffect(() => {
        const fetchOrders = async () => {
            try {
                setIsLoading(true);
                setError(null);
                const data = await OrderService.getOrders();
                setOrders(data);
            } catch (err) {
                setError(err instanceof Error ? err.message : "Failed to load orders");
                console.error(err);
            } finally {
                setIsLoading(false);
            }
        };

        fetchOrders();
    }, []);

    const getStatusColor = (status: OrderStatus) => {
        const colors = {
            Delivered: "bg-green-100 text-green-800",
            Shipped: "bg-blue-100 text-blue-800",
            Processing: "bg-yellow-100 text-yellow-800",
            Pending: "bg-gray-100 text-gray-800"
        };
        return colors[status] || colors.Pending;
    };

    if (isLoading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="bg-white p-8 rounded-xl shadow-sm text-center">
                    <svg xmlns="http://www.w3.org/2000/svg" className="h-12 w-12 mx-auto text-red-500 mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                    </svg>
                    <h3 className="text-lg font-medium text-gray-900 mb-2">Error Loading Orders</h3>
                    <p className="text-gray-500 mb-4">{error}</p>
                    <button
                        onClick={() => window.location.reload()}
                        className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-full transition-colors"
                    >
                        Try Again
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 text-gray-900">
            {/* Main Content */}
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <div className="flex items-center mb-6">
                    <button
                        onClick={() => router.back()}
                        className="flex items-center text-blue-600 hover:text-blue-800 transition-colors"
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-1" viewBox="0 0 20 20"
                             fill="currentColor">
                            <path fillRule="evenodd"
                                  d="M9.707 16.707a1 1 0 01-1.414 0l-6-6a1 1 0 010-1.414l6-6a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l4.293 4.293a1 1 0 010 1.414z"
                                  clipRule="evenodd"/>
                        </svg>
                        Back
                    </button>
                </div>

                <h1 className="text-3xl font-bold mb-2 flex items-center">
                    <span className="bg-gradient-to-r from-blue-500 to-blue-600 text-white p-3 rounded-full mr-4">
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24"
                             stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                                  d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z"/>
                        </svg>
                    </span>
                    My Orders
                </h1>

                <p className="text-gray-500 mb-8">View your order history and current order status</p>

                {orders.length === 0 ? (
                    <div className="bg-white rounded-xl shadow-sm p-8 text-center">
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-12 w-12 mx-auto text-gray-400 mb-4"
                             fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5}
                                  d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/>
                        </svg>
                        <h3 className="text-lg font-medium text-gray-700 mb-2">No orders yet</h3>
                        <p className="text-gray-500 mb-4">You haven&apos;t placed any orders yet.</p>
                        <Link href="/"
                              className="inline-block bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-full transition-colors">
                            Start Shopping
                        </Link>
                    </div>
                ) : (
                    <div className="space-y-6">
                        {orders.map((order) => (
                            <div
                                key={order.id}
                                className="bg-white rounded-xl shadow-sm hover:shadow-md transition-all duration-300 overflow-hidden border border-gray-100"
                            >
                                <div className="p-6">
                                    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-4">
                                        <h2 className="text-lg font-semibold text-gray-800">Order #{order.id}</h2>
                                        <div className="flex items-center mt-2 sm:mt-0">
                                            <span className={`px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(order.status as OrderStatus)}`}>
                                                {order.status}
                                            </span>
                                            <span className="text-sm text-gray-500 ml-3">
                                                {new Date(order.createdAt).toLocaleDateString("en-US", {
                                                    year: "numeric",
                                                    month: "short",
                                                    day: "numeric",
                                                    hour: "2-digit",
                                                    minute: "2-digit"
                                                })}
                                            </span>
                                        </div>
                                    </div>

                                    <div className="border-t border-gray-100 pt-4">
                                        {order.items.map((item, index) => (
                                            <div key={index} className="flex items-center py-3">
                                                <div className="w-16 h-16 bg-gray-100 rounded-lg overflow-hidden mr-4 relative">
                                                    <Image
                                                        src={item.image}
                                                        alt={item.name}
                                                        fill
                                                        sizes="64px"
                                                        className="object-cover"
                                                        loading="lazy"
                                                    />
                                                </div>
                                                <div className="flex-1">
                                                    <h3 className="font-medium text-gray-800">{item.name}</h3>
                                                    <p className="text-sm text-gray-500">Qty: {item.quantity}</p>
                                                </div>
                                                <div className="text-right">
                                                    <p className="font-medium text-gray-800">${(item.price * item.quantity).toFixed(2)}</p>
                                                    {item.price > 0 && (
                                                        <p className="text-xs text-gray-500">${item.price.toFixed(2)} each</p>
                                                    )}
                                                </div>
                                            </div>
                                        ))}
                                    </div>

                                    <div className="border-t border-gray-100 pt-4 mt-4 flex justify-between items-center">
                                        <div>
                                            <p className="text-sm text-gray-600">
                                                Payment method: <span className="font-medium">{order.paymentMethod || "Credit Card"}</span>
                                            </p>
                                            {order.trackingNumber && (
                                                <p className="text-sm text-gray-600 mt-1">
                                                    Tracking: <span className="font-medium">{order.trackingNumber}</span>
                                                </p>
                                            )}
                                        </div>
                                        <div className="text-right">
                                            <p className="text-sm text-gray-600">
                                                Subtotal: <span className="font-medium">${(order.total - (order.shipping || 0)).toFixed(2)}</span>
                                            </p>
                                            {order.shipping > 0 && (
                                                <p className="text-sm text-gray-600">
                                                    Shipping: <span className="font-medium">${order.shipping.toFixed(2)}</span>
                                                </p>
                                            )}
                                            <p className="text-lg font-bold text-blue-600 mt-1">
                                                Total: ${order.total.toFixed(2)}
                                            </p>
                                        </div>
                                    </div>
                                </div>

                                <div className="bg-gray-50 px-6 py-3 flex justify-end space-x-4">
                                    <button
                                        className="text-blue-600 hover:text-blue-800 font-medium text-sm px-4 py-2 rounded-lg hover:bg-blue-50 transition-colors">
                                        View Details
                                    </button>
                                    <button
                                        className="bg-blue-600 hover:bg-blue-700 text-white font-medium text-sm px-4 py-2 rounded-lg transition-colors">
                                        Track Order
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {/* Footer */}
            <footer className="bg-gray-800 text-white py-12">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="border-t border-gray-700 pt-8">
                        <div className="flex flex-col md:flex-row justify-between items-center">
                            <div className="mb-4 md:mb-0">
                                <p className="text-gray-400 text-sm">© 2025 AI Shop. All rights reserved.</p>
                            </div>
                            <div className="flex space-x-6">
                                <Link href="/privacy" className="text-gray-400 hover:text-white text-sm transition-colors">
                                    Privacy Policy
                                </Link>
                                <Link href="/terms" className="text-gray-400 hover:text-white text-sm transition-colors">
                                    Terms of Service
                                </Link>
                                <Link href="/cookies" className="text-gray-400 hover:text-white text-sm transition-colors">
                                    Cookie Policy
                                </Link>
                            </div>
                        </div>
                    </div>
                </div>
            </footer>
        </div>
    );
}
