// File: app/product/[id]/page.tsx
"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import Image from "next/image";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { API } from "@/lib/api";

interface Product {
    id: string;
    name: string;
    description: string;
    image: string;
    price: number;
    category: string;
    inStock: boolean;
}

export default function ProductPage() {
    const { id } = useParams();
    const [product, setProduct] = useState<Product | null>(null);
    const [recentlyViewed, setRecentlyViewed] = useState<Product[]>([]);
    const router = useRouter();

    useEffect(() => {
        fetch(`${API.product}/api/products/${id}`).then(res => res.json()).then(setProduct);
        fetch(`${API.userActivity}/api/user/recently-viewed`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "X-User-Email": "user@example.com" // dynamically get user email in real app
            },
        }).then(res => res.json()).then(setRecentlyViewed).catch(console.error);
        fetch(`${API.userActivity}/api/user/view`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "X-User-Email": "user@example.com" // dynamically get user email in real app
            },
            body: JSON.stringify({ productId: id })
        }).catch(console.error);
    }, [id]);


    const addToCart = () => {
        router.push("/orders")
    };

    if (!product) return <div className="p-6">Loading...</div>;

    return (
        <div className="min-h-screen bg-gray-50 text-gray-900">
            {/* Product Section */}
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
                <div className="grid md:grid-cols-2 gap-12">
                    {/* Product Image */}
                    <div className="bg-white rounded-2xl shadow-sm overflow-hidden">
                        <div className="relative aspect-square">
                            <Image
                                src={product.image}
                                alt={product.name}
                                fill
                                className="object-cover"
                            />
                        </div>
                        <div className="p-4 flex space-x-2 border-t border-gray-100">
                            {[1, 2, 3, 4].map((i) => (
                                <button key={i} className="w-16 h-16 bg-gray-100 rounded-lg overflow-hidden border-2 border-transparent hover:border-blue-500 transition-colors">
                                    <Image
                                        src={product.image}
                                        alt={`Thumbnail ${i}`}
                                        width={64}
                                        height={64}
                                        className="object-cover"
                                    />
                                </button>
                            ))}
                        </div>
                    </div>

                    {/* Product Details */}
                    <div>
                        <div className="mb-6">
          <span className="inline-block bg-gradient-to-r from-blue-500 to-blue-600 text-white text-xs font-medium px-2.5 py-0.5 rounded-full mb-3">
            {product.category}
          </span>
                            <h1 className="text-3xl md:text-4xl font-bold text-gray-900 mb-3">{product.name}</h1>

                            <div className="flex items-center mb-4">
                                <div className="flex items-center text-yellow-400 mr-3">
                                    {[...Array(5)].map((_, i) => (
                                        <svg key={i} xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                                            <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                                        </svg>
                                    ))}
                                </div>
                                <span className="text-sm text-gray-500">4.8 (128 reviews)</span>
                            </div>

                            <p className="text-lg text-gray-600 mb-6">{product.description}</p>

                            <div className="flex items-center mb-6">
                                <span className="text-3xl font-bold text-gray-900 mr-3">${product.price}</span>
                            </div>

                            <div className="flex items-center mb-8">
                                {product.inStock ? (
                                    <span className="flex items-center text-green-600">
                <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-1" viewBox="0 0 20 20" fill="currentColor">
                  <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                </svg>
                In Stock
              </span>
                                ) : (
                                    <span className="flex items-center text-red-500">
                <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-1" viewBox="0 0 20 20" fill="currentColor">
                  <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
                </svg>
                Out of Stock
              </span>
                                )}
                                <span className="ml-4 text-sm text-gray-500">Free shipping on orders over $50</span>
                            </div>

                            <div className="flex flex-col sm:flex-row gap-4 mb-8">
                                <div className="flex items-center border border-gray-200 rounded-lg overflow-hidden">
                                    <button className="px-3 py-2 bg-gray-100 text-gray-600 hover:bg-gray-200 transition-colors">
                                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                                            <path fillRule="evenodd" d="M5 10a1 1 0 011-1h8a1 1 0 110 2H6a1 1 0 01-1-1z" clipRule="evenodd" />
                                        </svg>
                                    </button>
                                    <span className="px-4 py-2">1</span>
                                    <button className="px-3 py-2 bg-gray-100 text-gray-600 hover:bg-gray-200 transition-colors">
                                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                                            <path fillRule="evenodd" d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z" clipRule="evenodd" />
                                        </svg>
                                    </button>
                                </div>
                                <button
                                    onClick={addToCart}
                                    className="flex-1 bg-gradient-to-r from-blue-600 to-blue-500 text-white px-6 py-3 rounded-lg font-medium hover:from-blue-700 hover:to-blue-600 transition-all shadow-md flex items-center justify-center"
                                >
                                    <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                                    </svg>
                                    Add to Cart
                                </button>
                                <button className="p-3 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors">
                                    <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                                    </svg>
                                </button>
                            </div>

                            <div className="flex flex-wrap gap-2 mb-8">
                                <div className="flex items-center text-sm bg-gray-100 px-3 py-1.5 rounded-full">
                                    <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 mr-1 text-gray-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 8h14M5 8a2 2 0 110-4h14a2 2 0 110 4M5 8v10a2 2 0 002 2h10a2 2 0 002-2V8m-9 4h4" />
                                    </svg>
                                    Free returns
                                </div>
                                <div className="flex items-center text-sm bg-gray-100 px-3 py-1.5 rounded-full">
                                    <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 mr-1 text-gray-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                                    </svg>
                                    Delivery in 2-3 days
                                </div>
                            </div>
                        </div>

                        {/* Specifications */}
                        <div className="mb-12">
                            <h3 className="text-xl font-semibold mb-4 flex items-center">
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2 text-blue-600" viewBox="0 0 20 20" fill="currentColor">
                                    <path fillRule="evenodd" d="M6 6V5a3 3 0 013-3h2a3 3 0 013 3v1h2a2 2 0 012 2v3.57A22.952 22.952 0 0110 13a22.95 22.95 0 01-8-1.43V8a2 2 0 012-2h2zm2-1a1 1 0 011-1h2a1 1 0 011 1v1H8V5zm1 5a1 1 0 011-1h.01a1 1 0 110 2H10a1 1 0 01-1-1z" clipRule="evenodd" />
                                    <path d="M2 13.692V16a2 2 0 002 2h12a2 2 0 002-2v-2.308A24.974 24.974 0 0110 15c-2.796 0-5.487-.46-8-1.308z" />
                                </svg>
                                Specifications
                            </h3>
                        </div>
                    </div>
                </div>


                {/* Recently Viewed */}
                <div className="mt-16 mb-12">
                    <div className="flex items-center justify-between mb-6">
                        <h2 className="text-2xl font-bold text-gray-800 flex items-center">
          <span className="bg-gradient-to-r from-blue-500 to-blue-600 text-white p-2 rounded-full mr-3">
            <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </span>
                            Recently Viewed
                        </h2>
                        <Link href="/history" className="text-blue-600 hover:underline flex items-center">
                            View all
                            <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 ml-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                            </svg>
                        </Link>
                    </div>
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
                        {recentlyViewed.map((item) => (
                            <Link
                                key={item.id}
                                href={`/product/${item.id}`}
                                className="bg-white rounded-xl shadow-sm hover:shadow-md transition-all duration-300 overflow-hidden group"
                            >
                                <div className="relative pt-[100%] bg-gray-100">
                                    <Image
                                        src={item.image}
                                        alt={item.name}
                                        fill
                                        className="object-cover group-hover:scale-105 transition-transform duration-300"
                                    />
                                </div>
                                <div className="p-4">
                                    <h3 className="font-semibold text-gray-800 mb-1 line-clamp-1">{item.name}</h3>
                                    <div className="flex items-center justify-between">
                                        <p className="text-lg font-bold text-blue-600">${item.price}</p>
                                        <button className="text-white bg-blue-600 hover:bg-blue-700 rounded-full p-2 transition-colors">
                                            <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                                            </svg>
                                        </button>
                                    </div>
                                </div>
                            </Link>
                        ))}
                    </div>
                </div>
            </div>

            {/* Consistent Footer */}
            <footer className="bg-gray-800 text-white py-12">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="border-t border-gray-700 pt-8">
                        <div className="flex flex-col md:flex-row justify-between items-center">
                            <div className="mb-4 md:mb-0">
                                <p className="text-gray-400 text-sm">© 2025 AI Shop. All rights reserved.</p>
                            </div>
                            <div className="flex space-x-6">
                                <Link href="/privacy" className="text-gray-400 hover:text-white text-sm transition-colors">Privacy Policy</Link>
                                <Link href="/terms" className="text-gray-400 hover:text-white text-sm transition-colors">Terms of Service</Link>
                                <Link href="/cookies" className="text-gray-400 hover:text-white text-sm transition-colors">Cookie Policy</Link>
                            </div>
                        </div>
                    </div>
                </div>
            </footer>
        </div>
    );
}
