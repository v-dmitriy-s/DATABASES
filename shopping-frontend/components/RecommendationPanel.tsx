// File: components/RecommendationPanel.tsx
"use client";

import { useEffect, useState } from "react";
import Image from "next/image";
import Link from "next/link";

interface Product {
    id: string;
    name: string;
    image: string;
    price: number;
}

export default function RecommendationPanel() {
    const [alsoBought, setAlsoBought] = useState<Product[]>([]);
    const [basedOnViews, setBasedOnViews] = useState<Product[]>([]);
    const [trending, setTrending] = useState<Product[]>([]);

    useEffect(() => {
        fetch("/api/recommendations/also-bought").then((res) => res.json()).then(setAlsoBought);
        fetch("/api/recommendations/view-history").then((res) => res.json()).then(setBasedOnViews);
        fetch("/api/trending").then((res) => res.json()).then(setTrending);
    }, []);

    return (
        <aside className="bg-white border-t px-6 py-8 text-sm text-gray-800">
            <div className="max-w-7xl mx-auto grid md:grid-cols-3 gap-10">
                <section>
                    <h3 className="text-lg font-semibold mb-3">🛍️ People also bought</h3>
                    <ul className="space-y-3">
                        {alsoBought.map((item) => (
                            <li key={item.id} className="flex items-center gap-4">
                                <Image src={item.image} alt={item.name} width={50} height={50} className="rounded-md" />
                                <Link href={`/product/${item.id}`} className="hover:underline">
                                    <span>{item.name}</span>
                                </Link>
                                <span className="ml-auto font-medium">${item.price}</span>
                            </li>
                        ))}
                    </ul>
                </section>

                <section>
                    <h3 className="text-lg font-semibold mb-3">👀 Based on your views</h3>
                    <ul className="space-y-3">
                        {basedOnViews.map((item) => (
                            <li key={item.id} className="flex items-center gap-4">
                                <Image src={item.image} alt={item.name} width={50} height={50} className="rounded-md" />
                                <Link href={`/product/${item.id}`} className="hover:underline">
                                    <span>{item.name}</span>
                                </Link>
                                <span className="ml-auto font-medium">${item.price}</span>
                            </li>
                        ))}
                    </ul>
                </section>

                <section>
                    <h3 className="text-lg font-semibold mb-3">🔥 Trending now</h3>
                    <ul className="space-y-3">
                        {trending.map((item) => (
                            <li key={item.id} className="flex items-center gap-4">
                                <Image src={item.image} alt={item.name} width={50} height={50} className="rounded-md" />
                                <Link href={`/product/${item.id}`} className="hover:underline">
                                    <span>{item.name}</span>
                                </Link>
                                <span className="ml-auto font-medium">${item.price}</span>
                            </li>
                        ))}
                    </ul>
                </section>
            </div>
        </aside>
    );
}
