// File: app/page.tsx
"use client";

import {useEffect, useState} from "react";
import Image from "next/image";
import Link from "next/link";
// import RecommendationPanel from "@/components/RecommendationPanel";
import {API} from "@/lib/api";

interface Product {
    id: string;
    name: string;
    image: string;
    price: number;
}

export default function HomePage() {
    const [trending, setTrending] = useState<Product[]>([]);
    const [recommended, setRecommended] = useState<Product[]>([]);
    const categories = ["Shoes", "Tech", "Clothing", "Accessories"];

    useEffect(() => {
        fetch(`${API.product}/api/products/trending`).then((res) => res.json()).then(setTrending);
        fetch(`${API.recommendation}/api/recommendations`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "X-User-Email": "user@example.com" // dynamically get user email in real app
            },
        }).then((res) => res.json()).then(setRecommended).catch(console.error);
    }, []);

    return (
        <main className="min-h-screen bg-gray-50 text-gray-900">

            {/* Main Content */}
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">

                {/* Hero Section */}
                <section
                    className="mb-12 rounded-2xl bg-gradient-to-r from-blue-500 to-blue-600 text-white p-8 md:p-12 shadow-lg">
                    <div className="max-w-2xl">
                        <h1 className="text-3xl md:text-4xl font-bold mb-4">Discover Amazing AI Products</h1>
                        <p className="text-lg mb-6 opacity-90">The future of shopping is here. Find tools that will
                            transform your workflow.</p>
                    </div>
                </section>

                {/* Trending Section */}
                <section className="mb-12">
                    <div className="flex items-center justify-between mb-6">
                        <h2 className="text-2xl font-bold text-gray-800 flex items-center">
                      <span className="bg-gradient-to-r from-orange-500 to-pink-500 text-white p-2 rounded-full mr-3">
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                          <path fillRule="evenodd"
                                d="M12.395 2.553a1 1 0 00-1.45-.385c-.345.23-.614.558-.822.88-.214.33-.403.713-.57 1.116-.334.804-.614 1.768-.84 2.734a31.365 31.365 0 00-.613 3.58 2.64 2.64 0 01-.945-1.067c-.328-.68-.398-1.534-.398-2.654A1 1 0 005.05 6.05 6.981 6.981 0 003 11a7 7 0 1011.95-4.95c-.592-.591-.98-.985-1.348-1.467-.363-.476-.724-1.063-1.207-2.03zM12.12 15.12A3 3 0 017 13s.879.5 2.5.5c0-1 .5-4 1.25-4.5.5 1 .786 1.293 1.371 1.879A2.99 2.99 0 0113 13a2.99 2.99 0 01-.879 2.121z"
                                clipRule="evenodd"/>
                        </svg>
                      </span>
                            Trending Now
                        </h2>
                    </div>
                    <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-4">
                        {trending.map((item) => (
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
                                    <div className="absolute top-2 right-2 bg-white rounded-full p-1 shadow-md">
                                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-pink-500"
                                             fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                                                  d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
                                        </svg>
                                    </div>
                                </div>
                                <div className="p-4">
                                    <h3 className="font-semibold text-gray-800 mb-1 line-clamp-1">{item.name}</h3>
                                    <div className="flex items-center justify-between">
                                        <p className="text-lg font-bold text-blue-600">${item.price}</p>
                                        <div className="flex items-center text-yellow-400">
                                            <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4"
                                                 viewBox="0 0 20 20" fill="currentColor">
                                                <path
                                                    d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"/>
                                            </svg>
                                            <span className="text-xs text-gray-500 ml-1">4.8</span>
                                        </div>
                                    </div>
                                </div>
                            </Link>
                        ))}
                    </div>
                </section>

                {/* Recommended Section */}
                <section className="mb-12">
                    <div className="flex items-center justify-between mb-6">
                        <h2 className="text-2xl font-bold text-gray-800 flex items-center">
                      <span className="bg-gradient-to-r from-green-500 to-teal-500 text-white p-2 rounded-full mr-3">
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20"
                             fill="currentColor">
                          <path
                              d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"/>
                        </svg>
                      </span>
                            Recommended for You
                        </h2>
                        <Link href="/recommended" className="text-blue-600 hover:underline flex items-center">
                            View all
                            <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 ml-1" fill="none"
                                 viewBox="0 0 24 24" stroke="currentColor">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7"/>
                            </svg>
                        </Link>
                    </div>
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                        {recommended.map((item) => (
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
                                    <div
                                        className="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/70 to-transparent p-4">
                                        <span
                                            className="text-xs text-white bg-blue-600 px-2 py-1 rounded-full">BESTSELLER</span>
                                    </div>
                                </div>
                                <div className="p-4">
                                    <h3 className="font-semibold text-gray-800 mb-1 line-clamp-1">{item.name}</h3>
                                    <div className="flex items-center justify-between">
                                        <p className="text-lg font-bold text-blue-600">${item.price}</p>
                                        <button
                                            className="text-white bg-blue-600 hover:bg-blue-700 rounded-full p-2 transition-colors">
                                            <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none"
                                                 viewBox="0 0 24 24" stroke="currentColor">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                                                      d="M12 6v6m0 0v6m0-6h6m-6 0H6"/>
                                            </svg>
                                        </button>
                                    </div>
                                </div>
                            </Link>
                        ))}
                    </div>
                </section>

                {/* Categories Section */}
                <section className="mb-12">
                    <h2 className="text-2xl font-bold text-gray-800 mb-6 flex items-center">
                    <span className="bg-gradient-to-r from-purple-500 to-indigo-500 text-white p-2 rounded-full mr-3">
                      <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20"
                           fill="currentColor">
                        <path fillRule="evenodd"
                              d="M17.707 9.293a1 1 0 010 1.414l-7 7a1 1 0 01-1.414 0l-7-7A.997.997 0 012 10V5a3 3 0 013-3h5c.256 0 .512.098.707.293l7 7zM5 6a1 1 0 100-2 1 1 0 000 2z"
                              clipRule="evenodd"/>
                      </svg>
                    </span>
                        Shop by Category
                    </h2>
                    <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6 gap-4">
                        {categories.map((category) => (
                            <Link
                                key={category}
                                href={`/category/${category}`}
                                className="bg-white rounded-xl shadow-sm hover:shadow-md transition-all duration-300 overflow-hidden group text-center p-6"
                            >
                                <div
                                    className="w-16 h-16 mx-auto mb-3 bg-blue-100 rounded-full flex items-center justify-center text-blue-600 group-hover:bg-blue-600 group-hover:text-white transition-colors">
                                    <svg xmlns="http://www.w3.org/2000/svg" className="h-8 w-8" fill="none"
                                         viewBox="0 0 24 24" stroke="currentColor">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                                              d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/>
                                    </svg>
                                </div>
                                <h3 className="font-semibold text-gray-800 group-hover:text-blue-600 transition-colors">{category}</h3>
                                <p className="text-xs text-gray-500 mt-1">Explore {category}</p>
                            </Link>
                        ))}
                    </div>
                </section>

                {/* Newsletter */}
                <section className="bg-gradient-to-r from-blue-500 to-blue-600 rounded-2xl p-8 text-white mb-12">
                    <div className="max-w-2xl mx-auto text-center">
                        <h2 className="text-2xl font-bold mb-2">Stay Updated</h2>
                        <p className="mb-6 opacity-90">Subscribe to our newsletter for the latest products and deals</p>
                        <div className="flex flex-col sm:flex-row gap-2 max-w-md mx-auto">
                            <input
                                type="email"
                                placeholder="Your email address"
                                className="flex-grow px-4 py-3 rounded-full text-gray-900 focus:outline-none focus:ring-2 focus:ring-white"
                            />
                            <button
                                className="bg-white text-blue-600 px-6 py-3 rounded-full font-medium hover:bg-gray-100 transition-colors shadow-md whitespace-nowrap">
                                Subscribe
                            </button>
                        </div>
                    </div>
                </section>
            </div>

            {/* Footer */}
            <footer className="bg-gray-800 text-white py-12">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-8 mb-8">
                        <div>
                            <h3 className="text-lg font-semibold mb-4">Shop</h3>
                            <ul className="space-y-2">
                                <li><Link href="/products" className="text-gray-300 hover:text-white transition-colors">All
                                    Products</Link></li>
                                <li><Link href="/trending"
                                          className="text-gray-300 hover:text-white transition-colors">Trending</Link>
                                </li>
                                <li><Link href="/new" className="text-gray-300 hover:text-white transition-colors">New
                                    Arrivals</Link></li>
                                <li><Link href="/deals"
                                          className="text-gray-300 hover:text-white transition-colors">Deals</Link></li>
                            </ul>
                        </div>
                        <div>
                            <h3 className="text-lg font-semibold mb-4">Company</h3>
                            <ul className="space-y-2">
                                <li><Link href="/about" className="text-gray-300 hover:text-white transition-colors">About
                                    Us</Link></li>
                                <li><Link href="/careers"
                                          className="text-gray-300 hover:text-white transition-colors">Careers</Link>
                                </li>
                                <li><Link href="/blog"
                                          className="text-gray-300 hover:text-white transition-colors">Blog</Link></li>
                                <li><Link href="/press"
                                          className="text-gray-300 hover:text-white transition-colors">Press</Link></li>
                            </ul>
                        </div>
                        <div>
                            <h3 className="text-lg font-semibold mb-4">Support</h3>
                            <ul className="space-y-2">
                                <li><Link href="/contact" className="text-gray-300 hover:text-white transition-colors">Contact
                                    Us</Link></li>
                                <li><Link href="/faq"
                                          className="text-gray-300 hover:text-white transition-colors">FAQs</Link></li>
                                <li><Link href="/shipping"
                                          className="text-gray-300 hover:text-white transition-colors">Shipping</Link>
                                </li>
                                <li><Link href="/returns"
                                          className="text-gray-300 hover:text-white transition-colors">Returns</Link>
                                </li>
                            </ul>
                        </div>
                        <div>
                            <h3 className="text-lg font-semibold mb-4">Connect</h3>
                            <div className="flex space-x-4 mb-4">
                                <Link href="#" className="text-gray-300 hover:text-white transition-colors">
                                    <svg className="h-6 w-6" fill="currentColor" viewBox="0 0 24 24">
                                        <path
                                            d="M22 12c0-5.523-4.477-10-10-10S2 6.477 2 12c0 4.991 3.657 9.128 8.438 9.878v-6.987h-2.54V12h2.54V9.797c0-2.506 1.492-3.89 3.777-3.89 1.094 0 2.238.195 2.238.195v2.46h-1.26c-1.243 0-1.63.771-1.63 1.562V12h2.773l-.443 2.89h-2.33v6.988C18.343 21.128 22 16.991 22 12z"/>
                                    </svg>
                                </Link>
                                <Link href="#" className="text-gray-300 hover:text-white transition-colors">
                                    <svg className="h-6 w-6" fill="currentColor" viewBox="0 0 24 24">
                                        <path
                                            d="M12.315 2c2.43 0 2.784.013 3.808.06 1.064.049 1.791.218 2.427.465a4.902 4.902 0 011.772 1.153 4.902 4.902 0 011.153 1.772c.247.636.416 1.363.465 2.427.048 1.067.06 1.407.06 4.123v.08c0 2.643-.012 2.987-.06 4.043-.049 1.064-.218 1.791-.465 2.427a4.902 4.902 0 01-1.153 1.772 4.902 4.902 0 01-1.772 1.153c-.636.247-1.363.416-2.427.465-1.067.048-1.407.06-4.123.06h-.08c-2.643 0-2.987-.012-4.043-.06-1.064-.049-1.791-.218-2.427-.465a4.902 4.902 0 01-1.772-1.153 4.902 4.902 0 01-1.153-1.772c-.247-.636-.416-1.363-.465-2.427-.047-1.024-.06-1.379-.06-3.808v-.63c0-2.43.013-2.784.06-3.808.049-1.064.218-1.791.465-2.427a4.902 4.902 0 011.153-1.772A4.902 4.902 0 015.45 2.525c.636-.247 1.363-.416 2.427-.465C8.901 2.013 9.256 2 11.685 2h.63zm-.081 1.802h-.468c-2.456 0-2.784.011-3.807.058-.975.045-1.504.207-1.857.344-.467.182-.8.398-1.15.748-.35.35-.566.683-.748 1.15-.137.353-.3.882-.344 1.857-.047 1.023-.058 1.351-.058 3.807v.468c0 2.456.011 2.784.058 3.807.045.975.207 1.504.344 1.857.182.466.399.8.748 1.15.35.35.683.566 1.15.748.353.137.882.3 1.857.344 1.054.048 1.37.058 4.041.058h.08c2.597 0 2.917-.01 3.96-.058.976-.045 1.505-.207 1.858-.344.466-.182.8-.398 1.15-.748.35-.35.566-.683.748-1.15.137-.353.3-.882.344-1.857.048-1.055.058-1.37.058-4.041v-.08c0-2.597-.01-2.917-.058-3.96-.045-.976-.207-1.505-.344-1.858a3.097 3.097 0 00-.748-1.15 3.098 3.098 0 00-1.15-.748c-.353-.137-.882-.3-1.857-.344-1.023-.047-1.351-.058-3.807-.058zM12 6.865a5.135 5.135 0 110 10.27 5.135 5.135 0 010-10.27zm0 1.802a3.333 3.333 0 100 6.666 3.333 3.333 0 000-6.666zm5.338-3.205a1.2 1.2 0 110 2.4 1.2 1.2 0 010-2.4z"/>
                                    </svg>
                                </Link>
                                <Link href="#" className="text-gray-300 hover:text-white transition-colors">
                                    <svg className="h-6 w-6" fill="currentColor" viewBox="0 0 24 24">
                                        <path
                                            d="M8.29 20.251c7.547 0 11.675-6.253 11.675-11.675 0-.178 0-.355-.012-.53A8.348 8.348 0 0022 5.92a8.19 8.19 0 01-2.357.646 4.118 4.118 0 001.804-2.27 8.224 8.224 0 01-2.605.996 4.107 4.107 0 00-6.993 3.743 11.65 11.65 0 01-8.457-4.287 4.106 4.106 0 001.27 5.477A4.072 4.072 0 012.8 9.713v.052a4.105 4.105 0 003.292 4.022 4.095 4.095 0 01-1.853.07 4.108 4.108 0 003.834 2.85A8.233 8.233 0 012 18.407a11.616 11.616 0 006.29 1.84"/>
                                    </svg>
                                </Link>
                            </div>
                            <p className="text-gray-300 mb-2">Download our app</p>
                            <div className="flex space-x-2">
                                <Link href="#" className="block">
                                    <img
                                        src="https://developer.apple.com/assets/elements/badges/download-on-the-app-store.svg"
                                        alt="App Store" className="h-10"/>
                                </Link>
                                <Link href="#" className="block">
                                    <img
                                        src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png"
                                        alt="Google Play" className="h-10"/>
                                </Link>
                            </div>
                        </div>
                    </div>
                    <div className="border-t border-gray-700 pt-8">
                        <div className="flex flex-col md:flex-row justify-between items-center">
                            <div className="mb-4 md:mb-0">
                                <p className="text-gray-400 text-sm">© 2025 AI Shop. All rights reserved.</p>
                            </div>
                            <div className="flex space-x-6">
                                <Link href="/privacy"
                                      className="text-gray-400 hover:text-white text-sm transition-colors">Privacy
                                    Policy</Link>
                                <Link href="/terms"
                                      className="text-gray-400 hover:text-white text-sm transition-colors">Terms of
                                    Service</Link>
                                <Link href="/cookies"
                                      className="text-gray-400 hover:text-white text-sm transition-colors">Cookie
                                    Policy</Link>
                            </div>
                        </div>
                    </div>
                </div>
            </footer>
        </main>
    );
}
