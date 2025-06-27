"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { useRouter } from "next/navigation";
import Image from "next/image";
import Link from "next/link";
import { API } from "@/lib/api";

interface Product {
    id: string;
    name: string;
    image: string;
    price: number;
}

interface Inventory {
    productId: string;
    stock: number;
}

export default function CategoryPage() {
    const { category } = useParams();
    const [products, setProducts] = useState<Product[]>([]);
    const [inventoryMap, setInventoryMap] = useState<Record<string, number>>({});
    const router = useRouter();

    useEffect(() => {
        fetch(`${API.product}/api/products/category/${category}`)
            .then(res => res.json())
            .then(setProducts);

        fetch(`${API.inventory}/api/inventory/stocks`)
            .then(res => res.json())
            .then((data: Inventory[]) => {
                const map: Record<string, number> = {};
                data.forEach(item => {
                    map[item.productId] = item.stock;
                });
                setInventoryMap(map);
            });
    }, [category]);

    return (
        <main className="min-h-screen bg-gray-50 text-gray-900">

            {/* Category Content */}
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

                {/* Category Header */}
                <div className="flex flex-col md:flex-row md:items-center md:justify-between mb-8">
                    <div className="flex items-center mb-4 md:mb-0">
                        <h1 className="text-3xl font-bold capitalize flex items-center">
          <span className="bg-gradient-to-r from-blue-500 to-blue-600 text-white p-3 rounded-full mr-4 shadow-md">
            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 8h14M5 8a2 2 0 110-4h14a2 2 0 110 4M5 8v10a2 2 0 002 2h10a2 2 0 002-2V8m-9 4h4" />
            </svg>
          </span>
                            {category}
                        </h1>
                        <span className="ml-4 bg-blue-100 text-blue-800 text-sm font-medium px-3 py-1 rounded-full shadow-sm">
          {products.length} {products.length === 1 ? 'product' : 'products'}
        </span>
                    </div>

                    {/* Sort/Filters */}
                    <div className="flex flex-col sm:flex-row gap-3">
                        <div className="relative">
                            <select className="appearance-none bg-white border border-gray-200 rounded-lg pl-4 pr-8 py-2 text-sm hover:bg-gray-50 transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 shadow-sm">
                                <option>Sort by: Featured</option>
                                <option>Price: Low to High</option>
                                <option>Price: High to Low</option>
                                <option>Newest Arrivals</option>
                                <option>Best Rated</option>
                            </select>
                            <div className="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-700">
                                <svg className="h-4 w-4" fill="currentColor" viewBox="0 0 20 20">
                                    <path fillRule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clipRule="evenodd" />
                                </svg>
                            </div>
                        </div>
                        <button className="flex items-center justify-center text-sm bg-white px-4 py-2 rounded-lg border border-gray-200 hover:bg-gray-50 transition-colors shadow-sm">
                            <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 mr-2 text-gray-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
                            </svg>
                            Filters
                        </button>
                    </div>
                </div>

                {/* Products Grid */}
                {products.length === 0 ? (
                    <div className="bg-white rounded-xl shadow-sm p-12 text-center max-w-2xl mx-auto">
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-16 w-16 mx-auto text-gray-400 mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                        </svg>
                        <h3 className="text-xl font-medium text-gray-700 mb-2">No products found</h3>
                        <p className="text-gray-500 mb-6">We couldn&apos;t find any products in this category.</p>
                        <Link href="/" className="inline-block bg-gradient-to-r from-blue-600 to-blue-500 hover:from-blue-700 hover:to-blue-600 text-white px-6 py-2 rounded-full transition-all shadow-md">
                            Browse All Products
                        </Link>
                    </div>
                ) : (
                    <>
                        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                            {products.map(product => (
                                <div key={product.id} className="group relative">
                                    <Link href={`/product/${product.id}`} className="block">
                                        <div className="bg-white rounded-xl shadow-sm hover:shadow-md transition-all duration-300 overflow-hidden h-full flex flex-col">
                                            <div className="relative pt-[100%] bg-gray-50">
                                                <Image
                                                    src={product.image}
                                                    alt={product.name}
                                                    fill
                                                    className="object-cover group-hover:scale-105 transition-transform duration-300"
                                                />

                                            </div>
                                            <div className="p-4 flex-1 flex flex-col">
                                                <h2 className="font-semibold text-gray-800 mb-1 line-clamp-2">{product.name}</h2>
                                                <div className="mt-auto">
                                                    <div className="flex items-center justify-between mb-2">
                                                        <div>
                                                            <p className="text-lg font-bold text-blue-600">${product.price.toFixed(2)}</p>
                                                        </div>
                                                        <div className="flex items-center">
                                                            <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 text-yellow-400" viewBox="0 0 20 20" fill="currentColor">
                                                                <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                                                            </svg>
                                                            <span className="text-xs text-gray-500 ml-1">4.8</span>
                                                        </div>
                                                    </div>
                                                    <div className="flex justify-between items-center pt-2 border-t border-gray-100">
                                                        {inventoryMap[product.id] > 0 ? (
                                                            <span className="text-xs text-green-600 flex items-center">
                            <span className="w-2 h-2 bg-green-500 rounded-full mr-2"></span>
                            In Stock
                          </span>
                                                        ) : (
                                                            <span className="text-xs text-red-500 flex items-center">
                            <span className="w-2 h-2 bg-red-500 rounded-full mr-2"></span>
                            Out of Stock
                          </span>
                                                        )}
                                                        <button
                                                            className="p-1 text-gray-400 hover:text-blue-600 transition-colors"
                                                            onClick={(e) => {
                                                                e.preventDefault();
                                                                // Add to wishlist functionality
                                                            }}
                                                        >
                                                            <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                                                            </svg>
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </Link>
                                    <button className="absolute bottom-20 left-1/2 transform -translate-x-1/2 opacity-0 group-hover:opacity-100 transition-opacity duration-200 bg-white text-blue-600 px-4 py-2 rounded-full font-medium text-sm hover:bg-gray-50 shadow-md border border-gray-200">
                                        Quick View
                                    </button>
                                </div>
                            ))}
                        </div>

                        {/* Pagination */}
                        {products.length > 0 && (
                            <div className="mt-12 flex justify-center">
                                <nav className="inline-flex rounded-md shadow-sm -space-x-px">
                                    <button className="px-3 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 transition-colors">
                                        <span className="sr-only">Previous</span>
                                        <svg className="h-5 w-5" fill="currentColor" viewBox="0 0 20 20">
                                            <path fillRule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clipRule="evenodd" />
                                        </svg>
                                    </button>
                                    <button className="px-4 py-2 border-t border-b border-gray-300 bg-white text-sm font-medium text-blue-600 hover:bg-blue-50 transition-colors">
                                        1
                                    </button>
                                    <button className="px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 transition-colors">
                                        2
                                    </button>
                                    <button className="px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 transition-colors">
                                        3
                                    </button>
                                    <button className="px-3 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 transition-colors">
                                        <span className="sr-only">Next</span>
                                        <svg className="h-5 w-5" fill="currentColor" viewBox="0 0 20 20">
                                            <path fillRule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clipRule="evenodd" />
                                        </svg>
                                    </button>
                                </nav>
                            </div>
                        )}
                    </>
                )}
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
        </main>
    );
}
